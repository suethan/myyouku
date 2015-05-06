package com.tz.myyouku.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BannerPager extends ViewPager {

	private OnSingleTouchListener singleTouchListener;
	private PointF pressPoint = new PointF();
	
	public BannerPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//通知父容器不拦截该事件
			getParent().requestDisallowInterceptTouchEvent(true);
			pressPoint.x = event.getX();
			pressPoint.y = event.getY();
			break;
		
		case MotionEvent.ACTION_MOVE:
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		
		case MotionEvent.ACTION_UP:
			//按下和松开是否是同一个点
			//两个点的位移
			if (PointF.length(event.getX() - pressPoint.x , event.getY() - pressPoint.y) < 5.0f) {
				//触发点击
				//执行回调方法
				Log.d("tz", "open video");
				if (singleTouchListener != null) {
					singleTouchListener.onSingleTouch(this);
				}
				return true;
			}
			
			
			break;
		default:
			break;
		}
		
		return super.onTouchEvent(event);
	}
	
	
	public interface OnSingleTouchListener{
		public void onSingleTouch(View v);
	}
	
	public void addOnSingleTouchListener(OnSingleTouchListener singleTouchListener) {
		this.singleTouchListener = singleTouchListener;
	}

}
