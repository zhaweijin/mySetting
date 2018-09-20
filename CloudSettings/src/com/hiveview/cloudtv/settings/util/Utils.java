package com.hiveview.cloudtv.settings.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.hiveview.manager.SystemInfoManager;
import android.R.bool;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.NetworkUtils;
import android.net.RouteInfo;
//import android.net.EthernetDevInfo;
import android.net.IpConfiguration;
import android.net.IpConfiguration.IpAssignment;
import android.net.StaticIpConfiguration;
import android.os.SystemProperties;
import android.net.EthernetManager;

import com.droidlogic.pppoe.PppoeDevInfo;
import com.droidlogic.pppoe.PppoeManager;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StatFs;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.hiveview.cloudtv.settings.pppoe.PppoeDataEntity;
import com.hiveview.cloudtv.settings.connectivity.ConnectivityManagerData;
import com.hiveview.cloudtv.settings.connectivity.ConnectivityManagerData.IPv4Util;
import com.hiveview.cloudtv.settings.EthernetConnectedAcivity;
import com.hiveview.cloudtv.settings.PppoeConnectActivity;
import com.hiveview.cloudtv.settings.R;
import com.hiveview.cloudtv.settings.SettingActivity;
import com.hiveview.cloudtv.settings.SettingsApplication;
import com.hiveview.cloudtv.settings.ethernet.EthernetDataEntity;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView;
import com.hiveview.manager.PingManager;
import com.hiveview.manager.SystemInfoManager;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;


public class Utils {
	private static final String TAG = "Utils";
	private static final boolean DEBUG = true;
	private static final String HOURS_12 = "12";
	private static final String HOURS_24 = "24";
	public static final long KEYDOWN_DELAY_TIME = 0L;
	
	public static final int DOMYBOX_10S = 0; //1.0s
	public static final int DOMYBOX_S905X_30X =1; //3.0x
	public static final int DOMYBOX_S905X_10X =2; //1.0x
	public static final int DOMYBOX_S905X_30VC =3; //3.0vc
	public static final int DOMYBOX_S905X_IPTV =4; //iptv
	public static final int DOMYBOX_S905X_40S =5; //4.0s
	
	
	
	public static final String PROP_DEVELOPER_ALLOW = "sys.hiveview.developer.allow"; //false 隐藏
	public static final String PROP_IPTV_ONLY_PPPOE = "sys.hiveview.iptv.only.pppoe"; //true 仅保留pppoe
	public static final int DEFAULT_AUTO_POWER_TIME = 43200;

    private static Toast mToast = null;
	public static String getGateway(Context context) {
		ConnectivityManagerData mConManagerData = new ConnectivityManagerData(context);

		PppoeDataEntity mpppoeData = new PppoeDataEntity(context);

		if(mConManagerData.isEthernetAvailable())
		{
			if(mpppoeData != null && mpppoeData.isPppoeRunning())
				return mpppoeData.getPppoeDnsAddress();
			else
			{
				if(mConManagerData.isEthernetConnecting())
					return mConManagerData.getEthernetGateway();
				else
					return null;
			}
		}
		else
		{
			return mConManagerData.getWifiIpAddress();
		}

	}



	public static String getAddress(int addr) {
		return NetworkUtils.intToInetAddress(addr).getHostAddress();
	}

	@SuppressLint("NewApi")
	public static Bitmap takeScreeShot(Activity activity) {
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();

		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		System.out.println(statusBarHeight);

		// 获取屏幕长和高
		Point displayPoint = new Point();
		activity.getWindowManager().getDefaultDisplay().getSize(displayPoint);
		int width = displayPoint.x;
		int height = displayPoint.y;
		// int width =
		// activity.getWindowManager().getDefaultDisplay().getWidth();
		// int height =
		// activity.getWindowManager().getDefaultDisplay().getHeight();
		// 去掉标题栏
		// Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return b;
	}

