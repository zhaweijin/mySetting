package com.hiveview.cloudtv.settings.upload;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.manager.SystemInfoManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.os.SystemProperties;
import android.util.Log;
import android.widget.Toast;

public class DataMessageHandler {
	private static SystemInfoManager manager;
	private int mBlockSize;
	
	/**
	 * 发送消息
	 * @return
	 * @throws Exception 
	 */
	public static boolean sendMessage(Context context) {
		boolean isSuccess = false;
		int errorCode = 0;
		
		try {
			//收集信息，并生成json
			manager = SystemInfoManager.getSystemInfoManager();
			//(1)所需信息
			DeviceInfo deviceInfo=new DeviceInfo();
			String mac=manager.getMacInfo();
			String macNew=mac.replace(":", "");
			deviceInfo.setMac(macNew);//mac
			deviceInfo.setSn(manager.getSnInfo());//sn
			deviceInfo.setModel(manager.getProductModel());//Model型号
			deviceInfo.setRomVersion(manager.getFirmwareVersion());//rom软件版本号
			deviceInfo.setRomBuildData(getReleaseTime());//romBuildData
			//发送端apk信息
				List<String> list=new ArrayList<String>();
				String packName=context.getPackageName();
				list.add(packName);
				list.add(context.getPackageManager()
	 					.getPackageInfo(packName, 0).versionCode+"");
			deviceInfo.setSendApkInfoList(list);//发送端apk信息集合
			
			deviceInfo.setUserId("null");//最后登录用户null
			deviceInfo.setLoginDate("null");//最后登录时间null
			//获取系统所有安装的apk信息
			PackageManager packageManager = context.getPackageManager();
			List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
			List<ApkInfo> apkInfoList=new ArrayList<ApkInfo>();
			for(int i=0;i<packageInfoList.size();i++){
				PackageInfo in=packageInfoList.get(i);
				String namePackage=in.applicationInfo.packageName;
				int code=in.versionCode;
				String dir=in.applicationInfo.sourceDir;
				long l=(new File(dir)).length();
				String size=getFileSize(l);
				ApkInfo apkInfo=new ApkInfo();
				apkInfo.setPackageName(namePackage);
				apkInfo.setVersion(code+"");
				apkInfo.setBinSize(size);
				 if ((in.applicationInfo.flags & in.applicationInfo.FLAG_SYSTEM) <= 0) {//非系统应用  
					      apkInfo.setType(2);
					 }else{//系统应用
						 
						 apkInfo.setType(0);
				 }
				apkInfoList.add(apkInfo);
			}
			
			deviceInfo.setApkInfoList(apkInfoList);//Apk信息集合
			deviceInfo.setTotalSize(getRomTotalSize());//总空间
			deviceInfo.setUnUsedSize(getRomAvailableSize());//剩余空间
			deviceInfo.setUnHoldSize(getCacheAvailableSize());//升级分区剩余空间
			
			
			
			//生成json
			String json=generateJson(deviceInfo);
	        Log.i("===========", "===正式模拟！");
	        //Toast.makeText(context, "开始上传服务器！", Toast.LENGTH_LONG).show();
			errorCode = HttpUtil.reqPost(json, context);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if(errorCode == 1) {
			isSuccess = true;
		}
		return isSuccess;
	}
	
	
	
	//Rom编译日期
	public static String getReleaseTime() {
				long getTime= SystemProperties.getLong("ro.build.date.utc", 0) * 1000;
				Date releaseTime = new Date(getTime);
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss "); 
				//设置发布时间的时区为东八区，修复发布时间随时区错乱的问题
				format.setTimeZone(TimeZone.getTimeZone("GMT+8")); 
				Log.i("settingtime","get time"+format.format(releaseTime));
				return format.format(releaseTime);
			}

	//剩余空间
	@SuppressLint("NewApi")
	public static String getRomAvailableSize() {
		File path = Environment.getDataDirectory();
		StatFs statFs = new StatFs(path.getPath());
		long blockSize = statFs.getBlockSizeLong();
		long availableBlocks = statFs.getAvailableBlocksLong();
		return blockSize * availableBlocks/1024/1024+"MB";
	}
	//升级分区剩余空间
	@SuppressLint("NewApi")
	public static String getCacheAvailableSize() {
	File path = new File("/cache");
	StatFs statFs = new StatFs(path.getPath());
	long blockSize = statFs.getBlockSizeLong();
	long availableBlocks = statFs.getAvailableBlocksLong();
	return blockSize * availableBlocks/1024/1024+"MB";
}
	//总空间
	@SuppressLint("NewApi")
	public static String getRomTotalSize(){
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
				Log.i("=====自己total", "===转化前=="+totalMemory);
				String str=totalMemory/1024/1024+"MB";
				Log.i("=====自己total", "===转化后=="+str);
		return str;
	}
	private static long formatTotalSize2Value(long totalSize) {
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
	
	//生成json的方法
		public static String generateJson(DeviceInfo deviceInfo){
					//JSONObject objTotal=new JSONObject();
					//对应自身apk的信息
					JSONObject objSendApk=new JSONObject();
					try {
						objSendApk.put("packageName", deviceInfo.getSendApkInfoList().get(0))
						   .put("version", deviceInfo.getSendApkInfoList().get(1));
					
					//所有安装的apk的信息
					JSONArray arrayApk=new JSONArray();
					JSONObject objApk;
					for(int i=0;i<deviceInfo.getApkInfoList().size();i++){
						objApk=new JSONObject();
						objApk.put("packageName", deviceInfo.getApkInfoList().get(i).getPackageName())
						      .put("version", deviceInfo.getApkInfoList().get(i).getVersion())
						      .put("binSize", deviceInfo.getApkInfoList().get(i).getBinSize())
						      .put("Type", deviceInfo.getApkInfoList().get(i).getType());
						arrayApk.put(objApk);
					}
					/*objTotal.put("mac", deviceInfo.getMac())
					        .put("sn", deviceInfo.getSn())
					        .put("model", deviceInfo.getModel())
					        .put("romVersion", deviceInfo.getRomVersion())
					        .put("romBuildData", deviceInfo.getRomBuildData())
					        .put("sendApkInfoList", objSendApk)
					        .put("userId", deviceInfo.getUserId())
					        .put("loginDate", deviceInfo.getLoginDate())
					        .put("apkInfoList", arrayApk)
					        .put("totalSize", deviceInfo.getTotalSize())
					        .put("unUsedSize", deviceInfo.getUnUsedSize())
					        .put("unHoldSize", deviceInfo.getUnHoldSize());*/
					StringBuffer test=new StringBuffer();
					test.append("{\"mac\":"+"\""+deviceInfo.getMac()+"\",");
					test.append("\"sn\":"+"\""+deviceInfo.getSn()+"\",");
					test.append("\"model\":"+"\""+deviceInfo.getModel()+"\",");
					test.append("\"romVersion\":"+"\""+deviceInfo.getRomVersion()+"\",");
					test.append("\"romBuildData\":"+"\""+deviceInfo.getRomBuildData()+"\",");
					test.append("\"sendApkInfoList\":"+objSendApk+",");
					test.append("\"userId\":\"null\",");
					test.append("\"loginDate\":\"null\",");
					test.append("\"apkInfoList\":"+arrayApk+",");
					test.append("\"totalSize\":"+"\""+deviceInfo.getTotalSize()+"\",");
					test.append("\"unUsedSize\":"+"\""+deviceInfo.getUnUsedSize()+"\",");
					test.append("\"unHoldSize\":"+"\""+deviceInfo.getUnHoldSize()+"\"}");
					String str=test.toString();
					Log.i("=======", "===实验json=="+str);
					String json1New=str.substring(0, str.length()/2);
					String json2New=str.substring(str.length()/2);
					Log.i("json1New==========", json1New);
					Log.i("json2New==========", json2New);
					return str;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return "null";
					}
					
					
			
		}
		public static String getFileSize(long size) {
			float mSize = 0;
			if (size > 1024 * 1024 * 1024) {
				mSize = (float) (Math.round((1f * size / (1024 * 1024 * 1024)) * 100)) / 100;
				return "" + mSize + "G";
			} else if (size > 1024 * 1024) {
				mSize = (float) (Math.round((1f * size / (1024 * 1024)) * 100)) / 100;
				return "" + mSize + "M";
			} else {
				mSize = (float) (Math.round((1f * size / 1024) * 100)) / 100;
				return "" + mSize + "K";
			}
		}

	

	
}
