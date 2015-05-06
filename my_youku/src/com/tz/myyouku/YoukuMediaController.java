package com.tz.myyouku;

import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

public class YoukuMediaController extends MediaController implements View.OnTouchListener{

	public YoukuMediaController(Context context) {
		super(context);
	}

	@Override
	protected View makeControllerView() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		setOnTouchListener(this);
		return inflater.inflate(R.layout.mediacontroller, null);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.d("tz", "YoukuMediaController touch");
		return false;
	}

	

}
