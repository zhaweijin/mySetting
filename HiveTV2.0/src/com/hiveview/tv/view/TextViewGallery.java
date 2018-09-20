package com.hiveview.tv.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hiveview.tv.R;
import com.hiveview.tv.service.exception.ServiceException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;


/**
 * 电影分类筛选组件<br />
 * <B>使用方法：</B><br />
 * 1、获取对象通过setData(List)设置数据，需要三行，就要调用三次setData(List)<br />
 * 2、通过调用show()方法，显示控件内容<br>
 * 3、点击当前选项的时候，调用getObject方法获取当前元素对应的List的元素
 * @author haozening
 *
 * @param <T>
 */
public class TextViewGallery<T> extends LinearLayout implements OnPageChangeListener, OnFocusChangeListener {
	private int mViewPageScrollState = ViewPager.SCROLL_STATE_IDLE;
	
	private Context mContext;
	private String mField;
	private Class<T> mClazz;
	private int mTextViewTemplate;
	private static final int PAGER_DISPLAY_NUM = 9;
	private static int DISPLAY_WIDTH;
	private static int ITEM_WIDTH;
	private TextViewGallery<T> _this;
	private Map<Integer, List<T>> mData;
	private Map<Integer, T> mResultData;
	private List<ViewPager> mPagerCollection;
	private List<List<View>> viewsCollection;
	
	private final int ARROW_WIDTH = 15;
	private final int ARROW_MARGIN = 15;
	
	private onUserSelectedListener<T> mOnFocusChanged;
	
