package com.hiveview.tv.view.pager3d;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class HiveAlphaViewFlipper extends HiveViewFlipper {

	public HiveAlphaViewFlipper(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public HiveAlphaViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HiveAlphaViewFlipper(Context context) {
		super(context);
	}

	@Override
	public void play() {
		if (!isInit) {
			init();
			isInit = true;
		}

		View outView = views.get(outIndex);
		Animator animatorOutAlpha = ObjectAnimator.ofFloat(outView, "alpha", 1f, 0f).setDuration(duration);
		animatorOutAlpha.start();

		View inView = views.get(inIndex);
		Animator animatorInAlpha = ObjectAnimator.ofFloat(inView, "alpha", 0f, 1f).setDuration(duration);
		inFlipperAnimatorListener.setInView(inView);
		animatorInAlpha.addListener(inFlipperAnimatorListener);
		animatorInAlpha.start();

		outIndex = inIndex;
		inIndex = inIndex + 1 == getChildCount() ? 0 : inIndex + 1;

		mHandler.sendEmptyMessageDelayed(0, intervalTime);
	}

	@Override
	public void init() {
		inIndex = 1;
		outIndex = 0;

		for (int i = 0; i < views.size(); i++) {
			views.get(i).setAlpha(1f);
		}
	}

	public void setDisplayedChild(int index) {
		inIndex = index;
	}

}
