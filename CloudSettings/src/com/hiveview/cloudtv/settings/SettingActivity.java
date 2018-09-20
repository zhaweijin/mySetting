package com.hiveview.cloudtv.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.os.SystemProperties;
import com.hiveview.cloudtv.settings.connectivity.ConnectivityManagerData;
import com.hiveview.cloudtv.settings.pppoe.PppoeDataEntity;
import com.hiveview.cloudtv.settings.util.ToastUtils;
import com.hiveview.cloudtv.settings.util.Utils;
import com.hiveview.manager.PingListener;
import com.hiveview.manager.PingManager;

public class SettingActivity extends Activity implements PingListener{
	private static final String TAG="SettingActivity";

	private RelativeLayout mMainLayout1;
	private RelativeLayout mMainLayout2;
	private RelativeLayout mMainLayout3;
	private RelativeLayout mMainLayout4;
	private ImageView imageView;
	final Context mContext = this;
	private String extraKey = "connection_status";
	private int defaultValue = 110000;
	private final int LAN_SUCCESS = 100005;
	private final int LAN_ERROR = 100006;
	private final int WIFI_SUCCESS = 100003;
	private final int WIFI_ERROR = 100004;
	private final int PPPOE_SUCCESS = 100001;
	private final int PPPOE_ERROR = 100002;
	private final int NETWORK_ERROR = 110000;
	private final int NETWOEK_CHECK = 110001;
	
	private ImageView about_image;
	private BroadcastReceiver mNetworkChangeListener = null;
	
