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
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.hiveview.manager.PmInstallManager;
import com.hiveview.manager.IPmInstallObserver;
import com.hiveview.tv.R;
import com.hiveview.tv.common.deviceinfo.device.Device;
import com.hiveview.tv.common.deviceinfo.device.Device.DeviceVersion;
import com.hiveview.tv.common.deviceinfo.device.DeviceFactory;
import com.hiveview.tv.common.silenceupgrade.Loader.LoaderListener;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.AppMarketEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.utils.FileUtils;
import com.hiveview.tv.utils.MD5Utils;
import com.hiveview.tv.utils.ToastUtils;

/**
 * 静默升级
 * 
 * @ClassName: NewUpgrader
 * @Description: TODO
 * @author: guosongsheng
 * @date 2014年9月17日 上午9:58:57
 * 
 */
@SuppressLint("DefaultLocale")
public class NewUpgrader {
	/**
	 * TAG
	 */
	private static final String TAG = "NewUpgrader";
	/**
	 * 上下文
	 */
	private static Context mContext;
	/**
	 * 获取应用列表
	 */
	private ArrayList<AppMarketEntity> appMarketList = null;
	/**
	 * launcher包名
	 */
	private static String TV_PACKAGE_NAME = "com.hiveview.tv";
	/**
	 * 安装应用
	 */
	protected static final int MSG_VERSION_DIALOG = 492;
	/**
	 * 升级计数器
	 */
	private int upgradeCount;
	/**
	 * 升级launcher实体
	 */
	private static AppMarketEntity launcherEntity;
	/**
	 * 需要升级应用的集合
	 */
	private ArrayList<AppMarketEntity> upgradeAppList;

	private static PmInstallManager pmManager;

