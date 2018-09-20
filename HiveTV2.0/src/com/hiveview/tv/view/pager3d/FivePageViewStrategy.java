package com.hiveview.tv.view.pager3d;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.view.MatrixAppView;
import com.hiveview.tv.view.MatrixGameView;
import com.hiveview.tv.view.MatrixMovieView;
import com.hiveview.tv.view.MatrixRecommendView;
import com.hiveview.tv.view.MatrixTVView;
import com.hiveview.tv.view.MatrixTVForeidgnView;
import com.hiveview.tv.view.NavigationNewTabView;
import com.hiveview.tv.view.SubTabView;
import com.hiveview.tv.view.pager3d.TabBasePageView.OnDataCompleted;
import com.hiveview.tv.view.pager3d.TabBasePageView.RecommendViewFocusDirectionListener;

@SuppressLint("NewApi")
public class FivePageViewStrategy extends Abstract3DPagerStrategy {

	/**
	 * @Fields blcakView:黑屏背景
	 */
	private View blackView;

	public FivePageViewStrategy(View3DPager _3dPager, OnDataCompleted dataCompleted) {
		super(_3dPager);

		MatrixAppView appView = new MatrixAppView(view3dPager.getContext(), new AppFocusDirectioner());
		MatrixMovieView movieView = new MatrixMovieView(view3dPager.getContext(), new MovieFocusDirectioner());
		MatrixRecommendView recommendView = new MatrixRecommendView(view3dPager.getContext(), new RecommendFocusDirectioner());
		MatrixGameView gameView = new MatrixGameView(view3dPager.getContext(), new GameFocusDirectioner());
		/* start by guosongsheng 美国测试使用 */
		TabBasePageView tvView = null;
		if (AppConstant.ISDOMESTIC) {
			tvView = new MatrixTVView(view3dPager.getContext(), new TvFocusDirectioner());
		} else {
			tvView = new MatrixTVForeidgnView(view3dPager.getContext(), new TvFocusDirectioner());
		}
		/* end by guosongsheng */
		appView.setOnDataCompleted(dataCompleted);
		movieView.setOnDataCompleted(dataCompleted);
		recommendView.setOnDataCompleted(dataCompleted);
		gameView.setOnDataCompleted(dataCompleted);
		tvView.setOnDataCompleted(dataCompleted);

		appView.setVisibility(View.INVISIBLE);
		movieView.setVisibility(View.INVISIBLE);
		tvView.setVisibility(View.INVISIBLE);
		gameView.setVisibility(View.INVISIBLE);

		view3dPager.addTabPageView(appView, 0);
		view3dPager.addTabPageView(movieView, 1);
		view3dPager.addTabPageView(recommendView, 2);
		view3dPager.addTabPageView(tvView, 3);
		view3dPager.addTabPageView(gameView, 4);
	}

	@Override
	public void moveToNext() {
		currentIndex = addIndex(currentIndex + 1);// 确定当前动画的下一个View
		int leftIndex1 = addIndex(currentIndex + 1);
		int leftIndex2 = addIndex(currentIndex + 2);
		int rightIndex1 = subtractIndex(currentIndex - 1);
		int rightIndex2 = subtractIndex(currentIndex - 2);

		// if(pageViewChangeListener)

		// 当前View的动画偏转
		TabBasePageView curPageView = pageViews.get(currentIndex);
		animationFactory.getNegUnitTo0RotateAnimation(curPageView);

		// 左1
		TabBasePageView leftPageView1 = pageViews.get(leftIndex1);
		animationFactory.getNeg2UnitToNegUnitRotateYAnimation(leftPageView1);

		// 左2
		TabBasePageView leftPageView2 = pageViews.get(leftIndex2);
		animationFactory.get2UnitToNeg2UnitRotateYAnimation(leftPageView2);

		// 右1
		TabBasePageView rightPageView1 = pageViews.get(rightIndex1);
		animationFactory.get0ToUnitRotateAnimation(rightPageView1);

		// 右1
		TabBasePageView rightPageView2 = pageViews.get(rightIndex2);
		animationFactory.getUnitTo2UnitRotateYAnimation(rightPageView2);

	}

