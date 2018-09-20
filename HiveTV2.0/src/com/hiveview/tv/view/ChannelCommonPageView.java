package com.hiveview.tv.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hiveview.tv.R;

public class ChannelCommonPageView extends ChannelBasePageView {

	
	private ViewGroup item_layout0;
	private ViewGroup item_layout1;
	private ViewGroup item_layout2;
	private ViewGroup item_layout3;
	private ViewGroup item_layout4;
	private ViewGroup item_layout5;
	private ViewGroup item_layout6;
	private ViewGroup item_layout7;
	
	
	private ImageView imageView0;
	private ImageView imageView1;
	private ImageView imageView2;
	private ImageView imageView3;
	private ImageView imageView4;
	private ImageView imageView5;
	private ImageView imageView6;
	private ImageView imageView7;

	

	public ChannelCommonPageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ChannelCommonPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ChannelCommonPageView(Context context) {
		super(context);
		init();
	}

	private void init() {
		setGravity(Gravity.CENTER);
		setClipChildren(false);
		ViewGroup.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		setLayoutParams(params);

		vContentContainer = inflate(getContext(), R.layout.channel_common_page_layout, null);
		addView(vContentContainer);

		
		item_layout0= (ViewGroup) findViewById(R.id.channel_13_layout);
		item_layout1= (ViewGroup) findViewById(R.id.channel_14_layout);
		item_layout2= (ViewGroup) findViewById(R.id.channel_15_layout);
		item_layout3= (ViewGroup) findViewById(R.id.channel_16_layout);
		item_layout4= (ViewGroup) findViewById(R.id.channel_17_layout);
		item_layout5= (ViewGroup) findViewById(R.id.channel_18_layout);
		item_layout6= (ViewGroup) findViewById(R.id.channel_19_layout);
		item_layout7= (ViewGroup) findViewById(R.id.channel_20_layout);
		
		
		layouts.add(item_layout0);
		layouts.add(item_layout1);
		layouts.add(item_layout2);
		layouts.add(item_layout3);
		layouts.add(item_layout4);
		layouts.add(item_layout5);
		layouts.add(item_layout6);
		layouts.add(item_layout7);
		
		imageView0 = (ImageView)item_layout0.getChildAt(0);
		imageView1 = (ImageView)item_layout1.getChildAt(0);
		imageView2 = (ImageView)item_layout2.getChildAt(0);
		imageView3 = (ImageView)item_layout3.getChildAt(0);
		imageView4 = (ImageView)item_layout4.getChildAt(0);
		imageView5 = (ImageView)item_layout5.getChildAt(0);
		imageView6 = (ImageView)item_layout6.getChildAt(0);
		imageView7 = (ImageView)item_layout7.getChildAt(0);

		imageViews.add(imageView0);
		imageViews.add(imageView1);
		imageViews.add(imageView2);
		imageViews.add(imageView3);
		imageViews.add(imageView4);
		imageViews.add(imageView5);
		imageViews.add(imageView6);
		imageViews.add(imageView7);

		for (int i = 0; i < imageViews.size(); i++) {
			layouts.get(i).setOnFocusChangeListener(focusChanageListener);
		}
	}

	

	public View getLeftUpBorderImageView() {
		return item_layout0;
	}

	public View getLeftDownBorderImageView() {
		return item_layout1;
	}

	public View getRightUpBorderImageView() {
		return item_layout6;
	}

	public View getRightDownBorderImageView() {
		return item_layout7;
	}

	@Override
	public List<View> getImageViews() {
		return imageViews;
	}

	@Override
	public List<ViewGroup> getLayouts() {
		return layouts;
	}

}
