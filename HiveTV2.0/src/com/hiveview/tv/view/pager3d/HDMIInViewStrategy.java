package com.hiveview.tv.view.pager3d;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.view.MatrixAppView;
import com.hiveview.tv.view.MatrixBluelightView;
import com.hiveview.tv.view.MatrixCinemaViewNew;
import com.hiveview.tv.view.MatrixCinemaViewNew.OnDataComplet;
import com.hiveview.tv.view.MatrixCinemaViewNew.PageChange;
import com.hiveview.tv.view.MatrixEducationView;
import com.hiveview.tv.view.MatrixGameView;
import com.hiveview.tv.view.MatrixRecommendView;
import com.hiveview.tv.view.MatrixTVForeidgnView;
import com.hiveview.tv.view.MatrixTVView;
import com.hiveview.tv.view.NavigationNewTabView;
import com.hiveview.tv.view.SubTabView;
import com.hiveview.tv.view.pager3d.Abstract3DPagerStrategy.LoadDataTask;
import com.hiveview.tv.view.pager3d.HDMIInNoBlueViewStrategy.EDU;
import com.hiveview.tv.view.pager3d.TabBasePageView.OnDataCompleted;
import com.hiveview.tv.view.pager3d.TabBasePageView.RecommendViewFocusDirectionListener;

@SuppressLint("NewApi")
public class HDMIInViewStrategy extends Abstract3DPagerStrategy {

	/**
	 * @Fields blcakView:黑屏背景
	 */
	private View blackView;
	private MatrixCinemaViewNew cinemaView = null ;
	TabBasePageView tvView = null;
	MatrixAppView appView;
	MatrixRecommendView recommendView;
	MatrixGameView gameView;
	MatrixEducationView educationView;
	private String TAG = "HDMIInViewStrategy";
	/**
	 * @Fields 数据加载完，动态改变首页焦点走向的接口
	 *  @author lhj
	 */
	private OnDataComplet onDataComplet;
	//教育左边推荐位
		private View matrix_education_layout_1;
	//首页右边推荐位
		private View matrix_recommend_layout_1,matrix_recommend_layout_3,matrix_recommend_layout_4;
	public HDMIInViewStrategy(View3DPager _3dPager, OnDataCompleted dataCompleted) {
		super(_3dPager);

		appView = new MatrixAppView(view3dPager.getContext(), new AppFocusDirectioner());
		MatrixBluelightView bluelightView = new MatrixBluelightView(view3dPager.getContext(), new BluelightFocusDirectioner());
		if(null == cinemaView){
			cinemaView = new MatrixCinemaViewNew(view3dPager.getContext(), new CinemaFocusDirectioner());
		}
		recommendView = new MatrixRecommendView(view3dPager.getContext(), new RecommendFocusDirectioner());
		gameView = new MatrixGameView(view3dPager.getContext(), new GameFocusDirectioner());
		 educationView = new MatrixEducationView(view3dPager.getContext(),new EDU());
		/* start by guosongsheng 美国测试使用 */
		
		if (AppConstant.ISDOMESTIC) {
			tvView = new MatrixTVView(view3dPager.getContext(), new TvFocusDirectioner());
		} else {
			Log.d(TAG , "AppConstant.ISDOMESTIC::");
			tvView = new MatrixTVForeidgnView(view3dPager.getContext(), new TvFocusDirectioner());
		}
		/* end by guosongsheng */
		appView.setOnDataCompleted(dataCompleted);
		cinemaView.setOnDataCompleted(dataCompleted);
		bluelightView.setOnDataCompleted(dataCompleted);
		recommendView.setOnDataCompleted(dataCompleted);
		gameView.setOnDataCompleted(dataCompleted);
		tvView.setOnDataCompleted(dataCompleted);
		educationView .setOnDataCompleted(dataCompleted);
		
		cinemaView.setPageChange(new PageChange() {
			
			public void previousPage() {
				view3dPager.moveToPrevious();
			}
			
			public void nextPage() {
				view3dPager.moveToNext();
			}
		});
		// @author lhj
		onDataComplet = new OnDataComplet() {
			
			@Override
			public void getCinemaLayout_6(int l6) {
				// TODO Auto-generated method stub
				matrix_recommend_layout_1.setNextFocusRightId((l6==View.VISIBLE)?R.id.matrix_cinema_layout_6_new_v:R.id.matrix_cinema_layout_6_new_h12);
				matrix_recommend_layout_4.setNextFocusRightId((l6==View.VISIBLE)?R.id.matrix_cinema_layout_6_new_v:R.id.matrix_cinema_layout_6_new_h11);			
			}
			
			@Override
			public void getCinemaLayout_1(int l1) {
				// TODO Auto-generated method stub
				matrix_recommend_layout_3.setNextFocusRightId((l1==View.VISIBLE)?R.id.matrix_cinema_layout_1_new_v:R.id.matrix_cinema_layout_1_new_h1);
				
			}

			@Override
			public void getCinemaLayout_11(int isVisible_11) {
				// TODO Auto-generated method stub
				matrix_education_layout_1.setNextFocusLeftId((isVisible_11==View.VISIBLE)?R.id.matrix_cinema_layout_11_new_v:R.id.matrix_cinema_layout_11_new_h21);
			}

			@Override
			public void getCinemaLayout_12(int isVisible_12) {
				// TODO Auto-generated method stub
				
			}
		};
		appView.setVisibility(View.INVISIBLE);
		cinemaView.setVisibility(View.INVISIBLE);
		bluelightView.setVisibility(View.INVISIBLE);
		tvView.setVisibility(View.INVISIBLE);
		gameView.setVisibility(View.INVISIBLE);
		educationView.setVisibility(View.INVISIBLE);

		view3dPager.addTabPageView(appView, 0);
		view3dPager.addTabPageView(educationView, 1);
		view3dPager.addTabPageView(cinemaView, 2);
		view3dPager.addTabPageView(recommendView, 3);
		view3dPager.addTabPageView(bluelightView, 4);
		view3dPager.addTabPageView(tvView, 5);
		view3dPager.addTabPageView(gameView, 6);
	}
	public void setCinemaOnClick(String value){
		cinemaView.openItemActivity(value);
	}
	
