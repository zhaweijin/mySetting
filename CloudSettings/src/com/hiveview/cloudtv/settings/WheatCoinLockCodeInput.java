package com.hiveview.cloudtv.settings;


import java.security.PublicKey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hiveview.cloudtv.common.CommonItemList;
import com.hiveview.cloudtv.settings.CommonActivity;








import com.hiveview.cloudtv.settings.util.ToastUtils;

import android.content.ContentResolver;
import android.util.Log;


public class WheatCoinLockCodeInput extends Activity {
	private Gallery gallery;
	private ImageView ivViewFocus;
	private TextView textView;
	private String[] myImageIds = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	private TextView textViewwc;
	private TextView textViewsc;
	private boolean isLongClick = false;// 鍥炲垹闀挎寜浜嬩欢鍒犻櫎绾跨▼鐨勬爣蹇椾綅
	private Context mContext = this;

	
	//huangke20150731xiugai
	private static final String GET_APPINSTALL_INFO = "GET_APPINSTALL_INFO";
	/* 鑾峰彇楹︾矑閿佷俊鎭殑鏂规硶 */
	private static final String GET_WHEAT_LOCK_INFO = "GET_WHEAT_LOCK_INFO";
	/* 璁剧疆楹︾矑閿佷俊鎭殑鏂规硶 */
	private static final String SET_WHEAT_LOCK_INFO = "SET_WHEAT_LOCK_INFO";
	/* 楹︾矑閿佹暟鎹簱鐨剈ri */
	public static final Uri URI_INSTALL_APK_INFO = Uri.parse("content://HiveViewCloudPayAuthorities/TABLE_WhearLock");

	// public static final Uri URI_INSTALL_APK_INFO = Uri.parse("content://HiveViewCloudAppStoreAuthorities/TABLE_INSTALLAPP");

	private static final String TO_RECHARGE = "com.hiveview.cloudscreen.user.userrecharge_activity";
	// private static final String TO_RECHARGE = "com.hiveview.appstore.main";
	/* @Fields DEFULT_CATEGORY 榛樿鐨凜ategory */
	private static final String DEFULT_CATEGORY = "android.intent.category.DEFAULT";
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wheat_code_input);

		ivViewFocus = (ImageView) findViewById(R.id.focus);
		textView = (TextView) findViewById(R.id.mm);
		textViewsc = (TextView) findViewById(R.id.sc);
		textViewwc = (TextView) findViewById(R.id.wc);
		textViewsc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String text = (String) textView.getText();
				if (text.length() > 0) {
					text = text.substring(0, text.length() - 1);
					textView.setText(text);
				}
			}
		});
		
		//鍙戦�佸箍鎾殑鏂瑰紡浼犺緭瀵嗙爜
		textViewwc.setOnClickListener(new OnClickListener() {
			
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
		
				String text = (String) textView.getText();				
				if(text.length()<4){
					ToastUtils.showToast(mContext,
							mContext.getResources().getString(R.string.wheat_codeinput_toast),
							Toast.LENGTH_SHORT);
				}else if(text.length() == 4)
				{
					String k = textView.getText().toString();
					ContentResolver cr = getContentResolver();
		            String isLock = "1";
		            String pwd = k; 
		            Bundle bundle = new Bundle();
					bundle.putString("lock_pwd", pwd);
					bundle.putString("is_lock", isLock);
					cr.call(URI_INSTALL_APK_INFO, SET_WHEAT_LOCK_INFO, pwd, bundle);
					
					Intent intent = new Intent();
					intent.putExtra("TEXT", k);
					intent.setAction("com.hiveview.cloudtv.settings.wheatcode");
					mContext.sendBroadcast(intent); 
				
	            finish();
				}

				//huangke20150731xiugai
   /*           ContentResolver cr = getContentResolver();
	    		Bundle b = cr.call(URI_INSTALL_APK_INFO, GET_WHEAT_LOCK_INFO, null, null);
	    		String is_lock = b.getString("is_lock");
	    		String lock_pwd = b.getString("lock_pwd");
	    		Log.i("test", "___is_lock=" + is_lock + "+++lock_pwd=" + lock_pwd);              */
				
			}
		});

		textViewsc.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				isLongClick = true;
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						while (isLongClick) {
							handler.sendEmptyMessage(1);// 寮�鍚竴涓嚎绋嬪惊鐜紝姣忛殧0.1姣鎼滅储鍏抽敭瀛楅暱搴﹀噺1
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}).start();
				return false;
			}
		});
		textViewsc.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER && isLongClick) {
					isLongClick = false;
				}
				return false;
			}
		});
			

		gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new TextAdapter(this));
		gallery.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				String aString = (String) textView.getText();
				String bString = Integer.toString(position);
				if (bString.length() > 1) {
					bString = bString.substring(bString.length() - 1, bString.length());
				}
				textView.setText(aString + bString);
			}
		});

		// 2147483647 / 2 = 1073741820 - 1
		// 璁剧疆gallery鐨勫綋鍓嶉」涓轰竴涓瘮杈冨ぇ鐨勬暟锛屼互渚夸竴寮�濮嬪氨鍙互宸﹀彸寰幆婊戝姩
		int n = Integer.MAX_VALUE / 2 % myImageIds.length;
		int itemPosition = Integer.MAX_VALUE / 2 - n;
		gallery.setSelection(itemPosition);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Log.e("huangke", "---back----");
			Intent intent = new Intent();
			intent.setAction("com.hiveview.cloudtv.settings.wheatcodeback");
			mContext.sendBroadcast(intent);
			finish();
		}
		return false;
	}
	

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				String text = textView.getText().toString();
				if (text.length() > 0) {
					text = text.substring(0, text.length() - 1);
					textView.setText(text);
				} else {
					isLongClick = false;
				}
			}
		}
	};

	public class TextAdapter extends BaseAdapter {
		private Context mContext;

		public TextAdapter(Context c) {
			mContext = c;
		}

		public int getCount() /* 涓�瀹氳閲嶅啓鐨勬柟娉昰etCount,浼犲洖鍥剧墖鏁扮洰鎬绘暟 */
		{
			return Integer.MAX_VALUE;
		}

		public Object getItem(int position) /* 涓�瀹氳閲嶅啓鐨勬柟娉昰etItem,浼犲洖position */
		{
			return position;
		}

		public long getItemId(int position) /* 涓�瀹氳閲嶅啓鐨勬柟娉昰etItemId,浼犲洖position */
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			TextView i = new TextView(mContext);
			i.setTextSize(60);
			i.setTextColor(android.graphics.Color.WHITE);
			i.setText(myImageIds[position % myImageIds.length]);
			i.setLayoutParams(new Gallery.LayoutParams((int) getResources().getDimension(R.dimen.text_key_width), (int) getResources().getDimension(
					R.dimen.text_key_height))); /* 閲嶆柊璁惧畾Layout鐨勫楂� */
			i.setGravity(Gravity.CENTER);
			i.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if (hasFocus) {
						ivViewFocus.setVisibility(View.VISIBLE);
					} else {
						ivViewFocus.setVisibility(View.INVISIBLE);
					}
				}
			});
			return i;
		}
	}
}

