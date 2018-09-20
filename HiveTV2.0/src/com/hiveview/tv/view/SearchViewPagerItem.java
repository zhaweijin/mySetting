package com.hiveview.tv.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.tv.R;
import com.hiveview.tv.common.HiveviewAppLib;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.content.ContentShowType;
import com.hiveview.tv.service.dao.ChannelDAO;
import com.hiveview.tv.service.entity.FirstClassListEntity;
import com.hiveview.tv.service.entity.SearchEntity;
import com.hiveview.tv.utils.StringUtils;
import com.hiveview.tv.utils.ToolsUtils;
import com.hiveview.tv.view.television.MarqueeText;

public class SearchViewPagerItem extends HiveBaseView {
	public SearchViewPagerItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SearchViewPagerItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SearchViewPagerItem(Context context, int everyPageSize, int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
		// TODO Auto-generated constructor stub
	}

	/*
	 * 1 ，电影 2 ，电视剧 4 ，动漫 6 ，综艺 这四个频道显示竖图 其它频道显示横图 *
	 */
	private int portrait_category_ids[] = { 1, 2, 4, 6 };

	/**
	 * 播放记录中的viewpager 的包含的view
	 */

	/**
	 * 包含一个view item 的 relativelayout
	 */
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

	/**
	 * 包含海报图片的linearlayout
	 */
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

	/**
	 * 海报图片的iamgeview
	 */
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

	/**
	 * 文字信息，依次是 电视剧名称 剩余时间 第几集 或者是第几期 一句话点看
	 */
	private MarqueeText tv1;
	private MarqueeText tv1_2;
	private MarqueeText tv1_3;
	private MarqueeText tv1_4;

	private MarqueeText tv2;
	private MarqueeText tv2_2;
	private MarqueeText tv2_3;
	private MarqueeText tv2_4;

	private MarqueeText tv3;
	private MarqueeText tv3_2;
	private MarqueeText tv3_3;
	private MarqueeText tv3_4;

	private MarqueeText tv4;
	private MarqueeText tv4_2;
	private MarqueeText tv4_3;
	private MarqueeText tv4_4;

	private MarqueeText tv5;
	private MarqueeText tv5_2;
	private MarqueeText tv5_3;
	private MarqueeText tv5_4;

	private MarqueeText tv6;
	private MarqueeText tv6_2;
	private MarqueeText tv6_3;
	private MarqueeText tv6_4;

	private MarqueeText tv7;
	private MarqueeText tv7_2;
	private MarqueeText tv7_3;
	private MarqueeText tv7_4;

	private MarqueeText tv8;
	private MarqueeText tv8_2;
	private MarqueeText tv8_3;
	private MarqueeText tv8_4;

	private MarqueeText tv9;
	private MarqueeText tv9_2;
	private MarqueeText tv9_3;
	private MarqueeText tv9_4;

	private MarqueeText tv10;
	private MarqueeText tv10_2;
	private MarqueeText tv10_3;
	private MarqueeText tv10_4;

	private MarqueeText tv11;
	private MarqueeText tv11_2;
	private MarqueeText tv11_3;
	private MarqueeText tv11_4;