	/* (non-Javadoc)
	 * @see com.hiveview.tv.view.pager3d.Abstract3DPagerStrategy#setUnregister()
	 * 
	 */
	public void setUnregister(){
		tvView.setUnregister();
		appView.setUnregister();
		recommendView.setUnregister();
		gameView.setUnregister();
	}
	
	public void setPageChange(String value){
		cinemaView.viewfliperNext();
	}
	public ArrayList<String> getCinemaName() {
		return cinemaView.getCinemaName();
	}
	@Override
	public void moveToNext() {
		currentIndex = addIndex(currentIndex + 1);// 确定当前动画的下一个View
		int leftIndex1 = addIndex(currentIndex + 1);
		int leftIndex2 = addIndex(currentIndex + 2);
		int leftIndex3 = addIndex(currentIndex + 3);
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
		// 左3
		TabBasePageView leftPageView3 = pageViews.get(leftIndex3);
		animationFactory.get2UnitToNeg2UnitRotateYAnimation(leftPageView3);

		// 右1
		TabBasePageView rightPageView1 = pageViews.get(rightIndex1);
		animationFactory.get0ToUnitRotateAnimation(rightPageView1);

		// 右1
		TabBasePageView rightPageView2 = pageViews.get(rightIndex2);
		animationFactory.getUnitTo2UnitRotateYAnimation(rightPageView2);
		cinemaView.setPageChange();
	}

	@Override
	public void moveToPrevious() {
		currentIndex = subtractIndex(currentIndex - 1);
		int rightIndex1 = subtractIndex(currentIndex - 1);
		int rightIndex2 = subtractIndex(currentIndex - 2);
		int leftIndex1 = addIndex(currentIndex + 1);
		int leftIndex2 = addIndex(currentIndex + 2);
		int leftIndex3 = addIndex(currentIndex + 3);

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

		// 左2
		TabBasePageView leftPageView3 = pageViews.get(leftIndex3);
		animationFactory.getNegUnitToNeg2UnitRotateYAnimation(leftPageView3);
		cinemaView.setPageChange();
	}

	public void setBlackView(View view) {
		this.blackView = view;
	}

