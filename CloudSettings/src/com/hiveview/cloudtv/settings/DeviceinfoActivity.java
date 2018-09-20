package com.hiveview.cloudtv.settings;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.os.SystemProperties;
import android.util.Log;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hiveview.cloudtv.common.CommonItemAdapter;
import com.hiveview.cloudtv.common.CommonItemList;
import com.hiveview.cloudtv.settings.connectivity.ConnectivityManagerData;
import com.hiveview.cloudtv.settings.ethernet.EthernetDataEntity;
import com.hiveview.cloudtv.settings.util.SettingInit;
import com.hiveview.cloudtv.settings.util.ToastUtils;
import com.hiveview.cloudtv.settings.util.Utils;
import com.hiveview.cloudtv.settings.widget.CustomProgressDialog;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView;
import com.hiveview.cloudtv.settings.widget.ResolutionDialog;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView.FocusViewAnimatorEndListener;
import com.hiveview.manager.SystemInfoManager;

@SuppressLint("ResourceAsColor")
public class DeviceinfoActivity extends Activity {

   private String TAG = "DeviceinfoActivity";

	private final int DEVICE_INFO_MODEL_INDEX			= 0;
	private final int DEVICE_INFO_SW_VERSION_INDEX		= 1;
	private final int DEVICE_INFO_ANDROID_VERSION_INDEX = 3;
	private final int DEVICE_INFO_HW_VERSION_INDEX		= 4;
	private final int DEVICE_INFO_IP_INDEX				= 5;
	private final int DEVICE_INFO_MAC_INDEX 			= 6;
	private final int DEVICE_INFO_SN_INDEX				= 7;
	private final int DEVICE_INFO_TIME_INDEX			= 2;
	private final int DEVICE_INFO_ROM_SIZE_INDEX		= 8;
	private final int DEVICE_INFO_RESET                 = 9;
	private String[] mArrays = new String[DEVICE_INFO_RESET+1];
	private String[] mInfoArrays = new String[DEVICE_INFO_RESET+1];
	private SystemInfoManager manager;
	private View mItemListCurView = null;
	private ListView mListView;
	private String mYunSn =null;
	private boolean mIsFirstIn = true;
	private LauncherFocusView mLauncherFocusView = null;
	private int mCurKeycode = -1;
	private boolean mTextColorChangeFlag = false;
	private boolean mFocusAnimationEndFlag = false;
	private TextView mItemName = null;
	private TextView mItemSetting = null;
	private Context mContext = this;
	private int mItemListCurPosition = -1;
	private long mKeyDownTime = 0l;
	private String mVersion = null;
 
	private int clickCount=0;
	private int software_version_click_count = 0;
	SimpleAdapter listItemAdapt;
	ArrayList<HashMap<String, Object>> listItem;
	
	private Drawable[] pageLefts = new Drawable[11];
	private Drawable[] pageRights = new Drawable[11];
	private String[] mItemSettings = new String[11];
	List<CommonItemList> mCommonItemList = new ArrayList<CommonItemList>();
	private CommonItemAdapter commonItemListAdapter = null;
	public static CustomProgressDialog mProgressDialog = null;
	public Activity mActivity;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				mInfoArrays[7] = mVersion;
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("infoitem", mArrays[7]);
				map.put("deviceinfo", mVersion);
				listItem.set(7, map);

				mInfoArrays[6]= mYunSn;
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				map1.put("infoitem", mArrays[6]);
				map1.put("deviceinfo", mYunSn);
				listItem.set(6, map1);
				
				if (listItemAdapt != null) {
					listItemAdapt.notifyDataSetChanged();
				}
			}else if(msg.what==2) {
				Log.i("wangqihui", "ether in");
	
		    	ConnectivityManagerData mConManData= new ConnectivityManagerData(mContext);
		    	mConManData.setEthernetInfo(true, null, null, mConManData.getEthernetDns(0), null);
		 		    
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.activity_deviceinfo);
		mActivity=this;
		mListView = (ListView) findViewById(R.id.device_info_list);
		mLauncherFocusView = (LauncherFocusView) findViewById(R.id.deviceinfo_focus_view);
		initListener();
		mLauncherFocusView.setAnimatorEndListener(new FocusViewAnimatorEndListener() {

			@Override
			public void OnAnimateEnd(View currentFocusView) {
				mFocusAnimationEndFlag = true;
				listTextColorSet();
			}

			@Override
			public void OnAnimateStart(View currentFocusView) {
				// TODO Auto-generated method stub
				mFocusAnimationEndFlag = false;
			}

		});
		// 获取设备信息,写入mInfoArrays中
		getDeviceInfo(this);
		// 绑定Layout里面的Listview
		ListView list = (ListView) findViewById(R.id.device_info_list);
		listItem = new ArrayList<HashMap<String, Object>>();
	 
		
		commonItemDataInit();
		commonItemListInit();
		
