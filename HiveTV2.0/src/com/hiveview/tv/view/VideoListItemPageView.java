package com.hiveview.tv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.tv.R;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.utils.StringUtils;
import com.hiveview.tv.utils.ToolsUtils;
import com.hiveview.tv.view.television.MarqueeText;

public class VideoListItemPageView extends HiveBaseView {

	private View item1;
	private View item2;
	private View item3;
	private View item4;
	private View item5;
	private View item6;
	private View item7;
	private View item8;
	private View item9;
	private View item10;
	private View item11;
	private View item12;
	private final static String TAG = "VideoListItemPageView";
	private View item_poster1;
	private View item_poster2;
	private View item_poster3;
	private View item_poster4;
	private View item_poster5;
	private View item_poster6;
	private View item_poster7;
	private View item_poster8;
	private View item_poster9;
	private View item_poster10;
	private View item_poster11;
	private View item_poster12;

	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	private ImageView image4;
	private ImageView image5;
	private ImageView image6;
	private ImageView image7;
	private ImageView image8;
	private ImageView image9;
	private ImageView image10;
	private ImageView image11;
	private ImageView image12;

	private MarqueeText tv1;
	private MarqueeText tv2;
	private MarqueeText tv3;
	private MarqueeText tv4;
	private MarqueeText tv5;
	private MarqueeText tv6;
	private MarqueeText tv7;
	private MarqueeText tv8;
	private MarqueeText tv9;
	private MarqueeText tv10;
	private MarqueeText tv11;
	private MarqueeText tv12;

	private FilmNewEntity mEntity;
	public GetFocusEntity focusEntity;
	/**
	 * 图片下载器
	 */
	protected ImageLoader imageLoader = null;
	private DisplayImageOptions optionsPoster;

	public GetFocusEntity getFocusEntity() {
		return focusEntity;
	}

	public void setFocusEntity(GetFocusEntity focusEntity) {
		this.focusEntity = focusEntity;
	}

	public VideoListItemPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VideoListItemPageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public VideoListItemPageView(Context context, int everyPageSize, int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
	}

