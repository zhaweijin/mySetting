package com.hiveview.tv.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hiveview.tv.R;
import com.hiveview.tv.view.pager3d.TabBasePageView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;

public class NavigationNewTabView extends RelativeLayout implements AnimatorListener {

	private static final String TAG = "NavigationNewTabView";

	private ImageView cursorImageView;

	private final int DURATION = 600;

	/**
	 * @Fields isCursor 判断top 是否在动画滑动状态
	 */
	private boolean isCursor = false;

	private Animator cursorImageViewAnimator = null;

	private ViewGroup[] views;

	// private TopTabsKeyListener tabsKeyListener = new TopTabsKeyListener();

	public NavigationNewTabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public NavigationNewTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NavigationNewTabView(Context context) {
		super(context);
		init();
	}

	private void init() {

		long time = System.currentTimeMillis();
		View view = inflate(getContext(), R.layout.top_navigation_tab_new_view, null);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.addView(view, params);
		cursorImageView = (ImageView) findViewById(R.id.navigation_cursor_view);
		Log.d(TAG, "loadImage NavigationNewTabView::init " + (System.currentTimeMillis() - time));
	}

	private final int TRANSX = 109;

	public void clearCursorAnimation() {
		if (null != cursorImageViewAnimator) {
			cursorImageViewAnimator.end();
			cursorImageViewAnimator = null;
		}
	}

	public void scrollCursorByDiff(int preIndex, int currentIndex, int pageCount) {

		int value = preIndex - currentIndex;
		Log.v(TAG, "preIndex "+preIndex);
		Log.v(TAG, "currentIndex "+currentIndex);

		if (value == 0)// 初始化，亮线游标在“推荐”位置
			return;

		float tx = cursorImageView.getTranslationX();
		
		Log.v(TAG, "tx "+tx);
		
		if (currentIndex == pageCount - 1 && preIndex == 0) {// 翻动下一页到最后一个位置，往第0位置翻动
			cursorImageViewAnimator = ObjectAnimator.ofFloat(cursorImageView, "translationX", tx, tx + ((pageCount - 1) * TRANSX)).setDuration(0);
			// cursorImageViewAnimator.setInterpolator(new
			// BounceInterpolator());
			cursorImageViewAnimator.addListener(this);
			cursorImageViewAnimator.start();
			return;
		}

		if (currentIndex == 0 && preIndex == pageCount - 1) {// 翻动上一页到第一个位置，往第最后一个位置翻动
			cursorImageViewAnimator = ObjectAnimator.ofFloat(cursorImageView, "translationX", tx, tx - ((pageCount - 1) * TRANSX)).setDuration(0);
			// cursorImageViewAnimator.setInterpolator(new
			// BounceInterpolator());
			cursorImageViewAnimator.addListener(this);
			cursorImageViewAnimator.start();
			return;
		}

		if (value > 0) {// 是往下一个位置翻动
			cursorImageViewAnimator = ObjectAnimator.ofFloat(cursorImageView, "translationX", tx, tx - value*TRANSX).setDuration(DURATION);
			// cursorImageViewAnimator.setInterpolator(new
			// BounceInterpolator());
			cursorImageViewAnimator.addListener(this);
			cursorImageViewAnimator.start();
		}

		if (value < 0) {// 是往上一个位置翻动
			cursorImageViewAnimator = ObjectAnimator.ofFloat(cursorImageView, "translationX", tx, tx - value*TRANSX).setDuration(DURATION);
			// cursorImageViewAnimator.setInterpolator(new
			// BounceInterpolator());
			cursorImageViewAnimator.addListener(this);
			cursorImageViewAnimator.start();
		}

	}
	
