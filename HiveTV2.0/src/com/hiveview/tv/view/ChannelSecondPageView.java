package com.hiveview.tv.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hiveview.tv.R;

public class ChannelSecondPageView extends ChannelBasePageView {

	private ImageView imageView0;
	private ImageView imageView1;
	private ImageView imageView2;
	private ImageView imageView3;
	private ImageView imageView4;
	private ImageView imageView5;
	private ImageView imageView6;

	private ViewGroup item_layout0;
	private ViewGroup item_layout01;
	private ViewGroup item_layout1;
	private ViewGroup item_layout2;
	private ViewGroup item_layout3;
	private ViewGroup item_layout4;
	private ViewGroup item_layout5;
	private ViewGroup item_layout6;

	public ChannelSecondPageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ChannelSecondPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ChannelSecondPageView(Context context) {
		super(context);
		init();
	}

	private void init() {
		setGravity(Gravity.CENTER);
		setClipChildren(false);
		ViewGroup.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		setLayoutParams(params);

		vContentContainer = inflate(getContext(), R.layout.channel_second_page_layout, null);
		addView(vContentContainer);

		item_layout0 = (ViewGroup) findViewById(R.id.channel_6_layout);
		item_layout01 = (ViewGroup) findViewById(R.id.channel_61_layout);
		item_layout1 = (ViewGroup) findViewById(R.id.channel_7_layout);
		item_layout2 = (ViewGroup) findViewById(R.id.channel_8_layout);
		item_layout3 = (ViewGroup) findViewById(R.id.channel_9_layout);
		item_layout4 = (ViewGroup) findViewById(R.id.channel_10_layout);
		item_layout5 = (ViewGroup) findViewById(R.id.channel_11_layout);
		item_layout6 = (ViewGroup) findViewById(R.id.channel_12_layout);
		
		setItemNextFocus();
		
		layouts.add(item_layout01);
		layouts.add(item_layout1);
		layouts.add(item_layout2);
		layouts.add(item_layout3);
		layouts.add(item_layout4);
		layouts.add(item_layout5);
		layouts.add(item_layout6);

		imageView0 = (ImageView) item_layout0.getChildAt(0);
		imageView1 = (ImageView) item_layout1.getChildAt(0);
		imageView2 = (ImageView) item_layout2.getChildAt(0);
		imageView3 = (ImageView) item_layout3.getChildAt(0);
		imageView4 = (ImageView) item_layout4.getChildAt(0);
		imageView5 = (ImageView) item_layout5.getChildAt(0);
		imageView6 = (ImageView) item_layout6.getChildAt(0);

		imageViews.add(imageView0);
		imageViews.add(imageView1);
		imageViews.add(imageView2);
		imageViews.add(imageView3);
		imageViews.add(imageView4);
		imageViews.add(imageView5);
		imageViews.add(imageView6);

		for (int i = 0; i < imageViews.size(); i++) {
			layouts.get(i).setOnFocusChangeListener(focusChanageListener);
		}
	}
	
	/**
	 * 	item_layout0 = (ViewGroup) findViewById(R.id.channel_6_layout);
		item_layout01 = (ViewGroup) findViewById(R.id.channel_61_layout);
		item_layout1 = (ViewGroup) findViewById(R.id.channel_7_layout);
		item_layout2 = (ViewGroup) findViewById(R.id.channel_8_layout);
		item_layout3 = (ViewGroup) findViewById(R.id.channel_9_layout);
		item_layout4 = (ViewGroup) findViewById(R.id.channel_10_layout);
		item_layout5 = (ViewGroup) findViewById(R.id.channel_11_layout);
		item_layout6 = (ViewGroup) findViewById(R.id.channel_12_layout);
		
	 * layouts.add(item_layout01);
		layouts.add(item_layout1);
		layouts.add(item_layout2);
		layouts.add(item_layout3);
		layouts.add(item_layout4);
		layouts.add(item_layout5);
		layouts.add(item_layout6);
	 * @Title: ChannelSecondPageView
	 * @author:张鹏展
	 * @Description: 控制焦点走向
	 */
	private void setItemNextFocus(){
		item_layout01.setNextFocusRightId(R.id.channel_9_layout);
		item_layout01.setNextFocusDownId(R.id.channel_7_layout);
		item_layout01.setNextFocusUpId(item_layout01.getId());
		item_layout1.setNextFocusRightId(R.id.channel_8_layout);
		item_layout1.setNextFocusUpId(item_layout01.getId());
		item_layout1.setNextFocusDownId(item_layout1.getId());
		item_layout2.setNextFocusLeftId(item_layout1.getId());
		item_layout2.setNextFocusRightId(R.id.channel_10_layout);
		item_layout2.setNextFocusDownId(item_layout2.getId());
		item_layout2.setNextFocusUpId(item_layout01.getId());
		item_layout3.setNextFocusLeftId(item_layout01.getId());
		item_layout3.setNextFocusRightId(R.id.channel_11_layout);
		item_layout3.setNextFocusDownId(R.id.channel_10_layout);
		item_layout3.setNextFocusUpId(item_layout3.getId());
		item_layout4.setNextFocusUpId(item_layout3.getId());
		item_layout4.setNextFocusLeftId(item_layout2.getId());
		item_layout4.setNextFocusRightId(R.id.channel_12_layout);
		item_layout4.setNextFocusDownId(item_layout4.getId());
		item_layout5.setNextFocusLeftId(R.id.channel_9_layout);
		item_layout5.setNextFocusDownId(R.id.channel_12_layout);
		item_layout5.setNextFocusUpId(item_layout5.getId());
		item_layout6.setNextFocusUpId(item_layout5.getId());
		item_layout6.setNextFocusLeftId(R.id.channel_10_layout);
		item_layout6.setNextFocusDownId(item_layout6.getId());
	}

	public View getLeftUpBorderImageView() {
		return item_layout01;
	}

	public View getLeftDownBorderImageView() {
		return item_layout1;
	}

	public View getRightUpBorderImageView() {
		return item_layout5;
	}

	public View getRightDownBorderImageView() {
		return item_layout6;
	}

	@Override
	public List<View> getImageViews() {
		return imageViews;
	}

	@Override
	public List<ViewGroup> getLayouts() {
		return layouts;
	}

	@Override
	public void OnGetFocus(View view) {
		if (view.getId() == R.id.channel_61_layout) {
			item_layout0.bringToFront();
			loadBigAnimation(item_layout0);
		} 

	}

	@Override
	public void OnLostFocus(View view) {
		if (view.getId() == R.id.channel_61_layout) {
			loadSmallAnimation(item_layout0);
		} 
	}
	

}
