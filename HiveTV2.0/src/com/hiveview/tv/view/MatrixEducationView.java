package com.hiveview.tv.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.CollectActivity;
import com.hiveview.tv.activity.HomeActivity;
import com.hiveview.tv.activity.PlayerRecordActivity;
import com.hiveview.tv.activity.SearchHomeActivity;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.content.ContentInvoker;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.ItemType;
import com.hiveview.tv.common.statistics.SimpleOnClickListener;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.service.dao.EducationDAO;
import com.hiveview.tv.service.entity.AppFocusEntity;
import com.hiveview.tv.utils.AnimationUtil;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.view.pager3d.HomeActions;
import com.hiveview.tv.view.pager3d.TabAppBasePageView;
import com.hiveview.tv.view.pager3d.TabBasePageView;

/**
 * @ClassName: MatrixCinemaView
 * @Description: 新增教育的view
 * @author: shenshaofei
 * @date 2015年3月30日 上午11:14:39
 * 
 */
public class MatrixEducationView extends TabAppBasePageView {
	protected static final String TAG = "MatrixEducationView";
//	private HashMap<Integer, AppRecommendView> mapDownload = new HashMap<Integer, AppRecommendView>();
	private static final int LOAD_DATA_SUCCESS = 0x00123;
	private static final int LOAD_DATA_FAIL = 0x00124;

	/**
	 * 教育的推荐位Layout
	 */
	private View matrix_education_layout_1, matrix_education_layout_2, matrix_education_layout_3, matrix_education_layout_4, matrix_education_layout_5,
			matrix_education_layout_6, matrix_education_layout_7;
	/**
	 * 推荐位轮播的ViewFlipper
	 */
	private ViewFlipper education_flipper_1, education_flipper_2, education_flipper_3, education_flipper_4, education_flipper_5, education_flipper_6;
	/**
	 * 显示推荐位文本信息的TextView数组
	 */
	private TextView[] desViews;
	/**
	 * 推荐位Layout数组
	 */
	private View[] views;
	/**
	 * @Fields educationList:应该显示数据的
	 */
	private ArrayList<ArrayList<AppFocusEntity>> educationList = null;

	/**
	 * @Fields recommendDAO:存储相关数据的数据库
	 */
	private EducationDAO recommendDAO = null;

	private LinkedList<ViewFlipper> viewFilpers;

	/**
	 * @Fields viewIndex:viewFilper中view 的index
	 */
	private int viewIndex = 0;
	private int[][] viewfliperData;
	private int pages = 0;
	private int thePageItems = 9;
	private ViewFlipper[] viewFliper;
	/**
	 * 按钮的位置
	 */
	private int btnIndex = 5;


	private ArrayList<View> educationViews;
	
	/**
	 * @Fields educationName 保存影院页面的频道名称
	 */
	private ArrayList<String> educationName;

	/**
	 * @Fields cinemaNo:埋点统计的编号
	 */
	private int[][] cinemaNo;

	private Context context;

	/**
	 * @Fields pageChange:需要换页的接口
	 */
	private PageChange pageChange;

	private int viewVisibleTime = 250;

	private Handler matrixEducationHandler = new Handler();

	private HomeActivity homeActivity;

	public MatrixEducationView(Context context) {
		super(context);
		init();
		this.context = context;
	}

	public MatrixEducationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		this.context = context;
	}

	public MatrixEducationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		this.context = context;
		
	}

	public MatrixEducationView(Context context, RecommendViewFocusDirectionListener listener) {
		super(context, listener);
		init();
		this.context = context;
	}
	HashMap<Integer, String> idHashMap = new HashMap<Integer, String>();

	@Override
	public void updateUIRefleshData() {
		sendLoadDataResultMessage(LOAD_DATA_SUCCESS);
	}

