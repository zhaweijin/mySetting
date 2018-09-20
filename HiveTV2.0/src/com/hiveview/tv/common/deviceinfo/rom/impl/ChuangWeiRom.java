/**
 * @Title AbsDeviceMiptRom.java
 * @Package com.hiveview.tv.common.device.impl
 * @author haozening
 * @date 2014年7月25日 上午10:07:20
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.deviceinfo.rom.impl;

import com.hiveview.tv.common.deviceinfo.rom.Rom;

/**
 * 创维Rom，使用创维Rom的设备需要设置此Rom
 * 
 * @ClassName AbsDeviceMiptRom
 * @Description
 * @author haozening
 * @date 2014年7月25日 上午10:07:20
 * 
 */
public class ChuangWeiRom extends Rom {

	protected String hardwareProp = "persist.sys.hwconfig.hw_ver";
	protected String softwareProp = "ro.product.firmware";
	protected String softwareProp_pre = "persist.sys.hwconfig.soft_ver";

	@Override
	public String getHardwareVersion() {
		return getProperty(hardwareProp);
	}

	@Override
	public String getSoftwareVersion() {
		String sortware = getProperty(softwareProp);
		if (null == sortware || "".equals(sortware)) {
			sortware = getProperty(softwareProp_pre);
		}
		return sortware;
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
