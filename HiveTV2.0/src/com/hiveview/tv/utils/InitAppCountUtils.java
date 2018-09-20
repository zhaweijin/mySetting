package com.hiveview.tv.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class InitAppCountUtils {

	private static final String TAG = "InitAppCountUtils";
	private Context mContext;
	private ArrayList<String> packageFilterList;
	private ArrayList<String> uninstallFilterList;
	public static final String TV_ONLIVE = "com.hiveview.tv.onlive";

	public InitAppCountUtils(Context context) {
		mContext = context;
	}

	/**
	 * 获取当前系统安装的应用列表,以List集合的形式返回(不包括游戏，包括用户从外接存储中安装的应用)
	 * 
	 * @param context
	 * @return
	 */
	@SuppressLint("NewApi")
	public List<String> getInstallAppsByListExceptGame(Context context) {

		initPakcageFilterList();
		initUninstallFilterList();

		List<String> list = new ArrayList<String>();
		HashMap<String, String> mapGameInstall = getInstalledGameCount();
		List<String> listGameInstall = new ArrayList<String>();
		// 得到PackageManager对象
		PackageManager pm = context.getPackageManager();
		// 得到系统安装的所有程序包的PackageInfo对象
		List<PackageInfo> packs = pm.getInstalledPackages(0);

		for (PackageInfo pi : packs) {
			if ((packageFilterList.indexOf(pi.applicationInfo.packageName) == -1)
					&& (((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && (pi.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0) || (uninstallFilterList
							.indexOf(pi.applicationInfo.packageName) > -1))) {
				list.add(pi.applicationInfo.packageName);
			}
		}


		int size = list.size();
		for (int i = 0; i < size; i++) {
			String packageName = list.get(i);
			if (mapGameInstall.get(packageName) != null) {
				listGameInstall.add(packageName);
			}
		}
		
		if (mapGameInstall.size() > 0) {
			list.removeAll(listGameInstall);
		}

		return list;
	}
	
	public List<String> getInstallAppsByList(Context context) {

		initPakcageFilterList();
		initUninstallFilterList();

		List<String> list = new ArrayList<String>();
		
		// 得到PackageManager对象
		PackageManager pm = context.getPackageManager();
		// 得到系统安装的所有程序包的PackageInfo对象
		List<PackageInfo> packs = pm.getInstalledPackages(0);

		for (PackageInfo pi : packs) {
			if ((packageFilterList.indexOf(pi.applicationInfo.packageName) == -1) && !isHiveviewPackage(pi.applicationInfo.packageName)
					&& (((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && (pi.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0) || (uninstallFilterList
							.indexOf(pi.applicationInfo.packageName) > -1))) {
				Log.d(TAG, "Pi==============="+pi.applicationInfo.packageName);
				list.add(pi.applicationInfo.packageName);
			}
		}

		return list;
	}

	/***
	 * 过滤com.hiveview开头的包名的apk，除了乐享电视
	 */
	private static boolean isHiveviewPackage(String packageName){
		if(packageName != null && !TV_ONLIVE.equals(packageName) && packageName.startsWith("com.hiveview")){
			return true;
		}
		return false;
	}
	
	public HashMap<String, String> getInstalledGameCount() {
		HashMap<String, String> mapGameInstall = new HashMap<String, String>();
		// // 获取盒子上的游戏安装信息
		ContentResolver resolver = mContext.getContentResolver();
		synchronized (resolver) {
			Cursor cursor = resolver.query(Uri.parse("content://HiveAppstore2Authorities/TABLE_INSTALLAPP"), null, " category_id =?", new String[] { "1" }, null);
			while (cursor.moveToNext()) {
				mapGameInstall.put(cursor.getString(cursor.getColumnIndex("package_name")), "");
			}
		}

		return mapGameInstall;
	}

	/***
	 * 添加不能卸载的应用的包名
	 */
	public void initUninstallFilterList() {
		uninstallFilterList = new ArrayList<String>();
		uninstallFilterList.add("com.hiveview.tv.onlive");
	}

	/***
	 * 
	 * @Title: AppStoreApplication
	 * @author:maliang
	 * @Description: TODO 添加需过滤的包名,不能在已装应用中显示
	 */
	public void initPakcageFilterList() {
		packageFilterList = new ArrayList<String>();
//		/**
//		 * 2.0需过滤的包名
//		 */
//		packageFilterList.add("com.hiveview.tv");
//		packageFilterList.add("com.hiveview.appstore2");
//		packageFilterList.add("com.hiveview.domyphonemateserver");
//		packageFilterList.add("com.hiveview.pay");
//		packageFilterList.add("com.hiveview.networkdetector");
//		packageFilterList.add("com.hiveview.externalstorage");
//		packageFilterList.add("com.hiveview.p2pservice");
//		/**
//		 * 1.0需过滤的包名
//		 */
//		packageFilterList.add("com.hiveview.domybox");
//		packageFilterList.add("com.qiyi.video");
//		packageFilterList.add("com.hiveview.appstore");
//		packageFilterList.add("com.amlogic.mediacenter");
//		packageFilterList.add("com.hiveview.settings");
//		packageFilterList.add("com.hiveview.subsettings");
//		packageFilterList.add("com.hiveview.weather");
//		packageFilterList.add("com.amlapp.update.otaupgrade");
//		packageFilterList.add("com.hiveview.bluelight");
//		packageFilterList.add("com.amlapp.update.otaupgrade");
//		packageFilterList.add("com.fone.player.domy");
//		packageFilterList.add("com.hiveview.worldcup.onlive");
//		packageFilterList.add("com.yueti.qmepg");
//
//		/**
//		 * 语音需过滤包名
//		 */
//		packageFilterList.add("com.iflytek.xiri.hal");
//		packageFilterList.add("com.iflytek.itvs");
//		packageFilterList.add("com.iflytek.showcome");
//		packageFilterList.add("com.iflytek.showcomesettings");
//		packageFilterList.add("com.iflytek.xiri");
		
		packageFilterList.add("cn.riverrun.tplayer");// 麦伴侣推片的兔子视频播放器
		packageFilterList.add("com.qiyi.tvplayer.drpeng");// 2.0的爱奇艺播放器包名
		packageFilterList.add("com.smit.livevideo");
		packageFilterList.add("com.qiyi.video");// 1.0的爱奇艺播放器包名
		packageFilterList.add("com.amlogic.mediacenter");
		packageFilterList.add("com.amlapp.update.otaupgrade");
		packageFilterList.add("com.amlapp.update.otaupgrade");
		packageFilterList.add("com.fone.player.domy");
		packageFilterList.add("com.yueti.qmepg");
		/**
		 * 语音需过滤包名
		 */
		packageFilterList.add("com.iflytek.xiri.hal");
		packageFilterList.add("com.iflytek.itvs");
		packageFilterList.add("com.iflytek.showcome");
		packageFilterList.add("com.iflytek.showcomesettings");
		packageFilterList.add("com.iflytek.xiri");
		packageFilterList.add("com.iflytek.xiri2.hal");
		packageFilterList.add("com.iflytek.pbsadapter");// 迅飞播放器
	}
}
