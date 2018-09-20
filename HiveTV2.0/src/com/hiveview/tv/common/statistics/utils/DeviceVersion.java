/**
 * @Title DeviceVersion.java
 * @Package com.hiveview.tv.utils
 * @author haozening
 * @date 2014年6月26日 下午3:10:25
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.statistics.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.util.Log;

/**
 * @ClassName DeviceVersion
 * @Description
 * @author haozening
 * @date 2014年6月26日 下午3:10:25
 * 
 */
public class DeviceVersion {

	private static final String TAG = "DeviceVersion";
	/**
	 * 同维设备型号
	 */
	private static final String DM1001 = "DM1001";
	/**
	 * 创维设备型号
	 */
	private static final String DM1002 = "DM1002";
	
	
	/**
	 * 获取设备ROM Version方法
	 * 
	 * @return
	 */
	public static String getDeviceVersion(Context context) {
		if (getDevice().equals(DM1001)) {
			String dm1001dv = getDM1001DeviceVersion(context);
			Log.d(TAG, " DM1001 DeviceVersion : " + dm1001dv);
			return dm1001dv;
		} else if (getDevice().equals(DM1002)) {
			String dm1002dv = getDM1002DeviceVersion();
			Log.d(TAG, " DM1002 DeviceVersion : " + dm1002dv);
			return dm1002dv;
		} else {
			Log.e(TAG, "getDeviceVersion() No Device Matched");
			return "null";
		}
	}
	/**
	 * 获取设备版本
	 * 
	 * @return
	 */
	private static String getDevice() {
		return android.os.Build.MODEL;
	}

	/**
	 * @Title getDM1002DeviceVersion
	 * @author haozening
	 * @Description 
	 * @return
	 */
	private static String getDM1002DeviceVersion() {
		return getProperty("persist.sys.hwconfig.hw_ver");
	}

	/**
	 * 同维获取设备版本
	 * @Title getDM1001DeviceVersion
	 * @author haozening
	 * @Description 
	 * @param context
	 * @return
	 */
	private static String getDM1001DeviceVersion(Context context) {
		String hw = null;
		try {
			hw = loadFileAsString("sys/class/aml_keys/aml_keys/hw");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (hw == null || hw.isEmpty()) {
				hw = getProperty("ro.product.hw.version");
				Log.i(TAG, "getDM1001DeviceVersion-->in finally:hw:"+hw);
			}
		}
		Log.i(TAG, "getDM1001DeviceVersion-->hw:"+hw);
		return hw;
	}

	public static String loadFileAsString(String filePath) throws java.io.IOException {
		File file = new File(filePath);
		if (!file.exists())
			return null;
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		return fileData.toString();
	}
	
	private static String getProperty(String key) {
		String result = "";
		try {
			Class<?> clazz = Class.forName("android.os.SystemProperties");
			Object obj = clazz.newInstance();
			Method method = clazz.getDeclaredMethod("get", String.class);
			Object resultObj = method.invoke(obj, key);
			result = resultObj.toString();
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

}
