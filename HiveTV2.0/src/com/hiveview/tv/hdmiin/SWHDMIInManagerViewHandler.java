package com.hiveview.tv.hdmiin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.hiveview.display.SWHDMIInManager;
import com.hiveview.display.SWHDMIInManagerImpl;
import com.hiveview.tv.onlive.player.MoviePlayer;
import com.hiveview.tv.service.RemotecontrolReceiver;
import com.hiveview.tv.view.HiveviewHdmiInView;

public class SWHDMIInManagerViewHandler implements BaseTvPlayHandlerInterface {
	private static final int MSG_TYPE_INIT_OVERLAY = 30001;
	private Context mContext = null;
	private static final String TAG = "SWHDMIInManagerViewHandler";

	boolean mIsSurfaceReady = false;

	private SurfaceView mSurfaceView = null;

	private SWHDMIInManager mManager;
	private boolean mIsInit = false;
	private boolean mIsStop = true;
	private boolean mIsFullScreen = false;
	/**
	 * 播放器
	 */
	private MoviePlayer  mMoviePlayer;
	/** 
	 * 插入或者拔出hdmin后发广播给电视页面，显示或者隐藏挡板
	 */
	public static final String SWHDMIIN="com.hiveview.tv.swhdmiin";
	/**
	 * @Fields mRxReceiver 广播接收器
	 */
	HDMIRxReceiver mRxReceiver;
	/**
	 * @Fields mFilter 要接收的广播信号
	 */
	IntentFilter mFilter;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TYPE_INIT_OVERLAY:
				HDMIinit();
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * @param @param rootView
	 * @param @param context
	 * @param @param isFullScreen
	 */
	public SWHDMIInManagerViewHandler(FrameLayout rootView, Context context, boolean isFullScreen) {
		mContext = context;
		mIsFullScreen = isFullScreen;
		mManager = SWHDMIInManagerImpl.getInstance();
		// 设置Hdmin连接状态
		HiveviewHdmiInView.isSignalable = mManager.isSignalable();
		Log.i(TAG, "SWHDMIInManagerViewHandler   -----------------------> HiveviewHdmiInView.isSignalable  : " + HiveviewHdmiInView.isSignalable);
		ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mSurfaceView = (SurfaceView) new SurfaceView(mContext);
		if (isFullScreen) {
			mSurfaceView.getHolder().setFixedSize(1280, 720);
		} else {
			mSurfaceView.getHolder().setFixedSize(636, 358);
		}
		mSurfaceView.getHolder().addCallback(new HDMIInSurfaceCallback());
		rootView.addView(mSurfaceView, params);
		// 注册hdmi相关逻辑处理的广播
		initReceiver();
	}

	private void HDMIinit() {
		mIsInit = true;
		mSurfaceView.setVisibility(View.VISIBLE);
		Log.i(TAG, "tvPlaySchemeInit-->.........................mIsFullScreen = " + mIsFullScreen + " mIsSurfaceReady = " + mIsSurfaceReady
				+ " mIsStop = " + mIsStop);
		if (mManager != null && mIsSurfaceReady && mManager.isSignalable()) {
			if (mIsStop) {
				mManager.init(mContext, mSurfaceView);
				mManager.start();
				mIsStop = !mIsStop;
			}
		}
	}

	/**
	 * 开始播放
	 * 
	 * @Title: SWHDMIInManagerViewHandler
	 * @author:郭松胜
	 * @Description: TODO
	 */
	@Override
	public void tvPlaySchemeInit() {
		// Log.i(TAG,
		// "tvPlaySchemeInit-------------------------------------------->start");
		mHandler.sendEmptyMessage(MSG_TYPE_INIT_OVERLAY);
		// Log.i(TAG,
		// "tvPlaySchemeInit-------------------------------------------->end");
	}

	/**
	 * 停止播放
	 * 
	 * @Title: SWHDMIInManagerViewHandler
	 * @author:郭松胜
	 * @Description: TODO
	 */
	@Override
	public void tvPlaySchemeDeinit() {
		Log.i(TAG, "tvPlaySchemeDeinit-->.........................mIsFullScreen = " + mIsFullScreen + " mIsSurfaceReady = " + mIsSurfaceReady
				+ " mIsStop = " + mIsStop);
		if (mManager != null && mIsSurfaceReady) {
			if (!mIsStop) {
				// Log.i(TAG, "tvPlaySchemeDeinit........................stop");
				mSurfaceView.setVisibility(View.INVISIBLE);
				mManager.stop();
				mManager.realese();
				mIsStop = !mIsStop;
			}
		}
		mIsInit = false;
		// Log.i(TAG, "tvPlaySchemeDeinit........................end");
	}

