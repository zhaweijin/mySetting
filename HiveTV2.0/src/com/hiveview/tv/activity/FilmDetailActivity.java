package com.hiveview.tv.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.box.framework.strategy.NeighborOneLineStrategy;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.factory.FilmViewPagerItemFactory;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.ItemType;
import com.hiveview.tv.common.statistics.KeyEventHandler;
import com.hiveview.tv.common.statistics.SimpleOnClickListener;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.dao.CollectVoideoNewDAO;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.PlayerRecordEntity;
import com.hiveview.tv.service.entity.VideoNewEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.QIYIRecordUtils;
import com.hiveview.tv.utils.QiYiPlayerUtil;
import com.hiveview.tv.utils.StringUtils;
import com.hiveview.tv.view.ProgressDialog;
import com.paster.util.JsonUtil;

/**
 * @ClassName: FilmDetailActivity
 * @Description: 电影详情页
 * @author:
 * @date 2014年12月5日 下午2:46:09
 * 
 */
public class FilmDetailActivity extends BaseActivity {
	private FilmNewEntity mFilmEntity = null;
	private List<FilmNewEntity> flims;

	/**
	 * 电影详情数据请求成功
	 */
	private final int REQUEST_FILM_DETAIL_SUCCESS = 1;
	/**
	 * 电影详情数据请求失败
	 */
	private final int REQUEST_FILM_DETAIL_FAIL = -1;
	/**
	 * 请求爱奇艺相关推荐数据成功
	 */
	private final int REQUEST_QIYI_RELATIVE_SUCCESS = 2;

	/**
	 * 请求爱奇艺相关推荐数据失败
	 */
	private final int REQUEST_QIYI_RELATIVE_FAIL = -2;

	/**
	 * 查询 是否已经在收藏列表中
	 */
	private final int QUREY_SUCRESS = 0x00123;

	/**
	 * 播放按钮默认图片
	 * 
	 * @Fields PLAYER_DETAIL
	 */
	private final int PLAYER_DETAIL = 0x00245;
	private final int PLATER_QIYI_SUCCESS = 3;
	private TextView mTitle;
	private TextView mContent;
	/**
	 * 电影详情的播放按钮
	 */
	private ImageView ivPlay;
	/**
	 * 电影详情的收藏按钮
	 */
	private ImageView ivCollect;
	/**
	 * 
	 */
	private ImageView mCover;
	private TextView mDescribeLine1;
	private TextView mDescribeLine2;
	private ImageView ivPagerLeft;
	private ImageView ivPagerRight;
	private HiveTVService dataService = null;
	private int mVideoId = 1;
	private String name = "";
	private ProgressDialog dialogView = null;
	private String vrsAlbumId = "";
	private String vrsTvId = "";
	/**
	 * 相关推荐内容为空
	 */
	private TextView tvFilmRelativeNoData;
	/**
	 * 数据库文件
	 */
	CollectVoideoNewDAO dao;
	/**
	 * qiyi sdk
	 */
	// QiyiTVApi qiyiTVApi;
	/**
	 * 获取奇异播放记录的回调
	 */
	// QiYiApiResultHandler apiResultHandler;

	/**
	 * 改变播放按钮 改为需播
	 */
	private final int PLAYER_GO_ON = 0x00140;

	private TextView tvFilmSocre;
	/**
	 * 显示数据列表的ViewPager
	 */
	private HivePreloadViewPager viewPager = null;
	private HivePagerAdapter adapter = null;

	/**
	 * 标签
	 * 
	 * @Fields TAG:TODO
	 */
	private String TAG = "FilmDetailActivity";

	/**
	 * 查询播放记录时候的loading
	 * 
	 * @Fields playerProgress
	 */
	// private ProgressBar playerProgress;

	DisplayImageOptions options;
	protected int mVideoType = 1;
	private boolean isFirstCreat = true;
	/**
	 * 调用播放器来源id
	 */
	private String sourceID = AppConstant.SOURCE_LIST_PAGE;

