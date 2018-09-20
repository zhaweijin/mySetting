package com.hiveview.cloudtv.settings;

import java.util.ArrayList;
import java.util.HashMap;

 

import android.os.SystemProperties;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.os.SystemProperties;

import com.hiveview.cloudtv.settings.connectivity.ConnectivityManagerData;
import com.hiveview.cloudtv.settings.pppoe.PppoeDataEntity;
import com.hiveview.cloudtv.settings.util.ToastUtils;
import com.hiveview.cloudtv.settings.util.Utils;
import com.hiveview.cloudtv.settings.widget.CommonListView;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView.FocusViewAnimatorEndListener;
import android.widget.Toast;

public class NetworkSettingsActivity extends Activity {

	ConnectivityManagerData mConnectivityData;

	private String TAG = "NetworkSettingsActivity";
	private String[] mArrays;
	
	private View mItemListCurView = null;
	private CommonListView mListView;
	private LauncherFocusView mLauncherFocusView = null;
	
	private boolean mTextColorChangeFlag = false;
	private boolean mFocusAnimationEndFlag = false;
	
	private TextView mItemName = null;
	private ImageView mImageView = null;
	
	private SimpleAdapter listItemAdapter;
	private ArrayList<HashMap<String, Object>> listItem;
	
	private int mItemListCurPosition = -1;
	private int mCurKeycode = -1;
	private long mKeyDownTime = 0l;
	private boolean mIsFirstIn = true;
	private Context mContext;
	
