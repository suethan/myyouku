package com.tz.myyouku.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.VideoView;

public class YoukuVideoView extends VideoView{

	public YoukuVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.d("tz", "onTouchEvent");
		
		return true;
	}

}
