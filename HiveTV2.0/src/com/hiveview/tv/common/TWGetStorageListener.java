package com.hiveview.tv.common;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

/**
 * 同维盒子获取存储空间使用信息
 * 
 * @ClassName: TWGetStorageListener
 * @Description: TODO
 * @author: 陈丽晓
 * @date 2014-7-6 下午2:30:58
 * 
 */
public class TWGetStorageListener implements GetStorageListener {

	@Override
	public Object[] getStorageInfo(Context context) {
		Object[] objects = new Object[2];
		// 获取data分区存储数据
		File pathData = Environment.getDataDirectory();
		StatFs statData = new StatFs(pathData.getPath());
		long blockSizeData = statData.getBlockSize();
		long totalBlocksData = statData.getBlockCount();
		long availableBlocksData = statData.getAvailableBlocks();
		// 获取system分区存储数据
		File pathSystem = Environment.getRootDirectory();
		StatFs statSystem = new StatFs(pathSystem.getPath());
		long blockSizeSystem = statSystem.getBlockSize();
		long totalBlocksSystem = statSystem.getBlockCount();
		long availableBlocksSystem = statSystem.getAvailableBlocks();
		// 获取cache分区存储数据
		File pathCache = Environment.getDownloadCacheDirectory();
		StatFs statCache = new StatFs(pathCache.getPath());
		long blockSizeCache = statCache.getBlockSize();
		long totalBlocksCache = statCache.getBlockCount();
		long availableBlocksCache = statCache.getAvailableBlocks();
		// 计算存储数值
		long totalMemory = formatTotalSize2Value(totalBlocksData * blockSizeData + totalBlocksSystem * blockSizeSystem + totalBlocksCache
				* blockSizeCache);
		long usedMemory = totalMemory - (availableBlocksData * blockSizeData);
		String usedSize = Formatter.formatShortFileSize(context, usedMemory);
		String totalSize = Formatter.formatShortFileSize(context, totalMemory);
		objects[0] = usedSize + "/" + totalSize;
		objects[1] = (int) ((usedMemory * 1.0f / totalMemory * 1.0f) * 100);
		return objects;
	}

	/**
	 * 检测盒子存储设备的版本（4G,8G,16G）
	 * 
	 * @Title: TWGetStorageListener
	 * @author:yupengtong
	 * @Description: 圆整存储总量
	 * @param totalSize
	 * @return
	 */
	private long formatTotalSize2Value(long totalSize) {
		long result = 0;
		long gB = 1024 * 1024 * 1024;
		long totalValue = totalSize;
		if (totalValue > 16 * gB) {
			return result;
		} else if (totalValue > (8 * gB)) {
			result = 16 * gB;
		} else if (totalValue > (4 * gB)) {
			result = 8 * gB;
		} else if (totalValue < (4 * gB)) {
			result = 4 * gB;
		}
		return result;
	}
}
