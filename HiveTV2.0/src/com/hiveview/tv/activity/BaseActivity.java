package com.hiveview.tv.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.KeyEventHandler;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.common.voice.OtherApkSenceUtils;
import com.hiveview.tv.common.voice.impl.OpenFunctionVoiceController;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.RecommendEntity;
import com.hiveview.tv.service.entity.SkinEntity;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.utils.ClientErrorDataCenter;
import com.hiveview.tv.utils.DialogUtils;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.OnDialogClickListener;
import com.hiveview.tv.utils.RemotecontrolUtils;
import com.hiveview.tv.utils.ToastUtils;
import com.hiveview.tv.view.AuxiliaryNetworkView;
import com.hiveview.tv.view.HiveViewErrorDialog;
import com.hiveview.tv.view.HiveViewNetFaultDialog;
import com.hiveview.tv.view.MatrixRecommendView;
import com.hiveview.tv.view.TipView;
import com.iflytek.STV.STV_Operator_Common.E_STBInfo;
import com.iflytek.xiri.scene.ISceneListener;
import com.paster.util.JsonUtil;


@SuppressLint("HandlerLeak")
public class BaseActivity extends FragmentActivity implements ISceneListener {

	private static final String TAG = "BaseActivity";
	protected HiveTVService service = new HiveTVService();
	// STV_Operator_Common 对象
	// private static STV_Operator_Common stv_operator = new
	// STV_Operator_Common();
	// 存放STB
	private static ArrayList<E_STBInfo> stbList;
	private final int REQUEST_PAGE_BACKGROUND_SUCCESS = 30000;
	private final int REQUEST_PAGE_BACKGROUND_FAIL = 30001;
	private final int REQUEST_PAGE_SET_BACKGROUND = 0x00154;
	private static ArrayList<SkinEntity> skinEntities = new ArrayList<SkinEntity>();
	private AppScene scene;
	public OtherApkSenceUtils otherApkSenceUtils;
	/* start by ZhaiJianfeng */
	HiveViewNetFaultDialog deviceCheckFailedDialog = null;
	/* end by ZhaiJianfeng */
	
	private long KeyTime = 700L;

	/* by zhangpengzhan */
	protected long startPlayerTime = 0;
	protected long startTime = 0;
	protected long resumeTime = 0;
	protected long pauseTime = 0;
	protected long stopTime = 0;
	protected long creatTime = 0;

	
	public static Stack<Activity> activityStack = new Stack<Activity>();
	
	private int count = 0;
	/**
	 * @Fields mKeyDownTime dispach 按键事件按键按下的时间
	 */
	private long mKeyDownTime = 0;
	
	/**
	 * @Fields getBgFailed
	 * 取背景图失败次数
	 */
	private int getBgFailed = 0;

	/**
	 * 访问次数计数器
	 */
	// public static int COUNTER = 0;
	// start by zhangpengzhan
	/**
	 * 是否锁网true为不锁，false锁网
	 */
	// public static boolean unLockNetFlag = true;
	/**
	 * 影片详情页浏览来源的报名对应关系
	 * 
	 * @Fields sourceMap
	 */
	public static Map<String, String> sourceMap = new HashMap<String, String>();
	static {
		sourceMap.put("com.hiveview.tv.activity.HomeActivity", "3");// 推荐位
		sourceMap.put("com.hiveview.tv.activity.SearchPageActivtiy", "2");// 搜索
		sourceMap.put("com.hiveview.tv.activity.SubjectDetailLandspaceActivity", "4");// 专题
		sourceMap.put("com.hiveview.tv.activity.SubjectDetailPortraitActivity", "4");// 专题
		sourceMap.put("com.hiveview.tv.activity.FilmDetailActivity", "5");// 影片中推荐
		sourceMap.put("com.hiveview.tv.activity.MovieDemandListActivity", "1");// 分类
		sourceMap.put("com.hiveview.tv.activity.VarietyPagerActivity", "1");// 分类
		sourceMap.put("com.hiveview.tv.activity.CollectActivity", "6");// 收藏
	}