	private int curPingStatus = 0;
	private ConnectivityManager connectivity = null;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case NETWOEK_CHECK:
				setNetState();
				break; 
			}
		}
		
	};

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		registerNetworkListener();
		activeActivity();
		setIN(false);
		setAnimation();
		PingManager.regCallBackListener(this);
		connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        
		
		initNetState();
		Log.v("test", "pppoe type==="+SettingsApplication.TYPE_PPPOE);
 
		 
 
	}
	
	private void setIN(boolean flag){
		mMainLayout1.setFocusable(flag);
		mMainLayout2.setFocusable(flag);
		mMainLayout3.setFocusable(flag);
		mMainLayout4.setFocusable(flag);
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	
	private void initNetState() {

        int activeType = Utils.getActiveNetworkType(this);
        if (activeType == ConnectivityManager.TYPE_WIFI) {
             imageView.setImageResource(R.drawable.wifi_success);
             
        } else if (activeType == ConnectivityManager.TYPE_ETHERNET
                        || activeType == SettingsApplication.TYPE_PPPOE) {
             imageView.setImageResource(R.drawable.lan_success);
        } else {
             imageView.setImageResource(R.drawable.network_error);
        }   
}
	
    /*
     * 0 缃戠粶閫?     * -999 缃戠粶涓嶉?
     * 512 缃戠粶瓒呮椂
     */
	private void setNetState() {
//		Log.i(TAG, "-----setNetState----");
		int activeType = Utils.getActiveNetworkType(this);
	    if(curPingStatus==0){
	    	if(activeType==ConnectivityManager.TYPE_WIFI){
	    		imageView.setImageResource(R.drawable.wifi_success);
	    	}else {
	    		imageView.setImageResource(R.drawable.lan_success);
			}
	    }else if(curPingStatus==-999){
	    }else {
	    	imageView.setImageResource(R.drawable.network_error);
		}
		
	}
	
	protected void onDestroy() {
   
		PingManager.unregCallBackListener(this);
		if (mNetworkChangeListener != null) {
			unregisterReceiver(mNetworkChangeListener);
			mNetworkChangeListener = null;
		}
		super.onDestroy();
	}

	
	@Override
	public void onPingChanged(int arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "receive msg:"+arg0);
	    curPingStatus = arg0;
		mHandler.sendEmptyMessage(NETWOEK_CHECK);
	}

 

	private void setAnimation() {
		Animation animation = AnimationUtils.loadAnimation(mContext,
				R.anim.setting_anim);
		Animation animation2 = AnimationUtils.loadAnimation(mContext,
				R.anim.setting_anim2);
		Animation animation3 = AnimationUtils.loadAnimation(mContext,
				R.anim.setting_anim3);
		Animation animation4 = AnimationUtils.loadAnimation(mContext,
				R.anim.setting_anim4);
		LayoutAnimationController lac = new LayoutAnimationController(animation);
		lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
		lac.setDelay(0.5f);

		LayoutAnimationController lac2 = new LayoutAnimationController(
				animation2);
		lac2.setOrder(LayoutAnimationController.ORDER_NORMAL);
		lac2.setDelay(0.5f);

		LayoutAnimationController lac3 = new LayoutAnimationController(
				animation3);
		lac3.setOrder(LayoutAnimationController.ORDER_NORMAL);
		lac3.setDelay(0.5f);

		LayoutAnimationController lac4 = new LayoutAnimationController(
				animation4);
		lac4.setOrder(LayoutAnimationController.ORDER_NORMAL);
		lac4.setDelay(0.5f);

		mMainLayout1.setLayoutAnimation(lac);
		mMainLayout2.setLayoutAnimation(lac2);
		mMainLayout3.setLayoutAnimation(lac3);
		mMainLayout4.setLayoutAnimation(lac4);
		animation2.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation paramAnimation) {
				// TODO Auto-generated method stub
			}

			@SuppressLint("NewApi")
			@Override
			public void onAnimationEnd(Animation paramAnimation) {
				// TODO Auto-generated method stub
				Log.i("BANGBANG", "-----------FUCK YOU");
				//mMainLayout1.requestFocus();
				setIN(true);
				mMainLayout1.setBackground(mContext.getResources().getDrawable(
						R.drawable.cycle_style));
				mMainLayout2.setBackground(mContext.getResources().getDrawable(
						R.drawable.cycle_style));
				mMainLayout3.setBackground(mContext.getResources().getDrawable(
						R.drawable.cycle_style));
				mMainLayout4.setBackground(mContext.getResources().getDrawable(
						R.drawable.cycle_style));
			}

			@Override
			public void onAnimationRepeat(Animation paramAnimation) {
				// TODO Auto-generated method stub

			}

		});
	}

	
	 

	private void activeActivity() {
		imageView = (ImageView) findViewById(R.id.network_image);
		mMainLayout1 = (RelativeLayout) findViewById(R.id.layout1);
		mMainLayout2 = (RelativeLayout) findViewById(R.id.layout2);
		mMainLayout3 = (RelativeLayout) findViewById(R.id.layout3);
		mMainLayout4 = (RelativeLayout) findViewById(R.id.layout4);
		about_image = (ImageView)findViewById(R.id.about_image);
		about_image.setBackgroundResource(R.drawable.overseas_about_domy);
		
		// 鍏充簬鏈満
		mMainLayout4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				startActivity(new Intent().setClass(mContext,
						DeviceinfoActivity.class));

			}
		});

		mMainLayout3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent().setClass(mContext,
						CommonActivity.class));
			}
		});
		mMainLayout2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent().setClass(mContext,
						NetworkSettingsActivity.class);
				intent.putExtra("isNativeGo", true);
				startActivity(intent);	
			}
		});
		mMainLayout1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent().setClass(mContext,VideoImageActivity.class));
			}
		});
	}
	
	
	private void registerNetworkListener() {
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		if (mNetworkChangeListener == null) {
			mNetworkChangeListener = new BroadcastReceiver() {

				@Override
				public void onReceive(Context arg0, Intent arg1) {
					ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
					if (networkInfo != null && networkInfo.isConnected()
							&& networkInfo.isAvailable()) {
//						 initNetState(true);
					} else {
						initNetState();
					}
				}
			};
		}
		registerReceiver(mNetworkChangeListener, filter);
	}
	
	 
}
