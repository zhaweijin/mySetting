package com.hiveview.tv.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

public class FileUtils {
	
	public static StatFs statfsROM = new StatFs(Environment.getDataDirectory().getAbsolutePath());
	public static StatFs statfsSD = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());

	// 获取可用空间大小
	public static boolean hasSpace(Context context) {
		long blocksRom = statfsROM.getAvailableBlocks();
		long sizeRom = statfsROM.getBlockSize();
		long totalRom = blocksRom * sizeRom;
		double romAvailableSize = StringUtils.getM(totalRom);

		long blocksSD = statfsSD.getAvailableBlocks();
		long sizeSD = statfsSD.getBlockSize();
		long totalSD = blocksSD * sizeSD;
		double sdAvailableSize = StringUtils.getM(totalSD);

		return romAvailableSize > 150 && sdAvailableSize > 150 ? true : false;
	}

}
