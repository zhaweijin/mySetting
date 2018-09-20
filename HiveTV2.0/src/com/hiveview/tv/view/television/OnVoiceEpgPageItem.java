package com.hiveview.tv.view.television;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.OnlivePlayerActivity;
import com.hiveview.tv.activity.VoiceOnliveEPGActivity;
import com.hiveview.tv.fragment.LiveEpgInfoFragment;
import com.hiveview.tv.fragment.LiveEpgInfoFragment.EpgPageInterface;
import com.hiveview.tv.service.OnliveTipService;
import com.hiveview.tv.service.dao.OnliveTipsDAO;
import com.hiveview.tv.service.entity.OnliveTipsEntity;
import com.hiveview.tv.service.entity.ProgramEntity;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.DateUtils;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.ToastUtils;
import com.hiveview.tv.view.onlive.OnliveEpg;
import com.hiveview.tv.view.onlive.OnliveEpg.ONLiveEpgInterface;
import com.hiveview.tv.view.onlive.OnliveSelect;
import com.hiveview.tv.view.onlive.VoiceOnliveEpg;
import com.hiveview.tv.view.onlive.VoiceOnliveEpg.ONVoiceEpgInterface;
import com.hiveview.tv.view.television.EpgPageItem.EpgFoursInterface;
import com.hiveview.tv.view.television.EpgPageItem.MOnFocusChangeListener;
/**
 * 
 * @ClassName: OnVoiceEpgPageItem
 * @Description: 语音XX频道的节目单的item_view
 * @author: lihongji
 * @date 2014年12月4日 下午5:40:27
 *
 */
public class OnVoiceEpgPageItem extends HiveBaseView implements OnClickListener {

	/**
	 * Tag
	 */
	private String TAG = "OnLiveEpgPageItem";

	/**
	 * 外层容器
	 */
	private LinearLayout llItem01;
	private LinearLayout llItem02;
	private LinearLayout llItem03;
	private LinearLayout llItem04;
	private LinearLayout llItem05;
	private LinearLayout llItem06;
	private LinearLayout llItem07;
	/**
	 * 图片
	 */
	private ImageView imageView1;
	private ImageView imageView2;
	private ImageView imageView3;
	private ImageView imageView4;
	private ImageView imageView5;
	private ImageView imageView6;
	private ImageView imageView7;

	/**
	 * 数据库类
	 */
	private OnliveTipsDAO dao;

	private int index;
	/**
	 * 直播提醒的提前量
	 */
	private int seconds = 60 * 1000;
	/**
	 * 定时任务要触发的广播
	 */
	private PendingIntent pendingIntent;
	/**
	 * 定时任务的管理类
	 */
	private AlarmManager alarmManager;
	/**
	 * 电台channelCodes
	 */
	private String HANNEL_CODES;

	private boolean isActivity = true;

	public OnVoiceEpgPageItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public OnVoiceEpgPageItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public OnVoiceEpgPageItem(Context context, int everyPageSize, int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
		mContext = context;
	}

	@Override
	protected void initView() {
		initViewContainer(R.layout.view_voice_epg_item);
		mContext = getContext();
		llItem01 = (LinearLayout) this.findViewById(R.id.ll_item_01);
		llItem02 = (LinearLayout) this.findViewById(R.id.ll_item_02);
		llItem03 = (LinearLayout) this.findViewById(R.id.ll_item_03);
		llItem04 = (LinearLayout) this.findViewById(R.id.ll_item_04);
		llItem05 = (LinearLayout) this.findViewById(R.id.ll_item_05);
		llItem06 = (LinearLayout) this.findViewById(R.id.ll_item_06);
		llItem07 = (LinearLayout) this.findViewById(R.id.ll_item_07);

		imageView1 = (ImageView) findItemView(R.id.iv_focus_point1);
		imageView2 = (ImageView) findItemView(R.id.iv_focus_point2);
		imageView3 = (ImageView) findItemView(R.id.iv_focus_point3);
		imageView4 = (ImageView) findItemView(R.id.iv_focus_point4);
		imageView5 = (ImageView) findItemView(R.id.iv_focus_point5);
		imageView6 = (ImageView) findItemView(R.id.iv_focus_point6);
		imageView7 = (ImageView) findItemView(R.id.iv_focus_point7);

		VoiceOnliveEpg.getEpgPage(new ONVoiceEpgInterface() {

			@Override
			public void getEpgPage(ONVoiceFoursInterface foursInterface) {
				// TODO Auto-generated method stub
				epgFoursInterface = foursInterface;
			}
		});
		// 获取定时任务管理类
		if (null == alarmManager) {
			alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		}
		// 设置向上焦点
		setNextFocusID(R.id.tv_epg);
		imageView1.setOnFocusChangeListener(new MOnFocusChangeListener());
		imageView2.setOnFocusChangeListener(new MOnFocusChangeListener());
		imageView3.setOnFocusChangeListener(new MOnFocusChangeListener());
		imageView4.setOnFocusChangeListener(new MOnFocusChangeListener());
		imageView5.setOnFocusChangeListener(new MOnFocusChangeListener());
		imageView6.setOnFocusChangeListener(new MOnFocusChangeListener());
		imageView7.setOnFocusChangeListener(new MOnFocusChangeListener());
	}

