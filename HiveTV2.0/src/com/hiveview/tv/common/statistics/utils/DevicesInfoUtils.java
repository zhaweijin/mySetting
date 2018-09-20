/**
 * @Title DevicesInfoUtil.java
 * @Package com.hiveview.tv.common.statistics.utils
 * @author haozening
 * @date 2014年6月27日 下午2:58:04
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.statistics.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;

/**
 * @ClassName DevicesInfoUtil
 * @Description
 * @author haozening
 * @date 2014年6月27日 下午2:58:04
 * 
 */
public class DevicesInfoUtils {
	private static final String TAG = "DevicesInfoUtils";
	private static final String LAUNCHER_PACKAGE = "com.hiveview.tv";

	/**
	 * @param _context
	 * 
	 * @return
	 */
	public static String getLocaldeviceId(Context _context) {
		String deviceId = "null";
		deviceId = Secure.getString(_context.getContentResolver(), Secure.ANDROID_ID);
		if (TextUtils.isEmpty(deviceId)) {
			deviceId = String.valueOf(System.currentTimeMillis());
		}
		return deviceId;
	}

	/** 获取版本 */
	public static String getVersionName(Context ctx) {
		try {
			PackageInfo packInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
			return packInfo.versionName;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	/**
	 * 获取本地连接mac
	 * 
	 * @Title getBoxDeviceLocalMac
	 * @author haozening
	 * @Description
	 * @return
	 */
	public static String getBoxDeviceLocalMac() {
		Log.d(TAG, "getBoxDeviceLocalMac()");
		String mac = null;
		FileInputStream fis_name = null;
		try {

			if (mac == null || mac.length() == 0) {
				String path = "sys/class/net/eth0/address";
				File file = new File(path);
				if (!file.exists()) {
					return "null";
				}
				Log.d(TAG, "sys/class/net/eth0/address");
				fis_name = new FileInputStream(path);
				byte[] buffer_name = new byte[8192];
				int byteCount_name = fis_name.read(buffer_name);
				if (byteCount_name > 0) {
					mac = new String(buffer_name, 0, byteCount_name, "utf-8");
				}
				Log.d(TAG, "sys/class/net/eth0/address Mac:" + mac);
			}

			if (mac.length() == 0 || mac == null) {
				return "null";
			}
		} catch (Exception io) {
			io.printStackTrace();
		} finally {
			if (fis_name != null) {
				try {
					fis_name.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		Log.d(TAG, "Mac:" + mac);
		return mac.trim();

	}

	/**
	 * 获取无线mac
	 * 
	 * @Title getBoxDeviceWlanMac
	 * @author haozening
	 * @Description TODO
	 * @return
	 */
	public static String getBoxDeviceWlanMac() {
		Log.d(TAG, "getBoxDeviceWlanMac()");
		String mac = null;
		FileInputStream fis_name = null;
		try {

			if (mac == null || mac.length() == 0) {
				String path = "sys/class/net/wlan0/address";
				Log.d(TAG, "sys/class/net/wlan0/address");
				File file = new File(path);
				if (!file.exists()) {
					return "null";
				}
				fis_name = new FileInputStream(path);
				byte[] buffer_name = new byte[8192];
				int byteCount_name = fis_name.read(buffer_name);
				if (byteCount_name > 0) {
					mac = new String(buffer_name, 0, byteCount_name, "utf-8");
				}
				Log.d(TAG, "sys/class/net/wlan0/address:" + mac);
			}

			if (mac.length() == 0 || mac == null) {
				return "null";
			}
		} catch (Exception io) {
			io.printStackTrace();
		} finally {
			if (fis_name != null) {
				try {
					fis_name.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		Log.d(TAG, "Mac:" + mac);
		return mac.trim();
	}

	public static String getLauncherVersion(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(LAUNCHER_PACKAGE, PackageManager.GET_ACTIVITIES);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "null";
	}

	/**
	 * 获取设备版本
	 * 
	 * @return
	 */
	public static String getDeviceModel() {
		return android.os.Build.MODEL;
	}
}
