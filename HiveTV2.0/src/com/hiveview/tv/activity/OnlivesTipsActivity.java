package com.hiveview.tv.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.Strategy.OnliveTipLineStrategy;
import com.hiveview.tv.common.factory.OnLivesTipsFactory;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.service.OnliveTipService;
import com.hiveview.tv.service.dao.OnliveTipsDAO;
import com.hiveview.tv.service.entity.OnliveTipsEntity;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.utils.DateUtils;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.OnDialogClickListener;
import com.hiveview.tv.utils.QiYiPlayerUtil;
import com.hiveview.tv.view.HiveViewNetFaultDialog;
import com.hiveview.tv.view.OnLivesTipsListItemPageView.GetFocusEntity;
import com.hiveview.tv.view.ProgressDialog;
import com.paster.util.JsonUtil;
//直播提醒
public class OnlivesTipsActivity extends BaseActivity {
	/** 每页的数据 */
	protected int PAGE_COUNT = 8;
	/**
	 * 显示数据列表的ViewPager
	 */
	private HivePreloadViewPager vpOnliveTips = null;
	private ProgressDialog mProgressDialog;

	private TextView tvOnliveTipsCount;

	private ImageView ivPagerLeft;
	private ImageView ivPagerRight;
	private View ivCenterLine;
	private TextView tvPageCurrent;

	private HiveViewNetFaultDialog hiveViewDialog;

	private ImageView ivNoDataSource;

	private HivePagerAdapter mAdapter = null;

	private LinearLayout llSelectPage;

	private View btnPageUp = null;
	private View btnPageDown = null;

	private int pageCount = 0;
	private int pagsIndex = 0;
	private int viewItemIndex = 0;
	/**
	 * 上下页按钮
	 */
	private boolean isChangePageByUpButton = false;
	private boolean isChangePageByDownButton = false;

	/**
	 * 数据访问成功
	 * 
	 * @Fields ON_LIVES_TIPS_SUCCESS:TODO
	 */
	private static final int ON_LIVES_TIPS_SUCCESS = 0x00150;
	/**
	 * 数据访问失败
	 * 
	 * @Fields ON_LIVES_TIP_FAIL:TODO
	 */
	private static final int ON_LIVES_TIP_FAIL = 0x00160;

	/**
	 * 删除数据
	 * 
	 * @Fields ON_LIVES_TIP_DELETE
	 */
	private static final int ON_LIVES_TIP_DELETE = 0x00170;

	/**
	 * 数据库访问方法
	 * 
	 * @Fields onliveTipsDAO:TODO
	 */
	private OnliveTipsDAO onliveTipsDAO;