//	public void setPageChange(PageChange pageChange) {
//		this.pageChange = pageChange;
//	}

	/**
	 * 推荐位相关View初始化
	 */
	protected void init() {
		Log.d(TAG, "::==>" + Environment.getExternalStorageDirectory());
		recommendDAO = new EducationDAO(getContext());
		// 初始化viewfiler 列表
		viewFilpers = new LinkedList<ViewFlipper>();
		educationViews = new ArrayList<View>();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View view = inflate(getContext(), R.layout.matrix_education_view, null);
		this.addView(view, params);
		//
		matrix_education_layout_1 = (View) findViewById(R.id.matrix_education_layout_1);
		matrix_education_layout_2 = (View) findViewById(R.id.matrix_education_layout_2);
		matrix_education_layout_3 = (View) findViewById(R.id.matrix_education_layout_3);
		matrix_education_layout_4 = (View) findViewById(R.id.matrix_education_layout_4);
		matrix_education_layout_5 = (View) findViewById(R.id.matrix_education_layout_5);
		matrix_education_layout_6 = (View) findViewById(R.id.matrix_education_layout_6);
		matrix_education_layout_7 = (View) findViewById(R.id.matrix_education_layout_7);
		
		educationViews.add(matrix_education_layout_1);
		educationViews.add(matrix_education_layout_2);
		educationViews.add(matrix_education_layout_3);
		educationViews.add(matrix_education_layout_4);
		educationViews.add(matrix_education_layout_5);
		educationViews.add(matrix_education_layout_6);
		educationViews.add(matrix_education_layout_7);
		idHashMap.put(R.id.matrix_education_layout_7, "教育中心");
		//
		education_flipper_1 = (ViewFlipper) findViewById(R.id.matrix_education_flipper_1);
		education_flipper_2 = (ViewFlipper) findViewById(R.id.matrix_education_flipper_2);
		education_flipper_3 = (ViewFlipper) findViewById(R.id.matrix_education_flipper_3);
		education_flipper_4 = (ViewFlipper) findViewById(R.id.matrix_education_flipper_4);
		education_flipper_5 = (ViewFlipper) findViewById(R.id.matrix_education_flipper_5);
		education_flipper_6 = (ViewFlipper) findViewById(R.id.matrix_education_flipper_6);
		views = new View[]{matrix_education_layout_1, matrix_education_layout_2, matrix_education_layout_3, matrix_education_layout_4,
				matrix_education_layout_5, matrix_education_layout_6, matrix_education_layout_7};

		// 初始化推荐位Layout数组
//		recommendEdgeViews = new View[] { matrix_education_layout_1, matrix_education_layout_2, matrix_education_layout_3, matrix_education_layout_4,
//				matrix_education_layout_5, matrix_education_layout_6, matrix_education_layout_7};

		// 初始化 推荐位轮播的ViewFlipper数组
		viewFliper = new ViewFlipper[] { education_flipper_1, education_flipper_2, education_flipper_3, education_flipper_4, education_flipper_5, education_flipper_6};
		for (int i = 0; i < viewFliper.length; i++) {// 轮播推荐位动画
			viewFliper[i].setInAnimation(getContext(), R.anim.down_in);
			viewFliper[i].setOutAnimation(getContext(), R.anim.down_out);
		}
		// 推荐位绑定焦点改变事件和焦点改变事件
		for (int i = 0; i < views.length; i++) {
			views[i].setOnFocusChangeListener(recommendLayoutFocusListener);
			views[i].setOnClickListener(new EducationLayoutClickListener());
		}
		focusRules();
		super.init();
	}

	ViewFlipper viewFlipper = null;

	/**
	 * right1--> matrix_app_layout_0 right2--> matrix_app_layout_5 left1-->
	 * matrix_recommend_flipper_2_out_layout left2--> matrix_recommend_layout_4
	 * 
	 * @Title: MatrixEducationView
	 * @Description: 焦点走向规则
	 */
	private void focusRules() {
		matrix_education_layout_1.setNextFocusRightId( matrix_education_layout_2.getId());
		matrix_education_layout_1.setNextFocusDownId(R.id.matrix_education_layout_1);
		matrix_education_layout_1.setNextFocusLeftId(R.id.matrix_cinema_layout_11_new_v);
		matrix_education_layout_1.setNextFocusUpId(R.id.navigation_tab_education_text);

		matrix_education_layout_2.setNextFocusRightId( matrix_education_layout_5.getId());
		matrix_education_layout_2.setNextFocusDownId( matrix_education_layout_3.getId());
		matrix_education_layout_2.setNextFocusLeftId(R.id.matrix_education_layout_1);
		matrix_education_layout_2.setNextFocusUpId(R.id.navigation_tab_education_text);

		matrix_education_layout_5.setNextFocusRightId(R.id.matrix_app_layout_0);
		matrix_education_layout_5.setNextFocusDownId( matrix_education_layout_6.getId());
		matrix_education_layout_5.setNextFocusLeftId(R.id.matrix_education_layout_2);
		matrix_education_layout_5.setNextFocusUpId(R.id.navigation_tab_education_text);

		matrix_education_layout_3.setNextFocusRightId(matrix_education_layout_4.getId());
		matrix_education_layout_3.setNextFocusDownId(matrix_education_layout_3.getId());
		matrix_education_layout_3.setNextFocusLeftId(R.id.matrix_education_layout_1);
		matrix_education_layout_3.setNextFocusUpId(R.id.matrix_education_layout_2);

		matrix_education_layout_4.setNextFocusRightId(R.id.matrix_education_layout_6);
		matrix_education_layout_4.setNextFocusDownId(matrix_education_layout_4.getId());
		matrix_education_layout_4.setNextFocusLeftId(R.id.matrix_education_layout_3);
		matrix_education_layout_4.setNextFocusUpId(R.id.matrix_education_layout_2);

		matrix_education_layout_6.setNextFocusRightId(R.id.matrix_app_layout_0);
		matrix_education_layout_6.setNextFocusUpId(R.id.matrix_education_layout_5);
		matrix_education_layout_6.setNextFocusLeftId(R.id.matrix_education_layout_4);
		matrix_education_layout_6.setNextFocusDownId(matrix_education_layout_7.getId());

		matrix_education_layout_7.setNextFocusRightId(R.id.matrix_app_layout_0);
		matrix_education_layout_7.setNextFocusUpId(R.id.matrix_education_layout_6);
		matrix_education_layout_7.setNextFocusLeftId(R.id.matrix_education_layout_4);
		matrix_education_layout_7.setNextFocusDownId(matrix_education_layout_7.getId());
		hashMap.put(matrix_education_layout_1, "8801");
		hashMap.put(matrix_education_layout_2, "8802");
		hashMap.put(matrix_education_layout_3, "8803");
		hashMap.put(matrix_education_layout_4, "8804");
		hashMap.put(matrix_education_layout_5, "8805");
		hashMap.put(matrix_education_layout_6, "8806");
		hashMap.put(matrix_education_layout_7, "8807");
		

	}

	/**
	 * @Fields bottomViewKeyListener:view 上的key事件
	 */
	private OnKeyListener bottomViewKeyListener = new OnKeyListener() {

		public boolean onKey(View v, int keyCode, KeyEvent event) {

			if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) && event.getAction() == KeyEvent.ACTION_DOWN) {
				if (null != educationViews && !educationViews.isEmpty()) {
					int viewIndex = educationViews.indexOf(v);
					if (viewIndex < educationViews.size() - 1 && educationViews.get(viewIndex + 1).getVisibility() == View.INVISIBLE) {
						if (null != pageChange) {
							if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {

							} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
								pageChange.previousPage();
							}

							Log.d(TAG, "getShowPageIndex::" + getShowPageIndex() + "||getPages::" + getPages());

						}
					} else if ((v.getId() == matrix_education_layout_1.getId() || v.getId() == matrix_education_layout_6.getId())
							&& keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) {

					}
				}
			}

			if (v.getId() == views[btnIndex].getId()) {
				if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
					// 一般的轮播推荐位失去焦点的动画效果
					AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.5f);
					alphaAnim.setDuration(100);
					alphaAnim.setInterpolator(new LinearInterpolator());
					v.startAnimation(alphaAnim);
				}
				if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.getAction() == KeyEvent.ACTION_UP) {
					v.bringToFront();
					// 一般的轮播推荐位获得焦点的动画效果
					AlphaAnimation alphaAnim = new AlphaAnimation(0.5f, 1.0f);
					alphaAnim.setDuration(100);
					alphaAnim.setInterpolator(new LinearInterpolator());
					v.startAnimation(alphaAnim);
				}
			}
			return false;
		}

	};

	/**
	 * 推荐位的焦点变化监听
	 */
	private OnFocusChangeListener recommendLayoutFocusListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View view, boolean isFocused) {

			if (isFocused) {
	/*			if (view.getId() == views[0].getId() || view.getId() == views[5].getId()) {
					focusRules();
				}*/
				view.bringToFront();
				// 一般的轮播推荐位获得焦点的动画效果
				AnimationUtil.loadBigAnimation(view);
				AlphaAnimation alphaAnim = new AlphaAnimation(0.9f, 1.0f);
				alphaAnim.setDuration(100);
				alphaAnim.setInterpolator(new LinearInterpolator());
				view.startAnimation(alphaAnim);
			} else {
				// 一般的轮播推荐位失去焦点的动画效果
				AnimationUtil.loadSmallAnimation(view);
			}

		}
	};

	/**
	 * @Title: MatrixEducationView
	 * @Description: 补齐页面
	 */
	public void setPageChange() {
		try {
			if (getShowPageIndex() == getPages()) {

				// 仅仅是一个延迟线程，不然焦点不会跳转到下一页item上
				matrixEducationHandler.postDelayed(new Runnable() {
					public void run() {
//						viewfliperNext();
						// 设置焦点走向
						focusRules();
						setBottomViewNextFocus();
					}
				}, viewVisibleTime);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadData(boolean isRefleshData) {
		int count = 0;
		do {
			educationList = recommendDAO.queryAppFocus(null, null, null, null);
			Log.d("TAG", "获得接口数据==》》"+educationList.size());
			if (educationList.size() > 0 || count == readDataFromDBCount) {
				break;
			}
			count++;
			try {
				Thread.sleep(readDataFromDBInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (educationList.size() == 0);

		if (null != educationList && educationList.size() > 0) {
			checkPageIsIdle();
		} else {
			sendLoadDataResultMessage(LOAD_DATA_FAIL);
		}

	}

	/**
	 * 设置教育上的推荐位数据
	 * 
	 * @param movieList
	 */
	
	public void setMatrixData() {
		try {
			int count = educationList.size();

			android.view.ViewGroup.LayoutParams params = new android.view.ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			for (int i = 0; i < count; i++) {
				ArrayList<AppFocusEntity> subList = educationList.get(i);// 获取每个轮播推荐位的数据集合
				int dataSize = subList.size();
				int viewSize = viewFliper[i].getChildCount();
				if (dataSize > viewSize) {// 当推荐位数据增加时，补充显示推荐位的View
					Log.d(TAG, "game pageview " + i + " position recommend data add");
					for (int j = 0; j < dataSize - viewSize; j++) {
						AppRecommendView recommendView = new AppRecommendView(getContext());
						viewFliper[i].addView(recommendView, params);
					}
				} else if (dataSize < viewSize) {// 当推荐位数据减少时，减少显示推荐位的View
					Log.d(TAG, "game pageview " + i + " position recommend data reduce");
					for (int j = dataSize; j < viewSize; j++) {
						if (null != viewFliper[i] && viewFliper[i].getChildCount() > 0) {
							viewFliper[i].removeViewAt(j);
						}
					}
				}

				// 改变推荐位的轮播状态
				if (subList.size() > 0) {
					changeFlipperFlipping(subList.size(), viewFliper[i], subList.get(0).getIntervalTime());
				}

				for (int k = 0; k < dataSize; k++) {// 填充到轮播的ViewFliper
					AppFocusEntity newEntity = subList.get(k);
					AppFocusEntity oldEntity = null;

					AppRecommendView recommendView = (AppRecommendView) viewFliper[i].getChildAt(k);

					if (null != recommendView.getTag()) {
						oldEntity = (AppFocusEntity) recommendView.getTag();
					}

					if (null == oldEntity || oldEntity.getContentId() != newEntity.getContentId()
							|| !oldEntity.getFocusLargeImg().equals(newEntity.getFocusLargeImg())) {
						recommendView.setTag(newEntity);
						ImageLoader.getInstance().displayImage(newEntity.getFocusLargeImg(), recommendView.getImageView(), options);
						recommendView.initProgressView(viewFliper[i].getMeasuredWidth() - 50);
						mapDownload.put(newEntity.getContentId(), recommendView);
					}
				}
			}
			this.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getCinemaName() {
		return educationName;
	}

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
	/*
	 * 
	 * 下载安装进度广播
	 * 
	 */
	@Override
	public void onAppStoreReceive(Intent intent) {
//		int category_id = intent.getIntExtra("category_id", 0);
		int category_id = 3;
		Log.d(TAG, "打印catgray_id。。。。。="+category_id);
		if (category_id == 3) {// 游戏类型
			int id = intent.getIntExtra("app_id", 0);
			AppRecommendView recommendView = mapDownload.get(id);
			if (HomeActions.ACTION_DOWNLOAD.equals(intent.getAction())) {
				// 应用下载进度广播

				if (null != recommendView) {
					int progress = intent.getIntExtra("download_progress", 0);
					recommendView.setProgress(progress);// 更新进度
					Log.d(TAG, "打印进度。。。。。="+progress);
				}
			} else if (HomeActions.ACTION_DOWNLOAD_PAUSE.equals(intent.getAction())) {
				if (null != recommendView) {
					recommendView.hideDownloadProgressView();
				}
			} else if (HomeActions.ACTION_INSTALL_APP_SUCCESS.equals(intent.getAction())
					|| HomeActions.ACTION_INSTALL_APP_FAIL.equals(intent.getAction())) {
				// 应用安装成功或失败
				System.out.println("安装成功：id：" + id);
				if (null != recommendView) {
					recommendView.hideInstallProgressView();// 隐藏安装进度
				}
			}
		}
	}

	/*
	 * 底部菜单栏
	 */
	public View getBottomMenuView() {
		bottomMenuViews = new View[] { matrix_education_layout_1 };

		viewFocusDirectionListener.setButtomMenuViewFocusDirection(bottomMenuViews);
		return new View(context);
	}


	/**
	 * 响应观看记录点击事件 点击后进入
	 */
	class CinemaRecordClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), PlayerRecordActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_record_text))
					.setSenceName(AppScene.getScene()).setViewPosition("4410").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应收藏点击事件 点击后进入
	 */
	class CinemaFavouriteClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), CollectActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_favourite_text))
					.setSenceName(AppScene.getScene()).setViewPosition("4409").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应已安装应用游戏记录点击事件 点击后进入
	 */
	class CinemaInstalledClickListener extends SimpleOnClickListener {
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
					.setSenceName(AppScene.getScene()).setViewPosition("4411").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应搜索点击事件 点击后进入
	 */
	class CinemaSearchClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), SearchHomeActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_search_text))
					.setSenceName(AppScene.getScene()).setViewPosition("4407").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应系统设置点击事件 点击后进入
	 */
	class CinemaSettingClickListener extends SimpleOnClickListener {
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
					.setSenceName(AppScene.getScene()).setViewPosition("4412").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应用户中心点击事件 点击后进入
	 */
	// class CinemaUserClickListener extends SimpleOnClickListener {
	// @Override
	// public DataHolder doOnClick(View view) {
	// Intent intent = new Intent();
	// intent.setAction("com.hiveview.user.usercenter");
	// intent.addCategory(Intent.CATEGORY_DEFAULT);
	// getContext().startActivity(intent);
	//
	// return new
	// DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON).setButton(String.valueOf(view.getId()),
	// getResources().getString(R.string.sub_navigation_common_user_text))
	// .setSenceName(AppScene.getScene()).setViewPosition("4413").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
	// }
	// }

	/**
	 * 响应外接媒体点击事件 点击后进入
	 */
	class CinemaExternalClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			AppUtil.openApp("com.hiveview.externalstorage", getContext());
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_external_text))
					.setSenceName(AppScene.getScene()).setViewPosition("4414").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * @Title: MatrixEducationView
	 * @Description: 底部菜单的焦点走向
	 */
	private void setBottomViewNextFocus() {
//		cinemaExternalLayout.setNextFocusUpId((matrix_cinema_layout_10.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_10.getId()
//				: (matrix_cinema_layout_5.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_5.getId() : R.id.navigation_tab_movie_text);
//		// cinemaUserLayout.setNextFocusUpId((matrix_cinema_layout_9.getVisibility()
//		// == View.VISIBLE) ? matrix_cinema_layout_9.getId()
//		// : (matrix_cinema_layout_4.getVisibility() == View.VISIBLE) ?
//		// matrix_cinema_layout_4.getId() : R.id.navigation_tab_movie_text);
//		cinemaSettingLayout.setNextFocusUpId((matrix_cinema_layout_8.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_8.getId()
//				: (matrix_cinema_layout_3.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_3.getId() : R.id.navigation_tab_movie_text);
//		cinemaSearchLayout.setNextFocusUpId((matrix_cinema_layout_8.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_8.getId()
//				: (matrix_cinema_layout_3.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_3.getId() : R.id.navigation_tab_movie_text);
//		cinemaInstalledLayout.setNextFocusUpId((matrix_cinema_layout_7.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_7.getId()
//				: (matrix_cinema_layout_2.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_2.getId() : R.id.navigation_tab_movie_text);
//		cinemaFavouriteLayout.setNextFocusUpId(matrix_cinema_layout_6.getId());
//		cinemaRecordLayout.setNextFocusUpId(matrix_cinema_layout_6.getId());
	}

	@Override
	public ViewGroup getTopMenuView() {
		ViewGroup movieTabView = (ViewGroup) inflate(getContext(), R.layout.top_menu_education_layout, null);
		TextView topTabTextView = (TextView) movieTabView.findViewById(R.id.navigation_tab_education_text);
		ImageView topFadeTabImageView = (ImageView) movieTabView.findViewById(R.id.navigation_tab_education_fade);
		topTabTextView.setOnFocusChangeListener(new TopTabOnFocusChangeListener(topTabTextView, topFadeTabImageView));
		topTabTextView.setNextFocusDownId(R.id.matrix_education_layout_1);
		topTabTextView.setNextFocusUpId(R.id.navigation_tab_education_text);

		return movieTabView;
	}

	/**
	 * @ClassName: RecommendLayoutClickListener
	 * @Description: 获取viewfilper 页码
	 */
	public int getShowPageIndex() {
		Log.d(TAG, "viewIndex==>" + viewIndex % (pages + 1));
		return viewIndex % (pages + 1);
	}

	public int getPages() {
		return pages;
	}
	HashMap<View, String> hashMap = new HashMap<View, String>();
	/**
	 * 响应点击事件 点击后进入 详情页
	 */
	class EducationLayoutClickListener extends SimpleOnClickListener {

		@Override
		public DataHolder doOnClick(View view) {
			// 确定用户当前点击的是那个位置的ViewFliper
			int index = -1;
			for (int i = 0; i < views.length; i++) {
				if (view == views[i]) {
					index = i;
					break;
				}

			}

			AppFocusEntity entity = null;
			if (index >= 0 && index < viewFliper.length) {// 轮播推荐位
				if (viewFliper[index].getCurrentView() == null || viewFliper[index].getChildCount() == 0)
					return null;
				entity = (AppFocusEntity) viewFliper[index].getCurrentView().getTag();

				if (null == entity) {
					return null;
				}

				String action = ContentInvoker.getInstance().getContentAction(entity.getFocusType());
				if (!TextUtils.isEmpty(action)) {
					Intent intent = new Intent(action);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					
					if (action.equals(ContentInvoker.CONTENT_ACTION_APP_ALBUM)) {// 应用专题的Action
						intent.putExtra("subject_id", entity.getContentId());
						intent.putExtra("categoryId","3");
						intent.putExtra("subjectPositionId", null != hashMap.get(view) ? hashMap.get(view).toString() : "");
					} else {// 应用详情的Action
						intent.putExtra("appid", entity.getContentId());
					}
					getContext().startActivity(intent);
					if (action.equals(ContentInvoker.CONTENT_ACTION_APP_ALBUM)) {
						return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_APP_GAME_RECOMMAND).setEntity(entity)
								.setSenceName(AppScene.getScene()).setSrcType(ItemType.SUBJECT).setTabNo(Tab.EDUCATION)
								.setViewPosition(null != hashMap.get(view) ? hashMap.get(view).toString() : "").build();
					} else {
						return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_GAME_RECOMMAND).setEntity(entity)
								.setSenceName(AppScene.getScene()).setSrcType(ItemType.EDUCATION).setTabNo(Tab.EDUCATION)
								.setViewPosition(null != hashMap.get(view) ? hashMap.get(view).toString() : "").build();
					}
				}

			} else {
				if (view == matrix_education_layout_7) { // 教育中心
					Log.d("TAG", ">>>>>>>>>hahahahha");
					Intent intent = new Intent();
					intent.setAction("com.hiveview.appstore.main");
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.putExtra("category_id", 3);
					intent.putExtra("app_type", 1);
					getContext().startActivity(intent);
				}
			}
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_EDUCATION_BUTTON)
					.setButton(String.valueOf(view.getId()), null == idHashMap.get(view.getId()) ? "" : idHashMap.get(view.getId()))
					.setSenceName(AppScene.getScene()).setSrcType(ItemType.BUTTON).setTabNo(Tab.EDUCATION)
					.setViewPosition(null != hashMap.get(view) ? hashMap.get(view).toString() : "").build();
			
		}

	
	}

	public void viewfliperNext() {
		try {
			viewIndex++;
			for (int i = 0; i < viewFliper.length; i++) {
				if (i == btnIndex) {
					continue;
				}
				if (viewfliperData[i][0] > getShowPageIndex()) {
					views[i].setVisibility(View.VISIBLE);
					viewFliper[i].showNext();
					viewFliper[i].clearAnimation();
					viewFliper[i].stopFlipping();
				} else if (viewfliperData[i][0] == getShowPageIndex()) {
					views[i].setVisibility(View.INVISIBLE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @ClassName: PageChange
	 * @Description: 需要调用翻页的接口
	 * 
	 */
	public interface PageChange {
		public void nextPage();

		public void previousPage();
	}
	
	
	
	
	/**
	 * @Title: MatrixCinemaView
	 * @author:张鹏展
	 * @Description: 语音控制打开对应的频道
	 * @param value
	 */
//	public void openItemActivity(String value){
//		try{
//			int entityIndex = educationName.indexOf(value);
//			AppFocusEntity entity = educationList.get(entityIndex);
//
//			String action = ChannelInvoker.getInstance().getContent(((AppFocusEntity) entity).getShow_type());
//			if (null != action) {
//				try {
//					Intent intent = new Intent(action);
//					intent.addCategory(Intent.CATEGORY_DEFAULT);
//					intent.putExtra("category_id", ((AppFocusEntity) entity).getFirstclass_id());
//					intent.putExtra("category_name", ((AppFocusEntity) entity).getFirstclass_name());
//					Log.d(TAG, "category_name::" + ((AppFocusEntity) entity).getFirstclass_name());
//					context.startActivity(intent);
//				} catch (Exception e) {
//					e.printStackTrace();
//					return;
//				}
//			}
//		/*KeyEventHandler.post(new DataHolder.Builder(context)
//				.setTabNo(Tab.FILM)
//				.setSrcType(ItemType.CLASS)
//				.setMovieDemandQueryInfo(String.valueOf(((FirstClassListEntity) entity).getFirstclass_name()),
//						String.valueOf(((FirstClassListEntity) entity).getFirstclass_id()))
//				.setDataType(DataType.TIME_LENGTH_MOVIE_DEMAND_LIST).setViewPosition(String.valueOf(cinemaNo[index][getShowPageIndex()]))
//				.setPositionId(String.valueOf(view.getId())).build());*/
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
}
