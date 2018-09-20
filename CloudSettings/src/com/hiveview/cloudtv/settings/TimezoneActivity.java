package com.hiveview.cloudtv.settings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hiveview.cloudtv.settings.imageprofile.TimeZoneEntity;
import com.hiveview.cloudtv.settings.util.Utils;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView.FocusViewAnimatorEndListener;
import com.hiveview.cloudtv.settings.widget.TimeZoneDialog;

public class TimezoneActivity extends Activity {

	private String TAG="TimezoneActivity";
	private Context mContext = this;
	public static final String XMLTAG_TIMEZONE ="timezone";
	public static  final int HOURS_1 =3600000;
	private int counter = 0;
	private ArrayList<String> mList = new ArrayList<String>();
	private List mListId = new ArrayList<String>();
	private ListView mListView =null;
	private LauncherFocusView focusView = null;
	private MyAdapter myAdpter = null;
	private TextView mTextView = null;
	private boolean bfocusViewInitStatus = true;
	private int mCurKeycode = KeyEvent.KEYCODE_0;
	private boolean mTextColorChangeFlag = false;
	private boolean mFocusAnimationEndFlag = false;
	private long mKeyDownTime = 0L;
	private int offsetY = 106;
	private int mCurrentPosition = 0;
	String extraStr;
	public ArrayList<TimeZoneEntity> timeZoneList =new ArrayList<TimeZoneEntity>();
	private TimeZoneDialog dialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		timeZoneList = SettingsApplication.getInstance().entities;
		setContentView(R.layout.activity_timezone);
		extraStr = getIntent().getStringExtra("config");
		Log.i("SettingsApplication", "---TimezoneActivity--"+SettingsApplication.getInstance().status);
		if(null != timeZoneList && timeZoneList.size()>0){
			Log.i(TAG, "----start from settingsApplication----");
			initDataFromSettingsApplication();
		}else if(null != extraStr &&!"".equals(extraStr)){
			Log.i(TAG, "----start from ExtraData----");
			initDataFromExtra(extraStr);
		}else{
			Log.i(TAG, "----start from default xml----");
			initDataFromXml();
		}
		
