/**
 * @Title TongWeiDevice.java
 * @Package com.hiveview.tv.common.deviceinfo.device
 * @author haozening
 * @date 2014年7月25日 下午12:04:34
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.deviceinfo.device;

import android.text.TextUtils;
import android.util.Log;

import com.hiveview.tv.common.deviceinfo.rom.Rom;
import com.hiveview.tv.common.deviceinfo.rom.RomFactory;
import com.hiveview.tv.common.deviceinfo.rom.RomFactory.RomName;

/**
 * 同维盒子，具体同维盒子型号在impl包中实现
 * 
 * @ClassName TongWeiDevice
 * @Description
 * @author haozening
 * @date 2014年7月25日 下午12:04:34
 * 
 */
public  class DeviceTongWei extends Device {
	/**
	 * TAG DeviceTongWei
	 */
	private static final String TAG = "DeviceTongWei";
	protected String snNode = "/sys/class/aml_keys/aml_keys/usid";
	protected String macNode = "/sys/class/net/eth0/address";
	protected String wlanMacNode = "sys/class/net/wlan0/address";
	protected String hardwareNode = "/sys/class/aml_keys/aml_keys/hw";

	@Override
	public Rom getRom() {
		if (null == rom) {
			rom = RomFactory.getInstance().getRom(RomName.TONGWEI);
		}
		return rom;
	}

	@Override
	public String getSN() {
		try {
			testThrowMyException();
			if (TextUtils.isEmpty(sn)) {
				sn = loadNode(snNode).trim();
			}
			return sn;
		} catch (Exception e) {
			// e.printStackTrace();
			Log.i(TAG, "Get sn error!");
			return Device.DEFAULT_SN;
		}
	}

	@Override
	public String getMac() {
		try {
			testThrowMyException();
			if (TextUtils.isEmpty(mac)) {
				mac = loadNode(macNode).trim();
			}
			return mac;
		} catch (Exception e) {
			// e.printStackTrace();
			Log.i(TAG, "Get mac error!");
			return getRom().getMac().trim();
		}
	}

	@Override
	public String getWlanMac() {
		try {
			testThrowMyException();
			if (TextUtils.isEmpty(wlanMac)) {
				wlanMac = loadNode(wlanMacNode).trim();
			}
			return wlanMac;
		} catch (Exception e) {
			// e.printStackTrace();
			Log.i(TAG, "Get wlanMac error!");
			return getRom().getWlanMac().trim();
		}
	}

	@Override
	public String getHardwareVersion() {
		try {
			testThrowMyException();
			if (TextUtils.isEmpty(hardwareVersion)) {
				hardwareVersion = loadNode(hardwareNode).trim();
				Log.i(TAG, "Get hardwareVersion success!");
			}
			return hardwareVersion;
		} catch (Exception e) {
			// e.printStackTrace();
			Log.i(TAG, "Get hardwareVersion error!");
			return getRom().getHardwareVersion().trim();
		}
	}

	@Override
	public String getSoftwareVersion() {
		if (TextUtils.isEmpty(softwareVersion)) {
			try {
				softwareVersion = getRom().getSoftwareVersion().trim();
			} catch (Exception e) {
				Log.i(TAG, "Get softwareVersion error!");
				return getRom().getSoftwareVersion().trim();
			}
		}
		return softwareVersion;
	}

}
