package com.hiveview.tv.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.tv.R;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.PlayerRecordEntity;
import com.hiveview.tv.utils.ToolsUtils;
import com.hiveview.tv.view.VideoListItemPageView.GetFocusEntity;
import com.hiveview.tv.view.television.MarqueeText;

/**
 * @author zhangpengzhan
 * 
 *         2014年4月14日 下午2:57:10
 */
public class PlayerRecordListItemPageView extends HiveBaseView {

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

	public PlayerRecordListItemPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PlayerRecordListItemPageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PlayerRecordListItemPageView(Context context, int everyPageSize, int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.view.HiveBaseView#initView()
	 */
	@Override
	protected void initView() {
		initViewContainer(R.layout.playerreocrd_list_item_layout);
		imageLoader = ImageLoader.getInstance();

		optionsPoster = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.variety_default_img)
				.showImageOnFail(R.drawable.variety_default_img).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
		// 每个item的最外层的布局
		item1 = findViewById(R.id.playerrecord_video_item1);
		item2 = findViewById(R.id.playerrecord_video_item2);
		item3 = findViewById(R.id.playerrecord_video_item3);
		item4 = findViewById(R.id.playerrecord_video_item4);
		item5 = findViewById(R.id.playerrecord_video_item5);
		item6 = findViewById(R.id.playerrecord_video_item6);
		item7 = findViewById(R.id.playerrecord_video_item7);
		item8 = findViewById(R.id.playerrecord_video_item8);
		item9 = findViewById(R.id.playerrecord_video_item9);
		item10 = findViewById(R.id.playerrecord_video_item10);
		item11 = findViewById(R.id.playerrecord_video_item11);
		item12 = findViewById(R.id.playerrecord_video_item12);
		// 要获取焦点的海报图
		image1 = (ImageView) findItemView(R.id.playerrecord_item1_poster_iv);
		image2 = (ImageView) findItemView(R.id.playerrecord_item2_poster_iv);
		image3 = (ImageView) findItemView(R.id.playerrecord_item3_poster_iv);
		image4 = (ImageView) findItemView(R.id.playerrecord_item4_poster_iv);
		image5 = (ImageView) findItemView(R.id.playerrecord_item5_poster_iv);
		image6 = (ImageView) findItemView(R.id.playerrecord_item6_poster_iv);
		image7 = (ImageView) findItemView(R.id.playerrecord_item7_poster_iv);
		image8 = (ImageView) findItemView(R.id.playerrecord_item8_poster_iv);
		image9 = (ImageView) findItemView(R.id.playerrecord_item9_poster_iv);
		image10 = (ImageView) findItemView(R.id.playerrecord_item10_poster_iv);
		image11 = (ImageView) findItemView(R.id.playerrecord_item11_poster_iv);
		image12 = (ImageView) findItemView(R.id.playerrecord_item12_poster_iv);

		// start:设置第二行的焦点 指向第一行的焦点 author:huzuwei
		image7.setNextFocusUpId(image1.getId());
		image8.setNextFocusUpId(image2.getId());
		image9.setNextFocusUpId(image3.getId());
		image10.setNextFocusUpId(image4.getId());
		image11.setNextFocusUpId(image5.getId());
		image12.setNextFocusUpId(image6.getId());
		// end

		// 文字信息分别是，期数、剩余时间、标题、一句话看点
		tv1 = (MarqueeText) findViewById(R.id.playerrecord_item1_title_tv);
		tv1_2 = (MarqueeText) findViewById(R.id.playerrecord_item1_title_tv2);
		tv1_3 = (MarqueeText) findViewById(R.id.playerrecord_item1_title_tv3);
		tv1_4 = (MarqueeText) findViewById(R.id.playerrecord_item1_title_tv4);
		tv2 = (MarqueeText) findViewById(R.id.playerrecord_item2_title_tv);
		tv2_2 = (MarqueeText) findViewById(R.id.playerrecord_item2_title_tv2);
		tv2_3 = (MarqueeText) findViewById(R.id.playerrecord_item2_title_tv3);
		tv2_4 = (MarqueeText) findViewById(R.id.playerrecord_item2_title_tv4);
		tv3 = (MarqueeText) findViewById(R.id.playerrecord_item3_title_tv);
		tv3_2 = (MarqueeText) findViewById(R.id.playerrecord_item3_title_tv2);
		tv3_3 = (MarqueeText) findViewById(R.id.playerrecord_item3_title_tv3);
		tv3_4 = (MarqueeText) findViewById(R.id.playerrecord_item3_title_tv4);
		tv4 = (MarqueeText) findViewById(R.id.playerrecord_item4_title_tv);
		tv4_2 = (MarqueeText) findViewById(R.id.playerrecord_item4_title_tv2);
		tv4_3 = (MarqueeText) findViewById(R.id.playerrecord_item4_title_tv3);
		tv4_4 = (MarqueeText) findViewById(R.id.playerrecord_item4_title_tv4);
		tv5 = (MarqueeText) findViewById(R.id.playerrecord_item5_title_tv);
		tv5_2 = (MarqueeText) findViewById(R.id.playerrecord_item5_title_tv2);
		tv5_3 = (MarqueeText) findViewById(R.id.playerrecord_item5_title_tv3);
		tv5_4 = (MarqueeText) findViewById(R.id.playerrecord_item5_title_tv4);
		tv6 = (MarqueeText) findViewById(R.id.playerrecord_item6_title_tv);
		tv6_2 = (MarqueeText) findViewById(R.id.playerrecord_item6_title_tv2);
		tv6_3 = (MarqueeText) findViewById(R.id.playerrecord_item6_title_tv3);
		tv6_4 = (MarqueeText) findViewById(R.id.playerrecord_item6_title_tv4);
		tv7 = (MarqueeText) findViewById(R.id.playerrecord_item7_title_tv);
		tv7_2 = (MarqueeText) findViewById(R.id.playerrecord_item7_title_tv2);
		tv7_3 = (MarqueeText) findViewById(R.id.playerrecord_item7_title_tv3);
		tv7_4 = (MarqueeText) findViewById(R.id.playerrecord_item7_title_tv4);
		tv8 = (MarqueeText) findViewById(R.id.playerrecord_item8_title_tv);
		tv8_2 = (MarqueeText) findViewById(R.id.playerrecord_item8_title_tv2);
		tv8_3 = (MarqueeText) findViewById(R.id.playerrecord_item8_title_tv3);
		tv8_4 = (MarqueeText) findViewById(R.id.playerrecord_item8_title_tv4);
		tv9 = (MarqueeText) findViewById(R.id.playerrecord_item9_title_tv);
		tv9_2 = (MarqueeText) findViewById(R.id.playerrecord_item9_title_tv2);
		tv9_3 = (MarqueeText) findViewById(R.id.playerrecord_item9_title_tv3);
		tv9_4 = (MarqueeText) findViewById(R.id.playerrecord_item9_title_tv4);
		tv10 = (MarqueeText) findViewById(R.id.playerrecord_item10_title_tv);
		tv10_2 = (MarqueeText) findViewById(R.id.playerrecord_item10_title_tv2);
		tv10_3 = (MarqueeText) findViewById(R.id.playerrecord_item10_title_tv3);
		tv10_4 = (MarqueeText) findViewById(R.id.playerrecord_item10_title_tv4);
		tv11 = (MarqueeText) findViewById(R.id.playerrecord_item11_title_tv);
		tv11_2 = (MarqueeText) findViewById(R.id.playerrecord_item11_title_tv2);
		tv11_3 = (MarqueeText) findViewById(R.id.playerrecord_item11_title_tv3);
		tv11_4 = (MarqueeText) findViewById(R.id.playerrecord_item11_title_tv4);
		tv12 = (MarqueeText) findViewById(R.id.playerrecord_item12_title_tv);
		tv12_2 = (MarqueeText) findViewById(R.id.playerrecord_item12_title_tv2);
		tv12_3 = (MarqueeText) findViewById(R.id.playerrecord_item12_title_tv3);
		tv12_4 = (MarqueeText) findViewById(R.id.playerrecord_item12_title_tv4);
		// 在海报图获取焦点的时候，让文字出现走马灯状态
		image1.setOnFocusChangeListener(new MarqueeTextStart(tv1, tv1_4, 0));
		image2.setOnFocusChangeListener(new MarqueeTextStart(tv2, tv2_4, 1));
		image3.setOnFocusChangeListener(new MarqueeTextStart(tv3, tv3_4, 2));
		image4.setOnFocusChangeListener(new MarqueeTextStart(tv4, tv4_4, 3));
		image5.setOnFocusChangeListener(new MarqueeTextStart(tv5, tv5_4, 4));
		image6.setOnFocusChangeListener(new MarqueeTextStart(tv6, tv6_4, 5));
		image7.setOnFocusChangeListener(new MarqueeTextStart(tv7, tv7_4, 6));
		image8.setOnFocusChangeListener(new MarqueeTextStart(tv8, tv8_4, 7));
		image9.setOnFocusChangeListener(new MarqueeTextStart(tv9, tv9_4, 8));
		image10.setOnFocusChangeListener(new MarqueeTextStart(tv10, tv10_4, 9));
		image11.setOnFocusChangeListener(new MarqueeTextStart(tv11, tv11_4, 10));
		image12.setOnFocusChangeListener(new MarqueeTextStart(tv12, tv12_4, 11));

	}