	class HDMIInSurfaceCallback implements SurfaceHolder.Callback {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// Log.i(TAG,
			// "surfaceCreated---------------------------------------------------->start");
			if (mIsInit) {
				mHandler.sendEmptyMessage(MSG_TYPE_INIT_OVERLAY);
			}
			mIsSurfaceReady = true;
			// Log.i(TAG,
			// "surfaceCreated---------------------------------------------------->end");
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			// Log.i(TAG,
			// "surfaceChanged-->.........................mIsFullScreen = "+mIsFullScreen
			// + " mIsSurfaceReady = "+mIsSurfaceReady + " mIsStop = "+mIsStop);
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// Log.i(TAG,
			// "surfaceDestroyed--------------------------------------------------->");
			mIsSurfaceReady = false;
		}
	}

	/**
	 * @Title: SWHDMIInManagerViewHandler
	 * @author:张鹏展
	 * @Description: 初始化广播接收器
	 */
	private void initReceiver() {
		mRxReceiver = new HDMIRxReceiver();
		mFilter = new IntentFilter();
		mFilter.addAction(SWHDMIInManager.INPUT_SOURCE_ACION);
		mContext.registerReceiver(mRxReceiver, mFilter);
		Log.d(TAG, "initReceiver==>init::");
	}

	/**
	 * @Title: SWHDMIInManagerViewHandler
	 * @author:张鹏展
	 * @Description: 解绑注册的广播接收器
	 */
	public void setUnregister() {
		if (null != mRxReceiver) {
			mContext.unregisterReceiver(mRxReceiver);
		}
	}

	/**
	 * @ClassName: HDMIRxReceiver
	 * @Description: hdmi in 的广播接收器
	 * @author: zhangpengzhan
	 * @date 2014年11月22日 上午11:16:53
	 * 
	 */
	class HDMIRxReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int plugged = intent.getIntExtra(SWHDMIInManager.EXTRA_PLUG_INFO, 1);
			Log.d(TAG, "HDMIRxReceiver==>plugged::" + plugged);
			if (plugged == 1 && mIsInit) {
				mSurfaceView.setVisibility(View.VISIBLE);
				Log.i(TAG, "tvPlaySchemeInit-->.........................mIsFullScreen = " + mIsFullScreen + " mIsSurfaceReady = " + mIsSurfaceReady
						+ " mIsStop = " + mIsStop);
				if (mManager != null && mIsSurfaceReady && mManager.isSignalable()) {
					if (mIsStop) {
						mManager.init(mContext, mSurfaceView);
						mManager.start();
						mIsStop = !mIsStop;
					}
				} 
			}else {
				if (mManager != null && mIsSurfaceReady && mIsInit) {
					if (!mIsStop) {
						 Log.i(TAG,"tvPlaySchemeDeinit........................stop");
						mSurfaceView.setVisibility(View.INVISIBLE);
						mManager.stop();
						mManager.realese();
						mIsStop = !mIsStop;
					}
				}
			}
			Intent SWIntent = new Intent();
			SWIntent.setAction(SWHDMIIN);
			// 监听Hdmin接入,拔出事件 设置时候有Hdmin的标志位
			if (plugged == 1) {
				// 设置接入Hdmin标志位
				HiveviewHdmiInView.isSignalable = true;
				SWIntent.putExtra("isSignalable", "true");
				Log.i(TAG, "SWHDMIInManagerViewHandler   -----------------------> HDMIRxReceiver : " + HiveviewHdmiInView.isSignalable);
			} else if (plugged == 0) {
				// 设置拔出Hdmin标志位
				HiveviewHdmiInView.isSignalable = false;
				SWIntent.putExtra("isSignalable", "false");
				Log.i(TAG, "SWHDMIInManagerViewHandler   -----------------------> HDMIRxReceiver : " + HiveviewHdmiInView.isSignalable);
			}
			context.sendBroadcast(SWIntent);
		}
	}
}
