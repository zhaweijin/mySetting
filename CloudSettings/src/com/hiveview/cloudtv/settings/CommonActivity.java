package com.hiveview.cloudtv.settings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParserException;

import com.hiveview.manager.ManufactoryManager;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.SystemProperties;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;

import com.hiveview.cloudtv.common.CommonItemList;
import com.hiveview.cloudtv.common.FileTitleTextView;
import com.hiveview.cloudtv.common.Input;
import com.hiveview.cloudtv.settings.util.ImageSharePreference;
import com.hiveview.cloudtv.settings.util.ToastUtils;
import com.hiveview.cloudtv.settings.util.Utils;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView.FocusViewAnimatorEndListener;
import com.hiveview.cloudtv.settings.widget.ResetDialog;
import com.hiveview.manager.ManufactoryManager;

public class CommonActivity extends Activity {
	final String TAG = "CommonActivity";
	int[] flag = new int[13];
	List<CommonItemList> mCommonItemList = new ArrayList<CommonItemList>();
	private String[] mArrays;
	private String[] WheatCode;
	private Drawable[] pageLefts = new Drawable[13];
	private Drawable[] pageRights = new Drawable[13];
	private String[] mItemSettings = new String[13];
	private long mKeyDownTime = 0L;
	private RelativeLayout mRelative = null;
	private CommonSettingOnList commonSettingOnListAdapter = null;
	private ListView commonSettingListView = null;
	public int gameSettingId = 0;
	public int monitorProtectId = 0;
	public int autopowerId=0;
	public int sleepTimeId = 0;
	public int themeColor = 0;
	public int timeTypeId = 0;
	public int volumeSelected=0;
	private int mTvNameSelectId = 0;
	private int  inputId=0;//数据源的位置
	private int wheatCodeSelectId = 0;
	private int languageId=0;
	private String[] languageDesc;
	private final BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
	PowerManager pm ;
	private LauncherFocusView focusView;
	public Context mContext = this;
	public Activity activity = this;
	public static Bitmap originalBitmap = null;
	public static Bitmap blurBitmap = null;
	public static final int RIGHT_ARROW_SMALL = 0;
	public static final int RIGHT_ARROW_BIG = 1;
	
	CharSequence[] screenTimeoutEntries, dreamTimeoutEntries;
	CharSequence[] screenTimeoutEntryValues, dreamTimeoutEntryValues;
    private String[] autopowerTimeoutEntrieValues,autopowerTimeoutEntries;

	private int TV_NAME = 100;
	private int PROTECT_MONITOR_TIME = 100;
	private int TIME_TYPE = 100;
	private int TIMEZONE_SETTINGS = 100;
	private int DEVELOPER_SETTINGS = 100;
	private int VERSION_CHECK = 100;
	private int RESET = 100;
	private int SENDLOG = 100;
	private int MAIBISUO =  100;
    private int AUTO_POWER_TIME = 100;  //hdmi 自动关屏
    private int LANGUAGE_SELECT = 100; //语言选择
    private int BLUETOOTH_SETTING = 100;
    private int KeyMenuCount = 0;
    private int INPUT=100;
    private int CONTROL_SETTING=100;
	private int FALLBACK_SCREEN_TIMEOUT_VALUE = 300000;
	public boolean bfocusViewInitStatus = true;
	private int mCurKeycode = KeyEvent.KEYCODE_0;
	private boolean mTextColorChangeFlag = false;
	private boolean mFocusAnimationEndFlag = false;
	private int mItemListCurPosition = -1;
	private TextView mTextView = null;
	private TextView mTextViewSetting = null;
	private ImageView mImageView = null;
	public int volumeValue = 0;
	private View mView;
	private String mKEDA = "com.iflytek.showcomesettings";
	private static final String PREFERENCE_BOX_SETTING = "preference_box_settings";

	final int BT_AUTOPAIR_FAILED = 0;
	final int BT_AUTOPAIR_NOTPAIR = 1;
	final int BT_AUTOPAIR_PAIRING = 2;
	final int BT_AUTOPAIR_PAIRED = 3;
	final int BT_AUTOPAIR_CONNECTING = 4;
	final int BT_AUTOPAIR_CONNECTED = 5;
	final int BT_AUTOPAIR_CONNECT_OK = 7;
	final int BT_AUTOPAIR_NEED_CONNECT = 8;

	private static final String GET_WHEAT_LOCK_INFO = "GET_WHEAT_LOCK_INFO";	
	private static final String SET_WHEAT_LOCK_INFO = "SET_WHEAT_LOCK_INFO";	
	public static final Uri URI_INSTALL_APK_INFO = Uri.parse("content://HiveViewCloudPayAuthorities/TABLE_WhearLock");
	private final String LOCK_OFF_S = "0";	
	private final String LOCK_ON_S = "1";
		
	private final int LOCK_OFF= 0;
	private final int LOCK_ON =1;
	
