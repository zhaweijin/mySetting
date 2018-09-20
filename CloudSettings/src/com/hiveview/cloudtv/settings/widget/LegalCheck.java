package com.hiveview.cloudtv.settings.widget;

import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hiveview.cloudtv.settings.R;
import com.hiveview.cloudtv.settings.upload.DataMessageHandler;
import com.hiveview.cloudtv.settings.util.Utils;

public class LegalCheck {

	
    private final int SHOW_TIME = 0x111;
    private final int SHOW_DIALOG = 0x222;
	private TextView line3;
	private int time_count = 15;
	private Timer timer;
	private String TAG = "LegalCheck";
	private SharedPreferences preferences;
	private Context context;

	public LegalCheck() {
	}
	
	public void check(Context mContext){
		context = mContext;
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				preferences = context.getSharedPreferences("Silence_Install", Context.MODE_PRIVATE);
				
				String version="";
				try {
					version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
				} catch (Exception e) {
					// TODO: handle exception
				}
				Log.v(TAG, "ver==="+version);
				if(!preferences.getBoolean("send_sucess", false) && (version.equals("5.1.2") || version.equals("5.1.3"))){
					boolean b=DataMessageHandler.sendMessage(context);
					if(b){
						preferences.edit().putBoolean("send_sucess", true).apply();
					}
					Log.v(TAG, "send result=="+b);
				}
				if(!Utils.boxLegalCheck()){
					mHandler.sendEmptyMessage(SHOW_DIALOG);
				}
			}
		}).start();
	}
 
	

	private void showAutoPowerDialog(Context mContext) {
		
		ResetDialog resetDialog = new ResetDialog(mContext,R.layout.box_legal_check_autopower_dialog);
		resetDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
		resetDialog.setCancelable(false);
		Window dialogWindow = resetDialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		lp.width = width;
		lp.height = height;
		dialogWindow.setAttributes(lp);
		resetDialog.show();

		line3 = (TextView) resetDialog.findViewById(R.id.line3);
		line3.setText("" + time_count);
		timer = new Timer();
		timer.schedule(task, 0, 1000);

	}

	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			mHandler.sendEmptyMessage(SHOW_TIME);
		}
	};

	private void autoPower() {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyEvent.KEYCODE_POWER);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case SHOW_TIME:
				time_count--;
				line3.setText("" + time_count);
				if (time_count <= 0) {
					timer.cancel();
					// reboot
					autoPower();
					Log.v(TAG, "reboot.....");
				}
				break;
			case SHOW_DIALOG:
				showAutoPowerDialog(context);
				break;
			}
			super.handleMessage(msg);
		}
	};

}
