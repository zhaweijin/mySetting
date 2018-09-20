package com.hiveview.cloudtv.settings.videoimage;


import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.droidlogic.app.OutputModeManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle ;

public class OutputUiManager {
    private static final String TAG = "OutputUiManager";
    private static boolean DEBUG = false;

    public static final String CVBS_MODE = "cvbs";
    public static final String HDMI_MODE = "hdmi";

    private static final String[] HDMI_LIST = {
        "1080p60hz",
        "1080p50hz",
        "720p60hz",
        "720p50hz",
        "2160p24hz",
        "2160p25hz",
        "2160p30hz",
        "2160p50hz",
        "2160p50hz420",
        "2160p60hz",
        "2160p60hz420",
        "smpte24hz",
        "1080p24hz",
        "576p50hz",
        "480p60hz",
        "1080i50hz",
        "1080i60hz",
/*        "576i50hz",
        "480i60hz",*/
        
    };
    private static final String[] HDMI_TITLE = {
        "1080p-60Hz",
        "1080p-50Hz",
        "720p-60Hz",
        "720p-50Hz",
        "4k2k-24Hz",
        "4k2k-25Hz",
        "4k2k-30Hz",
        "4k2k-50Hz",
        "4k2k-50Hz-420",
        "4k2k-60Hz",
        "4k2k-60Hz-420",
        "4k2k-smpte",
        "1080p-24Hz",
        "576p-50Hz",
        "480p-60Hz",
        "1080i-50Hz",
        "1080i-60Hz",
/*        "576i-50Hz",
        "480i-60Hz"*/
    };
    private static final String[] CVBS_MODE_VALUE_LIST = {
        "480cvbs",
        "576cvbs"
    };
    private static final String[] CVBS_MODE_TITLE_LIST = {
        "480 CVBS",
        "576 CVBS"
    };
    private static final int DEFAULT_HDMI_MODE = 0;
    private static final int DEFAULT_CVBS_MODE = 1;
    private static String[] mHdmiValueList;
    private static String[] mHdmiTitleList;

    private ArrayList<String> mTitleList = new ArrayList<String>();
    private ArrayList<String> mValueList = new ArrayList<String>();
    private ArrayList<String> mSupportList = new ArrayList<String>();

    private OutputModeManager mOutputModeManager;
    private Context mContext;

    private static String mUiMode;

    public OutputUiManager(Context context){
        mContext = context;
        mOutputModeManager = new OutputModeManager(mContext);
       
       Log.i(TAG, "i am in" );

        mUiMode = getUiMode();
        initModeValues(mUiMode);
    }

    public String getUiMode(){
        String currentMode = mOutputModeManager.getCurrentOutputMode();
        if (currentMode.contains(CVBS_MODE)) {
            mUiMode = CVBS_MODE;
        } else {
            mUiMode = HDMI_MODE;
        }
        return mUiMode;
    }

    public void updateUiMode(){
        mUiMode = getUiMode();
        initModeValues(mUiMode);
    }

    public int getCurrentModeIndex(){
         String currentMode = mOutputModeManager.getCurrentOutputMode();
         for (int i=0 ; i < mValueList.size();i++) {
             if (currentMode.equals(mValueList.get(i))) {
                return i ;
             }
         }
         if (mUiMode.equals(HDMI_MODE)) {
            return DEFAULT_HDMI_MODE;
         }else{
            return DEFAULT_CVBS_MODE;
         }
    }

    private void initModeValues(String mode){
        filterOutputMode();
        mTitleList.clear();
        mValueList.clear();

        if (mode.equalsIgnoreCase(HDMI_MODE)) {
            for (int i=0 ; i< mHdmiValueList.length; i++) {
                if (mHdmiTitleList[i] != null && mHdmiTitleList[i].length() != 0) {
                    mTitleList.add(mHdmiTitleList[i]);
                    mValueList.add(mHdmiValueList[i]);
                }
            }
        }else if (mode.equalsIgnoreCase(CVBS_MODE)) {
            for (int i = 0 ; i< CVBS_MODE_VALUE_LIST.length; i++) {
                mTitleList.add(CVBS_MODE_VALUE_LIST[i]);
            }
            for (int i=0 ; i < CVBS_MODE_VALUE_LIST.length ; i++) {
                mValueList.add(CVBS_MODE_VALUE_LIST[i]);
            }
        }
    }

    public void change2NewMode(final String mode) {
         Log.i(TAG, "i am come");
        mOutputModeManager.setBestMode(mode);
        Log.i(TAG, "i am dead");
    }

    public void change2BestMode() {
        mOutputModeManager.setBestMode(null);
    }
    public boolean isBestOutputmode(){
        return mOutputModeManager.isBestOutputmode();
    }

    public ArrayList<String> getOutputmodeTitleList(){
        return mTitleList;
    }

    public ArrayList<String> getOutputmodeValueList(){
        return mValueList;
    }

    public void  filterOutputMode() {
        List<String> list_value = new ArrayList<String>();
        List<String> list_title = new ArrayList<String>();

        mHdmiValueList = HDMI_LIST;
        mHdmiTitleList = HDMI_TITLE;

        for (int i = 0; i < mHdmiValueList.length; i++) {
            if (mHdmiValueList[i] != null) {
                list_value.add(mHdmiValueList[i]);
                list_title.add(mHdmiTitleList[i]);
            }
        }


        String str_edid = mOutputModeManager.getHdmiSupportList();
        
        Log.v(TAG, "str_edid=="+str_edid);
        if (str_edid != null && str_edid.length() != 0 && !str_edid.contains("null")) {
            List<String> list_hdmi_mode = new ArrayList<String>();
            List<String> list_hdmi_title = new ArrayList<String>();
            for (int i = 0; i < list_value.size(); i++) {
                if (isSupport(list_value.get(i), str_edid)) {
                    list_hdmi_mode.add(list_value.get(i));
                    list_hdmi_title.add(list_title.get(i));
                }
            }
            mHdmiValueList = list_hdmi_mode.toArray(new String[list_value.size()]);
            mHdmiTitleList = list_hdmi_title.toArray(new String[list_title.size()]);
        } else {
            mHdmiValueList = list_value.toArray(new String[list_value.size()]);
            mHdmiTitleList = list_title.toArray(new String[list_title.size()]);
        }
    }
    
    
    public boolean isSupport(String value,String edid){
    	boolean status = false;
    	String[] edids = edid.split(",");
    	for(int i=0;i<edids.length;i++){
    		if(value.equals(edids[i].toLowerCase())){
    			status = true;
    			break;
    		}
    	}
    	return status;
    }
}
