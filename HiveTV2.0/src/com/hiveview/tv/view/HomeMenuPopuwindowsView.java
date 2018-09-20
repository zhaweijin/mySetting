/**
 * @Title HomeMenuPopuwindowsView.java
 * @Package com.hiveview.tv.view
 * @author lihongji
 * @date 2014年12月29日 下午5:14:04
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.view;

import java.util.ArrayList;

import com.hiveview.tv.R;
import com.hiveview.tv.R.color;
import com.hiveview.tv.activity.BaseActivity;
import com.hiveview.tv.activity.CollectActivity;
import com.hiveview.tv.activity.HomeActivity;
import com.hiveview.tv.activity.PlayerRecordActivity;
import com.hiveview.tv.activity.SearchHomeActivity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.ItemType;
import com.hiveview.tv.common.statistics.SimpleOnClickListener;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.CloseBlueLightUtil;
import com.hiveview.tv.view.MatrixBluelightView.bluelightSearchClickListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @ClassName: HomeMenuPopuwindowsView
 * @Description:首页菜单view
 * @author: lihongji
 * @date 2014年12月29日 下午5:14:04
 * 
 */
public class HomeMenuPopuwindowsView extends PopupWindow {
	
	private static final String TAG = "HomeMenuPopuwindowsView";
	// 整体view
	private View mMenuView;
	private LinearLayout mMenu;
	// 每个item外部布局
	private LinearLayout search, recoder, collect, app, external, setting;
	// 每个item上图片
	private ImageView menu_image,search_image, recoder_image, collect_image, app_image, external_image, setting_image;
	// 默认图
	private int[] image = { R.drawable.home_search, R.drawable.home_recoder, R.drawable.home_collect, R.drawable.home_app, R.drawable.home_external,
			R.drawable.home_setting };
	// 选中图
	private int[] image1 = { R.drawable.home_search_select, R.drawable.home_recoer_select, R.drawable.home_collext_select,
			R.drawable.home_app_select, R.drawable.home_external_select, R.drawable.home_setting_select };
	private Context mcontext;

	/**
	 * @param @param context
	 */
	public HomeMenuPopuwindowsView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mcontext = context;
		init(context);
	}
/* (non-Javadoc)
 * @see android.widget.PopupWindow#dismiss()
 */
