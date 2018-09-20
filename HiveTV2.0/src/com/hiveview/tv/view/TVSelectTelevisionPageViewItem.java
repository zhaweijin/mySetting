package com.hiveview.tv.view;

import android.content.Context;
import android.util.AttributeSet;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.tv.R;
import com.hiveview.tv.service.entity.ChannelEntity;
import com.hiveview.tv.service.entity.LiveMediaEntity;

/***
 * 分类选台界面的每页显示10个频道的HiveBaseView
 * @author maliang
 * 2014-04-22
 */
public class TVSelectTelevisionPageViewItem extends HiveBaseView{

	private TvClassifyViewItemView item1;
	private TvClassifyViewItemView item2;
	private TvClassifyViewItemView item3;
	private TvClassifyViewItemView item4;
//	private TvClassifyViewItemView item5;
	private TvClassifyViewItemView item6;
	private TvClassifyViewItemView item7;
	private TvClassifyViewItemView item8;
	private TvClassifyViewItemView item9;
//	private TvClassifyViewItemView item10;

	public TVSelectTelevisionPageViewItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TVSelectTelevisionPageViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TVSelectTelevisionPageViewItem(Context context, int everyPageSize, int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
	}
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		initViewContainer(R.layout.tv_on_channel_pageview_layout_item);

		item1 = (TvClassifyViewItemView) findItemView(R.id.channel_item1);
		item2 = (TvClassifyViewItemView) findItemView(R.id.channel_item2);
		item3 = (TvClassifyViewItemView) findItemView(R.id.channel_item3);
		item4 = (TvClassifyViewItemView) findItemView(R.id.channel_item4);
//		item5 = (TvClassifyViewItemView) findItemView(R.id.channel_item5);
		item6 = (TvClassifyViewItemView) findItemView(R.id.channel_item6);
		item7 = (TvClassifyViewItemView) findItemView(R.id.channel_item7);
		item8 = (TvClassifyViewItemView) findItemView(R.id.channel_item8);
		item9 = (TvClassifyViewItemView) findItemView(R.id.channel_item9);
//		item10 = (TvClassifyViewItemView) findItemView(R.id.channel_item10);
	}

	@Override
	public void displayTextDataToItemView(HiveBaseEntity entity, int index) {
		// TODO Auto-generated method stub
		LiveMediaEntity realEntity = (LiveMediaEntity) entity;
		System.out.println("index="+index);
		switch (index) {
		case 0:
			setItemViewTag(realEntity, index, item1);
			item1.setSelectTVData(realEntity);
			break;
		case 1:
			setItemViewTag(realEntity, index, item2);
			item2.setSelectTVData(realEntity);
			break;
		case 2:
			setItemViewTag(realEntity, index, item3);
			item3.setSelectTVData(realEntity);
			break;
		case 3:
			setItemViewTag(realEntity, index, item4);
			item4.setSelectTVData(realEntity);
			break;
		case 4:
//			setItemViewTag(realEntity, index, item5);
//			item5.setSelectTVData(realEntity);
			break;
		case 5:
			setItemViewTag(realEntity, index, item6);
			item6.setSelectTVData(realEntity);
			break;
		case 6:
			setItemViewTag(realEntity, index, item7);
			item7.setSelectTVData(realEntity);
			break;
		case 7:
			setItemViewTag(realEntity, index, item8);
			item8.setSelectTVData(realEntity);
			break;
		case 8:
			setItemViewTag(realEntity, index, item9);
			item9.setSelectTVData(realEntity);
			break;
		case 9:
//			setItemViewTag(realEntity, index, item10);
//			item10.setSelectTVData(realEntity);
			break;
		default:
			break;
		}

	}

	@Override
	public void displayImageToItemView(HiveBaseEntity entity, int index) {
		// TODO Auto-generated method stub
		LiveMediaEntity realEntity = (LiveMediaEntity) entity;
		switch (index) {
		case 0:
			item1.setSelectTVImageData(realEntity);
			break;
		case 1:
			item2.setSelectTVImageData(realEntity);
			break;
		case 2:
			item3.setSelectTVImageData(realEntity);
			break;
		case 3:
			item4.setSelectTVImageData(realEntity);
			break;
		case 4:
//			item5.setSelectTVImageData(realEntity);
			break;
		case 5:
			item6.setSelectTVImageData(realEntity);
			break;
		case 6:              
			item7.setSelectTVImageData(realEntity);
			break;
		case 7:
			item8.setSelectTVImageData(realEntity);
			break;
		case 8:
			item9.setSelectTVImageData(realEntity);
			break;
		case 9:
//			item10.setSelectTVImageData(realEntity);
			break;
		default:
			break;
		}
	}

	@Override
	public void destoryFromViewPager() {
		// TODO Auto-generated method stub
		
	}

}
