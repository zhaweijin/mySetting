package com.hiveview.tv.hdmiin;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.OverlayView;
import android.widget.RelativeLayout;

import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;

public class OverlayViewHandler implements BaseTvPlayHandlerInterface {
	private static final String TAG = "OverlayViewHandler";

	/**
	 * cvteROM相关常量定义
	 */
	private static final String OUTPUT_MODE_PATH = "/sys/class/display/mode";
	private static final String FB0_PATH = "/sys/class/graphics/fb0/request2XScale";
	private static final String DISABLE_VIDEO_PATH = "/sys/class/video/disable_video";

	/**
	 * 分辨率相关配置常量定义
	 */
	private static final String[] MODES = { "1080i", "1080p", "720p", "480i", "480p", "480cvbs", "576i", "576p", "576cvbs" };
	private static final int[] WIDTH_DEFAULT = { 1920, 1920, 1280, 720, 720, 720, 720, 720, 720 };
	private static final int[] HEIGHT_DEFAULT = { 1080, 1080, 720, 480, 480, 480, 576, 576, 576 };
	private static final int NATIVE_WIDTH = 1280;
	private static final int NATIVE_HEIGHT = 720;

	private static final int MODE_720_X_formal = 265;
	private static final int MODE_720_Y_formal = 205;
	private static final int MODE_720_WIDTH_formal = 636;
	private static final int MODE_720_HEIGHT_formal = 358;
	private static final int MODE_1080_X_formal = 398;
	private static final int MODE_1080_Y_formal = 308;
	private static final int MODE_1080_WIDTH_formal = 636;
	private static final int MODE_1080_HEIGHT_formal = 358;

	private static final int MODE_720_X = 98;
	private static final int MODE_720_Y = 132;
	private static final int MODE_720_WIDTH = 828;
	private static final int MODE_720_HEIGHT = 466;
	private static final int MODE_1080_X = 149;
	private static final int MODE_1080_Y = 200;
	private static final int MODE_1080_WIDTH = 1255;
	private static final int MODE_1080_HEIGHT = 706;

	private static final int HDMI_IN_SHOW = 10001;
	private static final int MSG_TYPE_INIT_OVERLAY = 30001;

	/**
	 * 获取当前输出的分辨率模式
	 */
	private String outputmode = readFile(OUTPUT_MODE_PATH);
	/**
	 * 成员变量定义
	 */
	private Handler mAudioHandler = null;
	private Timer mAudioTimer = null;
	private OverlayView mOverlayView = null;
	private int mOverlayViewX = 0;
	private int mOverlayViewY = 0;
	private int mOverlayViewWidth = 0;
	private int mOverlayViewHeight = 0;
	private boolean mIsFullScreen = false;
	private Context context;
	private RelativeLayout re;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TYPE_INIT_OVERLAY:
				Viewinit();
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 
	 * @param context
	 * @return
	 */