	public void initLeftView(int index) {
		/*
		 * TabBasePageView leftPageView1 = pageViews.get(currentIndex - 1);
		 * animationFactory.get0ToUnitImmediatelyAnimation(leftPageView1);
		 * leftPageView1.stopPageViewCustomAnimation();
		 * leftPageView1.setVisibility(View.VISIBLE);
		 * Log.d("PageChangeAnimationFactory", "pageView.isShow():7:" +
		 * leftPageView1.isShown()); TabBasePageView leftPageView2 =
		 * pageViews.get(currentIndex - 2);
		 * animationFactory.get0ToUnit2ImmediatelyRotateYAnimation
		 * (leftPageView2); leftPageView2.stopPageViewCustomAnimation();
		 * leftPageView2.setVisibility(View.VISIBLE); TabBasePageView
		 * leftPageView3 = pageViews.get(currentIndex - 3);
		 * animationFactory.get0ToUnit2ImmediatelyRotateYAnimation
		 * (leftPageView3); leftPageView3.stopPageViewCustomAnimation();
		 * leftPageView3.setVisibility(View.VISIBLE); if (null != blackView) {
		 * blackView.setVisibility(View.GONE);
		 * 
		 * }
		 */
	}

	public void initRightView(int index) {
		/*
		 * TabBasePageView rightPageView1 = pageViews.get(currentIndex + 1);
		 * animationFactory
		 * .get0ToNegUnitImmediatelyRotateAnimation(rightPageView1);
		 * rightPageView1.stopPageViewCustomAnimation();
		 * rightPageView1.setVisibility(View.VISIBLE);
		 * 
		 * TabBasePageView rightPageView2 = pageViews.get(currentIndex + 2);
		 * animationFactory
		 * .get0ToNegUnit2ImmediatelyRotateAnimation(rightPageView2);
		 * rightPageView2.stopPageViewCustomAnimation();
		 * rightPageView2.setVisibility(View.VISIBLE); if (null != blackView) {
		 * blackView.setVisibility(View.GONE); }
		 */

	}

