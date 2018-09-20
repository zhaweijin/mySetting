package com.hiveview.tv.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.CollectActivity;
import com.hiveview.tv.activity.PlayerRecordActivity;
import com.hiveview.tv.activity.SearchHomeActivity;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.GetStorageListener;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.StorageAdapter;
import com.hiveview.tv.common.content.ContentInvoker;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.ItemType;
import com.hiveview.tv.common.statistics.SimpleOnClickListener;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.service.dao.AppFocusDAO;
import com.hiveview.tv.service.entity.AppFocusEntity;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.utils.AnimationUtil;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.DeviceSN;
import com.hiveview.tv.utils.InitAppCountUtils;
import com.hiveview.tv.utils.ToastUtils;
import com.hiveview.tv.view.pager3d.HomeActions;
import com.hiveview.tv.view.pager3d.TabAppBasePageView;

@SuppressLint("UseSparseArrays")
public class MatrixAppView extends TabAppBasePageView {

	private static final int APP_FOCUS_COUNT = 5;

	/**
	 * 应用页面的推荐位Layout
	 */
	private View matrix_app_layout_0, matrix_app_layout_1, matrix_app_layout_2, matrix_app_layout_3, matrix_app_layout_4, matrix_app_layout_5,
			matrix_app_layout_6;

	/**
	 * 推荐位轮播的ViewFlipper
	 */
	private ViewFlipper matrix_app_flipper_0, matrix_app_flipper_1, matrix_app_flipper_2, matrix_app_flipper_3,matrix_app_flipper_4;

	/**
	 * 应用页面的推荐位Layout数组
	 */
	private View[] appLayouts;

	/**
	 * H 显示安装应用个数的TextView
	 */
	private TextView tvInstallCount;

	/**
	 * 显示使用存储空间的TextView
	 */
	private TextView installMemory;

	private String storageInfo = null;

	/**
	 * 显示使用存储空间的进度的ProgressBar
	 */
	private ProgressBar installMemoryBar;

	private int usageRate = 0;

	/**
	 * 已安装的应用个数
	 */
	private int installAppCount = 0;

	private boolean isInitAppCount = false;

	private AppFocusDAO appFocusDAO = null;

	ArrayList<ArrayList<AppFocusEntity>> appList = null;

	private final int LOAD_DATA_SUCCESS = 100;

