package com.tz.myyouku;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.tz.myyouku.fragment.HomePageFragment;
import com.tz.myyouku.fragment.MyFragment;
import com.tz.myyouku.utils.ScreenUtils;
import com.tz.myyouku.view.TitleRadioGroup;

public class MainActivity extends FragmentActivity implements OnCheckedChangeListener, OnPageChangeListener {


	private long exitTime;
	private View iv_scroll;
	private TitleRadioGroup rg;
	private ViewPager vp;
	private float fromX;
	private List<Fragment> fragments;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//滑块
		iv_scroll = findViewById(R.id.iv_scroll);
		//计算滑块的宽度
		int screenWidth = ScreenUtils.getScreenWH(this)[0];
		iv_scroll.getLayoutParams().width = screenWidth / 4;
		
		rg = (TitleRadioGroup) findViewById(R.id.rg);
		rg.setOnCheckedChangeListener(this);
		
		vp = (ViewPager) findViewById(R.id.vp);
		MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
		vp.setAdapter(adapter);
		//设置缓存页面4个
		vp.setOffscreenPageLimit(rg.getRadioButtonCount());
		
		vp.setOnPageChangeListener(this);
		
		fragments = new ArrayList<Fragment>();
		for (int i = 0; i < rg.getRadioButtonCount(); i++) {
			Fragment f = new MyFragment();
			//首页
			if (i == 1) {
				f = new HomePageFragment();
			}
			//给每一个Fragment一个索引位置（标示）
			Bundle args = new Bundle();
			args.putInt("index", i);
			f.setArguments(args);
			fragments.add(f);
		}
	}
	
	
	class MyPagerAdapter extends FragmentPagerAdapter{

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return rg.getRadioButtonCount();
		}

	}
	

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		//获取被选中的RadioButton的索引位置，ViewPager对应的位置页面应该被选中
		for (int i = 0; i < rg.getRadioButtonCount(); i++) {
			RadioButton rb = rg.getRadioButtonAt(i);
			if (rb.getId() == checkedId) {
				vp.setCurrentItem(i);
				//选中按钮颜色设置为蓝色
				rb.setTextColor(getResources().getColor(R.color.light_blue));
			}else{
				//其他按钮颜色设置为黑色
				rb.setTextColor(getResources().getColor(R.color.dark_gray));
			}
		}
	}
	
	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		//选中对应的RadioButton
		RadioButton rb = rg.getRadioButtonAt(position);
		
		//滑块滑动到对应的RadioButton位置
		int[] location = new int[2];
		rb.getLocationInWindow(location);
		//滑动的距离
		float toX = location[0] + positionOffset * rb.getWidth();
		
		//平移动画
		TranslateAnimation animation = new TranslateAnimation(fromX, toX, 0, 0);
		animation.setDuration(30);
		animation.setFillAfter(true);
		//执行
		iv_scroll.startAnimation(animation);
		fromX = toX;
	}

	
	//按返回键两次退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				exitTime = System.currentTimeMillis();
				Toast.makeText(this, "再按一次退出优酷！", Toast.LENGTH_SHORT).show();
			}else{
				//两秒钟之内，按了两次直接退出
				finish();
			}
			//阻止onBackPressed方法的执行
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void onPageSelected(int position) {
		RadioButton rb = rg.getRadioButtonAt(position);
		rb.setChecked(true);
	}


	@Override
	public void onPageScrollStateChanged(int state) {}


	
}