	private int LIST_SIZE = 0;
	private ManufactoryManager manufactoryManager;
	private List<Input> inputObjList=new ArrayList<Input>();
	private String packageNameHidden[] = {"com.android.inputmethod.latin","jp.co.omronsoft.openwnn","com.amlogic.inputmethod.remote","net.sunniwell.inputmethod.swpinyin2"};
	private boolean b=false;
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				onInitSelect();
				commonSettingListView.setSelection(0);
				break;
			default:
				break;
			}
			
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);
		commonSettingListView = (ListView) findViewById(R.id.com_setting_list);
		//获取系统输入法的名字
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		List<InputMethodInfo> methodList = imm.getInputMethodList();
		for(InputMethodInfo mi : methodList ) {
		    String name = (String)mi.loadLabel(getPackageManager());
		    String packageName = mi.getPackageName();
		    String id=mi.getId();
		    //id:com.hiveview.customkeyboard/.SoftKeyboard
		    //===输入法的包名：com.hiveview.customkeyboard
		    if(packageName.equals("com.hiveview.customkeyboard")){
		    	name="大麦输入法";
		    }
		    //屏蔽某些输入法的处理
		    boolean hidden=false;
		    for(int i=0;i<packageNameHidden.length;i++){
		    	if(packageName.equals(packageNameHidden[i])){
		    		hidden=true;
		    		break;
		    	}
		    }
		    if(!hidden){//不屏蔽的输入法
		    	Input inputItem=new Input();
		    	inputItem.setId(id);
		    	inputItem.setName(name);
		    	inputObjList.add(inputItem);
		    }
		    
		}
		CommonItemDataInit();
		CommonItemListInit();
		onBlindListener();
		registerReceiver();
		regReceiver();
	}
 
	
	private String getTimeZoneCity(){
 
		TimeZone tz = TimeZone.getDefault();
		String displayName="";
        try {
            XmlResourceParser xrp = mContext.getResources().getXml(R.xml.timezones);
            while (xrp.next() != XmlResourceParser.START_TAG)
                continue;
            xrp.next();
            while (xrp.getEventType() != XmlResourceParser.END_TAG) {
                while (xrp.getEventType() != XmlResourceParser.START_TAG) {
                    if (xrp.getEventType() == XmlResourceParser.END_DOCUMENT) {
                    //    return myData;
                    }
                    xrp.next();
                }
                if (xrp.getName().equals(TimezoneActivity.XMLTAG_TIMEZONE)) {
                    String id = xrp.getAttributeValue(0);
                    displayName = xrp.nextText();
                    if(tz.getID().equals(id)){
                    	Log.v(TAG, "display name="+displayName);
                    	break;
                    }
                }
                while (xrp.getEventType() != XmlResourceParser.END_TAG) {
                    xrp.next();
                }
                xrp.next();
            }
            xrp.close();
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, "Ill-formatted timezones.xml file");
        } catch (java.io.IOException ioe) {
            Log.e(TAG, "Unable to read timezones.xml file");
        }
        return displayName;
   }
	
	private String getDisplayTimezone(String id, String displayName) {
		final long date = Calendar.getInstance().getTimeInMillis();
		if(displayName.equals(""))
			return "";
		
		final TimeZone tz = TimeZone.getTimeZone(id);
        final int offset = tz.getOffset(date);
        final int p = Math.abs(offset);
        final StringBuilder name = new StringBuilder();
        name.append("(GMT");
        
        if (offset < 0) {
            name.append('-');
        } else {
            name.append('+');
        }
        
        name.append(p / (TimezoneActivity.HOURS_1));
        name.append(':');
        
        int min = p / 60000;
        min %= 60;

        if (min < 10) {
            name.append('0');
        }
        name.append(min);
        name.append(") ");
        name.append(displayName);
        return name.toString();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(bluetoothReceiver);
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	private void registerReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.hiveview.bluetooth.le.btautopair.state");
		intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
		registerReceiver(bluetoothReceiver, intentFilter);
	}

	private void startBLService() {
		try {
			Intent intent = new Intent();
			intent.setAction("com.hiveview.bluetooth.le.BtAutoPair");
			startService(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {

		@SuppressLint("ShowToast")
		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)){
				Log.v(TAG, "time zone change");
				mCommonItemList.get(TIMEZONE_SETTINGS).setItemSetting(getDisplayTimezone((String)TimeZone.getDefault().getID(), getTimeZoneCity()));
				if(commonSettingOnListAdapter!=null)
				 //  commonSettingOnListAdapter.notifyDataSetChanged();
					commonSettingOnListAdapter.update(TIMEZONE_SETTINGS,commonSettingListView);
			}else {
				int state = intent.getIntExtra("state", 0);
				switch (state) {
				case BT_AUTOPAIR_FAILED: {
					// mStatus.setText(mContext.getResources().getString(R.string.bluetooth_error));
					break;
				}
				case BT_AUTOPAIR_NOTPAIR: {
					// mStatus.setText(mContext.getResources().getString(R.string.find_yaokongqi));
					break;
				}
				case BT_AUTOPAIR_PAIRING: {
					// mStatus.setText(mContext.getResources().getString(R.string.bluetooth_pairing));
					break;
				}
				case BT_AUTOPAIR_PAIRED: {
					// mStatus.setText(mContext.getResources().getString(R.string.bluetooth_pair_success));
					break;
				}
				case BT_AUTOPAIR_CONNECTING: {
					// mStatus.setText(mContext.getResources().getString(R.string.bluetooth_connecting));
					break;
				}
				case BT_AUTOPAIR_CONNECTED: {
					// mStatus.setText(mContext.getResources().getString(
					// R.string.bluetooth_connect_success));
					break;
				}
				case BT_AUTOPAIR_NEED_CONNECT: {

				startActivity(new Intent().setClass(mContext, ControlActivity.class));
				break;
			}

			case BT_AUTOPAIR_CONNECT_OK: {
				ToastUtils.showToast(mContext,
						mContext.getResources().getString(R.string.yaokongqi_connect),
						Toast.LENGTH_SHORT);
				;

				break;
			}

			}
		}
		}

	};

	public static boolean commitIntPref(Context context, String pref, String name, int def) {
		SharedPreferences sp = context.getSharedPreferences(pref, 0);
		Editor ed = sp.edit();
		ed.putInt(name, def);
		return ed.commit();
	}
	
	public static  int getIntPref(Context context, String pref, String name, int def) {
		SharedPreferences sp = context.getSharedPreferences(pref, 0);
		return sp.getInt(name, def);
	}
	
	/**first init listview Item*/
	private void CommonItemDataInit() {
		
		mTvNameSelectId =getBoxname();
		//初始化输入法的位置
		inputId=getInputMethod();
		ContentResolver resolver = this.getContentResolver();
		String inputMethodId = Settings.Secure.getString(resolver, 
				Settings.Secure.DEFAULT_INPUT_METHOD);
		//判断之前保存到额输入法存在，并确定所在位置
		for(int i=0;i<inputObjList.size();i++){
			if(inputMethodId.equals(inputObjList.get(i).getId())){
				inputId=i;
				break;
			}
		}
		if(inputId>=inputObjList.size()){
			inputId=0;
		}
		//将新数据保存，主要是用于卸载原有输入法，进入且选择默认输入法，无左右键的选择（输入法）
		saveInputMethod(inputId);
		if(inputObjList.size()!=0){
			saveInputMethodName(inputObjList.get(inputId).getName());
		}
		
		
		
		// 閼惧嘲褰囬弮鍫曟？閺嶇厧绱?
		if (Utils.is24Hour(mContext)) {
			timeTypeId = 1;
		} else {
			timeTypeId = 0;
		}

		manufactoryManager = ManufactoryManager.getManufactoryManager();
		String k;
		try {
			 k= manufactoryManager.getBootVolume();//SystemProperties.get("ubootenv.var.boot_vol", "0");	
		} catch (NoSuchMethodError e) {
			// TODO: handle exception
			e.printStackTrace();
			k=null;
		}
       
		if (null!=k){			
		  for(int i = 0 ; i < 21 ; i++){ 
			  if(k.equals(String.valueOf((i*5)/(float)100))){
				  volumeSelected = i;
			  }
		  }						
        }
		Log.i(TAG, "------boot video volume-----"+k +",volume i=="+volumeSelected);

		focusView = (LauncherFocusView) findViewById(R.id.activity_common_focusview);
		// mRelative = (RelativeLayout) findViewById(R.id.activity_common_id);
		focusView.setAnimatorEndListener(new FocusViewAnimatorEndListener() {

			@Override
			public void OnAnimateStart(View currentFocusView) {
				// TODO Auto-generated method stub
				mFocusAnimationEndFlag = false;
				/*View view = commonSettingListView.getSelectedView();
				FileTitleTextView tvSmall = (FileTitleTextView) view.findViewById(R.id.item_setting);
				tvSmall.setMarqueeRepeatLimit(100);
				Log.i("========", "执行！");*/
				
			}

			@Override
			public void OnAnimateEnd(View currentFocusView) {
				// TODO Auto-generated method stub
				mFocusAnimationEndFlag = true;
				listTextColorSet();
				/*View view = commonSettingListView.getSelectedView();
				FileTitleTextView tvSmall = (FileTitleTextView) view.findViewById(R.id.item_setting);
				tvSmall.setMarqueeRepeatLimit(0);*/
				
			}

		});
		
		
		screenTimeoutEntries = getResources().getStringArray(R.array.screen_timeout_entries);
		screenTimeoutEntryValues = getResources().getStringArray(R.array.screen_timeout_values);
		int currentTimeout =Settings.System.getInt(getContentResolver(), SCREEN_OFF_TIMEOUT,
                FALLBACK_SCREEN_TIMEOUT_VALUE);
		monitorProtectId = getEntryIndex(screenTimeoutEntryValues, currentTimeout);
		pm= (PowerManager) getSystemService(Context.POWER_SERVICE);
 
		autopowerTimeoutEntries = getResources().getStringArray(R.array.auto_power_timeout_entries);
		autopowerTimeoutEntrieValues = getResources().getStringArray(R.array.auto_power_timeout_entries_values);		

        int currentAutoPowerTimeout = getIntPref(getApplicationContext(),"auto_power_timeout", "auto_power_timeout" ,Utils.DEFAULT_AUTO_POWER_TIME);
		Log.d(TAG, "currentAutoPowerTimeout : " + currentAutoPowerTimeout);
		autopowerId = getEntryIndex(autopowerTimeoutEntrieValues, currentAutoPowerTimeout);
 
		
		try {
			ContentResolver cr = getContentResolver();
			Bundle b = cr.call(URI_INSTALL_APK_INFO, GET_WHEAT_LOCK_INFO, null,null);
			String is_lock = b.getString("is_lock");
			Log.d(TAG, "is_lock :" + is_lock);
        if(is_lock==null)
			is_lock = 0+"";
			String lock_pwd = b.getString("lock_pwd");
			Log.d(TAG, "lock_pwd :" + lock_pwd);
			WheatCode = new String[2];
			WheatCode[1] = getResources().getString(R.string.close);
			if ("1".equals(is_lock)) {
				wheatCodeSelectId = 0;
				WheatCode[0] = lock_pwd;
			} else {
				wheatCodeSelectId = 1;
                WheatCode[0] = getResources().getString(R.string.open);
			}
			
			//处理非法关机，麦粒锁没有输入的情况下关机，默认恢复到关闭状态
            if(lock_pwd==null){
                WheatCode = new String[2];
                wheatCodeSelectId = 1; 
                WheatCode[1] = getResources().getString(R.string.close);
            }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			WheatCode = new String[2];
            wheatCodeSelectId = 1;
			WheatCode[1] = getResources().getString(R.string.close);
		}

		Log.v(TAG, "box type=="+Utils.getBoxType()+",isoverseas="+Utils.isOverseas());
		
		if((Utils.getBoxType()==Utils.DOMYBOX_S905X_10X || Utils.getBoxType()==Utils.DOMYBOX_10S) 
				&& !Utils.isOverseas()){
			initDomybox10SInternal();
		}else if((Utils.getBoxType()==Utils.DOMYBOX_S905X_10X || Utils.getBoxType()==Utils.DOMYBOX_10S) 
				&& Utils.isOverseas()){
			initDomybox10sOverseas();
		}else if((Utils.getBoxType()==Utils.DOMYBOX_S905X_30X || Utils.getBoxType()==Utils.DOMYBOX_S905X_30VC) && !Utils.isOverseas()){
			initDomyboxS905Internal();
		}else if((Utils.getBoxType()==Utils.DOMYBOX_S905X_30X || Utils.getBoxType()==Utils.DOMYBOX_S905X_30VC) && Utils.isOverseas()){
			initDomyboxS905Overseas();
		}else if(Utils.getBoxType()==Utils.DOMYBOX_S905X_40S  && !Utils.isOverseas()){
			initDomyboxS90540SInternal();
		}else if(Utils.getBoxType()==Utils.DOMYBOX_S905X_40S  && Utils.isOverseas()){
			initDomyboxS90540SOverseas();
		} else {
			initDomybox905IPTV();
		}
		 
	    
		
		List<String> list = new ArrayList();
		for (int i = 0; i < mArrays.length; i++) {
			list.add(mArrays[i]);
		}
		for (int i = 0; i < list.size(); i++) {
			CommonItemList mTemp = new CommonItemList();
			mTemp.setItemName(list.get(i));

			if (null != pageLefts[i]) {
				mTemp.setPageLeft(pageLefts[i]);
			}
			if (null != pageRights[i]) {
				mTemp.setPageRight(pageRights[i]);
			}
			if (null != mItemSettings[i]) {
				mTemp.setItemSetting(mItemSettings[i]);
			}

			mCommonItemList.add(mTemp);
		}
	}

	private void CommonItemListInit() {
		if (commonSettingOnListAdapter == null) {
			commonSettingOnListAdapter = new CommonSettingOnList(this);
			commonSettingListView.setAdapter(commonSettingOnListAdapter);
		} else {
			commonSettingOnListAdapter.notifyDataSetChanged();
		}
	 
		commonSettingListView.setSelection(0);
	}

	private void onBlindListener() {
		// TODO Auto-generated method stub
		commonSettingListView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub		    
				mCurKeycode = keyCode;
				int selectItems = commonSettingListView.getSelectedItemPosition();
				View x = commonSettingListView.getSelectedView();
				if(x==null)
					return true;
				
				
				String is_lock="";
				String lock_pwd="";
				boolean GET_WHEAT_LOCK_SUCESS = true;
				ContentResolver cr = getContentResolver();
				try {
					Bundle b = cr.call(URI_INSTALL_APK_INFO, GET_WHEAT_LOCK_INFO, null, null);
					is_lock = b.getString("is_lock");
				//	Log.d(TAG,"is_lock :" + is_lock);
					lock_pwd = b.getString("lock_pwd");
				//	Log.d(TAG,"lock_pwd :" + lock_pwd);
					WheatCode[0] =  lock_pwd;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					GET_WHEAT_LOCK_SUCESS=false;
				}
     
				TextView tv = (TextView) x.findViewById(R.id.item_setting);
				ImageView imageRightTemp = (ImageView) x.findViewById(R.id.page_right);
				ImageView imageLeftTemp = (ImageView) x.findViewById(R.id.page_left);
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
							|| keyCode == KeyEvent.KEYCODE_DPAD_UP) {

						if (event.getRepeatCount() == 0) {
							mTextColorChangeFlag = true;
							mKeyDownTime = event.getDownTime();
						} else {
							mTextColorChangeFlag = false;
							if (event.getDownTime() - mKeyDownTime >= Utils.KEYDOWN_DELAY_TIME) {
								Log.e("KeyEvent", "time=" + (event.getDownTime() - mKeyDownTime)
										+ " count" + event.getRepeatCount());
								mKeyDownTime = event.getDownTime();
							} else {
								return true;
							}
						}

						if (!mFocusAnimationEndFlag) {
							mTextColorChangeFlag = false;
						}
					}

					if (keyCode == KeyEvent.KEYCODE_MENU) {
						KeyMenuCount++;
					} else {
						KeyMenuCount = 0;
					}

					switch (keyCode) {
					case KeyEvent.KEYCODE_MENU:
						if(selectItems == AUTO_POWER_TIME){
						if (KeyMenuCount == 10) {
							autopowerId = 0;
								KeyMenuCount = 0;
							tv.setText(autopowerTimeoutEntries[autopowerId]);
							commitIntPref(mContext, "auto_power_timeout", "auto_power_timeout", Integer.parseInt(autopowerTimeoutEntrieValues[autopowerId]));
							mCommonItemList.get(selectItems).setItemSetting(
											String.valueOf(autopowerTimeoutEntries[autopowerId]));
								mContext.sendBroadcast(new Intent("com.hiveview.hiveviewdog.action.autopowertimeout"));
							}
						}
                        break;
					case KeyEvent.KEYCODE_DPAD_RIGHT: {
						Log.i(TAG, "selectItems  KEYCODE_DPAD_RIGHT :" + selectItems);
						if (selectItems == PROTECT_MONITOR_TIME) {
							imageRightTemp.setImageDrawable(mContext.getResources().getDrawable(
									R.drawable.page_right_selected));
							monitorProtectId = (monitorProtectId + 1) % screenTimeoutEntries.length;
							 Settings.System.putInt(getContentResolver(), SCREEN_OFF_TIMEOUT, Integer
										.parseInt(screenTimeoutEntryValues[monitorProtectId].toString()));
							
							tv.setText(screenTimeoutEntries[monitorProtectId]);
							mCommonItemList.get(selectItems).setItemSetting(String.valueOf(screenTimeoutEntries[monitorProtectId]));
						} else if (selectItems == TIME_TYPE) {
							// TODO
							imageRightTemp.setImageDrawable(mContext.getResources().getDrawable(
									R.drawable.page_right_selected));
							timeTypeId = (timeTypeId + 1) % 2;

							tv.setText(getStringArrays(mContext, R.array.time_array3, timeTypeId));
							if (timeTypeId == 0) {
								Utils.set24Hour(false, mContext);
							} else if (timeTypeId == 1) {
								Utils.set24Hour(true, mContext);
							}
							mCommonItemList.get(selectItems).setItemSetting(getStringArrays(mContext, R.array.time_array3, timeTypeId));
						} else if (selectItems == TV_NAME) {
							imageRightTemp.setImageDrawable(CommonActivity.this.getResources()
									.getDrawable(R.drawable.page_right_selected));
							String[] boxs=mContext.getResources().getStringArray(R.array.tv_name);
							mTvNameSelectId = (mTvNameSelectId + 1) % boxs.length;
							tv.setText(boxs[mTvNameSelectId]);
							saveBoxname(mTvNameSelectId);
							mCommonItemList.get(selectItems).setItemSetting(boxs[mTvNameSelectId]);
						}else if (selectItems == INPUT) {
							if(inputObjList.size()!=0){
								imageRightTemp.setImageDrawable(CommonActivity.this.getResources()
										.getDrawable(R.drawable.page_right_selected));
								inputId = (inputId + 1) % inputObjList.size();
								tv.setText(inputObjList.get(inputId).getName());
								switchInputMethod(CommonActivity.this, inputObjList.get(inputId).getId());
								saveInputMethod(inputId);
								saveInputMethodName(inputObjList.get(inputId).getName());
								mCommonItemList.get(selectItems).setItemSetting(inputObjList.get(inputId).getName());
							}
								
							}else if (selectItems == AUTO_POWER_TIME) {
							imageRightTemp.setImageDrawable(mContext.getResources().getDrawable(R.drawable.page_right_selected));
							//设置海外右键可开启永不关机
							if (Utils.getBoxType()==Utils.DOMYBOX_S905X_40S  && Utils.isOverseas()) {
								if( autopowerId == 0 || autopowerId == 1 || autopowerId == 2 || autopowerId == 3 ) {
									autopowerId = 4;
									Log.i(TAG,"autopowerId01 is "+autopowerId);
								}else if(autopowerId == (autopowerTimeoutEntries.length - 1)){
									autopowerId =0;
									Log.i(TAG,"autopowerId02 is "+autopowerId);
								}else {
									autopowerId++;
									Log.i(TAG,"autopowerId03 is "+autopowerId);
								}
							}else {
								if (autopowerId == (autopowerTimeoutEntries.length - 1)) {
									autopowerId = 4;
								} else {
									autopowerId++;
								}
							}
							commitIntPref(mContext, "auto_power_timeout", "auto_power_timeout", Integer.parseInt(autopowerTimeoutEntrieValues[autopowerId]));
							tv.setText(autopowerTimeoutEntries[autopowerId]);
							mCommonItemList.get(selectItems).setItemSetting(
											String.valueOf(autopowerTimeoutEntries[autopowerId]));
							mContext.sendBroadcast(new Intent("com.hiveview.hiveviewdog.action.autopowertimeout"));
						}
                        else if (selectItems == MAIBISUO) {
							imageRightTemp.setImageDrawable(CommonActivity.this.getResources()
									.getDrawable(R.drawable.page_right_selected));
							if(!GET_WHEAT_LOCK_SUCESS)
								return false;
							String[] open = WheatCode;
							wheatCodeSelectId = (wheatCodeSelectId + 1) % open.length;
							tv.setText(open[wheatCodeSelectId]);
						//	saveBoxname(mTvNameSelectId);
							mCommonItemList.get(selectItems).setItemSetting(open[wheatCodeSelectId]);
							Log.d(TAG,"SETTING :" + open[wheatCodeSelectId]);
							if(wheatCodeSelectId == 0){
								Log.d(TAG,"getThePwd() 0:" + getThePwd());								
							//	mCommonItemList.get(selectItems).setItemSetting(getThePwd());
								startActivity(new Intent().setClass(mContext, WheatCoinLockCodeInput.class));
								Bundle bundle = new Bundle();
								bundle.putString("lock_pwd", lock_pwd);
								bundle.putString("is_lock", LOCK_ON_S);
								cr.call(URI_INSTALL_APK_INFO, SET_WHEAT_LOCK_INFO, lock_pwd, bundle); 
							}else if (wheatCodeSelectId == 1) {
								Log.d(TAG,"getThePwd() 1 :" + getThePwd());				
							//	mCommonItemList.get(selectItems).setItemSetting(WheatCode[(wheatCodeSelectId + 1) % 2]);
								Bundle bundle = new Bundle();
								bundle.putString("lock_pwd", lock_pwd);
								bundle.putString("is_lock", LOCK_OFF_S);
								cr.call(URI_INSTALL_APK_INFO, SET_WHEAT_LOCK_INFO, lock_pwd, bundle);  								 
							}
						}
						break;
					}
					case KeyEvent.KEYCODE_DPAD_LEFT: {
						if (selectItems == PROTECT_MONITOR_TIME) {
							imageLeftTemp.setImageDrawable(mContext.getResources().getDrawable(
									R.drawable.page_left_selected));
						    if(monitorProtectId==0){
						    	monitorProtectId=screenTimeoutEntries.length-1;
						    }else {
	                        	monitorProtectId = (monitorProtectId - 1);
						    }

						    Settings.System.putInt(getContentResolver(), SCREEN_OFF_TIMEOUT, Integer
									.parseInt(screenTimeoutEntryValues[monitorProtectId].toString()));
							tv.setText(screenTimeoutEntries[monitorProtectId]);
							mCommonItemList.get(selectItems).setItemSetting(String.valueOf(screenTimeoutEntries[monitorProtectId]));
						} else if (selectItems == TIME_TYPE) {

							imageLeftTemp.setImageDrawable(mContext.getResources().getDrawable(
									R.drawable.page_left_selected));
							timeTypeId = (timeTypeId + 1) % 2;

							tv.setText(getStringArrays(mContext, R.array.time_array3, timeTypeId));
							if (timeTypeId == 0) {
								Utils.set24Hour(false, mContext);
							} else if (timeTypeId == 1) {
								Utils.set24Hour(true, mContext);
							}
							mCommonItemList.get(selectItems).setItemSetting(getStringArrays(mContext, R.array.time_array3, timeTypeId));
						} else if (selectItems == TV_NAME) {
							imageLeftTemp.setImageDrawable(CommonActivity.this.getResources()
									.getDrawable(R.drawable.page_left_selected));
							String[] boxs=mContext.getResources().getStringArray(R.array.tv_name);
							if(mTvNameSelectId==0){
								mTvNameSelectId=boxs.length-1;
							}else {
								mTvNameSelectId--;
							}
							tv.setText(boxs[mTvNameSelectId]);
							saveBoxname(mTvNameSelectId);
							mCommonItemList.get(selectItems).setItemSetting(boxs[mTvNameSelectId]);
						} else if (selectItems == INPUT) {
							if(inputObjList.size()!=0){
								imageLeftTemp.setImageDrawable(CommonActivity.this.getResources()
										.getDrawable(R.drawable.page_left_selected));
								if(inputId==0){
									inputId=inputObjList.size()-1;
								}else {
									inputId--;
								}
								tv.setText(inputObjList.get(inputId).getName());
								switchInputMethod(CommonActivity.this, inputObjList.get(inputId).getId());
								saveInputMethod(inputId);
								saveInputMethodName(inputObjList.get(inputId).getName());
								mCommonItemList.get(selectItems).setItemSetting(inputObjList.get(inputId).getName());
							}
						
						}else if (selectItems == AUTO_POWER_TIME) {
							imageLeftTemp.setImageDrawable(mContext.getResources().getDrawable(
											R.drawable.page_left_selected));
							//设置海外左键可开启永不关机
							if (Utils.getBoxType()==Utils.DOMYBOX_S905X_40S  && Utils.isOverseas()) {
								Log.i(TAG, "oversea905X");
								if(autopowerId == 0){
									autopowerId = autopowerTimeoutEntries.length - 1;
								}else if (autopowerId == 4 || autopowerId == 3 || autopowerId == 2 || autopowerId == 1) {
									autopowerId = 0;
								}else {
									autopowerId = autopowerId - 1;
								}
							}else {
								Log.i(TAG, "other situations");
								if (autopowerId <= 4) {
									autopowerId = autopowerTimeoutEntries.length - 1;
								} else {
									autopowerId = (autopowerId - 1);
								}
							}
							commitIntPref(mContext, "auto_power_timeout", "auto_power_timeout", Integer.parseInt(autopowerTimeoutEntrieValues[autopowerId]));
							tv.setText(autopowerTimeoutEntries[autopowerId]);
							mCommonItemList.get(selectItems).setItemSetting(
											String.valueOf(autopowerTimeoutEntries[autopowerId]));
							mContext.sendBroadcast(new Intent("com.hiveview.hiveviewdog.action.autopowertimeout"));
						}
                        else if (selectItems == MAIBISUO) {
							imageLeftTemp.setImageDrawable(CommonActivity.this.getResources()
									.getDrawable(R.drawable.page_left_selected));
							if(!GET_WHEAT_LOCK_SUCESS)
								return false;
							String[] open = WheatCode;
							wheatCodeSelectId = (wheatCodeSelectId + 1) % open.length;
							tv.setText(open[wheatCodeSelectId]);
							mCommonItemList.get(selectItems).setItemSetting(open[wheatCodeSelectId]);
							Log.d(TAG,"SETTING :" + open[wheatCodeSelectId]);
								if(wheatCodeSelectId == 0){
								//	mCommonItemList.get(selectItems).setItemSetting(getThePwd());
									startActivity(new Intent().setClass(mContext, WheatCoinLockCodeInput.class));
									Bundle bundle = new Bundle();
									bundle.putString("lock_pwd", lock_pwd);
									bundle.putString("is_lock", LOCK_ON_S);
									cr.call(URI_INSTALL_APK_INFO, SET_WHEAT_LOCK_INFO, lock_pwd, bundle); 
								}else if (wheatCodeSelectId == 1) {
								//	mCommonItemList.get(selectItems).setItemSetting(WheatCode[(wheatCodeSelectId + 1)% 2]);
									Bundle bundle = new Bundle();
									bundle.putString("lock_pwd", lock_pwd);
									bundle.putString("is_lock", LOCK_OFF_S);
									cr.call(URI_INSTALL_APK_INFO, SET_WHEAT_LOCK_INFO, lock_pwd, bundle);  								 
								}
						}
						break;
					}
					}
				} else if (KeyEvent.ACTION_UP == event.getAction()) {
					if (!mTextColorChangeFlag) {
						mTextColorChangeFlag = true;
						listTextColorSet();
					}
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_RIGHT: {
						if (selectItems == TV_NAME || selectItems == PROTECT_MONITOR_TIME
								|| selectItems == TIME_TYPE || selectItems == MAIBISUO 
								|| selectItems == AUTO_POWER_TIME || selectItems ==INPUT)
							imageRightTemp.setImageDrawable(mContext.getResources().getDrawable(
									R.drawable.page_right));

						break;
					}
					case KeyEvent.KEYCODE_DPAD_LEFT: {
						if (selectItems == PROTECT_MONITOR_TIME || selectItems == TV_NAME
								|| selectItems == TIME_TYPE || selectItems == MAIBISUO 
								|| selectItems == AUTO_POWER_TIME || selectItems ==INPUT) {
							imageLeftTemp.setImageDrawable(mContext.getResources().getDrawable(
									R.drawable.page_left));
						}
						break;
					   }
					}
				}
				return false;

			}

		});

		commonSettingListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
                Log.v(TAG, "pos==="+position);

				if (position == VERSION_CHECK) {
					if (Utils.isNetworkAvailable(mContext)) {
						try {
							Intent intent = new Intent();
							intent.addCategory(Intent.CATEGORY_LAUNCHER);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setPackage("com.hiveview.upgrade");
							intent.setClassName("com.hiveview.upgrade","com.hiveview.upgrade.ManulActivity");
							mContext.startActivity(intent);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					} else {
						ToastUtils.showToast(mContext, getResources().getString(R.string.please_set_network),
								Toast.LENGTH_SHORT);
					}
				}
				
				if (position == LANGUAGE_SELECT) {
					startActivity(new Intent().setClass(mContext, LanguageActivity.class));
				}
	 
				if (position == DEVELOPER_SETTINGS) {
					startActivity(new Intent().setClass(mContext, DevloperModelActivity.class));
				}

				if (position == TIMEZONE_SETTINGS) {
					startActivity(new Intent().setClass(mContext, TimezoneActivity.class));
				}
				
				if (position == BLUETOOTH_SETTING) {
                    startActivity(new Intent().setClass(mContext, BlueToothActivity.class));
                }
				
				if(position==CONTROL_SETTING){
					startBLService();
				}

               

				if (position == RESET) {
				
					final ResetDialog dialog = new ResetDialog(mContext);
					Window dialogWindow = dialog.getWindow();
					WindowManager.LayoutParams lp = dialogWindow.getAttributes();
					lp.width = getResources().getDimensionPixelSize(R.dimen.recovery_display_width);
					lp.height = getResources().getDimensionPixelSize(R.dimen.recovery_display_height);
					dialogWindow.setAttributes(lp);
					dialog.show();
					TextView message = (TextView) dialog.findViewById(R.id.message1);
					message.setText(getString(R.string.clean_all_confirm));

					Button button1 = (Button) dialog.findViewById(R.id.button1);
					Button button2 = (Button) dialog.findViewById(R.id.button2);
					button1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View paramView) {
							// TODOAuto-generated method stub
							defaultBootVolume();
							mContext.sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
							dialog.dismiss();
						}
					});
					button2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View paramView) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
				} 
				
				/**
				 * 调试日志上传
				 * com.hiveview.cloudscreen.debug.DebugActivity
				 */
				if (position == SENDLOG) {
					PackageManager packageManager = getPackageManager();
				    if (checkPackInfo("com.hiveview.abn")) {
				        Intent intent = packageManager.getLaunchIntentForPackage("com.hiveview.abn");
				        startActivity(intent);
				    } else {
				    }
					
//					startActivity(new Intent().setClass(mContext, TimezoneActivity.class));
					
				}
				

			}

		});

		commonSettingListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> paramAdapterView, View view, int position,
					long paramLong) {
				mItemListCurPosition = position;
				Log.v(TAG, "onItemSelected=="+position);
				if(view==null){
					mHandler.sendEmptyMessageDelayed(0, 400);
					return;
				}
				
					
				mView = view;
				if (bfocusViewInitStatus) {
					focusView.initFocusView(mView, false, 0);
				}
				if (mTextView != null) {
					mTextView.setTextColor(mContext.getResources().getColor(R.color.grey5_color));
				}
				if (mTextViewSetting != null) {
					mTextViewSetting.setTextColor(mContext.getResources().getColor(
							R.color.grey5_color));
					//mTextViewSetting.setMarqueeRepeatLimit(0);
					mTextViewSetting.clearFocus();
					mTextViewSetting.setFocusable(false);
				}
				if (mImageView != null) {
					mImageView.setImageResource(R.drawable.page_right_big);
				}

				mTextView = (TextView) view.findViewById(R.id.item_name);
				if (position == TV_NAME || position == PROTECT_MONITOR_TIME || 
						position == TIME_TYPE || position == AUTO_POWER_TIME 
						|| position == TIMEZONE_SETTINGS || position == MAIBISUO 
						|| position==LANGUAGE_SELECT || position==INPUT) {
					mTextViewSetting = (TextView) view.findViewById(R.id.item_setting);
					
				} else {
					mTextViewSetting = null;
				}

				if (position == DEVELOPER_SETTINGS || position == RESET || position == SENDLOG
						|| position == VERSION_CHECK || position == LANGUAGE_SELECT || position == BLUETOOTH_SETTING||position==CONTROL_SETTING) {
					mImageView = (ImageView) view.findViewById(R.id.page_right);
				} else {
					mImageView = null;
				}

				if (mCurKeycode == KeyEvent.KEYCODE_DPAD_DOWN) {

					if (position < 5
							|| position > commonSettingListView.getCount() - 2
							|| (commonSettingListView.getFirstVisiblePosition() == 0 && view
									.getTop() < (view.getHeight() * 4))
							|| (commonSettingListView.getFirstVisiblePosition() != 0 && view
									.getTop() < view.getHeight() * 5)) {
						focusView.moveTo(mView);
					} else {
						listTextColorSet();
						commonSettingListView.setSelectionFromTop(position,
								view.getTop() - view.getHeight());

					}
					 /*由于 同维盒子 paramView.getTop() -paramView.getHeight() 时 在第三列时 值就小于0 了，
				     存在误差,所以增加一个误差值10，1.0+没有，所以改为在可控范围误差内 进行focuse的跳转  */
				} else if (mCurKeycode == KeyEvent.KEYCODE_DPAD_UP) {
					if ((mItemListCurPosition == 0 || commonSettingListView
							.getFirstVisiblePosition() == 0 && view.getTop() > (view.getHeight()))
							|| (commonSettingListView.getFirstVisiblePosition() != 0 && view
									.getTop() >= view.getHeight() ||(view.getTop() -view.getHeight()<10 && view.getTop() -view
									        .getHeight() >-10) )) {
						focusView.moveTo(mView);
					} else {
						listTextColorSet();
						commonSettingListView.setSelectionFromTop(mItemListCurPosition,
								view.getHeight());
					}

				} else if (mCurKeycode == KeyEvent.KEYCODE_PAGE_UP
						|| mCurKeycode == KeyEvent.KEYCODE_PAGE_DOWN) {
					focusView.moveTo(view);
				}
				if (bfocusViewInitStatus) {
					bfocusViewInitStatus = false;
					mTextColorChangeFlag = true;
					listTextColorSet();
				}
				// fixed the keyboard repeat mode
				if (!mTextColorChangeFlag && mFocusAnimationEndFlag) {
					if ((mItemListCurPosition == 0 || mItemListCurPosition == commonSettingListView
							.getCount() - 1)) {
						mTextColorChangeFlag = true;
						listTextColorSet();
					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> paramAdapterView) {
				// TODO Auto-generated method stub
			/*	View view = commonSettingListView.getSelectedView();
				FileTitleTextView tvSmall = (FileTitleTextView) view.findViewById(R.id.item_setting);
				tvSmall.setMarqueeRepeatLimit(0);*/

			}

		});
	}

	public static String getStringArrays(Context mContext, int id, int position) {
		String[] arrays = mContext.getResources().getStringArray(id);
		if (null != arrays) {
			return arrays[position];
		}
		return "";
	}

	private void listTextColorSet() {
		Log.v(TAG, "mTextColorChangeFlag=="+mTextColorChangeFlag+",mFocusAnimationEndFlag="+mFocusAnimationEndFlag);
		if (mTextColorChangeFlag && mFocusAnimationEndFlag) {
			if (mTextView != null) {
				mTextView.setTextColor(this.getResources().getColor(R.color.white));
			}
			if (mTextViewSetting != null) {
				mTextViewSetting.setTextColor(this.getResources().getColor(R.color.white));
				//mTextViewSetting.setMarqueeRepeatLimit(100);
				if(mItemListCurPosition!=LANGUAGE_SELECT && mItemListCurPosition!=TIMEZONE_SETTINGS){
					mTextViewSetting.setFocusable(true);
					mTextViewSetting.requestFocus();
				}
			}
			if (mImageView != null) {
				mImageView.setImageResource(R.drawable.page_right_big_selected);
			}
			mTextColorChangeFlag = false;

		}
	}

	class CommonSettingOnList extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context cont;
		private int selectItem;
		final int TYPE_1 = 0;
		final int TYPE_2 = 1;
		final int TYPE_3 = 2;

		class ViewHolder {
			TextView itemName;
			ImageView pageLeft;
			TextView itemSetting;
			ImageView pageRight;
		}

		public CommonSettingOnList(Context context) {
			super();
			cont = context;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			int i = mArrays != null ? mArrays.length : 0;
			Log.i("length", String.valueOf(i));
			return mArrays != null ? mArrays.length : 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void setSelectItem(int position) {
			selectItem = position;
		}

		public int getSelectItem() {
			return selectItem;
		}

		public int getItemViewType(int position) {
		//	Log.d(TAG,"getItemViewType :" + position);
			int p = position;
			if (p==DEVELOPER_SETTINGS || p==RESET || p==SENDLOG || p==VERSION_CHECK || p==BLUETOOTH_SETTING||p==CONTROL_SETTING)
				return TYPE_1;
			else if(p == TIMEZONE_SETTINGS || p==LANGUAGE_SELECT)
				return TYPE_3;
			else
				return TYPE_2;
		}

		public int getViewTypeCount() {
			return 3;
		}
		public void update(int index,ListView listview){
            //得到第一个可见item项的位置
            int visiblePosition = listview.getFirstVisiblePosition();
            //得到指定位置的视图，对listview的缓存机制不清楚的可以去了解下
            View view = listview.getChildAt(index - visiblePosition);
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.itemSetting = (TextView) view.findViewById(R.id.item_setting);
           /* if (null != mCommonItemList.get(index).getItemName()) {
				holder.itemName.setText(mCommonItemList.get(index).getItemName());
			}*/
			
			if (null != mCommonItemList.get(index).getItemSetting()) {
//				Log.d(TAG,"setText :" + mCommonItemList.get(position).getItemSetting());
				holder.itemSetting.setText(mCommonItemList.get(index).getItemSetting());
			}
        }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder1 = null;
			ViewHolder holder2 = null;
			ViewHolder holder3 = null;
			int type = getItemViewType(position);
		//	Log.d(TAG,"position" + position);
			if (convertView == null) {
				switch (type) {
				case TYPE_2: {
					convertView = mInflater.inflate(R.layout.activity_item_for_common, null);
					holder1 = new ViewHolder();
					holder1.itemName = (TextView) convertView.findViewById(R.id.item_name);
					holder1.itemSetting = (TextView) convertView.findViewById(R.id.item_setting);
					holder1.pageLeft = (ImageView) convertView.findViewById(R.id.page_left);
					holder1.pageRight = (ImageView) convertView.findViewById(R.id.page_right);

					convertView.setTag(holder1);
					break;
				}
				case TYPE_3:{
					convertView = mInflater.inflate(R.layout.activity_item_forcommon3, null);
					holder3 = new ViewHolder();
					holder3.itemName = (TextView) convertView.findViewById(R.id.item_name);
					holder3.pageRight = (ImageView) convertView.findViewById(R.id.page_right);
					holder3.itemSetting = (TextView)convertView.findViewById(R.id.item_setting);
					convertView.setTag(holder3);
					break;
				}
				case TYPE_1: {
					convertView = mInflater.inflate(R.layout.activity_item_forcommon2, null);
					holder2 = new ViewHolder();
					holder2.itemName = (TextView) convertView.findViewById(R.id.item_name);
					holder2.pageRight = (ImageView) convertView.findViewById(R.id.page_right);
					convertView.setTag(holder2);
					break;
				}
				}
			} else {
				switch (type) {
				case TYPE_3:{
					holder3 = (ViewHolder) convertView.getTag();
					break;
				}
				case TYPE_1: {
					holder2 = (ViewHolder) convertView.getTag();
					break;
				}
				case TYPE_2: {
		//			Log.d(TAG, "get TAG  + " + position);
					holder1 = (ViewHolder) convertView.getTag();
					break;
				}
				}
			}

			switch (type) {

			case (TYPE_2): {
				//Log.d(TAG,"getItemSetting :" +"position :" + position+ "    "+ mCommonItemList.get(position).getItemSetting() );
				if (null != mCommonItemList.get(position).getItemName()) {
					holder1.itemName.setText(mCommonItemList.get(position).getItemName());

				}
				if (null != mCommonItemList.get(position).getPageLeft()) {
					holder1.pageLeft.setImageDrawable(mCommonItemList.get(position).getPageLeft());
					holder1.pageLeft.setVisibility(View.VISIBLE);
					holder1.itemSetting.setVisibility(View.VISIBLE);

				} else {
					holder1.pageLeft.setImageDrawable(null);
					holder1.pageLeft.setVisibility(View.GONE);
				}
				if (null != mCommonItemList.get(position).getItemSetting()) {
					//Log.d(TAG,"getItemSetting :" +"position :" + position+ "    "+ mCommonItemList.get(position).getItemSetting() );
					holder1.itemSetting.setText(mCommonItemList.get(position).getItemSetting());
					holder1.itemSetting.setVisibility(View.VISIBLE);
				} else {
					holder1.itemSetting.setText(null);
					holder1.itemSetting.setVisibility(View.GONE);
				}
				if (null != mCommonItemList.get(position).getPageRight()) {

					holder1.pageRight
							.setImageDrawable(mCommonItemList.get(position).getPageRight());
					holder1.pageRight.setVisibility(View.VISIBLE);
				} else {
					holder1.pageLeft.setImageDrawable(null);
					holder1.pageLeft.setVisibility(View.GONE);

				}
				break;
			}
			case (TYPE_1): {
				if (null != mCommonItemList.get(position).getItemName()) {
					holder2.itemName.setText(mCommonItemList.get(position).getItemName());
				}
				break;
			}
			case (TYPE_3): {
				if (null != mCommonItemList.get(position).getItemName()) {
					holder3.itemName.setText(mCommonItemList.get(position).getItemName());
				}
				
				if (null != mCommonItemList.get(position).getItemSetting()) {
	//				Log.d(TAG,"setText :" + mCommonItemList.get(position).getItemSetting());
					holder3.itemSetting.setText(mCommonItemList.get(position).getItemSetting());
				}
				break;
			}
			}

			return convertView;
		}
	}


	private boolean saveBoxname(int mode){
		SharedPreferences sharedPref = this.getSharedPreferences("box_name",Context.MODE_PRIVATE);
		Editor ed = sharedPref.edit();
		ed.putInt("boxname", mode);
		ed.commit();
		return true;
	}
	private boolean saveInputMethod(int mode){
		SharedPreferences sharedPref = this.getSharedPreferences("inputMethod",Context.MODE_PRIVATE);
		Editor ed = sharedPref.edit();
		ed.putInt("inputMethod", mode);
		ed.commit();
		return true;
	}
	private boolean saveInputMethodName(String name){
		SharedPreferences sharedPref = this.getSharedPreferences("name",Context.MODE_PRIVATE);
		Editor ed = sharedPref.edit();
		ed.putString("name", name);
		ed.commit();
		return true;
	}

	private int	getBoxname(){
		SharedPreferences sharedPref = this.getSharedPreferences("box_name",Context.MODE_PRIVATE);
		return sharedPref.getInt("boxname",0);
	}
	private int	getInputMethod(){
		SharedPreferences sharedPref = this.getSharedPreferences("inputMethod",Context.MODE_PRIVATE);
		return sharedPref.getInt("inputMethod",0);
	}
	private String getInputMethodName(){
		SharedPreferences sharedPref = this.getSharedPreferences("name",Context.MODE_PRIVATE);
		return sharedPref.getString("name","");
	}
	private int getEntryIndex(CharSequence[] entryValues, long curValue) {
		for (int i = 0; i < entryValues.length; i++) {
			long value = Long.parseLong(entryValues[i].toString());
			if (curValue == value) {
				return i;
			}
		}
		return 0;
	}
	
 

	@SuppressLint("NewApi")
	private String getThePwd(){
		ContentResolver cr = getContentResolver();
		Bundle b = cr.call(URI_INSTALL_APK_INFO, GET_WHEAT_LOCK_INFO, null, null);
		//String is_lock = b.getString("is_lock");
		 return b.getString("lock_pwd");     
	}
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context paramContext, Intent paramIntent) {
			// TODO Auto-generated method stub
			String wheatcode = getThePwd();
			if (paramIntent.getAction().equals("com.hiveview.cloudtv.settings.wheatcodeback")) {
				Log.i(TAG, "wheatcodeback-----get the receiver------");	
				if (null !=wheatcode &&!"".equals(wheatcode)) {
					WheatCodeBackInit();
				}else {
				Log.d(TAG,"mCommonItemList " +wheatcode);
				mCommonItemList.get(MAIBISUO).setItemSetting(WheatCode[1]);
				  ContentResolver cr = getContentResolver();
			//	Bundle b = cr.call(URI_INSTALL_APK_INFO, GET_WHEAT_LOCK_INFO, null, null);
				Bundle bundle = new Bundle();
				bundle.putString("lock_pwd", "");
				bundle.putString("is_lock", LOCK_OFF_S);
				cr.call(URI_INSTALL_APK_INFO, SET_WHEAT_LOCK_INFO, "", bundle);  	
				WheatCodeBackInit();
			}
		}
			else if (paramIntent.getAction().equals("com.hiveview.cloudtv.settings.wheatcode")) {
				Log.i(TAG, "--wheatcode---get the receiver------" + getThePwd());	
				String k =getThePwd();
				mCommonItemList.get(MAIBISUO).setItemSetting(getThePwd());
				WheatCodeBackInit();				
			}
		}
	};   
	private void WheatCodeBackInit() {
		if (commonSettingOnListAdapter == null) {
			commonSettingOnListAdapter = new CommonSettingOnList(this);
			commonSettingListView.setAdapter(commonSettingOnListAdapter);
		} else {
			commonSettingOnListAdapter.notifyDataSetChanged();
		}
	}
	private void regReceiver() {
		IntentFilter intent = new IntentFilter();
		intent.addAction("com.hiveview.cloudtv.settings.wheatcodeback"); 
		intent.addAction("com.hiveview.cloudtv.settings.wheatcode");
		registerReceiver(mReceiver, intent);
	}
	
	
	private void onInitSelect(){
		View view2 = commonSettingListView.getChildAt(0);
		focusView.initFocusView(view2, false, 0);
		 
		mTextView = (TextView) view2.findViewById(R.id.item_name);
		mTextViewSetting = (TextView) view2.findViewById(R.id.item_setting);
		if (mTextView != null) {
			mTextView.setTextColor(this.getResources().getColor(R.color.white));
		}
		if (mTextViewSetting != null) {
			mTextViewSetting.setTextColor(this.getResources().getColor(R.color.white));
			//mTextViewSetting.setMarqueeRepeatLimit(100);
			if(mItemListCurPosition!=LANGUAGE_SELECT && mItemListCurPosition!=TIMEZONE_SETTINGS){
				mTextViewSetting.setFocusable(true);
				mTextViewSetting.requestFocus();
			}
		}
	}
	//切换输入法
	private void switchInputMethod(Context context,String inputmethod){
		ContentResolver resolver = context.getContentResolver();
		String inputMethod = Settings.Secure.getString(resolver, 
				Settings.Secure.DEFAULT_INPUT_METHOD);
		if(inputmethod!=null &&!inputMethod.equalsIgnoreCase(inputmethod)){
			Settings.Secure.putString(resolver,
					Settings.Secure.DEFAULT_INPUT_METHOD, inputmethod);
			Settings.Secure.putString(resolver,
					Settings.Secure.ENABLED_INPUT_METHODS, inputmethod);
		}
	}
	
	private void initDomybox10SInternal() {

		TV_NAME = 0;
		PROTECT_MONITOR_TIME = 1;
		TIME_TYPE = 2;
		DEVELOPER_SETTINGS = 3;
		VERSION_CHECK = 4;
		RESET = 5;
		MAIBISUO = 6;
		AUTO_POWER_TIME = 7;
		INPUT=8;
		SENDLOG = 9;
		LIST_SIZE=10;
		
		
		pageLefts[MAIBISUO] = this.getResources().getDrawable(
				R.drawable.page_left);
		pageRights[MAIBISUO] = this.getResources().getDrawable(
				R.drawable.page_right);
		pageLefts[INPUT]=this.getResources().getDrawable(
					  R.drawable.page_left);
					  pageRights[INPUT]=this.getResources().getDrawable(
					  R.drawable.page_right);
		
		 
		 

		String[] mTempArray = this.getResources().getStringArray(
				R.array.commonsetting);

		pageLefts[TV_NAME] = pageLefts[PROTECT_MONITOR_TIME] = pageLefts[TIME_TYPE] =  pageLefts[AUTO_POWER_TIME] = this
				.getResources().getDrawable(R.drawable.page_left);
		pageRights[TV_NAME] = pageRights[PROTECT_MONITOR_TIME] = pageRights[TIME_TYPE] =  pageRights[AUTO_POWER_TIME] = this
				.getResources().getDrawable(R.drawable.page_right);

		mItemSettings[TV_NAME] = CommonActivity.getStringArrays(mContext,
				R.array.tv_name, mTvNameSelectId);

		mItemSettings[PROTECT_MONITOR_TIME] = (String) screenTimeoutEntries[monitorProtectId];
		mItemSettings[TIME_TYPE] = CommonActivity.getStringArrays(mContext,
				R.array.time_array3, timeTypeId);
		mItemSettings[AUTO_POWER_TIME] = (String) autopowerTimeoutEntries[autopowerId];

		mItemSettings[MAIBISUO] = WheatCode[wheatCodeSelectId];
		mItemSettings[INPUT]=inputObjList.get(inputId).getName();
		switchInputMethod(this, inputObjList.get(inputId).getId());
		
		

		mArrays = new String[LIST_SIZE];
		mArrays[TV_NAME] = mTempArray[0];
		mArrays[INPUT]=mTempArray[15];
		mArrays[PROTECT_MONITOR_TIME] = mTempArray[1];
		mArrays[TIME_TYPE] = mTempArray[2];
		mArrays[DEVELOPER_SETTINGS] = mTempArray[7];
		mArrays[VERSION_CHECK] = mTempArray[8];
		mArrays[RESET] = mTempArray[9];
		mArrays[SENDLOG] = mTempArray[16];
		mArrays[MAIBISUO] = mTempArray[12];
		mArrays[AUTO_POWER_TIME] = mTempArray[13];

	}
	
	private void initDomybox10sOverseas() {
		LANGUAGE_SELECT = 0;
		TV_NAME = 1;
		PROTECT_MONITOR_TIME = 2;
		TIME_TYPE = 3;
		TIMEZONE_SETTINGS = 4;
		DEVELOPER_SETTINGS = 5;
		VERSION_CHECK = 6;
		RESET = 7;
		AUTO_POWER_TIME = 8;
		SENDLOG = 9;
		LIST_SIZE = 10;


		String[] mTempArray = this.getResources().getStringArray(
				R.array.commonsetting);
		pageLefts[TV_NAME] = pageLefts[PROTECT_MONITOR_TIME] = pageLefts[TIME_TYPE] = pageLefts[AUTO_POWER_TIME] = this
				.getResources().getDrawable(R.drawable.page_left);
		pageRights[TV_NAME] = pageRights[PROTECT_MONITOR_TIME] = pageRights[TIME_TYPE] = pageRights[AUTO_POWER_TIME] = this
				.getResources().getDrawable(R.drawable.page_right);
		mItemSettings[TV_NAME] = CommonActivity.getStringArrays(mContext,
				R.array.tv_name, mTvNameSelectId);
		mItemSettings[PROTECT_MONITOR_TIME] = (String) screenTimeoutEntries[monitorProtectId];
		mItemSettings[TIME_TYPE] = CommonActivity.getStringArrays(mContext,
				R.array.time_array3, timeTypeId);
		mItemSettings[AUTO_POWER_TIME] = (String) autopowerTimeoutEntries[autopowerId];
		mItemSettings[TIMEZONE_SETTINGS] = getDisplayTimezone((String) TimeZone
				.getDefault().getID(), getTimeZoneCity());
		languageDesc = this.getResources().getStringArray(
				R.array.choose_language);
		languageId = Utils.getLanguage();
		mItemSettings[LANGUAGE_SELECT] = languageDesc[languageId];
		Log.v(TAG, "ssss==" + languageDesc[languageId]);

		mArrays = new String[LIST_SIZE];
		mArrays[TV_NAME] = mTempArray[0];
		mArrays[PROTECT_MONITOR_TIME] = mTempArray[1];
		mArrays[TIME_TYPE] = mTempArray[2];
		mArrays[TIMEZONE_SETTINGS] = mTempArray[6];
		mArrays[DEVELOPER_SETTINGS] = mTempArray[7];
		mArrays[VERSION_CHECK] = mTempArray[8];
		mArrays[RESET] = mTempArray[9];
		mArrays[LANGUAGE_SELECT] = mTempArray[14];
		mArrays[AUTO_POWER_TIME] = mTempArray[12];
		mArrays[SENDLOG] = mTempArray[16];
	}
	
	private void initDomyboxS905Internal(){
       //INPUT的处理是905X的输入法导入
		TV_NAME = 0;
		PROTECT_MONITOR_TIME = 1;
		TIME_TYPE = 2;
		DEVELOPER_SETTINGS = 3;
		VERSION_CHECK = 4;
		RESET = 5;
		MAIBISUO = 6;
		AUTO_POWER_TIME = 7;
		BLUETOOTH_SETTING = 8;
		INPUT=9;
		SENDLOG = 10;
		LIST_SIZE=11;

		
		pageLefts[MAIBISUO] = this.getResources().getDrawable(
				R.drawable.page_left);
		pageRights[MAIBISUO] = this.getResources().getDrawable(
				R.drawable.page_right);
		pageLefts[INPUT]=this.getResources().getDrawable(
					  R.drawable.page_left);
					  pageRights[INPUT]=this.getResources().getDrawable(
					  R.drawable.page_right);


		String[] mTempArray = this.getResources().getStringArray(
				R.array.commonsetting);

		pageLefts[TV_NAME] = pageLefts[PROTECT_MONITOR_TIME] = pageLefts[TIME_TYPE] = pageLefts[AUTO_POWER_TIME] = this
				.getResources().getDrawable(R.drawable.page_left);
		pageRights[TV_NAME] = pageRights[PROTECT_MONITOR_TIME] = pageRights[TIME_TYPE] = pageRights[AUTO_POWER_TIME] = this
				.getResources().getDrawable(R.drawable.page_right);

		mItemSettings[TV_NAME] = CommonActivity.getStringArrays(mContext,
				R.array.tv_name, mTvNameSelectId);

		mItemSettings[PROTECT_MONITOR_TIME] = (String) screenTimeoutEntries[monitorProtectId];
		mItemSettings[TIME_TYPE] = CommonActivity.getStringArrays(mContext,
				R.array.time_array3, timeTypeId);
		mItemSettings[AUTO_POWER_TIME] = (String) autopowerTimeoutEntries[autopowerId];

		mItemSettings[MAIBISUO] = WheatCode[wheatCodeSelectId];
		mItemSettings[INPUT]=inputObjList.get(inputId).getName();
	    switchInputMethod(this, inputObjList.get(inputId).getId());
		

		mArrays = new String[LIST_SIZE];
		mArrays[TV_NAME] = mTempArray[0];
		mArrays[INPUT]=mTempArray[15];
		mArrays[PROTECT_MONITOR_TIME] = mTempArray[1];
		mArrays[TIME_TYPE] = mTempArray[2];
		mArrays[DEVELOPER_SETTINGS] = mTempArray[7];
		mArrays[VERSION_CHECK] = mTempArray[8];
		mArrays[RESET] = mTempArray[9];
		mArrays[SENDLOG] = mTempArray[16];
		mArrays[MAIBISUO] = mTempArray[12];
		mArrays[AUTO_POWER_TIME] = mTempArray[13];
		mArrays[BLUETOOTH_SETTING] =mTempArray[5];
	 
	}
	
    private void initDomyboxS905Overseas(){
    	LANGUAGE_SELECT = 0;
		TV_NAME = 1;
		PROTECT_MONITOR_TIME = 2;
		TIME_TYPE = 3;
		TIMEZONE_SETTINGS = 4;
		DEVELOPER_SETTINGS = 5;
		VERSION_CHECK = 6;
		RESET = 7;
		AUTO_POWER_TIME = 8;
		BLUETOOTH_SETTING = 9;
		SENDLOG = 10;
		LIST_SIZE = 11;
		

		String[] mTempArray = this.getResources().getStringArray(
				R.array.commonsetting);
		pageLefts[TV_NAME] = pageLefts[PROTECT_MONITOR_TIME] = pageLefts[TIME_TYPE]  = pageLefts[AUTO_POWER_TIME] = this
				.getResources().getDrawable(R.drawable.page_left);
		pageRights[TV_NAME] = pageRights[PROTECT_MONITOR_TIME] = pageRights[TIME_TYPE]  = pageRights[AUTO_POWER_TIME] = this
				.getResources().getDrawable(R.drawable.page_right);
		mItemSettings[TV_NAME] = CommonActivity.getStringArrays(mContext,
				R.array.tv_name, mTvNameSelectId);
		mItemSettings[PROTECT_MONITOR_TIME] = (String) screenTimeoutEntries[monitorProtectId];
		mItemSettings[TIME_TYPE] = CommonActivity.getStringArrays(mContext,
				R.array.time_array3, timeTypeId);
		mItemSettings[AUTO_POWER_TIME] = (String) autopowerTimeoutEntries[autopowerId];
		mItemSettings[TIMEZONE_SETTINGS] = getDisplayTimezone((String) TimeZone
				.getDefault().getID(), getTimeZoneCity());
		languageDesc = this.getResources().getStringArray(
				R.array.choose_language);
		languageId = Utils.getLanguage();
		mItemSettings[LANGUAGE_SELECT] = languageDesc[languageId];
		Log.v(TAG, "ssss==" + languageDesc[languageId]);

		mArrays = new String[LIST_SIZE];
		mArrays[TV_NAME] = mTempArray[0];
		mArrays[PROTECT_MONITOR_TIME] = mTempArray[1];
		mArrays[TIME_TYPE] = mTempArray[2];
		mArrays[TIMEZONE_SETTINGS] = mTempArray[6];
		mArrays[DEVELOPER_SETTINGS] = mTempArray[7];
		mArrays[VERSION_CHECK] = mTempArray[8];
		mArrays[RESET] = mTempArray[9];
		mArrays[SENDLOG] = mTempArray[16];
		mArrays[LANGUAGE_SELECT] = mTempArray[14];
		mArrays[AUTO_POWER_TIME] = mTempArray[13];
		mArrays[BLUETOOTH_SETTING] =mTempArray[5];
	}
    
    /**
     * 905x平台　4.0s国内版本
     */
    private void initDomyboxS90540SInternal(){
 		TV_NAME = 0;
 		PROTECT_MONITOR_TIME = 1;
 		TIME_TYPE = 2;
 		DEVELOPER_SETTINGS = 3;
 		VERSION_CHECK = 4;
 		RESET = 5;
 		MAIBISUO = 6;
 		AUTO_POWER_TIME = 7;
 		BLUETOOTH_SETTING = 8;
 		INPUT=9;	
 		CONTROL_SETTING=10;
 		SENDLOG = 11;
 		LIST_SIZE=12;		

 		
 		pageLefts[MAIBISUO] = this.getResources().getDrawable(
 				R.drawable.page_left);
 		pageRights[MAIBISUO] = this.getResources().getDrawable(
 				R.drawable.page_right);
 		pageLefts[INPUT]=this.getResources().getDrawable(
 					  R.drawable.page_left);
 					  pageRights[INPUT]=this.getResources().getDrawable(
 					  R.drawable.page_right);


 		String[] mTempArray = this.getResources().getStringArray(
 				R.array.commonsetting);

 		pageLefts[TV_NAME] = pageLefts[PROTECT_MONITOR_TIME] = pageLefts[TIME_TYPE] = pageLefts[AUTO_POWER_TIME] = this
 				.getResources().getDrawable(R.drawable.page_left);
 		pageRights[TV_NAME] = pageRights[PROTECT_MONITOR_TIME] = pageRights[TIME_TYPE] = pageRights[AUTO_POWER_TIME] = this
 				.getResources().getDrawable(R.drawable.page_right);

 		mItemSettings[TV_NAME] = CommonActivity.getStringArrays(mContext,
 				R.array.tv_name, mTvNameSelectId);

 		mItemSettings[PROTECT_MONITOR_TIME] = (String) screenTimeoutEntries[monitorProtectId];
 		mItemSettings[TIME_TYPE] = CommonActivity.getStringArrays(mContext,
 				R.array.time_array3, timeTypeId);
 		mItemSettings[AUTO_POWER_TIME] = (String) autopowerTimeoutEntries[autopowerId];

 		mItemSettings[MAIBISUO] = WheatCode[wheatCodeSelectId];
 		mItemSettings[INPUT]=inputObjList.get(inputId).getName();
 	    switchInputMethod(this, inputObjList.get(inputId).getId());
 		

 		mArrays = new String[LIST_SIZE];
 		mArrays[TV_NAME] = mTempArray[0];
 		mArrays[INPUT]=mTempArray[15];
 		mArrays[PROTECT_MONITOR_TIME] = mTempArray[1];
 		mArrays[TIME_TYPE] = mTempArray[2];
 		mArrays[DEVELOPER_SETTINGS] = mTempArray[7];
 		mArrays[VERSION_CHECK] = mTempArray[8];
 		mArrays[RESET] = mTempArray[9];
 		mArrays[SENDLOG] = mTempArray[16];
 		mArrays[MAIBISUO] = mTempArray[12];
 		mArrays[AUTO_POWER_TIME] = mTempArray[13];
 		mArrays[BLUETOOTH_SETTING] =mTempArray[5];
 		mArrays[CONTROL_SETTING]=mTempArray[4];
 	 
 	}
    
    
    /**
     * S905x平台，4.0s海外版本
     */
    private void initDomyboxS90540SOverseas(){
    	LANGUAGE_SELECT = 0;
		TV_NAME = 1;
		PROTECT_MONITOR_TIME = 2;
		TIME_TYPE = 3;
		TIMEZONE_SETTINGS = 4;
		DEVELOPER_SETTINGS = 5;
		VERSION_CHECK = 6;
		RESET = 7;
		AUTO_POWER_TIME = 8;
		BLUETOOTH_SETTING = 9;
		SENDLOG = 10;
		//CONTROL_SETTING=10;
		LIST_SIZE = 11;
		

		String[] mTempArray = this.getResources().getStringArray(
				R.array.commonsetting);
		pageLefts[TV_NAME] = pageLefts[PROTECT_MONITOR_TIME] = pageLefts[TIME_TYPE]  = pageLefts[AUTO_POWER_TIME] = this
				.getResources().getDrawable(R.drawable.page_left);
		pageRights[TV_NAME] = pageRights[PROTECT_MONITOR_TIME] = pageRights[TIME_TYPE]  = pageRights[AUTO_POWER_TIME] = this
				.getResources().getDrawable(R.drawable.page_right);
		mItemSettings[TV_NAME] = CommonActivity.getStringArrays(mContext,
				R.array.tv_name, mTvNameSelectId);
		mItemSettings[PROTECT_MONITOR_TIME] = (String) screenTimeoutEntries[monitorProtectId];
		mItemSettings[TIME_TYPE] = CommonActivity.getStringArrays(mContext,
				R.array.time_array3, timeTypeId);
		mItemSettings[AUTO_POWER_TIME] = (String) autopowerTimeoutEntries[autopowerId];
		mItemSettings[TIMEZONE_SETTINGS] = getDisplayTimezone((String) TimeZone
				.getDefault().getID(), getTimeZoneCity());
		languageDesc = this.getResources().getStringArray(
				R.array.choose_language);
		languageId = Utils.getLanguage();
		mItemSettings[LANGUAGE_SELECT] = languageDesc[languageId];
		Log.v(TAG, "ssss==" + languageDesc[languageId]);

		mArrays = new String[LIST_SIZE];
		mArrays[TV_NAME] = mTempArray[0];
		mArrays[PROTECT_MONITOR_TIME] = mTempArray[1];
		mArrays[TIME_TYPE] = mTempArray[2];
		mArrays[TIMEZONE_SETTINGS] = mTempArray[6];
		mArrays[DEVELOPER_SETTINGS] = mTempArray[7];
		mArrays[VERSION_CHECK] = mTempArray[8];
		mArrays[RESET] = mTempArray[9];
		mArrays[SENDLOG] = mTempArray[16];
		mArrays[LANGUAGE_SELECT] = mTempArray[14];
		mArrays[AUTO_POWER_TIME] = mTempArray[13];
		mArrays[BLUETOOTH_SETTING] =mTempArray[5];
		//mArrays[CONTROL_SETTING]=mTempArray[4];
	}
    
    /**
     * s905x平台,iptv版本
     */
    private void initDomybox905IPTV() {

    	Log.v(TAG, "iptv......");
		TV_NAME = 0;
		DEVELOPER_SETTINGS = 1;
		VERSION_CHECK = 2;
		RESET = 3;
		AUTO_POWER_TIME = 4;
		SENDLOG = 5;
		LIST_SIZE=6;
		

		//检测是否隐藏开发者选项
		if(!SystemProperties.get(Utils.PROP_DEVELOPER_ALLOW, "true").equals("true")){
			DEVELOPER_SETTINGS=100;
			VERSION_CHECK--;
			RESET--;
			AUTO_POWER_TIME--;
			SENDLOG--;
			LIST_SIZE--;
		}
		

		String[] mTempArray = this.getResources().getStringArray(
				R.array.commonsetting);

		pageLefts[TV_NAME] = pageLefts[AUTO_POWER_TIME] = this
				.getResources().getDrawable(R.drawable.page_left);
		pageRights[TV_NAME] = pageRights[AUTO_POWER_TIME] = this
				.getResources().getDrawable(R.drawable.page_right);

		mItemSettings[TV_NAME] = CommonActivity.getStringArrays(mContext,
				R.array.tv_name, mTvNameSelectId);
 
		mItemSettings[AUTO_POWER_TIME] = (String) autopowerTimeoutEntries[autopowerId];
  

		mArrays = new String[LIST_SIZE];
		mArrays[TV_NAME] = mTempArray[0];
		if(SystemProperties.get(Utils.PROP_DEVELOPER_ALLOW, "true").equals("true")){
		   mArrays[DEVELOPER_SETTINGS] = mTempArray[7];
		}
		mArrays[VERSION_CHECK] = mTempArray[8];
		mArrays[RESET] = mTempArray[9];
		mArrays[SENDLOG] = mTempArray[16];
		mArrays[AUTO_POWER_TIME] = mTempArray[13];

	}
    
    /**
     * 创建checkPackInfo()方法
     */
    
    private boolean checkPackInfo(String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
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
}
