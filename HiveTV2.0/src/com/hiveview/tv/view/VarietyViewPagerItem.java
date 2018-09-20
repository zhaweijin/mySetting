package com.hiveview.tv.view;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.FailReason;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageLoadingListener;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.tv.R;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.utils.StringUtils;
import com.hiveview.tv.utils.ToolsUtils;
import com.hiveview.tv.view.television.MarqueeText;

/**
 * @author zhangpengzhan
 * 
 *         2014年4月14日 下午2:57:10
 */
public class VarietyViewPagerItem extends HiveBaseView {

	/**
	 * 播放记录中的viewpager 的包含的view 一共八个
	 */

	protected static final String TAG = "VarietyViewPagerItem";
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

	/**
	 * 文字信息，依次是 电视剧名称 剩余时间 第几集 或者是第几期 一句话点看
	 */
	private MarqueeText tv1;
	private MarqueeText tv1_2;
	private MarqueeText tv1_3;

	private MarqueeText tv2;
	private MarqueeText tv2_2;
	private MarqueeText tv2_3;

	private MarqueeText tv3;
	private MarqueeText tv3_2;
	private MarqueeText tv3_3;

	private MarqueeText tv4;
	private MarqueeText tv4_2;
	private MarqueeText tv4_3;

	private MarqueeText tv5;
	private MarqueeText tv5_2;
	private MarqueeText tv5_3;

	private MarqueeText tv6;
	private MarqueeText tv6_2;
	private MarqueeText tv6_3;

	private MarqueeText tv7;
	private MarqueeText tv7_2;
	private MarqueeText tv7_3;
	private MarqueeText tv8;
	private MarqueeText tv8_2;
	private MarqueeText tv8_3;

	/**
	 * 图片下载器
	 */
	protected ImageLoader imageLoader = null;
	private DisplayImageOptions optionsPoster;

	public VarietyViewPagerItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VarietyViewPagerItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public VarietyViewPagerItem(Context context, int everyPageSize, int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
	}

	/*
	 * (non-Javadoc) 初始化 viewpager 页面的控件
	 * 
	 * @see com.hiveview.box.framework.view.HiveBaseView#initView() 初始化 view
	 */
	@Override
	protected void initView() {
		// 图片下载器
		imageLoader = ImageLoader.getInstance();
		// 图片下载设置内容
		optionsPoster = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.variety_default_img)
				.showImageOnFail(R.drawable.variety_default_img).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
		initViewContainer(R.layout.variety_new_list_item_layout);
		// 获取焦点的外框
		item1 = findItemView(R.id.rl_variety_list_first_line_1);
		item2 = findItemView(R.id.rl_variety_list_first_line_2);
		item3 = findItemView(R.id.rl_variety_list_first_line_3);
		item4 = findItemView(R.id.rl_variety_list_first_line_4);
		item5 = findItemView(R.id.rl_variety_list_second_line_1);
		item6 = findItemView(R.id.rl_variety_list_second_line_2);
		item7 = findItemView(R.id.rl_variety_list_second_line_3);
		item8 = findItemView(R.id.rl_variety_list_second_line_4);

		// 海报图
		image1 = (ImageView) findViewById(R.id.rl_variety_list_first_line_1_item_image);
		image2 = (ImageView) findViewById(R.id.rl_variety_list_first_line_2_item_image);
		image3 = (ImageView) findViewById(R.id.rl_variety_list_first_line_3_item_image);
		image4 = (ImageView) findViewById(R.id.rl_variety_list_first_line_4_item_image);
		image5 = (ImageView) findViewById(R.id.rl_variety_list_second_line_1_item_image);
		image6 = (ImageView) findViewById(R.id.rl_variety_list_second_line_2_item_image);
		image7 = (ImageView) findViewById(R.id.rl_variety_list_second_line_3_item_image);
		image8 = (ImageView) findViewById(R.id.rl_variety_list_second_line_4_item_image);
		// 期数 看点 和标题文字信息
		tv1 = (MarqueeText) findViewById(R.id.rl_variety_list_first_line_1_item_time);
		tv1_2 = (MarqueeText) findViewById(R.id.rl_variety_list_first_line_1_item_name);
		tv1_3 = (MarqueeText) findViewById(R.id.rl_variety_list_first_line_1_item_feature);

		tv2 = (MarqueeText) findViewById(R.id.rl_variety_list_first_line_2_item_time);
		tv2_2 = (MarqueeText) findViewById(R.id.rl_variety_list_first_line_2_item_name);
		tv2_3 = (MarqueeText) findViewById(R.id.rl_variety_list_first_line_2_item_feature);

		tv3 = (MarqueeText) findViewById(R.id.rl_variety_list_first_line_3_item_time);
		tv3_2 = (MarqueeText) findViewById(R.id.rl_variety_list_first_line_3_item_name);
		tv3_3 = (MarqueeText) findViewById(R.id.rl_variety_list_first_line_3_item_feature);

