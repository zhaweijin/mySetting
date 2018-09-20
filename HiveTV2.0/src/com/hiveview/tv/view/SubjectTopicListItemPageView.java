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
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.entity.SubjectEntity;
import com.hiveview.tv.service.entity.SubjectListEntity;
import com.hiveview.tv.view.television.MarqueeText;

/**
 * 类名 SubjectTopicListItemPageView
 * 
 * @author gusongsheng 专题列表 2014-4-17
 * 
 */
public class SubjectTopicListItemPageView extends HiveBaseView {

	private View item1;
	private View item2;
	private View item3;
	private View item4;
	private View item5;
	private View item6;

	private View item_poster1;
	private View item_poster2;
	private View item_poster3;
	private View item_poster4;
	private View item_poster5;
	private View item_poster6;

	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	private ImageView image4;
	private ImageView image5;
	private ImageView image6;

	private MarqueeText tv1;
	private MarqueeText tv2;
	private MarqueeText tv3;
	private MarqueeText tv4;
	private MarqueeText tv5;
	private MarqueeText tv6;

	private DisplayImageOptions optionsPoster;

	/**
	 * 图片下载器
	 */
	protected ImageLoader imageLoader = null;

	public SubjectTopicListItemPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SubjectTopicListItemPageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public SubjectTopicListItemPageView(Context context, int everyPageSize,
			int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
	}

