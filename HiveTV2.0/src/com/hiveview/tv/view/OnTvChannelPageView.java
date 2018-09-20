package com.hiveview.tv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.OnlivePlayerActivity;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.service.entity.ChannelEntity;
import com.hiveview.tv.utils.SwitchChannelUtils;

public class OnTvChannelPageView extends HiveBaseView {

	protected static final String TAG = "OnTvChannelPageView";
	private TvChannelPageViewItemView item1;
	private TvChannelPageViewItemView item2;
	private TvChannelPageViewItemView item3;
	private TvChannelPageViewItemView item4;
//	private TvChannelPageViewItemView item5;

	public OnTvChannelPageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		initView();
	}

	public OnTvChannelPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
//		initView();
	}

	public OnTvChannelPageView(Context context, int everyPageSize,int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
//		initView();
	}

	@Override
	protected void initView() {
		initViewContainer(R.layout.tv_on_channel_pageview_layout);

		item1 = (TvChannelPageViewItemView) findItemView(R.id.channel_select_item1);
		item2 = (TvChannelPageViewItemView) findItemView(R.id.channel_select_item2);
		item3 = (TvChannelPageViewItemView) findItemView(R.id.channel_select_item3);
		item4 = (TvChannelPageViewItemView) findItemView(R.id.channel_select_item4);
		//添加点击事件
		item1.setOnClickListener(new ItemViewClickListener());
		item2.setOnClickListener(new ItemViewClickListener());
		item3.setOnClickListener(new ItemViewClickListener());
		item4.setOnClickListener(new ItemViewClickListener());
	}

	@Override
	public void displayTextDataToItemView(HiveBaseEntity entity, int index) {
		ChannelEntity realEntity = (ChannelEntity) entity;
		switch (index) {
		case 0:
			setItemViewTag(realEntity, index, item1);
			item1.setTextData(realEntity);
			item1.setImageData(realEntity);
			break;
		case 1:
			setItemViewTag(realEntity, index, item2);
			item2.setTextData(realEntity);
			item2.setImageData(realEntity);
			break;
		case 2:
			setItemViewTag(realEntity, index, item3);
			item3.setTextData(realEntity);
			item3.setImageData(realEntity);
			break;
		case 3:
			setItemViewTag(realEntity, index, item4);
			item4.setTextData(realEntity);
			item4.setImageData(realEntity);
			break;
		default:
			break;
		}

	}

	@Override
	public void displayImageToItemView(HiveBaseEntity entity, int index) {
//		ChannelEntity realEntity = (ChannelEntity) entity;
//		switch (index) {
//		case 0:
//			item1.setImageData(realEntity);
//			break;
//		case 1:
//			item2.setImageData(realEntity);
//			break;
//		case 2:
//			item3.setImageData(realEntity);
//			break;
//		case 3:
//			item4.setImageData(realEntity);
//			break;
//		default:
//			break;
//		}
	}
	
	/**
	 *点击事件监听
	 *当条目被点击时调用
	 */
	class ItemViewClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			((OnlivePlayerActivity) mContext).closeMune();
			String channelName = ((TextView) v.findViewById(R.id.tv_television_name)).getText().toString();
			Log.i(TAG, "mOnChannelClickListener::onClick-->channelName:" + channelName);
			SwitchChannelUtils.switchChannel(getContext(), channelName, false, AppScene.getScene());
		}
		
	}

	@Override
	public void destoryFromViewPager() {
		item1.setVisibility(View.INVISIBLE);
		item2.setVisibility(View.INVISIBLE);
		item3.setVisibility(View.INVISIBLE);
		item4.setVisibility(View.INVISIBLE);
		item1.setImageRes();
		item2.setImageRes();
		item3.setImageRes();
		item4.setImageRes();
	}

}
