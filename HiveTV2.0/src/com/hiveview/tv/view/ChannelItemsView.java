package com.hiveview.tv.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.content.ChannelInvoker;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.ItemType;
import com.hiveview.tv.common.statistics.SimpleOnClickListener;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.service.entity.FirstClassListEntity;
import com.hiveview.tv.utils.LogUtil;

public class ChannelItemsView extends FrameLayout {
	protected DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.video_channels_default)
			.showImageOnFail(R.drawable.video_channels_default).resetViewBeforeLoading(false).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();

	private Context mContext;
	private View vContainer;

	private LinearLayout layoutVideoType1;
	private LinearLayout layoutVideoType2;
	private LinearLayout layoutVideoType3;
	private LinearLayout layoutVideoType4;
	private LinearLayout layoutVideoType5;
	private LinearLayout layoutVideoType6;
	private LinearLayout layoutVideoType7;
	private LinearLayout layoutVideoType8;
	private LinearLayout layoutVideoType9;
	private LinearLayout layoutVideoType10;
	private LinearLayout layoutVideoType11;
	private LinearLayout layoutVideoType12;
	private LinearLayout layoutVideoType13;
	private LinearLayout layoutVideoType14;
	private LinearLayout layoutVideoType15;
	private LinearLayout layoutVideoType16;
	private LinearLayout layoutVideoType17;
	private LinearLayout layoutVideoType18;
	private ArrayList<LinearLayout> ivTypeList;

	public ChannelItemsView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public ChannelItemsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public ChannelItemsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		ivTypeList = new ArrayList<LinearLayout>();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vContainer = inflater.inflate(R.layout.channel_items_layout, null);

		layoutVideoType1 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_1);
		layoutVideoType2 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_2);
		layoutVideoType3 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_3);
		layoutVideoType4 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_4);
		layoutVideoType5 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_5);
		layoutVideoType6 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_6);
		layoutVideoType7 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_7);
		layoutVideoType8 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_8);
		layoutVideoType9 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_9);
		layoutVideoType10 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_10);
		layoutVideoType11 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_11);
		layoutVideoType12 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_12);
		layoutVideoType13 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_13);
		layoutVideoType14 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_14);
		layoutVideoType15 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_15);
		layoutVideoType16 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_16);
		layoutVideoType17 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_17);
		layoutVideoType18 = (LinearLayout) vContainer.findViewById(R.id.layout_videotype_18);

		ivTypeList.add(layoutVideoType1);
		ivTypeList.add(layoutVideoType2);
		ivTypeList.add(layoutVideoType3);
		ivTypeList.add(layoutVideoType4);
		ivTypeList.add(layoutVideoType5);
		ivTypeList.add(layoutVideoType6);
		ivTypeList.add(layoutVideoType7);
		ivTypeList.add(layoutVideoType8);
		ivTypeList.add(layoutVideoType9);
		ivTypeList.add(layoutVideoType10);
		ivTypeList.add(layoutVideoType11);
		ivTypeList.add(layoutVideoType12);
		ivTypeList.add(layoutVideoType13);
		ivTypeList.add(layoutVideoType14);
		ivTypeList.add(layoutVideoType15);
		ivTypeList.add(layoutVideoType16);
		ivTypeList.add(layoutVideoType17);
		ivTypeList.add(layoutVideoType18);

		addView(vContainer);
	}

	public void setDataSource(ArrayList<FirstClassListEntity> firstClasses) {
		if (null == firstClasses || firstClasses.size() == 0) {
			LogUtil.info("data is error!!!!");
			return;
		}

		for (int i = 0; i < firstClasses.size(); i++) {
			FirstClassListEntity entity = firstClasses.get(i);
			LinearLayout layout = ivTypeList.get(i);
			layout.setVisibility(View.VISIBLE);
			layout.setOnClickListener(channelListener);
			layout.setTag(entity);
			ImageView image = (ImageView) layout.getChildAt(0);
			ImageLoader.getInstance().displayImage(entity.getIcon(), image, options);
			
			LogUtil.info(entity.getIcon());

		}

	}

	private OnClickListener channelListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			FirstClassListEntity entity = (FirstClassListEntity) view.getTag();
			// start bu author:zhangpengzhan
			// 如果数据不在影射表中的情况，都是按照横图直接播放打开设置
			String action = (null == ChannelInvoker.getInstance().getContent(entity.getFirstclass_id())) ? ChannelInvoker.VIEDEO_HORIZONTAL_LIST_ACTION : ChannelInvoker
					.getInstance().getContent(entity.getFirstclass_id());
			// end zhangpengzhawn
			Intent intent = new Intent(action);
			LogUtil.info(action + "点播的入口束图列表");
			// com.hiveview.video.list.VERTICAL点播电影入口
			// com.hiveview.video.list.VERTICAL点播综艺入口
			// com.hiveview.video.list.VERTICAL点播电视剧入口
			// com.hiveview.video.list.VERTICAL点播动漫入口

			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.putExtra("category_id", entity.getFirstclass_id());
			intent.putExtra("category_name", entity.getFirstclass_name());
			getContext().startActivity(intent);

		}
	};

	// 统计的，暂时没有用到
	class RecommendRecordLayoutListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {
			FirstClassListEntity entity = (FirstClassListEntity) view.getTag();
			String action = ChannelInvoker.getInstance().getContent(entity.getFirstclass_id());
			Intent intent = new Intent(action);

			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.putExtra("category_id", entity.getFirstclass_id());
			intent.putExtra("category_name", entity.getFirstclass_name());
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_FIRST_CLASS).setEntity(entity).setSenceName(AppScene.getScene()).setSrcType(ItemType.VIDEO)
					.setTabNo(Tab.APP).build();
		}
	}

}
