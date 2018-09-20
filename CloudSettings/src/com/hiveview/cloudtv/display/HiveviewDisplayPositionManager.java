package com.hiveview.cloudtv.display;

import android.content.Context;
import android.util.Log;
import com.droidlogic.app.SystemControlManager;
import com.droidlogic.app.OutputModeManager;

public class HiveviewDisplayPositionManager {
    private final static String TAG = "DisplayManager";
    private final static boolean DEBUG = false;
    private Context mContext = null;
    private SystemControlManager mSystenControl = null;
    SystemControlManager.DisplayInfo mDisplayInfo;
    private OutputModeManager mOutputModeManager = null;

    private final static int MAX_Height = 100;
    private final static int MIN_Height = 80;
    private static int screen_rate = MAX_Height;

    // sysfs path
    private final static String CPU0_SCALING_MIN_FREQ               = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";

    private String mCurrentLeftString = null;
    private String mCurrentTopString = null;
    private String mCurrentWidthString = null;
    private String mCurrentHeightString = null;

    private int mCurrentLeft = 0;
    private int mCurrentTop = 0;
    private int mCurrentWidth = 0;
    private int mCurrentHeight = 0;
    private int mCurrentRight = 0;
    private int mCurrentBottom = 0;

    private int mPreLeft = 0;
    private int mPreTop = 0;
    private int mPreRight = 0;
    private int mPreBottom = 0;
    private int mPreWidth = 0;
    private int mPreHeight = 0;
    
	private int mPlus = 3; //每点击一下移动的像素
	private int mMin=0;
	private int mMax=100;

    private  String mCurrentMode = null;

    private int mMaxRight = 0;
    private int mMaxBottom=0;

    public HiveviewDisplayPositionManager(Context context) {
        mContext = context;
        mSystenControl = new SystemControlManager(mContext);
        mOutputModeManager = new OutputModeManager(mContext);
        mDisplayInfo = mSystenControl.getDisplayInfo();
        initPostion();
    }

    public void initPostion() {
        mCurrentMode = mOutputModeManager.getCurrentOutputMode();
        initStep(mCurrentMode);
        initCurrentPostion();
        if (!mDisplayInfo.socType.contains("meson8")) {
            writeFile(OutputModeManager.FB0_FREE_SCALE , "1");
        }
        setScalingMinFreq(408000);
    }

    private void initCurrentPostion() {
        int [] position = mOutputModeManager.getPosition(mCurrentMode);
        mPreLeft = mCurrentLeft = position[0];
        mPreTop = mCurrentTop  = position[1];
        mPreWidth = mCurrentWidth = position[2];
        mPreHeight = mCurrentHeight= position[3];
        mPreRight=mCurrentRight=mCurrentWidth+mCurrentLeft-1;
        mPreBottom=mCurrentBottom=mCurrentTop+mCurrentHeight-1;
        Log.i("WANG", "mCurrentLeft:"+mCurrentLeft +" mCurrentTop:"+mCurrentTop + " mCurrentWidth:"+mCurrentWidth +"mCurrentHeight :" +mCurrentHeight);
    }

    //控制最左，和最右的 按钮
    public  void keyCodeLeft(boolean isLeft) {
		if (isLeft) {
			Log.i("WANG","IN LEFT TRUE");
			mCurrentLeft -= mPlus;
			if (mCurrentLeft <= mMin) {
				mCurrentLeft = mMin;
			}

		} else {
			Log.i("WANG","IN LEFT FALSE");
			mCurrentRight += mPlus;
			if (mCurrentRight >= mMaxRight) {
				mCurrentRight = mMaxRight;
			}
		}
		 setPosition(mCurrentLeft, mCurrentTop,mCurrentRight, mCurrentBottom, 0);
	}

    //控制  左2和 右1的按钮
	public void keyCodeRight(boolean isLeft) {
		if (isLeft) {
			Log.i("WANG","IN RIGTH TRUE");
			mCurrentLeft += mPlus;
			if (mCurrentLeft >= mMaxRight  || mCurrentLeft>=mCurrentRight ) {
				mCurrentLeft = mCurrentRight;
			}

		} else {
			Log.i("WANG","IN RIGTH FALSE");
			mCurrentRight -= mPlus;
			if (mCurrentRight <= mCurrentLeft) {
				mCurrentRight = mCurrentLeft;
			}
		}

		 setPosition(mCurrentLeft, mCurrentTop,mCurrentRight, mCurrentBottom, 0);
	}

