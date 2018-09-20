/**
 * @Title TestDeviceInfo.java
 * @Package com.hiveview.tv.common.device
 * @author haozening
 * @date 2014年7月25日 上午10:47:07
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.deviceinfo;

import java.io.File;

import android.content.Intent;
import android.os.Environment;
import android.os.StatFs;
import android.test.AndroidTestCase;
import android.util.Log;

import com.hiveview.tv.common.deviceinfo.device.Device;
import com.hiveview.tv.common.deviceinfo.device.DeviceFactory;

/**
 * @ClassName TestDeviceInfo
 * @Description
 * @author haozening
 * @date 2014年7月25日 上午10:47:07
 * 
 */
public class TestDeviceInfo extends AndroidTestCase {

	private static final String TAG = "TestDeviceInfo";

	public void test() {
		Device device = DeviceFactory.getInstance().getDevice();
		Log.d(TAG, "DeviceName : " + device.getDeviceName());
		Log.d(TAG, "HardwareVersion : " + device.getHardwareVersion());
		Log.d(TAG, "Mac : " + device.getMac());
		Log.d(TAG, "SN : " + device.getSN());
		Log.d(TAG, "SoftwareVersion : " + device.getSoftwareVersion());
		Log.d(TAG, "DeviceVersion" + device.getDeviceVersion());

	}

	// 看下读取sd卡的：

	public void readSDCard() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();
			long blockCount = sf.getBlockCount();
			long availCount = sf.getAvailableBlocks();
			Log.d(TAG, "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB");
			Log.d(TAG, "可用的block数目：:" + availCount + ",剩余空间:" + availCount * blockSize / 1024 + "KB");
		}
	}

	// data
	public void readData() {

		File sdcardDir = Environment.getDataDirectory();
		StatFs sf = new StatFs(sdcardDir.getPath());
		long blockSize = sf.getBlockSize();
		long blockCount = sf.getBlockCount();
		long availCount = sf.getAvailableBlocks();
		Log.d(TAG, "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB");
		Log.d(TAG, "可用的block数目：:" + availCount + ",剩余空间:" + availCount * blockSize / 1024 + "KB");

	}

	// cache
	public void readCache() {

		File sdcardDir = Environment.getDownloadCacheDirectory();
		StatFs sf = new StatFs(sdcardDir.getPath());
		long blockSize = sf.getBlockSize();
		long blockCount = sf.getBlockCount();
		long availCount = sf.getAvailableBlocks();
		Log.d(TAG, "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB");
		Log.d(TAG, "可用的block数目：:" + availCount + ",剩余空间:" + availCount * blockSize / 1024 + "KB");

	}

	// 然后看下读取系统内部空间的：

	public void readSystem() {
		File root = Environment.getRootDirectory();
		StatFs sf = new StatFs(root.getPath());
		long blockSize = sf.getBlockSize();
		long blockCount = sf.getBlockCount();
		long availCount = sf.getAvailableBlocks();
		Log.d(TAG, "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB");
		Log.d(TAG, "可用的block数目：:" + availCount + ",可用大小:" + availCount * blockSize / 1024 + "KB");
	}

	public void readTest() {
		File root = Environment.getInternalStorageDirectory();
		StatFs sf = new StatFs(root.getPath());
		long blockSize = sf.getBlockSize();
		long blockCount = sf.getBlockCount();
		long availCount = sf.getAvailableBlocks();
		Log.d(TAG, "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB");
		Log.d(TAG, "可用的block数目：:" + availCount + ",可用大小:" + availCount * blockSize / 1024 + "KB");
	}

	

}
