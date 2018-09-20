package com.hiveview.cloudtv.settings.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hiveview.cloudtv.display.HiveviewDisplayPositionManager;
import com.hiveview.cloudtv.settings.CommonActivity;
import com.hiveview.cloudtv.settings.DeviceinfoActivity;
import com.hiveview.cloudtv.settings.EthernetConnectedAcivity;
import com.hiveview.cloudtv.settings.bluetooth.LocalBluetoothAdapter;
import com.hiveview.cloudtv.settings.bluetooth.LocalBluetoothManager;
import com.hiveview.cloudtv.settings.ethernet.EthernetDataEntity;
import com.hiveview.cloudtv.settings.videoimage.OutputUiManager;
import com.hiveview.manager.ManufactoryManager;

import android.app.Activity;
import android.provider.Settings;
import android.os.SystemProperties;

import com.droidlogic.app.OutputModeManager;

import android.preference.PreferenceManager;

public class SettingInit {
	 private static final String PREFERENCE_AUTO_OUTPUT_MODE = "hiveview_pref";
	 private static final String PROPERTY_AUTO_OUTPUT_MODE = "auto.output.mode.property";
	 private static final String ACTION_OUTPUTMODE_SAVE = "android.intent.action.OUTPUTMODE_SAVE";
	 private static final String ACTION_OUTPUTMODE_CANCEL = "android.intent.action.OUTPUTMODE_CANCEL";
	 private static final String ACTION_OUTPUTMODE_CHANGE = "android.intent.action.OUTPUTMODE_CHANGE";
	 private static final String STR_DIGIT_AUDIO_OUTPUT = "ubootenv.var.digitaudiooutput";
	 
	 private final String ACTION_OUTPUTPOSITION_CHANGE = "android.intent.action.OUTPUTPOSITION_CHANGE";
	 private final String ACTION_OUTPUTPOSITION_CANCEL = "android.intent.action.OUTPUTPOSITION_CANCEL";
	 private final String ACTION_OUTPUTPOSITION_SAVE = "android.intent.action.OUTPUTPOSITION_SAVE";
	 private final String ACTION_OUTPUTPOSITION_DEFAULT_SAVE = "android.intent.action.OUTPUTPOSITION_DEFAULT_SAVE";
	 private final String OUTPUT_POSITION_X = "output_position_x";
	 private final String OUTPUT_POSITION_Y = "output_position_y";
	 private final String OUTPUT_POSITION_W = "output_position_w";
	 private final String OUTPUT_POSITION_H = "output_position_h";
	 private final String OUTPUT_POSITION_MODE = "output_position_mode";
	 
	 private  final static String mCurrentResolution = "/sys/class/display/mode";
	 private final static String OUTPUT_MODE = "output_mode"; 
	 private static final String PREFERENCE_BOX_SETTING = "preference_box_settings";
	 private  Context mContext;
	 private Activity mActivity;
	 private Handler mHandler;
	 public OutputUiManager outputManager ;
	 
	 private static OutputModeManager mom;
     public  void init(Context context,Activity activity ,Handler handler){
    	mContext=context;
    	mActivity=activity;
    	mHandler =handler;
    	Log.i("wangqihui","i am in thread");
    	new Thread(new InitTherd()).start();
    	
    	
    }
     
     
     private class InitTherd implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			audioInit();
			
	//		netInit();
			Message m = new Message();
			m.what = 2;
			mHandler.sendMessage(m);
		
			adjustInit();
			positionInit();
			boxNameInit();
			screenSaveInit();
			autoPowerInit();
			timeInit();
			developerInit();
			defaultBootVolume();
			defaultInputMethod();
			defalutBluetooth();
			try{
				Thread.sleep(2000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		
			if(null !=DeviceinfoActivity.mProgressDialog && DeviceinfoActivity.mProgressDialog.isShowing()){
				DeviceinfoActivity.stopProgressDialog();
			}
		}
    	 
     }
    
    /**
     * 默认 解码输出
     */
    public  void audioInit(){
    	mom = new OutputModeManager(mContext);
    	mom.setDigitalVoiceValue(OutputModeManager.PCM);
    }
    
    /**
     * 分辨率初始化,统一为 720p
     */
    public  void adjustInit(){
    	 outputManager =new OutputUiManager(mContext);
    	 outputManager.change2NewMode("720p60hz");
    }
    
    public void autoPowerInit(){
    
    	CommonActivity.commitIntPref(mContext, "auto_power_timeout", "auto_power_timeout", Utils.DEFAULT_AUTO_POWER_TIME);
    }
    
  /*
   * 网络手动调整恢复
   */
    
