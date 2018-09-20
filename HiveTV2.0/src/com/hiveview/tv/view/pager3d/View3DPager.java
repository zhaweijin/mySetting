package com.hiveview.tv.view.pager3d;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.hiveview.tv.view.NavigationNewTabView;
import com.hiveview.tv.view.SubTabView;

/**
 * 实现3D效果的View
 * 
 * @ClassName: View3DPager
 * @Description: TODO
 * @author: 陈丽晓
 * @date 2014-6-5 下午1:01:18
 * 
 */
@SuppressLint({ "NewApi", "UseSparseArrays" })
public class View3DPager extends RelativeLayout {

	/**
	 * 日志输出tag
	 */
	private final String TAG = "View3DPager";

	private List<TabBasePageView> pageViews = new ArrayList<TabBasePageView>();

	private Abstract3DPagerStrategy strategy = null;

	private OnPageViewChangeListener pageViewChangeListener = null;

	private NavigationNewTabView topTabs = null;
	private SubTabView subTabs = null;
	private boolean isRefleshData = false;

	public View3DPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public View3DPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public View3DPager(Context context) {
		super(context);
	}

	/**
	 * 显示当前页的下一个PageView
	 * 
	 * @Title: View3DPager
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void moveToNext() {
		//Log.d(TAG, "moveToNext====1");
		for (int i = 0; i < pageViews.size(); i++) {
			pageViews.get(i).clearAnimation();
		}
		topTabs.clearCursorAnimation();
		//Log.d(TAG, "moveToNext====2");
		topTabs.scrollCursor(strategy.addIndex(strategy.getCurrentIndex() + 1), strategy.getCurrentIndex(), pageViews.size());
		strategy.stopPageViewCustomAnimation();
		strategy.moveToNext();
		if (null != pageViewChangeListener) {
			pageViewChangeListener.onPageChangeStart(strategy.getCurrentIndex());
		}
		//Log.d(TAG, "moveToNext====3");
	}
	
	public void moveToNextByDiff(int diff) {
		//Log.d(TAG, "moveToNext====1");
		for (int i = 0; i < pageViews.size(); i++) {
			pageViews.get(i).clearAnimation();
		}
		topTabs.clearCursorAnimation();
		//Log.d(TAG, "moveToNext====2");
		topTabs.scrollCursorByDiff(strategy.addIndex(strategy.getCurrentIndex() + diff), strategy.getCurrentIndex(), pageViews.size());
		strategy.stopPageViewCustomAnimation();
		strategy.moveToNextByDiff(diff);
		if (null != pageViewChangeListener) {
			pageViewChangeListener.onPageChangeStart(strategy.getCurrentIndex());
		}
		//Log.d(TAG, "moveToNext====3");
	}

	/**
	 * 显示当前页的上一个PageView
	 * 
	 * @Title: View3DPager
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void moveToPrevious() {
		//Log.d(TAG, "moveToPrevious====4");
		for (int i = 0; i < pageViews.size(); i++) {
			pageViews.get(i).clearAnimation();
		}
		topTabs.clearCursorAnimation();
		//Log.d(TAG, "moveToPrevious====5");
		topTabs.scrollCursor(strategy.subtractIndex(strategy.getCurrentIndex() - 1), strategy.getCurrentIndex(), pageViews.size());
		strategy.stopPageViewCustomAnimation();
		strategy.moveToPrevious();
		if (null != pageViewChangeListener) {
			pageViewChangeListener.onPageChangeStart(strategy.getCurrentIndex());
		}
		//Log.d(TAG, "moveToPrevious====6");
	}
	
	
	public void moveToPreviousByDiff(int diff) {
		//Log.d(TAG, "moveToPrevious====4");
		for (int i = 0; i < pageViews.size(); i++) {
			pageViews.get(i).clearAnimation();
		}
		topTabs.clearCursorAnimation();
		//Log.d(TAG, "moveToPrevious====5");
		topTabs.scrollCursorByDiff(strategy.subtractIndex(strategy.getCurrentIndex() - diff), strategy.getCurrentIndex(), pageViews.size());
		strategy.stopPageViewCustomAnimation();
		strategy.moveToPreviousByDiff(diff);
		if (null != pageViewChangeListener) {
			pageViewChangeListener.onPageChangeStart(strategy.getCurrentIndex());
		}
		//Log.d(TAG, "moveToPrevious====6");
	}

	/**
	 * View3DPager 初始化显示
	 * 
	 * @Title: View3DPager
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param index
	 */
	public void initTabViewByIndex(int index, Abstract3DPagerStrategy strategy) {
		this.strategy = strategy;

		if (pageViews.size() == 0) {// 必须添加View才能显示
			Log.e(TAG, "please add TabPageView!");
			return;
		}

		TabBasePageView pageView = pageViews.get(index);
		if (null == pageView) {
			Log.e(TAG, index + " TabPageView is null");
			return;
		}

		if (null == strategy) {
			Log.e(TAG, "please call setStrategy!");
			return;
		}

		strategy.setPageViews(pageViews);

		for (int i = 0; i < pageViews.size(); i++) {// 所有View透明
			pageViews.get(i).setAlpha(0f);
		}

		// 当前PView突出显示
		pageView.setAlpha(1f);
		pageView.bringToFront();
		invalidate();

		strategy.initLeftView(index);
		strategy.initRightView(index);

	}

	/**
	 * 把根据业务定义的TabPageView（继承TabBasePageView）添加到View3DPager实例中，用于以3D效果显示
	 * 
	 * @Title: View3DPager
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param child
	 * @param index
	 */
	public void addTabPageView(TabBasePageView child, int index) {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		child.setLayoutParams(params);
		addView(child, index);
		child.setTag(index);
		pageViews.add(child);
	}

	/**
	 * 设置PageView换页监听
	 * 
	 * @Title: View3DPager
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param pageViewChangeListener
	 */
	public void setPageViewChangeListener(OnPageViewChangeListener pageViewChangeListener) {
		this.pageViewChangeListener = pageViewChangeListener;
		for (int i = 0; i < pageViews.size(); i++) {
			pageViews.get(i).setPageViewChangeListener(pageViewChangeListener);
			pageViews.get(i).setStrategy(strategy);
		}
	}

	/**
	 * 载入数据绘制到页面上
	 * 
	 * @Title: View3DPager
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param reflesh
	 *            此时载入数据是否是定时刷新请求的数据
	 * @param isFromCache
	 *            要加载的数据是否来自缓存
	 */
	public void loadData(boolean reflesh) {
		strategy.loadPageData(isRefleshData);
	}
	
	/**
	 * 设置Activity的Handler
	 * @Title setHandler
	 * @author haozening
	 * @Description 
	 * @param handler
	 */
	public void setHandler(Handler handler) {
		strategy.setHandler(handler);
	}

	public void setTopAndSubTabs(NavigationNewTabView top, SubTabView sub) {
		strategy.setTopAndSubTabs(top, sub);
		topTabs = top;
		subTabs = sub;
	}

	public int getCurrentPageIndex() {
		return strategy.getCurrentIndex();
	}

	public int get3DPagerChildCount() {
		return pageViews.size();
	}

	public void setInstallApkCount(int gameCount, int appCount) {
		strategy.setInstallApkCount(gameCount, appCount);
	}


	/**
	 * 动画完成，手动刷新PageView
	 * 
	 * @Title: View3DPager
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void invalidateAllPageView() {
		// postInvalidateDelayed(200);
		// for (int i = 0; i < pageViews.size(); i++) {
		// pageViews.get(i).postInvalidateDelayed(200);
		// }
	}

}
