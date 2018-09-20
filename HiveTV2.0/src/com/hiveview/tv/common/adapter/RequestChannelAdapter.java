package com.hiveview.tv.common.adapter;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class RequestChannelAdapter extends PagerAdapter {
	public List<View> mViewList;
	private View mCurrentView;

	public RequestChannelAdapter(List<View> vlist) {
		mViewList = vlist;
	}

	@Override
	public int getCount() {
		if (null != mViewList) {
			return mViewList.size();
		}
		return 0;
	}

	@Override
	public Object instantiateItem(View view, int index) {
		((ViewPager) view).addView(mViewList.get(index), 0);
		return mViewList.get(index);
	}

	@Override
	public void finishUpdate(View arg0) {

	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return (view == obj);

	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		mCurrentView = (View) object;
	}

	@Override
	public void destroyItem(View view, int position, Object arg2) {
		((ViewPager) view).removeView((View) arg2);
	}

	public View getPrimaryItem() {
		return mCurrentView;
	}

}
