/**
 * @Title RomFactory.java
 * @Package com.hiveview.tv.common.device
 * @author haozening
 * @date 2014年7月25日 上午11:42:31
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.deviceinfo.rom;

import com.hiveview.tv.common.deviceinfo.rom.impl.ChuangWeiRom;
import com.hiveview.tv.common.deviceinfo.rom.impl.RTKRom;
import com.hiveview.tv.common.deviceinfo.rom.impl.TongWeiRom;

/**
 * Rom工厂类
 * 
 * @ClassName RomFactory
 * @Description
 * @author haozening
 * @date 2014年7月25日 上午11:42:31
 * 
 */
public class RomFactory {

	public static class FactoryHolder {
		public static RomFactory factory = new RomFactory();
	}

	public static RomFactory getInstance() {
		return FactoryHolder.factory;
	}

	public Rom getRom(RomName name) {
		switch (name) {
		case TONGWEI:
			return new TongWeiRom();
		case CHUANGWEI:
			return new ChuangWeiRom();
		case RTK:
			return new RTKRom();
		}
		return null;
	}

	public static enum RomName {
		TONGWEI, CHUANGWEI ,RTK
	}

}
