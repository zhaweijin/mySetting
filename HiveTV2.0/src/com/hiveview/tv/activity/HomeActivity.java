package com.hiveview.tv.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hiveview.data.DataMessageHandler;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.AppFirstCreatInterface;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.content.CategoryDetailManager;
import com.hiveview.tv.common.deviceinfo.device.Device;
import com.hiveview.tv.common.deviceinfo.device.Device.DeviceVersion;
import com.hiveview.tv.common.deviceinfo.device.DeviceFactory;
import com.hiveview.tv.common.silenceupgrade.NewUpgrader;
import com.hiveview.tv.common.statistics.KeyEventHandler;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.common.statistics.Statistics;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.common.voice.IVoiceController;
import com.hiveview.tv.onlive.player.HiveviewVideoView;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.LoadService;
import com.hiveview.tv.service.NewUpgraderHandler;
import com.hiveview.tv.service.RefleshHandler;
import com.hiveview.tv.service.dao.CommonPreferenceDAO;
import com.hiveview.tv.service.dao.GameFocusDAO;
import com.hiveview.tv.service.dao.RecommendDAO;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.view.AuxiliaryNetworkView;
import com.hiveview.tv.view.AuxiliaryStorageView;
import com.hiveview.tv.view.AuxiliaryWeatherView;
import com.hiveview.tv.view.GuidePageView;
import com.hiveview.tv.view.HiveviewHdmiInView;
import com.hiveview.tv.view.HomeMenuPopuwindowsView;
import com.hiveview.tv.view.MatrixTVForeidgnView;
import com.hiveview.tv.view.NavigationNewTabView;
import com.hiveview.tv.view.NavigationNewTabView.onNavigationTabChangeListener;
import com.hiveview.tv.view.SubTabView;
import com.hiveview.tv.view.pager3d.Abstract3DPagerStrategy;
import com.hiveview.tv.view.pager3d.Chlitina_HDMIInViewStrategy;
import com.hiveview.tv.view.pager3d.HDMIInNoEDUViewStrategy;
import com.hiveview.tv.view.pager3d.HDMIInNoBlueViewStrategy;
import com.hiveview.tv.view.pager3d.HDMIInViewStrategy;
import com.hiveview.tv.view.pager3d.HomeActions;
import com.hiveview.tv.view.pager3d.NoHDMIInNoBlueNoEDUViewStrategy;
import com.hiveview.tv.view.pager3d.NoHDMIInNoBlueViewStrategy;
import com.hiveview.tv.view.pager3d.NoneHDMIInViewStrategy;
import com.hiveview.tv.view.pager3d.OnPageViewChangeListener;
import com.hiveview.tv.view.pager3d.TabBasePageView.OnDataCompleted;
import com.hiveview.tv.view.pager3d.View3DPager;
import com.hiveview.tv.view.television.voicecontrol.SignalChangedReceiver;
import com.paster.util.JsonUtil;
import com.paster.util.StringUtil;

@SuppressLint("UseSparseArrays")
public class HomeActivity extends BaseActivity {
	private static final String TAG = "HomeActivity";

	/**
	 * 是否是克丽缇娜版本
	 */
	public final static boolean isNeedChlitina = false;
	
	/**
	 * 是否是 百事通版本
	 */
	public final static boolean isBesTV = false;
	/**
	 * 百视通引导页点击设置或者开始体验
	 */
	public static boolean OpenBesTV = false;
	public static boolean SetBesTV = false;

	private static int TV_VIEW_INDEX = 4;
	private int CINEMA_VIEW_INDEX;
	private boolean isCinemaView = false;
	private String action;
	private View3DPager matrixPager;
	// model 第三和第五个字符串
	public static String model_3 = "";
	public static String model_5 = "";
	private final int DURATION = 600;
	/**
	 * 顶部导航索引
	 */
	public static final int NavigationViewIndex = 3;
	public static final int NavigationViewIndex_ISDOMESTIC = 2;
	/**
	 * @Fields theFlage 特殊的标识
	 */
	public static String theFlage = "2116,1016";
	/**
	 * 区分内外网的标识
	 */
	private int outer = 0;
	/**
	 * 盒子的sn
	 */
	private String SN;

	/**
	 * 顶部导航菜单
	 */
	private NavigationNewTabView navigationTabView;

	private AuxiliaryWeatherView auxiliaryWeatherView;

	/**
	 * 底部选项
	 */
	private SubTabView subTabView;

	public static int mCurrentViewIndex;

	/**
	 * @Fields 是否发送过开机统计,一次开机只发送一次
	 */
	private boolean isBootComplete = false;

	/*
	 * 判断是否是第一次进入
	 */
	private boolean isfirst = false;

	/*
	 * 判断是否是第一次进入
	 */
	private boolean isFirstRequest = false;

	/**
	 * 15分钟刷新一次 修改成30分钟
	 */
	public static int refleshDelayMillis = 10;

	/**
	 * 推荐位数据刷新成功
	 */
	public final static int RECOMMEND_DATA_REFLESH_SUCCESS = 10000;
	/**
	 * 刷新tab
	 */
	private final static int REFLESH_NAVIGATIONTAB = 10020;
	/**
	 * 有蓝光极清时电视的index
	 */
	protected static final int HAVE_BLUE_LIGHT = 5;
	/**
	 * 没有蓝光极清时电视的index
	 */
	protected static final int UN_HAVE_BLUE_LIGHT = 4;
	/**
	 * 引导页是否指引完成的标识
	 * 
	 * @Fields isSuccessed
	 */
	public static boolean isSuccessed = false;
	private boolean isGuide = false;

	/**
	 * tab 页码和index对应
	 * 
	 * @Fields sourceMap 游戏：1 极清： 7电视：2 推荐：3 影院：4 应用：5
	 */
	public static Map<Integer, String> sourceMap = new HashMap<Integer, String>();
	public Tab[] tab;

	private Abstract3DPagerStrategy abstract3dPagerStrategy = null;

	private static String UPGRADE_SERVICE = "com.hiveview.intent.action.START_UPGRADE_SERVICE";

	/**
	 * 本地是否有缓存 off by HaoZening
	 * 原因：在Application中将缓存加入memory中，如果没有（第一次启动或用户清空数据），图片会主动请求网络数据
	 */
	// private boolean hasCache = false;

	private static RelativeLayout relativeLayout;
	private static View imgApple2;

	private RefleshHandler refleshHandler = null;

	/**
	 * @Fields LAUNCHER_FIRST_CREAT:首次安装的消息
	 */
	private final int LAUNCHER_FIRST_CREAT = 0x001496;

	private GuidePageView guidePageView;
	/**
	 * @Fields activityView 窗口要绘制的view
	 */
	private View activityView;
	public static int versionCode = 1;
	/**
	 * 海外版flag
	 */
	private final static int DOMESTIC = 16;
	/**
	 * 克丽缇娜flag
	 */
	private final static int CHLITINA = 64;

	/**
	 * @Fields intranet 默认1是内网，0是外网
	 */
	private String intranet = "1";

	/**
	 * @Fields channelChangceReceiver:TODO 讯飞换台广播
	 */
	private SignalChangedReceiver channelChangceReceiver;

	/**
	 * 首页左侧菜单栏
	 */
	private HomeMenuPopuwindowsView leftPopupWindow;
	private static View homeMenuDraw;
	/**
	 * 首页logo
	 */
	private ImageView mHomeLogoImageView;

	// 当前activity的状态
	public static boolean isResume = true;

	private NewUpgraderHandler newUpgraderHandler = null;
	/**
	 * 是否发送过开机统计
	 */
	private boolean isSendIntervalDay = true;

