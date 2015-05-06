package com.tz.myyouku;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tz.myyouku.utils.NetworkChecker;

public class NetworkReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		NetworkChecker nc = new NetworkChecker(context);
		if (!nc.isAvailable()) {
			Toast.makeText(context, "网络连接不可用！", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context, "网络连接可用！", Toast.LENGTH_SHORT).show();
			if (nc.isMobile()) {
				Toast.makeText(context, "使用移动运营商网络！", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(context, "当前使用Wifi，不用流量就是任性！", Toast.LENGTH_SHORT).show();
			}
		}
		
	}

}
