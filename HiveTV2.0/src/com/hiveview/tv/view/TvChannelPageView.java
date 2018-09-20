package com.hiveview.tv.view;

import android.content.Context;
import android.util.AttributeSet;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.tv.R;
import com.hiveview.tv.service.entity.ChannelEntity;

public class TvChannelPageView extends HiveBaseView {

	private TvChannelPageViewItemView item1;
	private TvChannelPageViewItemView item2;
	private TvChannelPageViewItemView item3;
	private TvChannelPageViewItemView item4;
	private TvChannelPageViewItemView item5;
	private TvChannelPageViewItemView item6;
	private TvChannelPageViewItemView item7;
	private TvChannelPageViewItemView item8;
	private TvChannelPageViewItemView item9;
	private TvChannelPageViewItemView item10;
	private TvChannelPageViewItemView item11;
	private TvChannelPageViewItemView item12;
	
	public TvChannelPageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TvChannelPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TvChannelPageView(Context context, int everyPageSize, int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
	}

	@Override
	protected void initView() {
		initViewContainer(R.layout.tv_channel_pageview_layout_item);

		item1 = (TvChannelPageViewItemView) findItemView(R.id.channel_item1);
		item2 = (TvChannelPageViewItemView) findItemView(R.id.channel_item2);
		item3 = (TvChannelPageViewItemView) findItemView(R.id.channel_item3);
		item4 = (TvChannelPageViewItemView) findItemView(R.id.channel_item4);
		item5 = (TvChannelPageViewItemView) findItemView(R.id.channel_item5);
		item6 = (TvChannelPageViewItemView) findItemView(R.id.channel_item6);
		item7 = (TvChannelPageViewItemView) findItemView(R.id.channel_item7);
		item8 = (TvChannelPageViewItemView) findItemView(R.id.channel_item8);
		item9 = (TvChannelPageViewItemView) findItemView(R.id.channel_item9);
		item10 = (TvChannelPageViewItemView) findItemView(R.id.channel_item10);
		item11 = (TvChannelPageViewItemView) findItemView(R.id.channel_item11);
		item12 = (TvChannelPageViewItemView) findItemView(R.id.channel_item12);
	}

	@Override
	public void displayTextDataToItemView(HiveBaseEntity entity, int index) {
		ChannelEntity realEntity = (ChannelEntity) entity;
		switch (index) {
		case 0:
			setItemViewTag(realEntity, index, item1);
			item1.setTextData(realEntity);
			break;
		case 1:
			setItemViewTag(realEntity, index, item2);
			item2.setTextData(realEntity);
			break;
		case 2:
			setItemViewTag(realEntity, index, item3);
			item3.setTextData(realEntity);
			break;
		case 3:
			setItemViewTag(realEntity, index, item4);
			item4.setTextData(realEntity);
			break;
		case 4:
			setItemViewTag(realEntity, index, item5);
			item5.setTextData(realEntity);
			break;
		case 5:
			setItemViewTag(realEntity, index, item6);
			item6.setTextData(realEntity);
			break;
		case 6:
			setItemViewTag(realEntity, index, item7);
			item7.setTextData(realEntity);
			break;
		case 7:
			setItemViewTag(realEntity, index, item8);
			item8.setTextData(realEntity);
			break;
		case 8:
			setItemViewTag(realEntity, index, item9);
			item9.setTextData(realEntity);
			break;
		case 9:
			setItemViewTag(realEntity, index, item10);
			item10.setTextData(realEntity);
			break;
		case 10:
			setItemViewTag(realEntity, index, item11);
			item11.setTextData(realEntity);
			break;
		case 11:
			setItemViewTag(realEntity, index, item12);
			item12.setTextData(realEntity);
			break;
		default:
			break;
		}

	}

	@Override
	public void displayImageToItemView(HiveBaseEntity entity, int index) {
		ChannelEntity realEntity = (ChannelEntity) entity;
		switch (index) {
		case 0:
			item1.setImageData(realEntity);
			break;
		case 1:
			item2.setImageData(realEntity);
			break;
		case 2:
			item3.setImageData(realEntity);
			break;
		case 3:
			item4.setImageData(realEntity);
			break;
		case 4:
			item5.setImageData(realEntity);
			break;
		case 5:
			item6.setImageData(realEntity);
			break;
		case 6:
			item7.setImageData(realEntity);
			break;
		case 7:
			item8.setImageData(realEntity);
			break;
		case 8:
			item9.setImageData(realEntity);
			break;
		case 9:
			item10.setImageData(realEntity);
			break;
		case 10:
			item11.setImageData(realEntity);
			break;    
		case 11:
			item12.setImageData(realEntity);
			break;
		default:            
			break;
		}
	}

	@Override
	public void destoryFromViewPager() {

	}

}