	public static Handler statisticsHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Statistics.SEND_STATISTICS_FAIL:
				//Toast.makeText(HiveviewApplication.mContext, "SEND_STATISTICS_FAIL", 1).show();
				Statistics.send();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate-->start");
		activityView = View.inflate(getBaseContext(), R.layout.activity_home,
				null);
		relativeLayout = (RelativeLayout) activityView
				.findViewById(R.id.home_view);
		if (AppConstant.ISDOMESTIC) {
			isSuccessed = !checkIsShowGuide();
			if(isNeedChlitina){
				mCurrentViewIndex = 2;
			}else{
			mCurrentViewIndex = NavigationViewIndex;
			}
			CINEMA_VIEW_INDEX = 2;
		}else {
			isSuccessed = true;
			//checkIsShowGuide();
			Log.d(TAG, "<-------------------->" + isfirst);
			mCurrentViewIndex = NavigationViewIndex;
			CINEMA_VIEW_INDEX = 2;

		}
		Log.d(TAG, "isSuccessed==" + isSuccessed);
		// 引导页
		activityView
				.setVisibility(!isSuccessed ? View.INVISIBLE : View.VISIBLE);
		Log.d(TAG, "==onCreate;1" + activityView.getVisibility());
		// 如果是显示状态就不加载到activity的当窗口
		if (activityView.getVisibility() == View.VISIBLE) {
			setContentView(activityView);
			if(HomeActivity.isBesTV){
			AppUtil.openAppForPackageName("com.bestv.ott.baseservices", HomeActivity.this);
			}
		}
		Log.d(TAG, "==onCreate;2");
		// 定时刷新推荐位数据
		refleshHandler = new RefleshHandler(this);
		refleshHandler.setDelayMillis(refleshDelayMillis);// 设置每次刷新间隔时间
		refleshHandler.startReflesh();// 开始刷新
		newUpgraderHandler = new NewUpgraderHandler(HomeActivity.this);
		/* satrt by guosongsheng 添加新的静默升级 */
		if (!isBootComplete) {
			new NewUpgrader().upgradeApp(HomeActivity.this);
			// start author:zhangpengzhan
			// 淇濊瘉鍙彂閫佷竴娆″紑鏈虹粺璁�
			// KeyEventHandler.post(new
			// DataHolder.Builder(getBaseContext()).setTabNo(Tab.TAB).setViewPosition("0000").setIntervalDay("0").build());
			isBootComplete = true;
			Log.d(TAG, "::send the boot complete msg to service");
			// end
		}
		// checkCache();// 检测缓存
		initView();// 初始化View
		// 初始化监听数据变化的广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(LoadService.DATA_ACTION);
		filter.addAction(HomeActions.REFLESH_APP_GAME_INSTALL_COUNT_ACTION);
		filter.addAction(HomeActions.REFLESH_TV_RIGHT_ACTION);
		filter.addAction(HomeActions.REFLESH_APP_GAME_WHEN_USER_UNINSTALL_BY_OTHER_ACTION);
		filter.addAction(HomeActions.REFLESH_APP_GAME_UPDATE_ACTION);
		filter.addAction(HomeActions.REFLESH_HOME_APP_ACTION);
		filter.addAction(HomeActions.REFLESH_HOME_GAME_ACTION);
		filter.addAction(HomeActions.REFLESH_HOME_MOVIE_ACTION);
		filter.addAction(HomeActions.REFLESH_HOME_RECOMMEND_ACTION);
		filter.addAction(HomeActions.ACTION_RECOMMEND_LARGE_LOAD_COMPLETE);
		filter.addAction(HomeActions.NETWORK_CHANGE_ACTION);
		filter.addAction(AuxiliaryNetworkView.CONNECTION_STATUS_ACTION);
		filter.addAction(HomeActions.BOX_START_DEVICE_CHECK_FAIL_ACTION);
		filter.addAction(HomeActions.NEWUPGRADER_ACTION);
		filter.addAction(HomeActions.NEWUPGRADER_CANCEL_ACTION);
		registerReceiver(dataReceiver, filter);

		// 注册讯飞换台广播
		channelChangceReceiver = new SignalChangedReceiver();
		channelChangceReceiver.registerSignalChangedReceiver(this);

		matrixPager.loadData(false);
		Log.d(TAG, "==onCreate;3");
		// 判断action是否为空，不为空调用语音换台
		action = getIntent().getStringExtra(IVoiceController.COMMAND);