	@Override
	public void moveToPrevious() {
		currentIndex = subtractIndex(currentIndex - 1);
		int rightIndex1 = subtractIndex(currentIndex - 1);
		int rightIndex2 = subtractIndex(currentIndex - 2);
		int leftIndex1 = addIndex(currentIndex + 1);
		int leftIndex2 = addIndex(currentIndex + 2);

		// 当前View的动画偏转
		TabBasePageView curPageView = pageViews.get(currentIndex);
		animationFactory.getUnitTo0RotateAnimation(curPageView);

		// 右1
		TabBasePageView rightPageView1 = pageViews.get(rightIndex1);
		animationFactory.get2UnitToUnitRotateYAnimation(rightPageView1);

		// 右2
		TabBasePageView rightPageView2 = pageViews.get(rightIndex2);
		animationFactory.getNeg2UnitTo2UnitRotateYAnimation(rightPageView2);

		// 左1
		TabBasePageView leftPageView1 = pageViews.get(leftIndex1);
		animationFactory.get0ToNegUnitRotateAnimation(leftPageView1);

		// 左2
		TabBasePageView leftPageView2 = pageViews.get(leftIndex2);
		animationFactory.getNegUnitToNeg2UnitRotateYAnimation(leftPageView2);

	}

	public void setBlackView(View view) {
		this.blackView = view;
	}

	public void initLeftView(int index) {
		TabBasePageView leftPageView1 = pageViews.get(currentIndex - 1);
		animationFactory.get0ToUnitImmediatelyAnimation(leftPageView1);
		leftPageView1.stopPageViewCustomAnimation();
		leftPageView1.setVisibility(View.VISIBLE);
		Log.d("PageChangeAnimationFactory", "pageView.isShow():7:" + leftPageView1.isShown());
		TabBasePageView leftPageView2 = pageViews.get(currentIndex - 2);
		animationFactory.get0ToUnit2ImmediatelyRotateYAnimation(leftPageView2);
		leftPageView2.stopPageViewCustomAnimation();
		leftPageView2.setVisibility(View.VISIBLE);
		if (null != blackView) {
			blackView.setVisibility(View.GONE);

		}
	}

	public void initRightView(int index) {
		TabBasePageView rightPageView1 = pageViews.get(currentIndex + 1);
		animationFactory.get0ToNegUnitImmediatelyRotateAnimation(rightPageView1);
		rightPageView1.stopPageViewCustomAnimation();
		rightPageView1.setVisibility(View.VISIBLE);

		TabBasePageView rightPageView2 = pageViews.get(currentIndex + 2);
		animationFactory.get0ToNegUnit2ImmediatelyRotateAnimation(rightPageView2);
		rightPageView2.stopPageViewCustomAnimation();
		rightPageView2.setVisibility(View.VISIBLE);
		if (null != blackView) {
			blackView.setVisibility(View.GONE);
		}

	}

	@Override
	public void loadPageData(boolean isRefleshData) {
		// pageViews.get(2).loadData(isRefleshData);// 载入推荐页面的数据
		// pageViews.get(0).loadData(isRefleshData);// 载入应用页面的数据
		// pageViews.get(3).loadData(isRefleshData);// 载入电视页面的数据
		// pageViews.get(1).loadData(isRefleshData);// 载入影院页面的数据
		// pageViews.get(4).loadData(isRefleshData);// 载入游戏页面的数据
		new LoadDataTask().execute(2, isRefleshData);
		new LoadDataTask().execute(0, isRefleshData);
		new LoadDataTask().execute(3, isRefleshData);
		new LoadDataTask().execute(1, isRefleshData);
		new LoadDataTask().execute(4, isRefleshData);
	}

	@Override
	public void setTopAndSubTabs(NavigationNewTabView top, SubTabView sub) {
		super.setTopAndSubTabs(top, sub);
		top.addTopMenus(pageViews, currentIndex);
		sub.addBottomMenus(pageViews, currentIndex);
	}

	class GameFocusDirectioner implements RecommendViewFocusDirectionListener {

		@Override
		public void setRecommendViewsFocusDirection(View[] views) {
			if (null != views) {
				if (AppConstant.ISDOMESTIC) {
					views[2].setNextFocusRightId(R.id.matrix_tv_layout_0);
					views[3].setNextFocusRightId(R.id.matrix_tv_layout_1);
				} else {
					views[2].setNextFocusRightId(R.id.matrix_tv_layout_3);
					views[3].setNextFocusRightId(R.id.matrix_tv_layout_3);
				}
			}
		}

		@Override
		public void setButtomMenuViewFocusDirection(View[] views) {
			// 管理中心
			View gameManagerView = views[0];
			if (AppConstant.ISDOMESTIC) {
				gameManagerView.setNextFocusRightId(R.id.sub_navigation_tv_category_text_layout);
			} else {
				gameManagerView.setNextFocusRightId(R.id.sub_navigation_tv_direction_text_layout);
			}
			// 外接存储
			View gameUserView = views[1];
			gameUserView.setNextFocusLeftId(R.id.sub_navigation_app_setting_text_layout);
		}