		tv4 = (MarqueeText) findViewById(R.id.rl_variety_list_first_line_4_item_time);
		tv4_2 = (MarqueeText) findViewById(R.id.rl_variety_list_first_line_4_item_name);
		tv4_3 = (MarqueeText) findViewById(R.id.rl_variety_list_first_line_4_item_feature);

		tv5 = (MarqueeText) findViewById(R.id.rl_variety_list_second_line_1_item_time);
		tv5_2 = (MarqueeText) findViewById(R.id.rl_variety_list_second_line_1_item_name);
		tv5_3 = (MarqueeText) findViewById(R.id.rl_variety_list_second_line_1_item_feature);

		tv6 = (MarqueeText) findViewById(R.id.rl_variety_list_second_line_2_item_time);
		tv6_2 = (MarqueeText) findViewById(R.id.rl_variety_list_second_line_2_item_name);
		tv6_3 = (MarqueeText) findViewById(R.id.rl_variety_list_second_line_2_item_feature);

		tv7 = (MarqueeText) findViewById(R.id.rl_variety_list_second_line_3_item_time);
		tv7_2 = (MarqueeText) findViewById(R.id.rl_variety_list_second_line_3_item_name);
		tv7_3 = (MarqueeText) findViewById(R.id.rl_variety_list_second_line_3_item_feature);

