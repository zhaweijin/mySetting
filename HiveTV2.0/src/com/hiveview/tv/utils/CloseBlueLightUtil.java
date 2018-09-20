package com.hiveview.tv.utils;

import android.content.Intent;

import com.hiveview.tv.common.HiveviewApplication;

/**
 * @ClassName: CloseBlueLightUtil
 * @Description: 极清提供的关闭其播放器的广播
 * @author: zhangpengzhan
 * @date 2015年1月18日 下午1:14:41
 * 
 */
public class CloseBlueLightUtil {
	
	/**
	 * @Fields 极清提供的两条命令
	 */
	static String command_1 = "com.hiveview.cloudscreen.Action.HOME_CODE";
	static String command_2 = "com.hiveview.cloudscreen.Action.SOURCE_CODE";
	public static String command_3=  "com.hiveview.tv.home.menu";
	
	/**
	 * @Title: CloseBlueLightUtil
	 * @author:张鹏展
	 * @Description: 发送广播关闭极清播放器
	 */
	public static void closeBlueLight(){
		//HiveviewApplication.mContext.sendBroadcast(new Intent(command_1));
		HiveviewApplication.mContext.sendBroadcast(new Intent(command_2));
	}
	
	public static void closeHomeMenu(){
		//HiveviewApplication.mContext.sendBroadcast(new Intent(command_1));
		HiveviewApplication.mContext.sendBroadcast(new Intent(command_3));
	}
}
