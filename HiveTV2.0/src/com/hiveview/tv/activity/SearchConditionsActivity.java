package com.hiveview.tv.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.ThirdListEntity;
import com.hiveview.tv.service.entity.ThirdListResultEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.view.BaseView.CallBackItemViewFocusListener;
import com.hiveview.tv.view.BaseView.CallBackItemViewKeyListener;
import com.hiveview.tv.view.ProgressDialog;
import com.hiveview.tv.view.SearchConditionsCategoryView;
import com.paster.util.JsonUtil;

public class SearchConditionsActivity extends BaseActivity implements CallBackItemViewKeyListener, CallBackItemViewFocusListener {
	/**
	 * 搜索的界面
	 * 
	 * @Fields searchView
	 */
	private View searchView;

	/**
	 * 获取数据成功
	 * 
	 * @Fields GET_SEARCH_DATE_SUCCESS
	 */
	private static final int GET_SEARCH_DATE_SUCCESS = 0x00147;
	/**
	 * 获取数据成功
	 * 
	 * @Fields GET_SEARCH_DATE_SUCCESS
	 */
	private static final int GET_SEARCH_DATE_SUCCESS2 = 0x00149;

	/**
	 * 获取数据失败
	 * 
	 * @Fields GET_SEARCH_DATE_FAIL
	 */
	private static final int GET_SEARCH_DATE_FAIL = 0x00148;

	/**
	 * @Fields searchView 搜索布局的夫控件
	 */
	private LinearLayout searchViewRelat;
	/**
	 * 背景图，中心的深色区域
	 * 
	 * @Fields bgImageCenter
	 */
	private ImageView bgImageCenter;

	/**
	 * 选中的焦点框
	 * 
	 * @Fields focusLines
	 */
	private LinearLayout focusLines;

	/**
	 * 搜寻结果的数据
	 * 
	 * @Fields searchResult
	 */
	private ArrayList<ThirdListEntity> searchResult = new ArrayList<ThirdListEntity>();

	/**
	 * 分类筛选的数据集合
	 * 
	 * @Fields resultEntites
	 */
	ArrayList<ThirdListResultEntity> resultEntites;

	/**
	 * 分类筛选的背景
	 * 
	 * @Fields search_relative
	 */
	private ImageView search_bg;

	/**
	 * 左右图片指示器
	 * 
	 * @Fields search_left
	 */
	private ImageView search_left, search_right;

	/**
	 * 输入搜索和ok,重置
	 * 
	 * @Fields search_ok
	 */
	private LinearLayout search_edit, search_ok, search_resume;
	/**
	 * loading 框
	 * 
	 * @Fields pd
	 */
	private ProgressDialog pd;
	/**
	 * 现实分类信息的夫view
	 * 
	 * @Fields relativeLayout1
	 */
	private RelativeLayout relativeLayout1;

	/**
	 * 分类类型
	 * 
	 * @Fields isOrietation
	 */
	private boolean isOrietation;

	/**
	 * 单片无详情
	 * 
	 * @Fields gridView
	 */
	private GridView gridView;

	/**
	 * error 提示
	 * 
	 * @Fields errorTextView
	 */
	private TextView errorTextView;
	/**
	 * @Fields tag:上次的标签
	 */
	private String tag = null;
	/**
	 * gridview adapter start by huzuwei
	 */
	private NewGrideAdapter adapter;
	private String TAG = "SearchConditionsActivity";

	/**
	 * 浮层当前焦点的位置
	 */
	private int x;
	private int y;
	private int width;
	private int height;
	/**
	 * 是否初次展示
	 */
	private boolean isfirst = true;

