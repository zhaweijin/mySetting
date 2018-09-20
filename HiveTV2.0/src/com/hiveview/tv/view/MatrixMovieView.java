package com.hiveview.tv.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.BaseActivity;
import com.hiveview.tv.activity.CollectActivity;
import com.hiveview.tv.activity.NewChannelActivity;
import com.hiveview.tv.activity.PlayerRecordActivity;
import com.hiveview.tv.activity.SearchHomeActivity;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.content.ContentInvoker;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.ItemType;
import com.hiveview.tv.common.statistics.SimpleOnClickListener;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.service.VideoService;
import com.hiveview.tv.service.dao.BaseDAO;
import com.hiveview.tv.service.dao.RecommendDAO;
import com.hiveview.tv.service.entity.RecommendEntity;
import com.hiveview.tv.utils.AnimationUtil;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.view.pager3d.TabBasePageView;
import com.hiveview.tv.view.pager3d.ViewFlipperInAnimationListener;

public class MatrixMovieView extends TabBasePageView {

	protected static final String TAG = "MatrixMovieView";

	/**
	 * 影院页面的推荐位Layout
	 */
	private View matrix_movie_layout_0, matrix_movie_layout_1, matrix_movie_layout_2, matrix_movie_layout_3, matrix_movie_layout_4,
			matrix_movie_layout_5;

	/**
	 * 推荐位轮播的ViewFlipper
	 */
	private ViewFlipper movie_flipper_0, movie_flipper_1, movie_flipper_2, movie_flipper_3, movie_flipper_4, movie_flipper_5;

	/**
	 * 显示推荐位文本信息的TextView
	 */
	private TextView matrix_movie_flipper_0_text_view, matrix_movie_flipper_1_text_view, matrix_movie_flipper_2_text_view,
			matrix_movie_flipper_3_text_view, matrix_movie_flipper_4_text_view, matrix_movie_flipper_5_text_view;

	/**
	 * 显示推荐位文本信息的TextView数组
	 */
	private TextView[] desViews;

	private ArrayList<ArrayList<RecommendEntity>> movieList = null;

	private RecommendDAO recommendDAO = null;

	private final int LOAD_DATA_SUCCESS = 100;

	private final int LOAD_DATA_FAIL = -100;
	/**
	 * 是否直接播放
	 * 
	 * @Fields isPlayer
	 */
	public static boolean isPlayer = false;

