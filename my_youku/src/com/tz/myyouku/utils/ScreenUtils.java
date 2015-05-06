package com.tz.myyouku.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtils {

	/**
	 * 获取屏幕的宽高
	 * @param context
	 * @return
	 */
	public static int[] getScreenWH(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		return new int[] { outMetrics.widthPixels, outMetrics.heightPixels };
	}

}