		@Override
		public void setTopMenuViewFocusDirection(View topTabView) {
			// topTabView.setNextFocusRightId(R.id.navigation_tab_tv_text);
		}

	}

	class TvFocusDirectioner implements RecommendViewFocusDirectionListener {

		@Override
		public void setRecommendViewsFocusDirection(View[] views) {

		}

		@Override
		public void setButtomMenuViewFocusDirection(View[] views) {
			// 分类选台
			View categoryView = views[0];
			categoryView.setNextFocusLeftId(R.id.sub_navigation_game_setting_text_layout);

			// 直播提醒
			View notificationView = views[2];
			notificationView.setNextFocusRightId(R.id.sub_navigation_recommend_record_text_layout);
			if (!AppConstant.ISDOMESTIC) {
				/* start by guosongsheng 机顶盒设置 */
				View settingView = views[1];
				settingView.setNextFocusLeftId(R.id.sub_navigation_game_setting_text_layout);
				settingView.setNextFocusRightId(R.id.sub_navigation_recommend_record_text_layout);
				/* end by guosongsheng */
			}
		}

		@Override
		public void setTopMenuViewFocusDirection(View topTabView) {

		}

	}

	class AppFocusDirectioner implements RecommendViewFocusDirectionListener {

		@Override
		public void setRecommendViewsFocusDirection(View[] views) {
		}

		@Override
		public void setButtomMenuViewFocusDirection(View[] views) {
			// 应用管理
			View appManager = views[2];
			// 外接存储
			View externalView = views[0];
			appManager.setNextFocusLeftId(R.id.sub_navigation_app_external_text_layout);
			appManager.setNextFocusRightId(R.id.sub_navigation_game_user_text_layout);
			appManager.setNextFocusUpId(R.id.matrix_app_layout_4);
			externalView.setNextFocusRightId(R.id.sub_navigation_app_setting_text_layout);
		}

		@Override
		public void setTopMenuViewFocusDirection(View topTabView) {
			// TODO Auto-generated method stub

		}

	}

	class RecommendFocusDirectioner implements RecommendViewFocusDirectionListener {

		@Override
		public void setRecommendViewsFocusDirection(View[] views) {
			if (null != views) {
				if (!AppConstant.ISDOMESTIC) {
					views[0].setNextFocusLeftId(R.id.matrix_tv_layout_6);
					views[4].setNextFocusLeftId(R.id.matrix_tv_layout_7);
				} else {
					views[0].setNextFocusLeftId(R.id.matrix_tv_layout_3);
					views[4].setNextFocusLeftId(R.id.matrix_tv_layout_3);
				}
			}
		}

		@Override
		public void setButtomMenuViewFocusDirection(View[] views) {
			// 观看记录
			View recommendRecordLayout = views[1];
			if (AppConstant.ISDOMESTIC) {
				recommendRecordLayout.setNextFocusLeftId(R.id.sub_navigation_tv_notification_text_layout);
			} else {
				recommendRecordLayout.setNextFocusLeftId(R.id.sub_navigation_tv_direction_text_layout);
			}
			// 系统设置
			View recommendSetting = views[3];
			recommendSetting.setNextFocusRightId(R.id.sub_navigation_movie_search_text_layout);
		}

		@Override
		public void setTopMenuViewFocusDirection(View topTabView) {
			// topTabView.setNextFocusLeftId(R.id.navigation_tab_tv_text);
		}

	}

	class MovieFocusDirectioner implements RecommendViewFocusDirectionListener {

		@Override
		public void setRecommendViewsFocusDirection(View[] views) {
		}

		@Override
		public void setButtomMenuViewFocusDirection(View[] views) {

			// 搜索视频
			View movieSearchLayout = views[0];
			movieSearchLayout.setNextFocusLeftId(R.id.sub_navigation_recommend_setting_text_layout);

			// 影片收藏
			View movieFavouriteLayout = views[2];
			movieFavouriteLayout.setNextFocusRightId(R.id.sub_navigation_app_external_text_layout);
		}

		@Override
		public void setTopMenuViewFocusDirection(View topTabView) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void setInstallApkCount(int gameCount, int appCount) {
		((MatrixGameView) pageViews.get(4)).setGameInstallCount(gameCount);
		((MatrixAppView) pageViews.get(0)).setGameInstallCount(appCount);
	}

	@Override
	public void moveToNextByDiff(int diff) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveToPreviousByDiff(int diff) {
		// TODO Auto-generated method stub
		
	}

}
