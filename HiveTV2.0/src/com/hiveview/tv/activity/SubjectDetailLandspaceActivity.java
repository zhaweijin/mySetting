package com.hiveview.tv.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.strategy.NeighborOneLineStrategy;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.content.ContentInvoker;
import com.hiveview.tv.common.factory.LandSpaceItemFactory;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.ItemType;
import com.hiveview.tv.common.statistics.KeyEventHandler;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.SubjectEntity;
import com.hiveview.tv.service.entity.SubjectInfo;
import com.hiveview.tv.service.entity.SubjectInfoList;
import com.hiveview.tv.service.entity.SubjectListEntity;
import com.hiveview.tv.service.entity.SubjectListEntitys;
import com.hiveview.tv.service.entity.VideoEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.PlayerParamsUtils;
import com.hiveview.tv.utils.PlayerParamsUtils.VideoPlayerListener;
import com.hiveview.tv.view.ProgressDialog;

/**
 * 专题横图 显示方式
 * 
 * @ClassName: SubjectDetailLandspaceActivity
 * @Description: TODO
 * @author: 周一川
 * @date 2014-6-3 下午2:21:37
 * 
 */
public class SubjectDetailLandspaceActivity extends BaseActivity {

	private static final String TAG = "SubjectDetailLandspaceActivity";
	private ProgressDialog loadingDialog;

	private ImageView ivPagerLeft;
	private ImageView ivPagerRight;

	private TextView tvSubjectName;
	private TextView tvSubjectDesc;
	private View baseInfoView;
	/**
	 * 一次加载的条数
	 */
	private int PAGE_SIZE = 120;
	/**
	 * 页数
	 */
	private int PAGE_NUM = 1;

	private HivePreloadViewPager videoListViewPager;