	/**
	 * 让指定的view 获取焦点
	 * 
	 * @Title: OnLiveEpgPageItem
	 * @author:张鹏展
	 * @Description: TODO
	 * @param indexView
	 */
	public void getTheInTimeView(int indexView) {
		switch (indexView) {
		case 0:
			imageView1.requestFocus();
			break;
		case 1:
			imageView2.requestFocus();
			break;
		case 2:
			imageView3.requestFocus();
			break;
		case 3:
			imageView4.requestFocus();
			break;
		case 4:
			imageView5.requestFocus();
			break;
		case 5:
			imageView6.requestFocus();
			break;
		case 6:
			imageView7.requestFocus();
			break;
		default:
			break;
		}
	}

	@Override
	public void displayTextDataToItemView(HiveBaseEntity entity, int index) {
		ProgramEntity realEntity = (ProgramEntity) entity;
		switch (index) {
		case 0:
			((TextView) llItem01.findViewById(R.id.tv_epg_time)).setText(realEntity.getStart_time());
			((TextView) llItem01.findViewById(R.id.tv_epg_content)).setText(replaceStrings(realEntity.getName()));
			setItemViewTag(realEntity, index, imageView1);
			setEntity(llItem01, realEntity, HANNEL_CODES, mContext);
			imageView1.setOnClickListener(new MyOnClickListener(realEntity, llItem01));
			llItem01.setVisibility(View.VISIBLE);
			break;
		case 1:
			((TextView) llItem02.findViewById(R.id.tv_epg_time)).setText(realEntity.getStart_time());
			((TextView) llItem02.findViewById(R.id.tv_epg_content)).setText(replaceStrings(realEntity.getName()));
			setItemViewTag(realEntity, index, imageView2);
			setEntity(llItem02, realEntity, HANNEL_CODES, mContext);
			imageView2.setOnClickListener(new MyOnClickListener(realEntity, llItem02));
			llItem02.setVisibility(View.VISIBLE);
			break;
		case 2:
			((TextView) llItem03.findViewById(R.id.tv_epg_time)).setText(realEntity.getStart_time());
			((TextView) llItem03.findViewById(R.id.tv_epg_content)).setText(replaceStrings(realEntity.getName()));
			setItemViewTag(realEntity, index, imageView3);
			setEntity(llItem03, realEntity, HANNEL_CODES, mContext);
			imageView3.setOnClickListener(new MyOnClickListener(realEntity, llItem03));
			llItem03.setVisibility(View.VISIBLE);
			break;
		case 3:
			((TextView) llItem04.findViewById(R.id.tv_epg_time)).setText(realEntity.getStart_time());
			((TextView) llItem04.findViewById(R.id.tv_epg_content)).setText(replaceStrings(realEntity.getName()));
			setItemViewTag(realEntity, index, imageView4);
			setEntity(llItem04, realEntity, HANNEL_CODES, mContext);
			imageView4.setOnClickListener(new MyOnClickListener(realEntity, llItem04));
			llItem04.setVisibility(View.VISIBLE);
			break;
		case 4:
			((TextView) llItem05.findViewById(R.id.tv_epg_time)).setText(realEntity.getStart_time());
			((TextView) llItem05.findViewById(R.id.tv_epg_content)).setText(replaceStrings(realEntity.getName()));
			setItemViewTag(realEntity, index, imageView5);
			setEntity(llItem05, realEntity, HANNEL_CODES, mContext);
			imageView5.setOnClickListener(new MyOnClickListener(realEntity, llItem05));
			llItem05.setVisibility(View.VISIBLE);
			break;
		case 5:
			((TextView) llItem06.findViewById(R.id.tv_epg_time)).setText(realEntity.getStart_time());
			((TextView) llItem06.findViewById(R.id.tv_epg_content)).setText(replaceStrings(realEntity.getName()));
			setItemViewTag(realEntity, index, imageView6);
			setEntity(llItem06, realEntity, HANNEL_CODES, mContext);
			imageView6.setOnClickListener(new MyOnClickListener(realEntity, llItem06));
			llItem06.setVisibility(View.VISIBLE);
			break;
		case 6:
			((TextView) llItem07.findViewById(R.id.tv_epg_time)).setText(realEntity.getStart_time());
			((TextView) llItem07.findViewById(R.id.tv_epg_content)).setText(replaceStrings(realEntity.getName()));
			setItemViewTag(realEntity, index, imageView7);
			setEntity(llItem07, realEntity, HANNEL_CODES, mContext);
			imageView7.setOnClickListener(new MyOnClickListener(realEntity, llItem07));
			llItem07.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

	}

	@Override
	public void displayImageToItemView(HiveBaseEntity entity, int index) {

	}

	@Override
	public void destoryFromViewPager() {
		llItem01.setVisibility(View.GONE);
		llItem02.setVisibility(View.GONE);
		llItem03.setVisibility(View.GONE);
		llItem04.setVisibility(View.GONE);
		llItem05.setVisibility(View.GONE);
		llItem06.setVisibility(View.GONE);
		llItem07.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {

	}
	
	

	public interface ONVoiceFoursInterface {
		public void getEpgFours(ProgramEntity entity, View view,LinearLayout ll);
		// public void getEpgFours(String s);

	}
	private ONVoiceFoursInterface epgFoursInterface;
	class MOnFocusChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View view, boolean hasFocus) {
			ProgramEntity entity = (ProgramEntity) view.getTag();
			Log.d(TAG, "MOnFocusChangeListener::");
			mFocusListener.onCallBackFocusChange(view, hasFocus);
			if (hasFocus && isActivity) {
				if (view == llItem01.findViewById(R.id.iv_focus_point1)) {
					epgFoursInterface.getEpgFours(entity, imageView1,llItem01);
				} else if (view == llItem02.findViewById(R.id.iv_focus_point2)) {
					epgFoursInterface.getEpgFours(entity, imageView2,llItem02);
				} else if (view == llItem03.findViewById(R.id.iv_focus_point3)) {
					epgFoursInterface.getEpgFours(entity, imageView3,llItem03);
				} else if (view == llItem04.findViewById(R.id.iv_focus_point4)) {
					epgFoursInterface.getEpgFours(entity, imageView4,llItem04);
				} else if (view == llItem05.findViewById(R.id.iv_focus_point5)) {
					epgFoursInterface.getEpgFours(entity, imageView5,llItem05);
				} else if (view == llItem06.findViewById(R.id.iv_focus_point6)) {
					epgFoursInterface.getEpgFours(entity, imageView6,llItem06);
				} else if (view == llItem07.findViewById(R.id.iv_focus_point7)) {
					epgFoursInterface.getEpgFours(entity, imageView7,llItem07);
				}
			}
		}

	}
	/**
	 * 点击事件
	 * 
	 * @ClassName: MyOnClickListener
	 * @Description: TODO
	 * @author: 周一川
	 * @date 2014-7-24 下午3:27:21
	 * 
	 */
	private class MyOnClickListener implements OnClickListener {

