package com.hiveview.cloudtv.settings;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.backup.BackupManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.ActivityManagerNative;
import android.app.IActivityManager;

  
import com.hiveview.cloudtv.settings.util.Utils;
import com.hiveview.cloudtv.settings.widget.LanguageDialog;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView;
import com.hiveview.cloudtv.settings.widget.ResolutionDialog;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView.FocusViewAnimatorEndListener;

public class GuideLanguage extends Activity {
	private String TAG = "GuideLanguage";
 
	 
	private RelativeLayout layout_en;
	private RelativeLayout layout_us;
	
	private final int EN = 0;
	private final int US = 1;
	
	private Context mContext;
	
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
                init();
				break;
			default:
				break;
			}
			
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_language_setting);
		 
	 
		mContext = this;
		initView();
		 
	 
		init();
		mHandler.sendEmptyMessageDelayed(0, 500);
		 
	}
	
	private void init(){
		if(Utils.getLanguage()==EN){
			Log.v(TAG, "en...");
			layout_en.setFocusable(true);
			layout_en.requestFocus();
			layout_en.requestFocusFromTouch();
		}else if(Utils.getLanguage()==US){
			Log.v(TAG, "us...");
			layout_us.setFocusable(true);
			layout_us.requestFocus();
			layout_us.requestFocusFromTouch();
		}
		
	}
	 
	private void updateLanguage(Locale locale) {
		Log.e(TAG, "===>  updateLanguage = "+locale.getLanguage());
		
		IActivityManager am = ActivityManagerNative.getDefault();
		Configuration config;
		try {
			config = am.getConfiguration();
			config.locale = locale;//Locale.SIMPLIFIED_CHINESE;
			config.userSetLocale = true;
			am.updateConfiguration(config);
			BackupManager.dataChanged("com.android.providers.settings"); 
			writeCacheLocal(locale);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initView(){
		
	    
		layout_en = (RelativeLayout)findViewById(R.id.layout_en);
		layout_us = (RelativeLayout)findViewById(R.id.layout_us);
		layout_en.setOnClickListener(onClickListener);
		layout_us.setOnClickListener(onClickListener);
		
	}
	
	private void writeCacheLocal(Locale locale){
		//String fileName = "/cache/recovery/last_locale1";
		File RECOVERY_DIR = new File("/cache/recovery");
	    File COMMAND_FILE = new File(RECOVERY_DIR, "last_locale");
	    
	    RECOVERY_DIR.mkdirs();  // In case we need it
        COMMAND_FILE.delete();
        
        FileWriter command;
        try {
        	command = new FileWriter(COMMAND_FILE);
            command.write(locale.toString());
           // command.write("\n");
            command.close();
        } catch(Exception e){ 
			e.printStackTrace(); 
		}finally {
            
        }
		 Log.d(TAG, "write cache = " + locale.toString());
	}
	

	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private void tipsDialog(final int id) {
		final LanguageDialog dialog = new LanguageDialog(mContext);
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = getResources().getDimensionPixelSize(
				R.dimen.diy_dialog_width);
		lp.height = getResources().getDimensionPixelSize(
				R.dimen.diy_dialog_height);
		dialogWindow.setAttributes(lp);
		dialog.show();
		TextView message = (TextView) dialog.findViewById(R.id.message1);
		Button button1 = (Button) dialog.findViewById(R.id.button1);
		Button button2 = (Button) dialog.findViewById(R.id.button2);
		if(id == EN){
			message.setText(getString(R.string.language_dialog_tips_zh));
			button1.setText(getString(R.string.language_ok_zh));
			button2.setText(getString(R.string.language_cancel_zh));
		}else{
			message.setText(getString(R.string.language_dialog_tips_en));
			button1.setText(getString(R.string.language_ok_en));
			button2.setText(getString(R.string.language_cancel_en));
		}
		
		button1.setOnClickListener(new OnClickListener() {

			public void onClick(View paramView) {
				// TODOAuto-generated method stub
				dialog.dismiss();
				setLanguage(id);
			}
		});

		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}

		});
	}
	
	View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.layout_en:
				tipsDialog(EN);
				break;
			case R.id.layout_us:
				tipsDialog(US);
				break;
			default:
				break;
			}
		}
	};
	
	private void setLanguage(int id){
		if(id == 0){
			updateLanguage(Locale.SIMPLIFIED_CHINESE);
		}else if(id== 1){
			updateLanguage(Locale.US);
		}

		Log.v(TAG, "finish");
		
		startGuide();
		finish();
		
	}
	
	
	private void startGuide(){
		Log.v(TAG, "setting startGuide >>>>>>>>>>>>>>>>>>>>>");
    	ComponentName componentName = new ComponentName("com.hiveview.cloudscreen.guide",
				"com.hiveview.cloudscreen.guide.AbroadActivity");
		Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        try{
            startActivity(intent);
        }catch (ActivityNotFoundException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