	private String TAG = "PlayerRecordListItemPageView";

	/**
	 * 文字走马灯的状态设置
	 * 
	 * @ClassName: MarqueeTextStart
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月4日 下午1:36:00
	 * 
	 */
	class MarqueeTextStart implements OnFocusChangeListener {
		MarqueeText tv;
		MarqueeText tv2;
		int viewIndex;

		public MarqueeTextStart(MarqueeText tv, MarqueeText tv2, int viewIndex) {
			this.tv = tv;
			this.tv2 = tv2;
			this.viewIndex = viewIndex;
		}

		PlayerRecordEntity playerRecordEntity;

		/*
		 * 修改看点也能走马灯滚动 (non-Javadoc)
		 * 
		 * @see
		 * android.view.View.OnFocusChangeListener#onFocusChange(android.view
		 * .View, boolean)
		 */
		public void onFocusChange(View v, boolean hasFocus) {
			Log.d(TAG, "====listitempageview=");
			playerRecordEntity = (PlayerRecordEntity) v.getTag();
			if (null != playerRecordEntity) {
				if (hasFocus) {// 获取焦点的时候出现高亮的颜色
					// image.requestFocus();
					focusEntity.getFocusEntity(playerRecordEntity, viewIndex);
					mFocusListener.onCallBackFocusChange(v, hasFocus);
					tv.setTextColor(0xffffffff);
					tv2.setTextColor(0xffffffff);
					tv.setStart(hasFocus);
					tv.setText(playerRecordEntity.getName());
					tv2.setStart(hasFocus);
					tv2.setText(playerRecordEntity.getDescription());

				} else {// 失去焦点的时候文字显示灰色
					tv.setStart(hasFocus);
					tv.setText(playerRecordEntity.getName());
					tv.setTextColor(0xff9a9a9a);
					tv2.setStart(hasFocus);
					tv2.setText(playerRecordEntity.getDescription());
					tv2.setTextColor(0xff9a9a9a);
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
		PlayerRecordEntity entity = (PlayerRecordEntity) baseEntity;
		if (null != entity) {
			if (index == 0) {
				setItemViewTag(entity, 0, image1);
				setTextData(tv1, tv1_2, tv1_3, tv1_4, entity, item1);
				setImageViewFrame(image1, entity);
			} else if (index == 2) {
				setItemViewTag(entity, 1, image2);
				setTextData(tv2, tv2_2, tv2_3, tv2_4, entity, item2);
				setImageViewFrame(image2, entity);
			} else if (index == 4) {
				setItemViewTag(entity, 2, image3);
				setTextData(tv3, tv3_2, tv3_3, tv3_4, entity, item3);
				setImageViewFrame(image3, entity);
			} else if (index == 6) {
				setItemViewTag(entity, 3, image4);
				setTextData(tv4, tv4_2, tv4_3, tv4_4, entity, item4);
				setImageViewFrame(image4, entity);
			} else if (index == 8) {
				setItemViewTag(entity, 4, image5);
				setTextData(tv5, tv5_2, tv5_3, tv5_4, entity, item5);
				setImageViewFrame(image5, entity);
			} else if (index == 10) {
				setItemViewTag(entity, 5, image6);
				setTextData(tv6, tv6_2, tv6_3, tv6_4, entity, item6);
				setImageViewFrame(image6, entity);
			} else if (index == 1) {
				setItemViewTag(entity, 6, image7);
				setTextData(tv7, tv7_2, tv7_3, tv7_4, entity, item7);
				setImageViewFrame(image7, entity);
			} else if (index == 3) {
				setItemViewTag(entity, 7, image8);
				setTextData(tv8, tv8_2, tv8_3, tv8_4, entity, item8);
				setImageViewFrame(image8, entity);
			} else if (index == 5) {
				setItemViewTag(entity, 8, image9);
				setTextData(tv9, tv9_2, tv9_3, tv9_4, entity, item9);
				setImageViewFrame(image9, entity);
			} else if (index == 7) {
				setItemViewTag(entity, 9, image10);
				setTextData(tv10, tv10_2, tv10_3, tv10_4, entity, item10);
				setImageViewFrame(image10, entity);
			} else if (index == 9) {
				setItemViewTag(entity, 10, image11);
				setTextData(tv11, tv11_2, tv11_3, tv11_4, entity, item11);
				setImageViewFrame(image11, entity);
			} else if (index == 11) {
				setItemViewTag(entity, 11, image12);
				setTextData(tv12, tv12_2, tv12_3, tv12_4, entity, item12);
				setImageViewFrame(image12, entity);
			}
		}
	}

	// tv 电视剧名字； tv2 剩余时间 ；tv3 集数或者期号；tv4 一句话看点
	private void setTextData(TextView tv, TextView tv2, TextView tv3, TextView tv4, PlayerRecordEntity entity, View ItemView) {
		if (entity.isShow()) {
			ItemView.setVisibility(View.VISIBLE);
			tv.setText(entity.getName());
			tv2.setText(entity.getSurplusTime());
			if (Boolean.valueOf(entity.getOrientation())) {// true 竖图
				tv3.setVisibility(View.VISIBLE);
				tv4.setVisibility(View.INVISIBLE);
				tv3.setText(entity.getAlbums());
			} else {
				tv3.setVisibility(View.VISIBLE);
				tv3.setText(entity.getAlbums());
				tv4.setVisibility(View.VISIBLE);
				tv4.setText(entity.getDescription());
			}
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
		PlayerRecordEntity entity = (PlayerRecordEntity) baseEntity;
		if (null != entity) {
			if (index == 0) {
				displayImage(image1, entity);
			} else if (index == 2) {
				displayImage(image2, entity);
			} else if (index == 4) {
				displayImage(image3, entity);
			} else if (index == 6) {
				displayImage(image4, entity);
			} else if (index == 8) {
				displayImage(image5, entity);
			} else if (index == 10) {
				displayImage(image6, entity);
			} else if (index == 1) {
				displayImage(image7, entity);
			} else if (index == 3) {
				displayImage(image8, entity);
			} else if (index == 5) {
				displayImage(image9, entity);
			} else if (index == 7) {
				displayImage(image10, entity);
			} else if (index == 9) {
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
		image1.setOnClickListener(listener);
		image2.setOnClickListener(listener);
		image3.setOnClickListener(listener);
		image4.setOnClickListener(listener);
		image5.setOnClickListener(listener);
		image6.setOnClickListener(listener);
		image7.setOnClickListener(listener);
		image8.setOnClickListener(listener);
		image9.setOnClickListener(listener);
		image10.setOnClickListener(listener);
		image11.setOnClickListener(listener);
		image12.setOnClickListener(listener);
	}

	/**
	 * 设置图片的横竖状态
	 * 
	 * @Title: PlayerRecordListItemPageView
	 * @author:张鹏展
	 * @Description:
	 * @param imageView
	 * @param entity
	 */
	private void setImageViewFrame(ImageView imageView, PlayerRecordEntity entity) {
		if (!Boolean.valueOf(entity.getOrientation())) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(180, 120);
			imageView.setLayoutParams(params);

		}
	}

	/**
	 * 显示图片
	 * 
	 * @Title: PlayerRecordListItemPageView
	 * @author:张鹏展
	 * @Description:
	 * @param imageView
	 * @param entity
	 */
	private void displayImage(ImageView imageView, PlayerRecordEntity entity) {
		// 判断是否是横竖图片

		if (null == imageLoader) {
			imageLoader = ImageLoader.getInstance();
		}

		if (entity.getSource() == 0) {//点播 ==0
			if (null != entity && null != entity.getImage() && !entity.getImage().equals("") && !entity.getImage().equals("null")) {
				imageLoader.displayImage(ToolsUtils.createImgUrl(entity.getImage(), Boolean.valueOf(entity.getOrientation())), imageView,
						optionsPoster);
			}
		} else {//极清 ==1
			imageLoader.displayImage(entity.getImage(), imageView, optionsPoster);
		}

	}

	/*
	 * 不会超过10页用不着调用销毁方法 (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.view.HiveBaseView#destoryFromViewPager()
	 */
	@Override
	public void destoryFromViewPager() {

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
		public void getFocusEntity(PlayerRecordEntity playerRecordEntity, int entityIndex);
	}

}