/*		Utils.formatSize(this, getRomTotalSize());
		Utils.formatSize(this, getRomAvailableSize());*/
		 
	}

	 
	//列表数据初始化
	public void commonItemDataInit(){
		
		//选择框焦点初始化
	
	    //读出列表数据
		mArrays =this.getResources().getStringArray(R.array.deviceinfo);

	
		pageRights[DEVICE_INFO_RESET] = this.getResources().getDrawable(
				R.drawable.page_right_big);
		
		mItemSettings =mInfoArrays;
		
		for(int i=0 ;i<mArrays.length; i++){
			CommonItemList item = new CommonItemList();
			item.setItemName(mArrays[i]);
			if (null != pageLefts[i]) {
				item.setPageLeft(pageLefts[i]);
			}
			if (null != pageRights[i]) {
				item.setPageRight(pageRights[i]);
			}
			if (null != mItemSettings[i]) {
				item.setItemSetting(mItemSettings[i]);
			}

			mCommonItemList.add(item);
		}
		
		
	}
	
	//列表显示初始化
	public void commonItemListInit(){
		if (commonItemListAdapter == null) {
			commonItemListAdapter = new CommonItemAdapter(this,mCommonItemList,0);
			mListView.setAdapter(commonItemListAdapter);
		} else {
			commonItemListAdapter.notifyDataSetChanged();
		}
		mListView.setSelection(0);
	}

	

	private void initListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt,
					long paramLong) {
				// TODO Auto-generated method stub
				mItemListCurPosition = paramInt;
				if(mItemListCurPosition==DEVICE_INFO_RESET){  
					dialog();
				}else {
					Utils.startListFocusAnimation(DeviceinfoActivity.this, mLauncherFocusView,
							R.anim.list_focus_anim);
				}
			

               if(paramInt==(mListView.getCount()-3)){     //SN					
					clickCount++;					
				}else if(paramInt == (mListView.getCount()-2)){  //内部存储空间
                    if(clickCount==3){
//                    	startFactoryTest();
                    	debugTool();
                    }
					clickCount=0;
				}
               
               if(paramInt==1){
            	   software_version_click_count++;
            	   Log.v(TAG, "softwore count="+software_version_click_count);
            	   if(software_version_click_count>=10 && (!SystemProperties.get(Utils.PROP_DEVELOPER_ALLOW, "true").equals("true"))){
            		   SystemProperties.set(Utils.PROP_DEVELOPER_ALLOW,"true");
            		   ToastUtils.showToast(DeviceinfoActivity.this, "开发者模式已开启!", Toast.LENGTH_LONG);
            	   }
               }
			}

		});
		mListView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View paramView, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				mCurKeycode = keyCode;

				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
							|| keyCode == KeyEvent.KEYCODE_DPAD_UP) {
						mCurKeycode = keyCode;
						if (event.getRepeatCount() == 0) {
							mTextColorChangeFlag = true;
							mKeyDownTime = event.getDownTime();
						} else {
							mTextColorChangeFlag = false;
							if (event.getDownTime() - mKeyDownTime >= Utils.KEYDOWN_DELAY_TIME) {
								Log.e("KeyEvent", "time=" + (event.getDownTime() - mKeyDownTime)
										+ " count" + event.getRepeatCount());
								mKeyDownTime = event.getDownTime();
							} else {
								return true;
							}
						}

						if (!mFocusAnimationEndFlag) {
							mTextColorChangeFlag = false;
						}
					}
				} else if (event.getAction() == KeyEvent.ACTION_UP) {
					if (!mTextColorChangeFlag) {
						mTextColorChangeFlag = true;
						listTextColorSet();
					}
				} 

				return false;
			}

		});
		mListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onItemSelected(AdapterView<?> paramAdapterView, View paramView,
					int paramInt, long paramLong) {
				// TODO Auto-generated method stub
				mItemListCurPosition = paramInt;
				mItemListCurView = paramView;
				if (mIsFirstIn) {
					mLauncherFocusView.initFocusView(paramView, false, 0f);

				}
				if (mItemName != null) {
					mItemName.setTextColor(mContext.getResources().getColor(R.color.grey5_color));
				}
				if (mItemSetting != null) {
					mItemSetting
							.setTextColor(mContext.getResources().getColor(R.color.grey5_color));
				}
				mItemName = (TextView) paramView.findViewById(R.id.item_name);
				mItemSetting = (TextView) paramView.findViewById(R.id.item_setting);

				if (mIsFirstIn) {
					Log.i("BANGBANG", "----At the onitemselect---" + mTextColorChangeFlag);
					mIsFirstIn = false;
					mTextColorChangeFlag = true;
					listTextColorSet();
				}

                if(paramInt!=1)
					software_version_click_count=0;

				if(mCurKeycode == KeyEvent.KEYCODE_DPAD_CENTER)
				{
				}



				if (mCurKeycode == KeyEvent.KEYCODE_DPAD_DOWN) {
					if (paramInt == mListView.getCount() - 2) {
						mLauncherFocusView.initFocusView(paramView, false, 0f);
					}
					if (paramInt < 5
							|| paramInt > mListView.getCount() - 2
							|| (mListView.getFirstVisiblePosition() == 0 && paramView.getTop() < (paramView
									.getHeight() * 4))
							|| (mListView.getFirstVisiblePosition() != 0 && paramView.getTop() < paramView
									.getHeight() * 5)) {
						mLauncherFocusView.moveTo(paramView);
					} else {
						listTextColorSet();
						mListView.setSelectionFromTop(paramInt,
								paramView.getTop() - paramView.getHeight());
					}
				} else if (mCurKeycode == KeyEvent.KEYCODE_DPAD_UP) {
				
					 /*由于 同维盒子 paramView.getTop() -paramView.getHeight() 时 在第三列时 值就小于0 了，
					     存在误差,所以增加一个误差值10，1.0+没有，所以改为在可控范围误差内 进行focuse的跳转  */
					if ((mItemListCurPosition == 0 || mListView.getFirstVisiblePosition() == 0
							&& paramView.getTop() > (paramView.getHeight()))
							|| (mListView.getFirstVisiblePosition() != 0 && paramView.getTop() >=paramView.getHeight() 
							||(paramView.getTop() -paramView.getHeight()<10 && paramView.getTop() -paramView
						        .getHeight() >-10)) ) {
						
					
						mLauncherFocusView.moveTo(paramView);
					} else {
						listTextColorSet();
						mListView.setSelectionFromTop(mItemListCurPosition, paramView.getHeight());
					}
				} else if (mCurKeycode == KeyEvent.KEYCODE_PAGE_UP
						|| mCurKeycode == KeyEvent.KEYCODE_PAGE_DOWN) {
					mLauncherFocusView.moveTo(paramView);
				}
				// fixed the keyboard repeat mode
				if (!mTextColorChangeFlag && mFocusAnimationEndFlag) {
					if ((mItemListCurPosition == 0 || mItemListCurPosition == mListView.getCount() - 1)) {
						mTextColorChangeFlag = true;
						listTextColorSet();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> paramAdapterView) {
				// TODO Auto-generated method stub

			}

		});

	}
	
	
	/* 获取发布时间 */
	private String getReleaseTime() {

		long getTime = SystemProperties.getLong("ro.build.date.utc", 0) * 1000;
		Date releaseTime = new Date(getTime);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss ");
		// 设置发布时间的时区为东八区，修复发布时间随时区错乱的问题
		format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		Log.i("settingtime", "get time" + format.format(releaseTime));
		return format.format(releaseTime);
	}
 

	// TODO
	private void getDeviceInfo(Context context) {
 
		manager = SystemInfoManager.getSystemInfoManager();
		try {
			mInfoArrays[DEVICE_INFO_MODEL_INDEX] = manager.getProductModel();
			mInfoArrays[DEVICE_INFO_SW_VERSION_INDEX] = manager.getFirmwareVersion();
			mInfoArrays[DEVICE_INFO_ANDROID_VERSION_INDEX] = manager.getAndroidVersion();
			mInfoArrays[DEVICE_INFO_HW_VERSION_INDEX] = manager.getHWFirmwareVersion();
			mInfoArrays[DEVICE_INFO_IP_INDEX] = Utils.getIP(this);//Utils.getDefaultIpAddresses(context);
			mInfoArrays[DEVICE_INFO_MAC_INDEX] = manager.getMacInfo();;
			mInfoArrays[DEVICE_INFO_SN_INDEX] = manager.getSnInfo();
			mInfoArrays[DEVICE_INFO_TIME_INDEX] = getReleaseTime();
			mInfoArrays[DEVICE_INFO_ROM_SIZE_INDEX] = getString(R.string.availablesize)
					+ Utils.formatSize(this, getRomAvailableSize()) + getString(R.string.totalsize)
					+ Utils.getStorageInfo(mContext);

		} catch (Exception e) {
			// TODO Auto-generated catch block
	
			e.printStackTrace();
		}
	}

	/**
	 * 获得机身内存大小
	 * 
	 * @return
	 */
	@SuppressLint("NewApi")
	private long getRomTotalSize() {
		File path = Environment.getDataDirectory();
		StatFs statFs = new StatFs(path.getPath());
		long blockSize = statFs.getBlockSizeLong();
		long tatalBlocks = statFs.getBlockCountLong();
		return blockSize * tatalBlocks;
	}

	/**
	 * 获得机身可用内存
	 * 
	 * @return
	 */
	@SuppressLint("NewApi")
	private long getRomAvailableSize() {
		File path = Environment.getDataDirectory();
		StatFs statFs = new StatFs(path.getPath());
		long blockSize = statFs.getBlockSizeLong();
		long availableBlocks = statFs.getAvailableBlocksLong();
		return blockSize * availableBlocks;
	}

	private void listTextColorSet() {
		if (mItemName != null && mTextColorChangeFlag && mFocusAnimationEndFlag) {
			mItemName.setTextColor(this.getResources().getColor(R.color.white));
		}
		if (mItemSetting != null && mTextColorChangeFlag && mFocusAnimationEndFlag) {
			mItemSetting.setTextColor(this.getResources().getColor(R.color.white));
		}
		if (mTextColorChangeFlag && mFocusAnimationEndFlag) {
			mTextColorChangeFlag = false;
		}

	}
	
	
	 ResolutionDialog dialog=null  ;
	public void dialog() {
		  dialog = new ResolutionDialog(mContext);
	      Window dialogWindow = dialog.getWindow();
	      WindowManager.LayoutParams lp = dialogWindow.getAttributes();
	      lp.width = getResources().getDimensionPixelSize(R.dimen.adjust_display_width);
		  lp.height = getResources().getDimensionPixelSize(R.dimen.adjust_display_height);
	      dialogWindow.setAttributes(lp);
	      dialog.show();
	      TextView message = (TextView) dialog
	                      .findViewById(R.id.message1);
	      message.setText(getString(R.string.setting_init_title));

	      Button button1 = (Button) dialog.findViewById(R.id.button1);
	      Button button2 = (Button) dialog.findViewById(R.id.button2);
	      button1.setOnClickListener(new OnClickListener() {

	          public void onClick(View paramView) {
	                  // TODOAuto-generated method stub
	          	 dialog.dismiss();
	          	 if (mProgressDialog == null ||  mProgressDialog.isShowing()) {
	     			mProgressDialog = new CustomProgressDialog(DeviceinfoActivity.this);
	     			startProgressDialog("");
	     			SettingInit settingInit = new SettingInit();
	     			settingInit.init(mContext,mActivity,mHandler);
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
			      	      	      	      	      	      	  	
		  private void startProgressDialog(String msg) {
				
				mProgressDialog.setMessage(msg);
				mProgressDialog.setCancelable(true);
				mProgressDialog.show();
				mProgressDialog.startLoading();
			}

			public static void stopProgressDialog() {
				if (mProgressDialog != null) {
					mProgressDialog.stopLoading();
					mProgressDialog.dismiss();
					mProgressDialog = null;
				}
			}
	
 
	
	private void startFactoryTest(){
		try {
    		final String MUSIC_PKG_NAME = "com.hiveview.factorytest";
            final String MUSIC_ACT_NAME = "com.hiveview.factorytest.Main";
            ComponentName componentName = new ComponentName(
                    MUSIC_PKG_NAME,
                    MUSIC_ACT_NAME);
    		Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(componentName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopProgressDialog();
	}

	private void debugTool() {
		try {
			Intent mIntent = getPackageManager().getLaunchIntentForPackage(
					"com.tool.debug");
			startActivity(mIntent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