	public void scrollCursor(int preIndex, int currentIndex, int pageCount) {

		int value = preIndex - currentIndex;

		if (value == 0)// 初始化，亮线游标在“推荐”位置
			return;

		float tx = cursorImageView.getTranslationX();
		if (currentIndex == pageCount - 1 && preIndex == 0) {// 翻动下一页到最后一个位置，往第0位置翻动
			cursorImageViewAnimator = ObjectAnimator.ofFloat(cursorImageView, "translationX", tx, tx + ((pageCount - 1) * TRANSX)).setDuration(0);
			// cursorImageViewAnimator.setInterpolator(new
			// BounceInterpolator());
			cursorImageViewAnimator.addListener(this);
			cursorImageViewAnimator.start();
			return;
		}

		if (currentIndex == 0 && preIndex == pageCount - 1) {// 翻动上一页到第一个位置，往第最后一个位置翻动
			cursorImageViewAnimator = ObjectAnimator.ofFloat(cursorImageView, "translationX", tx, tx - ((pageCount - 1) * TRANSX)).setDuration(0);
			// cursorImageViewAnimator.setInterpolator(new
			// BounceInterpolator());
			cursorImageViewAnimator.addListener(this);
			cursorImageViewAnimator.start();
			return;
		}

		if (value > 0) {// 是往下一个位置翻动
			cursorImageViewAnimator = ObjectAnimator.ofFloat(cursorImageView, "translationX", tx, tx - TRANSX).setDuration(DURATION);
			// cursorImageViewAnimator.setInterpolator(new
			// BounceInterpolator());
			cursorImageViewAnimator.addListener(this);
			cursorImageViewAnimator.start();
		}

		if (value < 0) {// 是往上一个位置翻动
			cursorImageViewAnimator = ObjectAnimator.ofFloat(cursorImageView, "translationX", tx, tx + TRANSX).setDuration(DURATION);
			// cursorImageViewAnimator.setInterpolator(new
			// BounceInterpolator());
			cursorImageViewAnimator.addListener(this);
			cursorImageViewAnimator.start();
		}

	}

	public void addTopMenus(List<TabBasePageView> list, int currentIndex) {
		int size = list.size();
		views = new ViewGroup[size];
		RelativeLayout.LayoutParams centerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		centerParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		this.addView(list.get(currentIndex).getTopMenuView(), centerParams);
		views[currentIndex] = list.get(currentIndex).getTopMenuView();

		for (int i = currentIndex - 1; i >= 0; i--) {
			RelativeLayout.LayoutParams otherParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			views[i] = list.get(i).getTopMenuView();
			otherParams.addRule(RelativeLayout.RIGHT_OF, views[i + 1].getId());
			otherParams.leftMargin = -81;
			this.addView(views[i], otherParams);
			views[i].getChildAt(0).setNextFocusLeftId(views[i + 1].getChildAt(0).getId());
			views[i + 1].getChildAt(0).setNextFocusRightId(views[i].getChildAt(0).getId());
			// views[i].setFocusableInTouchMode(true);
		}

		for (int i = currentIndex + 1; i < size; i++) {
			RelativeLayout.LayoutParams otherParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			views[i] = list.get(i).getTopMenuView();
			otherParams.addRule(RelativeLayout.LEFT_OF, views[i - 1].getId());
			otherParams.rightMargin = -81;
			this.addView(views[i], otherParams);

			views[i].getChildAt(0).setNextFocusRightId(views[i - 1].getChildAt(0).getId());
			views[i - 1].getChildAt(0).setNextFocusLeftId(views[i].getChildAt(0).getId());
			// views[i].setFocusableInTouchMode(true);
		}

		views[0].getChildAt(0).setNextFocusRightId(views[views.length - 1].getChildAt(0).getId());
		views[views.length - 1].getChildAt(0).setNextFocusLeftId(views[0].getChildAt(0).getId());

		requestLayout();

	}

	public interface onNavigationTabChangeListener {

		void onScrollStart(int targetIndex);

		void onScrollComplete(int currentIndex);
	}

	private onNavigationTabChangeListener tabChangeListener;

	private long mKeyDownTime;

	private int count;

	public void setOnNavigationTabChangeListener(onNavigationTabChangeListener tabChangeListener) {
		this.tabChangeListener = tabChangeListener;
	}

	public onNavigationTabChangeListener getTabChangeListener() {
		return tabChangeListener;
	}

	public boolean dispatchKeyEvent(KeyEvent event) {

		
		return super.dispatchKeyEvent(event);
	}



	public boolean isCursor() {
		return isCursor;
	}

	public void setCursor(boolean isCursor) {
		this.isCursor = isCursor;
	}

	public void setIndexRequestFocus(int index) {
		if (null == views || index > views.length)
			return;
		if(!views[index].requestFocus()){
			views[index].requestFocusFromTouch();
			//views[index].setFocusableInTouchMode(true);
		}
		Log.d("AA", views[index].requestFocus()+"|view::"+views[index]);
	}

	@Override
	public void onAnimationStart(Animator animation) {
		setCursor(true);

	}

	@Override
	public void onAnimationEnd(Animator animation) {
		setCursor(false);

	}

	@Override
	public void onAnimationCancel(Animator animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animator animation) {
		// TODO Auto-generated method stub

	}
}
