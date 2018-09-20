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
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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
import com.hiveview.tv.service.dao.GameFocusDAO;
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
public class MatrixGameView extends TabAppBasePageView {

	/**
	 * 推荐位Layout
	 */
	private View matrix_game_layout_0, matrix_game_layout_1, matrix_game_layout_2, matrix_game_layout_3, matrix_game_layout_4, matrix_game_layout_5,
			matrix_game_layout_6;

	/**
	 * 轮播推荐位的ViewFlipper
	 */
	private ViewFlipper matrix_game_flipper_0, matrix_game_flipper_1, matrix_game_flipper_2, matrix_game_flipper_3, matrix_game_flipper_4;

	/**
	 * 推荐位Layout数组
	 */
	private View[] views;

	/**
	 * 游戏安装数量
	 */
	private TextView tvInstallCount;

	/**
	 * 显示使用存储空间的TextView
	 */
	private TextView installMemory;

	/**
	 * 显示使用存储空间的进度的ProgressBar
	 */
	private ProgressBar installMemoryBar;

	private int usageRate = 0;

	/**
	 * 已安装的应用个数
	 */
	private int installGameCount = 0;

	private GameFocusDAO gameFocusDAO = null;

	ArrayList<ArrayList<AppFocusEntity>> gameList = null;

	private final int LOAD_DATA_SUCCESS = 100;

	private final int LOAD_DATA_FAIL = -100;

	private boolean isInitGameCount = false;