    public  void netInit(){
    
    	EthernetDataEntity mEthernetDataEntity = EthernetDataEntity.getInstance(mContext);
    	boolean isAutoFlag = mEthernetDataEntity.getAutoFlag(true);
        if(!isAutoFlag){
        	mEthernetDataEntity.getEthernetDhcpInfo(true);
        	String mV4DNSAddress =mEthernetDataEntity.getDNSAddress(isAutoFlag);
        	mEthernetDataEntity.updateEthDevInfo(true, null, null, mV4DNSAddress, null);
        }
    
    }
    
    
    /**
     * 手动调整恢复 为 720p的默认值
     */
    
    public void positionInit(){
    /*	 Intent intent_output_position = new Intent(ACTION_OUTPUTPOSITION_CHANGE);
 		intent_output_position.putExtra(OUTPUT_POSITION_X, 0);
 		intent_output_position.putExtra(OUTPUT_POSITION_Y, 0);
 		intent_output_position.putExtra(OUTPUT_POSITION_W, 1280);
 		intent_output_position.putExtra(OUTPUT_POSITION_H, 720);
 		intent_output_position.putExtra(OUTPUT_POSITION_MODE, 0);
 		mContext.sendBroadcast(intent_output_position);
 		
 		Intent intent_output_position2 = new Intent(ACTION_OUTPUTPOSITION_SAVE);
 		intent_output_position2.putExtra(OUTPUT_POSITION_X, 0);
 		intent_output_position2.putExtra(OUTPUT_POSITION_Y, 0);
 		intent_output_position2.putExtra(OUTPUT_POSITION_W, 1280);
 		intent_output_position2.putExtra(OUTPUT_POSITION_H, 720);
 		mContext.sendBroadcast(intent_output_position2);*/
    	HiveviewDisplayPositionManager displaymanager=new HiveviewDisplayPositionManager(mContext);
    	displaymanager.setPosition(0,0,1279,719,0);
    }
    
    
    /**
     * box name init
     */
    public void boxNameInit(){
    	SharedPreferences sharedPref = mContext.getSharedPreferences("box_name",Context.MODE_PRIVATE);
    	Editor ed = sharedPref.edit();
		ed.putInt("boxname", 0);
		ed.commit();
    }
    
    /**
     * 屏保时间设置,默认十分钟开启屏保
     */
    
    public void screenSaveInit(){
    	Settings.System.putInt(mContext.getContentResolver(),"screen_off_timeout", 600000);
		Editor editor = mContext.getSharedPreferences(PREFERENCE_BOX_SETTING, Context.MODE_PRIVATE).edit();
	        editor.putInt("screen_timeout", 600000);
		 editor.commit();   
		
    }
    
    /**
     * 时间显示默认 24小时制
     */
    
    public void timeInit(){
    	if (!Utils.is24Hour(mContext)) {
    		Utils.set24Hour(true, mContext);
		} 
    }
    
    
    /**
     * 开发者模式初始化
     */
    public void developerInit(){
    	//adb 调试
    	/*Utils.setNonMarketAppsAllowed(mActivity,
    			mContext, false);*/
    	if(SystemProperties.get(Utils.PROP_DEVELOPER_ALLOW, "true").equals("true"))
    	   Utils.setNonMarketAppsAllowed(mContext, true);
    	
    	Utils.setAdbDebug(mContext, 0);
    	/*Utils.setAdbDebug(mContext, 1);*/
    }
 
    
    /**
     * 开机音量恢复默认值
     */
    private void defaultBootVolume(){
    	try {
        	ManufactoryManager manufactoryManager = ManufactoryManager.getManufactoryManager();
            manufactoryManager.setBootVolume("0.3");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
    
    
    
    private void defaultInputMethod(){
    	String inputmethod = "com.hiveview.customkeyboard/.SoftKeyboard";
    	ContentResolver resolver = mContext.getContentResolver();
		String inputMethod = Settings.Secure.getString(resolver, 
				Settings.Secure.DEFAULT_INPUT_METHOD);
		if(inputmethod!=null &&!inputMethod.equalsIgnoreCase(inputmethod)){
			Settings.Secure.putString(resolver,
					Settings.Secure.DEFAULT_INPUT_METHOD, inputmethod);
			Settings.Secure.putString(resolver,
					Settings.Secure.ENABLED_INPUT_METHODS, inputmethod);
		}
    }
    
    private void defalutBluetooth(){
    	if(Utils.getBoxType()==Utils.DOMYBOX_S905X_30X || Utils.getBoxType()==Utils.DOMYBOX_S905X_30VC){
        	try {
        		LocalBluetoothManager  mLocalManager = LocalBluetoothManager.getInstance(mContext);;
            	LocalBluetoothAdapter mLocalAdapter = mLocalManager.getBluetoothAdapter();
            
                mLocalAdapter.setBluetoothEnabled(false);
    		} catch (Exception e) {
    			// TODO: handle exception
    		}
    	}
    }
}
