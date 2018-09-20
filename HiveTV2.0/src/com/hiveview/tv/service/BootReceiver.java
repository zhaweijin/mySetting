package com.hiveview.tv.service;

import java.util.Date;
import java.util.Random;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

import com.hiveview.tv.R;
import com.hiveview.tv.activity.HomeActivity;
import com.hiveview.tv.common.statistics.KeyEventHandler;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.utils.DateUtils;
import com.hiveview.tv.view.pager3d.HomeActions;

/**
 * @ClassName: BootReceiver
 * @Description:
 * @author: zhangpengzhan
 * @date 2014年5月28日 下午3:25:10
 * 
 */
public class BootReceiver extends BroadcastReceiver {
	// log tag
	private static final String TAG = "BootReceiver";
	/**
	 * 开机的action
	 * 
	 * @Fields bootAction
	 */
	private String bootAction = "android.intent.action.BOOT_COMPLETED";
	/**
	 * @Fields statisticsAction:开机统计
	 */
	private String statisticsAction = "com.hiveview.tv.statistics";

	/**
	 * @Fields timeAction:分析时间的action
	 */
	private String timeAction = "com.hiveview.tv.time";
	/**
	 * @Fields timeAction:分析时间的action
	 */
	private String timeStartAction = "com.hiveview.tv.timeStart";

	/**
	 * @Fields timePendingIntentRequestCoding:分析时间定时任务 pendingIntent 标志
	 */
	private int timePendingIntentRequestCoding = 0x001474566;
	/**
	 * @Fields activiteAction:活跃用户统计
	 */
	private String activiteAction = "com.hiveview.tv.activeStatistics";
	/**
	 * 定时任务的管理类
	 */
	private AlarmManager alarmManager;
	/**
	 * 定时任务要触发的广播
	 */
	private PendingIntent pendingIntent;

	private PendingIntent timePendingIntent;

	/**
	 * @Fields oneDay:一天时长
	 */
	private int oneDay = 24 * 60 * 60 * 1000;

	/**
	 * @Fields twoHours:两个小时的时长
	 */
	private static int twoHours = 2 * 60 * 60 * 1000;
	/**
	 * @Fields twoHours: 一个小时的时长
	 */
	private int oneHours = 1 * 60 * 60 * 1000;

