package com.hiveview.tv.common.silenceupgrade;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

public class PackageManagerUtils {

	
	public static ApplicationInfo getApplicationInfo(Context context,String packageName){
		
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> appinfoList = pm.getInstalledApplications(ApplicationInfo.FLAG_INSTALLED);
		
		for(ApplicationInfo appInfo : appinfoList){
			
			if(appInfo.packageName.equals(packageName) && appInfo.sourceDir.contains("system")) {
				
				return appInfo;
			}
		}
		
		
		return null;
		
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static ApplicationInfo getApplicationInfoDataDir(Context context,String packageName){
		
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> appinfoList = pm.getInstalledApplications(ApplicationInfo.FLAG_INSTALLED);
		
		for(ApplicationInfo appInfo : appinfoList){
			
			if(appInfo.packageName.equals(packageName) && !appInfo.sourceDir.contains("system")) {
				
				return appInfo;
			}
		}
		
		
		return null;
		
	}
	
	
	public static PackageInfo getPackageInfo(Context context,String path) {
		
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path,PackageManager.GET_ACTIVITIES);
        
        return info;
        
    }
	
}
