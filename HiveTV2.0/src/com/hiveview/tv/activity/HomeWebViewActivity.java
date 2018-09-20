package com.hiveview.tv.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.hiveview.tv.R;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.view.ProgressDialog;

import android.R.integer;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint({ "HandlerLeak", "SetJavaScriptEnabled" })
public class HomeWebViewActivity extends BaseActivity{

	private WebView webView;
	
	private String urlString;
	
	private ProgressDialog progressDialog;
	
	private final int USER_INPUTE=1000;
	
	private final int PASSWORD_INPUTE=USER_INPUTE+1;

	protected String TAG="HomeWebViewActivity";
	
	
	 Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case USER_INPUTE:
				Log.i(TAG, "USER_INPUTE............start");
				Process process = null;
				try {
					process = Runtime.getRuntime().exec(" adb shell input text qwe ");
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String str = null;
					while ((str = reader.readLine()) != null) {
						Log.d(TAG, "reader.readLine() ......................................:" + str);
					}

					BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					String strerror = null;
					while ((strerror = error.readLine()) != null) {
						Log.d(TAG, "error.readLine() .................................: " + strerror);
					}
					reader.close();
					error.close();
					process.destroy();
					Log.i(TAG, "USER_INPUTE............end");
				} catch (Exception e) {
					Log.i(TAG, "USER_INPUTEError............"+ e.toString());
					e.printStackTrace();
				}
				break;
			case PASSWORD_INPUTE:
				
				break;

			default:
				break;
			}
		};
	};
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Log.v(TAG, "onCreate");
		setContentView(R.layout.activity_home_webview);
		webView=(WebView) findViewById(R.id.home_webview);
		progressDialog=(ProgressDialog) findViewById(R.id.home_webview_loading);
		
		progressDialog.setVisibility(View.VISIBLE);
		urlString=getIntent().getStringExtra("ChlitinaUri");
		
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new webViewClient());
		webView.setClickable(false);
		webView.setAddStatesFromChildren(false);
		WebSettings mWebSettings=webView.getSettings();
		
		mWebSettings.setDefaultTextEncodingName("utf-8");
		mWebSettings.setPluginsEnabled(true);
		mWebSettings.setPluginState(PluginState.ON);
		mWebSettings.setRenderPriority(RenderPriority.HIGH);
		mWebSettings.setJavaScriptEnabled(true);
	
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v(TAG, "onResume");
		webView.onResume();
		
		webView.loadUrl(urlString);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v(TAG, "onDestroy");
		webView.stopLoading();
		webView.clearCache(true);
		webView.clearHistory();
		webView.clearFormData();
		webView.destroyDrawingCache();
		webView.destroy();

	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v(TAG, "onPause");
	}
	
	 @Override   
	    //设置回退    
	    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法    
	    public boolean onKeyDown(int keyCode, KeyEvent event) {    
	        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {    
//	        	if(webView.canGoBack()){
//	        	webView.goBack(); //goBack()表示返回WebView的上一页面    
//	            return true;    
//	        	}else{
	        		finish();
	        	//}
	        }  
	        return false;    
	    }    
	        
	    //Web视图    
	    private class webViewClient extends WebViewClient {    
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {    
	            view.loadUrl(url);    
	            return false;    
	        }   
	        
	        @Override
	        public void onPageFinished(WebView view, String url) {
	        	// TODO Auto-generated method stub
	        	super.onPageFinished(view, url);
	    		HttpTaskManager.getInstance().submit(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.i(TAG, "USER_INPUTE............start");
						Process process = null;
						BufferedReader reader=null;
						BufferedReader error=null;
						String str = null;
						String strerror = null;
						try {
							
							process = Runtime.getRuntime().exec(" adb shell input text Chlitina ");
							reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
							while ((str = reader.readLine()) != null) {
								Log.d(TAG, "reader.readLine() ......................................:" + str);
							}
							error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
							while ((strerror = error.readLine()) != null) {
								Log.d(TAG, "error.readLine() .................................: " + strerror);
							}
							
							process = Runtime.getRuntime().exec(" adb shell input keyevent 20 ");
							reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
							while ((str = reader.readLine()) != null) {
								Log.d(TAG, "reader.readLine() ......................................:" + str);
							}

							error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
							while ((strerror = error.readLine()) != null) {
								Log.d(TAG, "error.readLine() .................................: " + strerror);
							}
							process = Runtime.getRuntime().exec(" adb shell input text 333333 ");
							reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
							while ((str = reader.readLine()) != null) {
								Log.d(TAG, "reader.readLine() ......................................:" + str);
							}

							error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
							while ((strerror = error.readLine()) != null) {
								Log.d(TAG, "error.readLine() .................................: " + strerror);
							}
							
							process = Runtime.getRuntime().exec(" adb shell input keyevent 20 ");
							reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
							while ((str = reader.readLine()) != null) {
								Log.d(TAG, "reader.readLine() ......................................:" + str);
							}

							error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
							while ((strerror = error.readLine()) != null) {
								Log.d(TAG, "error.readLine() .................................: " + strerror);
							}
							
							reader.close();
							error.close();
							process.destroy();
							Log.i(TAG, "USER_INPUTE............end");
						} catch (Exception e) {
							Log.i(TAG, "USER_INPUTEError............"+ e.toString());
							e.printStackTrace();
						}
						//mHandler.sendEmptyMessageDelayed(USER_INPUTE, 1000);	
					}
	    			
	    		});
	    		
	    		progressDialog.setVisibility(View.INVISIBLE);
	        }
	        
	        @Override
	        public void onPageStarted(WebView view, String url, Bitmap favicon) {
	        	// TODO Auto-generated method stub
	        	super.onPageStarted(view, url, favicon);
	        	webView.setVisibility(View.VISIBLE);
	        }
	        
	        @Override
	        public void onReceivedError(WebView view, int errorCode,
	        		String description, String failingUrl) {
	        	// TODO Auto-generated method stub
	        	super.onReceivedError(view, errorCode, description, failingUrl);
	        	progressDialog.setVisibility(View.INVISIBLE);
	    		Toast.makeText(HomeWebViewActivity.this, "加载失败，请稍候再试", Toast.LENGTH_SHORT)
				.show();
	        }
	    }    
	    
	   
}