	/**
	 * @Fields skipViewID:断网的时候不阻止进入的view 的id 号
	 */
	private static ArrayList<Integer> skipViewID = new ArrayList<Integer>();
	static {
		skipViewID.add(R.id.v_gudie_start);// 引导页设置按钮
		skipViewID.add(R.id.matrix_recommend_layout_4);// 首页已装应用游戏
		skipViewID.add(R.id.sub_navigation_common_game_installed_text_layout);// 游戏已装应用游戏
		skipViewID.add(R.id.sub_navigation_common_bluelight_installed_text_layout);// 极清已装应用游戏
		skipViewID.add(R.id.sub_navigation_common_tv_installed_text_layout);// 电视已装应用游戏
		skipViewID.add(R.id.sub_navigation_common_cinema_installed_text_layout);// 影院已装应用游戏
		skipViewID.add(R.id.sub_navigation_common_app_installed_text_layout);// 应用已装应用游戏

		skipViewID.add(R.id.sub_navigation_common_app_external_text_layout);// 应用外接存储
		skipViewID.add(R.id.sub_navigation_common_game_external_text_layout);// 游戏外接存储
		skipViewID.add(R.id.sub_navigation_common_tv_external_text_layout);// 电视外接存储
		skipViewID.add(R.id.sub_navigation_common_bluelight_external_text_layout);// 极清外接存储
		skipViewID.add(R.id.sub_navigation_common_recommend_external_text_layout);// 首页外接存储
		skipViewID.add(R.id.sub_navigation_common_cinema_external_text_layout);// 游戏外接存储

		skipViewID.add(R.id.sub_navigation_common_app_setting_text_layout);// 应用系统设置
		skipViewID.add(R.id.sub_navigation_common_game_setting_text_layout);// 游戏系统设置
		skipViewID.add(R.id.sub_navigation_common_tv_setting_text_layout);// 电视系统设置
		skipViewID.add(R.id.sub_navigation_common_bluelight_setting_text_layout);// 极清系统设置
		skipViewID.add(R.id.sub_navigation_common_recommend_setting_text_layout);// 首页系统设置
		skipViewID.add(R.id.sub_navigation_common_cinema_setting_text_layout);// 游戏系统设置
	}

	/**
	 * 当前页面是否时有key响应
	 * 
	 * @Fields isOneKeyDown
	 */
	private boolean isOneKeyDown = false;
	/**
	 * 是否点击播放
	 * 
	 * @Fields isOnClick
	 */
	protected boolean isOnClick = false;
	/**
	 * 需要传递的实体类
	 * 
	 * @Fields video_Entity
	 */
	protected HiveBaseEntity video_Entity = null;
	/**
	 * 需要传递的实体类
	 * 
	 * @Fields video_Entity
	 */
	public static HiveBaseEntity player_Entity = null;
	/**
	 * 需要传递的实体类
	 * 
	 * @Fields video_Entity
	 */
	public HiveBaseEntity player_video_Entity = null;
	/**
	 * 下层的activity
	 * 
	 * @Fields fromName
	 */
	protected String fromName = null;
	protected String firstClassName = null;
	protected String firstClassId = null;

	private String errorCode = "";
	private boolean isCloseActivity = false;

	/* end by zhangpengzhan */

	private DisplayImageOptions optionsBg = new DisplayImageOptions.Builder().cacheOnDisc(true).build();

