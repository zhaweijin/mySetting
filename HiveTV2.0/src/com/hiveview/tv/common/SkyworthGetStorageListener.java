package com.hiveview.tv.common;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

/**
 * 创维盒子获取存储空间使用信息
 * 
 * @ClassName: SkyworthGetStorageListener
 * @Description: TODO
 * @author: 陈丽晓
 * @date 2014-7-6 下午2:32:27
 * 
 */
public class SkyworthGetStorageListener implements GetStorageListener {

	@Override
	public Object[] getStorageInfo(Context context) {
		Object[] objects = new Object[2];
		File file = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(file.getPath());
		int mBlockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		long availiableBlocks = stat.getAvailableBlocks();
		long usedBlocks = formatTotalSize2Value(totalBlocks, mBlockSize) / mBlockSize - availiableBlocks;
		String usedSize = Formatter.formatFileSize(context, usedBlocks * mBlockSize);
		long gB = 1024 * 1024 * 1024;
		objects[0] = usedSize + "/" + formatTotalSize2Value(totalBlocks, mBlockSize) / gB + " GB";
		objects[1] = (int) ((usedBlocks * mBlockSize * 1.0f / formatTotalSize2Value(totalBlocks, mBlockSize) * 1.0f) * 100);
		return objects;
	}

	/**
	 * 检测盒子存储设备的版本（4G,8G,16G）
	 * 
	 * @Title: SkyworthGetStorageListener
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param totalSize
	 * @param mBlockSize
	 * @return
	 */
	private long formatTotalSize2Value(long totalSize, int mBlockSize) {
		long result = 0;
		long gB = 1024 * 1024 * 1024;
		long totalValue = totalSize * mBlockSize;
		if (totalValue > (8 * gB)) {
			result = 16 * gB;
		} else if (totalValue > (4 * gB)) {
			result = 8 * gB;
		} else {
			result = 4 * gB;
		}
		return result;
	}
}
