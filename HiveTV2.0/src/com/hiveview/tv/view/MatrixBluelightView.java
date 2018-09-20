package com.hiveview.tv.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;
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
import com.hiveview.tv.service.VideoService;
import com.hiveview.tv.service.dao.BaseDAO;
import com.hiveview.tv.service.dao.RecommendDAO;
import com.hiveview.tv.service.entity.RecommendEntity;
import com.hiveview.tv.utils.AnimationUtil;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.view.pager3d.TabBasePageView;
import com.hiveview.tv.view.pager3d.ViewFlipperInAnimationListener;
import com.hiveview.tv.view.television.MarqueeText;

public class MatrixBluelightView extends TabBasePageView {

	protected static final String TAG = MatrixBluelightView.class.getSimpleName();

	/**
	 * 蓝光页面的推荐位Layout
	 */
	private View matrix_bluelight_layout_0, matrix_bluelight_layout_1, matrix_bluelight_layout_2, matrix_bluelight_layout_3,
			matrix_bluelight_layout_4, matrix_bluelight_layout_5;

	/**
	 * 推荐位轮播的ViewFlipper
	 */
	private ViewFlipper bluelight_flipper_0, bluelight_flipper_1, bluelight_flipper_2, bluelight_flipper_3, bluelight_flipper_4, bluelight_flipper_5;

	/**
	 * 显示推荐位文本信息的TextView
	 */
	private MarqueeText matrix_bluelight_flipper_0_text_view, matrix_bluelight_flipper_1_text_view, matrix_bluelight_flipper_2_text_view,
			matrix_bluelight_flipper_3_text_view, matrix_bluelight_flipper_4_text_view, matrix_bluelight_flipper_5_text_view;

	/**
	 * 显示推荐位文本信息的TextView数组
	 */
	private MarqueeText[] desViews;

	private ArrayList<ArrayList<RecommendEntity>> bluelightList = null;

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
	 * @Fields theFocusEntity
	 */
	public RecommendEntity theFocusEntity;
	
	/**
	 * @Fields entities viewfilper 中只有一张图的实体类的集合
	 */
	private ArrayList<RecommendEntity> entities;

	/**
	 * 蓝光页面的推荐位Layout数组
	 */
	private View[] recommendLayouts;

