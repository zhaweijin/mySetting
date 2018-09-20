package com.hiveview.cloudtv.settings.pppoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.SystemProperties;
import android.util.Log;

import android.os.IBinder;
import android.os.ServiceManager;

import com.droidlogic.pppoe.PppoeManager;
import com.droidlogic.pppoe.IPppoeManager;
import com.droidlogic.pppoe.PppoeStateTracker;
import com.droidlogic.pppoe.PppoeDevInfo;
import com.droidlogic.app.SystemControlManager;


import com.amlogic.pppoe.PppoeOperation;

public class PppoeDataEntity {
	private static final String TAG = "Pppoe";
    public static final int PPPOE_STATE_UNDEFINED = 0;
    public static final int PPPOE_STATE_DISCONNECTED = 1;
    public static final int PPPOE_STATE_CONNECTING = 2;
    public static final int PPPOE_STATE_DISCONNECTING = 3;
    public static final int PPPOE_STATE_CONNECT_FAILED = 4;
    public static final int PPPOE_STATE_CONNECTED = 5;
	public static final int PPPOE_STATE_PLUGOUT = 6;
    
    public static final String PPPOE_RUNNING_FLAG = "net.pppoe.running";
    public static final String SYS_PRO_PPP_CONN_STA = "net.pppoe.isConnected";
    public static final String PPPOE_INTERNET_INTERFACE = "eth0";
    public static final String SHARE_PREFERENCE_FILE_NAME = "inputdata";
    public static final String INFO_AUTOCONNECT = "auto_dial_flag";
    public static final String INFO_USERNAME = "name";
    public static final String INFO_PASSWORD = "passwd";
    public static final String ETHERNET_DHCP_REPEAT_FLAG = "net.dhcp.repeat";
    public static final String PPPOE_EXTRA_STATE = "pppoe_state";
    public static final boolean PPPOE_EXTRA_STATE_CONNECT = true;
    public static final boolean PPPOE_EXTRA_STATE_DISCONNECT = false;
    
    
	private Context mContext = null;
	private static PppoeDataEntity mPppoeDataEntity = null;
	private PppoeOperation mPppOperation = null;
	private PppoeManager mPppoeManager = null;
	private PppoeDevInfo mPppoeDevinfo = null;


	
	private static int mStatus = PPPOE_STATE_UNDEFINED;  //false:normal true:plugou
	
	public static PppoeDataEntity getInstance(Context context){
		if(mPppoeDataEntity==null){
			mPppoeDataEntity = new PppoeDataEntity(context);
		}
		return mPppoeDataEntity;
	}
	
