package com.hiveview.tv.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.entity.ChannelEntity;
import com.hiveview.tv.service.entity.LiveMediaEntity;
import com.hiveview.tv.utils.SwitchChannelUtils;
import com.hiveview.tv.view.television.MarqueeText;

public class TvChannelPageViewItemView extends FrameLayout {
	private static final String TAG = "TvChannelPageViewItemView";

	/**
	 * 显示电视节目的缩略图
	 */
	// private ImageView ivProgramThumb = null;

	/**
	 * 显示电视台logo
	 */
	private ImageView ivTelevisionLogo = null;

	/**
	 * 显示电视台名称
	 */
	private MarqueeText tvTelevisionName = null;

	/**
	 * 显示电视节目的播放进度
	 */
	// private SeekBar barProgramProgress = null;

	/**
	 * 当前电视节目开始时间
	 */
	private TextView tvStartTime = null;

	/**
	 * 当前电视节目结束时间
	 */
	// private TextView tvEndTime = null;

	/**
	 * 当前节目名称
	 */
	private MarqueeText tvCurProgramName = null;

	/**
	 * 下一个节目名称
	 */
	private MarqueeText tvNextProgramName = null;
	/**
	 * 当前节目在电视列表中的编号 add by guosongsheng
	 */
	private TextView tvProgramNumber;
	private DisplayImageOptions optionsPoster;
	private DisplayImageOptions optionsLogo;

	/**
	 * 图片下载器
	 */
	protected ImageLoader imageLoader = null;

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public TvChannelPageViewItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public TvChannelPageViewItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TvChannelPageViewItemView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	private void init(Context context) {
		imageLoader = ImageLoader.getInstance();

		optionsPoster = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.live_fast_select_tvchanel)
				.showImageOnFail(R.drawable.live_fast_select_tvchanel).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();

