package com.hiveview.tv.view;

import java.util.List;
import java.util.Map;

import com.hiveview.tv.R;
import com.hiveview.tv.view.TextViewGallery.onUserSelectedListener;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;


public class CategoryPopupWindow<T>{
	
	private CategoryLayout<T> mView;
	private TextViewGallery<T> mCategory;
	private PopupWindow mPopupWindow;
	private boolean hasData = false;
	private Context mContext;
	public ReleasePopupWindow mReleasePopupWindow;
	public CategoryPopupWindow(Context context, int width, int height) {
		init(context, width, height);
	}
	public CategoryPopupWindow(Context context) {
		init(context);
	}
	
	private void init(Context context) {
		initView(context);
		initPopupWindow();
	}
	
	private void init(Context context, int width, int height){
		initView(context);
		initPopupWindow(width, height);
	}
	
	private void initView(Context context) {
		mContext = context;
		mView = new CategoryLayout<T>(context);
		mCategory = mView.getCategoryView();
	}
	
	private void initPopupWindow(int width, int height) {
		mPopupWindow = new PopupWindow(mView, width, height);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CC000000")));
	}
	private void initPopupWindow() {
		int width = mContext.getResources().getDisplayMetrics().widthPixels;
		int height = mContext.getResources().getDisplayMetrics().heightPixels + 100;
		mPopupWindow = new PopupWindow(mView, width, height);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CC000000")));
		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				mReleasePopupWindow.dismiss();
			}
		});
		
	}
	
	public void putData(Map<Integer, List<T>> data) {
		for (Map.Entry<Integer, List<T>> entry : data.entrySet())
			mCategory.putData(entry.getKey(), entry.getValue());
		hasData = true;
	} 
	
	public void setNoData(boolean noData){
		hasData = noData;
	}
	
	public boolean hasData() {
		return hasData;
	}
	
	public void show(View parent, int gravity, int x, int y) {
		mPopupWindow.showAtLocation(parent, gravity, x, y);
	}
	
	public void dismiss() {
		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
			hasData = false;
		}
	}
	
	public void setOnUserSelectedListener(onUserSelectedListener<T> userSelectedListener){
		mCategory.setOnUserSelectedListener(userSelectedListener);
	}
	
	public TextViewGallery<T> getCategoryView(){
		return mCategory;
	}
	
	public ImageView getSearchView() {
		return mView.getSearchView();
	}

	public ImageView getConfirmView() {
		return mView.getSelectView();
	}
	
	public ImageView getResetView() {
		return mView.getResetView();
	}
	
	public void initView(String field,Class<T> clazz){
		mCategory.display(R.layout.popup_category_textview, field, clazz);
	}
	
	public void reset() {
		mCategory.reset();
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
