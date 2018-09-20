package com.hiveview.tv.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;

import com.hiveview.tv.R;
import com.hiveview.tv.activity.HomeActivity;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.deviceinfo.device.DeviceFactory;
import com.hiveview.tv.service.exception.ServiceException;

@SuppressLint("SimpleDateFormat")
public class AppUtil {

	protected static final String TAG = "AppUtils";

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
	 * @Title: AppUtil
	 * @author:张鹏展
	 * @Description: 通过包名打开app
	 * @param packageName
	 * @param context
	 * 
	 *           
	 */
	public static void openAppForPackageName(String packageName,
			Context context) {
		try {
			Log.d(TAG, "openAppForPackageName-->0");
			Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
			Log.d(TAG, "openAppForPackageName-->1");
			if (intent != null) {
				Log.d(TAG, "openAppForPackageName-->2");
				context.startActivity(intent);
				Log.d(TAG, "openAppForPackageName-->3");
			}else{
				ToastUtils.alert(context, context.getResources().getString(R.string.yet_open));
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtils.alert(context,context.getResources().getString(R.string.yet_open));
			Log.d(TAG, "openAppForPackageName-->4");
		}
		
	}

	/** 开启应用 */
	public static void openApp(String packageName, Context context) {
		try {

			if ("com.hiveview.externalstorage".equals(packageName)) {
				
				String mModel=DeviceFactory.getInstance().getDevice().getModel().toString();
				//区别设备型号打开不同的外界存储
				if (!(HomeActivity.theFlage).contains(mModel.substring(2,6))) {
					Intent intent = context.getPackageManager()
							.getLaunchIntentForPackage("com.fone.player.domy");
					if (intent != null) {
						context.startActivity(intent);
					}
				} else {
					Log.v(TAG, mModel.substring(2,6));
					Intent intent1 = new Intent();
					intent1.putExtra("file_type", 1004);
					intent1.setAction("com.hiveview.externalstorage.action.APP_HOME");// 外接存储主界面的activity的action
					if (intent1 != null) {
						context.startActivity(intent1);
					}
				}
			} else {
				Intent intent = context.getPackageManager()
						.getLaunchIntentForPackage(packageName);
				if (intent != null) {
					context.startActivity(intent);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Intent intent = context.getPackageManager()
					.getLaunchIntentForPackage(packageName);
			if (intent != null) {
				context.startActivity(intent);
			}
		}
	}

	/**
	 * @param _context
	 * 
	 * @return
	 */
	public static String getLocaldeviceId(Context _context) {
		String deviceId = "null";
		try {
			deviceId = Secure.getString(_context.getContentResolver(), Secure.ANDROID_ID);
			if (TextUtils.isEmpty(deviceId))
				throw new ServiceException();
		} catch (ServiceException e) {
			deviceId = String.valueOf(System.currentTimeMillis());
		}
		return deviceId;
	}

//	public static String getDeviceMac() {
//		String macSerial = null;
//		String str = "";
//		try {
//			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
//			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
//			LineNumberReader input = new LineNumberReader(ir);
//
//			for (; null != str;) {
//				str = input.readLine();
//				if (str != null) {
//					macSerial = str.trim();// 去空格
//					break;
//				}
//			}
//		} catch (IOException ex) {
//			// 赋予默认值
//		}
//		return macSerial;
//	}

//	public static String getBoxDeviceMac() {
//		String Mac = null;
//		try {
//			String path = "sys/class/net/wlan0/address";
//			if ((new File(path)).exists()) {
//				FileInputStream fis = new FileInputStream(path);
//				byte[] buffer = new byte[8192];
//				int byteCount = fis.read(buffer);
//				if (byteCount > 0) {
//					Mac = new String(buffer, 0, byteCount, "utf-8");
//				}
//			}
//
//			if (Mac == null || Mac.length() == 0) {
//				path = "sys/class/net/eth0/address";
//				FileInputStream fis_name = new FileInputStream(path);
//				
	byte[] buffer_name = new byte[8192];
//				int byteCount_name = fis_name.read(buffer_name);
//				if (byteCount_name > 0) {
//					Mac = new String(buffer_name, 0, byteCount_name, "utf-8");
//				}
//			}
//
//			if (Mac.length() == 0 || Mac == null) {
//				return "";
//			}
//		} catch (Exception io) {
//
//		}
//
//		return Mac.trim();
//
//	}
//	/**
//	 * 获取本地连接mac
//	 * @Title getBoxDeviceLocalMac
//	 * @author haozening
//	 * @Description 
//	 * @return
//	 */
//	public static String getBoxDeviceLocalMac() {
//		Log.d(TAG, "getBoxDeviceLocalMac()");
//		String mac = null;
//		FileInputStream fis_name = null;
//		try {
//
//			if (mac == null || mac.length() == 0) {
//				String path = "sys/class/net/eth0/address";
//				File file = new File(path);
//				if (!file.exists()) {
//					return "null";
//				}
//				Log.d(TAG, "sys/class/net/eth0/address");
//				fis_name = new FileInputStream(path);
//				byte[] buffer_name = new byte[8192];
//				int byteCount_name = fis_name.read(buffer_name);
//				if (byteCount_name > 0) {
//					mac = new String(buffer_name, 0, byteCount_name, "utf-8");
//				}
//				Log.d(TAG, "sys/class/net/eth0/address Mac:" + mac);
//			}
//
//			if (mac.length() == 0 || mac == null) {
//				return "null";
//			}
//		} catch (Exception io) {
//			io.printStackTrace();
//		} finally {
//			if (fis_name != null) {
//				try {
//					fis_name.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//		Log.d(TAG, "Mac:" + mac);
//		return mac.trim();
//
//	}
//	/**
//	 * 获取无线mac
//	 * @Title getBoxDeviceWlanMac
//	 * @author haozening
//	 * @Description TODO
//	 * @return
//	 */
//	public static String getBoxDeviceWlanMac() {
//		Log.d(TAG, "getBoxDeviceWlanMac()");
//		String mac = null;
//		FileInputStream fis_name = null;
//		try {
//
//			if (mac == null || mac.length() == 0) {
//				String path = "sys/class/net/wlan0/address";
//				Log.d(TAG, "sys/class/net/wlan0/address");
//				File file = new File(path);
//				if (!file.exists()) {
//					return "null";
//				}
//				fis_name = new FileInputStream(path);
//				byte[] buffer_name = new byte[8192];
//				int byteCount_name = fis_name.read(buffer_name);
//				if (byteCount_name > 0) {
//					mac = new String(buffer_name, 0, byteCount_name, "utf-8");
//				}
//				Log.d(TAG, "sys/class/net/wlan0/address:" + mac);
//			}
//
//			if (mac.length() == 0 || mac == null) {
//				return "null";
//			}
//		} catch (Exception io) {
//			io.printStackTrace();
//		} finally {
//			if (fis_name != null) {
//				try {
//					fis_name.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//		Log.d(TAG, "Mac:" + mac);
//		return mac.trim();
//	}

	/**
	 * 获取设备版本
	 * @return
	 */
	public static String getDeviceModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 判断现在时间是否在列表时间的后面
	 * 
	 * @param time
	 * @return
	 */
	public static Boolean nowIsAferBoolean(String time) {
		// 转化格式
		Boolean restultBoolean = false;
		try {
			SimpleDateFormat sFormat = new SimpleDateFormat("HH:mm");
			Date date = sFormat.parse(time);
			long mTime = new Date().getTime() - System.currentTimeMillis();
			Date b = new Date(System.currentTimeMillis() + mTime);
			String bv = sFormat.format(b);
			Date date3 = sFormat.parse(bv);
			restultBoolean = date3.after(date);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return restultBoolean;

	}

	/**
	 * 判断日期时间是否在当前日期时间的后面
	 * 
	 * @author zhaijianfeng
	 * @param time
	 * @return
	 */
	public static Boolean nowDateTimeIsAferBoolean(String dateTime) {
		// 转化格式
		Boolean restultBoolean = false;
		Log.i(TAG, "nowDateTimeIsAferBoolean-->start");

		try {
			SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = sFormat.parse(dateTime);
			Log.i(TAG, "nowDateTimeIsAferBoolean-->dateTime:" + sFormat.format(date));
			Date currentDateTime = new Date(System.currentTimeMillis());
			restultBoolean = currentDateTime.after(date);
			Log.i(TAG, restultBoolean + "=nowDateTimeIsAferBoolean-->end" + sFormat.format(currentDateTime));
		} catch (ParseException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}

		return restultBoolean;

	}

	

}
