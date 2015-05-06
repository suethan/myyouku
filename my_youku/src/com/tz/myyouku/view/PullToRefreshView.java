package com.tz.myyouku.view;

import java.util.Timer;
import java.util.TimerTask;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.tz.myyouku.R;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class PullToRefreshView extends ScrollView {

	private View header;
	private ImageView iv_ptr;
	private TextView tv_ptr;
	//滑动的范围
	private int scrollSpan;
	private float pressY;
	private float distance;
	private Timer timer;
	private int rotation;

	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		//头部
		header = findViewById(R.id.pull_refresh_header);
		iv_ptr = (ImageView) header.findViewById(R.id.iv_ptr);
		tv_ptr = (TextView) findViewById(R.id.tv_ptr);
		
		scrollSpan = header.getHeight() + 5;
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//获取按下的Y坐标
			this.pressY = event.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			Log.d("tz", "distance:"+distance);
			//计算手指滑动的距离
			distance = (event.getY() - pressY) * 0.6f;
			if (distance <= 2) {
				break;
			}
			if (distance > scrollSpan) {
				tv_ptr.setText("松手刷新");
			}
			//旋转
			ViewHelper.setRotation(iv_ptr, distance * 10);
			//平移
			ViewHelper.setTranslationY(this, distance);
			break;
			
		case MotionEvent.ACTION_UP:
			distance = (event.getY() - pressY) * 0.6f;
			if (distance <= 2) {
				break;
			}
			//播放载入的动画
			playLoadAnimation();
			
			//访问网络
			new PullDataTask().execute();
			
			break;
		default:
			break;
		}
		
		return true;
	}
	
	class PullDataTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			//模拟网络访问
			SystemClock.sleep(3000);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			timer.cancel();
			
			rollBackToOrgin();
		}
		
	}
	

	private void playLoadAnimation() {
		//滚动到限定的位置
		rollBackToSpan();
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				//不断的旋转
				((Activity)getContext()).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ViewHelper.setRotation(iv_ptr, rotation+=30);
					}
				});
			}
		}, 10, 50);
		
	}

	/**
	 * 滚动到限定的位置
	 */
	private void rollBackToSpan() {
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1).setDuration(500);
		valueAnimator.start();
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator va) {
				float progress = Float.parseFloat(va.getAnimatedValue().toString());
				ViewHelper.setTranslationY(PullToRefreshView.this, scrollSpan + ((distance - scrollSpan) * (1 - progress)));
				if (progress == 1) {
					tv_ptr.setText("正在载入");
				}
			}
		});
	}
	
	
	/**
	 * header滑动到初始位置
	 */
	private void rollBackToOrgin() {
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1).setDuration(500);
		valueAnimator.start();
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator va) {
				float progress = Float.parseFloat(va.getAnimatedValue().toString());
				ViewHelper.setTranslationY(PullToRefreshView.this, scrollSpan * (1-progress));
				if (progress == 1) {
					tv_ptr.setText("下拉刷新");
				}
			}
		});
	}
	

}