	/**
	 * 挂起的intent
	 */
	private long startTime;
	/**
	 * 是否是删除操作
	 * 
	 * @Fields isDelete:TODO
	 */
	private boolean isDelete = false;
	/**
	 * 科大讯飞ok键的keyCode
	 */
	private static int KEYCODE = 66;
	OnliveTipsEntity currentEntity;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_onlive_tips);
		onliveTipsDAO = new OnliveTipsDAO(getBaseContext());
		init();
		getOnliveTips();
	}

	private void init() {
		// 直播提醒的viepager
		vpOnliveTips = (HivePreloadViewPager) this.findViewById(R.id.vp_onlive_tips);
		vpOnliveTips.setVisibility(View.INVISIBLE);
		mProgressDialog = (ProgressDialog) this.findViewById(R.id.iv_hiveview_loading);
		// 中间划线
		ivCenterLine = (View) this.findViewById(R.id.iv_center_line);
		ivCenterLine.setVisibility(View.INVISIBLE);
		// 左右指示器图片
		ivPagerLeft = (ImageView) this.findViewById(R.id.iv_pager_left);
		ivPagerRight = (ImageView) this.findViewById(R.id.iv_pager_right);
		ivPagerLeft.setVisibility(View.INVISIBLE);
		ivPagerRight.setVisibility(View.INVISIBLE);
		// 没有数据的背景图
		ivNoDataSource = (ImageView) this.findViewById(R.id.iv_no_data);
		ivNoDataSource.setVisibility(View.INVISIBLE);
		// 各种文字
		tvPageCurrent = (TextView) this.findViewById(R.id.tv_page_current);
		tvOnliveTipsCount = (TextView) this.findViewById(R.id.tv_tips_count);
		llSelectPage = (LinearLayout) this.findViewById(R.id.ll_select_page);
		tvPageCurrent.setVisibility(View.INVISIBLE);
		tvOnliveTipsCount.setVisibility(View.INVISIBLE);
		llSelectPage.setVisibility(View.INVISIBLE);
		// 上下页按钮
		btnPageUp = findViewById(R.id.onlive_tips_btn_up);
		btnPageDown = findViewById(R.id.onlive_tips_btn_down);
		btnPageUp.setVisibility(View.INVISIBLE);
		btnPageDown.setVisibility(View.INVISIBLE);

		// 上下页焦点设置
		btnPageUp.setNextFocusRightId(R.id.onlive_tips_btn_down);
		btnPageDown.setNextFocusLeftId(R.id.onlive_tips_btn_up);
		// 上下页的按键监听
		btnPageUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				isChangePageByUpButton = true;
				vpOnliveTips.setCurrentItem(vpOnliveTips.getCurrentItem() - 1);
			}
		});

		btnPageDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				isChangePageByDownButton = true;
				vpOnliveTips.setCurrentItem(vpOnliveTips.getCurrentItem() + 1);
			}
		});

		hiveViewDialog = new HiveViewNetFaultDialog(this, onDialogClickListener);
		hiveViewDialog.setTitleContent(this.getResources().getString(R.string.cancel_onlive_tips));
	}
	
	private OnDialogClickListener onDialogClickListener = new OnDialogClickListener() {

		// 确定删除直播提醒
		public void onConfirm() {

			if (null != currentEntity) {
				Log.d("OnLivesTipsActivtiy->onConfirm->time->start", System.currentTimeMillis() + "");
				// 从列表中删除 所选项目
				dataSources.remove(currentEntity);
				// 从数据库中删除 所选项目
				onliveTipsDAO.delete("television_logo_name = ? and date = ? and start_time = ?", new String[] {
						currentEntity.getTelevisionName(), currentEntity.getDate(), currentEntity.getStart_time() });
				// 删除对应的定时任务
				StringBuffer buffer = new StringBuffer();
				buffer.append(currentEntity.getDate());
				buffer.append(" ");
				buffer.append(currentEntity.getStart_time());
				try {
					startTime = DateUtils.stringToLong(buffer.toString(), "yyyy-MM-dd HH:mm");
					Log.d("OnLivesTipsActivity===>onConfirm===>startTime::", startTime + "");
					cancelAlar(startTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 证明是删除操作 不是初始化操作
				isDelete = true;

				if (dataSources != null && !dataSources.isEmpty()) {

					handler.sendEmptyMessage(ON_LIVES_TIP_DELETE);
				} else {
					handler.sendEmptyMessage(ON_LIVES_TIP_FAIL);

				}
				Log.d("OnLivesTipsActivtiy->onConfirm->time->end", System.currentTimeMillis() + "");
			}
			hiveViewDialog.dismiss();
		}

		@Override
		public void onCancel() {
			hiveViewDialog.dismiss();
		}
	};

	ArrayList<OnliveTipsEntity> dataSources;

	private void getOnliveTips() {
		HttpTaskManager.getInstance().submit(new Runnable() {

			public void run() {
				dataSources = (ArrayList<OnliveTipsEntity>) onliveTipsDAO.query(null, null, null, "tip_time");
				if (dataSources != null && !dataSources.isEmpty()) {

					handler.sendEmptyMessage(ON_LIVES_TIPS_SUCCESS);
				} else {
					handler.sendEmptyMessage(ON_LIVES_TIP_FAIL);

				}

			}
		});

	}

	/*
	 * 接收消息 执行对应的状态 (non-Javadoc)
	 * 
	 * @see com.hiveview.tv.activity.BaseActivity#processData(int)
	 */
	@Override
	protected void processData(int msgWhat) {
		// TODO Auto-generated method stub
		super.processData(msgWhat);
		switch (msgWhat) {
		case ON_LIVES_TIPS_SUCCESS:
			tvPageCurrent.setVisibility(View.VISIBLE);
			tvOnliveTipsCount.setVisibility(View.VISIBLE);
			llSelectPage.setVisibility(View.VISIBLE);
			ivNoDataSource.setVisibility(View.VISIBLE);
			ivCenterLine.setVisibility(View.VISIBLE);
			vpOnliveTips.setVisibility(View.VISIBLE);
			ivNoDataSource.setVisibility(View.INVISIBLE);

			if (null == mAdapter) {
				mAdapter = new HivePagerAdapter(this, new OnLivesTipsFactory(new ViewItemFocusListener(), new ViewItemKeyListener(),
						new ViewItemClickListener(), new ViewItemGetFocus()), vpOnliveTips, PAGE_COUNT, new OnliveTipLineStrategy());
			}
			mAdapter.setDataTotalSize(dataSources.size());// 设置数据总记录数
			vpOnliveTips.setAdapter(mAdapter);
			mAdapter.addDataSource(dataSources);
			// 设置总页数
			pageCount = (int) Math.ceil(dataSources.size() / (double) PAGE_COUNT);

			StringBuffer sb = new StringBuffer();
			sb.append(String.format(OnlivesTipsActivity.this.getResources().getString(R.string.onlive_tips_pager_current), String.valueOf(1)));
			sb.append(OnlivesTipsActivity.this.getResources().getString(R.string.onlive_tips_pager_division));
			sb.append(String.format(OnlivesTipsActivity.this.getResources().getString(R.string.onlive_tips_pager_count), String.valueOf(pageCount)));
			tvPageCurrent.setText(sb.toString());
			tvOnliveTipsCount.setText(String.format(this.getResources().getString(R.string.onlive_tips_count), String.valueOf(dataSources.size())));

			ivPagerLeft.setVisibility(View.INVISIBLE);
			ivPagerRight.setVisibility(View.VISIBLE);

			if (dataSources.size() == 1 || dataSources.size() == 0) {// 只有一页的情况
				ivPagerLeft.setVisibility(View.INVISIBLE);
				ivPagerRight.setVisibility(View.INVISIBLE);
			}

			vpOnliveTips.setPreloadingListener(new OnPreloadingListener() {

				@Override
				public void preLoading(int pageSize) {
					// TODO Auto-generated method stub

				}

				@Override
				public void preLoadNotFinish() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onFirstPage() {

				}

				@Override
				public void onLastPage() {
					// TODO Auto-generated method stub

				}

				@Override
				public void setPageCurrent(int pageIndex) {
					pagsIndex = pageIndex - 1;
					if (pageIndex == 1) {// 第一页，左右指示按钮显示
						ivPagerLeft.setVisibility(View.INVISIBLE);
						ivPagerRight.setVisibility(View.VISIBLE);
						btnPageUp.setVisibility(View.INVISIBLE);
						btnPageDown.setVisibility(View.VISIBLE);
						if (isChangePageByUpButton) {
							btnPageDown.requestFocus();
							// 初始化焦点状态，保证用户在翻页的时候按键总是在同一按钮上
							isChangePageByUpButton = false;
						}
					} else if (pageIndex == pageCount) {// 最后一页，左右指示按钮显示
						ivPagerLeft.setVisibility(View.VISIBLE);
						ivPagerRight.setVisibility(View.INVISIBLE);
						btnPageUp.setVisibility(View.VISIBLE);
						btnPageDown.setVisibility(View.INVISIBLE);
						if (isChangePageByDownButton) {
							btnPageUp.requestFocus();
							// 初始化焦点状态，保证用户在翻页的时候按键总是在同一按钮上
							isChangePageByDownButton = false;
						} 
					} else {// 中间页面，左右指示按钮显示
						ivPagerLeft.setVisibility(View.VISIBLE);
						ivPagerRight.setVisibility(View.VISIBLE);
						btnPageUp.setVisibility(View.VISIBLE);
						btnPageDown.setVisibility(View.VISIBLE);
					}

					StringBuffer sb = new StringBuffer();
					sb.append(String.format(OnlivesTipsActivity.this.getResources().getString(R.string.onlive_tips_pager_current),
							String.valueOf(pageIndex)));
					sb.append(OnlivesTipsActivity.this.getResources().getString(R.string.onlive_tips_pager_division));
					sb.append(String.format(OnlivesTipsActivity.this.getResources().getString(R.string.onlive_tips_pager_count),
							String.valueOf(pageCount)));
					tvPageCurrent.setText(sb.toString());
					if (isChangePageByUpButton || isChangePageByDownButton) {
						if (isChangePageByUpButton) {
							btnPageUp.requestFocus();
							// 初始化焦点状态，保证用户在翻页的时候按键总是在同一按钮上
							isChangePageByUpButton = false;
						}

						if (isChangePageByDownButton) {
							btnPageDown.requestFocus();
							// 初始化焦点状态，保证用户在翻页的时候按键总是在同一按钮上
							isChangePageByDownButton = false;
						}
					}
				}

			});

			// 只有一页的情况
			if (pageCount == 1) {
				btnPageUp.setVisibility(View.INVISIBLE);
				btnPageDown.setVisibility(View.INVISIBLE);
				llSelectPage.setVisibility(View.INVISIBLE);

			} else if (pageCount > 1) {
				btnPageUp.setVisibility(View.INVISIBLE);
				btnPageDown.setVisibility(View.VISIBLE);
				llSelectPage.setVisibility(View.VISIBLE);
				ivPagerLeft.setVisibility(View.INVISIBLE);
				ivPagerRight.setVisibility(View.VISIBLE);
			}

			break;
		case ON_LIVES_TIP_FAIL:// 删除全部数据的时候执行的方法
			ivNoDataSource.setVisibility(View.VISIBLE);
			tvOnliveTipsCount.setVisibility(View.GONE);
			ivCenterLine.setVisibility(View.GONE);
			llSelectPage.setVisibility(View.GONE);
			ivPagerLeft.setVisibility(View.INVISIBLE);
			ivPagerRight.setVisibility(View.INVISIBLE);
			if (null != mAdapter)
				mAdapter.clear();
			vpOnliveTips.setVisibility(View.INVISIBLE);
			break;
		case ON_LIVES_TIP_DELETE:
			vpOnliveTips.setVisibility(View.VISIBLE);
			mAdapter.addDataSource(dataSources, pagsIndex, viewItemIndex);

			// 设置总页数
			pageCount = (int) Math.ceil(dataSources.size() / (double) PAGE_COUNT);
			// 设置相应的页码数据
			StringBuffer sb2 = new StringBuffer();
			sb2.append(String.format(OnlivesTipsActivity.this.getResources().getString(R.string.onlive_tips_pager_current),
					String.valueOf(pagsIndex + 1)));
			sb2.append(OnlivesTipsActivity.this.getResources().getString(R.string.onlive_tips_pager_division));
			sb2.append(String.format(OnlivesTipsActivity.this.getResources().getString(R.string.onlive_tips_pager_count), String.valueOf(pageCount)));
			tvPageCurrent.setText(sb2.toString());
			tvOnliveTipsCount.setText(String.format(this.getResources().getString(R.string.onlive_tips_count), String.valueOf(dataSources.size())));
			// 只有一页的情况
			if (pageCount == 1) {
				btnPageUp.setVisibility(View.INVISIBLE);
				btnPageDown.setVisibility(View.INVISIBLE);
				llSelectPage.setVisibility(View.INVISIBLE);

			}
			isDelete = false;
			break;
		}
	}

	/**
	 * 定时任务
	 */
	AlarmManager alarmManager;
	/**
	 * 一个挂起的intent
	 */
	PendingIntent pendingIntent;

	/**
	 * 取消当前的定时任务 参数是 日期 时间
	 * 
	 * @param date
	 * @param start_Time
	 */
	public void cancelAlar(long startTime) {
		// 获取定时任务管理类
		if (null == alarmManager)
			alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

		// 制作定时任务
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), OnliveTipService.class);

		pendingIntent = PendingIntent.getService(getApplicationContext(), (int) startTime, intent, 0);
		alarmManager.cancel(pendingIntent);

	}

	/**
	 * 监听ItemView上焦点事件，猜测用户操作，焦点落上时获取此ItemView关联的Entity,便于用户删除收藏
	 * 
	 * @author chenlixiao
	 * 
	 */
	class ViewItemFocusListener implements CallBackItemViewFocusListener {

		@Override
		public void onCallBackFocusChange(View view, boolean has) {

			if (has) {
			}
		}

	}

	/**
	 * 监听ItemView上的按MENU键，用户选择是否取消收藏
	 * 
	 * @author chenlixiao
	 * 
	 */
	class ViewItemKeyListener implements CallBackItemViewKeyListener {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 响应菜单按键
				Log.d("copy->", "ViewItemKeyListener");
				if (null != v.getTag()) {
					currentEntity = (OnliveTipsEntity) v.getTag();
					if (!hiveViewDialog.isShowing())
						hiveViewDialog.show();
				}
			}
			//处理科大讯飞ok键
			if (keyCode == KEYCODE && event.getAction() == KeyEvent.ACTION_DOWN) {
				if (null != v.getTag()) {
					currentEntity = (OnliveTipsEntity) v.getTag();
					if (!hiveViewDialog.isShowing())
						hiveViewDialog.show();
				}
			}
			return false;
		}
	}

	class ViewItemClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			Log.d("copy->", "ViewItemClickListener");
			// hiveViewDialog.show();
		}

	}

	class ViewItemGetFocus implements GetFocusEntity {

		@Override
		public void getFocusEntity(OnliveTipsEntity mEntity, int entityIndex) {
			// TODO Auto-generated method stub
			viewItemIndex = entityIndex;
			currentEntity = mEntity;

		}

	}
	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		super.onExecute(intent);
		if (intent.hasExtra("_scene")
				&& intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if ("cancle".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(OnlivesTipsActivity.this, "取消提醒",
							intent);
					if (!hiveViewDialog.isShowing())
						hiveViewDialog.show();
				}else if ("yes".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(OnlivesTipsActivity.this, "是",
							intent);
					if (null != currentEntity) {
						Log.d("OnLivesTipsActivtiy->onConfirm->time->start", System.currentTimeMillis() + "");
						// 从列表中删除 所选项目
						dataSources.remove(currentEntity);
						// 从数据库中删除 所选项目
						onliveTipsDAO.delete("television_logo_name = ? and date = ? and start_time = ?", new String[] {
								currentEntity.getTelevisionName(), currentEntity.getDate(), currentEntity.getStart_time() });
						// 删除对应的定时任务
						StringBuffer buffer = new StringBuffer();
						buffer.append(currentEntity.getDate());
						buffer.append(" ");
						buffer.append(currentEntity.getStart_time());
						try {
							startTime = DateUtils.stringToLong(buffer.toString(), "yyyy-MM-dd HH:mm");
							Log.d("OnLivesTipsActivity===>onConfirm===>startTime::", startTime + "");
							cancelAlar(startTime);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// 证明是删除操作 不是初始化操作
						isDelete = true;

						if (dataSources != null && !dataSources.isEmpty()) {

							handler.sendEmptyMessage(ON_LIVES_TIP_DELETE);
						} else {
							handler.sendEmptyMessage(ON_LIVES_TIP_FAIL);

						}
						Log.d("OnLivesTipsActivtiy->onConfirm->time->end", System.currentTimeMillis() + "");
					}
					hiveViewDialog.dismiss();
				}else if ("no".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(OnlivesTipsActivity.this, "否",
							intent);
					hiveViewDialog.dismiss();
				}
			}

		}
	}
	HashMap<String, String[]> commands = new HashMap<String, String[]>();
	// 场景id
	private String scenceId = "com.hiveview.tv.activity.OnlivesTipsActivity";

	public String onQuery() {
		// TODO 自动生成的方法存根
		commands.put("cancle", new String[] { "取消提醒" });
		commands.put("yes", new String[] { "是" });
		commands.put("no", new String[] { "否" });

		HashMap<String, String[]> fuzzayWords1 = new HashMap<String, String[]>();
		// fuzzayWords1.put("video", VIDEOS);
		JSONObject jsonObject = null;
		// 预定义槽二
		// String fuzzayVideoKey = "video";
		// String[] fuzzayVideoValue1 = new String[] { "收藏" };
		// String[] fuzzayVideoValue2 = new String[] { "取消收藏"};
		ArrayList<String[]> fuzzayValue = new ArrayList<String[]>();
		// fuzzayValue.add(fuzzayVideoValue1);
		// fuzzayValue.add(fuzzayVideoValue2);
		// 第三类
		HashMap<String, ArrayList<String[]>> fuzzayWords2 = new HashMap<String, ArrayList<String[]>>();
		fuzzayWords2.put("hello", fuzzayValue);
		try {
			jsonObject = JsonUtil.makeScenceJson(scenceId, commands,
					fuzzayWords1, fuzzayWords2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonObject.toString();

	}

}
