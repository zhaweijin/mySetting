/**
 * @Title DeviceUtils.java
 * @Package com.hiveview.settings.utils
 * @author ZhaiJianfeng
 * @date 2014-7-28 下午1:09:51
 * @Description TODO
 * @version V1.0
 */
package com.hiveview.tv.utils;

import android.app.DevInfoManager;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.deviceinfo.device.Device;
import com.hiveview.tv.common.deviceinfo.device.Device.DeviceVersion;
import com.hiveview.tv.common.deviceinfo.device.DeviceFactory;

/**
 * @ClassName: DeviceUtils
 * @Description: 获取设备信息
 * @author: ZhaiJianfeng
 * @date 2014-7-28 下午1:09:51
 * 
 */
public class DeviceBoxUtils {

	/**
	 * @Fields MAC 盒子的有线mac地址
	 */
	public static String MAC = "";
	/**
	 * @Fields SN 盒子的sn号码
	 */
	public static String SN = "";
	/**
	 * @Fields VERSION rom 的版本
	 */
	public static String VERSION = "";
	/**
	 * @Fields MODE 盒子的型号
	 */
	public static String MODE = "";

	/**
	 * @Fields APPVERSION launcher version code
	 */
	public static String APPVERSION = "";

	/**
	 * @Fields DEVICENAME 设备名称
	 */
	public static String DEVICENAME = "";

	private static Device device = DeviceFactory.getInstance().getDevice();
	private static DeviceVersion version = device.getDeviceVersion(); // 获取设备版本号，version为enum类型
	private int versionCode = version.getVersionCode(); // getVersionCode返回int类型，版本号：1
														// / 2
	private static DevInfoManager mDevInfoManager = (DevInfoManager) HiveviewApplication.mContext.getSystemService(DevInfoManager.DATA_SERVER);
	private static String TAG = "DeviceUtils";

	public static int getDeviceVersionCode() {
		try {
	
		// 根据model第三位区分版本号
		String model3 = getDeviceModel().substring(2, 3);
		//1代表1.0的盒子
		if (model3.equals("1")) {
			return 1;
		}//2代表2.0的盒子
		else if (model3.equals("2")) {
			return 2;
		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	//	return device.getDeviceVersion().getVersionCode();
	}

	/**
	 * @Title: DeviceUtils
	 * @author:张鹏展
	 * @Description: 获取盒子 mac 地址
	 * @return
	 */
	public static String getMac() {
		try {
			
		if ("".equals(MAC)) {
			if (null != mDevInfoManager) {
				Log.d(TAG,
						"DevInfoManager.STBMAC" + mDevInfoManager.getValue(DevInfoManager.STB_MAC) == null ? "0" : mDevInfoManager
								.getValue(DevInfoManager.STB_MAC));
				MAC = mDevInfoManager.getValue(DevInfoManager.STB_MAC).isEmpty() ? device.getMac() : mDevInfoManager.getValue(DevInfoManager.STB_MAC);
			} else {
				MAC = device.getMac();
			}

		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return MAC;

	}

	/**
	 * @Title: DeviceUtils
	 * @author:张鹏展
	 * @Description: 获取盒子sn 号码
	 * @return
	 */
	public static String getSn() {
		try {
			
		if ("".equals(SN)) {
			if (null != mDevInfoManager) {
				Log.d(TAG,
						"DevInfoManager.STBSN" + mDevInfoManager.getValue(DevInfoManager.STB_SN) == null ? "0" : mDevInfoManager
								.getValue(DevInfoManager.STB_SN));
				SN = mDevInfoManager.getValue(DevInfoManager.STB_SN).isEmpty() ? device.getSN() : mDevInfoManager.getValue(DevInfoManager.STB_SN);
			} else {
				SN = device.getSN();
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return SN;
	}

	/**
	 * @Title: DeviceUtils
	 * @author:张鹏展
	 * @Description: 系统软件版本号
	 * @return
	 */
	public static String getSoftVersion(){
		try{
		if("".equals(VERSION)){
			if(null != mDevInfoManager){
				VERSION = mDevInfoManager.getValue(DevInfoManager.SWVERSION).isEmpty()? device.getSoftwareVersion() : mDevInfoManager.getValue(DevInfoManager.SWVERSION);
			} else {
				VERSION = device.getSoftwareVersion();
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return VERSION;
	}
	
	public static String getWifiMac() {
		return device.getWlanMac();
	}

	public static String getDeviceName() {
		return device.getDeviceName();
	}
	

	/**
	 * @Title: DeviceUtils
	 * @author:张鹏展
	 * @Description: 获取盒子mode号码
	 * @return
	 */
	public static String getDeviceModel() {
		try{
		if ("".equals(MODE)) {
			if (null != mDevInfoManager) {
				MODE = mDevInfoManager.getValue(DevInfoManager.MODEL);
				Log.d(TAG, "MODEL))))"+mDevInfoManager.getValue(DevInfoManager.MODEL));
			} else {
				MODE = device.getModel();
			}
		}
		Log.d(TAG, "MODEL====="+MODE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return MODE;
	}

	public static boolean isDM1001Device() {
		return device.getDeviceName().equals("DM1001");
	}

	public static boolean isDM1002Device() {
		return device.getDeviceName().equals("DM1002");
	}
}
