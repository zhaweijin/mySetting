package com.hiveview.tv.view.pager3d;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.hiveview.tv.R;
import com.hiveview.tv.view.MatrixAppView;
import com.hiveview.tv.view.MatrixCinemaView;
import com.hiveview.tv.view.MatrixCinemaViewNew;
import com.hiveview.tv.view.MatrixGameView;
import com.hiveview.tv.view.MatrixMovieView;
import com.hiveview.tv.view.MatrixRecommendView;
import com.hiveview.tv.view.NavigationNewTabView;
import com.hiveview.tv.view.SubTabView;
import com.hiveview.tv.view.pager3d.FivePageViewStrategy.MovieFocusDirectioner;
import com.hiveview.tv.view.pager3d.TabBasePageView.OnDataCompleted;
import com.hiveview.tv.view.pager3d.TabBasePageView.RecommendViewFocusDirectionListener;

@SuppressLint("NewApi")
public class FourPageViewStrategy extends Abstract3DPagerStrategy {

	private View blackView;

	public FourPageViewStrategy(View3DPager _3dPager, OnDataCompleted dataCompleted) {
		super(_3dPager);
		MatrixAppView appView = new MatrixAppView(view3dPager.getContext(), new AppFocusDirectioner());
//		MatrixMovieView movieView = new MatrixMovieView(view3dPager.getContext(), new MovieFocusDirectioner());
		MatrixRecommendView recommendView = new MatrixRecommendView(view3dPager.getContext(), new RecommendFocusDirectioner());
		MatrixGameView gameView = new MatrixGameView(view3dPager.getContext(), new GameFocusDirectioner());
		MatrixCinemaViewNew movieView = new MatrixCinemaViewNew(view3dPager.getContext(), new MovieFocusDirectioner());
		appView.setOnDataCompleted(dataCompleted);
		movieView.setOnDataCompleted(dataCompleted);
		recommendView.setOnDataCompleted(dataCompleted);
		gameView.setOnDataCompleted(dataCompleted);

		view3dPager.addTabPageView(appView, 0);
		view3dPager.addTabPageView(movieView, 1);
		view3dPager.addTabPageView(recommendView, 2);
		view3dPager.addTabPageView(gameView, 3);
	}

	public void setBlackView(View view) {
		this.blackView = view;
	}

	@Override
	public void moveToNext() {
		currentIndex = addIndex(currentIndex + 1);// 确定当前动画的下一个View
		int leftIndex1 = addIndex(currentIndex + 1);
		int rightIndex1 = subtractIndex(currentIndex - 1);
		int rightIndex2 = subtractIndex(currentIndex - 2);

		// if(pageViewChangeListener)

		// 当前View的动画偏转
		TabBasePageView curPageView = pageViews.get(currentIndex);
		animationFactory.getNegUnitTo0RotateAnimation(curPageView);

		// 左1
		TabBasePageView leftPageView1 = pageViews.get(leftIndex1);
		animationFactory.get2UnitToNeg2UnitRotateYAnimation(leftPageView1);

		// 右1
		TabBasePageView rightPageView1 = pageViews.get(rightIndex1);
		animationFactory.get0ToUnitRotateAnimation(rightPageView1);

		// 右1
		TabBasePageView rightPageView2 = pageViews.get(rightIndex2);
		animationFactory.getUnitTo2UnitRotateYAnimation(rightPageView2);

		animationFactory.getNeg2UnitToNegUnitRotateYAnimation(leftPageView1);

	}