        initView();	
        setTheListener();
	}
	
	private void initDataFromSettingsApplication(){
		final long date = Calendar.getInstance().getTimeInMillis();
		for(int i=0;i<timeZoneList.size();i++){
			Log.i(TAG, "--------id--------"+timeZoneList.get(i).getId()+"------name-------"+timeZoneList.get(i).getName());
			addItem(timeZoneList.get(i).getId(), timeZoneList.get(i).getName(), date);
		}
	}
	//当此activity被开机引导拉起的时候，屏蔽返回键。
	public boolean dispatchKeyEvent(KeyEvent event){
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			if(null != extraStr &&!"".equals(extraStr)){
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}
	
	
	private void initDataFromExtra(String extra) {
        String configJSON = extra;
        JSONObject json;
		try {
			json = new JSONObject(configJSON);
			JSONArray jsonArray = json.getJSONArray("time_zone");
			 ArrayList<TimeZoneEntity> list =new ArrayList<TimeZoneEntity>();
			for(int i =0;i<jsonArray.length();i++){
				TimeZoneEntity entity = new TimeZoneEntity();
				JSONObject json2 = (JSONObject) jsonArray.get(i);
				entity.setId(json2.getString("id"));
				entity.setName(json2.getString("name"));
				list.add(entity);
			}
            SettingsApplication.getInstance().entities = list;
            final long date = Calendar.getInstance().getTimeInMillis();
    		for(int i=0;i<list.size();i++){
    			addItem(list.get(i).getId(), list.get(i).getName(), date);
    		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
        Log.d("SettingsApplication", "getJSON---> " + configJSON);
    }
	
	
	private void setTheListener(){
		mListView.setOnKeyListener(onKeyListener);
		mListView.setOnItemClickListener(onItemClickListener);
		mListView.setOnItemSelectedListener(onItemSelectedListener);
	}
	
	private void initAdapter(){
		if(myAdpter == null){
			myAdpter = new MyAdapter(mContext);
			mListView.setAdapter(myAdpter);
		}else{
			myAdpter.notifyDataSetChanged();
		}
	}
	
	private void initView(){
		mListView=(ListView)findViewById(R.id.com_setting_list);
		focusView=(LauncherFocusView)findViewById(R.id.activity_common_focusview);
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
		mListView.setSelection(getTheTimeZoneId()!=-1 ? getTheTimeZoneId():0);
	}

    private void initDataFromXml(){
    	 final long date = Calendar.getInstance().getTimeInMillis();
         try {
             XmlResourceParser xrp = mContext.getResources().getXml(R.xml.timezones);
             while (xrp.next() != XmlResourceParser.START_TAG)
                 continue;
             xrp.next();
             while (xrp.getEventType() != XmlResourceParser.END_TAG) {
                 while (xrp.getEventType() != XmlResourceParser.START_TAG) {
                     if (xrp.getEventType() == XmlResourceParser.END_DOCUMENT) {
                     //    return myData;
                     }
                     xrp.next();
                 }
                 if (xrp.getName().equals(XMLTAG_TIMEZONE)) {
                     String id = xrp.getAttributeValue(0);
                     String displayName = xrp.nextText();
                     addItem(id, displayName,date);
                 }
                 while (xrp.getEventType() != XmlResourceParser.END_TAG) {
                     xrp.next();
                 }
                 xrp.next();
             }
             xrp.close();
         } catch (XmlPullParserException xppe) {
             Log.e(TAG, "Ill-formatted timezones.xml file");
         } catch (java.io.IOException ioe) {
             Log.e(TAG, "Unable to read timezones.xml file");
         }
    }

	private void addItem(String id, String displayName,long date) {
        final TimeZone tz = TimeZone.getTimeZone(id);
        final int offset = tz.getOffset(date);
        final int p = Math.abs(offset);
        final StringBuilder name = new StringBuilder();
        name.append("(GMT");
        
        if (offset < 0) {
            name.append('-');
        } else {
            name.append('+');
        }
        
        name.append(p / (HOURS_1));
        name.append(':');
        
        int min = p / 60000;
        min %= 60;

        if (min < 10) {
            name.append('0');
        }
        name.append(min);
        name.append(") ");
        name.append(getTimeZoneCity(tz));
        counter++;
        mList.add(name.toString());
        mListId.add(id);
       // Log.i(TAG, name.toString());        
	}
	private String getTimeZoneCity(TimeZone tz){
		 
		//TimeZone tz = TimeZone.getDefault();
		String displayName="";
        try {
            XmlResourceParser xrp = mContext.getResources().getXml(R.xml.timezones);
            while (xrp.next() != XmlResourceParser.START_TAG)
                continue;
            xrp.next();
            while (xrp.getEventType() != XmlResourceParser.END_TAG) {
                while (xrp.getEventType() != XmlResourceParser.START_TAG) {
                    if (xrp.getEventType() == XmlResourceParser.END_DOCUMENT) {
                    //    return myData;
                    }
                    xrp.next();
                }
                if (xrp.getName().equals(TimezoneActivity.XMLTAG_TIMEZONE)) {
                    String id = xrp.getAttributeValue(0);
                    displayName = xrp.nextText();
                    if(tz.getID().equals(id)){
                    	Log.v(TAG, "display name="+displayName);
                    	break;
                    }
                }
                while (xrp.getEventType() != XmlResourceParser.END_TAG) {
                    xrp.next();
                }
                xrp.next();
            }
            xrp.close();
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, "Ill-formatted timezones.xml file");
        } catch (java.io.IOException ioe) {
            Log.e(TAG, "Unable to read timezones.xml file");
        }
        return displayName;
   }
	
	
	private int getTheTimeZoneId(){
		TimeZone tz = TimeZone.getDefault();
		return mListId.indexOf(tz.getID());
	}
	
	private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
		
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
	
	private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View view, int position,
				long arg3) {
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
					Log.i(TAG, "------"+(view.getTop() - view.getHeight()));
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
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private void listTextColorSet(){
		if (mTextColorChangeFlag && mFocusAnimationEndFlag) {
			if (mTextView != null&&mTextView.getCurrentTextColor()!=mContext.getResources().getColor(R.color.select_text_color)) {
				mTextView.setTextColor(this.getResources().getColor(R.color.white));
			}
			mTextColorChangeFlag = false;

		}
	}
	
	
	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			// TODO Auto-generated method stub
			mCurrentPosition = position;
			/*final AlarmManager alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
			alarm.setTimeZone((String)mListId.get(position));
			Log.i(TAG, "-------"+(String)mListId.get(position)+"---------"+mList.get(position));
			initAdapter();*/
			dialog();
			
		}
	};
	
	
	
	
	public void dialog() {
		  dialog = new TimeZoneDialog(mContext);
	      Window dialogWindow = dialog.getWindow();
	      WindowManager.LayoutParams lp = dialogWindow.getAttributes();
	      lp.width = getResources().getDimensionPixelSize(R.dimen.diy_dialog_width);
	      lp.height = getResources().getDimensionPixelSize(R.dimen.diy_dialog_height);
	      dialogWindow.setAttributes(lp);
	      dialog.show();
	      TextView message = (TextView) dialog.findViewById(R.id.message1);
	      TextView message2 = (TextView) dialog
                  .findViewById(R.id.message2);
	      message.setText(getString(R.string.confirm_your_timezone));
	      message2.setText(mList.get(mCurrentPosition));

	      Button button1 = (Button) dialog.findViewById(R.id.button1);
	      Button button2 = (Button) dialog.findViewById(R.id.button2);
	      button1.setOnClickListener(new OnClickListener() {

	          public void onClick(View paramView) {
	                  // TODOAuto-generated method stub
	        	  final AlarmManager alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
	  			  alarm.setTimeZone((String)mListId.get(mCurrentPosition));
	  			  Log.i(TAG, "-------"+(String)mListId.get(mCurrentPosition)+"---------"+mList.get(mCurrentPosition));
	  			  if(!TextUtils.isEmpty(extraStr)){
	  				dialog.dismiss();
	  				TimezoneActivity.this.finish();
	  			  }else{
	  				dialog.dismiss();
	  				initAdapter();
	  			  }
	          	 
	          	 
	             
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
			return mListId.get(arg0);
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
			int k = getTheTimeZoneId();
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
	
	
}
