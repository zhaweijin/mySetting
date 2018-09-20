package com.hiveview.tv.view.pager3d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public abstract class TabBasePageView extends RelativeLayout {

	protected boolean isRefleshData = false;
	protected Handler activityHandler;
	protected OnDataCompleted dataCompleted;

	public interface OnDataCompleted{
		public void onCompleted();
	}

	/**
	 * 翻页动画是否在进行中
	 */
	private static boolean isRotating = false;

	/**
	 * 当PageView发生改变时，回调接口
	 */
	private OnPageViewChangeListener pageViewChangeListener = null;

	/**
	 * 采用的何种显示策略
	 */
	protected Abstract3DPagerStrategy strategy = null;

	/**
	 * 当前页的渐隐渐现动画
	 */
	private Animator alphaAnimator = null;

	/**
	 * 页面发生改变时，执行的偏转动画
	 */
	private RotateAnimationListener rotateAnimationListener = new RotateAnimationListener();

	protected ViewFlipper[] flippers = null;

	protected View[] recommendEdgeViews = null;
	protected View[] bottomMenuViews = null;

	protected RecommendViewFocusDirectionListener viewFocusDirectionListener = null;

	protected DisplayImageOptions options = new DisplayImageOptions.Builder().resetViewBeforeLoading(false).cacheOnDisc(true).cacheInMemory(true)
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();

	protected int readDataFromDBInterval = 500;

	protected int readDataFromDBCount = 60;

	protected final String TAG = TabBasePageView.class.getSimpleName();

	@SuppressLint("HandlerLeak")
	public Handler dataHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			processData(msg.what);
		}

	};

	public TabBasePageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TabBasePageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TabBasePageView(Context context, RecommendViewFocusDirectionListener listener) {
		super(context);
		viewFocusDirectionListener = listener;
	}

	public TabBasePageView(Context context) {
		super(context);
	}

	protected void init() {
		if (null != viewFocusDirectionListener) {
			viewFocusDirectionListener.setRecommendViewsFocusDirection(recommendEdgeViews);
		}
	}

	/**
	 * 耗时操作完成后数据处理逻辑,参数为int,与 processData(Message msg)排斥使用
	 */
	protected void processData(int msgWhat) {

	}
	/**
	 * @Title: TabBasePageView
	 * @author:张鹏展
	 * @Description: 注册信息解绑
	 */
	public void setUnregister(){
		
	}
	
	/**
	 * 设置从Activity传过来的Handler
	 * @Title setHandler
	 * @author haozening
	 * @Description 
	 * @param handler
	 */
	public void setHandler(Handler handler) {
		activityHandler = handler;
	}
	public void setOnDataCompleted(OnDataCompleted dataCompleted) {
		this.dataCompleted = dataCompleted;
	}

	/**
	 * 发送耗时操作的完成结果的方法
	 * 
	 * @Title: TabBasePageView
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param msgWhat
	 */
	protected void sendLoadDataResultMessage(int msgWhat) {
		dataHandler.sendEmptyMessage(msgWhat);
	}

	/**
	 * 发生耗时加载或网络请求数据
	 * 
	 * @param runnable
	 *            请求数据的耗时操作
	 */
	protected final void submitRequest(Runnable runnable) {
		if (null != runnable) {
			HttpTaskManager.getInstance().submit(runnable);
		}
	}

	/**
	 * 启动当前页面的动画运行
	 * 
	 * @Title: TabBasePageView
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void startPageViewCustomAnimation() {
		if (null == flippers) {
			return;
		}

		for (int i = 0; i < flippers.length; i++) {
			if (flippers[i].getChildCount() > 1) {
				flippers[i].startFlipping();
				for (int k = 0; k < flippers[i].getChildCount(); k++) {
					flippers[i].getChildAt(k).clearAnimation();
				}
			}
		}

	}

	/**
	 * 停止当前页面的动画执行，防止阻塞UI线程
	 * 
	 * @Title: TabBasePageView
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void stopPageViewCustomAnimation() {
		if (null == flippers) {
			return;
		}

		for (int i = 0; i < flippers.length; i++) {
			flippers[i].stopFlipping();
		}
	}

	public void loadData(boolean isRefleshData) {
		this.isRefleshData = isRefleshData;
	}

	/**
	 * 取消旋转动画和渐隐渐现动画
	 */
	@Override
	public void clearAnimation() {
		super.clearAnimation();
		if (null != alphaAnimator) {
			alphaAnimator.cancel();
			alphaAnimator = null;
		}
	}

	/**
	 * 设置View的渐隐渐现动画
	 * 
	 * @Title: TabBasePageView
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param animator
	 */
	public void setPageViewChangeAnimator(Animator animator, Animation rotateAnimation) {

		clearAnimation();// 清空正在执行动画

		alphaAnimator = animator;// 启动属性渐隐渐现动画
		if (null != alphaAnimator) {
			alphaAnimator.start();
		}

		if (null != rotateAnimation) {// 启动偏转动画
			int pageIndex = (Integer) getTag();
			if (null != strategy && strategy.getCurrentIndex() == pageIndex) {// 当前的PageView是否为用户选择的页面
				rotateAnimation.setAnimationListener(rotateAnimationListener);
			}
			startAnimation(rotateAnimation);
		}
	}

	/**
	 * 设置页面改变时的监听
	 * 
	 * @Title: TabBasePageView
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param pageViewChangeListener
	 */
	public void setPageViewChangeListener(OnPageViewChangeListener pageViewChangeListener) {
		this.pageViewChangeListener = pageViewChangeListener;
	}

	/**
	 * 设置当前展示的策略
	 * 
	 * @Title: TabBasePageView
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param strategy
	 */
	public void setStrategy(Abstract3DPagerStrategy strategy) {
		this.strategy = strategy;
	}

	/**
	 * 当前页面是否在滚动中
	 * 
	 * @Title: TabBasePageView
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public static boolean isRotating() {
		return isRotating;
	}

	/**
	 * 用户选中的PageView，页面偏转
	 * 
	 * @ClassName: RotateAnimationListener
	 * @Description: TODO
	 * @author: 陈丽晓
	 * @date 2014-6-10 下午5:46:10
	 * 
	 */
	class RotateAnimationListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation arg0) {
			isRotating = false;// 偏转动画执行完成
			strategy.setRotating(isRotating);
			startPageViewCustomAnimation();
			if (null != pageViewChangeListener) {
				pageViewChangeListener.onPageChangeComplete(strategy.getCurrentIndex());
			}
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {

		}

		@Override
		public void onAnimationStart(Animation arg0) {
			isRotating = true;// 旋转动画开始执行
			strategy.setRotating(isRotating);
		}

	}

	/**
	 * 推荐位的View向左向右下个焦点方向指定
	 * 
	 * @ClassName: RecommendViewFocusDirectionListener
	 * @Description: TODO
	 * @author: 陈丽晓
	 * @date 2014-7-24 上午11:25:46
	 * 
	 */
	public interface RecommendViewFocusDirectionListener {
		public void setRecommendViewsFocusDirection(View[] views);

		public void setButtomMenuViewFocusDirection(View[] views);
		
		
		public void setTopMenuViewFocusDirection(View topTabView);
	}

	protected void checkPageIsIdle() {
		while (strategy.isRotating()) {
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		updateUIRefleshData();
	}

	public abstract void updateUIRefleshData();

	public View getBottomMenuView() {
		return null;
	}

	/**
	 * 根据轮播推荐位的子推荐位的数目，是否轮播推荐位
	 * 
	 * @Title: TabBasePageView
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param subCount
	 *            推荐位数据个数
	 * @param flipper
	 *            轮播推荐位
	 * @param interval
	 *            推荐轮播时间间隔
	 */
	protected void changeFlipperFlipping(int subCount, ViewFlipper flipper, int interval) {
		if (subCount > 1) {// 设置轮播推荐位的间隔时间
			interval = interval != 0 ? interval : 5000;
			flipper.setFlipInterval(interval);
			flipper.startFlipping();
		} else {
			flipper.stopFlipping();
		}

		for (int k = 0; k < flipper.getChildCount(); k++) {
			flipper.getChildAt(k).clearAnimation();
		}
	}

	private int DURATION = 600;

	private AnimatorSet getFocusAnimatorSet(TextView tv, ImageView image) {
		AnimatorSet getFocusAnimSet = new AnimatorSet();
		Animator tvOutScaleXAnim = ObjectAnimator.ofFloat(tv, "scaleX", 1.0f, 1.2f).setDuration(DURATION);
		Animator tvOutScaleYAnim = ObjectAnimator.ofFloat(tv, "scaleY", 1.0f, 1.2f).setDuration(DURATION);
		Animator tvAlphaOutAnim = ObjectAnimator.ofFloat(image, "alpha", 0f, 1.0f).setDuration(DURATION);
		getFocusAnimSet.playTogether(tvOutScaleXAnim, tvOutScaleYAnim, tvAlphaOutAnim);
		return getFocusAnimSet;
	}

	private AnimatorSet lostFocusAnimatorSet(TextView tv, ImageView image) {
		AnimatorSet lostFocusAnimSet = new AnimatorSet();
		Animator tvInScaleXAnim = ObjectAnimator.ofFloat(tv, "scaleX", 1.2f, 1.0f).setDuration(DURATION);
		Animator tvInScaleYAnim = ObjectAnimator.ofFloat(tv, "scaleY", 1.2f, 1.0f).setDuration(DURATION);
		Animator tvAlphaInAnim = ObjectAnimator.ofFloat(image, "alpha", 1.0f, 0.0f).setDuration(DURATION);
		lostFocusAnimSet.playTogether(tvInScaleXAnim, tvInScaleYAnim, tvAlphaInAnim);
		return lostFocusAnimSet;
	}

	public ViewGroup getTopMenuView() {
		return null;
	}

	public class TopTabOnFocusChangeListener implements OnFocusChangeListener {

		private TextView tvTextView;
		private ImageView imageView;
		private AnimatorSet getFocusSet;
		private AnimatorSet lostFocusSet;
		private int pageIndex=0;

		public TopTabOnFocusChangeListener(TextView tv, ImageView image) {
			this.tvTextView = tv;
			this.imageView = image;
			pageIndex=(Integer) getTag();
		}

		@Override
		public void onFocusChange(View arg0, boolean hasCache) {
			
			if (null == getFocusSet || null == lostFocusSet) {
				getFocusSet = getFocusAnimatorSet(tvTextView, imageView);
				lostFocusSet = lostFocusAnimatorSet(tvTextView, imageView);
			}
			if (hasCache) {
				getFocusSet.start();
				imageView.setVisibility(View.VISIBLE);
				strategy.getTopTabs().getTabChangeListener().onScrollStart(pageIndex);
				HiveviewApplication.mcurrentfocus=arg0;
			} else {
				lostFocusSet.start();
				imageView.setVisibility(View.INVISIBLE);
			}
		}
	}

}
