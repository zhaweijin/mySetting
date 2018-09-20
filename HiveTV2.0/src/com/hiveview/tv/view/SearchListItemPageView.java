package com.hiveview.tv.view;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.hiveview.tv.utils.ToolsUtils;
import com.hiveview.tv.view.television.MarqueeText;

/**
 * 类名 MovieListItemPageView
 * 
 * @author gusongsheng 专辑列表 2014-4-17
 * 
 */
public class SearchListItemPageView extends HiveBaseView {

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

	private MarqueeText tvUpdate1;
	private MarqueeText tvUpdate2;
	private MarqueeText tvUpdate3;
	private MarqueeText tvUpdate4;
	private MarqueeText tvUpdate5;
	private MarqueeText tvUpdate6;
	private MarqueeText tvUpdate7;
	private MarqueeText tvUpdate8;
	private MarqueeText tvUpdate9;
	private MarqueeText tvUpdate10;
	private MarqueeText tvUpdate11;
	private MarqueeText tvUpdate12;

	private DisplayImageOptions optionsPoster;
	/**
	 * 1表示多集 0 只有一集
	 */
	private final int isSeries = 1;
	/**
	 * 综艺分类id
	 */
	private final int isZongYi = 6;

	/**
	 * 图片下载器
	 */
	protected ImageLoader imageLoader = null;
	/**
	 * @Fields itemViewFocusChange:item 焦点监听
	 */
	private ItemViewFocusChange itemViewFocusChange;

	public SearchListItemPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SearchListItemPageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SearchListItemPageView(Context context, int everyPageSize, int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
	}

	/**
	 * 初始化数据
	 */
	@Override
	protected void initView() {
		initViewContainer(R.layout.movie_demand_list_item_layout);

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

		tvUpdate1 = (MarqueeText) findItemView(R.id.item1_update_tv);
		tvUpdate2 = (MarqueeText) findItemView(R.id.item2_update_tv);
		tvUpdate3 = (MarqueeText) findItemView(R.id.item3_update_tv);
		tvUpdate4 = (MarqueeText) findItemView(R.id.item4_update_tv);
		tvUpdate5 = (MarqueeText) findItemView(R.id.item5_update_tv);
		tvUpdate6 = (MarqueeText) findItemView(R.id.item6_update_tv);
		tvUpdate7 = (MarqueeText) findItemView(R.id.item7_update_tv);
		tvUpdate8 = (MarqueeText) findItemView(R.id.item8_update_tv);
		tvUpdate9 = (MarqueeText) findItemView(R.id.item9_update_tv);
		tvUpdate10 = (MarqueeText) findItemView(R.id.item10_update_tv);
		tvUpdate11 = (MarqueeText) findItemView(R.id.item11_update_tv);
		tvUpdate12 = (MarqueeText) findItemView(R.id.item12_update_tv);

		item_poster1.setOnFocusChangeListener(new ItemFocusChangeListener(tv1,item1));
		item_poster2.setOnFocusChangeListener(new ItemFocusChangeListener(tv2,item2));
		item_poster3.setOnFocusChangeListener(new ItemFocusChangeListener(tv3,item3));
		item_poster4.setOnFocusChangeListener(new ItemFocusChangeListener(tv4,item4));
		item_poster5.setOnFocusChangeListener(new ItemFocusChangeListener(tv5,item5));
		item_poster6.setOnFocusChangeListener(new ItemFocusChangeListener(tv6,item6));
		item_poster7.setOnFocusChangeListener(new ItemFocusChangeListener(tv7,item7));
		item_poster8.setOnFocusChangeListener(new ItemFocusChangeListener(tv8,item8));
		item_poster9.setOnFocusChangeListener(new ItemFocusChangeListener(tv9,item9));
		item_poster10.setOnFocusChangeListener(new ItemFocusChangeListener(tv10,item10));
		item_poster11.setOnFocusChangeListener(new ItemFocusChangeListener(tv11,item11));
		item_poster12.setOnFocusChangeListener(new ItemFocusChangeListener(tv12,item12));

	}

	class ItemFocusChangeListener implements OnFocusChangeListener {

		public MarqueeText tv;
		private View v;

