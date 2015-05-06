package com.tz.myyouku.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChecker {

	
	private ConnectivityManager manager;

	public NetworkChecker(Context context) {
		manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	

	/**
	 * 网络是否可用
	 * @return true代表可用  false代表不可用
	 */
	public boolean isAvailable(){
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null || !info.isAvailable()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 是否是试用的移动运行商网络
	 * @return
	 */
	public boolean isMobile(){
		NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (info != null && info.isConnected()) {
			return true;
		}
		return false;
	}
	
	
}
