package com.hiveview.tv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.FrameLayout;

import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.utils.AnimationUtil;

public class BaseFramelayout extends FrameLayout implements OnFocusChangeListener {


	public BaseFramelayout(Context context) {
		super(context);
	}

	public BaseFramelayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BaseFramelayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 发送网络请求请求数据
	 * 
	 * @param runnable
	 *            请求数据的耗时操作
	 */
	public final void submitRequest(Runnable runnable) {
		if (null != runnable) {
			HttpTaskManager.getInstance().submit(runnable);
		}
	}


	/**
	 * View获取焦点时的处理
	 */
	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		if (hasFocus) {
			view.bringToFront();
			view.startAnimation(AnimationUtil.getBigAnimation(getContext()));
			view.invalidate();
		} else {
			view.startAnimation(AnimationUtil.getLitterAnimation(getContext()));
		}
	}

}
