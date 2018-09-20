package com.hiveview.cloudtv.settings.videoimage;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

 

import com.hiveview.cloudtv.display.HiveviewDisplayPositionManager;
import com.hiveview.cloudtv.settings.R;

public class PositionSetting extends Activity implements OnClickListener,OnLongClickListener {
	private final String TAG = "PositionSetting";
	private boolean zoom_flag = false; //zoom_flag is true: zoom in;zoom_flag is false: zoom out
    public  Context context;
	private TextView mchangeZoomBtn;
	private TextView mleftBtn;
	private TextView mrightBtn;
	private TextView mtopBtn;
	private TextView mbottomBtn;
	private TextView mleftBtn2;
	private TextView mrightBtn2;
	private TextView mtopBtn2;
	private TextView mbottomBtn2;
    
	private boolean isLongClick = false;
	private Thread mLongClickThread;
	HiveviewDisplayPositionManager displaymanager;
	
	private boolean isHomeNotSave=true;

    private ImageView arrow_top;
//    private ImageView arrow_down;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(TAG, "--------onCreate--------");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_position);		
		context=this;
		displaymanager=new HiveviewDisplayPositionManager(this);
		TextView mHelp = (TextView)findViewById(R.id.positionsetting_help);
		
		mchangeZoomBtn = (TextView)findViewById(R.id.arrow_center);
		mleftBtn = (TextView)findViewById(R.id.left_in);
		mleftBtn2 = (TextView)findViewById(R.id.left_out);
		mrightBtn = (TextView)findViewById(R.id.right_in);
		mrightBtn2 = (TextView)findViewById(R.id.right_out);
		mtopBtn = (TextView)findViewById(R.id.top_in);
		mtopBtn2 = (TextView)findViewById(R.id.top_out);
		mbottomBtn = (TextView)findViewById(R.id.bottom_in);
		mbottomBtn2 = (TextView)findViewById(R.id.bottom_out);
		
		arrow_top = (ImageView)findViewById(R.id.arrow_top);
	/*	mchangeZoomBtn.setOnClickListener(this);
		mleftBtn.setOnClickListener(this);
		mleftBtn2.setOnClickListener(this);
		mrightBtn.setOnClickListener(this);
		mrightBtn2.setOnClickListener(this);
		mtopBtn.setOnClickListener(this);
		mtopBtn2.setOnClickListener(this);
		mbottomBtn.setOnClickListener(this);
		mbottomBtn2.setOnClickListener(this);
		
		
		mleftBtn.setOnLongClickListener(this);
		mleftBtn2.setOnLongClickListener(this);
		mrightBtn.setOnLongClickListener(this);
		mrightBtn2.setOnLongClickListener(this);
		mtopBtn.setOnLongClickListener(this);
		mtopBtn2.setOnLongClickListener(this);
		mbottomBtn.setOnLongClickListener(this);
		mbottomBtn2.setOnLongClickListener(this);*/
		
		
		
		mchangeZoomBtn.setOnClickListener(this);
		mleftBtn.setOnClickListener(this);
		mleftBtn2.setOnClickListener(this);
		mrightBtn.setOnClickListener(this);
		mrightBtn2.setOnClickListener(this);
		mtopBtn.setOnClickListener(this);
		mtopBtn2.setOnClickListener(this);
		mbottomBtn.setOnClickListener(this);
		mbottomBtn2.setOnClickListener(this);
		
		
		mleftBtn.setOnLongClickListener(this);
		mleftBtn2.setOnLongClickListener(this);
		mrightBtn.setOnLongClickListener(this);
		mrightBtn2.setOnLongClickListener(this);
		mtopBtn.setOnLongClickListener(this);
		mtopBtn2.setOnLongClickListener(this);
		mbottomBtn.setOnLongClickListener(this);
		mbottomBtn2.setOnLongClickListener(this);
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG,"--------onConfigurationChanged--------");
    }
	