	private MarqueeText tv12;
	private MarqueeText tv12_2;
	private MarqueeText tv12_3;
	private MarqueeText tv12_4;
	/**
	 * 数据库
	 */
	// start:实例化数据库，author:huzuwei
	private ChannelDAO channeldao;
	// end
	/**
	 * 图片下载器
	 */
	protected ImageLoader imageLoader = null;
	private DisplayImageOptions optionsPoster;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.view.HiveBaseView#initView() 初始化 view
	 */
	@Override
	protected void initView() {
		initViewContainer(R.layout.search_page_item_layout);
		imageLoader = ImageLoader.getInstance();
		channeldao = new ChannelDAO(getContext());

		item1 = findViewById(R.id.search_video_item1);
		item2 = findViewById(R.id.search_video_item2);
		item3 = findViewById(R.id.search_video_item3);
		item4 = findViewById(R.id.search_video_item4);
		item5 = findViewById(R.id.search_video_item5);
		item6 = findViewById(R.id.search_video_item6);
		item7 = findViewById(R.id.search_video_item7);
		item8 = findViewById(R.id.search_video_item8);
		item9 = findViewById(R.id.search_video_item9);
		item10 = findViewById(R.id.search_video_item10);
		item11 = findViewById(R.id.search_video_item11);
		item12 = findViewById(R.id.search_video_item12);

		item_poster1 = findItemView(R.id.search_item1_poster_layout);
		item_poster2 = findItemView(R.id.search_item2_poster_layout);
		item_poster3 = findItemView(R.id.search_item3_poster_layout);
		item_poster4 = findItemView(R.id.search_item4_poster_layout);
		item_poster5 = findItemView(R.id.search_item5_poster_layout);
		item_poster6 = findItemView(R.id.search_item6_poster_layout);
		item_poster7 = findItemView(R.id.search_item7_poster_layout);
		item_poster8 = findItemView(R.id.search_item8_poster_layout);
		item_poster9 = findItemView(R.id.search_item9_poster_layout);
		item_poster10 = findItemView(R.id.search_item10_poster_layout);
		item_poster11 = findItemView(R.id.search_item11_poster_layout);
		item_poster12 = findItemView(R.id.search_item12_poster_layout);

		// start:设置第二行的焦点 指向第一行的焦点 author:huzuwei
		item_poster7.setNextFocusUpId(item_poster1.getId());
		item_poster8.setNextFocusUpId(item_poster2.getId());
		item_poster9.setNextFocusUpId(item_poster3.getId());
		item_poster10.setNextFocusUpId(item_poster4.getId());
		item_poster11.setNextFocusUpId(item_poster5.getId());
		item_poster12.setNextFocusUpId(item_poster6.getId());
		// end

		image1 = (ImageView) findViewById(R.id.search_item1_poster_iv);
		image2 = (ImageView) findViewById(R.id.search_item2_poster_iv);
		image3 = (ImageView) findViewById(R.id.search_item3_poster_iv);
		image4 = (ImageView) findViewById(R.id.search_item4_poster_iv);
		image5 = (ImageView) findViewById(R.id.search_item5_poster_iv);
		image6 = (ImageView) findViewById(R.id.search_item6_poster_iv);
		image7 = (ImageView) findViewById(R.id.search_item7_poster_iv);
		image8 = (ImageView) findViewById(R.id.search_item8_poster_iv);
		image9 = (ImageView) findViewById(R.id.search_item9_poster_iv);
		image10 = (ImageView) findViewById(R.id.search_item10_poster_iv);
		image11 = (ImageView) findViewById(R.id.search_item11_poster_iv);
		image12 = (ImageView) findViewById(R.id.search_item12_poster_iv);

		tv1 = (MarqueeText) findViewById(R.id.search_item1_title_tv);
		tv1_2 = (MarqueeText) findViewById(R.id.search_item1_title_tv2);
		tv1_3 = (MarqueeText) findViewById(R.id.search_item1_title_tv3);
		tv1_4 = (MarqueeText) findViewById(R.id.search_item1_title_tv4);
		tv2 = (MarqueeText) findViewById(R.id.search_item2_title_tv);
		tv2_2 = (MarqueeText) findViewById(R.id.search_item2_title_tv2);
		tv2_3 = (MarqueeText) findViewById(R.id.search_item2_title_tv3);
		tv2_4 = (MarqueeText) findViewById(R.id.search_item2_title_tv4);
		tv3 = (MarqueeText) findViewById(R.id.search_item3_title_tv);
		tv3_2 = (MarqueeText) findViewById(R.id.search_item3_title_tv2);
		tv3_3 = (MarqueeText) findViewById(R.id.search_item3_title_tv3);
		tv3_4 = (MarqueeText) findViewById(R.id.search_item3_title_tv4);
		tv4 = (MarqueeText) findViewById(R.id.search_item4_title_tv);
		tv4_2 = (MarqueeText) findViewById(R.id.search_item4_title_tv2);
		tv4_3 = (MarqueeText) findViewById(R.id.search_item4_title_tv3);
		tv4_4 = (MarqueeText) findViewById(R.id.search_item4_title_tv4);
		tv5 = (MarqueeText) findViewById(R.id.search_item5_title_tv);
		tv5_2 = (MarqueeText) findViewById(R.id.search_item5_title_tv2);
		tv5_3 = (MarqueeText) findViewById(R.id.search_item5_title_tv3);
		tv5_4 = (MarqueeText) findViewById(R.id.search_item5_title_tv4);
		tv6 = (MarqueeText) findViewById(R.id.search_item6_title_tv);
		tv6_2 = (MarqueeText) findViewById(R.id.search_item6_title_tv2);
		tv6_3 = (MarqueeText) findViewById(R.id.search_item6_title_tv3);
		tv6_4 = (MarqueeText) findViewById(R.id.search_item6_title_tv4);
		tv7 = (MarqueeText) findViewById(R.id.search_item7_title_tv);
		tv7_2 = (MarqueeText) findViewById(R.id.search_item7_title_tv2);
		tv7_3 = (MarqueeText) findViewById(R.id.search_item7_title_tv3);
		tv7_4 = (MarqueeText) findViewById(R.id.search_item7_title_tv4);
		tv8 = (MarqueeText) findViewById(R.id.search_item8_title_tv);
		tv8_2 = (MarqueeText) findViewById(R.id.search_item8_title_tv2);
		tv8_3 = (MarqueeText) findViewById(R.id.search_item8_title_tv3);
		tv8_4 = (MarqueeText) findViewById(R.id.search_item8_title_tv4);
		tv9 = (MarqueeText) findViewById(R.id.search_item9_title_tv);
		tv9_2 = (MarqueeText) findViewById(R.id.search_item9_title_tv2);
		tv9_3 = (MarqueeText) findViewById(R.id.search_item9_title_tv3);
		tv9_4 = (MarqueeText) findViewById(R.id.search_item9_title_tv4);
		tv10 = (MarqueeText) findViewById(R.id.search_item10_title_tv);
		tv10_2 = (MarqueeText) findViewById(R.id.search_item10_title_tv2);
		tv10_3 = (MarqueeText) findViewById(R.id.search_item10_title_tv3);
		tv10_4 = (MarqueeText) findViewById(R.id.search_item10_title_tv4);
		tv11 = (MarqueeText) findViewById(R.id.search_item11_title_tv);
		tv11_2 = (MarqueeText) findViewById(R.id.search_item11_title_tv2);
		tv11_3 = (MarqueeText) findViewById(R.id.search_item11_title_tv3);
		tv11_4 = (MarqueeText) findViewById(R.id.search_item11_title_tv4);
		tv12 = (MarqueeText) findViewById(R.id.search_item12_title_tv);
		tv12_2 = (MarqueeText) findViewById(R.id.search_item12_title_tv2);
		tv12_3 = (MarqueeText) findViewById(R.id.search_item12_title_tv3);
		tv12_4 = (MarqueeText) findViewById(R.id.search_item12_title_tv4);

		item_poster1.setOnFocusChangeListener(new MarqueeTextStart(tv1, tv1_2, tv1_3, tv1_4));
		item_poster2.setOnFocusChangeListener(new MarqueeTextStart(tv2, tv2_2, tv2_3, tv2_4));
		item_poster3.setOnFocusChangeListener(new MarqueeTextStart(tv3, tv3_2, tv3_3, tv3_4));
		item_poster4.setOnFocusChangeListener(new MarqueeTextStart(tv4, tv4_2, tv4_3, tv4_4));
		item_poster5.setOnFocusChangeListener(new MarqueeTextStart(tv5, tv5_2, tv5_3, tv5_4));
		item_poster6.setOnFocusChangeListener(new MarqueeTextStart(tv6, tv6_2, tv6_3, tv6_4));
		item_poster7.setOnFocusChangeListener(new MarqueeTextStart(tv7, tv7_2, tv7_3, tv7_4));
		item_poster8.setOnFocusChangeListener(new MarqueeTextStart(tv8, tv8_2, tv8_3, tv8_4));
		item_poster9.setOnFocusChangeListener(new MarqueeTextStart(tv9, tv9_2, tv9_3, tv9_4));
		item_poster10.setOnFocusChangeListener(new MarqueeTextStart(tv10, tv10_2, tv10_3, tv10_4));
		item_poster11.setOnFocusChangeListener(new MarqueeTextStart(tv11, tv11_2, tv11_3, tv11_4));
		item_poster12.setOnFocusChangeListener(new MarqueeTextStart(tv12, tv12_2, tv12_3, tv12_4));

	}

