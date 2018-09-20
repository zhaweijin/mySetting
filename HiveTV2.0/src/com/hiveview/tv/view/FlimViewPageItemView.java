package com.hiveview.tv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.tv.R;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.view.television.MarqueeText;

/**
 * @ClassName: FlimViewPageItemView
 * @Description:
 * @author: zhangpengzhan
 * @date 2014年6月6日 下午1:56:20
 * 
 */
public class FlimViewPageItemView extends HiveBaseView {

	/**
	 * 6个每页中需要现实的item
	 * 
	 * @Fields itemView1
	 */
	private View itemView1;
	private View itemView2;
	private View itemView3;
	private View itemView4;
	private View itemView5;
	private View itemView6;
	
	/**
	 * 包含在海报图外边的父控件
	 * @Fields l1
	 */
	private LinearLayout l1;
	private LinearLayout l2;
	private LinearLayout l3;
	private LinearLayout l4;
	private LinearLayout l5;
	private LinearLayout l6;

	/**
	 * 每页的item的海报图
	 * 
	 * @Fields imageView1
	 */
	private ImageView imageView1;
	private ImageView imageView2;
	private ImageView imageView3;
	private ImageView imageView4;
	private ImageView imageView5;
	private ImageView imageView6;
	/**
	 * 每页的item的标题信息
	 * 
	 * @Fields marqueeText1
	 */
	private MarqueeText marqueeText1;
	private MarqueeText marqueeText2;
	private MarqueeText marqueeText3;
	private MarqueeText marqueeText4;
	private MarqueeText marqueeText5;
	private MarqueeText marqueeText6;

	/**
	 * 图片下载器
	 */
	protected ImageLoader imageLoader = null;
	/**
	 * 图片下载器的配置器
	 * 
	 * @Fields optionsPoster
	 */
	private DisplayImageOptions optionsPoster;

	public FlimViewPageItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FlimViewPageItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FlimViewPageItemView(Context context, int everyPageSize, int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
	}

	/*
	 * 初始化相关控件 (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.view.HiveBaseView#initView()
	 */
	@Override
	protected void initView() {
		// 添加布局的文件
		initViewContainer(R.layout.film_viewpage_item_layout);
		// 初始化图片下载器
		imageLoader = ImageLoader.getInstance();
		// 初始化图片下载器的配置相关
		optionsPoster = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.epg_image_default)
				.showImageOnFail(R.drawable.epg_image_default).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
		// 初始化控件
		itemView1 = findViewById(R.id.film_item_rl_1);
		itemView2 = findViewById(R.id.film_item_rl_2);
		itemView3 = findViewById(R.id.film_item_rl_3);
		itemView4 = findViewById(R.id.film_item_rl_4);
		itemView5 = findViewById(R.id.film_item_rl_5);
		itemView6 = findViewById(R.id.film_item_rl_6);
		//海报图的父控件
		l1 = (LinearLayout) findItemView(R.id.film_l_1);
		l2 = (LinearLayout) findItemView(R.id.film_l_2);
		l3 = (LinearLayout) findItemView(R.id.film_l_3);
		l4 = (LinearLayout) findItemView(R.id.film_l_4);
		l5 = (LinearLayout) findItemView(R.id.film_l_5);
		l6 = (LinearLayout) findItemView(R.id.film_l_6);
		
		// 海报图
		imageView1 = (ImageView) findViewById(R.id.iv_poster1);
		imageView2 = (ImageView) findViewById(R.id.iv_poster2);
		imageView3 = (ImageView) findViewById(R.id.iv_poster3);
		imageView4 = (ImageView) findViewById(R.id.iv_poster4);
		imageView5 = (ImageView) findViewById(R.id.iv_poster5);
		imageView6 = (ImageView) findViewById(R.id.iv_poster6);
		

		// 标题信息
		marqueeText1 = (MarqueeText) findViewById(R.id.tv_name1);
		marqueeText2 = (MarqueeText) findViewById(R.id.tv_name2);
		marqueeText3 = (MarqueeText) findViewById(R.id.tv_name3);
		marqueeText4 = (MarqueeText) findViewById(R.id.tv_name4);
		marqueeText5 = (MarqueeText) findViewById(R.id.tv_name5);
		marqueeText6 = (MarqueeText) findViewById(R.id.tv_name6);
		
