package com.hiveview.tv.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class StorageAuxiliaryView extends RelativeLayout{
	
	
	private Handler mHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			
			updateTime();
			
			sendEmptyMessageDelayed(0, 60000);
			
		};
	};
	
	private TextView timeTextView;
	private ImageView netStatusImageView;
	
	public StorageAuxiliaryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public StorageAuxiliaryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public StorageAuxiliaryView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
	
		timeTextView = new TextView(getContext());
		timeTextView.setGravity(Gravity.CENTER);
		timeTextView.setTextSize(19);
		timeTextView.setTextColor(Color.WHITE);
		
		netStatusImageView = new ImageView(getContext());
		
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		this.addView(timeTextView,params);
		
		updateTime();
		
		mHandler.sendEmptyMessageDelayed(0, 5000);
		
	}
	
	private void updateTime(){
		
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		SimpleDateFormat date = new SimpleDateFormat("hh:mm");
		timeTextView.setText(date.format(new Date()));

	}
	

}