	private int focusViewIndex = -1;
	/**
	 * @Fields sharedPreferences 数据存储
	 */
	private SharedPreferences sharedPreferences;
	/**
	 * @Fields openNewsApp 打开极清首映的标示
	 */
	private String openNewsApp = "0";

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		};
	};

	public MatrixBluelightView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MatrixBluelightView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MatrixBluelightView(Context context) {
		super(context);
		init();
	}

	public MatrixBluelightView(Context context, RecommendViewFocusDirectionListener listener) {
		super(context, listener);
		init();
	}

	/**
	 * 推荐位相关View初始化
	 */
	protected void init() {
		recommendDAO = new RecommendDAO(getContext());
		sharedPreferences = getContext().getSharedPreferences(HiveviewApplication.RecommendTag, Context.MODE_WORLD_READABLE);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		View view = inflate(getContext(), R.layout.matrix_bluelight_view, null);

		this.addView(view, params);

		// 初始化推荐位Layout初始化
		matrix_bluelight_layout_0 = findViewById(R.id.matrix_bluelight_layout_0);
		matrix_bluelight_layout_1 = findViewById(R.id.matrix_bluelight_layout_1);
		matrix_bluelight_layout_2 = findViewById(R.id.matrix_bluelight_layout_2);
		matrix_bluelight_layout_3 = findViewById(R.id.matrix_bluelight_layout_3);
		matrix_bluelight_layout_4 = findViewById(R.id.matrix_bluelight_layout_4);
		matrix_bluelight_layout_5 = findViewById(R.id.matrix_bluelight_layout_5);

		// 初始化推荐位轮播的ViewFlipper
		bluelight_flipper_0 = (ViewFlipper) findViewById(R.id.matrix_bluelight_flipper_0);
		bluelight_flipper_1 = (ViewFlipper) findViewById(R.id.matrix_bluelight_flipper_1);
		bluelight_flipper_2 = (ViewFlipper) findViewById(R.id.matrix_bluelight_flipper_2);
		bluelight_flipper_3 = (ViewFlipper) findViewById(R.id.matrix_bluelight_flipper_3);
		bluelight_flipper_4 = (ViewFlipper) findViewById(R.id.matrix_bluelight_flipper_4);
		bluelight_flipper_5 = (ViewFlipper) findViewById(R.id.matrix_bluelight_flipper_5);

		// 初始化显示推荐位文本的TextView
		matrix_bluelight_flipper_0_text_view = (MarqueeText) findViewById(R.id.matrix_bluelight_flipper_0_text_view);
		matrix_bluelight_flipper_1_text_view = (MarqueeText) findViewById(R.id.matrix_bluelight_flipper_1_text_view);
		matrix_bluelight_flipper_2_text_view = (MarqueeText) findViewById(R.id.matrix_bluelight_flipper_2_text_view);
		matrix_bluelight_flipper_3_text_view = (MarqueeText) findViewById(R.id.matrix_bluelight_flipper_3_text_view);
		matrix_bluelight_flipper_4_text_view = (MarqueeText) findViewById(R.id.matrix_bluelight_flipper_4_text_view);
		matrix_bluelight_flipper_5_text_view = (MarqueeText) findViewById(R.id.matrix_bluelight_flipper_5_text_view);

		matrix_bluelight_layout_2.setNextFocusDownId(R.id.sub_navigation_common_bluelight_record_text_layout);
		matrix_bluelight_layout_3.setNextFocusDownId(R.id.sub_navigation_common_bluelight_installed_text_layout);
		matrix_bluelight_layout_4.setNextFocusDownId(R.id.sub_navigation_common_bluelight_search_text_layout);
		matrix_bluelight_layout_5.setNextFocusDownId(R.id.sub_navigation_common_bluelight_setting_text_layout);

		// 推荐0位置的焦点定位
		matrix_bluelight_layout_0.setNextFocusUpId(R.id.navigation_tab_bluelight_text);
		matrix_bluelight_layout_0.setNextFocusRightId(R.id.matrix_bluelight_layout_1);
		matrix_bluelight_layout_0.setNextFocusDownId(matrix_bluelight_layout_2.getId());

		// 推荐1位置的焦点定位
		matrix_bluelight_layout_1.setNextFocusUpId(R.id.navigation_tab_bluelight_text);
		matrix_bluelight_layout_1.setNextFocusRightId(R.id.matrix_bluelight_layout_5);
		matrix_bluelight_layout_1.setNextFocusLeftId(R.id.matrix_bluelight_layout_0);
		matrix_bluelight_layout_1.setNextFocusDownId(R.id.matrix_bluelight_layout_3);

		// 推荐2位置的焦点定位
		matrix_bluelight_layout_2.setNextFocusRightId(R.id.matrix_bluelight_layout_3);
		matrix_bluelight_layout_2.setNextFocusDownId(matrix_bluelight_layout_2.getId());
		matrix_bluelight_layout_2.setNextFocusUpId(R.id.matrix_bluelight_layout_0);

		// 推荐3位置的焦点定位

		matrix_bluelight_layout_3.setNextFocusUpId(R.id.matrix_bluelight_layout_1);
		matrix_bluelight_layout_3.setNextFocusRightId(R.id.matrix_bluelight_layout_4);
		matrix_bluelight_layout_3.setNextFocusLeftId(R.id.matrix_bluelight_layout_2);
		matrix_bluelight_layout_3.setNextFocusDownId(matrix_bluelight_layout_3.getId());

		// 推荐4位置的焦点定位

		matrix_bluelight_layout_4.setNextFocusUpId(R.id.matrix_bluelight_layout_1);
		matrix_bluelight_layout_4.setNextFocusRightId(R.id.matrix_bluelight_layout_5);
		matrix_bluelight_layout_4.setNextFocusLeftId(R.id.matrix_bluelight_layout_3);
		matrix_bluelight_layout_4.setNextFocusDownId(matrix_bluelight_layout_4.getId());

		// 推荐5位置的焦点定位

		matrix_bluelight_layout_5.setNextFocusUpId(R.id.navigation_tab_bluelight_text);
		matrix_bluelight_layout_5.setNextFocusRightId(R.id.matrix_recommend_layout_0);
		matrix_bluelight_layout_5.setNextFocusLeftId(R.id.matrix_bluelight_layout_1);
		matrix_bluelight_layout_5.setNextFocusDownId(matrix_bluelight_layout_5.getId());

		// 初始化 推荐位轮播的ViewFlipper数组
		flippers = new ViewFlipper[] { bluelight_flipper_0, bluelight_flipper_1, bluelight_flipper_2, bluelight_flipper_3, bluelight_flipper_4,
				bluelight_flipper_5 };

		recommendEdgeViews = new View[] { matrix_bluelight_layout_0, matrix_bluelight_layout_2, matrix_bluelight_layout_5 };

		// ViewFlipper 轮播动画

		for (int i = 0; i < flippers.length; i++) {
			Animation inAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.down_in);
			Animation outAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.down_out);
			inAnimation.setAnimationListener(new ViewFlipperInAnimationListener(flippers[i], i) {

				@Override
				public void onStartInAnimation(ViewFlipper viewFlipper, int viewFlipperIndexInPage) {
					theFocusEntity = (RecommendEntity) flippers[viewFlipperIndexInPage].getCurrentView().getTag();
					if (TextUtils.isEmpty(theFocusEntity.getContentName())) {
						desViews[viewFlipperIndexInPage].setVisibility(View.INVISIBLE);
					} else {
						desViews[viewFlipperIndexInPage].setVisibility(View.VISIBLE);
					}
//					setOnFocusViewChange(new FocusViewItem(viewFlipperIndexInPage, viewFlipperIndexInPage == focusViewIndex ? true : false));
					 desViews[viewFlipperIndexInPage].setText(theFocusEntity.getContentName());
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
		desViews = new MarqueeText[] { matrix_bluelight_flipper_0_text_view, matrix_bluelight_flipper_1_text_view,
				matrix_bluelight_flipper_2_text_view, matrix_bluelight_flipper_3_text_view, matrix_bluelight_flipper_4_text_view,
				matrix_bluelight_flipper_5_text_view };

		// 初始化推荐位Layout数组
		recommendLayouts = new View[] { matrix_bluelight_layout_0, matrix_bluelight_layout_1, matrix_bluelight_layout_2, matrix_bluelight_layout_3,
				matrix_bluelight_layout_4, matrix_bluelight_layout_5 };

		// 推荐位绑定焦点改变事件和焦点改变事件
		for (int i = 0; i < recommendLayouts.length; i++) {
			recommendLayouts[i].setOnFocusChangeListener(recommendLayoutFocusListener);
			// recommendLayouts[i].setOnClickListener(recommendLayoutClickListener);RecommendLayoutClickListener
			recommendLayouts[i].setOnClickListener(new RecommendLayoutClickListener());
		}
		hashMap.put(matrix_bluelight_layout_0, "7701");
		hashMap.put(matrix_bluelight_layout_1, "7702");
		hashMap.put(matrix_bluelight_layout_2, "7703");
		hashMap.put(matrix_bluelight_layout_3, "7704");
		hashMap.put(matrix_bluelight_layout_4, "7705");
		hashMap.put(matrix_bluelight_layout_5, "7706");
		super.init();

	}

	/**
	 * @Title: MatrixBluelightView
	 * @author:张鹏展
	 * @Description: 焦点选中不同的item的时候，区分显示看点或者是名称
	 * @param focusViewItem
	 */
	private void setOnFocusViewChange(FocusViewItem focusViewItem) {
		int viewIndex = focusViewItem.getViewIndex();
		boolean isFocusView = focusViewItem.isViewFocus();
		switch (viewIndex) {
		case 0:
			desViews[0].setText(isFocusView ? null != theFocusEntity?theFocusEntity.getContentDesc():entities.get(viewIndex).getContentDesc() : null != theFocusEntity?theFocusEntity.getContentName():entities.get(viewIndex).getContentName());
			desViews[0].setStart(isFocusView);
			break;
		case 1:
			desViews[1].setText(isFocusView ? null != theFocusEntity?theFocusEntity.getContentDesc():entities.get(viewIndex).getContentDesc() : null != theFocusEntity?theFocusEntity.getContentName():entities.get(viewIndex).getContentName());
			desViews[1].setStart(isFocusView);
			
			break;
		case 2:
			desViews[2].setText(isFocusView ? null != theFocusEntity?theFocusEntity.getContentDesc():entities.get(viewIndex).getContentDesc() : null != theFocusEntity?theFocusEntity.getContentName():entities.get(viewIndex).getContentName());
			desViews[2].setStart(isFocusView);
			break;
		case 3:
			desViews[3].setText(isFocusView ? null != theFocusEntity?theFocusEntity.getContentDesc():entities.get(viewIndex).getContentDesc() : null != theFocusEntity?theFocusEntity.getContentName():entities.get(viewIndex).getContentName());
			desViews[3].setStart(isFocusView);
			break;
		case 4:
			desViews[4].setText(isFocusView ? null != theFocusEntity?theFocusEntity.getContentDesc():entities.get(viewIndex).getContentDesc() : null != theFocusEntity?theFocusEntity.getContentName():entities.get(viewIndex).getContentName());
			desViews[4].setStart(isFocusView);
			break;
		case 5:
			desViews[5].setText(isFocusView ? null != theFocusEntity?theFocusEntity.getContentDesc():entities.get(viewIndex).getContentDesc() : null != theFocusEntity?theFocusEntity.getContentName():entities.get(viewIndex).getContentName());
			desViews[5].setStart(isFocusView);
			break;
		default:
			break;
		}
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

//			if (index == 0) {// 打开蓝光极清应用-->0号位绑定首映APP跳转
////				AppUtil.openApp("com.hiveview.bluelight", getContext());
//				AppUtil.openAppForPackageName("com.hiveview.premiere" , getContext());
//				return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_GAME_BUTTON)
//						.setButton(String.valueOf(view.getId()), "蓝光极清首映").setSenceName(AppScene.getScene()).setSrcType(ItemType.BUTTON)
//						.setTabNo(Tab.BULE).setViewPosition(null != hashMap.get(view) ? hashMap.get(view).toString() : "")
//						.build();
//			}

			if (flippers[index].getCurrentView() == null || flippers[index].getChildCount() == 0) {
				return null;
			}

			// 获取推荐位绑定的数据
			RecommendEntity entity = (RecommendEntity) flippers[index].getCurrentView().getTag();

			if (null == entity)
				return null;
			// 获取当前数据类型对应的详情Activity的Action
			String action = ContentInvoker.getInstance().getContentAction(entity.getFocusType());
			//logic:执行逻辑1：正常执行 2.调用极清首映apk
			String openApp = sharedPreferences.getString(String.valueOf(entity.getContentId()), "1");
			Log.d(TAG, "openApp::"+openApp);
			if(openNewsApp.equals(entity.getIsApk())){
				Log.d(TAG, "openApp::true");
				AppUtil.openAppForPackageName(entity.getApkPackage(), getContext());
				return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_GAME_BUTTON)
						.setButton(String.valueOf(view.getId()), entity.getContentName()).setSenceName(AppScene.getScene()).setSrcType(ItemType.BUTTON)
						.setTabNo(Tab.BULE).setViewPosition(null != hashMap.get(view) ? hashMap.get(view).toString() : "")
						.build();
			}else if (!TextUtils.isEmpty(action)) {
				if (action.equals(ContentInvoker.CONTENT_ACTION_BLUE_2_HALL)// 极清2.0影厅
						|| action.equals(ContentInvoker.CONTENT_ACTION_BLUE_2_SUBJECT)// 极清2.0专题
						|| action.equals(ContentInvoker.CONTENT_ACTION_BLUE_2_ALBUM)// 极清2.0专辑
						) {// 极清2.0促销
					try {
						Intent it = new Intent();
						ComponentName componentName = new ComponentName("com.hiveview.bluelight", "com.hiveview.bluelight.activity.BlueLightActivity");
						it.setComponent(componentName);
						it.putExtra("page", "com.hiveview.bluelight.page.DispatchPage");
						it.putExtra("contentId", entity.getContentId() + "");
						it.putExtra("contentType", entity.getFocusType() + "");
						Log.d(TAG, "contentId=====" + entity.getContentId() + "||getFocusType=========" + entity.getFocusType() );
						getContext().startActivity(it);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if(action
						.equals(ContentInvoker.CONTENT_ACTION_BLUE_2_FORSALES)){
					try {
						Intent it = new Intent();
						ComponentName componentName = new ComponentName(
								"com.hiveview.bluelight",
								"com.hiveview.bluelight.activity.BlueLightActivity");
						it.setComponent(componentName);
						it.putExtra("page",
								"com.hiveview.bluelight.page.DispatchPage");
						it.putExtra("contentId", entity.getCpId() + "");
						it.putExtra("contentType", entity.getFocusType()
								+ "");
						Log.d(TAG,
								"cp=====" + entity.getCpId()
										+ "||getFocusType========="
										+ entity.getFocusType());
						getContext().startActivity(it);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				else if(action.equals(ContentInvoker.CONTENT_ACTION_BLUE__PREMIERE)){//极清首映类型
					try {
					Log.v(TAG, "CONTENT_ACTION_BLUE__PREMIERE=="+entity.getContentId());
					Intent intent = getContext().getPackageManager().getLaunchIntentForPackage("com.hiveview.premiere");
					intent.putExtra("page_id", 103);
					intent.putExtra("id", entity.getContentId());
					getContext().startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (action.equals(ContentInvoker.CONTENT_ACTION_BLUE)) {// 蓝光极清类型的视频
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
					.setSrcType(ItemType.VIDEO).setTabNo(Tab.BULE).setViewPosition(null != hashMap.get(view) ? hashMap.get(view).toString() : "")
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
				// setOnFocusViewChange(view, isFocused);
				view.bringToFront();

				for (int i = 0; i < recommendLayouts.length; i++) {
					if (view.equals(recommendLayouts[i])) {
						focusViewIndex = i;
					}
				}
//				setOnFocusViewChange(new FocusViewItem(focusViewIndex, true));
				if (view.getId() == R.id.matrix_bluelight_layout_1) {//
					// 第2个位置的推荐位，可露头的推荐位，动画要特殊处理
					findViewById(R.id.matrix_bluelight_flipper_1_out_layout).bringToFront();
					AnimationUtil.loadBigAnimation(findViewById(R.id.matrix_bluelight_flipper_1_out_layout));
				} else if (view.getId() == R.id.matrix_bluelight_layout_5) {//
					// 第2个位置的推荐位，可露头的推荐位，动画要特殊处理
					findViewById(R.id.matrix_bluelight_flipper_5_out_layout).bringToFront();
					AnimationUtil.loadBigAnimation(findViewById(R.id.matrix_bluelight_flipper_5_out_layout));
				}

				// 一般的轮播推荐位获得焦点的动画效果
				AnimationUtil.loadBigAnimation(view);
				AlphaAnimation alphaAnim = new AlphaAnimation(0.9f, 1.0f);
				alphaAnim.setDuration(100);
				alphaAnim.setInterpolator(new LinearInterpolator());
				view.startAnimation(alphaAnim);
			} else {
				// setOnFocusViewChange(view, isFocused);
				// 一般的轮播推荐位失去焦点的动画效果
				AnimationUtil.loadSmallAnimation(view);
				for (int i = 0; i < recommendLayouts.length; i++) {
					if (view.equals(recommendLayouts[i])) {
//						setOnFocusViewChange(new FocusViewItem(i, false));
						focusViewIndex = -1;
					}
				}
				if (view.getId() == R.id.matrix_bluelight_layout_1) {// 第1个位置露头轮播推荐位失去焦点的动画效果

					AnimationUtil.loadSmallAnimation(findViewById(R.id.matrix_bluelight_flipper_1_out_layout));

				} else if (view.getId() == R.id.matrix_bluelight_layout_5) {// 第5个位置露头轮播推荐位失去焦点的动画效果

					AnimationUtil.loadSmallAnimation(findViewById(R.id.matrix_bluelight_flipper_5_out_layout));

				}

			}

		}
	};

	/**
	 * @ClassName: FocusViewItem
	 * @Description: view的状态
	 * @author: zhangpengzhan
	 * @date 2014年11月21日 下午4:13:35
	 * 
	 */
	class FocusViewItem {
		private int viewIndex;
		private boolean viewFocus;

		public FocusViewItem(int viewIndex, boolean viewFocus) {
			this.viewIndex = viewIndex;
			this.viewFocus = viewFocus;
		}

		public int getViewIndex() {
			return viewIndex;
		}

		public void setViewIndex(int viewIndex) {
			this.viewIndex = viewIndex;
		}

		public boolean isViewFocus() {
			return viewFocus;
		}

		public void setViewFocus(boolean viewFocus) {
			this.viewFocus = viewFocus;
		}
	}

	@Override
	protected void processData(int msgWhat) {
		switch (msgWhat) {
		case LOAD_DATA_SUCCESS:
			setMatrixData();
			int flipersLength = flippers.length;
			entities = new ArrayList<RecommendEntity>();
			// 推荐位只有一个推荐图的时候，动画不能执行，特此在这里加载一遍
			for (int i = 0; i < flipersLength; i++) {
				try {
					entities.add((RecommendEntity)flippers[i].getCurrentView().getTag());
					// 该情况只是应对只有一个推荐位的问题
					if (flippers[i].getChildCount() == 1) {
						RecommendEntity entity = (RecommendEntity) flippers[i].getCurrentView().getTag();
						entities.add((RecommendEntity)flippers[i].getCurrentView().getTag());
						if (TextUtils.isEmpty(entity.getContentDesc())) {
							desViews[i].setVisibility(View.INVISIBLE);
						} else {
							desViews[i].setVisibility(View.VISIBLE);
							desViews[i].setText(entity.getContentDesc());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			break;
		case LOAD_DATA_FAIL:

			break;
		default:
			break;
		}
	}

	@Override
	public void loadData(boolean isRefleshData) {
		Log.i(TAG, "loadData-->start");
		int count = 0;
		do {
			bluelightList = recommendDAO.queryMatrix(null, BaseDAO.MATRIX_TYPE + " = " + BaseDAO.MATRIX_BLUELIGHT, null, null);
			if (bluelightList.get(bluelightList.size() - 1).size() > 0 || count == readDataFromDBCount) {
				break;
			}
			count++;
			try {
				Thread.sleep(readDataFromDBInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (bluelightList.get(bluelightList.size() - 1).size() == 0);
		if (null != bluelightList && bluelightList.get(bluelightList.size() - 1).size() > 0) {
			checkPageIsIdle();
		} else {
			sendLoadDataResultMessage(LOAD_DATA_FAIL);
		}
		Log.i(TAG, "loadData-->end");
	}

	/**
	 * 设置蓝光上的推荐位数据
	 * 
	 * @param bluelightList
	 */
	public void setMatrixData() {
		try {
			Log.i(TAG, "setMatrixData-->start");
			int count = bluelightList.size();
			Log.v(TAG, "count=="+count);

			android.view.ViewGroup.LayoutParams params = new android.view.ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			for (int i = 0; i < count; i++) {
				ArrayList<RecommendEntity> subList = bluelightList.get(i);// 获取每个推荐位上的数据列表
				int dataSize = subList.size();
				int viewSize = flippers[i].getChildCount();
				if (dataSize > viewSize) {// 当推荐位数据增加时，补充显示推荐位的View
					Log.d(TAG, "bluelight pageview " + i + " position recommend data add");
					for (int j = 0; j < dataSize - viewSize; j++) {
						ImageView image = new ImageView(getContext());
						flippers[i].addView(image, params);
						Log.d(TAG, "bluelight pageview " + i + " position childs count " + flippers[i].getChildCount());
					}
				} else if (dataSize < viewSize) {// 当推荐位数据减少时，减少显示推荐位的View
					Log.d(TAG, "bluelight pageview " + i + " position recommend data reduce");
					for (int j = dataSize; j < viewSize; j++) {
						if (null != flippers[i] && flippers[i].getChildCount() > 0) {
							Log.d(TAG, "bluelight pageview " + i + " position childs count " + flippers[i].getChildCount());
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
			Log.i(TAG, "setMatrixData-->end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateUIRefleshData() {
		sendLoadDataResultMessage(LOAD_DATA_SUCCESS);
	}

	@Override
	public View getBottomMenuView() {
		View bluelightTabView = inflate(getContext(), R.layout.sub_navigation_common_bluelight, null);
		View bluelightRecordLayout = bluelightTabView.findViewById(R.id.sub_navigation_common_bluelight_record_text_layout);
		View bluelightFavouriteLayout = bluelightTabView.findViewById(R.id.sub_navigation_common_bluelight_favourite_text_layout);
		View bluelightInstalledLayout = bluelightTabView.findViewById(R.id.sub_navigation_common_bluelight_installed_text_layout);
		View bluelightSearchLayout = bluelightTabView.findViewById(R.id.sub_navigation_common_bluelight_search_text_layout);
		View bluelightSettingLayout = bluelightTabView.findViewById(R.id.sub_navigation_common_bluelight_setting_text_layout);
		// View bluelightUserLayout =
		// bluelightTabView.findViewById(R.id.sub_navigation_common_bluelight_user_text_layout);
		View bluelightExternalLayout = bluelightTabView.findViewById(R.id.sub_navigation_common_bluelight_external_text_layout);

		bluelightRecordLayout.setOnClickListener(new bluelightRecordClickListener());
		bluelightFavouriteLayout.setOnClickListener(new bluelightFavouriteClickListener());
		bluelightInstalledLayout.setOnClickListener(new bluelightInstalledClickListener());
		bluelightSearchLayout.setOnClickListener(new bluelightSearchClickListener());
		bluelightSettingLayout.setOnClickListener(new bluelightSettingClickListener());
		// bluelightUserLayout.setOnClickListener(new
		// bluelightUserClickListener());
		bluelightExternalLayout.setOnClickListener(new bluelightExternalClickListener());

		bluelightRecordLayout.setNextFocusUpId(R.id.matrix_bluelight_layout_2);
		bluelightFavouriteLayout.setNextFocusUpId(R.id.matrix_bluelight_layout_2);
		bluelightInstalledLayout.setNextFocusUpId(R.id.matrix_bluelight_layout_3);
		bluelightSearchLayout.setNextFocusUpId(R.id.matrix_bluelight_layout_4);
		bluelightSettingLayout.setNextFocusUpId(R.id.matrix_bluelight_layout_4);
		// bluelightUserLayout.setNextFocusUpId(R.id.matrix_bluelight_layout_5);
		bluelightExternalLayout.setNextFocusUpId(R.id.matrix_bluelight_layout_5);

		bottomMenuViews = new View[] { bluelightRecordLayout, matrix_bluelight_layout_0, matrix_bluelight_layout_2, bluelightSearchLayout,
				bluelightSettingLayout, bluelightExternalLayout };// bluelightUserLayout,
		viewFocusDirectionListener.setButtomMenuViewFocusDirection(bottomMenuViews);
		return bluelightTabView;
	}

	/**
	 * 响应观看记录点击事件 点击后进入
	 */
	class bluelightRecordClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), PlayerRecordActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_record_text))
					.setSenceName(AppScene.getScene()).setViewPosition("7707").setSrcType(ItemType.BUTTON).setTabNo(Tab.BULE).build();
		}
	}

	/**
	 * 响应收藏点击事件 点击后进入
	 */
	class bluelightFavouriteClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), CollectActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_favourite_text))
					.setSenceName(AppScene.getScene()).setViewPosition("7708").setSrcType(ItemType.BUTTON).setTabNo(Tab.BULE).build();
		}
	}

	/**
	 * 响应已安装应用游戏记录点击事件 点击后进入
	 */
	class bluelightInstalledClickListener extends SimpleOnClickListener {
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
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_installed_text))
					.setSenceName(AppScene.getScene()).setViewPosition("7709").setSrcType(ItemType.BUTTON).setTabNo(Tab.BULE).build();
		}
	}

	/**
	 * 响应搜索点击事件 点击后进入
	 */
	class bluelightSearchClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), SearchHomeActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_search_text))
					.setSenceName(AppScene.getScene()).setViewPosition("7710").setSrcType(ItemType.BUTTON).setTabNo(Tab.BULE).build();
		}
	}

	/**
	 * 响应系统设置点击事件 点击后进入
	 */
	class bluelightSettingClickListener extends SimpleOnClickListener {
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
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_setting_text))
					.setSenceName(AppScene.getScene()).setViewPosition("7711").setSrcType(ItemType.BUTTON).setTabNo(Tab.BULE).build();
		}
	}

	/**
	 * 响应用户中心点击事件 点击后进入
	 */
	// class bluelightUserClickListener extends SimpleOnClickListener {
	// @Override
	// public DataHolder doOnClick(View view) {
	// Intent intent = new Intent();
	// intent.setAction("com.hiveview.user.usercenter");
	// intent.addCategory(Intent.CATEGORY_DEFAULT);
	// getContext().startActivity(intent);
	//
	// return new
	// DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
	// .setButton(String.valueOf(view.getId()),
	// getResources().getString(R.string.sub_navigation_common_user_text))
	// .setSenceName(AppScene.getScene()).setViewPosition("7712").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
	// }
	// }

	/**
	 * 响应外接媒体点击事件 点击后进入
	 */
	class bluelightExternalClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			AppUtil.openApp("com.hiveview.externalstorage", getContext());
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_external_text))
					.setSenceName(AppScene.getScene()).setViewPosition("7713").setSrcType(ItemType.BUTTON).setTabNo(Tab.BULE).build();
		}
	}

	@Override
	public ViewGroup getTopMenuView() {
		ViewGroup bluelightTabView = (ViewGroup) inflate(getContext(), R.layout.top_menu_bluelight_layout, null);
		TextView topTabTextView = (TextView) bluelightTabView.findViewById(R.id.navigation_tab_bluelight_text);
		ImageView topFadeTabImageView = (ImageView) bluelightTabView.findViewById(R.id.navigation_tab_bluelight_fade);
		topTabTextView.setOnFocusChangeListener(new TopTabOnFocusChangeListener(topTabTextView, topFadeTabImageView));

		topTabTextView.setNextFocusDownId(R.id.matrix_bluelight_layout_0);
		topTabTextView.setNextFocusUpId(R.id.navigation_tab_bluelight_text);

		return bluelightTabView;
	}

}
