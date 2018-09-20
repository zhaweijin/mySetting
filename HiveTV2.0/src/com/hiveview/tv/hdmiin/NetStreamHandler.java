package com.hiveview.tv.hdmiin;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.hiveview.tv.R;
import com.hiveview.tv.onlive.player.CommonControllerOverlay.ControllerCallback;
import com.hiveview.tv.onlive.player.MoviePlayer;

public class NetStreamHandler implements ControllerCallback, BaseTvPlayHandlerInterface{

	private static final String TAG = "NetStreamHandler";
	private static final String STREAM_URL = "http://218.249.165.21/iphone/cctv5+.m3u8";
	
	/**
	 * 分辨率相关配置常量定义
	 */
	private static final int MODE_720_X = 265;
	private static final int MODE_720_Y = 205;
	private static final int MODE_720_WIDTH = 636;
	private static final int MODE_720_HEIGHT = 358;
	private static final int MODE_1080_X = 398;
	private static final int MODE_1080_Y = 308;
	private static final int MODE_1080_WIDTH = 955;
	private static final int MODE_1080_HEIGHT = 537;
	
	/**
	 * 成员变量定义
	 */
	private int mSurfaceViewX = 0;
	private int mSurfaceViewY = 0;
	private int mSurfaceViewWidth = 0;
	private int mSurfaceViewHeight = 0;

	Context mContext = null;
	boolean mIsFullScreen = false;
	private int mDisplayMode = 1; /*1:720,2:1080*/
//	SurfaceView mSurfaceView = null;
	FrameLayout mRootView = null;
	private MoviePlayer mPlayer;
	

	public NetStreamHandler(FrameLayout rootView, Context context, boolean isFullScreen) {
		mContext = context;
		mRootView = rootView;
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.view_onlive_netstream, null);
		
		rootView.addView(view, params);
		mIsFullScreen = isFullScreen;
		initLocSize();
	}
	
	private void initLocSize() {
		if (1 == mDisplayMode) {
			if (mIsFullScreen) {
				mSurfaceViewX = 0;
				mSurfaceViewY = 0;
				mSurfaceViewWidth = 1280;
				mSurfaceViewHeight = 720;
			} else {
				mSurfaceViewX = MODE_720_X;
				mSurfaceViewY = MODE_720_Y;
				mSurfaceViewWidth = MODE_720_WIDTH;
				mSurfaceViewHeight = MODE_720_HEIGHT;
			}
		}else if (2 == mDisplayMode){
			if (mIsFullScreen) {
				mSurfaceViewX = 0;
				mSurfaceViewY = 0;
				mSurfaceViewWidth = 1920;
				mSurfaceViewHeight = 1080;
			} else {
				mSurfaceViewX = MODE_1080_X;
				mSurfaceViewY = MODE_1080_Y;
				mSurfaceViewWidth = MODE_1080_WIDTH;
				mSurfaceViewHeight = MODE_1080_HEIGHT;
			}
		}
	}
	public void netStreamInit() {
		Log.d(TAG, "NetStreamInit-->start");
		mPlayer = createPlayer(mRootView, mContext,Uri.parse(STREAM_URL),/*mFinishOnCompletion*/true);
    	if (mPlayer != null) {
    		Log.d(TAG, "onResume-->mPlayer!= null");
    		mPlayer.onResume();
    	}
		if(mIsFullScreen){
//			nelt.setFullscale();
		}else{
//			nelt.setWindow(mSurfaceViewWidth, mSurfaceViewHeight, mSurfaceViewX, mSurfaceViewY);
		}
		Log.d(TAG, "NetStreamInit-->end");
	}
	public void netStreamDeinit() {
    	Log.d(TAG, "NetStreamDeinit-->start");
		if (mPlayer != null) {
    		mPlayer.onDestroy();
    	}
        Log.d(TAG, "NetStreamDeinit-->start");
	}
	
    private MoviePlayer createPlayer(FrameLayout rootView, Context context, Uri uri, boolean finishOnCompletion) {
    	if (mPlayer != null) {
    		mPlayer.onDestroy(); 
        	mPlayer = null;
    	}
        mPlayer = new MoviePlayer(rootView, context, uri, this, finishOnCompletion) {
            @Override
            public void onCompletion() {
                if (/*mFinishOnCompletion*/true) {
//                    finish();
                }
            }
        };
        return mPlayer;
    }

	@Override
	public void OnErrDialogEnsure() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnErrDialogCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tvPlaySchemeInit() {
		Log.d(TAG, "NetStreamInit-->start");
		mPlayer = createPlayer(mRootView, mContext,
				Uri.parse(STREAM_URL),/*mFinishOnCompletion*/true);
    	if (mPlayer != null) {
    		Log.d(TAG, "onResume-->mPlayer!= null");
    		mPlayer.onResume();
    	}
		if(mIsFullScreen){
//			nelt.setFullscale();
		}else{
//			nelt.setWindow(mSurfaceViewWidth, mSurfaceViewHeight, mSurfaceViewX, mSurfaceViewY);
		}
		Log.d(TAG, "NetStreamInit-->end");
		
	}

	@Override
	public void tvPlaySchemeDeinit() {
		Log.d(TAG, "NetStreamDeinit-->start");
		if (mPlayer != null) {
    		mPlayer.onDestroy();
    	}
        Log.d(TAG, "NetStreamDeinit-->start");
	}

	@Override
	public void setUnregister() {
		// TODO Auto-generated method stub
		
	}
}
                    