		l1.setOnFocusChangeListener(new MarqueeTextStart(marqueeText1));
		l2.setOnFocusChangeListener(new MarqueeTextStart(marqueeText2));
		l3.setOnFocusChangeListener(new MarqueeTextStart(marqueeText3));
		l4.setOnFocusChangeListener(new MarqueeTextStart(marqueeText4));
		l5.setOnFocusChangeListener(new MarqueeTextStart(marqueeText5));
		l6.setOnFocusChangeListener(new MarqueeTextStart(marqueeText6));

	}

	/**
	 * 焦点框获取焦点的时候文字显示高亮状态 该滚动的就开始滚动
	 * 
	 * @ClassName: MarqueeTextStart
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月6日 上午11:31:32
	 * 
	 */
	class MarqueeTextStart implements OnFocusChangeListener {

		/**
		 * 要显示变化的文字信息
		 * 
		 * @Fields mt
		 */
		private MarqueeText mt;

		public MarqueeTextStart(MarqueeText mt) {
			this.mt = mt;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (null != v.getTag()) {
				// 获取焦点view的 tag
				FilmNewEntity entity = (FilmNewEntity) v.getTag();
				// true 为获取焦点状态
				if (hasFocus) {
					mt.setStart(hasFocus);
					mt.setText(entity.getName());
					mt.setTextColor(0xffffffff);
				} else {// 失去焦点状态
					mt.setStart(hasFocus);
					mt.setText(entity.getName());
					mt.setTextColor(0xff9a9a9a);
				}
			}
		}

	}

	/*
	 * 显示文字信息 (non-Javadoc)
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
				setItemViewTag(entity, 0, l1);
				setTextData(marqueeText1, entity, itemView1);
			} else if (index == 1) {
				setItemViewTag(entity, 1, l2);
				setTextData(marqueeText2, entity, itemView2);
			} else if (index == 2) {
				setItemViewTag(entity, 2, l3);
				setTextData(marqueeText3, entity, itemView3);
			} else if (index == 3) {
				setItemViewTag(entity, 3, l4);
				setTextData(marqueeText4, entity, itemView4);
			} else if (index == 4) {
				setItemViewTag(entity, 4, l5);
				setTextData(marqueeText5, entity, itemView5);
			} else if (index == 5) {
				setItemViewTag(entity, 5, l6);
				setTextData(marqueeText6, entity, itemView6);
			}
		}

	}

	/**
	 * 负责显示文字信息
	 * 
	 * @Title: FlimViewPageItemView
	 * @author:张鹏展
	 * @Description:
	 * @param marqueeText
	 * @param entity
	 */
	private void setTextData(MarqueeText marqueeText, FilmNewEntity entity, View itemView) {
		if (null != entity) {
			itemView.setVisibility(View.VISIBLE);
			marqueeText.setText(entity.getName());

		}
	}

	/*
	 * 显示海报图信息 (non-Javadoc)
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
				displayImage(imageView1, entity);
			} else if (index == 1) {
				displayImage(imageView2, entity);
			} else if (index == 2) {
				displayImage(imageView3, entity);
			} else if (index == 3) {
				displayImage(imageView4, entity);
			} else if (index == 4) {
				displayImage(imageView5, entity);
			} else if (index == 5) {
				displayImage(imageView6, entity);
			}
		}

	}

	/**
	 * 负责显示海报图
	 * 
	 * @Title: FlimViewPageItemView
	 * @author:张鹏展
	 * @Description:
	 * @param imageView
	 * @param entity
	 */
	private void displayImage(ImageView imageView, FilmNewEntity entity) {
		if (null != entity) {
			if (null == imageLoader)
				imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(entity.getPosterUrl(), imageView, optionsPoster);
		}
	}

	/*
	 * 销毁view 时候的方法 (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.view.HiveBaseView#destoryFromViewPager()
	 */
	@Override
	public void destoryFromViewPager() {
		// 所有的资源恢复到初始化状态

		itemView1.setVisibility(View.INVISIBLE);
		itemView2.setVisibility(View.INVISIBLE);
		itemView3.setVisibility(View.INVISIBLE);
		itemView4.setVisibility(View.INVISIBLE);
		itemView5.setVisibility(View.INVISIBLE);
		itemView6.setVisibility(View.INVISIBLE);

		imageView1.setImageResource(R.drawable.default_film_detail_item);
		imageView2.setImageResource(R.drawable.default_film_detail_item);
		imageView3.setImageResource(R.drawable.default_film_detail_item);
		imageView4.setImageResource(R.drawable.default_film_detail_item);
		imageView5.setImageResource(R.drawable.default_film_detail_item);
		imageView6.setImageResource(R.drawable.default_film_detail_item);

		marqueeText1.setText("");
		marqueeText2.setText("");
		marqueeText3.setText("");
		marqueeText4.setText("");
		marqueeText5.setText("");
		marqueeText6.setText("");
	}
	/**
	 * 添加item的点击事件
	 * @Title: VideoListItemPageView
	 * @author:张鹏展
	 * @Description: 
	 * @param listener
	 */
	public void setItemViewClickListener(OnClickListener listener) {
		l1.setOnClickListener(listener);
		l2.setOnClickListener(listener);
		l3.setOnClickListener(listener);
		l4.setOnClickListener(listener);
		l5.setOnClickListener(listener);
		l6.setOnClickListener(listener);
	}
}
