package com.hiveview.tv.view;

import java.util.ArrayList;
import java.util.List;

import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class SearchCategoryPopupWindow extends RelativeLayout{
	
	private PopupWindow mPopupWindow;
	private Context mContext;
	private ArrayList<String> mData;
	private final static int ITEM_WIDTH = 132;
	private final static int ITEM_HEIGHT = 132 ;
	private final static int LINEAR_SIZE = 6;
	private static int TOP_PARAMS = 0;
	private final static int ITEM_MARGIN = 12;
	
	private LinearLayout mVarietyMainControl;
	private List<LinearLayout> linearLayouts;
	public ReleasePopupWindow mReleasePopupWindow;
	private onUserSelectedListener mUserSelectedListener;
	private LinearLayout.LayoutParams linearParams;
	private static String mResultStr;
	private static String mSelectedTag;
	private static int mSelectedIndex = -1;
	private static boolean isBreak = false;
	
	public SearchCategoryPopupWindow(Context context,ArrayList<String> mTagList,String selectedTag) {
		super(context);
		mContext = context;
		mData = mTagList;
		mSelectedTag = selectedTag;
		initPopupWindow();
	}
	
	private void initPopupWindow() {
		initMyLinerLayout();
		mPopupWindow = new PopupWindow(this, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CC000000")));
		mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				mReleasePopupWindow.dismiss();
				isBreak = false;
			}
		});
	}
	
	private void initMyLinerLayout(){
		initMyTagView();
		mVarietyMainControl = new LinearLayout(mContext);
		LayoutParams mainControlParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//		mainControlParams.leftMargin = 207;
		mainControlParams.topMargin = getTopParamsByCount();
		mainControlParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mVarietyMainControl.setOrientation(LinearLayout.VERTICAL);
		for(LinearLayout linear : linearLayouts){
			mVarietyMainControl.addView(linear,linearParams);
		}
		mVarietyMainControl.requestFocus();
		this.addView(mVarietyMainControl,mainControlParams);
	}
	private void initMyTagView(){
		linearLayouts = new ArrayList<LinearLayout>();
		int size = getCount();
		mSelectedIndex = getSelectedIndex(mSelectedTag);
		for(int i=0;i<size;i++){
			if(isBreak){
				break;
			}
			LinearLayout linearLayout = new LinearLayout(mContext);
			linearParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			for(int j=0;j<LINEAR_SIZE;j++){
				int index = getTotalIndex(i,j);
				if(index == mData.size()){
					isBreak = true;
					break;
				}
				LinearLayout textLinearLayout = new LinearLayout(mContext);
				LayoutParams textLinearParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				textLinearLayout.setBackgroundResource(R.drawable.movie_list_select_normal);
				TextView textView = new TextView(mContext);
				if(i==0 && j==0){
					textView.setTag(0);
					textView.setText("全部");
					textView.requestFocus();
					if(mSelectedIndex == -1){
						textView.setTag("true,0");
						textView.setTextColor(Color.parseColor("#FF8B00"));
					}else{
						textView.setTag("false,0");
					}
				}else{
					String tag = mData.get(index);
					String content[] = tag.split(":");
					textView.setText(content[1]);
					if(index==mSelectedIndex){
						textView.setTag("true,"+content[0]);
						textView.setTextColor(Color.parseColor("#FF8B00"));
					}else{
						textView.setTag("false,"+content[0]);
					}
				}
				LayoutParams textViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				textView.setPadding(5, 5, 5, 5);
				textView.setTextSize(24);
				textView.setGravity(Gravity.CENTER);
				textView.setBackgroundResource(R.drawable.search_list_textview_bg);
				textView.getBackground().setAlpha(204);
				textView.setLayoutParams(textViewParams);
				textView.setFocusable(true);
				textView.setOnFocusChangeListener(mOnFocusChangeListener);
				textView.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if((keyCode == AppConstant.IFly_Center_KeyCode||keyCode == KeyEvent.KEYCODE_DPAD_CENTER) && event.getAction() == KeyEvent.ACTION_DOWN)
						{
							android.util.Log.i("king","KeyEvent.KEYCODE_DPAD_CENTER");
							mUserSelectedListener.onUserSelected(mResultStr);
							return true;
						}
						return false;
					}
				});
				textLinearLayout.addView(textView,textViewParams);
				linearLayout.addView(textLinearLayout,textLinearParams);
			}
			linearLayouts.add(linearLayout);
		}
	}
	
	
	private int getSelectedIndex(String selectedTag){
		for(int i = 0 ; i < mData.size() ; i++){
			if((mData.get(i).split(":"))[1].equals(selectedTag)){
				return i;
			}
		}
		return -1;
	}
	
	private int getTotalIndex(int index,int j){
		int position = index*LINEAR_SIZE+j;
		return position-1;
	}
	
	private int getCount(){
		int result = 0;
		int total = mData.size()+1;
		if(total % LINEAR_SIZE == 0){
			result = total / LINEAR_SIZE;
		}else{
			result = total / LINEAR_SIZE + 1;
		}
		return result;
	}
	
	private int getTopParamsByCount(){
		if(getCount()==1){
			TOP_PARAMS = 422 - ITEM_HEIGHT;
		}
		if(getCount() == 2 || getCount() == 3){
			TOP_PARAMS = 422 - 2*ITEM_HEIGHT - ITEM_MARGIN;
		}
		if(getCount() == 4 || getCount() == 5){
			TOP_PARAMS = 422 - 3*ITEM_HEIGHT - 2*ITEM_MARGIN;
		}
		return TOP_PARAMS;
	}
	
	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		return true;
	}
	
	private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			String tag = (String)((TextView)v).getTag();
			if(hasFocus){
				mResultStr = tag.split(",")[1]+":"+((TextView) v).getText().toString();
				((LinearLayout)v.getParent()).setBackgroundResource(R.drawable.movie_list_select_focus1);
				if(tag.split(",")[0].equals("true")){
					((TextView)v).setTextColor(Color.parseColor("#FF8B00"));
				}else{
					((TextView)v).setTextColor(Color.parseColor("#FFFFFF"));
				}
			}else{
				mResultStr = "";
				((LinearLayout)v.getParent()).setBackgroundResource(0);
				if(tag.split(",")[0].equals("false")){
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
