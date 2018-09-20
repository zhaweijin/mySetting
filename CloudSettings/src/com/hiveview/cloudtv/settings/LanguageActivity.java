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
import android.widget.TextView;
import android.app.ActivityManagerNative;
import android.app.IActivityManager;

import com.hiveview.cloudtv.settings.util.Utils;
import com.hiveview.cloudtv.settings.widget.LanguageDialog;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView.FocusViewAnimatorEndListener;

public class LanguageActivity extends Activity {
	private String TAG = "LanguageActivity";
	private LauncherFocusView focusView = null;
	private ListView mListView = null;
	private Context mContext = this;
	private List<String> mList = new ArrayList<String>();
	private MyAdapter myAdpter = null;
	private TextView mTextView = null;
	private boolean bfocusViewInitStatus = true;
	private int mCurKeycode = KeyEvent.KEYCODE_0;
	private boolean mTextColorChangeFlag = false;
	private boolean mFocusAnimationEndFlag = false;
	private long mKeyDownTime = 0L;
	private boolean isFromGuide = false;
	
	
	private final int ZH = 0;
	private final int US = 1;
	private final int ES = 2;
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				onInitSelect();
				break;
			default:
				break;
			}
			
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language);
		focusView = (LauncherFocusView) findViewById(R.id.deviceinfo_focus_view);
		mListView = (ListView) findViewById(R.id.device_info_list);
		/*SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.activity_item_for_common, 
				new String[]{"item_name"}, new int[]{R.id.item_name});
		listView.setAdapter(adapter);*/
		initData();
		initView();
		setTheListener();
		isFromGuide = getIntent().getBooleanExtra("isFromGuide", false);
	}
	
	private void setTheListener(){
		OnKeyListener onKeyListener = new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				mCurKeycode = keyCode;
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
							|| keyCode == KeyEvent.KEYCODE_DPAD_UP) {

						if (event.getRepeatCount() == 0) {
							mTextColorChangeFlag = true;
							mKeyDownTime = event.getDownTime();
						} else {
							mTextColorChangeFlag = false;
							if (event.getDownTime() - mKeyDownTime >= Utils.KEYDOWN_DELAY_TIME) {
								mKeyDownTime = event.getDownTime();
							} else {
								return true;
							}
						}

						if (!mFocusAnimationEndFlag) {
							mTextColorChangeFlag = false;
						}
					}
					}else{
						if (!mTextColorChangeFlag) {
							mTextColorChangeFlag = true;
							listTextColorSet();
						}
					}
				return false;
			}
			
		};
		mListView.setOnKeyListener(onKeyListener);
		OnItemClickListener onItemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.v(TAG, "onItem click=" +position);
				Log.v(TAG, "getLanguage=" +Utils.getLanguage());
				
				if(isFromGuide){
					tipsDialog(position);
				}else{
					if(position == 0 && Utils.getLanguage()!=0){
						updateLanguage(Locale.SIMPLIFIED_CHINESE);
					}else if(position== 1 && Utils.getLanguage()!=1){
						updateLanguage(Locale.US);
					}else if(position== 2 && Utils.getLanguage()!=2){
						updateLanguage(new Locale("es","US"));
					}
					
					initAdapter();
				}	
			}
		};
		mListView.setOnItemClickListener(onItemClickListener);
		OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//	Log.i(TAG, "-----"+position);
					if (bfocusViewInitStatus) {
						focusView.initFocusView(view, false, 0);
					}
					
					if(mTextView!=null&&mTextView.getCurrentTextColor()!=mContext.getResources().getColor(R.color.select_text_color)){
						mTextView.setTextColor(mContext.getResources().getColor(R.color.grey5_color));
					}
					
					mTextView = (TextView) view.findViewById(R.id.infoitem);
					if (mCurKeycode == KeyEvent.KEYCODE_DPAD_DOWN) {

						if (position < 5
								|| position > mListView.getCount() - 2
								|| (mListView.getFirstVisiblePosition() == 0 && view.getTop() < (view.getHeight() * 4))
								|| (mListView.getFirstVisiblePosition() != 0 && view.getTop() < view.getHeight() * 5)) {
							focusView.moveTo(view);
						} else {
							listTextColorSet();
							
							mListView.setSelectionFromTop(position,view.getTop() - view.getHeight());

						}
					} else if (mCurKeycode == KeyEvent.KEYCODE_DPAD_UP) {
						if ((position == 0 || mListView.getFirstVisiblePosition() == 0 && view.getTop() > (view.getHeight()))
								|| (mListView.getFirstVisiblePosition() != 0 && view.getTop() >= view.getHeight())
								|| Math.abs(view.getTop()-view.getHeight())<10) {
							focusView.moveTo(view);
						} else {
							//Log.i(TAG, "------"+(view.getTop() - view.getHeight()));
							listTextColorSet();
							mListView.setSelectionFromTop(position,view.getHeight());
						}

					} else if (mCurKeycode == KeyEvent.KEYCODE_PAGE_UP
							|| mCurKeycode == KeyEvent.KEYCODE_PAGE_DOWN) {
						focusView.moveTo(view);
					}
					
					if (bfocusViewInitStatus) {
						bfocusViewInitStatus = false;
						mTextColorChangeFlag = true;
						listTextColorSet();
					}
					// fixed the keyboard repeat mode
					if (!mTextColorChangeFlag && mFocusAnimationEndFlag) {
						if ((position == 0 || position == mListView
								.getCount() - 1)) {
							mTextColorChangeFlag = true;
							listTextColorSet();
						}
					}
				
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		};
		mListView.setOnItemSelectedListener(onItemSelectedListener);
	}
	
	private void initData(){
		mList.add(getResources().getString(R.string.chinese));
		mList.add(getResources().getString(R.string.english));
		mList.add(getResources().getString(R.string.spanish));
	}
	private void updateLanguage(Locale locale) {
		Log.e(TAG, "===>  updateLanguage = "+locale.getLanguage());
		
		IActivityManager am = ActivityManagerNative.getDefault();
		Log.i(TAG, "===am=="+am);
		Configuration config;
		try {
			config = am.getConfiguration();
			Log.i(TAG, "===config=="+config);
			config.locale = locale;//Locale.SIMPLIFIED_CHINESE;
			Log.i(TAG, "===config.locale=="+config.locale);
			config.userSetLocale = true;
			am.updateConfiguration(config);
			Log.i(TAG, "===configupdate=="+am.getConfiguration());
			BackupManager.dataChanged("com.android.providers.settings"); 
			writeCacheLocal(locale);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initView(){
		//mListView=(ListView)findViewById(R.id.com_setting_list);
	//	focusView=(LauncherFocusView)findViewById(R.id.activity_common_focusview);
		focusView.setAnimatorEndListener(new FocusViewAnimatorEndListener() {

			@Override
			public void OnAnimateStart(View currentFocusView) {
				// TODO Auto-generated method stub
				mFocusAnimationEndFlag = false;
			}

			@Override
			public void OnAnimateEnd(View currentFocusView) {
				// TODO Auto-generated method stub
				mFocusAnimationEndFlag = true;
				listTextColorSet();
			}

		});
		initAdapter();	
	    
		mListView.setSelection(Utils.getLanguage());
		mHandler.sendEmptyMessageDelayed(0, 500);
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

//		 try{ 
//		        FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);
//		        byte [] bytes = lang.getBytes(); 
//		        fout.write(bytes); 
//		        fout.flush();
//		        fout.close(); 
//		 }catch(Exception e){ 
//			 e.printStackTrace(); 
//		 } 
		 Log.d(TAG, "===write cache = " + locale.toString());
	}
	
	private void initAdapter(){
		if(myAdpter == null){
			myAdpter = new MyAdapter(mContext);
			mListView.setAdapter(myAdpter);
		}else{
			myAdpter.notifyDataSetChanged();
		}
	}
	
	private void listTextColorSet(){
		if (mTextColorChangeFlag && mFocusAnimationEndFlag) {
			if (mTextView != null&&mTextView.getCurrentTextColor()!=mContext.getResources().getColor(R.color.select_text_color)) {
				mTextView.setTextColor(this.getResources().getColor(R.color.white));
			}
			mTextColorChangeFlag = false;

		}
	}
	
	private List<Map<String,Object>> getData(){
		List <Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String ,Object> map = new HashMap<String, Object>();
		map.put("item_name", this.getResources().getString(R.string.chinese));
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("item_name", this.getResources().getString(R.string.english));
		list.add(map);
		return list;
	}
	
	class MyAdapter extends BaseAdapter{
	      
		private Context context;
		private LayoutInflater mInflater;
		public MyAdapter(Context context) {
			 super();
			 this.context = context;
			 this.mInflater = LayoutInflater.from(context);
		}
		
		
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.activity_timezone_item, null);
				viewHolder = new ViewHolder();
				viewHolder.item =(TextView) convertView.findViewById(R.id.infoitem);
				convertView.setTag(viewHolder);
			}else{
				viewHolder =(ViewHolder) convertView.getTag();
			}
			
			viewHolder.item.setText((String)mList.get(position));
			int k = Utils.getLanguage();
			//Log.i(TAG, "----get View--position---"+k+"-----"+position);
			if(position == k){
				viewHolder.item.setTextColor(context.getResources().getColor(R.color.select_text_color));
			}else{
				viewHolder.item.setTextColor(context.getResources().getColor(R.color.grey5_color));
			}
			
			
			return convertView;
		}
		
		
		public final class ViewHolder{
			public TextView item;
		}
	}
	
	private void onInitSelect(){
		
		mListView.setFocusable(true);
		mListView.requestFocus();
		mListView.requestFocusFromTouch();
		
		int selectIndex = Utils.getLanguage();
		
		View view2 = mListView.getChildAt(selectIndex);
		focusView.initFocusView(view2, false, 0);
		
		Log.v(TAG, "index=="+selectIndex);
		mListView.setSelection(selectIndex);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(isFromGuide){
				return true;
			}
		}else if(keyCode == KeyEvent.KEYCODE_MENU){
			
		//	updateLanguage(new Locale("es", "US"));
		}
		return super.onKeyDown(keyCode, event);
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
		if(id == ZH){
			message.setText(getString(R.string.language_dialog_tips_zh));
			button1.setText(getString(R.string.language_ok_zh));
			button2.setText(getString(R.string.language_cancel_zh));
		}else if(id==US){
			message.setText(getString(R.string.language_dialog_tips_en));
			button1.setText(getString(R.string.language_ok_en));
			button2.setText(getString(R.string.language_cancel_en));
		}else if(id==ES){
			message.setText(getString(R.string.language_dialog_tips_es));
			button1.setText(getString(R.string.language_ok_es));
			button2.setText(getString(R.string.language_cancel_es));
		}
		
		button1.setOnClickListener(new OnClickListener() {

			public void onClick(View paramView) {
				// TODOAuto-generated method stub
				dialog.dismiss();
				if(id == 0 && Utils.getLanguage()!=0){
					updateLanguage(Locale.SIMPLIFIED_CHINESE);
				}else if(id== 1 && Utils.getLanguage()!=1){
					updateLanguage(Locale.US);
				}else if(id== 2 && Utils.getLanguage()!=2){
					updateLanguage(new Locale("es","US"));
				}
				
			
				startGuide();
				LanguageActivity.this.finish();
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
}
