/**
 * HomeSwitchTabUtil.java[V 1.0.0]
 * classes : com.hiveview.tv.utils.HomeSwitchTabUtil
 * 李红记 Creat at 2014年4月23日 上午11:34:58
 */
package com.hiveview.tv.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.hiveview.tv.activity.HomeActivity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.deviceinfo.device.Device;
import com.hiveview.tv.common.deviceinfo.device.DeviceFactory;
import com.hiveview.tv.common.deviceinfo.device.Device.DeviceVersion;
import com.hiveview.tv.common.voice.AppSpeaker;

/**
 * 语音切换首页tab
 * 
 * @author jia
 * 
 */
public class HomeSwitchTabUtil {
	private static final String TAG = "HomeActivity";

	/*
	 * 根据action获取index值
	 */
	@SuppressWarnings("null")
	public static int getHomeTabIndex(Context context, String title) {

		/*
		 * Device device = DeviceFactory.getInstance().getDevice();
		 * device.initDeviceInfo(context); DeviceVersion version =
		 * device.getDeviceVersion(); int versionCode =
		 * version.getVersionCode();
		 */
		int versionCode = DeviceBoxUtils.getDeviceVersionCode();
		//Log.v(TAG, "versionCode====" + versionCode);
		int i = -1;
		// 1.0盒子
		if (versionCode == 1) {
			if (title != null) {
				Log.v(TAG, "title1====" + title);
				if (AppConstant.ISDOMESTIC) {// 国内
					// 需要出现极清
					if (HiveviewApplication.outer == 4) {
						if (title.equals("game")) {
							i = 5;
						} else if (title.equals("blue")) {
							i = 4;
						} else if (title.equals("recom")) {
							i = 3;
						} else if (title.equals("movie")) {
							i = 2;
						} else if (title.equals("edu")) {
							i = 1;
						} else if (title.equals("app")) {
							i = 0;
						}// 不需要出现极清
					} else if (HiveviewApplication.outer == 8) {
						if (title.equals("game")) {
							i = 4;
						} else if (title.equals("recom")) {
							i = 3;
						} else if (title.equals("movie")) {
							i = 2;
						} else if (title.equals("edu")) {
							i = 1;
						} else if (title.equals("app")) {
							i = 0;
						}
					}
				}
			 else // 国外
			{
				Log.v(TAG, "title2====" + title);
				if (title.equals("game")) {
					i = 5;
				} else if (title.equals("tv")) {
					i = 4;
				} else if (title.equals("blue")) {
					i = 3;
				} else if (title.equals("recom")) {
					i = 2;
				} else if (title.equals("movie")) {
					i = 1;
				} else if (title.equals("app")) {
					i = 0;
				}}
			}
		}
		// 2.0盒子
		else if (versionCode == 2) {
			if (title != null) {
				if (AppConstant.ISDOMESTIC) {// 国内
					// 需要出现极清
					if (HiveviewApplication.outer == 4) {
						if (title.equals("game")) {
							i = 6;
						} else if (title.equals("tv")) {
							i = 5;
						} else if (title.equals("blue")) {
							i = 4;
						} else if (title.equals("recom")) {
							i = 3;
						} else if (title.equals("movie")) {
							i = 2;
						} else if (title.equals("edu")) {
							i = 1;
						} else if (title.equals("app")) {
							i = 0;
						}
					} else // 不需要出现极清
					if (HiveviewApplication.outer == 8) {
						if (title.equals("game")) {
							i = 5;
						} else if (title.equals("tv")) {
							i = 4;
						} else if (title.equals("recom")) {
							i = 3;
						} else if (title.equals("movie")) {
							i = 2;
						} else if (title.equals("edu")) {
							i = 1;
						} else if (title.equals("app")) {
							i = 0;
						}
					}
				} else // 国外
				{
					if (title.equals("game")) {
						i = 5;
					} else if (title.equals("tv")) {
						i = 4;
					}  else if (title.equals("blue")) {
						i = 3;
					}else if (title.equals("recom")) {
						i = 2;
					} else if (title.equals("movie")) {
						i = 1;
					} else if (title.equals("app")) {
						i = 0;
					}
				}
			}
		}
		return i;
	}

	/*
	 * 根据action获取index值
	 */
	public static String getHomeTabName(String title) {
		String tabName = "";
		if (title != null) {
			if (title.equals("game")) {

				tabName = "游戏";
			} else if (title.equals("tv")) {
				tabName = "电视";
			} else if (title.equals("blue")) {
				tabName = "极清";
			} else if (title.equals("recom")) {
				tabName = "首页";
			} else if (title.equals("movie")) {
				tabName = "影院";
			} else if (title.equals("app")) {
				tabName = "应用";
			} else if (title.equals("edu")) {
				tabName = "教育";
			}
		}
		return tabName;
	}

	public static void closeSiRi(final Context context, String tabName,
			Intent intent) {
		final AppSpeaker speaker = new AppSpeaker(context);
		speaker.begin(intent);
		speaker.showText(tabName, true);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				speaker.close();
			}
		}, 1500);
	}
}
