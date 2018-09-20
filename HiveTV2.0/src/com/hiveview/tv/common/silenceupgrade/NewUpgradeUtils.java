package com.hiveview.tv.common.silenceupgrade;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.hiveview.tv.service.entity.AppMarketEntity;
import com.hiveview.tv.service.net.HttpTaskManager;

public class NewUpgradeUtils {

	private static final String TAG = "UpgradeUtils";

	public static ArrayList<AppMarketEntity> checkVersion(Context context, ArrayList<AppMarketEntity> list) {

		ArrayList<AppMarketEntity> versionList = new ArrayList<AppMarketEntity>();

		int count = list.size();

		for (int i = 0; i < count; i++) {

			AppMarketEntity entity = list.get(i);
			int newVersion = Integer.valueOf(entity.getVersionNo().replace(".", ""));
			ApplicationInfo appInfo = PackageManagerUtils.getApplicationInfo(context, entity.getBundleId());
			ApplicationInfo appInfoDataDir = PackageManagerUtils.getApplicationInfoDataDir(context, entity.getBundleId());

			if (appInfo == null && appInfoDataDir == null) {
				versionList.add(entity);
			}
			String versionName = "";
			int oldVersion = 0;
			String versionNameDataDir = "";
			int oldVersionDataDir = 0;

			// data和system目录中的apk只有一个可以被android作为默认的apk打开，所以，只要有一个不为空，就可判断版本
			if (appInfo != null) {
				versionName = PackageManagerUtils.getPackageInfo(context, appInfo.sourceDir).versionName;
				oldVersion = Integer.valueOf(versionName.replace(".", ""));
				if (newVersion > oldVersion) {
					versionList.add(entity);
				}
			} else if (appInfoDataDir != null) {
				versionNameDataDir = PackageManagerUtils.getPackageInfo(context, appInfoDataDir.sourceDir).versionName;
				oldVersionDataDir = Integer.valueOf(versionNameDataDir.replace(".", ""));
				if (newVersion > oldVersionDataDir) {
					versionList.add(entity);
				}
			}

		}

		return versionList;
	}

	/**
	 * 获取最新的launcher版本对比本地版本
	 * 
	 * @Title: UpgradeUtils
	 * @author:郭松胜
	 * @Description: TODO
	 * @param context
	 * @param launcherEntity
	 * @return
	 */
	public static boolean isLatestLauncher(Context context, AppMarketEntity launcherEntity) {

		try {
			int newVersion = Integer.valueOf(launcherEntity.getVersionNo().replace(".", ""));
			ApplicationInfo appInfo = PackageManagerUtils.getApplicationInfo(context, "com.hiveview.tv");
			String versionName = PackageManagerUtils.getPackageInfo(context, appInfo.sourceDir).versionName;
			int oldVersion = Integer.valueOf(versionName.replace(".", ""));
			if (newVersion > oldVersion) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			return true;
		}

	}

	public static int deleteAppWithBundleID(Context context, String packageName) throws IOException, InterruptedException {
		ApplicationInfo info = PackageManagerUtils.getApplicationInfo(context, packageName);
		Process process = null;
		if (info == null)
			return -1;
		process = Runtime.getRuntime().exec("/system/xbin/su");
		DataOutputStream os = new DataOutputStream(process.getOutputStream());
		os.writeBytes("adb remount \n");
		os.writeBytes("busybox rm " + info.sourceDir + " \n");
		os.writeBytes("sync \n");
		os.writeBytes("exit \n");
		int code = process.waitFor();
		Log.e("msg", "code = " + code);
		return code;
	}

	public static int push(final File file) throws IOException, InterruptedException {
		Process process = null;
		process = Runtime.getRuntime().exec("/system/xbin/su");
		DataOutputStream os = new DataOutputStream(process.getOutputStream());
		os.writeBytes("adb remount \n");
		os.writeBytes("umask 0133 \n");
		os.writeBytes("busybox mv " + file.getAbsolutePath() + " /system/app \n");
		os.writeBytes("sync \n");
		os.writeBytes("exit \n");
		int code = process.waitFor();
		Log.e("msg", "code = " + code);
		return code;
	}

	public static void install_back(final File file) throws Exception {
		Log.d(TAG, "install");
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		Log.d(TAG, "install run");
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("adb install -r" + file.getAbsolutePath());
			// DataOutputStream os = new
			// DataOutputStream(process.getOutputStream());
			// os.writeBytes("sync \n");
			// os.writeBytes("exit \n");

			BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String strerror = null;
			while ((strerror = error.readLine()) != null) {
				Log.d(TAG, "error.readLine() : " + strerror);
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String str = null;
			Log.d(TAG, "start read");
			while ((str = reader.readLine()) != null) {
				Log.d(TAG, "reader.readLine() : " + str);
			}
			Log.d(TAG, "end read");
			Log.d(TAG, "start read");
			// process.destroy();
			// os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// }
		// }).start();
	}

	/**
	 * 安装
	 * @Title: NewUpgradeUtils
	 * @author:郭松胜
	 * @Description: TODO
	 * @param filePath
	 */
	public static void install(String filePath) {
		Log.i(TAG, "install............");
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("adb install -r " + filePath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String str = null;
			while ((str = reader.readLine()) != null) {
				Log.d(TAG, "reader.readLine() ......................................:" + str);
			}

			BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String strerror = null;
			while ((strerror = error.readLine()) != null) {
				Log.d(TAG, "error.readLine() .................................: " + strerror);
			}
			reader.close();
			error.close();
			process.destroy();
			Log.i(TAG, "install............end");
		} catch (Exception e) {
			Log.i(TAG, "installError............"+ e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 重启
	 * @Title: NewUpgradeUtils
	 * @author:郭松胜
	 * @Description: TODO
	 */
	public static void reBoot() {
		HttpTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				try {
					String reboot = "adb reboot";
					Process p = Runtime.getRuntime().exec(reboot);

					if (p.exitValue() != 0)
						p.destroy();

				} catch (IOException e) {

				}
			}
		});
	}
	
	/**
	 * 安装完毕后删除下载的文件  目前使用命令，不建议
	 * @Title: NewUpgradeUtils
	 * @author:郭松胜
	 * @Description: TODO
	 * @param context
	 * @param filePath
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void deleteApp(Context context, String filePath) throws IOException, InterruptedException {
		try {
			if (null != filePath) {
				Process process = null;
				process = Runtime.getRuntime().exec("rm " + filePath);
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String str = null;
				while ((str = reader.readLine()) != null) {
					Log.d(TAG, "reader.readLine() : " + str);
				}

				BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String strerror = null;
				while ((strerror = error.readLine()) != null) {
					Log.d(TAG, "error.readLine() : " + strerror);
				}
				reader.close();
				error.close();
				process.destroy();
			}
		} catch (Exception e) {
			Log.i(TAG, "deleteApp............error:" + filePath + ",e:" + e.toString());
		}
	}

}
