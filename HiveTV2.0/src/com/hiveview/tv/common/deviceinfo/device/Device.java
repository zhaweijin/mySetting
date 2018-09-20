/**
 * @Title IDevice.java
 * @Package com.hiveview.tv.common.device
 * @author haozening
 * @date 2014年7月24日 下午6:33:41
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.deviceinfo.device;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.DevInfoManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.hiveview.tv.common.deviceinfo.rom.Rom;
import com.hiveview.tv.service.HiveTVService;

/**
 * 设备对象，从此设备类中获取设备信息
 * 
 * @ClassName IDevice
 * @Description
 * @author haozening
 * @date 2014年7月24日 下午6:33:41
 * 
 */
public abstract class Device {
	/**
	 * 设备码
	 */
	private static String dvicesCode = "";
	/**
	 * 默认的sn
	 */
	public static final String DEFAULT_SN = "DMA11111120111111";
	/**
	 * MAC
	 */
	public static String mac = "";
	/**
	 * WlanMac
	 */
	public static String wlanMac = "";
	/**
	 * sn
	 */
	public static String sn = "";
	/**
	 * hardwareVersion
	 */
	public static String hardwareVersion = "";
	/**
	 * SoftwareVersion
	 */
	public static String softwareVersion = "";
	/**
	 * androidId
	 */
	public static String androidId = "";
	/**
	 * 设备类型
	 */
	public static String model = "";
	/**
	 * deviceVersion
	 */
	public static String deviceVersion = "";
	/**
	 * rom
	 */
	public static Rom rom;
	/**
	 * deviceName
	 */
	public static String deviceName;

	public DevInfoManager mDevInfoManager;
	/**
	 * WifiManager
	 */
	public WifiManager wifiManager;

	/**
	 * 设备原型
	 */
	public static final Device PROTO = new Device() {

		@Override
		public String getDeviceName() {
			return Rom.getModel();
		}

		@Override
		public Rom getRom() {
			return Rom.NULL;
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
		public String getSoftwareVersion() {
			return "";
		}

		@Override
		public String getAndroidId(Context context) {
			return "";
		}
	};
	private static final String TAG = "Device";

	/**
	 * 获取设备名称
	 * 
	 * @Title getDeviceName
	 * @author haozening
	 * @Description
	 * @return
	 */
	public String getDeviceName() {
		if (TextUtils.isEmpty(deviceName)) {
			return getClass().getSimpleName();
		}
		return deviceName;
	}

	/**
	 * 获取Android id
	 * 
	 * @Title getAndroidId
	 * @author haozening
	 * @Description
	 * @param context
	 * @return
	 */
	public String getAndroidId(Context context) {
		return getRom().getAndroidId(context);
	}

	/**
	 * 设置设备的Rom
	 * 
	 * @Title getRom
	 * @author haozening
	 * @Description
	 * @return
	 */
	public abstract Rom getRom();

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
	 * 
	 * @Title getWlanMac
	 * @author haozening
	 * @Description
	 * @return
	 */
	public abstract String getWlanMac();

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
	 * 获取设备版本号
	 * 
	 * @Title getDeviceVersion
	 * @author haozening
	 * @Description
	 * @return 如果匹配到设备，返回DeviceVersion值，匹配不到返回null
	 */
	public DeviceVersion getDeviceVersion() {
		return DeviceVersion.getVersion(getDeviceName());
	}

	/**
	 * 获取节点
	 * 
	 * @Title loadNode
	 * @author haozening
	 * @Description
	 * @param node
	 * @return
	 * @throws IOException
	 */
	protected static String loadNode(String node) throws Exception {
		// Log.d(TAG, "loadNode() --> node : " + node);
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(node));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		reader.close();
		// Log.d(TAG, "loadNode() --> node : " + node + "  value : " +
		// fileData.toString());
		if ("".equals(fileData.toString())) {
			// throw new Exception();
			Log.i(TAG, "LoadNode error.");
		}
		return fileData.toString();
	}

	/**
	 * 获取设备型号
	 * 
	 * @Title getModel
	 * @author haozening
	 * @Description
	 * @return
	 */
	public String getModel() {
		Log.d(TAG, "=======");
		if (TextUtils.isEmpty(model)) {
			model = android.os.Build.MODEL;
			Log.d(TAG, "android.os.Build.MODEL = " + model);
			if ("DM1002".equals(model)) {
				Log.d(TAG, "model equals DM1002||DM1102");
				return model;
			} else {
				try {
					// testThrowMyException();
					Log.d(TAG, "loadNode : " + loadNode("sys/class/aml_keys/aml_keys/model"));
					model = loadNode("sys/class/aml_keys/aml_keys/model");
					return model;
				} catch (Exception e) {
					// e.printStackTrace();
					Log.i(TAG, "GetModel error ....");
					Log.d(TAG, "Rom.getModel() : " + Rom.getModel());
					model = Rom.getModel();
					return model;
				}
			}

		}
		return model;

	}

	/**
	 * 测试抛异常方法
	 * 
	 * @Title testThrowMyException
	 * @author haozening
	 * @Description
	 * @throws IOException
	 */
	public static void testThrowMyException() throws IOException {
		// TODO 需要注释掉
		// throw new IOException();
	}

	/**
	 * 设备版本，如果增加设备，或者增加版本，在此处添加enum值即可</br> 规则：VERSION(versionCode,
	 * deviceName,[deviceName]...)
	 * 
	 * @ClassName DeviceVersion
	 * @Description
	 * @author haozening
	 * @date 2014年7月28日 上午11:01:17
	 * 
	 */
	public static enum DeviceVersion {
		// RTD 1195项目根据最新的大麦盒子产品型号规则命名为 DM2116，请大家知悉。
		VERSION_1(1, "DM1001", "DM1002", "DM1003", "DM1004", "DM1005", "DB1004", "DB1001", "DB1005","DM1102"), VERSION_2(2, "DM2003", "DM2004", "DM2005",
				"DM2116","DB2116");

		private int versionCode;
		private List<String> deviceName = new ArrayList<String>();

		private DeviceVersion(int versionCode, String... deviceName) {
			this.versionCode = versionCode;
			Collections.addAll(this.deviceName, deviceName);
		}

		public static DeviceVersion getVersion(String deviceName) {
			DeviceVersion[] versions = DeviceVersion.values();
			for (int i = 0; i < versions.length; i++) {
				Log.d(TAG, "versions[" + i + "].deviceName = " + versions[i].deviceName);
				if (versions[i].deviceName.contains(deviceName)) {
					return versions[i];
				}
			}
			return null;
		}

		public int getVersionCode() {
			return versionCode;
		}
	}

	/**
	 * 获取设备码
	 * 
	 * @Title: Device
	 * @author:郭松胜
	 * @Description: TODO
	 * @return
	 */
	public String getDeviceCode(Context context) throws Exception {
		if (TextUtils.isEmpty(dvicesCode)) {
			Device device = DeviceFactory.getInstance().getDevice();
			dvicesCode = new HiveTVService().getBoxCode(context, device.getMac(), device.getSN());
		}
		return dvicesCode;
	}

	/**
	 * 初始化
	 * 
	 * @Title: Device
	 * @author:guosongsheng
	 * @Description:
	 * @param context
	 */
	public void initDeviceInfo(Context context) {
		mDevInfoManager = (DevInfoManager) context.getSystemService(DevInfoManager.DATA_SERVER);
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}
}
