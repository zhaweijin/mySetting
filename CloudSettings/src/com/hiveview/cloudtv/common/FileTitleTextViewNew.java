package com.hiveview.cloudtv.common;

import com.hiveview.cloudtv.settings.ManualAddWifiAcvitity;
import com.hiveview.cloudtv.settings.PppoeConnectActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FileTitleTextViewNew  extends TextView {
	public FileTitleTextViewNew(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public FileTitleTextViewNew(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FileTitleTextViewNew(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public boolean isFocused(){
//		 Intent intent=getIntent();
//	     Bundle bundle=intent.getExtras();  
//	     str=bundle.getString("addwifi_item");  
//	     if(str.equals("success")){
//	    	 return true;
//	     }
//		if(ManualAddWifiAcvitity.mManualWifiSecurityRelativeLayout.isSelected()==true){
//			return true;
//			
//		}
		if(ManualAddWifiAcvitity.index ==1){
			ManualAddWifiAcvitity.index =0;
			return true;
		}
		if(PppoeConnectActivity.index ==0){
			PppoeConnectActivity.index =1;
			return true;
		}
		return false;
	}
	

}