	// 剧集列表
	private List<VideoNewEntity> newVideos = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		creatTime = System.currentTimeMillis();
		setContentView(R.layout.film_detail_layout);
		init();
		getContentResolver().registerContentObserver(QIYIRecordUtils.PLAYER_RECORD, true, cob);
	}

	private ContentObserver cob = new ContentObserver(new Handler()) {
		@Override
		public boolean deliverSelfNotifications() {
			return super.deliverSelfNotifications();
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Log.d(TAG, "---onChange");
			//监听播放记录数据库变化，改变播放状态，（解决当走到onresum,并没有走到保存数据库，拿不到数据库）
			if (!isFirstCreat) {
				submitRequest(new getQIYIPlayerRecords());
				isFirstCreat = true;
			}
			// testBtn.setText(DataUtils.getChangeName(getApplicationContext()));
		}
	};

	/**
	 * 初始化
	 */
	private void init() {
		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_film_detail_big)
				.showImageOnFail(R.drawable.default_film_detail_big).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();

		dao = new CollectVoideoNewDAO(getApplicationContext());
		// 初始化奇异的sdk
		// qiyiTVApi = QiyiTVApi.getInstance();
		// apiResultHandler = new QiYiApiResultHandler();
		initView();
		dataService = new HiveTVService();
		Serializable serializable = getIntent().getSerializableExtra("entity");
		sourceID = getIntent().getStringExtra("source");
		if (null == serializable) {
			mVideoId = getIntent().getIntExtra("id", 0);
			if (0 != mVideoId) {
				Log.d(TAG, "网络请求");
				requestFilmDetail();
			} else {
				LogUtil.info("no film id!!!!");
			}
		} else {

			mFilmEntity = (FilmNewEntity) serializable;
			video_Entity = mFilmEntity;
			mVideoType = mFilmEntity.getCid();
			mVideoId = mFilmEntity.getId();
			name = mFilmEntity.getName();
			/*
			 * if (0 == mVideoId) { requestFilmDetail(); } else{
			 * 
			 * 
			 * fillFilmInfo(); }
			 */
			fillFilmInfo();
		}
		// 确保推荐内容无信息 在在加载时显示
		tvFilmRelativeNoData.setVisibility(View.INVISIBLE);
		dialogView.setVisibility(View.VISIBLE);
		viewPager.setVisibility(View.GONE);

	}

	/**
	 * 处理网络数据返回逻辑
	 */
	@Override
	protected void processData(int msgWhat) {
		switch (msgWhat) {
		case REQUEST_FILM_DETAIL_SUCCESS:
			// playerProgress.setVisibility(View.GONE);
			fillFilmInfo();
			break;
		case REQUEST_FILM_DETAIL_FAIL:
			// showDialogAboutNetFault();
			// playerProgress.setVisibility(View.GONE);
			dialogView.setVisibility(View.INVISIBLE);
			break;
		case REQUEST_QIYI_RELATIVE_SUCCESS:// 请求奇艺的数据推荐数据成功
			// fillPagers();
			if (null == adapter) {
				adapter = new HivePagerAdapter(this, new FilmViewPagerItemFactory(new ViewItemFocusListener(), new ViewItemKeyListener(),
						new ViewItemClickListener()), viewPager, 6, new NeighborOneLineStrategy());
			}
			// 添加数据
			adapter.addDataSource(flims);
			int pageViewSize = 6;
			final int pageCount = (int) Math.ceil(flims.size() / (double) pageViewSize);
			viewPager.setAdapter(adapter);
			// playerProgress.setVisibility(View.GONE);
			ivPagerLeft.setVisibility(View.INVISIBLE);
			ivPagerRight.setVisibility(View.VISIBLE);
			viewPager.setPreloadingListener(new OnPreloadingListener() {

				public void setPageCurrent(int pageIndex) {
					// TODO Auto-generated method stub
					/*
					 * if (pageIndex == 0) {// 第一页，左右指示按钮显示
					 * ivPagerLeft.setVisibility(View.INVISIBLE);
					 * ivPagerRight.setVisibility(View.VISIBLE); } else
					 */if (pageIndex >= pageCount) {// 最后一页，左右指示按钮显示
						ivPagerLeft.setVisibility(View.VISIBLE);
						ivPagerRight.setVisibility(View.INVISIBLE);
					} else {
						// 中间页面，左右指示按钮显示
						ivPagerLeft.setVisibility(View.VISIBLE);
						ivPagerRight.setVisibility(View.VISIBLE);
					}
				}

				public void preLoading(int pageSize) {
					// TODO Auto-generated method stub

				}

				public void preLoadNotFinish() {
					// TODO Auto-generated method stub

				}

				public void onLastPage() {

				}

				public void onFirstPage() {
					ivPagerLeft.setVisibility(View.INVISIBLE);
					ivPagerRight.setVisibility(View.VISIBLE);

				}
			});
			dialogView.setVisibility(View.INVISIBLE);
			viewPager.setVisibility(View.VISIBLE);
			break;
		case REQUEST_QIYI_RELATIVE_FAIL:
			// 相关推荐加载失败时 显示没有相关信息提示
			// playerProgress.setVisibility(View.GONE);
			dialogView.setVisibility(View.INVISIBLE);
			ivPagerLeft.setVisibility(View.INVISIBLE);
			ivPagerRight.setVisibility(View.INVISIBLE);
			tvFilmRelativeNoData.setVisibility(View.VISIBLE);
			break;
		case QUREY_SUCRESS:
			// 如果存在收藏的列表中 就显示已收藏
			// playerProgress.setVisibility(View.GONE);
			ivCollect.setImageResource(R.drawable.film_detail_fav_has);
			ivCollect.setTag(true);
			break;
		case PLAYER_GO_ON:
			// 影院续播获取播放记录有问题，暂时注释取消续播状态--沈少飞--2015-7-1
			ivPlay.setImageResource(R.drawable.player_go);
			break;
		case PLAYER_DETAIL:
			ivPlay.setImageResource(R.drawable.film_detail_play);
			break;
		case PLATER_QIYI_SUCCESS:

			break;
		default:
			break;
		}
	}

	/**
	 * 设置焦点顺序
	 */
	private void setFocusSequence() {
		ivPlay.setNextFocusDownId(R.id.film_pagaer);
		ivPlay.setNextFocusLeftId(ivPlay.getId());
		ivCollect.setNextFocusDownId(R.id.film_pagaer);
		ivCollect.setNextFocusRightId(ivCollect.getId());
	}

	/**
	 * 初始化View
	 */
	private void initView() {

		mTitle = (TextView) this.findViewById(R.id.tv_title);
		mContent = (TextView) this.findViewById(R.id.tv_content);
		ivPlay = (ImageView) this.findViewById(R.id.iv_film_detail_play);
		mCover = (ImageView) this.findViewById(R.id.iv_film_detail_cover);
		mDescribeLine1 = (TextView) this.findViewById(R.id.tv_des_line1);
		mDescribeLine2 = (TextView) this.findViewById(R.id.tv_des_line2);
		ivPagerLeft = (ImageView) this.findViewById(R.id.iv_pager_left);
		ivPagerRight = (ImageView) this.findViewById(R.id.iv_pager_right);
		viewPager = (HivePreloadViewPager) findViewById(R.id.film_pagaer);
		// playerProgress = (ProgressBar) findViewById(R.id.progress_small);

		dialogView = (ProgressDialog) findViewById(R.id.iv_films_loading);
		ivCollect = (ImageView) findViewById(R.id.iv_film_detail_fav);
		ivCollect.setTag(false);
		tvFilmSocre = (TextView) findViewById(R.id.tv_film_socre);
		tvFilmRelativeNoData = (TextView) findViewById(R.id.film_relative_no_data);
		ivPlay.requestFocus();

		ivPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				video_Entity = mFilmEntity;
				Log.v(TAG, "JSONAnalyze1====" + com.alibaba.fastjson.JSONObject.toJSONString(video_Entity));

				Log.d(TAG, "---ivPlay::vrsAlbumId:" + vrsAlbumId + "--vrsTvId:" + vrsTvId);
				if (vrsAlbumId.length() > 0 && vrsTvId.length() > 0) {
					isOnClick = true;
					startPlayerTime = System.currentTimeMillis();
					long time = startPlayerTime - creatTime;
					sendStatistics(String.valueOf(time), mFilmEntity, DataType.CLICK_TAB_FILM);
					// QiYiPlayerUtil.startNewQiYiPlayer(FilmDetailActivity.this,
					// vrsTvId, vrsAlbumId);
					QiYiPlayerUtil.startSDKPlayer(FilmDetailActivity.this, com.alibaba.fastjson.JSONObject.toJSONString(video_Entity), null, false,
							true, false, null, false);
			//		handler.sendEmptyMessage(PLAYER_GO_ON);
					try {
						KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setTabNo(Tab.TAB).setViewPosition("0306").setSource(sourceID)
								.setEntity((FilmNewEntity) player_Entity).setDataType(DataType.CLICK_TAB_FILM)
								.setSrcType(sourceID.equals(AppConstant.SOURCE_SUBJECT) ? ItemType.SUBJECT : ItemType.VIDEO).build());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					showErrorDialog(HiveViewErrorCode.E0000601, false);
				}
			}
		});
		ivCollect.setOnClickListener(new RecommendRecordLayoutListener());
		setFocusSequence();
		/*
		 * ivCollect.setOnClickListener(new OnClickListener() {
		 * 
		 * videoset_id = ? and uid = ? public void onClick(View arg0) { if (null
		 * != mFilmEntity) { // 如果存在收藏的列表中 就显示已收藏
		 * 
		 * Log.i(TAG, "ivCollect.getTag()::" + ivCollect.getTag().toString());
		 * // 修改如果当前是收藏状态就修改成非收藏状态,同时删除数据库的数据 boolean isSaveed =
		 * Boolean.parseBoolean(ivCollect.getTag().toString()); if (isSaveed) {
		 * dao.delete("cp_videoset_id = ?", new String[] {
		 * String.valueOf(mFilmEntity.getCp_videoset_id()) });
		 * ivCollect.setImageResource(R.drawable.film_detail_fav);
		 * ivCollect.setTag(false); } else {
		 * ivCollect.setImageResource(R.drawable.film_detail_fav_has);
		 * dao.insert(mFilmEntity); ivCollect.setTag(true); } Log.d(TAG,
		 * "=============ivCollect.setOnClickListene======");
		 * 
		 * 
		 * ToastUtils.alert(FilmDetailActivity.this, getResources()
		 * .getString(R.string.collect_success_text));
		 * 
		 * } } });
		 */
	}

	/**
	 * 收藏的监听
	 * 
	 * @ClassName: RecommendRecordLayoutListener
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月30日 上午10:10:55
	 * 
	 */
	class RecommendRecordLayoutListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {
			if (null != mFilmEntity) {
				boolean isSaveed = Boolean.parseBoolean(ivCollect.getTag().toString());
				// 修改如果当前是收藏状态就修改成非收藏状态,同时删除数据库的数据
				if (isSaveed) {
					dao.delete("id = ?", new String[] { String.valueOf(mFilmEntity.getId()) });
					ivCollect.setImageResource(R.drawable.film_detail_fav);
					ivCollect.setTag(false);
					return null;
				} else {
					dao.insert(mFilmEntity);
					ivCollect.setImageResource(R.drawable.film_detail_fav_has);
					ivCollect.setTag(true);

				}

			}
			// start:修改为Tab.TAB，author:huzuwei
			return new DataHolder.Builder(getBaseContext()).setDataType(DataType.CLICK_TAB_FILM).setTabNo(Tab.TAB).setViewPosition("0101")
					.setEntity(mFilmEntity)
					// .setSrcType(ItemType.VIDEO)
					.build();
			// end
		}
	}

	/**
	 * 显示电影详情信息
	 */
	private void fillFilmInfo() {

		// submitRequest(new getQIYIPlayerRecords());
		try {
			// start:修改查询条件,判断cp_videoset_id是否为空，author:huzuwei
			// ArrayList<FilmEntity> filmEntities = new ArrayList<FilmEntity>();
			// if (null != mFilmEntity.getCp_videoset_id()
			// && !mFilmEntity.getCp_videoset_id().toString().equals("")) {
			// filmEntities = dao.query(
			// new String[] {
			// String.valueOf(mFilmEntity.getVideoset_id()),
			// String.valueOf(mFilmEntity.getUid()) },
			// "cp_videoset_id = ?", new String[] { String
			// .valueOf(mFilmEntity.getCp_videoset_id()) },
			// null);
			// } else {
			// filmEntities = dao.query(
			// new String[] {
			// String.valueOf(mFilmEntity.getVideoset_id()),
			// String.valueOf(mFilmEntity.getUid()) },
			// "videoset_id = ? and uid=?",
			// new String[] {
			// String.valueOf(mFilmEntity.getVideoset_id()),
			// String.valueOf(mFilmEntity.getUid()) }, null);
			// }
			// end
			// Log.d(TAG, "======filmEntities=" + filmEntities.size() + "...."
			// + filmEntities);
			ArrayList<FilmNewEntity> filmEntities = dao.query(null, "id = ?", new String[] { null == String.valueOf(mFilmEntity.getId()) ? ""
					: String.valueOf(mFilmEntity.getId()) }, null);

			Log.d(TAG, "======filmEntities=" + filmEntities.size());
			if (filmEntities.size() != 0) {
				handler.sendEmptyMessage(QUREY_SUCRESS);
			}
		} catch (Exception e) {
			showErrorDialog(HiveViewErrorCode.E0000601, false);
			e.printStackTrace();
		}

		StringBuilder describeLine1 = new StringBuilder();
		StringBuilder describeLine2 = new StringBuilder();
		player_Entity = mFilmEntity;
		// 显示电影的时长、上映时间、分类信息
		describeLine1.append(mFilmEntity.getEqLen() == 0 ? "" : getResources().getString(R.string.text_time_long))
				.append(mFilmEntity.getEqLen() == 0 ? "" : mFilmEntity.getEqLen() / 60)
				.append(mFilmEntity.getEqLen() == 0 ? "" : getResources().getString(R.string.text_minute))
				.append(TextUtils.isEmpty(mFilmEntity.getTime()) ? "" : getResources().getString(R.string.text_film_detail_separater))
				.append(TextUtils.isEmpty(mFilmEntity.getTime()) ? "" : getResources().getString(R.string.text_issue_time))
				.append(TextUtils.isEmpty(mFilmEntity.getTime()) ? "" : mFilmEntity.getTime()).append(/*
																									 * getResources
																									 * (
																									 * )
																									 * .
																									 * getString
																									 * (
																									 * R
																									 * .
																									 * string
																									 * .
																									 * text_year
																									 * )
																									 */"")
				.append(TextUtils.isEmpty(mFilmEntity.getTagNames()) ? "" : getResources().getString(R.string.text_film_detail_separater))
				.append(TextUtils.isEmpty(mFilmEntity.getTagNames()) ? "" : getResources().getString(R.string.text_category))
				.append(TextUtils.isEmpty(mFilmEntity.getTagNames()) ? "" : mFilmEntity.getTagNames());

		// 显示电影的导演，主演信息
		describeLine2.append(TextUtils.isEmpty(mFilmEntity.getDirectors()) ? "" : getResources().getString(R.string.text_director))
				.append(TextUtils.isEmpty(mFilmEntity.getDirectors()) ? "" : mFilmEntity.getDirectors())
				.append(TextUtils.isEmpty(mFilmEntity.getMainActors()) ? "" : getResources().getString(R.string.text_film_detail_separater))
				.append(TextUtils.isEmpty(mFilmEntity.getMainActors()) ? "" : getResources().getString(R.string.text_actor))
				.append(TextUtils.isEmpty(mFilmEntity.getMainActors()) ? "" : mFilmEntity.getMainActors());

		mDescribeLine1.setText(StringUtils.isEmpty(describeLine1.toString()) ? "" : describeLine1);// 时长，上映时间，分类信息
		mDescribeLine2.setText(StringUtils.isEmpty(describeLine2.toString()) ? "" : describeLine2);// 显示导演，主演信息

		mTitle.setText(StringUtils.isEmpty(mFilmEntity.getName()) ? "" : mFilmEntity.getName());// 电影名称
		mContent.setText(StringUtils.isEmpty(mFilmEntity.getDesc()) ? "" : mFilmEntity.getDesc());// 电影详情
		String socre = String.valueOf(mFilmEntity.getScore());
		if (null != socre) {
			tvFilmSocre.setText(socre + "分");// 评分
		}

		// 出现"null"这样的数据 需要过滤
		if (null != mFilmEntity.getPosterUrl() && mFilmEntity.getPosterUrl().length() > 0 && !"null".equals(mFilmEntity.getPosterUrl())) {
			String sizeNewUrl = StringUtils.getImage260_360Url(mFilmEntity.getPosterUrl());
			ImageLoader.getInstance().displayImage(sizeNewUrl, mCover, options);
		} else if (null != mFilmEntity.getPosterUrl() && mFilmEntity.getPosterUrl().length() > 0 && !"null".equals(mFilmEntity.getPosterUrl())) {

		} else if (null != mFilmEntity.getPosterUrl() && mFilmEntity.getPosterUrl().length() > 0 && !"null".equals(mFilmEntity.getPosterUrl())) {
			ImageLoader.getInstance().displayImage(mFilmEntity.getPosterUrl(), mCover, options);
		}

		// 只有电影详细页相关推荐不需要重新请求VideoList，其他页面进入详细页都需要请求剧集列表，得到vrsTvId
		// 和vrsAlbumId去启动奇艺播放器
		boolean isRequestVideoList = getIntent().getBooleanExtra("request", false);
		Log.d(TAG, "得到电影剧集" + isRequestVideoList);
		if (!isRequestVideoList) {
			Log.d(TAG, "得到电影剧集:");
			requestVideoList();
		} else {

			// String sizeNewUrl =
			// StringUtils.getImage260_360Url(mFilmEntity.getVideoset_img());
			ImageLoader.getInstance().displayImage(mFilmEntity.getPosterUrl(), mCover, options);
			// 相关推荐的直接得到vrsAlbumId和vrsTvId就可以直接播放
			// vrsAlbumId = mFilmEntity.getCp_videoset_id().split(",")[1];
			// vrsTvId = mFilmEntity.getCp_videoset_id().split(",")[0];
			Log.d(TAG, "-----mFilmEntity::" + mFilmEntity.toString());
			requestVideoList(mFilmEntity.getId());
		}

		// if (null != mFilmEntity.getCp_videoset_id()
		// && !mFilmEntity.getCp_videoset_id().equals("null")
		// && mFilmEntity.getCp_videoset_id().length() > 0) {

		// 第0元素：vrsTvId，第1个元素：vrsAlbumId
		// String tmpVrsAlbumId = mFilmEntity.getCp_videoset_id().split(",")[0];
		// String tmpVrsTvId =
		// mFilmEntity.getCp_videoset_id().split(",")[0];
		int chnnid = mFilmEntity.getCid();
		int id = mFilmEntity.getId();
		LogUtil.info("--chnnid:" + chnnid + "----id:" + id);
		if (chnnid != 0 && id != 0) {
			requestQiyiRecommand(chnnid, id);
		} else {// 如果数据为空就发送失败的消息，做出对应的处理
			handler.sendEmptyMessage(REQUEST_QIYI_RELATIVE_FAIL);
		}

	}

	/**
	 * 网络请求电影详情数据
	 */
	private void requestFilmDetail() {
		// // 请求电影详情数据
		submitRequest(new SafeRunnable() {
			@Override
			public void requestData() {// 奇异的接口啊

				List<FilmNewEntity> list = dataService.getFilmDetail(FilmDetailActivity.this, mVideoId);
				Log.d(TAG, "电影详情：" + list.size());
				if (null != list && list.size() > 0) {// 同样获取的数据不为零的话发送成功的消息
					mFilmEntity = list.get(0);
					mVideoType = mFilmEntity.getCid();
					name = mFilmEntity.getName();
					video_Entity = mFilmEntity;
					handler.sendEmptyMessage(REQUEST_FILM_DETAIL_SUCCESS);
				} else {// 数据量为零的时候就发送失败的消息
					handler.sendEmptyMessage(REQUEST_FILM_DETAIL_FAIL);
					// 显示错误提示框
					showErrorDialog(HiveViewErrorCode.E0000601, false);
				}
				/*
				 * if (mVideoId != 0) { List<FilmEntity> list2 =
				 * dataService.getFilmDetail( FilmDetailActivity.this,
				 * mVideoId); if (null != list2 && list2.size() > 0) {
				 * FilmEntity mFilmEntity2 = list2.get(0); List<FilmEntity> list
				 * = dataService.getFilmDetail( FilmDetailActivity.this,
				 * mFilmEntity2.getCp_videoset_id()); if (null != list &&
				 * list.size() > 0) { mFilmEntity = list.get(0);
				 * handler.sendEmptyMessage(REQUEST_FILM_DETAIL_SUCCESS); } else
				 * { handler.sendEmptyMessage(REQUEST_FILM_DETAIL_FAIL); } }
				 * else { handler.sendEmptyMessage(REQUEST_FILM_DETAIL_FAIL); }
				 * 
				 * } else { List<FilmEntity> list = dataService.getFilmDetail(
				 * FilmDetailActivity.this, mFilmEntity.getCp_videoset_id()); if
				 * (null != list && list.size() > 0) { mFilmEntity =
				 * list.get(0);
				 * handler.sendEmptyMessage(REQUEST_FILM_DETAIL_SUCCESS); } else
				 * { handler.sendEmptyMessage(REQUEST_FILM_DETAIL_FAIL); } }
				 */
			}

			@Override
			public void processServiceException(ServiceException e) {
				// 网络发生异常 了同样发送失败的消息
				Message msg = handler.obtainMessage();
				msg.what = REQUEST_FILM_DETAIL_FAIL;
				msg.obj = e.getErrorCode();
				handler.sendMessage(msg);
				// 显示错误提示框
				showErrorDialog(e.getErrorCode(), true);
			}
		});

	}

	/**
	 * 得到电影剧集
	 */
	private void requestVideoList() {
		submitRequest(new SafeRunnable() {
			@Override
			public void requestData() {
				// start author zhangpengzhan
				Log.d(TAG, "----requestVideoList");
				newVideos = dataService.getVideoList(FilmDetailActivity.this, mVideoId, mVideoType, "", 10, 1, "");
				// end 为了适配3d频道修改mVideoType 从数据中获取对应的频道号
				Log.d(TAG, "---newVideos:" + newVideos.size());
				if (newVideos != null && newVideos.size() > 0) {
					VideoNewEntity entity = newVideos.get(0);
					vrsAlbumId = String.valueOf(entity.getVideosetId());// ntity.getCp_videoset_id().split(",")[1];
					vrsTvId = String.valueOf(entity.getVideoId());
					;// entity.getCp_video_id().split(",")[1];
					Log.d(TAG, "--vrsAlbumId:" + vrsAlbumId + "--vrsTvId:" + vrsTvId);
				}

			}

			@Override
			public void processServiceException(ServiceException e) {
				e.printStackTrace();
				// 显示错误提示框
				showErrorDialog(e.getErrorCode(), false);
			}
		});
	}

	private void requestVideoList(final int videoset_id) {
		submitRequest(new SafeRunnable() {
			@Override
			public void requestData() {
				// start author zhangpengzhan
				Log.d(TAG, "----requestVideoList");
				List<VideoNewEntity> newVideos = dataService.getVideoList(FilmDetailActivity.this, videoset_id, mVideoType, "", 10, 1, "");
				// end 为了适配3d频道修改mVideoType 从数据中获取对应的频道号
				Log.d(TAG, "---newVideos:" + newVideos.size());
				if (newVideos != null && newVideos.size() > 0) {
					VideoNewEntity entity = newVideos.get(0);
					vrsAlbumId = String.valueOf(entity.getVideosetId());
					;// ntity.getCp_videoset_id().split(",")[1];
					vrsTvId = String.valueOf(entity.getVideoId());// entity.getCp_video_id().split(",")[1];
					Log.d(TAG, "--vrsAlbumId:" + vrsAlbumId + "--vrsTvId:" + vrsTvId);
				}

			}

			@Override
			public void processServiceException(ServiceException e) {
				e.printStackTrace();
				// 显示错误提示框
				showErrorDialog(e.getErrorCode(), false);
			}
		});
	}

	/**
	 * 网络请求爱奇艺相关推荐
	 */
	private void requestQiyiRecommand(final int chnnid, final int id) {
		// 请求电影推荐数据
		submitRequest(new SafeRunnable() {
			@Override
			public void requestData() {
				// 通过接口获取数据
				flims = dataService.getQiyiFilmRelativeRecommands(chnnid, id);
				if (null != flims && flims.size() > 0) {// 如果有数据
					handler.sendEmptyMessage(REQUEST_QIYI_RELATIVE_SUCCESS);
				} else {// 如果没有数据
					handler.sendEmptyMessage(REQUEST_QIYI_RELATIVE_FAIL);
				}

			}

			/*
			 * 网络异常的方法 (non-Javadoc)
			 * 
			 * @see
			 * com.hiveview.tv.service.request.SafeRunnable#processServiceException
			 * (com.hiveview.tv.service.exception.ServiceException)
			 */
			@Override
			public void processServiceException(ServiceException e) {
				e.printStackTrace();
				showErrorDialog(e.getErrorCode(), false);
				handler.sendEmptyMessage(REQUEST_QIYI_RELATIVE_FAIL);
			}

		});
	}

	/**
	 * 获取播放记录
	 * 
	 * @ClassName: GetPlayerRecord
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月5日 下午2:04:50
	 * 
	 */
	// class GetPlayerRecord implements Runnable {
	// public GetPlayerRecord() {
	//
	// }
	//
	// public void run() {
	// // 获取奇艺的播放记录
	// qiyiTVApi.getPlayRecords(apiResultHandler);
	// }
	// }

	// class QiYiApiResultHandler implements IApiResultHandler {
	//
	// @Override
	// public void onFailure(int arg0) {// 记录获取失败
	// // TODO Auto-generated method stub
	//
	// }
	//
	// public void onSuccess(List<?> arg0) {// 记录获取成功
	// @SuppressWarnings("unchecked")
	// List<QYTVAlbum> list = (List<QYTVAlbum>) arg0;
	//
	// for (QYTVAlbum entitys : list) {
	// if (entitys.getVrsAlbumId().equals(vrsAlbumId)) {// 如果在记录的列表中包含当前的视频
	// Log.d(TAG, "我在详情页 可以继续播放");
	// // 如果他存在于播放记录，并且开始时间为零就是说明改影片已经播放完毕
	// if (entitys.getStartTime() == 0) {// 状态改为播放
	// handler.sendEmptyMessage(PLAYER_DETAIL);
	// } else {// 状态改为续播
	// handler.sendEmptyMessage(PLAYER_GO_ON);
	// }
	//
	// }
	// }
	//
	// }
	//
	// }
	class getQIYIPlayerRecords extends SafeRunnable {

		@Override
		public void requestData() {
			Log.d(TAG, "getQIYIPlayerRecords----mVideoId::" + mVideoId);
			String videoId = String.valueOf(mVideoId);
			if (QIYIRecordUtils.getHistoryList(FilmDetailActivity.this, videoId) != null
					&& QIYIRecordUtils.getHistoryList(FilmDetailActivity.this, videoId).size() != 0) {
				for (PlayerRecordEntity entity : QIYIRecordUtils.getHistoryList(FilmDetailActivity.this, videoId)) {
					PlayerRecordEntity entitys = entity;
					Log.d(TAG, "getQIYIPlayerRecords----entitys.getName()::" + entitys.getName());
					Log.d(TAG, "getQIYIPlayerRecords----entitys.getStartTime()::" + entitys.getStartTime());
					if (entitys.getStartTime() > 0) {
						handler.sendEmptyMessage(PLAYER_GO_ON);
					} else {
						handler.sendEmptyMessage(PLAYER_DETAIL);
					}
				}

			} else {
				handler.sendEmptyMessage(PLAYER_DETAIL);
			}

		}

		@Override
		public void processServiceException(ServiceException e) {
			// TODO Auto-generated method stub

		}
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "----onResume");
		super.onResume();
		submitRequest(new getQIYIPlayerRecords());
		isFirstCreat = false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "==============onDestroy");
		getContentResolver().unregisterContentObserver(cob);
		// qiyiTVApi.clearPlayRecords(apiResultHandler);
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
			Log.d(TAG, "==" + view.getId() + "===" + has);

		}

	}

	/**
	 * 
	 * item view 身上的按键监听
	 */
	class ViewItemKeyListener implements CallBackItemViewKeyListener {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// 最后一个数据的 右键给屏蔽掉
			if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {// 响应菜单按键
				int entityIndex = flims.indexOf((FilmNewEntity) v.getTag());
				if (entityIndex == (flims.size() - 1))
					return true;
			}
			return false;
		}
	}

	/**
	 * 监听ItemView上的点击事件
	 * 
	 * @author lihongji
	 * 
	 */
	class ViewItemClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// int p = (Integer) v.getTag();
			Intent intent = new Intent();
			intent.setClass(getBaseContext(), FilmDetailActivity.class);
			intent.putExtra("entity", (FilmNewEntity) v.getTag());
			intent.putExtra("request", true);
			intent.putExtra("source", AppConstant.SOURCE_RELATED_RECOMMAND);
			startPlayerTime = System.currentTimeMillis();
			long time = startPlayerTime - creatTime;
			// sendStatistics(String.valueOf(time), mFilmEntity,
			// DataType.CLICK_TAB_FILM);
			startActivity(intent);
		}

	}

	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		super.onExecute(intent);
		if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if ("play".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(FilmDetailActivity.this, "播放", intent);
					Log.v(TAG, "JSONAnalyze1====" + com.alibaba.fastjson.JSONObject.toJSONString(video_Entity));
					if (vrsAlbumId.length() > 0 && vrsTvId.length() > 0) {
						isOnClick = true;
						startPlayerTime = System.currentTimeMillis();
						long time = startPlayerTime - creatTime;
						// sendStatistics(String.valueOf(time), mFilmEntity,
						// DataType.CLICK_TAB_FILM);
						// QiYiPlayerUtil.startNewQiYiPlayer(FilmDetailActivity.this,
						// vrsTvId, vrsAlbumId);
						QiYiPlayerUtil.startSDKPlayer(FilmDetailActivity.this, com.alibaba.fastjson.JSONObject.toJSONString(video_Entity), null,
								false, true, false, null, false);
						handler.sendEmptyMessage(PLAYER_GO_ON);
					} else {
						showErrorDialog(HiveViewErrorCode.E0000601, false);
					}
				} else if ("collect".equals(command)) {

					if (null != mFilmEntity) {
						boolean isSaveed = Boolean.parseBoolean(ivCollect.getTag().toString());
						// 修改如果当前是收藏状态就修改成非收藏状态,同时删除数据库的数据
						if (isSaveed) {
							HomeSwitchTabUtil.closeSiRi(FilmDetailActivity.this, "您已经收藏", intent);

						} else {
							dao.insert(mFilmEntity);
							ivCollect.setImageResource(R.drawable.film_detail_fav_has);
							ivCollect.setTag(true);
							HomeSwitchTabUtil.closeSiRi(FilmDetailActivity.this, "收藏", intent);
						}

					}
				} else if ("canclecollect".equals(command)) {

					if (null != mFilmEntity) {
						boolean isSaveed = Boolean.parseBoolean(ivCollect.getTag().toString());
						// 修改如果当前是收藏状态就修改成非收藏状态,同时删除数据库的数据
						if (isSaveed) {
							dao.delete("id = ?", new String[] { String.valueOf(mFilmEntity.getId()) });
							ivCollect.setImageResource(R.drawable.film_detail_fav);
							ivCollect.setTag(false);
							HomeSwitchTabUtil.closeSiRi(FilmDetailActivity.this, "取消收藏", intent);
						} else {
							HomeSwitchTabUtil.closeSiRi(FilmDetailActivity.this, "您还未收藏", intent);

						}

					}

				}
			}

		}
	}

	HashMap<String, String[]> commands = new HashMap<String, String[]>();
	// 场景id
	private String scenceId = "com.hiveview.tv.activity.MovieDemandListActivity";

	public String onQuery() {
		// TODO 自动生成的方法存根
		commands.put("play", new String[] { "播放", "续播" });
		commands.put("collect", new String[] { "收藏" });
		commands.put("canclecollect", new String[] { "取消收藏" });
		Log.d(TAG, "onQunery====>1");

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
			jsonObject = JsonUtil.makeScenceJson(scenceId, commands, fuzzayWords1, fuzzayWords2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "onQunery====>2");
		return jsonObject.toString();

	}

}
