package com.hiveview.cloudtv.settings;

import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.EthernetManager;

import com.droidlogic.pppoe.PppoeManager;
import com.droidlogic.pppoe.PppoeStateTracker;

import android.net.wifi.WifiManager;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.amlogic.pppoe.PppoeOperation;
import com.hiveview.cloudtv.settings.pppoe.PppoeDataEntity;
import com.hiveview.cloudtv.settings.upload.DataMessageHandler;
import com.hiveview.cloudtv.settings.util.Utils;
import com.hiveview.cloudtv.settings.widget.LegalCheck;
import com.hiveview.cloudtv.settings.widget.ResetDialog;
import com.hiveview.cloudtv.settings.connectivity.ConnectivityStateService;



public class BootCompletedReceiverS905 extends BroadcastReceiver {
	private static final String TAG = "BootAML";
	private static final boolean DEBUG = true;
	private WifiManager mWifiManager = null;
	private EthernetManager mEthMng = null;
	private PppoeDataEntity mPppoeDataEntity = null;
	private Context mContext;

	private static SharedPreferences mSharePreferences = null;
	private static boolean mSwitchNetState = false; 
	private static boolean mIsFirst = false;

	private final String CLASS_NAME = "SoftAPConfigFragment";
 
	@Override
	public void onReceive(Context context, Intent intent) {
		if(DEBUG){
			Log.e(TAG, "onReceive......................."+intent.getAction());
		}
		if(SystemProperties.getInt("persist.sys.factory.mode", 0) == 1){
    		return;
    	}
		mContext = context;
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mEthMng = (EthernetManager) context.getSystemService(Context.ETHERNET_SERVICE);
		mSharePreferences = context.getSharedPreferences(CLASS_NAME, Context.MODE_PRIVATE);
		 
		//if (Utils.isOverseas()) {
			startAirPlay(context);
		//}
		
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			if(DEBUG){
				Log.e(TAG, "ACTION_BOOT_COMPLETED.......................");
			}
			
			if(SystemProperties.get(Utils.PROP_DEVELOPER_ALLOW, "true").equals("true"))
			    Utils.setNonMarketAppsAllowed(context, true);
			
			mPppoeDataEntity = PppoeDataEntity.getInstance(context);

			int currentTimeout =Settings.System.getInt(context.getContentResolver(), SCREEN_OFF_TIMEOUT,
					600000);
			Log.i(TAG, "currentTimeout is "+currentTimeout);
		
			if(currentTimeout ==3600000){
				currentTimeout=600000;
			}
			
			//去掉锁网功能
//			new LegalCheck().check(mContext);
			
			Log.i(TAG, "currentTimeout is "+currentTimeout);
			Settings.System.putInt(context.getContentResolver(), "screen_off_timeout",currentTimeout);
		
			Intent intent1 = new Intent();
			intent1.setAction("com.hiveview.connectivity.action.Main");
			context.startService(intent1);

			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					doNetWork(mContext, false);
					connectPPPoe(mContext);
				}
			}).start();
			
			mIsFirst = true;
			
			Log.v(TAG, "boot ethernet status="+Utils.checkEthState());
			if(Utils.checkEthState())
			  bootEnableWifiAp();
			mContext.sendBroadcast(new Intent("com.hiveview.hiveviewdog.action.autopowertimeout"));
			//
			//延迟10分钟上传服务器，若失败，再隔20分钟上传一次。最多上传两次！
			  /*Date date = new Date(System.currentTimeMillis());
				SimpleDateFormat dateFormat = new SimpleDateFormat("_yyyyMMdd_HHmmss");
				Log.i("=======", "===进入时间：" + dateFormat.format(date));*/
		        /*final TimerTask task = new TimerTask()
		        {  
		            public void run()
		            {  
		               //execute the task 
		            	boolean b=DataMessageHandler.sendMessage(mContext);
		            	if(!b){
		            		execute();
		            	}
		            }  
		        };  
		        Timer timer = new Timer();
		        //600000
		        timer.schedule(task,600000);*/
			/*Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat("_yyyyMMdd_HHmmss");
			Log.i("===jdgb接到广播===", dateFormat.format(date));*/
			if(!Utils.isOverseas()){//国内版传埋点，海外不传
				 new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								for(int i=0;i<600;i++){
									Thread.sleep(1000);
								}
								
								/*Date date2 = new Date(System.currentTimeMillis());
								SimpleDateFormat dateFormat2 = new SimpleDateFormat("_yyyyMMdd_HHmmss");
								Log.i("===kszxsc开始执行上传===", dateFormat2.format(date2));*/
								boolean b=DataMessageHandler.sendMessage(mContext);
				            	if(!b){
				            		/*Date date3 = new Date(System.currentTimeMillis());
									SimpleDateFormat dateFormat3 = new SimpleDateFormat("_yyyyMMdd_HHmmss");
									Log.i("===jrzxdecsc进入执行第二次上传===", dateFormat3.format(date3));*/
				            		//execute();
									for(int i=0;i<1200;i++){
										Thread.sleep(1000);	
									}
									
									/*Date date4 = new Date(System.currentTimeMillis());
									SimpleDateFormat dateFormat4 = new SimpleDateFormat("_yyyyMMdd_HHmmss");
									Log.i("===kszxdecsc开始执行第二次上传===", dateFormat4.format(date4));*/
									DataMessageHandler.sendMessage(mContext);
				            	}
								
							} catch (Exception e) {
								// TODO: handle exception
							}
						
							
						}
					}).start();
			}
           
			
			
			
			
			
			
		} else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			if(DEBUG){
				//Log.e(TAG, "CONNECTIVITY_ACTION.......................donetwork S905 no need");
			}
			
		} else if (intent.getAction().equals(PppoeManager.PPPOE_STATE_CHANGED_ACTION)) {
			Log.e(TAG, "PPPOE_STATE_CHANGED_ACTION");
			int event = intent.getIntExtra(PppoeManager.EXTRA_PPPOE_STATE, PppoeManager.PPPOE_STATE_UNKNOWN);
			if (event == PppoeStateTracker.EVENT_CONNECTED) {
				if(DEBUG){
					Log.e(TAG, "EVENT_CONNECTED ");
				}
				SystemProperties.set(PppoeDataEntity.SYS_PRO_PPP_CONN_STA, "true");
				PppoeDataEntity.SetPppoeStatus(PppoeDataEntity.PPPOE_STATE_CONNECTED);


			} else if (event == PppoeStateTracker.EVENT_DISCONNECTED) {
				if(DEBUG){
					Log.e(TAG, "EVENT_DISCONNECTED ");
				}
				//mPppoeDataEntity.clearPppoeRunningFlag();
				SystemProperties.set(PppoeDataEntity.SYS_PRO_PPP_CONN_STA, "false");
				PppoeDataEntity.SetPppoeStatus(PppoeDataEntity.PPPOE_STATE_DISCONNECTED);
				
			} else if (event == PppoeStateTracker.EVENT_CONNECT_FAILED) {
				if(DEBUG){
					Log.e(TAG, "EVENT_CONNECT_FAILED ");
				}
				String ppp_err = intent.getStringExtra(PppoeManager.EXTRA_PPPOE_ERRCODE);
				//mPppoeDataEntity.clearPppoeRunningFlag();
				
				SystemProperties.set(PppoeDataEntity.SYS_PRO_PPP_CONN_STA, "false");
				PppoeDataEntity.SetPppoeStatus(PppoeDataEntity.PPPOE_STATE_CONNECT_FAILED);
				
			}
		}
		else if(intent.getAction().equals(ConnectivityStateService.HW_INTENT))
		{
			if(!mIsFirst)
				return;

			
			Log.e(TAG, "================com.hiveview.linkstatus.change================="+Utils.checkEthState());
			int event = intent.getIntExtra(ConnectivityStateService.HW_INTENT_EXTRA,ConnectivityStateService.HW_STATUS_UNKNOW);
			if(event == ConnectivityStateService.HW_STATUS_CONNECT)
			{
				Log.v(TAG, "ethernet");
				//close wifi
				if (mWifiManager.isWifiEnabled()) {
					mWifiManager.setWifiEnabled(false);
					Log.i(TAG, "wifi close......................");
					
					//for ping plan
					Intent mintent = new Intent("android.net.ethernet.ETH_DNS_CHANGED");
					mContext.sendBroadcast(mintent);
				}
				
				if (mSharePreferences.getBoolean("wifi_ap_auto_open", false)) {
                    if (mWifiManager.getWifiApState() == WifiManager.WIFI_AP_STATE_DISABLED
                                    || mWifiManager.getWifiApState() == WifiManager.WIFI_AP_STATE_DISABLING
                                    || mWifiManager.getWifiApState() == WifiManager.WIFI_AP_STATE_FAILED) {
                            Log.v(TAG, "enableWifiAP");
                            mWifiManager.setWifiApEnabled(null, true);
                    }
                }
			}
			else
			{
				Log.v(TAG, "wifi state=="+mWifiManager.getWifiState());
				
				if(Utils.checkEthState())
					return;
				
				if (mWifiManager.getWifiApState() == WifiManager.WIFI_AP_STATE_ENABLED
						|| mWifiManager.getWifiApState() == WifiManager.WIFI_AP_STATE_ENABLING) {
					Log.v(TAG, "disableWifiAP");
					mWifiManager.setWifiApEnabled(null, false);
					try{
					   Thread.sleep(500);
					}catch(Exception e){
					  e.printStackTrace();
					}
				}
 
				//start wifi
				if (!mWifiManager.isWifiEnabled()) {
					mWifiManager.setWifiEnabled(true);
					if(DEBUG){
						Log.i(TAG, "wifi open....................");
					}
					
					//for ping plan
					Intent mintent = new Intent("android.net.ethernet.ETH_DNS_CHANGED");
					mContext.sendBroadcast(mintent);
				}
			}
 
		}
		
	}

		
	 
	@SuppressLint("NewApi")
	private void connectPPPoe(Context context) {
 
		if (Utils.checkEthState()) {

			String name = mPppoeDataEntity.getPppoeName();
			String password =mPppoeDataEntity.getPppoePassword();

			Log.e(TAG, "name ="+name);
			Log.e(TAG, "password="+password);
			Log.e(TAG, "gateway="+Utils.getGateway(mContext));

	 
			try {

				boolean autoConnect = mPppoeDataEntity.getPppoeAutoConnectFlag();
				if(DEBUG){
					Log.e(TAG, "CONNECTIVITY_ACTION.......................pppoe autoConnect="+autoConnect);
				}
				if (autoConnect) {
					mPppoeDataEntity.setPppoeAutoConnectFlag(autoConnect);
					
					if(DEBUG){
						Log.e(TAG, "CONNECTIVITY_ACTION.......................pppoe state-- " + mPppoeDataEntity.getPppoeConnState());
					}
					if (null == name || name.isEmpty() || null == password || password.isEmpty()) {
						if(DEBUG){
							Log.e(TAG, "name  or password is null ");
						}
						return;
					}
					if (mPppoeDataEntity.getPppoeConnState() != PppoeOperation.PPP_STATUS_CONNECTED) {

						((WifiManager) mContext.getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(false);

						// not need mPppoeDataEntity.setPppoeRunningFlag();
						mPppoeDataEntity.connect(PppoeDataEntity.PPPOE_INTERNET_INTERFACE, name, password);
						if(DEBUG){
							Log.e(TAG, "CONNECTIVITY_ACTION.......................pppoe connecting");
						}

					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

 

	
	private void doNetWork(Context context, boolean needConnectPpp) {
	 
			if (Utils.checkEthState()) {
				if(DEBUG){
					Log.e(TAG, "CONNECTIVITY_ACTION.......................ethnet");
				}
				Log.v(TAG, "wifi status>>"+mWifiManager.getWifiState());
				if (mWifiManager.isWifiEnabled()) {
					mWifiManager.setWifiEnabled(false);
					if(DEBUG){
						Log.i(TAG, "wifi close......................");
					}
				}
			} else {
				if(DEBUG){
					Log.e(TAG, "CONNECTIVITY_ACTION.......................wifi=="+mWifiManager.getWifiState());
				}
				if (!mWifiManager.isWifiEnabled()) {
					if(mWifiManager.getWifiApState()== WifiManager.WIFI_AP_STATE_ENABLED||
						mWifiManager.getWifiApState()==WifiManager.WIFI_AP_STATE_ENABLING){
						mWifiManager.setWifiApEnabled(null, false);
						try{
						   Thread.sleep(500);
						}catch(Exception e){
						   e.printStackTrace();
						}
					}
					mWifiManager.setWifiEnabled(true);
					if(DEBUG){
						Log.i(TAG, "wifi open....................");
					}
				}
			}
	}
	
	private void bootEnableWifiAp() {
		if (mSharePreferences.getBoolean("wifi_ap_auto_open", false)) {
			Log.v(TAG, "wifi ap state---" + mWifiManager.getWifiApState());
			if (mWifiManager.getWifiApState() != WifiManager.WIFI_AP_STATE_ENABLED) {
				Log.v(TAG, "enableWifiAP");
				mWifiManager.setWifiApEnabled(null, true);
			}
		}
	}
	  public void execute(){
	    	TimerTask task2 = new TimerTask(){   

	    	    public void run(){   

	    	    //execute the task 
	    	   /* Date date3 = new Date(System.currentTimeMillis());
      		SimpleDateFormat dateFormat3 = new SimpleDateFormat("_yyyyMMdd_HHmmss");
      		Log.i("=======", "===第二次开始执行时间：" + dateFormat3.format(date3));*/
	    	        DataMessageHandler.sendMessage(mContext);

	    	    }   

	    	};   

	    	Timer timer2 = new Timer(); 
            //1200000
	    	timer2.schedule(task2, 1200000);
	    }
	 
	  /**
		 * 启动乐播投屏服务
		 * @param context
		 */
		private void startAirPlay(Context context){
			try {
				Intent service = new Intent();
				ComponentName comp = new ComponentName("com.hpplay.happyplay.aw","com.hpplay.happyplay.aw.AirPlayService");
				service.setComponent(comp);
				context.startService(service);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
}
