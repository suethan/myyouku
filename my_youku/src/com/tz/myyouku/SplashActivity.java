package com.tz.myyouku;

import java.util.Timer;
import java.util.TimerTask;

import com.tz.myyouku.utils.NetworkChecker;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private TextView tv_version;
	private TextView tv_mnetwork;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//这个Activity显示的布局文件
		setContentView(R.layout.splash);
		
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_mnetwork = (TextView) findViewById(R.id.tv_mnetwork);
		
		//1.获取应用程序的版本号
		PackageManager pm = getPackageManager();
		try {
			//应用程序的相关信息
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			tv_version.setText(info.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		//2.检测网络
		//1.打开应用时检测
		NetworkChecker nc = new NetworkChecker(this);
		if (!nc.isAvailable()) {
			Toast.makeText(this, "网络连接不可用！", Toast.LENGTH_SHORT).show();
		}else{
			//是移动运营商网络
			if(nc.isMobile()){
				//设置可见
				tv_mnetwork.setVisibility(View.VISIBLE);
			}
		}
		//2.监听实时网络状态
		
		
		//跳转到主界面
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this,MainActivity.class);
				startActivity(intent);
				//干掉自己
				finish();
			}
		}, 4000);
		
	}
	
	
	
}
