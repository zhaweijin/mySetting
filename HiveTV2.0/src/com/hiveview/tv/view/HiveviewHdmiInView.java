package com.hiveview.tv.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;

import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.hdmiin.BaseTvPlayHandlerInterface;
import com.hiveview.tv.hdmiin.InvokeTVHandler;
import com.hiveview.tv.hdmiin.NetStreamHandler;
import com.hiveview.tv.hdmiin.OverlayViewHandler;
import com.hiveview.tv.hdmiin.SWHDMIInManagerViewHandler;
import com.hiveview.tv.utils.SwitchChannelUtils;

public class HiveviewHdmiInView extends FrameLayout {
	private static final String TAG = "HiveviewHdmiInView";
	public static final String ACTION_SMALL_SCREEN_SHOW = "com.hiveview.tv.view.hdmiin.small_show";
	public static final String ACTION_SMALL_SCREEN_UNSHOW = "com.hiveview.tv.view.hdmiin.small_unshow";
	public static final String ACTION_SMALL_SCREEN_PAUSE = "com.hiveview.tv.view.hdmiin.small_pause";
	public static final String ACTION_SMALL_SCREEN_RESUME = "com.hiveview.tv.view.hdmiin.small_resume";
	public static final String ACTION_SMALL_SCREEN_SWITCH = "com.hiveview.tv.view.hdmiin.small_switch";
	public static final String VIDEO_DISABLE = "1";
	private static boolean switchsizer=false;
	/**
	 * @Fields isSignalable:TODO 是否有接入Hdmin 目前只有2.0+有提供接口
	 */
	public static boolean isSignalable = true;

	private Context mContext;
	private SurfaceHolder mOverlayViewSurfaceHolder = null;
	private boolean mActivityValid = false;
	private boolean mSwitchToTV = false;
	private BaseTvPlayHandlerInterface handlerInterface = null;
//	private InvokeTVHandler mInvokeTVHandler = null;
//	private NetStreamHandler mNetStreamHandler = null;
	/**
	 * cvte Hdmin
	 */
	public static boolean mCVTE = true;
	/**
	 * Invoke Hdmin
	 */
	public static boolean mInvoke = false;
	/**
	 * net Hdmin
	 */
	public static boolean mNetStream = false;
	/**
	 *rtk Hdmin
	 */
	public static boolean mRtkHdminRx = false;
	

	
	// private Button btnFullScreem;
	public static boolean getSwitchsizer() {
		return switchsizer;
	}

