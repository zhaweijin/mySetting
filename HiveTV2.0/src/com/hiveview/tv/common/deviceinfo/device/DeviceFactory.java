/**
 * @Title DeviceFactory.java
 * @Package com.hiveview.tv.common.device
 * @author haozening
 * @date 2014年7月24日 下午6:37:36
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.deviceinfo.device;

import java.io.FileOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.deviceinfo.device.impl.DB1001;
import com.hiveview.tv.common.deviceinfo.device.impl.DB1004;
import com.hiveview.tv.common.deviceinfo.device.impl.DB1005;
import com.hiveview.tv.common.deviceinfo.device.impl.DB2116;
import com.hiveview.tv.common.deviceinfo.device.impl.DM1001;
import com.hiveview.tv.common.deviceinfo.device.impl.DM1002;
import com.hiveview.tv.common.deviceinfo.device.impl.DM1003;
import com.hiveview.tv.common.deviceinfo.device.impl.DM1004;
import com.hiveview.tv.common.deviceinfo.device.impl.DM1005;
import com.hiveview.tv.common.deviceinfo.device.impl.DM1102;
import com.hiveview.tv.common.deviceinfo.device.impl.DM2003;
import com.hiveview.tv.common.deviceinfo.device.impl.DM2004;
import com.hiveview.tv.common.deviceinfo.device.impl.DM2005;
import com.hiveview.tv.common.deviceinfo.device.impl.DM2116;

/**
 * 设备工厂类，从此工厂获取当前设备
 * 
 * @ClassName DeviceFactory
 * @Description
 * @author haozening
 * @date 2014年7月24日 下午6:37:36
 * 
 */
public class DeviceFactory {

	private static final String TAG = "DeviceFactory";
	private DeviceList deviceList = new DeviceList();
	private Device chuangweiDevice , tongweiDevice , realtekDevice;

	public static class FactoryHolder {
		public static DeviceFactory factory = new DeviceFactory();
	}

	/**
	 * 获取工厂对象
	 * 
	 * @Title getInstance
	 * @author haozening
	 * @Description
	 * @return
	 */
	public static DeviceFactory getInstance() {
		return FactoryHolder.factory;
	}

	/**
	 * 获取当前设备
	 * 
	 * @Title getDevice
	 * @author haozening
	 * @Description
	 * @return
	 */
	public Device getDevice() {
		Device device=null;
		tongweiDevice = new DeviceTongWei();
		chuangweiDevice = new DeviceChuangWei();
		realtekDevice = new DeviceRTK();
		realtekDevice.initDeviceInfo(HiveviewApplication.mContext);
		Log.v("getDevice", "getDevice==tongweiDevice.getSN()"+tongweiDevice.getSN());
		Log.v("getDevice", "getDevice==chuangweiDevice.getSN()"+chuangweiDevice.getSN());
		Log.v("getDevice", "getDevice==realtekDevice.getSN()"+realtekDevice.getSN());
		Device.sn=null;
		try {
			if (!TextUtils.isEmpty(tongweiDevice.getSN())&&!Device.DEFAULT_SN.equals(tongweiDevice.getSN())) {
				device= tongweiDevice;
			}else
			if (!TextUtils.isEmpty(chuangweiDevice.getSN())&&!Device.DEFAULT_SN.equals(chuangweiDevice.getSN())) {
				device=chuangweiDevice;
			}else
			if (!TextUtils.isEmpty(realtekDevice.getSN())&&!Device.DEFAULT_SN.equals(realtekDevice.getSN())) {
				device= realtekDevice;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 

		StackTraceElement[] elements = new Exception().getStackTrace();
		String comsString = "";
		for (int i = 0; i < elements.length; i++) {
			comsString = comsString + elements[i].toString() + "##";
		}
		String outString = "model=" + device.getModel() + "mac=" + device.getMac() + ";sn=" + device.getSN() + ";rom="
				+ device.getSoftwareVersion();
		writeFileData(HiveviewApplication.mContext, "deviceinfo.log", outString + "###" + comsString + "\n");

		Log.v("getDevice","model="+device.getModel()+"mac=" +device.getMac()+";sn="+device.getSN()+";rom="+device.getSoftwareVersion()+new Exception().getStackTrace()[0].toString());
		return device;
	}

	public static void writeFileData(Context context,String filename, String message){
        try { 
           FileOutputStream fout = context.openFileOutput(filename, Context.MODE_APPEND);//获得FileOutputStream
           //将要写入的字符串转换为byte数组
           byte[]  bytes = message.getBytes();
           fout.write(bytes);//将byte数组写入文件
           fout.close();//关闭文件输出流
       } catch (Exception e) {
           e.printStackTrace();
       }
    }


	/**
	 * 需要生产的设备注册列表
	 * 
	 * @ClassName DeviceRegister
	 * @Description
	 * @author haozening
	 * @date 2014年7月25日 上午10:17:29
	 * 
	 */
	public static class DeviceList extends ArrayList<Device> {
		/**
		 * @Fields serialVersionUID
		 */
		private static final long serialVersionUID = -4986164987294197739L;

		public DeviceList() {
			add(new DM1001());
			add(new DM1002());
			add(new DM1102());
			add(new DM1003());
			add(new DM1004());
			add(new DM1005());
			add(new DM2003());
			add(new DM2004());
			add(new DM2005());
			add(new DM2116());
			add(new DB1001());
			add(new DB1004());
			add(new DB1005());
			add(new DB2116());
		}
	}

}
