package com.hiveview.tv.common;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.apache.http.cookie.Cookie;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageLoaderConfiguration;
import com.hiveview.box.framework.image.ImageSize;
import com.hiveview.box.framework.image.LruMemoryCache;
import com.hiveview.box.framework.image.Md5FileNameGenerator;
import com.hiveview.box.framework.image.MemoryCacheUtils;
import com.hiveview.box.framework.image.QueueProcessingType;
import com.hiveview.reporter.handler.CrashHandler;
import com.hiveview.tv.common.deviceinfo.device.Device;
import com.hiveview.tv.common.deviceinfo.device.DeviceFactory;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.LoadService;
import com.hiveview.tv.service.LockVipService;
import com.hiveview.tv.service.RemotecontrolReceiver;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.service.request.ApiConstant;
import com.hiveview.tv.service.request.GetAppFocusListRequest;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.DeviceBoxUtils;
import com.hiveview.tv.utils.STBSettingInfoUtil;
import com.hiveview.tv.view.AuxiliaryNetworkView;

public class HiveviewApplication extends Application {
	protected static final String TAG = "HiveviewApplication";
	private static SharedPreferences spCookies;
	public static Context mContext = null;
	/**
	 * @Fields RecommendTag 记录内外网状态和是否打开新闻客户端处理
	 */
	public static String RecommendTag = "Recommend_Tag";
	// intranet:1内网 0 外网
	public static String intranet = "intranet";
	public static String packageNameForNewsAPP = "com.hiveview.bluelight.news";
	public static String packageNameForRemiereApp="com.hiveview.premiere";
	
	/**
	 * @Fields bgBitmap:统一背景的图
	 */
	public static Bitmap bgBitmap;
	
	/**
	 * @Fields isIntranet 内外网tag是否更新
	 */
	public static boolean isIntranet =  false;
	
	/**
	 * 全局图片的图片渲染配置
	 */
	public static Config Bitmapconfig = Bitmap.Config.ARGB_8888;
	
	/**
	 * @Fields outer
	 * 区分内外网标识
	 * 2^2内网
	 * 2^3外网
	 */
	public static int outer = 0;
	
	/* start by ZhaiJianfeng */
	/**
	 * 记录当前网络状态
	 */
	private static int netStatus = AuxiliaryNetworkView.DEFAULT_STATE;
	
	public static String mDeviceName="DeviceRTK";
	
	public static View mcurrentfocus=null;
	

	/* end by ZhaiJianfeng */
	@Override
	public void onCreate() {
		super.onCreate();
		HttpTaskManager.getInstance().submit(new Runnable() {

			@Override
			public void run() {
				new HiveTVService().deviceCheck(ApiConstant.APP_VERSION,
						AppUtil.getLocaldeviceId(HiveviewApplication.this),
						DeviceBoxUtils.getMac(), ApiConstant.UUID);

			}
		});

		// Intent intent = new Intent(this, OnliveTipService.class);
		// this.startService(intent);
		mContext = getBaseContext();
		/*start by huzuwei,添加错误日志上报sdk*/
		CrashHandler.getInstance().init(getApplicationContext(), true);//sdk初始化
		/*end by huzuwei*/
		/*start by lihongji,动态注册遥控器快捷键广播*/
		RemotecontrolReceiver  mRemotecontrolReceiver= new RemotecontrolReceiver();  
		IntentFilter filter = new IntentFilter();
		filter.addAction(RemotecontrolReceiver.BROADCAST_LAUNCHER_ACTION);
		filter.addAction(RemotecontrolReceiver.BROADCAST_SETTING_ACTION);
		filter.addAction(RemotecontrolReceiver.BROADCAST_TV_ACTION);
		registerReceiver(mRemotecontrolReceiver, filter);
		/* end by lihongji */
		initImageLoader(getApplicationContext());
		// /太多线程并发,还没用
		// preLoadHomeData(getApplicationContext());
		STBSettingInfoUtil.InitOperator(mContext);
		STBSettingInfoUtil.adaptSTBDefineScenes(this);
		Intent intentService = new Intent(getBaseContext(), LoadService.class);
		intentService.putExtra("isNeedDeviceCheck", true);
		getBaseContext().startService(intentService);
		if(!AppConstant.ISDOMESTIC){
		Intent intent = new Intent(mContext, LockVipService.class);
		mContext.startService(intent);}
	}
	
