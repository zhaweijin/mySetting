package com.hiveview.tv.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hiveview.tv.R;

public class ChannelFirstPageView extends ChannelBasePageView {

	private ImageView imageView0;
	private ImageView imageView1;
	private ImageView imageView2;
	private ImageView imageView3;
	private ImageView imageView4;
	private ImageView imageView5;

	private ViewGroup item_layout0;
	private ViewGroup item_layout01;
	private ViewGroup item_layout1;
	private ViewGroup item_layout2;
	private ViewGroup item_layout3;
	private ViewGroup item_layout4;
	private ViewGroup item_layout41;
	private ViewGroup item_layout5;

	public ChannelFirstPageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ChannelFirstPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ChannelFirstPageView(Context context) {
		super(context);
		init();
	}

	private void init() {
		vContentContainer = inflate(getContext(), R.layout.channel_first_page_layout, null);
		addView(vContentContainer);

		item_layout01 = (ViewGroup) findViewById(R.id.channel_01_layout);
		item_layout0 = (ViewGroup) findViewById(R.id.channel_0_layout);
		item_layout1 = (ViewGroup) findViewById(R.id.channel_1_layout);
		item_layout2 = (ViewGroup) findViewById(R.id.channel_2_layout);
		item_layout3 = (ViewGroup) findViewById(R.id.channel_3_layout);
		item_layout4 = (ViewGroup) findViewById(R.id.channel_4_layout);
		item_layout41 = (ViewGroup) findViewById(R.id.channel_41_layout);
		item_layout5 = (ViewGroup) findViewById(R.id.channel_5_layout);
		
		setItemNextFocus();

		layouts.add(item_layout01);
		layouts.add(item_layout1);
		layouts.add(item_layout2);
		layouts.add(item_layout3);
		layouts.add(item_layout41);
		layouts.add(item_layout5);

		imageView0 = (ImageView) item_layout0.getChildAt(0);
		imageView1 = (ImageView) item_layout1.getChildAt(0);
		imageView2 = (ImageView) item_layout2.getChildAt(0);
		imageView3 = (ImageView) item_layout3.getChildAt(0);
		imageView4 = (ImageView) item_layout4.getChildAt(0);
		imageView5 = (ImageView) item_layout5.getChildAt(0);

		imageViews.add(imageView0);
		imageViews.add(imageView1);
		imageViews.add(imageView2);
		imageViews.add(imageView3);
		imageViews.add(imageView4);
		imageViews.add(imageView5);

		for (int i = 0; i < imageViews.size(); i++) {
			layouts.get(i).setOnFocusChangeListener(focusChanageListener);
		}
	}
	
	/**
	 * @Title: ChannelFirstPageView
	 * @author:张鹏展
	 * @Description: 手动设置焦点的走向
	 */
	private void setItemNextFocus(){
		item_layout01.setNextFocusRightId(R.id.channel_1_layout);
		item_layout01.setNextFocusUpId(item_layout01.getId());
		item_layout01.setNextFocusDownId(item_layout01.getId());
		
		item_layout1.setNextFocusDownId(R.id.channel_2_layout);
		item_layout1.setNextFocusRightId(R.id.channel_4_layout);
		item_layout1.setNextFocusLeftId(item_layout01.getId());
		item_layout1.setNextFocusUpId(item_layout1.getId());
		
		item_layout2.setNextFocusLeftId(item_layout01.getId());
		item_layout2.setNextFocusRightId(R.id.channel_3_layout);
		item_layout2.setNextFocusUpId(item_layout1.getId());
		item_layout2.setNextFocusDownId(item_layout2.getId());
		
		item_layout3.setNextFocusLeftId(item_layout2.getId());
		item_layout3.setNextFocusUpId(item_layout1.getId());
		item_layout3.setNextFocusRightId(R.id.channel_5_layout);
		item_layout3.setNextFocusDownId(item_layout3.getId());
		
		item_layout41.setNextFocusLeftId(item_layout1.getId());
		item_layout41.setNextFocusDownId(R.id.channel_5_layout);
		item_layout41.setNextFocusUpId(item_layout41.getId());
		
		item_layout5.setNextFocusLeftId(item_layout3.getId());
		item_layout5.setNextFocusDownId(item_layout5.getId());
		item_layout5.setNextFocusUpId(item_layout41.getId());
		
	}

	public View getRightUpBorderImageView() {
		return item_layout41;
	}

	public View getRightDownBorderImageView() {
		return item_layout5;
	}

	@Override
	public List<View> getImageViews() {
		return imageViews;
	}

	@Override
	public List<ViewGroup> getLayouts() {
		// TODO Auto-generated method stub
		return layouts;
	}

	@Override
	public void OnGetFocus(View view) {
		if (view.getId() == R.id.channel_01_layout) {
			item_layout0.bringToFront();
			loadBigAnimation(item_layout0);
		} else if (view.getId() == R.id.channel_41_layout) {
			item_layout4.bringToFront();
			loadBigAnimation(item_layout4);
		}

	}

	@Override
	public void OnLostFocus(View view) {
		if (view.getId() == R.id.channel_01_layout) {
			loadSmallAnimation(item_layout0);
		} else if (view.getId() == R.id.channel_41_layout) {
			loadSmallAnimation(item_layout4);
		}
	}

}
