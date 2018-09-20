package com.hiveview.tv.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.hdmiin.BaseTvPlayHandlerInterface;
import com.hiveview.tv.hdmiin.InvokeTVHandler;
import com.hiveview.tv.hdmiin.NetStreamHandler;
import com.hiveview.tv.hdmiin.OverlayViewHandler;
import com.hiveview.tv.hdmiin.SWHDMIInManagerViewHandler;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.OnliveTipService;
import com.hiveview.tv.service.dao.OnliveTipsDAO;
import com.hiveview.tv.service.entity.ChannelEntity;
import com.hiveview.tv.service.entity.OnliveTipsEntity;
import com.hiveview.tv.service.entity.ProgramEntity;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.DateUtils;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.KeyMappingHashMapUtil;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.STBSettingInfoUtil;
import com.hiveview.tv.utils.SwitchChannelUtils;
import com.hiveview.tv.utils.ToastUtils;
import com.hiveview.tv.view.HiveviewHdmiInView;
import com.hiveview.tv.view.TvChannelPageViewItemView;
import com.hiveview.tv.view.onlive.OnliveEpg;
import com.hiveview.tv.view.television.voicecontrol.TVCodeBroadcastReciver;
import com.paster.util.JsonUtil;

public class OnlivePlayerActivity extends BaseActivity implements OnKeyListener {
	private static final String TAG = "OnlivePlayerActivity";

	private static String CHANNEL_CODE = "";

	private static String CHANNEL_LOGO = "";

	private static String CHANNEL_NAME = "";
	/**
	 * 获取电台信息成功
	 */
	protected static final int RQUEST_CHANNEL_FAIL = 200;
	/**
	 * 获取电台信息失败
	 */
	protected static final int RQUEST_CHANNEL_SUCCESS = -200;

	// private View viewRecommned;
	private View viewSelect;
	private OnliveEpg viewEpg;

	private static View rlMenuTips;

	private TextView tvRecommend;
	private TextView tvSelect;
	private TextView tvEpg;

	/**
	 * 精彩推荐的外框
	 * 
	 * @Fields flRemmond:TODOl
	 */
	private FrameLayout flRemmond;
	/**
	 * @Fields flSelect:TODO 快速选台的外框
	 */
	private FrameLayout flSelect;

	/**
	 * @Fields flEPG:TODO节目单的外框
	 */
	private FrameLayout flEPG;

	// private boolean mCVTE = true;
	// private boolean mInvoke = false;
	// private boolean mNetStream = false;
	// private boolean mRtkHdminRx = false;
	private BaseTvPlayHandlerInterface handlerInterface = null;
	/**
	 * 图片下载器
	 */
	protected ImageLoader imageLoader = null;
	/**
	 * 选中的id
	 */
	private int[] tvs = null;
	private TextView[] tvViews = null;
	private DisplayImageOptions optionsPoster;
	/**
	 * 从ViewPager中的ItemView上往上按键焦点落到Id为mUpFocusId的View上
	 */
	private int mUpFocusId = 0;
	/**
	 * 央视名称 TextView
	 */
	private TextView tvCCTV;
	/**
	 * 卫视名称 TextView
	 */
	private TextView tvStatelliteTV;
	/**
	 * 地方卫视名称 TextView
	 */
	private TextView tvLocal;
	/**
	 * 高清频道名称 TextView
	 */
	private TextView tvHDChannel;
	/**
	 * 收费名称 TextView
	 */
	private TextView tvSubChannel;
	/**
	 * 电视Tab的数组
	 */
	private View tabViews[] = null;

