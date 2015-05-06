package com.tz.myyouku.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tz.myyouku.FullScreenActivity;
import com.tz.myyouku.R;
import com.tz.myyouku.beans.VideoInfo;
import com.tz.myyouku.utils.CacheUtils;
import com.tz.myyouku.utils.Constants;
import com.tz.myyouku.utils.HttpUtils;
import com.tz.myyouku.view.BannerPager;
import com.tz.myyouku.view.BannerPager.OnSingleTouchListener;

public class HomePageFragment extends Fragment implements OnPageChangeListener, OnSingleTouchListener {

	// 所有的广告条
	private List<VideoInfo> banners = new ArrayList<VideoInfo>();
	private View layout;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private BannerAdapter adapter;
	private Timer timer = new Timer();
	private int currentItme;
	private BannerPager vp;

	private TextView tv_tilte;
	private TextView tv_play_times;
	
	public static final int MESSAGE_FLIP = 1;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_FLIP:
				vp.setCurrentItem(currentItme);
				break;

			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.fragment_homepage, null);
		vp = (BannerPager) findViewById(R.id.vp_banner);
		adapter = new BannerAdapter();
		vp.setAdapter(adapter);
		vp.setOnPageChangeListener(this);
		vp.addOnSingleTouchListener(this);
		
		tv_tilte = (TextView) findViewById(R.id.tv_title);
		tv_play_times = (TextView) findViewById(R.id.tv_play_times);

		// 初始化图片加载框架（缓存图片）
		initImageLoader();

		// 请求服务器端获取首页的影片信息
		new InitTask().execute();

		return layout;
	}

	class BannerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return banners.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// ViewPager的每一页都是一个图片
			VideoInfo info = banners.get(position);
			ImageView imageView = info.getImageView();
			if (imageView == null) {
				imageView = new ImageView(getActivity());
				imageView.setScaleType(ScaleType.CENTER_CROP);
				info.setImageView(imageView);
			}
			imageLoader.displayImage(info.getImgUrl(), imageView, options);

			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	class InitTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			String json = null;
			CacheUtils cache = new CacheUtils(getActivity(),
					CacheUtils.CACHE_FILE_HOMEPAGE);
			try {
				json = HttpUtils.get(Constants.URL_HOME_PAGE);
				// 缓存
				cache.save(json);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			// 没有网络的情况，直接读取缓存
			if (json == null) {
				json = cache.load();
			}
			//Log.d("tz", json);

			return json;
		}

		@Override
		protected void onPostExecute(String json) {
			// 解析json字符串
			JSONArray ja = null;
			try {
				ja = new JSONArray(json);
				VideoInfo info = null;
				banners.clear();
				for (int i = 0; i < ja.length(); i++) {
					// json对象转为影片对象
					JSONObject jo = (JSONObject) ja.get(i);
					info = new VideoInfo();
					info.setId(jo.getInt("id"));
					info.setTitle(jo.getString("title"));
					info.setDesc(jo.getString("desc"));
					try {
						info.setPlayTimes(jo.getString("playTimes"));
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					try {
						info.setImgUrl(Constants.URL_IMAGES
								+ jo.getString("imgUrl"));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					try {
						info.setVideoUrl(jo.getString("videoUrl"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					banners.add(info);
				}

				// 刷新
				adapter.notifyDataSetChanged();

				// 每隔两秒钟分页一次
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						currentItme = (currentItme + 1) % banners.size();
						// 通知主线程更新，翻页
						handler.sendEmptyMessage(MESSAGE_FLIP);
					}
				}, 2000, 2000);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	private View findViewById(int id) {
		return layout.findViewById(id);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
	}

	private void initImageLoader() {
		File cacheDir = StorageUtils.getOwnCacheDirectory(getActivity(),
				Constants.IMAGE_CACHE_PATH);

		// DisplayImageOptions用于指导每一个Imageloader根据网络图片的状态
		// （空白、下载错误、正在下载）显示对应的图片，是否将缓存加载到磁盘上，下载完后对图片进行怎么样的处理。
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true).build();

		// ImageLoaderConfiguration是针对图片缓存的全局配置，
		// 主要有线程类、缓存大小、磁盘大小、图片下载与解析、日志方面的配置
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity()).defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new LruMemoryCache(12 * 1024 * 1024))
				.memoryCacheSize(12 * 1024 * 1024)
				.discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.top_banner_android)
				.showImageForEmptyUri(R.drawable.top_banner_android)
				.showImageOnFail(R.drawable.top_banner_android)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		// 翻页之后,更新影片描述文字
		VideoInfo info = banners.get(position);
		tv_tilte.setText(info.getTitle());
		if (info.getPlayTimes() != null) {
			tv_play_times.setText("播放：" + info.getPlayTimes());
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onSingleTouch(View v) {
		//去播放
		//获取影片对象
		VideoInfo info = banners.get(currentItme);
		Intent intent = new Intent(getActivity(),FullScreenActivity.class);
		intent.putExtra("videoUrl", Constants.URL_HOST+info.getVideoUrl());
		Log.d("tz", "videoUrl:" + Constants.URL_HOST+info.getVideoUrl());
		startActivity(intent);
	}

}
