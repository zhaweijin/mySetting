package com.hiveview.tv.view;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hiveview.manager.PingListener;
import com.hiveview.manager.PingManager;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.LockNetReceiver;

@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class AuxiliaryNetworkView extends RelativeLayout implements PingListener {
	protected static final String TAG = "AuxiliaryNetworkView";
	protected static final int MSG_REFRESH_WIFI_RSSI = 4001;
	protected static final int MSG_PING_WIFI = 4002;
	protected static final int MSG_PING_LOCATION = 4003;
	protected static final int MSG_NET_STATUS_CHANGED = 4004;
	protected static final int MSG_CHECK_NETWORK_STATUS = 4005;
	protected static final int MSG_CHECK_NETWORK_STATUS_FINISHED = 4006;

	public static final String NET_STATUS_NOTIFY_ACTION = "com.hiveview.tv.NET_STATUS_NOTIFY_ACTION";
	public static final String CONNECTION_STATUS_ACTION = "com.hiveview.tv.ACTION_INTERNET_CONNECTION_STATUS";
	public static final String CONNECTION_STATUS = "connection_status";
	public static final int CHECK_STATUS_DELAY_TIME = 20000;
	public static final int PPPOE_STATE_EABLED = 100001;
	public static final int PPPOE_STATE_DISABLED = 100002;
	public static final int WIFI_STATE_EABLED = 100003;
	public static final int WIFI_STATE_DISABLED = 100004;
	public static final int ETHERNET_STATE_EABLED = 100005;
	public static final int ETHERNET_STATE_DISABLED = 100006;
	public static final int DEFAULT_STATE = 110000;

	private Context mContext;
	private ImageView ivConnectStatus;

	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
	private WifiManager wifiService;
	private WifiInfo wifiInfo;
	private PingTask mPingTask = null;
	private int mCurrentNetworkStatus = 0;
	private int mCurrentWifiLevel = 0;
	// launcher 自己的ping动作是否开启
	private static boolean isPingStart = false;
	private Handler mNetStatusHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.i(TAG, "handleMessage-->start");
			Log.i(TAG, "handleMessage-->msg.what:" + msg.what);
			// 多添加一重变量判断
			if (MSG_CHECK_NETWORK_STATUS == msg.what && isPingStart) {
				checkNetworkavAilable();
			}
			if (MSG_CHECK_NETWORK_STATUS_FINISHED == msg.what) {
				setNetStatusShow(mCurrentNetworkStatus, msg.arg1);
				Message InvokeNetworkCheckMsg = Message.obtain();
				InvokeNetworkCheckMsg.what = MSG_CHECK_NETWORK_STATUS;
				mNetStatusHandler.sendMessageDelayed(InvokeNetworkCheckMsg, CHECK_STATUS_DELAY_TIME);
				invalidate();
			} else {

			}
			Log.i(TAG, "handleMessage-->end");
		}
	};


	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// checkNetwork();
			Log.i(TAG, "onReceive-->start");
			if (CONNECTION_STATUS_ACTION.equals(intent.getAction())) {
				Log.i(TAG, "onReceive-->com.hiveview.tv.ACTION_INTERNET_CONNECTION_STATUS");
				Bundle extras = intent.getExtras();
				if (extras != null) {
					mCurrentNetworkStatus = extras.getInt(CONNECTION_STATUS);
					Log.i(TAG, "onReceive-->mCurrentNetworkStatus:" + mCurrentNetworkStatus);
				} else {
					Log.e(TAG, "onReceive-->should not be here:extras == null");
				}
			}
			// 接收rom pingservice 发送的广播
			if (LockNetReceiver.NET_WORK_STATUS_CHANGCE.equals(intent.getAction())) {
				Log.i(TAG, "onReceive-->start::" + mPingTask);
				// 如果接受到rom的ping,就停止launher自己ping动作
				if (isPingStart && null != mPingTask) {
					mPingTask.cancel(true);
					isPingStart = false;
					Log.i(TAG, "onReceive-->mPingTask:" + mPingTask.isCancelled());
				}
				// 接受rom广播中带的参数
				Log.i(TAG, "receive ping task......");
				Bundle b = intent.getBundleExtra("ping");
				// 设置默认值 -3
				int ret = b.getInt("status", -3);
				boolean isResult = ret == 0 ? true : false;
				setNetStatus(isResult);
				Log.i(TAG, "onReceive-->end");
			}
		}
	};

	public AuxiliaryNetworkView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public AuxiliaryNetworkView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public AuxiliaryNetworkView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	private void init() {
		// long time = System.currentTimeMillis();
		ivConnectStatus = new ImageView(getContext());
		// author: 张鹏展 盒子ping 方案 start
		/*
		 * int pingstatus = PingManager.getPingStatus();
		 * Log.e(TAG,"ping--get----KKkkk---state = " + pingstatus);
		 */
		PingManager.regCallBackListener(this);
		// end
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);

		this.addView(ivConnectStatus, params);

		wifiService = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

		/* 注册广播 */
		checkNetworkavAilable();

		setBroadCast();
		// Log.d(TAG, "loadImage AuxiliaryNetworkView::init  " +
		// (System.currentTimeMillis() - time));
	}

	private void setBroadCast() {
		// 生成广播处理
		// 实例化过滤器并设置要过滤的广播
		/*
		 * IntentFilter intentFilter = new
		 * IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
		 * intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); //注册广播
		 * mContext.registerReceiver(mReceiver, intentFilter);
		 */
		try {
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(CONNECTION_STATUS_ACTION);
			intentFilter.addAction(LockNetReceiver.NET_WORK_STATUS_CHANGCE);
			// 注册广播
			mContext.registerReceiver(mReceiver, intentFilter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		setUnregister();
		super.finalize();
	}

	public void setUnregister() {
		// author start zhangpengzhan
		PingManager.unregCallBackListener(this);
		// end
		mContext.unregisterReceiver(mReceiver);
	}

	private boolean pppoeConnectStatus() {

		ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo[] infos = connectivity.getAllNetworkInfo();

		for (NetworkInfo info : infos)

		{

			if (info.getTypeName().equals("pppoe") && info.getState().toString().equals("CONNECTED"))

			{

				return true;

			}

		}

		return false;

	}

	/** 未连接网络 */
	private void unConnect() {
		ivConnectStatus.setImageResource(R.drawable.network_unconnected);
	}

	/**
	 * 设置wifi连接 按照产品定义，屏蔽ping功能
	 * */
	private void wifiConnect(Boolean connect, boolean statusChanged) {
		connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (null == connectivityManager) {
			return;
		}
		info = connectivityManager.getActiveNetworkInfo();
		wifiInfo = wifiService.getConnectionInfo();

		if (null == info || null == wifiInfo) {
			return;
		}
		int rssi = wifiInfo.getRssi();

		if (connect) {
			int level = WifiManager.calculateSignalLevel(rssi, 4);
			if (mCurrentWifiLevel != level || statusChanged) {
				ivConnectStatus.setImageResource(getResByRssi(rssi, level));
				mCurrentWifiLevel = level;
			}
		} else {
			// 不可用
			ivConnectStatus.setImageResource(R.drawable.wifi_connect_abnormal);
		}

	}

	/** 设置本地连接 */
	private void locationConnect(Boolean connect) {
		Log.i(TAG, "locationConnect-->START");
		Log.i(TAG, "locationConnect-->connect:" + connect);

		if (connect) {
			ivConnectStatus.setImageResource(R.drawable.location_connected);
		} else {
			// 不可用
			ivConnectStatus.setImageResource(R.drawable.location_connected_abnormal);
		}
		Log.i(TAG, "locationConnect-->END");
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	// -100 -55
	private int getResByRssi(int rssi, int level) {
		int drawableId = 0;
		switch (level) {
		case 0:
			drawableId = R.drawable.wifi_signal_1;
			break;
		case 1:
			drawableId = R.drawable.wifi_signal_2;
			break;
		case 2:
			drawableId = R.drawable.wifi_signal_3;
			break;
		case 3:
			drawableId = R.drawable.wifi_signal_4;
			break;
		default:
			drawableId = R.drawable.wifi_signal_3;
			break;
		}
		return drawableId;
	}

	/** 检查网络可用 */
	private void checkNetworkavAilable() {
		if (mPingTask != null) {
			mPingTask.cancel(true);
		}

		mPingTask = new PingTask();
		mPingTask.execute("");

	}

	class PingTask extends AsyncTask<String, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			return ping();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			setNetStatus(result);
		}
	}

	/**
	 * @Title: AuxiliaryNetworkView
	 * @author:张鹏展
	 * @Description: 发送网络状态的消息
	 * @param isResult
	 */
	private void setNetStatus(boolean isResult) {
		Message msg = Message.obtain();
		msg.what = MSG_CHECK_NETWORK_STATUS_FINISHED;
		// true 标示外网是通畅的, false把标示外网不同
		if (isResult) {
			Log.d(TAG, "onPostExecute-->result:" + 1);
			msg.arg1 = 1;
			mNetStatusHandler.sendMessage(msg);
		} else {
			Log.d(TAG, "onPostExecute-->result:" + -1);
			msg.arg1 = -1;
			mNetStatusHandler.sendMessage(msg);
		}
	}

	private boolean ping() {
		Log.d(TAG, "ping()==start");
		boolean result = false;
		isPingStart = true;
		try {
			/*
			 * 鉴权 将探测地址修改为域名防止个别机器当机造成异常
			 */
			java.lang.Process p = Runtime.getRuntime().exec(AppConstant.ISDOMESTIC?"ping -c 2 -s 100 -w 3 api.pthv.gitv.tv":"ping -c 2 -s 100 -w 3 pic.usa.domybox.com");
			int status = p.waitFor();
			if (status == 0) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	private void setNetStatusShow(int netWorkStatus, int checkResult) {
		switch (netWorkStatus) {
		case PPPOE_STATE_EABLED:
		case ETHERNET_STATE_EABLED:
		case PPPOE_STATE_DISABLED:
		case ETHERNET_STATE_DISABLED:
			if (1 == checkResult) {
				HiveviewApplication.setNetStatus(ETHERNET_STATE_EABLED);
				locationConnect(true);
			} else {
				HiveviewApplication.setNetStatus(ETHERNET_STATE_DISABLED);
				locationConnect(false);
			}
			break;
		case WIFI_STATE_EABLED:
		case WIFI_STATE_DISABLED:
			if (1 == checkResult) {
				HiveviewApplication.setNetStatus(WIFI_STATE_EABLED);
				wifiConnect(true, true);
			} else {
				HiveviewApplication.setNetStatus(WIFI_STATE_DISABLED);
				wifiConnect(false, true);
			}
			break;
		default:
			if (1 == checkResult) {
				HiveviewApplication.setNetStatus(ETHERNET_STATE_EABLED);
				locationConnect(true);
				Log.e(TAG, "setNetStatusShow-->should not be here");
			} else {
				HiveviewApplication.setNetStatus(ETHERNET_STATE_DISABLED);
				locationConnect(false);
				unConnect();
			}
			// HiveviewApplication.setNetStatus(DEFAULT_STATE);

			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hiveview.manager.PingListener#onPingChanged(int)
	 */
	public void onPingChanged(int arg0) {
		// 接收rom 回调方法
		Log.i(TAG, "onReceive-->start::" + mPingTask);
		// 如果接受到rom的ping,就停止launcher自己ping动作
		if (isPingStart && null != mPingTask) {
			mPingTask.cancel(true);
			isPingStart = false;
			Log.i(TAG, "onReceive-->mPingTask:" + mPingTask.isCancelled());
		}
		// 接受rom 监听回调参数
		int ret = arg0;
		boolean isResult = ret == 0 ? true : false;
		setNetStatus(isResult);
		Log.i(TAG, "onReceive-->end");
	}

}
