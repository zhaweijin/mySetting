package com.hiveview.cloudtv.settings;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hiveview.cloudtv.common.CommonItemAdapter;
import com.hiveview.cloudtv.common.CommonItemList;
import com.hiveview.cloudtv.settings.util.Utils;
import com.hiveview.cloudtv.settings.videoimage.AdjustActivity;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView.FocusViewAnimatorEndListener;

import android.R.integer;
import android.os.SystemProperties;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.droidlogic.app.OutputModeManager;


/**
 * @auth wangqihui
 * @category 设置影音图像功能
 * @time 2015/8/17
 * */
public class VideoImageActivity extends Activity {
	public String TAG="VideoImageActivity";
	private ListView mListView;
	List<CommonItemList> mCommonItemList = new ArrayList<CommonItemList>();
	private LauncherFocusView focusView;
	private CommonItemAdapter commonItemListAdapter = null;
	public Context mContext = this;
	
 
	private Drawable[] pageLefts = new Drawable[3];
	private Drawable[] pageRights = new Drawable[3];
	private String[] mItemSettings = new String[3];
	private String[] mArrays;
	
	
	private boolean mTextColorChangeFlag = false;
	private boolean mFocusAnimationEndFlag = false;
	private int mItemListCurPosition = -1;
	public boolean bfocusViewInitStatus = true; //列表是否为选中前状态
	private TextView mTextView = null;
	private TextView mTextViewSetting = null;
	private ImageView mImageView = null;
	private View mView;
	private long mKeyDownTime = 0L;  //按钮按住的时间长度
	private int mCurKeycode = KeyEvent.KEYCODE_0; 
	
