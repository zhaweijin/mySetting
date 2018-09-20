package com.hiveview.tv.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public abstract class ChannelBasePageView extends RelativeLayout {

	protected View vContentContainer = null;
	protected ViewPager mViewPager = null;
	protected OnChannelItemFocusChanageListener focusChanageListener = new OnChannelItemFocusChanageListener();
	protected View focusView = null;
	protected ArrayList<View> imageViews = new ArrayList<View>();
	protected ArrayList<ViewGroup> layouts = new ArrayList<ViewGroup>();

	public ChannelBasePageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ChannelBasePageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ChannelBasePageView(Context context) {
		super(context);
		setGravity(Gravity.CENTER);
		setClipChildren(false);
		ViewGroup.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		setLayoutParams(params);
	}

	public void loadBigAnimation(final View v) {
		ObjectAnimator animBigX = ObjectAnimator.ofFloat(v, "scaleX", 1.125f);
		ObjectAnimator animBigY = ObjectAnimator.ofFloat(v, "scaleY", 1.125f);
		AnimatorSet set = new AnimatorSet();
		animBigX.setDuration(200);
		animBigY.setDuration(200);
		set.playTogether(animBigX, animBigY);
		set.start();
	}

	public void loadSmallAnimation(View v) {
		ObjectAnimator animBigX = ObjectAnimator.ofFloat(v, "scaleX", 1f);
		ObjectAnimator animBigY = ObjectAnimator.ofFloat(v, "scaleY", 1f);
		AnimatorSet set = new AnimatorSet();
		animBigX.setDuration(200);
		animBigY.setDuration(200);
		set.playTogether(animBigX, animBigY);
		set.start();
	}

	public class OnChannelItemFocusChanageListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				bringToFront();
				v.bringToFront();
				loadBigAnimation(v);
				OnGetFocus(v);
			} else {
				focusView = v;
				loadSmallAnimation(v);
				OnLostFocus(v);
			}

			vContentContainer.invalidate();
			invalidate();
			ViewGroup parent = ((ViewGroup) getParent());
			parent.invalidate();

		}

	}

	public View getLeftUpBorderImageView() {
		return null;
	}

	public View getLeftDownBorderImageView() {
		return null;
	}

	public View getRightUpBorderImageView() {
		return null;
	}

	public View getRightDownBorderImageView() {
		return null;
	}

	public View getFocusView() {
		return focusView;
	}

	public void setChannelItemClickListener(OnClickListener clickListener) {
		if (null != clickListener) {
			for (int i = 0; i < imageViews.size(); i++) {
				layouts.get(i).setVisibility(View.INVISIBLE);
				layouts.get(i).setFocusable(false);
				layouts.get(i).setOnClickListener(clickListener);
			}
		}
	}

	public abstract List<View> getImageViews();

	public abstract List<ViewGroup> getLayouts();

	public void OnGetFocus(View view) {
	}

	public void OnLostFocus(View view) {
	}

}
