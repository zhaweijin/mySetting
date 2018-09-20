package com.hiveview.tv.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.utils.ToolsUtils;
import com.hiveview.tv.view.television.MarqueeText;

/**
 * @ClassName: ProjectItemViewContainer
 * @Description:
 * @author: zhangpengzhan
 * @date 2014年6月19日 下午6:09:51
 * 
 */
public class ProjectItemViewContainer extends HiveBaseView {
	// 包含整个item的view
	private View item1;
	private View item2;
	private View item3;
	private View item4;
	private View item5;
	private View item6;
	// 包行图片的view
	private View item_poster1;
	private View item_poster2;
	private View item_poster3;
	private View item_poster4;
	private View item_poster5;
	private View item_poster6;
	// 需要显示的海报图
	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	private ImageView image4;
	private ImageView image5;
	private ImageView image6;
	// 海报的名称
	private MarqueeText tv1;
	private MarqueeText tv2;
	private MarqueeText tv3;
	private MarqueeText tv4;
	private MarqueeText tv5;
	private MarqueeText tv6;
	// 更新的剧集的名称
	private TextView tv1_1;
	private TextView tv2_1;
	private TextView tv3_1;
	private TextView tv4_1;
	private TextView tv5_1;
	private TextView tv6_1;
	// 图片下载器的配置
	private DisplayImageOptions optionsPoster;

	// 构造方法
	public ProjectItemViewContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ProjectItemViewContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ProjectItemViewContainer(Context context, int everyPageSize, int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
	}

