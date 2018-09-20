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
import android.util.Log;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class AuxiliaryTimeView extends RelativeLayout{
	
	public static  TimeChangeListner timeChangeListner;
	private static final String TAG = "AuxiliaryTimeView";

	private Handler mHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			
			updateTime();
			
			sendEmptyMessageDelayed(0, 60000);
			
		};
	};
	
	
	private TextView timeTextView;
	
	public AuxiliaryTimeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public AuxiliaryTimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AuxiliaryTimeView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		long time = System.currentTimeMillis();
		timeTextView = new TextView(getContext());
		timeTextView.setGravity(Gravity.CENTER);
		timeTextView.setTextSize(19);
		timeTextView.setTextColor(Color.WHITE);
		
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		this.addView(timeTextView,params);
		
		updateTime();
		
		mHandler.sendEmptyMessageDelayed(0, 5000);
		Log.d(TAG, "loadImage AuxiliaryTimeView::init  " + (System.currentTimeMillis() - time));
		
	}
	
	private void updateTime(){
		
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		//hh:mm 12小时制,, HH:mm 24小时制
		SimpleDateFormat date = new SimpleDateFormat("HH:mm");
		timeTextView.setText(date.format(new Date()));
		if(null != timeChangeListner){
			timeChangeListner.updateTime(new Date());
		}
	}
	
	/**
	 * 设置时间变换接口
	 * @Title: AuxiliaryTimeView
	 * @author:郭松胜
	 * @Description: TODO
	 * @param timeChangeListner
	 */
	public void setTimeChangeListner (TimeChangeListner timeChangeList){
		timeChangeListner = timeChangeList;
	}
	
	/**
	 * 时间变换接口
	 * @ClassName: TimeChangeListner
	 * @Description: TODO
	 * @author: guosongsheng
	 * @date 2014年8月28日 上午9:33:45
	 *
	 */
	public interface TimeChangeListner {
		public void updateTime(Date date);
	}

}
