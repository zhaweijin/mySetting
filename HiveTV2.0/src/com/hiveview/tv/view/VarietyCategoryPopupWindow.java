	package com.hiveview.tv.view;

import java.util.ArrayList;
import java.util.List;

import com.hiveview.tv.R;
import com.hiveview.tv.service.entity.FilmCategorySecondary;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class VarietyCategoryPopupWindow extends RelativeLayout{

	private PopupWindow mPopupWindow;
	private Context mContext;
	private List<FilmCategorySecondary> mData;
	private final static int ITEM_WIDTH = 130;
	private final static int ITEM_HEIGHT = 57 ;
	private final static int LINEAR_SIZE = 6;
	private int []TEMP_SIZE;
	private View mRootView;
	private ImageView mVarietySearchTextView;
	private LinearLayout mVarietyMainControl;
	private List<LinearLayout> linearLayouts;
	public ReleasePopupWindow mReleasePopupWindow;
	private onUserSelectedListener mUserSelectedListener;
	private static String mResultStr;
	private static String mSelectedTag;
	private boolean isSelected = true;
	private List<TextView> mTempTextView;
	private static boolean isBreak = false;
	
	public VarietyCategoryPopupWindow(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public VarietyCategoryPopupWindow(Context context,List<FilmCategorySecondary> list,String selectedTag) {
		super(context);
		mContext = context;
		mData = list;
		mSelectedTag = selectedTag;
		initPopupWindow();
	}
	
	private void initPopupWindow() {
		mRootView = LayoutInflater.from(mContext).inflate(R.layout.variety_list_popup_category_layout, null);
		mVarietySearchTextView = (ImageView)mRootView.findViewById(R.id.variety_enter_text_search);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.rightMargin = 30;
		params.topMargin = 30;
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT | RelativeLayout.ALIGN_PARENT_TOP);
		this.addView(mRootView,params);

		initMyLinerLayout();
		
		mPopupWindow = new PopupWindow(this, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CC000000")));
		mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			
			@Override
			public void onDismiss() {
				isBreak = false;
				mReleasePopupWindow.dismiss();
			}
		});
	}
	
	private void initMyLinerLayout(){
		initMyTagView();
		mVarietyMainControl = new LinearLayout(mContext);
		LayoutParams mainControlParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		mainControlParams.rightMargin = 250;
		mainControlParams.topMargin = 120;
		mainControlParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT | RelativeLayout.ALIGN_PARENT_TOP);
		mVarietyMainControl.setOrientation(LinearLayout.VERTICAL);
		for(LinearLayout linear : linearLayouts){
			mVarietyMainControl.addView(linear);
		}
		mVarietyMainControl.setBackgroundResource(R.drawable.variety_list_select_bg);
		mVarietyMainControl.getBackground().setAlpha(204);
		mVarietyMainControl.requestFocus();
		this.addView(mVarietyMainControl,mainControlParams);
	}
	private void initMyTagView(){
		linearLayouts = new ArrayList<LinearLayout>();
		int size = getCount();
		TEMP_SIZE = new int[size];
		for(int i=0;i<size;i++){
			if(isBreak){
				break;
			}
			TEMP_SIZE[i] = LINEAR_SIZE;
			LinearLayout linearLayout = new LinearLayout(mContext);
			LayoutParams linearParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			linearLayout.setLayoutParams(linearParams);
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			linearLayout.setGravity(Gravity.CENTER);
			for(int j=0;j<TEMP_SIZE[i];j++){
				int index = getTotalIndex(i,j);
				if(index == mData.size()){
					isBreak = true;
					break;
				}
				TextView textView = new TextView(mContext);
				if(i==0 && j==0){
					mTempTextView = new ArrayList<TextView>();
					mTempTextView.add(textView);
					textView.requestFocus();
				}
				LayoutParams textViewParams;
				String tag = mData.get(getTotalIndex(i,j)).getThirdclass_name();
				if(tag.length()<=4){
					textViewParams = new LayoutParams(ITEM_WIDTH,ITEM_HEIGHT);
				}else{
					textViewParams = new LayoutParams(ITEM_WIDTH*2,ITEM_HEIGHT);
					TEMP_SIZE[i]=TEMP_SIZE[i]-1;
					if(j==TEMP_SIZE[i]){
						FilmCategorySecondary fcs = mData.get(getTotalIndex(i,j));
						mData.remove(getTotalIndex(i,j));
						mData.add(fcs);
						j--;
						continue;
					}
				}
				textView.setTag(false);
				if(tag.equals(mSelectedTag)){
					textView.setTextColor(Color.parseColor("#FF8B00"));
					textView.setTag(true);
					isSelected = false;
				}
				textView.setText(tag);
				textView.setPadding(5, 5, 5, 5);
				textView.setTextSize(24);
				textView.setGravity(Gravity.CENTER);
				textView.setLayoutParams(textViewParams);
				textView.setFocusable(true);
				textView.setOnFocusChangeListener(mOnFocusChangeListener);
				textView.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.getAction() == KeyEvent.ACTION_DOWN)
						{
							mUserSelectedListener.onUserSelected(mResultStr);
							return true;
						}
						return false;
					}
				});
				linearLayout.addView(textView);
			}
			linearLayouts.add(linearLayout);
		}
		if(isSelected){
			mTempTextView.get(0).setTextColor(Color.parseColor("#FF8B00"));
			mTempTextView.get(0).setTag(true);
		}
	}
	
	private int getTotalIndex(int index,int j){
		int position = 0;
		for(int i=0;i<index;i++){
			position +=TEMP_SIZE[i];
		}
		return (position + j);
	}
	
	private int getCount(){
		int result = 0;
		int total = mData.size();
		for(FilmCategorySecondary fcs : mData){
			if(fcs.getThirdclass_name().length()>4){
				total++;
			}
		}
		if(total % LINEAR_SIZE == 0){
			result = total / LINEAR_SIZE;
		}else{
			result = total / LINEAR_SIZE + 1;
		}
		return result;
	}
	
	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		return true;
	}
	
	private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				mResultStr = ((TextView) v).getText().toString();
				v.setBackgroundResource(R.drawable.movie_list_select_focus);
				if((Boolean)v.getTag()){
					((TextView)v).setTextColor(Color.parseColor("#FF8B00"));
				}else{
					((TextView)v).setTextColor(Color.parseColor("#FFFFFF"));
				}
			}else{
				mResultStr = "";
				v.setBackgroundResource(0);
				if(!(Boolean)v.getTag()){
					((TextView) v).setTextColor(Color.parseColor("#9A9A9A"));
				}
			}
		}
	};
	
	public void show(View parent, int gravity, int x, int y) {
		mPopupWindow.showAtLocation(parent, gravity, x, y);
	}
	
	public void dismiss() {
		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}
	
	public void setOnUserSelectedListener(onUserSelectedListener userSelectedListener){
		mUserSelectedListener = userSelectedListener;
	}
	
	public interface onUserSelectedListener{
		public void onUserSelected(String resultData);
	}
	
	public ImageView getSearchView() {
		return mVarietySearchTextView;
	}

	public boolean isShowing() {
		return mPopupWindow.isShowing();
	}
	
	public void setReleasePopupWindow(ReleasePopupWindow releasePopupWindow){
		mReleasePopupWindow = releasePopupWindow;
	}
	public interface ReleasePopupWindow{
		public void dismiss();
	}

}