	@Override
	protected void initView() {
		initViewContainer(R.layout.video_list_item_layout);
		// 图片下载器
		imageLoader = ImageLoader.getInstance();
		// 设置图片下载器
		optionsPoster = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.epg_image_default)
				.showImageOnFail(R.drawable.epg_image_default).resetViewBeforeLoading(false).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
		// 包含海报图的父控件
		item1 = findViewById(R.id.video_item1);
		item2 = findViewById(R.id.video_item2);
		item3 = findViewById(R.id.video_item3);
		item4 = findViewById(R.id.video_item4);
		item5 = findViewById(R.id.video_item5);
		item6 = findViewById(R.id.video_item6);
		item7 = findViewById(R.id.video_item7);
		item8 = findViewById(R.id.video_item8);
		item9 = findViewById(R.id.video_item9);
		item10 = findViewById(R.id.video_item10);
		item11 = findViewById(R.id.video_item11);
		item12 = findViewById(R.id.video_item12);
		// 获取焦点的布局框
		item_poster1 = findItemView(R.id.item1_poster_layout);
		item_poster2 = findItemView(R.id.item2_poster_layout);
		item_poster3 = findItemView(R.id.item3_poster_layout);
		item_poster4 = findItemView(R.id.item4_poster_layout);
		item_poster5 = findItemView(R.id.item5_poster_layout);
		item_poster6 = findItemView(R.id.item6_poster_layout);
		item_poster7 = findItemView(R.id.item7_poster_layout);
		item_poster8 = findItemView(R.id.item8_poster_layout);
		item_poster9 = findItemView(R.id.item9_poster_layout);
		item_poster10 = findItemView(R.id.item10_poster_layout);
		item_poster11 = findItemView(R.id.item11_poster_layout);
		item_poster12 = findItemView(R.id.item12_poster_layout);
		// 海报图
		image1 = (ImageView) findViewById(R.id.item1_poster_iv);
		image2 = (ImageView) findViewById(R.id.item2_poster_iv);
		image3 = (ImageView) findViewById(R.id.item3_poster_iv);
		image4 = (ImageView) findViewById(R.id.item4_poster_iv);
		image5 = (ImageView) findViewById(R.id.item5_poster_iv);
		image6 = (ImageView) findViewById(R.id.item6_poster_iv);
		image7 = (ImageView) findViewById(R.id.item7_poster_iv);
		image8 = (ImageView) findViewById(R.id.item8_poster_iv);
		image9 = (ImageView) findViewById(R.id.item9_poster_iv);
		image10 = (ImageView) findViewById(R.id.item10_poster_iv);
		image11 = (ImageView) findViewById(R.id.item11_poster_iv);
		image12 = (ImageView) findViewById(R.id.item12_poster_iv);
		// 标题
		tv1 = (MarqueeText) findViewById(R.id.item1_title_tv);
		tv2 = (MarqueeText) findViewById(R.id.item2_title_tv);
		tv3 = (MarqueeText) findViewById(R.id.item3_title_tv);
		tv4 = (MarqueeText) findViewById(R.id.item4_title_tv);
		tv5 = (MarqueeText) findViewById(R.id.item5_title_tv);
		tv6 = (MarqueeText) findViewById(R.id.item6_title_tv);
		tv7 = (MarqueeText) findViewById(R.id.item7_title_tv);
		tv8 = (MarqueeText) findViewById(R.id.item8_title_tv);
		tv9 = (MarqueeText) findViewById(R.id.item9_title_tv);
		tv10 = (MarqueeText) findViewById(R.id.item10_title_tv);
		tv11 = (MarqueeText) findViewById(R.id.item11_title_tv);
		tv12 = (MarqueeText) findViewById(R.id.item12_title_tv);
		// 为焦点框添加焦点监听
		item_poster1.setOnFocusChangeListener(new MarqueeTextStart(tv1, 0));
		item_poster2.setOnFocusChangeListener(new MarqueeTextStart(tv2, 1));
		item_poster3.setOnFocusChangeListener(new MarqueeTextStart(tv3, 2));
		item_poster4.setOnFocusChangeListener(new MarqueeTextStart(tv4, 3));
		item_poster5.setOnFocusChangeListener(new MarqueeTextStart(tv5, 4));
		item_poster6.setOnFocusChangeListener(new MarqueeTextStart(tv6, 5));
		item_poster7.setOnFocusChangeListener(new MarqueeTextStart(tv7, 6));
		item_poster8.setOnFocusChangeListener(new MarqueeTextStart(tv8, 7));
		item_poster9.setOnFocusChangeListener(new MarqueeTextStart(tv9, 8));
		item_poster10.setOnFocusChangeListener(new MarqueeTextStart(tv10, 9));
		item_poster11.setOnFocusChangeListener(new MarqueeTextStart(tv11, 10));
		item_poster12.setOnFocusChangeListener(new MarqueeTextStart(tv12, 11));

	}

	/**
	 * 焦点监听
	 * 
	 * @ClassName: MarqueeTextStart
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月4日 下午4:01:29
	 * 
	 */
	class MarqueeTextStart implements OnFocusChangeListener {
		MarqueeText tv;
		int entityIndex;

		public MarqueeTextStart(MarqueeText tv, int entityIndex) {
			this.tv = tv;
			this.entityIndex = entityIndex;
		}

		/*
		 * 获取焦点的时候文字显示高亮 (non-Javadoc)
		 * 
		 * @see
		 * android.view.View.OnFocusChangeListener#onFocusChange(android.view
		 * .View, boolean)
		 */
		public void onFocusChange(View v, boolean hasFocus) {
			if (null != v.getTag()) {
				// start:修改focusEntity放在位置,author:huzuwei
				// focusEntity.getFocusEntity(mEntity,entityIndex);
				if (hasFocus) {// 获取焦点的出现高亮和走马灯
					mEntity = (FilmNewEntity) v.getTag();
					focusEntity.getFocusEntity(mEntity, entityIndex);
					// end
					tv.setStart(hasFocus);
					tv.setTextColor(0xffffffff);
					if (!StringUtils.isEmpty(mEntity.getName())) {
						tv.setText(mEntity.getName());
					}
				} else {// 设置恢复原来颜色
					FilmNewEntity mEntitys = (FilmNewEntity) v.getTag();
					tv.setStart(hasFocus);
					tv.setTextColor(0xff9a9a9a);
					if(!StringUtils.isEmpty(mEntitys.getName())){
						tv.setText(mEntitys.getName());
					}
				}
			}

		}
	}

	/*
	 * 按顺序显示文字信息 (non-Javadoc)
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
				setTextData(tv1, entity, item1);
			} else if (index == 1) {
				setItemViewTag(entity, 1, item_poster2);
				setTextData(tv2, entity, item2);
			} else if (index == 2) {
				setItemViewTag(entity, 2, item_poster3);
				setTextData(tv3, entity, item3);
			} else if (index == 3) {
				setItemViewTag(entity, 3, item_poster4);
				setTextData(tv4, entity, item4);
			} else if (index == 4) {
				setItemViewTag(entity, 4, item_poster5);
				setTextData(tv5, entity, item5);
			} else if (index == 5) {
				setItemViewTag(entity, 5, item_poster6);
				setTextData(tv6, entity, item6);
			} else if (index == 6) {
				setItemViewTag(entity, 6, item_poster7);
				setTextData(tv7, entity, item7);
			} else if (index == 7) {
				setItemViewTag(entity, 7, item_poster8);
				setTextData(tv8, entity, item8);
			} else if (index == 8) {
				setItemViewTag(entity, 8, item_poster9);
				setTextData(tv9, entity, item9);
			} else if (index == 9) {
				setItemViewTag(entity, 9, item_poster10);
				setTextData(tv10, entity, item10);
			} else if (index == 10) {
				setItemViewTag(entity, 10, item_poster11);
				setTextData(tv11, entity, item11);
			} else if (index == 11) {
				setItemViewTag(entity, 11, item_poster12);
				setTextData(tv12, entity, item12);
			}
		}
	}

	/**
	 * 设置文字信息
	 * 
	 * @Title: VideoListItemPageView
	 * @author:张鹏展
	 * @Description:
	 * @param tv
	 * @param entity
	 * @param ItemView
	 */
	private void setTextData(TextView tv, FilmNewEntity entity, View ItemView) {
		tv.setText(entity.getName());
		ItemView.setVisibility(View.VISIBLE);
	}

	/*
	 * 按顺序显示图片 (non-Javadoc)
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
			} else if (index == 6) {
				displayImage(image7, entity);
			} else if (index == 7) {
				displayImage(image8, entity);
			} else if (index == 8) {
				displayImage(image9, entity);
			} else if (index == 9) {
				displayImage(image10, entity);
			} else if (index == 10) {
				displayImage(image11, entity);
			} else if (index == 11) {
				displayImage(image12, entity);
			}
		}
	}

	/**
	 * 添加item的点击事件
	 * 
	 * @Title: VideoListItemPageView
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
		item_poster7.setOnClickListener(listener);
		item_poster8.setOnClickListener(listener);
		item_poster9.setOnClickListener(listener);
		item_poster10.setOnClickListener(listener);
		item_poster11.setOnClickListener(listener);
		item_poster12.setOnClickListener(listener);
	}

	/**
	 * 显示图片信息
	 * 
	 * @Title: VideoListItemPageView
	 * @author:张鹏展
	 * @Description:
	 * @param imageView
	 * @param entity
	 */
	private void displayImage(ImageView imageView, FilmNewEntity entity) {
		// imageView.setImageUrl(ToolsUtils.createImgUrl(entity.getVideoset_tv_img(),
		// true), new Handler());
		if (null == imageLoader) {
			imageLoader = ImageLoader.getInstance();
		}
		Log.v(TAG, entity.getId() + "");
		// 点播
		if (entity.getSource() == 0) {
			if (entity.getId() != 0) {
				// Log.v(TAG, "1" + entity.getVideoset_tv_img());//大图
				// imageLoader.displayImage(ToolsUtils.createImgUrl(entity.getVideoset_tv_img(),
				// true), imageView, optionsPoster);
				Log.v(TAG, "1" + entity.getPosterUrl());
				imageLoader.displayImage(ToolsUtils.createImgUrl(entity.getPosterUrl(), true), imageView, optionsPoster);
			} else {
				// Log.v(TAG, "2" + entity.getVideoset_img());//小图
				// imageLoader.displayImage(entity.getVideoset_img(), imageView,
				// optionsPoster);
				Log.v(TAG, "2" + entity.getPosterUrl());
				imageLoader.displayImage(entity.getPosterUrl(), imageView, optionsPoster);
			}
		}
		// 极清
		else if (entity.getSource() == 1) {
			imageLoader.displayImage(entity.getPosterUrl(), imageView, optionsPoster);
		}
		// 极清
		else if (entity.getSource() == 2) {
			imageLoader.displayImage(entity.getPosterUrl(), imageView, optionsPoster);
		}
	}

	/*
	 * 重置各个item (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.view.HiveBaseView#destoryFromViewPager()
	 */
	@Override
	public void destoryFromViewPager() {
		item1.setVisibility(View.INVISIBLE);
		item2.setVisibility(View.INVISIBLE);
		item3.setVisibility(View.INVISIBLE);
		item4.setVisibility(View.INVISIBLE);
		item5.setVisibility(View.INVISIBLE);
		item6.setVisibility(View.INVISIBLE);
		item7.setVisibility(View.INVISIBLE);
		item8.setVisibility(View.INVISIBLE);
		item9.setVisibility(View.INVISIBLE);
		item10.setVisibility(View.INVISIBLE);
		item11.setVisibility(View.INVISIBLE);
		item12.setVisibility(View.INVISIBLE);
		tv1.setText("");
		tv2.setText("");
		tv3.setText("");
		tv4.setText("");
		tv5.setText("");
		tv6.setText("");
		tv7.setText("");
		tv8.setText("");
		tv9.setText("");
		tv10.setText("");
		tv11.setText("");
		tv12.setText("");
		image1.setImageResource(R.drawable.epg_image_default);
		image2.setImageResource(R.drawable.epg_image_default);
		image3.setImageResource(R.drawable.epg_image_default);
		image4.setImageResource(R.drawable.epg_image_default);
		image5.setImageResource(R.drawable.epg_image_default);
		image6.setImageResource(R.drawable.epg_image_default);
		image7.setImageResource(R.drawable.epg_image_default);
		image8.setImageResource(R.drawable.epg_image_default);
		image9.setImageResource(R.drawable.epg_image_default);
		image10.setImageResource(R.drawable.epg_image_default);
		image11.setImageResource(R.drawable.epg_image_default);
		image12.setImageResource(R.drawable.epg_image_default);
	}

	/**
	 * 焦点回调方法
	 * 
	 * @ClassName: GetFocusEntity
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月4日 下午4:02:37
	 * 
	 */
	public interface GetFocusEntity {
		public void getFocusEntity(FilmNewEntity mEntity, int entityIndex);
	}

}
