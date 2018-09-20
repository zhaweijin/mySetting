package com.hiveview.tv.utils;

import android.util.Log;

/**
 * 日志记录
 * 
 */
public class LogUtil {
	/**
	 * 开发阶段
	 */
	private static final int DEVELOP = 0;
	/**
	 * 内部测试阶段
	 */
	private static final int DEBUG = 1;
	/**
	 * 公开测试
	 */
	private static final int BATE = 2;
	/**
	 * 正式版
	 */
	private static final int RELEASE = 3;

	/**
	 * 当前阶段标示
	 */
	private static int currentStage = DEVELOP;

	public static void info(String msg) {
		info(LogUtil.class, msg);
	}

	public static void info(Class clazz, String msg) {
		switch (currentStage) {
		case DEVELOP:
			// 控制台输出
			Log.i(clazz.getSimpleName(), msg);
			break;
		case DEBUG:
			// 在应用下面创建目录存放日志
			break;
		case BATE:
			// 写日志到sdcard
			break;
		case RELEASE:
			// 一般不做日志记录
			break;
		}
	}
}
