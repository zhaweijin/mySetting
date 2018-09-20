package com.hiveview.cloudtv.settings.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import android.util.Log;

public class HttpCheck {

	private Timer timeoutTimer;
	private String TAG = "HttpCheck";
	private HttpCheckListener httpCheckListener;
	private boolean result = false;
	
	public HttpCheck(HttpCheckListener listener){
		httpCheckListener = listener;
		startCheck();
	}
	

	private void startCheck(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				startTimer();
				pingConnectServerIsOk();
			}
		}).start();
	}
	
	public interface HttpCheckListener{
		void httpCheckResult(boolean result);
	}
	
	
	public void startTimer(){
		Log.v(TAG, "start timer");
		if (timeoutTimer == null) {
			timeoutTimer = new Timer();
			timeoutTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					Log.v(TAG, ">>>time out");
					cancelTimer();
					httpCheckListener.httpCheckResult(result);
				}
			}, 1000*4);
		}
	}
	
	
	
	public void pingConnectNtpIsOk(){
		HttpURLConnection conn = null;
		String correct_result="";
		try {
            Log.v(TAG, "start pingConnectNtpIsOk");
			String urlString = "";
			if(Utils.isOverseas()){
				urlString = "http://api.ntp.global.domybox.com";
				correct_result="Welcome";
			}else {
				urlString = "http://ntp.pthvcdn.gitv.tv";
				correct_result="Welcome";
			}
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10*000);
			conn.setConnectTimeout(10*000);
			String content="";
			Log.v(TAG, "code>"+conn.getResponseCode());
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = conn.getInputStream();
				if (inputStream != null) {
					InputStreamReader inputreader = new InputStreamReader(inputStream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					while ((line = buffreader.readLine()) != null) {
						content += line +"\n";
					}
					inputStream.close();
				}
				String title = content.substring(content.indexOf("<title>")+7, content.indexOf("</title>"));
				if(title.contains(correct_result))
                    result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		

		if (timeoutTimer != null) {
			Log.v(TAG, "pingConnectNtpIsOk");
			cancelTimer();
			httpCheckListener.httpCheckResult(result);
		}

	}
	
	private  void cancelTimer() {
    	Log.v(TAG, "cancelTimer");
		if (timeoutTimer != null) {
			timeoutTimer.cancel();
			timeoutTimer = null;
		}
	}
	
	public void pingConnectServerIsOk(){
		HttpURLConnection conn = null;
		String correct_result="";
		try {
			Log.v(TAG, "start pingConnectServerIsOk");
			String urlString = "";
			if(Utils.isOverseas()){
				urlString = "http://testspeed.global.domybox.com/testconnect/index.html";
				correct_result="hiveview2016";
			}else {
				urlString = "http://ntp.pthvcdn.gitv.tv";
				correct_result="Welcome";
			}
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10*000);
			conn.setConnectTimeout(10*000);
			String content="";
			Log.v(TAG, "code>"+conn.getResponseCode());
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = conn.getInputStream();
				if (inputStream != null) {
					InputStreamReader inputreader = new InputStreamReader(inputStream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					while ((line = buffreader.readLine()) != null) {
						content += line;
					}
					inputStream.close();
				}
				Log.v(TAG, "content="+content);
				if(content.contains(correct_result))
					result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		 
		if (timeoutTimer != null && result) {
			Log.v(TAG, "set pingConnectServerIsOk result");
			cancelTimer();
			httpCheckListener.httpCheckResult(result);
		}else {
			if(timeoutTimer != null)
			   pingConnectNtpIsOk();
		}
	}
}