	private int WIFI_MENU = 100;
	private int ETHERNET_MENU = 100;
	private int PPPOE_MENU = 100;
	private int TEST_SPEED_MENU = 100;
	private int NETWORK_CHECK_MENU = 100;

 
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.network_setting);
		mContext = this;
		
		mConnectivityData = new ConnectivityManagerData(mContext);
		//showEthernetInfo();
		
		if(SystemProperties.get(Utils.PROP_IPTV_ONLY_PPPOE, "false").equals("true")){
			initIPTV();
		}else {
			init();
		}
		initEvent();
		initListener();
 
	}

	private void initIPTV() {
		mArrays = new String[2];
		mArrays[0] = getResources().getString(R.string.ethernet_name_menu);
		mArrays[1] = getResources().getString(R.string.pppoe_name);

		ETHERNET_MENU = 0;
		PPPOE_MENU = 1;
	}
	
	private void init(){
		
		mArrays = new String[5];
	    mArrays[0] = getResources().getString(R.string.wifi_name);
	    mArrays[1] = getResources().getString(R.string.ethernet_name);
	    mArrays[2] = getResources().getString(R.string.pppoe_name);
	    mArrays[3] = getResources().getString(R.string.test_speed_title);
	    mArrays[4] = getResources().getString(R.string.ethernet_dis_check_net);
		
	    WIFI_MENU = 0;
        ETHERNET_MENU = 1;
        PPPOE_MENU = 2;
        TEST_SPEED_MENU = 3;
        NETWORK_CHECK_MENU = 4;
	}
	
	private void initEvent(){
		mListView = (CommonListView) findViewById(R.id.device_info_list);
		mLauncherFocusView = (LauncherFocusView) findViewById(R.id.deviceinfo_focus_view);
		
		mLauncherFocusView.setAnimatorEndListener(new FocusViewAnimatorEndListener() {

			@Override
			public void OnAnimateEnd(View currentFocusView) {
				mFocusAnimationEndFlag = true;
				listTextColorSet();
			}

			@Override
			public void OnAnimateStart(View currentFocusView) {
				// TODO Auto-generated method stub
				mFocusAnimationEndFlag = false;
			}
		});
		
		
        listItem = new ArrayList<HashMap<String, Object>>();
		
		for (int i = 0; i < mArrays.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("item", mArrays[i]);
			listItem.add(map);
		}
		
		
		listItemAdapter = new SimpleAdapter(this, listItem, R.layout.network_setting_item,
				new String[] { "item"}, new int[] { R.id.item});
		mListView.setAdapter(listItemAdapter);
	}
	
	
	private void listTextColorSet() {
		if (mItemName != null && mTextColorChangeFlag && mFocusAnimationEndFlag) {
			mItemName.setTextColor(this.getResources().getColor(R.color.white));
		}

		if (mImageView != null) {
			mImageView.setImageResource(R.drawable.page_right_big_selected);
		}
		
		if (mTextColorChangeFlag && mFocusAnimationEndFlag) {
			mTextColorChangeFlag = false;
		}
	}
	
	
	
	private void initListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt,
					long paramLong) {
				// TODO Auto-generated method stub
                if(paramInt==WIFI_MENU){
                	if(!mConnectivityData.isEthernetAvailable()){
						jump2Wifi();
					}else {
						Utils.startListFocusAnimation(NetworkSettingsActivity.this, mLauncherFocusView,
								R.anim.list_focus_anim);
						ToastUtils.showToast(NetworkSettingsActivity.this, getResources().getString(R.string.tips_pull_out_ethernet), 1500);
					}
                }
                
                if(paramInt==ETHERNET_MENU){
                	if(mConnectivityData.isEthernetAvailable() && Utils.getActiveNetworkType(NetworkSettingsActivity.this) != SettingsApplication.TYPE_PPPOE){
						jump2Ethernet();
					}else if(mConnectivityData.isEthernetAvailable() && Utils.getActiveNetworkType(NetworkSettingsActivity.this) == SettingsApplication.TYPE_PPPOE){
						Utils.startListFocusAnimation(NetworkSettingsActivity.this, mLauncherFocusView,R.anim.list_focus_anim);
						ToastUtils.showToast(NetworkSettingsActivity.this, getResources().getString(R.string.used_pppoeing), 1500);
					}else {
						Utils.startListFocusAnimation(NetworkSettingsActivity.this, mLauncherFocusView,R.anim.list_focus_anim);
						ToastUtils.showToast(NetworkSettingsActivity.this, getResources().getString(R.string.tips_pull_in_ethernet), 1500);
					}
                }
                
				if(paramInt==PPPOE_MENU){
					if(mConnectivityData.isEthernetAvailable()){
				    	jump2Pppoe();
					}else{
						Utils.startListFocusAnimation(NetworkSettingsActivity.this, mLauncherFocusView,
								R.anim.list_focus_anim);
						ToastUtils.showToast(NetworkSettingsActivity.this, getResources().getString(R.string.tips_pull_in_ethernet), 1500);
					}
				}
				 
				if(paramInt==TEST_SPEED_MENU){
					startActivity(new Intent(NetworkSettingsActivity.this, TestSpeedNewActivity.class));
				}
				
				if(paramInt==NETWORK_CHECK_MENU){
		          //国内修改检测网络问题功能的页面，海外的维持原状
					if(Utils.isOverseas()){//海外
						startActivity(new Intent(NetworkSettingsActivity.this, CheckNetworkActivity.class));
					}else{//国内
						//Toast.makeText(mContext, "国内！！！", Toast.LENGTH_SHORT).show();
						startActivity(new Intent(NetworkSettingsActivity.this, NetWorkActivity.class));
					}
				}
				
			}

		});
		mListView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View paramView, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				mCurKeycode = keyCode;
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
							|| keyCode == KeyEvent.KEYCODE_DPAD_UP) {
						mCurKeycode = keyCode;
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
				} else if (event.getAction() == KeyEvent.ACTION_UP) {
					if (!mTextColorChangeFlag) {
						mTextColorChangeFlag = true;
						listTextColorSet();
					}
				}

				return false;
			}

		});
		mListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onItemSelected(AdapterView<?> paramAdapterView, View paramView,
					int paramInt, long paramLong) {
				// TODO Auto-generated method stub
				mItemListCurPosition = paramInt;
				mItemListCurView = paramView;
				if (mIsFirstIn) {
					mLauncherFocusView.initFocusView(paramView, false, 0f);
				}
				//把之前的还原
				if (mItemName != null) {
					Log.v(TAG, "set mItemName");
					mItemName.setTextColor(mContext.getResources().getColor(R.color.grey5_color));
				}
				if (mImageView != null) {
					mImageView.setImageResource(R.drawable.page_right_big);
				}

				//初始化
				mItemName = (TextView) paramView.findViewById(R.id.item);
				mImageView = (ImageView) paramView.findViewById(R.id.more_img);

				if (mIsFirstIn) {
					mIsFirstIn = false;
					mTextColorChangeFlag = true;
					listTextColorSet();
				}

				if (mCurKeycode == KeyEvent.KEYCODE_DPAD_DOWN) {
					if (paramInt < 5
							|| paramInt > mListView.getCount() - 2
							|| (mListView.getFirstVisiblePosition() == 0 && paramView.getTop() < (paramView
									.getHeight() * 4))
							|| (mListView.getFirstVisiblePosition() != 0 && paramView.getTop() < paramView
									.getHeight() * 5)) {
						mLauncherFocusView.moveTo(paramView);
					} else {
						listTextColorSet();
						mListView.setSelectionFromTop(paramInt,
								paramView.getTop() - paramView.getHeight());
					}
				} else if (mCurKeycode == KeyEvent.KEYCODE_DPAD_UP) {
					if ((mItemListCurPosition == 0 || mListView.getFirstVisiblePosition() == 0
							&& paramView.getTop() > (paramView.getHeight()))
							|| (mListView.getFirstVisiblePosition() != 0 && paramView.getTop() >= paramView
									.getHeight())) {
						mLauncherFocusView.moveTo(paramView);
					} else {
						listTextColorSet();
						mListView.setSelectionFromTop(mItemListCurPosition, paramView.getHeight());
					}
				} else if (mCurKeycode == KeyEvent.KEYCODE_PAGE_UP
						|| mCurKeycode == KeyEvent.KEYCODE_PAGE_DOWN) {
					mLauncherFocusView.moveTo(paramView);
				}
				// fixed the keyboard repeat mode
				if (!mTextColorChangeFlag && mFocusAnimationEndFlag) {
					if ((mItemListCurPosition == 0 || mItemListCurPosition == mListView.getCount() - 1)) {
						mTextColorChangeFlag = true;
						listTextColorSet();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> paramAdapterView) {
				// TODO Auto-generated method stub
			}

		});

	}

   private void showEthernetInfo(){
		
		Log.e(TAG, "============================================");
		Log.e(TAG, "ethernet available" + mConnectivityData.isEthernetAvailable());
		Log.e(TAG, "ethernet ip " + mConnectivityData.getEthernetIpAddress());
		Log.e(TAG, "ethernet mac " + mConnectivityData.getEthernetMacAddress());
		Log.e(TAG, "ethernet dns " + mConnectivityData.getEthernetDns(0));
		Log.e(TAG, "ethernet dns1 " + mConnectivityData.getEthernetDns(1));
		Log.e(TAG, "ethernet domains " + mConnectivityData.getEthernetDomains());
		Log.e(TAG, "============================================");

	}
	
 
  
	
	private void jump2Ethernet(){
		Intent intent = new Intent(NetworkSettingsActivity.this, EthernetConnectedAcivity.class);
		startActivity(intent);
	}
 
	
	private void jump2Wifi(){
		Intent intent = new Intent(NetworkSettingsActivity.this, WifiConnectActivity.class);
		startActivity(intent);
	}
	
 
	
	private void jump2Pppoe(){
		boolean isPPPOEOK = Utils.getActiveNetworkType(NetworkSettingsActivity.this) == SettingsApplication.TYPE_PPPOE?true:false;
		Log.v(TAG, "network type="+Utils.getActiveNetworkType(NetworkSettingsActivity.this));
		if(isPPPOEOK){
			jump2Ethernet();
		}else {
			Intent intent = new Intent(NetworkSettingsActivity.this, PppoeConnectActivity.class);
			intent.putExtra(PppoeDataEntity.PPPOE_EXTRA_STATE, PppoeDataEntity.PPPOE_EXTRA_STATE_CONNECT);
			startActivity(intent);
		}
	}
}