		ProgramEntity entity;
		View view;

		public MyOnClickListener(ProgramEntity entity, LinearLayout view) {
			this.entity = entity;
			this.view = view;
		}

		@Override
		public void onClick(View v) {
			OnliveTipsEntity onliveTipsEntity = new OnliveTipsEntity();
			onliveTipsEntity.setDate(entity.getDate());
			onliveTipsEntity.setEnd_time(entity.getEnd_time());
			onliveTipsEntity.setHasvideo(entity.getHasvideo());
			onliveTipsEntity.setName(entity.getName());
			onliveTipsEntity.setSource(entity.getSource());
			onliveTipsEntity.setStart_time(entity.getStart_time());
			onliveTipsEntity.setTags(entity.getTags());
			onliveTipsEntity.setWiki_cover(entity.getWiki_cover());
			onliveTipsEntity.setWiki_id(entity.getWiki_id());
			onliveTipsEntity.setTelevisionLogoUrl(((VoiceOnliveEPGActivity) mContext).getCHANNEL_LOGO().toString());
			onliveTipsEntity.setTelevisionName(OnliveSelect.channelName);
			StringBuffer time = new StringBuffer();
			time.append(entity.getDate());
			time.append(" ");
			time.append(entity.getStart_time());
			try {
				onliveTipsEntity.setTip_time(String.valueOf(DateUtils.stringToLong(time.toString(), "yyyy-MM-dd HH:mm")));

				Log.d(TAG, "==直播提醒的台标==" + ((VoiceOnliveEPGActivity) mContext).getCHANNEL_LOGO().toString());
				Log.d(TAG, "==直播提醒的台ming==" + OnliveSelect.channelName);
				if (null == dao)
					dao = new OnliveTipsDAO(mContext);
				// 需要填充到直播提醒列表的 数据
				if (!AppUtil.nowDateTimeIsAferBoolean(entity.getDate() + " " + entity.getStart_time())) {
					if (dao.isExist(new String[] { OnliveSelect.channelName, entity.getDate(), entity.getStart_time() })) {
						dao.delete("television_logo_name = ? and date = ? and start_time = ?", new String[] {
								OnliveSelect.channelName , entity.getDate(), entity.getStart_time() });
						StringBuffer buffer = new StringBuffer();
						buffer.append(entity.getDate());
						buffer.append(" ");
						buffer.append(entity.getStart_time());

						long startTime;
						startTime = DateUtils.stringToLong(buffer.toString(), "yyyy-MM-dd HH:mm");
						cancelAlar(startTime);
						ToastUtils.alert(mContext,getResources().getString(R.string.alert_cancle_liveremind));
						((VoiceOnliveEPGActivity) mContext).setCurrentPager(index, view.findViewById(R.id.iv_focus_point));
						((ImageView) view.findViewById(R.id.iv_onlive_logo1)).setVisibility(View.GONE);
						((View) view.findViewById(R.id.iv_logo)).setVisibility(View.GONE);
						return;
					}

					((VoiceOnliveEPGActivity) mContext).setCurrentPager(index, view.findViewById(R.id.iv_focus_point));
					((ImageView) view.findViewById(R.id.iv_onlive_logo1)).setVisibility(View.VISIBLE);
					((View) view.findViewById(R.id.iv_logo)).setVisibility(View.VISIBLE);
					dao.insert(onliveTipsEntity);
					ToastUtils.alert(mContext,getResources().getString(R.string.alert_join_liveremind));

					Log.d(TAG, "定时发消息");
					// 制作定时任务
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("name", entity.getName());
					bundle.putString("wiki_cover", String.valueOf(entity.getWiki_cover()));
					bundle.putString("date", entity.getDate());
					bundle.putString("start_time", entity.getStart_time());
					bundle.putString("end_time", entity.getEnd_time());
					bundle.putString("wiki_id", entity.getWiki_id());
					bundle.putString("logo", ((VoiceOnliveEPGActivity) mContext).getCHANNEL_LOGO().toString());
					bundle.putString("logoName", OnliveSelect.channelName);
					intent.putExtras(bundle);
					intent.setClass(mContext, OnliveTipService.class);
					StringBuffer buffer = new StringBuffer();
					buffer.append(entity.getDate());
					buffer.append(" ");
					buffer.append(entity.getStart_time());
					// 获取定时任务管理类
					if (null == alarmManager) {
						alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
					}
					long startTime;
					startTime = DateUtils.stringToLong(buffer.toString(), "yyyy-MM-dd HH:mm");
					pendingIntent = PendingIntent.getService(mContext, (int) startTime, intent, 0);
					alarmManager.set(AlarmManager.RTC, startTime - seconds, pendingIntent);
					Log.d(TAG, "long time ==" + System.currentTimeMillis());
				} else {
					ToastUtils.alert(mContext, getResources().getString(R.string.alert_no_backsee));
				}
			} catch (Exception e) {
				LogUtil.info(this + "==" + e.getMessage());

				e.printStackTrace();
			}

		}
	};