	/* end by huzuwei */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		searchView = LayoutInflater.from(getBaseContext()).inflate(R.layout.search_conditions_layout, null);
		setContentView(searchView);
		pd = (ProgressDialog) searchView.findViewById(R.id.pd_list_loading);
		search_edit = (LinearLayout) searchView.findViewById(R.id.search_button3);
		Intent intent = getIntent();
		isOrietation = intent.getBooleanExtra("orientation", true);
		if (isOrietation) {
			init1();
		} else {
			tag = intent.getStringExtra("tag");
			init2();
		}
	}

	/**
	 * 单片无详情
	 * 
	 * @Title: SearchConditionsActivity
	 * @author:张鹏展
	 * @Description:
	 */
	private void init2() {
		gridView = (GridView) searchView.findViewById(R.id.gridView1);
		errorTextView = (TextView) searchView.findViewById(R.id.error_textview);
		errorTextView.setVisibility(View.INVISIBLE);
		// 显示分类信息的父view
		relativeLayout1 = (RelativeLayout) searchView.findViewById(R.id.relativeLayout1);
		relativeLayout1.setVisibility(View.VISIBLE);
		gridView.setVisibility(View.VISIBLE);
		// 拉取数据
		int videoType = getIntent().getIntExtra("videoType", 1);
		submitRequest(new GetThirdClassResult(String.valueOf(videoType)));
		search_edit.setNextFocusDownId(R.id.gridView1);
		search_edit.setOnKeyListener(new EditOnKeyListener());
	}

	/**
	 * 多维度筛选view
	 * 
	 * @Title: SearchConditionsActivity
	 * @author:张鹏展
	 * @Description:
	 */
	private void init1() {
		// 显示分类信息的父view
		relativeLayout1 = (RelativeLayout) searchView.findViewById(R.id.relativeLayout1);
		relativeLayout1.setVisibility(View.VISIBLE);
		// 左右图片指示器
		search_left = (ImageView) searchView.findViewById(R.id.search_left);
		search_left.setVisibility(View.VISIBLE);
		search_right = (ImageView) searchView.findViewById(R.id.search_right);
		search_right.setVisibility(View.VISIBLE);
		// 包含搜索的控件
		searchViewRelat = (LinearLayout) searchView.findViewById(R.id.search_view);
		searchViewRelat.setVisibility(View.INVISIBLE);
		// 中心的深色区域
		bgImageCenter = (ImageView) searchView.findViewById(R.id.image_bg_center);
		bgImageCenter.setVisibility(View.VISIBLE);
		// 选中的焦点框
		focusLines = (LinearLayout) searchView.findViewById(R.id.search_focus_lines);
		focusLines.setVisibility(View.VISIBLE);
		// focusLines.setVisibility(View.INVISIBLE);
		// 输入搜索按钮和确认搜索按钮

		search_resume = (LinearLayout) searchView.findViewById(R.id.search_button2);
		search_resume.setVisibility(View.VISIBLE);

		search_ok = (LinearLayout) searchView.findViewById(R.id.search_button1);
		search_ok.setVisibility(View.VISIBLE);
		// 搜索的背景
		search_bg = (ImageView) searchView.findViewById(R.id.image_bg);
		search_bg.setVisibility(View.VISIBLE);
		int videoType = getIntent().getIntExtra("videoType", 1);
		// 拉取数据
		submitRequest(new GetThirdClassResult(String.valueOf(videoType)));
	}

	/**
	 * @ClassName: EditOnKeyListener
	 * @Description: 输入搜索按钮的key监听事件
	 * @author: zhangpengzhan
	 * @date 2014年10月16日 下午3:04:35
	 * 
	 */
	class EditOnKeyListener implements OnKeyListener {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
				gridView.requestFocus();
				// gridView.getChildAt(0).requestFocus();
				gridView.setSelection(0);
				return true;
			}

			return false;
		}

	}

	private ArrayList<SearchConditionsCategoryView> arrayList = new ArrayList<SearchConditionsCategoryView>();
	ArrayList<HashMap<String, Object>> lstImageItem;
	// 实现一个布局
	SearchConditionsCategoryView scc_view;

	// 接受消息
	protected void processData(int msgWhat) {
		// TODO Auto-generated method stub
		super.processData(msgWhat);
		switch (msgWhat) {
		// 数据获取成功的时候响应的消息
		case GET_SEARCH_DATE_SUCCESS:
			// 显示相关控件
			/* 处理 1136 焦点获取start by huzuwei */
			search_edit.setFocusable(false);
			search_ok.setFocusable(false);
			search_resume.setFocusable(false);

			/* end by huzuwei */
			pd.setVisibility(View.INVISIBLE);
			focusLines.setVisibility(View.VISIBLE);
			relativeLayout1.setVisibility(View.VISIBLE);
			searchViewRelat.setVisibility(View.VISIBLE);
			search_left.setVisibility(View.VISIBLE);
			search_right.setVisibility(View.VISIBLE);
			search_resume.setVisibility(View.VISIBLE);
			search_ok.setVisibility(View.VISIBLE);
			// 搜索菜单的行数
			lines = resultEntites.size();
			// 设置中心深色背景区域的高度，不同的行数高度不同
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(115, 56 * lines);
			rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			rlp.topMargin = 9;
			bgImageCenter.setLayoutParams(rlp);
			bgImageCenter.setFocusable(false);
			// tag 的选中框
			RelativeLayout.LayoutParams rlp_line = new RelativeLayout.LayoutParams(130, 62);
			rlp_line.addRule(RelativeLayout.CENTER_HORIZONTAL);
			focusLines.setLayoutParams(rlp_line);
			// 背景
			RelativeLayout.LayoutParams rlp_bg = new RelativeLayout.LayoutParams(2000, 57 * lines);
			rlp_bg.addRule(RelativeLayout.CENTER_VERTICAL);
			rlp_bg.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			rlp_bg.topMargin = 20;
			rlp_bg.bottomMargin = 17;
			search_bg.setLayoutParams(rlp_bg);
			SearchConditionsCategoryView categoryView = null;

			// 添加控件
			for (int i = 0; i < lines; i++) {
				searchResult.add(i, new ThirdListEntity());
				// 得到分类的list
				List<ThirdListEntity> list = resultEntites.get(i).getClassThirdList();

				for (int j = 0; j < list.size(); j++) {
					if (list.get(j).getThirdClassName().contains(getResources().getString(R.string.list_top_category))) {
						list.remove(j);
					}
				}
				if (list.size() == 0)
					continue;
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(10000, 59);
				lp.gravity = Gravity.CENTER_HORIZONTAL;
				lp.topMargin = 1;
				// 计算中心位置
				int viewIndex = list.size() % 2 == 0 ? (list.size() / 2) : ((list.size() - 1) / 2);
				scc_view = new SearchConditionsCategoryView(getBaseContext(), list);
				scc_view.setId(i);
				// 设置他行数id
				scc_view.setLineIndex(i);
				// 设置中心view 的索引
				scc_view.setFirstViewIndex(viewIndex);
				if (i == 0) {
					scc_view.requestFirstViewFocus();
					scc_view.setUpViewRequest(search_edit);
				}
				if (i == lines - 1) {
					scc_view.setDownViewRequest(search_ok);
				}
				// 设置view 的监听
				scc_view.setCallBackKeyListener(this);
				scc_view.setCallBackFocusListener(this);
				// 添加到当前的布局中
				searchViewRelat.addView(scc_view, lp);
				searchViewRelat.requestLayout();
				arrayList.add(scc_view);
			}

			break;
		// 数据失败的时候响应的消息
		case GET_SEARCH_DATE_FAIL:

			pd.setVisibility(View.INVISIBLE);
			// 根据对应的参数隐藏信息
			if (isOrietation) {// 多维度筛选
				focusLines.setVisibility(View.INVISIBLE);
				relativeLayout1.setVisibility(View.INVISIBLE);
				searchViewRelat.setVisibility(View.INVISIBLE);
				search_left.setVisibility(View.INVISIBLE);
				search_right.setVisibility(View.INVISIBLE);
				search_ok.setVisibility(View.INVISIBLE);
				search_resume.setVisibility(View.INVISIBLE);
				RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				rllp.addRule(RelativeLayout.CENTER_IN_PARENT);
				search_edit.setLayoutParams(rllp);

			} else {
				gridView.setVisibility(View.INVISIBLE);
				errorTextView.setVisibility(View.VISIBLE);
				relativeLayout1.setVisibility(View.INVISIBLE);
//				search_left.setVisibility(View.INVISIBLE);
//				search_right.setVisibility(View.INVISIBLE);

			}
			break;
		case GET_SEARCH_DATE_SUCCESS2:// 单片无详情的搜索

			// 显示相关控件
			pd.setVisibility(View.INVISIBLE);
			gridView.setVisibility(View.VISIBLE);
			errorTextView.setVisibility(View.INVISIBLE);
			// 生成动态数组，并且转入数据
			// 去除重复的item
			Set set = new HashSet();
			int all = resultEntites.size();

			for (int i = 0; i < all; i++) {
				// 得到分类的list
				List<ThirdListEntity> list = resultEntites.get(i).getClassThirdList();
				for (ThirdListEntity entites : list) {
					// 跳过全部 这个标签，在最后的时候会把全部的标签添加到第一个item
					if (getResources().getString(R.string.list_top_category).equals(entites.getThirdClassName()))
						continue;
					HashMap<String, Object> map1 = new HashMap<String, Object>();
					map1.put("ItemName", entites.getThirdClassName());// 按序号做
					map1.put("ItemId", entites.getThirdClassId());// 按序号做
					set.add(map1);

				}
				Log.i(TAG, "---set:" + set);
			}
			lstImageItem = new ArrayList<HashMap<String, Object>>(set);
			/*去除重复的数据*/
			for(int ii=0;ii<lstImageItem.size()-1;ii++){
				for(int kk = ii+1;kk<lstImageItem.size();kk++){
					if(lstImageItem.get(ii).get("ItemName").equals(lstImageItem.get(kk).get("ItemName")))
					{
						lstImageItem.remove(kk);
						kk--;
					}
				}
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemName", getResources().getString(R.string.list_top_category));// 按序号做
			map.put("ItemId", "");// 按序号做
			lstImageItem.add(0, map);
			Log.i(TAG, "---lstImageItem2:" + lstImageItem);
			// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
			SimpleAdapter saImageItems = new SimpleAdapter(SearchConditionsActivity.this, // 没什么解释
					lstImageItem,// 数据来源
					R.layout.searchconditionstagview,// night_item的XML实现

					// 动态数组与ImageItem对应的子项
					new String[] { "ItemName" },

					// XML文件里面的一个ImageView,两个TextView ID
					new int[] { R.id.textView1 });
			saImageItems.notifyDataSetChanged();
			// 添加并且显示
			adapter = new NewGrideAdapter(lstImageItem);
			gridView.setAdapter(adapter);
			// adapter.clearData();
			// 添加消息处理
			gridView.setOnItemClickListener(new ItemClickListener());
			gridView.requestFocus();
			// gridView.getChildAt(0).requestFocus();
			gridView.setSelection(0);
			break;
		default:
			break;
		}

	}

	/**
	 * 搜索的监听
	 * 
	 * @Title: MovieDemandListActivity
	 * @author:张鹏展
	 * @Description:
	 * @param v
	 */
	public void searchBtn(View v) {
		switch (v.getId()) {
		case R.id.search_button1:
			// 发送数据
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			StringBuffer sb1 = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();
			// 拼接字符串
			for (ThirdListEntity s : searchResult) {
				// 0 全部的标签是客户端自己添加的排除
				String id = String.valueOf(s.getThirdClassId());
				if (!"0".equals(id)) {
					sb1.append(id);
					sb1.append(",");

				}
				sb2.append(TextUtils.isEmpty(s.getThirdClassName()) ? "" : s.getThirdClassName());
				sb2.append("  ");
			}
			// 去除最后一个逗号
			String result = sb1.substring(0, (sb1.length() - 1) >= 0 ? sb1.length() - 1 : 0).toString();
			Log.d("SearchConditionActivtiy==>searchBtn::", "reault::" + result);
			Log.d("SearchConditionActivtiy==>searchBtn::", "sb2.toString()::" + sb2.toString());
			bundle.putString("Result", result);
			bundle.putString("ResultName", sb2.toString());
			intent.putExtras(bundle);
			SearchConditionsActivity.this.setResult(AppConstant.SEARCH_DATE_RESULT, intent);
			SearchConditionsActivity.this.finish();
			break;
		case R.id.search_button2:
			// 复位全部控件
			for (int i = 0; i < searchViewRelat.getChildCount(); i++) {
				SearchConditionsCategoryView scc_view = (SearchConditionsCategoryView) searchViewRelat.getChildAt(i);
				scc_view.setResetViewIndex();
				Log.d("SearchConditionActivity==>searchBtn::", "iii_" + i);
				if (i == 0) {
					scc_view.requestFirstViewFocus();
				}
			}
			break;
		case R.id.search_button3:
			// 输入筛选
			if(AppConstant.ISDOMESTIC){
				Intent intent3 = new Intent("com.hiveview.cloudscreen.search.action.SEARCH");
				intent3.putExtra("resultShowType", "vertical");
				intent3.addCategory(Intent.CATEGORY_DEFAULT);
				intent3.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent3);	
			}else {
			Intent intent3 = new Intent();
			intent3.setClass(this, SearchHomeActivity.class);
			startActivity(intent3);
			}
			this.finish();
			break;
		}
	}

	/**
	 * 得到分类搜索的类型
	 * 
	 * @ClassName: GetThirdClassResult
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年7月4日 上午10:48:10
	 * 
	 */
	class GetThirdClassResult extends SafeRunnable {
		/**
		 * video 类型
		 * 
		 * @Fields videoType
		 */
		private String videoType;

		public GetThirdClassResult(String videoType) {
			this.videoType = videoType;
		}

		@Override
		public void requestData() {
			// TODO Auto-generated method stub
			HiveTVService service = new HiveTVService();
			resultEntites = (ArrayList<ThirdListResultEntity>) service.getThirdClassResultEntites(getBaseContext(), videoType);
			if (null != resultEntites && resultEntites.size() != 0 && resultEntites.get(0).getClassThirdList().size() != 0) {
				if (isOrietation) {
					handler.sendEmptyMessage(GET_SEARCH_DATE_SUCCESS);
				} else {
					handler.sendEmptyMessage(GET_SEARCH_DATE_SUCCESS2);
				}
			} else {
				handler.sendEmptyMessage(GET_SEARCH_DATE_FAIL);
			}
			for (int i = 0; i < resultEntites.size(); i++) {
				Log.d("TextActivity==>resultEntites.seize::", resultEntites.get(i).toString() + "");
			}
			Log.d("TextActivity==>resultEntites.seize::", resultEntites.size() + "");
		}

		@Override
		public void processServiceException(ServiceException e) {
			showErrorDialog(e.getErrorCode(), true);

		}

	}

	/**
	 * 当前行数
	 */
	private int line;
	/**
	 * 总行数
	 */
	private int lines;

	/*
	 * view 上的按键事件 (non-Javadoc) none 不显示
	 * 
	 * @see com.hiveview.tv.view.BaseView.CallBackItemViewKeyListener#onKey(int,
	 * android.view.View, int, android.view.KeyEvent)
	 */
	public boolean onKey(final int lines, View v, int keyCode, KeyEvent event) {
		/* 处理hv-1136 焦点获取start by huzuwei */
		search_edit.setFocusable(true);
		search_ok.setFocusable(true);
		search_resume.setFocusable(true);
		line = lines;
		/* end by huzuwei */
		Log.d("textActivity===>", "view::" + ((ThirdListEntity) v.getTag()).getThirdClassName());

		Log.d("SearchCondition===>onCalBackClick::", "name0::" + ((ThirdListEntity) v.getTag()).getThirdClassName());
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		x = location[0];
		y = location[1];
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();

		LogUtil.info("x" + x + "y" + y + "width" + width + "height" + height);

		// 添加数据到 搜索标签的列表
		if (null != v.getTag()) {
			// 防止重复添加
			searchResult.remove(lines);
			ThirdListEntity thirdListEntity = new ThirdListEntity();
			thirdListEntity.setThirdClassName("");
			LogUtil.info("lines-->" + lines);
			searchResult.add(lines, (ThirdListEntity) (null == v.getTag() ? thirdListEntity : v.getTag()));
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (lines < arrayList.size() - 1) {
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						arrayList.get(lines + 1).requestFirstViewFocus(true);
					}
				}, 1);
				return true;
			}
		}
		return false;
	}

	/*
	 * 按键监听 (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int,
	 * android.view.KeyEvent)
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCallBackFocusChange(int lines, View arg0, boolean has) {
		// TODO Auto-generated method stub
		if (has) {
			focusLines.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams rlp_line = new RelativeLayout.LayoutParams(130, 60);
			rlp_line.topMargin = lines * 60;
			rlp_line.addRule(RelativeLayout.CENTER_HORIZONTAL);
			focusLines.setLayoutParams(rlp_line);
		} else {
			focusLines.setVisibility(View.INVISIBLE);
		}
	}

	// 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0,// The AdapterView where the
													// click happened
				View arg1,// The view within the AdapterView that was clicked
				int arg2,// The position of the view in the adapter
				long arg3// The row id of the item that was clicked
		) {
			// 在本例中arg2=arg3
			HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
			TextView tv = (TextView) arg1.findViewById(R.id.textView1);
			// tv.setTextColor(0xffFFE890);
			/* start by huzuwei */
			tv.setTextColor(Color.parseColor("#FF8B00"));
			/* end by huzuwei */
			// 显示所选Item的ItemText
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			StringBuffer sb1 = new StringBuffer();
			Log.d("SearchConditionActivtiy==>searchBtn::", "reault::" + sb1.toString());
			bundle.putString("Result", String.valueOf(item.get("ItemId")));
			bundle.putString("ResultName", String.valueOf(item.get("ItemName")));
			intent.putExtras(bundle);
			SearchConditionsActivity.this.setResult(AppConstant.SEARCH_DATE_RESULT, intent);
			SearchConditionsActivity.this.finish();

		}
	}

	/**
	 * grideview 的适配器
	 * 
	 * @ClassName: GrideAdapter
	 * @Description: TODO
	 * @author: zhangpengzhan
	 * @date 2014年8月20日 上午11:23:45
	 * 
	 */
	class GrideAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> lstImageItem;

		public GrideAdapter(ArrayList<HashMap<String, Object>> lstImageItem) {
			this.lstImageItem = lstImageItem;
		}

		public int getCount() {
			return lstImageItem.size();
		}

		public Object getItem(int position) {
			return lstImageItem.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView || null == convertView.getTag()) {
				convertView = null == convertView ? LayoutInflater.from(getBaseContext()).inflate(R.layout.searchconditionstagview, null)
						: convertView;
				AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(130, 60);
				convertView.setLayoutParams(layoutParams);
				HashMap<String, Object> hashMap = lstImageItem.get(position);
				convertView.setTag(hashMap);
				RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(130, 60);
				layoutParams2.addRule(Gravity.CENTER);
				TextView tv = (TextView) convertView.findViewById(R.id.textView1);
				tv.setLayoutParams(layoutParams2);
				tv.setText(TextUtils.isEmpty(hashMap.get("ItemName").toString()) ? "" : hashMap.get("ItemName").toString());
				Log.i(TAG, "--convertview=null--tv.getText():" + tv.getText());
				if ((TextUtils.isEmpty(hashMap.get("ItemName").toString()) ? "" : hashMap.get("ItemName").toString()).equals(tag)) {
					// tv.setTextColor(0xffFFE890);
					/* start by huzuwei */
					tv.setTextColor(Color.parseColor("#FF8B00"));
					/* end by huzuwei */
				} else {
					tv.setTextColor(0xffffffff);
				}
			} else {
				HashMap<String, Object> hashMap = (HashMap<String, Object>) convertView.getTag();
				TextView tv = (TextView) convertView.findViewById(R.id.textView1);
				tv.setText(TextUtils.isEmpty(hashMap.get("ItemName").toString()) ? "" : hashMap.get("ItemName").toString());
				Log.i(TAG, "==convertview!=null==tv.getText():" + tv.getText());
				if ((TextUtils.isEmpty(hashMap.get("ItemName").toString()) ? "" : hashMap.get("ItemName").toString()).equals(tag)) {
					// tv.setTextColor(0xffFFE890);
					/* start by huzuwei */
					tv.setTextColor(Color.parseColor("#FF8B00"));
					/* end by huzuwei */
				} else {
					tv.setTextColor(0xffffffff);
				}
			}
			return convertView;
		}

	}

	/**
	 * 
	 * @ClassName: NewGrideAdapter
	 * @Description: getview中方法重新定义，避免多次加载造成多次创建view,造成大量的缓存
	 * @author: huzuwei
	 * @date 2014年8月29日 下午4:54:20
	 * 
	 */
	class NewGrideAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> lstImageItem;

		public NewGrideAdapter(ArrayList<HashMap<String, Object>> lstImageItem) {
			this.lstImageItem = lstImageItem;
		}

		public void clearData() {
			if (lstImageItem != null) {
				lstImageItem.clear();
			}
		}

		public int getCount() {
			return lstImageItem.size();
		}

		public Object getItem(int position) {
			return lstImageItem.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			View view;

			if (null == convertView) {
				// convertView = null == convertView ?
				// LayoutInflater.from(getBaseContext()).inflate(R.layout.searchconditionstagview,
				// null)
				// : convertView;
				view = LayoutInflater.from(getBaseContext()).inflate(R.layout.searchconditionstagview, parent, false);
				holder = new ViewHolder();

				AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(130, 60);
				view.setLayoutParams(layoutParams);

				holder.tv = (TextView) view.findViewById(R.id.textView1);
				view.setTag(holder);

			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}

			HashMap<String, Object> hashMap = lstImageItem.get(position);
			RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(130, 60);
			layoutParams2.addRule(Gravity.CENTER);
			holder.tv.setLayoutParams(layoutParams2);
			holder.tv.setText(TextUtils.isEmpty(hashMap.get("ItemName").toString()) ? "" : hashMap.get("ItemName").toString());
			Log.i(TAG, "--convertview=null--tv.getText():" + holder.tv.getText());
			if ((TextUtils.isEmpty(hashMap.get("ItemName").toString()) ? "" : hashMap.get("ItemName").toString()).equals(tag)) {
				// tv.setTextColor(0xffFFE890);
				/* start by huzuwei */
				holder.tv.setTextColor(Color.parseColor("#FF8B00"));
				/* end by huzuwei */
			} else {
				holder.tv.setTextColor(0xffffffff);
			}
			return view;
		}

	}

	class ViewHolder {
		public TextView tv;

	}

	/* end by huzuwei */

	private List<String> region;
	private HashMap<String, String> regionMap;
	private int regionsize;

	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		super.onExecute(intent);
		if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if ("affirm".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(SearchConditionsActivity.this, "确认筛选", intent);
					// 发送数据
					Intent intent1 = new Intent();
					Bundle bundle = new Bundle();
					StringBuffer sb1 = new StringBuffer();
					StringBuffer sb2 = new StringBuffer();
					// LogUtil.info("searchResult-->" +
					// searchResult.toString());
					// 拼接字符串
					for (ThirdListEntity s : searchResult) {
						// 0 全部的标签是客户端自己添加的排除
						String id = String.valueOf(s.getThirdClassId());
						if (!"0".equals(id)) {
							sb1.append(id);
							sb1.append(",");

						}
						sb2.append(TextUtils.isEmpty(s.getThirdClassName()) ? "" : s.getThirdClassName());
						sb2.append("  ");
					}
					// 去除最后一个逗号
					String result = sb1.substring(0, (sb1.length() - 1) >= 0 ? sb1.length() - 1 : 0).toString();

					bundle.putString("Result", result);
					bundle.putString("ResultName", sb2.toString());
					Log.d("SearchConditionActivtiy==>searchBtn::", "reault::" + result);
					Log.d("SearchConditionActivtiy==>searchBtn::", "reaultName::" + sb2.toString());
					intent1.putExtras(bundle);
					SearchConditionsActivity.this.setResult(AppConstant.SEARCH_DATE_RESULT, intent1);
					SearchConditionsActivity.this.finish();
				} else if ("reset".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(SearchConditionsActivity.this, "复位重选", intent);
					// 复位全部控件
					for (int i = 0; i < searchViewRelat.getChildCount(); i++) {
						SearchConditionsCategoryView scc_view = (SearchConditionsCategoryView) searchViewRelat.getChildAt(i);
						scc_view.setResetViewIndex();
						Log.d("SearchConditionActivity==>searchBtn::", "iii_" + i);
					}
				} else if ("search".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(SearchConditionsActivity.this, "搜索", intent);
					// 输入筛选
					Intent intent3 = new Intent();
					intent3.setClass(this, SearchHomeActivity.class);
					startActivity(intent3);
					this.finish();
				} else // 竖图
				if (isOrietation) {
					region = new ArrayList<String>();
					regionMap = new HashMap<String, String>();

					List<ThirdListEntity> listone = new ArrayList<ThirdListEntity>();
					if (resultEntites != null) {
						listone = resultEntites.get(line).getClassThirdList();
						for (int i = 0; i < resultEntites.get(line).getClassThirdList().size(); i++) {
							if (resultEntites.get(line).getClassThirdList().get(i).getThirdClassName() != null
									&& resultEntites.get(line).getClassThirdList().get(i).getThirdClassName() != "none") {
								region.add(resultEntites.get(line).getClassThirdList().get(i).getThirdClassName());
							}
						}
						regionsize = region.size() % 2 == 0 ? (region.size() / 2 - 1) : ((region.size() - 1) / 2);
						for (int i = 0; i < region.size(); i++) {
							regionMap.put(i + "", region.get(i));
						}
					}

					if (listone != null && listone.size() != 0) {
						for (int i = 0; i < listone.size(); i++) {
							if (listone.get(i).getThirdClassName().equals(command)) {
								HomeSwitchTabUtil.closeSiRi(SearchConditionsActivity.this, listone.get(i).getThirdClassName(), intent);
								for (String key : regionMap.keySet()) {
									if (regionMap.get(key).equals(command)) {
										SearchConditionsCategoryView scc_view = (SearchConditionsCategoryView) searchViewRelat.getChildAt(line);
										scc_view.moveTo(Integer.parseInt(key), line, regionsize);
									}
								}
							}
						}
					}

				}// 横图
				else {
					for (int i = 0; i < lstImageItem.size(); i++) {
						String com = (String) lstImageItem.get(i).get("ItemName");
						if (com.equals(command)) {
							HomeSwitchTabUtil.closeSiRi(SearchConditionsActivity.this, String.valueOf(lstImageItem.get(i).get("ItemName")), intent);

							Intent intent1 = new Intent();
							Bundle bundle = new Bundle();
							StringBuffer sb1 = new StringBuffer();
							Log.d("SearchConditionActivtiy==>searchBtn::", "reault::" + lstImageItem.get(i).get("ItemName"));
							bundle.putString("Result", String.valueOf(lstImageItem.get(i).get("ItemId")));
							bundle.putString("ResultName", String.valueOf(lstImageItem.get(i).get("ItemName")));
							intent1.putExtras(bundle);
							SearchConditionsActivity.this.setResult(AppConstant.SEARCH_DATE_RESULT, intent1);
							SearchConditionsActivity.this.finish();

						}
					}

				}

			}

		}
	}

	public HashMap<String, String[]> setCommands(String key, String vaule) {
		// 固定词
		commands.put(key, new String[] { new StringBuffer().append(vaule).toString() });
		return commands;
	}

	HashMap<String, String[]> commands;
	// 场景id
	private String scenceId = "com.hiveview.tv.activity.SearchConditionsActivity";

	public String onQuery() {
		// TODO 自动生成的方法存根
		commands = new HashMap<String, String[]>();
		commands.put("affirm", new String[] { "确认筛选" });
		commands.put("reset", new String[] { "复位重选" });
		commands.put("search", new String[] { "输入进行搜索", "搜索" });
		commands.put("up", new String[] { "上", "向上", "往上" });
		commands.put("down", new String[] { "下", "向下", "往下" });
		commands.put("left", new String[] { "左", "向左", "往左" });
		commands.put("rigth", new String[] { "右", "向右", "往右" });
		commands.put("center", new String[] { "确定" });
		commands.put("back", new String[] { "返回" });
		commands.put("home", new String[] { "主页", "home" });
		commands.put("menus", new String[] { "菜单" });
		// commands.put("power", new String[] { "关机","关闭","关盒子","关闭盒子" });
		commands.put("boot", new String[] { "重启", "重新启动", "重启盒子" });
		// 竖图
		if (isOrietation) {

			List<ThirdListEntity> list = new ArrayList<ThirdListEntity>();
			if (resultEntites != null) {
				list = resultEntites.get(line).getClassThirdList();
			}
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					setCommands(list.get(i).getThirdClassName(), list.get(i).getThirdClassName());
				}

			}

		}
		// 横图
		else {
			if (lstImageItem != null) {
				for (int i = 0; i < lstImageItem.size(); i++) {

					setCommands((String) lstImageItem.get(i).get("ItemName"), (String) lstImageItem.get(i).get("ItemName"));
				}
			}
		}
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
