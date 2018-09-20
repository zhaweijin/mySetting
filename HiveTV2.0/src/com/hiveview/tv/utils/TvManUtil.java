package com.hiveview.tv.utils;

import android.content.Context;
import android.widget.Toast;

import com.iflytek.itvs.ITVSInterface;

/**
 * ITVS管理
 * 
 * @author admin
 * 
 */
public class TvManUtil {
	/**
	 * 当前信号源
	 */
	public static String curSource = null;
	/**
	 * 是否正常换台
	 */
	public static boolean IsChange = true;
	/**
	 * ITVSManager管理类
	 */
	public static ITVSInterface itvsManager = null;

	/**
	 * 清空选项
	 * 
	 * @param context
	 */
	public void clearTVState(final Context context) {
		if (null == TvManUtil.itvsManager) {
			TvManUtil.itvsManager = new ITVSInterface();
			TvManUtil.itvsManager.registerClient(context, null, new ITVSInterface.ConnectionEvent() {
				@Override
				public void OnReturn(ITVSInterface arg0, boolean arg1) {
					Toast.makeText(context, "1!", Toast.LENGTH_LONG).show();
					TvManUtil.itvsManager.setActiveDevice(null);
				}
			});
		} else {
			TvManUtil.itvsManager.setActiveDevice(null);
		}
	}

	/**
	 * 获得当前的控制的信号源
	 * 
	 * @return
	 */
	private String temp = null;

	public String getCurClient(final Context context) {
		if (null == TvManUtil.itvsManager) {
			TvManUtil.itvsManager = new ITVSInterface();
			TvManUtil.itvsManager.registerClient(context, null, new ITVSInterface.ConnectionEvent() {
				@Override
				public void OnReturn(ITVSInterface arg0, boolean arg1) {
					Toast.makeText(context, "1!", Toast.LENGTH_LONG).show();
					temp = TvManUtil.itvsManager.getActiveClient();
				}
			});
		} else {
			temp = TvManUtil.itvsManager.getActiveClient();
		}
		return temp;
	}

	/**
	 * 设置当前激活的设备
	 * 
	 * @param context
	 * @return
	 */
	public void setCurClient(final Context context, final String temp) {
		if (null == TvManUtil.itvsManager) {
			TvManUtil.itvsManager = new ITVSInterface();
			TvManUtil.itvsManager.registerClient(context, null, new ITVSInterface.ConnectionEvent() {
				@Override
				public void OnReturn(ITVSInterface arg0, boolean arg1) {
					Toast.makeText(context, "1!", Toast.LENGTH_LONG).show();
					TvManUtil.itvsManager.setActiveDevice(temp);
				}
			});
		} else {
			TvManUtil.itvsManager.setActiveDevice(temp);
		}
	}

	/**
	 * 通知直播状态
	 * 
	 * @param context
	 * @return
	 */
	public static void tellTVState(final Context context, final boolean isLive, final String channelName) {
		if (null == TvManUtil.itvsManager) {
			TvManUtil.itvsManager = new ITVSInterface();
		}
		TvManUtil.itvsManager.registerClient(context, null, new ITVSInterface.ConnectionEvent() {
			@Override
			public void OnReturn(ITVSInterface arg0, boolean arg1) {
//				Toast.makeText(context, "1!", Toast.LENGTH_LONG).show();
				TvManUtil.itvsManager.tellLiveStatusChange(isLive, channelName);
			}
		});
		// } else {
		// TvManUtil.itvsManager.tellLiveStatusChange(isLive, channelName);
		// }
	}
}
