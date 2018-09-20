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
import com.hiveview.tv.utils.DialogUtils;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.KeyMappingHashMapUtil;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.QiYiPlayerUtil;
import com.hiveview.tv.utils.STBSettingInfoUtil;
import com.hiveview.tv.utils.SwitchChannelUtils;
import com.hiveview.tv.utils.ToastUtils;
import com.hiveview.tv.utils.TvManUtil;
import com.hiveview.tv.view.HiveviewHdmiInView;
import com.hiveview.tv.view.TvChannelPageViewItemView;
import com.hiveview.tv.view.onlive.OnliveEpg;
import com.hiveview.tv.view.onlive.VoiceOnliveEpg;
import com.hiveview.tv.view.television.voicecontrol.TVCodeBroadcastReciver;
import com.paster.util.JsonUtil;
/**
 * 
 * 
 * @ClassName: VoiceOnliveEPGActivity
 * @Description: 语音XX频道的节目单Activity
 * @author: lihongji
 * @date 2014年12月4日 下午5:34:57
 *
 */
public class VoiceOnliveEPGActivity extends BaseActivity  {
	private static final String TAG = "VoiceOnliveEPGActivity";

	private static String CHANNEL_CODE = null;

	private static String CHANNEL_LOGO = null;

	private static String CHANNEL_NAME = null;
	
	private static String STRATTIME = null;
	/**
	 * 获取电台信息成功
	 */
	protected static final int RQUEST_CHANNEL_FAIL = 200;
	/**
	 * 获取电台信息失败
	 */
	protected static final int RQUEST_CHANNEL_SUCCESS = -200;

	private VoiceOnliveEpg viewEpg;



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
	private DisplayImageOptions optionsPoster;
	/**
	 * 从ViewPager中的ItemView上往上按键焦点落到Id为mUpFocusId的View上
	 */
	private int mUpFocusId = 0;

	/**
	 * 节目单VIew
	 */
	private int index = 0;
	
	private static List<ChannelEntity> channelList;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_epg_menu_tip);
		initView();
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
		super.onDestroy();
		Log.i(TAG, "onDestroy");	
	}

	private void initView() {

		viewEpg = (VoiceOnliveEpg) this.findViewById(R.id.view_epg);
		viewEpg.setVisibility(View.VISIBLE);
		viewEpg.requestFocus();
		
//		// 获取定时任务管理类
//		if (null == alarmManager) {
//			alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//		}
//		// 获取电台信息
//		getChannelsByNames();
//		Log.i(TAG, "initView->end");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		Log.i(TAG, "onDestroy");
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
				Log.i(TAG, "getChannelsByNames" );
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
			CHANNEL_CODE= channelEntity.getCode();
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i(TAG, "onKeyDown-->keyCode:" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			/* start by guosongsheng 美国测试版本不用浮层 */
			if (AppConstant.ISDOMESTIC) {
					// 节目单不为空时
					if (null != viewEpg) {
						viewEpg.notifyDataSetChanged();
				}
			}
			/* end by guosongsheng */
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i(TAG, "KEYCODE_BACK");
				finish();
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




	public static LinearLayout currentView;
	public static ProgramEntity currententity;

	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		super.onExecute(intent);
		if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if ("join".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(VoiceOnliveEPGActivity.this, getResources().getString(R.string.alert_join_liveremind), intent);
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
	private String scenceId = "com.hiveview.tv.activity.VoiceOnliveEPGActivity";

	public String onQuery() {
		// TODO 自动生成的方法存根
		commands.put("join", new String[] { "加入提醒", "增加直播提醒", "直播提醒", "添加到直播提醒" });
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
