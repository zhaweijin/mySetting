package com.hiveview.tv.view.pager3d;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;

public class HiveViewFlipper extends RelativeLayout {

	/**
	 * 延迟时间后进行轮播
	 */
	protected Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (isPlay) {
				play();
			}
		}

	};

	/**
	 * 子View进入和出去动画的执行时间
	 */
	protected int duration = 800;

	/**
	 * 是否出初始化
	 */
	protected boolean isInit = false;

	/**
	 * 轮播是否进行切换播放
	 */
	private boolean isPlay = true;

	/**
	 * 轮播要出去的子View的顺序
	 */
	protected int outIndex = 0;

	/**
	 * 轮播要进入显示的子View的顺序
	 */
	protected int inIndex = 1;

	/**
	 * 轮播进入显示的回调监听
	 */
	protected FlipperInChangeListener flipperInChangeListener;
	/**
	 * 轮播出去显示的动画执行完成
	 */
	protected OutFlipperAnimatorListener OutFlipperAnimatorListener = new OutFlipperAnimatorListener();

	/**
	 * 轮播进入显示的动画执行完成
	 */
	protected InFlipperAnimatorListener inFlipperAnimatorListener = new InFlipperAnimatorListener();

	/**
	 * 保存子View显示位置的集合
	 */
	protected ArrayList<View> views = new ArrayList<View>();

	/**
	 * 轮播间隔时间，毫秒数
	 */
	protected int intervalTime = 5000;

	/**
	 * 当前的Flipper在页面中的位置
	 */
	protected int flipperIndexInPage = 0;

	public HiveViewFlipper(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public HiveViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HiveViewFlipper(Context context) {
		super(context);
	}

	/**
	 * 添加子View,设置初始显示
	 * 
	 * @Title: MyViewFlipper
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param child
	 * @param index
	 */
	public void addChildView(View child, int index,ViewGroup.LayoutParams params) {
		views.add(child);
		addView(child,params);
		if (index != 0) {
			child.setAlpha(0f);
		}
	}

	/**
	 * 初始化View在Y轴的显示位置
	 * 
	 * @Title: MyViewFlipper
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void init() {
		if (getChildCount() <= 1) {
			return;
		}

		for (int i = 0; i < views.size(); i++) {
			View view = views.get(i);
			if (i != 0) {
				view.setTranslationY(view.getTranslationY() + view.getHeight());
			}
		}

		inIndex = 1;
		outIndex = 0;
	}

	/**
	 * 播放轮播动画
	 * 
	 * @Title: MyViewFlipper
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void play() {
		if (!isInit) {
			init();
			isInit = true;
		}

		View outView = views.get(outIndex);
		outView.setAlpha(1f);
		Animator outAnimator = ObjectAnimator.ofFloat(outView, "translationY", outView.getTranslationY(), outView.getTranslationY() - outView.getHeight()).setDuration(duration);
		OutFlipperAnimatorListener.setOutView(outView);
		outAnimator.addListener(OutFlipperAnimatorListener);
		outAnimator.start();
		System.out.println("outView.getTranslationY(): " + outView.getTranslationY());

		View inView = views.get(inIndex);
		inView.setAlpha(1f);
		inFlipperAnimatorListener.setInView(inView);
		Animator inAnimator = ObjectAnimator.ofFloat(inView, "translationY", inView.getTranslationY(), inView.getTranslationY() - inView.getHeight()).setDuration(duration);
		inAnimator.addListener(inFlipperAnimatorListener);
		inAnimator.start();

		outIndex = inIndex;
		inIndex = inIndex + 1 == getChildCount() ? 0 : inIndex + 1;

		mHandler.sendEmptyMessageDelayed(0, intervalTime);
	}

	/**
	 * 停止轮播
	 * 
	 * @Title: MyViewFlipper
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void stopFlipping() {
		isPlay = false;
	}

	/**
	 * 启动轮播动画
	 * 
	 * @Title: MyViewFlipper
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void startFlipping() {
		isPlay = true;// 状态位为开始轮播
		if (getChildCount() > 1) {
			mHandler.sendEmptyMessageDelayed(0, intervalTime);
		}

	}

	/**
	 * 
	 * @ClassName: OutFlipperAnimatorListener
	 * @Description: TODO
	 * @author: 陈丽晓
	 * @date 2014-6-17 下午1:31:09
	 * 
	 */
	class OutFlipperAnimatorListener implements AnimatorListener {
		/**
		 * 轮播出去的子View
		 */
		private View outView;

		public void setOutView(View v) {
			outView = v;
		}

		@Override
		public void onAnimationStart(Animator animation) {

		}

		@Override
		public void onAnimationEnd(Animator animation) {
			// 出去动画轮播完成后，设置到初始位置，等待下一轮轮播
			outView.setTranslationY(outView.getTranslationY() + 2 * outView.getHeight());
		}

		@Override
		public void onAnimationCancel(Animator animation) {

		}

		@Override
		public void onAnimationRepeat(Animator animation) {

		}

	}

	/**
	 * 
	 * @ClassName: InFlipperAnimatorListener
	 * @Description: TODO
	 * @author: 陈丽晓
	 * @date 2014-6-17 下午1:32:15
	 * 
	 */
	class InFlipperAnimatorListener implements AnimatorListener {
		/**
		 * 进入显示的View
		 */
		private View inView;

		public void setInView(View v) {
			inView = v;
		}

		@Override
		public void onAnimationStart(Animator animation) {
			if (null != flipperInChangeListener) {// 轮播进入动画开始执行，回调接口
				flipperInChangeListener.onFlipperChange(inView,flipperIndexInPage);
			}
		}

		@Override
		public void onAnimationEnd(Animator animation) {

		}

		@Override
		public void onAnimationCancel(Animator animation) {

		}

		@Override
		public void onAnimationRepeat(Animator animation) {

		}

	}

	/**
	 * 轮播进入的回调接口
	 * 
	 * @ClassName: FlipperInChangeListener
	 * @Description: TODO
	 * @author: 张鹏展
	 * @date 2014-6-17 下午1:34:06
	 * 
	 */
	public interface FlipperInChangeListener {
		public void onFlipperChange(View v,int flipperIndexInPage);
	}

	/**
	 * 设置轮播进入的回调接口
	 * 
	 * @Title: MyViewFlipper
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param flipperInChangeListener
	 */
	public void setFlipperInChangeListener(FlipperInChangeListener flipperInChangeListener) {
		this.flipperInChangeListener = flipperInChangeListener;
	}

	/***
	 * 设置每次间隔的轮播时间
	 * 
	 * @Title: MyViewFlipper
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param intervalTime
	 */
	public void setFlipInterval(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	/**
	 * 得到当前显示的View
	 * 
	 * @Title: HiveViewFlipper
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public View getCurrentView() {
		return views.get(inIndex);
	}

	/**
	 * 得到当前显示的View的索引
	 * 
	 * @Title: HiveViewFlipper
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public int getCurrentIndex() {
		return inIndex;
	}

	public int getFlipperIndexInPage() {
		return flipperIndexInPage;
	}

	public void setFlipperIndexInPage(int flipperIndexInPage) {
		this.flipperIndexInPage = flipperIndexInPage;
	}
	
	

}