		tv8 = (MarqueeText) findViewById(R.id.rl_variety_list_second_line_4_item_time);
		tv8_2 = (MarqueeText) findViewById(R.id.rl_variety_list_second_line_4_item_name);
		tv8_3 = (MarqueeText) findViewById(R.id.rl_variety_list_second_line_4_item_feature);
		// 为获取焦点框的布局添加焦点监听
		item1.setOnFocusChangeListener(new MarqueeTextFochs(tv1_2, tv1_3));
		item2.setOnFocusChangeListener(new MarqueeTextFochs(tv2_2, tv2_3));
		item3.setOnFocusChangeListener(new MarqueeTextFochs(tv3_2, tv3_3));
		item4.setOnFocusChangeListener(new MarqueeTextFochs(tv4_2, tv4_3));
		item5.setOnFocusChangeListener(new MarqueeTextFochs(tv5_2, tv5_3));
		item6.setOnFocusChangeListener(new MarqueeTextFochs(tv6_2, tv6_3));
		item7.setOnFocusChangeListener(new MarqueeTextFochs(tv7_2, tv7_3));
		item8.setOnFocusChangeListener(new MarqueeTextFochs(tv8_2, tv8_3));

	}

	/**
	 * 焦点监听事件，负责在获取焦点的时候，让文字出现走马灯效果
	 * 
	 * @ClassName: MarqueeTextFochs
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月4日 下午3:42:59
	 * 
	 */
	class MarqueeTextFochs implements OnFocusChangeListener {
		MarqueeText mt;
		MarqueeText mt2;

		public MarqueeTextFochs(MarqueeText mt, MarqueeText mt2) {
			this.mt = mt;
			this.mt2 = mt2;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {

			if (null != focusChangeListeners) {
				focusChangeListeners.viewFocusListener(v, hasFocus);
			}
			if (hasFocus) {// 获取焦点的时候出现文字高亮
				mt.setTextColor(0xffffffff);
				mt2.setTextColor(0xffffffff);
			} else {// 失去焦点的时候文字恢复原来颜色
				mt.setTextColor(0xff9a9a9a);
				mt2.setTextColor(0xff9a9a9a);
			}
		}

	}

	/**
	 * @ClassName: ViewItemFocusChangeListeners
	 * @Description: view 焦点监听
	 * @author: zhangpengzhan
	 * @date 2014年8月18日 下午1:10:28
	 * 
	 */
	public interface ViewItemFocusChangeListeners {
		public void viewFocusListener(View arg0, boolean arg1);
	}

	/**
	 * @Fields focusChangeListeners:焦点监听实例
	 */
	private ViewItemFocusChangeListeners focusChangeListeners;

	public void setViewItemFocusChangeListeners(ViewItemFocusChangeListeners focusChangeListeners) {
		this.focusChangeListeners = focusChangeListeners;
	}

	/*
	 * (non-Javadoc) 对应的关系 设置文字显示每个viewitem 的资源和信息
	 * 
	 * @see
	 * com.hiveview.box.framework.view.HiveBaseView#displayTextDataToItemView
	 * (com.hiveview.box.framework.entity.HiveBaseEntity, int) 填充实体类 和相关信息
	 */
	public void displayTextDataToItemView(HiveBaseEntity baseEntity, int index) {
		FilmNewEntity entity = (FilmNewEntity) baseEntity;
		if (null != entity) {
			if (index == 0) {
				setItemViewTag(entity, 0, item1);
				setTextData(tv1, tv1_2, tv1_3, entity, item1);
			} else if (index == 1) {
				setItemViewTag(entity, 1, item2);
				setTextData(tv2, tv2_2, tv2_3, entity, item2);
			} else if (index == 2) {
				setItemViewTag(entity, 2, item3);
				setTextData(tv3, tv3_2, tv3_3, entity, item3);
			} else if (index == 3) {
				setItemViewTag(entity, 3, item4);
				setTextData(tv4, tv4_2, tv4_3, entity, item4);
			} else if (index == 4) {
				setItemViewTag(entity, 4, item5);
				setTextData(tv5, tv5_2, tv5_3, entity, item5);
			} else if (index == 5) {
				setItemViewTag(entity, 5, item6);
				setTextData(tv6, tv6_2, tv6_3, entity, item6);
			} else if (index == 6) {
				setItemViewTag(entity, 6, item7);
				setTextData(tv7, tv7_2, tv7_3, entity, item7);
			} else if (index == 7) {
				setItemViewTag(entity, 7, item8);
				setTextData(tv8, tv8_2, tv8_3, entity, item8);
			}
		}
	}

	// 添加文字信息
	private void setTextData(MarqueeText tv, MarqueeText tv2, MarqueeText tv3, FilmNewEntity entity, View ItemView) {
		ItemView.setVisibility(View.VISIBLE);
		int upDate = entity.getCurrCount();
		if (upDate != 0) {
			tv.setText(String.format(getResources
					  ().getString(R.string.search_landscape_time_update), entity.getCurrCount()));// time
		} else {
			DecimalFormat df = new DecimalFormat("00");
			int mins = entity.getEqLen() / 60;
			int sed = entity.getEqLen() % 60;
			tv.setText(String.valueOf(mins) + ":" + String.valueOf(df.format(sed)));// time
		}	
			if(!StringUtils.isEmpty(entity.getName())){
				tv2.setText(entity.getName());// name
			}
			if(!StringUtils.isEmpty(entity.getFocusName())){
				tv3.setText(entity.getFocusName());// focus
			}
	}

	/*
	 * (non-Javadoc) 设置图片的对应关系和相关资源
	 * 
	 * @see
	 * com.hiveview.box.framework.view.HiveBaseView#displayImageToItemView(com
	 * .hiveview.box.framework.entity.HiveBaseEntity, int) 图片信息 列表的映射关系
	 */
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
			}
		}
	}

	/**
	 * @param listener
	 *            添加view 的按键监听
	 */
	public void setItemViewClickListener(OnClickListener listener) {
		item1.setOnClickListener(listener);
		item2.setOnClickListener(listener);
		item3.setOnClickListener(listener);
		item4.setOnClickListener(listener);
		item5.setOnClickListener(listener);
		item6.setOnClickListener(listener);
		item7.setOnClickListener(listener);
		item8.setOnClickListener(listener);
	}

	/**
	 * 显示图片
	 * 
	 * @param imageView
	 * @param entity
	 */
	private void displayImage(final ImageView imageView, final FilmNewEntity entity) {
		if (null == imageLoader) {
			imageLoader = ImageLoader.getInstance();
		}
		imageLoader.displayImage(ToolsUtils.createImgUrl(entity.getPosterUrl(), false), imageView, optionsPoster,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				if(entity.getName()!=null){
				Log.v(TAG, "onLoadingFailed   "+entity.getName());}
				if(entity.getPicUrl()!=null){
				imageLoader.displayImage(ToolsUtils.createImgUrl(entity.getPicUrl(), false), imageView, optionsPoster);
				}}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	/*
	 * 重置view资源 (non-Javadoc)
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

		image1.setImageResource(R.drawable.variety_default_img);
		image2.setImageResource(R.drawable.variety_default_img);
		image3.setImageResource(R.drawable.variety_default_img);
		image4.setImageResource(R.drawable.variety_default_img);
		image5.setImageResource(R.drawable.variety_default_img);
		image6.setImageResource(R.drawable.variety_default_img);
		image7.setImageResource(R.drawable.variety_default_img);
		image8.setImageResource(R.drawable.variety_default_img);
		tv1.setText("");
		tv1_2.setText("");
		tv1_3.setText("");
		tv2.setText("");
		tv2_2.setText("");
		tv2_3.setText("");
		tv3.setText("");
		tv3_2.setText("");
		tv3_3.setText("");
		tv4.setText("");
		tv4_2.setText("");
		tv4_3.setText("");
		tv5.setText("");
		tv5_2.setText("");
		tv5_3.setText("");
		tv6.setText("");
		tv6_2.setText("");
		tv6_3.setText("");
		tv7.setText("");
		tv7_2.setText("");
		tv7_3.setText("");
		tv8.setText("");
		tv8_2.setText("");
		tv8_3.setText("");
	}

}
