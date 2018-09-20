package com.hiveview.tv.view;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.tv.R;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.utils.ToolsUtils;
import com.hiveview.tv.view.VarietyViewPagerItem.MarqueeTextFochs;
import com.hiveview.tv.view.television.MarqueeText;

/**
 * @ClassName: LandSpaceItemViewContainer
 * @Description: 
 * @author: zhangpengzhan
 * @date 2014年6月20日 下午2:09:00
 * 
 */
public class LandSpaceItemViewContainer extends HiveBaseView {

	/**
	 * 播放记录中的viewpager 的包含的view 一共八个
	 */

	/**
	 * 包含一个view item 的 relativelayout
	 */
	private View item1;
	private View item2;
	private View item3;
	private View item4;

	/**
	 * 海报图片的iamgeview
	 */
	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	private ImageView image4;

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

	/**
	 * 图片下载器
	 */
	protected ImageLoader imageLoader = null;
	private DisplayImageOptions optionsPoster;

	public LandSpaceItemViewContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LandSpaceItemViewContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LandSpaceItemViewContainer(Context context, int everyPageSize, int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
	}

	@Override
	protected void initView() {
		initViewContainer(R.layout.subject_items_land_layout);
		// 图片下载器
		imageLoader = ImageLoader.getInstance();
		// 图片下载设置内容
		optionsPoster = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.variety_default_img)
				.showImageOnFail(R.drawable.variety_default_img).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
		// 获取焦点的外框
		item1 = findItemView(R.id.rl_variety_list_first_line_1);
		item2 = findItemView(R.id.rl_variety_list_first_line_2);
		item3 = findItemView(R.id.rl_variety_list_first_line_3);
		item4 = findItemView(R.id.rl_variety_list_first_line_4);

		// 海报图
		image1 = (ImageView) findViewById(R.id.rl_variety_list_first_line_1_item_image);
		image2 = (ImageView) findViewById(R.id.rl_variety_list_first_line_2_item_image);
		image3 = (ImageView) findViewById(R.id.rl_variety_list_first_line_3_item_image);
		image4 = (ImageView) findViewById(R.id.rl_variety_list_first_line_4_item_image);
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

		// 为获取焦点框的布局添加焦点监听
		item1.setOnFocusChangeListener(new MarqueeTextFochs(tv1_2, tv1_3));
		item2.setOnFocusChangeListener(new MarqueeTextFochs(tv2_2, tv2_3));
		item3.setOnFocusChangeListener(new MarqueeTextFochs(tv3_2, tv3_3));
		item4.setOnFocusChangeListener(new MarqueeTextFochs(tv4_2, tv4_3));

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
			if (hasFocus) {// 获取焦点的时候出现文字高亮
				mt.setTextColor(0xffffffff);
				mt2.setTextColor(0xffffffff);
			} else {// 失去焦点的时候文字恢复原来颜色
				mt.setTextColor(0xff9a9a9a);
				mt2.setTextColor(0xff9a9a9a);
			}
		}

	}

	@Override
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
			}
		}
	}

	// 添加文字信息
	private void setTextData(MarqueeText tv, MarqueeText tv2, MarqueeText tv3, FilmNewEntity entity, View ItemView) {
		ItemView.setVisibility(View.VISIBLE);
		int upDate = entity.getCurrCount();
		//爱奇艺新接口舍弃这个字段
//		if (upDate != 1) {
//			tv.setText("更新至" + String.valueOf(upDate) + "集");// time
//		} else {
//			DecimalFormat df = new DecimalFormat("00");
//			int mins = entity.getEqLen() / 60;
//			int sed = entity.getEqLen() % 60;
//			tv.setText(String.valueOf(mins) + ":" + String.valueOf(df.format(sed)));// time
//		}
		tv.setVisibility(View.INVISIBLE);
		tv2.setText(entity.getName());// name
		tv3.setText(entity.getFocusName());// focus
		Log.d("LandSpaceItemViewContainer==>", "tv_name::"+entity.getName()+"==tv_focus::"+entity.getFocusName());
		Log.d("LandSpaceItemViewContainer==>", "time::"+entity.getEqLen());
	}

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
			}
		}
	}

	/**
	 * 显示图片
	 * 
	 * @param imageView
	 * @param entity
	 */
	private void displayImage(ImageView imageView, FilmNewEntity entity) {
		if (null == imageLoader)  
			imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(entity.getPosterUrl(), imageView, optionsPoster);

	}

	@Override
	public void destoryFromViewPager() {
		item1.setVisibility(View.INVISIBLE);
		item2.setVisibility(View.INVISIBLE);
		item3.setVisibility(View.INVISIBLE);
		item4.setVisibility(View.INVISIBLE);

		image1.setImageResource(R.drawable.variety_default_img);
		image2.setImageResource(R.drawable.variety_default_img);
		image3.setImageResource(R.drawable.variety_default_img);
		image4.setImageResource(R.drawable.variety_default_img);
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
	}

	public void setItemViewClickListener(OnClickListener listener) {
		item1.setOnClickListener(listener);
		item2.setOnClickListener(listener);
		item3.setOnClickListener(listener);
		item4.setOnClickListener(listener);
	}

}