		if (action != null) {
			String tabName = HomeSwitchTabUtil.getHomeTabName(action);
			isfirst = true;
			switchTab(action, getIntent(), tabName);
		}
		/* start by ZhaiJianfeng */
		/**
		 * 启动外部service
		 */
		startExternalService();
		/* end by ZhaiJianfeng */

	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();

	}

	/**
	 * 
	 * @Title: startExternalService
	 * @author:ZhaiJianfeng
	 * @Description: 开机启动外部service
	 */
	private void startExternalService() {
		/** add by HaoZening */
		/**
		 * 启动黑名单服务
		 */
		try {
			Intent blackList = new Intent();
			blackList.setAction("com.hiveview.intent.action.LAUNCHER_START");
			startService(blackList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/** end by HaoZening */
		/** add by ZhaiJianfeng */
		/**
		 * 启动白名单服务
		 */
		try {
			Intent whiteList = new Intent();
			whiteList
					.setAction("com.hiveview.intent.action.WHITE_LIST_SERVICE");
			startService(whiteList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * 启动屏保广告服务
		 */
		try {
			Intent screenSaverAD = new Intent();
			screenSaverAD
					.setAction("com.hiveview.intent.action.SCEEN_SAVER_AD_SERVICE");
			startService(screenSaverAD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * 启动开机广告单服务
		 */
		try {
			Intent bootVideoAD = new Intent();
			bootVideoAD
					.setAction("com.hiveview.intent.action.BOOT_VIDEO_AD_SERVICE");
			startService(bootVideoAD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/** end by ZhaiJianfeng */
	}

	/**
	 * 检测是否需要再次显示引导页
	 * 
	 * @Title: HomeActivity
	 * @author:陈丽晓
	 * @Description:
	 */
	private boolean checkIsShowGuide() {
		CommonPreferenceDAO preferenceDAO = new CommonPreferenceDAO(
				getApplicationContext());
		Log.v(TAG, "checkIsShowGuide");
		if (!preferenceDAO.getIsShowGudie()) {
			Log.v(TAG, "checkIsShowGuide1");
			handler.sendEmptyMessage(LAUNCHER_FIRST_CREAT);
			Log.v(TAG, "checkIsShowGuide2");
			return true;
		}
		Log.v(TAG, "checkIsShowGuide3");
		return false;
	}

	@Override
	protected void processData(int msgWhat) {
		switch (msgWhat) {
		case RECOMMEND_DATA_REFLESH_SUCCESS:
			getPageBackgroundFromDB(true);
			matrixPager.loadData(true);
			CategoryDetailManager.getInstance().init(HomeActivity.this);
			break;
		case REFLESH_NAVIGATIONTAB:
			ValueAnimator animator = ObjectAnimator.ofFloat(1.0f);
			animator.setDuration(DURATION);
			animator.addUpdateListener(new AnimatorUpdateListener() {
				public void onAnimationUpdate(ValueAnimator animation) {
					navigationTabView.invalidate();
				}
			});
			animator.start();
			break;
		case LAUNCHER_FIRST_CREAT:
			Log.d(TAG, "Load the GuidePageView::");
			Device device = DeviceFactory.getInstance().getDevice();
			// device.initDeviceInfo(this);
			// String model = device.getModel();

			/*
			 * // 2116 平台不显示引导图 if ("DM2116".equals(model)) { isSuccessed =
			 * true; activityView.setVisibility(View.VISIBLE);
			 * setContentView(activityView); break; }
			 */
			// final WindowManager mWm = (WindowManager)
			// this.getSystemService(Context.WINDOW_SERVICE);
			/*
			 * WindowManager.LayoutParams mParams = new
			 * WindowManager.LayoutParams(); mParams.type =
			 * WindowManager.LayoutParams.TYPE_SYSTEM_ALERT; mParams.width =
			 * WindowManager.LayoutParams.MATCH_PARENT; mParams.height =
			 * WindowManager.LayoutParams.MATCH_PARENT;
			 */
			/**
			 * 判断海外版，国内版开机引导页启动方式，国内版启动开机引导没有不进语言设置； 海外没有引导页启动语言设置
			 */
			if (AppConstant.ISDOMESTIC) {
				guidePageView = new GuidePageView(getBaseContext());
				guidePageView
						.setTheLastPageOnClick(new AppFirstCreatInterface() {
							public void overView() {
								// mWm.removeView(guidePageView);
								isSuccessed = true;
								if(HomeActivity.isBesTV){
								activityView.setVisibility(View.INVISIBLE);}else{
									activityView.setVisibility(View.VISIBLE);
								}
								setContentView(activityView);
								//引导页点击开始体验时进入百视通apk
								if(HomeActivity.isBesTV&&OpenBesTV){
								AppUtil.openAppForPackageName("com.bestv.ott.baseservices", HomeActivity.this);
								activityView.postDelayed(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										activityView.setVisibility(View.VISIBLE);
									}
								}, 10000);
								}
							}
						});
				isSuccessed = false;
				setContentView(guidePageView);
				// mWm.addView(guidePageView, mParams);
				guidePageView.setVisibility(View.VISIBLE);
//				if(isBesTV){
//					guidePageView.postDelayed(new Runnable() {
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							guidePageView.getvStart().requestFocus();
//							guidePageView.getvStart().requestFocusFromTouch();
//							Toast.makeText(getApplicationContext(), "===="+guidePageView.getvStart().isFocused(), Toast.LENGTH_LONG).show();
//						}
//					}, 10000);
//				}
				Log.d(TAG,
						"==LAUNCHER_FIRST_CREAT" + activityView.getVisibility());
			} else {

				// 标记为用户已经浏览过引导页
				CommonPreferenceDAO preferenceDAO = new CommonPreferenceDAO(
						getBaseContext());
				preferenceDAO.setIsShowGudie(true);
				Intent intent = new Intent(
						"com.hiveview.domybox.ACTION_SETTING_TIME_ZONE");
				startActivity(intent);
				Intent intent2 = new Intent(
						"com.hiveview.domybox.ACTION_SETTING_LANGUAGE");
				startActivity(intent2);
			}
			break;
		default:
			break;
		}
	}

	// off by HaoZening 原因：检测耗时1s以上，Application中已经异步进行加载图片到memory中
	// /**
	// * 检测本地是否有缓存，有缓存先加载缓存，这样体验更好
	// *
	// * @Title: HomeActivity
	// * @author:陈丽晓
	// * @Description:
	// */
	// private void checkCache() {
	// // 检测本地是否有缓存
	// hasCache = (new RecommendDAO(this)).isCache() && (new
	// GameFocusDAO(this)).isCache() ? true : false;
	// if (hasCache) {// 如果本地有缓存立即去请求最新的推荐位，背景数据
	// refleshHandler.startRefleshImmediately();
	// CategoryDetailManager.getInstance().init(this);
	// }
	//
	// v_blackbackground.postDelayed(new Runnable() {
	//
	// @Override
	// public void run() {
	// v_blackbackground.setVisibility(View.GONE);
	// }
	// }, 18000);
	//
	// }

	private class HomeDataCompleted implements OnDataCompleted {

		@Override
		public void onCompleted() {
			Log.d(TAG, "onCompleted loadImage" + System.currentTimeMillis());
			// 推荐大图第三张大图加载完成
			// v_blackbackground.setVisibility(View.GONE);
			// 静默升级
			// new Upgrader().upgradeApp(HomeActivity.this);

			/* end by guosongsheng */
			// navigationTabView.setIndexRequestFocus(2);

		}
	};

	public int getVersionCode() {
		return versionCode;
	}

	private void initView() {

		matrixPager = (View3DPager) activityView
				.findViewById(R.id.matrix_pager_view);
		homeMenuDraw = (View) activityView.findViewById(R.id.home_menu_back);
		// matrixPager.setHandler(handler);
		navigationTabView = (NavigationNewTabView) activityView
				.findViewById(R.id.navigation_tab_view);
		subTabView = (SubTabView) activityView
				.findViewById(R.id.matrix_sub_tab_view);
		mHomeLogoImageView = (ImageView) activityView
				.findViewById(R.id.home_logo);	
		// 国外不显示天气
		if (!AppConstant.ISDOMESTIC) {
			//auxiliaryWeatherView.setVisibility(View.GONE);
			mHomeLogoImageView.setBackgroundResource(R.drawable.logo_haiwai);
		} else {
			if(isBesTV){
				mHomeLogoImageView.setBackgroundResource(R.drawable.logo_bestv);	
			}
			else
			if(isNeedChlitina){
				mHomeLogoImageView.setBackgroundResource(R.drawable.logo_chlitina);
			}else{
				mHomeLogoImageView.setBackgroundResource(R.drawable.logo);}
			auxiliaryWeatherView = (AuxiliaryWeatherView) activityView
					.findViewById(R.id.auxiliary_weather_view);
			auxiliaryWeatherView.setVisibility(View.VISIBLE);
		}
		// edit by HaoZening 在子线程中进行发送，后续代码并不依赖与此步结果，异步节省时间
		new Thread(new Runnable() {

			@Override
			public void run() {
				KeyEventHandler.post(new DataHolder.Builder(getBaseContext())
						.setTabNo(Tab.RECOMMEND)
						.setViewPosition("0301")
						.setPositionId(
								null == sourceMap.get(mCurrentViewIndex) ? "3"
										: sourceMap.get(mCurrentViewIndex))
						.setDataType(null).build());
			}
		}).start();
		try {
			ContentResolver resolver = HomeActivity.this.getContentResolver();
			// 通过call方法
			Bundle bundleWlanMac = resolver.call(
					Uri.parse("content://HiveViewAuthoritiesDataProvider"),
					"getNetwork", null, null);
			if (null != bundleWlanMac) {
				boolean isGroupUser = (Boolean) bundleWlanMac
						.get("isGroupUser");
				// 如果isGroupUser为true：内外，false：外网
				if (isGroupUser) {
					getSharedPreferences(HiveviewApplication.intranet,
							MODE_WORLD_READABLE).edit()
							.putString(HiveviewApplication.intranet, "1")
							.commit();
					Log.d(TAG,
							"ContentResolver==HiveviewApplication.isIntranet::" + 1);
					// Toast.makeText(getApplicationContext(),
					// "---isGroupUser:内网：" +isGroupUser, 0).show();
				} else {
					getSharedPreferences(HiveviewApplication.intranet,
							MODE_WORLD_READABLE).edit()
							.putString(HiveviewApplication.intranet, "0")
							.commit();
					Log.d(TAG,
							"ContentResolver==HiveviewApplication.isIntranet::" + 0);
					// Toast.makeText(getApplicationContext(),
					// "---isGroupUser:外网" + isGroupUser, 0).show();
				}
			} else {
				getSharedPreferences(HiveviewApplication.intranet,
						MODE_WORLD_READABLE).edit()
						.putString(HiveviewApplication.intranet, "-1").commit();
				Log.d(TAG, "ContentResolver==HiveviewApplication.isIntranet::"
						+ -1);
				// Toast.makeText(getApplicationContext(), "---bundleWlanMac:" +
				// bundleWlanMac, 0).show();
			}
		} catch (Exception e) {
			getSharedPreferences(HiveviewApplication.intranet,
					MODE_WORLD_READABLE).edit()
					.putString(HiveviewApplication.intranet, "-1").commit();
			Log.d(TAG, "ContentResolver==HiveviewApplication.isIntranet::" + -1
					+ e.toString());
			// Toast.makeText(getApplicationContext(), "---e.printStackTrace():"
			// + e.toString(), 0).show();
			e.printStackTrace();
		}
		try {
			Device device = DeviceFactory.getInstance().getDevice();
			// device.initDeviceInfo(this);
			DeviceVersion version = device.getDeviceVersion();
			String model = device.getModel();
			// String model = "DM2116";
			model_3 = model.substring(2, 3);
			model_5 = model.substring(4, 5);
			Log.v(TAG, "model_3::" + model_3);
			Log.v(TAG, "model_5::" + model_5);
			Log.v(TAG, "model=" + model);
			Log.v(TAG, "mac=" + device.getMac());
			Log.v(TAG, "sn=" + device.getSN());
			// 设置Hdmin类型
			setDeviceHdminModel(model_3, model_5);
			versionCode = Integer.valueOf(model_3);
			SN = device.getSN();
			// 从网络获取的内外网标识
			String intranetStr = getSharedPreferences(
					HiveviewApplication.intranet, MODE_WORLD_READABLE)
					.getString(HiveviewApplication.intranet, "-1");
			Log.d(TAG, "HiveviewApplication.isIntranet::" + intranetStr);
			if ("-1".equals(intranetStr)) {
				if (StringUtil.isStringEmpty(SN)) {
					// 需要出现极清
					outer = 4;
				} else {
					String SNHeard = SN.substring(0, 2);
					if (SNHeard.equals("DM")) {
						// 需要出现极清
						outer = 4;
					} else {
						// 不需要出现极清
						outer = 8;
					}
				}
			} else {
				outer = this.intranet.equals(intranetStr) ? 4 : 8;
			}
			HiveviewApplication.outer = this.outer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		int outerJurisdiction = -1;
		// 国内
		if (AppConstant.ISDOMESTIC) {
			if (isNeedChlitina) {
				outerJurisdiction=versionCode+CHLITINA;
			} else {
				outerJurisdiction = versionCode + outer;
			}
		} else// 海外
		{
			outerJurisdiction = versionCode + DOMESTIC;
		}
		Log.d(TAG, "outerJurisdiction::" + outerJurisdiction);
		switch (outerJurisdiction) {
		case 1:
			abstract3dPagerStrategy = new NoneHDMIInViewStrategy(matrixPager,
					new HomeDataCompleted());
			matrixPager.initTabViewByIndex(mCurrentViewIndex,
					abstract3dPagerStrategy);
			tab = new Tab[] { Tab.APP, Tab.EDUCATION, Tab.FILM, Tab.RECOMMEND,
					Tab.BULE, Tab.GAME };
			sourceMap.put(0, "5");
			sourceMap.put(1, "8");
			sourceMap.put(2, "4");
			sourceMap.put(3, "3");
			sourceMap.put(4, "7");
			sourceMap.put(5, "1");
			break;
		case 2:
			abstract3dPagerStrategy = new HDMIInViewStrategy(matrixPager,
					new HomeDataCompleted());
			matrixPager.initTabViewByIndex(mCurrentViewIndex,
					abstract3dPagerStrategy);
			tab = new Tab[] { Tab.APP, Tab.EDUCATION, Tab.FILM, Tab.RECOMMEND,
					Tab.BULE, Tab.TV, Tab.GAME };
			sourceMap.put(0, "5");
			sourceMap.put(1, "8");
			sourceMap.put(2, "4");
			sourceMap.put(3, "3");
			sourceMap.put(4, "7");
			sourceMap.put(5, "2");
			sourceMap.put(6, "1");
			break;
		case 5:// 不包含电视有极清
			abstract3dPagerStrategy = new NoneHDMIInViewStrategy(matrixPager,
					new HomeDataCompleted());
			matrixPager.initTabViewByIndex(mCurrentViewIndex,
					abstract3dPagerStrategy);
			tab = new Tab[] { Tab.APP, Tab.EDUCATION, Tab.FILM, Tab.RECOMMEND,
					Tab.BULE, Tab.GAME };
			sourceMap.put(0, "5");
			sourceMap.put(1, "8");
			sourceMap.put(2, "4");
			sourceMap.put(3, "3");
			sourceMap.put(4, "7");
			sourceMap.put(5, "1");
			break;
		case 6:// 全部都包含
			abstract3dPagerStrategy = new HDMIInViewStrategy(matrixPager,
					new HomeDataCompleted());
			matrixPager.initTabViewByIndex(mCurrentViewIndex,
					abstract3dPagerStrategy);
			tab = new Tab[] { Tab.APP, Tab.EDUCATION, Tab.FILM, Tab.RECOMMEND,
					Tab.BULE, Tab.TV, Tab.GAME };
			sourceMap.put(0, "5");
			sourceMap.put(1, "8");
			sourceMap.put(2, "4");
			sourceMap.put(3, "3");
			sourceMap.put(4, "7");
			sourceMap.put(5, "2");
			sourceMap.put(6, "1");
			break;
		case 9:// 没有极清没有电视
			abstract3dPagerStrategy = new NoHDMIInNoBlueViewStrategy(
					matrixPager, new HomeDataCompleted());
			matrixPager.initTabViewByIndex(mCurrentViewIndex,
					abstract3dPagerStrategy);
			tab = new Tab[] { Tab.APP, Tab.EDUCATION, Tab.FILM, Tab.RECOMMEND,
					Tab.GAME };
			sourceMap.put(0, "5");
			sourceMap.put(1, "8");
			sourceMap.put(2, "4");
			sourceMap.put(3, "3");
			sourceMap.put(4, "1");
			break;
		case 10:// 有电视没有极清
			abstract3dPagerStrategy = new HDMIInNoBlueViewStrategy(matrixPager,
					new HomeDataCompleted());
			matrixPager.initTabViewByIndex(mCurrentViewIndex,
					abstract3dPagerStrategy);
			tab = new Tab[] { Tab.APP, Tab.EDUCATION, Tab.FILM, Tab.RECOMMEND,
					Tab.TV, Tab.GAME };
			sourceMap.put(0, "5");
			sourceMap.put(1, "8");
			sourceMap.put(2, "4");
			sourceMap.put(3, "3");
			sourceMap.put(4, "2");
			sourceMap.put(5, "1");
			break;
		case 17:// 没有电视没有极清没有教育(海外版)
			// abstract3dPagerStrategy = new NoHDMIInNoBlueNoEDUViewStrategy(
			// matrixPager, new HomeDataCompleted());
			// matrixPager.initTabViewByIndex(mCurrentViewIndex,
			// abstract3dPagerStrategy);
			// tab = new Tab[] { Tab.APP, Tab.FILM, Tab.RECOMMEND, Tab.GAME };
			// sourceMap.put(0, "5");
			// sourceMap.put(1, "4");
			// sourceMap.put(2, "3");
			// sourceMap.put(4, "1");
			abstract3dPagerStrategy = new HDMIInNoEDUViewStrategy(
					matrixPager, new HomeDataCompleted());
			matrixPager.initTabViewByIndex(mCurrentViewIndex,
					abstract3dPagerStrategy);
			tab = new Tab[] { Tab.APP, Tab.FILM, Tab.RECOMMEND,Tab.BULE, Tab.TV,
					Tab.GAME };
			sourceMap.put(0, "5");
			sourceMap.put(1, "4");
			sourceMap.put(2, "3");
			sourceMap.put(3, "7");
			sourceMap.put(4, "2");
			sourceMap.put(5, "1");
			break;
		case 18:// 有电视没有极清没有教育(海外版)
			abstract3dPagerStrategy = new HDMIInNoEDUViewStrategy(
					matrixPager, new HomeDataCompleted());
			matrixPager.initTabViewByIndex(mCurrentViewIndex,
					abstract3dPagerStrategy);
			tab = new Tab[] { Tab.APP, Tab.FILM, Tab.RECOMMEND,Tab.BULE, Tab.TV,
					Tab.GAME };
			sourceMap.put(0, "5");
			sourceMap.put(1, "4");
			sourceMap.put(2, "3");
			sourceMap.put(3, "7");
			sourceMap.put(4, "2");
			sourceMap.put(5, "1");
			break;
		case 65:// 克丽缇娜tab 1.0+ 盒子
			abstract3dPagerStrategy = new Chlitina_HDMIInViewStrategy(
					matrixPager, new HomeDataCompleted());
			matrixPager.initTabViewByIndex(mCurrentViewIndex,
					abstract3dPagerStrategy);
			tab = new Tab[] { Tab.APP, Tab.EDUCATION,  Tab.RECOMMEND,Tab.FILM,
					Tab.GAME };
			sourceMap.put(0, "5");
			sourceMap.put(1, "8");
			sourceMap.put(2, "3");
			sourceMap.put(3, "4");
			sourceMap.put(4, "1");
			break;
		case 66:// 克丽缇娜tab 2.0+ 盒子
			abstract3dPagerStrategy = new Chlitina_HDMIInViewStrategy(
					matrixPager, new HomeDataCompleted());
			matrixPager.initTabViewByIndex(mCurrentViewIndex,
					abstract3dPagerStrategy);
			tab = new Tab[] { Tab.APP, Tab.EDUCATION,  Tab.RECOMMEND,Tab.FILM,
					Tab.GAME };
			sourceMap.put(0, "5");
			sourceMap.put(1, "8");
			sourceMap.put(2, "3");
			sourceMap.put(3, "4");
			sourceMap.put(4, "1");
			break;
		default:
			abstract3dPagerStrategy = new NoneHDMIInViewStrategy(matrixPager,
					new HomeDataCompleted());
			matrixPager.initTabViewByIndex(mCurrentViewIndex,
					abstract3dPagerStrategy);
			tab = new Tab[] { Tab.APP, Tab.EDUCATION, Tab.FILM, Tab.RECOMMEND,
					Tab.BULE, Tab.GAME };
			sourceMap.put(0, "5");
			sourceMap.put(1, "8");
			sourceMap.put(2, "4");
			sourceMap.put(3, "3");
			sourceMap.put(4, "7");
			sourceMap.put(5, "1");
			break;
		}
		matrixPager.setTopAndSubTabs(navigationTabView, subTabView);

		matrixPager.setPageViewChangeListener(new OnPageViewChangeListener() {

			@Override
			public void onPageChangeStart(int mCurrentIndex) {
				subTabView.setCurrentItem(mCurrentIndex);
				Log.v(TAG, "mCurrentIndex==" + mCurrentIndex + ";;;;;"
						+ mCurrentViewIndex);
				


				mCurrentViewIndex = mCurrentIndex;
				Log.v(TAG, "mCurrentIndex  "+mCurrentViewIndex);
				// 国内
				if (AppConstant.ISDOMESTIC) {
					if (outer == 4) { // 需要极清版本
						TV_VIEW_INDEX = HAVE_BLUE_LIGHT;
					} else if (outer == 8) { // 不需要极清版本
						TV_VIEW_INDEX = UN_HAVE_BLUE_LIGHT;
					}
				}// 国外
				else {
					TV_VIEW_INDEX = 4;
				}
				if (TV_VIEW_INDEX == mCurrentIndex) {
					Log.i(TAG, "onMatrixPageChangeComplete-->sendBroad");
					if (AppConstant.ISDOMESTIC) {
						sendBroadcast(new Intent(
								HiveviewHdmiInView.ACTION_SMALL_SCREEN_SHOW));// 初始化HdminView
																				// MatrixTVForeidgnView
					} else {
						sendBroadcast(new Intent(
								MatrixTVForeidgnView.ACTION_SMALL_SCREEN_SHOW));
					}
					AppScene.setScene(AppScene.TVVIEW_SCENE);
					Log.i(TAG, "onMatrixPageChangeComplete-->end");
				} else {
					AppScene.setScene(AppScene.HOME_SCENE);
					if (AppConstant.ISDOMESTIC) {
						sendBroadcast(new Intent(
								HiveviewHdmiInView.ACTION_SMALL_SCREEN_UNSHOW));
					} else {
						sendBroadcast(new Intent(
								MatrixTVForeidgnView.ACTION_SMALL_SCREEN_UNSHOW));
					}
				}

				if (CINEMA_VIEW_INDEX == mCurrentIndex) {
					isCinemaView = true;
				} else {
					isCinemaView = false;
				}

				navigationTabView.invalidate();
				matrixPager.invalidateAllPageView();
				if (null == sourceMap.get(mCurrentViewIndex)) {
					Log.v(TAG, "3");
				} else {
					Log.v(TAG, sourceMap.get(mCurrentViewIndex));
				}

			
				
				KeyEventHandler.post(new DataHolder.Builder(getBaseContext())
						.setTabNo(
								null == tab[mCurrentIndex] ? Tab.RECOMMEND
										: tab[mCurrentIndex])
						.setViewPosition("0301")
						.setPositionId(
								null == sourceMap.get(mCurrentIndex) ? "3"
										: sourceMap.get(mCurrentIndex))
						.setDataType(null).build());
			}

			@Override
			public void onPageChangeComplete(int mCurrentIndex) {}

		});

		navigationTabView
				.setOnNavigationTabChangeListener(new onNavigationTabChangeListener() {

					@Override
					public void onScrollStart(int targetIndex) {
						Log.d(TAG,
								"dispatchKeyEvent::"
										+ navigationTabView.isCursor());

						if (matrixPager.getCurrentPageIndex() == matrixPager
								.get3DPagerChildCount() - 1 && targetIndex == 0) {// 翻动下一页到最后一个位置，往第0位置翻动
							matrixPager.moveToNext();
							return;
						}

						if (matrixPager.getCurrentPageIndex() == 0
								&& targetIndex == matrixPager
										.get3DPagerChildCount() - 1) {// 翻动上一页到第一个位置，往第最后一个位置翻动
							matrixPager.moveToPrevious();
							return;
						}

						if (matrixPager.getCurrentPageIndex() - targetIndex > 0) {
							matrixPager.moveToPrevious();
							return;
						}

						if ((matrixPager.getCurrentPageIndex() - targetIndex) < 0) {
							matrixPager.moveToNext();
							return;
						}

					}

					@Override
					public void onScrollComplete(int currentIndex) {
					}
				});

		// 显示推荐页
		// <<<<<<< .mine
		// subTabView.setCurrentItem(3);
		// =======
		subTabView.setCurrentItem(mCurrentViewIndex);

		// 左侧菜单
		leftPopupWindow = new HomeMenuPopuwindowsView(this);
		// initPopuptWindow();
	}

	/**
	 * 设配Hdmin 类型
	 * 
	 * @Title: HomeActivity
	 * @author:guosongsheng
	 * @Description:
	 * @param model
	 */
	private void setDeviceHdminModel(String model_3, String model_5) {
		if (!TextUtils.isEmpty(model_3) && !TextUtils.isEmpty(model_5)
				&& model_3.equals("2")) {
			if (model_5.contains("1")) {
				HiveviewHdmiInView.mCVTE = false;
				HiveviewHdmiInView.mRtkHdminRx = true;
				Log.i(TAG, "setDeviceHdminModel................" + model_5);
			} else if (model_5.contains("0")) {
				HiveviewHdmiInView.mCVTE = true;
				HiveviewHdmiInView.mRtkHdminRx = false;
				Log.i(TAG, "setDeviceHdminModel................" + model_5);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume-->start");
		isResume = true;
		//引导页点击设置后进入设置 从设置返回时进入百视通apk
		if(HomeActivity.isBesTV&&SetBesTV){
			SetBesTV=false;
			AppUtil.openAppForPackageName("com.bestv.ott.baseservices", HomeActivity.this);
			activityView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					activityView.setVisibility(View.VISIBLE);
				}
			}, 10000);
			}
		/*
		 * if (DeviceUtils.isDM1002Device()) {
		 * Util.startACTIVATE(getApplicationContext()); }
		 */
		if (TV_VIEW_INDEX == mCurrentViewIndex) {
			AppScene.setScene(AppScene.TVVIEW_SCENE);
			if (AppConstant.ISDOMESTIC) {
				this.sendBroadcast(new Intent(
						HiveviewHdmiInView.ACTION_SMALL_SCREEN_SHOW));
			} else {
				this.sendBroadcast(new Intent(
						MatrixTVForeidgnView.ACTION_SMALL_SCREEN_SHOW));
			}
		} else {
			AppScene.setScene(this.getClass().getName());
		}
		// 国外
		if (!AppConstant.ISDOMESTIC) {
				if (HiveviewApplication.mcurrentfocus != null) {
					Log.v(TAG, mCurrentViewIndex + "mcurrentfocus="
							+ HiveviewApplication.mcurrentfocus.getId());
					HiveviewApplication.mcurrentfocus.setFocusable(true);
					HiveviewApplication.mcurrentfocus
							.setFocusableInTouchMode(true);
					HiveviewApplication.mcurrentfocus.requestFocus();
					HiveviewApplication.mcurrentfocus.requestFocusFromTouch();
			}
		}
		if (!isFirstRequest) {
			navigationTabView.setIndexRequestFocus(mCurrentViewIndex);
			isFirstRequest = true;
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		isResume = false;
		Log.i(TAG, "onPause-->start");

		if (AppConstant.ISDOMESTIC) {
			/*
			 * 国内 判断是否为大小屏切换，是的话不调用overlayViewDeinit()
			 */
			if (HiveviewHdmiInView.getSwitchsizer()) {
				sendBroadcast(new Intent(
						HiveviewHdmiInView.ACTION_SMALL_SCREEN_PAUSE));
				HiveviewHdmiInView.setSwitchsizer(false);
			} else {
				Intent it = new Intent(
						HiveviewHdmiInView.ACTION_SMALL_SCREEN_UNSHOW);
				it.putExtra("currentViewIndex", mCurrentViewIndex);
				this.sendBroadcast(it);
			}

		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "onStop");
		if (!AppConstant.ISDOMESTIC) // 国外
		{
			Log.i(TAG, "onStop 释放media");
			// sendBroadcast(new Intent(
			// MatrixTVForeidgnView.ACTION_SMALL_SCREEN_PAUSE));
			try {
				HiveviewVideoView.stopPlayback();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		// HiveviewHdmiInView smallScreem =
		// (HiveviewHdmiInView)this.findViewById(R.id.view_hdmi_small);
		// smallScreem.setIsSmall(false);

	}

	private int count = 0;
	private long mKeyDownTime = 0;
	private boolean isup = true;

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.d(TAG, "dispatchKeyEvent>>>>>>>>>>>>>>>>>0");
			isup = false;
		}

		if (event.getAction() == KeyEvent.ACTION_UP) {
			Log.d(TAG, "dispatchKeyEvent>>>>>>>>>>>>>>>>>1");
			isup = true;
		}

		// 临时使用代码，保证用户在断网状态下，单击能够进入Launcher
		/*
		 * if (guideView.getVisibility() == View.VISIBLE && event.getKeyCode()
		 * == KeyEvent.KEYCODE_DPAD_CENTER) { guideView.btnPerformClick(event);
		 * return false; }
		 */

		// if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER &&
		// !HiveTVService.isAvailable(getApplicationContext())) {
		// View subFocusView = subTabView.findFocus();
		// View recommendFocusView = matrixPager.findFocus();
		// if (null != subFocusView) {
		// if (subFocusView.getId() ==
		// R.id.sub_navigation_app_external_text_layout) {
		// subFocusView.performClick();
		// return true;
		// }
		// } else if (null != recommendFocusView) {
		// if (recommendFocusView.getId() == R.id.matrix_game_layout_4 ||
		// recommendFocusView.getId() == R.id.matrix_app_layout_5) {
		// recommendFocusView.performClick();
		// return true;
		// }
		// }
		// }
		//
		return super.dispatchKeyEvent(event);

	}

	public void showMenuDraw() {
		homeMenuDraw.setVisibility(View.VISIBLE);
	}

	public static void unshowMenuDraw() {
		homeMenuDraw.setVisibility(View.INVISIBLE);
	}

	private PopupWindow mPopupWindow;

	/*
	 * 创建PopupWindow
	 */
	private void initPopuptWindow() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View popupWindow = layoutInflater.inflate(
				R.layout.home_popupwindow_left, null);

		// 创建一个PopupWindow
		// 参数1：contentView 指定PopupWindow的内容
		// 参数2：width 指定PopupWindow的width
		// 参数3：height 指定PopupWindow的height
		mPopupWindow = new PopupWindow(popupWindow, 100, 130);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Log.d(TAG, "matrixPager.moveToPrevious()>>0");
		// 屏蔽back按键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() <= 1) {
			// 这里是位置显示方式,在屏幕的左侧
			if (isSuccessed) {
				showMenuDraw();
				leftPopupWindow.showAtLocation(relativeLayout, Gravity.LEFT, 0,
						0);
				leftPopupWindow.setFocusable(true);
				Log.v(TAG, "leftPopupWindow" + leftPopupWindow.isFocusable());
			}
			return super.onKeyDown(keyCode, event);
		}
		// Log.d(TAG, "dispatchKeyEvent::" +
		// abstract3dPagerStrategy.isRotating() + "===" +
		// navigationTabView.isCursor());
		/*
		 * if (abstract3dPagerStrategy.isRotating() ||
		 * navigationTabView.isCursor()) { //return true; }
		 */

		if (null != matrixPager.findFocus()) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				int viewId = matrixPager.findFocus().getId();
				if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT
						&& (viewId == R.id.matrix_game_layout_6
								|| viewId == R.id.matrix_game_layout_4
								|| viewId == R.id.matrix_game_layout_5
								|| viewId == R.id.matrix_tv_layout_5
								|| viewId == R.id.matrix_tv_layout_6
								|| viewId == R.id.matrix_tv_layout_7
								|| viewId == R.id.matrix_recommend_layout_1
								|| viewId == R.id.matrix_bluelight_layout_5
								|| viewId == R.id.matrix_recommend_layout_3
								|| viewId == R.id.matrix_tv_layout_8
								|| viewId == R.id.matrix_tv_layout_3_f
								|| viewId == R.id.matrix_recommend_layout_4
								|| viewId == R.id.matrix_cinema_layout_11_new_v
								|| viewId == R.id.matrix_cinema_layout_12_new_v
								|| viewId == R.id.matrix_cinema_layout_11_new_h21
								|| viewId == R.id.matrix_cinema_layout_11_new_h22
								|| viewId == R.id.matrix_cinema_layout_12_new_h23
								|| viewId == R.id.matrix_cinema_layout_12_new_h24
								|| viewId == R.id.matrix_app_layout_5
								|| viewId == R.id.matrix_app_layout_6
								|| viewId == R.id.matrix_app_layout_4
								|| viewId == R.id.matrix_tv_layout_3
								|| viewId == R.id.matrix_education_layout_5
								|| viewId == R.id.matrix_education_layout_6 || viewId == R.id.matrix_education_layout_7)) {

					/* start by guosongsheng 美国测试使用 */
					if (AppConstant.ISDOMESTIC
							&& viewId == R.id.matrix_tv_layout_3) {
					} else {
						Log.d(TAG, "matrixPager.moveToPrevious()>>1");
						handler.sendEmptyMessage(REFLESH_NAVIGATIONTAB);
						matrixPager.moveToPrevious();
						Log.d(TAG, "matrixPager.moveToPrevious()>>2");
					}
					/* end by guosongsheng */
				} else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT
						&& (viewId == R.id.matrix_app_layout_0
								|| viewId == R.id.matrix_recommend_layout_0
								|| viewId == R.id.thumb_layout_0
								|| viewId == R.id.matrix_bluelight_layout_0
								|| viewId == R.id.matrix_bluelight_layout_2
								|| viewId == R.id.matrix_cinema_layout_1_new_v
								|| viewId == R.id.matrix_cinema_layout_6_new_v
								|| viewId == R.id.matrix_cinema_layout_1_new_h1
								|| viewId == R.id.matrix_cinema_layout_1_new_h2
								|| viewId == R.id.matrix_cinema_layout_6_new_h11
								|| viewId == R.id.matrix_cinema_layout_6_new_h12
								|| viewId == R.id.matrix_tv_layout_2
								|| viewId == R.id.matrix_tv_layout_3_f
								|| viewId == R.id.matrix_tv_layout_0
								|| viewId == R.id.matrix_tv_layout_1
								|| viewId == R.id.matrix_tv_layout_3
								|| viewId == R.id.matrix_game_layout_0 || viewId == R.id.matrix_education_layout_1)) {

					/* start by guosongsheng 美国测试使用 */
					if (AppConstant.ISDOMESTIC
							&& viewId == R.id.matrix_tv_layout_3) {
					} else {
						Log.d(TAG, "matrixPager.moveToPrevious()>>3");
						handler.sendEmptyMessage(REFLESH_NAVIGATIONTAB);
						matrixPager.moveToNext();
						Log.d(TAG, "matrixPager.moveToPrevious()>>4");
					}
					/* end by guosongsheng */

				}
			}
		}

		if (null != subTabView.findFocus()) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				int viewId = subTabView.findFocus().getId();
				if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT
						&& (viewId == R.id.sub_navigation_common_recommend_external_text_layout
								|| viewId == R.id.sub_navigation_common_movie_external_text_layout
								|| viewId == R.id.sub_navigation_common_game_external_text_layout
								|| viewId == R.id.sub_navigation_common_cinema_external_text_layout
								|| viewId == R.id.sub_navigation_common_app_external_text_layout
								|| viewId == R.id.sub_navigation_common_tv_external_text_layout
								|| viewId == R.id.sub_navigation_tv_direction_text_layout || viewId == R.id.sub_navigation_common_bluelight_external_text_layout)) {
					/* start by guosongsheng 美国测试使用 */
					if (AppConstant.ISDOMESTIC
							&& viewId == R.id.sub_navigation_tv_direction_text_layout) {
					} else {
						Log.d(TAG, "matrixPager.moveToPrevious()>>5");
						matrixPager.moveToPrevious();
						Log.d(TAG, "matrixPager.moveToPrevious()>>6");
					}
					/* end by guosongsheng */
				} else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT
						&& (viewId == R.id.sub_navigation_common_recommend_favourite_text_layout
								|| viewId == R.id.sub_navigation_common_bluelight_record_text_layout
								|| viewId == R.id.sub_navigation_common_tv_record_text_layout
								|| viewId == R.id.sub_navigation_common_game_record_text_layout
								|| viewId == R.id.sub_navigation_common_app_record_text_layout
								|| viewId == R.id.sub_navigation_common_cinema_record_text_layout
								|| viewId == R.id.sub_navigation_tv_direction_text_layout || viewId == R.id.sub_navigation_common_movie_record_text_layout)) {
					/* start by guosongsheng 美国测试使用 */
					if (AppConstant.ISDOMESTIC
							&& viewId == R.id.sub_navigation_tv_direction_text_layout) {
					} else {
						Log.d(TAG, "matrixPager.moveToPrevious()>>7");
						matrixPager.moveToNext();
						Log.d(TAG, "matrixPager.moveToPrevious()>>8");
					}
					/* end by guosongsheng */
				}
			}
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "onStart-->start");
		AppScene.setScene(AppScene.HOME_SCENE);
	}

	private BroadcastReceiver dataReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent mIntent) {
			if (mIntent.getAction().equals(LoadService.DATA_ACTION)) {// 数据下载完成
				Log.d(TAG,
						"onReceive loadImage DATA_ACTION"
								+ System.currentTimeMillis());
				Log.i(TAG, "dataReceiver::onReceive::DATA_ACTION-->POS 9");
				handler.sendEmptyMessage(RECOMMEND_DATA_REFLESH_SUCCESS);
			}
			/* start by guosongsheng 响应用户置顶电视台移到MatrixTVView中 */
			// else if
			// (mIntent.getAction().equals(HomeActions.REFLESH_TV_RIGHT_ACTION))
			// {// 响应用户置顶电视台
			// // setMatrixTvData();
			// }
			/* end by guosongsheng */
			else if (mIntent.getAction().equals(
					HomeActions.REFLESH_APP_GAME_INSTALL_COUNT_ACTION)) {// 刷新应用游戏View的显示
				Log.d(TAG,
						"onReceive loadImage REFLESH_APP_GAME_INSTALL_COUNT_ACTION"
								+ System.currentTimeMillis());
				int gameCount = mIntent.getIntExtra("install_game_count", 0);
				int appCount = mIntent.getIntExtra("install_app_count", 0);
				matrixPager.setInstallApkCount(gameCount, appCount);
			} else if (mIntent.getAction().equals(
					HomeActions.REFLESH_APP_GAME_UPDATE_ACTION)) {// 用户机器上安装的应用游戏有更新的通知
				Log.d(TAG, "onReceive loadImage REFLESH_APP_GAME_UPDATE_ACTION"
						+ System.currentTimeMillis());
				int appUpdateCount = mIntent.getIntExtra("app_number", 0);
				int gameUpdateCount = mIntent.getIntExtra("game_number", 0);
				// matrixPager.setInstallApkCount(0, 0);
				/*
				 * subTabView.setAppAndGameUpdateCount(appUpdateCount,
				 * gameUpdateCount);// 更新显示数量
				 */} else if (mIntent.getAction().equals(
					HomeActions.ACTION_RECOMMEND_LARGE_LOAD_COMPLETE)) {
				Log.d(TAG,
						"onReceive loadImage ACTION_RECOMMEND_LARGE_LOAD_COMPLETE"
								+ System.currentTimeMillis());
				// 推荐大图第三张大图加载完成
				// v_blackbackground.setVisibility(View.GONE);
				// 静默升级
				// new Upgrader().upgradeApp(HomeActivity.this);
				// navigationTabView.setIndexRequestFocus(2);

			} else if (mIntent.getAction().equals(
					HomeActions.NETWORK_CHANGE_ACTION)) {// 网络环境发送改变
				Log.d(TAG,
						"onReceive loadImage NETWORK_CHANGE_ACTION"
								+ System.currentTimeMillis());
				/**
				 * 防止开机时没网络导致开机统计没有发出，所以再此有网后发送一次开机统计
				 */
				if (HiveTVService.isAvailable(HomeActivity.this)
						&& isSendIntervalDay) {
					isSendIntervalDay = false;

					SharedPreferences sharedPreferences = HomeActivity.this
							.getSharedPreferences("boot_time",
									Context.MODE_PRIVATE);
					Editor editor = sharedPreferences.edit();
					long last_time = sharedPreferences.getLong(
							"last_boot_time", new Date().getTime());
					Date last_date = new Date(last_time);
					java.util.Date dt = new Date();
					Log.d(TAG, "系统时间:" + System.currentTimeMillis());
					long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
					String intervalDay;
					long diff = dt.getTime() - last_date.getTime();
					intervalDay = String.valueOf(diff / nd);// 计算差多少天
					Log.v(TAG, "intervalDay=" + intervalDay);
					editor.putLong("last_boot_time", dt.getTime());

					editor.commit();// 别忘了提交哦

					KeyEventHandler.post(new DataHolder.Builder(
							HomeActivity.this).setTabNo(Tab.TAB)
							.setViewPosition("0000")
							.setIntervalDay(intervalDay).build());
				}
				// 检测本地是否有缓存
				boolean hasCache = (new RecommendDAO(HomeActivity.this))
						.isCache()
						&& (new GameFocusDAO(HomeActivity.this)).isCache() ? true
						: false;
				if (!hasCache) {
					if (HiveTVService.isAvailable(HomeActivity.this)) {
						// getApplication().getSharedPreferences("HomeActvity",
						// Context.MODE_PRIVATE).edit().putString(DateUtils.getAfterMinuteDate(0),
						// " date form homeActivity").commit();
						Intent intentService = new Intent(HomeActivity.this,
								LoadService.class);
						intentService.putExtra("isNeedDeviceCheck", true);
						startService(intentService);
					}
				} else {
					hasCache = false;
				}
			} else if (mIntent.getAction().equals(
					HomeActions.BOX_START_DEVICE_CHECK_FAIL_ACTION)) {// 鉴权失败，显示Launcher页面
				Log.d(TAG,
						"onReceive loadImage BOX_START_DEVICE_CHECK_FAIL_ACTION"
								+ System.currentTimeMillis());
				handler.sendEmptyMessage(RECOMMEND_DATA_REFLESH_SUCCESS);
				// checkIsShowGuide();
				// v_blackbackground.setVisibility(View.GONE);
			} else if (mIntent.getAction().equals(
					HomeActions.NEWUPGRADER_ACTION)) {
				Log.d(TAG, "HomeActions.NEWUPGRADER_ACTION");
				Random random = new Random();
				int time = random.nextInt(420) % 420 + 1;
				newUpgraderHandler.setDelayMillis(time);
				newUpgraderHandler.startNewUpgrader();
			} else if (mIntent.getAction().equals(
					HomeActions.NEWUPGRADER_CANCEL_ACTION)) {
				Log.d(TAG, "HomeActions.NEWUPGRADER_CANCEL_ACTION");
				newUpgraderHandler.stopNewUpgrader();
			}
		}
	};

	protected void onDestroy() {

		unregisterReceiver(dataReceiver);
		// 反注册讯飞换台广播
		channelChangceReceiver.unRegisterSignalChangedReceiver(this);
		((AuxiliaryNetworkView) activityView
				.findViewById(R.id.auxiliary_network_view)).setUnregister();
		((AuxiliaryWeatherView) activityView
				.findViewById(R.id.auxiliary_weather_view)).setUnregister();
		((AuxiliaryStorageView) activityView
				.findViewById(R.id.auxiliary_storage_view)).setUnregister();
		abstract3dPagerStrategy.setUnregister();
		action = null;
		super.onDestroy();
	};

	/*
	 * 
	 * 语音切换首页tab
	 */
	protected void switchTab(String action, Intent intent, String tabName) {

        int targetTabIndex = HomeSwitchTabUtil.getHomeTabIndex(HomeActivity.this, action);
        Log.v("RemotecontrolReceiver", "targetTabIndex=" + targetTabIndex + ";mCurrentViewIndex=" + mCurrentViewIndex);
        // 避免没有极清页面语音极清时ｌａｕｎｃｈｅｒ崩溃
        if (targetTabIndex == -1) {
            HomeSwitchTabUtil.closeSiRi(HomeActivity.this, "暂不支持此功能", intent);
            return;
        }
        if (mCurrentViewIndex > targetTabIndex) {
        	this.matrixPager.moveToPreviousByDiff(mCurrentViewIndex-targetTabIndex);
        	
            this.navigationTabView.setIndexRequestFocus(targetTabIndex);

        } else if (targetTabIndex > mCurrentViewIndex) {
        	this.matrixPager.moveToNextByDiff(targetTabIndex-mCurrentViewIndex);
            this.navigationTabView.setIndexRequestFocus(targetTabIndex);

        } else if (targetTabIndex == mCurrentViewIndex) {
            if (this.isfirst) {
                HomeSwitchTabUtil.closeSiRi(HomeActivity.this, "正在打开" + tabName, intent);
                this.isfirst = false;
            } else {
                HomeSwitchTabUtil.closeSiRi(HomeActivity.this, "已经在" + tabName + "了", intent);

			}
			return;
		}

        HomeSwitchTabUtil.closeSiRi(HomeActivity.this, "正在打开" + tabName, intent);
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.FragmentActivity#onNewIntent(android.content.Intent
	 * )
	 */
	@Override
	protected void onNewIntent(Intent intent) {

		super.onNewIntent(intent);
		action = intent.getStringExtra(IVoiceController.COMMAND);
		String tabName = HomeSwitchTabUtil.getHomeTabName(action);
		if (action != null) {
			Log.v(TAG, "onNewIntent-->action:" + action);
			Log.v("RemotecontrolReceiver", "onNewIntent-->action:" + action);
			switchTab(action, intent, tabName);

		}
	}

	public HashMap<String, String[]> setCommands(String key, String vaule) {
		// 固定词
		commands.put(
				key,
				new String[] {
						new StringBuffer().append(vaule).toString(),
						new StringBuffer().append("打开").append(vaule)
								.toString(),
						new StringBuffer().append("打开").append(vaule)
								.append("频道").toString(),
						new StringBuffer().append("进入").append(vaule)
								.append("频道").toString(),
						new StringBuffer().append("进入").append(vaule)
								.toString() });
		return commands;
	}

	HashMap<String, String[]> commands;
	HashMap<String, String[]> commands1 = new HashMap<String, String[]>();
	private ArrayList<String> list;

	public String onQuery() {

		Log.d(TAG, "onQunery====>1");
		if (AppScene.getScene().equals(AppScene.GUIDE_SCENE)) {
			commands = new HashMap<String, String[]>();
			commands.put("start", new String[] { "开始体验", "开始" });
			commands.put("setting", new String[] { "设置", "进入设置" });
			commands.put("up", new String[] { "上", "向上", "往上" });
			commands.put("down", new String[] { "下", "向下", "往下" });
			commands.put("left", new String[] { "左", "向左", "往左" });
			commands.put("rigth", new String[] { "右", "向右", "往右" });
			commands.put("center", new String[] { "确定" });
			commands.put("back", new String[] { "返回" });
			commands.put("home", new String[] { "主页", "home" });
			commands.put("menus", new String[] { "菜单" });
			// commands.put("power", new String[] { "关机", "关闭", "关盒子", "关闭盒子"
			// });
			commands.put("boot", new String[] { "重启", "重新启动", "重启盒子" });
		} else if (isCinemaView) {
			commands = new HashMap<String, String[]>();
			list = abstract3dPagerStrategy.getCinemaName();
			if (null != list && list.size() != 0) {
				for (String value : list) {
					setCommands(value, value);
				}
			}
			commands.put("换一批频道", new String[] { "换一批频道", "请换一批频道", "再换一批频道",
					"更多频道", "换频道" });
			// 固定词
			// commands.put("播放", new String[] { "播放" });
			// commands.put("续播", new String[] { "续播" });
			// 预定义语义
			commands.put("play", new String[] { "$P(_PLAY)" });
			commands.put("page", new String[] { "$P(_PAGE)" });
			commands.put("episode", new String[] { "$P(_EPISODE)" });
			commands.put("select", new String[] { "$P(_SELECT)" });
			// 特殊增加
			commands.put("music", new String[] { "$P(_MUSIC)" });
			// / 模糊槽
			commands.put("video", new String[] { "$W(video)" });
			commands.put("test", new String[] { "$W(hello)" });
			commands.put("up", new String[] { "上", "向上", "往上" });
			commands.put("down", new String[] { "下", "向下", "往下" });
			commands.put("left", new String[] { "左", "向左", "往左" });
			commands.put("rigth", new String[] { "右", "向右", "往右" });
			commands.put("center", new String[] { "确定" });
			commands.put("back", new String[] { "返回" });
			commands.put("home", new String[] { "主页", "home" });
			commands.put("menus", new String[] { "菜单" });
			// commands.put("power", new String[] { "关机", "关闭", "关盒子", "关闭盒子"
			// });
			commands.put("boot", new String[] { "重启", "重新启动", "重启盒子" });
		}
		commands1.put("up", new String[] { "上", "向上", "往上" });
		commands1.put("down", new String[] { "下", "向下", "往下" });
		commands1.put("left", new String[] { "左", "向左", "往左" });
		commands1.put("rigth", new String[] { "右", "向右", "往右" });
		commands1.put("center", new String[] { "确定" });
		commands1.put("back", new String[] { "返回" });
		commands1.put("home", new String[] { "主页", "home" });
		commands1.put("menus", new String[] { "菜单" });
		// commands1.put("power", new String[] { "关机", "关闭", "关盒子", "关闭盒子" });
		commands1.put("boot", new String[] { "重启", "重新启动", "重启盒子" });

		HashMap<String, String[]> fuzzayWords1 = new HashMap<String, String[]>();
		// fuzzayWords1.put("video", VIDEOS);
		JSONObject jsonObject = null;
		JSONObject jsonObject1 = null;
		// 预定义槽二
		// String fuzzayVideoKey = "video";
		// String[] fuzzayVideoValue1 = new String[] { "收藏" };
		// String[] fuzzayVideoValue2 = new String[] { "取消收藏"};
		ArrayList<String[]> fuzzayValue = new ArrayList<String[]>();
		// fuzzayValue.add(fuzzayVideoValue1);
		// fuzzayValue.add(fuzzayVideoValue2);
		// 第三类
		HashMap<String, ArrayList<String[]>> fuzzayWords2 = new HashMap<String, ArrayList<String[]>>();
		fuzzayWords2.put("hello", fuzzayValue);
		try {
			jsonObject = JsonUtil.makeScenceJson(scenceId, commands,
					fuzzayWords1, fuzzayWords2);
			jsonObject1 = JsonUtil.makeScenceJson(scenceId, commands1,
					fuzzayWords1, fuzzayWords2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "onQunery====>2");
		if (AppScene.getScene().equals(AppScene.GUIDE_SCENE)) {
			return jsonObject.toString();
		} else if (!isCinemaView) {
			return jsonObject1.toString();
		}
		return jsonObject.toString();
	}

	// 场景id
	private String scenceId = "com.hiveview.tv.activity.HomeActivity";
	final String[] VIDEOS = new String[] { "电影", "电视剧", "综艺", "片花", "体育", "汽车" };

	public void onExecute(Intent intent) {
		super.onExecute(intent);
		if (intent.hasExtra("_scene")
				&& intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if (command.equals("start")) {
					guidePageView.hideGudiePage();
					HomeSwitchTabUtil.closeSiRi(HomeActivity.this, "开始体验",
							intent);
				} else if (command.equals("setting")) {
					guidePageView.GuideSettings();
					HomeSwitchTabUtil.closeSiRi(HomeActivity.this, "进入设置",
							intent);
				} else if (command.equals("换一批频道")) {
					abstract3dPagerStrategy.setPageChange(command);
					HomeSwitchTabUtil.closeSiRi(HomeActivity.this, "正在"
							+ command, intent);
				} else if (null != list && list.size() != 0) {
					for (String value : list) {
						if (value.equals(command)) {

							abstract3dPagerStrategy.setCinemaOnClick(command);
							HomeSwitchTabUtil.closeSiRi(HomeActivity.this,
									"正在打开" + command + "频道", intent);
						}
					}
				}

			}
		}
		Log.d(TAG, "onExecute====>");

	}

}