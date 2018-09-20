package com.hiveview.tv.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.LiveEpgInfoActivity;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.dao.OnliveTipsDAO;
import com.hiveview.tv.service.entity.OnliveTipsEntity;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.CloseBlueLightUtil;
import com.hiveview.tv.utils.DateUtils;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.SwitchChannelUtils;
import com.hiveview.tv.view.OnliveTipsView;
import com.hiveview.tv.view.OnliveTipsView.OnConfirmAndCancal;
import com.iflytek.xiri.scene.ISceneListener;
import com.paster.util.JsonUtil;

/**
 * 提醒显示 直播提醒 使用的是window messager
 */
public class OnliveTipService extends Service implements ISceneListener {
	/**
	 * 没有操作 消除提醒界面
	 */
	public static final int ONLIVE_TIPS_MESSAGE = 0x00457;
	/**
	 * 显示提醒界面
	 */
	private static final int INTERVAL = 0x00546;
	private static final int INTERVAL2 = 0x00556;
	private static final int INTERVAL3 = 0x00566;
	/**
	 * 1970年
	 */
	private static final String YEAR_1970 = "1970";
	/**
	 * 图片下载器
	 */
	protected ImageLoader imageLoader = null;
	private DisplayImageOptions optionsPoster;
	/**
	 * 定时任务的管理类
	 */
	private AlarmManager alarmManager;
	/**
	 * 定时任务要触发的广播
	 */
	private PendingIntent pendingIntent;

	/**
	 * 直播提醒的提示信息 1 2 3
	 */
	private String TVinfo = " 即将在三十秒后播放" + "";

	private String TVinfo2 = " 已经开始";

	private String TVinfo3 = " 已经结束";

	/**
	 * 提醒要直播的电视剧的名字
	 */
	private String name;

	/**
	 * 台标名称
	 */
	private String logoName;
	/**
	 * 电视的海报图
	 */
	private String wiki_cover;
	/**
	 * 提醒的日期
	 */
	private String date;
	/**
	 * 日期 月 日 提示框需要显示的文字
	 */
	private String month;
	/**
	 * 日 提示框需要显示的文字信息
	 */
	private String day;
	/**
	 * 开始时间
	 */
	private String start_time;
	/**
	 * 结束时间
	 */
	private String end_time;
	/**
	 * 直播的电视的id
	 */
	private String wiki_id;
	/**
	 * intent 包含的数据列表
	 */
	private Bundle bundle;
	/**
	 * 传递过来的开始时间 long 自己计算的
	 */
	private long startTime;
	/**
	 * 是否是开机广播发出的消息
	 * 
	 * @Fields isBoot
	 */
	private boolean isBoot = false;
	/**
	 * 直播提醒的提前量
	 */
	private int seconds = 60 * 1000;
	// 提醒界面的布局
	OnliveTipsView view;

	@SuppressLint("HandlerLeak")
	private Handler mHander = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case INTERVAL:
				// 重置提示信息
				view.setTextInfo(name + " " + TVinfo);
				// 下载海报图
				imageLoader.displayImage(wiki_cover, view.getImageView(), optionsPoster);

