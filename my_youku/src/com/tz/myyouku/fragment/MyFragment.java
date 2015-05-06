package com.tz.myyouku.fragment;

import com.tz.myyouku.R;
import com.tz.myyouku.R.drawable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyFragment extends Fragment {

	int[] imgs = {
			R.drawable.img_1,
			R.drawable.img_2,
			R.drawable.img_3,
			R.drawable.img_4
	};

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ImageView iv = new ImageView(getActivity());
		Bundle args = getArguments();
		int index = args.getInt("index");
		iv.setImageResource(imgs[index]);
		
		return iv;
	}
	
}