	private SubjectListEntity entity;
	private ArrayList<SubjectInfoList> subjectInfoList;
	private ArrayList<FilmNewEntity> videoSetList;
	private SubjectInfoList SubjectInfoLists;
	private final int REQUEST_SUBJECT_VIDEO_LIST = 10;
	private static final int PAGESIZE = 4;
	private int pageCount = -1;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.subjec_detail_landspace_layout);
		Log.v("action", "CONTENT_ACTION_MOVIESUBJECT5");
		init();

	}

	/**
	 * 初始化界面
	 * 
	 * @Title: SubjectDetailLandspaceActivity
	 * @author:周一川
	 * @Description: TODO
	 */
	private void init() {
		Intent intent = this.getIntent();
		entity = (SubjectListEntity) intent.getSerializableExtra("entity");
		Log.v("action", "CONTENT_ACTION_MOVIESUBJECT6"+entity);
		if (null != entity) {
			video_Entity = entity;
			ivPagerLeft = (ImageView) this.findViewById(R.id.iv_pager_left);
			ivPagerRight = (ImageView) this.findViewById(R.id.iv_pager_right);
			tvSubjectName = (TextView) this.findViewById(R.id.tv_subject_name);
			tvSubjectDesc = (TextView) this.findViewById(R.id.tv_subject_desc);
			loadingDialog = (ProgressDialog) this.findViewById(R.id.iv_bulelight_loading);
			videoListViewPager = (HivePreloadViewPager) this.findViewById(R.id.vp_recommend);
			baseInfoView = findViewById(R.id.rl_subject_info);
			setPageBackgroundImage(entity.getSubjectBgImg());
//			tvSubjectName.setText(SubjectListEntitys.getPageContent().get(0).getSubjectName());
//			tvSubjectDesc.setText(SubjectListEntitys.getPageContent().get(0).getSubjectDesc());
			baseInfoView.setVisibility(View.INVISIBLE);
			videoListViewPager.setVisibility(View.INVISIBLE);
			loadingDialog.setVisibility(View.VISIBLE);
			requestSubjects();
		} else {
			// 如果上个界面穿过来的数据为空， 就自杀， (建议给用户一些提示， 这样用户会感到很奇怪界面一闪)
			finish();
			LogUtil.info("data error!!!!!!");
		}

	}

	@Override
	protected void processData(int msgWhat) {
		super.processData(msgWhat);
		switch (msgWhat) {
		case REQUEST_SUBJECT_VIDEO_LIST:
			fillPagers();
			loadingDialog.setVisibility(View.GONE);
			baseInfoView.setVisibility(View.VISIBLE);
			videoListViewPager.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	/**
	 * 从网络上请求专题的剧集数据，比如说从专题选择“蝙蝠侠”进入的，此时在这里请求的数据是“蝙蝠侠1”，“蝙蝠侠2”等
	 */
	private void requestSubjects() {
		submitRequest(new SafeRunnable() {
			@Override
			public void requestData() {
				subjectInfoList = new HiveTVService().getSubjectInfo("",entity.getSubjectId()+"","",PAGE_SIZE,PAGE_NUM);
			//	Log.v(TAG, "subjectInfoList=="+subjectInfoList.toString());
				if (null != subjectInfoList && subjectInfoList.size() > 0) {
					SubjectInfoLists = subjectInfoList.get(0);
					videoSetList=new ArrayList<FilmNewEntity>();
					List<SubjectInfo> mSubjectEntitys=SubjectInfoLists.getPageContent();
					for(int i=0;i<mSubjectEntitys.size();i++){
					FilmNewEntity mFilmNewEntity=new FilmNewEntity();
					SubjectInfo mSubjectEntity=mSubjectEntitys.get(i);
					mFilmNewEntity.setId(mSubjectEntity.getContentId());
					mFilmNewEntity.setPosterUrl(mSubjectEntity.getContentImg());
					mFilmNewEntity.setName(mSubjectEntity.getContentName());
					mFilmNewEntity.setCid(mSubjectEntity.getContentCid());
					mFilmNewEntity.setSubject_id(mSubjectEntity.getSubjectId());
					videoSetList.add(mFilmNewEntity);
					}
					Log.v(TAG, "SubjectListEntitys=="+SubjectInfoLists.toString());
					handler.sendEmptyMessage(REQUEST_SUBJECT_VIDEO_LIST);
				}
			}

			@Override
			public void processServiceException(ServiceException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 填充ViewPager的内容
	 */
	private void fillPagers() {
	

		
		// 可以显示几页，取上限制，如16个需要显示3页
		pageCount = (int) Math.ceil(videoSetList.size() / (double) PAGESIZE);// 可以显示几页，取上限制，如16个需要显示3页

		// 用android 的原生的控件是有问题的， 所以要我们自己的封装的控件
		// FilmPagerAdapter adapter = new FilmPagerAdapter(views);
		HivePagerAdapter adapter = new HivePagerAdapter(getApplicationContext(), new LandSpaceItemFactory(new ViewItemKeyListener(),
				new ViewItemClickListener()), videoListViewPager, PAGESIZE, new NeighborOneLineStrategy());
		adapter.addDataSource(videoSetList);
		// 设HivePagerAdapter具体有多少的itme
		adapter.setDataTotalSize(videoSetList.size());

		videoListViewPager.setAdapter(adapter);// ViewPager填充
		videoListViewPager.setPreloadingListener(new ViewPagerProLoadingListener());

		ivPagerLeft.setVisibility(View.INVISIBLE);
		ivPagerRight.setVisibility(View.INVISIBLE);

		if (pageCount == 1) {// 只有一页的情况
			ivPagerLeft.setVisibility(View.INVISIBLE);
			ivPagerRight.setVisibility(View.INVISIBLE);
		} else if (pageCount > 1) {
			// 页数大于1的时候显示翻页图片
			ivPagerRight.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * HivePagerAdapter 回调具体按键的onkey
	 * 
	 * @ClassName: ViewItemKeyListener
	 * @Description: TODO
	 * @author: 周一川
	 * @date 2014-6-3 下午1:49:52
	 * 
	 */
	class ViewItemKeyListener implements CallBackItemViewKeyListener {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			Log.v(TAG, v.getTag().toString());
			if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {// 响应菜单按键

			}
			return false;
		}
	}

	/**
	 * 获取焦点的itme响应点击事件 点击后进入 详情页
	 */
	class ViewItemClickListener implements OnClickListener {

		@Override
		public void onClick(View view) {

			FilmNewEntity entity1 = (FilmNewEntity) view.getTag();
			Log.v(TAG, "FilmEntity=="+entity1.toString());
			int viewIndex = videoSetList.indexOf(entity1) + 1;
			entity1.setPosition_id(viewIndex);
			entity1.setSubject_id(SubjectInfoLists.getPageContent().get(viewIndex-1).getSubjectId());
			entity1.setSubject_name(SubjectInfoLists.getPageContent().get(viewIndex-1).getContentName());
			Log.v(TAG, "viewIndex="+viewIndex+"getSubjectId=="+SubjectInfoLists.getPageContent().get(viewIndex-1).getSubjectId()+
					"getContentName=="+SubjectInfoLists.getPageContent().get(viewIndex-1).getContentName());
			sendSubjectStatistics(entity1, DataType.CLICK_TAB_SUBJECT);
			String action = ContentInvoker.getInstance().getContentAction(entity1.getCid());
			Log.d(TAG, "action======" + action);
			if (!TextUtils.isEmpty(action)) {// 跳转到详情页面
				try {
					Intent intent = new Intent(action);
					intent.putExtra("id", SubjectInfoLists.getPageContent().get(viewIndex-1).getContentId());
					intent.putExtra("source", AppConstant.SOURCE_SUBJECT);
					startActivity(intent);
				} catch (Exception e) {
					Log.d(TAG, "error==" + e.toString());
					e.printStackTrace();
				}
			} else {// 直接调用奇艺的播放器播放
				PlayerParamsUtils.getVideoPlayParams(entity1.getId(), entity1.getCid(), "",getApplicationContext(),null);
				KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setTabNo(Tab.TAB)
						.setViewPosition("0306").setSource(AppConstant.SOURCE_SUBJECT)
						.setEntity(entity1).setDataType(DataType.CLICK_TAB_FILM)
						.setSrcType(ItemType.SUBJECT)
						.build());
			}

		}
	}

	/**
	 * HivePagerAdapter 提供简便操作 可以动态的做一些动作
	 * 
	 * @ClassName: ViewPagerProLoadingListener
	 * @Description: TODO
	 * @author: 周一川
	 * @date 2014-6-3 下午1:52:27
	 * 
	 */
	class ViewPagerProLoadingListener implements OnPreloadingListener {

		@Override
		public void preLoading(int pageSize) {

		}

		@Override
		public void preLoadNotFinish() {

		}

		@Override
		public void onFirstPage() {
			// 引导左右图标
			ivPagerLeft.setVisibility(View.INVISIBLE);
			ivPagerRight.setVisibility(View.VISIBLE);
		}

		@Override
		public void onLastPage() {

		}

		@Override
		public void setPageCurrent(int pageIndex) {
			// 引导左右图标的影藏
			ivPagerLeft.setVisibility(View.VISIBLE);
			ivPagerRight.setVisibility(View.VISIBLE);
			// 最后一页的判断算法
			if (pageIndex >= pageCount) {
				ivPagerLeft.setVisibility(View.VISIBLE);
				ivPagerRight.setVisibility(View.INVISIBLE);
			}
		}
	}
}
