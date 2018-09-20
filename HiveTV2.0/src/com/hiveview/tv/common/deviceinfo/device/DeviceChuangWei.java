/**
 * @Title ChuangWeiDevice.java
 * @Package com.hiveview.tv.common.deviceinfo.device.impl
 * @author haozening
 * @date 2014年7月25日 下午12:05:15
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
 * 创维盒子，具体型号在impl包中
 * 
 * @ClassName ChuangWeiDevice
 * @Description
 * @author haozening
 * @date 2014年7月25日 下午12:05:15
 * 
 */
public  class DeviceChuangWei extends Device {
	/**
	 * TAG
	 */
	private static final String TAG = "DeviceChuangWei";
	protected String snNode = "/sys/class/mipt_hwconfig/customsn";
	protected String macNode = "/sys/class/net/eth0/address";
	protected String wlanMacNode = "sys/class/net/wlan0/address";

	@Override
	public Rom getRom() {
		if (null == rom) {
			rom = RomFactory.getInstance().getRom(RomName.CHUANGWEI);
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
			if (TextUtils.isEmpty(hardwareVersion)) {
				hardwareVersion = getRom().getHardwareVersion().trim();
				Log.i(TAG, "Get hardwareVersion success!");
			}
		} catch (Exception e) {
			Log.i(TAG, "Get hardwareVersion error!");
			return getRom().getHardwareVersion().trim();
		}

		return hardwareVersion;
	}

	@Override
	public String getSoftwareVersion() {
		try {
			if (TextUtils.isEmpty(softwareVersion)) {
				softwareVersion = getRom().getSoftwareVersion().trim();
			}
		} catch (Exception e) {
			Log.i(TAG, "Get softwareVersion error!");
			return getRom().getSoftwareVersion().trim();
		}
		return softwareVersion;
	}

}