	/**
	 * 初始化数据
	 */
	@Override
	protected void initView() {
		initViewContainer(R.layout.subject_list_topic_item_layout);
		//start author：zhangpengzhan  这个方法在初始化view 的时候初始化一次就行了
		optionsPoster = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.subject_default)
		.showImageOnFail(R.drawable.subject_default)
		.resetViewBeforeLoading(false).cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true)
		.build();
		//end
		item1 = findViewById(R.id.ll_item_first_line_01);
		item2 = findViewById(R.id.ll_item_first_line_02);
		item3 = findViewById(R.id.ll_item_first_line_03);
		item4 = findViewById(R.id.ll_item_second_line_01);
		item5 = findViewById(R.id.ll_item_second_line_02);
		item6 = findViewById(R.id.ll_item_second_line_03);

		item_poster1 = findItemView(R.id.ll_common_first_line_01);
		item_poster2 = findItemView(R.id.ll_common_first_line_02);
		item_poster3 = findItemView(R.id.ll_common_first_line_03);
		item_poster4 = findItemView(R.id.ll_common_second_line_01);
		item_poster5 = findItemView(R.id.ll_common_second_line_02);
		item_poster6 = findItemView(R.id.ll_common_second_line_03);

		image1 = (ImageView) findViewById(R.id.iv_common_first_line_01);
		image2 = (ImageView) findViewById(R.id.iv_common_first_line_02);
		image3 = (ImageView) findViewById(R.id.iv_common_first_line_03);
		image4 = (ImageView) findViewById(R.id.iv_common_second_line_01);
		image5 = (ImageView) findViewById(R.id.iv_common_second_line_02);
		image6 = (ImageView) findViewById(R.id.iv_common_second_line_03);

		tv1 = (MarqueeText) findViewById(R.id.tv_common_first_line_01);
		tv2 = (MarqueeText) findViewById(R.id.tv_common_first_line_02);
		tv3 = (MarqueeText) findViewById(R.id.tv_common_first_line_03);
		tv4 = (MarqueeText) findViewById(R.id.tv_common_second_line_01);
		tv5 = (MarqueeText) findViewById(R.id.tv_common_second_line_02);
		tv6 = (MarqueeText) findViewById(R.id.tv_common_second_line_03);

	}
	/**
	 * 焦点改变事件
	 */
	class ItemFocusChangeListener implements OnFocusChangeListener {
		//标题
		public MarqueeText tv;

		public ItemFocusChangeListener(MarqueeText tv) {
			this.tv = tv;
		}

		@Override
		public void onFocusChange(View v, boolean arg1) {
			if(null!=v&&null!=tv){
				SubjectListEntity entity = (SubjectListEntity) v.getTag();
				tv.setStart(arg1);
				tv.setText(entity.getSubjectName());
			}
			//获取焦点时字体颜色为白色 失去焦点时为灰色
			if(arg1){
				tv.setTextColor(0xffffffff);
			}else{
				tv.setTextColor(0xff9a9a9a);
			}
		}

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
		SubjectListEntity entity = (SubjectListEntity) baseEntity;
		if (null != entity) {
			if (index == 0) {
				item_poster1.setOnFocusChangeListener(new ItemFocusChangeListener(tv1));
				setItemViewTag(entity, 0, item_poster1);
				setTextData(tv1, entity, item1);
			} else if (index == 1) {
				item_poster2.setOnFocusChangeListener(new ItemFocusChangeListener(tv2));
				setItemViewTag(entity, 1, item_poster2);
				setTextData(tv2, entity, item2);
			} else if (index == 2) {
				item_poster3.setOnFocusChangeListener(new ItemFocusChangeListener(tv3));
				setItemViewTag(entity, 2, item_poster3);
				setTextData(tv3, entity, item3);
			} else if (index == 3) {
				item_poster4.setOnFocusChangeListener(new ItemFocusChangeListener(tv4));
				setItemViewTag(entity, 3, item_poster4);
				setTextData(tv4, entity, item4);
			} else if (index == 4) {
				item_poster5.setOnFocusChangeListener(new ItemFocusChangeListener(tv5));
				setItemViewTag(entity, 4, item_poster5);
				setTextData(tv5, entity, item5);
			} else if (index == 5) {
				item_poster6.setOnFocusChangeListener(new ItemFocusChangeListener(tv6));
				setItemViewTag(entity, 5, item_poster6);
				setTextData(tv6, entity, item6);
			}
		}
	}

	/**
	 * 为TextView赋值
	 * 
	 * @param tv
	 *            当前的TextView
	 * @param entity
	 *            SubjectTopicEntity数据对象
	 * @param ItemView
	 */
	private void setTextData(TextView tv, SubjectListEntity entity,
			View itemView) {
		tv.setText(entity.getSubjectName());
		itemView.setVisibility(View.VISIBLE);
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
		SubjectListEntity entity = (SubjectListEntity) baseEntity;
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

	public void setItemViewClickListener(OnClickListener listener) {
		item_poster1.setOnClickListener(listener);
		item_poster2.setOnClickListener(listener);
		item_poster3.setOnClickListener(listener);
		item_poster4.setOnClickListener(listener);
		item_poster5.setOnClickListener(listener);
		item_poster6.setOnClickListener(listener);
	}

	/**
	 * 加载图片
	 * 
	 * @param imageView
	 *            ImagerView
	 * @param entity
	 */
	private void displayImage(ImageView imageView, SubjectListEntity entity) {
		//start author zhangpengzhan
		//只有在为空的情况下初始化一次就ok了
		if(null == imageLoader)
			imageLoader = ImageLoader.getInstance();
		//这个也是需要配置一次的
	/*	optionsPoster = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.subject_default)
				.showImageOnFail(R.drawable.subject_default)
				.resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.build();*/
		//end 
		imageLoader.displayImage(entity.getSubjectPic(), imageView,
				optionsPoster);
	}

	@Override
	public void destoryFromViewPager() {
		image1.setImageResource(R.drawable.subject_default);
		image2.setImageResource(R.drawable.subject_default);
		image3.setImageResource(R.drawable.subject_default);
		image4.setImageResource(R.drawable.subject_default);
		image5.setImageResource(R.drawable.subject_default);
		image6.setImageResource(R.drawable.subject_default);
		tv1.setText("");
		tv2.setText("");
		tv3.setText("");
		tv4.setText("");
		tv5.setText("");
		tv6.setText("");
	}

}