		optionsLogo = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.live_channel_logo_default)
				.showImageOnFail(R.drawable.live_channel_logo_default).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View vContainer = inflater.inflate(R.layout.tv_channel_pageview_item_layout_item, null);
		addView(vContainer);

		ivTelevisionLogo = (ImageView) findViewById(R.id.iv_television_logo);
		tvTelevisionName = (MarqueeText) findViewById(R.id.tv_television_name);
		tvStartTime = (TextView) findViewById(R.id.tv_start_time);
		tvNextProgramName = (MarqueeText) findViewById(R.id.tv_program_thumb);
		tvCurProgramName = (MarqueeText) findViewById(R.id.tv_program);
		tvProgramNumber = (TextView) findViewById(R.id.iv_program_number);
		// ==================
		// ivProgramThumb = (ImageView) findViewById(R.id.iv_program_thumb);
		// barProgramProgress = (SeekBar) findViewById(R.id.sb_television_item);
		// tvEndTime = (TextView) findViewById(R.id.tv_end_time);
		this.setOnClickListener(mOnChannelClickListener);

	}

	/**
	 * 设置当前页面的电视频道的文本数据显示
	 * 
	 * @param entity
	 */
	public void setTextData(ChannelEntity entity) {
		setVisibility(View.VISIBLE);
		String names = replaceStrings(entity.getName());
		tvTelevisionName.setText(names);
//		int position = entity.getPositionInItemView() + 1;
//		tvProgramNumber.setText(position + "");

		JSONObject json = entity.getProgram();
		if (null != json) {
			tvStartTime.setText(json.optString("start_time") + " - " + json.optString("end_time"));
			// tvEndTime.setText(json.optString("end_time"));
			tvCurProgramName.setText(replaceStrings(json.optString("name")));
			// tvNextProgramName.setText(json.optString("end_time") + " " +
			// json.optString("next_name"));
			// DateUtils.setProgramPlayProgress(barProgramProgress,
			// json.optString("start_time"), json.optString("end_time"));
		}
	}

	/**
	 * 替换中文字符转成英文字符
	 * 
	 * @Title: TvChannelPageViewItemView
	 * @author:周一川
	 * @Description: TODO
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

	/**
	 * 设置当前节目和下一个节目的名称是否有跑马灯的滚动效果
	 * 
	 * @param isMove
	 */
	public void setTextMarquee(boolean isMove) {
		/* start by guosongsheng 添加电台名称跑马灯 */
		tvTelevisionName.setStart(isMove);
		tvTelevisionName.setText(tvTelevisionName.getText().toString());
		/* end by guosongsheng */
		tvCurProgramName.setStart(isMove);
		tvCurProgramName.setText(tvCurProgramName.getText().toString());
		// tvNextProgramName.setStart(isMove);
		// tvNextProgramName.setText(tvNextProgramName.getText().toString());
	}

	/**
	 * 设置当前页面的电视频道的图片显示
	 * 
	 * @param entity
	 */
	public void setImageData(ChannelEntity entity) {
		imageLoader.displayImage(entity.getLogo(), ivTelevisionLogo, optionsLogo);
		JSONObject json = entity.getProgram();
		if (null != json) {
			// imageLoader.displayImage(json.optString("wiki_cover"),
			// ivProgramThumb, optionsPoster);
		}
	}
	
	 /**
	 * @Title: TvChannelPageViewItemView
	 * @author:郭松胜
	 * @Description:设置图片前景图
	 */
	public void setImageRes(){
		 if(null != ivTelevisionLogo){
			 ivTelevisionLogo.setImageResource(R.drawable.live_channel_logo_default);
		 }
	 }

	/***
	 * maliang :设置分类选台当前页面电视频道的文本数据显示 2014-04-22
	 * 
	 * @param entity
	 */
	public void setSelectTVData(LiveMediaEntity entity) {
		setVisibility(View.VISIBLE);
		tvTelevisionName.setText(entity.getChannel_name());
		JSONObject json = entity.getInfo();
		if (null != json) {
			String startTime = entity.getStart_time().trim();
			String endTime = entity.getEnd_time().trim();
			Date startDate = null;
			Date endDate = null;
			try {
				startDate = formatter.parse(startTime);
				endDate = formatter.parse(endTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, "分类选台频道开始时间和结束时间string转date错误!");
				e.printStackTrace();
			}
			Calendar calendar = Calendar.getInstance();
			if (startDate != null) {
				calendar.setTime(startDate);
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				startTime = "";
				if (hour < 10)
					startTime = "0";
				startTime += hour + ":";
				if (minute < 10)
					startTime += "0";
				startTime += minute;
			} else {
				startTime = "";
			}
			if (endDate != null) {
				calendar.setTime(endDate);
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				endTime = "";
				if (hour < 10)
					endTime = "0";
				endTime += hour + ":";
				if (minute < 10)
					endTime += "0";
				endTime += minute;
			} else {
				endTime = "";
			}
			tvStartTime.setText(startTime + " - " + endTime);
			// tvEndTime.setText(endTime);
			tvCurProgramName.setText(entity.getTitle());
			// tvNextProgramName.setText(endTime + " " + entity.getNext_name());
			// DateUtils.setProgramPlayProgress(barProgramProgress, startTime,
			// endTime);
		}
	}

	/***
	 * maliang:设置分类选台当前页面电视频道的图片数据显示 2014-04-22
	 * 
	 * @param entity
	 */
	public void setSelectTVImageData(LiveMediaEntity entity) {
		imageLoader.displayImage(entity.getChannel_logourl(), ivTelevisionLogo, optionsLogo);
		JSONObject json = entity.getPosters();
		if (null != json) {
			try {
				JSONArray jsonArray = json.getJSONArray("poster");// 接收的jsonobject是一个json数组
				if (jsonArray.length() > 0) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(0);
					// imageLoader.displayImage(jsonObject.optString("url"),
					// ivProgramThumb, optionsPoster);
				}
			} catch (JSONException e) {
				Log.i(TAG, "分类选台 图片数据 json数组解析错误!");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 点击事件 播放电视
	 */
	private OnClickListener mOnChannelClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// 电台名称
			String channelName = ((TextView) v.findViewById(R.id.tv_television_name)).getText().toString();
			Log.i(TAG, "mOnChannelClickListener::onClick-->channelName:" + channelName);
			SwitchChannelUtils.switchChannel(getContext(), channelName, false, AppScene.getScene());
		}
	};

}