	private String storageInfo = null;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			/*
			 * switch (msg.what) { case 0:
			 * tvInstallCount.setText(String.format(getResources
			 * ().getString(R.string.install_game_count), installGameCount));
			 * break; case -1:
			 * tvInstallCount.setText(String.format(getResources(
			 * ).getString(R.string.install_game_count), installGameCount));
			 * installMemory.setText(storageInfo); installMemoryBar.setMax(100);
			 * installMemoryBar.setProgress(usageRate);
			 * 
			 * break; default: break; }
			 */

		};
	};

	public MatrixGameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MatrixGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MatrixGameView(Context context, RecommendViewFocusDirectionListener listener) {
		super(context, listener);
		init();
	}

	public MatrixGameView(Context context) {
		super(context);
		init();
	}

	HashMap<Integer, String> idHashMap = new HashMap<Integer, String>();

	protected void init() {
		gameFocusDAO = new GameFocusDAO(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		View view = inflate(getContext(), R.layout.matrix_game_view, null);

		this.addView(view, params);

		// 初始化推荐位Layout
		matrix_game_layout_0 = findViewById(R.id.matrix_game_layout_0);
		matrix_game_layout_1 = findViewById(R.id.matrix_game_layout_1);
		matrix_game_layout_2 = findViewById(R.id.matrix_game_layout_2);
		matrix_game_layout_3 = findViewById(R.id.matrix_game_layout_3);
		matrix_game_layout_4 = findViewById(R.id.matrix_game_layout_4);
		matrix_game_layout_5 = findViewById(R.id.matrix_game_layout_5);// 排行榜
		matrix_game_layout_6 = findViewById(R.id.matrix_game_layout_6);// 游戏中心

		idHashMap.put(R.id.matrix_game_layout_5, "排行榜");
		idHashMap.put(R.id.matrix_game_layout_6, "游戏中心");

		// 初始化轮播的推荐位
		matrix_game_flipper_0 = (ViewFlipper) findViewById(R.id.matrix_game_flipper_0);
		matrix_game_flipper_1 = (ViewFlipper) findViewById(R.id.matrix_game_flipper_1);
		matrix_game_flipper_2 = (ViewFlipper) findViewById(R.id.matrix_game_flipper_2);
		matrix_game_flipper_3 = (ViewFlipper) findViewById(R.id.matrix_game_flipper_3);
		matrix_game_flipper_4 = (ViewFlipper) findViewById(R.id.matrix_game_flipper_4);

		/*
		 * // 初始化显示存储空间和安装游戏数量的View tvInstallCount = (TextView)
		 * findViewById(R.id.matrix_game_install_count); installMemory =
		 * (TextView) findViewById(R.id.matrix_game_memory_used);
		 * installMemoryBar = (ProgressBar)
		 * findViewById(R.id.matrix_game_memory_used_progress);
		 */

		// 初始化轮播推荐位ViewFlipper数组
		flippers = new ViewFlipper[] { matrix_game_flipper_0, matrix_game_flipper_1, matrix_game_flipper_2, matrix_game_flipper_3,
				matrix_game_flipper_4 };

		for (int i = 0; i < flippers.length; i++) {// 轮播推荐位动画
			flippers[i].setInAnimation(getContext(), R.anim.down_in);
			flippers[i].setOutAnimation(getContext(), R.anim.down_out);
		}

		// 初始化轮播推荐位Layout
		views = new View[] { matrix_game_layout_0, matrix_game_layout_1, matrix_game_layout_2, matrix_game_layout_3, matrix_game_layout_4,
				matrix_game_layout_5, matrix_game_layout_6,  };

		recommendEdgeViews = new View[] { matrix_game_layout_0, matrix_game_layout_5, matrix_game_layout_6, matrix_game_layout_4 };

		// 绑定轮播推荐的点击和焦点改变事件
		for (int i = 0; i < views.length; i++) {
			views[i].setOnClickListener(new GameLayoutClickListener());
			views[i].setOnFocusChangeListener(recommendLayoutFocusListener);
		}

		// 推荐位0位置的焦点方向设置
		matrix_game_layout_0.setNextFocusUpId(R.id.navigation_tab_game_text);
		matrix_game_layout_0.setNextFocusRightId(R.id.matrix_game_layout_1);
		matrix_game_layout_0.setNextFocusLeftId(R.id.matrix_app_layout_4);
		matrix_game_layout_0.setNextFocusDownId(R.id.matrix_game_layout_0);

		// 推荐位1位置的焦点方向设置
		matrix_game_layout_1.setNextFocusUpId(R.id.navigation_tab_game_text);
		matrix_game_layout_1.setNextFocusRightId(R.id.matrix_game_layout_4);
		matrix_game_layout_1.setNextFocusLeftId(R.id.matrix_game_layout_0);
		matrix_game_layout_1.setNextFocusDownId(R.id.matrix_game_layout_2);

		// 推荐位2位置的焦点方向设置
		matrix_game_layout_2.setNextFocusUpId(R.id.matrix_game_layout_1);
		matrix_game_layout_2.setNextFocusRightId(R.id.matrix_game_layout_3);
		matrix_game_layout_2.setNextFocusLeftId(R.id.matrix_game_layout_0);
		matrix_game_layout_2.setNextFocusDownId(R.id.matrix_game_layout_2);

		// 推荐位3位置的焦点方向设置
		matrix_game_layout_3.setNextFocusUpId(R.id.matrix_game_layout_1);
		matrix_game_layout_3.setNextFocusRightId(R.id.matrix_game_layout_5);
		matrix_game_layout_3.setNextFocusLeftId(R.id.matrix_game_layout_2);
		matrix_game_layout_3.setNextFocusDownId(matrix_game_layout_3.getId());

		// 推荐位4位置的焦点方向设置
		matrix_game_layout_4.setNextFocusUpId(R.id.navigation_tab_game_text);
		matrix_game_layout_4.setNextFocusRightId(R.id.matrix_bluelight_layout_0);
		matrix_game_layout_4.setNextFocusLeftId(R.id.matrix_game_layout_1);
		matrix_game_layout_4.setNextFocusDownId(R.id.matrix_game_layout_5);

		// 推荐位5位置的焦点方向设置
		matrix_game_layout_5.setNextFocusUpId(R.id.matrix_game_layout_4);
		matrix_game_layout_5.setNextFocusRightId(R.id.matrix_bluelight_layout_1);
		matrix_game_layout_5.setNextFocusLeftId(R.id.matrix_game_layout_3);
		matrix_game_layout_5.setNextFocusDownId(R.id.matrix_game_layout_6);

		// 推荐位6位置的焦点方向设置
		matrix_game_layout_6.setNextFocusUpId(R.id.matrix_game_layout_5);
		matrix_game_layout_6.setNextFocusLeftId(R.id.matrix_game_layout_3);
		matrix_game_layout_6.setNextFocusRightId(R.id.matrix_bluelight_layout_2);
		matrix_game_layout_6.setNextFocusDownId(R.id.matrix_game_layout_6);


		hashMap.put(matrix_game_layout_0, "1101");
		hashMap.put(matrix_game_layout_1, "1102");
		hashMap.put(matrix_game_layout_2, "1103");
		hashMap.put(matrix_game_layout_3, "1104");
		hashMap.put(matrix_game_layout_4, "1115");
		hashMap.put(matrix_game_layout_5, "1108");
		hashMap.put(matrix_game_layout_6, "1106");

		super.init();

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
			gameList = gameFocusDAO.queryAppFocus(null, null, null, null);
			if (gameList.size() > 0 || count == readDataFromDBCount) {
				break;
			}
			count++;
			try {
				Thread.sleep(readDataFromDBInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (gameList.get(gameList.size() - 1).size() == 0);

		if (null != gameList && gameList.size() > 0) {
			checkPageIsIdle();
		} else {
			sendLoadDataResultMessage(LOAD_DATA_FAIL);
		}

	}

	@Override
	public void updateUIRefleshData() {
		sendLoadDataResultMessage(LOAD_DATA_SUCCESS);
	}

	// by zhangpengzhan
	HashMap<View, String> hashMap = new HashMap<View, String>();

	/**
	 * 响应点击事件 点击后进入 详情页
	 */
	class GameLayoutClickListener extends SimpleOnClickListener {

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
			if (index >= 0 && index < flippers.length) {// 轮播推荐位
				if (flippers[index].getCurrentView() == null || flippers[index].getChildCount() == 0)
					return null;
				entity = (AppFocusEntity) flippers[index].getCurrentView().getTag();

				if (null == entity) {
					return null;
				}

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
						intent.putExtra("categoryId","1");
						intent.putExtra("subjectPositionId", null != hashMap.get(view) ? hashMap.get(view).toString() : "");
					} else {// 应用详情的Action
						intent.putExtra("appid", entity.getContentId());
					}
					getContext().startActivity(intent);
				}
				if (action.equals(ContentInvoker.CONTENT_ACTION_APP_ALBUM)) {
					return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_APP_GAME_RECOMMAND).setEntity(entity)
							.setSenceName(AppScene.getScene()).setSrcType(ItemType.SUBJECT).setTabNo(Tab.GAME)
							.setViewPosition(null != hashMap.get(view) ? hashMap.get(view).toString() : "").build();
				} else {
					return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_GAME_RECOMMAND).setEntity(entity)
							.setSenceName(AppScene.getScene()).setSrcType(ItemType.GAME).setTabNo(Tab.GAME)
							.setViewPosition(null != hashMap.get(view) ? hashMap.get(view).toString() : "").build();
				}
			} else {
				if (view == matrix_game_layout_5) { // 游戏排行榜
					Intent intent = new Intent();
					intent.setAction("com.hiveview.appstore.top");
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.putExtra("category_id", 1);
					getContext().startActivity(intent);
		
				}
				if (view == matrix_game_layout_6) { // 游戏商店(游戏中心)
					Intent intent = new Intent();
					intent.setAction("com.hiveview.appstore.main");
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.putExtra("category_id", 1);
					intent.putExtra("app_type", 1);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					getContext().startActivity(intent);
				}
			}
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_GAME_BUTTON)
					.setButton(String.valueOf(view.getId()), null == idHashMap.get(view.getId()) ? "" : idHashMap.get(view.getId()))
					.setSenceName(AppScene.getScene()).setSrcType(ItemType.BUTTON).setTabNo(Tab.GAME)
					.setViewPosition(null != hashMap.get(view) ? hashMap.get(view).toString() : "").build();
		}

	}

	/**
	 * 设置游戏推荐位数据
	 * 
	 * @param gameList
	 */
	public void setMatrixData() {
		try {
			int count = gameList.size();

			android.view.ViewGroup.LayoutParams params = new android.view.ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			for (int i = 0; i < count; i++) {
				ArrayList<AppFocusEntity> subList = gameList.get(i);// 获取每个轮播推荐位的数据集合
				int dataSize = subList.size();
				int viewSize = flippers[i].getChildCount();
				if (dataSize > viewSize) {// 当推荐位数据增加时，补充显示推荐位的View
					Log.d(TAG, "game pageview " + i + " position recommend data add");
					for (int j = 0; j < dataSize - viewSize; j++) {
						AppRecommendView recommendView = new AppRecommendView(getContext());
						flippers[i].addView(recommendView, params);
					}
				} else if (dataSize < viewSize) {// 当推荐位数据减少时，减少显示推荐位的View
					Log.d(TAG, "game pageview " + i + " position recommend data reduce");
					for (int j = dataSize; j < viewSize; j++) {
						if (null != flippers[i] && flippers[i].getChildCount() > 0) {
							flippers[i].removeViewAt(j);
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
						mapDownload.put(newEntity.getContentId(), recommendView);
					}
				}
			}
			this.setVisibility(View.VISIBLE);
			getInstallGameCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到应用在盒子上的安装的游戏信息以及存储空间信息
	 * 
	 * @Title: MatrixGameView
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void getInstallGameCount() {
		HttpTaskManager.getInstance().submit(new Runnable() {

			@Override
			public void run() {
				if (!isInitGameCount) {// 初始化时获取用户盒子上安装应用游戏的信息
					InitAppCountUtils initAppCount = new InitAppCountUtils(getContext());
					installGameCount = initAppCount.getInstalledGameCount().size();
					isInitGameCount = true;
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
				/*
				 * if (view.getId() == R.id.matrix_game_layout_4) {
				 * AnimationUtil.loadBigAnimation(installMemoryBar);
				 * AnimationUtil.loadBigAnimation(tvInstallCount); }
				 */
				AnimationUtil.loadBigAnimation(view);
				AlphaAnimation alphaAnim = new AlphaAnimation(0.9f, 1.0f);
				alphaAnim.setDuration(100);
				alphaAnim.setInterpolator(new LinearInterpolator());
				view.startAnimation(alphaAnim);
				HiveviewApplication.mcurrentfocus=view;
				Log.v(TAG, "mcurrentfocus="+view.getId());
			} else {// 推荐位失去焦点的动画效果
				AnimationUtil.loadSmallAnimation(view);
				/*
				 * if (view.getId() == R.id.matrix_game_layout_4) {
				 * AnimationUtil.loadBigAnimation(installMemoryBar);
				 * AnimationUtil.loadBigAnimation(tvInstallCount); }
				 */
			}

		}
	};

	@Override
	public void startPageViewCustomAnimation() {
		super.startPageViewCustomAnimation();
	}

	@Override
	public void stopPageViewCustomAnimation() {
		super.stopPageViewCustomAnimation();
	}

	public void setGameInstallCount(int count) {
		installGameCount = count;
		mHandler.sendEmptyMessage(0);
		postDelayed(new Runnable() {

			@Override
			public void run() {
				getInstallGameCount();
			}
		}, 5000);

	}

	@Override
	public void onAppStoreReceive(Intent intent) {
		int category_id = intent.getIntExtra("category_id", 0);
		if (category_id == 1) {// 游戏类型
			int id = intent.getIntExtra("app_id", 0);
			AppRecommendView recommendView = mapDownload.get(id);
			if (HomeActions.ACTION_DOWNLOAD.equals(intent.getAction())) {
				// 应用下载进度广播

				if (null != recommendView) {
					int progress = intent.getIntExtra("download_progress", 0);
					recommendView.setProgress(progress);// 更新进度
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

	@Override
	public ViewGroup getTopMenuView() {
		ViewGroup gameTabView = (ViewGroup) inflate(getContext(), R.layout.top_menu_game_layout, null);
		TextView topTabTextView = (TextView) gameTabView.findViewById(R.id.navigation_tab_game_text);
		ImageView topFadeTabImageView = (ImageView) gameTabView.findViewById(R.id.navigation_tab_game_fade);
		topTabTextView.setOnFocusChangeListener(new TopTabOnFocusChangeListener(topTabTextView, topFadeTabImageView));

		topTabTextView.setNextFocusDownId(R.id.matrix_game_layout_0);
		topTabTextView.setNextFocusUpId(R.id.navigation_tab_game_text);

		viewFocusDirectionListener.setTopMenuViewFocusDirection(topTabTextView);
		return gameTabView;
	}

	@Override
	public View getBottomMenuView() {
		View gameTabView = inflate(getContext(), R.layout.sub_navigation_common_game, null);
		View gameRecordLayout = gameTabView.findViewById(R.id.sub_navigation_common_game_record_text_layout);
		View gameFavouriteLayout = gameTabView.findViewById(R.id.sub_navigation_common_game_favourite_text_layout);
		View gameInstalledLayout = gameTabView.findViewById(R.id.sub_navigation_common_game_installed_text_layout);
		View gameSearchLayout = gameTabView.findViewById(R.id.sub_navigation_common_game_search_text_layout);
		View gameSettingLayout = gameTabView.findViewById(R.id.sub_navigation_common_game_setting_text_layout);
		// View gameUserLayout =
		// gameTabView.findViewById(R.id.sub_navigation_common_game_user_text_layout);
		View gameExternalLayout = gameTabView.findViewById(R.id.sub_navigation_common_game_external_text_layout);

		gameRecordLayout.setOnClickListener(new gameRecordClickListener());
		gameFavouriteLayout.setOnClickListener(new gameFavouriteClickListener());
		gameInstalledLayout.setOnClickListener(new gameInstalledClickListener());
		gameSearchLayout.setOnClickListener(new gameSearchClickListener());
		gameSettingLayout.setOnClickListener(new gameSettingClickListener());
		// gameUserLayout.setOnClickListener(new gameUserClickListener());
		gameExternalLayout.setOnClickListener(new gameExternalClickListener());

		gameRecordLayout.setNextFocusUpId(R.id.matrix_game_layout_5);
		gameFavouriteLayout.setNextFocusUpId(R.id.matrix_game_layout_5);
		gameInstalledLayout.setNextFocusUpId(R.id.matrix_game_layout_3);
		gameSearchLayout.setNextFocusUpId(R.id.matrix_game_layout_3);
		gameSettingLayout.setNextFocusUpId(R.id.matrix_game_layout_4);
		// gameUserLayout.setNextFocusUpId(R.id.matrix_game_layout_4);

		bottomMenuViews = new View[] { gameRecordLayout, matrix_game_layout_4,matrix_game_layout_5, matrix_game_layout_6, gameSearchLayout, gameSettingLayout,
		/* gameUserLayout, */gameExternalLayout };

		viewFocusDirectionListener.setButtomMenuViewFocusDirection(bottomMenuViews);
		return gameTabView;
	}

	/**
	 * 响应观看记录点击事件 点击后进入
	 */
	class gameRecordClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), PlayerRecordActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_record_text))
					.setSenceName(AppScene.getScene()).setViewPosition("1111").setSrcType(ItemType.BUTTON).setTabNo(Tab.GAME).build();
		}
	}

	/**
	 * 响应收藏点击事件 点击后进入
	 */
	class gameFavouriteClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), CollectActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_favourite_text))
					.setSenceName(AppScene.getScene()).setViewPosition("1112").setSrcType(ItemType.BUTTON).setTabNo(Tab.GAME).build();
		}
	}

	/**
	 * 响应已安装应用游戏记录点击事件 点击后进入
	 */
	class gameInstalledClickListener extends SimpleOnClickListener {
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
					.setSenceName(AppScene.getScene()).setViewPosition("1113").setSrcType(ItemType.BUTTON).setTabNo(Tab.GAME).build();
		}
	}

	/**
	 * 响应搜索点击事件 点击后进入
	 */
	class gameSearchClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), SearchHomeActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_search_text))
					.setSenceName(AppScene.getScene()).setViewPosition("1114").setSrcType(ItemType.BUTTON).setTabNo(Tab.GAME).build();
		}
	}

	/**
	 * 响应系统设置点击事件 点击后进入
	 */
	class gameSettingClickListener extends SimpleOnClickListener {
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
					.setSenceName(AppScene.getScene()).setViewPosition("1115").setSrcType(ItemType.BUTTON).setTabNo(Tab.GAME).build();
		}
	}

	/**
	 * 响应用户中心点击事件 点击后进入
	 * 
	 * class gameUserClickListener extends SimpleOnClickListener {
	 * 
	 * @Override public DataHolder doOnClick(View view) { Intent intent = new
	 *           Intent(); intent.setAction("com.hiveview.user.usercenter");
	 *           intent.addCategory(Intent.CATEGORY_DEFAULT);
	 *           getContext().startActivity(intent);
	 * 
	 *           return new DataHolder.Builder(getContext())
	 *           .setDataType(DataType.CLICK_TAB_BUTTON)
	 *           .setButton(String.valueOf(view.getId()),
	 *           getResources().getString
	 *           (R.string.sub_navigation_common_user_text))
	 *           .setSenceName(AppScene
	 *           .getScene()).setViewPosition("1110").setSrcType
	 *           (ItemType.BUTTON).setTabNo(Tab.GAME).build(); } }
	 */
	class gameUserClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {
			Intent intent = new Intent();
			intent.setAction("com.hiveview.user.usercenter");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			getContext().startActivity(intent);

			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_user_text))
					.setSenceName(AppScene.getScene()).setViewPosition("1110").setSrcType(ItemType.BUTTON).setTabNo(Tab.GAME).build();
		}
	}

	/**
	 * 响应外接媒体点击事件 点击后进入
	 */
	class gameExternalClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			AppUtil.openApp("com.hiveview.externalstorage", getContext());
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_external_text))
					.setSenceName(AppScene.getScene()).setViewPosition("1116").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}
}