	public static void setApplicationBGBitmap(Activity activity) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.3f, 0.3f);

		Bitmap bitmap = Utils.takeScreeShot(activity);
		Bitmap scaleBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		SettingsApplication.getInstance().setBGBitmap(
				BitmapBlurUtil.getInstance().blurBitmap(scaleBitmap, activity));
	}

	public static Bitmap getApplicationBGBitmap() {
		return SettingsApplication.getInstance().getBGBitmap();
	}

	public static boolean pingHost(String str) {
		boolean isConnected = false;
		SettingsApplication.getInstance().setPingStatus(PingManager.getPingStatus());
		if(SettingsApplication.getInstance().getPingStatus() == 0){
			isConnected = true;
		}
/*
		try {
			Process p = Runtime.getRuntime().exec("ping -c 1 -w 100 " + str);
			int status = p.waitFor();
			if (status == 0) {
				isConnected = true;// ???
			} else {
				isConnected = false;// ???
			}
		} catch (IOException e) {
		} catch (InterruptedException e) {
		}
*/
		return isConnected;
	}

	public static boolean isIpValide(String value){
		boolean result = true;
		try {
			String[] ips = value.split("\\.");
			for(int i=0;i<ips.length;i++){
				if(ips[i].length()!=1 && ips[i].startsWith("0")){
					result = false;
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static boolean isIpAddress(String value, Handler mHandler) {
		Message msg = Message.obtain();
		if (TextUtils.isEmpty(value) || value.equals("0.0.0.0")
				|| value.endsWith(".") || !isIpValide(value)) {
//			if (TextUtils.isEmpty(value) || value.endsWith(".")) {
				msg.what = EthernetDataEntity.ALERTMSG_IPINVALID;
				mHandler.sendMessage(msg);
//			}
			return false;
		}
		int start = 0;
		int end = value.indexOf('.');
		int numBlocks = 0;

		while (start < value.length()) {
			if (end == -1) {
				end = value.length();
			}

			try {
				int block = Integer.parseInt(value.substring(start, end));
				if (DEBUG) {
					Log.e(TAG, "block=" + block);
				}
				if ((block > 255) || (block < 0)) {
					Log.e(TAG, "(block > 255) || (block < 0)");
					msg.what = EthernetDataEntity.ALERTMSG_MAX_ITEM_255;
					mHandler.sendMessage(msg);
					return false;
				}
			} catch (NumberFormatException e) {
				Log.e(TAG, "NumberFormatException");
				msg.what = EthernetDataEntity.ALERTMSG_IPINVALID;
				mHandler.sendMessage(msg);
				return false;
			}

			numBlocks++;

			start = end + 1;
			end = value.indexOf('.', start);
		}
		if (DEBUG) {
			Log.e(TAG, "numBlocks =" + numBlocks);
		}
		if (numBlocks > 4) {
			Log.e(TAG, "numBlocks > 4");
			msg.what = EthernetDataEntity.ALERTMSG_EXCEED_CHRATER;
			mHandler.sendMessage(msg);
		} else if (numBlocks < 4) {
			Log.e(TAG, "numBlocks < 4");
			msg.what = EthernetDataEntity.ALERTMSG_IPINVALID;
			mHandler.sendMessage(msg);
		}
		return numBlocks == 4;
	}

	public static int getMask(String maskAddr) {
		if (!isValidMask(maskAddr))
			return -1;
		String[] maskSplit = { "0", "0", "0", "0" };
		maskSplit = maskAddr.split("\\.");
		String mask = "";
		String binaryMask = "";
		for (int i = 0; i < 4; i++) {
			int maskTemp = Integer.parseInt(maskSplit[i]);
			mask = mask.concat(Integer.toString(maskTemp)).concat(".");
			binaryMask = binaryMask.concat(Integer.toBinaryString(maskTemp));
		}
		return binaryMask.lastIndexOf("1") + 1;
	}

	public static boolean isValidMask(String mask) {
		int maskNum = 0;
		int maskBit = 0;
		if (mask.indexOf(".") == -1) {
			try {
				maskBit = Byte.parseByte(mask);
			} catch (Exception e) {
				return false;
			}
			if (maskBit > 31 || maskBit < 1) {
				return false;
			}
			return true;
		}
		String[] maskSplit = mask.split("\\.");
		String maskBinString = "";
		if (maskSplit.length != 4)
			return false;
		for (int i = 0; i < maskSplit.length; i++) {
			try {
				maskNum = Integer.parseInt(maskSplit[i]);
			} catch (Exception e) {
				return false;
			}
			if (i == 0 && Integer.numberOfLeadingZeros(maskNum) == 32)
				return false;
			if (Integer.numberOfLeadingZeros(maskNum) != 24)
				if (Integer.numberOfLeadingZeros(maskNum) != 32)
					return false;
			maskBinString = maskBinString.concat(Integer
					.toBinaryString(maskNum));
		}
		if (maskBinString.indexOf("0") < maskBinString.lastIndexOf("1"))
			return false;
		return true;
	}


	public static boolean isValidMask(String mask,Handler mHandler) {
		int maskNum = 0;
		int maskBit = 0;
		Message msg = Message.obtain();
		if (mask.indexOf(".") == -1) {
			try {
				maskBit = Byte.parseByte(mask);
			} catch (Exception e) {
				msg.what = EthernetDataEntity.ALERTMSG_IPINVALID;
				mHandler.sendMessage(msg);
				return false;
			}
			if (maskBit > 31 || maskBit < 1) {
				msg.what = EthernetDataEntity.ALERTMSG_IPINVALID;
				mHandler.sendMessage(msg);
				return false;
			}
			return true;
		}
		String[] maskSplit = mask.split("\\.");
		String maskBinString = "";
		if (maskSplit.length != 4){
			msg.what = EthernetDataEntity.ALERTMSG_IPINVALID;
			mHandler.sendMessage(msg);
			return false;
		}
		for (int i = 0; i < maskSplit.length; i++) {
			try {
				maskNum = Integer.parseInt(maskSplit[i]);
			} catch (Exception e) {
				msg.what = EthernetDataEntity.ALERTMSG_IPINVALID;
				mHandler.sendMessage(msg);
				return false;
			}
			if (i == 0 && Integer.numberOfLeadingZeros(maskNum) == 32){
				msg.what = EthernetDataEntity.ALERTMSG_IPINVALID;
				mHandler.sendMessage(msg);
				return false;
			}
			if (Integer.numberOfLeadingZeros(maskNum) != 24)
				if (Integer.numberOfLeadingZeros(maskNum) != 32){
					msg.what = EthernetDataEntity.ALERTMSG_IPINVALID;
					mHandler.sendMessage(msg);
					return false;
				}
			maskBinString = maskBinString.concat(Integer
					.toBinaryString(maskNum));
		}
		if (maskBinString.indexOf("0") < maskBinString.lastIndexOf("1")){
			msg.what = EthernetDataEntity.ALERTMSG_IPINVALID;
			mHandler.sendMessage(msg);
			return false;
		}
		return true;
	}



	

	public static int getActiveNetworkType(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if(networkInfo!=null){
			return networkInfo.getType();
		}
		return 0;
	}
		
	public static boolean checkEthState() {
		String net_file = "/sys/class/ethernet/linkspeed";
		String res = "";
		String[] tmpString = new String[1];//new String[3];
		
		for (int i = 0; i < tmpString.length; i++) {
			tmpString[i] = null;
		}
		
		File file = new File(net_file);
		BufferedReader reader = null;
		try {

			reader = new BufferedReader(new FileReader(file));
			
			// String tempString = null;
			int line = 0;

			 while (line<tmpString.length && (tmpString[line] = reader.readLine())!= null) {
				 System.out.println("line " + line + ": " + tmpString[line]);
				 line++;
			 }
			//res = reader.readLine();
			//Log.e(TAG, "read message:" + res);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		//if (null != tmpString[0] && null!=tmpString[2]) {
		if (null != tmpString[0]) {
			String[] result1 = new String[2];
			//String[] result2 = new String[2];
			
			for (int i = 0; i < result1.length; i++) {
				result1[i] = null;
			}
			//for (int i = 0; i < result2.length; i++) {
			//	result2[i] = null;
			//}
			result1 = tmpString[0].split(": ", 2);
			//result2 = tmpString[2].split(" : ", 2);
			Log.e(TAG, "read message split1" + result1[0] + ":" + result1[1]);
			//Log.e(TAG, "read message split2" + result2[0] + ":" + result2[1]);
			//if (result1[1] != null && result2[1] !=null) {
			if (result1[1] != null ) {
//				if ("link".equals(result1[1]) || "100".equals(result2[1])) {
//					return true;
//				} else if ("unlink".equals(result1[1])||"10".equals(result2[1])) {
//					return false;
//				}
				if ("link".equals(result1[1])) {
					return true;
				} else if ("unlink".equals(result1[1])) {
					return false;
				}
			}
		}
		return false;
	}

	public static void startListFocusAnimation(Context context,
			LauncherFocusView focusView, int resId) {
		Animation animation = AnimationUtils.loadAnimation(context, resId);
		focusView.startAnimation(animation);
	}

	public static boolean is24Hour(Context context) {
		return DateFormat.is24HourFormat(context);

	}

	public static void set24Hour(boolean is24Hour, Context context) {
		Settings.System.putString(context.getContentResolver(),
				Settings.System.TIME_12_24, is24Hour ? HOURS_24 : HOURS_12);
	}

	// 检测是否允许安装未知来源的应用
	@SuppressLint("NewApi")
	public static boolean isNonMarketAppsAllowed(Context context) {

		return Settings.Global.getInt(context.getContentResolver(),
				Settings.Global.INSTALL_NON_MARKET_APPS, 0) > 0;
	}

	// 检测ADB调试是否开启
	@SuppressLint("NewApi")
	public static boolean isAdbOrNot(Context context) {
		return Settings.Global.getInt(context.getContentResolver(),
				Settings.Global.ADB_ENABLED, 0) != 0;
	}

	// 设置是否允许安装未知来源的应用
	@SuppressLint("NewApi")
	public static void setNonMarketAppsAllowed(Context context, boolean enabled) {
		final UserManager um = (UserManager) context
				.getSystemService(Context.USER_SERVICE);
		if (um.hasUserRestriction(UserManager.DISALLOW_INSTALL_UNKNOWN_SOURCES)) {
			return;
		}
		// Change the system setting
		Settings.Global.putInt(context.getContentResolver(),
				Settings.Global.INSTALL_NON_MARKET_APPS, enabled ? 1 : 0);
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	public static void setAdbDebug(Context context, int enabled) {
		Settings.Global.putInt(context.getContentResolver(),
				Settings.Global.ADB_ENABLED, enabled);
	}

	/**
	 * Returns the default link's IP addresses, if any, taking into account IPv4
	 * and IPv6 style addresses.
	 * 
	 * @param context
	 *            the application context
	 * @return the formatted and newline-separated IP addresses, or null if
	 *         none.
	 */
	public static String getDefaultIpAddresses(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		LinkProperties prop = cm.getActiveLinkProperties();
		return formatIpAddresses(prop);

	}
	
	
	public static String getIP(Context context){
		ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(null!=networkInfo){
        	if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
        		WifiManager mWifiManager = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                return Formatter.formatIpAddress(wifiInfo.getIpAddress());
        	}else {
				return getDefaultIpAddresses(context);
			}
        	
        }
        return null;
	}

	private static String formatIpAddresses(LinkProperties prop) {
		if (prop == null)
			return null;
		Iterator<InetAddress> iter = prop.getAllAddresses().iterator();
		// If there are no entries, return null
		if (!iter.hasNext())
			return null;
		// Concatenate all available addresses, comma separated
		String addresses = "";

		if (iter.hasNext()) {
			// addresses += "\n";
			addresses += iter.next().getHostAddress();
		}

		return addresses;
	}

	public static String formatSize(Context context, long size) {
		return Formatter.formatFileSize(context, size);
	}
	
	public static String getStorageInfo(Context context) {
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
		objects[0] = usedSize + "," + totalSize;
		objects[1] = (int) ((usedMemory * 1.0f / totalMemory * 1.0f) * 100);
		return totalSize;
		
		}
	
	/*
	 * 检测盒子存储设备的版本（4G,8G,16G）
	 * 
	 * @Title: TWGetStorageListener
	 * @author:yupengtong
	 * @Description: 圆整存储总量
	 * @param totalSize
	 * @return
	 */
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


 

    public static boolean checkNetworkIsActive(Context context) {
		boolean mIsNetworkUp = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		
		if (info != null) {
			mIsNetworkUp = info.isAvailable();
		}
		return mIsNetworkUp;
	}

   public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
	
	
	public static void showToast(Context context, String text, int duration) {
		if (mToast == null) {
			mToast = Toast.makeText(context, text, duration);
		} else {
			mToast.setText(text);
			mToast.setDuration(duration);
		}
		
		mToast.show();
	}

    public static boolean isOverseas(){
		try {
			 SystemInfoManager systemInfoManager = SystemInfoManager.getSystemInfoManager();
			 //2 海外；1 国内电视；0 国内盒子；-1 出错
			 if(systemInfoManager.getSofewareInfo()==2)
				 return true;
			 return false;
		 } catch (Exception e) {
			// TODO: handle exception
		 }
		
		 return false;
	}
 
	
	public static boolean checkBlank(String str) {
		Pattern pattern = Pattern.compile("[\\s]+");
		Matcher matcher = pattern.matcher(str);
		boolean flag = false;
		while (matcher.find()) {
			flag = true;
		}
		return flag;
	}
	
	
	public static boolean hasIlleaglAlpha(String input){
		boolean t=false;
		String str = input.toString();
		for(int i=0,l=str.length();i<l;i++){
			if(str.charAt(i)>128){
				t=true;
				break;
			}
		}
		return t;
	}
	
	public static boolean checkPackageIsExist(Context context,
            String packageName) {
    try {
            PackageManager mPm = context.getPackageManager();
            ApplicationInfo mAppInfo = mPm.getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
    } catch (NameNotFoundException e) {
            Log.e("TAG", "Exception when retrieving package: " + packageName, e);
            // show dialog
            return false;
    }
	
   }


	// 0 is chinese ;1 is english
		public static int getLanguage() {
			try {
				IActivityManager am = ActivityManagerNative.getDefault();
				Configuration config;
				config = am.getConfiguration();
				String lang = config.locale.getLanguage();
				Log.i(TAG, "----language is -----" + lang);
				if (Locale.SIMPLIFIED_CHINESE.getLanguage().equals(lang)) {
					return 0;
				} else if(Locale.ENGLISH.getLanguage().equals(lang)){
					return 1;
				} else if(lang.equals("es")){
					return 2;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return 0;
		}

	
	public static String getInterfaceMask(String interfaceName){
		//for 1.0s　android 5.1
		if(!SystemProperties.get("dhcp.wlan0.mask","").equals(""))
		    return SystemProperties.get("dhcp.wlan0.mask","");
		
		String wlanMask="";
		for (String s : readRouteList("/proc/net/route")) {
            String[] fields = s.split("\t");
            if (fields.length > 7) {
                String iface = fields[0];
                if (interfaceName.equals(iface)) {
                    String mask = fields[7];
                    try {
                        int prefixLength =NetworkUtils.netmaskIntToPrefixLength(
                                (int)Long.parseLong(mask, 16));
                        Log.v(TAG, "preLength=="+prefixLength);
                        IPv4Util iputil = new IPv4Util();
                        Log.v(TAG, "mask=="+iputil.prefixLen2Mask(prefixLength));
                        wlanMask = iputil.prefixLen2Mask(prefixLength);
                        break;
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing route " + s + " : " + e);
                        continue;
                    }
                }
            }
        }
		return wlanMask;
	}
	

	private static ArrayList<String> readRouteList(String filename) {
        FileInputStream fstream = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            fstream = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String s;
            while (((s = br.readLine()) != null) && (s.length() != 0)) {
                list.add(s);
            }    
        } catch (IOException ex) {
            // return current list, possibly empty
        } finally {
            if (fstream != null) {
                try {
                    fstream.close();
                } catch (IOException ex) {}
            }    
        }    
        return list;
    } 
	
	//DU.72.31.20  s905x
	//FF.70.31.22  1.0s
	public static int getBoxType(){
		try {
			SystemInfoManager manager = SystemInfoManager.getSystemInfoManager();
			
		    String version = manager.getFirmwareVersion();
 
		    
		    String[] vers = version.split("\\.");
                    if(vers[0].equals("IP") || vers[0].equals("IF")){  //IPTV eg.IP.70.10.01 or IF.70.10.01
                        return DOMYBOX_S905X_IPTV;
                    }		   
   
		    if(Integer.parseInt(vers[1])==74){
		    	return DOMYBOX_S905X_30X;   //3.0x
		    }else if(Integer.parseInt(vers[1])==70){
		    	return DOMYBOX_10S;   //1.0s
		    }else if(Integer.parseInt(vers[1])==72){
		    	return DOMYBOX_S905X_10X; //1.0x
		    }else if(Integer.parseInt(vers[1])==76){
		    	return DOMYBOX_S905X_30VC; //3.0vc
		    }else if(Integer.parseInt(vers[1])==78){
		    	return DOMYBOX_S905X_40S;//4.0s
		    }else{
		    	return DOMYBOX_10S;
		    }
		} catch (Exception e) {
			// TODO: handle exception
		}

		return DOMYBOX_10S;
		
	 
	}
	
	
	
	public static void goPppoeConnect(Context mContext){
		ConnectivityManagerData mConnectivityData = new ConnectivityManagerData(mContext);
		if(mConnectivityData.isEthernetAvailable()){
	    	jump2Pppoe(mContext);
		}else{
			ToastUtils.showToast(mContext, mContext.getResources().getString(R.string.tips_pull_in_ethernet), 1500);
		}
	}
	
	public static void jump2Pppoe(Context mContext){
		boolean isPPPOEOK = getActiveNetworkType(mContext) == SettingsApplication.TYPE_PPPOE?true:false;
		Log.v(TAG, "network type="+Utils.getActiveNetworkType(mContext));
		if(isPPPOEOK){
			jump2Ethernet(mContext);
		}else {
			Intent intent = new Intent(mContext, PppoeConnectActivity.class);
			intent.putExtra(PppoeDataEntity.PPPOE_EXTRA_STATE, PppoeDataEntity.PPPOE_EXTRA_STATE_CONNECT);
			mContext.startActivity(intent);
		}
	}
	
	public static void jump2Ethernet(Context mContext){
		Intent intent = new Intent(mContext, EthernetConnectedAcivity.class);
		mContext.startActivity(intent);
	}
	
	
	public static boolean boxLegalCheck(){
		try {
			SystemInfoManager manager = SystemInfoManager.getSystemInfoManager();
			String softwareVersion = manager.getFirmwareVersion();
			String model = manager.getProductModel();
			String sn = manager.getSnInfo();
			Log.v(TAG, "model=="+model+",sn="+sn+",version="+softwareVersion);
			//网内DM4036的盒子被非法窜改DB4036
			if(model.equals("DB4036")){
				//首先校验SN
				if(sn.startsWith("DMD")){
					return false;
				}
				//如果SN也被窜改,再判断软件版本号
				/*if(softwareVersion.equals("PB.70.31.11") || 
							softwareVersion.equals("PB.70.31.12") ||
							softwareVersion.equals("PB.70.31.20") ||
							softwareVersion.equals("PB.70.31.50")){
					return false;
				}*/
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}
}
