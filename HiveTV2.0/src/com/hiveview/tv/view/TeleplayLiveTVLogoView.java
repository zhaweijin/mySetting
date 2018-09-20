package com.hiveview.tv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hiveview.tv.R;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.utils.SwitchChannelUtils;

public class TeleplayLiveTVLogoView extends FrameLayout {

	private LayoutInflater inflater;
	private Context mContext;
	private View vContainer;

	private View tv1;
	private View tv2;
	private View tv3;
	private View tv4;
	private View tv5;

	public TeleplayLiveTVLogoView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public TeleplayLiveTVLogoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public TeleplayLiveTVLogoView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	private void init() {
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vContainer = inflater.inflate(R.layout.teleplay_tv_item_layout, null);
		addView(vContainer);

		tv1 = vContainer.findViewById(R.id.lr_tv_4);
		tv1.setOnClickListener(new ViewOnClickListener());
		tv2 = vContainer.findViewById(R.id.lr_tv_5);
		tv2.setOnClickListener(new ViewOnClickListener());

		tv3 = vContainer.findViewById(R.id.lr_tv_6);
		tv3.setOnClickListener(new ViewOnClickListener());
		tv4 = vContainer.findViewById(R.id.lr_tv_7);
		tv4.setOnClickListener(new ViewOnClickListener());
		tv5 = vContainer.findViewById(R.id.lr_tv_8);
		tv5.setOnClickListener(new ViewOnClickListener());
	}

	/**
	 * 创建电视频道显示View
	 * 
	 * @param p
	 * @param text
	 * @param realPosition
	 */
	public void createGroupButton(int p, int realPosition,
			OnKeyListener listener, OnFocusChangeListener focusChangeListener) {
		if (p == 0) {
			setButtonAsButtonGroup(realPosition, tv1, listener,
					focusChangeListener);
		} else if (p == 1) {
			setButtonAsButtonGroup(realPosition, tv2, listener,
					focusChangeListener);
		} else if (p == 2) {
			setButtonAsButtonGroup(realPosition, tv3, listener,
					focusChangeListener);
		} else if (p == 3) {
			setButtonAsButtonGroup(realPosition, tv4, listener,
					focusChangeListener);
		} else if (p == 4) {
			setButtonAsButtonGroup(realPosition, tv5, listener,
					focusChangeListener);
		}

	}

	public ImageView getLogoImageView(int p) {
		ImageView imageView = null;
		if (p == 0) {
			imageView = (ImageView) tv1.findViewById(R.id.iv_logo);
		} else if (p == 1) {
			imageView = (ImageView) tv2.findViewById(R.id.iv_logo);
		} else if (p == 2) {
			imageView = (ImageView) tv3.findViewById(R.id.iv_logo);
		} else if (p == 3) {
			imageView = (ImageView) tv4.findViewById(R.id.iv_logo);
		} else if (p == 4) {
			imageView = (ImageView) tv5.findViewById(R.id.iv_logo);
		}

		return imageView;
	}

	/**
	 * 设置当某个按钮要作为分组按钮显示在屏幕中所拥有的属性
	 * 
	 * @param position
	 * @param btn
	 * @param text
	 */
	private void setButtonAsButtonGroup(int position, View btn,
			OnKeyListener listener, OnFocusChangeListener focusChangeListener) {
		btn.setVisibility(View.VISIBLE);
		btn.setTag(position);
		btn.setOnKeyListener(listener);
		btn.setOnFocusChangeListener(focusChangeListener);
	}

	public void setDefault01Focus() {
		tv1.requestFocus();
	}

	public void setNextUpFocus(int id) {
		tv1.setNextFocusUpId(id);
		tv2.setNextFocusUpId(id);
		tv3.setNextFocusUpId(id);
		tv4.setNextFocusUpId(id);
		tv5.setNextFocusUpId(id);
	}

	class ViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SwitchChannelUtils.switchChannel(mContext, "北京卫视", false,AppScene.getScene());
		}

	}

}
