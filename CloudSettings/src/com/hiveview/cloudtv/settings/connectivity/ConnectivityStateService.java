package com.hiveview.cloudtv.settings.connectivity;

import android.R.bool;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;

import android.widget.Toast;
import com.android.server.net.BaseNetworkObserver;
import android.os.INetworkManagementService;
import android.os.ServiceManager;

import com.amlogic.pppoe.PppoeOperation;
import com.droidlogic.app.SystemControlManager;
import com.hiveview.cloudtv.settings.pppoe.PppoeDataEntity;




public class ConnectivityStateService extends Service {
	private static final String TAG = "ConnectivityStateService";
	private final boolean DEBUG = true;
	public static final String  HW_INTENT				= "com.hiveview.linkstatus.change";
	public static final String  HW_INTENT_EXTRA			= "HW_STATUS";
	public static final int  HW_STATUS_UNKNOW 			= -1;
	public static final int  HW_STATUS_DISCONNECT 		= 0;
	public static final int  HW_STATUS_CONNECT 			= 1;

	public PppoeDataEntity 		mPppoeDataEntity 		= null;
	public static SystemControlManager 	mSystemControlManager	= null;
	
    private InterfaceObserver 			mInterfaceObserver;
    private INetworkManagementService 	mNMService;


	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}


	@Override
	public void onCreate() {
		super.onCreate();

        Log.v(TAG, ">>>>>oncreate");
        IBinder b = ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE);
        mNMService = INetworkManagementService.Stub.asInterface(b);
        mInterfaceObserver = new InterfaceObserver();
        try {
            mNMService.registerObserver(mInterfaceObserver);
        } catch (RemoteException e) {
            Log.e(TAG, "Could not register InterfaceObserver " + e);
        }

		try{
			mSystemControlManager = new SystemControlManager(this);
			
			Intent intent1 = new Intent(HW_INTENT);
			if(mSystemControlManager.readSysFs("/sys/class/ethernet/linkspeed").contains("unlink"))
			{
				intent1.putExtra(HW_INTENT_EXTRA, HW_STATUS_DISCONNECT);
			}
			else
			{
				intent1.putExtra(HW_INTENT_EXTRA, HW_STATUS_CONNECT);
			}		
			//this.sendBroadcast(intent1);
			this.sendBroadcastAsUser(intent1, UserHandle.ALL);
		}catch (Exception e) {
			Log.e(TAG, "error-----------------------except onCreate");
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
 
    private void updateInterfaceState(String iface, boolean up) {
        Intent intent = new Intent(HW_INTENT);
		Log.d(TAG , "updateInterfaceState = " + iface + "  " + up);
		try{
	        if (!iface.contains("eth0")) {
	            return;
	        }
	        if (up) 
			{
				intent.putExtra(HW_INTENT_EXTRA, HW_STATUS_CONNECT);
	        }
			else
			{
				intent.putExtra(HW_INTENT_EXTRA, HW_STATUS_DISCONNECT);
			}
			
			//this.sendBroadcast(intent);
			this.sendBroadcastAsUser(intent, UserHandle.ALL);
		
		}catch (Exception e) {
            Log.e(TAG, "error-----------------------except  updateInterfaceState");
			e.printStackTrace();
		}
    }

	
	private class InterfaceObserver extends BaseNetworkObserver {
		@Override
		public void interfaceLinkStateChanged(String iface, boolean up) {
			updateInterfaceState(iface, up);
		}
	}


}
