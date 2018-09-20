/**
 * @Title RemotecontrolReceiver.java
 * @Package com.hiveview.tv.service
 * @author lihongji
 * @date 2014年12月2日 上午11:23:07
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.service;

import java.io.IOException;

import com.hiveview.tv.activity.HomeActivity;
import com.hiveview.tv.activity.OnlivePlayerActivity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.voice.IVoiceController;
import com.hiveview.tv.common.voice.impl.OpenFunctionVoiceController;
import com.hiveview.tv.utils.CloseBlueLightUtil;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.view.AuxiliaryNetworkView;
import com.hiveview.tv.view.HiveviewHdmiInView;
import com.hiveview.tv.view.pager3d.TabBasePageView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * @ClassName: RemotecontrolReceiver
 * @Description:新红外遥控器广播接受类
 * @author: lihongji
 * @date 2014年12月2日 上午11:23:07
 * 
 */
public class RemotecontrolReceiver extends BroadcastReceiver {

	public final static String BROADCAST_LAUNCHER_VALUE = "com.hiveview.tv.value";
	public final static String BROADCAST_SETTING_VALUE = "com.hiveview.settings.value";
	/**
	 * launhcer快捷键、一键清理action
	 */
	public final static String BROADCAST_LAUNCHER_ACTION = "com.hiveview.tv.REMOTECONTROL";
	/**
	 * 设置一键清理action
	 */
	public final static String BROADCAST_SETTING_ACTION = "com.hiveview.settings.REMOTECONTROL";
	/**
	 * 快捷键一键清理场景
	 */
	private final static String TV_CLEARCACHE_SENCE = "com.hiveview.settings.clearcache";
	/**
	 * 蓝牙遥控器空鼠键一键清理、一键全屏action
	 */
	public final static String BROADCAST_TV_ACTION = "com.hiveview.keycode";
	// 一键全屏
	public final static int KEYCODE_TV_SOURCE = 2057;
	// 空鼠键一键清理
	public final static int KEYCODE_TV_CLEARCACHE = 2058;
	// 语音提示
	public final static int KEYCODE_TV_MICROPHONE = 2054;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("RemotecontrolReceiver", "keycode1========="
				+AppScene.getScene());
		// 引导页未加载完成时屏蔽搜索等功能键
		if (!HomeActivity.isSuccessed) {
			return;
		}
		// 搜索、应用、游戏、电视、极清等
		String name = intent.getExtras().getString(BROADCAST_LAUNCHER_VALUE);
		// 一键清理
		String clearcache = intent.getExtras().getString(
				BROADCAST_SETTING_VALUE);
		// 空鼠键一键清理和全屏
		Bundle kBundle = intent.getBundleExtra("KeyCode");
		Log.v("RemotecontrolReceiver", "keycode2=========");
		int keycode = 0;
		if (kBundle != null) {
			keycode = kBundle.getInt("value");
			Log.v("RemotecontrolReceiver", "keycode3=========" + keycode);
		}
		if(!AppConstant.ISDOMESTIC){
			HiveviewApplication.mcurrentfocus=null;
		}
		// !TabBasePageView.isRotating() tab页面切换动画完成再执行下次按键操作
		if (name != null && !TabBasePageView.isRotating()) {
			Log.v("RemotecontrolReceiver", "onReceive" + name);
			// 影院
			if (name.equals("TV_VIDEO")) {
				Intent in = new Intent(context, HomeActivity.class);
				in.putExtra(IVoiceController.COMMAND, "movie");
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(in);
				// 关闭极清
				CloseBlueLightUtil.closeBlueLight();
				CloseBlueLightUtil.closeHomeMenu();
			}
			// 游戏
			else if (name.equals("TV_GAME")) {
				Log.v("RemotecontrolReceiver", "keycode3========="+name);
				Intent in = new Intent(context, HomeActivity.class);
				in.putExtra(IVoiceController.COMMAND, "game");
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(in);
				// 关闭极清
				CloseBlueLightUtil.closeBlueLight();
				CloseBlueLightUtil.closeHomeMenu();
			}
			// 应用
			else if (name.equals("TV_APP")) {
				Intent in = new Intent(context, HomeActivity.class);
				in.putExtra(IVoiceController.COMMAND, "app");
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(in);
				// 关闭极清
				CloseBlueLightUtil.closeBlueLight();
				CloseBlueLightUtil.closeHomeMenu();
			}
			// 蓝光
			else if (name.equals("TV_HDVIDEO")) {
				if(AppConstant.ISDOMESTIC){
				// 需要出现极清
				if (HiveviewApplication.outer == 4) {
					Intent in = new Intent(context, HomeActivity.class);
					in.putExtra(IVoiceController.COMMAND, "blue");
					in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(in);
					// 关闭极清
					CloseBlueLightUtil.closeBlueLight();
					CloseBlueLightUtil.closeHomeMenu();
				}}else{
					Intent in = new Intent(context, HomeActivity.class);
					in.putExtra(IVoiceController.COMMAND, "blue");
					in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(in);
					// 关闭极清
					CloseBlueLightUtil.closeBlueLight();
					CloseBlueLightUtil.closeHomeMenu();
				}
			}
			// 搜索
			else if (name.equals("TV_SEARCH")) {
				// 已经在搜索页时屏蔽搜索功能键
				if (AppScene.getScene().equals(
						"com.hiveview.tv.activity.SearchHomeActivity")) {
					return;
				}
				if(AppConstant.ISDOMESTIC){
					Intent intent3 = new Intent("com.hiveview.cloudscreen.search.action.SEARCH");
					intent3.putExtra("resultShowType", "vertical");
					intent3.addCategory(Intent.CATEGORY_DEFAULT);
					intent3.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent3);	
				}else{
				Intent in = new Intent();
				int netStatus = HiveviewApplication.getNetStatus();
				in.setAction(OpenFunctionVoiceController.MOVIE_SEARCH_ACTION);
				in.addCategory(Intent.CATEGORY_DEFAULT);
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				in.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(in);
				}
				// 关闭极清
				CloseBlueLightUtil.closeBlueLight();
				CloseBlueLightUtil.closeHomeMenu();
			}
			// 关机
			else if (name.equals("shutdown")) {
				try {
					Log.v("RemotecontrolReceiver", "RemotecontrolReceiver"
							+ name);
					Process p = Runtime.getRuntime().exec(
							"adb shell input keyevent 26");
					// 关闭极清
					CloseBlueLightUtil.closeBlueLight();
					CloseBlueLightUtil.closeHomeMenu();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 重启
			else if (name.equals("reboot")) {
				try {
					Log.v("RemotecontrolReceiver", "RemotecontrolReceiver"
							+ name);
					Process p = Runtime.getRuntime().exec("adb reboot");
					// 关闭极清
					CloseBlueLightUtil.closeBlueLight();
					CloseBlueLightUtil.closeHomeMenu();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 确定
			else if (name.equals("center")) {
				try {
					Log.v("RemotecontrolReceiver", "RemotecontrolReceiver"
							+ name);
					Process p = Runtime.getRuntime().exec(
							"adb shell input keyevent 23");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 返回
			else if (name.equals("back")) {
				try {
					Log.v("RemotecontrolReceiver", "RemotecontrolReceiver"
							+ name);
					Process p = Runtime.getRuntime().exec(
							"adb shell input keyevent 4");
					// 关闭极清
					CloseBlueLightUtil.closeBlueLight();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 主页
			else if (name.equals("home")) {
				try {
					Log.v("RemotecontrolReceiver", "RemotecontrolReceiver"
							+ name);
					Process p = Runtime.getRuntime().exec(
							"adb shell input keyevent 3");
					// 关闭极清
					CloseBlueLightUtil.closeBlueLight();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 菜单
			else if (name.equals("menu")) {
				try {
					Log.v("RemotecontrolReceiver", "RemotecontrolReceiver"
							+ name);
					Process p = Runtime.getRuntime().exec(
							"adb shell input keyevent 82");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 上
			else if (name.equals("up")) {
				try {
					Log.v("RemotecontrolReceiver", "RemotecontrolReceiver"
							+ name);
					Process p = Runtime.getRuntime().exec(
							"adb shell input keyevent 19");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 下
			else if (name.equals("down")) {
				try {
					Log.v("RemotecontrolReceiver", "RemotecontrolReceiver"
							+ name);
					Process p = Runtime.getRuntime().exec(
							"adb shell input keyevent 20");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 左
			else if (name.equals("left")) {
				try {
					Log.v("RemotecontrolReceiver", "RemotecontrolReceiver"
							+ name);
					Process p = Runtime.getRuntime().exec(
							"adb shell input keyevent 21");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// 右
			else if (name.equals("right")) {
				try {
					Log.v("RemotecontrolReceiver", "RemotecontrolReceiver"
							+ name);
					Process p = Runtime.getRuntime().exec(
							"adb shell input keyevent 22");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (clearcache != null && !TabBasePageView.isRotating()) {
			// 一键清理
			if (clearcache.equals("TV_CLEARCACHE")) {
				Intent in = new Intent();
				int netStatus = HiveviewApplication.getNetStatus();
				in.setAction("com.hiveview.settings.ACTION_SETTING");
				in.addCategory(Intent.CATEGORY_DEFAULT);
				in.putExtra("memory", "memory");
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				/* start by ZhaiJianfeng */
				in.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				/* end by ZhaiJianfeng */
				context.startActivity(in);
				AppScene.setScene(TV_CLEARCACHE_SENCE);
				// 关闭极清
				CloseBlueLightUtil.closeBlueLight();
				CloseBlueLightUtil.closeHomeMenu();
			}
		} else // 空鼠键一键清理
		if (keycode == KEYCODE_TV_CLEARCACHE && !TabBasePageView.isRotating()) {
			Log.v("RemotecontrolReceiver", "keycode4=========" + keycode);
			Intent in = new Intent();
			int netStatus = HiveviewApplication.getNetStatus();
			in.setAction("com.hiveview.settings.ACTION_SETTING");
			in.addCategory(Intent.CATEGORY_DEFAULT);
			in.putExtra("memory", "memory");
			in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			/* start by ZhaiJianfeng */
			in.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
			/* end by ZhaiJianfeng */
			context.startActivity(in);
			AppScene.setScene(TV_CLEARCACHE_SENCE);
			// 关闭极清
			CloseBlueLightUtil.closeBlueLight();
			CloseBlueLightUtil.closeHomeMenu();

		} else // 全屏
		if (keycode == KEYCODE_TV_SOURCE && !TabBasePageView.isRotating()) {
			Log.v("RemotecontrolReceiver", "keycode5=========" + keycode);

//			Log.v("RemotecontrolReceiver", "keycode7=========" + keycode);
//			// //有hdmin接入时跳入大屏。暂时没用到
//			if (HiveviewHdmiInView.isSignalable) {
//
//				// 已经是大屏界面 不再跳转
//				if (AppScene.getScene().equals(
//						"com.hiveview.tv.activity.OnlivePlayerActivity")&&OnlivePlayerActivity.isResume) {
//					Log.v("RemotecontrolReceiver", "keycode6========="
//							+ keycode);
//					return;
//				}
//				Intent in = new Intent(context, OnlivePlayerActivity.class);
//				context.sendBroadcast(new Intent(
//						HiveviewHdmiInView.ACTION_SMALL_SCREEN_SWITCH));
//				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(in);
//			} else
//			// 否则调到电视页面
//			{
				// 已经在电视tab则不再跳转
				if (AppScene.getScene().equals(
						"com.hiveview.tv.view.MaxtrTvView")&&HomeActivity.isResume) {
					return;
				}
				Log.v("RemotecontrolReceiver", "keycode8=========" + keycode);
				Intent in = new Intent(context, HomeActivity.class);
				in.putExtra(IVoiceController.COMMAND, "tv");
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(in);
				// 关闭极清
				CloseBlueLightUtil.closeBlueLight();
				CloseBlueLightUtil.closeHomeMenu();
			//}
		}

	}

}
