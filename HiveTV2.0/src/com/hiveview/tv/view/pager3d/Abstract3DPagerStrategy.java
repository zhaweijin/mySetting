package com.hiveview.tv.view.pager3d;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;

import com.hiveview.tv.activity.HomeActivity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.view.NavigationNewTabView;
import com.hiveview.tv.view.SubTabView;

@SuppressLint("UseSparseArrays")
public abstract class Abstract3DPagerStrategy {
	/**
	 * 所有PageView的载体
	 */
	protected View3DPager view3dPager = null;
	/**
	 * 直面用户的Tab页View的索引
	 */
	protected int currentIndex;

	/**
	 * 存储PageView的Map
	 */
	protected List<TabBasePageView> pageViews = new ArrayList<TabBasePageView>();

	protected NavigationNewTabView topTabs = null;

	protected SubTabView subTabs = null;
	protected Handler activityHandler;

	/**
	 * 翻页动画是否在进行中
	 */
	private boolean isRotating = false;

	/**
	 * 创建PageView翻页的库动画对象
	 */
	protected PageChangeAnimationFactory animationFactory = new PageChangeAnimationFactory();

	public Abstract3DPagerStrategy(View3DPager view3dPager) {
		super();
		this.view3dPager = view3dPager;
		if (AppConstant.ISDOMESTIC) {
			if(HomeActivity.isNeedChlitina){
				currentIndex = 2;
			}else{
			currentIndex = HomeActivity.NavigationViewIndex;
			}
		} else {
			currentIndex = HomeActivity.NavigationViewIndex_ISDOMESTIC;
		}
	}

	/**
	 * @Title: TabBasePageView
	 * @author:张鹏展
	 * @Description: 注册信息解绑
	 */
	public void setUnregister() {

	}

	/**
	 * 以3D动画的形式翻转到下一页的TabPageView
	 * 
	 * @Title: Abstract3DPagerStrategy
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public abstract void moveToNext();

	public abstract void moveToNextByDiff(int diff);
	/**
	 * 以3D动画的形式翻转到上一页的TabPageView
	 * 
	 * @Title: Abstract3DPagerStrategy
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public abstract void moveToPrevious();

	public abstract void moveToPreviousByDiff(int diff);
	/**
	 * 初始化时，当前页的左侧的TabPageView的3d偏转
	 * 
	 * @Title: Abstract3DPagerStrategy
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public abstract void initLeftView(int index);

	/**
	 * 初始化时，当前页的右侧的TabPageView的3d偏转
	 * 
	 * @Title: Abstract3DPagerStrategy
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public abstract void initRightView(int index);

	/**
	 * @Title: Abstract3DPagerStrategy
	 * @author:张鹏展
	 * @Description: 语音控制影院item
	 * @param value
	 */
	public void setCinemaOnClick(String value) {
	}

	/**
	 * @Title: Abstract3DPagerStrategy
	 * @author:张鹏展
	 * @Description: 语音控制影院换页
	 * @param value
	 */
	public void setPageChange(String value) {
	}

	/**
	 * @Title: Abstract3DPagerStrategy
	 * @author:张鹏展
	 * @Description: 获取影院的频道数据
	 * @return
	 */
	public ArrayList<String> getCinemaName() {
		return null;
	}

	/**
	 * 预计算当前页面的下一个位置
	 * 
	 * @Title: View3DPager
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param index
	 * @return
	 */
	protected int addIndex(int index) {
		if (index >= pageViews.size()) {
			index = index - pageViews.size();
		}
		return index;
	}

	/**
	 * 设置Activity的Handler
	 * 
	 * @Title setHandler
	 * @author haozening
	 * @Description
	 * @param handler
	 */
	public void setHandler(Handler handler) {
		activityHandler = handler;
	}

	/**
	 * 预计算当前页面的上一个位置
	 * 
	 * @Title: Abstract3DPagerStrategy
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param index
	 * @return
	 */
	protected int subtractIndex(int index) {
		if (index < 0) {
			index = index + pageViews.size();
		}

		return index;
	}

	/**
	 * 设置要显示的PageView的集合
	 * 
	 * @Title: Abstract3DPagerStrategy
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param pageViews
	 */
	public void setPageViews(List<TabBasePageView> pageViews) {
		this.pageViews = pageViews;
	}

	/**
	 * 得到当前页面的索引
	 * 
	 * @Title: Abstract3DPagerStrategy
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}

	public abstract void loadPageData(boolean isRefleshData);

	class LoadDataTask extends Thread {

		private static final String TAG = "LoadDataTask";
		Object[] params;

		@Override
		public void run() {
			super.run();
			Log.d(TAG, "LoadDataTask::doInBackground loadImage");
			int i = Integer.valueOf(params[0].toString());
			boolean isRefleshData = Boolean.valueOf(params[1].toString());
			pageViews.get(i).loadData(isRefleshData);
		}

		public void execute(Object... params) {
			this.params = params;
			start();
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
		for (int i = 0; i < pageViews.size(); i++) {
			pageViews.get(i).stopPageViewCustomAnimation();
		}
	}

	public void setTopAndSubTabs(NavigationNewTabView top, SubTabView sub) {
		this.topTabs = top;
		this.subTabs = sub;
	}

	public void setInstallApkCount(int gameCount, int appCount) {

	}

	public boolean isRotating() {
		return isRotating;
	}

	public void setRotating(boolean isRotating) {
		this.isRotating = isRotating;
	}

	public NavigationNewTabView getTopTabs() {
		return topTabs;
	}

}