	@Override
	public void moveToPrevious() {
		currentIndex = subtractIndex(currentIndex - 1);
		int rightIndex1 = subtractIndex(currentIndex - 1);
		int rightIndex2 = subtractIndex(currentIndex - 2);
		int leftIndex1 = addIndex(currentIndex + 1);

		// 当前View的动画偏转
		TabBasePageView curPageView = pageViews.get(currentIndex);
		animationFactory.getUnitTo0RotateAnimation(curPageView);

		// 右1
		TabBasePageView rightPageView1 = pageViews.get(rightIndex1);
		animationFactory.get2UnitToUnitRotateYAnimation(rightPageView1);

		// 右2
		final TabBasePageView rightPageView2 = pageViews.get(rightIndex2);
		animationFactory.getNegUnitToNeg2UnitRotateYAnimation(rightPageView2, new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				animationFactory.getNeg2UnitTo2UnitRotateYAnimation(rightPageView2);
			}
		});

		// 左1
		TabBasePageView leftPageView1 = pageViews.get(leftIndex1);
		animationFactory.get0ToNegUnitRotateAnimation(leftPageView1);

	}

	@Override
	public void initLeftView(int index) {
		TabBasePageView leftPageView1 = pageViews.get(currentIndex - 1);
		animationFactory.get0ToUnitImmediatelyAnimation(leftPageView1);
		leftPageView1.stopPageViewCustomAnimation();

		TabBasePageView leftPageView2 = pageViews.get(currentIndex - 2);
		animationFactory.get0ToUnit2ImmediatelyRotateYAnimation(leftPageView2);
		leftPageView2.stopPageViewCustomAnimation();
		if (null != blackView) {
			blackView.setVisibility(View.GONE);
		}
	}

	@Override
	public void initRightView(int index) {
		TabBasePageView rightPageView1 = pageViews.get(currentIndex + 1);
		animationFactory.get0ToNegUnitImmediatelyRotateAnimation(rightPageView1);
		rightPageView1.stopPageViewCustomAnimation();
		if (null != blackView) {
			blackView.setVisibility(View.GONE);
		}
	}

	@Override
	public void loadPageData(boolean isRefleshData) {
		// pageViews.get(2).loadData(isRefleshData);// 载入推荐页面的数据
		// pageViews.get(3).loadData(true);// 载入游戏页面的数据
		// pageViews.get(1).loadData(true);// 载入影院页面的数据
		// pageViews.get(0).loadData(true);// 载入应用页面的数据

		new LoadDataTask().execute(2, isRefleshData);
		new LoadDataTask().execute(3, true);
		new LoadDataTask().execute(1, true);
		new LoadDataTask().execute(0, true);
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
				views[2].setNextFocusRightId(R.id.matrix_recommend_layout_0);
				views[3].setNextFocusRightId(R.id.matrix_recommend_layout_0);
			}
		}

		@Override
		public void setButtomMenuViewFocusDirection(View[] views) {
			// View gameManagerView = views[0];
			// 管理中心
			View gameManagerView = views[0];
			// 外接存储
			View gameUserView = views[1];
			gameManagerView.setNextFocusRightId(R.id.sub_navigation_recommend_record_text_layout);
			gameUserView.setNextFocusLeftId(R.id.sub_navigation_app_setting_text_layout);
			// gameManagerView.setNextFocusRightId(R.id.sub_navigation_recommend_record_text_layout);
			// gameManagerView.setNextFocusLeftId(R.id.sub_navigation_app_setting_text_layout);
		}

		@Override
		public void setTopMenuViewFocusDirection(View topTabView) {
			// topTabView.setNextFocusRightId(R.id.navigation_tab_recommend_text);
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
				views[0].setNextFocusLeftId(R.id.matrix_game_layout_6);
				//views[4].setNextFocusLeftId(R.id.matrix_game_layout_7);
			}
		}

		@Override
		public void setButtomMenuViewFocusDirection(View[] views) {
			// 观看记录
			View recommendRecordLayout = views[1];
			recommendRecordLayout.setNextFocusLeftId(R.id.sub_navigation_game_setting_text_layout);
			// 系统设置
			View recommendSetting = views[3];
			recommendSetting.setNextFocusRightId(R.id.sub_navigation_movie_search_text_layout);
		}

		@Override
		public void setTopMenuViewFocusDirection(View topTabView) {
			// topTabView.setNextFocusLeftId(R.id.navigation_tab_game_text);
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

		}

	}

	@Override
	public void setInstallApkCount(int gameCount, int appCount) {
		((MatrixGameView) pageViews.get(3)).setGameInstallCount(gameCount);
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