	public static void setSwitchsizer(boolean switchsizer) {
		HiveviewHdmiInView.switchsizer = switchsizer;
	}


	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
		//	Log.i(TAG, "onReceive-->start");
			if (ACTION_SMALL_SCREEN_SHOW.equals(intent.getAction())) {
			//	Log.i(TAG," onReceive-->ACTION_SMALL_SCREEN_SHOW.equals === (intent.getAction())");
				/*start by guosongsheng 美国测试版不需要*/
				if(AppConstant.ISDOMESTIC){ 
//					SwitchChannelUtils.initSTBChannel(context);  //去掉第一次换台,直接显示hdmin
				}
				/*end by guosongsheng*/
				if (mCVTE) {
					Log.i(TAG," onReceive-->ACTION_SMALL_SCREEN_SHOW.equals === (intent.getAction())");
					handlerInterface.tvPlaySchemeInit();
				}
				if (mInvoke) {
					Log.i(TAG," onReceive-->ACTION_SMALL_SCREEN_SHOW.equals === (intent.getAction())");
					handlerInterface.tvPlaySchemeInit();
				}
				if(mNetStream) {
					Log.i(TAG," onReceive-->ACTION_SMALL_SCREEN_SHOW.equals === (intent.getAction())");
					handlerInterface.tvPlaySchemeInit();
				}
				if(mRtkHdminRx) {
					Log.i("SWHDMIInManagerViewHandler"," onReceive ..................tvPlaySchemeInit1");
					handlerInterface.tvPlaySchemeInit();
				}
			} else if (ACTION_SMALL_SCREEN_UNSHOW.equals(intent.getAction())) {
				if (mCVTE) {
				//	Log.i(TAG," onReceive-->ACTION_SMALL_SCREEN_SHOW.equals === (intent.getAction())");
					handlerInterface.tvPlaySchemeDeinit();
				}
				if (mInvoke) {
				//	Log.i(TAG," onReceive-->ACTION_SMALL_SCREEN_SHOW.equals === (intent.getAction())");
					handlerInterface.tvPlaySchemeDeinit();
				}
				if(mNetStream) {
			//		Log.i(TAG," onReceive-->ACTION_SMALL_SCREEN_SHOW.equals === (intent.getAction())");
					handlerInterface.tvPlaySchemeDeinit();
				}
				if(mRtkHdminRx) {
			//		Log.i("SWHDMIInManagerViewHandler"," onReceive ..................tvPlaySchemeInit2");
					handlerInterface.tvPlaySchemeDeinit();
				}
		//		Log.i(TAG,"onReceive-->ACTION_SMALL_SCREEN_UNSHOW.equals(intent.getAction())");
						

			} else if (ACTION_SMALL_SCREEN_RESUME.equals(intent.getAction())) {
			//	Log.i(TAG,"onReceive-->ACTION_SMALL_SCREEN_RESTART.equals(intent.getAction())");
						

				if (mCVTE) {
				//	Log.i(TAG," onReceive-->ACTION_SMALL_SCREEN_SHOW.equals === (intent.getAction())");
					handlerInterface.tvPlaySchemeInit();
				}
				if (mInvoke) {
			//		Log.i(TAG," onReceive-->ACTION_SMALL_SCREEN_SHOW.equals === (intent.getAction())");
					handlerInterface.tvPlaySchemeInit();
				}
				if(mNetStream) {
				//	Log.i(TAG," onReceive-->ACTION_SMALL_SCREEN_SHOW.equals === (intent.getAction())");
					handlerInterface.tvPlaySchemeInit();
				}
				if(mRtkHdminRx) {
			//		Log.i("SWHDMIInManagerViewHandler"," onReceive ..................tvPlaySchemeInit3");
					handlerInterface.tvPlaySchemeInit();
				}

			} else if (ACTION_SMALL_SCREEN_PAUSE.equals(intent.getAction())) {

				if (!mSwitchToTV) {
					if (mCVTE) {
				//		Log.i(TAG," onReceive-->ACTION_SMALL_SCREEN_SHOW.equals === (intent.getAction())1");
						handlerInterface.tvPlaySchemeDeinit();
					}
				}
				if (mInvoke) {
				//	Log.i(TAG," onReceive-->ACTION_SMALL_SCREEN_SHOW.equals === (intent.getAction())2");
					handlerInterface.tvPlaySchemeDeinit();
				}
				if(mNetStream) {
				//	Log.i(TAG," onReceive-->ACTION_SMALL_SCREEN_SHOW.equals === (intent.getAction())3");
					handlerInterface.tvPlaySchemeDeinit();
				}
				if(mRtkHdminRx) {
			//		Log.i("SWHDMIInManagerViewHandler"," onReceive ..................tvPlaySchemeInit4");
					handlerInterface.tvPlaySchemeDeinit();
				}
			//	Log.i(TAG,"onReceive-->ACTION_SMALL_SCREEN_PAUSE.equals(intent.getAction())");
						
//				int currentViewIndex = intent.getIntExtra("currentViewIndex", 2);
//				if (1 == currentViewIndex) {
//					//writeFile(DISABLE_VIDEO_PATH, VIDEO_DISABLE);
//
//				} else {
//					writeFile(DISABLE_VIDEO_PATH, VIDEO_DISABLE);
//				}
//				if (mActivityValid == false) {
//					return;
//				}
//				stopAudioHandleTimer();
//				mActivityValid = false;
//				Log.i(TAG, "onReceive-->switchFlag:" + mSwitchToTV);
//				if (mOverlayView != null) {
////					if (mOverlayView.isPowerOn()) {
//					if (mOverlayView.isPowerOn() && currentViewIndex != 1) {
//						mOverlayView.deinit();
//						// mOverlayView.displayHdmi();
//					}
//				}
				mSwitchToTV = false;

			} else if (ACTION_SMALL_SCREEN_SWITCH.equals(intent.getAction())) {
				setSwitchsizer(true);
				//Log.i(TAG,"onReceive-->ACTION_SMALL_SCREEN_SWITCH.equals(intent.getAction())");
						
				mSwitchToTV = true;
			}
			//Log.i(TAG, "onReceive-->end");
		}
	};
	

	protected void registerReceiver() {
		Log.i(TAG, "registerReceiver-->start");
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_SMALL_SCREEN_SHOW);
		intentFilter.addAction(ACTION_SMALL_SCREEN_UNSHOW);
		intentFilter.addAction(ACTION_SMALL_SCREEN_RESUME);
		intentFilter.addAction(ACTION_SMALL_SCREEN_PAUSE);
		intentFilter.addAction(ACTION_SMALL_SCREEN_SWITCH);
		mContext.registerReceiver(mIntentReceiver, intentFilter);
		Log.i(TAG, "registerReceiver-->end");
	}

	public void setUnregister(){
		if (null != mIntentReceiver) {
			mContext.unregisterReceiver(mIntentReceiver);
		}
		if(null != handlerInterface){
			handlerInterface.setUnregister();
		}
		if (null != mIntentReceiver) {
			mContext.unregisterReceiver(mIntentReceiver);
		}
	}
	
	public HiveviewHdmiInView(Context context) {
		super(context);
		Log.i(TAG, "HiveviewHdmiInView-->start");
		mContext = context;
		init();
		Log.i(TAG, "HiveviewHdmiInView-->end");
	}

	public HiveviewHdmiInView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(TAG, "HiveviewHdmiInView-->start");
		mContext = context;
		init();
		Log.i(TAG, "HiveviewHdmiInView-->end");
	}

	private void init() {
		Log.i(TAG, "init-->start");
//
//		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT);
//
////		OverlayView overlayView = (OverlayView) OverlayViewHandler.createOverlayView(getContext());
////		this.addView(overlayView, params);
		
		if (mCVTE) {
			handlerInterface = new OverlayViewHandler(this, getContext(), false/*isFullScreen*/);
		}       
		if(mInvoke) {
			handlerInterface = new InvokeTVHandler(this, getContext(), false/* isFullScreen */);
		}
		if(mNetStream) {
			handlerInterface = new NetStreamHandler(this, getContext(), false/* isFullScreen */);
		}
		if(mRtkHdminRx) {
			handlerInterface = new SWHDMIInManagerViewHandler(this, getContext(), false/* isFullScreen */);
			Log.i(TAG, "new SWHDMIInManagerViewHandler-->.........................");
		}
			OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener();
		// this.getViewTreeObserver().addOnGlobalLayoutListener(listener);
		registerReceiver();
		Log.i(TAG, "init-->end");
	}

	class MyOnGlobalLayoutListener implements OnGlobalLayoutListener {

		@SuppressWarnings("deprecation")
		public void onGlobalLayout() {
			Log.i(TAG, "onGlobalLayout-->start");
			if (mActivityValid == true) {
				return;
			}
			mActivityValid = true;
			Log.i(TAG, "onGlobalLayou-->end");
			
		}
	}


	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		
	}
}
