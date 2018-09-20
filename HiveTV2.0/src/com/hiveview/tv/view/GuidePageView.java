package com.hiveview.tv.view;

import java.lang.ref.PhantomReference;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;

import com.hiveview.tv.R;
import com.hiveview.tv.activity.HomeActivity;
import com.hiveview.tv.common.AppFirstCreatInterface;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.dao.CommonPreferenceDAO;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.utils.LogUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class GuidePageView extends RelativeLayout {

	private static final String TAG = "GuidePageView";
	private ViewPager viewPager = null;
	private View vStart;
	public View getvStart() {
		return vStart;
	}

	public void setvStart(View vStart) {
		this.vStart = vStart;
	}
	private View vSettings;
	private List<View> mListViews = new ArrayList<View>();
	private List<ImageView> dots = new ArrayList<ImageView>();
	private GuidePageCompleteListener completeListener;
	private LinearLayout dotContainer = null;
	private boolean isAnimStarted = false;
	private AppFirstCreatInterface appFirstCreatInterface;
	
	private int mViewPagerState = ViewPager.SCROLL_STATE_IDLE;
	
	ImageView imageView1;
	ImageView imageView2;
	ImageView imageView3;
	ImageView imageView4;

	public GuidePageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public GuidePageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public GuidePageView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		long time = System.currentTimeMillis();
		View vContainer = inflate(context, R.layout.guide_page_layout, null);
		addView(vContainer);
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		dotContainer = (LinearLayout) findViewById(R.id.guide_dot_container);
		for (int i = 0; i < dotContainer.getChildCount(); i++) {
			dots.add((ImageView) dotContainer.getChildAt(i));
		}

		dots.get(0).setImageResource(R.drawable.guide_dot_select);

		changeViewPageScroller();

		// 引导页前5页
		new FoundViewTask(context).execute();
		
		Log.d(TAG, "loadImage GuidePageView::init  " + (System.currentTimeMillis() - time));

	}
	
	private void initAfterFoundView(FoundViewHolder holder) {
		// 引导页前4页
		imageView1 = new ImageView(getContext());
		imageView2 = new ImageView(getContext());
		imageView3 = new ImageView(getContext());
		imageView4 = new ImageView(getContext());
		
		vStart = holder.start;
		vSettings = holder.setting;

		vStart.setNextFocusRightId(R.id.v_gudie_setting);
		vSettings.setNextFocusLeftId(R.id.v_gudie_start);
		// 点击开始体验
		vStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(HomeActivity.isBesTV){
				HomeActivity.OpenBesTV=true;
				HomeActivity.SetBesTV=false;
				}
				saveHasGuideState();
				if (null != completeListener) {
					// 用户观看完引导，回调方法
//					Intent intent = new Intent("com.hiveview.domybox.ACTION_SETTING_TIME_ZONE");
//					getContext().startActivity(intent);
//					Intent intent2 = new Intent("com.hiveview.domybox.ACTION_SETTING_LANGUAGE");
//					getContext().startActivity(intent2);
					completeListener.OnGuidePageComplete();
				}

				// 动画隐藏引导页
				hideGudiePage();
			}
		});

		// 点击设置
		vSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(HomeActivity.isBesTV){
				HomeActivity.OpenBesTV=false;
				HomeActivity.SetBesTV=true;
				}
				GuideSettings();
			}
		});

		imageView1.setScaleType(ScaleType.FIT_XY);
		imageView2.setScaleType(ScaleType.FIT_XY);
		imageView3.setScaleType(ScaleType.FIT_XY);
		imageView4.setScaleType(ScaleType.FIT_XY);
		Log.d(TAG, "first times gc==");
		System.gc();
		new LoadImageTask(imageView1).execute(R.drawable.guide_01);
		new LoadImageTask(imageView2).execute(R.drawable.guide_02);
		new LoadImageTask(imageView3).execute(R.drawable.guide_03);
		new LoadImageTask(imageView4).execute(R.drawable.guide_04);
		
