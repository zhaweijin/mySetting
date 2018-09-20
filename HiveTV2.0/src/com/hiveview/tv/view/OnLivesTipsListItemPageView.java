package com.hiveview.tv.view;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.BaseActivity;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.OnliveTipsEntity;
import com.hiveview.tv.view.television.MarqueeText;

/**
 * @ClassName: OnLivesTipsListItemPageView
 * @Description: TODO
 * @author: zhangpengzhan
 * @date 2014年5月20日 上午10:22:51
 * 
 */
public class OnLivesTipsListItemPageView extends HiveBaseView {
	/**
	 * TAG
	 */
	private static final String TAG = "OnLivesTipsListItemPageView";
	private Context mContext;

	private FrameLayout flFirstLine01;
	private FrameLayout flFirstLine02;
	private FrameLayout flFirstLine03;
	private FrameLayout flFirstLine04;
	// private FrameLayout flFirstLine05;

	private FrameLayout flSecondLine01;
	private FrameLayout flSecondLine02;
	private FrameLayout flSecondLine03;
	private FrameLayout flSecondLine04;
	// private FrameLayout flSecondLine05;

	private ImageView ivFocus01;
	private ImageView ivFocus02;
	private ImageView ivFocus03;
	private ImageView ivFocus04;
	private ImageView ivFocus05;
	private ImageView ivFocus06;
	private ImageView ivFocus07;
	private ImageView ivFocus08;
	private ImageView ivFocus09;
	private ImageView ivFocus10;

	private TextView tvFocus01;
	private TextView tvFocus02;
	private TextView tvFocus03;
	private TextView tvFocus04;
	// private TextView tvFocus05;
	private TextView tvFocus06;
	private TextView tvFocus07;
	private TextView tvFocus08;
	private TextView tvFocus09;
	// private TextView tvFocus10;

	private LinearLayout llFocus01;

	private LinearLayout llFocus02;
	private LinearLayout llFocus03;
	private LinearLayout llFocus04;
	// private LinearLayout llFocus05;
	private LinearLayout llFocus06;
	private LinearLayout llFocus07;
	private LinearLayout llFocus08;
	private LinearLayout llFocus09;
	// private LinearLayout llFocus10;
	/**
	 * 图片下载器
	 */
	protected ImageLoader imageLoader = null;
	private DisplayImageOptions optionsPoster, optionsPoster2;

	public OnLivesTipsListItemPageView(Context context, int everyPageSize, int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
		// TODO Auto-generated constructor stub
	}

	public OnLivesTipsListItemPageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public OnLivesTipsListItemPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initView() {
		imageLoader = ImageLoader.getInstance();
		// 有横竖两种图片下载配置
		optionsPoster = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.live_channel_logo_default)
				.showImageOnFail(R.drawable.live_channel_logo_default).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
		optionsPoster2 = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.iv_focus_default)
				.showImageOnFail(R.drawable.iv_focus_default).resetViewBeforeLoading(false).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
		// 添加布局文件到当前
		initViewContainer(R.layout.view_onlive_tips_item);
		// 需要获取焦点的布局框
		flFirstLine01 = (FrameLayout) findItemView(R.id.fl_first_line_01);
		flFirstLine02 = (FrameLayout) findItemView(R.id.fl_first_line_02);
		flFirstLine03 = (FrameLayout) findItemView(R.id.fl_first_line_03);
		flFirstLine04 = (FrameLayout) findItemView(R.id.fl_first_line_04);
		// flFirstLine05 = (FrameLayout) findItemView(R.id.fl_first_line_05);

		flSecondLine01 = (FrameLayout) findItemView(R.id.fl_second_line_01);
		flSecondLine02 = (FrameLayout) findItemView(R.id.fl_second_line_02);
		flSecondLine03 = (FrameLayout) findItemView(R.id.fl_second_line_03);
		flSecondLine04 = (FrameLayout) findItemView(R.id.fl_second_line_04);
		// flSecondLine05 = (FrameLayout) findItemView(R.id.fl_second_line_05);
		// 焦点图
		ivFocus01 = (ImageView) findViewById(R.id.iv_focus_point_01);
		ivFocus02 = (ImageView) findViewById(R.id.iv_focus_point_02);
		ivFocus03 = (ImageView) findViewById(R.id.iv_focus_point_03);
		ivFocus04 = (ImageView) findViewById(R.id.iv_focus_point_04);
		// ivFocus05 = (ImageView) findViewById(R.id.iv_focus_point_05);
		ivFocus06 = (ImageView) findViewById(R.id.iv_focus_point_06);
		ivFocus07 = (ImageView) findViewById(R.id.iv_focus_point_07);
		ivFocus08 = (ImageView) findViewById(R.id.iv_focus_point_08);
		ivFocus09 = (ImageView) findViewById(R.id.iv_focus_point_09);
		// ivFocus10 = (ImageView) findViewById(R.id.iv_focus_point_10);
		// 标题
		tvFocus01 = (TextView) findViewById(R.id.tv_focus_01);
		tvFocus02 = (TextView) findViewById(R.id.tv_focus_02);
		tvFocus03 = (TextView) findViewById(R.id.tv_focus_03);
		tvFocus04 = (TextView) findViewById(R.id.tv_focus_04);
		// tvFocus05 = (TextView) findViewById(R.id.tv_focus_05);
		tvFocus06 = (TextView) findViewById(R.id.tv_focus_06);
		tvFocus07 = (TextView) findViewById(R.id.tv_focus_07);
		tvFocus08 = (TextView) findViewById(R.id.tv_focus_08);
		tvFocus09 = (TextView) findViewById(R.id.tv_focus_09);
		// tvFocus10 = (TextView) findViewById(R.id.tv_focus_10);
		// 添加焦点监听。在获取焦点的时候。焦点图改变背景
		flFirstLine01.setOnFocusChangeListener(new ImageChange(ivFocus01, tvFocus01, 0, flFirstLine01));
		flFirstLine02.setOnFocusChangeListener(new ImageChange(ivFocus02, tvFocus02, 1, flFirstLine02));
		flFirstLine03.setOnFocusChangeListener(new ImageChange(ivFocus03, tvFocus03, 2, flFirstLine03));
		flFirstLine04.setOnFocusChangeListener(new ImageChange(ivFocus04, tvFocus04, 3, flFirstLine04));
		// flFirstLine05.setOnFocusChangeListener(new ImageChange(ivFocus05,
		// tvFocus05, 4));
		flSecondLine01.setOnFocusChangeListener(new ImageChange(ivFocus06, tvFocus06, 5, flSecondLine01));
		flSecondLine02.setOnFocusChangeListener(new ImageChange(ivFocus07, tvFocus07, 6, flSecondLine02));
		flSecondLine03.setOnFocusChangeListener(new ImageChange(ivFocus08, tvFocus08, 7, flSecondLine03));
		flSecondLine04.setOnFocusChangeListener(new ImageChange(ivFocus09, tvFocus09, 8, flSecondLine04));
		// flSecondLine05.setOnFocusChangeListener(new ImageChange(ivFocus10,
		// tvFocus10, 9));
		// 焦点图的父控件
		llFocus01 = (LinearLayout) findViewById(R.id.ll_focus_01);
		llFocus02 = (LinearLayout) findViewById(R.id.ll_focus_02);
		llFocus03 = (LinearLayout) findViewById(R.id.ll_focus_03);
		llFocus04 = (LinearLayout) findViewById(R.id.ll_focus_04);
		// llFocus05 = (LinearLayout) findViewById(R.id.ll_focus_05);
		llFocus06 = (LinearLayout) findViewById(R.id.ll_focus_06);
		llFocus07 = (LinearLayout) findViewById(R.id.ll_focus_07);
		llFocus08 = (LinearLayout) findViewById(R.id.ll_focus_08);
		llFocus09 = (LinearLayout) findViewById(R.id.ll_focus_09);
		// llFocus10 = (LinearLayout) findViewById(R.id.ll_focus_10);

	}

	/**
	 * 焦点框的焦点监听事件
	 * 
	 * @ClassName: ImageChange
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月4日 下午3:25:44
	 * 
	 */
	class ImageChange implements OnFocusChangeListener {

		ImageView image;
		TextView tv;
		MarqueeText tv1;
		int viewIndex;
		FrameLayout frameLayout;

		public ImageChange(ImageView image, TextView tv, int viewIndex, FrameLayout frameLayout) {
			this.image = image;
			this.tv = tv;
			this.viewIndex = viewIndex;
			this.frameLayout = frameLayout;
		}

		public void onFocusChange(View v, boolean hasFocus) {
			OnliveTipsEntity entity = (OnliveTipsEntity) v.getTag();
			tv1 = ((MarqueeText) v.findViewById(R.id.tv_television_name));
			
			if (null != entity) {
				if (hasFocus) {// 获取焦点的时候改变背景
					if (null != FocusEntity) {
						FocusEntity.getFocusEntity(entity, viewIndex);
					}
					tv1.setStart(hasFocus);
					tv1.setText(replaceStrings(entity.getTelevisionName()));
					image.setBackgroundResource(R.drawable.epg_item_focus);
				} else {// 失去焦点的时候更换背景
					image.setBackgroundResource(R.drawable.epg_item_nomarl);
					tv1.setStart(hasFocus);
					tv1.setText(replaceStrings(entity.getTelevisionName()));
				}
			}
		}
	}

	/*
	 * 填充对应的item的文字信息 (non-Javadoc)
	 * 
	 * @see
	 * com.hiveview.box.framework.view.HiveBaseView#displayTextDataToItemView
	 * (com.hiveview.box.framework.entity.HiveBaseEntity, int)
	 */
	@Override
	public void displayTextDataToItemView(HiveBaseEntity BaseEntity, int index) {
		OnliveTipsEntity entity = (OnliveTipsEntity) BaseEntity;
		if (null != entity) {
			if (index == 0) {
				setItemViewTag(entity, 0, flFirstLine01);
				setTextData(flFirstLine01, entity);
				tvFocus01.setText(setDate(entity.getDate(), entity.getStart_time()));
				llFocus01.setVisibility(View.VISIBLE);
			} else if (index == 2) {
				setItemViewTag(entity, 1, flFirstLine02);
				setTextData(flFirstLine02, entity);
				tvFocus02.setText(setDate(entity.getDate(), entity.getStart_time()));
				llFocus02.setVisibility(View.VISIBLE);
			} else if (index == 4) {
				setItemViewTag(entity, 2, flFirstLine03);
				setTextData(flFirstLine03, entity);
				tvFocus03.setText(setDate(entity.getDate(), entity.getStart_time()));
				llFocus03.setVisibility(View.VISIBLE);
			} else if (index == 6) {
				setItemViewTag(entity, 3, flFirstLine04);
				setTextData(flFirstLine04, entity);
				tvFocus04.setText(setDate(entity.getDate(), entity.getStart_time()));
				llFocus04.setVisibility(View.VISIBLE);
			} else if (index == 1) {
				setItemViewTag(entity, 4, flSecondLine01);
				setTextData(flSecondLine01, entity);
				tvFocus06.setText(setDate(entity.getDate(), entity.getStart_time()));
				llFocus06.setVisibility(View.VISIBLE);
			} else if (index == 3) {
				setItemViewTag(entity, 5, flSecondLine02);
				setTextData(flSecondLine02, entity);
				tvFocus07.setText(setDate(entity.getDate(), entity.getStart_time()));
				llFocus07.setVisibility(View.VISIBLE);
			} else if (index == 5) {
				setItemViewTag(entity, 6, flSecondLine03);
				setTextData(flSecondLine03, entity);
				tvFocus08.setText(setDate(entity.getDate(), entity.getStart_time()));
				llFocus08.setVisibility(View.VISIBLE);
			} else if (index == 7) {
				setItemViewTag(entity, 7, flSecondLine04);
				setTextData(flSecondLine04, entity);
				tvFocus09.setText(setDate(entity.getDate(), entity.getStart_time()));
				llFocus09.setVisibility(View.VISIBLE);
			}
		}

	}

	/**
	 * 替换中文字符转成英文字符
	 * 
	 * @Title: TvChannelPageViewItemView
	 * @author:周一川
	 * @param input
	 *            准备替换的字符串
	 * @return 替换好的字符串
	 */
	public static String replaceStrings(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	// 显示文字信息
	private void setTextData(FrameLayout frameLayout, OnliveTipsEntity onliveTipsEntity) {
		if (null != onliveTipsEntity) {
			frameLayout.setVisibility(View.VISIBLE);
			((TextView) frameLayout.findViewById(R.id.tv_television_name)).setText(onliveTipsEntity.getTelevisionName());
			Log.i(TAG, "..........................onliveTipsEntity.getName()：" + onliveTipsEntity.getName());
			if (null != onliveTipsEntity && !TextUtils.isEmpty(onliveTipsEntity.getName())) {
				((TextView) frameLayout.findViewById(R.id.tv_program_name)).setText(replaceStrings(onliveTipsEntity.getName()));
			}
		}
	}

	/*
	 * 填充对应item的图片信息 (non-Javadoc)
	 * 
	 * @see
	 * com.hiveview.box.framework.view.HiveBaseView#displayImageToItemView(com
	 * .hiveview.box.framework.entity.HiveBaseEntity, int)
	 */
	@Override
	public void displayImageToItemView(HiveBaseEntity baseEntity, int index) {
		OnliveTipsEntity entity = (OnliveTipsEntity) baseEntity;
		if (null != entity) {
			if (index == 0) {
				displayImage(flFirstLine01, entity);
			} else if (index == 2) {
				displayImage(flFirstLine02, entity);
			} else if (index == 4) {
				displayImage(flFirstLine03, entity);
			} else if (index == 6) {
				displayImage(flFirstLine04, entity);
			} else if (index == 8) {
				// displayImage(flFirstLine05, entity);
			} else if (index == 1) {
				displayImage(flSecondLine01, entity);
			} else if (index == 3) {
				displayImage(flSecondLine02, entity);
			} else if (index == 5) {
				displayImage(flSecondLine03, entity);
			} else if (index == 7) {
				displayImage(flSecondLine04, entity);
			} else if (index == 9) {
				// displayImage(flSecondLine05, entity);
			}
		}
	}

	/**
	 * 显示图片
	 * 
	 * @param frameLayout
	 * @param onliveTipsEntity
	 */
	private void displayImage(FrameLayout frameLayout, OnliveTipsEntity onliveTipsEntity) {
		if (null != onliveTipsEntity) {
			frameLayout.setVisibility(View.VISIBLE);

			if (null == imageLoader) {
				imageLoader = ImageLoader.getInstance();
			}
			imageLoader.displayImage(onliveTipsEntity.getTelevisionLogoUrl().toString(),
					((ImageView) frameLayout.findViewById(R.id.iv_television_logo)), optionsPoster);
			imageLoader.displayImage(onliveTipsEntity.getWiki_cover().toString(), ((ImageView) frameLayout.findViewById(R.id.iv_thumb)),
					optionsPoster2);
		}
	}

	/*
	 * 添加点击时间 (non-Javadoc)
	 * 
	 * @see
	 * android.view.View#setOnClickListener(android.view.View.OnClickListener)
	 */
	@Override
	public void setOnClickListener(OnClickListener l) {
		flFirstLine01.setOnClickListener(l);
		flFirstLine02.setOnClickListener(l);
		flFirstLine03.setOnClickListener(l);
		flFirstLine04.setOnClickListener(l);
		// flFirstLine05.setOnClickListener(l);
		flSecondLine01.setOnClickListener(l);
		flSecondLine02.setOnClickListener(l);
		flSecondLine03.setOnClickListener(l);
		flSecondLine04.setOnClickListener(l);
		// flSecondLine05.setOnClickListener(l);
		super.setOnClickListener(l);
	}

	/*
	 * 添加按键响应事件 (non-Javadoc)
	 * 
	 * @see android.view.View#setOnKeyListener(android.view.View.OnKeyListener)
	 */
	@Override
	public void setOnKeyListener(OnKeyListener l) {
		flFirstLine01.setOnKeyListener(l);
		flFirstLine02.setOnKeyListener(l);
		flFirstLine03.setOnKeyListener(l);
		flFirstLine04.setOnKeyListener(l);
		// flFirstLine05.setOnKeyListener(l);
		flSecondLine01.setOnKeyListener(l);
		flSecondLine02.setOnKeyListener(l);
		flSecondLine03.setOnKeyListener(l);
		flSecondLine04.setOnKeyListener(l);
		// flSecondLine05.setOnKeyListener(l);
		super.setOnKeyListener(l);
	}

	/*
	 * 资源回收方法 (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.view.HiveBaseView#destoryFromViewPager()
	 */
	@Override
	public void destoryFromViewPager() {
		flFirstLine01.setVisibility(View.GONE);
		flFirstLine02.setVisibility(View.GONE);
		flFirstLine03.setVisibility(View.GONE);
		flFirstLine04.setVisibility(View.GONE);
		// flFirstLine05.setVisibility(View.GONE);
		flSecondLine01.setVisibility(View.GONE);
		flSecondLine02.setVisibility(View.GONE);
		flSecondLine03.setVisibility(View.GONE);
		flSecondLine04.setVisibility(View.GONE);
		// flSecondLine05.setVisibility(View.GONE);
		llFocus01.setVisibility(View.INVISIBLE);
		llFocus02.setVisibility(View.INVISIBLE);
		llFocus03.setVisibility(View.INVISIBLE);
		llFocus04.setVisibility(View.INVISIBLE);
		// llFocus05.setVisibility(View.INVISIBLE);
		llFocus06.setVisibility(View.INVISIBLE);
		llFocus07.setVisibility(View.INVISIBLE);
		llFocus08.setVisibility(View.INVISIBLE);
		llFocus09.setVisibility(View.INVISIBLE);
		// llFocus10.setVisibility(View.INVISIBLE);
		((ImageView) flFirstLine01.findViewById(R.id.iv_television_logo)).setBackgroundResource(0);
		((ImageView) flFirstLine01.findViewById(R.id.iv_thumb)).setBackgroundResource(0);
		((TextView) flFirstLine01.findViewById(R.id.tv_television_name)).setText("");
		((TextView) flFirstLine01.findViewById(R.id.tv_program_name)).setText("");
		((ImageView) flFirstLine02.findViewById(R.id.iv_television_logo)).setBackgroundResource(0);
		((ImageView) flFirstLine02.findViewById(R.id.iv_thumb)).setBackgroundResource(0);
		((TextView) flFirstLine02.findViewById(R.id.tv_television_name)).setText("");
		((TextView) flFirstLine02.findViewById(R.id.tv_program_name)).setText("");
		((ImageView) flFirstLine03.findViewById(R.id.iv_television_logo)).setBackgroundResource(0);
		((ImageView) flFirstLine03.findViewById(R.id.iv_thumb)).setBackgroundResource(0);
		((TextView) flFirstLine03.findViewById(R.id.tv_television_name)).setText("");
		((TextView) flFirstLine03.findViewById(R.id.tv_program_name)).setText("");
		((ImageView) flFirstLine04.findViewById(R.id.iv_television_logo)).setBackgroundResource(0);
		((ImageView) flFirstLine04.findViewById(R.id.iv_thumb)).setBackgroundResource(0);
		((TextView) flFirstLine04.findViewById(R.id.tv_television_name)).setText("");
		((TextView) flFirstLine04.findViewById(R.id.tv_program_name)).setText("");
		// ((ImageView)
		// flFirstLine05.findViewById(R.id.iv_television_logo)).setBackgroundResource(0);
		// ((ImageView)
		// flFirstLine05.findViewById(R.id.iv_thumb)).setBackgroundResource(0);
		// ((TextView)
		// flFirstLine05.findViewById(R.id.tv_television_name)).setText("");
		// ((TextView)
		// flFirstLine05.findViewById(R.id.tv_program_name)).setText("");
		((ImageView) flSecondLine01.findViewById(R.id.iv_television_logo)).setBackgroundResource(0);
		((ImageView) flSecondLine01.findViewById(R.id.iv_thumb)).setBackgroundResource(0);
		((TextView) flSecondLine01.findViewById(R.id.tv_television_name)).setText("");
		((TextView) flSecondLine01.findViewById(R.id.tv_program_name)).setText("");
		((ImageView) flSecondLine02.findViewById(R.id.iv_television_logo)).setBackgroundResource(0);
		((ImageView) flSecondLine02.findViewById(R.id.iv_thumb)).setBackgroundResource(0);
		((TextView) flSecondLine02.findViewById(R.id.tv_television_name)).setText("");
		((TextView) flSecondLine02.findViewById(R.id.tv_program_name)).setText("");
		((ImageView) flSecondLine03.findViewById(R.id.iv_television_logo)).setBackgroundResource(0);
		((ImageView) flSecondLine03.findViewById(R.id.iv_thumb)).setBackgroundResource(0);
		((TextView) flSecondLine03.findViewById(R.id.tv_television_name)).setText("");
		((TextView) flSecondLine03.findViewById(R.id.tv_program_name)).setText("");
		((ImageView) flSecondLine04.findViewById(R.id.iv_television_logo)).setBackgroundResource(0);
		((ImageView) flSecondLine04.findViewById(R.id.iv_thumb)).setBackgroundResource(0);
		((TextView) flSecondLine04.findViewById(R.id.tv_television_name)).setText("");
		// ((TextView)
		// flSecondLine04.findViewById(R.id.tv_program_name)).setText("");
		// ((ImageView)
		// flSecondLine05.findViewById(R.id.iv_television_logo)).setBackgroundResource(0);
		// ((ImageView)
		// flSecondLine05.findViewById(R.id.iv_thumb)).setBackgroundResource(0);
		// ((TextView)
		// flSecondLine05.findViewById(R.id.tv_television_name)).setText("");
		// ((TextView)
		// flSecondLine05.findViewById(R.id.tv_program_name)).setText("");
		tvFocus01.setText("");
		tvFocus09.setText("");
		tvFocus08.setText("");
		tvFocus07.setText("");
		tvFocus06.setText("");
		// tvFocus05.setText("");
		tvFocus04.setText("");
		tvFocus03.setText("");
		tvFocus02.setText("");
		tvFocus01.setText("");

	}

	/**
	 * date 的格式 2014-10-1
	 * 
	 * time 格式 01:00
	 * 
	 * @param date
	 * @param time
	 * @return
	 */
	protected String setDate(String date, String time) {
		StringBuffer sb = new StringBuffer();
		sb.append(date.split("-")[1] + "月");
		sb.append(date.split("-")[2] + "日");
		sb.append(" " + time);
		return sb.toString();
	}

	/**
	 * 焦点回调方法
	 * 
	 * @Fields FocusEntity
	 */
	public GetFocusEntity FocusEntity;

	public void setFocusEntity(GetFocusEntity FocusEntity) {
		this.FocusEntity = FocusEntity;
	}

	public GetFocusEntity getFocusEntity() {
		return FocusEntity;
	}

	public interface GetFocusEntity {
		public void getFocusEntity(OnliveTipsEntity mEntity, int entityIndex);
	}

}