	/**
	 * 影院页面的推荐位Layout数组
	 */
	private View[] recommendLayouts;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		};
	};

	public MatrixMovieView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MatrixMovieView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MatrixMovieView(Context context) {
		super(context);
		init();
	}

	public MatrixMovieView(Context context, RecommendViewFocusDirectionListener listener) {
		super(context, listener);
		init();
	}

	/**
	 * 推荐位相关View初始化
	 */
	protected void init() {
		recommendDAO = new RecommendDAO(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		View view = inflate(getContext(), R.layout.matrix_movie_view, null);

		this.addView(view, params);

		// 初始化推荐位Layout初始化
		matrix_movie_layout_0 = findViewById(R.id.matrix_movie_layout_0);
		matrix_movie_layout_1 = findViewById(R.id.matrix_movie_layout_1);
		matrix_movie_layout_2 = findViewById(R.id.matrix_movie_layout_2);
		matrix_movie_layout_3 = findViewById(R.id.matrix_movie_layout_3);
		matrix_movie_layout_4 = findViewById(R.id.matrix_movie_layout_4);
		matrix_movie_layout_5 = findViewById(R.id.matrix_movie_layout_5);

		// 初始化推荐位轮播的ViewFlipper
		movie_flipper_0 = (ViewFlipper) findViewById(R.id.matrix_movie_flipper_0);
		movie_flipper_1 = (ViewFlipper) findViewById(R.id.matrix_movie_flipper_1);
		movie_flipper_2 = (ViewFlipper) findViewById(R.id.matrix_movie_flipper_2);
		movie_flipper_3 = (ViewFlipper) findViewById(R.id.matrix_movie_flipper_3);
		movie_flipper_4 = (ViewFlipper) findViewById(R.id.matrix_movie_flipper_4);
		movie_flipper_5 = (ViewFlipper) findViewById(R.id.matrix_movie_flipper_5);

		// 初始化显示推荐位文本的TextView
		matrix_movie_flipper_0_text_view = (TextView) findViewById(R.id.matrix_movie_flipper_0_text_view);
		matrix_movie_flipper_1_text_view = (TextView) findViewById(R.id.matrix_movie_flipper_1_text_view);
		matrix_movie_flipper_2_text_view = (TextView) findViewById(R.id.matrix_movie_flipper_2_text_view);
		matrix_movie_flipper_3_text_view = (TextView) findViewById(R.id.matrix_movie_flipper_3_text_view);
		matrix_movie_flipper_4_text_view = (TextView) findViewById(R.id.matrix_movie_flipper_4_text_view);
		matrix_movie_flipper_5_text_view = (TextView) findViewById(R.id.matrix_movie_flipper_5_text_view);

		// 推荐0位置的焦点定位
		matrix_movie_layout_0.setNextFocusUpId(R.id.navigation_tab_movie_text);
		matrix_movie_layout_0.setNextFocusRightId(R.id.matrix_movie_layout_1);
		matrix_movie_layout_0.setNextFocusLeftId(R.id.matrix_recommend_layout_3);
		matrix_movie_layout_0.setNextFocusDownId(R.id.sub_navigation_movie_search_text_layout);

		// 推荐1位置的焦点定位
		matrix_movie_layout_1.setNextFocusUpId(R.id.navigation_tab_movie_text);
		matrix_movie_layout_1.setNextFocusRightId(R.id.matrix_movie_layout_2);
		matrix_movie_layout_1.setNextFocusLeftId(R.id.matrix_movie_layout_0);
		matrix_movie_layout_1.setNextFocusDownId(R.id.matrix_movie_layout_3);

		// 推荐2位置的焦点定位
		matrix_movie_layout_2.setNextFocusUpId(R.id.navigation_tab_movie_text);
		matrix_movie_layout_2.setNextFocusRightId(R.id.matrix_app_layout_0);
		matrix_movie_layout_2.setNextFocusLeftId(R.id.matrix_movie_layout_1);
		matrix_movie_layout_2.setNextFocusDownId(R.id.matrix_movie_layout_5);

		// 推荐3位置的焦点定位
		matrix_movie_layout_3.setNextFocusUpId(R.id.matrix_movie_layout_1);
		matrix_movie_layout_3.setNextFocusRightId(R.id.matrix_movie_layout_4);
		matrix_movie_layout_3.setNextFocusLeftId(R.id.matrix_movie_layout_0);
		matrix_movie_layout_3.setNextFocusDownId(R.id.sub_navigation_movie_channel_text_layout);

		// 推荐4位置的焦点定位
		matrix_movie_layout_4.setNextFocusUpId(R.id.matrix_movie_layout_1);
		matrix_movie_layout_4.setNextFocusRightId(R.id.matrix_movie_layout_5);
		matrix_movie_layout_4.setNextFocusLeftId(R.id.matrix_movie_layout_3);
		matrix_movie_layout_4.setNextFocusDownId(R.id.sub_navigation_movie_channel_text_layout);

		// 推荐5位置的焦点定位
		matrix_movie_layout_5.setNextFocusUpId(R.id.matrix_movie_layout_2);
		matrix_movie_layout_5.setNextFocusRightId(R.id.matrix_app_layout_5);
		matrix_movie_layout_5.setNextFocusLeftId(R.id.matrix_movie_layout_4);
		matrix_movie_layout_5.setNextFocusDownId(R.id.sub_navigation_movie_favourite_text_layout);

		// 初始化 推荐位轮播的ViewFlipper数组
		flippers = new ViewFlipper[] { movie_flipper_0, movie_flipper_1, movie_flipper_2, movie_flipper_3, movie_flipper_4, movie_flipper_5 };

		recommendEdgeViews = new View[] { matrix_movie_layout_0, matrix_movie_layout_2, matrix_movie_layout_5 };

		// ViewFlipper 轮播动画

		for (int i = 0; i < flippers.length; i++) {
			Animation inAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.down_in);
			Animation outAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.down_out);
			inAnimation.setAnimationListener(new ViewFlipperInAnimationListener(flippers[i], i) {

				@Override
				public void onStartInAnimation(ViewFlipper viewFlipper, int viewFlipperIndexInPage) {
					RecommendEntity entity = (RecommendEntity) flippers[viewFlipperIndexInPage].getCurrentView().getTag();
					if (TextUtils.isEmpty(entity.getContentDesc())) {
						desViews[viewFlipperIndexInPage].setVisibility(View.INVISIBLE);
					} else {
						if (viewFlipperIndexInPage != 3) {
							desViews[viewFlipperIndexInPage].setVisibility(View.VISIBLE);
							desViews[viewFlipperIndexInPage].setText(entity.getContentDesc());
						}
					}
				}

				@Override
				public void onRepeatInAnimation(ViewFlipper viewFlipper, int viewFlipperIndexInPage) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onEndInAnimation(ViewFlipper viewFlipper, int viewFlipperIndexInPage) {
					// TODO Auto-generated method stub

				}

			});
			flippers[i].setInAnimation(inAnimation);
			flippers[i].setOutAnimation(outAnimation);
		}

		// 初始化显示推荐位文本的TextView数组
		desViews = new TextView[] { matrix_movie_flipper_0_text_view, matrix_movie_flipper_1_text_view, matrix_movie_flipper_2_text_view,
				matrix_movie_flipper_3_text_view, matrix_movie_flipper_4_text_view, matrix_movie_flipper_5_text_view };

		// 初始化推荐位Layout数组
		recommendLayouts = new View[] { matrix_movie_layout_0, matrix_movie_layout_1, matrix_movie_layout_2, matrix_movie_layout_3,
				matrix_movie_layout_4, matrix_movie_layout_5 };

		// 推荐位绑定焦点改变事件和焦点改变事件
		for (int i = 0; i < recommendLayouts.length; i++) {
			recommendLayouts[i].setOnFocusChangeListener(recommendLayoutFocusListener);
			// recommendLayouts[i].setOnClickListener(recommendLayoutClickListener);RecommendLayoutClickListener
			recommendLayouts[i].setOnClickListener(new RecommendLayoutClickListener());
		}
		hashMap.put(matrix_movie_layout_0, "4401");
		hashMap.put(matrix_movie_layout_1, "4402");
		hashMap.put(matrix_movie_layout_2, "4403");
		hashMap.put(matrix_movie_layout_3, "4404");
		hashMap.put(matrix_movie_layout_4, "4405");
		hashMap.put(matrix_movie_layout_5, "4406");
		super.init();

	}

	// by zhangpengzhan
	HashMap<View, String> hashMap = new HashMap<View, String>();

	/**
	 * 响应点击事件 点击后进入 详情页
	 */
	class RecommendLayoutClickListener extends SimpleOnClickListener {

		@Override
		public DataHolder doOnClick(View view) {
			int index = -1;
			for (int i = 0; i < recommendLayouts.length; i++) {
				if (view == recommendLayouts[i]) {
					index = i;
				}
			}

			if (index == 3) {// 打开蓝光极清应用
				AppUtil.openApp("com.hiveview.movie", getContext());
				return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_GAME_BUTTON)
						.setButton(String.valueOf(view.getId()), "蓝光极清").setSenceName(AppScene.getScene()).setSrcType(ItemType.BUTTON)
						.setViewPosition("4404").setTabNo(Tab.FILM).setViewPosition(null != hashMap.get(view) ? hashMap.get(view).toString() : "")
						.build();
			}

			if (flippers[index].getCurrentView() == null || flippers[index].getChildCount() == 0) {
				return null;
			}

			// 获取推荐位绑定的数据
			RecommendEntity entity = (RecommendEntity) flippers[index].getCurrentView().getTag();

			if (null == entity)
				return null;
			// 获取当前数据类型对应的详情Activity的Action
			String action = ContentInvoker.getInstance().getContentAction(entity.getFocusType());

			if (!TextUtils.isEmpty(action)) {

				if (action.equals(ContentInvoker.CONTENT_ACTION_BLUE)) {// 蓝光极清类型的视频
					Intent intent = new Intent(action);
					intent.putExtra("videoset_id", entity.getContentId());
					getContext().sendBroadcast(intent);
				} else if (action.equals(ContentInvoker.CONTENT_ACTION_BLUE_SUBJECT_DETAIL)) {// 极清专题详情
					Intent intent = new Intent(action);
					intent.putExtra("subject_id", entity.getContentId());
					getContext().sendBroadcast(intent);
				} else {// 奇艺，搜狐的数据并且有详情页视频，如电影，电视剧，综艺，动漫
					Intent intent = new Intent(action);
					intent.putExtra("id", entity.getContentId());
					intent.putExtra("type", entity.getContentType());
					getContext().startActivity(intent);
				}

			} else {// 无详情页的视频（如：音乐，体育，片花，旅游记录片等），点击直接播放就行
				Intent intent = new Intent(getContext(), VideoService.class);
				intent.putExtra("id", entity.getContentId());
				intent.putExtra("type", entity.getContentType());
				getContext().startService(intent);
				BaseActivity.player_Entity = entity;
				isPlayer = true;
			}

			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_RECOMMEND).setEntity(entity).setSenceName(AppScene.getScene())
					.setSrcType(ItemType.VIDEO).setTabNo(Tab.FILM).setViewPosition(null != hashMap.get(view) ? hashMap.get(view).toString() : "")
					.build();
		}

	}

	/**
	 * 推荐位的焦点变化监听
	 */
	private OnFocusChangeListener recommendLayoutFocusListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View view, boolean isFocused) {

			if (isFocused) {

				view.bringToFront();

				if (view.getId() == R.id.matrix_movie_layout_0) {// 第0个位置的推荐位，可露头的推荐位，动画要特殊处理
					findViewById(R.id.matrix_movie_flipper_0_out_layout).bringToFront();
					AnimationUtil.loadBigAnimation(findViewById(R.id.matrix_movie_flipper_0_out_layout));
				} else if (view.getId() == R.id.matrix_movie_layout_2) {// 第2个位置的推荐位，可露头的推荐位，动画要特殊处理
					findViewById(R.id.matrix_movie_flipper_2_out_layout).bringToFront();
					AnimationUtil.loadBigAnimation(findViewById(R.id.matrix_movie_flipper_2_out_layout));
				}

				// 一般的轮播推荐位获得焦点的动画效果
				AnimationUtil.loadBigAnimation(view);
				AlphaAnimation alphaAnim = new AlphaAnimation(0.9f, 1.0f);
				alphaAnim.setDuration(100);
				alphaAnim.setInterpolator(new LinearInterpolator());
				view.startAnimation(alphaAnim);

			} else {

				// 一般的轮播推荐位失去焦点的动画效果
				AnimationUtil.loadSmallAnimation(view);

				if (view.getId() == R.id.matrix_movie_layout_0) {// 第0个位置露头轮播推荐位失去焦点的动画效果

					AnimationUtil.loadSmallAnimation(findViewById(R.id.matrix_movie_flipper_0_out_layout));

				} else if (view.getId() == R.id.matrix_movie_layout_2) {// 第2个位置露头轮播推荐位失去焦点的动画效果

					AnimationUtil.loadSmallAnimation(findViewById(R.id.matrix_movie_flipper_2_out_layout));

				}

			}

		}
	};

	@Override
	protected void processData(int msgWhat) {
		switch (msgWhat) {
		case LOAD_DATA_SUCCESS:
			setMatrixData();
			break;
		case LOAD_DATA_FAIL:

			break;
		default:
			break;
		}
	}

	@Override
	public void loadData(boolean isRefleshData) {
		int count = 0;
		do {
			movieList = recommendDAO.queryMatrix(null, null, null, null);
			if (movieList.get(movieList.size() - 1).size() > 0 || count == readDataFromDBCount) {
				break;
			}
			count++;
			try {
				Thread.sleep(readDataFromDBInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (movieList.get(movieList.size() - 1).size() == 0);

		if (null != movieList && movieList.get(movieList.size() - 1).size() > 0) {
			checkPageIsIdle();
		} else {
			sendLoadDataResultMessage(LOAD_DATA_FAIL);
		}

	}

	/**
	 * 设置影院上的推荐位数据
	 * 
	 * @param movieList
	 */
	public void setMatrixData() {

		int count = movieList.size();

		android.view.ViewGroup.LayoutParams params = new android.view.ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		for (int i = 0; i < count; i++) {
			ArrayList<RecommendEntity> subList = movieList.get(i);// 获取每个推荐位上的数据列表
			int dataSize = subList.size();
			int viewSize = flippers[i].getChildCount();
			if (dataSize > viewSize) {// 当推荐位数据增加时，补充显示推荐位的View
				Log.d(TAG, "movie pageview " + i + " position recommend data add");
				for (int j = 0; j < dataSize - viewSize; j++) {
					ImageView image = new ImageView(getContext());
					flippers[i].addView(image, params);
					Log.d(TAG, "movie pageview " + i + " position childs count " + flippers[i].getChildCount());
				}
			} else if (dataSize < viewSize) {// 当推荐位数据减少时，减少显示推荐位的View
				Log.d(TAG, "movie pageview " + i + " position recommend data reduce");
				for (int j = dataSize; j < viewSize; j++) {
					if (null != flippers[i] && flippers[i].getChildCount() > 0) {
						Log.d(TAG, "movie pageview " + i + " position childs count " + flippers[i].getChildCount());
						flippers[i].removeViewAt(j);
					}
				}
			}

			// 改变推荐位的轮播状态
			if (subList.size() > 0) {
				changeFlipperFlipping(subList.size(), flippers[i], subList.get(0).getIntervalTime());
			}

			for (int k = 0; k < dataSize; k++) {// 填充每个推荐位位置的ViewFliper
				RecommendEntity newEntity = subList.get(k);
				RecommendEntity oldEntity = null;
				ImageView image = (ImageView) flippers[i].getChildAt(k);
				if (null != image.getTag()) {
					oldEntity = (RecommendEntity) image.getTag();
				}

				if (null == newEntity.getFocusThumbImg()) {
					continue;
				}

				if (null == oldEntity || oldEntity.getContentId() != newEntity.getContentId()
						|| !oldEntity.getFocusThumbImg().equals(newEntity.getFocusThumbImg())) {
					image.setTag(newEntity);
					image.setScaleType(ScaleType.FIT_XY);
					ImageLoader.getInstance().displayImage(newEntity.getFocusThumbImg(), image, options);
					if (k == 0) {
						desViews[i].setText(newEntity.getContentDesc());
					}
				}
			}
		}

	}

	@Override
	public void updateUIRefleshData() {
		sendLoadDataResultMessage(LOAD_DATA_SUCCESS);
	}

	/*
	 * @Override public View getBottomMenuView() { View movieTabView =
	 * inflate(getContext(), R.layout.sub_navigation_movie, null);
	 * 
	 * View movieSearchLayout =
	 * movieTabView.findViewById(R.id.sub_navigation_movie_search_text_layout);
	 * View movieChannelLayout =
	 * movieTabView.findViewById(R.id.sub_navigation_movie_channel_text_layout);
	 * View movieFavouriteLayout =
	 * movieTabView.findViewById(R.id.sub_navigation_movie_favourite_text_layout
	 * );
	 * 
	 * // 搜索视频 movieSearchLayout.setNextFocusUpId(R.id.matrix_movie_layout_3);
	 * movieSearchLayout
	 * .setNextFocusRightId(R.id.sub_navigation_movie_channel_text_layout);
	 * movieSearchLayout
	 * .setNextFocusDownId(R.id.sub_navigation_movie_search_text_layout); //
	 * movieSearchLayout
	 * .setNextFocusLeftId(R.id.sub_navigation_movie_search_text_layout);
	 * movieSearchLayout
	 * .setNextFocusLeftId(R.id.sub_navigation_recommend_setting_text_layout);
	 * 
	 * // 点播频道 movieChannelLayout.setNextFocusUpId(R.id.matrix_movie_layout_3);
	 * movieChannelLayout
	 * .setNextFocusRightId(R.id.sub_navigation_movie_favourite_text_layout);
	 * movieChannelLayout
	 * .setNextFocusDownId(R.id.sub_navigation_movie_channel_text_layout);
	 * movieChannelLayout
	 * .setNextFocusLeftId(R.id.sub_navigation_movie_search_text_layout);
	 * 
	 * // 影片收藏
	 * movieFavouriteLayout.setNextFocusUpId(R.id.matrix_movie_layout_4); //
	 * movieFavouriteLayout
	 * .setNextFocusRightId(R.id.sub_navigation_movie_favourite_text_layout);
	 * movieFavouriteLayout
	 * .setNextFocusRightId(R.id.sub_navigation_app_external_text_layout);
	 * movieFavouriteLayout
	 * .setNextFocusDownId(R.id.sub_navigation_movie_favourite_text_layout);
	 * movieFavouriteLayout
	 * .setNextFocusLeftId(R.id.sub_navigation_movie_channel_text_layout);
	 * 
	 * movieSearchLayout.setOnClickListener(new MovieSearchClickListener());
	 * movieFavouriteLayout.setOnClickListener(new
	 * MovieFavouriteLayoutListener());
	 * movieChannelLayout.setOnClickListener(new MovieTabViewClickListener());
	 * 
	 * bottomMenuViews = new View[] { movieSearchLayout, movieChannelLayout,
	 * movieFavouriteLayout };
	 * viewFocusDirectionListener.setButtomMenuViewFocusDirection
	 * (bottomMenuViews); return movieTabView; }
	 *//**
	 * 响应点击事件 点击后进入
	 */
	/*
	 * class MovieSearchClickListener extends SimpleOnClickListener {
	 * 
	 * @Override public DataHolder doOnClick(View view) {
	 * 
	 * Intent intent = new Intent(getContext(), SearchHomeActivity.class);
	 * getContext().startActivity(intent); return new
	 * DataHolder.Builder(getContext()) .setDataType(DataType.CLICK_TAB_BUTTON)
	 * .setButton(String.valueOf(view.getId()), ((TextView)
	 * view.findViewById(R.id
	 * .sub_navigation_movie_search_text_view)).getText().toString())
	 * .setSenceName
	 * (AppScene.getScene()).setViewPosition("4407").setSrcType(ItemType
	 * .BUTTON).setTabNo(Tab.FILM).build(); } }
	 *//**
	 * 响应点击事件 点击后进入
	 */
	/*
	 * class MovieFavouriteLayoutListener extends SimpleOnClickListener {
	 * 
	 * @Override public DataHolder doOnClick(View view) {
	 * 
	 * Intent intent = new Intent(getContext(), CollectActivity.class);
	 * getContext().startActivity(intent); return new
	 * DataHolder.Builder(getContext()) .setDataType(DataType.CLICK_TAB_BUTTON)
	 * .setButton(String.valueOf(view.getId()), ((TextView)
	 * view.findViewById(R.id
	 * .sub_navigation_movie_favourite_text_view)).getText().toString())
	 * .setSenceName
	 * (AppScene.getScene()).setViewPosition("4409").setSrcType(ItemType
	 * .BUTTON).setTabNo(Tab.FILM).build(); } }
	 */
	@Override
	public View getBottomMenuView() {
		View movieTabView = inflate(getContext(), R.layout.sub_navigation_common_movie, null);
		View movieRecordLayout = movieTabView.findViewById(R.id.sub_navigation_common_movie_record_text_layout);
		View movieFavouriteLayout = movieTabView.findViewById(R.id.sub_navigation_common_movie_favourite_text_layout);
		View movieInstalledLayout = movieTabView.findViewById(R.id.sub_navigation_common_movie_installed_text_layout);
		View movieSearchLayout = movieTabView.findViewById(R.id.sub_navigation_common_movie_search_text_layout);
		View movieSettingLayout = movieTabView.findViewById(R.id.sub_navigation_common_movie_setting_text_layout);
		// View movieUserLayout =
		// movieTabView.findViewById(R.id.sub_navigation_common_movie_user_text_layout);
		View movieExternalLayout = movieTabView.findViewById(R.id.sub_navigation_common_movie_external_text_layout);

		movieRecordLayout.setOnClickListener(new movieRecordClickListener());
		movieFavouriteLayout.setOnClickListener(new movieFavouriteClickListener());
		movieInstalledLayout.setOnClickListener(new movieInstalledClickListener());
		movieSearchLayout.setOnClickListener(new movieSearchClickListener());
		movieSettingLayout.setOnClickListener(new movieSettingClickListener());
		// movieUserLayout.setOnClickListener(new movieUserClickListener());
		movieExternalLayout.setOnClickListener(new movieExternalClickListener());
		bottomMenuViews = new View[] { movieRecordLayout, movieFavouriteLayout, movieInstalledLayout, movieSearchLayout, movieSettingLayout,
				movieExternalLayout };// sub_navigation_common_movie_setting_text_layout
		viewFocusDirectionListener.setButtomMenuViewFocusDirection(bottomMenuViews);
		return movieTabView;
	}

	/**
	 * 响应观看记录点击事件 点击后进入
	 */
	class movieRecordClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), PlayerRecordActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext())
					.setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()),
							((TextView) view.findViewById(R.id.sub_navigation_common_movie_record_text_view)).getText().toString())
					.setSenceName(AppScene.getScene()).setViewPosition("4407").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应收藏点击事件 点击后进入
	 */
	class movieFavouriteClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), CollectActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext())
					.setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()),
							((TextView) view.findViewById(R.id.sub_navigation_common_movie_favourite_text_view)).getText().toString())
					.setSenceName(AppScene.getScene()).setViewPosition("4409").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应已安装应用游戏记录点击事件 点击后进入
	 */
	class movieInstalledClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent();
			intent.setAction("com.hiveview.appstore.buy");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			intent.putExtra("category_id", 2);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext())
					.setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()),
							((TextView) view.findViewById(R.id.sub_navigation_common_movie_installed_text_view)).getText().toString())
					.setSenceName(AppScene.getScene()).setViewPosition("4407").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应搜索点击事件 点击后进入
	 */
	class movieSearchClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), SearchHomeActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext())
					.setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()),
							((TextView) view.findViewById(R.id.sub_navigation_common_movie_search_text_view)).getText().toString())
					.setSenceName(AppScene.getScene()).setViewPosition("4407").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应系统设置点击事件 点击后进入
	 */
	class movieSettingClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent();
			intent.setAction("com.hiveview.settings.ACTION_SETTING");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			/* start by ZhaiJianfeng */
			int netStatus = HiveviewApplication.getNetStatus();
			intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
			/* end by ZhaiJianfeng */
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext())
					.setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()),
							((TextView) view.findViewById(R.id.sub_navigation_common_movie_setting_text_view)).getText().toString())
					.setSenceName(AppScene.getScene()).setViewPosition("4407").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应用户中心点击事件 点击后进入
	 * 
	 * class movieUserClickListener extends SimpleOnClickListener {
	 * 
	 * @Override public DataHolder doOnClick(View view) { Intent intent = new
	 *           Intent(); intent.setAction("com.hiveview.user.usercenter");
	 *           intent.addCategory(Intent.CATEGORY_DEFAULT);
	 *           getContext().startActivity(intent);
	 * 
	 *           return new DataHolder.Builder(getContext())
	 *           .setDataType(DataType.CLICK_TAB_BUTTON)
	 *           .setButton(String.valueOf(view.getId()), ((TextView)
	 *           view.findViewById
	 *           (R.id.sub_navigation_common_movie_user_text_view
	 *           )).getText().toString())
	 *           .setSenceName(AppScene.getScene()).setViewPosition
	 *           ("4407").setSrcType
	 *           (ItemType.BUTTON).setTabNo(Tab.FILM).build(); } }
	 */
	
	
	/**
	 * 响应外接媒体点击事件 点击后进入
	 */
	class movieExternalClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			AppUtil.openApp("com.fone.player.domy", getContext());
			return new DataHolder.Builder(getContext())
					.setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()),
							((TextView) view.findViewById(R.id.sub_navigation_common_movie_external_text_view)).getText().toString())
					.setSenceName(AppScene.getScene()).setViewPosition("4407").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应点击事件 点击后进入
	 */
	class MovieTabViewClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {
			// Intent intent = new Intent(getContext(), ChannelActivity.class);
			Intent intent = new Intent(getContext(), NewChannelActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext())
					.setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()),
							((TextView) view.findViewById(R.id.sub_navigation_movie_channel_text_view)).getText().toString())
					.setSenceName(AppScene.getScene()).setViewPosition("4408").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	@Override
	public ViewGroup getTopMenuView() {
		ViewGroup movieTabView = (ViewGroup) inflate(getContext(), R.layout.top_menu_movie_layout, null);
		TextView topTabTextView = (TextView) movieTabView.findViewById(R.id.navigation_tab_movie_text);
		ImageView topFadeTabImageView = (ImageView) movieTabView.findViewById(R.id.navigation_tab_movie_fade);
		topTabTextView.setOnFocusChangeListener(new TopTabOnFocusChangeListener(topTabTextView, topFadeTabImageView));

		topTabTextView.setNextFocusDownId(R.id.matrix_movie_layout_0);
		topTabTextView.setNextFocusUpId(R.id.navigation_tab_movie_text);

		return movieTabView;
	}

}