	private final int LOAD_DATA_FAIL = -100;
	private HashMap<Integer, AppRecommendView> mapDownloadView = new HashMap<Integer, AppRecommendView>();
	/**
	 * @Fields hashMap:每个可点击位置上埋点id
	 */
	private HashMap<View, String> hashMap = new HashMap<View, String>();
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			/*
			 * switch (msg.what) { case 0:
			 * tvInstallCount.setText(String.format(getResources
			 * ().getString(R.string.install_app_count), installAppCount));
			 * break; case -1: installMemory.setText(storageInfo);
			 * installMemoryBar.setMax(100);
			 * installMemoryBar.setProgress(usageRate);
			 * tvInstallCount.setText(String
			 * .format(getResources().getString(R.string.install_app_count),
			 * installAppCount)); break; default: break; }
			 */
		};
	};

	public MatrixAppView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MatrixAppView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MatrixAppView(Context context, RecommendViewFocusDirectionListener listener) {
		super(context, listener);
		init();
	}

	public MatrixAppView(Context context) {
		super(context);
		init();
	}

	/**
	 * view 的 id 和文字的映射
	 * 
	 * @Fields idHashMap
	 */
	HashMap<Integer, String> idHashMap = new HashMap<Integer, String>();

	/**
	 * 初始化推荐位相关的View
	 */
	protected void init() {
		long time = System.currentTimeMillis();
		appFocusDAO = new AppFocusDAO(getContext());

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		View view = inflate(getContext(), R.layout.matrix_app_view, null);
		this.addView(view, params);

		// 初始化推荐位Layout
		matrix_app_layout_0 = findViewById(R.id.matrix_app_layout_0);
		matrix_app_layout_1 = findViewById(R.id.matrix_app_layout_1);
		matrix_app_layout_2 = findViewById(R.id.matrix_app_layout_2);
		matrix_app_layout_3 = findViewById(R.id.matrix_app_layout_3);
		matrix_app_layout_4 = findViewById(R.id.matrix_app_layout_4);
		matrix_app_layout_5 = findViewById(R.id.matrix_app_layout_5);
		matrix_app_layout_6 = findViewById(R.id.matrix_app_layout_6);

		idHashMap.put(R.id.matrix_app_layout_5, getResources().getString(R.string.search_app_rank));
		idHashMap.put(R.id.matrix_app_layout_6, getResources().getString(R.string.search_app_store));

		// 初始化推荐位轮播的ViewFlipper
		matrix_app_flipper_0 = (ViewFlipper) findViewById(R.id.matrix_app_flipper_0);
		matrix_app_flipper_1 = (ViewFlipper) findViewById(R.id.matrix_app_flipper_1);
		matrix_app_flipper_2 = (ViewFlipper) findViewById(R.id.matrix_app_flipper_2);
		matrix_app_flipper_3 = (ViewFlipper) findViewById(R.id.matrix_app_flipper_3);
		matrix_app_flipper_4 = (ViewFlipper) findViewById(R.id.matrix_app_flipper_4);
		// 初始化轮播推荐位ViewFlipper的数组
		flippers = new ViewFlipper[] { matrix_app_flipper_0, matrix_app_flipper_1, matrix_app_flipper_2, matrix_app_flipper_3,matrix_app_flipper_4 };

		recommendEdgeViews = new View[] { matrix_app_layout_0, matrix_app_layout_4, matrix_app_layout_5, matrix_app_layout_6 };

		// 初始化推荐位Layout数组
		appLayouts = new View[] { matrix_app_layout_0, matrix_app_layout_1, matrix_app_layout_2, matrix_app_layout_3, matrix_app_layout_4,
				matrix_app_layout_5, matrix_app_layout_6 };

		// 推荐位绑定点击事件
		for (int i = 0; i < appLayouts.length; i++) {
			// appLayouts[i].setOnClickListener(appLayoutClickListener);
			appLayouts[i].setOnClickListener(new RecommendLayoutClickListener());
			appLayouts[i].setOnFocusChangeListener(recommendLayoutFocusListener);
		}

		// 轮播推荐位动画
		for (int i = 0; i < flippers.length; i++) {
			flippers[i].setInAnimation(getContext(), R.anim.down_in);
			flippers[i].setOutAnimation(getContext(), R.anim.down_out);
		}

		// 推荐位0位置的焦点方向
		matrix_app_layout_0.setNextFocusUpId(R.id.navigation_tab_app_text);
		matrix_app_layout_0.setNextFocusRightId(R.id.matrix_app_layout_1);
		matrix_app_layout_0.setNextFocusLeftId(R.id.matrix_education_layout_5);
		matrix_app_layout_0.setNextFocusDownId(R.id.matrix_app_layout_0);

		// 推荐位1位置的焦点方向
		matrix_app_layout_1.setNextFocusUpId(R.id.navigation_tab_app_text);
		matrix_app_layout_1.setNextFocusRightId(R.id.matrix_app_layout_4);
		matrix_app_layout_1.setNextFocusLeftId(R.id.matrix_app_layout_0);
		matrix_app_layout_1.setNextFocusDownId(R.id.matrix_app_layout_2);

		// 推荐位2位置的焦点方向
		matrix_app_layout_2.setNextFocusUpId(R.id.matrix_app_layout_1);
		matrix_app_layout_2.setNextFocusRightId(R.id.matrix_app_layout_3);
		matrix_app_layout_2.setNextFocusLeftId(R.id.matrix_app_layout_0);
		//焦点指向 下方工具栏收起
		matrix_app_layout_2.setNextFocusDownId(matrix_app_layout_2.getId());

		// 推荐位3位置的焦点方向
		matrix_app_layout_3.setNextFocusUpId(R.id.matrix_app_layout_1);
		matrix_app_layout_3.setNextFocusRightId(R.id.matrix_app_layout_5);
		matrix_app_layout_3.setNextFocusLeftId(R.id.matrix_app_layout_2);
		matrix_app_layout_3.setNextFocusDownId(matrix_app_layout_3.getId());

		// 推荐位4位置的焦点方向
		matrix_app_layout_4.setNextFocusUpId(R.id.navigation_tab_app_text);
		matrix_app_layout_4.setNextFocusRightId(R.id.matrix_game_layout_0);
		matrix_app_layout_4.setNextFocusLeftId(R.id.matrix_app_layout_1);
		matrix_app_layout_4.setNextFocusDownId(R.id.matrix_app_layout_5);

		// 推荐位5位置的焦点方向
		matrix_app_layout_5.setNextFocusUpId(R.id.matrix_app_layout_4);
		matrix_app_layout_5.setNextFocusRightId(R.id.matrix_game_layout_0);
		matrix_app_layout_5.setNextFocusLeftId(R.id.matrix_app_layout_3);
		matrix_app_layout_5.setNextFocusDownId(R.id.matrix_app_layout_6);

		// 推荐位6位置的焦点方向
		matrix_app_layout_6.setNextFocusUpId(R.id.matrix_app_layout_5);
		matrix_app_layout_6.setNextFocusRightId(R.id.matrix_game_layout_0);
		matrix_app_layout_6.setNextFocusLeftId(R.id.matrix_app_layout_3);
		matrix_app_layout_6.setNextFocusDownId(matrix_app_layout_6.getId());

		hashMap.put(matrix_app_layout_0, "5501");
		hashMap.put(matrix_app_layout_1, "5502");
		hashMap.put(matrix_app_layout_2, "5503");
		hashMap.put(matrix_app_layout_3, "5504");
		hashMap.put(matrix_app_layout_4, "5505");
		hashMap.put(matrix_app_layout_5, "5509");
		hashMap.put(matrix_app_layout_6, "5507");

		super.init();
		Log.d(TAG, "MatrixAppView::init  " + (System.currentTimeMillis() - time));
	}

	/**
	 * 响应点击事件 点击后进入 详情页
	 */
	class RecommendLayoutClickListener extends SimpleOnClickListener {

		@Override
		public DataHolder doOnClick(View view) {
			// 确定用户当前点击的是那个位置的ViewFliper
			int index = -1;
			for (int i = 0; i < appLayouts.length; i++) {
				if (view == appLayouts[i]) {
					index = i;
					break;
				}
			}
			AppFocusEntity entity = null;
			if (index >= 0 && index < flippers.length) {// 点击的是 0-4位置的推荐位Layout

				if (flippers[index].getCurrentView() == null || flippers[index].getChildCount() == 0)
					return null;
				entity = (AppFocusEntity) flippers[index].getCurrentView().getTag();
				if (null == entity)
					return null;
				String action = ContentInvoker.getInstance().getContentAction(entity.getFocusType());
				//0是apk
				if ("0".equals(entity.getIsApk())) {
					Log.d(TAG, "openApp::true");
					AppUtil.openAppForPackageName(entity.getApkPackage(), getContext());
				} else
				if (!TextUtils.isEmpty(action)) {
					Intent intent = new Intent(action);
					intent.addCategory(Intent.CATEGORY_DEFAULT);

					if (action.equals(ContentInvoker.CONTENT_ACTION_APP_ALBUM)) {// 应用专题的Action
						intent.putExtra("subject_id", entity.getContentId());
						intent.putExtra("categoryId","2");
						intent.putExtra("subjectPositionId", null != hashMap.get(view) ? hashMap.get(view).toString() : "");
					} else { // 应用详情
							intent.putExtra("appid", entity.getContentId());
						
					}
					getContext().startActivity(intent);
			
				}
				return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_GAME_RECOMMAND).setEntity(entity)
						.setSenceName(AppScene.getScene()).setSrcType(ItemType.APP).setTabNo(Tab.APP)
						.setViewPosition(null != hashMap.get(view) ? hashMap.get(view).toString() : "").build();
			} else {
				/*
				 * if (view == matrix_app_layout_5) { // 点击已安装的应用 Intent intent
				 * = new Intent();
				 * intent.setAction("com.hiveview.appstore.buy");
				 * intent.addCategory(Intent.CATEGORY_DEFAULT);
				 * intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				 * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 * intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				 * intent.putExtra("category_id", 2);
				 * getContext().startActivity(intent); }
				 */
				if (view == matrix_app_layout_5) { // 点击的是排行榜
					Intent intent = new Intent();
					intent.setAction("com.hiveview.appstore.top");
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.putExtra("category_id", 2);
					getContext().startActivity(intent);
				}
				if (view == matrix_app_layout_6) { // 点击的是应用中心
					Intent intent = new Intent();
					intent.setAction("com.hiveview.appstore.main");
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.putExtra("category_id", 2);
					intent.putExtra("app_type", 1);
					getContext().startActivity(intent);
				}
			}

			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_APP_BUTTON)
					.setButton(String.valueOf(view.getId()), null == idHashMap.get(view.getId()) ? "" : idHashMap.get(view.getId()))
					.setSenceName(AppScene.getScene()).setSrcType(ItemType.BUTTON).setTabNo(Tab.APP)
					.setViewPosition(null != hashMap.get(view) ? hashMap.get(view).toString() : "").build();
		}

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

	@Override
	public void loadData(boolean isRefleshData) {
		int count = 0;
		do {
			appList = appFocusDAO.queryAppFocus(null, null, null, null);
			if (appList.get(0).size() > 0 || count == readDataFromDBCount) {
				break;
			}
			count++;
			try {
				Thread.sleep(readDataFromDBInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (appList.get(0).size() == 0);

		if (null != appList && appList.get(0).size() > 0) {
			checkPageIsIdle();
		} else {
			sendLoadDataResultMessage(LOAD_DATA_FAIL);
		}

	}

	@Override
	public void updateUIRefleshData() {
		sendLoadDataResultMessage(LOAD_DATA_SUCCESS);
	}

	/**
	 * 设置应用界面的推荐位数据
	 * 
	 * @param appList
	 */
	public void setMatrixData() {
		try {
			int count = APP_FOCUS_COUNT;// appList.size();

			android.view.ViewGroup.LayoutParams params = new android.view.ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			for (int i = 0; i < count; i++) {
				ArrayList<AppFocusEntity> subList = appList.get(i);// 获取每个轮播推荐位的数据集合
				int dataSize = subList.size();
				int viewSize = flippers[i].getChildCount();
				if (dataSize > viewSize) {// 当推荐位数据增加时，补充显示推荐位的View
					Log.d(TAG, "app pageview " + i + " position recommend data add");
					for (int j = 0; j < dataSize - viewSize; j++) {
						AppRecommendView recommendView = new AppRecommendView(getContext());
						flippers[i].addView(recommendView, params);
						Log.d(TAG, "app pageview " + i + " position childs count " + flippers[i].getChildCount());
					}
				} else if (dataSize < viewSize) {// 当推荐位数据减少时，减少显示推荐位的View
					Log.d(TAG, "app pageview " + i + " position recommend data reduce");
					for (int j = dataSize; j < viewSize; j++) {
						if (null != flippers[i] && flippers[i].getChildCount() > 0) {
							flippers[i].removeViewAt(j);
							Log.d(TAG, "app pageview " + i + " position childs count " + flippers[i].getChildCount());
						}
					}
				}

				// 改变推荐位的轮播状态
				if (subList.size() > 0) {
					changeFlipperFlipping(subList.size(), flippers[i], subList.get(0).getIntervalTime());
				}

				for (int k = 0; k < dataSize; k++) {// 填充到轮播的ViewFliper
					AppFocusEntity newEntity = subList.get(k);
					AppFocusEntity oldEntity = null;

					AppRecommendView recommendView = (AppRecommendView) flippers[i].getChildAt(k);

					if (null != recommendView.getTag()) {
						oldEntity = (AppFocusEntity) recommendView.getTag();
					}

					if (null == oldEntity || oldEntity.getContentId() != newEntity.getContentId()
							|| !oldEntity.getFocusLargeImg().equals(newEntity.getFocusLargeImg())) {
						recommendView.setTag(newEntity);
						ImageLoader.getInstance().displayImage(newEntity.getFocusLargeImg(), recommendView.getImageView(), options);
						recommendView.initProgressView(flippers[i].getMeasuredWidth() - 50);
						mapDownloadView.put(newEntity.getContentId(), recommendView);
					}

				}

			}

			getInstallAppCount();
			this.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	/**
	 * 得到应用在盒子上的安装信息以及存储空间信息
	 */
	public void getInstallAppCount() {
		HttpTaskManager.getInstance().submit(new Runnable() {

			@Override
			public void run() {
				if (!isInitAppCount) {
					InitAppCountUtils initAppCount = new InitAppCountUtils(getContext());
					installAppCount = initAppCount.getInstallAppsByListExceptGame(getContext()).size();
					isInitAppCount = true;
				}

				// 根据当前用户使用的盒子，适配获取存储空间信息
				StorageAdapter adapter = new StorageAdapter(DeviceSN.getDevice());
				GetStorageListener storageListener = adapter.getStorageInfo();
				if (null != storageListener) {
					Object[] objects = storageListener.getStorageInfo(getContext());
					usageRate = (Integer) objects[1];
					storageInfo = (String) objects[0];
				}
				mHandler.sendEmptyMessage(-1);
			}
		});
	}

	/**
	 * 应用推荐位的焦点变化监听
	 */
	private OnFocusChangeListener recommendLayoutFocusListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View view, boolean isFocused) {

			if (isFocused) {// 推荐位获得焦点的动画效果
				view.bringToFront();
//				if (view.getId() == R.id.matrix_app_layout_0) {//
//					// 第2个位置的推荐位，可露头的推荐位，动画要特殊处理
//					findViewById(R.id.matrix_app_flipper_0_out_layout).bringToFront();
//					AnimationUtil.loadBigAnimation(findViewById(R.id.matrix_app_flipper_0_out_layout));
//				}
				AnimationUtil.loadBigAnimation(view);

				AlphaAnimation alphaAnim = new AlphaAnimation(0.9f, 1.0f);
				alphaAnim.setDuration(100);
				alphaAnim.setInterpolator(new LinearInterpolator());
				view.startAnimation(alphaAnim);
				Log.v(TAG, "mcurrentfocus="+view.getId());
				HiveviewApplication.mcurrentfocus=view;
			} else {// 推荐位失去焦点的动画效果
				AnimationUtil.loadSmallAnimation(view);
//				if (view.getId() == R.id.matrix_app_layout_0) {// 第1个位置露头轮播推荐位失去焦点的动画效果
//					AnimationUtil.loadSmallAnimation(findViewById(R.id.matrix_app_flipper_0_out_layout));
//				}
			}

		}
	};

	public void setGameInstallCount(int count) {
		installAppCount = count;
		mHandler.sendEmptyMessage(0);
		postDelayed(new Runnable() {

			@Override
			public void run() {
				getInstallAppCount();
			}
		}, 5000);

	}

	@Override
	public void onAppStoreReceive(Intent intent) {
//		int category_id = intent.getIntExtra("category_id", 0);
//		if (category_id == 2) {// 应用类型
			int id = intent.getIntExtra("app_id", 0);
			AppRecommendView recommendView = mapDownloadView.get(id);
			// ViewFlipper flipper = mapDownloadFlipper.get(id);
			if (HomeActions.ACTION_DOWNLOAD.equals(intent.getAction())) {
				// 应用下载进度广播
				if (null != recommendView) {
					int progress = intent.getIntExtra("download_progress", 0);
					recommendView.setProgress(progress);// 更新进度,方法内已判断进度条是否显示，没显示自动显示，丽晓大神GJ！
				}
			} else if (HomeActions.ACTION_DOWNLOAD_PAUSE.equals(intent.getAction())) {
				if (null != recommendView) {
					recommendView.hideDownloadProgressView();
				}
			} else if (HomeActions.ACTION_INSTALL_APP_SUCCESS.equals(intent.getAction())
					|| HomeActions.ACTION_INSTALL_APP_FAIL.equals(intent.getAction())) {
				// 应用安装成功或失败
				if (null != recommendView) {
					// flipper.startFlipping();
					// for (int k = 0; k < flipper.getChildCount(); k++) {
					// flipper.getChildAt(k).clearAnimation();
					// }
					recommendView.hideInstallProgressView();// 隐藏安装进度
				}
			}
//		}
	}

	@Override
	public View getBottomMenuView() {

		View appTabView = inflate(getContext(), R.layout.sub_navigation_common_app, null);
		View appRecordLayout = appTabView.findViewById(R.id.sub_navigation_common_app_record_text_layout);
		View appFavouriteLayout = appTabView.findViewById(R.id.sub_navigation_common_app_favourite_text_layout);
		View appInstalledLayout = appTabView.findViewById(R.id.sub_navigation_common_app_installed_text_layout);
		View appSearchLayout = appTabView.findViewById(R.id.sub_navigation_common_app_search_text_layout);
		View appSettingLayout = appTabView.findViewById(R.id.sub_navigation_common_app_setting_text_layout);
		// View appUserLayout =
		// appTabView.findViewById(R.id.sub_navigation_common_app_user_text_layout);
		View appExternalLayout = appTabView.findViewById(R.id.sub_navigation_common_app_external_text_layout);

		appRecordLayout.setOnClickListener(new appRecordClickListener());
		appFavouriteLayout.setOnClickListener(new appFavouriteClickListener());
		appInstalledLayout.setOnClickListener(new appInstalledClickListener());
		appSearchLayout.setOnClickListener(new appSearchClickListener());
		appSettingLayout.setOnClickListener(new appSettingClickListener());
		// appUserLayout.setOnClickListener(new appUserClickListener());
		appExternalLayout.setOnClickListener(new appExternalClickListener());

		appRecordLayout.setNextFocusUpId(R.id.matrix_app_layout_4);
		appFavouriteLayout.setNextFocusUpId(R.id.matrix_app_layout_4);
		appInstalledLayout.setNextFocusUpId(R.id.matrix_app_layout_2);
		appSearchLayout.setNextFocusUpId(R.id.matrix_app_layout_2);
		appSettingLayout.setNextFocusUpId(R.id.matrix_app_layout_3);
		// appUserLayout.setNextFocusUpId(R.id.matrix_app_layout_6);
		appExternalLayout.setNextFocusUpId(R.id.matrix_app_layout_6);

		bottomMenuViews = new View[] {matrix_app_layout_0 };// ,																			// appUserLayout
		viewFocusDirectionListener.setButtomMenuViewFocusDirection(bottomMenuViews);

		return appTabView;
	}

	@Override
	public ViewGroup getTopMenuView() {
		ViewGroup appTabView = (ViewGroup) inflate(getContext(), R.layout.top_menu_app_layout, null);
		TextView topTabTextView = (TextView) appTabView.findViewById(R.id.navigation_tab_app_text);
		ImageView topFadeTabImageView = (ImageView) appTabView.findViewById(R.id.navigation_tab_app_fade);
		topTabTextView.setOnFocusChangeListener(new TopTabOnFocusChangeListener(topTabTextView, topFadeTabImageView));

		topTabTextView.setNextFocusDownId(R.id.matrix_app_layout_0);
		topTabTextView.setNextFocusUpId(R.id.navigation_tab_app_text);

		return appTabView;
	}

	/**
	 * 响应观看记录点击事件 点击后进入
	 */
	class appRecordClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), PlayerRecordActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_record_text))
					.setSenceName(AppScene.getScene()).setViewPosition("5513").setSrcType(ItemType.BUTTON).setTabNo(Tab.APP).build();
		}
	}

	/**
	 * 响应收藏点击事件 点击后进入
	 */
	class appFavouriteClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), CollectActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_favourite_text))
					.setSenceName(AppScene.getScene()).setViewPosition("5514").setSrcType(ItemType.BUTTON).setTabNo(Tab.APP).build();
		}
	}

	/**
	 * 响应已安装应用游戏记录点击事件 点击后进入
	 */
	class appInstalledClickListener extends SimpleOnClickListener {
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
					.setSenceName(AppScene.getScene()).setViewPosition("5515").setSrcType(ItemType.BUTTON).setTabNo(Tab.APP).build();
		}
	}

	/**
	 * 响应搜索点击事件 点击后进入
	 */
	class appSearchClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), SearchHomeActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_search_text))
					.setSenceName(AppScene.getScene()).setViewPosition("5516").setSrcType(ItemType.BUTTON).setTabNo(Tab.APP).build();
		}
	}

	/**
	 * 响应系统设置点击事件 点击后进入
	 */
	class appSettingClickListener extends SimpleOnClickListener {
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
					.setSenceName(AppScene.getScene()).setViewPosition("5517").setSrcType(ItemType.BUTTON).setTabNo(Tab.APP).build();
		}
	}

	/**
	 * 响应用户中心点击事件 点击后进入
	 */
	// class appUserClickListener extends SimpleOnClickListener {
	// @Override
	// public DataHolder doOnClick(View view) {
	// Intent intent = new Intent();
	// intent.setAction("com.hiveview.user.usercenter");
	// intent.addCategory(Intent.CATEGORY_DEFAULT);
	// getContext().startActivity(intent);
	//
	// return new DataHolder.Builder(getContext())
	// .setDataType(DataType.CLICK_TAB_BUTTON)
	// .setButton(String.valueOf(view.getId()),
	// getResources().getString(R.string.sub_navigation_common_user_text))
	// .setSenceName(AppScene.getScene()).setViewPosition("5511").setSrcType(ItemType.BUTTON).setTabNo(Tab.APP).build();
	// }
	// }

	/**
	 * 响应外接媒体点击事件 点击后进入
	 */
	class appExternalClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			AppUtil.openApp("com.hiveview.externalstorage", getContext());
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_external_text))
					.setSenceName(AppScene.getScene()).setViewPosition("5512").setSrcType(ItemType.BUTTON).setTabNo(Tab.APP).build();
		}
	}

}