	//列表选项参数
    private final static int AUDIO_DECODE=0;
    private final static int DISPLAY_ADJUEST=1;

    
    public  int outputType=0 ;
    private static OutputModeManager mom;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_video_image);
		mListView = (ListView) findViewById(R.id.video_image_list);
		//列表数据初始化
		this.commonItemDataInit();
		//列表展示初始化
		this.commonItemListInit();
		//绑定监听事件
		this.bindListener();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.finish();
	}
	
	//列表数据初始化
	public void commonItemDataInit(){
		
		//选择框焦点初始化
		focusView = (LauncherFocusView) findViewById(R.id.video_image_focus_view);
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
		
	    //读出列表数据
		mArrays =this.getResources().getStringArray(R.array.video_image_array);
        //音频设置 ，直接当前页面设置，配上左右选择按钮
		pageLefts[AUDIO_DECODE] = this.getResources().getDrawable(
				R.drawable.page_left);
		pageRights[AUDIO_DECODE] = this.getResources().getDrawable(
				R.drawable.page_right);
		pageRights[DISPLAY_ADJUEST] = this.getResources().getDrawable(
				R.drawable.page_right);
		
		
		mom = new OutputModeManager(this);
		String hdmiAOMode = getDigitalSoundMode();
        if(null!= hdmiAOMode && hdmiAOMode.equals(OutputModeManager.PCM))
        {
        	outputType=0;
        	Log.i(	TAG, " i am pcm " +hdmiAOMode);
        }else if(null!= hdmiAOMode && hdmiAOMode.equals(OutputModeManager.HDMI_RAW)){
        	outputType=1;
        	Log.i(	TAG, " i am hdmi "+hdmiAOMode);
        }else if(null!= hdmiAOMode && hdmiAOMode.equals(OutputModeManager.SPDIF_RAW)){
        	outputType=2;
        	Log.i(	TAG, " i am spdif "+hdmiAOMode);
        }
        Log.v(TAG, "box type=="+Utils.getBoxType());
        if(Utils.getBoxType()==Utils.DOMYBOX_10S || Utils.getBoxType()==Utils.DOMYBOX_S905X_10X 
        		|| Utils.getBoxType()==Utils.DOMYBOX_S905X_30VC || Utils.getBoxType()==Utils.DOMYBOX_S905X_IPTV||Utils.getBoxType()==Utils.DOMYBOX_S905X_40S){
        	if(outputType==2)
        		outputType = 0;  //异常被设置默认自动
        	mItemSettings[AUDIO_DECODE] = VideoImageActivity.getStringArrays(mContext, R.array.audio_decoding_array_10s,
    				outputType);
        }else if(Utils.getBoxType()==Utils.DOMYBOX_S905X_30X){
        	mItemSettings[AUDIO_DECODE] = VideoImageActivity.getStringArrays(mContext, R.array.audio_decoding_array_s905,
    				outputType);
        }
		
		
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
			commonItemListAdapter = new CommonItemAdapter(this,mCommonItemList,R.array.video_image_do);
			mListView.setAdapter(commonItemListAdapter);
		} else {
			commonItemListAdapter.notifyDataSetChanged();
		}
		mListView.setSelection(0);
	}
	
	//事件监听初始化
	public void bindListener(){
		mListView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				
				int selectItems = mListView.getSelectedItemPosition();
				View selectView = mListView.getSelectedView();
				if(selectView==null) return true;
				mCurKeycode=keyCode;
				TextView tv = (TextView) selectView.findViewById(R.id.item_setting);
				ImageView imageRightTemp = (ImageView) selectView.findViewById(R.id.page_right);
				ImageView imageLeftTemp = (ImageView) selectView.findViewById(R.id.page_left);
				//按钮按下时
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
							|| keyCode == KeyEvent.KEYCODE_DPAD_UP) {

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
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_RIGHT: {

						Log.i(TAG, "selectItems:" + selectItems);
						if (selectItems == AUDIO_DECODE) {
							imageRightTemp.setImageDrawable(mContext.getResources().getDrawable(
									R.drawable.page_right_selected));
							String[] arrays;
							if(Utils.getBoxType()==Utils.DOMYBOX_10S || Utils.getBoxType()==Utils.DOMYBOX_S905X_10X 
					        		|| Utils.getBoxType()==Utils.DOMYBOX_S905X_30VC
					        		|| Utils.getBoxType()==Utils.DOMYBOX_S905X_IPTV){
								arrays = getResources().getStringArray(R.array.audio_decoding_array_10s);
							}else if(Utils.getBoxType()==Utils.DOMYBOX_S905X_30X){
								arrays = getResources().getStringArray(R.array.audio_decoding_array_s905);
							}else {
								arrays = getResources().getStringArray(R.array.audio_decoding_array_10s);
							}
 
							Log.v(TAG, "size=="+arrays.length);
							outputType = (outputType + 1) % arrays.length ;
							
				           //本地保存值，暂时未定参数，待写
							setDigitalSoundMode(outputType);
							if(Utils.getBoxType()==Utils.DOMYBOX_10S || Utils.getBoxType()==Utils.DOMYBOX_S905X_10X 
					        		|| Utils.getBoxType()==Utils.DOMYBOX_S905X_30VC
					        		|| Utils.getBoxType()==Utils.DOMYBOX_S905X_IPTV||Utils.getBoxType()==Utils.DOMYBOX_S905X_40S){
								tv.setText(getStringArrays(mContext, R.array.audio_decoding_array_10s,
										outputType));
					        }else if(Utils.getBoxType()==Utils.DOMYBOX_S905X_30X){
					        	tv.setText(getStringArrays(mContext, R.array.audio_decoding_array_s905,
										outputType));
					        }		
						}

						break;
					}
					case KeyEvent.KEYCODE_DPAD_LEFT: {
						if (selectItems == AUDIO_DECODE) {
							imageLeftTemp.setImageDrawable(mContext.getResources().getDrawable(
									R.drawable.page_left_selected));
							String[] arrays;
							if(Utils.getBoxType()==Utils.DOMYBOX_10S || Utils.getBoxType()==Utils.DOMYBOX_S905X_10X 
					        		|| Utils.getBoxType()==Utils.DOMYBOX_S905X_30VC
						            || Utils.getBoxType()==Utils.DOMYBOX_S905X_IPTV){
								arrays = getResources().getStringArray(R.array.audio_decoding_array_10s);
							}else if(Utils.getBoxType()==Utils.DOMYBOX_S905X_30X){
								arrays = getResources().getStringArray(R.array.audio_decoding_array_s905);
							}else {
								arrays = getResources().getStringArray(R.array.audio_decoding_array_10s);
							}
							
							outputType = (outputType + 1) % arrays.length ;
							setDigitalSoundMode(outputType);
							if(Utils.getBoxType()==Utils.DOMYBOX_10S || Utils.getBoxType()==Utils.DOMYBOX_S905X_10X 
					        		|| Utils.getBoxType()==Utils.DOMYBOX_S905X_30VC
					        		|| Utils.getBoxType()==Utils.DOMYBOX_S905X_IPTV||Utils.getBoxType()==Utils.DOMYBOX_S905X_40S){
								tv.setText(getStringArrays(mContext, R.array.audio_decoding_array_10s,
										outputType));
					        }else if(Utils.getBoxType()==Utils.DOMYBOX_S905X_30X){
					        	tv.setText(getStringArrays(mContext, R.array.audio_decoding_array_s905,
										outputType));
					        }
						} 
						break;
					}
					}
				 //按钮弹起时
				} else if (KeyEvent.ACTION_UP == event.getAction()) {
					if (!mTextColorChangeFlag) {
						mTextColorChangeFlag = true;
						listTextColorSet();
					}
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_RIGHT: {
						if (selectItems == AUDIO_DECODE)
						{
							imageRightTemp.setImageDrawable(mContext.getResources().getDrawable(
									R.drawable.page_right));
					    }
						 break;
					}
					case KeyEvent.KEYCODE_DPAD_LEFT: {
						if (selectItems == AUDIO_DECODE )
						{
							imageLeftTemp.setImageDrawable(mContext.getResources().getDrawable(
									R.drawable.page_left));
						}
						break;

					}
					}
				}
				
				return false;
			}
			
		});
		
		/**
		 * @show 列表点击响应
		 */
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				if(position==DISPLAY_ADJUEST){
					startActivity(new Intent().setClass(mContext, AdjustActivity.class));
				}

			}
		});	
			
		
		/**
		 * @show 列表选中响应
		 */
		mListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> paramAdapterView, View view, int position,
					long paramLong) {
				mItemListCurPosition = position;
				mView = view;
				if (bfocusViewInitStatus) {
					focusView.initFocusView(mView, false, 0);
				}
				if (mTextView != null) {
					mTextView.setTextColor(mContext.getResources().getColor(R.color.grey5_color));
				}
				if (mTextViewSetting != null) {
					mTextViewSetting.setTextColor(mContext.getResources().getColor(
							R.color.grey5_color));
				}
				if (mImageView != null) {
					mImageView.setImageResource(R.drawable.page_right_big);
				}

				mTextView = (TextView) view.findViewById(R.id.item_name);
				if ( position == AUDIO_DECODE) {
					mTextViewSetting = (TextView) view.findViewById(R.id.item_setting);
				} else {
					mTextViewSetting = null;
				}

				if (position == DISPLAY_ADJUEST ) {
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
						mListView.setSelectionFromTop(position,
								view.getTop() - view.getHeight());

					}
				} else if (mCurKeycode == KeyEvent.KEYCODE_DPAD_UP) {
					if ((mItemListCurPosition == 0 || mListView
							.getFirstVisiblePosition() == 0 && view.getTop() > (view.getHeight()))
							|| (mListView.getFirstVisiblePosition() != 0 && view.getTop() >= view.getHeight())) {
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
	
	
	private void listTextColorSet() {
		if (mTextColorChangeFlag && mFocusAnimationEndFlag) {
			if (mTextView != null) {
				mTextView.setTextColor(this.getResources().getColor(R.color.white));
			}
			if (mTextViewSetting != null) {
				mTextViewSetting.setTextColor(this.getResources().getColor(R.color.white));
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
	
	
	
	 public String getDigitalSoundMode(){
	        String mode = OutputModeManager.PCM;
	        switch (getDigitalVoiceMode() & 0x0f) {
	            case OutputModeManager.IS_PCM:
	                mode = OutputModeManager.PCM;
	                break;
	            case OutputModeManager.IS_HDMI:
	                mode = OutputModeManager.HDMI_RAW;
	                break;
	            case OutputModeManager.IS_SPDIF:
	                mode = OutputModeManager.SPDIF_RAW;
	                break;
	            default: break;
	        }
	        return mode;
	    }

	    private int getDigitalVoiceMode(){	
	    	try {
	    		return mom.getDigitalVoiceMode2();
			} catch (NoSuchMethodError e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	    	return 0;
	    }


	    private void setDigitalSoundMode(int mode){
	        String value = OutputModeManager.PCM;
	        switch (mode) {
	            case 0:
	                value = OutputModeManager.PCM;
	                break;
	            case 1:
	                value = OutputModeManager.HDMI_RAW;
	                break;
	            case 2:
	                value = OutputModeManager.SPDIF_RAW;
	                break;
	            default:break;
	        }
	        Log.v(TAG, "set digital mode=="+value);
	        mom.setDigitalVoiceValue(value);
	    }
}