	@Override
	public void loadPageData(boolean isRefleshData) {
		// pageViews.get(2).loadData(isRefleshData);//
		// pageViews.get(0).loadData(isRefleshData);// 载入应用页面的数据
		// pageViews.get(3).loadData(isRefleshData);// 载入电视页面的数据
		// pageViews.get(1).loadData(isRefleshData);// 载入影院页面的数据
		// pageViews.get(4).loadData(isRefleshData);// 载入游戏页面的数据
		/* 首页 */
		new LoadDataTask().execute(3, isRefleshData);
		/* 极清 */
		new LoadDataTask().execute(4, isRefleshData);
		/* 影院 */
		new LoadDataTask().execute(2, isRefleshData);
		/* 电视 */
		new LoadDataTask().execute(5, isRefleshData);
		/* 应用 */
		new LoadDataTask().execute(0, isRefleshData);
		/* 游戏 */
		new LoadDataTask().execute(6, isRefleshData);
		/* 教育 */
		new LoadDataTask().execute(1, isRefleshData);

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
				if (!AppConstant.ISDOMESTIC) {
					views[1].setNextFocusRightId(R.id.matrix_tv_layout_0);
					views[2].setNextFocusRightId(R.id.matrix_tv_layout_1);
					views[3].setNextFocusRightId(R.id.matrix_tv_layout_2);
				} else {
					views[1].setNextFocusRightId(R.id.matrix_tv_layout_3_f);
					views[2].setNextFocusRightId(R.id.matrix_tv_layout_3_f);
					views[3].setNextFocusRightId(R.id.matrix_tv_layout_3_f);
				}
			}
		}

		@Override
		public void setButtomMenuViewFocusDirection(View[] views) {

			// 外接媒体
			View gameRecordLayout = views[0];
			gameRecordLayout.setNextFocusLeftId(R.id.sub_navigation_common_app_external_text_layout);

			// 观看记录
			View gameExternalLayout = views[views.length - 1];
			if (AppConstant.ISDOMESTIC) {
				gameExternalLayout.setNextFocusRightId(R.id.sub_navigation_common_tv_record_text_layout);
			} else {
				gameExternalLayout.setNextFocusRightId(R.id.sub_navigation_tv_direction_text_layout);
			}
			if (AppConstant.ISDOMESTIC) {
			views[1].setNextFocusRightId(R.id.matrix_tv_layout_0);
			views[2].setNextFocusRightId(R.id.matrix_tv_layout_1);
			views[3].setNextFocusRightId(R.id.matrix_tv_layout_2);
			}else{
				views[1].setNextFocusRightId(R.id.matrix_tv_layout_3_f);
				views[2].setNextFocusRightId(R.id.matrix_tv_layout_3_f);
				views[3].setNextFocusRightId(R.id.matrix_tv_layout_3_f);
			}
		}

		@Override
		public void setTopMenuViewFocusDirection(View topTabView) {
			// topTabView.setNextFocusRightId(R.id.navigation_tab_tv_text);
		}

	}

	class TvFocusDirectioner implements RecommendViewFocusDirectionListener {

		@Override
		public void setRecommendViewsFocusDirection(View[] views) {
			if(null != views){
				if (AppConstant.ISDOMESTIC) {
				if(null != views[3]){
					views[3].setNextFocusRightId(R.id.matrix_bluelight_layout_0);
				}
				if(null != views[4]){
					views[4].setNextFocusRightId(R.id.matrix_bluelight_layout_0);
				}
				if(null != views[5]){
					views[5].setNextFocusRightId(R.id.matrix_bluelight_layout_2);
				}
				}else{
					views[0].setNextFocusRightId(R.id.matrix_bluelight_layout_0);
					views[0].setNextFocusLeftId(R.id.matrix_game_layout_4);
				}
			}
		}

		@Override
		public void setButtomMenuViewFocusDirection(View[] views) {
			// 分类选台
			View categoryView = views[0];
			categoryView.setNextFocusLeftId(R.id.sub_navigation_common_game_external_text_layout);

			if (!AppConstant.ISDOMESTIC) {
				/* start by guosongsheng 机顶盒设置 */
			/*	View settingView = views[1];
				settingView.setNextFocusLeftId(R.id.sub_navigation_recommend_user_text_layout);
				settingView.setNextFocusRightId(R.id.sub_navigation_recommend_record_text_layout);*/
				/* end by guosongsheng */
			} else {
				// 直播提醒
				View notificationView = views[views.length - 1];
				notificationView.setNextFocusRightId(R.id.sub_navigation_common_bluelight_record_text_layout);
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
			// 外接媒体
//			View appRecordLayout = views[0];
//			appRecordLayout.setNextFocusLeftId(R.id.sub_navigation_common_cinema_external_text_layout);
//
//			// 观看记录
//			View appExternalLayout = views[views.length - 1];
//			appExternalLayout.setNextFocusRightId(R.id.sub_navigation_common_game_record_text_layout);

		}

		@Override
		public void setTopMenuViewFocusDirection(View topTabView) {

		}

	}

	class RecommendFocusDirectioner implements RecommendViewFocusDirectionListener {

		@Override
		public void setRecommendViewsFocusDirection(View[] views) {
/*			if (null != views) {
				if (AppConstant.ISFOREIDGN) {
					views[0].setNextFocusLeftId(R.id.matrix_tv_layout_6);
					views[3].setNextFocusLeftId(R.id.matrix_tv_layout_7);
				} else {
					views[0].setNextFocusLeftId(R.id.matrix_tv_layout_3);
					views[3].setNextFocusLeftId(R.id.matrix_tv_layout_3);
				}
			}*/
		}

		@Override
		public void setButtomMenuViewFocusDirection(View[] views) {
			// 观看记录
			View recommendFavouriteLayout = views[0];
			recommendFavouriteLayout.setNextFocusLeftId(R.id.sub_navigation_common_bluelight_external_text_layout);

			View recommendExternalLayout = views[3];
			// 外接存储
			recommendExternalLayout.setNextFocusRightId(R.id.sub_navigation_common_cinema_record_text_layout);
			//首页左侧向左焦点运动
			views[1].setNextFocusLeftId(R.id.matrix_bluelight_layout_5);
			views[2].setNextFocusLeftId(R.id.matrix_bluelight_layout_5);
			matrix_recommend_layout_1=	views[4];
			matrix_recommend_layout_3=	views[5];
			matrix_recommend_layout_4=	views[6];
			cinemaView.getCinemaLayout(onDataComplet);
		}

		@Override
		public void setTopMenuViewFocusDirection(View topTabView) {
		}

	}

	class CinemaFocusDirectioner implements RecommendViewFocusDirectionListener {

		@Override
		public void setRecommendViewsFocusDirection(View[] views) {
		}

		@Override
		public void setButtomMenuViewFocusDirection(View[] views) {

			/*
			 * // 搜索视频 View movieSearchLayout = views[0];
			 * movieSearchLayout.setNextFocusLeftId
			 * (R.id.sub_navigation_recommend_setting_text_layout);
			 * 
			 * //影片收藏 View movieFavouriteLayout = views[2];
			 * movieFavouriteLayout.
			 * setNextFocusRightId(R.id.sub_navigation_app_external_text_layout
			 * );
			 */
			// 外接媒体
			View movieRecordLayout = views[0];
			movieRecordLayout.setNextFocusLeftId(R.id.sub_navigation_common_recommend_external_text_layout);

			// 观看记录
			View movieExternalLayout = views[views.length - 1];
			movieExternalLayout.setNextFocusRightId(R.id.sub_navigation_common_app_record_text_layout);
		}

		@Override
		public void setTopMenuViewFocusDirection(View topTabView) {

		}

	}

	class BluelightFocusDirectioner implements RecommendViewFocusDirectionListener {

		@Override
		public void setRecommendViewsFocusDirection(View[] views) {
			if (null != views) {
				/*if (AppConstant.ISDOMESTIC) {
					views[0].setNextFocusLeftId(R.id.matrix_tv_layout_6);
					views[1].setNextFocusLeftId(R.id.matrix_tv_layout_7);
				} else {
					views[0].setNextFocusLeftId(R.id.matrix_tv_layout_3_f);
					views[1].setNextFocusLeftId(R.id.matrix_tv_layout_3_f);
				}*/
			}
		}

		@Override
		public void setButtomMenuViewFocusDirection(View[] views) {

			// 外接媒体
			View bluelightRecordLayout = views[0];

			if (AppConstant.ISDOMESTIC) {
				bluelightRecordLayout.setNextFocusLeftId(R.id.sub_navigation_common_tv_external_text_layout);
			} else {
				// 外接媒体
				bluelightRecordLayout.setNextFocusLeftId(R.id.sub_navigation_tv_notification_text_layout);
			}
			// 影片收藏
			View bluelightExternalLayout = views[views.length - 1];
			bluelightExternalLayout.setNextFocusRightId(R.id.sub_navigation_common_recommend_favourite_text_layout);
			if (AppConstant.ISDOMESTIC) {
				views[1].setNextFocusLeftId(R.id.matrix_tv_layout_6);
				views[2].setNextFocusLeftId(R.id.matrix_tv_layout_8);
			} else {
				views[1].setNextFocusLeftId(R.id.matrix_tv_layout_3_f);
				views[2].setNextFocusLeftId(R.id.matrix_tv_layout_3_f);
			}
		}

		@Override
		public void setTopMenuViewFocusDirection(View topTabView) {

		}

	}

	@Override
	public void setInstallApkCount(int gameCount, int appCount) {
		((MatrixRecommendView) pageViews.get(3)).setAppInstallCount(appCount+gameCount);

	}

	class EDU implements RecommendViewFocusDirectionListener{

		@Override
		public void setRecommendViewsFocusDirection(View[] views) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setButtomMenuViewFocusDirection(View[] views) {
			// TODO Auto-generated method stub
			matrix_education_layout_1=views[0];
		}

		@Override
		public void setTopMenuViewFocusDirection(View topTabView) {
			// TODO Auto-generated method stub
			
		}
		
	}
	@Override
	public void moveToNextByDiff(int diff) {
		currentIndex = addIndex(currentIndex + diff);// 确定当前动画的下一个View
		int leftIndex1 = addIndex(currentIndex + 1);
		int leftIndex2 = addIndex(currentIndex + 2);
		int leftIndex3 = addIndex(currentIndex + 3);
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
		// 左3
		TabBasePageView leftPageView3 = pageViews.get(leftIndex3);
		animationFactory.get2UnitToNeg2UnitRotateYAnimation(leftPageView3);

		// 右1
		TabBasePageView rightPageView1 = pageViews.get(rightIndex1);
		animationFactory.get0ToUnitRotateAnimation(rightPageView1);

		// 右1
		TabBasePageView rightPageView2 = pageViews.get(rightIndex2);
		animationFactory.getUnitTo2UnitRotateYAnimation(rightPageView2);
		cinemaView.setPageChange();
	}
	@Override
	public void moveToPreviousByDiff(int diff) {
		currentIndex = subtractIndex(currentIndex - diff);
		int rightIndex1 = subtractIndex(currentIndex - 1);
		int rightIndex2 = subtractIndex(currentIndex - 2);
		int leftIndex1 = addIndex(currentIndex + 1);
		int leftIndex2 = addIndex(currentIndex + 2);
		int leftIndex3 = addIndex(currentIndex + 3);

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

		// 左2
		TabBasePageView leftPageView3 = pageViews.get(leftIndex3);
		animationFactory.getNegUnitToNeg2UnitRotateYAnimation(leftPageView3);
		cinemaView.setPageChange();
	}
}
