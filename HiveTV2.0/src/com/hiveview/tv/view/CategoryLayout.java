package com.hiveview.tv.view;

import com.hiveview.tv.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;


public class CategoryLayout<T> extends FrameLayout {

	private View mView;
	private ImageView mSearch;
	private ImageView mSelect;
	private ImageView mReset;
	private TextViewGallery<T> mCategory;
	
	public CategoryLayout(Context context) {
		super(context);
		init(context);
	}

	public CategoryLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public CategoryLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	@SuppressWarnings("unchecked")
	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.movie_list_popup_category_layout, null);
		
		mSearch = (ImageView) mView.findViewById(R.id.tv_enter_text_search);
		mSelect = (ImageView) mView.findViewById(R.id.tv_search);
		mReset = (ImageView) mView.findViewById(R.id.tv_reset);
		mCategory = (TextViewGallery<T>) mView.findViewById(R.id.ll_category_container);
		mView.setAlpha(204);
		this.addView(mView);
	}
	
	public View getContentView() {
		return mView;
	}
	
	public ImageView getSearchView() {
		return mSearch;
	}
	
	public ImageView getSelectView() {
		return mSelect;
	}
	
	public ImageView getResetView() {
		return mReset;
	}
	
	public TextViewGallery<T> getCategoryView() {
		return mCategory;
	}

}
