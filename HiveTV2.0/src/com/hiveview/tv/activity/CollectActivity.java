package com.hiveview.tv.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.strategy.NeighborTwoLineStrategy;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.content.ContentInvoker;
import com.hiveview.tv.common.factory.VideoItemFactory;
import com.hiveview.tv.service.dao.CollectVoideoNewDAO;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.MoviePermissionEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.BlueLightUtils;
import com.hiveview.tv.utils.BlueLightVipUtil;
import com.hiveview.tv.utils.DialogUtils;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.OnDialogClickListener;
import com.hiveview.tv.utils.ToastUtils;
import com.hiveview.tv.view.MatrixTVForeidgnView;
import com.hiveview.tv.view.ProgressDialog;
import com.hiveview.tv.view.VideoListItemPageView.GetFocusEntity;
import com.paster.util.JsonUtil;

/**
 * @ClassName: CollectActivity
 * @Description: 影片收藏
 * @author:
 * @date 2014年12月9日 上午9:33:12
 * 
 */
public class CollectActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener {

	public static final int LOAD_TVLIST_SUCCESS = 41;

	/**
	 * 本地收藏的数据集合
	 */
	private List<FilmNewEntity> collectListDatas = new ArrayList<FilmNewEntity>();

	/**
	 * 载入本地数据完成
	 */
	private final int LOAD_COLLECT_DATA_FINISH = 7000;

	/**
	 * 载入本地数据为空
	 */
	private final int LOAD_COLLECT_DATA_EMPTY = -7000;

	/**
	 * 是否是删除的
	 */
	private final int LOAD_COLLECT_DATA_DELETE = 0x00147;

	/**
	 * 每页显示多少数据
	 */
	private final int pageViewSize = 12;

	/**
	 * 显示数据列表的ViewPager
	 */
	private HivePreloadViewPager mViewPager = null;
	private HivePagerAdapter adapter = null;

	/**
	 * 上一页按钮
	 */
	private View vUpPage = null;
	/**
	 * 下一页按钮
	 */
	private View vDownPage = null;

	/**
	 * 左侧指示器
	 */
	private ImageView imageLeft = null;

	/**
	 * 右侧指示器
	 */
	private ImageView imageRight = null;
	/**
	 * 没有数据的背景图
	 */
	private ImageView noDataImage = null;

	/**
	 * 显示当前页
	 */
	private TextView tvCurrentPage = null;

	/**
	 * 是否是初次展示首页
	 */
	private boolean isFristCreate = false;

	/**
	 * 显示总页数
	 */
	private TextView tvTotalPage = null;

	/*
	 * 
	 * 影片收藏总数
	 */
	private TextView tvTotal = null;

	/**
	 * 用户提示(按菜单键清除影片收藏)
	 * 
	 */
	private TextView tvUserHine = null;

	/**
	 * 
	 * 分隔符
	 * 
	 */
	private TextView tvDivision = null;
	/**
	 * 列表的总数
	 * 
	 * @Fields listToal:TODO
	 */
	private int listToal;

	/**
	 * 上下页按钮 焦点状态
	 */
	private boolean isChangePageByUpButton = false;
	private boolean isChangePageByDownButton = false;

	/**
	 * 是否是删除
	 */
	private boolean isDelete = false;

	/**
	 * 如果收藏为空的时候提示用户收藏为空的提示消息
	 */
	private TextView mCollectHine = null;
	/**
	 * 总页数
	 */
	private int pageCount;
	/**
	 * 当前的页码数
	 * 
	 * @Fields pageIndex
	 */
	private int pageIndexs = 0;
	/**
	 * 焦点所在的位置
	 * 
	 * @Fields entityIndexs
	 */
	private int entityIndexs = 0;

	private CancelDialogListener dialogListener;
	private FilmNewEntity mDelEntity = null;
	private CollectVoideoNewDAO daoCollect = null;
	private ProgressDialog vLoading = null;
	/**
	 * 当前页面的view
	 * 
	 * @Fields views:TODO
	 */
	public List<View> viewList;

