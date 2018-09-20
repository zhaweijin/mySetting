package com.hiveview.tv.hdmiin;

import android.content.Context;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.neldtv.mstar.platform.NelTerminal;

public class InvokeTVHandler implements BaseTvPlayHandlerInterface{

	private static final String TAG = "InvokeTVHandler";
	
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
	SurfaceView mSurfaceView = null;
	
	private NelTerminal nelt=null;
	public InvokeTVHandler(FrameLayout rootView, Context context, boolean isFullScreen) {
		mContext = context;
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		mSurfaceView = (SurfaceView) new SurfaceView(mContext);
		nelt=new NelTerminal(mSurfaceView);
		rootView.addView(mSurfaceView, params);
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
	
	@Override
	public void tvPlaySchemeInit() {
		if(mIsFullScreen){
			nelt.setFullscale();
		}else{
			nelt.setWindow(mSurfaceViewWidth, mSurfaceViewHeight, mSurfaceViewX, mSurfaceViewY);
		}
		nelt.open();
	}

	@Override
	public void tvPlaySchemeDeinit() {
		nelt.close();
	}

	@Override
	public void setUnregister() {
		// TODO Auto-generated method stub
		
	}
}
