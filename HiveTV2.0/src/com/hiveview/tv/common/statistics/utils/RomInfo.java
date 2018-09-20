/**
 * @Title RomInfo.java
 * @Package com.hiveview.authentication.util
 * @author haozening
 * @date 2014年5月28日 下午5:13:12
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.statistics.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.util.Log;

/**
 * @ClassName RomInfo
 * @Description
 * @author haozening
 * @date 2014年5月28日 下午5:13:12
 * 
 */
public class RomInfo {

	private static final String TAG = RomInfo.class.getSimpleName();
	/**
	 * 同维设备型号
	 */
	private static final String DM1001 = "DM1001";
	/**
	 * 创维设备型号
	 */
	private static final String DM1002 = "DM1002";
	/**
	 * 同维读取ROM Version方式
	 */
	private static final String DM1001_ROM_VERSION = "ro.product.firmware";
	/**
	 * 创维读取ROM Version方式
	 */
	private static final String DM1002_ROM_VERSION = "persist.sys.hwconfig.soft_ver";

	/**
	 * 获取设备ROM Version方法
	 * 
	 * @return
	 */
	public static String getRomVersion() {
		Log.d(TAG, getDevice());
		if (getDevice().equals(DM1001)) {
			String dm1001rv = getDM1001RomVersion();
			Log.d(TAG, " DM1001 RomVersion : " + dm1001rv);
			return dm1001rv;
		} else if (getDevice().equals(DM1002)) {
			String dm1002rv = getDM1002RomVersion();
			Log.d(TAG, " DM1002 RomVersion : " + dm1002rv);
			return dm1002rv;
		} else {
			Log.e(TAG, "getRomVersion() No Device Matched");
			return "null";
		}
	}

	/**
	 * @Title getDM1001RomVersion
	 * @author haozening
	 * @Description
	 * @return
	 */
	private static String getDM1001RomVersion() {
		String resultstr = getProperty(DM1001_ROM_VERSION);
//		Log.d(TAG, "getDM1001RomVersion before format : " + resultstr);
//		int length = resultstr.length();
//		String first = resultstr.substring(length - 5, length - 3);
//		String second = resultstr.substring(length - 3, length);
//		String result = "1." + first + "." + second;
		Log.d(TAG, "getDM1001RomVersion : " + resultstr);
		return resultstr;
	}

	/**
	 * @Title getDM1002RomVersion
	 * @author haozening
	 * @Description
	 * @return
	 */
	private static String getDM1002RomVersion() {
		String resultstr = getProperty(DM1002_ROM_VERSION);
//		Log.d(TAG, "getDM1002RomVersion before format : " + resultstr);
//		int length = resultstr.length();
//		String first = resultstr.substring(0, 1);
//		String second = resultstr.substring(1, 3);
//		String third = resultstr.substring(3, length);
//		String result = first + "." + second + "." + third;
		Log.d(TAG, "getDM1002RomVersion : " + resultstr);
		return resultstr;
	}

	private static String getProperty(String key) {
		String result = "";
		try {
			Class<?> clazz = Class.forName("android.os.SystemProperties");
			Object obj = clazz.newInstance();
			Method method = clazz.getDeclaredMethod("get", String.class);
			Object resultObj = method.invoke(obj, key);
			result = resultObj.toString();
			Log.d(TAG, result);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * 获取设备版本
	 * 
	 * @return
	 */
	private static String getDevice() {
		return android.os.Build.MODEL;
	}
}
