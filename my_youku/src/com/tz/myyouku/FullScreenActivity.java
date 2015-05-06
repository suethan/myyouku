package com.tz.myyouku;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tz.myyouku.utils.ScreenUtils;

public class FullScreenActivity extends Activity implements OnInfoListener,
		OnBufferingUpdateListener, OnTouchListener {

	private VideoView videoView;
	private ProgressBar pb;
	private TextView download_rate;
	private TextView load_rate;
	private YoukuMediaController mediaController;
	private GestureDetector gestureDetector;
	private int screenWidth;
	private int screenHeight;
	private AudioManager audioManager;
	private int volume;
	private int maxVolume;
	private ImageView iv_volume;
	private TextView tv_volume;
	private View ll_volume;
	private float brightness;
	private View ll_brightness;
	private TextView tv_brightness;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!LibsChecker.checkVitamioLibs(this)) {
			return;
		}

		// 指定布局
		setContentView(R.layout.fullscreen);

		// 要播放的影片对象
		Intent intent = getIntent();
		String path = intent.getStringExtra("videoUrl");
		Log.d("tz", "path:" + path);

		videoView = (VideoView) findViewById(R.id.videoView);
		// 设置播放的视频地址
		Uri uri = Uri.parse(path);
		videoView.setVideoURI(uri);
		mediaController = new YoukuMediaController(this);
		videoView.setMediaController(mediaController);
		videoView.requestFocus();
		videoView.setOnInfoListener(this);
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// 正常速度
				mp.setPlaybackSpeed(1f);
			}
		});
		videoView.setOnBufferingUpdateListener(this);

		pb = (ProgressBar) findViewById(R.id.probar);
		// 加载进度
		download_rate = (TextView) findViewById(R.id.download_rate);
		// 缓冲进度
		load_rate = (TextView) findViewById(R.id.load_rate);

		// 手势监控
		gestureDetector = new GestureDetector(this, new YoukuGestureListener());
		// 屏幕宽高
		int[] size = ScreenUtils.getScreenWH(this);
		screenWidth = size[0];
		screenHeight = size[1];

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// 最大音量
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		ll_volume = findViewById(R.id.ll_volume);
		iv_volume = (ImageView) findViewById(R.id.iv_volume);
		tv_volume = (TextView) findViewById(R.id.tv_volume);

		videoView.setOnTouchListener(this);

		ll_brightness = findViewById(R.id.ll_brightness);
		tv_brightness = (TextView) findViewById(R.id.tv_brightness);

	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch (what) {
		// 缓冲开始
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (videoView.isPlaying()) {
				videoView.pause();
				pb.setVisibility(View.VISIBLE);
				download_rate.setText("");
				load_rate.setText("");
				download_rate.setVisibility(View.VISIBLE);
				load_rate.setVisibility(View.VISIBLE);
			}
			break;
		// 缓冲完成
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			videoView.start();
			pb.setVisibility(View.GONE);
			download_rate.setVisibility(View.GONE);
			load_rate.setVisibility(View.GONE);
			break;
		// 缓冲进行中
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			download_rate.setText("" + extra + "kb/s" + "");
			break;

		default:
			break;
		}

		return false;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		load_rate.setText(percent + "%");
	}

	class YoukuGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if (mediaController.isShowing()) {
				mediaController.hide();
			} else {
				mediaController.show();
			}
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// Log.d("tz", "onScroll");
			float mOldX = e1.getX(), mOldY = e1.getY();
			int y = (int) e2.getY();

			if (mOldX > screenWidth * 0.5f) {
				// 右侧滑动
				onBrightnessSlide((mOldY - y) / screenHeight);
			} else if (mOldX < screenWidth / 0.5f) {
				// 左边滑动
				// /6f降低敏感度
				onVolumeSlide((mOldY - y) / screenHeight);

			}
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}

	/**
	 * 调节音量
	 * 
	 * @param percent
	 */
	public void onVolumeSlide(float percent) {
		if (volume == -1) {
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            ll_volume.setVisibility(View.VISIBLE);
            if (volume < 0){
                volume = 0;
            }
        }

        int index = (int) (percent * maxVolume) + volume;
        if (index >= maxVolume){
            index = maxVolume;
        }
        else if (index <= 0){
            index = 0;
            iv_volume.setImageResource(R.drawable.play_gesture_volume_no);
        }else{
        	iv_volume.setImageResource(R.drawable.play_gesture_volume);
        }
        // 变更声音
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        int v = (int) (((float) index / (float) maxVolume) * 100);
		tv_volume.setText(v + "%");
		
		/*
		// 获取音量
		int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		int index = (int) (percent * maxVolume) + volume;
		
		if (volume == -1) {
			volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (volume < 0){
            	volume = 0;
            }	
        }

		Log.d("tz", "index:" + index + ",percent:" + percent);
		if (index > maxVolume) {
			index = maxVolume;
		} else if (index < 0) {
			index = 0;
			iv_volume.setImageResource(R.drawable.play_gesture_volume_no);
		} else if (index > 0) {
			iv_volume.setImageResource(R.drawable.play_gesture_volume);
		}
		// 设置声音
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
		int v = (int) (((float) index / (float) maxVolume) * 100);
		tv_volume.setText(v + "%");*/
	}

	/**
	 * 调节亮度
	 * 
	 * @param percent
	 */
	public void onBrightnessSlide(float percent) {
		if (brightness < 0) {
			brightness = getWindow().getAttributes().screenBrightness;
			ll_brightness.setVisibility(View.VISIBLE);
			if (brightness <= 0.00f) {
				brightness = 0.50f;
			}
			if (brightness < 0.01f) {
				brightness = 0.01f;
			}
		}
		

		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.screenBrightness = brightness + percent;
		if (params.screenBrightness > 1.0f) {
			params.screenBrightness = 1.0f;
		} else if (params.screenBrightness < 0.01f) {
			params.screenBrightness = 0.01f;
		}
		getWindow().setAttributes(params);
		
		tv_brightness.setText((int)(params.screenBrightness * 100) +"%");

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			return true;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			Log.d("tz", "ACTION_UP");
			endGesture();
			break;
		default:
			break;
		}

		return true;
	}

	/**
	 * 结束手势
	 */
	private void endGesture() {
		//归位
		volume = -1;
		brightness = -1;
		// 隐藏
		ll_volume.setVisibility(View.GONE);

		ll_brightness.setVisibility(View.GONE);
	}

}