	/**
	 * 检查 组件 升级
	 * 
	 * @Title: NewUpgrader
	 * @author:郭松胜
	 * @Description: TODO
	 * @param ctx
	 */
	public void upgradeApp(Context ctx) {
		mContext = ctx;
		try {
			final UpgradeHandler handler = new UpgradeHandler(mContext, this);
			// 检测是否升级
			HttpTaskManager.getInstance().submit(new Runnable() {
				@Override
				public void run() {

					try {
						Log.d(TAG, "upgradeApp==>0");
						// 应用升级空间是否充足
						if (!FileUtils.hasSpace(mContext)) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									Log.d(TAG, "upgradeApp==>1");
									ToastUtils.alert(
											mContext,
											mContext.getResources().getString(
													R.string.alert_no_space));
									Log.d(TAG, "upgradeApp==>2");
								}
							});
							return;
						}
						Log.d(TAG, "upgradeApp==>3");
						// 加载设备详情获取设备类型、设备的rom版本
						Device device = DeviceFactory.getInstance().getDevice();
						// device.initDeviceInfo(mContext);
						String deviceSoftwareVersion = "";
						// 获取应用类表
						if (!TextUtils.isEmpty(device.getSoftwareVersion())) {
							deviceSoftwareVersion = device.getSoftwareVersion();
							deviceSoftwareVersion = deviceSoftwareVersion
									.replace(".", "");
						}
						Log.d(TAG, "upgradeApp==>4");
						appMarketList = new HiveTVService().upgrader(
								device.getModel(), deviceSoftwareVersion,
								device.getMac(), device.getSN());
						Log.d(TAG, "upgradeApp==>5");
						Log.i(TAG, "appMarketList..........................:"
								+ appMarketList.size());
						if (null != appMarketList && appMarketList.size() > 0) {
							for (AppMarketEntity appMarkEntity : appMarketList) {
								// 获取的包名不等于空并且等launcher的包名
								if ((!TextUtils.isEmpty(appMarkEntity
										.getBundleId()) && appMarkEntity
										.getBundleId().equals(TV_PACKAGE_NAME))) {
									launcherEntity = appMarkEntity;
									Log.d(TAG, "upgradeApp==>6");
									break;
								}
							}
						} else {
							Log.d(TAG, "upgradeApp==>7");
							return;
						}
						Log.d(TAG, "upgradeApp==>8");
						// 检查版本号 获取需要升级的应用
						upgradeAppList = NewUpgradeUtils.checkVersion(mContext,
								appMarketList);
						Log.d(TAG, "upgradeApp==>9");
						// 发送消息安装应用
						handler.sendEmptyMessage(MSG_VERSION_DIALOG);
						Log.d(TAG, "upgradeApp==>10");
					} catch (ServiceException e) {
						Log.d(TAG,
								"upgradeApp-->ServiceException e:"
										+ e.toString());
						e.printStackTrace();
					}

				}
			});
		} catch (Exception e) {
			Log.d(TAG, "upgradeApp-->Exception e:" + e.toString());
		}

	}

	/**
	 * Handler
	 * 
	 * @ClassName: UpgradeHandler
	 * @Description: TODO
	 * @author: guosongsheng
	 * @date 2014年9月17日 上午10:04:41
	 * 
	 */
	private static class UpgradeHandler extends Handler {
		private WeakReference<Context> contextRef;
		private NewUpgrader upgraderRef;

		public UpgradeHandler(Context context, NewUpgrader upgrader) {
			contextRef = new WeakReference<Context>(context);
			upgraderRef = upgrader;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Context context = contextRef.get();
			Log.i(TAG, "UpgradeHandler..................");
			if (null == contextRef || null == upgraderRef) {
				return;
			}
			if (msg.what == MSG_VERSION_DIALOG) {
				boolean isLatest = false;
				// launcher是否需要升级
				if (upgraderRef.launcherEntity != null) {
					isLatest = NewUpgradeUtils.isLatestLauncher(context,
							upgraderRef.launcherEntity);
				}

				// launcher需要升级 并且launcherEntity不为空
				if (isLatest || upgraderRef.launcherEntity == null) {
					upgraderRef.loadApp();
					return;
				}
				if (upgraderRef.launcherEntity != null) {
					// 只升级系统应用
					upgraderRef.loadApp();
				}
			}
		}
	}

	
	 String mModel = null;
	/**
	 * 下载app
	 * 
	 * @Title: NewUpgrader
	 * @author:郭松胜
	 * @Description: TODO
	 */
	private void loadApp() {
		try {
			Device device = DeviceFactory.getInstance().getDevice();
			// device.initDeviceInfo(this);
			DeviceVersion version = device.getDeviceVersion();
			mModel = device.getModel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (final AppMarketEntity entity : upgradeAppList) {

			Loader loader = new FileLoader(entity, new LoaderListener() {

				@Override
				public void onStart(AppMarketEntity entity) {

				}

				@Override
				public void onError(String message, int code) {
					upgradeCount++;
					if (upgradeCount == upgradeAppList.size()
							|| upgradeCount > upgradeAppList.size()) {
						Log.i(TAG, "onError.........................");
						if (mModel != null) {
							installApp(mModel, upgradeAppList);
						}
					}
				}

				@Override
				public void onFailure(String message, int code) {
					upgradeCount++;
					if (upgradeCount == upgradeAppList.size()
							|| upgradeCount > upgradeAppList.size()) {
						Log.i(TAG, "onFailure.........................");
						if (mModel != null) {
							installApp(mModel, upgradeAppList);
						}
					}
				}

				@Override
				public void onComplete(String message) {
					upgradeCount++;
					if (upgradeCount == upgradeAppList.size()
							|| upgradeCount > upgradeAppList.size()) {
						Log.i(TAG, "onComplete.........................");
						if (mModel != null) {
							installApp(mModel, upgradeAppList);
						}
					}
				}
			});

			HttpTaskManager.getInstance().submit(new Thread(loader));

		}
	}

	/***
	 * @Title: AppUtil
	 * @author:maliang
	 * @Description: TODO 根据盒子的model采取不同应用安装逻辑
	 * @param filePath
	 * @param entity
	 * @param isFromUpdate
	 * @param model
	 */
	public void installApp(String model, ArrayList<AppMarketEntity> appList) {
		Log.i(TAG, "installApp model=" + model);
		if (!TextUtils.isEmpty(model)
				&& model.length() >= 5 && "1".equals("" + model.charAt(4))) {
			installAppNew(appList);
		} else {
			installAppLast(appList);
		}
	}

	/***
	 * @Title: AppUtil
	 * @author:maliang
	 * @Description: TODO DM1016 DB1016 DM2116 DB2116 DM2116US 这些型号盒子的安装方式
	 * @param filePath
	 *            下载的apk所在的路径，即为安装路径
	 * @param entity
	 *            下载的apk的信息类
	 * @return
	 */
	public static void installAppNew(ArrayList<AppMarketEntity> appList) {
		Log.v(TAG, "installAppNew");
		pmManager = PmInstallManager.getPmInstallManager();
	
		try {
		for (AppMarketEntity entity : appList) {
			String fileName = "";
			fileName = entity.getVersionUrl().substring(
					entity.getVersionUrl().lastIndexOf("/") + 1);
			Log.i(TAG, "installApp........name:" + fileName);
			File file = new File(FileLoadUtils.getLoadExternalDir()
					+ fileName);
		
			pmManager.pmInstall(file.getAbsolutePath(), new InstallObserver(file.getAbsolutePath()));
		}
		
		if (launcherEntity != null) {
			Log.d(TAG, "launcherEntity != null");
			String fileName = "";
			fileName = launcherEntity.getVersionUrl().substring(
					launcherEntity.getVersionUrl().lastIndexOf("/") + 1);
			Log.i(TAG, "installApp........name:" + fileName);
			File file = new File(FileLoadUtils.getLoadExternalDir()
					+ fileName);
			pmManager.pmInstall(file.getAbsolutePath(), new InstallObserver(file.getAbsolutePath()));
		}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pmManager=null;
		}
	}

	public static class InstallObserver extends IPmInstallObserver.Stub {

		private String filePath;
		
		public InstallObserver(String absolutePath) {
			// TODO Auto-generated constructor stub
			filePath=absolutePath;
		}

		@Override
		public void packageInstalled(String arg0, int arg1)
				throws RemoteException {
			if (arg1 == PmInstallManager.PM_INSTALL_SUCCEEDED) {
				Log.e(TAG, "install success");
				Log.i(TAG, "installApp arg0=" + arg0);
				Log.i(TAG, "installApp arg1" + arg1);
				if (PmInstallManager.PM_INSTALL_SUCCEEDED == arg1) {
					Log.i(TAG, "install success.............");
					try {
						NewUpgradeUtils.deleteApp(mContext,filePath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				} 
			} 
		}
	};


	public void installAppLast(ArrayList<AppMarketEntity> appList) {
		try {
			Log.v(TAG, "installAppLast");
			for (AppMarketEntity entity : appList) {
				String fileName = "";
				fileName = entity.getVersionUrl().substring(
						entity.getVersionUrl().lastIndexOf("/") + 1);
				Log.i(TAG, "installApp........name:" + fileName);
				File file = new File(FileLoadUtils.getLoadExternalDir()
						+ fileName);
				String md5Value = null;
				/* start by guosongsheng 修改加密的参数 */
				md5Value = MD5Utils.getFileMD5String(file);
				// MD5Utils.getMD5String(fileName);
				/* end by guosongsheng */
				if (entity.getMd5().toLowerCase().equals(md5Value.toLowerCase())) {
					try {
						NewUpgradeUtils.install(file.getAbsolutePath());
					} catch (Exception e) {
						NewUpgradeUtils.install(file.getAbsolutePath());
						e.printStackTrace();
					}
				}
				NewUpgradeUtils.deleteApp(mContext,
						FileLoadUtils.getLoadExternalDir() + fileName);
			}

			if (launcherEntity != null) {
				Log.d(TAG, "launcherEntity != null");
				String fileName = "";
				fileName = launcherEntity.getVersionUrl().substring(
						launcherEntity.getVersionUrl().lastIndexOf("/") + 1);
				Log.i(TAG, "installApp........name:" + fileName);
				File file = new File(FileLoadUtils.getLoadExternalDir()
						+ fileName);
				String md5Value = null;
				/* start by guosongsheng 修改加密的参数 */
				md5Value = MD5Utils.getFileMD5String(file);
				// MD5Utils.getMD5String(fileName);
				/* end by guosongsheng */
				long time = System.currentTimeMillis();
				if (launcherEntity.getMd5().equals(md5Value)) {
					try {
						Log.i(TAG, "launcherEntity.............install:");
						NewUpgradeUtils.install(file.getAbsolutePath());
					} catch (Exception e) {
						Log.i(TAG,
								"launcherEntity.............install Exception:");
						Log.d(TAG, "Exception");
						NewUpgradeUtils.install(file.getAbsolutePath());
					}
					Log.i(TAG, "launcherEntity.............install time:"
							+ (System.currentTimeMillis() - time));
					// NewUpgradeUtils.reBoot();
					NewUpgradeUtils.deleteApp(mContext,
							FileLoadUtils.getLoadExternalDir() + fileName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