	// 初始化pageview的所有控件
	protected void initView() {
		initViewContainer(R.layout.project_page_times_layout);
		item1 = findViewById(R.id.video_item1);
		item2 = findViewById(R.id.video_item2);
		item3 = findViewById(R.id.video_item3);
		item4 = findViewById(R.id.video_item4);
		item5 = findViewById(R.id.video_item5);
		item6 = findViewById(R.id.video_item6);

		item_poster1 = findItemView(R.id.item1_poster_layout);
		item_poster2 = findItemView(R.id.item2_poster_layout);
		item_poster3 = findItemView(R.id.item3_poster_layout);
		item_poster4 = findItemView(R.id.item4_poster_layout);
		item_poster5 = findItemView(R.id.item5_poster_layout);
		item_poster6 = findItemView(R.id.item6_poster_layout);

		image1 = (ImageView) findViewById(R.id.item1_poster_iv);
		image2 = (ImageView) findViewById(R.id.item2_poster_iv);
		image3 = (ImageView) findViewById(R.id.item3_poster_iv);
		image4 = (ImageView) findViewById(R.id.item4_poster_iv);
		image5 = (ImageView) findViewById(R.id.item5_poster_iv);
		image6 = (ImageView) findViewById(R.id.item6_poster_iv);

		tv1 = (MarqueeText) findViewById(R.id.item1_title_tv);
		tv2 = (MarqueeText) findViewById(R.id.item2_title_tv);
		tv3 = (MarqueeText) findViewById(R.id.item3_title_tv);
		tv4 = (MarqueeText) findViewById(R.id.item4_title_tv);
		tv5 = (MarqueeText) findViewById(R.id.item5_title_tv);
		tv6 = (MarqueeText) findViewById(R.id.item6_title_tv);

		tv1_1 = (TextView) findViewById(R.id.updata_tv1);
		tv2_1 = (TextView) findViewById(R.id.updata_tv2);
		tv3_1 = (TextView) findViewById(R.id.updata_tv3);
		tv4_1 = (TextView) findViewById(R.id.updata_tv4);
		tv5_1 = (TextView) findViewById(R.id.updata_tv5);
		tv6_1 = (TextView) findViewById(R.id.updata_tv6);

		// 设置当前的item获取焦点的时候这是文字走马灯滚动
		item_poster1.setOnFocusChangeListener(new ItemFocusChangeListener(tv1));
		item_poster2.setOnFocusChangeListener(new ItemFocusChangeListener(tv2));
		item_poster3.setOnFocusChangeListener(new ItemFocusChangeListener(tv3));
		item_poster4.setOnFocusChangeListener(new ItemFocusChangeListener(tv4));
		item_poster5.setOnFocusChangeListener(new ItemFocusChangeListener(tv5));
		item_poster6.setOnFocusChangeListener(new ItemFocusChangeListener(tv6));
		// 创建图片下载器的配置相关
		optionsPoster = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.epg_image_default)
				.showImageOnFail(R.drawable.epg_image_default).resetViewBeforeLoading(false).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
	}

	/**
	 * 焦点变化事件
	 */
	class ItemFocusChangeListener implements OnFocusChangeListener {

		public MarqueeText tv;

		public ItemFocusChangeListener(MarqueeText tv) {
			this.tv = tv;
		}

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			FilmNewEntity entity = (FilmNewEntity) arg0.getTag();
			tv.setStart(arg1);
			tv.setText(entity.getName());
			// 获取焦点时字体颜色为白色 失去焦点时为灰色
			if (arg1) {// 获得焦点的时候显示高亮
				tv.setTextColor(0xffffffff);
			} else {// 失去焦点的时候恢复暗色
				tv.setTextColor(0xff9a9a9a);
				tv.setText(entity.getName());
			}

		}

	}

	/*
	 * 
	 * 显示海报图的文字信息 (non-Javadoc)
	 * 
	 * @see
	 * com.hiveview.box.framework.view.HiveBaseView#displayTextDataToItemView
	 * (com.hiveview.box.framework.entity.HiveBaseEntity, int)
	 */
	@Override
	public void displayTextDataToItemView(HiveBaseEntity baseEntity, int index) {
		FilmNewEntity entity = (FilmNewEntity) baseEntity;
		if (null != entity) {
			if (index == 0) {
				setItemViewTag(entity, 0, item_poster1);
				setTextData(tv1, tv1_1, entity, item1);
			} else if (index == 1) {
				setItemViewTag(entity, 1, item_poster2);
				setTextData(tv2, tv2_1, entity, item2);
			} else if (index == 2) {
				setItemViewTag(entity, 2, item_poster3);
				setTextData(tv3, tv3_1, entity, item3);
			} else if (index == 3) {
				setItemViewTag(entity, 3, item_poster4);
				setTextData(tv4, tv4_1, entity, item4);
			} else if (index == 4) {
				setItemViewTag(entity, 4, item_poster5);
				setTextData(tv5, tv5_1, entity, item5);
			} else if (index == 5) {
				setItemViewTag(entity, 5, item_poster6);
				setTextData(tv6, tv6_1, entity, item6);
			}
		}
	}

	/**
	 * 设置单个item的文字信息
	 * 
	 * @Title: ProjectItemViewContainer
	 * @author:张鹏展
	 * @Description:
	 * @param tv
	 * @param entity
	 * @param ItemView
	 */
	private void setTextData(TextView tv, TextView tv1, FilmNewEntity entity, View ItemView) {
		ItemView.setVisibility(View.VISIBLE);
		tv.setText(entity.getName());
		int allSize = entity.getTotal();
		int updateSize = entity.getCurrCount();
		// ISSERIES == entity.getIsSeries() 测试提bug 电视剧和动漫有的没有提示多集，
		// 这个字段就弃用了。直接判断他节目类型
		//爱奇艺新接口舍弃这个字段
//		if (entity.getCid() == AppConstant.VIDEO_TYPE_TELEPLAY) {
//			if (allSize == updateSize) {
//				tv1.setText(allSize + "集全");
//			} else {
//				tv1.setText("已更新至" + entity.getCurrCount() + "集");
//			}
//		} else {
//			tv1.setVisibility(View.INVISIBLE);
//		}
		tv1.setVisibility(View.INVISIBLE);
	}

	/*
	 * 设置海报图的图片信息 (non-Javadoc)
	 * 
	 * @see
	 * com.hiveview.box.framework.view.HiveBaseView#displayImageToItemView(com
	 * .hiveview.box.framework.entity.HiveBaseEntity, int)
	 */
	@Override
	public void displayImageToItemView(HiveBaseEntity baseEntity, int index) {
		FilmNewEntity entity = (FilmNewEntity) baseEntity;
		if (null != entity) {
			if (index == 0) {
				displayImage(image1, entity);
			} else if (index == 1) {
				displayImage(image2, entity);
			} else if (index == 2) {
				displayImage(image3, entity);
			} else if (index == 3) {
				displayImage(image4, entity);
			} else if (index == 4) {
				displayImage(image5, entity);
			} else if (index == 5) {
				displayImage(image6, entity);
			}
		}
	}

	/**
	 * 加载图片
	 * 
	 * @param imageView
	 *            ImagerView
	 * @param entity
	 */
	private void displayImage(ImageView imageView, FilmNewEntity entity) {
		if (null == imageLoader)// 如果图片下载器为空就初始化下载器
			imageLoader = ImageLoader.getInstance();
		if (null != entity && null != entity.getPosterUrl() && !entity.getPosterUrl().equals("")
				&& !entity.getPosterUrl().equals("null")) {// 存在图片下载路径
			imageLoader.displayImage(ToolsUtils.createImgUrl(entity.getPosterUrl(), true), imageView, optionsPoster);
		}
	}

	//
	/**
	 * 设置item的点击事件
	 * 
	 * @Title: ProjectItemViewContainer
	 * @author:张鹏展
	 * @Description:
	 * @param listener
	 */
	public void setItemViewClickListener(OnClickListener listener) {
		item_poster1.setOnClickListener(listener);
		item_poster2.setOnClickListener(listener);
		item_poster3.setOnClickListener(listener);
		item_poster4.setOnClickListener(listener);
		item_poster5.setOnClickListener(listener);
		item_poster6.setOnClickListener(listener);
	}

	/*
	 * 销毁item的方法 (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.view.HiveBaseView#destoryFromViewPager()
	 */
	public void destoryFromViewPager() {
		image1.setImageResource(R.drawable.epg_image_default);
		image2.setImageResource(R.drawable.epg_image_default);
		image3.setImageResource(R.drawable.epg_image_default);
		image4.setImageResource(R.drawable.epg_image_default);
		image5.setImageResource(R.drawable.epg_image_default);
		image6.setImageResource(R.drawable.epg_image_default);
		tv1.setText("");
		tv2.setText("");
		tv3.setText("");
		tv4.setText("");
		tv5.setText("");
		tv6.setText("");
		item1.setVisibility(View.INVISIBLE);
		item2.setVisibility(View.INVISIBLE);
		item3.setVisibility(View.INVISIBLE);
		item4.setVisibility(View.INVISIBLE);
		item5.setVisibility(View.INVISIBLE);
		item6.setVisibility(View.INVISIBLE);
	}
}
