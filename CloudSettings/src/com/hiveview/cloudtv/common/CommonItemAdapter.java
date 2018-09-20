package com.hiveview.cloudtv.common;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hiveview.cloudtv.settings.R;

public class CommonItemAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context cont;
	private int selectItem;
	private int arrayId;
	private int[] items ;
	final static int TYPE_1 = 0;
	final static int TYPE_2 = 1;
	List<CommonItemList> mCommonItemList =null;
	class ViewHolder {
		TextView itemName;
		ImageView pageLeft;
		TextView itemSetting;
		ImageView pageRight;
	}

	/**
	 * @author wangqihui
	 * @param mCommonItemList List<CommonItemList> 列表值
	 * @param int arrayId 为Array数组id,用来记录可以在当前列表页面 直接左右选择操作的列ID
	 * */
	public CommonItemAdapter(Context context,List<CommonItemList> mCommonItemList,int arrayId) {
		super();
		cont = context;
		this.mCommonItemList=mCommonItemList;
		this.arrayId =arrayId;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int i = mCommonItemList != null ? mCommonItemList.size() : 0;
		Log.i("length", String.valueOf(i));
		return mCommonItemList != null ? mCommonItemList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setSelectItem(int position) {
		selectItem = position;
	}

	public int getSelectItem() {
		return selectItem;
	}

	public int getItemViewType(int position) {
		int p = position;
		
		if(arrayId!=0){
			items=cont.getResources().getIntArray(arrayId);
		}
	   
		if(null!=items && items.length>0){
			for(int i=0;i<items.length ;i++){
				if(p==items[i]){
					return TYPE_2;
				}
			}
		}
		
		return TYPE_1;
	}

	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder1 = null;
		ViewHolder holder2 = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case TYPE_2: {
				convertView = mInflater.inflate(R.layout.activity_item_for_common, null);
				holder1 = new ViewHolder();
				holder1.itemName = (TextView) convertView.findViewById(R.id.item_name);
				holder1.itemSetting = (TextView) convertView.findViewById(R.id.item_setting);
				holder1.pageLeft = (ImageView) convertView.findViewById(R.id.page_left);
				holder1.pageRight = (ImageView) convertView.findViewById(R.id.page_right);
				convertView.setTag(holder1);
				break;
			}
			case TYPE_1: {
				convertView = mInflater.inflate(R.layout.activity_item_forcommon2, null);
				holder2 = new ViewHolder();
				holder2.itemName = (TextView) convertView.findViewById(R.id.item_name);
				holder2.itemSetting = (TextView) convertView.findViewById(R.id.item_setting);
				holder2.pageRight = (ImageView) convertView.findViewById(R.id.page_right);
				convertView.setTag(holder2);
				break;
			}
			}
		} else {
			switch (type) {
			case TYPE_1: {
				holder2 = (ViewHolder) convertView.getTag();
				break;
			}
			case TYPE_2: {
				holder1 = (ViewHolder) convertView.getTag();
				break;
			}
			}
		}

		switch (type) {

		case (TYPE_2): {
			if (null != mCommonItemList.get(position).getItemName()) {
				holder1.itemName.setText(mCommonItemList.get(position).getItemName());

			}
			if (null != mCommonItemList.get(position).getPageLeft()) {
				holder1.pageLeft.setImageDrawable(mCommonItemList.get(position).getPageLeft());
				holder1.pageLeft.setVisibility(View.VISIBLE);
				holder1.itemSetting.setVisibility(View.VISIBLE); //显示当前分辨率

			} else {
				holder1.pageLeft.setImageDrawable(null);
				holder1.pageLeft.setVisibility(View.GONE);
			}
			if (null != mCommonItemList.get(position).getItemSetting()) {
				holder1.itemSetting.setText(mCommonItemList.get(position).getItemSetting());
				holder1.itemSetting.setVisibility(View.VISIBLE);
			
			} else {
				holder1.itemSetting.setText(null);
				holder1.itemSetting.setVisibility(View.GONE);
			}
			if (null != mCommonItemList.get(position).getPageRight()) {

				holder1.pageRight
						.setImageDrawable(mCommonItemList.get(position).getPageRight());
				holder1.pageRight.setVisibility(View.VISIBLE);

			} else {

				holder1.pageRight.setImageDrawable(null);
				holder1.pageRight.setVisibility(View.GONE);

			}
			break;
		}
		case (TYPE_1): {
			if (null != mCommonItemList.get(position).getItemName()) {
				holder2.itemName.setText(mCommonItemList.get(position).getItemName());

			}
			
			if (null != mCommonItemList.get(position).getItemSetting()) {
				holder2.itemSetting.setText(mCommonItemList.get(position).getItemSetting());
				holder2.itemSetting.setVisibility(View.VISIBLE);
			} else {
				holder2.itemSetting.setText(null);
				holder2.itemSetting.setVisibility(View.GONE);
			}
			
			if (null != mCommonItemList.get(position).getPageRight()) {

		//		holder2.pageRight
		//				.setImageDrawable(mCommonItemList.get(position).getPageRight());
				holder2.pageRight.setVisibility(View.VISIBLE);

			} else {

		//		holder2.pageRight.setImageDrawable(null);
				holder2.pageRight.setVisibility(View.GONE);

			}
			
			break;
		}
		}

		return convertView;
	}
}