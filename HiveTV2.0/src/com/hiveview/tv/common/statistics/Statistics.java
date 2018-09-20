/**
 * @Title BaseStatistics.java
 * @Package com.hiveview.tv.common.statistics
 * @author haozening
 * @date 2014年6月24日 下午4:10:54
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.statistics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.hiveview.data.DataMessageHandler;
import com.hiveview.data.entity.DataEntity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.deviceinfo.device.Device;
import com.hiveview.tv.common.deviceinfo.device.DeviceFactory;
import com.hiveview.tv.common.statistics.utils.DevicesInfoUtils;
import com.hiveview.tv.service.LockVipService;

/**
 * 统计封装类，将公共数据进行封装，开放tabNo，actionType，actionInfo参数的设置
 * 
 * @ClassName BaseStatistics
 * @Description
 * @author haozening
 * @date 2014年6月24日 下午4:10:54
 * 
 */
@SuppressLint("HandlerLeak")
public class Statistics {

	private static final String TAG = "Statistics";

//	private static final String STATISTICS_URL = "http://data.domybox.com/data_collect/data/LogForV2.json";
	private static final String STATISTICS_URL = AppConstant.ISDOMESTIC?"http://data.pthv.gitv.tv/data_collect/data/LogForV2.json":"http://collect.data.usa.domybox.com/data_collect/data/LogForV2.json";

    public static final int SEND_STATISTICS_FAIL =160126;

	/**
	 * 项目ID
	 */
	private String projectId;

	/**
	 * 设备S/N
	 */
	private String sn;
	/**
	 * 设备id
	 */
	private String deviceid;
	/**
	 * 有线mac
	 */
	private String mac;
	/**
	 * 无线mac
	 */
	private String wlanmac;
	/**
	 * apk版本
	 */
	private String version;
	/**
	 * 设备型号
	 */
	private String model;
	/**
	 * rom版本号
	 */
	private String romVersion;
	/**
	 * 设备硬件版本
	 */
	private String deviceVersion;
	/**
	 * launcher版本号
	 */
	private String launcherVersion;

	/**
	 * tab位置
	 */
	private String tabNo;
	/**
	 * 设置行为标识
	 */
	private String actionType;
	/**
	 * 设置扩展参数
	 */
	private String actionInfo;
	/**
	 * 距上次开机间隔
	 */
	private String intervalDay;
	
	private static DataEntity entity ;
	
	private Context mContext;
	
	private static Handler handler;
	
	public Statistics(Context context, String tabNo, String actionType, String actionInfo) {
		try {
		Device device = DeviceFactory.getInstance().getDevice();
		//device.initDeviceInfo(context);
		mContext=context;
		entity= new DataEntity();
		projectId = "launcher2.0";	
		
		sn = device.getSN();    //@@
		deviceid = device.getAndroidId(context);   //
		mac = device.getMac();   //@@
		wlanmac = device.getWlanMac();  
		model = device.getModel();     
		romVersion = device.getSoftwareVersion();  //@@ 
		deviceVersion = device.getHardwareVersion(); //@@
		
		
		version = DevicesInfoUtils.getVersionName(context);
		launcherVersion = DevicesInfoUtils.getLauncherVersion(context);
		this.tabNo = tabNo;
		this.actionType = actionType;
		this.actionInfo = actionInfo;
		} catch (Exception e) {
			e.printStackTrace();
			}
	}

	
	public void sendPre(Handler mhandler) {
		handler=mhandler;
		entity.setProjectId(projectId);
		entity.setSn(sn);
		entity.setDeviceid(deviceid);
		entity.setMac(mac);
		entity.setWlanmac(wlanmac);
		entity.setVersion(version);
		entity.setModel(model);
		entity.setRomVersion(romVersion);
		entity.setDeviceVersion(deviceVersion);
		entity.setLanucherVersion(launcherVersion);
		entity.setTabNo(tabNo);
		entity.setActionType(actionType);
		entity.setActionInfo(actionInfo);
		entity.setDataUrl(STATISTICS_URL);
		Log.v(TAG, "model="+entity.getModel()+";mac="+entity.getMac()+";sn="+entity.getSn()+";romversion="+entity.getRomVersion());
		send();
	}

	public static void send() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean isSuccess = DataMessageHandler.sendMessage(entity,HiveviewApplication.mContext);
				Log.d(TAG,
						"isSuccess : " + isSuccess + " entity " + entity.getActionInfo() + "  " + entity.getActionType() + " " + entity.getDataUrl()
								+ " " + entity.getDeviceid());
				if(!isSuccess&&handler!=null){
					handler.sendEmptyMessageDelayed(SEND_STATISTICS_FAIL, 5000);
				}
			}
		}).start();
	}
	
}