	/**
	 * 取消当前的定时任务 参数是 日期 时间
	 * 
	 * @param date
	 * @param start_Time
	 */
	public void cancelAlar(long startTime) {
		// 获取定时任务管理类
		if (null == alarmManager)
			alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

		// 制作定时任务
		Intent intent = new Intent();
		intent.setClass(mContext, OnliveTipService.class);

		pendingIntent = PendingIntent.getService(mContext, (int) startTime, intent, 0);
		alarmManager.cancel(pendingIntent);

	}

	/**
	 * @param linearLayout
	 * @param entity
	 */
	private void setEntity(LinearLayout linearLayout, ProgramEntity entity, String CHANNEL_CODE, Context context) {
		linearLayout.setTag(entity);
		if (null == dao) {
			dao = new OnliveTipsDAO(context);
		}
		linearLayout.setTag(entity);
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar nowDate = Calendar.getInstance();
		nowDate.setTimeInMillis(System.currentTimeMillis());
		String nowTime = nowDate.get(Calendar.YEAR) + "-" + (nowDate.get(Calendar.MONTH) + 1) + "-" + nowDate.get(Calendar.DAY_OF_MONTH);// maliang
																																			// 2014-04-26:获取当日yyyy-MM-dd格式的日期与entity的日期比较，得知是否是当天
		boolean isNowDay = false;
		try {
			isNowDay = (sFormat.parse(nowTime).getTime() == sFormat.parse(entity.getDate()).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// linearLayout.findViewById(R.id.iv_focus_point).setOnClickListener(new
		// MyOnClickListener(entity, linearLayout));
		// ((TextView)
		// linearLayout.findViewById(R.id.tv_epg_time)).setText(entity.getStart_time());
		// ((TextView)
		// linearLayout.findViewById(R.id.tv_epg_content)).setText(entity.getName());
		Log.d(TAG,
				"==检测正在直播==" + entity.getStart_time() + "-----" + entity.getEnd_time() + "===isActivity=" + isActivity + "==isNowDay=" + isNowDay
						+ "===AppUtil.nowIsAferBoolean(entity.getStart_time())=="
						+ AppUtil.nowDateTimeIsAferBoolean(entity.getDate() + " " + entity.getStart_time())
						+ "===AppUtil.nowIsAferBoolean(entity.getEnd_time()=="
						+ AppUtil.nowDateTimeIsAferBoolean(entity.getDate() + " " + entity.getEnd_time()));

		ImageView logoImage = ((ImageView) linearLayout.findViewById(R.id.iv_onlive_logo));
		logoImage.setBackgroundResource(R.drawable.live_flag_image);

		if (AppUtil.nowDateTimeIsAferBoolean(entity.getDate() + " " + entity.getStart_time())
				&& !AppUtil.nowDateTimeIsAferBoolean(entity.getDate() + " " + entity.getEnd_time()) && isActivity && isNowDay) {
			Log.d(TAG, "==存在正在直播==");
			((VoiceOnliveEPGActivity) mContext).setCurrentPager(index, linearLayout.findViewById(R.id.iv_focus_point));
			((ImageView) linearLayout.findViewById(R.id.iv_onlive_logo)).setVisibility(View.VISIBLE);
			((View) linearLayout.findViewById(R.id.iv_logo)).setVisibility(View.VISIBLE);
			// photoAnimation.start();

		}
		Log.d(TAG, "===entity.getWiki_id()===" + entity.getWiki_id());
		// 存在直播提醒的数据库里边
		if (dao.isExist(new String[] {OnliveSelect.channelName, entity.getDate(), entity.getStart_time() })) {
			// if (dao.isExist(new String[] { CHANNEL_CODE, entity.getDate(),
			// entity.getStart_time() })) {
			// if (dao.isExists(new String[] { CHANNEL_CODE, entity.getDate()
			// })) {
			Log.d(TAG, "==存在正在直播,显示闹铃图片==");
			((VoiceOnliveEPGActivity) mContext).setCurrentPager(index, linearLayout.findViewById(R.id.iv_focus_point));
			((ImageView) linearLayout.findViewById(R.id.iv_onlive_logo1)).setVisibility(View.VISIBLE);
			((View) linearLayout.findViewById(R.id.iv_logo)).setVisibility(View.VISIBLE);
			// }
			// }
		}
	}

	public boolean isActivity() {
		return isActivity;
	}

	public void setActivity(boolean isActivity) {
		this.isActivity = isActivity;
	}

	/**
	 * 设置焦点
	 * 
	 * @Title: OnLiveEpgPageItem
	 * @author:郭松胜
	 * @Description: TODO
	 * @param viewID
	 */
	public void setNextFocusID(int viewID) {
		imageView1.setNextFocusUpId(viewID);
		imageView2.setNextFocusUpId(viewID);
		imageView3.setNextFocusUpId(viewID);
		imageView4.setNextFocusUpId(viewID);
		imageView5.setNextFocusUpId(viewID);
		imageView6.setNextFocusUpId(viewID);
		imageView7.setNextFocusUpId(viewID);

		imageView1.setNextFocusDownId(imageView1.getId());
		imageView2.setNextFocusDownId(imageView2.getId());
		imageView3.setNextFocusDownId(imageView3.getId());
		imageView4.setNextFocusDownId(imageView4.getId());
		imageView5.setNextFocusDownId(imageView5.getId());
		imageView6.setNextFocusDownId(imageView6.getId());
		imageView7.setNextFocusDownId(imageView7.getId());
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
}