	public PppoeDataEntity(Context context){
		mContext = context;
		mPppOperation = new PppoeOperation();
		try {
			IBinder b = ServiceManager.getService("pppoe");
			IPppoeManager PppoeService = IPppoeManager.Stub.asInterface(b);
			mPppoeManager = new PppoeManager(PppoeService, mContext);

			if(mPppoeManager.isPppoeConfigured())
			{
				Log.d(TAG, "is pppoe configured: ");
				
				mPppoeDevinfo = mPppoeManager.getSavedPppoeConfig();
				if (mPppoeDevinfo != null) {
					Log.d(TAG, "IP: " + mPppoeDevinfo.getIpAddress());
					Log.d(TAG, "MASK: " + mPppoeDevinfo.getNetMask());
					Log.d(TAG, "GW: " + mPppoeDevinfo.getRouteAddr());
					Log.d(TAG, "DNS: " + mPppoeDevinfo.getDnsAddr());
				}

			}
			else
			{
				Log.d(TAG, "is pppoe not configured" );
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	public static int getPppoeStatus()
	{
		return mStatus;
	}


	public static void	SetPppoeStatus(int Status)
	{
		if(Status == PPPOE_STATE_CONNECTED)
		{
			Log.d(TAG, "PPPOE_STATE_CONNECTED");
			SystemProperties.set(ETHERNET_DHCP_REPEAT_FLAG, "disabled");
			SystemProperties.set(PppoeDataEntity.PPPOE_RUNNING_FLAG, "100");
			
			mStatus = PPPOE_STATE_CONNECTED;
		}
		else if(Status == PPPOE_STATE_DISCONNECTED)
		{
			Log.d(TAG, "PPPOE_STATE_DISCONNECTED");
			SystemProperties.set(ETHERNET_DHCP_REPEAT_FLAG, "enabled");
			SystemProperties.set(PppoeDataEntity.PPPOE_RUNNING_FLAG, "0");
			if(mStatus != PPPOE_STATE_PLUGOUT)
			{
				mStatus = PPPOE_STATE_DISCONNECTED;
			}
		}
		else if(Status == PPPOE_STATE_PLUGOUT)
		{
			Log.d(TAG, "PPPOE_STATE_PLUGOUT");
			mStatus = PPPOE_STATE_PLUGOUT;
		}
		else if (Status == PPPOE_STATE_CONNECT_FAILED)
		{
			Log.d(TAG, "PPPOE_STATE_CONNECT_FAILED");
			mStatus = PPPOE_STATE_CONNECT_FAILED;
		}
		else
		{
			mStatus = Status;
			Log.d(TAG, "mStatus other " + mStatus);
		}
	
	}

	public boolean isPppoeRunning()
	{
		if("100".equals(SystemProperties.get(PppoeDataEntity.PPPOE_RUNNING_FLAG)))
			return true;
		else	
			return false;
	}
	
	public String getPppoeName() {
		SharedPreferences sharedata = mContext.getSharedPreferences(SHARE_PREFERENCE_FILE_NAME, Context.MODE_MULTI_PROCESS);
		return sharedata.getString(PppoeDataEntity.INFO_USERNAME, null);
	}

	public void setPppoeName(String userName){
		SharedPreferences sharedata = mContext.getSharedPreferences(SHARE_PREFERENCE_FILE_NAME, Context.MODE_MULTI_PROCESS);
	    SharedPreferences.Editor editor = sharedata.edit();
	    editor.putString(INFO_USERNAME, userName);
	    editor.commit();
	}
	
	public String getPppoePassword() {
		SharedPreferences sharedata = mContext.getSharedPreferences(SHARE_PREFERENCE_FILE_NAME, Context.MODE_MULTI_PROCESS);
		return sharedata.getString(PppoeDataEntity.INFO_PASSWORD, null);
	}
	
	public void setPppoePassword(String password){
	    SharedPreferences sharedata = mContext.getSharedPreferences(SHARE_PREFERENCE_FILE_NAME, Context.MODE_MULTI_PROCESS);
	    SharedPreferences.Editor editor = sharedata.edit();
	    editor.putString(INFO_PASSWORD, password);
	    editor.commit();
	}
	
	public boolean getPppoeAutoConnectFlag(){
		SharedPreferences sharedata = mContext.getSharedPreferences(SHARE_PREFERENCE_FILE_NAME,Context.MODE_MULTI_PROCESS);
		return sharedata.getBoolean(PppoeDataEntity.INFO_AUTOCONNECT, true);
	}
	
	public void setPppoeAutoConnectFlag(boolean autoConnect){
		SharedPreferences sharedata = mContext.getSharedPreferences(SHARE_PREFERENCE_FILE_NAME,Context.MODE_MULTI_PROCESS);
		Editor editor = sharedata.edit();
		editor.putBoolean(PppoeDataEntity.INFO_AUTOCONNECT, autoConnect);
		editor.commit();
	}
	
	private  void setPppoeRunningFlag() {
		SystemProperties.set(ETHERNET_DHCP_REPEAT_FLAG, "disabled");
		SystemProperties.set(PppoeDataEntity.PPPOE_RUNNING_FLAG, "100");
		String propVal = SystemProperties.get(PppoeDataEntity.PPPOE_RUNNING_FLAG);
		int n = 0;
		if (propVal.length() != 0) {
			try {
				n = Integer.parseInt(propVal);
			} catch (NumberFormatException e) {
			}
		} else {
			Log.e(TAG, "failed to setPppoeRunningFlag");
		}

		return;
	}

	private void clearPppoeRunningFlag() {
		SystemProperties.set(ETHERNET_DHCP_REPEAT_FLAG, "enabled");
		SystemProperties.set(PppoeDataEntity.PPPOE_RUNNING_FLAG, "0");
		String propVal = SystemProperties.get(PppoeDataEntity.PPPOE_RUNNING_FLAG);
		int n = 0;
		if (propVal.length() != 0) {
			try {
				n = Integer.parseInt(propVal);
			} catch (NumberFormatException e) {
			}
		} else {
			Log.e(TAG, "failed to clearPppoeRunningFlag");
		}

		return;
	}
	
	public int getPppoeConnState() {
		if(mPppOperation == null){
			Log.e(TAG, "mPppOperation is null.......");
			return -1;
		}
		return mPppOperation.status(PppoeDataEntity.PPPOE_INTERNET_INTERFACE);
	}
	
	public boolean connect(String device, String name, String password){
		if(mPppOperation == null){
			Log.e(TAG, "mPppOperation is null.......");
			return false;
		}
		
		setPppoeRunningFlag();
		return mPppOperation.connect(device, name, password);
	}
	
	public boolean disconnect(){
		if(mPppOperation == null){
			Log.e(TAG, "mPppOperation is null.......");
			return false;
		}
		
		setPppoeRunningFlag();
		return  mPppOperation.disconnect();
	}

	public boolean terminate(){
		if(mPppOperation == null){
			Log.e(TAG, "mPppOperation is null.......");
			return false;
		}
		return  mPppOperation.terminate();
	}

	
	public boolean getPppoeDevInfo(){
		boolean result = false;
		try {
			if(mPppoeManager!=null){
				mPppoeDevinfo = mPppoeManager.getSavedPppoeConfig();
				result = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return result;
	}
	
	public String getPppoeIpAddress(){
		if(mPppoeDevinfo==null){
			Log.e(TAG, "getPppoeIpAddress mPppoeDevinfo is null.........");
			return "0.0.0.0";
		}
		return  mPppoeDevinfo.getIpAddress();
	}
	
	public String getPppoeNetmask(){
		if(mPppoeDevinfo==null){
			Log.e(TAG, "getPppoeNetmask mPppoeDevinfo is null.........");
			return "255.255.255.0";
		}
		return  mPppoeDevinfo.getNetMask();
	}
	
	public String getPppoeDnsAddress(){
		if(mPppoeDevinfo==null){
			Log.e(TAG, "getPppoeDnsAddress mPppoeDevinfo is null.........");			
			return "0.0.0.0";
			
		}
		return  mPppoeDevinfo.getDnsAddr();
	}
	
	public String getPppoeRouteAddress(){
		if(mPppoeDevinfo==null){
			Log.e(TAG, "getPppoeRouteAddress mPppoeDevinfo is null.........");
			return "0.0.0.0";
		}
		return  mPppoeDevinfo.getRouteAddr();
	}
}