	public OverlayViewHandler(FrameLayout rootView, Context context, boolean isFullScreen) {
		this.context = context;
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		Log.d(TAG, "OverlayViewHandler");
		mOverlayView = (OverlayView) new OverlayView(context);
		;
		rootView.addView(mOverlayView, params);
		re = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.black, null);
		rootView.addView(re, params);
		mIsFullScreen = isFullScreen;
	}

	private void setOverlayViewHeight(int height) {
		Log.d(TAG, "setOverlayViewHeight(): " + height);
		if (mOverlayView != null) {
			if (!outputmode.contains("480") && !outputmode.contains("576")) {
				Log.d(TAG, "setOverlayViewHeight(), mOverlayView.setBottom: " + (height + mOverlayView.getTop()));
				mOverlayView.setBottom(height + mOverlayView.getTop());
			}

			Log.d(TAG, "setOverlayViewHeight(), HDMI_IN_SHOW");
		}
	}

	// public void overlayViewInit() {
	// Log.i(TAG, "overlayViewInit-->start");
	// mHandler.sendEmptyMessage(MSG_TYPE_INIT_OVERLAY);
	// Log.i(TAG, "overlayViewInit-->end");
	//
	// }

	private void Viewinit() {
		Log.i(TAG, "Viewinit-->start");
		outputmode = readFile(OUTPUT_MODE_PATH);
		/* 获取小屏不通分辨率下的坐标和宽高 */
		if (outputmode.contains("1080")) {
			Log.i(TAG, "Viewinit-->pos 1");
			Log.i(TAG, "Viewinit-->outputmode.contains(1080)");
			// writeFile("/sys/class/graphics/fb0/request2XScale", "8");
			/* start by guosongsheng 美国测试版 */
			if (AppConstant.ISDOMESTIC) {
				mOverlayViewX = MODE_1080_X_formal;
				mOverlayViewY = MODE_1080_Y_formal;
				mOverlayViewWidth = MODE_1080_WIDTH_formal;
				mOverlayViewHeight = MODE_1080_HEIGHT_formal;
			} else {
				mOverlayViewX = MODE_1080_X;
				mOverlayViewY = MODE_1080_Y;
				mOverlayViewWidth = MODE_1080_WIDTH;
				mOverlayViewHeight = MODE_1080_HEIGHT;
			}
			/* end by guosongsheng */
		} else {
			Log.i(TAG, "Viewinit-->pos 2");
			Log.i(TAG, "Viewinit-->not outputmode.contains(1080)");
			// writeFile("/sys/class/graphics/fb0/request2XScale", "2");
			/* start by guosongsheng 美国测试版 */
			if (AppConstant.ISDOMESTIC) {
				mOverlayViewX = MODE_720_X_formal;
				mOverlayViewY = MODE_720_Y_formal;
				mOverlayViewWidth = MODE_720_WIDTH_formal;
				mOverlayViewHeight = MODE_720_HEIGHT_formal;
			} else {
				mOverlayViewX = MODE_720_X;
				mOverlayViewY = MODE_720_Y;
				mOverlayViewWidth = MODE_720_WIDTH;
				mOverlayViewHeight = MODE_720_HEIGHT;
			}
			/* end by guosongsheng */
		}

		/* 使能mOverlayView */
		Log.i(TAG, "Viewinit-->pos 3");
		if (mOverlayView != null) {
			Log.i(TAG, "Viewinit-->pos 4");
			if (!mOverlayView.isPowerOn() || !mOverlayView.isEnable()) {
				Log.i(TAG, "Viewinit-->pos 5");
				Log.d(TAG, "PLUGGED, init");
				mOverlayView.init();
				Log.d(TAG, "PLUGGED, startAudioHandleTimer");
				startAudioHandleTimer();
				mOverlayView.enableAudio(1);
				/* 设置宽高 */
				int width = mOverlayView.getHActive();
				int height = mOverlayView.getVActive();
				boolean is_dvi = mOverlayView.isDvi();
				boolean is_interlace = mOverlayView.isInterlace();
				if (width != 0 && height != 0) {
					Log.i(TAG, "overlayViewInit-->pos 6");
					int viewWidth = mOverlayView.getWidth();
					Log.d(TAG, "startTimer(), viewWidth: " + viewWidth);
					if (outputmode.contains("1080")) {
						int viewHeight = (is_interlace ? 2 : 1) * viewWidth * height / width;
						Log.d(TAG, "startTimer(), viewHeight: " + viewHeight);
						Log.i(TAG, "Viewinit-->pos 7");
						setOverlayViewHeight(viewHeight);
						// if (viewWidth != 0) {
						// if (width != 720) {
						// setOverlayViewHeight(viewHeight);
						// } else if (width == 720) {
						// setOverlayViewHeight(MODE_720_HEIGHT);
						// }
						// }
					} else {
						Log.i(TAG, "Viewinit-->pos 8");
						if (viewWidth != 0) {
							Log.i(TAG, "Viewinit-->pos 9");
							setOverlayViewHeight(mOverlayViewHeight);
						}
					}

				}
			}
		}

		/**
		 * 设置surface的位置与宽高
		 */
		int loc[] = new int[2];
		mOverlayView.getLocationOnScreen(loc);
		Log.i(TAG, "Viewinit-->pos 10");

		if (!mIsFullScreen && loc[0] == 0 && loc[1] == 0)
			return;
		Log.i(TAG, "Viewinit-->pos 11");
		int axis[] = new int[4];
		int defaultWH[] = new int[2];
		getUbootAxis(axis, defaultWH);
		int x = axis[0];
		int y = axis[1];
		int w = axis[2];
		int h = axis[3];
		Log.i(TAG, "Viewinit-->pos 12");
		Log.d(TAG, "loc: " + loc[0] + ", " + loc[1]);
		if (!mIsFullScreen) {
			x = axis[0] + (loc[0] * defaultWH[0] / NATIVE_WIDTH) * axis[2] / defaultWH[0];
			y = axis[1] + (loc[1] * defaultWH[1] / NATIVE_HEIGHT) * axis[3] / defaultWH[1];
			w = (mOverlayViewWidth * defaultWH[0] / NATIVE_WIDTH) * axis[2] / defaultWH[0];
			h = (mOverlayViewHeight * defaultWH[1] / NATIVE_HEIGHT) * axis[3] / defaultWH[1];

			Log.i(TAG, "Viewinit-->pos 13");
		}
		if (w != 0 && h != 0) {
			mOverlayView.displayPip(x, y, w, h);
			mOverlayView.invalidate();
		}
		re.setVisibility(View.GONE);

		Log.i(TAG, "Viewinit-->end");
	}

	/*
	 * public void ViewDeinit() {
	 * 
	 * re.setVisibility(View.VISIBLE);
	 * 
	 * 
	 * 
	 * }
	 */
	// public void overlayViewDeinit() {
	// Log.i(TAG, "overlayViewDeinit-->start");
	//
	// mHandler.removeMessages(MSG_TYPE_INIT_OVERLAY);
	// //writeFile("/sys/class/graphics/fb0/request2XScale", "2");
	// stopAudioHandleTimer();
	// if(mOverlayView != null) {
	// Log.i(TAG, "overlayViewDeinit-->pos 1");
	// if(mOverlayView.isPowerOn()) {
	// Log.i(TAG, "overlayViewDeinit-->pos 2");
	// Log.d(TAG, "UNPLUGGED, deinit");
	// mOverlayView.deinit();
	// }
	// }
	//
	// Log.i(TAG, "overlayViewDeinit-->end");
	//
	// }

	private void startAudioHandleTimer() {
		Log.i(TAG, "startAudioHandleTimer-->start");
		// if (mActivityValid == false) {
		// return;
		// }

		if (mAudioHandler == null) {
			mOverlayView.enableAudio(1);

			mAudioHandler = new Handler() {
				public void handleMessage(Message msg) {
					if (mOverlayView != null) {
						mOverlayView.handleAudio();
					}
					super.handleMessage(msg);
				}
			};
		}

		if (mAudioTimer == null) {
			mAudioTimer = new Timer();
			mAudioTimer.schedule(new TimerTask() {
				public void run() {
					mAudioHandler.sendEmptyMessage(0);
				}
			}, 0, 20);
		}
		Log.i(TAG, "startAudioHandleTimer-->end");
	}

	private void stopAudioHandleTimer() {
		//Log.i(TAG, "stopAudioHandleTimer-->start");
		if (mAudioTimer != null) {
			Log.i(TAG, "stopAudioHandleTimer-->will mAudioTimer.cancel()");
			mAudioTimer.cancel();
			mAudioTimer = null;
			mOverlayView.enableAudio(0);
		}
		if (mOverlayView != null) {
			mOverlayView.handleAudio();
		}
		//Log.i(TAG, "stopAudioHandleTimer-->end");
	}

	private void getUbootAxis(int[] axis, int[] defaultWH) {
		Log.i(TAG, "getUbootAxis-->start");
		String xEnv = "";
		String yEnv = "";
		String wEnv = "";
		String hEnv = "";
		int widthDefault = 0, heightDefault = 0;

		for (int i = 0; i < MODES.length; i++) {
			String mode = MODES[i];
			if (outputmode.contains(mode)) {
				xEnv = "ubootenv.var." + mode + "outputx";
				yEnv = "ubootenv.var." + mode + "outputy";
				wEnv = "ubootenv.var." + mode + "outputwidth";
				hEnv = "ubootenv.var." + mode + "outputheight";
				widthDefault = WIDTH_DEFAULT[i];
				heightDefault = HEIGHT_DEFAULT[i];
				break;
			}
		}
		Class<?> systemPropertiesClass = null;
		Method method = null;
		String methodName = "getInt";
		String className = "android.os.SystemProperties";
		try {
			systemPropertiesClass = Class.forName(className);
			method = systemPropertiesClass.getDeclaredMethod("getInt", String.class, int.class);
			method.setAccessible(true);
			try {
				Integer intObj = (Integer) method.invoke(systemPropertiesClass, xEnv, 0);
				axis[0] = intObj.intValue();
				intObj = (Integer) method.invoke(systemPropertiesClass, yEnv, 0);
				axis[1] = intObj.intValue();
				intObj = (Integer) method.invoke(systemPropertiesClass, wEnv, widthDefault);
				axis[2] = intObj.intValue();
				intObj = (Integer) method.invoke(systemPropertiesClass, hEnv, heightDefault);
				axis[3] = intObj.intValue();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG, e.toString());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG, e.toString());
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG, e.toString());
			}
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e(TAG, e1.toString());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e(TAG, e1.toString());
		}

		defaultWH[0] = widthDefault;
		defaultWH[1] = heightDefault;
		Log.d(TAG, "getUbootAxis(), axis: " + axis[0] + ", " + axis[1] + ", " + axis[2] + ", " + axis[3]);
		Log.d(TAG, "getUbootAxis(), defaultWH: " + defaultWH[0] + ", " + defaultWH[1]);
		Log.i(TAG, "getUbootAxis-->end");
	}

	private void getUbootAxis(int[] axis) {
		int defaultWH[] = new int[2];
		getUbootAxis(axis, defaultWH);
	}

	static int writeFile(String path, String val) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path), 64);
			try {
				writer.write(val);
			} finally {
				writer.close();
			}
			return 0;

		} catch (IOException e) {
			//Log.e(TAG, "IO Exception when write: " + path, e);
			return 1;
		}
	}

	static String readFile(String fileName) {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = 32;
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			res.trim();
			fin.close();
		} catch (Exception e) {
		}
		return res;
	}

	@Override
	public void tvPlaySchemeInit() {
		Log.i(TAG, "overlayViewInit-->start");
		mHandler.sendEmptyMessage(MSG_TYPE_INIT_OVERLAY);
		Log.i(TAG, "overlayViewInit-->end");
	}

	@Override
	public void tvPlaySchemeDeinit() {
	//	Log.i(TAG, "overlayViewDeinit-->start");

		mHandler.removeMessages(MSG_TYPE_INIT_OVERLAY);
		// writeFile("/sys/class/graphics/fb0/request2XScale", "2");
		stopAudioHandleTimer();
		if (mOverlayView != null) {
		//	Log.i(TAG, "overlayViewDeinit-->pos 1");
			if (mOverlayView.isPowerOn()) {
			//	Log.i(TAG, "overlayViewDeinit-->pos 2");
			//	Log.d(TAG, "UNPLUGGED, deinit");
				mOverlayView.deinit();
			}
		}

	//	Log.i(TAG, "overlayViewDeinit-->end");
	}

	@Override
	public void setUnregister() {
		// TODO Auto-generated method stub
		
	}
}