	public AppScene getScene() {
		return scene;
	}

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case AppConstant.NO_0:
				if (null != findViewById(R.id.tipView))
					findViewById(R.id.tipView).setVisibility(View.GONE);
				break;

			}

		};
	};

	
	
	public Handler pageBgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REQUEST_PAGE_BACKGROUND_SUCCESS:
				getBgFailed++;
				removeMessages(REQUEST_PAGE_BACKGROUND_SUCCESS);
				if(getBgFailed > 5){
					break;
				}
				Log.d(TAG, "::get bgBitmap times =="+getBgFailed);
				setPageBackgroundImage();
				break;
			case REQUEST_PAGE_BACKGROUND_FAIL:
				break;
			case REQUEST_PAGE_SET_BACKGROUND:
				ImageView ivBg = (ImageView) findViewById(R.id.iv_background);
				if (null != ivBg && null != HiveviewApplication.bgBitmap) {
					ivBg.setImageBitmap(HiveviewApplication.bgBitmap);
				}
				break;
			}
		}

	};

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			processData(msg.what);
			processData(msg);
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Intent intent = getIntent();
		// Log.d(TAG, this + ":::" + intent);
		fromName = AppScene.CURRENCE_SCENE;
		creatTime = System.currentTimeMillis();
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		getPageBackgroundFromDB(false);
	}
	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		getPageBackgroundFromDB(false);
	}

	@Override
	protected void onStart() {
		super.onStart();
		startTime = System.currentTimeMillis();
		startTime = startTime - stopTime;
		Log.d(TAG, this.getClass().getName() + "::onStart");
		/*
		 * 初始化STV_Operator_Common，连接服务器，成功之后，才可以调用超级智控的接口进行相关操作。
		 */
	}

	@Override
	protected void onResume() {
		super.onResume();
		activityStack.add(this);
		Log.v("BaseActivity "," onResume"
				+ activityStack.lastElement().getLocalClassName());
		Log.d(TAG, this.getClass().getName() + "::onResume");
		// start by atuhor:zhangpengzhan
		// 保证程序健壮性catch
		
		scenceId=this.getClass().getName() ;
		try {
			resumeTime = System.currentTimeMillis();
			long platerTime = 0;
			// 如果是点击播放回来的
			if (isOnClick) {

				// 统计播放时长.点击播放之后的时长
				platerTime = resumeTime - startPlayerTime;
				if ("com.hiveview.tv.activity.FilmDetailActivity".equals(this.getClass().getName())
						|| "com.hiveview.tv.activity.VarietyPagerActivity".equals(this.getClass().getName())) {
					KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setTimeLength(String.valueOf(platerTime))
							.setBeginTime(String.valueOf(pauseTime)).setEndTime(String.valueOf(resumeTime)).setEntity((FilmNewEntity) player_Entity)
							.setViewPosition("0201")
							// .setSrcType(ItemType.VIDEO)
							.setTabNo(Tab.FILM).setDataType(DataType.CLICK_TAB_FILM).build());
				} else if ("com.hiveview.tv.activity.TeleplayDetailActivity".equals(this.getClass().getName())) {// 电视详情
					KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setTimeLength(String.valueOf(platerTime))
							.setBeginTime(String.valueOf(pauseTime)).setEndTime(String.valueOf(resumeTime)).setEntity((FilmNewEntity) video_Entity)
							.setViewPosition("0201")
							// .setSrcType(ItemType.VIDEO)
							.setTabNo(Tab.FILM).setDataType(DataType.CLICK_TAB_FILM).build());
				} else if ("com.hiveview.tv.activity.VarietyDetailActivity".equals(this.getClass().getName())) {// 综艺详情
					KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setTimeLength(String.valueOf(platerTime))
							.setBeginTime(String.valueOf(pauseTime)).setEndTime(String.valueOf(resumeTime)).setEntity((FilmNewEntity) video_Entity)
							.setViewPosition("0201")
							// .setSrcType(ItemType.VIDEO)
							.setTabNo(Tab.FILM).setDataType(DataType.CLICK_TAB_FILM).build());
				}

				// 重置点击变量
				isOnClick = false;
			}
			if (MatrixRecommendView.isPlayer && "com.hiveview.tv.activity.HomeActivity".equals(this.getClass().getName())) {// 竖版频道
				KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setTimeLength(String.valueOf(platerTime))
						.setBeginTime(String.valueOf(pauseTime)).setEndTime(String.valueOf(resumeTime)).setEntity((RecommendEntity) player_Entity)
						.setViewPosition("0201")
						// .setSrcType(ItemType.VIDEO)
						.setTabNo(Tab.TAB).setDataType(DataType.CLICK_TAB_RECOMMEND).build());
				MatrixRecommendView.isPlayer = false;
			}
			Log.d(TAG, "onResume");
			if (otherApkSenceUtils != null) {
				Log.d(TAG, "otherApkSenceUtils != null" + (otherApkSenceUtils != null));
				otherApkSenceUtils.release();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// end by zhangpengzhan
		/* 为语音操控提供场景信息 */
		Log.d(TAG, this.getClass().getName());
		scene = AppScene.getInstance();
		scene.createScene(this, this);
		scene.initScene(this.getClass().getName());

	}

	@Override
	protected void onPause() {
		pauseTime = System.currentTimeMillis();
		Log.d(TAG, this.getClass().getName() + "::onPause");
		scene.release();
		super.onPause();
	}

	/* 当前页面停止活动 */
	protected void onStop() {
		stopTime = System.currentTimeMillis();
		Log.d(TAG, this.getClass().getName() + "::onStop");
		super.onStop();
	}

	/* activity销毁的时候调用的 */
	protected void onDestroy() {
		Log.d(TAG, this.getClass().getName() + "::onDestroy");
		long allTime = 0;
		try {
			allTime = stopTime - creatTime;
			String allTimes = String.valueOf(allTime);
			Log.d(TAG, "allTime::" + allTime);
			Log.d(TAG, "isOneKeyDown::" + isOneKeyDown);
			if (null != video_Entity
					&& ("com.hiveview.tv.activity.FilmDetailActivity".equals(this.getClass().getName())
							|| "com.hiveview.tv.activity.TeleplayDetailActivity".equals(this.getClass().getName())
							|| "com.hiveview.tv.activity.VarietyDetailActivity".equals(this.getClass().getName()) || "com.hiveview.tv.activity.VarietyPagerActivity"
								.equals(this.getClass().getName()))) {// 说明是详情页的activity
				KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setStayTimeLength(allTimes).setEntity(video_Entity).setTabNo(Tab.TAB)
						.setViewPosition("0302").setNoAction(isOneKeyDown ? "0" : "1")
						.setSource(null == sourceMap.get(fromName) ? "0" : sourceMap.get(fromName)).setDataType(DataType.CLICK_TAB_FILM).build());
				Log.d(TAG, "LAST_SCENE::" + AppScene.LAST_SCENE);
				Log.d(TAG, "allTime::" + allTime);
				Log.d(TAG, "isOneKeyDown::" + isOneKeyDown);
			} else {// 不是详情页面
				Log.d(TAG, "当前的class名字::" + this.getClass().getName());
				if ("com.hiveview.tv.activity.SubjectTopicListActivity".equals(this.getClass().getName())) {// 专题的第一个列表
					KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setStayTimeLength(allTimes).setTabNo(Tab.TAB)
							.setViewPosition("0303").setNoAction(isOneKeyDown ? "0" : "1").setMovieDemandQueryInfo("专题", "0")
							// .setSrcType(ItemType.VIDEO)
							.setDataType(DataType.TIME_LENGTH_MOVIE_DEMAND_LIST).build());
					Log.d(TAG, "allTime::" + allTime);
				} else if ("com.hiveview.tv.activity.SubjectDetailLandspaceActivity".equals(this.getClass().getName())
						|| "com.hiveview.tv.activity.SubjectDetailPortraitActivity".equals(this.getClass().getName())) {// 横版和竖版的详细专题列表
					// start:修改0302为0304,author:huzuwei
					KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setStayTimeLength(allTimes).setEntity(video_Entity)
							.setTabNo(Tab.TAB).setViewPosition("0304").setNoAction(isOneKeyDown ? "0" : "1")
							.setDataType(DataType.TIME_LENGTH_SUBJECT)
							// .setSrcType(ItemType.VIDEO)
							.build());
					// end
					Log.d(TAG, "allTime::" + allTime);
				} else if ("com.hiveview.tv.activity.VarietyPagerActivity".equals(this.getClass().getName())) {// 横版频道
					KeyEventHandler.post(new DataHolder.Builder(getBaseContext())
							.setStayTimeLength(allTimes)
							// start by huzuwei
							// .setTabNo(Tab.DEFAULT)
							// end by huzuwei
							.setViewPosition("0303").setNoAction(isOneKeyDown ? "0" : "1")
							.setMovieDemandQueryInfo(null == firstClassName ? "" : firstClassName, null == firstClassId ? "" : firstClassId)
							.setTabNo(Tab.TAB)
							// .setSrcType(ItemType.VIDEO)
							.setDataType(DataType.TIME_LENGTH_MOVIE_DEMAND_LIST).build());
					Log.d(TAG, "allTime::" + allTime);
				} else if ("com.hiveview.tv.activity.MovieDemandListActivity".equals(this.getClass().getName())) {// 竖版频道
					KeyEventHandler.post(new DataHolder.Builder(getBaseContext())
							.setStayTimeLength(allTimes)
							// start by huzuwei
							// .setTabNo(Tab.DEFAULT)
							// end by huuzuwei
							.setViewPosition("0303").setNoAction(isOneKeyDown ? "0" : "1")
							.setMovieDemandQueryInfo(null == firstClassName ? "" : firstClassName, null == firstClassId ? "" : firstClassId)
							.setTabNo(Tab.TAB)
							// .setSrcType(ItemType.VIDEO)
							.setDataType(DataType.TIME_LENGTH_MOVIE_DEMAND_LIST).build());
					Log.d(TAG, "allTime::" + allTime);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		isOneKeyDown = false;
		Log.d(TAG, "HiveviewAppLib.sourceMap::" + sourceMap.size());
		Log.d(TAG, "fromName::" + fromName);
		Log.d(TAG, "name::" + this.getClass().getName());
		super.onDestroy();
	}

	/**
	 * 耗时操作完成后数据处理逻辑,参数为int,与 processData(Message msg)排斥使用
	 */
	protected void processData(int msgWhat) {
	}

	/**
	 * 耗时操作完成后数据处理逻辑,参数为Message,意味着可以携带更多的信息,与 processData(int msgWhat)排斥使用
	 */
	protected void processData(Message msg) {
	}

	/**
	 * 发送网络请求请求数据
	 * 
	 * @param runnable
	 *            请求数据的耗时操作
	 */
	protected final void submitRequest(Runnable runnable) {
		if (null != runnable) {
			HttpTaskManager.getInstance().submit(runnable);
		}
	}

	/**
	 * 网络故障时调用的对话框
	 * 
	 * @param listener
	 */
	protected void showDialogAboutNetFault() {
		DialogUtils.showDialogAboutNetFault(this, new OnDialogClickListener() {

			@Override
			public void onConfirm() {

			}

			@Override
			public void onCancel() {

			}
		});
	}

	/**
	 * 
	 * @func: showDialogAboutDeviceCheckFailed
	 * @author:ZhaiJianfeng
	 * @Description: 鉴权错误时弹框
	 */
	protected void showDialogAboutDeviceCheckFailed() {
		deviceCheckFailedDialog = new HiveViewNetFaultDialog(this, new OnDialogClickListener() {

			@Override
			public void onConfirm() {
				try {
					Intent mIntent = new Intent();
					mIntent.setAction("com.hiveview.domybox.ACTION_SETTING_NET");
					mIntent.addCategory(Intent.CATEGORY_DEFAULT);
					startActivity(mIntent);
					deviceCheckFailedDialog.dismiss();
				} catch (Exception e) {
					Log.e(TAG, "showDialogAboutDeviceCheckFailed:onConfirm:e" + e.toString());
					e.printStackTrace();
				}
			}

			@Override
			public void onCancel() {
				deviceCheckFailedDialog.dismiss();
			}
		});
		deviceCheckFailedDialog.setTitleContent(getResources().getString(R.string.net_exception_message));
		deviceCheckFailedDialog.setButtonsText(getResources().getString(R.string.net_exception_setting),getResources().getString(R.string.net_exception_cancel));
		deviceCheckFailedDialog.show();
	}

	/**
	 * 得到当前的背景图url信息
	 * 
	 * @Title: BaseActivity
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param isRefleshBgData
	 *            是否要刷新原有的背景图信息
	 */
	protected void getPageBackgroundFromDB(boolean isRefleshBgData) {
		if (isRefleshBgData) {
			skinEntities.clear();
		}

		/*if (skinEntities.size() == 0) {
			ubmitRequest(new SafeRunnable() {

				@Override
				public void requestData() {
					PageSkinDAO skinDAO = new PageSkinDAO(getApplicationContext());
					while (skinEntities.size() != 7) {
						skinEntities = skinDAO.query(null, null, null, null);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					int what = (null != skinEntities && skinEntities.size() > 0) ? REQUEST_PAGE_BACKGROUND_SUCCESS : REQUEST_PAGE_BACKGROUND_FAIL;
					pageBgHandler.sendEmptyMessage(what);
				}

				@Override
				public void processServiceException(ServiceException e) {
					pageBgHandler.sendEmptyMessage(REQUEST_PAGE_BACKGROUND_FAIL);
				}
			});
		} else {*/
		pageBgHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		});
		submitRequest(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setPageBackgroundImage();
			}
		});
			
		/*}*/
	}

	/**
	 * 当异步得到各个背景图信息时，子类可根据获得的背景图信息来设置背景
	 * 
	 * @param skins
	 */
	private void setPageBackgroundImage() {
		// ImageView ivBg = (ImageView) findViewById(R.id.iv_background);
		// if (null != ivBg && null != skinEntities && skinEntities.size() > 1)
		// {
		/*
		 * // end 背景的索引是1所以修改 判断列表长度最小为2 SkinEntity entity =
		 * skinEntities.get(getBackgroundIndex() < skinEntities.size() ?
		 * getBackgroundIndex() : 0); String url = getBackgroundIsInside() ?
		 * entity.getImg_url_inside() : entity.getImg_url_outside();
		 * Log.d("hive_bg", url); ImageLoader.getInstance().displayImage(url,
		 * ivBg, optionsBg);
		 */
		try {
			Log.d(TAG, "::bgBitmap is start");
			if(null == HiveviewApplication.bgBitmap){
				ContentResolver resolver = BaseActivity.this.getContentResolver();
				Bundle argBundle = new Bundle();
				argBundle.putInt("type", 7);
				argBundle.putString("columeImage", "img_url_outside");
				Bundle bitmapBundle = resolver.call(Uri.parse("content://HiveViewAuthoritiesDataProvider"), "getBackgroundSkins", null, argBundle);
				HiveviewApplication.bgBitmap = (Bitmap) bitmapBundle.getParcelable("img_url_outside");
				Log.d(TAG, "::bgBitmap is success");
				if(null == HiveviewApplication.bgBitmap&&"com.hiveview.tv.activity.HomeActivity".equals(this.getClass().getName())){
					Log.d(TAG, "::bgBitmap is null");
					//如果bitmap 没有拿去成功，两分钟后再取一次
					pageBgHandler.removeMessages(REQUEST_PAGE_BACKGROUND_SUCCESS);
					pageBgHandler.sendEmptyMessageDelayed(REQUEST_PAGE_BACKGROUND_SUCCESS,2*60*1000);
				}else{
					pageBgHandler.sendEmptyMessage(REQUEST_PAGE_SET_BACKGROUND);
				}
			}else{
				pageBgHandler.sendEmptyMessage(REQUEST_PAGE_SET_BACKGROUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
	}

	/**
	 * 加载指定url的背景图，主要在专题详情页中使用，加载属于专题自己独有的背景
	 * 
	 * @Title: BaseActivity
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param bgUrl
	 */
	protected void setPageBackgroundImage(String bgUrl) {
		try {
			ImageView ivBg = (ImageView) findViewById(R.id.iv_background);
			if (null != ivBg && null != bgUrl && bgUrl.length() > 0) {
				ImageLoader.getInstance().displayImage(bgUrl, ivBg, optionsBg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	/**
//	 * 返回当前页面对应的背景图(SkinEntity对象)，所在的背景图对象集合（skinEntities）中的位置
//	 * 如：0.游戏，1.电视，2.推荐，3.影院，4.应用
//	 * 
//	 * @return
//	 */
//	private int getBackgroundIndex() {
//		return 6;
//	}
//
//	/**
//	 * 返回当前页面对应的背景图(SkinEntity对象),是否使用SkinEntity对象的img_url_inside字段的值作为当前页背景的URL
//	 * ,返回true表示是，否则用img_url_outside 作为背景图的URL
//	 */
//	private boolean getBackgroundIsInside() {
//		return false;
//	}

	/**
	 * 用户提示
	 * 
	 * @param text
	 */
	protected void alert(String text) {
		ToastUtils.alert(this, text);
	}

	/**
	 * 用户提示，数据错误
	 * 
	 * @param text
	 */
	protected void alertError() {
		ToastUtils.alert(this, getString(R.string.alert_data_error));
	}

	protected void showErrorTipView(String errorCode) {

		TipView tipView = (TipView) findViewById(R.id.tipView);
		if (null != tipView) {
			tipView.setVisibility(View.VISIBLE);
			tipView.showTipWithCode(errorCode);
		}
		mHandler.removeMessages(AppConstant.NO_0);
		mHandler.sendEmptyMessageDelayed(AppConstant.NO_0, 5000);
	}

	protected void hideErrorTipView() {
		if (null != findViewById(R.id.tipView))
			findViewById(R.id.tipView).setVisibility(View.GONE);
	}

	

	public boolean dispatchKeyEvent(KeyEvent event) {
		Log.d(TAG, "dispatchKeyEvent::KeyEvent.event-->1:"+event.getAction()+"||"+event.getKeyCode());
		if (event.getRepeatCount() <= 1) {
			mKeyDownTime = event.getEventTime();
			count = 0;
		} else {
			if("com.hiveview.tv.activity.HomeActivity".equals(this.getClass().getName())){
				KeyTime = 800L;
			}else{
				KeyTime = 300L;
			}
			if (event.getEventTime() - mKeyDownTime >= KeyTime) {
				count++;
				Log.d(TAG, "dispatchKeyEvent::KeyEvent.event-->1.1:"+count);
				mKeyDownTime = event.getEventTime();
			} else {
				return true;
			}
		}
		
		//RemotecontrolUtils.onKeyExecute(this, scene, event);
		//Log.d(TAG, "dispatchKeyEvent::KeyEvent.event-->2:"+event.getAction());
		if (event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
			isOneKeyDown = true;
		}
		//Log.d(TAG, "dispatchKeyEvent::KeyEvent.event-->3:"+event.getAction());
		/** start by ZhaiJianfeng */

		if (deviceCheckFailedDialog != null && deviceCheckFailedDialog.isShowing()) {
			return true;
		}
		//Log.d(TAG, "dispatchKeyEvent::KeyEvent.event-->4:"+event.getAction());
		if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER && (!HiveTVService.isAvailable(getApplicationContext())
		/* || !HiveviewApplication.isNetStatusAvailiable() */) && !(skipViewID.contains(getCurrentFocus() != null ? getCurrentFocus().getId() : 0))) {
			showDialogAboutDeviceCheckFailed();
			return true;
		}
		//Log.d(TAG, "dispatchKeyEvent::KeyEvent.event-->5:"+event.getAction());
		// /* end by ZhaiJianfeng */
		// if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER &&
		// !preferenceDAO.getErrorCode().equals(ErrorCode.CODE_SUCCESS)) {
		// showErrorTipView(preferenceDAO.getErrorCode());
		// }

		// if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER &&
		// (!HiveTVService.isAvailable(getApplicationContext()) ||
		// !HiveviewApplication.isNetStatusAvailiable())) {
		// showDialogAboutDeviceCheckFailed();
		// return true;
		// }
		// if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER &&
		// (!HiveTVService.isAvailable(getApplicationContext()) ||
		// !HiveviewApplication.isNetStatusAvailiable())) {
		// showDialogAboutDeviceCheckFailed();
		// return true;
		// }
		/* end by ZhaiJianfeng */
		return super.dispatchKeyEvent(event);
	}

	public static ArrayList<E_STBInfo> getSTBSetting() {
		for (E_STBInfo stbInfo : stbList) {
			Log.d(TAG, "stbInfo.provinceName" + stbInfo.provinceName);
			Log.d(TAG, "stbInfo.cityName" + stbInfo.cityName);
			Log.d(TAG, "stbInfo.spName" + stbInfo.spName);
			Log.d(TAG, "stbInfo.irName" + stbInfo.irName);
			Log.d(TAG, "stbInfo.inputSource" + stbInfo.inputSource);

		}

		return stbList;

	}




	/**
	 * 点击播放按钮时候发送统计消息
	 * 
	 * @Title: BaseActivity
	 * @author:张鹏展
	 * @Description:
	 * @param allTimes
	 * @param video_Entity
	 * @param dataType
	 */
	protected void sendStatistics(String allTimes, HiveBaseEntity video_Entity, DataType dataType) {
		KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setStayTimeLength(allTimes).setEntity(video_Entity)
		// .setSrcType(ItemType.VIDEO)
				.setTabNo(Tab.TAB).setViewPosition("0302").setNoAction(isOneKeyDown ? "0" : "1").setDataType(dataType).build());
	}

	/**
	 * 点击播放按钮时候发送统计消息
	 * 
	 * @Title: BaseActivity
	 * @author:张鹏展
	 * @Description:
	 * @param allTimes
	 * @param video_Entity
	 * @param dataType
	 */
	protected void sendSubjectStatistics(HiveBaseEntity video_Entity, DataType dataType) {
		KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setEntity(video_Entity).setTabNo(Tab.TAB).setViewPosition("0305")
				.setDataType(dataType).build());
	}

	/**
	 * 错误提示框
	 * 
	 * @Title: BaseActivity
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param code
	 *            由异常信息抛出的错误码
	 * @param isClose
	 *            是否关闭当前的Activity
	 */
	public void showErrorDialog(String code, boolean isClose) {
		errorCode = code;
		this.isCloseActivity = isClose;
		if (errorCode.equals(HiveViewErrorCode.E0000599)) {// 网络未连接
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					showDialogAboutDeviceCheckFailed();
				}
			});
		} else if (errorCode.equals(HiveViewErrorCode.E0000605)) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					alert(ClientErrorDataCenter.getInstance().getErrorContentByErrorCode(errorCode));
				}
			});
		} else {// 程序网络出错，提示框
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// start author:zhangpengzhan 判断当前的activity是否是被finish
					// ，如果已经finish就不显示dialog
					if (!BaseActivity.this.isFinishing()) {// end
						HiveViewErrorDialog dialog = new HiveViewErrorDialog(BaseActivity.this, errorCode, isCloseActivity);

						dialog.show();
					}
				}
			});
		}

	}
	
	@Override
	public void onExecute(Intent intent) {
		Log.d(TAG, "onExecute");
		String command = intent.getStringExtra("_command");
		Log.d(TAG, command);
		if (command.equals("setnet")) {
			HomeSwitchTabUtil.closeSiRi(this,
					"设置网络", intent);
			try {
				Intent mIntent = new Intent();
				mIntent.setAction("com.hiveview.domybox.ACTION_SETTING_NET");
				mIntent.addCategory(Intent.CATEGORY_DEFAULT);
				startActivity(mIntent);
				deviceCheckFailedDialog.dismiss();
			} catch (Exception e) {
				Log.e(TAG, "showDialogAboutDeviceCheckFailed:onConfirm:e" + e.toString());
				e.printStackTrace();
			}
		}else if (command.equals("cancle")) {
			deviceCheckFailedDialog.dismiss();
			HomeSwitchTabUtil.closeSiRi(this,
					"取消", intent);
		}else
			if (command.equals("up")) {
				try {
					Process p = Runtime.getRuntime().exec("adb shell input keyevent 19");
					HomeSwitchTabUtil.closeSiRi(this, "上", intent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (command.equals("down")) {
				try {
					Process p = Runtime.getRuntime().exec("adb shell input keyevent 20");
					HomeSwitchTabUtil.closeSiRi(this, "下", intent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (command.equals("left")) {
				try {
					Process p = Runtime.getRuntime().exec("adb shell input keyevent 21");
					HomeSwitchTabUtil.closeSiRi(this, "左", intent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (command.equals("rigth")) {

				try {
					Process p = Runtime.getRuntime().exec("adb shell input keyevent 22");
					HomeSwitchTabUtil.closeSiRi(this, "右", intent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (command.equals("center")) {

				try {
					Process p = Runtime.getRuntime().exec("adb shell input keyevent 23");
					HomeSwitchTabUtil.closeSiRi(this, "确定", intent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (command.equals("back")) {

				try {
					Process p = Runtime.getRuntime().exec("adb shell input keyevent 4");
					HomeSwitchTabUtil.closeSiRi(this, "返回", intent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (command.equals("menus")) {

				try {
					Process p = Runtime.getRuntime().exec("adb shell input keyevent 82");
					HomeSwitchTabUtil.closeSiRi(this, "菜单", intent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (command.equals("home")) {

				try {
					Process p = Runtime.getRuntime().exec("adb shell input keyevent 3");
					HomeSwitchTabUtil.closeSiRi(this, "首页", intent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//			} else if (command.equals("power")) {
//
//				try {
//					Process p = Runtime.getRuntime().exec("adb shell input keyevent 26");
//					HomeSwitchTabUtil.closeSiRi(this, "关机", intent);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			} else if (command.equals("boot")) {

				try {
					Process p = Runtime.getRuntime().exec("adb reboot");
					HomeSwitchTabUtil.closeSiRi(this, "重启", intent);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}  
		
		return;
	}
	
	HashMap<String, String[]> commands = new HashMap<String, String[]>();
	{
		commands.put("up", new String[] { "上", "向上", "往上" });
		commands.put("down", new String[] { "下", "向下", "往下" });
		commands.put("left", new String[] { "左", "向左", "往左" });
		commands.put("rigth", new String[] { "右", "向右", "往右" });
		commands.put("center", new String[] { "确定" });
		commands.put("back", new String[] { "返回" });
		commands.put("home", new String[] { "主页","home" });
		commands.put("menus", new String[] { "菜单" });
//		commands.put("power", new String[] { "关机","关闭","关盒子","关闭盒子" });
		commands.put("boot", new String[] { "重启","重新启动","重启盒子"});
	}

	// 场景id
	private String scenceId ;
	@Override
	public String onQuery() {
		// TODO 自动生成的方法存根
		commands.put("setnet", new String[] { "设置网络", "去设置" });
		commands.put("cancle", new String[] { "取消" });
		Log.d(TAG, "onQunery====>1");

		HashMap<String, String[]> fuzzayWords1 = new HashMap<String, String[]>();
		// fuzzayWords1.put("video", VIDEOS);
		JSONObject jsonObject = null;
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
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "onQunery====>2");
		return jsonObject.toString();
	}
}
