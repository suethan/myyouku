package com.tz.myyouku.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TitleRadioGroup extends RadioGroup {

	private List<RadioButton> buttons;
	
	public TitleRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * 获取RadioButton的总数
	 * @return
	 */
	public int getRadioButtonCount(){
		if (buttons != null) {
			return buttons.size();
		}
		
		buttons = new ArrayList<RadioButton>();
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view instanceof RadioButton) {
				buttons.add((RadioButton)view);
			}
		}
		
		return buttons.size();
	}
	
	/**
	 * 根据索引获取对应的RadioButton
	 * @param index
	 * @return
	 */
	public RadioButton getRadioButtonAt(int index){
		return buttons.get(index);
	}
	
	

}
