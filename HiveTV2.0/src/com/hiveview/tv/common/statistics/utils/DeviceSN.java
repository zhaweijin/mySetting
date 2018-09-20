package com.hiveview.tv.common.statistics.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import com.hiveview.tv.service.exception.ServiceException;

import android.util.Log;

/**
 * 获取设备SN的类
 * @author haozening
 *
 */
public class DeviceSN {
	
	private static final String TAG = DeviceSN.class.getSimpleName();
	/**
	 * 同维设备型号
	 */
	private static final String DM1001 = "DM1001";
	/**
	 * 创维设备型号
	 */
	private static final String DM1002 = "DM1002";
	/**
	 * 同维读取SN文件
	 */
	private static final String DM1001_SN_NODE = "/sys/class/aml_keys/aml_keys/usid";
	/**
	 * 创维读取SN文件
	 */
	private static final String DM1002_SN_NODE = "/sys/class/mipt_hwconfig/customsn";

	/**
	 * 获取设备SN方法
	 * @return
	 */
	public static String getSN() throws ServiceException{
		Log.d(TAG, getDevice());
		if (getDevice().equals(DM1001)) {
			String dm1001sn;
			try {
				dm1001sn = getDM1001SN();
			} catch (Exception e) {
				throw new ServiceException();
			}
			Log.d(TAG, " DM1001 SN : " + dm1001sn);
			return dm1001sn;
		} else if (getDevice().equals(DM1002)){
			String dm1002sn;
			try {
				dm1002sn = getDM1002SN();
			} catch (Exception e) {
				throw new ServiceException();
			}
			Log.d(TAG, " DM1002 SN : " + dm1002sn);
			return dm1002sn;
		} else {
			Log.e(TAG,"getSN() No Device Matched");
			return "null";
		}
	}
	
	/**
	 * 获取设备版本
	 * @return
	 */
	private static String getDevice() {
		return android.os.Build.MODEL;
	}
	
	/**
	 * 同维SN
	 * @return
	 */
	private static String getDM1001SN() throws ServiceException{
//		return getFlashValueByKeyname("userid");
		String sn;
		try {
			sn = getSerialid(DM1001_SN_NODE);
		} catch (Exception e) {
			throw new ServiceException();
		}
		return sn;
	}
	
	/**
	 * 创维SN
	 * @return
	 */
	private static String getDM1002SN() throws ServiceException{
		String sn;
		try {
			sn = getSerialid(DM1002_SN_NODE);		
		} catch (Exception e) {
			throw new ServiceException();
		}
		return sn;
	}
	
	/**
	 * 同维获取SN方法
	 * @param keyname
	 * @return
	 */
	private static String getFlashValueByKeyname(String keyname) {
        String commandstr = "";
        String resultstr = "";
        String str = "";
        if (keyname == null || keyname.length() == 0)
        {
            return null;
        }
        else
        { 	    
            commandstr = "efuse_tool " + keyname + " read ";            
            //Log.e(TAG, "getFlashValueByKeyname commandstr=" + commandstr);
        }
        
        try {
            Process pp = Runtime.getRuntime().exec(commandstr);
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            
            for (; null != str;) {
                str = input.readLine();
                if (str != null) {
                    resultstr = str.trim();
                    //Log.e(TAG,"getFlashValueByKeyname str.trim() ="+ resultstr);
                    int index = resultstr.indexOf("=");
                    if (index == -1)
                        return null;                    
                    //Log.e(TAG,"getFlashValueByKeyname index="+ index);
                    resultstr = resultstr.substring(index+1);                    
                    //Log.e(TAG,"getFlashValueByKeyname resultstr="+ resultstr);
                    break;
                }
            }
        } catch (IOException ex) {            
            ex.printStackTrace();
        }
        return resultstr;
    }

	/**
	 * 创维获取SN方法
	 * @return
	 */
	private static String getSerialid(String name) throws ServiceException {
		// 客户化SN号的读取，读取路径更改为：/sys/class/mipt_hwconfig/customsn 
	//	File file = new File("/sys/class/mipt_hwconfig/serialid");
		File file = new File(name);
		if (!file.exists()) {
			return "";
		}
		BufferedReader reader = null;
		String str = null;
		StringBuilder builder = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			while ((str = reader.readLine()) != null) {
				builder.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new ServiceException();
				}
			}
		}
		return builder.toString().trim();
	}
	

}
