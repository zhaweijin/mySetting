/**
 * @Title DeviceAmlogicRom.java
 * @Package com.hiveview.tv.common.device.impl
 * @author haozening
 * @date 2014年7月25日 上午9:52:50
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.deviceinfo.rom.impl;

import com.hiveview.tv.common.deviceinfo.rom.Rom;

/**
 * 创维Rom，使用创维Rom的设备需要设置此Rom
 * 
 * @ClassName DeviceAmlogicRom
 * @Description
 * @author haozening
 * @date 2014年7月25日 上午9:52:50
 * 
 */
public class TongWeiRom extends Rom {

	protected String hardwareProp = "ro.product.hw.version";
	protected String softwareProp = "ro.product.firmware";

	@Override
	public String getSoftwareVersion() {
		return getProperty(softwareProp);
	}

	@Override
	public String getHardwareVersion() {
		return getProperty(hardwareProp);
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

}