				break;
			case INTERVAL2:
				// isHaveRemind();
				// 重置提示信息
				view.setTextInfo(name + " " + TVinfo2);
				// 下载海报图
				imageLoader.displayImage(wiki_cover, view.getImageView(), optionsPoster);
				break;
			case INTERVAL3:
				// isHaveRemind();
				// 重置提示信息
				view.getBtnGotoOnlive().setText(" 重新选择");
				view.setTextInfo(name + " " + TVinfo3);
				// 下载海报图
				imageLoader.displayImage(wiki_cover, view.getImageView(), optionsPoster);
				break;
			case ONLIVE_TIPS_MESSAGE:
				if (null != view) {
					// 移除界面
					// 从数据库中删除 所选项目
					new OnliveTipsDAO(getBaseContext()).delete("television_logo_name = ? and date = ? and start_time = ?", new String[] { logoName,
							date, start_time });
					// view.setAnimation(AnimationUtils.loadAnimation(OnliveTipService.this,
					// R.anim.down_out));
					mWm.removeView(view);
					view = null;
					cancelAlar();
				}
				break;
			default:
				break;
			}
		};
	};

	private String TAG = "OnliveTipService";

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		scene.release();
	}

	// 启动这个server
	public void onStart(Intent intent, int startId) {
		// getBaseContext().getSharedPreferences("server-" +
		// System.currentTimeMillis(), 0).edit().putBoolean("boot",
		// true).commit();
		if (null != intent)
			bundle = intent.getExtras();
		// 如果是接受的是开机广播，就是检测是否还有未完成的直播提醒的任务
		if (null != bundle) {
			isBoot = bundle.getBoolean("boot", false);
			// ToastUtils.alertShow(getApplicationContext(),
			// "server::::::::::::::::::::::::::::" + isBoot);
			if (isBoot) {
				Log.i(TAG, "..........................:" + getCurrentYear());
				if (!YEAR_1970.equals(getCurrentYear())) {
					isHaveRemind();
				}
				isBoot = false;
			} else {
				// 接受各种数据
				name = bundle.getString("name");
				wiki_cover = bundle.getString("wiki_cover");
				date = bundle.getString("date");
				month = date.split("-")[1] + " 月";
				day = date.split("-")[2] + " 日";
				start_time = bundle.getString("start_time");
				end_time = bundle.getString("end_time");
				wiki_id = bundle.getString("wiki_id");
				imageLoader = ImageLoader.getInstance();
				logoName = bundle.getString("logoName");

				Log.i(TAG, "-------------------------------->startTime:" + start_time + "------->date:" + date);
				// 从数据库中删除 所选项目
				new OnliveTipsDAO(getBaseContext()).delete("television_logo_name = ? and date = ? and start_time = ?", new String[] { logoName, date,
						start_time });
				StringBuffer buffer = new StringBuffer();
				buffer.append(date);
				buffer.append(" ");
				buffer.append(start_time);
				try {
					startTime = DateUtils.stringToLong(buffer.toString(), "yyyy-MM-dd HH:mm");
				} catch (ParseException e) {
					e.printStackTrace();
				}
				optionsPoster = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.variety_default_img)
						.showImageOnFail(R.drawable.variety_default_img).resetViewBeforeLoading(false).cacheOnDisc(true)
						.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();

				Log.d(TAG, "date==" + date + "===start_time=" + start_time);
				String currentDate = "";
				try {
					currentDate = DateUtils.dateToString(new Date(), "yyyy-MM-dd");
					Log.i(TAG, "-------------------------------->currentDate:" + currentDate);
					if (date.equals(currentDate)) {
						// 检测 控件的初始化情况
						Log.i(TAG, "-------------------------------->currentDate==date");
						init();
					} else {
						return;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*
				 * //发送信息 isHaveRemind();
				 */
				if (!AppUtil.nowDateTimeIsAferBoolean(date + " " + start_time)) {
					mHander.sendEmptyMessage(INTERVAL);
				}

				if (AppUtil.nowDateTimeIsAferBoolean(date + " " + start_time) && !AppUtil.nowDateTimeIsAferBoolean(date + " " + end_time)) {

					mHander.sendEmptyMessage(INTERVAL2);
				}
				// if (AppUtil.nowDateTimeIsAferBoolean(date + " " + end_time))
				// {
				//
				// mHander.sendEmptyMessage(INTERVAL3);
				// }

				// mHander.removeMessages(ONLIVE_TIPS_MESSAGE);
				// mHander.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE, 30 *
				// 1000);
			}
		} else {

		}
	}

	/**
	 * 目前没有用到
	 */
	public void isHaveRemind() {
		new Thread(new Runnable() {
			public void run() {
				// getBaseContext().getSharedPreferences("server-runnable" +
				// System.currentTimeMillis(), 0).edit().putBoolean("boot",
				// true).commit();
				ArrayList<OnliveTipsEntity> dataSources = (ArrayList<OnliveTipsEntity>) new OnliveTipsDAO(OnliveTipService.this).query(null, null,
						null, null);

				for (OnliveTipsEntity entity : dataSources) {
					if (!AppUtil.nowDateTimeIsAferBoolean(entity.getDate() + " " + entity.getStart_time())) {
						try {
							Intent intent = new Intent();
							Bundle bundle = new Bundle();
							bundle.putString("name", entity.getName());
							bundle.putString("wiki_cover", String.valueOf(entity.getWiki_cover()));
							bundle.putString("date", entity.getDate());
							bundle.putString("start_time", entity.getStart_time());
							bundle.putString("end_time", entity.getEnd_time());
							bundle.putString("wiki_id", entity.getWiki_id());
							bundle.putString("logo", entity.getTelevisionLogoUrl());
							bundle.putString("logoName", entity.getTelevisionName());
							intent.putExtras(bundle);
							intent.setClass(getBaseContext(), OnliveTipService.class);
							StringBuffer buffer = new StringBuffer();
							buffer.append(entity.getDate());
							buffer.append(" ");
							buffer.append(entity.getStart_time());

							long startTime;
							if (null == alarmManager) {
								alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
							}
							startTime = DateUtils.stringToLong(buffer.toString(), "yyyy-MM-dd HH:mm");
							Log.d(TAG, "add pengdingIntent->time::" + startTime);
							pendingIntent = PendingIntent.getService(getBaseContext(), (int) startTime, intent, 0);
							alarmManager.set(AlarmManager.RTC, startTime - seconds, pendingIntent);
							SimpleDateFormat sdf3 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.CHINA);
							String t = String.valueOf(sdf3.format(System.currentTimeMillis()));
							/*
							 * getApplicationContext().getSharedPreferences("boot"
							 * , MODE_PRIVATE).edit() .putString(t,
							 * String.valueOf
							 * (System.currentTimeMillis())).commit();
							 */

						} catch (ParseException e) {
							e.printStackTrace();
							continue;
						}
					} else {
						new OnliveTipsDAO(OnliveTipService.this).delete("television_logo_name = ? and date = ? and start_time = ?", new String[] {
								entity.getTelevisionName(), entity.getDate(), entity.getStart_time() });
						Log.d(TAG, "delete pengdingIntent->name::" + entity.getTelevisionName());
					}

					// 当前时间不在 播放时间后边
				}
			}
		}).start();
		;

	}

	/**
	 * 系统级的 提示警告框
	 */
	private WindowManager mWm;

	/**
	 * 提示框的布局 位置 等数据
	 */
	private WindowManager.LayoutParams mParams;
	private AppScene scene;
	// 初始化windowmanager
	private void init() {
		scene = AppScene.getInstance();
		scene.createScene(this, this);
		scene.initScene(scenceId);
		if (null == mWm) {
			mWm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
			mParams = new WindowManager.LayoutParams();
			mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
			mParams.format = 1;
			mParams.flags = mParams.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
			mParams.flags = mParams.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
			mParams.windowAnimations = android.R.style.Animation_Dialog;
			mParams.alpha = 1.0f;
			mParams.width = mWm.getDefaultDisplay().getWidth() - 2;
			mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
			mParams.gravity = Gravity.TOP;

		}
		if (null == view) {
			// 显示的布局
			view = new OnliveTipsView(OnliveTipService.this, mHander);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(layoutParams);
			// view.setAnimation(AnimationUtils.loadAnimation(OnliveTipService.this,
			// R.anim.up_in));
			// 按钮监听
			view.setListener(new OnConfirmAndCancal() {

				// 按下确定
				public void OnConfirm() {
					//start author:zhangpengzhan
					CloseBlueLightUtil.closeBlueLight();
					//end
					play();
				}

				// 按下取消
				public void OnCancal() {
					cancle();
				}
			});

		} else {
			// 不为空 移除之前的布局
			mWm.removeView(view);
		}

		mWm.addView(view, mParams);
	}
	
	public void play(){
		if (null != view) {
			// 点击播放
			SwitchChannelUtils.switchChannel(OnliveTipService.this, logoName, false, AppScene.getScene());
			OnliveTipService.this.sendBroadcast(new Intent("bluelight_stop_action"));
			// 移除界面
			// view.setAnimation(AnimationUtils.loadAnimation(OnliveTipService.this,
			// R.anim.down_out));
			mWm.removeView(view);
			// 从数据库中删除 所选项目
			new OnliveTipsDAO(getBaseContext()).delete("television_logo_name = ? and date = ? and start_time = ?", new String[] {
					logoName, date, start_time });
			view = null;
			cancelAlar();
		}
	}
	
	public void cancle(){
		if (null != view) {
			// 移除界面
			// view.setAnimation(AnimationUtils.loadAnimation(OnliveTipService.this,
			// R.anim.down_out));
			mWm.removeView(view);
			// 从数据库中删除 所选项目
			new OnliveTipsDAO(getBaseContext()).delete("television_logo_name = ? and date = ? and start_time = ?", new String[] {
					logoName, date, start_time });
			view = null;
			cancelAlar();
		}
	}

	/**
	 * 取消当前的定时任务 参数是 日期 时间
	 * 
	 * @param date
	 * @param start_Time
	 */
	public void cancelAlar() {
		// 获取定时任务管理类
		if (null == alarmManager)
			alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

		// 制作定时任务
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), OnliveTipService.class);
		Log.d(TAG, "long time ==" + startTime);
		pendingIntent = PendingIntent.getService(getApplicationContext(), (int) startTime, intent, 0);
		alarmManager.cancel(pendingIntent);

	}

	//
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 获取系统时间的年份
	 * 
	 * @Title: OnliveTipService
	 * @author:guosongsheng
	 * @Description: TODO
	 * @return
	 */
	public String getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR) + "";
	}

	@Override
	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if ("go".equals(command)) {
					play();
					HomeSwitchTabUtil.closeSiRi(OnliveTipService.this, "现在去看", intent);
				} else if ("cancle".equals(command)) {
					cancle();
					HomeSwitchTabUtil.closeSiRi(OnliveTipService.this, "取消提醒", intent);
				}
			}
		}
	}

	HashMap<String, String[]> commands = new HashMap<String, String[]>();
	// 场景id
	private String scenceId = "com.hiveview.tv.service.OnliveTipService";

	@Override
	public String onQuery() {
		// TODO 自动生成的方法存根
		commands.put("go", new String[] { "现在去看" });
		commands.put("cancle", new String[] { "不再提醒", "取消提醒" });

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
