package com.hiveview.cloudtv.settings;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.droidlogic.pppoe.PppoeManager;
import com.droidlogic.pppoe.PppoeStateTracker;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.SystemProperties;

import com.hiveview.cloudtv.settings.connectivity.ConnectivityManagerData;
import com.hiveview.cloudtv.settings.connectivity.ConnectivityStateService;
import com.hiveview.cloudtv.settings.ethernet.EthernetDataEntity;
import com.hiveview.cloudtv.settings.ethernet.EthernetItemEntity;
import com.hiveview.cloudtv.settings.ethernet.EthernetItemHolder;
import com.hiveview.cloudtv.settings.pppoe.PppoeDataEntity;
import com.hiveview.cloudtv.settings.util.ToastUtils;
import com.hiveview.cloudtv.settings.util.Utils;
import com.hiveview.cloudtv.settings.widget.CommonListView;
import com.hiveview.cloudtv.settings.widget.CustomProgressDialog;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView;
import com.hiveview.cloudtv.settings.widget.ResolutionDialog;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView.FocusViewAnimatorEndListener;
import com.hiveview.cloudtv.settings.wifi.WifiAccessPoint;
import com.hiveview.cloudtv.settings.wifi.WifiDataEntity;
import com.hiveview.cloudtv.settings.wifi.WifiInfoConfigEntity;
 

public class EthernetConnectedAcivity extends Activity {
	private static final String TAG = "EthernetActivity";
	private static final boolean DEBUG = true;
 
	private static final int ETHERNET_ITEM_WIFI_SHARE = 0;
	private static final int ETHERNET_ITEM_IPV4 = 18;//2;
	private static final int ETHERNET_ITEM_IPV4_MANUAL_ADJUST = 1;//3;
	private static final int ETHERNET_ITEM_IPV4_IP_ADDRESS = 2;//4;
	private static final int ETHERNET_ITEM_IPV4_NET_MASK = 3;//5;
	private static final int ETHERNET_ITEM_IPV4_GATEWAY = 4;//6;
	private static final int ETHERNET_ITEM_IPV4_DNS = 5;//7;
	private static final int ETHERNET_ITEM_IPV6 = 19;//8;
	private static final int ETHERNET_ITEM_IPV6_MANUAL_ADJUST = 9;
	private static final int ETHERNET_ITEM_IPV6_IP_ADDRESS = 10;
	private static final int ETHERNET_ITEM_IPV6_NET_MASK = 11;
	private static final int ETHERNET_ITEM_IPV6_GATEWAY = 12;
	private static final int ETHERNET_ITEM_IPV6_DNS = 13;
	private static final int ETHERNET_ITEM_DISABLE_NET = 6;//14;
//	private static final int ETHERNET_ITEM_PPPOE_AUTO_CONNECT = 8;//15;

	
	public Context mContext = this;
	
	public ConnectivityManagerData mConManData = null;
	private CommonListView mItemList = null;
	private EthernetAdapter mItemlistAdapter = null;
	private int mItemListCurPosition = -1;
	private EthernetItemEntity mItemCurEntity = null;
	private EthernetItemHolder mItemCurHolder = null;
	// list focus view
	private View mItemListCurView = null;
	private LauncherFocusView mLauncherFocusView = null;
	private boolean mIsFirstIn = true;
	// ethernet data
	private boolean isAutoFlag = false;
	private String mV4IPAddress = null;
	private String mV4GatewayAddress = null;
	private String mV4DNSAddress = null;
	private String mV4NetmaskAddress = null;
	private WifiDataEntity mWifiDataEntity = null;
	private PppoeDataEntity mPppoeDataEntity = null;
	
	private Toast mToast = null;
	private LinearLayout mMainLayout = null;
	private boolean mKeyboardOKFlag = false;
	private boolean mTextEditFlag = false;
	
	private int mCurKeycode = KeyEvent.KEYCODE_0;
	private boolean mTextColorChangeFlag = false;
	private boolean mFocusAnimationEndFlag = false;
	private long mKeyDownTime = 0L;
	
	private boolean isPPPOE = false;

	private int activeEthType;
	
	private CustomProgressDialog mProgressDialog = null;
    private ResolutionDialog tipsDialog;
	
	private static final String CLASS_NAME = "SoftAPConfigFragment";
	private SharedPreferences mSharePreferences = null;

	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		setContentView(R.layout.ethernet_connected_main);
		
		Log.v(TAG, "type=="+Utils.getActiveNetworkType(EthernetConnectedAcivity.this));
		isPPPOE = Utils.getActiveNetworkType(EthernetConnectedAcivity.this) == SettingsApplication.TYPE_PPPOE?true:false;
		Log.v(TAG, "isPPPOE=="+isPPPOE);
		
		mWifiDataEntity = WifiDataEntity.getInstance(this);
		mPppoeDataEntity = PppoeDataEntity.getInstance(this);
		mConManData= new ConnectivityManagerData(mContext);
		mSharePreferences = getSharedPreferences(CLASS_NAME, Context.MODE_PRIVATE);
		activeEthType = Utils.getActiveNetworkType(EthernetConnectedAcivity.this);
		initEthernetData(true);
		Log.v(TAG, "activeEthType=="+activeEthType);
 
        

		initView();
 
		regeditReceiver();
		
