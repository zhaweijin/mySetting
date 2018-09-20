/**
 * @Title DeviceRom.java
 * @Package com.hiveview.tv.common.device
 * @author haozening
 * @date 2014年7月25日 上午11:26:29
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.deviceinfo.rom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;

import com.hiveview.tv.common.deviceinfo.device.Device;

/**
 * Rom类，不同版本设备对应不同Rom，具体实现在impl包
 * 
 * @ClassName DeviceRom
 * @Description
 * @author haozening
 * @date 2014年7月25日 上午11:26:29
 * 
 */
public abstract class Rom {

	private static final String TAG = "Rom";

	public static final Rom NULL = new Rom() {

		@Override
		public String getSoftwareVersion() {
			return "";
		}

		@Override
		public String getSN() {
			return "";
		}

		@Override
		public String getMac() {
			return "";
		}

		@Override
		public String getWlanMac() {
			return "";
		}

		@Override
		public String getHardwareVersion() {
			return "";
		}

		@Override
		public String getAndroidId(Context context) {
			return "";
		}
	};

	/**
	 * 获取盒子SN
	 * 
	 * @Title getSN
	 * @author haozening
	 * @Description
	 * @return
	 */
	public abstract String getSN();

	/**
	 * 获取盒子Mac
	 * 
	 * @Title getMac
	 * @author haozening
	 * @Description
	 * @return
	 */
	public abstract String getMac();
	
	/**
	 * 获取盒子无线Mac
	 * @Title getWlanMac
	 * @author haozening
	 * @Description 
	 * @return
	 */
	public abstract String getWlanMac();
	
	/**
	 * 获取Android id
	 * @Title getAndroidId
	 * @author haozening
	 * @Description 
	 * @param context
	 * @return
	 */
	public String getAndroidId(Context context) {
		String deviceId = "";
		deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		if (null == deviceId || "".equals(deviceId)) {
			deviceId = String.valueOf(System.currentTimeMillis());
		}
		return deviceId;
	}

	/**
	 * 获取硬件版本
	 * 
	 * @Title getHardwareVersion
	 * @author haozening
	 * @Description
	 * @return
	 */
	public abstract String getHardwareVersion();

	/**
	 * 获取软件版本
	 * 
	 * @Title getSoftwareVersion
	 * @author haozening
	 * @Description
	 * @return
	 */
	public abstract String getSoftwareVersion();

	/**
	 * 获取prop中的值
	 * 
	 * @Title getProperty
	 * @author haozening
	 * @Description
	 * @param key
	 * @return
	 */
	protected static String getProperty(String key) {
		String result = "";
		try {
			testThrowMyException();
			Class<?> clazz = Class.forName("android.os.SystemProperties");
			Object obj = clazz.newInstance();
			Method method = clazz.getDeclaredMethod("get", String.class);
			Object resultObj = method.invoke(obj, key);
			result = resultObj.toString();
			Log.d(TAG, "getProperty(\"" + key + "\") result : " + result);
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
			Log.i(TAG, ""+e.toString());
		} catch (NoSuchMethodException e) {
//			e.printStackTrace();
			Log.i(TAG, ""+e.toString());
		} catch (InstantiationException e) {
//			e.printStackTrace();
			Log.i(TAG, ""+e.toString());
		} catch (IllegalAccessException e) {
//			e.printStackTrace();
			Log.i(TAG, ""+e.toString());
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			Log.i(TAG, ""+e.toString());
		} catch (InvocationTargetException e) {
//			e.printStackTrace();
			Log.i(TAG, ""+e.toString());
		} catch (Exception e) {
//			e.printStackTrace();
			Log.i(TAG, ""+e.toString());
		}
		return result.toString();
	}

	/**
	 * 获取设备型号
	 * 
	 * @Title getModel
	 * @author haozening
	 * @Description
	 * @return
	 */
	public static String getModel() {
		if(TextUtils.isEmpty(Device.model)){
			Device.model = getProperty("ro.product.model");
			if (null == Device.model || "".equals(Device.model)) {
				Device.model = android.os.Build.MODEL;
			}
		}
		return Device.model;
	}

	/**
	 * 测试抛异常方法
	 * 
	 * @Title testThrowMyException
	 * @author haozening
	 * @Description
	 * @throws ClassNotFoundException
	 */
	public static void testThrowMyException() throws ClassNotFoundException {
		// TODO 需要注释掉
		// throw new ClassNotFoundException();
	}

}
