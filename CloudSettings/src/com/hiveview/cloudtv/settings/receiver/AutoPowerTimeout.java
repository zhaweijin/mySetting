package com.hiveview.cloudtv.settings.receiver;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import com.hiveview.cloudtv.settings.R;
import com.hiveview.cloudtv.settings.bluetooth.Utils;
import com.hiveview.cloudtv.settings.widget.ResetDialog;

import android.os.SystemProperties;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hiveview.cloudtv.settings.CommonActivity;

public  class AutoPowerTimeout extends BroadcastReceiver {

	private final String tag = "AutoPowerTimeout";
	private Context mContext = null;
	//private boolean TimerRunning = false;
	private static int  DialogRunning = 0;
	private static ResetDialog dialog = null;
	// private final int MsgTimerInit = 0;
	private final int MsgTimerStart = 1;
	private final int MsgOpenDialog = 2;
	private final int MsgCloseDialog =3;
	private final int MsgTimerSecStart = 4;
	private final int MsgTimerSecUpdate = 5;
	private final int MsgSystemPoweroff  =6;
	private final  int Sec = 1000;
	private final int Hour = Sec * 60 * 60;
	private int TimeSec = 60;
	private static long currentTimeout = 0;
	private static Timer timer = null;
	private static TimerTask task = null;
	private static TimerTask task2 = null;

	private final Handler handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
				
			case MsgTimerStart:
				Log.i(tag, "MsgTimerStart" + currentTimeout);
				timer = new Timer();
				task = new MyTimerTask();
		        timer.schedule (task, currentTimeout * Sec);
				break;
			case MsgCloseDialog:
				Log.i(tag, "MsgCloseDialog" + currentTimeout);
				dialog.dismiss();
				DialogRunning--;
				break;
			case MsgTimerSecStart:
				Log.i(tag, "MsgTimerSecStart" + currentTimeout);
				TimeSec = 60;
				timer = new Timer();
				task2 = new MyTimerTask2();
				timer.schedule (task2, 1000,1000);
				break;
			case MsgTimerSecUpdate:
				Log.i(tag, "MsgTimerSecUpdate" + TimeSec);
				if (TimeSec > 0) {
					TimeSec -- ;
					TextView message = (TextView) dialog.findViewById(R.id.line2);
					message.setText(mContext.getString(R.string.AutoPowerDialog_Text3) + "  (  " + TimeSec + "s )");
				} else {
					taskCancel();
					sendMsg(MsgSystemPoweroff);
				}
				break;
			case MsgSystemPoweroff:
				Log.i(tag, "MsgSystemPoweroff:#### Power Off");
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
				break;
			case MsgOpenDialog: {
				Log.i(tag, "MsgOpenDialog");
				if(!SystemProperties.get("hiveview.allow.autopoweroff", "true").equals("true")){
					return;
				}
				if (DialogRunning == 0) {
				DialogRunning ++;
				TimeSec = 60;
					taskCancel();
				dialog = new ResetDialog(mContext,R.layout.autopower_dialog);
				dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
	
				Window dialogWindow = dialog.getWindow();
				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
				lp.width = 736;
				lp.height = 360;
				dialogWindow.setAttributes(lp);
				dialog.show();
				TextView message = (TextView) dialog.findViewById(R.id.line1);
				message.setText(mContext.getString(R.string.AutoPowerDialog_Text1)
							+ ((currentTimeout < Hour / Sec) ? currentTimeout / 60 : (currentTimeout / 3600))
							+ mContext.getString((currentTimeout < Hour / Sec) ? R.string.Minute : R.string.Hour)
						+ mContext.getString(R.string.AutoPowerDialog_Text2));
				message = (TextView) dialog.findViewById(R.id.line2);
				message.setText(mContext.getString(R.string.AutoPowerDialog_Text3) + "  (  " + TimeSec + "s )");
				sendMsg(MsgTimerSecStart);
				}
				}
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	private void taskCancel() {
		if (timer != null) {
			Log.i(tag, "timer cancel");
			timer.cancel();
			timer = null;
		}
		if (task != null) {
			Log.i(tag, "task cancel");
			task.cancel();
			task = null;
		}
		if (task2 != null) {
			Log.i(tag, "task2 cancel");
			task2.cancel();
			task2 = null;
		}
	}

	private void sendMsg(int MsgId) {
        Message message = new Message();
		message.what = MsgId;
		handler.sendMessage(message);
    }
    
	class MyTimerTask extends TimerTask{
		  @Override
		  public void run() {
		   // TODO Auto-generated method stub
		   sendMsg(MsgOpenDialog);
			this.cancel();
		  }
	}
	
	class MyTimerTask2 extends TimerTask{
		  @Override
		  public void run() {
		   // TODO Auto-generated method stub
		   sendMsg(MsgTimerSecUpdate);
		  }
	}
    
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		mContext = context;
		
		if (DialogRunning > 0) {
			sendMsg(MsgCloseDialog);
		}
		taskCancel();

		currentTimeout = CommonActivity.getIntPref(mContext, "auto_power_timeout", "auto_power_timeout", com.hiveview.cloudtv.settings.util.Utils.DEFAULT_AUTO_POWER_TIME);
		Log.i(tag, "Reveive key : timer Start ------currentTimeout :" + currentTimeout);

		if (currentTimeout > 0) {
			sendMsg(MsgTimerStart);
        }
	}
}
