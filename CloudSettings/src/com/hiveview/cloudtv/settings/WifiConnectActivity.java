package com.hiveview.cloudtv.settings;

import static android.net.wifi.WifiConfiguration.INVALID_NETWORK_ID;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Timer;
import java.util.TimerTask;

import android.net.IpConfiguration;
import android.net.StaticIpConfiguration;
import android.R.bool;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.NetworkUtils;
import android.net.RouteInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.IpConfiguration.IpAssignment;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.EthernetManager;
import android.os.SystemProperties;

import com.hiveview.cloudtv.settings.connectivity.ConnectivityStateService;
import com.hiveview.cloudtv.settings.util.HttpCheck;
import com.hiveview.cloudtv.settings.util.ToastUtils;
import com.hiveview.cloudtv.settings.util.HttpCheck.HttpCheckListener;
import com.hiveview.cloudtv.settings.util.Utils;
import com.hiveview.cloudtv.settings.widget.CloundMenuWindow;
import com.hiveview.cloudtv.settings.widget.CustomProgressDialog;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView;
import com.hiveview.cloudtv.settings.widget.CommonListView;
import com.hiveview.cloudtv.settings.widget.CloundMenuWindow.MenuItemEntity;
import com.hiveview.cloudtv.settings.widget.CloundMenuWindow.OnSelectedItemClickListener;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView.FocusViewAnimatorEndListener;
import com.hiveview.cloudtv.settings.wifi.WifiAccessPoint;
import com.hiveview.cloudtv.settings.wifi.WifiDataEntity;
import com.hiveview.cloudtv.settings.wifi.WifiInfoConfigEntity;
import com.hiveview.cloudtv.settings.wifi.WifiItemHolder;
import com.hiveview.manager.PingListener;
import com.hiveview.manager.PingManager;


public class WifiConnectActivity extends Activity implements WifiAccessPoint.RefershCallbacks{
	private static final String TAG = "WifiConnectActivity";
	private static final boolean DEBUG = true;
	// Combo scans can take 5-6s to complete - set to 10s.
	private static final int WIFI_RESCAN_INTERVAL_MS = 10 * 1000;
	private static final int WIFI_CONNECT_STATE_MSG = 0x0001;
	private static final int WIFI_CONNECT_STATE_FAILED_MSG = 0x0002;
	private static final int WIFI_CONNECT_STATE_SUCCESS_MSG = 0x0003;
	private static final int WIFI_CONNECT_TIMEOUT = 0x0004;
	private static final int WIFI_UPDATE_LIST = 0x0005;
	
	
	private static final int WIFI_ACCESS_POINTS_UPDATE_ACTION = 0x0006;
 
	
	private static final int WIFI_OP_RE_CONNECT = 1000;
	private static final int WIFI_OP_FORGET = 999;
	
	private CommonListView mWifiList = null;
	private WifiItemHolder mItemCurHolder = null;
	private WifiConnectAdapter mWifiAdapter = null;
	private IntentFilter mWifiMSGFilter = null;
	private BroadcastReceiver mWifiMSGReceiver = null;
	private Scanner mScanner;
	private WifiDataEntity mWifiDataEntity = null;
	private List<WifiAccessPoint> mAccessPoints;
	private AtomicBoolean mConnected = new AtomicBoolean(false);
	private WifiAccessPoint mSelectedAccessPoint = null;
	private WifiAccessPoint mClickAccessPoint = null;
	private WifiAccessPoint oldAccessPoint = null;
	private DetailedState mLastState;
	private WifiInfo mLastInfo;
	private ImageView mCurWifiConnectStateImg = null;
	private Handler mHandler = null;
	private int mErrNetId = -1;
	private int mListCurPosition = -1;
	private LinkProperties mLinkProperties = null;
	private CustomProgressDialog mProgressDialog = null;

    private Timer timeoutTimer;
	// list focus view
	private View mItemListCurView = null;
	private LauncherFocusView mLauncherFocusView = null;
	private boolean mIsFirstIn = true;
	
	private int curPingStatus = 0;
	private int top = 0;
	
	private int mCurKeycode = KeyEvent.KEYCODE_0;
	private Animation mWifiPasswordErrorAnimation = null;

	private boolean mTextColorChangeFlag = false;
	private boolean mEditTextFocusFlag = false;
	private boolean mFocusAnimationEndFlag = false;
	private long mKeyDownTime = 0L;
	
	private WifiItemHolder mCurConnectHolder = null;
	private boolean connectingWifi = false;
	private boolean goTop = false;
	
	private boolean forgetSucessToast = true;
	
