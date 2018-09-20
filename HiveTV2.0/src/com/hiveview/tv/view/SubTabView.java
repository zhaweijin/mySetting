package com.hiveview.tv.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.tv.R;
import com.hiveview.tv.view.pager3d.TabBasePageView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

public class SubTabView extends RelativeLayout {

	private int oldIndex = -1;
	private final int DURATION = 600;

	private View[] views;
	/**
	 * 显示app更新数目的的TextView
	 */
	private TextView tvAppUpdateCount = null;

	/**
	 * 线束game更新数目的TextView
	 */
	private TextView tvGameUpdateCount = null;

	private AnimatorSet subOutAnimatorSet = new AnimatorSet();
	private AnimatorSet subInAnimatorSet = new AnimatorSet();

	public SubTabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SubTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SubTabView(Context context) {
		super(context);
	}

	public void setCurrentItem(int mIndex) {

		for (int i = 0; i < views.length; i++) {
			views[i].setAlpha(0f);
		}

		if (oldIndex == -1) {
			views[mIndex].setAlpha(1f);
			oldIndex = mIndex;
			return;
		}

		subOutAnimatorSet = new AnimatorSet();
		ViewHelper.setPivotX(views[oldIndex], views[oldIndex].getWidth() / 2);
		ViewHelper.setPivotY(views[oldIndex], views[oldIndex].getHeight() / 2);

		Animator rotateOutAnimator = ObjectAnimator.ofFloat(views[oldIndex], "rotationX", 0f, 180f).setDuration(DURATION);
		Animator alphaOutAnimator = ObjectAnimator.ofFloat(views[oldIndex], "alpha", 1f, 0f).setDuration(DURATION);
		subOutAnimatorSet.playTogether(rotateOutAnimator, alphaOutAnimator);

		subInAnimatorSet = new AnimatorSet();
		ViewHelper.setPivotX(views[mIndex], views[mIndex].getWidth() / 2);
		ViewHelper.setPivotY(views[mIndex], views[mIndex].getHeight() / 2);
		Animator rotateInAnimator = ObjectAnimator.ofFloat(views[mIndex], "rotationX", 180f, 360f).setDuration(DURATION);
		Animator alphaInAnimator = ObjectAnimator.ofFloat(views[mIndex], "alpha", 0f, 1f).setDuration(DURATION);
		subInAnimatorSet.playTogether(rotateInAnimator, alphaInAnimator);

		subOutAnimatorSet.start();
		subInAnimatorSet.start();
		oldIndex = mIndex;

	}

	/**
	 * 设置应用游戏的更新数量
	 * 
	 * @param appUpdateCount
	 *            应用的更新数量
	 * @param gameUpdateCount
	 *            游戏的更新数量
	 */
/*	public void setAppAndGameUpdateCount(int appUpdateCount, int gameUpdateCount) {
		int appVisible = appUpdateCount == 0 ? View.GONE : View.VISIBLE;
		int gameVisible = gameUpdateCount == 0 ? View.GONE : View.VISIBLE;

		if (null == tvAppUpdateCount || null == tvGameUpdateCount) {
			tvAppUpdateCount = (TextView) findViewById(R.id.sub_navigation_app_upate_count);
			tvGameUpdateCount = (TextView) findViewById(R.id.sub_navigation_game_upate_count);
		}

		tvAppUpdateCount.setVisibility(appVisible);
		tvGameUpdateCount.setVisibility(gameVisible);

		if (appUpdateCount != 0) {
			tvAppUpdateCount.setText(appUpdateCount + "");
		}

		if (gameUpdateCount != 0) {
			tvGameUpdateCount.setText(gameUpdateCount + "");
		}
	}*/

	public void addBottomMenus(List<TabBasePageView> list, int currentIndex) {
		int size = list.size();
		views = new View[size];
		for (int i = 0; i < size; i++) {
			views[i] = list.get(i).getBottomMenuView();
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			this.addView(views[i], params);

		}

		for (int i = 0; i < views.length; i++) {
			float alpha = currentIndex == i ? 1f : 0f;
			View childView = this.getChildAt(i);
			childView.setAlpha(alpha);
		}

	}

}