		public ItemFocusChangeListener(MarqueeText tv,View v) {
			this.tv = tv;
			this.v=v;
		}

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			if (null != itemViewFocusChange) {
				itemViewFocusChange.itemFocusChange(v, arg1);
			}
			FilmEntity entity = (FilmEntity) arg0.getTag();
			if (null != entity) {
				tv.setStart(arg1);
				tv.setText(entity.getVideoset_name());
				tv.setTextColor(arg1 ? 0xffffffff : 0xff9a9a9a);
			}
		}

	}

	public void setItemViewFocusChange(ItemViewFocusChange itemViewFocusChange) {
		this.itemViewFocusChange = itemViewFocusChange;
	}

	public interface ItemViewFocusChange {
		public void itemFocusChange(View view, boolean arg1);
	}

	/**
	 * 为数据赋值
	 * 
	 * @param baseEntity
	 *            数据对象
	 * @param index
	 *            位置
	 */
	@Override
	public void displayTextDataToItemView(HiveBaseEntity baseEntity, int index) {
		FilmEntity entity = (FilmEntity) baseEntity;
		if (null != entity) {
			if (index == 0) {
				setItemViewTag(entity, 0, item_poster1);
				setTextData(tvUpdate1, tv1, entity, item1);
			} else if (index == 1) {
				setItemViewTag(entity, 1, item_poster2);
				setTextData(tvUpdate2, tv2, entity, item2);
			} else if (index == 2) {
				setItemViewTag(entity, 2, item_poster3);
				setTextData(tvUpdate3, tv3, entity, item3);
			} else if (index == 3) {
				setItemViewTag(entity, 3, item_poster4);
				setTextData(tvUpdate4, tv4, entity, item4);
			} else if (index == 4) {
				setItemViewTag(entity, 4, item_poster5);
				setTextData(tvUpdate5, tv5, entity, item5);
			} else if (index == 5) {
				setItemViewTag(entity, 5, item_poster6);
				setTextData(tvUpdate6, tv6, entity, item6);
			} else if (index == 6) {
				setItemViewTag(entity, 6, item_poster7);
				setTextData(tvUpdate7, tv7, entity, item7);
			} else if (index == 7) {
				setItemViewTag(entity, 7, item_poster8);
				setTextData(tvUpdate8, tv8, entity, item8);
			} else if (index == 8) {
				setItemViewTag(entity, 8, item_poster9);
				setTextData(tvUpdate9, tv9, entity, item9);
			} else if (index == 9) {
				setItemViewTag(entity, 9, item_poster10);
				setTextData(tvUpdate10, tv10, entity, item10);
			} else if (index == 10) {
				setItemViewTag(entity, 10, item_poster11);
				setTextData(tvUpdate11, tv11, entity, item11);
			} else if (index == 11) {
				setItemViewTag(entity, 11, item_poster12);
				setTextData(tvUpdate12, tv12, entity, item12);
			}
		}
	}

	/**
	 * 为TextView赋值
	 * 
	 * @param tv
	 *            当前的TextView
	 * @param entity
	 *            FilmEntity数据对象
	 * @param ItemView
	 */
	private void setTextData(TextView tvUpdate, TextView tv, FilmEntity entity, View ItemView) {
		tv.setText(entity.getVideoset_name());
		int allSize = entity.getVideoset_total();
		int updateSize = entity.getVideoset_update();
		if (isSeries == entity.getIsSeries() && isZongYi != entity.getVideoset_type()) {
			if (allSize == updateSize) {
				tvUpdate.setText(allSize + "集全");
			} else {
				tvUpdate.setText("已更新至" + entity.getVideoset_update() + "集");
			}
		} else {
			tvUpdate.setVisibility(View.INVISIBLE);
		}
		ItemView.setVisibility(View.VISIBLE);
	}

	/**
	 * 为ImageView赋值
	 * 
	 * @param baseEntity
	 * @param index
	 *            ImageView的位置
	 */
	@Override
	public void displayImageToItemView(HiveBaseEntity baseEntity, int index) {
		FilmEntity entity = (FilmEntity) baseEntity;
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
	 * 加载图片
	 * 
	 * @param imageView
	 *            ImagerView
	 * @param entity
	 */
	private void displayImage(ImageView imageView, FilmEntity entity) {
		imageLoader = ImageLoader.getInstance();
		optionsPoster = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.epg_image_default)
				.showImageOnFail(R.drawable.epg_image_default).resetViewBeforeLoading(false).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();

		if (null != entity && null != entity.getVideoset_img() && !entity.getVideoset_img().equals("") && !entity.getVideoset_img().equals("null")) {
			imageLoader.displayImage(entity.getVideoset_img(), imageView, optionsPoster);
		}
	}

	@Override
	public void destoryFromViewPager() {
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
		item1.setVisibility(View.INVISIBLE);
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
	}

}