		if (mProgressDialog == null) {
			mProgressDialog = new CustomProgressDialog(EthernetConnectedAcivity.this);
		}
		mHandler.sendEmptyMessageDelayed(EthernetDataEntity.MSG_DHCP_TIMEOUT, 500);
		
	}

	private void initView() {
		mItemList = (CommonListView) findViewById(R.id.ethernet_list);
		mItemList.setOnItemClickListener(mItemListOnItemClickListener);
		mItemList.setOnKeyListener(mItemListOnKeyListener);
		mItemList.setOnItemSelectedListener(mItemListOnItemSelectedListener);

		
		// list focus view
		mIsFirstIn = true;
		mLauncherFocusView = (LauncherFocusView) findViewById(R.id.ethernet_connect_focus_view);
		mLauncherFocusView.setAnimatorEndListener(new FocusViewAnimatorEndListener() {
			@Override
			public void OnAnimateStart(View currentFocusView) {
				if(DEBUG){
					Log.e(TAG, "onAnimationStart...");
				}
				mFocusAnimationEndFlag = false;
			}
			
			@Override
			public void OnAnimateEnd(View currentFocusView) {
				// TODO Auto-generated method stub
				//mLauncherFocusView.initFocusView(mItemListCurView, false, 0f);
				if(DEBUG){
					Log.e(TAG, "OnAnimateEnd...");
				}
				mFocusAnimationEndFlag = true;
				listTextColorSet();
			}
			
		});
		
		mMainLayout = (LinearLayout)findViewById(R.id.ethernet_connect_main_llyout);
		mMainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
/*				if(DEBUG){
					Log.e(TAG, "I'm here to listen keyboard event ");
				}*/
	 
				if(!mKeyboardOKFlag && mTextEditFlag){
					if(DEBUG){
						Log.e(TAG, "I'm here to listen keyboard event and recovery data");
					}
					recoveryItemEditText();
				}
			}
		});
		initItemList();
	}

	private void initEthernetData(boolean isRepeat) {
		Log.v(TAG, "isRepeat="+isRepeat+",isPPPOE="+isPPPOE);
		if (isRepeat) {
			isAutoFlag = mConManData.isDhcpRuning("eth0");
			Log.e(TAG, "initEthernetData " + isAutoFlag);
		}
		
		if(isPPPOE){
			isAutoFlag = true;
			mPppoeDataEntity.getPppoeDevInfo();
			mV4IPAddress = mPppoeDataEntity.getPppoeIpAddress().trim();
			mV4NetmaskAddress = mPppoeDataEntity.getPppoeNetmask().trim();
			mV4GatewayAddress = mPppoeDataEntity.getPppoeRouteAddress().trim();
			mV4DNSAddress = mPppoeDataEntity.getPppoeDnsAddress().trim();
		}else{
			mV4IPAddress = mConManData.getEthernetIpAddress().trim();
			mV4NetmaskAddress = mConManData.getEthernetNetmask().trim();
			mV4GatewayAddress = mConManData.getEthernetGateway().trim();
			mV4DNSAddress = mConManData.getEthernetDns(0).trim();
		}
		mKeyboardOKFlag = false;
		
		
		Log.v(TAG, "ip="+mV4IPAddress);
		Log.v(TAG, "mV4NetmaskAddress="+mV4NetmaskAddress);
		Log.v(TAG, "mV4GatewayAddress="+mV4GatewayAddress);
		Log.v(TAG, "mV4DNSAddress="+mV4DNSAddress);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unRegeditReceiver();
		stopProgressDialog();
        mHandler.removeMessages(EthernetDataEntity.MSG_DHCP_TIMEOUT);
        stopProgressDialog();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mItemlistAdapter!=null){
			mItemlistAdapter.notifyDataSetChanged();
		}
		
		Log.v(TAG, "onResume");
	}

	AdapterView.OnItemClickListener mItemListOnItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
			// TODO Auto-generated method stub
			if (DEBUG)
				Log.e(TAG, "item click position=" + position);
		    if (ETHERNET_ITEM_WIFI_SHARE == position) {
				Utils.setApplicationBGBitmap(EthernetConnectedAcivity.this);
				Intent intent = new Intent(EthernetConnectedAcivity.this, WifiShareActivity.class);
				startActivity(intent);
			} else if (ETHERNET_ITEM_DISABLE_NET == position) {
				//mPppoeDataEntity.disconnect();
				Utils.setApplicationBGBitmap(EthernetConnectedAcivity.this);
				Intent intent = new Intent(EthernetConnectedAcivity.this, PppoeConnectActivity.class);
				intent.putExtra(PppoeDataEntity.PPPOE_EXTRA_STATE, PppoeDataEntity.PPPOE_EXTRA_STATE_DISCONNECT);
				startActivityForResult(intent, 0);
			}  
			// edit focus get
			if (mItemCurEntity != null
					&& EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_EDIT == mItemCurEntity.getItemCategory()) {
				if (DEBUG)
					Log.e(TAG, "item edit click...............");
				if (!isAutoFlag) {
					/*mItemCurHolder.mItemContentEdit.setCursorVisible(true);
					mItemCurHolder.mItemContentEdit.setVisibility(View.VISIBLE);*/
					mItemList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
					// mItemList.setFocusable(false);
					mItemCurHolder.mItemContentEdit.setFocusable(true);
					mItemCurHolder.mItemContentEdit.requestFocus();
					mItemCurHolder.mItemContentEdit.setCursorVisible(true);
					mItemCurHolder.mItemContentEdit.setSelection(mItemCurHolder.mItemContentEdit.getText().toString().trim().length());
					mItemCurHolder.mItemContentEdit.addTextChangedListener(mTextWatcher);
					mItemCurHolder.mItemContentEdit.setOnEditorActionListener(mEditorActionListener);
					mItemCurHolder.mItemContentEdit.setOnKeyListener(mEditTextOnKeyListener);
					InputMethodManager mng = (InputMethodManager) EthernetConnectedAcivity.this
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					mng.showSoftInput(mItemCurHolder.mItemContentEdit, InputMethodManager.SHOW_IMPLICIT);
				}else{
					Utils.startListFocusAnimation(EthernetConnectedAcivity.this, mLauncherFocusView, R.anim.list_focus_anim);
				}
			}
		}

	};

	
	@SuppressLint("NewApi")
	View.OnKeyListener mItemListOnKeyListener = new View.OnKeyListener() {

		@Override
		public boolean onKey(View view, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
  
				if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN || keyCode==KeyEvent.KEYCODE_DPAD_UP){
					
					if (mProgressDialog != null && mProgressDialog.isShowing()) {
						return true;
					}
					
					mCurKeycode = keyCode;
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
				}
				System.out.println("item posion=" + mItemListCurPosition);
				if (mItemCurEntity == null || mItemCurHolder == null) {
					if (DEBUG)
						Log.e(TAG, "item cur entity null");
					return false;
				}
				if (mItemCurEntity.getItemCategory() != EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_SELECT) {
					return false;
				}
				
				switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_LEFT:
					if (ETHERNET_ITEM_IPV4 == mItemListCurPosition) {
						if (EthernetItemEntity.SELECT_OFF == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[1]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_ON);
						} else if (EthernetItemEntity.SELECT_ON == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[0]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_OFF);
						}
					} else if (ETHERNET_ITEM_IPV4_MANUAL_ADJUST == mItemListCurPosition) {
						//因为有时候进入这个页面的时候，网络状态还没有改变过来，所以需要每次都判断，确保功能生效
						activeEthType = Utils.getActiveNetworkType(EthernetConnectedAcivity.this);
						if(activeEthType == SettingsApplication.TYPE_PPPOE){
							return false;
						}
						
						if((null!=mProgressDialog) && !mProgressDialog.isShowing()){
							startProgressDialog("");
							mHandler.removeMessages(EthernetDataEntity.MSG_DHCP_TIMEOUT);
							mHandler.sendEmptyMessageDelayed(EthernetDataEntity.MSG_DHCP_TIMEOUT, 2*1000);
						}else {
							return false;
						}
						
						
						if (EthernetItemEntity.SELECT_OFF == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[1]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_ON);
							isAutoFlag = false;
							mHandler.sendEmptyMessage(EthernetDataEntity.MSG_IPV4_ADJUST_STATIC);
						} else if (EthernetItemEntity.SELECT_ON == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[0]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_OFF);
							isAutoFlag = true;
							mHandler.sendEmptyMessage(EthernetDataEntity.MSG_IPV4_ADJUST_DHCP);
						}
					} else if (ETHERNET_ITEM_IPV6 == mItemListCurPosition) {
						if (EthernetItemEntity.SELECT_OFF == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[1]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_ON);
						} else if (EthernetItemEntity.SELECT_ON == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[0]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_OFF);
						}
					} else if (ETHERNET_ITEM_IPV6_MANUAL_ADJUST == mItemListCurPosition) {
						if (EthernetItemEntity.SELECT_OFF == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[1]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_ON);
						} else if (EthernetItemEntity.SELECT_ON == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[0]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_OFF);
						}
					}
					mItemCurHolder.mItemLeftArrow.setImageResource(R.drawable.left_arrow_selected);
					mItemCurHolder.mItemRightArrow.setImageResource(R.drawable.right_arrow_unselected);
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					if (ETHERNET_ITEM_IPV4 == mItemListCurPosition) {
						if (EthernetItemEntity.SELECT_OFF == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[1]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_ON);
						} else if (EthernetItemEntity.SELECT_ON == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[0]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_OFF);
						}
					} else if (ETHERNET_ITEM_IPV4_MANUAL_ADJUST == mItemListCurPosition) {
						activeEthType = Utils.getActiveNetworkType(EthernetConnectedAcivity.this);
						if(activeEthType == SettingsApplication.TYPE_PPPOE){
							return false;
						}
						
						if((null!=mProgressDialog) && !mProgressDialog.isShowing()){
							startProgressDialog("");
							mHandler.removeMessages(EthernetDataEntity.MSG_DHCP_TIMEOUT);
							mHandler.sendEmptyMessageDelayed(EthernetDataEntity.MSG_DHCP_TIMEOUT, 2*1000);
						}else {
							return false;
						}
						
						if (EthernetItemEntity.SELECT_OFF == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[1]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_ON);
							isAutoFlag = false;
							mHandler.sendEmptyMessage(EthernetDataEntity.MSG_IPV4_ADJUST_STATIC);
						} else if (EthernetItemEntity.SELECT_ON == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[0]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_OFF);
							isAutoFlag = true;
							mHandler.sendEmptyMessage(EthernetDataEntity.MSG_IPV4_ADJUST_DHCP);
						}
					} else if (ETHERNET_ITEM_IPV6 == mItemListCurPosition) {
						if (EthernetItemEntity.SELECT_OFF == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[1]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_ON);
						} else if (EthernetItemEntity.SELECT_ON == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[0]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_OFF);
						}
					} else if (ETHERNET_ITEM_IPV6_MANUAL_ADJUST == mItemListCurPosition) {
						if (EthernetItemEntity.SELECT_OFF == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[1]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_ON);
						} else if (EthernetItemEntity.SELECT_ON == mItemCurEntity.getItemSelectFlag()) {
							mItemCurHolder.mItemContentTv.setText(mItemCurEntity.getItemContents()[0]);
							mItemCurEntity.setItemSelectFlag(EthernetItemEntity.SELECT_OFF);
						}
					}
					mItemCurHolder.mItemLeftArrow.setImageResource(R.drawable.left_arrow_unselected);
					mItemCurHolder.mItemRightArrow.setImageResource(R.drawable.right_arrow_selected);
					break;
				case KeyEvent.KEYCODE_BACK:
					if(DEBUG){
						Log.e(TAG, "keycode return ..................");
					}
					break;
				default:
					break;
				}
			}else if(event.getAction() == KeyEvent.ACTION_UP){

				if(DEBUG){
					Log.e(TAG, "keycode action up");
				}
				if(!mTextColorChangeFlag){
					mTextColorChangeFlag = true;
					listTextColorSet();
				}
			}
			return false;
		}
	};

	AdapterView.OnItemSelectedListener mItemListOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View view, int position, long arg3) {
			// TODO Auto-generated method stub
			if (DEBUG)
				Log.e(TAG,
						"item list select position=" + position + "visible pos=" + mItemList.getLastVisiblePosition());
			mItemListCurPosition = position;
			// list focus view
			mItemListCurView = view;
			if (mIsFirstIn) {
				mLauncherFocusView.initFocusView(mItemListCurView, false, 0f);
			}
			// arrow clear
			if (mItemCurHolder != null && mItemCurEntity != null
					&& EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_SELECT == mItemCurEntity.getItemCategory()) {
				mItemCurHolder.mItemLeftArrow.setImageResource(R.drawable.left_arrow_unselected);
				mItemCurHolder.mItemRightArrow.setImageResource(R.drawable.right_arrow_unselected);
			}
			// text color clear
			if (mItemCurHolder != null) {
				mItemCurHolder.mItemTitle.setTextColor(getResources().getColor(R.color.settings_9a9a9a));
				if (mItemCurEntity != null) {
					if (EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_SELECT == mItemCurEntity.getItemCategory()) {
						mItemCurHolder.mItemContentTv.setTextColor(getResources().getColor(R.color.settings_9a9a9a));
					} else if (EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_EDIT == mItemCurEntity
							.getItemCategory()) {
						mItemCurHolder.mItemContentEdit.setTextColor(getResources().getColor(R.color.settings_9a9a9a));
					} else if (EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_ENTER == mItemCurEntity
							.getItemCategory()) {
						mItemCurHolder.mItemRightArrow.setImageResource(R.drawable.right_enter_point_unselected);
					}
				}
			}
			// focus recovery
			if (mItemCurEntity != null
					&& EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_EDIT == mItemCurEntity.getItemCategory()) {
				if (mItemCurHolder.mItemContentEdit.isFocused()) {
					if (DEBUG){
						Log.e(TAG, "item edit is focusable");
					}
					mItemCurHolder.mItemContentEdit.setFocusable(false);
					mItemCurHolder.mItemContentEdit.requestFocus();
					mItemList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
					//mItemCurHolder.mItemContentEdit.addTextChangedListener(null);
					//mItemCurHolder.mItemContentEdit.setOnEditorActionListener(null);
					// mItemList.setFocusable(true);
				}
			}

			mItemCurEntity = (EthernetItemEntity) ((EthernetAdapter) mItemList.getAdapter()).getItem(position);
			mItemCurHolder = (EthernetItemHolder) view.getTag();
			
			// text color set in mLauncherFocusView onAnimationEnd()
			if(mIsFirstIn){
				mIsFirstIn = false;
				mTextColorChangeFlag = true;
				listTextColorSet();
			}
			
			if(mCurKeycode == KeyEvent.KEYCODE_DPAD_DOWN){
				/*
				if(mItemListCurPosition == mItemList.getCount()-2){
					mLauncherFocusView.initFocusView(mItemListCurView, false, 0f);
				}
				*/
				Log.e(TAG, "list getheight:"+mItemList.getHeight()+
						" view getTop:"+view.getTop()+"height:"+
						view.getHeight()+" visible first:"+mItemList.getFirstVisiblePosition());
				
				if(mItemListCurPosition<5 || mItemListCurPosition>mItemList.getCount()-2
						||(mItemList.getFirstVisiblePosition()==0&&view.getTop()<(view.getHeight()*4))
						||(mItemList.getFirstVisiblePosition()!=0&&view.getTop()<view.getHeight()*5)){
					mLauncherFocusView.moveTo(mItemListCurView);
				}else{
					listTextColorSet();
					mItemList.setSelectionFromTop(mItemListCurPosition, view.getTop()-view.getHeight());
				}
			}else if(mCurKeycode == KeyEvent.KEYCODE_DPAD_UP){
				//mLauncherFocusView.moveTo(mItemListCurView);
				/*
				if(mItemListCurPosition == 1){
					mLauncherFocusView.initFocusView(mItemListCurView, false, 0f);
				}
				*/
				Log.e(TAG, "list getheight:"+mItemList.getHeight()+
						" view getTop:"+view.getTop()+"height:"+
						view.getHeight()+" visible first:"+mItemList.getFirstVisiblePosition());
				
				if((mItemListCurPosition == 0||mItemList.getFirstVisiblePosition()==0&&view.getTop()>=(view.getHeight()))
						||(mItemList.getFirstVisiblePosition()!=0&&view.getTop()>view.getHeight())){
					mLauncherFocusView.moveTo(mItemListCurView);
				}else{
					listTextColorSet();
					mItemList.setSelectionFromTop(mItemListCurPosition, view.getHeight());
				}
			}
			
			//fixed the keyboard repeat mode
			if(!mTextColorChangeFlag && mFocusAnimationEndFlag){
				if((mItemListCurPosition == 0 
						|| mItemListCurPosition==mItemList.getCount()-1)){
					mTextColorChangeFlag = true;
					listTextColorSet();
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			if (DEBUG)
				Log.e(TAG, "item list unselect ");
			mItemList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

		}

	};

	private void listTextColorSet(){
		if (mItemCurHolder != null && mTextColorChangeFlag && mFocusAnimationEndFlag) {
			mTextColorChangeFlag = false;
			mItemCurHolder.mItemTitle.setTextColor(getResources().getColor(R.color.settings_ffffff));
			if (mItemCurEntity != null) {
				if (EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_SELECT == mItemCurEntity.getItemCategory()) {
					mItemCurHolder.mItemContentTv.setTextColor(getResources().getColor(R.color.settings_ffffff));
				} else if (EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_EDIT == mItemCurEntity
						.getItemCategory()) {
					if (!isAutoFlag) {
						mItemCurHolder.mItemContentEdit.setTextColor(getResources().getColor(
								R.color.settings_ffffff));
					}
				} else if (EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_ENTER == mItemCurEntity
						.getItemCategory()) {
					mItemCurHolder.mItemRightArrow.setImageResource(R.drawable.right_enter_point_selected);
				}
			}
		}	
	}
	
	private void initItemList() {
		Log.v(TAG, "initItemList");
		ArrayList<EthernetItemEntity> itemList = new ArrayList<EthernetItemEntity>();
		String[] item = getResources().getStringArray(R.array.ethernet_connected_item_list);
		String[] onOff = getResources().getStringArray(R.array.on_off);
		int activeType = Utils.getActiveNetworkType(EthernetConnectedAcivity.this);

		for (int i = 0; i < item.length; i++) {
			EthernetItemEntity entity = new EthernetItemEntity();
			entity.setItemTitle(item[i]);
			switch (i) {
			case ETHERNET_ITEM_WIFI_SHARE:
				entity.setItemCategory(EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_ENTER);
				break;
			case ETHERNET_ITEM_IPV4:
			case ETHERNET_ITEM_IPV4_MANUAL_ADJUST:
			case ETHERNET_ITEM_IPV6:
			case ETHERNET_ITEM_IPV6_MANUAL_ADJUST:
				entity.setItemCategory(EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_SELECT);
				entity.setItemContents(onOff);
				break;
			case ETHERNET_ITEM_IPV4_IP_ADDRESS:
			case ETHERNET_ITEM_IPV4_NET_MASK:
			case ETHERNET_ITEM_IPV4_GATEWAY:
			case ETHERNET_ITEM_IPV4_DNS:
			case ETHERNET_ITEM_IPV6_IP_ADDRESS:
			case ETHERNET_ITEM_IPV6_NET_MASK:
			case ETHERNET_ITEM_IPV6_GATEWAY:
			case ETHERNET_ITEM_IPV6_DNS:
				entity.setItemCategory(EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_EDIT);
				break;
			case ETHERNET_ITEM_DISABLE_NET:
				entity.setItemCategory(EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_NULL);
				if(activeType != SettingsApplication.TYPE_PPPOE){
					continue;
				}
				break;
			default:
				break;
			}
			itemList.add(entity);
		}
		
		
		mItemlistAdapter = new EthernetAdapter(EthernetConnectedAcivity.this, itemList);
		mItemList.setAdapter(mItemlistAdapter);
 
	}

	private void initItemView(EthernetItemHolder holder, EthernetItemEntity itemEntity, int position) {
		holder.mItemTitle.setText(itemEntity.getItemTitle());
		switch (itemEntity.getItemCategory()) {
		case EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_EDIT:
			holder.mItemLeftArrow.setVisibility(View.GONE);
			holder.mItemRightArrow.setVisibility(View.GONE);
			holder.mItemContentTv.setVisibility(View.GONE);
			holder.mItemContentEdit.setVisibility(View.VISIBLE);
			break;
		case EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_ENTER:
			holder.mItemLeftArrow.setVisibility(View.GONE);
			holder.mItemRightArrow.setVisibility(View.VISIBLE);
			holder.mItemRightArrow.setImageResource(R.drawable.right_enter_point_unselected);
			holder.mItemContentTv.setVisibility(View.GONE);
			holder.mItemContentEdit.setVisibility(View.GONE);
			break;
		case EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_NULL:
			holder.mItemLeftArrow.setVisibility(View.GONE);
			holder.mItemRightArrow.setVisibility(View.GONE);
			holder.mItemContentTv.setVisibility(View.GONE);
			holder.mItemContentEdit.setVisibility(View.GONE);
			break;
		case EthernetItemEntity.ETHERNET_CONNECT_ITEM_CATEGORY_SELECT:
			holder.mItemLeftArrow.setVisibility(View.VISIBLE);
			holder.mItemRightArrow.setVisibility(View.VISIBLE);
			holder.mItemRightArrow.setImageResource(R.drawable.right_arrow_unselected);
			holder.mItemContentTv.setVisibility(View.VISIBLE);
			holder.mItemContentEdit.setVisibility(View.GONE);
			break;
		default:
			break;
		}

	}

	private void initItemData(EthernetItemHolder holder, EthernetItemEntity itemEntity, int position) {
		switch (position) {
		case ETHERNET_ITEM_WIFI_SHARE:
			int wifiApState = mWifiDataEntity.getWifiApState();
//			Log.v(TAG, "ap status="+wifiApState+",submit="+mSharePreferences.getBoolean("SUBMIT", false));
			if (wifiApState == WifiManager.WIFI_AP_STATE_ENABLED || wifiApState == WifiManager.WIFI_AP_STATE_ENABLING) {
				if(mSharePreferences.getBoolean("SUBMIT", false)){
					holder.mItemContentTv.setVisibility(View.VISIBLE);
					holder.mItemContentTv.setText(getResources().getText(R.string.wifi_share_opened).toString());
				}else {
					holder.mItemContentTv.setVisibility(View.INVISIBLE);
				}
			}else{
				holder.mItemContentTv.setVisibility(View.INVISIBLE);
			}
			break;
		case ETHERNET_ITEM_IPV4:
			holder.mItemContentTv.setText(itemEntity.getItemContents()[1]);
			itemEntity.setItemSelectFlag(EthernetItemEntity.SELECT_ON);
			break;
		case ETHERNET_ITEM_IPV4_MANUAL_ADJUST:
			if (isAutoFlag) {
				holder.mItemContentTv.setText(itemEntity.getItemContents()[0]);
				itemEntity.setItemSelectFlag(EthernetItemEntity.SELECT_OFF);
			} else {
				holder.mItemContentTv.setText(itemEntity.getItemContents()[1]);
				itemEntity.setItemSelectFlag(EthernetItemEntity.SELECT_ON);
			}
			break;
		case ETHERNET_ITEM_IPV4_IP_ADDRESS:
			holder.mItemContentEdit.setText(mV4IPAddress);
			if(mV4IPAddress!=null && mV4IPAddress.length()>0)
			   holder.mItemContentEdit.setSelection(mV4IPAddress.length());
			break;
		case ETHERNET_ITEM_IPV4_NET_MASK:
			holder.mItemContentEdit.setText(mV4NetmaskAddress);
			if(mV4NetmaskAddress!=null && mV4NetmaskAddress.length()>0)
			   holder.mItemContentEdit.setSelection(mV4NetmaskAddress.length());
			break;
		case ETHERNET_ITEM_IPV4_GATEWAY:
			holder.mItemContentEdit.setText(mV4GatewayAddress);
			if(mV4GatewayAddress!=null && mV4GatewayAddress.length()>0)
			   holder.mItemContentEdit.setSelection(mV4GatewayAddress.length());
			break;
		case ETHERNET_ITEM_IPV4_DNS:
			holder.mItemContentEdit.setText(mV4DNSAddress);
			if(mV4DNSAddress!=null && mV4DNSAddress.length()>0)
			   holder.mItemContentEdit.setSelection(mV4DNSAddress.length());
			break;
		case ETHERNET_ITEM_IPV6:
			holder.mItemContentTv.setText(itemEntity.getItemContents()[0]);
			itemEntity.setItemSelectFlag(EthernetItemEntity.SELECT_OFF);
			break;
		case ETHERNET_ITEM_IPV6_MANUAL_ADJUST:
			holder.mItemContentTv.setText(itemEntity.getItemContents()[0]);
			itemEntity.setItemSelectFlag(EthernetItemEntity.SELECT_OFF);
			break;
		case ETHERNET_ITEM_IPV6_IP_ADDRESS:
			holder.mItemContentEdit.setText("");
			break;
		case ETHERNET_ITEM_IPV6_NET_MASK:
			holder.mItemContentEdit.setText("");
			break;
		case ETHERNET_ITEM_IPV6_GATEWAY:
			holder.mItemContentEdit.setText("");
			break;
		case ETHERNET_ITEM_IPV6_DNS:
			holder.mItemContentEdit.setText("");
			break;
		case ETHERNET_ITEM_DISABLE_NET:
			break;

		default:
			break;
		}
		
	}

	class EthernetAdapter extends BaseAdapter {
		LayoutInflater mInflater = null;
		private Context mContext = null;
		private ArrayList<EthernetItemEntity> mList = null;

		public EthernetAdapter(Context context, ArrayList<EthernetItemEntity> list) {
			mContext = context;
			mList = list;
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			EthernetItemHolder holder = null;
			if (convertView == null) {
				holder = new EthernetItemHolder();
				convertView = mInflater.inflate(R.layout.ethernet_item_list, null);
				holder.mItemTitle = (TextView) convertView.findViewById(R.id.ethernet_item_title);
				holder.mItemLeftArrow = (ImageView) convertView.findViewById(R.id.ethernet_item_left_arrow);
				holder.mItemContentTv = (TextView) convertView.findViewById(R.id.ethernet_item_content_tv);
				holder.mItemContentEdit = (EditText) convertView.findViewById(R.id.ethernet_item_content_edit);
				holder.mItemRightArrow = (ImageView) convertView.findViewById(R.id.ethernet_item_right_arrow);
				convertView.setTag(holder);
			} else {
				holder = (EthernetItemHolder) convertView.getTag();
			}

			final EthernetItemEntity itemEntity = mList.get(position);

			initItemView(holder, itemEntity, position);
			initItemData(holder, itemEntity, position);
			
	
			return convertView;
		}

	}
 
	View.OnKeyListener mEditTextOnKeyListener = new View.OnKeyListener() {
		
		@Override
		public boolean onKey(View view, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			/*if(DEBUG){
				Log.e(TAG, "I'm here......................onkey");
			}*/
			if(event.getAction() == KeyEvent.ACTION_DOWN){
				if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
					if(mItemCurHolder!=null){
						mItemCurHolder.mItemContentEdit.requestFocus();
					}
				}else if(keyCode== KeyEvent.KEYCODE_DPAD_UP){
					mItemCurHolder.mItemContentEdit.setCursorVisible(false);
					mItemCurHolder.mItemContentEdit.setFocusable(false);
					mItemList.setSelectionFromTop(mItemListCurPosition-1, mItemList.getSelectedView().getHeight());
				}
			}
			return false;
		}
	};
	
	private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
				try {
					if (DEBUG) {
						Log.e(TAG, "getEditText()=" + getItemEditText());
						Log.e(TAG, "mItemListCurPosition=" + mItemListCurPosition);
                                        }
					mItemCurHolder.mItemContentEdit.setCursorVisible(false);
					mItemCurHolder.mItemContentEdit.setFocusable(false);
                                       
					InputMethodManager mng = (InputMethodManager) EthernetConnectedAcivity.this
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					mng.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					
					if (mItemListCurPosition == ETHERNET_ITEM_IPV4_NET_MASK) {
						if (!Utils.isValidMask(getItemEditText(), mHandler)) {
							Log.e(TAG, "!isValidMask(getEditText())");
							recoveryItemEditText();
							return false;
						}
					} else {
						if (!Utils.isIpAddress(getItemEditText(), mHandler)) {
							Log.e(TAG, "!isIpAddress(getEditText())");
							recoveryItemEditText();
							return false;
						}
					}
					
					mItemCurHolder.mItemContentEdit.setCursorVisible(false);
					mKeyboardOKFlag = true;
					updateEthernetInfo(getItemEditText());
					
					
				} catch (Exception e) {
					Log.e(TAG, "Hide key broad error", e);
				}
			}
			return false;
		}
	};

	private String getItemEditText() {
		String result = null;
		if (mItemCurHolder != null) {
			result = mItemCurHolder.mItemContentEdit.getText().toString().trim();
		}
		return result;
	}

	private void recoveryItemEditText() {
		mTextEditFlag = false;
		mKeyboardOKFlag = false;
		if (mItemCurHolder != null) {
			if (mItemListCurPosition == ETHERNET_ITEM_IPV4_IP_ADDRESS) {
				mItemCurHolder.mItemContentEdit.setText(mV4IPAddress);
				if(mV4IPAddress!=null && mV4IPAddress.length()>0)
				   mItemCurHolder.mItemContentEdit.setSelection(mV4IPAddress.length());
			} else if (mItemListCurPosition == ETHERNET_ITEM_IPV4_DNS) {
				mItemCurHolder.mItemContentEdit.setText(mV4DNSAddress);
				if(mV4DNSAddress!=null && mV4DNSAddress.length()>0)
				   mItemCurHolder.mItemContentEdit.setSelection(mV4DNSAddress.length());
			} else if (mItemListCurPosition == ETHERNET_ITEM_IPV4_GATEWAY) {
				mItemCurHolder.mItemContentEdit.setText(mV4GatewayAddress);
				if(mV4GatewayAddress!=null && mV4GatewayAddress.length()>0)
		        	mItemCurHolder.mItemContentEdit.setSelection(mV4GatewayAddress.length());
			} else if (mItemListCurPosition == ETHERNET_ITEM_IPV4_NET_MASK) {
				mItemCurHolder.mItemContentEdit.setText(mV4NetmaskAddress);
				if(mV4NetmaskAddress!=null && mV4NetmaskAddress.length()>0)
				   mItemCurHolder.mItemContentEdit.setSelection(mV4NetmaskAddress.length());
			}
		}
	}

	private void updateEthernetInfo(String str) {
		if (mItemListCurPosition == ETHERNET_ITEM_IPV4_IP_ADDRESS) {
			mV4IPAddress = str;
		} else if (mItemListCurPosition == ETHERNET_ITEM_IPV4_DNS) {
			mV4DNSAddress = str;
		} else if (mItemListCurPosition == ETHERNET_ITEM_IPV4_GATEWAY) {
			mV4GatewayAddress = str;
		} else if (mItemListCurPosition == ETHERNET_ITEM_IPV4_NET_MASK) {
			mV4NetmaskAddress = str;
		}
		
		mHandler.sendEmptyMessage(EthernetDataEntity.MSG_IPV4_ADJUST_STATIC);
	}
	
	private TextWatcher mTextWatcher = new TextWatcher() {
		public void onTextChanged(CharSequence sequence, int start, int before, int count) {
			// TODO Auto-generated method stub

		}

		public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {
			// TODO Auto-generated method stub
			/*if (DEBUG) {
				Log.e(TAG, "sequence=" + sequence);
				Log.e(TAG, "start=" + start);
				Log.e(TAG, "count=" + count);
				Log.e(TAG, "after=" + after);
			}*/
		}

		public void afterTextChanged(Editable editable) {
			// TODO Auto-generated method stub
			try {
				String tempString = editable.toString();
				/*if (DEBUG) {
					Log.e(TAG, "tempString=" + tempString);
				}*/
				mTextEditFlag = true;
				if (tempString.trim().length() > EthernetDataEntity.IP_MAX_LENGTH) {
					Message msg = Message.obtain();
					msg.what = EthernetDataEntity.ALERTMSG_EXCEED_CHRATER;
					mHandler.sendMessage(msg);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	};

	private void showDhcpInfo() {
		if(mConManData.isDhcpSucess())
		   initEthernetData(false);
	}
	
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case EthernetDataEntity.MSG_IPV4_ADJUST_DHCP:;
				initEthernetData(false);
				mConManData.setEthernetInfo(true, null, null, mV4DNSAddress, null);
				if (DEBUG) {
					Log.e(TAG, "get ip:" + mV4IPAddress + " mask:" + mV4NetmaskAddress + " gw:" + mV4GatewayAddress
							+ " dns:" + mV4DNSAddress);
				}
				mItemlistAdapter.notifyDataSetChanged();
				mHandler.removeMessages(EthernetDataEntity.MSG_IPV4_GET_DATA);
				mHandler.sendEmptyMessageDelayed(EthernetDataEntity.MSG_IPV4_GET_DATA, 2000);
				break;
			case EthernetDataEntity.MSG_IPV4_ADJUST_STATIC:

				if (DEBUG) {
					Log.e(TAG, "set ip:" + mV4IPAddress + " mask:" + mV4NetmaskAddress + " gw:" + mV4GatewayAddress
							+ " dns:" + mV4DNSAddress);
				}
                checkSetValid();
 
				if (DEBUG) {
					Log.e(TAG, "I'm here static.................3");
				}

				try {
					mConManData.setEthernetInfo(false, mV4IPAddress, mV4NetmaskAddress, mV4DNSAddress,mV4GatewayAddress);
//					initEthernetData(false);
				} catch (Exception e) {
					// TODO: handle exception
					ToastUtils.showToast(EthernetConnectedAcivity.this, getResources().getString(R.string.set_failed), Toast.LENGTH_LONG);
				    initEthernetData(false);
					mItemlistAdapter.notifyDataSetChanged();
				}
				
				break;
			case EthernetDataEntity.ALERTMSG_EXCEED_CHRATER:
				ToastUtils.showToast(EthernetConnectedAcivity.this,getResources().getString(R.string.str_ip_full_characters),Toast.LENGTH_LONG);
				break;
			case EthernetDataEntity.ALERTMSG_IPNOGATEWAY:
				ToastUtils.showToast(EthernetConnectedAcivity.this,getResources().getString(R.string.str_ip_no_gateway),Toast.LENGTH_LONG);
				break;
			case EthernetDataEntity.ALERTMSG_IPINVALID:
				ToastUtils.showToast(EthernetConnectedAcivity.this,getResources().getString(R.string.str_ip_address_invalid),Toast.LENGTH_LONG);
				break;
			case EthernetDataEntity.ALERTMSG_MAX_ITEM_255:
				ToastUtils.showToast(EthernetConnectedAcivity.this,getResources().getString(R.string.str_ip_max_value),Toast.LENGTH_LONG);
				break;
			case EthernetDataEntity.ALERTMSG_IP_EQUAL_GATEWAY:
				ToastUtils.showToast(EthernetConnectedAcivity.this,getResources().getString(R.string.str_ip_equal_gateway),Toast.LENGTH_LONG);
				break;
			case EthernetDataEntity.MSG_IPV4_GET_DATA:
				showDhcpInfo();
				mItemlistAdapter.notifyDataSetChanged();
				stopProgressDialog();
				dhcpTimeoutDialog(getResources().getString(R.string.ethernet_dhcp_timeout));
				break;
            case EthernetDataEntity.MSG_DHCP_TIMEOUT:
				stopProgressDialog();
				dhcpTimeoutDialog(getResources().getString(R.string.ethernet_dhcp_timeout));
				break;
			default:
				break;
			}
		}
	};
	
	
	private void checkSetValid(){
		 Log.v(TAG, "checkSetValid...");
		 if(mV4IPAddress==null || mV4IPAddress.equals("0.0.0.0") 
				 || mV4IPAddress.equals("")){
			 ToastUtils.showToast(EthernetConnectedAcivity.this, 
					 getResources().getString(R.string.ip_null), Toast.LENGTH_LONG);
			 return;
         }
         if(mV4GatewayAddress==null || mV4GatewayAddress.equals("0.0.0.0") 
        		 || mV4GatewayAddress.equals("")){
        	 ToastUtils.showToast(EthernetConnectedAcivity.this, 
        			 getResources().getString(R.string.gateway_null), Toast.LENGTH_LONG);
        	 return;
         }
         if(mV4NetmaskAddress==null || mV4NetmaskAddress.equals("0.0.0.0") 
        		 || mV4NetmaskAddress.equals("")){
        	 ToastUtils.showToast(EthernetConnectedAcivity.this, 
        			 getResources().getString(R.string.mask_null), Toast.LENGTH_LONG);
        	 return;
         }
         if(mV4DNSAddress==null || mV4DNSAddress.equals("0.0.0.0") 
        		 || mV4DNSAddress.equals("")){
         	ToastUtils.showToast(EthernetConnectedAcivity.this, 
         			getResources().getString(R.string.dns_null), Toast.LENGTH_LONG);
         	return;
         }
	}
	
	private void regeditReceiver() {
		IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mFilter.addAction("android.net.conn.TETHER_STATE_CHANGED"); //wifi 热点
        mFilter.addAction(PppoeManager.PPPOE_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver, mFilter);
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v(TAG, "action=="+action);
            
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				Log.v(TAG, "eth status==" + Utils.checkEthState());
				if (!Utils.checkEthState()) {
					finish();
				} else {
					if (!isPPPOE && mConManData.isDhcpRuning("eth0")) {
						mHandler.removeMessages(EthernetDataEntity.MSG_IPV4_GET_DATA);
						mHandler.sendEmptyMessageDelayed(EthernetDataEntity.MSG_IPV4_GET_DATA, 2500);
					}
				}
 
            }else if(action.equals("android.net.conn.TETHER_STATE_CHANGED")){
            	int wifiApState = mWifiDataEntity.getWifiApState();
            	if(wifiApState==WifiManager.WIFI_AP_STATE_ENABLED || wifiApState==WifiManager.WIFI_AP_STATE_DISABLED){
        		if(mItemlistAdapter!=null){
            			mItemlistAdapter.notifyDataSetChanged();
            		}
            	}
            }else if(action.equals(PppoeManager.PPPOE_STATE_CHANGED_ACTION)){
            	int event = intent.getIntExtra(PppoeManager.EXTRA_PPPOE_STATE, PppoeManager.PPPOE_STATE_UNKNOWN);
            	Log.v(TAG, "pppoe receive event="+event);
            	if (event == PppoeStateTracker.EVENT_DISCONNECTED || event == PppoeStateTracker.EVENT_CONNECT_FAILED) {
            		finish();
            	}
            } 
        }
    };
    
    private void unRegeditReceiver(){
    	unregisterReceiver(mReceiver);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode) { //根据状态码，处理返回结果  
        case PppoeConnectActivity.RESULT_DISCONNECT_SECUSS:
        	Log.v(TAG, "finish");
        	finish();
        	break;
        
        default:  
        break;  
        }    
	}
   
    

    private void startProgressDialog(String msg) {
		mProgressDialog.setMessage(msg);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
		mProgressDialog.startLoading();
	}

	private void stopProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.stopLoading();
			mProgressDialog.dismiss();
		}
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    		stopProgressDialog();
    	}
    	return super.onKeyDown(keyCode, event);
    }
  
    
    private void dhcpTimeoutDialog(String message){
    	Log.v(TAG, "dhcpTimeoutDialog 1=="+mConManData.isDhcpRuning(ConnectivityManagerData.ETH_NAME));
    	Log.v(TAG, "dhcpTimeoutDialog 2=="+mConManData.isDhcpSucess());
    	if(!isPPPOE && mConManData.isDhcpRuning(ConnectivityManagerData.ETH_NAME) && 
    			!mConManData.isDhcpSucess() && !isFinishing()){
    		Log.v(TAG, "set null");
    		mV4DNSAddress=mV4GatewayAddress=mV4IPAddress=mV4NetmaskAddress="";
    		mItemlistAdapter.notifyDataSetChanged();
    		
    	    if (tipsDialog == null) {
			 tipsDialog = new ResolutionDialog(this);
			 Window dialogWindow = tipsDialog.getWindow();
			 WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			 lp.width = getResources().getDimensionPixelSize(R.dimen.diy_dialog_width);
			 lp.height = getResources().getDimensionPixelSize(R.dimen.diy_dialog_height);
			 dialogWindow.setAttributes(lp);

			 TextView textView = (TextView) dialogWindow.findViewById(R.id.message1);
			 textView.setText(message);
			
			
			 Button button1 = (Button)dialogWindow.findViewById(R.id.button1);
			 Button button2 = (Button)dialogWindow.findViewById(R.id.button2);
             button1.setText(getResources().getString(R.string.enter));
             button2.setText(getResources().getString(R.string.cancel));
             button1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					tipsDialog.dismiss();
				}
			 });
             button2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					tipsDialog.dismiss();
				}
			 });
             tipsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface arg0) {
					// TODO Auto-generated method stub
					tipsDialog = null;
				}
			 });
		}else {
			if(tipsDialog!=null){
				TextView textView = (TextView) tipsDialog.getWindow().findViewById(R.id.message1);
				textView.setText(message);
			}
		}
		if (!tipsDialog.isShowing())
			tipsDialog.show();
      }else {
    	  if(tipsDialog!=null && tipsDialog.isShowing())
		       tipsDialog.dismiss();
	  }
    	
    }
    
     
}
