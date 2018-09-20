package com.hiveview.tv.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Scroller;
import android.widget.TextView;

import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.tv.R;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.content.CategoryDetailManager;
import com.hiveview.tv.common.content.ChannelInvoker;
import com.hiveview.tv.service.dao.ChannelDAO;
import com.hiveview.tv.service.entity.FirstClassListEntity;
import com.hiveview.tv.view.ChannelBasePageView;
import com.hiveview.tv.view.ChannelCommonPageView;
import com.hiveview.tv.view.ChannelFirstPageView;
import com.hiveview.tv.view.ChannelSecondPageView;
import com.hiveview.tv.view.ProgressDialog;

public class NewChannelActivity extends BaseActivity {

	private View vContainer;
	private ViewPager viewPager = null;
	private TextView tvChannelCount = null;
	private ArrayList<ChannelBasePageView> views = new ArrayList<ChannelBasePageView>();

	private ChannelFirstPageView firstView;
	private ChannelSecondPageView secondView;
	private int oldPageIndex = 0;
	private ArrayList<FirstClassListEntity> channelList = new ArrayList<FirstClassListEntity>();
	private final int LOAD_SUCCESS = 2000;
	private ProgressDialog loadingDialog = null;
	
	private DisplayImageOptions options = new DisplayImageOptions.Builder().resetViewBeforeLoading(false).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel_new_layout);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		vContainer = findViewById(R.id.v_container);
		tvChannelCount = (TextView) findViewById(R.id.tv_video_type_count);
		loadingDialog=(ProgressDialog) findViewById(R.id.pd_list_loading);
		
		loadingDialog.setVisibility(View.VISIBLE);
		viewPager.setVisibility(View.INVISIBLE);
		tvChannelCount.setVisibility(View.INVISIBLE);
		submitRequest(new Runnable() {

			@Override
			public void run() {
				while (channelList.size() == 0) {
					ChannelDAO channelDAO = new ChannelDAO(getApplicationContext());
					channelList = channelDAO.query(null, null, null, null);
					if (channelList.size() > 0) {
						handler.sendEmptyMessage(LOAD_SUCCESS);
						break;
					} else {
						channelList = service.getFirstClassList(getResources().getString(R.string.language));
					}

					if (channelList.size() > 0) {
						channelDAO.delete(null, null);
						channelDAO.insert(channelList);
						CategoryDetailManager.getInstance().init(NewChannelActivity.this);
						handler.sendEmptyMessage(LOAD_SUCCESS);
						break;
					}
				}

			}
		});

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				ChannelBasePageView oldPageView = (ChannelBasePageView) views.get(oldPageIndex);
				ChannelBasePageView newPageView = (ChannelBasePageView) views.get(arg0);
				View focusView = oldPageView.getFocusView();
				if (focusView == oldPageView.getRightUpBorderImageView()) {
					newPageView.getLeftUpBorderImageView().requestFocus();
				}

				if (focusView == oldPageView.getRightDownBorderImageView()) {
					newPageView.getLeftDownBorderImageView().requestFocus();
				}

				if (focusView == oldPageView.getLeftUpBorderImageView()) {
					newPageView.getRightUpBorderImageView().requestFocus();
				}

				if (focusView == oldPageView.getLeftDownBorderImageView()) {
					newPageView.getRightDownBorderImageView().requestFocus();
				}

				oldPageIndex = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				vContainer.invalidate();
				viewPager.invalidate();
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	@Override
	protected void processData(int msgWhat) {
		switch (msgWhat) {
		case LOAD_SUCCESS:
			tvChannelCount.setText(String.format(tvChannelCount.getText().toString(), channelList.size()));
			if (null != channelList && channelList.size() > 0) {
				firstView = new ChannelFirstPageView(this);
				secondView = new ChannelSecondPageView(this);
				views.add(firstView);
				views.add(secondView);
				if (channelList.size() > (firstView.getImageViews().size() + secondView.getImageViews().size())) {
					int val = channelList.size() - firstView.getImageViews().size() - secondView.getImageViews().size();
					int pageSize = 8;
					int pageCount = (int) Math.ceil(val / (double) pageSize);
					for (int i = 0; i < pageCount; i++) {
						ChannelCommonPageView commonView = new ChannelCommonPageView(this);
						views.add(commonView);
					}
				}

				int entityIndex = 0;
				for (int i = 0; i < views.size(); i++) {
					List<View> list = views.get(i).getImageViews();
					List<ViewGroup> layouts = views.get(i).getLayouts();
					views.get(i).setChannelItemClickListener(new ChannelItemClickListener());
					for (int j = 0; j < list.size(); j++) {
						if (entityIndex >= channelList.size())
							break;
						FirstClassListEntity entity = channelList.get(entityIndex);
						ViewGroup group = layouts.get(j);
						group.setVisibility(View.VISIBLE);
						group.setFocusable(true);
						ImageView image = (ImageView) list.get(j);
						image.setScaleType(ScaleType.FIT_XY);
						group.setTag(entity);
						ImageLoader.getInstance().displayImage(entity.getIcon(), image, options);
						entityIndex++;
					}
				}
			}

			viewPager.setOffscreenPageLimit(5);
			//start author:zhangpengzhan 修改两个view之间的空隙
			viewPager.setPageMargin(-15);
			//end
			changeViewPageScroller();
			
			viewPager.setAdapter(new ChannelPageAdapter());
			//start author:zhangpengzhan 防止第二页图片加载不全
			secondView.requestLayout();
			//end
			loadingDialog.setVisibility(View.INVISIBLE);
			viewPager.setVisibility(View.VISIBLE);
			tvChannelCount.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	class ChannelItemClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (null != v.getTag()) {
				FirstClassListEntity entity = (FirstClassListEntity) v.getTag();
				String action = ChannelInvoker.getInstance().getContent(entity.getShow_type());
				if (null != action) {
					Intent intent = new Intent(action);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.putExtra("category_id", entity.getFirstclass_id());
					intent.putExtra("category_name", entity.getFirstclass_name());
					startActivity(intent);
				}
			}
		}

	}

	class ChannelPageAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(views.get(position));

		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(views.get(position));
			return views.get(position);
		}

	}

	/**
	 * 改变ViewPager翻页的速度
	 * 
	 * @Title: HivePreloadViewPager
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	private void changeViewPageScroller() {
		try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			FixedSpeedScroller scroller;
			// LinearInterpolator匀速AccelerateDecelerateInterpolator
			scroller = new FixedSpeedScroller(this, new AccelerateDecelerateInterpolator());// 匀速翻页
			mField.set(viewPager, scroller);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 修正ViewPager翻页滚动的速度
	 * 
	 * @author chenlixiao
	 * 
	 */
	class FixedSpeedScroller extends Scroller {
		private int mDuration = 600;

		public FixedSpeedScroller(Context context) {
			super(context);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration) {
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		public void setmDuration(int time) {
			mDuration = time;
		}

		public int getmDuration() {
			return mDuration;
		}

	}
}