@Override
public void dismiss() {
	HomeActivity.unshowMenuDraw();
	super.dismiss();
}

	/**
	 * @Title: HomeMenuPopuwindowsView
	 * @author:lihongji
	 * @Description:
	 * @param context
	 */
	private void init(Context context) {
		// TODO Auto-generated method stub
		
    IntentFilter filter = new IntentFilter();
        filter.addAction(CloseBlueLightUtil.command_3);
        context.registerReceiver(this.dataReceiver, filter);
		// 获得布局
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.home_popupwindow_left, null);
		// 得到布局中的每个LinearLayout控件
		mMenu = (LinearLayout) mMenuView.findViewById(R.id.home_menu);
		menu_image = (ImageView) mMenuView.findViewById(R.id.home_menu_menu);
		search = (LinearLayout) mMenuView.findViewById(R.id.home_search);
		recoder = (LinearLayout) mMenuView.findViewById(R.id.home_recoder);
		collect = (LinearLayout) mMenuView.findViewById(R.id.home_collect);
		app = (LinearLayout) mMenuView.findViewById(R.id.home_app);
		external = (LinearLayout) mMenuView.findViewById(R.id.home_external);
		setting = (LinearLayout) mMenuView.findViewById(R.id.home_setting);

		final int[] menu_ll = { R.id.home_search, R.id.home_recoder, R.id.home_collect, R.id.home_app, R.id.home_external, R.id.home_setting };
		final int[] menu_img = { R.id.home_search_image, R.id.home_recoder_image, R.id.home_collect_image, R.id.home_app_image,
				R.id.home_external_image, R.id.home_setting_image };
		// 设置焦点监听
		for (int i = 0; i < menu_ll.length; i++) {
			final LinearLayout menu = (LinearLayout) mMenuView.findViewById(menu_ll[i]);
			final ImageView menu_image = (ImageView) mMenuView.findViewById(menu_img[i]);
			final int image_select = image1[i];
			final int image_unselect = image[i];
			menu.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					
					if (hasFocus) {
						// 获得焦点 背景色为橙色，图片为选中图
						Log.v(TAG, "menu-->onFocusChange"+hasFocus);
						menu.setBackgroundResource(R.drawable.navigation_tab_fade_bg);
						menu_image.setImageResource(image_select);

					} else {
						// 未活的焦点 背景色为默认色，图片为默认图
						Log.v(TAG, "menu-->onFocusChange"+hasFocus);
						menu.setBackgroundColor(0);
						menu_image.setImageResource(image_unselect);
					}
				}
			});
			// 设置按键事件
			menu.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub

                    if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {
                        Log.v("onKey", "onKey-->keyCode: false" + keyCode + HomeMenuPopuwindowsView.this.isShowing());
                        // HomeActivity.removeFromHome();
                        HomeMenuPopuwindowsView.this.dismiss();
                        return false;
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) {
                        Log.v("onKey", "onKey-->keyCode: true" + keyCode);
                        return true;
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
                        Log.v("onKey", "onKey-->keyCode: true" + keyCode);
                        return true;
                    } else {
                        Log.v("onKey", "onKey-->keyCode:" + keyCode);
                        return false;
                    }
                }
            });

		}
		
		search.setFocusableInTouchMode(true);
		// 设置点击事件
		search.setOnClickListener(new bluelightSearchClickListener());
		recoder.setOnClickListener(new bluelightRecordClickListener());
		collect.setOnClickListener(new bluelightFavouriteClickListener());
		app.setOnClickListener(new bluelightInstalledClickListener());
		external.setOnClickListener(new bluelightExternalClickListener());
		setting.setOnClickListener(new bluelightSettingClickListener());
		// 设置焦点走向
		search.setNextFocusDownId(R.id.home_recoder);
		recoder.setNextFocusDownId(R.id.home_collect);
		collect.setNextFocusDownId(R.id.home_app);
		app.setNextFocusDownId(R.id.home_external);
		external.setNextFocusDownId(R.id.home_setting);
		setting.setNextFocusDownId(R.id.home_search);

		search.setNextFocusUpId(R.id.home_setting);
		recoder.setNextFocusUpId(R.id.home_search);
		collect.setNextFocusUpId(R.id.home_recoder);
		app.setNextFocusUpId(R.id.home_collect);
		external.setNextFocusUpId(R.id.home_app);
		setting.setNextFocusUpId(R.id.home_external);

		search.setNextFocusLeftId(R.id.home_search);
		recoder.setNextFocusLeftId(R.id.home_recoder);
		collect.setNextFocusLeftId(R.id.home_collect);
		app.setNextFocusLeftId(R.id.home_app);
		external.setNextFocusLeftId(R.id.home_external);
		setting.setNextFocusLeftId(R.id.home_setting);

		search.setNextFocusRightId(R.id.home_search);
		recoder.setNextFocusRightId(R.id.home_recoder);
		collect.setNextFocusRightId(R.id.home_collect);
		app.setNextFocusRightId(R.id.home_app);
		external.setNextFocusRightId(R.id.home_external);
		setting.setNextFocusRightId(R.id.home_setting);
		this.setContentView(mMenuView);
		// 设置PopupWindow弹出窗体的宽
		this.setWidth(287);
		// 设置PopupWindow弹出窗体的高
		this.setHeight(LayoutParams.FILL_PARENT);
		// 设置PopupWindow弹出窗体可点击
		this.setAnimationStyle(R.style.AnimationFade);
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体的背景
		// 默认影片搜索获取焦点
		search.requestFocus();
	}
	
	

	/**
	 * 响应观看记录点击事件 点击后进入
	 */
	class bluelightRecordClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(mcontext, PlayerRecordActivity.class);
			mcontext.startActivity(intent);
			new Handler().postDelayed(new Runnable(){
			    public void run() {
			    //execute the task
			    	dismiss();
			    }
			 }, 1000);
			return new DataHolder.Builder(mcontext).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), mcontext.getResources().getString(R.string.sub_navigation_common_record_text))
					.setSenceName(AppScene.getScene()).setViewPosition("btn01").setSrcType(ItemType.BUTTON).setTabNo(Tab.BULE).build();
		}
	}

	/**
	 * 响应收藏点击事件 点击后进入
	 */
	class bluelightFavouriteClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(mcontext, CollectActivity.class);
			mcontext.startActivity(intent);
			new Handler().postDelayed(new Runnable(){
			    public void run() {
			    //execute the task
			    	dismiss();
			    }
			 }, 1000);
			return new DataHolder.Builder(mcontext).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), mcontext.getResources().getString(R.string.sub_navigation_common_favourite_text))
					.setSenceName(AppScene.getScene()).setViewPosition("btn02").setSrcType(ItemType.BUTTON).setTabNo(Tab.BULE).build();
		}
	}

	/**
	 * 响应已安装应用游戏记录点击事件 点击后进入
	 */
	class bluelightInstalledClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent();
			intent.setAction("com.hiveview.appstore.buy");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			intent.putExtra("category_id", 2);
			mcontext.startActivity(intent);
			new Handler().postDelayed(new Runnable(){
			    public void run() {
			    //execute the task
			    	dismiss();
			    }
			 }, 1000);
			return new DataHolder.Builder(mcontext).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), mcontext.getResources().getString(R.string.sub_navigation_common_installed_text))
					.setSenceName(AppScene.getScene()).setViewPosition("btn03").setSrcType(ItemType.BUTTON).setTabNo(Tab.BULE).build();
		}
	}

	/**
	 * 响应搜索点击事件 点击后进入
	 */
	class bluelightSearchClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {
			if(AppConstant.ISDOMESTIC){
			Intent intent = new Intent("com.hiveview.cloudscreen.search.action.SEARCH");
			intent.putExtra("resultShowType", "vertical");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			mcontext.startActivity(intent);
			}else{
			Intent intent = new Intent(mcontext, SearchHomeActivity.class);
			mcontext.startActivity(intent);
			}
			new Handler().postDelayed(new Runnable(){
			    public void run() {
			    //execute the task
			    	dismiss();
			    }
			 }, 1000);
			return new DataHolder.Builder(mcontext).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), mcontext.getResources().getString(R.string.sub_navigation_common_search_text))
					.setSenceName(AppScene.getScene()).setViewPosition("btn04").setSrcType(ItemType.BUTTON).setTabNo(Tab.BULE).build();
		}
	}

	/**
	 * 响应系统设置点击事件 点击后进入
	 */
	class bluelightSettingClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent();
			intent.setAction("com.hiveview.settings.ACTION_SETTING");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			/* start by ZhaiJianfeng */
			int netStatus = HiveviewApplication.getNetStatus();
			intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
			/* end by ZhaiJianfeng */
			mcontext.startActivity(intent);
			new Handler().postDelayed(new Runnable(){
			    public void run() {
			    //execute the task
			    	dismiss();
			    }
			 }, 1000);
			return new DataHolder.Builder(mcontext).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), mcontext.getResources().getString(R.string.sub_navigation_common_setting_text))
					.setSenceName(AppScene.getScene()).setViewPosition("btn05").setSrcType(ItemType.BUTTON).setTabNo(Tab.BULE).build();
		}
	}

	/**
	 * 响应用户中心点击事件 点击后进入
	 */
	// class bluelightUserClickListener extends SimpleOnClickListener {
	// @Override
	// public DataHolder doOnClick(View view) {
	// Intent intent = new Intent();
	// intent.setAction("com.hiveview.user.usercenter");
	// intent.addCategory(Intent.CATEGORY_DEFAULT);
	// mcontext.startActivity(intent);
	//
	// return new
	// DataHolder.Builder(mcontext).setDataType(DataType.CLICK_TAB_BUTTON)
	// .setButton(String.valueOf(view.getId()),
	// getResources().getString(R.string.sub_navigation_common_user_text))
	// .setSenceName(AppScene.getScene()).setViewPosition("7712").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
	// }
	// }

	/**
	 * 响应外接媒体点击事件 点击后进入
	 */
	class bluelightExternalClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			AppUtil.openApp("com.hiveview.externalstorage", mcontext);
			new Handler().postDelayed(new Runnable(){
			    public void run() {
			    //execute the task
			    	dismiss();
			    }
			 }, 1000);
			return new DataHolder.Builder(mcontext).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), mcontext.getResources().getString(R.string.sub_navigation_common_external_text))
					.setSenceName(AppScene.getScene()).setViewPosition("btn06").setSrcType(ItemType.BUTTON).setTabNo(Tab.BULE).build();
		}
	}

    
    private  BroadcastReceiver dataReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			   if (arg1.getAction().equals(CloseBlueLightUtil.command_3)) {
				   HomeMenuPopuwindowsView.this.dismiss();   
			   }
		}
    	
    };
}
