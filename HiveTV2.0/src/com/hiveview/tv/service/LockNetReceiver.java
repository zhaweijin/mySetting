package com.hiveview.tv.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.hiveview.tv.activity.HomeActivity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewApplication;

/**
 * 
 * @ClassName: LockNetReceiver
 * @Description: TODO
 * @author: guosongsheng
 * @date 2014年9月20日 下午6:19:17
 * 
 */
public class LockNetReceiver extends BroadcastReceiver {
	/**
	 * ConnectivityManager
	 */
	private ConnectivityManager connectivityManager = null;
	/**
	 * NetworkInfo
	 */
	private NetworkInfo info = null;
	/**
	 * rom网络状态改变时发送广播
	 */
	public static final String NET_WORK_STATUS_CHANGCE = "com.hiveview.pingswanserver.notifiy";
	/**
	 * 网络状态
	 */
	private static boolean isNetWrokChangce = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		// Log.i("LockNet", "网络状态已经改变");
		String action = intent.getAction();
		if (action.equals("android.intent.action.BOOT_COMPLETED")) {// 系统网络变换广播
			Log.i("LockNet", "receive boot task......");
			try {
				connectivityManager = (ConnectivityManager) HiveviewApplication.mContext
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					if (AppConstant.ISDOMESTIC) {
						Log.i("LockNet",
								"onReceive CONNECTIVITY_ACTION.........info.isAvailable:"
										+ info.isAvailable());
						
						if(HomeActivity.isNeedChlitina){
							Log.i("LockNet", "国内克丽缇娜版本 不需要锁网");
						}// 国内开启锁网服务
						else{
						Intent lockNetServiceIntent = new Intent(
								HiveviewApplication.mContext,
								LockNetService.class);
						HiveviewApplication.mContext
								.startService(lockNetServiceIntent);
						}
						
					} else {
						Log.i("LockNet", "国外版版本 不需要锁网");
					}
				}
				return;
			} catch (Exception e) {
				Log.i("LockNet",
						"onReceive....CONNECTIVITY_ACTION......Exception:"
								+ e.toString());
			}
		} else if (action.equals(NET_WORK_STATUS_CHANGCE)) {// ping任务广播
			try {
				Log.i("LockNet", "receive ping task......");
				Bundle b = intent.getBundleExtra("ping");
				// 设置默认值 -3
				int ret = b.getInt("status", -3);
				if (ret == 0) { // 0表示ping任务成功
					if (isNetWrokChangce) {
						Log.i("LockNet",
								"onReceive NET_WORK_STATUS_CHANGCE........."
										+ ret + ",......isNetWrokChangce:"
										+ isNetWrokChangce);
						if (AppConstant.ISDOMESTIC) {
							if(HomeActivity.isNeedChlitina){
								Log.i("LockNet", "国内克丽缇娜版本 不需要锁网");
							}// 国内开启锁网服务
							else{
							Intent lockNetServiceIntent = new Intent(
									HiveviewApplication.mContext,
									LockNetService.class);
							HiveviewApplication.mContext
									.startService(lockNetServiceIntent);
							}
							
						} else {
							Log.i("LockNet", "国外版版本 不需要锁网");
						}
						isNetWrokChangce = false;
					}
				} else {
					isNetWrokChangce = true;
					Log.i("LockNet",
							"onReceive NET_WORK_STATUS_CHANGCE has changce........."
									+ ret + ",....isNetWrokChangce:"
									+ isNetWrokChangce);
				}
				return;
			} catch (Exception e) {
				Log.i("LockNet",
						"onReceive......NET_WORK_STATUS_CHANGCE......Exception:"
								+ e.toString());
			}

		}
	}

}
