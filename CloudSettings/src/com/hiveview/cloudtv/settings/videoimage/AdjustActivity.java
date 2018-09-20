package com.hiveview.cloudtv.settings.videoimage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hiveview.cloudtv.common.CommonItemAdapter;
import com.hiveview.cloudtv.common.CommonItemList;
import com.hiveview.cloudtv.settings.R;
import com.hiveview.cloudtv.settings.util.ImageSharePreference;
import com.hiveview.cloudtv.settings.util.Utils;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView.FocusViewAnimatorEndListener;
import com.hiveview.cloudtv.settings.widget.ResolutionDialog;
import com.hiveview.cloudtv.settings.widget.SubmitDialog;
import com.hiveview.cloudtv.settings.widget.SubmitDialog.OnDismiss;
import com.hiveview.cloudtv.settings.widget.SubmitDialog.onResolution;
//import android.app.SystemWriteManager;
//import com.hiveview.cloudtv.common.HdmiInfo;
//import android.os.display.DisplayManager;

/**
 * 
 * @author wangqihui
 * @category 显示设置调整功能
 * @time 2015/8/17
 **/
public class AdjustActivity extends Activity implements OnDismiss {
	public String TAG = "AdjustActivity";
	private ListView mListView;
	List<CommonItemList> mCommonItemList = new ArrayList<CommonItemList>();
	private LauncherFocusView focusView;
	private CommonItemAdapter commonItemListAdapter = null;
	public Context mContext = this;
	private Drawable[] pageLefts = new Drawable[2];
	private Drawable[] pageRights = new Drawable[2];
	private String[] mItemSettings = new String[2];
	private String[] mArrays;
	private ImageSharePreference mPreference;

	private boolean mTextColorChangeFlag = false;
	private boolean mFocusAnimationEndFlag = false;
	private int mItemListCurPosition = -1;
	public boolean bfocusViewInitStatus = true; // 列表是否为选中前状态
	private TextView mTextView = null;
	private TextView mTextViewSetting = null;
	
	TextView showtext=null; //制式切换回原来要改的 textview	
	private ImageView mImageView = null;
	private View mView;
	private long mKeyDownTime = 0L; // 按钮按住的时间长度
	private int mCurKeycode = KeyEvent.KEYCODE_0;

	private final static int RESOLUTION_ADJUSTMENT = 0;
	private final static int MANUALLY_ADJUSTMENT = 1;
	
	public static final int ROLLBACK_TIEM = 30;
	private static int mCount = ROLLBACK_TIEM;

	public final static int DISPLAY_STANDARD_1080P_60 = 0;
	public final static int DISPLAY_STANDARD_1080P_50 = 1;
	public final static int DISPLAY_STANDARD_1080P_30 = 2;
	public final static int DISPLAY_STANDARD_1080P_25 = 3;
	public final static int DISPLAY_STANDARD_1080P_24 = 4;
	public final static int DISPLAY_STANDARD_1080I_60 = 5;
	public final static int DISPLAY_STANDARD_1080I_50 = 6;
	public final static int DISPLAY_STANDARD_720P_60 = 7;
	public final static int DISPLAY_STANDARD_720P_50 = 8;
	  private boolean isSetResolution = false ;
	
