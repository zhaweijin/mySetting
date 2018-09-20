/**
 * @Title DeviceChecker.java
 * @Package com.hiveview.tv.common.devicecheck
 * @author haozening
 * @date 2014年6月20日 下午3:41:45
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.silenceupgrade;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.silenceupgrade.Loader.LoaderListener;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.AppMarketEntity;
import com.hiveview.tv.service.entity.SYSUpDataEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.FileUtils;
import com.hiveview.tv.utils.MD5Utils;
import com.hiveview.tv.utils.ToastUtils;
import com.hiveview.tv.view.UpgradeLauncherDialog;


/**
 * @ClassName DeviceChecker
 * @Description 
 * @author haozening
 * @date 2014年6月20日 下午3:41:45
 * 
 */
public class Upgrader {
	protected static final int MSG_VERSION_DIALOG = 492;

	private static final String TAG = "Upgrader";
	
	private int upgradeCount;
	private SYSUpDataEntity launcherEntity;
	private ArrayList<AppMarketEntity> upgradeAppList;
	private Context context;
	
	/** 检查 组件 升级 */
	public void upgradeApp(Context ctx) {
		context = ctx;
		final UpgradeHandler handler = new UpgradeHandler(context, this);
		// 检测是否升级
		HttpTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {

				try {
					//应用升级空间是否充足
					if (!FileUtils.hasSpace(context)) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								ToastUtils.alert(context, context.getResources().getString(R.string.alert_no_space));
							}
						});
						return;
					}

					try {
						//获取launcher版本
						PackageManager packageManager = context.getPackageManager();
						PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
						String version = packInfo.versionName;
						launcherEntity = new HiveTVService().getLauncherVersionInfo(version);
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
					//获取升级应用列表
					ArrayList<AppMarketEntity> appList = new HiveTVService().getSystemAppList(AppUtil.getVersionName(context));
					//检查版本号 获取需要升级的应用
					upgradeAppList = UpgradeUtils.checkVersion(context, appList);

					handler.sendEmptyMessage(MSG_VERSION_DIALOG);

				} catch (ServiceException e) {
					Log.e(TAG, "upgradeApp-->ServiceException e:"+e.toString());
					e.printStackTrace();
				}

			}
		});
	}
	
	private static class UpgradeHandler extends Handler{
		private WeakReference<Context> contextRef;
		private WeakReference<Upgrader> upgraderRef;
		
		public UpgradeHandler(Context context, Upgrader upgrader) {
			contextRef = new WeakReference<Context>(context);
			upgraderRef = new WeakReference<Upgrader>(upgrader);
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Context context = contextRef.get();
			Upgrader upgrader = upgraderRef.get();
			if (null == context || null == upgrader) {
				return;
			}
			if (msg.what == MSG_VERSION_DIALOG) {
				boolean isLatest = false;
				//launcher需要升级
				if (upgrader.launcherEntity != null) {
					isLatest = UpgradeUtils.isLatestLauncher(context, upgrader.launcherEntity);
				}
				
				if (isLatest || upgrader.launcherEntity == null) {
					upgrader.loadApp();
					return;
				}
				if (upgrader.launcherEntity != null) {
					if (upgrader.launcherEntity.getType() == AppConstant.NO_0) {
						upgrader.loadApp();
					} else {
						UpgradeLauncherDialog upgradeLauncherDialog = new UpgradeLauncherDialog(context, R.style.dialog);
						upgradeLauncherDialog.setLauncherData(upgrader.launcherEntity);
		
						if (upgrader.launcherEntity.getType() == AppConstant.NO_1) {
							upgradeLauncherDialog.setOptionalModel(true);
						} else if (upgrader.launcherEntity.getType() == AppConstant.NO_2) {
							upgradeLauncherDialog.setOptionalModel(false);
						}
						upgradeLauncherDialog.show();
					}
				}
			}
		}
	}
	
	private void loadApp() {

		for (final AppMarketEntity entity : upgradeAppList) {

			Loader loader = new FileLoader(entity, new LoaderListener() {

				@Override
				public void onStart(AppMarketEntity entity) {

				}

				@Override
				public void onError(String message, int code) {
					upgradeCount++;
					if (upgradeCount == upgradeAppList.size() || upgradeCount > upgradeAppList.size()) {
						executeInstall(upgradeAppList);
					}
				}

				@Override
				public void onFailure(String message, int code) {
					upgradeCount++;
					if (upgradeCount == upgradeAppList.size() || upgradeCount > upgradeAppList.size()) {
						executeInstall(upgradeAppList);
					}
				}

				@Override
				public void onComplete(String message) {
					upgradeCount++;
					if (upgradeCount == upgradeAppList.size() || upgradeCount > upgradeAppList.size()) {
						executeInstall(upgradeAppList);
					}
				}
			});

			HttpTaskManager.getInstance().submit(new Thread(loader));

		}
	}
	

	private void executeInstall(ArrayList<AppMarketEntity> appList) {

		Log.d(TAG, "executeInstall()");
		for (AppMarketEntity entity : appList) {
			Log.d(TAG, "executeInstall()->for()");
			String fileName = entity.getVersionUrl().substring(entity.getVersionUrl().lastIndexOf("/") + 1);
			File file = new File(FileLoadUtils.getLoadExternalDir() + fileName);
			String md5Value = null;
			try {
				Log.d(TAG, "MD5Utils.getFileMD5String(file)");
				md5Value = MD5Utils.getFileMD5String(file);
			} catch (IOException e) {
				md5Value = "";
			}

			Log.d(TAG, "check md5");
			if (entity.getMd5().equals(md5Value)) {
				Log.d(TAG, "md5 equal");
				try {
					Log.d(TAG, "deleteAppWithBundleID");
					ApplicationInfo info = PackageManagerUtils.getApplicationInfo(context, entity.getBundleId());
					/* start by haozening */
					int code = 0;
					if (null != info && null != info.sourceDir && new File(info.sourceDir).exists()) {
						code = UpgradeUtils.deleteAppWithBundleID(context, entity.getBundleId());
						if (code != 0) { // 删除失败直接进行install
							UpgradeUtils.install(file);
							return;
						}
					}
					/* end by haozening */
					Log.d(TAG, "push");
					code = UpgradeUtils.push(file);
					if (code != 0) {
						UpgradeUtils.install(file);
					}
					Log.d(TAG, "after push");
				} catch (IOException e) {
					Log.d(TAG, "IOException");
					UpgradeUtils.install(file);
				} catch (InterruptedException e) {
					Log.d(TAG, "InterruptedException");
					UpgradeUtils.install(file);
				} catch (Exception e) {
					Log.d(TAG, "Exception");
					UpgradeUtils.install(file);
				}
			}
		}

		if (launcherEntity != null) {
			Log.d(TAG, "launcherEntity != null");
			String fileName = launcherEntity.getUrl().substring(launcherEntity.getUrl().lastIndexOf("/") + 1);
			File file = new File(FileLoadUtils.getLoadExternalDir() + fileName);
			String md5Value = null;
			try {
				Log.d(TAG, "MD5Utils.getFileMD5String(file)");
				md5Value = MD5Utils.getFileMD5String(file);
			} catch (IOException e) {
				md5Value = "";
			}

			Log.d(TAG, "check md5");
			if (launcherEntity.getMd5().equals(md5Value)) {
				Log.d(TAG, "md5 equal");
				try {
					Log.d(TAG, "push");
					int code = UpgradeUtils.push(file);
					if (code != 0) {
						UpgradeUtils.install(file);
					}
					UpgradeUtils.deleteAppWithBundleID(context, "com.hiveview.domybox");
				} catch (IOException e) {
					Log.d(TAG, "IOException");
					UpgradeUtils.install(file);
				} catch (InterruptedException e) {
					Log.d(TAG, "InterruptedException");
					UpgradeUtils.install(file);
				} catch (Exception e) {
					Log.d(TAG, "Exception");
					UpgradeUtils.install(file);
				}
				UpgradeUtils.reBoot();
			}

		}

	}
}