/*	
	class ZoomClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			isLongClick = false;
          mWorkHandler.sendMessage(mWorkHandler.obtainMessage(v.getId()));
	    }
	}
	
	class ZoomLongClikListener implements OnLongClickListener{

		@Override
		public boolean onLongClick(final View view) {
			// TODO Auto-generated method stub
			Log.i("WANG","in long click");
			isLongClick = false;
			try {
				if (mLongClickThread != null) {
					mLongClickThread = null;
				}
				isLongClick = true;
				mLongClickThread = new Thread(new Runnable() {
					@Override
					public void run() {
						do {
							Log.i("WANG","in long click run ");
							try {
								mWorkHandler.sendMessage(mWorkHandler
										.obtainMessage(view.getId()));
								Thread.sleep(150);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} while (isLongClick);
					}
				});
				mLongClickThread.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		
	}*/
	
	@Override
	public void onClick(View v) {
		isLongClick = false;
      mWorkHandler.sendMessage(mWorkHandler.obtainMessage(v.getId()));
    }


	@Override
	public boolean onLongClick(final View view) {
		// TODO Auto-generated method stub
		Log.i("WANG","in long click");
		isLongClick = false;
		try {
			if (mLongClickThread != null) {
				mLongClickThread = null;
			}
			isLongClick = true;
			mLongClickThread = new Thread(new Runnable() {
				@Override
				public void run() {
					do {
						Log.i("WANG","in long click run ");
						try {
							mWorkHandler.sendMessage(mWorkHandler
									.obtainMessage(view.getId()));
							Thread.sleep(150);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} while (isLongClick);
				}
			});
			mLongClickThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
		
		
		
		private Handler mWorkHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Log.i("WANG","I AM HANDEL");
		switch (msg.what) {
		
		case R.id.left_out:
			if(arrow_top.getVisibility()==View.VISIBLE){
				arrow_top.setVisibility(View.GONE);
			}else {
				arrow_top.setVisibility(View.VISIBLE);
			}
			displaymanager.keyCodeLeft(true);
			if(arrow_top.getVisibility()!=View.VISIBLE){
				arrow_top.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.left_in:
			if(arrow_top.getVisibility()==View.VISIBLE){
				arrow_top.setVisibility(View.GONE);
			}else {
				arrow_top.setVisibility(View.VISIBLE);
			}
			displaymanager.keyCodeRight(true);
			if(arrow_top.getVisibility()!=View.VISIBLE){
				arrow_top.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.right_out:
			if(arrow_top.getVisibility()==View.VISIBLE){
				arrow_top.setVisibility(View.GONE);
			}else {
				arrow_top.setVisibility(View.VISIBLE);
			}
			displaymanager.keyCodeLeft(false);
			if(arrow_top.getVisibility()!=View.VISIBLE){
				arrow_top.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.right_in:
			if(arrow_top.getVisibility()==View.VISIBLE){
				arrow_top.setVisibility(View.GONE);
			}else {
				arrow_top.setVisibility(View.VISIBLE);
			}
			displaymanager.keyCodeRight(false);
			if(arrow_top.getVisibility()!=View.VISIBLE){
				arrow_top.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.bottom_out:
			if(arrow_top.getVisibility()==View.VISIBLE){
				arrow_top.setVisibility(View.GONE);
			}else {
				arrow_top.setVisibility(View.VISIBLE);
			}
		    displaymanager.keyCodeDown(true);
		    if(arrow_top.getVisibility()!=View.VISIBLE){
				arrow_top.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.bottom_in:
			if(arrow_top.getVisibility()==View.VISIBLE){
				arrow_top.setVisibility(View.GONE);
			}else {
				arrow_top.setVisibility(View.VISIBLE);
			}
			displaymanager.keyCodeUp(true);
			if(arrow_top.getVisibility()!=View.VISIBLE){
				arrow_top.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.top_out:
			if(arrow_top.getVisibility()==View.VISIBLE){
				arrow_top.setVisibility(View.GONE);
			}else {
				arrow_top.setVisibility(View.VISIBLE);
			}
			displaymanager.keyCodeDown(false);
			if(arrow_top.getVisibility()!=View.VISIBLE){
				arrow_top.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.top_in:
			if(arrow_top.getVisibility()==View.VISIBLE){
				arrow_top.setVisibility(View.GONE);
			}else {
				arrow_top.setVisibility(View.VISIBLE);
			}
			displaymanager.keyCodeUp(false);
			if(arrow_top.getVisibility()!=View.VISIBLE){
				arrow_top.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.arrow_center:
			isHomeNotSave=false;
			((Activity) context).finish();
			break;

		}
		super.handleMessage(msg);
		}
	};
		
		

	public boolean onKeyDown(int keyCode, KeyEvent msg) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	mchangeZoomBtn.setFocusable(true);
	    	mchangeZoomBtn.requestFocus();
	 		return true;
	 		}
	 		return super.onKeyDown(keyCode, msg);
	}

	
	

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i(TAG, "--------onResume--------");
		super.onResume();
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
        super.onPause();
        Log.i(TAG, "--------onPause--------");
    //    displaymanager.saveDisplayPosition();
	}
	
	 @Override
	 protected void onStop() {
	    if(isHomeNotSave){
	    	displaymanager.returnBack();
	    	this.finish();
	    }
	    Log.i(TAG, "--------onStop--------");
	    super.onStop();
	 }
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "--------onDestroy--------");
		if (isLongClick) {
			isLongClick = false;
		}
		super.onDestroy();
	}
}