	private String randomNum;
	public int getResolution() {
		return resolution;
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	private int[] mAllSupportStandards;
	private int mCurrentStandard;

	private final static String mOutputStatusConfig = "/sys/class/amhdmitx/amhdmitx0/disp_cap";
	private final static String mCurrentResolution = "/sys/class/display/mode";
	private final String ACTION_OUTPUTMODE_SAVE = "android.intent.action.OUTPUTMODE_SAVE";
	private int resolution = -1;
	private int backresolution;
	
	private final String OUTPUT_MODE = "output_mode";
	private static final String FREQ_DEFAULT = "";//60hz
	private static final String FREQ_SETTING = "50hz";
	private static final String PREFERENCE_AUTO_OUTPUT_MODE = "hiveview_pref";
	private static final String PROPERTY_AUTO_OUTPUT_MODE = "auto.output.mode.property";
	private int sel_index = -1 ;
    private String[] mEntryValues;
//	public static SystemWriteManager sw; 
	private static final int GET_USER_OPERATION=1;    
	private ArrayList<String> mTitleList = new ArrayList<String>();
	private ArrayList<String> mValueList = new ArrayList<String>();
	public OutputUiManager outputManager ;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_adjust_setting);
//		sw = (SystemWriteManager)getApplicationContext().getSystemService("system_write");
		mListView = (ListView) findViewById(R.id.adjust_setting_list);
		// 列表数据初始化
		this.commonItemDataInit();
		// 列表展示初始化
		this.commonItemListInit();
		// 绑定监听事件
		this.bindListener();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.finish();
	}

	// 列表数据初始化
	public void commonItemDataInit() {
		mPreference = new ImageSharePreference(mContext);
		mEntryValues = getResources().getStringArray(R.array.outputmode_entries_display);
		
		 outputManager =new OutputUiManager(this);
		resolution =outputManager.getCurrentModeIndex();
		backresolution =resolution;
		mTitleList =outputManager.getOutputmodeTitleList();
		mValueList=outputManager.getOutputmodeValueList();
		mItemSettings[0]=mTitleList.get(resolution);
		// 选择框焦点初始化
		focusView = (LauncherFocusView) findViewById(R.id.adjust_setting_focus_view);
		// mRelative = (RelativeLayout) findViewById(R.id.activity_common_id);
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

		// 读出列表数据
		mArrays = this.getResources().getStringArray(
				R.array.adjust_setting_array);

		Log.i(TAG, "standard :" + mCurrentStandard);

		// 显示调整 ，直接当前页面设置，配上左右选择按钮
		pageLefts[0] = this.getResources().getDrawable(R.drawable.page_left);
		pageRights[0] = this.getResources().getDrawable(R.drawable.page_right);

		pageRights[MANUALLY_ADJUSTMENT] = this.getResources().getDrawable(R.drawable.page_right);
		
		for (int i = 0; i < mArrays.length; i++) {
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

	// 列表显示初始化
	public void commonItemListInit() {
		if (commonItemListAdapter == null) {
			commonItemListAdapter = new CommonItemAdapter(this,
					mCommonItemList, R.array.adjust_setting_do);
			mListView.setAdapter(commonItemListAdapter);
		} else {
			commonItemListAdapter.notifyDataSetChanged();
		}
		mListView.setSelection(0);
	}

	// 事件监听初始化
	public void bindListener() {
		mListView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {

				int selectItems = mListView.getSelectedItemPosition();
				View selectView = mListView.getSelectedView();
				if (selectView == null)
					return true;
				mCurKeycode = keyCode;
				TextView tv = (TextView) selectView
						.findViewById(R.id.item_setting);
				ImageView imageRightTemp = (ImageView) selectView
						.findViewById(R.id.page_right);
				ImageView imageLeftTemp = (ImageView) selectView
						.findViewById(R.id.page_left);
				// 按钮按下时
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
							|| keyCode == KeyEvent.KEYCODE_DPAD_UP) {

						if (event.getRepeatCount() == 0) {
							mTextColorChangeFlag = true;
							mKeyDownTime = event.getDownTime();
						} else {
							mTextColorChangeFlag = false;
							if (event.getDownTime() - mKeyDownTime >= Utils.KEYDOWN_DELAY_TIME) {
								Log.e("KeyEvent",
										"time="
												+ (event.getDownTime() - mKeyDownTime)
												+ " count"
												+ event.getRepeatCount());
								mKeyDownTime = event.getDownTime();
							} else {
								return true;
							}
						}

						if (!mFocusAnimationEndFlag) {
							mTextColorChangeFlag = false;
						}
					}
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_RIGHT: {
						if (selectItems == RESOLUTION_ADJUSTMENT) {
						Log.i(TAG, "selectItems:" + selectItems);
						imageRightTemp.setImageDrawable(mContext.getResources().getDrawable(
								R.drawable.page_right_selected));
						resolution = (resolution + 1) % mTitleList.size() ;
						tv.setText(mTitleList.get(resolution));

						break;
						}
					}
					case KeyEvent.KEYCODE_DPAD_LEFT: {
						if (selectItems == RESOLUTION_ADJUSTMENT) {
							imageLeftTemp.setImageDrawable(mContext.getResources().getDrawable(
									R.drawable.page_left_selected));
							 if(resolution ==0){
									resolution=mTitleList.size()-1;
								}else {
									resolution=resolution - 1 ;
								}
							tv.setText(mTitleList.get(resolution));
						break;
						}
					}
					}
					// 按钮弹起时
				} else if (KeyEvent.ACTION_UP == event.getAction()) {
					if (!mTextColorChangeFlag) {
						mTextColorChangeFlag = true;
						listTextColorSet();
					}
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_RIGHT: {
						if (selectItems == RESOLUTION_ADJUSTMENT) {
							imageRightTemp.setImageDrawable(mContext
									.getResources().getDrawable(
											R.drawable.page_right));
						}
						break;
					}
					case KeyEvent.KEYCODE_DPAD_LEFT: {
						if (selectItems == RESOLUTION_ADJUSTMENT) {
							imageLeftTemp.setImageDrawable(mContext
									.getResources().getDrawable(
											R.drawable.page_left));
						}
						break;

					}
					}
				}

				return false;
			}

		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				View selectView = mListView.getSelectedView();
				showtext= (TextView) selectView.findViewById(R.id.item_setting);
				if (position == MANUALLY_ADJUSTMENT) {
					startActivity(new Intent().setClass(mContext,
							PositionSetting.class));
				}else if(position==RESOLUTION_ADJUSTMENT){
					 if(resolution==backresolution){
						 return;
					 }
					 dialog(showtext) ;
				}
			}
		});
		mListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> paramAdapterView,
					View view, int position, long paramLong) {
				mItemListCurPosition = position;
				mView = view;
				if (bfocusViewInitStatus) {
					focusView.initFocusView(mView, false, 0);
				}
				if (mTextView != null) {
					mTextView.setTextColor(mContext.getResources().getColor(
							R.color.grey5_color));
				}
				if (mTextViewSetting != null) {
					mTextViewSetting.setTextColor(mContext.getResources()
							.getColor(R.color.grey5_color));
				}
				if (mImageView != null) {
					mImageView.setImageResource(R.drawable.page_right_big);
				}

				mTextView = (TextView) view.findViewById(R.id.item_name);
				if (position == RESOLUTION_ADJUSTMENT) {
					mTextViewSetting = (TextView) view
							.findViewById(R.id.item_setting);
				} else {
					mTextViewSetting = null;
				}

				if (position == MANUALLY_ADJUSTMENT) {
					mImageView = (ImageView) view.findViewById(R.id.page_right);
				} else {
					mImageView = null;
				}

				if (mCurKeycode == KeyEvent.KEYCODE_DPAD_DOWN) {

					if (position < 5
							|| position > mListView.getCount() - 2
							|| (mListView.getFirstVisiblePosition() == 0 && view
									.getTop() < (view.getHeight() * 4))
							|| (mListView.getFirstVisiblePosition() != 0 && view
									.getTop() < view.getHeight() * 5)) {
						focusView.moveTo(mView);
					} else {
						listTextColorSet();
						mListView.setSelectionFromTop(position, view.getTop()
								- view.getHeight());

					}
				} else if (mCurKeycode == KeyEvent.KEYCODE_DPAD_UP) {
					if ((mItemListCurPosition == 0 || mListView
							.getFirstVisiblePosition() == 0
							&& view.getTop() > (view.getHeight()))
							|| (mListView.getFirstVisiblePosition() != 0 && view
									.getTop() >= view.getHeight())) {
						focusView.moveTo(mView);
					} else {
						listTextColorSet();
						mListView.setSelectionFromTop(mItemListCurPosition,
								view.getHeight());
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
					if ((mItemListCurPosition == 0 || mItemListCurPosition == mListView
							.getCount() - 1)) {
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

	private void listTextColorSet() {
		if (mTextColorChangeFlag && mFocusAnimationEndFlag) {
			if (mTextView != null) {
				mTextView.setTextColor(this.getResources().getColor(
						R.color.white));
			}
			if (mTextViewSetting != null) {
				mTextViewSetting.setTextColor(this.getResources().getColor(
						R.color.white));
			}
			if (mImageView != null) {
				mImageView.setImageResource(R.drawable.page_right_big_selected);
			}
			mTextColorChangeFlag = false;

		}
	}

	public static String getStringArrays(Context mContext, int id, int position) {
		String[] arrays = mContext.getResources().getStringArray(id);
		if (null != arrays) {
			return arrays[position];
		}
		return "";
	}

	public String getBestMatchResolution() {
		ArrayList<String> resolutionList = new ArrayList<String>();

		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		String readLine = null;

		try {
			fileReader = new FileReader(mOutputStatusConfig);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		bufferedReader = new BufferedReader(fileReader);

		try {
			while ((readLine = bufferedReader.readLine()) != null) {
				resolutionList.add(readLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			bufferedReader.close();
			fileReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (resolutionList.isEmpty()) {
			return "720p";
		}

		for (int index = 0; index < resolutionList.size(); index++) {
			if (resolutionList.get(index).contains("*")) {
				return resolutionList.get(index);
			}
		}

		return "720p";
	}

	private void setOutputResolution(String resolution) {
		Intent change_intent = new Intent(
				"android.intent.action.OUTPUTMODE_CHANGE");
		Intent save_intent = new Intent(ACTION_OUTPUTMODE_SAVE);

		String newMode = null;
		String currentMode = null;

		FileReader fileReader = null;
		try {
			fileReader = new FileReader(mCurrentResolution);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		BufferedReader bufferedReader = null;
		bufferedReader = new BufferedReader(fileReader);

		try {
			currentMode = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Force to set to 720p
		if (resolution == null) {
			change_intent.putExtra(OUTPUT_MODE, "720p" + FREQ_DEFAULT);
			save_intent.putExtra(OUTPUT_MODE, "720p" + FREQ_DEFAULT);
		} else {
			if (resolution.contains("480i")) {
				change_intent.putExtra(OUTPUT_MODE, "480i");
				save_intent.putExtra(OUTPUT_MODE, "480i");
			} else if (resolution.contains("480p")) {
				change_intent.putExtra(OUTPUT_MODE, "480p");
				save_intent.putExtra(OUTPUT_MODE, "480p");
			} else if (resolution.contains("576i")) {
				change_intent.putExtra(OUTPUT_MODE, "576i");
				save_intent.putExtra(OUTPUT_MODE, "576i");
			} else if (resolution.contains("576p")) {
				change_intent.putExtra(OUTPUT_MODE, "576p");
				save_intent.putExtra(OUTPUT_MODE, "576p");
			} else if (resolution.contains("720p")) {
				if (resolution.contains(FREQ_SETTING)) {
					change_intent.putExtra(OUTPUT_MODE, "720p" + FREQ_SETTING);
					save_intent.putExtra(OUTPUT_MODE, "720p" + FREQ_SETTING);
				} else {
					change_intent.putExtra(OUTPUT_MODE, "720p" + FREQ_DEFAULT);
					save_intent.putExtra(OUTPUT_MODE, "720p" + FREQ_DEFAULT);
				}
			} else if (resolution.contains("1080i")) {
				if (resolution.contains(FREQ_SETTING)) {
					change_intent.putExtra(OUTPUT_MODE, "1080i" + FREQ_SETTING);
					save_intent.putExtra(OUTPUT_MODE, "1080i" + FREQ_SETTING);
				} else {
					change_intent.putExtra(OUTPUT_MODE, "1080i" + FREQ_DEFAULT);
					save_intent.putExtra(OUTPUT_MODE, "1080i" + FREQ_DEFAULT);
				}
			} else if (resolution.contains("1080p")) {
				if (resolution.contains(FREQ_SETTING)) {
					change_intent.putExtra(OUTPUT_MODE, "1080p" + FREQ_SETTING);
					save_intent.putExtra(OUTPUT_MODE, "1080p" + FREQ_SETTING);
				} else {
					change_intent.putExtra(OUTPUT_MODE, "1080p" + FREQ_DEFAULT);
					save_intent.putExtra(OUTPUT_MODE, "1080p" + FREQ_DEFAULT);
				}
			}
		}

		newMode = change_intent.getStringExtra(OUTPUT_MODE);

		if (currentMode != null && newMode != null) {
			if (currentMode.equals(newMode)) {
				return;
			}
		}

		sendBroadcast(change_intent);
		sendBroadcast(save_intent);
	}

	@SuppressWarnings("resource")
	public String getCurrentMode(){
		
		String newMode = null;
		String currentMode = null;

		FileReader fileReader = null;
		try {
			fileReader = new FileReader(mCurrentResolution);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		BufferedReader bufferedReader = null;
		bufferedReader = new BufferedReader(fileReader);

		try {
			return currentMode = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			return getBestMatchResolution();
		}
	}
	private void setResolution()
	{
	/*/	String old_mode = sw.readSysfs(mCurrentResolution);
		if(!old_mode.equals(Config.Display2LogicEntry(mEntryValues[sel_index])))
		{
			Intent intent = new Intent(this, OutputSetConfirm.class);
			intent.putExtra("set_mode", Config.Display2LogicEntry(mEntryValues[sel_index] + ""));
			Config.Logd(getClass().getName(),  Config.Display2LogicEntry(mEntryValues[sel_index] + ""));
			startActivityForResult(intent, GET_USER_OPERATION);		
		}*/
    
	}
	
	

	public void dialog( final TextView tv) {
		
		 final ResolutionDialog dialog = new ResolutionDialog(mContext);
         Window dialogWindow = dialog.getWindow();
         WindowManager.LayoutParams lp = dialogWindow.getAttributes();
         lp.width = getResources().getDimensionPixelSize(R.dimen.adjust_display_width);
         lp.height = getResources().getDimensionPixelSize(R.dimen.adjust_display_height);
         dialogWindow.setAttributes(lp);
         dialog.show();
         TextView message = (TextView) dialog
                         .findViewById(R.id.message1);
         message.setText(getString(R.string.display_toast));

         Button button1 = (Button) dialog.findViewById(R.id.button1);
         Button button2 = (Button) dialog.findViewById(R.id.button2);
         button1.setOnClickListener(new OnClickListener() {
			
		 public void onClick(View paramView) {
			      dialog.dismiss();
			      Log.v(TAG, "set out put==="+mValueList.get(resolution));
			      outputManager.change2NewMode(mValueList.get(resolution)); 
				  startTimer();
				  dialog2();
			 } 
		 });
		 
         button2.setOnClickListener(new OnClickListener() {

 			@Override
 		 public void onClick(View v) {
              dialog.dismiss();
              resetTimer();
         }
 		 });
	}
	
	
	
	public SubmitDialog editdialog;
	public void dialog2() {
		
		
		randomNum =this.getRandom();
		
		editdialog = new com.hiveview.cloudtv.settings.widget.SubmitDialog(mContext, 
			AdjustActivity.this,randomNum);
		editdialog.setResolutionListener(new onResolution() {
			
			@Override
			public void setResolution() {
				// TODO Auto-generated method stub
			//	backresolution
				Log.d("AdjustActivity","backresolution:" + backresolution);
				rollBack(showtext);
			}
		});
		editdialog.show();
		
	
	}
 
	
	
	private Runnable run = new Runnable() {

		public void run() {
			mCount--;
			if (mCount > 0) {
				mHandler.postDelayed(run, 1000L);
			} else {
				rollBack(showtext);
				hideDialog();
				resetTimer();
			}
		}

	};
	
	private void rollBack(TextView tv) {
		if(tv!=null){
			 outputManager.change2NewMode(mValueList.get(backresolution)); 
			 resolution=backresolution;
			 tv.setText(mTitleList.get(backresolution));
		}
	}

	private void resetTimer() {
		mCount = ROLLBACK_TIEM;
		mHandler.removeCallbacks(run);
	}
	
	private void startTimer() {
		mHandler.removeCallbacks(run);
		mHandler.postDelayed(run, 1000L);
	}
	
	private void hideDialog() {
		if(editdialog!=null){
			   Log.i(TAG,"domiss");
			   editdialog.dismiss();
		}
	}
	
	private Handler mHandler = new Handler() {};

	

	@Override
	public void setOnDismiss(String value) {
		// TODO Auto-generated method stub
		if(randomNum.equals(value)){
				isSetResolution = true ;
			  editdialog.dismiss();
		      backresolution =resolution; 
		      resetTimer();
		}else {
			  editdialog.dismiss();
	          rollBack(showtext);
			  resetTimer();
		}
	}
	
	/* (非 Javadoc) 
	* <p>Title: onPause</p> 
	* <p>Description: </p>  
	* @see android.app.Activity#onPause() 
	*/
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(!isSetResolution){
			Log.d(TAG,"submit faild");
			rollBack(showtext);
			resetTimer();
		}else{
			Log.d(TAG,"submit success");
		}
		
		super.onPause();
		
	}

	/*
	 * 取 100到999的随机数
	 */
	public String getRandom(){
		int max = 999;
		int min = 100;
		Random rand = new Random();
		int num = rand.nextInt(max - min + 1) + min;
		return String.valueOf(num);
	}
	
	
}