	@SuppressLint("NewApi")
	public TextViewGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setWillNotDraw(false);
		init(context);
	}

	public TextViewGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
		init(context);
	}

	public TextViewGallery(Context context) {
		super(context);
		setWillNotDraw(false);
		init(context);
	}

	@SuppressLint("UseSparseArrays")
	private void init(Context context){
		this.mContext = context;
		this.setClipChildren(false);
		this.setOrientation(LinearLayout.VERTICAL);
		_this = this;
		if (mData == null){
			mData = new HashMap<Integer, List<T>>();
		} 
		if (mResultData == null){
			mResultData = new HashMap<Integer, T>();
		} 
		if (mPagerCollection == null){
			mPagerCollection = new ArrayList<ViewPager>();
		}
		if(viewsCollection == null){
			viewsCollection = new ArrayList<List<View>>();
		}
	}
	
	public TextViewGallery<T> putData(Integer key, List<T> list) {
		this.mData.put(key, list);
		return this;
	}
	/**
	 * 在View上显示数据方法
	 * @param textViewRes TextView模板资源Id
	 * @param field 要显示的对象的属性，泛型T对应的字段
	 * @param clazz T对象类型
	 */
	public void display(int textViewRes, String field, Class<T> clazz){
		this.mField = field;
		this.mClazz = clazz;
		this.mTextViewTemplate = textViewRes;
		initItemSize();
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
	private void initItemSize() {
			this.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
				@Override
				public boolean onPreDraw() {
					DISPLAY_WIDTH = _this.getWidth() - 2 * (ARROW_WIDTH + ARROW_MARGIN);
					ITEM_WIDTH = DISPLAY_WIDTH / PAGER_DISPLAY_NUM;
					for (Map.Entry<Integer, List<T>> entry : mData.entrySet()) {
						addViewPager(entry.getKey(), entry.getValue());
					}
					TextViewGallery.this.requestFocus();
					TextViewGallery.this.setBackgroundResource(R.drawable.movie_list_viewpager_bg);
					_this.getViewTreeObserver().removeOnPreDrawListener(this);

					return true;
				}
			});
	}
	
	/**
	 * ViewPager
	 * @param list
	 */
	private void addViewPager(Integer key, List<T> list) {
		FrameLayout layout = new FrameLayout(mContext);
		layout.setClipChildren(false);
		
		// 给FrameLayout设置宽高以及重力属性
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout.getLayoutParams();
		if (layoutParams == null) {
			layoutParams = new RelativeLayout.LayoutParams(ITEM_WIDTH,LayoutParams.WRAP_CONTENT);
		} else {
			layoutParams.width = layoutParams.MATCH_PARENT;
			layoutParams.height =layoutParams.WRAP_CONTENT;
		}
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.setLayoutParams(layoutParams);
		layout.setBackgroundColor(Color.parseColor("#1d1f23"));
		// 计算ViewPager默认页位置
		int currentItem = getDefaultItemPosition(list.size());
		// 初始化ViewPager
		final CategoryViewPager pager = new CategoryViewPager(mContext);
		pager.setOffscreenPageLimit(PAGER_DISPLAY_NUM);
		pager.setClipChildren(false);
		pager.setOnPageChangeListener(this);
		pager.setTag(key);
		
		// 初始化List中的TextView，并设置适配器
		List<View> views = initTextView(list);
		List<View> origin_views = new ArrayList<View>();
		origin_views.addAll(views);
		viewsCollection.add(origin_views);
		ViewPagerAdapter vpa = new ViewPagerAdapter(views);
		if (views.size() > 0) {
			changeViewPageScroller(pager);
			pager.setAdapter(vpa);
			// 设置默认页页数 以及默认页文字颜色
			pager.setCurrentItem(currentItem);
			View view = views.get(currentItem);
			TextView tv = (TextView)view.findViewById(R.id.movie_list_category_selected_text);
			tv.setTextColor(Color.parseColor("#FF8B00"));
		}
		// 给FrameLayout添加ViewPager
		layout.addView(pager);
		mPagerCollection.add(pager);
		
		createViewPagerContainer(layout, list.size() > 8);
	}
	
	/**
	 * 
	 * @param frameLayout
	 * @param flag 是否显示箭头
	 */
	private void createViewPagerContainer(FrameLayout frameLayout, boolean flag) {
		// 左箭头
		LinearLayout llLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)llLayout.getLayoutParams();
		if (layoutParams == null) {
			layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 57);
		} else {
			layoutParams.width = LayoutParams.MATCH_PARENT;
			layoutParams.height = 57;
		}
		layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
		llLayout.setLayoutParams(layoutParams);
		
		ImageView left = new ImageView(mContext);
		LinearLayout.LayoutParams leftLayoutParams = (LinearLayout.LayoutParams)left.getLayoutParams();
		if (leftLayoutParams == null) {
			leftLayoutParams = new LinearLayout.LayoutParams(ARROW_WIDTH, LinearLayout.LayoutParams.WRAP_CONTENT);
		} else {
			leftLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
			leftLayoutParams.width = ARROW_WIDTH;
		}
		leftLayoutParams.rightMargin = ARROW_MARGIN;
		leftLayoutParams.gravity = Gravity.CENTER;
		left.setLayoutParams(leftLayoutParams);
		left.setImageResource(R.drawable.btn_arrow_left);
		
		// 右箭头
		ImageView right = new ImageView(mContext);
		LinearLayout.LayoutParams rightLayoutParams = (LinearLayout.LayoutParams)right.getLayoutParams();
		if (rightLayoutParams == null) {
			rightLayoutParams = new LinearLayout.LayoutParams(ARROW_WIDTH,LinearLayout.LayoutParams.WRAP_CONTENT);
		} else {
			rightLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
			rightLayoutParams.width = ARROW_WIDTH;
		}
		rightLayoutParams.leftMargin = ARROW_MARGIN;
		rightLayoutParams.gravity = Gravity.CENTER;
		right.setLayoutParams(rightLayoutParams);
		right.setImageResource(R.drawable.btn_arrow_right);
		
		RelativeLayout container = new RelativeLayout(mContext);
		container.setClipChildren(false);
		LinearLayout.LayoutParams containerLayoutParams = (LinearLayout.LayoutParams)container.getLayoutParams();
		if (containerLayoutParams == null) {
			containerLayoutParams = new LinearLayout.LayoutParams(DISPLAY_WIDTH, 57);
		} else {
			containerLayoutParams.width = DISPLAY_WIDTH;
			containerLayoutParams.height = 57;
		}
		container.setLayoutParams(containerLayoutParams);
		container.addView(frameLayout);
		
		int visibility = flag ? View.VISIBLE : View.INVISIBLE;
		left.setVisibility(visibility);
		right.setVisibility(visibility);
		
		llLayout.addView(left);
		llLayout.addView(container);
		llLayout.addView(right);

		this.addView(llLayout);
	}
	
	/**
	 * 初始化ViewPager的数据
	 * @param list
	 * @return
	 */
	private List<View> initTextView(List<T> list) throws ServiceException{
		List<View> views = new ArrayList<View>();
		for (T t : list) {
			View view = (View) LayoutInflater.from(mContext).inflate(mTextViewTemplate, null);
			TextView tv = (TextView)view.findViewById(R.id.movie_list_category_selected_text);
			tv.setTextColor(Color.parseColor("#FFFFFF"));
			tv.setTextSize(21);
			view.setFocusable(true);
			view.setOnFocusChangeListener(this);
			view.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					ViewPager viewPager = (ViewPager) v.getParent();
					@SuppressWarnings("unchecked")
					ViewPagerAdapter adapter = (ViewPagerAdapter)viewPager.getAdapter();
					List<View> list;
					View tempTv;
					List<View> newList;
					int currentPosition;
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						switch (event.getKeyCode()) {
						case KeyEvent.KEYCODE_DPAD_LEFT:
							//向左获取焦点，循环移动
							list = adapter.getDataSource();
							tempTv = list.get(list.size()-1);
							list.remove(list.size()-1);
							newList = new ArrayList<View>();
							newList.add(tempTv);
							newList.addAll(list);
							adapter.setDataSource(newList);
							if(list.size()<PAGER_DISPLAY_NUM){
								viewPager.setOffscreenPageLimit(list.size());
							}
							adapter.notifyDataSetChanged();
							currentPosition = getDefaultItemPosition(newList.size());
							viewPager.setCurrentItem(currentPosition);
							viewPager.invalidate();
							break;
						case KeyEvent.KEYCODE_DPAD_RIGHT:
							//向右获取焦点，循环移动
							list = adapter.getDataSource();
							tempTv = list.get(0);
							list.remove(0);
							list.add(tempTv);
							adapter.setDataSource(list);
							if(list.size()<PAGER_DISPLAY_NUM){
								viewPager.setOffscreenPageLimit(list.size());
							}
							adapter.notifyDataSetChanged();
							currentPosition = getDefaultItemPosition(list.size());
							viewPager.setCurrentItem(currentPosition);
							viewPager.invalidate();
							break;
						case KeyEvent.KEYCODE_DPAD_DOWN:
						case KeyEvent.KEYCODE_DPAD_UP:
							((ViewPager)v.getParent()).requestDisallowInterceptTouchEvent(true);
							return false;
						case  KeyEvent.KEYCODE_BACK:
							((Activity)mContext).dispatchKeyEvent(event);
							return false;
						}
					}
					return true;
				}
			});
			Object text = null;
			try {
				Field field = mClazz.getDeclaredField(mField);
				field.setAccessible(true);
				text = field.get(t);
			} catch (IllegalAccessException e) {
				throw new ServiceException(e.getMessage());
			} catch (IllegalArgumentException e) {
				throw new ServiceException(e.getMessage());
			} catch (NoSuchFieldException e) {
				throw new ServiceException(e.getMessage());
			}
			if (text != null && text instanceof CharSequence) {
				view.setTag(t);
				tv.setText(text.toString());
				views.add(view);
			}
		}
		return views;
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (mViewPageScrollState == ViewPager.SCROLL_STATE_SETTLING) {
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
	
	private class ViewPagerAdapter extends PagerAdapter {
		private List<View> list;
		
		public ViewPagerAdapter(List<View> list) {
			super();
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}
		
		public List<View> getDataSource(){
			return list;
		}
		
		public void setDataSource(List<View> list){
			this.list = list;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		public View getCurrentItem(int viewPagerCurrentItem) {
			return list.get(viewPagerCurrentItem);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}

		@Override
		public int getItemPosition(Object object) {
			return list.indexOf(object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(list.get(position));
			return list.get(position);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		mViewPageScrollState = arg0;
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		this.invalidate();
	}

	@Override
	public void onPageSelected(int arg0) {
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		ViewPager viewPager = (ViewPager) v.getParent();
		ViewPagerAdapter adapter = (ViewPagerAdapter)viewPager.getAdapter();
		adapter.notifyDataSetChanged();
		if (v.hasFocus()) {
			v.setBackgroundResource(R.drawable.select_list_focus_bg);
			mResultData.put(Integer.parseInt(viewPager.getTag().toString()), (T)v.getTag());
			try{
				mOnFocusChanged.doUserSelected(mResultData);
			}catch(NullPointerException e){
				throw new ServiceException("TextViewGallery必须设置OnFocusChangedListener监听:" + e.getMessage());
			}
		} else {
			TextView tv = (TextView)v.findViewById(R.id.movie_list_category_selected_text);
			tv.setTextColor(Color.parseColor("#FFFFFF"));
			tv.setBackgroundColor(0);
		}
		keepSelectedItemState();
	}
	
	public void keepSelectedItemState(){
		for(ViewPager pager : mPagerCollection){
			int currentPosition =pager.getCurrentItem();
			@SuppressWarnings("unchecked")
			View view =(View)((ViewPagerAdapter)pager.getAdapter()).getCurrentItem(currentPosition);
			TextView tv = (TextView)view.findViewById(R.id.movie_list_category_selected_text);
			tv.setTextColor(Color.parseColor("#FF8B00"));
//			tv.setBackgroundColor(Color.parseColor("#1d1f23"));
		}
	}
	
	public void setOnUserSelectedListener(onUserSelectedListener<T> focusChangedListener) {
		mOnFocusChanged = focusChangedListener;
	}
	
	public interface onUserSelectedListener<T>{
		public void doUserSelected(Map<Integer, T> resultData);
	}
	
	@SuppressWarnings("unchecked")
	public void reset() {
		int i = 0;
		for (ViewPager pager : mPagerCollection) {	
			List<View> views = new ArrayList<View>();
			views.addAll(viewsCollection.get(i++));
			ViewPagerAdapter adapter = new ViewPagerAdapter(views);
			for(View view : views){
				TextView tv = (TextView)view.findViewById(R.id.movie_list_category_selected_text);
				tv.setTextColor(Color.parseColor("#FFFFFF"));
				tv.setBackgroundColor(0);
			}
			pager.removeAllViews();
			pager.setAdapter(adapter);	
			adapter.notifyDataSetChanged();
			
			int currentItemPosition = getDefaultItemPosition(adapter.getCount());
			pager.setCurrentItem(currentItemPosition, true);
			View view = adapter.getCurrentItem(pager.getCurrentItem());
			TextView tv = (TextView)view.findViewById(R.id.movie_list_category_selected_text);
			tv.setTextColor(Color.parseColor("#FF8B00"));
//			tv.setBackgroundColor(Color.parseColor("#1d1f23"));
			
		}
	}
	
	private int getDefaultItemPosition(int listSize) {
		if (listSize <= 0)
			return 0;
		else
			return listSize / 2 - 1 + listSize % 2;
	}
	
	//反射机制   控制 viewpager滑动时间  为800
	private void changeViewPageScroller(ViewPager pager) {
		try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			FixedSpeedScroller scroller;
			scroller = new FixedSpeedScroller(mContext,new AccelerateDecelerateInterpolator());
			mField.set(pager, scroller);
		} catch (Exception e) {
		}

	}

	class FixedSpeedScroller extends Scroller {
		private int mDuration = 0;

		public FixedSpeedScroller(Context context) {
			super(context);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy,
				int duration) {
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

	};
	
}