	public void keyCodeUp(boolean isBottom) {
		if (isBottom) {
			Log.i("WANG","IN UP TRUE");
			mCurrentBottom -= mPlus;
			if (mCurrentBottom <= mCurrentTop) {
				mCurrentBottom = mCurrentTop;
			}

		} else {
			Log.i("WANG","IN UP FALSE");
			mCurrentTop += mPlus;
			if (mCurrentTop >= mCurrentBottom) {
				mCurrentTop = mCurrentBottom;
			}

		}

		 setPosition(mCurrentLeft, mCurrentTop,mCurrentRight, mCurrentBottom, 0);
	}

	public void keyCodeDown(boolean isBottom) {
		if (isBottom) {
			Log.i("WANG","IN DOWN TRUE");
			mCurrentBottom += mPlus;
			/*edit by wqh 此处不需要判断mLastTop <= mMin ，顶部的值肯定是小于 mMin的，
			 * 所以导致向下调整都是一次性复位
			 */
			if ( mCurrentBottom >= mMaxBottom) {
				mCurrentBottom = mMaxBottom;
			}

		} else {
			Log.i("WANG","IN DOWN FALSE");
			mCurrentTop -= mPlus;
			if (mCurrentTop <= mMin ) {
				mCurrentTop = mMin;
			}

		}

		 setPosition(mCurrentLeft, mCurrentTop,mCurrentRight, mCurrentBottom, 0);
	}


    public void saveDisplayPosition() {
   
        if ( !isScreenPositionChanged())
            return;
      
        mOutputModeManager.savePosition(mCurrentLeft, mCurrentTop, mCurrentWidth, mCurrentHeight);
        
        setScalingMinFreq(96000);
    }

    private void writeFile(String file, String value) {
        mSystenControl.writeSysFs(file, value);
    }

    private final void setScalingMinFreq(int scalingMinFreq) {
        int minFreq = scalingMinFreq;
        String minFreqString = Integer.toString(minFreq);

        mSystenControl.writeSysFs(CPU0_SCALING_MIN_FREQ, minFreqString);
    }
    private void initStep(String mode) {
        if (mode.contains(OutputModeManager.HDMI_480)) {
            mMaxRight = 719;
            mMaxBottom = 479;
        }else if (mode.contains(OutputModeManager.HDMI_576)) {
            mMaxRight = 719;
            mMaxBottom = 575;
        }else if (mode.contains(OutputModeManager.HDMI_720)) {
            mMaxRight = 1279;
            mMaxBottom = 719;
        }else if (mode.contains(OutputModeManager.HDMI_1080)) {
            mMaxRight = 1919;
            mMaxBottom = 1079;
        }else if (mode.contains(OutputModeManager.HDMI_4K2K)) {
            mMaxRight = 3839;
            mMaxBottom = 2159;
        } else if (mode.contains(OutputModeManager.HDMI_SMPTE)) {
            mMaxRight = 4095;
            mMaxBottom = 2159;
        } else {
            mMaxRight = 1919;
            mMaxBottom = 1079;
        }
    }

    public  void setPosition(int l, int t, int r, int b, int mode) {
     
        int left =  l;
        int top =  t;
        int right =  r;
        int bottom =  b;
 //       mCurrentWidth = mCurrentRight - mCurrentLeft+1;
 //       mCurrentHeight = mCurrentBottom - mCurrentTop+1;
        
        mCurrentWidth = right-left+1;
        mCurrentHeight = bottom - top+1;
        
        int width = mCurrentWidth;
        int height = mCurrentHeight;

        if (left < 0) {
            left = 0 ;
        }

        if (top < 0) {
            top = 0 ;
        }
        right = Math.min(right,mMaxRight);
        bottom = Math.min(bottom,mMaxBottom);
        
        Log.i("WANG", "RINGHT"+right);
        Log.i("WANG", "bottom"+bottom);
    	Log.i("WANG", "mCurrentLeft:"+mCurrentLeft +" mCurrentTop:"+mCurrentTop + " mCurrentWidth:"+mCurrentWidth +"mCurrentHeight :" +mCurrentHeight);
        writeFile(OutputModeManager.FB0_WINDOW_AXIS, left + " " + top + " " + right + " " + bottom);

        mOutputModeManager.savePosition(left, top, width, height);
        mOutputModeManager.setOsdMouse(left, top, width, height);
    }

    public boolean isScreenPositionChanged(){
        if (mPreLeft== mCurrentLeft && mPreTop == mCurrentTop
            && mPreWidth == mCurrentWidth && mPreHeight == mCurrentHeight)
        {   Log.i("WANG","isScreenPositionChanged false");
            return false;
        }
        else{
        	 Log.i("WANG","isScreenPositionChanged true");
        	 return true;
        }
            
    }
    
    public void returnBack(){
    //	  mCurrentWidth =mPreWidth ;
   //       mCurrentHeight = mPreHeight;
          this.setPosition(mPreLeft, mPreTop, mPreRight, mPreBottom, 0);
    }
}