	/**
	 * flSelectTv FrameLayout
	 */
	private FrameLayout flSelectTv;
	/**
	 * 央视FrameLayout
	 */
	private FrameLayout flCCTV;
	/**
	 * 卫视FrameLayout
	 */
	private FrameLayout flStatelliteTV;
	/**
	 * 本地FrameLayout
	 */
	private FrameLayout flLocal;
	/**
	 * 高清FrameLayout
	 */
	private FrameLayout flHDChannel;
	/**
	 * 收费FrameLayout
	 */
	private FrameLayout flSubChannel;
	/**
	 * 电台结合
	 */
	private List<ChannelEntity> channelList;
	/**
	 * 精彩推荐条目
	 */
	// protected LinearLayout rl_movie_list_first_line_1_item;
	// protected LinearLayout rl_movie_list_first_line_2_item;
	// protected LinearLayout rl_movie_list_first_line_3_item;
	// protected LinearLayout rl_movie_list_first_line_4_item;
	/**
	 * 推荐视频View的ID数组
	 */
	private int[] recommendId = null;
	/**
	 * 节目单VIew
	 */
	private boolean isback = false;
	private int index = 0;
	//当前activity的状态
	public static boolean isResume=true;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_onlive);
		initView();
	}

	public boolean getIsback() {
		return isback;
	}

	public void setIsback(boolean isback) {
		this.isback = isback;
	}

	@Override
	protected void onResume() {
		// TODO
		Log.i(TAG, "onResume->start");
		super.onResume();
		isResume=true;
		if (HiveviewHdmiInView.mCVTE) {
			handlerInterface.tvPlaySchemeInit();
		}
		if (HiveviewHdmiInView.mInvoke) {
			handlerInterface.tvPlaySchemeInit();
		}

		if (HiveviewHdmiInView.mNetStream) {
			handlerInterface.tvPlaySchemeInit();
		}
		if (HiveviewHdmiInView.mRtkHdminRx) {
			Log.i("SWHDMIInManagerViewHandler", "OnlivePlayerActivity................onResume");
			handlerInterface.tvPlaySchemeInit();
		}

		STBSettingInfoUtil.adaptSTBSetCurrentScene(OnlivePlayerActivity.this, STBSettingInfoUtil.HDMIIN_SCENE);
		Log.i(TAG, "onResume->end");
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause->start");
		super.onPause();
		isResume=false;
		/*
		 * 判断是否由tv页面切换过来，是的话不调用overlayViewDeinit()
		 */
		if (!(getIsback() && AppScene.getLastScene().equals(AppScene.TVVIEW_SCENE))) {
			Log.i("SWHDMIInManagerViewHandler", "OnlivePlayerActivity................onPause");
			if (HiveviewHdmiInView.mCVTE) {
				handlerInterface.tvPlaySchemeDeinit();
			}
		}
		if (HiveviewHdmiInView.mNetStream) {
			handlerInterface.tvPlaySchemeDeinit();
		}
		if (HiveviewHdmiInView.mInvoke) {
			handlerInterface.tvPlaySchemeDeinit();
		}

		if (HiveviewHdmiInView.mRtkHdminRx) {
			handlerInterface.tvPlaySchemeDeinit();
		}
		setIsback(false);
		// writeFile(DISABLE_VIDEO_PATH, VIDEO_DISABLE);
		// writeFile(FB0_PATH, mMode);
		STBSettingInfoUtil.adaptSTBSetCurrentScene(OnlivePlayerActivity.this, null);
		Log.i(TAG, "onPause->end");
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "onStop->start");
		super.onStop();
		Log.i(TAG, "onStop->end");
	}

	@Override
	protected void onDestroy() {
		// 告诉讯飞退出直播
		// Log.i(TAG, "notifyTVLiveStatus------------------>onDestroy");
		// TvManUtil.tellTVState(this, false, null);
		try {
			if (null != viewEpg) {
				viewEpg.unRegisterReceiver();
			}
		} catch (Exception e) {
			Log.i(TAG, "unRegisterReceiver   epg  error");
		}
		if (null != handlerInterface) {
			handlerInterface.setUnregister();
		}
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

	private void initView() {
		// CHANNEL_CODE = getIntent().getStringExtra(AppConstant.CHANNEL_CODE);
		// // 台标
		// CHANNEL_LOGO = getIntent().getStringExtra(AppConstant.CHANNEL_LOGO);
		//
		CHANNEL_NAME = SwitchChannelUtils.getCurrentChannelName(this);
		//
		// imageLoader = ImageLoader.getInstance();
		//
		// optionsPoster = new DisplayImageOptions.Builder()
		// .showImageForEmptyUri(R.drawable.epg_image_default)
		// .showImageOnFail(R.drawable.epg_image_default)
		// .resetViewBeforeLoading(false).cacheOnDisc(true)
		// .imageScaleType(ImageScaleType.EXACTLY)
		// .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
		// .build();
		Log.i(TAG, "initView->start");
		FrameLayout rootView = (FrameLayout) this.findViewById(R.id.fl_onlive_player);

		if (HiveviewHdmiInView.mCVTE) {
			handlerInterface = new OverlayViewHandler(rootView, this, true/* isFullScreen */);
			// mOverlayViewHandler = new
			// OverlayViewHandler(overlayView/*rootView*/, this,
			// true/*isFullScreen*/);
		}
		if (HiveviewHdmiInView.mInvoke) {
			handlerInterface = new InvokeTVHandler(rootView, this, true/* isFullScreen */);
		}
		if (HiveviewHdmiInView.mNetStream) {
			handlerInterface = new NetStreamHandler(rootView, this, true/* isFullScreen */);
		}
		if (HiveviewHdmiInView.mRtkHdminRx) {
			Log.i("SWHDMIInManagerViewHandler", "OnlivePlayerActivity................new SWHDMIInManagerViewHandler");
			handlerInterface = new SWHDMIInManagerViewHandler(rootView, this, true/* isFullScreen */);
		}

		LayoutInflater inflater = getLayoutInflater();
		rlMenuTips = inflater.inflate(R.layout.activity_onlive_menu_tip, null);
		rootView.addView(rlMenuTips);
		rlMenuTips.setVisibility(View.GONE);
		// viewRecommned = this.findViewById(R.id.view_onlive_recommend);
		viewSelect = this.findViewById(R.id.view_onlive_select);
		viewEpg = (OnliveEpg) this.findViewById(R.id.view_onlive_epg);

		// 第一层菜单 名称
		tvRecommend = (TextView) this.findViewById(R.id.tv_recommend);
		tvSelect = (TextView) this.findViewById(R.id.tv_select);
		tvEpg = (TextView) this.findViewById(R.id.tv_epg);
		tvEpg.setOnKeyListener(this);
		flRemmond = (FrameLayout) this.findViewById(R.id.fl_recommend);
		flSelect = (FrameLayout) this.findViewById(R.id.fl_select_tv);
		flEPG = (FrameLayout) this.findViewById(R.id.fl_sub_tv);

		tvRecommend.setNextFocusRightId(R.id.tv_select);
		tvSelect.setNextFocusRightId(R.id.tv_epg);
		tvSelect.setNextFocusLeftId(R.id.tv_recommend);
		tvEpg.setNextFocusLeftId(R.id.tv_select);

		tvs = new int[] { R.id.tv_recommend, R.id.tv_select, R.id.tv_epg };
		tvViews = new TextView[] { tvRecommend, tvSelect, tvEpg };

		// 电台分类名称
		tvCCTV = (TextView) this.findViewById(R.id.tv_cctv);
		tvStatelliteTV = (TextView) this.findViewById(R.id.tv_satellite);
		tvLocal = (TextView) this.findViewById(R.id.tv_local);
		tvHDChannel = (TextView) this.findViewById(R.id.tv_hd_channel);
		tvSubChannel = (TextView) this.findViewById(R.id.tv_subscription_channel);

		// 电台分类的FrameLayout
		flCCTV = (FrameLayout) this.findViewById(R.id.fl_cctv);
		flStatelliteTV = (FrameLayout) this.findViewById(R.id.fl_satellite);
		flLocal = (FrameLayout) this.findViewById(R.id.fl_local);
		flHDChannel = (FrameLayout) this.findViewById(R.id.fl_hd_channel);
		flSubChannel = (FrameLayout) this.findViewById(R.id.fl_subscription_channel);

		// 设置循环焦点
		// flSubChannel.setNextFocusRightId(R.id.fl_cctv);
		// flCCTV.setNextFocusLeftId(R.id.fl_subscription_channel);
		// 设置快速选台的向下时的焦点
		tvSelect.setNextFocusDownId(R.id.fl_cctv);

		tvEpg.setNextFocusDownId(R.id.vp_epg_select);

		flSelectTv = (FrameLayout) this.findViewById(R.id.fl_select_tv);
		// 频道tab 数组
		tabViews = new View[] { flCCTV, flStatelliteTV, flLocal, flHDChannel, flSubChannel };
		// 精彩推荐条目
		// rl_movie_list_first_line_1_item = (LinearLayout)
		// this.findViewById(R.id.rl_movie_list_first_line_1_item);
		// rl_movie_list_first_line_2_item = (LinearLayout)
		// this.findViewById(R.id.rl_movie_list_first_line_2_item);
		// rl_movie_list_first_line_3_item = (LinearLayout)
		// this.findViewById(R.id.rl_movie_list_first_line_3_item);
		// rl_movie_list_first_line_4_item = (LinearLayout)
		// this.findViewById(R.id.rl_movie_list_first_line_4_item);

		recommendId = new int[] { R.id.rl_movie_list_first_line_1_item, R.id.rl_movie_list_first_line_2_item, R.id.rl_movie_list_first_line_3_item,
				R.id.rl_movie_list_first_line_4_item };
		// 精彩推荐条目添加焦点变化事件
		// rl_movie_list_first_line_1_item.setOnKeyListener(channelTabKeyListener);
		// rl_movie_list_first_line_2_item.setOnKeyListener(channelTabKeyListener);
		// rl_movie_list_first_line_3_item.setOnKeyListener(channelTabKeyListener);
		// rl_movie_list_first_line_4_item.setOnKeyListener(channelTabKeyListener);

		tvRecommend.setOnFocusChangeListener(mOnFocusChangeListener);
		tvSelect.setOnFocusChangeListener(mOnFocusChangeListener);
		tvEpg.setOnFocusChangeListener(mOnFocusChangeListener);

		// Tab 添加(央视，卫视，本地，高清，收费)的按键事件
		flCCTV.setOnKeyListener(channelTabKeyListener);
		flStatelliteTV.setOnKeyListener(channelTabKeyListener);
		flLocal.setOnKeyListener(channelTabKeyListener);
		flHDChannel.setOnKeyListener(channelTabKeyListener);
		flSubChannel.setOnKeyListener(channelTabKeyListener);
		flSelectTv.setOnKeyListener(channelTabKeyListener);
		// 获取定时任务管理类
		if (null == alarmManager) {
			alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		}
		// 获取电台信息
		getChannelsByNames();
		Log.i(TAG, "initView->end");
	}

	/**
	 * 获取电台信息
	 * 
	 * @Title: OnlivePlayerActivity
	 * @author:郭松胜
	 * @Description: TODO
	 */
	private void getChannelsByNames() {
		HttpTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				// 通过ChannelName获取channelCode 测试数据cctv-1
				Log.i(TAG, "上个界面传下来的界面名称" + TVCodeBroadcastReciver.author);
				channelList = new HiveTVService().getChannelsByNames(new String[] { CHANNEL_NAME });
				if (channelList == null || channelList.size() == 0) {
					handler.sendEmptyMessage(RQUEST_CHANNEL_FAIL);
				} else {
					handler.sendEmptyMessage(RQUEST_CHANNEL_SUCCESS);
				}
			};
		});
	}

	/**
	 * 耗时操作完成后数据处理逻辑,参数为int,与 processData(Message msg)排斥使用
	 */
	protected void processData(int msgWhat) {
		switch (msgWhat) {
		case RQUEST_CHANNEL_SUCCESS:// 获取电台信息成功
			setChannelCode();
			break;
		case RQUEST_CHANNEL_FAIL:// 获取电台信息失败
			Log.i("OnlivePlayerActivity", "获取电台信息,返回数据为空");
			break;

		default:
			break;
		}
	};

	/**
	 * 设置ChannelCode
	 * 
	 * @Title: OnlivePlayerActivity
	 * @author:郭松胜
	 * @Description: TODO
	 */
	private void setChannelCode() {
		if (null != channelList && channelList.size() > 0) {
			// 获取集合中的第一个元素
			ChannelEntity channelEntity = channelList.get(0);
			CHANNEL_LOGO = channelEntity.getLogo();
		}
	}

	private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (v.getId() == R.id.tv_select) {
					mUpFocusId = R.id.tv_select;
					viewEpg.setVisibility(View.GONE);
					viewSelect.setVisibility(View.VISIBLE);
					flSelect.setBackgroundResource(R.drawable.fast_selection_tab_fours);
					// viewRecommned.setVisibility(View.GONE);
				} else if (v.getId() == R.id.tv_epg) {
					mUpFocusId = R.id.tv_epg;
					viewEpg.setVisibility(View.VISIBLE);
					viewSelect.setVisibility(View.GONE);
					flEPG.setBackgroundResource(R.drawable.fast_selection_tab_fours);
					// viewRecommned.setVisibility(View.GONE);
				} else if (v.getId() == R.id.tv_recommend) {
					mUpFocusId = R.id.tv_recommend;
					viewEpg.setVisibility(View.GONE);
					viewSelect.setVisibility(View.GONE);
					flRemmond.setBackgroundResource(R.drawable.fast_selection_tab_fours);
					// viewRecommned.setVisibility(View.VISIBLE);
				}
				for (int i = 0; i < tvs.length; i++) {
					if (tvs[i] == v.getId()) {
						tvViews[i].setTextColor(Color.parseColor("#FF8B00"));
					} else {
						tvViews[i].setTextColor(Color.parseColor("#FFFFFF"));
						tvCCTV.setTextColor(Color.parseColor("#FFFFFF"));
						tvStatelliteTV.setTextColor(Color.parseColor("#FFFFFF"));
						tvLocal.setTextColor(Color.parseColor("#FFFFFF"));
						tvHDChannel.setTextColor(Color.parseColor("#FFFFFF"));
						tvSubChannel.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			} else {
				flRemmond.setBackgroundResource(0);
				flSelect.setBackgroundResource(0);
				flEPG.setBackgroundResource(0);
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG, "onKeyDown-->keyCode:" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			/* start by guosongsheng 美国测试版本不用浮层 */
			if (AppConstant.ISDOMESTIC) {
				if (rlMenuTips.isShown()) {
					rlMenuTips.setVisibility(View.GONE);
					// mOverlayView.requestFocus();
				} else {
					// 节目单不为空时
					if (null != viewEpg) {
						viewEpg.notifyDataSetChanged();
					}
					rlMenuTips.setVisibility(View.VISIBLE);
					tvRecommend.requestFocus();
				}
			}
			/* end by guosongsheng */
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i(TAG, "KEYCODE_BACK");
			setIsback(true);

			if (rlMenuTips.isShown()) {
				rlMenuTips.setVisibility(View.GONE);
				return true;
				// mOverlayView.requestFocus();
			} else {
				finish();
			}
		}/*
		 * else if (keyCode == KeyEvent.ACTION_DOWN ||
		 * KeyMappingHashMapUtil.getInstance().containsKey(keyCode)) {
		 * 如果菜单非隐藏状态，遥控器按钮作为机顶盒遥控器进行处理 Log.i(TAG,
		 * "onKeyDown-->will do sbtv key event, keyCode:" + keyCode); if
		 * (!rlMenuTips.isShown()) { Log.i(TAG,
		 * "onKeyDown-->will notifyTVKeyPress.");
		 * STBSettingInfoUtil.notifySTBIrKeyPress
		 * (KeyMappingHashMapUtil.getInstance().get(keyCode));// //
		 * STBSettingInfoUtil.notifyTVKeyPress(keyCode);//过期 return true; } }
		 */
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (null == event) {
			return false;
		}
		int keyCode = event.getKeyCode();
		Log.i(TAG, "onKeyDown-->will do sbtv key event, keyCode:" + keyCode + "  ,  HiveviewHdmiInView.isSignalable  "
				+ HiveviewHdmiInView.isSignalable);
		if (KeyMappingHashMapUtil.getInstance().containsKey(keyCode) && event.getAction() == KeyEvent.ACTION_DOWN && HiveviewHdmiInView.isSignalable) {
			/* 如果菜单非隐藏状态，遥控器按钮作为机顶盒遥控器进行处理 */
			if (!rlMenuTips.isShown()) {
				Log.i(TAG, "onKeyDown-->will notifyTVKeyPress.");
				STBSettingInfoUtil.notifySTBIrKeyPress(KeyMappingHashMapUtil.getInstance().get(keyCode));
				return true;
			} else if (rlMenuTips.isShown()
					&& (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)) {
				// 如果菜单显示 只过滤声音加减键
				Log.i(TAG, "onKeyDown-->will notifyTVKeyPress." + event.getKeyCode());
				STBSettingInfoUtil.notifySTBIrKeyPress(KeyMappingHashMapUtil.getInstance().get(keyCode));
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 台标地址
	 * 
	 * @return
	 */
	public String getCHANNEL_LOGO() {
		return CHANNEL_LOGO;
	}

	/**
	 * 台标地址
	 * 
	 * @return
	 */
	public String setCHANNEL_LOGO(String channel_logo) {
		CHANNEL_LOGO = channel_logo;
		return CHANNEL_LOGO;
	}

	/**
	 * 电视台名称
	 * 
	 * @return
	 */
	public String getCHANNEL_NAME() {
		return CHANNEL_NAME;
	}

	/**
	 * 设置电视台名称
	 * 
	 * @return
	 */
	public String setCHANNEL_NAME(String channel_name) {
		CHANNEL_NAME = channel_name;
		return CHANNEL_NAME;
	}

	int currentPagerNum = 0;
	View pagerFocusPoint;

	public void setCurrentPager(int index, View findViewById) {
		currentPagerNum = index;
		pagerFocusPoint = findViewById;
	}

	/**
	 * ItemView上焦点改变事件回调接口
	 * 
	 * 
	 */
	class ItemViewFocusListener implements CallBackItemViewFocusListener {

		@Override
		public void onCallBackFocusChange(View view, boolean has) {

			TvChannelPageViewItemView itemView = (TvChannelPageViewItemView) view;
			if (has) {
				ChannelEntity entity = (ChannelEntity) itemView.getTag();
				int index = entity.getPositionInItemView();
				Log.d(TAG, index + "");
				if (entity.getPositionInItemView() < 4) {
					itemView.setNextFocusUpId(mUpFocusId);
				}

				itemView.setTextMarquee(true);
			} else {
				itemView.setTextMarquee(false);
			}

		}
	}

	/**
	 * 监听Tab(央视，卫视，本地，高清，收费)的按键事件
	 * 
	 */
	OnKeyListener channelTabKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// 当前的Tab,如果是向下按键Tab中的TextView显示橘黄色
			for (int i = 0; i < recommendId.length; i++) {
				if (recommendId[i] == v.getId()) {
					v.setNextFocusUpId(mUpFocusId);
				}
			}
			// 快速选台 下的电台分类向上按时焦点移动到 快速选台上
			for (int j = 0; j < tabViews.length; j++) {
				if (tabViews[j] == v) {
					v.setNextFocusUpId(mUpFocusId);
				}
			}
			return false;
		}

	};

	/**
	 * 关闭菜单
	 */
	public static void closeMune() {
		rlMenuTips.setVisibility(View.GONE);
	}

	public static boolean isMeunShow() {
		if (null != rlMenuTips) {
			return rlMenuTips.isShown();
		} else {
			return false;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int id = v.getId();
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (id) {
			case R.id.tv_epg:
				viewEpg.getTheOneView();
				break;
			default:
				break;
			}
		}

		return false;
	}

	public static LinearLayout currentView;
	public static ProgramEntity currententity;

	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		super.onExecute(intent);
		if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if ("prev".equals(command)) {
					try {
						Process p = Runtime.getRuntime().exec("adb shell input keyevent 21");
						HomeSwitchTabUtil.closeSiRi(this, "上一个频道", intent);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if ("next".equals(command)) {
					try {
						Process p = Runtime.getRuntime().exec("adb shell input keyevent 21");
						HomeSwitchTabUtil.closeSiRi(this, "下一个频道", intent);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if ("recom".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(OnlivePlayerActivity.this, "精彩推荐", intent);
					if (AppConstant.ISDOMESTIC) {

						rlMenuTips.setVisibility(View.VISIBLE);
						tvRecommend.requestFocus();

					}
				} else if ("quick".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(OnlivePlayerActivity.this, "快速选台", intent);
					if (AppConstant.ISDOMESTIC) {

						rlMenuTips.setVisibility(View.VISIBLE);
						tvSelect.requestFocus();

					}
				} else if ("epginfo".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(OnlivePlayerActivity.this, "节目单", intent);
					if (AppConstant.ISDOMESTIC) {

						rlMenuTips.setVisibility(View.VISIBLE);
						tvEpg.requestFocus();

					}
				} else if ("cctv".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(OnlivePlayerActivity.this, "打开央视频道", intent);
					flCCTV.requestFocus();
				} else if ("tv".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(OnlivePlayerActivity.this, "打开卫视频道", intent);
					flStatelliteTV.requestFocus();
				} else if ("local".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(OnlivePlayerActivity.this, "打开本地频道", intent);
					flLocal.requestFocus();
				} else if ("hd".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(OnlivePlayerActivity.this, "打开高清频道", intent);
					flHDChannel.requestFocus();
				} else if ("charge".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(OnlivePlayerActivity.this, "打开收费频道", intent);
					flSubChannel.requestFocus();
				} else if ("join".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(OnlivePlayerActivity.this, getResources().getString(R.string.alert_join_liveremind), intent);
					currentView = viewEpg.getEpgLayout();
					currententity = viewEpg.getEntity();
					if (currententity != null && currentView != null) {
						LogUtil.info("currententity==>" + currententity.getName());
						OnliveTipsEntity onliveTipsEntity = new OnliveTipsEntity();
						onliveTipsEntity.setDate(currententity.getDate());
						onliveTipsEntity.setEnd_time(currententity.getEnd_time());
						onliveTipsEntity.setHasvideo(currententity.getHasvideo());
						onliveTipsEntity.setName(currententity.getName());
						onliveTipsEntity.setSource(currententity.getSource());
						onliveTipsEntity.setStart_time(currententity.getStart_time());
						onliveTipsEntity.setTags(currententity.getTags());
						onliveTipsEntity.setWiki_cover(currententity.getWiki_cover());
						onliveTipsEntity.setWiki_id(currententity.getWiki_id());
						onliveTipsEntity.setTelevisionLogoUrl(getCHANNEL_LOGO().toString());
						onliveTipsEntity.setTelevisionName(getCHANNEL_NAME().toString());
						StringBuffer time = new StringBuffer();
						time.append(currententity.getDate());
						time.append(" ");
						time.append(currententity.getStart_time());
						try {
							onliveTipsEntity.setTip_time(String.valueOf(DateUtils.stringToLong(time.toString(), "yyyy-MM-dd HH:mm")));

							Log.d(TAG, "==直播提醒的台标==" + this.getCHANNEL_LOGO().toString());
							Log.d(TAG, "==直播提醒的台ming==" + this.getCHANNEL_NAME().toString());
							if (null == dao)
								dao = new OnliveTipsDAO(this);
							// 需要填充到直播提醒列表的 数据
							if (!AppUtil.nowDateTimeIsAferBoolean(currententity.getDate() + " " + currententity.getStart_time())) {
								if (dao.isExist(new String[] { getCHANNEL_NAME().toString(), currententity.getDate(), currententity.getStart_time() })) {
									dao.delete("television_logo_name = ? and date = ? and start_time = ?", new String[] {
											getCHANNEL_NAME().toString(), currententity.getDate(), currententity.getStart_time() });
									StringBuffer buffer = new StringBuffer();
									buffer.append(currententity.getDate());
									buffer.append(" ");
									buffer.append(currententity.getStart_time());

									long startTime;
									startTime = DateUtils.stringToLong(buffer.toString(), "yyyy-MM-dd HH:mm");
									cancelAlar(startTime);
									ToastUtils.alert(this, getResources().getString(R.string.alert_cancle_liveremind));
									setCurrentPager(index, currentView.findViewById(R.id.iv_focus_point));
									((ImageView) currentView.findViewById(R.id.iv_onlive_logo1)).setVisibility(View.GONE);
									((View) currentView.findViewById(R.id.iv_logo)).setVisibility(View.GONE);
									return;
								}

								setCurrentPager(index, currentView.findViewById(R.id.iv_focus_point));
								((ImageView) currentView.findViewById(R.id.iv_onlive_logo1)).setVisibility(View.VISIBLE);
								((View) currentView.findViewById(R.id.iv_logo)).setVisibility(View.VISIBLE);

								dao.insert(onliveTipsEntity);
								ToastUtils.alert(this, getResources().getString(R.string.alert_join_liveremind));

								Log.d(TAG, "定时发消息");
								// 制作定时任务
								Intent intent1 = new Intent();
								Bundle bundle = new Bundle();
								bundle.putString("name", currententity.getName());
								bundle.putString("wiki_cover", String.valueOf(currententity.getWiki_cover()));
								bundle.putString("date", currententity.getDate());
								bundle.putString("start_time", currententity.getStart_time());
								bundle.putString("end_time", currententity.getEnd_time());
								bundle.putString("wiki_id", currententity.getWiki_id());
								bundle.putString("logo", getCHANNEL_LOGO().toString());
								bundle.putString("logoName", getCHANNEL_NAME().toString());
								intent1.putExtras(bundle);
								intent1.setClass(this, OnliveTipService.class);
								StringBuffer buffer = new StringBuffer();
								buffer.append(currententity.getDate());
								buffer.append(" ");
								buffer.append(currententity.getStart_time());

								long startTime;
								startTime = DateUtils.stringToLong(buffer.toString(), "yyyy-MM-dd HH:mm");
								pendingIntent = PendingIntent.getService(this, (int) startTime, intent1, 0);
								alarmManager.set(AlarmManager.RTC, startTime - seconds, pendingIntent);
								Log.d(TAG, "long time ==" + System.currentTimeMillis());
							} else {
								ToastUtils.alert(this, getResources().getString(R.string.alert_no_backsee));
							}
						} catch (Exception e) {
							LogUtil.info(this + "==" + e.getMessage());

							e.printStackTrace();
						}
					}
				}
			}

		}
	}

	/**
	 * 取消当前的定时任务 参数是 日期 时间
	 * 
	 * @param date
	 * @param start_Time
	 */
	/**
	 * 定时任务的管理类
	 */
	private AlarmManager alarmManager;
	/**
	 * 定时任务要触发的广播
	 */
	private PendingIntent pendingIntent;

	/**
	 * 数据库类
	 */
	private OnliveTipsDAO dao;
	/**
	 * 直播提醒的提前量
	 */
	private int seconds = 60 * 1000;

	public void cancelAlar(long startTime) {
		// 获取定时任务管理类
		if (null == alarmManager)
			alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

		// 制作定时任务
		Intent intent = new Intent();
		intent.setClass(this, OnliveTipService.class);

		pendingIntent = PendingIntent.getService(this, (int) startTime, intent, 0);
		alarmManager.cancel(pendingIntent);

	}

	HashMap<String, String[]> commands = new HashMap<String, String[]>();
	// 场景id
	private String scenceId = "com.hiveview.tv.activity.OnlivePlayerActivity";

	public String onQuery() {
		// TODO 自动生成的方法存根
		commands.put("recom", new String[] { "精彩推荐", "推荐" });
		commands.put("quick", new String[] { "快速选台", "选台" });
		commands.put("epginfo", new String[] { "节目单" });
		commands.put("cctv", new String[] { "央视", "中央台", "央视频道", "中央频道" });
		commands.put("tv", new String[] { "卫视", "卫视频道" });
		commands.put("local", new String[] { "本地", "地方台" });
		commands.put("hd", new String[] { "高清", "高清频道", "高清台" });
		commands.put("charge", new String[] { "收费", "收费频道", "收费台" });
		commands.put("join", new String[] { "加入提醒", "增加直播提醒", "直播提醒", "添加到直播提醒" });
		commands.put("prev", new String[] { "上一台", "上一频道", "上一个台", "上一个频道", "上个台", "上个频道" });
		commands.put("next", new String[] { "下一台", "下一频道", "下一个台", "下一个频道", "下个台", "下个频道" });
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
			jsonObject = JsonUtil.makeScenceJson(scenceId, commands, fuzzayWords1, fuzzayWords2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "onQunery====>2");
		return jsonObject.toString();

	}

}