	private ImageLoaderConfiguration config;
	/**
	 * 初始化图片下载类
	 * 
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 将图片预加载到内存缓存
	 * @Title preLoadHomeData
	 * @Description 
	 * @param context
	 */
	public static void preLoadHomeData(Context context) {
		final ImageLoader loader = ImageLoader.getInstance();
		final SharedPreferences preferences = context.getSharedPreferences(AppConstant.PRE_LOAD_IMAGE, Context.MODE_PRIVATE);
		final Set<String> set = preferences.getStringSet(AppConstant.PRE_LOAD_IMAGE_KEY, null);
		Log.d(TAG, "loadImage set has data : " + (null != set && set.size() > 0));
		if (null != set && set.size() > 0) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						for (String str : set) {
							File file = loader.getDiscCache().get(str);
							// Set<String> imageSize =
							// preferences.getStringSet(str, null);
							// if (null != imageSize) {
							// Object[] size = imageSize.toArray();
							// Log.d(TAG, "loadImage : height : " +
							// size[0].toString() + " width : " +
							// size[1].toString());
							// ImageSize targetSize = new
							// ImageSize(Integer.valueOf(size[1].toString()),
							// Integer.valueOf(size[0].toString()));
							String memoryCacheKey = MemoryCacheUtils.generateKey(str, getMaxImageSize());
							Log.d(TAG, "loadImage : memoryCacheKey : " + memoryCacheKey);
							loader.getMemoryCache().put(memoryCacheKey, BitmapFactory.decodeFile(file.getAbsolutePath()));
							// }
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	static ImageSize getMaxImageSize() {
		DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
		int width = displayMetrics.widthPixels;
		int height = displayMetrics.heightPixels;
		return new ImageSize(width, height);
	}

	public static String getCookie() {
		if (spCookies == null)
			spCookies = mContext.getSharedPreferences(AppConstant.SP_COOKIES, Context.MODE_PRIVATE);
		return spCookies.getString(AppConstant.SP_COOKIES, "");
	}

	@SuppressLint("CommitPrefEdits")
	public static void saveCookie(List<Cookie> cookies) {
		if (cookies != null && cookies.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Cookie ck : cookies) {
				sb.append(ck.getName()).append('=').append(ck.getValue()).append(";");
			}
			String strCookies = sb.toString();
			if (spCookies == null)
				spCookies = mContext.getSharedPreferences(AppConstant.SP_COOKIES, Context.MODE_PRIVATE);

			Editor edit = spCookies.edit();
			edit.putString(AppConstant.SP_COOKIES, strCookies);
			edit.commit();
		}
	}

	/* start by ZhaiJianfeng */
	public static void setNetStatus(int status) {
		if (netStatus != status) {
			Intent intent = new Intent(AuxiliaryNetworkView.NET_STATUS_NOTIFY_ACTION);
			mContext.sendBroadcast(intent);
		}
		netStatus = status;
	}

	public static int getNetStatus() {
		return netStatus;
	}

	public static Context getContext() {
		return mContext;
	}

	public static boolean isNetStatusAvailiable() {
		return netStatus == AuxiliaryNetworkView.ETHERNET_STATE_EABLED || netStatus == AuxiliaryNetworkView.PPPOE_STATE_EABLED
				|| netStatus == AuxiliaryNetworkView.WIFI_STATE_EABLED;
	}
	/* end by ZhaiJianfeng */
	
	/**
	 * 
	 * @param context
	 * @return
	 * @author xiaomo
	 */
    public static String getVersionName()//获取版本号  
    {  
       try {  
           PackageInfo pi=getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);  
           return pi.versionName;  
       } catch (NameNotFoundException e) {  
            // TODO Auto-generated catch block  
           e.printStackTrace();  
           return "2.0";  
       }  
    }  
}
