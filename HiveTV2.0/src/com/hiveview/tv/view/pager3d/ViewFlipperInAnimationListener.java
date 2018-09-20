package com.hiveview.tv.view.pager3d;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ViewFlipper;

public abstract class ViewFlipperInAnimationListener implements AnimationListener {

	private ViewFlipper viewFlipper = null;
	private int viewFlipperIndexInPage = 0;

	@Override
	public void onAnimationEnd(Animation arg0) {
		onEndInAnimation(viewFlipper, viewFlipperIndexInPage);
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		onRepeatInAnimation(viewFlipper, viewFlipperIndexInPage);
	}

	@Override
	public void onAnimationStart(Animation arg0) {
		onStartInAnimation(viewFlipper, viewFlipperIndexInPage);
	}

	public ViewFlipperInAnimationListener(ViewFlipper viewFlipper, int viewFlipperIndexInPage) {
		this.viewFlipper = viewFlipper;
		this.viewFlipperIndexInPage = viewFlipperIndexInPage;
	}

	public abstract void onStartInAnimation(ViewFlipper viewFlipper, int viewFlipperIndexInPage);
	public abstract void onRepeatInAnimation(ViewFlipper viewFlipper, int viewFlipperIndexInPage);
	public abstract void onEndInAnimation(ViewFlipper viewFlipper, int viewFlipperIndexInPage);

}