	/**
	 * 焦点监听方法，在里边设置获取焦点的时候文字出现走马灯运动
	 * 
	 * @ClassName: MarqueeTextStart
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月4日 下午3:36:29
	 * 
	 */
	class MarqueeTextStart implements OnFocusChangeListener {
		MarqueeText tv;
		MarqueeText tv2;
		MarqueeText tv3;
		MarqueeText tv4;

		public MarqueeTextStart(MarqueeText tv, MarqueeText tv2, MarqueeText tv3, MarqueeText tv4) {
			this.tv = tv;
			this.tv2 = tv2;
			this.tv3 = tv3;
			this.tv4 = tv4;
		}

		SearchEntity SearchEntity;

		public void onFocusChange(View v, boolean hasFocus) {
			if (null != v) {
				SearchEntity = (SearchEntity) v.getTag();
				if (null != SearchEntity) {// 横图
					if (hasFocus) {

						if (!isPortraitMode(SearchEntity.getRecord_type())) {// 横图
							// 设置文字高亮
							tv.setStart(hasFocus);
							tv.setText(SearchEntity.getRecord_name());
							tv.setTextColor(0xffffffff);
							tv2.setTextColor(0xffffffff);
							tv3.setTextColor(0xffffffff);
						} else {
							tv4.setStart(hasFocus);
							tv4.setText(SearchEntity.getRecord_name());
							tv4.setTextColor(0xffffffff);
						}

					} else {// 设置文字原来的颜色

						if (!isPortraitMode(SearchEntity.getRecord_type())) {// 横图

							tv.setStart(hasFocus);
							tv.setText(SearchEntity.getRecord_name());
							tv.setTextColor(0xff9a9a9a);
							tv2.setTextColor(0xff9a9a9a);
							tv3.setTextColor(0xff9a9a9a);
						} else {
							tv4.setStart(hasFocus);
							tv4.setText(SearchEntity.getRecord_name());
							tv4.setTextColor(0xff9a9a9a);
						}
					}
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hiveview.box.framework.view.HiveBaseView#displayTextDataToItemView
	 * (com.hiveview.box.framework.entity.HiveBaseEntity, int) 填充实体类 和相关信息
	 */
	public void displayTextDataToItemView(HiveBaseEntity baseEntity, int index) {
		SearchEntity entity = (SearchEntity) baseEntity;
		if (null != entity) {
			if (index == 0) {
				item1.setVisibility(View.VISIBLE);
				setItemViewTag(entity, 0, item_poster1);
				setTextData(tv1, tv1_2, tv1_3, tv1_4, entity, item1);
				setImageViewFrame(image1, entity);
			} else if (index == 1) {
				item2.setVisibility(View.VISIBLE);
				setItemViewTag(entity, 1, item_poster2);
				setTextData(tv2, tv2_2, tv2_3, tv2_4, entity, item2);
				setImageViewFrame(image2, entity);
			} else if (index == 2) {
				item3.setVisibility(View.VISIBLE);
				setItemViewTag(entity, 2, item_poster3);
				setTextData(tv3, tv3_2, tv3_3, tv3_4, entity, item3);
				setImageViewFrame(image3, entity);
			} else if (index == 3) {
				item4.setVisibility(View.VISIBLE);
				setItemViewTag(entity, 3, item_poster4);
				setTextData(tv4, tv4_2, tv4_3, tv4_4, entity, item4);
				setImageViewFrame(image4, entity);
			} else if (index == 4) {
				item5.setVisibility(View.VISIBLE);
				setItemViewTag(entity, 4, item_poster5);
				setTextData(tv5, tv5_2, tv5_3, tv5_4, entity, item5);
				setImageViewFrame(image5, entity);
			} else if (index == 5) {
				item6.setVisibility(View.VISIBLE);
				setItemViewTag(entity, 5, item_poster6);
				setTextData(tv6, tv6_2, tv6_3, tv6_4, entity, item6);
				setImageViewFrame(image6, entity);
			} else if (index == 6) {
				item7.setVisibility(View.VISIBLE);
				setItemViewTag(entity, 6, item_poster7);
				setTextData(tv7, tv7_2, tv7_3, tv7_4, entity, item7);
				setImageViewFrame(image7, entity);
			} else if (index == 7) {
				item8.setVisibility(View.VISIBLE);
				setItemViewTag(entity, 7, item_poster8);
				setTextData(tv8, tv8_2, tv8_3, tv8_4, entity, item8);
				setImageViewFrame(image8, entity);
			} else if (index == 8) {
				item9.setVisibility(View.VISIBLE);
				setItemViewTag(entity, 8, item_poster9);
				setTextData(tv9, tv9_2, tv9_3, tv9_4, entity, item9);
				setImageViewFrame(image9, entity);
			} else if (index == 9) {
				item10.setVisibility(View.VISIBLE);
				setItemViewTag(entity, 9, item_poster10);
				setTextData(tv10, tv10_2, tv10_3, tv10_4, entity, item10);
				setImageViewFrame(image10, entity);
			} else if (index == 10) {
				item11.setVisibility(View.VISIBLE);
				setItemViewTag(entity, 10, item_poster11);
				setTextData(tv11, tv11_2, tv11_3, tv11_4, entity, item11);
				setImageViewFrame(image11, entity);
			} else if (index == 11) {
				item12.setVisibility(View.VISIBLE);
				setImageViewFrame(image12, entity);
				setItemViewTag(entity, 11, item_poster12);
				setTextData(tv12, tv12_2, tv12_3, tv12_4, entity, item12);
			}
		}
	}

	// tv 电视剧名字； tv2 剩余时间 ；tv3 集数或者期号；tv4 一句话看点
	private void setTextData(TextView tv, TextView tv2, TextView tv3, TextView tv4, SearchEntity entity, View ItemView) {
		int up_date = entity.getRecord_update();
		int all = entity.getRecord_total();
		if (!isPortraitMode(entity.getRecord_type())) {// 横图
			tv.setSingleLine(false);
			// 设置文字左对齐
			tv.setGravity(Gravity.LEFT);
			tv.setMaxLines(4);
			tv.setText(entity.getRecord_name());
			tv2.setVisibility(View.VISIBLE);
			tv3.setVisibility(up_date <= 1 ? View.GONE : View.VISIBLE);
			tv3.setText(up_date >= all ? String.valueOf(entity.getRecord_update()) + "集" : "更新至" + String.valueOf(entity.getRecord_update()) + "集");
			tv2.setText( !tv3.isShown() ? StringUtils.intTimeString(entity.getRecord_time_length()) : "");// time
			if (null != channeldao.queryMap().get(entity.getRecord_type())) {
				tv4.setVisibility(View.VISIBLE);
				// start author:zhangpengzhan 不显示频道名
				tv4.setText("");
				// end
			} else {
				tv4.setVisibility(View.VISIBLE);
				tv4.setText("");
			}
			// end

		} else {
			tv2.setVisibility(View.GONE);
			tv4.setVisibility(View.VISIBLE);
			tv.setVisibility(View.GONE);
			tv3.setVisibility(View.GONE);
			tv.setSingleLine(true);
			tv4.setText(entity.getRecord_name());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hiveview.box.framework.view.HiveBaseView#displayImageToItemView(com
	 * .hiveview.box.framework.entity.HiveBaseEntity, int) 图片信息 列表的映射关系
	 */
	public void displayImageToItemView(HiveBaseEntity baseEntity, int index) {
		SearchEntity entity = (SearchEntity) baseEntity;
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
	 * @param listener
	 *            添加view 的按键监听
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
	 * @Title: SearchViewPagerItem
	 * @author:张鹏展
	 * @Description:
	 * @param imageView
	 * @param entity
	 */
	private void displayImage(ImageView imageView, SearchEntity entity) {
		// 判断是否是横竖图片
		/*
		 */
		String imageUrl;
		if (null == imageLoader) {
			imageLoader = ImageLoader.getInstance();
		}
		if (!isPortraitMode(entity.getRecord_type())) {
			optionsPoster = new DisplayImageOptions.Builder().resetViewBeforeLoading(false).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
			imageUrl = ToolsUtils.createImgUrl(entity.getRecord_img(), false);
		} else {
			optionsPoster = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.epg_image_default)
					.showImageOnFail(R.drawable.epg_image_default).resetViewBeforeLoading(false).cacheOnDisc(true)
					.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
			imageUrl = ToolsUtils.createImgUrl(entity.getRecord_tv_img(), true);
		}
		imageView.setVisibility(View.VISIBLE);

		imageLoader.displayImage(imageUrl, imageView, optionsPoster);

	}

	/**
	 * 设置图片的横竖状态
	 * 
	 * @Title: SearchViewPagerItem
	 * @author:张鹏展
	 * @Description:
	 * @param imageView
	 * @param entity
	 */
	public void setImageViewFrame(ImageView imageView, SearchEntity entity) {
		imageView.setVisibility(View.VISIBLE);
		if (!isPortraitMode(entity.getRecord_type())) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(172, 96);
			params.leftMargin = 1;
			imageView.setLayoutParams(params);
			imageView.setBackgroundResource(R.drawable.variety_default_img);
		}
	}

	/*
	 * item name="android:layout_width">171.6dip</item> <item
	 * name="android:layout_height">230.34dip</item> 回调销毁的方法 (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.view.HiveBaseView#destoryFromViewPager()
	 */
	@Override
	public void destoryFromViewPager() {
		// 初始化 item 属性
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
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(172, 230);
		params.leftMargin = 0;
		image1.setBackgroundResource(R.drawable.epg_image_default);
		image2.setBackgroundResource(R.drawable.epg_image_default);
		image3.setBackgroundResource(R.drawable.epg_image_default);
		image4.setBackgroundResource(R.drawable.epg_image_default);
		image5.setBackgroundResource(R.drawable.epg_image_default);
		image6.setBackgroundResource(R.drawable.epg_image_default);
		image7.setBackgroundResource(R.drawable.epg_image_default);
		image8.setBackgroundResource(R.drawable.epg_image_default);
		image9.setBackgroundResource(R.drawable.epg_image_default);
		image10.setBackgroundResource(R.drawable.epg_image_default);
		image11.setBackgroundResource(R.drawable.epg_image_default);
		image12.setBackgroundResource(R.drawable.epg_image_default);
		image1.setImageResource(-1);
		image2.setImageResource(-1);
		image3.setImageResource(-1);
		image4.setImageResource(-1);
		image5.setImageResource(-1);
		image6.setImageResource(-1);
		image7.setImageResource(-1);
		image8.setImageResource(-1);
		image9.setImageResource(-1);
		image10.setImageResource(-1);
		image11.setImageResource(-1);
		image12.setImageResource(-1);
		image1.setLayoutParams(params);
		image2.setLayoutParams(params);
		image3.setLayoutParams(params);
		image4.setLayoutParams(params);
		image5.setLayoutParams(params);
		image6.setLayoutParams(params);
		image7.setLayoutParams(params);
		image8.setLayoutParams(params);
		image9.setLayoutParams(params);
		image10.setLayoutParams(params);
		image11.setLayoutParams(params);
		image12.setLayoutParams(params);

		tv1.setText("");
		tv1.setGravity(Gravity.CENTER_HORIZONTAL);
		tv1_2.setText("");
		tv1_3.setText("");
		tv1_4.setText("");
		tv2.setText("");
		tv2.setGravity(Gravity.CENTER_HORIZONTAL);
		tv2_2.setText("");
		tv2_3.setText("");
		tv2_4.setText("");
		tv3.setText("");
		tv3.setGravity(Gravity.CENTER_HORIZONTAL);
		tv3_2.setText("");
		tv3_3.setText("");
		tv3_4.setText("");
		tv4.setText("");
		tv4.setGravity(Gravity.CENTER_HORIZONTAL);
		tv4_2.setText("");
		tv4_3.setText("");
		tv4_4.setText("");
		tv5.setText("");
		tv5.setGravity(Gravity.CENTER_HORIZONTAL);
		tv5_2.setText("");
		tv5_3.setText("");
		tv5_4.setText("");
		tv6.setText("");
		tv6.setGravity(Gravity.CENTER_HORIZONTAL);
		tv6_2.setText("");
		tv6_3.setText("");
		tv6_4.setText("");
		tv7.setText("");
		tv7.setGravity(Gravity.CENTER_HORIZONTAL);
		tv7_2.setText("");
		tv7_3.setText("");
		tv7_4.setText("");
		tv8.setText("");
		tv8.setGravity(Gravity.CENTER_HORIZONTAL);
		tv8_2.setText("");
		tv8_3.setText("");
		tv8_4.setText("");
		tv9.setText("");
		tv9.setGravity(Gravity.CENTER_HORIZONTAL);
		tv9_2.setText("");
		tv9_3.setText("");
		tv9_4.setText("");
		tv10.setText("");
		tv10.setGravity(Gravity.CENTER_HORIZONTAL);
		tv10_2.setText("");
		tv10_3.setText("");
		tv10_4.setText("");
		tv11.setText("");
		tv11.setGravity(Gravity.CENTER_HORIZONTAL);
		tv11_2.setText("");
		tv11_3.setText("");
		tv11_4.setText("");
		tv12.setText("");
		tv12.setGravity(Gravity.CENTER_HORIZONTAL);
		tv12_2.setText("");
		tv12_3.setText("");
		tv12_4.setText("");
	}

	/**
	 * 判断需要显示横图还是显示竖图 true 为 竖图
	 * 
	 * @param category_id
	 * @return
	 */
	/*
	 * 1 ，电影 2 ，电视剧 4 ，动漫 6 ，综艺 这四个频道显示竖图 其它频道显示横图 * show_type 为 0 的 是 单片无详情的 1
	 * 2 3 是多剧集有详情页的需要竖图
	 */
	private boolean isPortraitMode(int category_id) {
		ArrayList<FirstClassListEntity> array = channeldao.query(new String[] { "show_type" }, "firstclass_id=?",
				new String[] { String.valueOf(category_id) }, null);
		if (null != array && array.size() == 0) {
			Log.d(VIEW_LOG_TAG, ":::null"+"||category_id::"+category_id);
			return true;
		}
		FirstClassListEntity firstClassListEntity = array.get(0);
		int show_type = firstClassListEntity.getShow_type();
		if (show_type != ContentShowType.TYPE_SINGLE_VIDEO_NO_DETAIL) {
			Log.d(VIEW_LOG_TAG, ":::show_type"+show_type+"||category_id::"+category_id);
			return true;
		}
		Log.d(VIEW_LOG_TAG, ":::show_type"+show_type+"||category_id::"+category_id);
		return false;
	}

}