	private HandlerThread wifiHandlerThread = new HandlerThread("update_wifi_list");
	private Handler wifiHandler;
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		setContentView(R.layout.wifi_connect_main);
 
		
		initWifiHandle();
		mWifiMSGFilter = new IntentFilter();
		mWifiMSGFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		mWifiMSGFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		mWifiMSGFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
		mWifiMSGFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		mWifiMSGFilter.addAction(WifiManager.CONFIGURED_NETWORKS_CHANGED_ACTION);
		mWifiMSGFilter.addAction(WifiManager.LINK_CONFIGURATION_CHANGED_ACTION);
		mWifiMSGFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		mWifiMSGFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		mWifiMSGReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				handleEvent(context, intent);
			}
		};
		mWifiDataEntity = WifiDataEntity.getInstance(this);
		Log.v(TAG, "mWifiDataEntity.getWifiApState=="+mWifiDataEntity.getWifiApState());
		//if(mWifiDataEntity.getWifiApState() != WifiManager.WIFI_AP_STATE_DISABLED){
		if(mWifiDataEntity.getWifiApState() == WifiManager.WIFI_AP_STATE_ENABLED||
			mWifiDataEntity.getWifiApState() == WifiManager.WIFI_AP_STATE_ENABLING){
			mWifiDataEntity.setWifiApEnabled(null, false);
			try{
			  Thread.sleep(500);
			}catch(Exception e){
			  e.printStackTrace();
			}
			Log.v(TAG,"set wifi AP disable true");
		}
		
 
		Settings.Global.putInt(getContentResolver(), Settings.Global.WIFI_SAVED_STATE, 0);
 
		mScanner = new Scanner();
		startProgressDialog(getResources().getString(R.string.wifi_scaning));

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case WIFI_CONNECT_STATE_MSG:
					curPingStatus = msg.arg1; 
					Log.v(TAG, "update wifi ping state=="+curPingStatus);
					if(mEditTextFocusFlag){
						if(DEBUG){
							Log.e(TAG, "mEditTextFocusFlag ...... flag="+mEditTextFocusFlag);
						}
						return;
					}
					WifiConnectAdapter mAdapter = (WifiConnectAdapter) mWifiList.getAdapter();
					if (null != mAdapter) {
						mAdapter.notifyDataSetChanged();
					}
					break;
				case WIFI_CONNECT_STATE_FAILED_MSG:
					if(mItemCurHolder!=null){
						mItemCurHolder.mItemContentEdit.startAnimation(mWifiPasswordErrorAnimation);
						mItemCurHolder.mItemContentEdit.setText("");
					}
					mEditTextFocusFlag = false;
					ToastUtils.showToast(WifiConnectActivity.this, 
							getResources().getString(R.string.wifi_password_error), Toast.LENGTH_SHORT);
					connectingWifi = false;
					cancelTimer();
					break;
				case WIFI_CONNECT_STATE_SUCCESS_MSG:	
					Log.v(TAG, "cur pos=="+mListCurPosition);
					if(mListCurPosition>1){
						goTop = true;
						mWifiList.setSelectionFromTop(1, top);
//						mWifiList.setSelection(1);
//						updateAccessPoints();
					}
					break;
				case WIFI_CONNECT_TIMEOUT:
					connectingWifi = false;
					cancelTimer();
					break;
				case WIFI_UPDATE_LIST:
			        Log.v(TAG, "update wifi list......");
					refersh();
				default:
					break;
				}
			}
		};
 
		
		initView();
		regeditReceiver();
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode) { //根据状态码，处理返回结果  
        case WifiInfoConfigEntity.RESULT_WIFI_MODIFY_OK:  
        	  boolean saveFlag = data.getBooleanExtra(WifiInfoConfigEntity.SAVE_CONFIG, false);
        	  if(DEBUG){
        		  Log.e(TAG, "get save flag "+saveFlag);
        	  }
        	  if(saveFlag){
        		  WifiInfoConfigEntity dataConfig = data.getParcelableExtra(WifiInfoConfigEntity.KEY);
        		  WifiConfiguration wifiConfig = getWifiPointConfig(dataConfig);
        		  save(wifiConfig);
        	  }
        	break;
        case WifiInfoConfigEntity.RESULT_WIFI_FORGET:
        	forget();
        	break;
        case WifiInfoConfigEntity.RESULT_WIFI_ADD:
        	String ssid = data.getStringExtra(WifiInfoConfigEntity.SSID_KEY);
        	String password = data.getStringExtra(WifiInfoConfigEntity.PASSWORD_KEY);
        	int security = data.getIntExtra(WifiInfoConfigEntity.SECURITY_KEY, WifiAccessPoint.SECURITY_NONE);
        	WifiConfiguration config = getWifiAddConfig(ssid, password, security);
			if (config != null) {
				mWifiDataEntity.save(config, mSaveListener);
			}
			updateAccessPoints();
			mWifiList.setSelection(mWifiList.getAdapter().getCount() - 2);
        	break;
        default:  
        break;  
        }    
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
 
		unRegeditReceiver();
		stopProgressDialog();
	}



	private void initView() {
		mWifiList = (CommonListView) findViewById(R.id.wifi_list);
		mWifiList.setOnItemClickListener(mWifiOnItemClickListener);
		mWifiList.setOnItemSelectedListener(mWifiOnItemSelectedListener);
		mWifiList.setOnKeyListener(mWifiListOnKeyListener);
		

		// list focus view
		mIsFirstIn = true;
		mLauncherFocusView = (LauncherFocusView) findViewById(R.id.wifi_connect_focus_view);
		mLauncherFocusView.setAnimatorEndListener(new FocusViewAnimatorEndListener() {
			
			@Override
			public void OnAnimateStart(View currentFocusView) {
				// TODO Auto-generated method stub
				mFocusAnimationEndFlag = false;
			}

			@Override
			public void OnAnimateEnd(View currentFocusView) {
				// TODO Auto-generated method stub
				//mLauncherFocusView.initFocusView(mItemListCurView, false, 0f);
				mFocusAnimationEndFlag = true;
				listTextColorSet();
			}
		});

		mWifiPasswordErrorAnimation = AnimationUtils.loadAnimation(this, R.anim.wifi_password_error);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mScanner.pause();
		this.unregisterReceiver(mWifiMSGReceiver);
		mHandler.removeMessages(WIFI_CONNECT_STATE_MSG);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
        mWifiDataEntity.setWifiApEnabled(null, false);
        Log.v(TAG,"check wifi state....."+mWifiDataEntity.getWifiState());
        if (mWifiDataEntity.getWifiState()==WifiManager.WIFI_STATE_DISABLED || 
             mWifiDataEntity.getWifiState()==WifiManager.WIFI_STATE_UNKNOWN){
           mWifiDataEntity.setWifiEnabled(true);
           Log.v(TAG,"set wifi enable true.....");
        }  
        
		registerReceiver(mWifiMSGReceiver, mWifiMSGFilter);
		if (mWifiDataEntity.isWifiEnabled()) {
			mScanner.resume();
		}
		
		updateAccessPoints();
		
		if(Utils.checkEthState()){
        	finish();
		}
		super.onResume();
	}

	AdapterView.OnItemClickListener mWifiOnItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			// TODO Auto-generated method stub
            Log.v(TAG, "connectingWifi="+connectingWifi);
			if(connectingWifi && (position!=0)){
				Utils.showToast(WifiConnectActivity.this, getString(R.string.wifi_connecting), 1500);
				return;	
			}
			
			if (position == 0) {
				Utils.setApplicationBGBitmap(WifiConnectActivity.this);
				Intent intent = new Intent(WifiConnectActivity.this, ManualAddWifiAcvitity.class);
				startActivityForResult(intent, 0);
			}
			
			
			mClickAccessPoint = (WifiAccessPoint)(((WifiConnectAdapter)mWifiList.getAdapter()).getItem(position));
			Log.v(TAG, "onItemClick ssid="+mClickAccessPoint.ssid);
			if (position != 0 && mItemCurHolder != null && mClickAccessPoint!=null) {
				if (mClickAccessPoint.wifiConnState == WifiAccessPoint.STATE_DISABLE || mClickAccessPoint.getLevel()==-1) {
					showConnectOrForgetDialog(mClickAccessPoint);
					return;
				}
                connectingWifi = false;

				if (mClickAccessPoint.wifiConnState == WifiAccessPoint.STATE_CONNECTED) {
					onKeyRight();
					return;
				}

				if (mClickAccessPoint.security == WifiAccessPoint.SECURITY_NONE) {
					connect(mClickAccessPoint);
				}else{
					WifiConfiguration config = mClickAccessPoint.getConfig();
					if (config != null) {
//						Log.v(TAG, "3333=="+config.disableReason);
						//有时候wifi还没有进行校验，就直接保存了WIFI信息，这个时候需要能够忘记网络
						//&& config.disableReason != WifiConfiguration.DISABLED_AUTH_FAILURE
						if (hasPassword(config)) {
							showConnectOrForgetDialog(mClickAccessPoint);
						}
					}else{
//						Log.v(TAG, "2222");
                        mEditTextFocusFlag = true;
						mItemCurHolder.mItemContentEdit.setVisibility(View.VISIBLE);
						mItemCurHolder.mItemContentEdit.setFocusable(true);
						mWifiList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
					        mItemCurHolder.mItemContentEdit.setFocusableInTouchMode(true);	
						mItemCurHolder.mItemContentEdit.requestFocus();
						mItemCurHolder.mItemContentEdit.setCursorVisible(true);
						mItemCurHolder.mItemContentEdit.setOnEditorActionListener(mEditorActionListener);
						InputMethodManager mng = (InputMethodManager) WifiConnectActivity.this
								.getSystemService(Activity.INPUT_METHOD_SERVICE);
						mng.showSoftInput(mItemCurHolder.mItemContentEdit, InputMethodManager.SHOW_IMPLICIT);
						mItemCurHolder.mItemContentEdit.setText("");
					}
				}
			}else{
				if(DEBUG){
					Log.e(TAG, "enter here mItemCurHolder state:"+(mItemCurHolder==null)+
							" mClickAccessPoint state:"+(mClickAccessPoint==null));
				}
			}
		}

	};

	AdapterView.OnItemSelectedListener mWifiOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View view, int position, long arg3) {
			// TODO Auto-generated method stub
			// list focus view
			mItemListCurView = view;
			mListCurPosition = position;
			mSelectedAccessPoint = (WifiAccessPoint)(((WifiConnectAdapter)mWifiList.getAdapter()).getItem(position));
			if (mIsFirstIn) {
				mLauncherFocusView.initFocusView(mItemListCurView, false, 0f);
			}
			if(DEBUG){
				Log.e(TAG, "cur keycode :"+mCurKeycode+"mListCurPosition=="+mListCurPosition);
			}
			if(position==1){
				top = view.getTop();
			}


			if (mItemCurHolder != null) {
				if (mItemCurHolder.mItemContentEdit.isFocusable()) {
					mItemCurHolder.mItemContentEdit.setFocusable(false);
					mItemCurHolder.mItemContentEdit.setVisibility(View.INVISIBLE);
					mItemCurHolder.mItemContentEdit.setCursorVisible(false);
					mWifiList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
					mItemCurHolder.mItemContentEdit.requestFocus();
					mEditTextFocusFlag = false;
				}
			}
			// text color reset
			if (mItemCurHolder != null) {
				mItemCurHolder.mItemTitle.setTextColor(getResources().getColor(R.color.settings_9a9a9a));
				mItemCurHolder.mItemContentEdit.setTextColor(getResources().getColor(R.color.settings_9a9a9a));
			}
			mItemCurHolder = (WifiItemHolder) view.getTag();
			
			if(goTop){
				goTop = false;
				mLauncherFocusView.moveToWifiTop(mItemListCurView);
				Log.v(TAG, "moveToWifiTop");
				return;
			}
 
			if(mCurKeycode == KeyEvent.KEYCODE_DPAD_DOWN){
 
				if(mListCurPosition<5 || mListCurPosition>mWifiList.getCount()-2
						||(mWifiList.getFirstVisiblePosition()==0&&view.getTop()<(view.getHeight()*4))
						||(mWifiList.getFirstVisiblePosition()!=0&&view.getTop()<view.getHeight()*5)){
					mLauncherFocusView.moveTo(mItemListCurView);
				}else{
					mWifiList.setSelectionFromTop(mListCurPosition, view.getTop()-view.getHeight());
					listTextColorSet();
				}
 
			}else if(mCurKeycode == KeyEvent.KEYCODE_DPAD_UP){
				//mLauncherFocusView.moveTo(mItemListCurView);
				
				if(mListCurPosition == 1){
					mLauncherFocusView.initFocusView(mItemListCurView, false, 0f);
				}
				
				Log.e(TAG, "list getheight:"+mWifiList.getHeight()+
						" view getTop:"+view.getTop()+"height:"+
						view.getHeight()+" visible first:"+mWifiList.getFirstVisiblePosition());
				
				if((mListCurPosition == 0||mWifiList.getFirstVisiblePosition()==0&&view.getTop()>=(view.getHeight()))
						||(mWifiList.getFirstVisiblePosition()!=0&&view.getTop()>=view.getHeight())){
					mLauncherFocusView.moveTo(mItemListCurView);
				}else{
					listTextColorSet();
					mWifiList.setSelectionFromTop(mListCurPosition, view.getHeight());
				}
			}
 
			
			// text color set in mLaunchFocusView onAnimationEnd()
			if(mIsFirstIn){
				mIsFirstIn = false;
				mTextColorChangeFlag = true;
				listTextColorSet();
			}
			if(!mTextColorChangeFlag && mFocusAnimationEndFlag){
				if(mListCurPosition == 0 
						|| mListCurPosition==mWifiList.getCount()-1){
					mTextColorChangeFlag = true;
					listTextColorSet();
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	};

	private void listTextColorSet(){
		Log.v(TAG, "mTextColorChangeFlag=="+mTextColorChangeFlag+",mFocusAnimationEndFlag="+mFocusAnimationEndFlag);
		if (mItemCurHolder != null && mTextColorChangeFlag&&mFocusAnimationEndFlag) {
//			mTextColorChangeFlag = false;
			mItemCurHolder.mItemTitle.setTextColor(getResources().getColor(R.color.settings_ffffff));
			mItemCurHolder.mItemContentEdit.setTextColor(getResources().getColor(R.color.settings_ffffff));
			//mItemCurHolder.mItemSignalImg.setImageState(new int[] { R.attr.state_selected }, true);
//			Log.v(TAG, "set color white");
		}
	}
	
	View.OnKeyListener mWifiListOnKeyListener = new View.OnKeyListener() {
		
		@Override
		public boolean onKey(View view, int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_PAGE_DOWN || keyCode == KeyEvent.KEYCODE_PAGE_UP){
	    		return true;
	    	}
			if(event.getAction() == KeyEvent.ACTION_DOWN){
				if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN 
						|| keyCode==KeyEvent.KEYCODE_DPAD_UP){
					mCurKeycode =  keyCode;
					
					if(event.getRepeatCount() == 0){
						mTextColorChangeFlag = true;
						mKeyDownTime = event.getDownTime();
					}else{
						mTextColorChangeFlag = false;
						if(event.getDownTime()-mKeyDownTime>=Utils.KEYDOWN_DELAY_TIME){
							Log.e("KeyEvent", "time="+(event.getDownTime()-mKeyDownTime)+" count"+event.getRepeatCount());
							mKeyDownTime = event.getDownTime();
						}else{
							return true;
						}
					}
					if(!mFocusAnimationEndFlag){
						mTextColorChangeFlag = false;
					}
				}else if(keyCode  == KeyEvent.KEYCODE_MENU){
					
					if(mWifiDataEntity.getConnectionInfo().getSSID().equals("0x")){
						return true;  //如果没有已经连接上的热点，那么就不弹出断开对话框
					}
					
					 
					MenuItemEntity entity = new MenuItemEntity();
					entity.setItemName(WifiConnectActivity.this.getResources().getString(
							R.string.ethernet_item_disable_net));
					entity.setItemIconFocusResId(R.drawable.setting_focus);
					entity.setItemPosition(999);
					entity.setItemIconResId(R.drawable.setting_focus);

					List<MenuItemEntity> menuList = new ArrayList<CloundMenuWindow.MenuItemEntity>();
					menuList.add(entity);
					CloundMenuWindow window = new CloundMenuWindow(WifiConnectActivity.this, menuList);

					window.setItemSelectedListener(new OnSelectedItemClickListener() {

						@Override
						public void selectedItemClick(MenuItemEntity entity) {
							// TODO Auto-generated method stub

							WifiAccessPoint accessPoint = (WifiAccessPoint)(((WifiConnectAdapter)mWifiList.getAdapter()).getItem(1));
							if(DEBUG){
								Log.i(TAG, "cur wifi connect state:"+accessPoint.wifiConnState);
							}
							if (accessPoint.wifiConnState == WifiAccessPoint.STATE_CONNECTED || 
									accessPoint.wifiConnState == WifiAccessPoint.STATE_CONNECTING) {
								if (accessPoint.networkId == INVALID_NETWORK_ID) {
									// Should not happen, but a monkey seems to triger it
									Log.e(TAG, "Failed to forget invalid network " + mSelectedAccessPoint.getConfig());
									return;
								}
                                connectingWifi = false;
								mWifiDataEntity.forget(accessPoint.networkId, mForgetListener);

								if (mWifiDataEntity.isWifiEnabled()) {
									mScanner.resume();
								}
								updateAccessPoints();
							}

						}
					});
					window.show();
				}
			}else if(event.getAction() == KeyEvent.ACTION_UP){
				if(!mTextColorChangeFlag){
					mTextColorChangeFlag = true;
					listTextColorSet();
				}
			}
			// TODO Auto-generated method stub
			return false;
		}
	};
	
	
	private void showConnectOrForgetDialog(final WifiAccessPoint wifiAccessPoint){
		List<MenuItemEntity> menuList = new ArrayList<CloundMenuWindow.MenuItemEntity>();
		if(wifiAccessPoint.getLevel()!=-1){
	        MenuItemEntity connect_entity = new MenuItemEntity();
	        connect_entity.setItemName(WifiConnectActivity.this.getResources().getString(
					R.string.ethernet_dis_reconnect));
	        connect_entity.setItemIconFocusResId(R.drawable.setting_focus);
	        connect_entity.setItemPosition(WIFI_OP_RE_CONNECT);
	        connect_entity.setItemIconResId(R.drawable.setting_focus);
	        
	        menuList.add(connect_entity);
		}
        
		MenuItemEntity entity = new MenuItemEntity();
		entity.setItemName(WifiConnectActivity.this.getResources().getString(
				R.string.ethernet_item_disable_net));
		entity.setItemIconFocusResId(R.drawable.setting_focus);
		entity.setItemPosition(WIFI_OP_FORGET);
		entity.setItemIconResId(R.drawable.setting_focus);
		menuList.add(entity);
		
		
		CloundMenuWindow window = new CloundMenuWindow(WifiConnectActivity.this, menuList);

		window.setItemSelectedListener(new OnSelectedItemClickListener() {

			@Override
			public void selectedItemClick(MenuItemEntity entity) {
				// TODO Auto-generated method stub
				Log.v(TAG, "entity ===" + entity.getItemPosition());

				if (wifiAccessPoint.networkId == INVALID_NETWORK_ID)
					return;
				if (entity.getItemPosition() == WIFI_OP_RE_CONNECT) {
					connect(wifiAccessPoint);
				} else if (entity.getItemPosition() == WIFI_OP_FORGET) {
					connectingWifi = false;
					mWifiDataEntity.forget(wifiAccessPoint.networkId,mForgetListener);
					if (mWifiDataEntity.isWifiEnabled()) {
						mScanner.resume();
					}
					updateAccessPoints();
				}
			}
		});
		window.show();
	}

	WifiManager.ActionListener mConnectListener = new WifiManager.ActionListener() {
		public void onSuccess() {
			if(DEBUG){
				Log.e(TAG, "wifi connect success!");
			}
			mEditTextFocusFlag = false;
			mHandler.sendEmptyMessage(WIFI_CONNECT_STATE_SUCCESS_MSG);
		}

		public void onFailure(int reason) {
			if(DEBUG){
				Log.e(TAG, "wifi connect failed!");
			}
			mHandler.sendEmptyMessage(WIFI_CONNECT_STATE_FAILED_MSG);
		}
	};

	WifiManager.ActionListener mSaveListener = new WifiManager.ActionListener() {
		public void onSuccess() {
		}

		public void onFailure(int reason) {
			ToastUtils.showToast(WifiConnectActivity.this, getResources().getString(R.string.wifi_failed_save_message), Toast.LENGTH_SHORT);
		}
	};

	WifiManager.ActionListener mForgetListener = new WifiManager.ActionListener() {
		public void onSuccess() {
			if(!forgetSucessToast){
				forgetSucessToast = true;
				return;
			}
			ToastUtils.showToast(WifiConnectActivity.this, getResources().getString(R.string.wifi_sucess_forget_message),
					Toast.LENGTH_SHORT);
		}

		public void onFailure(int reason) {
			ToastUtils.showToast(WifiConnectActivity.this, getResources().getString(R.string.wifi_failed_forget_message),
						Toast.LENGTH_SHORT);
		}
	};

	private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			Log.v(TAG, "mEditorActionListener");
			if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
				try {
					if (DEBUG) {
						Log.e(TAG, "onclick enter password="+mClickAccessPoint.ssid);
					}
					InputMethodManager mng = (InputMethodManager) WifiConnectActivity.this
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					mng.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					
					if(view.getText().toString().trim().equals("")){
						ToastUtils.showToast(WifiConnectActivity.this, getResources().getString(R.string.wifi_password_null), 1500);
						return true;
					}
					Log.e(TAG, "onclick enter password="+mClickAccessPoint.ssid);
					connect(mClickAccessPoint);
					
					
					if (mItemCurHolder.mItemContentEdit.isFocusable()) {
						mItemCurHolder.mItemContentEdit.setFocusable(false);
						mItemCurHolder.mItemContentEdit.setVisibility(View.INVISIBLE);
						mItemCurHolder.mItemContentEdit.setCursorVisible(false);
						mWifiList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
						mItemCurHolder.mItemContentEdit.requestFocus();
						mEditTextFocusFlag = false;
					}
				} catch (Exception e) {
					Log.e(TAG, "Hide key broad error", e);
				}
			}
			return false;
		}
	};
	
	public void startWifiConnectLoading() {
		if(mListCurPosition == 0){
			return;
		}
        if(mCurConnectHolder!=null){
        	mCurConnectHolder.mItemSignalImg.setImageBitmap(null);
        	mCurConnectHolder.mItemSignalImg.setBackgroundResource(R.drawable.wifi_connect_progress);
    		AnimationDrawable animationDrawable = (AnimationDrawable)mCurConnectHolder.mItemSignalImg.getBackground();  
            
            if(!animationDrawable.isRunning())
            {  
                animationDrawable.start();  
            }
        }
	}

	@SuppressLint("NewApi")
	public void stopWifiConnectLoading(){
        if(mCurConnectHolder!=null){
        	mCurConnectHolder.mItemSignalImg.setBackgroundResource(R.drawable.wifi_connect_progress);
        	AnimationDrawable animationDrawable = (AnimationDrawable)mCurConnectHolder.mItemSignalImg.getBackground();  
            
            if(animationDrawable.isRunning())
            {  
                animationDrawable.stop();  
            }
            mCurConnectHolder.mItemSignalImg.setBackground(null);
        }

	}
	
	private void connect(WifiAccessPoint wifiAccessPoint){
		Log.i(TAG,"i am come to connect");
		mSelectedAccessPoint = wifiAccessPoint;
		startTimer();
		if (wifiAccessPoint.wifiConnState == WifiAccessPoint.STATE_DISABLE || wifiAccessPoint.getLevel()==-1) {
			ToastUtils.showToast(WifiConnectActivity.this, getResources().getString(R.string.wifi_not_in_range), Toast.LENGTH_SHORT);
			return;
		}

		if (wifiAccessPoint.wifiConnState == WifiAccessPoint.STATE_CONNECTED) {
			onKeyRight();
			return;
		}
		stopWifiConnectLoading();
		mCurConnectHolder = mItemCurHolder;
		
        connectingWifi = true;

		mWifiDataEntity.disconnect();
		if (mSelectedAccessPoint.security == WifiAccessPoint.SECURITY_NONE) {
			wifiAccessPoint.generateOpenNetworkConfig();
			mWifiDataEntity.connect(wifiAccessPoint.getConfig(), mConnectListener);
		} else {
			WifiConfiguration config = wifiAccessPoint.getConfig();
			Log.v(TAG, "connect ssid==="+wifiAccessPoint.ssid);
			if (config != null) {
				if (hasPassword(config)
						&& config.disableReason != WifiConfiguration.DISABLED_AUTH_FAILURE) {
					mWifiDataEntity.connect(config, mConnectListener);
				} else {
					WifiConfiguration tmpconfig = getConfig(wifiAccessPoint, 
							mItemCurHolder.mItemContentEdit.getText().toString());
					Log.i(TAG,"i am come to cnnect"+ mItemCurHolder.mItemContentEdit.getText().toString());
					mWifiDataEntity.connect(tmpconfig, mConnectListener);
				}
			} else {
				WifiConfiguration tmpconfig = getConfig(wifiAccessPoint, 
						mItemCurHolder.mItemContentEdit.getText().toString());
				mWifiDataEntity.connect(tmpconfig, mConnectListener);
			}
		}
	}
 
	
	
	private void save(WifiConfiguration config){
		Log.v(TAG, "save wifi config");
		startTimer();
		if (config == null) {
			if (mSelectedAccessPoint != null && mSelectedAccessPoint.networkId != INVALID_NETWORK_ID) {
				Log.i(TAG, "-1--heyf---ssid:" + mSelectedAccessPoint.ssid + " config:"
						+ mSelectedAccessPoint.getConfig().toString());
				mWifiDataEntity.connect(mSelectedAccessPoint.networkId, mConnectListener);
			}
		} else if (config.networkId != INVALID_NETWORK_ID) {
			/*			if (hasPassword(config)) {
				if (config.disableReason != WifiConfiguration.DISABLED_AUTH_FAILURE) {
					Log.v(TAG, "save disconnect");
					mWifiDataEntity.disconnect();
					Log.i(TAG, "-2--heyf---ssid:" + mSelectedAccessPoint.ssid + " config:"
							+ config.ipAssignment + " state:" + config.status);
					mWifiDataEntity.connect(config, mConnectListener);
					mWifiDataEntity.saveConfiguration();
				} else {*/
					Log.v(TAG, "1save.....");
					mWifiDataEntity.save(config, mSaveListener);
//					mWifiDataEntity.connect(config, mConnectListener);
//				}
//			}
		} else {
			Log.v(TAG, "2save.....");
			mWifiDataEntity.save(config, mSaveListener);
		}
		
		if (mWifiDataEntity.isWifiEnabled()) {
			mScanner.forceScan();
		}
		updateAccessPoints();
	}
	
	void forget() {
		connectingWifi = false;
		cancelTimer();
		if (mSelectedAccessPoint.networkId == INVALID_NETWORK_ID) {
			// Should not happen, but a monkey seems to triger it
			Log.e(TAG, "Failed to forget invalid network " + mSelectedAccessPoint.getConfig());
			return;
		}
         
		mWifiDataEntity.forget(mSelectedAccessPoint.networkId, mForgetListener);

		if (mWifiDataEntity.isWifiEnabled()) {
			mScanner.resume();
		}
		updateAccessPoints();
	}
	
	@SuppressLint("NewApi")
	private boolean hasPassword(WifiConfiguration config) {
		boolean hasPassword = false;
		if (config != null) {
			switch (mSelectedAccessPoint.security) {
			case WifiAccessPoint.SECURITY_WEP:
				if(DEBUG){
					Log.i(TAG, "--heyf--password--" + config.wepKeys[0] + "----");
				}
				if (!TextUtils.isEmpty(config.wepKeys[0])) {
					hasPassword = true;
				}
				break;

			case WifiAccessPoint.SECURITY_PSK:
				if(DEBUG){
					Log.i(TAG, "--heyf--passpord--" + config.preSharedKey + "----");
				}
				if (!TextUtils.isEmpty(config.preSharedKey)) {
					hasPassword = true;
				}

				break;

			case WifiAccessPoint.SECURITY_EAP:
				if(DEBUG){
					Log.i(TAG, "--heyf--passpord--" + config.enterpriseConfig.getPassword() + "----");
				}
				//if (!TextUtils.isEmpty(config.password.value())) { //new api
				if (!TextUtils.isEmpty(config.enterpriseConfig.getPassword())) {
					hasPassword = true;
				}
				break;

			default:
				break;
			}
		}
//		Log.v(TAG, "haspassword=="+hasPassword);
		return hasPassword;

	}
	
	@SuppressLint("NewApi")
	private WifiConfiguration getConfig(WifiAccessPoint accessPoint,String password) {
		WifiConfiguration config = mSelectedAccessPoint.getConfig();
		if (config == null) {
			config = new WifiConfiguration();
		}

		if (accessPoint == null) {
			config.SSID = accessPoint.convertToQuotedString(accessPoint.ssid);
			// If the user adds a network manually, assume that it is hidden.
			config.hiddenSSID = true;
		} else if (accessPoint.networkId == WifiConfiguration.INVALID_NETWORK_ID) {
			config.SSID = WifiAccessPoint.convertToQuotedString(accessPoint.ssid);
		} else {
			config.networkId = mSelectedAccessPoint.networkId;
		}

		switch (accessPoint.security) {
		case WifiAccessPoint.SECURITY_NONE:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			break;

		case WifiAccessPoint.SECURITY_WEP:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
			if (password.length() != 0) {
				int length = password.length();
				// WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
				if ((length == 10 || length == 26 || length == 58)
						&& password.matches("[0-9A-Fa-f]*")) {
					config.wepKeys[0] = password;
				} else {
					config.wepKeys[0] = '"' + password + '"';
				}
			}
			break;

		case WifiAccessPoint.SECURITY_PSK:
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
			if (password.length() != 0) {
				if (password.matches("[0-9A-Fa-f]{64}")) {
					config.preSharedKey = password;
				} else {
					config.preSharedKey = '"' + password + '"';
				}
			}
			break;

		case WifiAccessPoint.SECURITY_EAP:
			config.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
			config.allowedKeyManagement.set(KeyMgmt.IEEE8021X);
			if (password.length() != 0) {
				//config.password.setValue(password);
				config.enterpriseConfig.setPassword(password);
			}
			break;

		default:
			return null;
		}

		return config;
	}
	
	private void onKeyRight(){
		Intent intent = new Intent(WifiConnectActivity.this, WifiModifyActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable(WifiInfoConfigEntity.KEY,getWifiInfoConfig());
		intent.putExtras(bundle);
		intent.putExtra(WifiInfoConfigEntity.WIFI_DISCONNECT_MODIFY_KEY, false);
		startActivityForResult(intent, 0);
	}
	
	private void handleEvent(Context context, Intent intent) {
		String action = intent.getAction();
		Bundle bundle = intent.getExtras();
		/*if (bundle != null) {
			for (String key : bundle.keySet()) {
				Log.d(TAG, "---action: " + action + "---key:" + key + "--value:" + bundle.get(key));
			}
		}*/
		if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
			updateWifiState(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN));
		} else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)
				|| WifiManager.CONFIGURED_NETWORKS_CHANGED_ACTION.equals(action)
				|| WifiManager.LINK_CONFIGURATION_CHANGED_ACTION.equals(action)) {
			Log.e(TAG, "#############receive action " + action);
			updateAccessPoints();
		} else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(action)) {
			SupplicantState state = (SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
			if (!mConnected.get() && SupplicantState.isHandshakeState(state)) {
				updateConnectionState(WifiInfo.getDetailedStateOf(state));
			}
			
			int authState = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
			if (authState == WifiManager.ERROR_AUTHENTICATING) {
				if(DEBUG){
					Log.e(TAG, "auth failed!.........");
				}
				ToastUtils.showToast(WifiConnectActivity.this, 
						getResources().getString(R.string.wifi_password_error), Toast.LENGTH_SHORT);
				connectingWifi = false;
				cancelTimer();
		 
				try {
					if(((WifiConnectAdapter)mWifiList.getAdapter()).getCount()>1){
						int error_network_id = ((WifiAccessPoint)(((WifiConnectAdapter)mWifiList.getAdapter()).getItem(1))).networkId;
						if(mWifiDataEntity!=null && mWifiList!=null && error_network_id!=-1){
							forgetSucessToast = false;
							mWifiDataEntity.forget(error_network_id,mForgetListener);
						}
					}
	 		        
					if(mItemCurHolder!=null)
					   mItemCurHolder.mItemContentEdit.setText("");
					updateConnectionState(WifiInfo.getDetailedStateOf(state));
					updateAccessPoints();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		} else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
			NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			mConnected.set(info.isConnected());
			updateConnectionState(info.getDetailedState());
			updateAccessPoints();
		} else if (WifiManager.RSSI_CHANGED_ACTION.equals(action)) {
			updateConnectionState(null);
		}
	}
	
	private WifiInfoConfigEntity getWifiInfoConfig(){
		WifiInfoConfigEntity config = new WifiInfoConfigEntity();
		WifiConfiguration wifiConfig = null;
		
		if(mSelectedAccessPoint!=null && mSelectedAccessPoint.networkId != WifiConfiguration.INVALID_NETWORK_ID){
			wifiConfig = mSelectedAccessPoint.getConfig();
		}
		config.setWifiPointSecurity((mSelectedAccessPoint == null) ? WifiAccessPoint.SECURITY_NONE
				: mSelectedAccessPoint.security);
	
		if(wifiConfig!=null){
			Log.v("test", "ipAssignment="+wifiConfig.getIpConfiguration().ipAssignment);
			if(wifiConfig.getIpConfiguration().ipAssignment == IpAssignment.STATIC){
				config.setIPv4Assignment(WifiAccessPoint.STATIC);
				StaticIpConfiguration sIpConfig = wifiConfig.getIpConfiguration().getStaticIpConfiguration();
//				Log.v("test", "all="+sIpConfig.toString());
				
				/*Log.v("test", "gateway="+sIpConfig.gateway.getHostAddress());
				Log.v("test", "ip="+sIpConfig.ipAddress.getAddress().getHostAddress());
				Log.v("test", "dns1="+sIpConfig.dnsServers.get(0).getHostAddress());*/
				
				
				LinkAddress linkaddress = sIpConfig.ipAddress;
				int addr = NetworkUtils.prefixLengthToNetmaskInt(linkaddress.getNetworkPrefixLength());
				Log.v("test", "mask="+Utils.getAddress(addr));
				
				
				config.setIPv4IPAddr(sIpConfig.ipAddress.getAddress().getHostAddress());
				config.setIPv4GatewayAddr(sIpConfig.gateway.getHostAddress());
				config.setIPv4NetmaskAddr(Utils.getAddress(addr));
				config.setIPv4DNSAddr(sIpConfig.dnsServers.get(0).getHostAddress());
                         
			}else if(wifiConfig.getIpConfiguration().ipAssignment == IpAssignment.DHCP){
				config.setIPv4Assignment(WifiAccessPoint.DHCP);
				
				DhcpInfo dhcpInfo = mWifiDataEntity.getDhcpInfo(); 
				if(null != dhcpInfo){
					/*Log.v("test", "dhcp ip=="+Utils.getAddress(dhcpInfo.ipAddress));
					Log.v("test", "dhcp gateway=="+Utils.getAddress(dhcpInfo.gateway));
					Log.v("test", "len==="+dhcpInfo.netmask);
					Log.v("test", "dhcp mask=="+Utils.getAddress(dhcpInfo.netmask));
					Log.v("test", "dhcp dns1=="+Utils.getAddress(dhcpInfo.dns1));
					*/
					
					config.setIPv4IPAddr(Utils.getAddress(dhcpInfo.ipAddress));
					config.setIPv4GatewayAddr(Utils.getAddress(dhcpInfo.gateway));
					config.setIPv4NetmaskAddr(Utils.getInterfaceMask("wlan0"));
					config.setIPv4DNSAddr(Utils.getAddress(dhcpInfo.dns1));
				}
			}else{
				config.setIPv4Assignment(WifiAccessPoint.UNASSIGNED);
			}
			
	 
		}
		return config;
	}
	
	
	 
	
	private WifiConfiguration getWifiPointConfig(WifiInfoConfigEntity infoConfig){
		WifiConfiguration config = mSelectedAccessPoint.getConfig();
		if (config == null)
			config = new WifiConfiguration();

		if (mSelectedAccessPoint.networkId == INVALID_NETWORK_ID) {
			config.SSID = WifiAccessPoint.convertToQuotedString(mSelectedAccessPoint.ssid);
		} else {
			config.networkId = mSelectedAccessPoint.networkId;
		}
		
/*		Log.v(TAG, "ssid="+config.SSID+",security>>>"+mSelectedAccessPoint.security);
		
		Log.v(TAG, "networkid="+config.networkId);*/
 
		config.allowedKeyManagement.clear();
        config.allowedAuthAlgorithms.clear();
		switch (mSelectedAccessPoint.security) {
		case WifiAccessPoint.SECURITY_NONE:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			break;

		case WifiAccessPoint.SECURITY_WEP:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
			break;

		case WifiAccessPoint.SECURITY_PSK:
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
			break;

		case WifiAccessPoint.SECURITY_EAP:
			config.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
			config.allowedKeyManagement.set(KeyMgmt.IEEE8021X);
			break;

		default:
			return null;
		}
		
		int ipv4Assignment = infoConfig.getIPv4Assignment();
		if(ipv4Assignment == WifiAccessPoint.STATIC){
			config.setIpAssignment(IpAssignment.STATIC); 
		}else if(ipv4Assignment == WifiAccessPoint.DHCP){
			config.setIpAssignment(IpAssignment.DHCP); 
		}else{
			config.setIpAssignment(IpAssignment.UNASSIGNED);
		}
 

		return config;
	}
	
	private WifiConfiguration getWifiAddConfig(String ssid,String password,int security){
		WifiConfiguration config = new WifiConfiguration();

		config.SSID = WifiAccessPoint.convertToQuotedString(ssid);
		config.hiddenSSID = true;

		switch (security) {
		case WifiAccessPoint.SECURITY_NONE:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			break;
		case WifiAccessPoint.SECURITY_WEP:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
			if (password.length() != 0) {
				int length = password.length();
				// WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
				if ((length == 10 || length == 26 || length == 58)
						&& password.matches("[0-9A-Fa-f]*")) {
					config.wepKeys[0] = password;
				} else {
					config.wepKeys[0] = '"' + password + '"';
				}
			}
			break;

		case WifiAccessPoint.SECURITY_PSK:
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
			if (password.length() != 0) {
				if (password.matches("[0-9A-Fa-f]{64}")) {
					config.preSharedKey = password;
				} else {
					config.preSharedKey = '"' + password + '"';
				}
			}
			break;

		default:
			break;
		}

		return config;
	}
 
	
	private void updateConnectionState(DetailedState state) {
		/* sticky broadcasts can call this when wifi is disabled */
		if(mWifiDataEntity == null){
			mWifiDataEntity = WifiDataEntity.getInstance(WifiConnectActivity.this);
		}
		if (!mWifiDataEntity.isWifiEnabled()) {
			mScanner.pause();
			return;
		}

		if (state == DetailedState.OBTAINING_IPADDR) {
			mScanner.pause();
		} else {
			mScanner.resume();
		}

		mLastInfo = mWifiDataEntity.getConnectionInfo();
		if (state != null) {
			mLastState = state;
		}

		try {
			for (int i = 0; i < mAccessPoints.size(); i++) {
				if (null != mAccessPoints.get(i)) {
					mAccessPoints.get(i).update(mLastInfo, mLastState);
				}
			}
 
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void updateWifiState(int state) {
		switch (state) {
		case WifiManager.WIFI_STATE_ENABLED:
//			Log.d(TAG, "updateWifiState heyf wifi enabled....");
			mScanner.resume();
			return; // not break, to avoid the call to pause() below

		case WifiManager.WIFI_STATE_ENABLING:
//			Log.d(TAG, "updateWifiState heyf wifi enabling....");
			break;

		case WifiManager.WIFI_STATE_DISABLED:
//			Log.d(TAG, "updateWifiState heyf wifi disabled....");
			break;
		}

		mLastInfo = null;
		mLastState = null;
		mScanner.pause();
	}

	private void updateAccessPoints() {
		final int wifiState = mWifiDataEntity.getWifiState();

		if(mEditTextFocusFlag){
			if(DEBUG){
				Log.e(TAG, "mEditTextFocusFlag ...... flag="+mEditTextFocusFlag);
			}
			return;
		}
		
		switch (wifiState) {
		case WifiManager.WIFI_STATE_ENABLED:
			Log.d(TAG, "updateAccessPoints heyf wifi enabled...........");
			wifiHandler.removeMessages(WIFI_ACCESS_POINTS_UPDATE_ACTION);
			wifiHandler.sendEmptyMessage(WIFI_ACCESS_POINTS_UPDATE_ACTION);
		 
			break;

		case WifiManager.WIFI_STATE_ENABLING:
			Log.d(TAG, "updateAccessPoints heyf wifi enabling....");
			if (null != mAccessPoints) {
				mAccessPoints.clear();
			}
			break;

		case WifiManager.WIFI_STATE_DISABLING:
			Log.d(TAG, "updateAccessPoints heyf wifi disableing....");
			break;

		case WifiManager.WIFI_STATE_DISABLED:
			Log.d(TAG, "updateAccessPoints heyf wifi disabled....");
			break;
		}
	}

	/** Returns sorted list of access points */
	private List<WifiAccessPoint> constructAccessPoints() {
		ArrayList<WifiAccessPoint> accessPoints = new ArrayList<WifiAccessPoint>();
		/**
		 * Lookup table to more quickly update AccessPoints by only considering
		 * objects with the correct SSID. Maps SSID -> List of AccessPoints with
		 * the given SSID.
		 */
		Multimap<String, WifiAccessPoint> apMap = new Multimap<String, WifiAccessPoint>();

		final List<WifiConfiguration> configs = mWifiDataEntity.getConfiguredNetworks();
		
		if (configs != null) {
			for (WifiConfiguration config : configs) {
				WifiAccessPoint accessPoint = new WifiAccessPoint(WifiConnectActivity.this, config, this);
				if (null != mAccessPoints) {
					for (int i = 0; i < mAccessPoints.size(); i++) {
						if (accessPoint.ssid.equals(mAccessPoints.get(i).ssid)) {
							accessPoint.isERRShow = mAccessPoints.get(i).isERRShow;
							break;
						}
					}
				}
				accessPoint.update(mLastInfo, mLastState);
				accessPoints.add(accessPoint);
				apMap.put(accessPoint.ssid, accessPoint);
			}
		}

		final List<ScanResult> results = mWifiDataEntity.getScanResults();
		if (results != null) {
			for (ScanResult result : results) {
				// Ignore hidden and ad-hoc networks.
				if (result.SSID == null || result.SSID.length() == 0 || result.capabilities.contains("[IBSS]")) {
					continue;
				}
				boolean found = false;
				for (WifiAccessPoint accessPoint : apMap.getAll(result.SSID)) {
					if (accessPoint.update(result))
						found = true;
				}
				if (!found) {
					WifiAccessPoint accessPoint = new WifiAccessPoint(WifiConnectActivity.this, result, this);
					accessPoints.add(accessPoint);
					apMap.put(accessPoint.ssid, accessPoint);
				}
			}
		}
		
		// Pre-sort accessPoints to speed preference insertion
		Collections.sort(accessPoints);

		return accessPoints;
	}

	private class Multimap<K, V> {
		private HashMap<K, List<V>> store = new HashMap<K, List<V>>();

		/** retrieve a non-null list of values with key K */
		List<V> getAll(K key) {
			List<V> values = store.get(key);
			return values != null ? values : Collections.<V> emptyList();
		}

		void put(K key, V val) {
			List<V> curVals = store.get(key);
			if (curVals == null) {
				curVals = new ArrayList<V>(3);
				store.put(key, curVals);
			}
			curVals.add(val);
		}
	}

	private class Scanner extends Handler {
		private int mRetry = 0;

		void resume() {
			if (!hasMessages(0)) {
				sendEmptyMessage(0);
			}
		}

		void forceScan() {
			removeMessages(0);
			sendEmptyMessage(0);
		}

		void pause() {
			mRetry = 0;
			removeMessages(0);
		}

		@Override
		public void handleMessage(Message message) {
			if (mWifiDataEntity.startScanActive()) {
				mRetry = 0;
			} else if (++mRetry >= 3) {
				mRetry = 0;
				ToastUtils.showToast(WifiConnectActivity.this, getResources().getString(R.string.wifi_fail_to_scan), Toast.LENGTH_LONG);
				return;
			}
			sendEmptyMessageDelayed(0, WIFI_RESCAN_INTERVAL_MS);
		}
	}

	@Override
	public void refersh() {
		// TODO Auto-generated method stub
        if(mEditTextFocusFlag){
			return;
		}
        
//        refreshConfigurationWifi();
		final WifiConnectAdapter mAdapter = (WifiConnectAdapter) mWifiList.getAdapter();
		if (null != mAdapter) {
			Collections.sort(mAccessPoints);
			mAdapter.notifyData(mAccessPoints);
			stopProgressDialog();
		} else {
			if (null != mAccessPoints && mAccessPoints.size() > 0) {
				mWifiList.setAdapter(new WifiConnectAdapter(WifiConnectActivity.this, mAccessPoints));
				stopProgressDialog();
			}
		}
	}
	
	
	private void refreshConfigurationWifi(){
		//******add by carter start
		try {
//			Log.v("carter", "refresh configuration wifi");
			final List<WifiConfiguration> configs = mWifiDataEntity.getConfiguredNetworks();
			if (configs != null) {
				for (WifiConfiguration config : configs) {
					boolean canNotscanFound = false;
					for (ScanResult result : mWifiDataEntity.getScanResults()) {
						// Ignore hidden and ad-hoc networks.
						if (result.SSID == null || result.SSID.length() == 0 || result.capabilities.contains("[IBSS]")) {
							continue;
						}

						if(WifiAccessPoint.convertToQuotedString(result.SSID).equals(config.SSID)){
							canNotscanFound = true;
							break;
						}
					}
					if(!canNotscanFound && mAccessPoints!=null){
//						Log.v("carter", "no scan found>>>>>"+config.SSID);
						for (int i = 0; i < mAccessPoints.size(); i++) {
							if (null != mAccessPoints.get(i) && null != mAccessPoints.get(i).ssid 
									&& WifiAccessPoint.convertToQuotedString(mAccessPoints.get(i).ssid).equals(config.SSID)) {
								Log.v("carter", "no scan found set none old state>>>>>"+mAccessPoints.get(i).getWifiConnState());
								mAccessPoints.get(i).setWifiConnState(WifiAccessPoint.NONE);
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	 
		//**********end
	}

	class WifiConnectAdapter extends BaseAdapter {
		LayoutInflater mInflater = null;
		private Context mContext = null;
		List<WifiAccessPoint> mList = null;
		int index = 0;

		public WifiConnectAdapter(Context context, List<WifiAccessPoint> wifiList) {
			mContext = context;
			if (wifiList == null) {
				mList = new ArrayList<WifiAccessPoint>();
			} else {
				mList = wifiList;
				WifiConfiguration config = new WifiConfiguration();
				config.SSID = context.getResources().getString(R.string.wifi_manual_add);
				WifiAccessPoint accessPoint = new WifiAccessPoint(context, config, WifiConnectActivity.this);
				mList.add(0, accessPoint);
			}
			mInflater = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public void setIndex(int selected) {
			index = selected;
		}

		public void notifyData(List<WifiAccessPoint> wifiList) {
			mList.clear();
			mList.addAll(wifiList);

			WifiConfiguration config = new WifiConfiguration();
			config.SSID = mContext.getResources().getString(R.string.wifi_manual_add);
			WifiAccessPoint accessPoint = new WifiAccessPoint(mContext, config, WifiConnectActivity.this);
			mList.add(0, accessPoint);

			notifyDataSetChanged();
		}

		public void notifyState() {
			notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			WifiItemHolder holder = null;
			if (convertView == null) {
				holder = new WifiItemHolder();
				convertView = mInflater.inflate(R.layout.wifi_item_list, null);
				holder.mItemTitle = (TextView) convertView.findViewById(R.id.wifi_item_title);
				holder.mItemSecurityImg = (ImageView) convertView.findViewById(R.id.wifi_item_security_img);
				holder.mItemContentEdit = (EditText) convertView.findViewById(R.id.wifi_item_password_edit);
				holder.mItemSignalImg = (ImageView) convertView.findViewById(R.id.wifi_item_signal_img);
				holder.mItemMoreImg = (ImageView) convertView.findViewById(R.id.wifi_item_more_img);
				holder.mItemSignalStateImg = (ImageView)convertView.findViewById(R.id.wifi_item_signal_state_img);
				convertView.setTag(holder);
			} else {
				holder = (WifiItemHolder) convertView.getTag();
			}
			
			String pointState = "";
			final WifiAccessPoint accessPoint = mList.get(position);
			
			int wifiLevelInt = accessPoint.getLevel();
			switch (accessPoint.wifiConnState) {
			case WifiAccessPoint.STATE_CONNECTING:
				pointState = accessPoint.pointState;
				mSelectedAccessPoint = accessPoint;    //澧炲姞鍔ㄦ�佽皟鏁达紝鍥犱负鏂紑缃戠粶鐨勬椂鍊欙紝鏃犳硶纭畾閭ｄ釜鏄凡缁忚閫夋嫨鐨凷SID
				break;
			case WifiAccessPoint.STATE_CONNECTED:
				connectingWifi = false;
                cancelTimer();
				holder.mItemMoreImg.setVisibility(View.VISIBLE);
				pointState = accessPoint.pointState;
				if (mErrNetId == accessPoint.networkId) {
					mErrNetId = -1;
				}
				break;
			case WifiAccessPoint.STATE_ERROR:
//				Log.d(TAG, "---heyf-ssid:" + accessPoint.ssid + "--iserrshow:" + accessPoint.isERRShow
//						+ "------pointState:" + accessPoint.pointState);
				if (accessPoint.isERRShow) {
					pointState = accessPoint.pointState;
				} else {
					pointState = "";
				}
                connectingWifi = false;
                cancelTimer();
				mErrNetId = accessPoint.networkId;
				break;
			default:
				pointState = "";
				break;
			}
			
			//初始左边安全状态
			if(accessPoint.security == WifiAccessPoint.SECURITY_NONE){
				holder.mItemSecurityImg.setImageDrawable(null);
			}else{
				holder.mItemSecurityImg.setImageResource(R.drawable.wifi_state_locked);
			}
			
			holder.mItemSignalImg.setTag(position);
			if (position == 0) {
				holder.mItemSignalImg.setImageBitmap(null);
				holder.mItemMoreImg.setVisibility(View.INVISIBLE);
				holder.mItemSecurityImg.setImageResource(R.drawable.wifi_state_add);
				holder.mItemSignalImg.setVisibility(View.GONE);
			}else if(position ==1){
				holder.mItemSignalImg.setVisibility(View.VISIBLE);
				holder.mItemSignalImg.setBackgroundResource(R.drawable.wifi_connect_progress);

				AnimationDrawable animationDrawable = (AnimationDrawable)holder.mItemSignalImg.getBackground();  	          
				if (mSelectedAccessPoint!=null && accessPoint.ssid.equals(mSelectedAccessPoint.ssid)
					&& accessPoint.wifiConnState == WifiAccessPoint.STATE_CONNECTING) {
					if(!animationDrawable.isRunning()){
		                animationDrawable.start();  
		            }
				} else {
					if(animationDrawable.isRunning()){  
		                animationDrawable.stop();  
		                holder.mItemSignalImg.setBackgroundResource(android.R.color.transparent);
		            }
				}				
				
				if(accessPoint.wifiConnState == WifiAccessPoint.STATE_CONNECTED){
					//右边的箭头状态
					holder.mItemMoreImg.setVisibility(View.VISIBLE);
					//右边的ping外网状态
					holder.mItemSignalStateImg.setVisibility(curPingStatus==0?View.INVISIBLE:View.VISIBLE);
					//wifi信号
					holder.mItemSignalImg.setImageLevel(accessPoint.getLevel());
					holder.mItemSignalImg.setImageResource(R.drawable.wifi_signal_selected);
					
					//左边wifi安全为勾选，已经连接上状态
					holder.mItemSecurityImg.setImageResource(R.drawable.wifi_state_connected);
				}else {
					//右边的箭头状态
					holder.mItemMoreImg.setVisibility(View.INVISIBLE);
					//右边的ping外网状态
					holder.mItemSignalStateImg.setVisibility(View.INVISIBLE);
					
					//wifi信号
					if(!animationDrawable.isRunning()){
						holder.mItemSignalImg.setImageLevel(accessPoint.getLevel());
						holder.mItemSignalImg.setImageResource(R.drawable.wifi_signal_unselected);
					}else {
						holder.mItemSignalImg.setImageDrawable(null);
					}
				}

			}else {
				holder.mItemSignalImg.setVisibility(View.VISIBLE);
				holder.mItemSignalStateImg.setVisibility(View.INVISIBLE);
				holder.mItemMoreImg.setVisibility(View.INVISIBLE);
				holder.mItemSignalImg.setBackgroundResource(R.drawable.wifi_connect_progress);
				AnimationDrawable animationDrawable = (AnimationDrawable)holder.mItemSignalImg.getBackground();  
	            if(animationDrawable.isRunning()){  
	                animationDrawable.stop();  
	                holder.mItemSignalImg.setBackgroundResource(android.R.color.transparent);
	            }	
				
	            //右边的wifi信号图标
				if(wifiLevelInt!=-1){
					holder.mItemSignalImg.setImageLevel(wifiLevelInt);
					holder.mItemSignalImg.setImageResource(R.drawable.wifi_signal_unselected);
				}else {
					holder.mItemSignalImg.setImageDrawable(null);
				}
				
			}
			
			holder.mItemTitle.setText(getSSIDAndState(accessPoint.ssid, 
					pointState,accessPoint.wifiConnState));
			return convertView;
		}

	}

	String getSSIDAndState(String ssid, String pointState,int wifiConnState) {
		String text = null;
		if(wifiConnState == WifiAccessPoint.STATE_CONNECTED){
			text = ssid + getResources().getString(R.string.wifi_connected_success);
		}else{
			text  =  ssid + " " + pointState;
		}
		return text;
	}
 

	private void startProgressDialog(String msg) {
		if (mProgressDialog == null) {
			mProgressDialog = new CustomProgressDialog(WifiConnectActivity.this);
		}
		mProgressDialog.setMessage(msg);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
		mProgressDialog.startLoading();
	}

	private void stopProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.stopLoading();
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
	
	
	private void regeditReceiver() {
		IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mFilter.addAction(ConnectivityStateService.HW_INTENT);
        mFilter.addAction("com.hiveview.cloudtv.settings.wifi.modify.save");
        mFilter.addAction("android.net.pppoe.PPPOE_STATE_CHANGED");
        registerReceiver(mReceiver, mFilter);
	}
	
	private boolean isWaiting = true;
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v(TAG, action+"==state=="+Utils.checkEthState()+",active="+Utils.checkNetworkIsActive(WifiConnectActivity.this));
            if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            	Log.v(TAG, "network change>>>>>"+Utils.checkNetworkIsActive(WifiConnectActivity.this));	
            }else if(action.equals("com.hiveview.cloudtv.settings.wifi.modify.save")){
            	Log.v(TAG, "get wifi modify message");
            	boolean saveFlag = intent.getBooleanExtra(WifiInfoConfigEntity.SAVE_CONFIG, false);
         
            	if(saveFlag){
          		  WifiInfoConfigEntity dataConfig = intent.getParcelableExtra(WifiInfoConfigEntity.KEY);
          		  IpConfiguration ipConfig = intent.getParcelableExtra(WifiInfoConfigEntity.KEY2);

          		  
          		  
          		/*StaticIpConfiguration sIpConfig = ipConfig.getStaticIpConfiguration();
				Log.v("test", ">gateway="+sIpConfig.gateway.getHostAddress());
				Log.v("test", ">ip="+sIpConfig.ipAddress.getAddress().getHostAddress());
				Log.v("test", ">dns1="+sIpConfig.dnsServers.get(0).getHostAddress());
				*/
          		  
          		  
//          		  WifiConfiguration wifiConfig = getWifiPointConfig(dataConfig);
          		WifiConfiguration wifiConfig = getWifiConfiguration(mWifiDataEntity.getWifiManager(), mSelectedAccessPoint.networkId);
          		wifiConfig.setIpConfiguration(ipConfig);
          		save(wifiConfig);
          	   }
            }
            
            if(intent.getAction().equals(ConnectivityStateService.HW_INTENT)){
				int event = intent.getIntExtra(ConnectivityStateService.HW_INTENT_EXTRA,ConnectivityStateService.HW_STATUS_UNKNOW);
				if(event == ConnectivityStateService.HW_STATUS_CONNECT){
					finish();
				}
			}
        }
    };
    
    private void unRegeditReceiver(){
    	unregisterReceiver(mReceiver);
    }
    
 
    
    
    public  WifiConfiguration getWifiConfiguration(WifiManager wifiManager, int networkId) {
        Log.v(TAG, "networkid="+networkId);
    	List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration configuredNetwork : configuredNetworks) {
                if (configuredNetwork.networkId == networkId) {
                    return configuredNetwork;
                }
            }
        }
        return null;
    }

     private void startTimer() {
		if (timeoutTimer == null) {
			timeoutTimer = new Timer();
			timeoutTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					mHandler.sendEmptyMessage(WIFI_CONNECT_TIMEOUT);
				}
			}, 1000*10);
		}
	}
    
    
    private void cancelTimer() {
		if (timeoutTimer != null) {
			timeoutTimer.cancel();
			timeoutTimer = null;
		}
	}
 
    
    private void initWifiHandle(){
    	wifiHandlerThread.start();
    	wifiHandler = new Handler(wifiHandlerThread.getLooper()){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
			    switch (msg.what) {
				case WIFI_ACCESS_POINTS_UPDATE_ACTION:
					Log.d(TAG, "updateAccessPoints heyf wifi update...........");
					final List<WifiAccessPoint> accessPoints = constructAccessPoints();
					mAccessPoints = accessPoints;
					mHandler.removeMessages(WIFI_UPDATE_LIST);
				    mHandler.sendEmptyMessage(WIFI_UPDATE_LIST);
					break;
				}	
			}
    	};
    }
  
}   