//		imageView1.setImageResource(R.drawable.guide_01);
//		imageView2.setImageResource(R.drawable.guide_02);
//		imageView3.setImageResource(R.drawable.guide_03);
//		imageView4.setImageResource(R.drawable.guide_04);

		if(!HomeActivity.isBesTV){
		mListViews.add(imageView1);
		mListViews.add(imageView2);
		mListViews.add(imageView3);
		mListViews.add(imageView4);
		}else{
			dotContainer.setVisibility(View.INVISIBLE);
		}
		mListViews.add(holder.endGudiePage);

		for (int i = 0; i < mListViews.size(); i++) {
			
			if (i != 4) {
				mListViews.get(i).setFocusable(true);
			} else {
				if(!HomeActivity.isBesTV){
				mListViews.get(4).setFocusable(false);}else{
					mListViews.get(0).setFocusable(false);
					mListViews.get(0).setFocusableInTouchMode(false);
				}

				vStart.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if (mViewPagerState != ViewPager.SCROLL_STATE_IDLE) {
							return true;
						}

						if ((keyCode == KeyEvent.KEYCODE_DPAD_UP|| keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) && event.getAction() == KeyEvent.ACTION_DOWN) {
							return true;
						}

						return false;
					}
				});

				vSettings.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						// || (&& )
						if (mViewPagerState != ViewPager.SCROLL_STATE_IDLE) {
							return true;
						}

						if ((keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
								&& event.getAction() == KeyEvent.ACTION_DOWN) {
							return true;
						}

						return false;
					}
				});
			}

			mListViews.get(i).setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (mViewPagerState != ViewPager.SCROLL_STATE_IDLE) {
						return true;
					}

					return false;
				}
			});
		}

		viewPager.setAdapter(new GuidePagerAdapter());
		
		if(HomeActivity.isBesTV){
			vStart.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					vStart.requestFocus();
					vStart.requestFocusFromTouch();
					//Toast.makeText(getContext(), "==="+vStart.isFocused(), Toast.LENGTH_LONG).show();
				if(!vStart.isFocused()){
					vStart.requestFocus();
				}
				}
			}, 1000);
			
			AppScene.setScene(AppScene.GUIDE_SCENE);
		}
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				setPageDotSelect(index);
				if (index == mListViews.size() - 1) {
					vStart.requestFocus();
					AppScene.setScene(AppScene.GUIDE_SCENE);
				}else{
					AppScene.setScene(AppScene.HOME_SCENE);
				}
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				mViewPagerState = state;
			}
		});
	}
	
	public void GuideSettings(){
		saveHasGuideState();
		// 此时网络没有连接，启动设置apk
		hideGudiePage();
		Intent intent = new Intent();
		intent.setAction("com.hiveview.settings.ACTION_SETTING");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		int netStatus = HiveviewApplication.getNetStatus();
		intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
		getContext().startActivity(intent);
	}
	
	private class LoadImageTask  extends Thread {

		private ImageView iv;
		private Handler handler = new Handler();
		private Integer[] params;
		public LoadImageTask(ImageView iv) {
			this.iv = iv;
		}
		@Override
		public void run() {
			super.run();
			final Drawable drawable = Drawable.createFromStream(getResources().openRawResource(params[0]), "guid_" + params[0]);
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					onPostExecute(drawable);
				}
			});
		}

		protected void onPostExecute(Drawable result) {
			iv.setImageDrawable(result);
			iv.invalidate();
		}
		
		public void execute(Integer...params) {
			this.params = params;
			start();
		}
		
	}
	
	private class FoundViewTask extends Thread {

		private Context context;
		private Handler handler = new Handler();
		public FoundViewTask(Context context) {
			this.context = context;
		}
		@Override
		public void run() {
			View endGudiePage = inflate(context, R.layout.guide_end_page_layout, null);
			vStart = endGudiePage.findViewById(R.id.v_gudie_start);
			vSettings = endGudiePage.findViewById(R.id.v_gudie_setting);
			final FoundViewHolder holder = new FoundViewHolder();
			holder.start = vStart;
			holder.setting = vSettings;
			holder.endGudiePage = endGudiePage;
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					onPostExecute(holder);
				}
			});
			super.run();
		}

		protected void onPostExecute(FoundViewHolder result) {
			initAfterFoundView(result);
		}
		
		public void execute(){
			start();
		}
		
		
	}
	
	private class FoundViewHolder{
		View endGudiePage;
		View start;
		View setting;
	}

	private void saveHasGuideState() {
		HttpTaskManager.getInstance().submit(new Runnable() {

			@Override
			public void run() {
				// 标记为用户已经浏览过引导页
				CommonPreferenceDAO preferenceDAO = new CommonPreferenceDAO(getContext());
				preferenceDAO.setIsShowGudie(true);
			}
		});

	}

	class GuidePagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			container.addView(mListViews.get(position), 0);// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
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
			scroller = new FixedSpeedScroller(getContext(), new AccelerateDecelerateInterpolator());// 匀速翻页
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
		private int mDuration = 900;

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

	public void hideGudiePage() {
		if(isAnimStarted){
			return;
		}
		isAnimStarted = true;
	//	AppScene.setScene(AppScene.HOME_SCENE);
		int duration = 800;
		AnimatorSet set = new AnimatorSet();
		Animator scaleXAnimator = ObjectAnimator.ofFloat(viewPager, "scaleX", 1, 4).setDuration(duration);
		Animator scaleYAnimator = ObjectAnimator.ofFloat(viewPager, "scaleY", 1, 4).setDuration(duration);
		Animator alphaAnimator = ObjectAnimator.ofFloat(viewPager, "alpha", 1f, 0f).setDuration(duration);
		set.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);
		set.start();
		postDelayed(new Runnable(){
			@Override
			public void run() {
				if(null != appFirstCreatInterface){
					appFirstCreatInterface.overView();
				}
			}
		}, duration/2);
		set.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
			
			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				setVisibility(View.GONE);
				recycleImageView();
				
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
	}

	public interface GuidePageCompleteListener {
		public void OnGuidePageComplete();
	}

	public void setGuidePageCompleteListener(GuidePageCompleteListener listener) {
		completeListener = listener;
	}

	private void setPageDotSelect(int selectIndex) {

		for (int i = 0; i < dots.size(); i++) {
			ImageView imageView = dots.get(i);
			imageView.setImageResource(R.drawable.guide_dot_normal);
		}

		dots.get(selectIndex).setImageResource(R.drawable.guide_dot_select);
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if (visibility == View.VISIBLE) {
			viewPager.setFocusableInTouchMode(true);
			viewPager.requestFocus();
		}
	}

	public int getCurrentPageIndex() {
		return viewPager.getCurrentItem();
	}

	public void btnPerformClick(KeyEvent event) {

		if (findFocus() == vStart && KeyEvent.ACTION_DOWN == event.getAction()) {
			vStart.performClick();
		}

		if (findFocus() == vSettings && KeyEvent.ACTION_DOWN == event.getAction()) {
			vSettings.performClick();
		}
	}
	
	public void setButtonFocusTrue() {
		vStart.setFocusable(true);
		vSettings.setFocusable(true);
	}

	/**
	 * @Title: GuidePageView
	 * @author:张鹏展
	 * @Description: 界面消失之后释放内存
	 */
	private void recycleImageView() {
		this.removeAllViews();
		int size = mListViews.size();
		for (int i = 0; i < size-1; i++) {
			ImageView image = (ImageView) mListViews.get(i);
			BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
			Bitmap bmp = drawable.getBitmap();
			if (null != bmp && !bmp.isRecycled()) {
				bmp.recycle();
				
				Log.d(TAG, "=="+i+"==image::"+image+"bmp"+bmp);
				bmp = null;
				Log.d(TAG, "=="+i+"==image::"+image+"bmp"+bmp);
				image = null;
				Log.d(TAG, "=="+i+"==image::"+image+"bmp"+bmp);
				
				
			}
		}
		System.gc();
		requestLayout();
		mListViews.clear();
	}
	
	public void setTheLastPageOnClick(AppFirstCreatInterface appFirstCreatInterface){
		this.appFirstCreatInterface = appFirstCreatInterface;
	}
	@Override
	public boolean dispatchKeyShortcutEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchKeyShortcutEvent(event);
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchKeyEvent(event);
	}
	
}
