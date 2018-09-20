package com.hiveview.cloudtv.settings;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class ControlActivity extends Activity {
	final int BT_AUTOPAIR_FAILED = 0;
	final int BT_AUTOPAIR_NOTPAIR = 1;
	final int BT_AUTOPAIR_PAIRING = 2;
	final int BT_AUTOPAIR_PAIRED = 3;
	final int BT_AUTOPAIR_CONNECTING = 4;
	final int BT_AUTOPAIR_CONNECTED = 5;
	final int  BT_AUTOPAIR_TIMEOUT = 6;
	final int BT_AUTOPAIR_CONNECT_OK = 7;
	final int BT_AUTOPAIR_NEED_CONNECT = 8;
	TextView mStatus;
	Context mContext = this;
	ImageView mCycle1;
	ImageView mCycle2;
	ImageView mHand;
	ImageView mYao;
	TextView mWarn1;
	TextView mWarn2;
	TextView and;
	ImageView img1;
	ImageView img2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		mStatus = (TextView) findViewById(R.id.status);
		mCycle1 = (ImageView) findViewById(R.id.cycle1);
		mCycle2 = (ImageView) findViewById(R.id.cycle2);
		mHand = (ImageView) findViewById(R.id.hand);
		mYao = (ImageView) findViewById(R.id.yaokongqi);
		mWarn1 = (TextView) findViewById(R.id.warning1);
		and=(TextView)findViewById(R.id.and);
		mWarn2=(TextView)findViewById(R.id.warning2);
		img1=(ImageView)findViewById(R.id.imageView1);
		img2=(ImageView)findViewById(R.id.imageView2);
		setFlickerAnimation(mCycle1);
		setFlickerAnimation(mCycle2);
		registerReceiver();
		 //startBLService();
	}

	private void setFlickerAnimation(ImageView iv_chat_head) {
		final Animation animation = new AlphaAnimation(1, 0); // Change alpha
																// from fully
																// visible to
																// invisible
		animation.setDuration(500); // duration - half a second
		animation.setInterpolator(new LinearInterpolator()); // do not alter
																// animation
																// rate
		animation.setRepeatCount(Animation.INFINITE); // Repeat animation
														// infinitely
		animation.setRepeatMode(Animation.REVERSE); //
		iv_chat_head.setAnimation(animation);
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
	protected void onDestroy() {
		Intent intent = new Intent();
		intent.setAction("com.hiveview.bluetooth.le.BtAutoPair");
		stopService(intent);
		this.unregisterReceiver(bluetoothReceiver);
		super.onDestroy();
	}

	private void registerReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.hiveview.bluetooth.le.btautopair.state");
		registerReceiver(bluetoothReceiver, intentFilter);
	}

	private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			int state = intent.getIntExtra("state", 0);
			switch (state) {
			case BT_AUTOPAIR_FAILED: {
				mStatus.setText(mContext.getResources().getString(R.string.bluetooth_error));
				break;
			}
			case BT_AUTOPAIR_NOTPAIR: {
				mStatus.setText(mContext.getResources().getString(R.string.find_yaokongqi));
				break;
			}
			case BT_AUTOPAIR_PAIRING: {
				mStatus.setText(mContext.getResources().getString(R.string.bluetooth_pairing));
				break;
			}
			case BT_AUTOPAIR_PAIRED: {
				mStatus.setText(mContext.getResources().getString(R.string.bluetooth_pair_success));
				break;
			}
			case BT_AUTOPAIR_CONNECTING: {
				mStatus.setText(mContext.getResources().getString(R.string.bluetooth_connecting));
				break;
			}
			case BT_AUTOPAIR_CONNECTED: {
				mStatus.setText(mContext.getResources().getString(
						R.string.bluetooth_connect_success));
				break;
			}
			case BT_AUTOPAIR_TIMEOUT:{
				mStatus.setText(mContext.getResources().getString(R.string.bluetooth_timeout));
				break;
			}
			case BT_AUTOPAIR_NEED_CONNECT: {
				mCycle1.setVisibility(View.VISIBLE);
				mCycle2.setVisibility(View.VISIBLE);
				mHand.setVisibility(View.VISIBLE);
				mYao.setVisibility(View.VISIBLE);

				mWarn1.setText(mContext.getResources().getString(R.string.bluetooth_warning1));
				and.setText(mContext.getResources().getString(R.string.bluetooth_warningand));
				mWarn2.setText(mContext.getResources().getString(R.string.bluetooth_warning2));
				and.setVisibility(View.VISIBLE);
				mWarn2.setVisibility(View.VISIBLE);
				img1.setVisibility(View.VISIBLE);
				img2.setVisibility(View.VISIBLE);

				break;
			}

			case BT_AUTOPAIR_CONNECT_OK: {
				mCycle1.setVisibility(View.INVISIBLE);
				mCycle2.setVisibility(View.INVISIBLE);
				mHand.setVisibility(View.INVISIBLE);
				mYao.setVisibility(View.INVISIBLE);
				mWarn1.setText(mContext.getResources().getString(R.string.yaokongqi_connect));
				and.setVisibility(View.GONE);
				mWarn2.setVisibility(View.GONE);
				img1.setVisibility(View.GONE);
				img2.setVisibility(View.GONE);
				break;
			}

			}
		}

	};
}
