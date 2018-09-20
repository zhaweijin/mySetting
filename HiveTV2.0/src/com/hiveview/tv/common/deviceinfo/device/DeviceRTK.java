package com.hiveview.tv.common.deviceinfo.device;

import android.app.DevInfoManager;
import android.net.wifi.WifiInfo;
import android.text.TextUtils;
import android.util.Log;

import com.hiveview.tv.common.deviceinfo.rom.Rom;
import com.hiveview.tv.common.deviceinfo.rom.RomFactory;
import com.hiveview.tv.common.deviceinfo.rom.RomFactory.RomName;

/**
 * RTK平台设备信息
 * 
 * @ClassName: DeviceRTK
 * @Description:
 * @author: guosongsheng
 * @date 2014-11-2 下午8:04:48
 * 
 */
public class DeviceRTK extends Device {
	/**
	 * TAG
	 */
	private static final String TAG = "DeviceRTK";

	@Override
	public Rom getRom() {
		if(null == rom){
			rom = RomFactory.getInstance().getRom(RomName.RTK);
		}
		return rom;
	}

	@Override
	public String getSN() {
		try {
			testThrowMyException();
			if (TextUtils.isEmpty(sn)) {
				sn = mDevInfoManager.getValue(DevInfoManager.STB_SN).trim();
			}
			return sn;
		} catch (Exception e) {
//			e.printStackTrace();
			Log.i(TAG, "Get sn error!");
			return Device.DEFAULT_SN;
		}
	}

	@Override
	public String getMac() {
		try {
			testThrowMyException();
			if (TextUtils.isEmpty(mac)) {
				mac = mDevInfoManager.getValue(DevInfoManager.STB_MAC).trim();
			}
			return mac;
		} catch (Exception e) {
//			e.printStackTrace();
			Log.i(TAG, "Get mac error!");
			return getRom().getMac().trim();
		}
	}

	@Override
	public String getWlanMac() {
		try {
			testThrowMyException();
			if (TextUtils.isEmpty(wlanMac)) {
				if (null != mDevInfoManager && null != wifiManager) {
					WifiInfo wifiInfo = wifiManager.getConnectionInfo();
					wlanMac = wifiInfo.getMacAddress().trim();
				}
			}
			return wlanMac;
		} catch (Exception e) {
//			e.printStackTrace();
			Log.i(TAG, "Get wlanMac error!");
			return getRom().getWlanMac().trim();
		}
	}

	@Override
	public String getHardwareVersion() {
		try {
			testThrowMyException();
			if (TextUtils.isEmpty(hardwareVersion)) {
				if (null != mDevInfoManager) {
					hardwareVersion = mDevInfoManager.getValue(DevInfoManager.HWVERSION).trim();
				}
			}
			Log.i(TAG, "Get hardwareVersion success!");
			return hardwareVersion;
		} catch (Exception e) {
//			e.printStackTrace();
			Log.i(TAG, "Get hardwareVersion error!");
			return getRom().getHardwareVersion().trim();
		}
	}

	@Override
	public String getSoftwareVersion() {
		try {
			if (TextUtils.isEmpty(softwareVersion)) {
				if (null != mDevInfoManager) {
					softwareVersion = mDevInfoManager.getValue(DevInfoManager.SWVERSION).trim();
				}
			}
		} catch (Exception e) {
//			e.printStackTrace();
			Log.i(TAG, "Get softwareVersion error!");
			return getRom().getSoftwareVersion().trim();
		}
		return softwareVersion;
	}
}