	private boolean isResume = false;
	private boolean isEmpty = true;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		// start 将布局collect_layout修改为collect_layout_1 author:huzuwei
		setContentView(R.layout.collect_layout_1);
		// end
		viewList = new ArrayList<View>();
		isFristCreate = true;
		initView();
	//	Log.v(TAG, "极清收藏" + BlueLightUtils.getFavoriteList(this).toString());

	}

	/*
	 * 输入输出的格式 %s输出一个 字符串 %c以字符形式输出单个字符 %f6位小数 %d十进制 %o八进制 %x十六进制
	 */

	/**
	 * 初始化View
	 */
	private void initView() {

		// 暂时没有收藏记录的提示消息
		mCollectHine = (TextView) findViewById(R.id.collect_layout_collecthint);
		noDataImage = (ImageView) findViewById(R.id.collect_no_data);
		vLoading = (ProgressDialog) findViewById(R.id.pd_list_loading);
		tvTotal = (TextView) findViewById(R.id.tv_collect_count);
		vDownPage = findViewById(R.id.list_page_down_ll);
		vUpPage = findViewById(R.id.list_page_up_ll);
		tvDivision = (TextView) findViewById(R.id.list_division);
		imageLeft = (ImageView) findViewById(R.id.list_left);
		imageRight = (ImageView) findViewById(R.id.list_right);
		tvUserHine = (TextView) findViewById(R.id.user_hint);
		vUpPage.setOnClickListener(this);
		vDownPage.setOnClickListener(this);
		vUpPage.setOnFocusChangeListener(this);
		vDownPage.setOnFocusChangeListener(this);

		tvCurrentPage = (TextView) findViewById(R.id.list_page_current);
		tvTotalPage = (TextView) findViewById(R.id.list_page_count);
		mViewPager = (HivePreloadViewPager) findViewById(R.id.vp_list);
		mViewPager.setNextPreviousPageView(vUpPage, vDownPage);
		tvUserHine.setVisibility(View.INVISIBLE);
		imageLeft.setVisibility(View.INVISIBLE);
		imageRight.setVisibility(View.INVISIBLE);
		tvDivision.setVisibility(View.INVISIBLE);
		mCollectHine.setVisibility(View.INVISIBLE);
		tvTotal.setVisibility(View.INVISIBLE);
		vDownPage.setVisibility(View.INVISIBLE);
		tvCurrentPage.setVisibility(View.INVISIBLE);
		vUpPage.setVisibility(View.INVISIBLE);
		tvTotalPage.setVisibility(View.INVISIBLE);

		dialogListener = new CancelDialogListener();

		// 载入数据
		loadCollectData();
	}

	protected void processData(int msgWhat) {
		switch (msgWhat) {
		case LOAD_COLLECT_DATA_FINISH:// 收藏数据加载完成
			isEmpty = false;
			Log.v(TAG, "processData" + collectListDatas.toString());
			tvUserHine.setVisibility(View.VISIBLE);
			imageLeft.setVisibility(View.VISIBLE);
			imageRight.setVisibility(View.VISIBLE);
			tvDivision.setVisibility(View.VISIBLE);
			tvTotal.setVisibility(View.VISIBLE);
			vDownPage.setVisibility(View.VISIBLE);
			tvCurrentPage.setVisibility(View.VISIBLE);
			vUpPage.setVisibility(View.VISIBLE);
			tvTotalPage.setVisibility(View.VISIBLE);
			vLoading.setVisibility(View.GONE);
			mCollectHine.setVisibility(View.GONE);// 保证在有数据的时候这个提示信息绝对不会出现的
			noDataImage.setVisibility(View.GONE);
			if (null == adapter) {
				adapter = new HivePagerAdapter(this, new VideoItemFactory(new ViewItemFocusListener(), new ViewItemKeyListener(),
						new ViewItemClickListener(), new GetFocusEntitys()), mViewPager, pageViewSize, new NeighborTwoLineStrategy());
				adapter.setDataTotalSize(listToal);// 设置数据总记录数
				adapter.addDataSource(collectListDatas);
			} else {
				// 添加数据
				adapter.addDataSource(collectListDatas, pageIndexs, entityIndexs);
			}
			// adapter.addDataSource(collectListDatas);
			Log.d(TAG, "pageIndex->" + pageIndexs + "-entityIndexs->" + entityIndexs);
			listToal = collectListDatas.size();
			tvTotal.setText(String.format(getResources().getString(R.string.collect_total_text), listToal));

			// 设置第一页
			tvCurrentPage.setText(1 + "");
			// 设置总页数
			pageCount = (int) Math.ceil(collectListDatas.size() / (double) pageViewSize);
			tvTotalPage.setText(String.format(getResources().getString(R.string.list_top_page_count), pageCount));

			mViewPager.setAdapter(adapter);

			mViewPager.setPreloadingListener(new OnPreloadingListener() {

				@Override
				public void setPageCurrent(int pageIndex) {// 翻页过程
					tvCurrentPage.setText(pageIndex + "");// 设置当前页数
					pageIndexs = pageIndex - 1;
					Log.d(TAG, "pageIndexs::" + pageIndexs);
					if (pageIndex != 1) {// 显示上一页按钮
						vUpPage.setVisibility(View.VISIBLE);
						imageLeft.setVisibility(View.VISIBLE);
					}

					if (pageIndex != adapter.getViews().size()) {// 显示下一页按钮
						vDownPage.setVisibility(View.VISIBLE);
						imageRight.setVisibility(View.VISIBLE);
					}
					// 最后一页
					if (pageIndex >= pageCount) {
						vUpPage.setVisibility(View.VISIBLE);
						// start 将INVISIBLE设置为GONE author:huzuwei
						vDownPage.setVisibility(View.GONE);
						// end
						imageLeft.setVisibility(View.VISIBLE);
						imageRight.setVisibility(View.INVISIBLE);
					}

					if (isChangePageByUpButton || isChangePageByDownButton) {
						if (isChangePageByUpButton) {
							vUpPage.requestFocus();
							// 初始化焦点状态，保证用户在翻页的时候按键总是在同一按钮上
							isChangePageByUpButton = false;
						}

						if (isChangePageByDownButton) {
							vDownPage.requestFocus();
							// 初始化焦点状态，保证用户在翻页的时候按键总是在同一按钮上
							isChangePageByDownButton = false;
						}
					} else {
						// vUpPage.setFocusable(false);
						// vDownPage.setFocusable(false);

					}

				}

				public void preLoading(int pageSize) {

				}

				public void preLoadNotFinish() {

				}

				public void onLastPage() {// 最后一页
					/*
					 * vUpPage.setVisibility(View.VISIBLE);
					 * vDownPage.setVisibility(View.INVISIBLE);
					 * vUpPage.requestFocus();
					 */
				}

				@Override
				public void onFirstPage() {// 第一页.
					imageLeft.setVisibility(View.INVISIBLE);
					imageRight.setVisibility(View.VISIBLE);
					vUpPage.setVisibility(View.INVISIBLE);
					vDownPage.setVisibility(View.VISIBLE);

				}
			});

			// 判断是否只有一页的情况
			if (collectListDatas.size() > pageViewSize) {

				imageLeft.setVisibility(View.INVISIBLE);
				imageRight.setVisibility(View.VISIBLE);
				vDownPage.setVisibility(View.VISIBLE);
				vUpPage.setVisibility(View.INVISIBLE);
			} else {
				// 只有一页
				imageLeft.setVisibility(View.INVISIBLE);
				imageRight.setVisibility(View.INVISIBLE);
				// start:只有一页，页数右对齐 author:huzuwei
				vDownPage.setVisibility(View.GONE);
				// end
				vUpPage.setVisibility(View.INVISIBLE);
			}
			// 在viewpager 没有初始化完成的时候，不让按钮可以获取焦点
			// 按钮防止抢夺焦点
			vDownPage.setFocusableInTouchMode(true);
			vUpPage.setFocusableInTouchMode(true);
			vDownPage.setFocusable(true);
			vUpPage.setFocusable(true);
			// 设置第一个item获取焦点
			// adapter.getCurrentPageView().setPositionGetFocus(0);
			mViewPager.setNextPreviousPageView(vUpPage, vDownPage);
			mViewPager.requestFocus();
			break;
		case LOAD_COLLECT_DATA_EMPTY:// 空数据的时候
			isEmpty = true;
			mViewPager.setVisibility(View.INVISIBLE);
			vLoading.setVisibility(View.INVISIBLE);
			tvUserHine.setVisibility(View.INVISIBLE);
			imageLeft.setVisibility(View.INVISIBLE);
			imageRight.setVisibility(View.INVISIBLE);
			tvDivision.setVisibility(View.INVISIBLE);
			tvTotal.setVisibility(View.INVISIBLE);
			vDownPage.setVisibility(View.INVISIBLE);
			tvCurrentPage.setVisibility(View.INVISIBLE);
			vUpPage.setVisibility(View.INVISIBLE);
			tvTotalPage.setVisibility(View.INVISIBLE);
			mCollectHine.setVisibility(View.INVISIBLE);// 提示用户没有播放记录
			noDataImage.setVisibility(View.VISIBLE);
			break;
		case LOAD_COLLECT_DATA_DELETE:
			if (((int) Math.ceil(collectListDatas.size() / (double) pageViewSize) - 1) < pageIndexs && isResume)
				pageIndexs--;

			isResume = false;
			adapter.addDataSource(collectListDatas, pageIndexs, entityIndexs);

			tvTotal.setText(String.format(getResources().getString(R.string.collect_total_text), collectListDatas.size()));
			adapter.setDataTotalSize(collectListDatas.size());// 设置数据总记录数

			// 设置页码
			tvCurrentPage.setText((pageIndexs + 1) + "");
			// 设置总页数
			pageCount = (int) Math.ceil(collectListDatas.size() / (double) pageViewSize);
			tvTotalPage.setText(String.format(getResources().getString(R.string.list_top_page_count), pageCount));
			// 判断是否只有一页的情况

			if (collectListDatas.size() > pageViewSize) {

				// 不是第一页
				if (pageIndexs != 0) {
					// 判断是否最后一页

					if (pageIndexs >= pageCount - 1) {
						imageLeft.setVisibility(View.VISIBLE);
						imageRight.setVisibility(View.INVISIBLE);
						// start:设置为隐藏，author:huzuwei
						vDownPage.setVisibility(View.GONE);
						// end
						vUpPage.setVisibility(View.VISIBLE);
					} else {
						imageLeft.setVisibility(View.VISIBLE);
						imageRight.setVisibility(View.VISIBLE);
						vDownPage.setVisibility(View.VISIBLE);
						vUpPage.setVisibility(View.VISIBLE);
					}
				} else {
					// 第一页
					imageLeft.setVisibility(View.INVISIBLE);
					imageRight.setVisibility(View.VISIBLE);
					vDownPage.setVisibility(View.VISIBLE);
					vUpPage.setVisibility(View.INVISIBLE);
				}
			} else {
				// 只有一页
				imageLeft.setVisibility(View.INVISIBLE);
				imageRight.setVisibility(View.INVISIBLE);
				// start:只有一页，页数右对齐 author:huzuwei
				vDownPage.setVisibility(View.GONE);
				// end
				vUpPage.setVisibility(View.INVISIBLE);
				tvCurrentPage.setText(mViewPager.getCurrentItem() + 1 + "");
			}
			vLoading.setVisibility(View.INVISIBLE);
			isDelete = false;
			isEmpty = false;
			break;

		default:

			break;
		}
	}

	/**
	 * 从数据库中加载收藏记录
	 */
	private void loadCollectData() {
		submitRequest(new Runnable() {
			@Override
			public void run() {
				daoCollect = new CollectVoideoNewDAO(CollectActivity.this);
				if (null != collectListDatas && collectListDatas.size() != 0)
					collectListDatas.clear();
				// 根据当前的用户ID去查询，按时间降序排列

				List<FilmNewEntity> collect_video = daoCollect.query(null, CollectVoideoNewDAO.UID + " = ?", new String[] { "0" }, CollectVoideoNewDAO.UID
						+ " asc");
				collectListDatas.addAll(BlueLightUtils.getFavoriteList(CollectActivity.this));
				collectListDatas.addAll(collect_video);
				if (isDelete) {
					if (collectListDatas.size() != 0) {
						handler.sendEmptyMessage(LOAD_COLLECT_DATA_DELETE);
					} else {
						handler.sendEmptyMessage(LOAD_COLLECT_DATA_EMPTY);
					}
				} else {
					if (collectListDatas.size() != 0) {
						Log.v(TAG, collectListDatas.toString());
						handler.sendEmptyMessage(LOAD_COLLECT_DATA_FINISH);
					} else {
						handler.sendEmptyMessage(LOAD_COLLECT_DATA_EMPTY);
					}
				}
			}
		});
	}

	/**
	 * 取消收藏对话框按钮事件监听
	 * 
	 * @author chenlixiao
	 * 
	 */
	class CancelDialogListener implements OnDialogClickListener {

		@Override
		public void onConfirm() {// 确定取消收藏
			Log.v(TAG, mDelEntity.toString());
			if (null != mDelEntity) {
				if (mDelEntity.getSource() == 0) {
					isDelete = true;
					vLoading.setVisibility(View.VISIBLE);
					// start:修改删除条件,如果videoset_id不为空,则根据其删除,否则根据cp_videoset_id删除，author:huzuwei
					// 如果是综艺，则根据Videoset_id删除，其他根据CP_VIDEOSET_ID删除
					// if (mDelEntity.getVideoset_type() == 6) {
					// daoCollect.delete(CollectVoideoDAO.VIDEOSET_ID + " = ?",
					// new
					// String[] { mDelEntity.getVideoset_id() + "" });
					//
					// } else {
					// daoCollect.delete(CollectVoideoDAO.CP_VIDEOSET_ID +
					// " = ?",
					// new String[] { mDelEntity.getCp_videoset_id() + "" });
					// }
					if (mDelEntity.getId() != 0) {
						daoCollect.delete(CollectVoideoNewDAO.ID + " = ?", new String[] { mDelEntity.getId() + "" });
					}
//					else if (mDelEntity.getVideoset_id() == 0 && !mDelEntity.getCp_videoset_id().toString().equals("")) {
//						daoCollect.delete(CollectVoideoNewDAO.CP_VIDEOSET_ID + " = ?", new String[] { mDelEntity.getCp_videoset_id() + "" });
//					}
					// end
					vUpPage.setFocusable(false);
					ToastUtils.alert(CollectActivity.this, getResources().getString(R.string.collect_cancel_text));
					// adapter.clear();
				} else if (mDelEntity.getSource() == 1) {

					BlueLightUtils.deleteCOLLECT(CollectActivity.this, Integer.parseInt(mDelEntity.getCp()),mDelEntity.getId());
				} else if (mDelEntity.getSource() == 2) {

					BlueLightUtils.deleteCOLLECT(CollectActivity.this, mDelEntity.getCpid(),mDelEntity.getId());
				}
				loadCollectData();
			} else {
				ToastUtils.alert(getBaseContext(), getResources().getString(R.string.alert_no_delete));
			}
			DialogUtils.closeDialogCancelCollect();
		}

		@Override
		public void onCancel() {// 关闭对话框
			DialogUtils.closeDialogCancelCollect();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.list_page_down_ll:// 上一页按钮
			// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
			isChangePageByDownButton = true;
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
			break;
		case R.id.list_page_up_ll:// 下一页按钮
			// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
			isChangePageByUpButton = true;
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
			break;
		default:
			break;
		}
	}

	private String TAG = "CollectActivity";

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

			if (has) {
				// mDelEntity = (FilmEntity) view.getTag();

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
			if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {// 响应菜单按键
				DialogUtils.showDialogCancelCollect(CollectActivity.this, dialogListener);
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
			FilmNewEntity entity = (FilmNewEntity) v.getTag();
			//极清2.0
			if (entity.getSource()==1){
				Intent it = new Intent();
				ComponentName componentName = new ComponentName("com.hiveview.bluelight", "com.hiveview.bluelight.activity.BlueLightActivity");
				it.setComponent(componentName);
				it.putExtra("page", "com.hiveview.bluelight.page.DispatchPage");
				it.putExtra("contentId", entity.getId() + "");
				it.putExtra("source", AppConstant.SOURCE_COLLECT);
				it.putExtra("contentType", "2003");
				startActivity(it);
			 }else
			if (entity.getSource()==2){//zhi bo
				Log.v(TAG, "entity.getSource() == 2====111111");
				// 调到极清直播详情页
				try {
					Intent it = new Intent();
					ComponentName componentName = new ComponentName(
							"com.hiveview.bluelight",
							"com.hiveview.bluelight.activity.BlueLightActivity");
					it.setComponent(componentName);
					it.putExtra("page",
							"com.hiveview.bluelight.page.DispatchPage");
					it.putExtra("contentId", entity.getId() + "");
					it.putExtra("cpId", entity.getCpid() + "");
					it.putExtra("contentType", "2007");
					startActivity(it);
				} catch (Exception e) {
					e.printStackTrace();
				}
			 }
			// 点播
			else if (entity.getSource() == 0) {
			String action = ContentInvoker.getInstance().getContentAction(entity.getCid());
			if (null != action && !action.equals("")) {
				Intent intent = new Intent(action);
				intent.putExtra("source", AppConstant.SOURCE_COLLECT);
				if (entity.getId() != 0) {
					intent.putExtra("entity", entity);

					
						startActivity(intent);
					} else {
						intent.putExtra("entity", entity);
						intent.putExtra("request", true);
						startActivity(intent);
					}
				}
			}
			
		}

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// 焦点移走，下次在进入第一页的时候就不是第一次进入的考虑是否要把焦点设置在下一页的按钮上

		isFristCreate = hasFocus;
	}

	/*
	 * 刷新列表的机制 如果判断列表的数量发生了变化的判断不准确 判断列表中的每个元素 效率和需要增加额外的内存开销 (non-Javadoc)
	 * 
	 * @see com.hiveview.tv.activity.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (null != adapter) {
			isDelete = true;
			isResume = true;
			// adapter.clear();
			loadCollectData();
		}
	}

	/**
	 * 获取焦点所在实体类
	 * 
	 * @author zhangpengzhan
	 * 
	 *         2014年4月26日 下午6:33:50
	 */
	public class GetFocusEntitys implements GetFocusEntity {

		public void getFocusEntity(FilmNewEntity mEntity, int entityIndex) {
			// TODO Auto-generated method stub
			mDelEntity = mEntity;
			entityIndexs = entityIndex;
		}

	}

	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if("collect".equals(command)){  
					HomeSwitchTabUtil.closeSiRi(CollectActivity.this, "当前已经在收藏", intent);
				}else
				if ("dialog".equals(command)) {
					//对话框是否已显示
					boolean isShow=DialogUtils.isDialogShow();
					Log.v("collect--onExecute", "isShow="+isShow);
					if (!isEmpty&&!isShow) {
						HomeSwitchTabUtil.closeSiRi(CollectActivity.this, "取消收藏", intent);
						DialogUtils.showDialogCancelCollect(CollectActivity.this, dialogListener);
					}else{
						HomeSwitchTabUtil.closeSiRi(CollectActivity.this, "暂无收藏影片", intent);
					}
				} else if ("yes".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(CollectActivity.this, "是", intent);

					if (null != mDelEntity) {
						if (mDelEntity.getSource() == 0) {
							isDelete = true;
							vLoading.setVisibility(View.VISIBLE);
							// start:修改删除条件,如果videoset_id不为空,则根据其删除,否则根据cp_videoset_id删除，author:huzuwei
							// 如果是综艺，则根据Videoset_id删除，其他根据CP_VIDEOSET_ID删除
							// if (mDelEntity.getVideoset_type() == 6) {
							// daoCollect.delete(CollectVoideoDAO.VIDEOSET_ID + " = ?",
							// new
							// String[] { mDelEntity.getVideoset_id() + "" });
							//
							// } else {
							// daoCollect.delete(CollectVoideoDAO.CP_VIDEOSET_ID +
							// " = ?",
							// new String[] { mDelEntity.getCp_videoset_id() + "" });
							// }
							if (mDelEntity.getId() != 0) {
								daoCollect.delete(CollectVoideoNewDAO.ID + " = ?", new String[] { mDelEntity.getId() + "" });
							}
//							else if (mDelEntity.getId() == 0 && !mDelEntity.getCp_videoset_id().toString().equals("")) {
//								daoCollect.delete(CollectVoideoDAO.CP_VIDEOSET_ID + " = ?", new String[] { mDelEntity.getCp_videoset_id() + "" });
//							}
							// end
							vUpPage.setFocusable(false);
							ToastUtils.alert(CollectActivity.this, getResources().getString(R.string.collect_cancel_text));
							// adapter.clear();
						} else if (mDelEntity.getSource() == 1) {

							BlueLightUtils.deleteCOLLECT(CollectActivity.this, Integer.parseInt(mDelEntity.getCp()),mDelEntity.getId());
						} else if (mDelEntity.getSource() == 2) {

							BlueLightUtils.deleteCOLLECT(CollectActivity.this, mDelEntity.getCpid(),mDelEntity.getId());
						}
						loadCollectData();
					} else {
						ToastUtils.alert(getBaseContext(),  getResources().getString(R.string.alert_no_delete));
					}
					DialogUtils.closeDialogCancelCollect();
				} else if ("no".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(CollectActivity.this, "否", intent);
					DialogUtils.closeDialogCancelCollect();
				} else if ("page".equals(command)) {
					String action = intent.getStringExtra("_action");
					if ("PREV".equals(action)) {

						if (Integer.parseInt(tvCurrentPage.getText().toString()) == 1 || Integer.parseInt(tvCurrentPage.getText().toString()) == 0) {
							HomeSwitchTabUtil.closeSiRi(CollectActivity.this, "您已经在第一页了", intent);
						} else {
							HomeSwitchTabUtil.closeSiRi(CollectActivity.this, "上一页", intent);
							isChangePageByDownButton = true;
							mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
						}
					}

					else if ("NEXT".equals(action)) {
						if (Integer.parseInt(tvCurrentPage.getText().toString()) == pageCount) {
							HomeSwitchTabUtil.closeSiRi(CollectActivity.this, "您已经在最后一页了", intent);
						} else {
							HomeSwitchTabUtil.closeSiRi(CollectActivity.this, "下一页", intent);
							// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
							isChangePageByUpButton = true;
							mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
						}
					}
					// else if("INDEX".equals(action)){
					// int index = intent.getIntExtra("index", -1);
					// HomeSwitchTabUtil
					// .closeSiRi(MovieDemandListActivity.this,
					// "第"+index+"页", intent);
					// // 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
					// mViewPager.setCurrentItem(index);
					//
					// }
				}
			}
		}
	}
	HashMap<String, String[]> commands = new HashMap<String, String[]>();
	// 场景id
	private String scenceId = "com.hiveview.tv.activity.CollectActivity";

	public String onQuery() {
		// TODO 自动生成的方法存根
		commands.put("collect", new String[] { "收藏" });
		commands.put("dialog", new String[] { "取消收藏", "删除收藏" });
		commands.put("yes", new String[] { "是" });
		commands.put("no", new String[] { "否" });
		commands.put("page", new String[] { "$P(_PAGE)" });
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