	/**
	 * @Fields startTime:定时任务开始的时间
	 */
	private long startTime = 0;
	private static int count = 0;
	/**
	 * @Fields context:系统拿到程序上下文
	 */
	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		// context.getSharedPreferences("hiveview1",
		// 0).edit().putBoolean("boot", true).commit();
		mContext = context;
		// 如果是开机广播
		if (bootAction.equals(intent.getAction())) {
			Log.d(TAG, "BootReceiver-----bootAction");
			if (null == alarmManager) {
				alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			}
			Intent broadcastIntent = new Intent(statisticsAction);
			pendingIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), broadcastIntent, 0);
			alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + oneDay, oneDay, pendingIntent);
			// 每次开机都去打开直播提醒的服务，检测时候还有没有执行的提醒任务，添加到系统的定时任务中
			Intent intent1 = new Intent();
			Bundle bundle = new Bundle();
			// 添加一个标志，证明是开机提醒的标志
			bundle.putBoolean("boot", true);
			intent1.putExtras(bundle);
			// 直播提醒的服务
			intent1.setClass(context, OnliveTipService.class);
			// 开启服务
			context.startService(intent1);

			// 创建一个分析时间的定时任务
			startTime = System.currentTimeMillis() + oneHours;
			createAlar();

		} else if (activiteAction.equals(intent.getAction())) {
			SharedPreferences sharedPreferences = context.getSharedPreferences("boot_time", Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			// 上次开机后使用时间(活跃用户统计)
			int last_waittime = sharedPreferences.getInt("last_wait_time", 0) + 5;
			// 是否发送
			boolean issend = sharedPreferences.getBoolean("issend", false);
			// 是否发送过并且有没有超过30分钟
			if (last_waittime >= 30 & !issend) {
				// 埋点统计
				Log.v(TAG, "活跃用户统计");
				editor.putBoolean("issend", true);
				LoadService.cancleAlarm();
				KeyEventHandler.post(new DataHolder.Builder(context).setTabNo(Tab.TAB).setViewPosition("0308").build());
			}
			Log.v(TAG, "活跃用户统计" + last_waittime);
			editor.putInt("last_wait_time", last_waittime);// 把新的用户开机后使用的时间存入
			editor.commit();
		} else if (timeStartAction.equals(intent.getAction())) { // app
																	// 初始化发送时间分析广播

			// 现在时间在九点到第二天七点之前,可以延长刷新时间了
			int minute = DateUtils.getCurrentDateToMinute();
			// 如果时间区间在不是一个正点位置
			int differenceMinute = 60 - minute;
			// 必须要先取消掉当前的定时任务
			cancelAlar();
			// 重新定义一个距离下一个整点比较近的时间点重新开始执行
			startTime = System.currentTimeMillis() + differenceMinute * 60 * 1000;
			createAlar();
			// context.getSharedPreferences("hiveview1",
			// 0).edit().putString(DateUtils.getAfterMinuteDate(0) + "",
			// "如果时间不是处于一个正点的位置就开始自动校正定时任务的时间")
			// .commit();

		} else if (statisticsAction.equals(intent.getAction())) {// 开机统计
			// 开机统计
//			Log.i(TAG, "BootReceiver::onHandleIntent-->before boot statistics");
//
//			SharedPreferences sharedPreferences = context.getSharedPreferences("boot_time", Context.MODE_PRIVATE);
//			Editor editor = sharedPreferences.edit();
//			long last_time = sharedPreferences.getLong("last_boot_time", new Date().getTime());
//			Date last_date = new Date(last_time);
//
//			java.util.Date dt = new Date();
//			Log.d(TAG, "系统时间:" + System.currentTimeMillis());
//			// 日期转换
//			// SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.CHINA);
//			// SimpleDateFormat sdf2 = new SimpleDateFormat("dd", Locale.CHINA);
//			// String months_string = sdf.format(dt);
//			// String days_string = sdf2.format(dt);
//			// int months = Integer.parseInt(months_string);
//			// int days = (Integer.parseInt(days_string)) + 0;
//			//
//			// String last_months_string = sdf.format(last_date);
//			// String last_days_string = sdf2.format(last_date);
//			// int last_months = Integer.parseInt(last_months_string);
//			// int last_days = (Integer.parseInt(last_days_string)) + 0;
//			long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
//			String intervalDay;
//			long diff = dt.getTime() - last_date.getTime();
//			intervalDay = String.valueOf(diff / nd);// 计算差多少天
//
//			// if(last_months==months){
//			// intervalDay=String.valueOf(days-last_days);
//			// }else{
//			// intervalDay=String.valueOf((months-last_months)*30+days-last_days);
//			// }
//			Log.v(TAG, "intervalDay=" + intervalDay);
//			editor.putLong("last_boot_time", dt.getTime());
//
//			editor.commit();// 别忘了提交哦
//
//			KeyEventHandler.post(new DataHolder.Builder(context).setTabNo(Tab.TAB).setViewPosition("0000").setIntervalDay(intervalDay).build());
		} else if (timeAction.equals(intent.getAction())) {
			int years = DateUtils.getCurrentDateToYears();
			if (years == 1970) {
				// 开机时间没有校正
				// context.getSharedPreferences("hiveview1",
				// 0).edit().putString(DateUtils.getAfterMinuteDate(0) + "",
				// "开机时间没有校正").commit();
			} else {
				// context.getSharedPreferences("hiveview1",
				// 0).edit().putString(DateUtils.getAfterMinuteDate(0) + "",
				// "系统时间已经校正").commit();
				// 系统时间已经校正
				int hours = DateUtils.getCurrentDateToHour();
				// 现在时间在九点到第二天七点之前,可以延长刷新时间了
				int minute = DateUtils.getCurrentDateToMinute();
				// 如果时间区间在不是一个正点位置
				int differenceMinute = 60 - minute;
				// 如果时间不是处于一个正点的位置就开始自动校正定时任务的时间
				if (DateUtils.rangeInDefined(differenceMinute, 1, 55)) {
					// 必须要先取消掉当前的定时任务
					cancelAlar();
					// 重新定义一个距离下一个整点比较近的时间点重新开始执行
					startTime = System.currentTimeMillis() + differenceMinute * 60 * 1000;
					createAlar();
					// context.getSharedPreferences("hiveview1", 0).edit()
					// .putString(DateUtils.getAfterMinuteDate(0) + "",
					// "如果时间不是处于一个正点的位置就开始自动校正定时任务的时间").commit();
				}

				if (!DateUtils.rangeInDefined(hours, 8, 24)) {// 发送静默升级广播
					Log.d(TAG, "DateUtils---hours:" + hours + "--count::" + count);
					if (count == 0) {
						NewUpgraderHandler.isNewUpgrader = true;
						Intent newUpgraderIntent = new Intent(HomeActions.NEWUPGRADER_ACTION);
						mContext.sendBroadcast(newUpgraderIntent);
					}
					count++;
				} else {// 发送取消静默升级广播
					Log.d(TAG, "---hours:" + hours + "--count::" + count);
					NewUpgraderHandler.isNewUpgrader = false;
					if (count > 0) {
						Intent newUpgraderIntent = new Intent(HomeActions.NEWUPGRADER_CANCEL_ACTION);
						mContext.sendBroadcast(newUpgraderIntent);
						count = 0;
					}

				}
				if (!DateUtils.rangeInDefined(hours, 7, 21)) {
					// 进入夜场时间，减缓launcher刷新的时间
					//减少盒子在同一个时间段内同时访问
					Random random = new Random();
					int time = (random.nextInt(120) % 120 + 120) * 60 * 1000;

					RefleshHandler.delayMillis = time;

					// context.getSharedPreferences("hiveview1", 0).edit()
					// .putString(DateUtils.getAfterMinuteDate(0) + "",
					// String.valueOf(hours) +
					// " 进入夜场时间，减缓launcher刷新的时间").commit();
				} else {
					// 大白天，跑起来吧
					RefleshHandler.delayMillis = HomeActivity.refleshDelayMillis * 60 * 1000;

					// context.getSharedPreferences("hiveview1",
					// 0).edit().putString(DateUtils.getAfterMinuteDate(0)+"",
					// "大白天，跑起来吧").commit();
				}
			}
			// 定时任务，检查系统时间，更改刷新时间
			Log.i(TAG, "定时任务，检查系统时间，更改刷新时间");
			// context.getSharedPreferences("hiveview1",
			// 0).edit().putString(DateUtils.getAfterMinuteDate(0) + "",
			// "::定时任务，检查系统时间，更改刷新时间").commit();
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
			alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		// 制作定时任务
		Intent intent = new Intent(timeAction);
		pendingIntent = PendingIntent.getService(mContext, timePendingIntentRequestCoding, intent, 0);
		alarmManager.cancel(pendingIntent);
		// mContext.getSharedPreferences("hiveview1",
		// 0).edit().putString(DateUtils.getAfterMinuteDate(0) + "",
		// "cancelAlar()").commit();
	}

	/**
	 * @Title: BootReceiver
	 * @author:张鹏展
	 * @Description: 创建一个定时任务
	 */
	public void createAlar() {
		// 分析时间修改launcher2.0轮询时间
		if (null == alarmManager)
			alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(timeAction);
		timePendingIntent = PendingIntent.getBroadcast(mContext, timePendingIntentRequestCoding, intent, 0);
		// 刚开机的时候，时间肯定是不对的。所以我们需要先设定一个十分钟的闹铃事件，循环时间同样是十分钟
		alarmManager.setRepeating(AlarmManager.RTC, startTime, oneHours, timePendingIntent);
		// mContext.getSharedPreferences("hiveview1",
		// 0).edit().putString(DateUtils.getAfterMinuteDate(0) + "",
		// "createAlar()").commit();
	}
}
