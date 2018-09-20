package com.hiveview.cloudtv.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

import com.hiveview.cloudtv.settings.imageprofile.ImageEntity;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView.FocusViewAnimatorEndListener;

public class ImageActivity extends Activity {

	private Context context = this;
	private List<ImageEntity> list = new ArrayList<ImageEntity>();
	private ImageAdapter mAdapter = null;
	private ListView mListView = null;
	private View mCurrentView = null;
	private LauncherFocusView focusView;
	public boolean bfocusViewInitStatus = true;
	private int mCurKeycode = KeyEvent.KEYCODE_0;
	private VideoView mVideoView;
	private int currentTime;
	private int musicDuration;
	private int safeback;
	/*
	 * changed by hyl
	 private Tv tv;
	 private SourceInput_Type source;
	*/

	private int is_save = 1;
	private final int mColorTemperature = 0;
	private final int mBrightness = 1;
	private final int mContrast = 2;
	private final int mSaturation = 3;
	private final int mHue = 4;
	private final int mSharpness = 5;
	private final int mBacklight = 6;
	private boolean isfirst = true;
	private final int reduceTime = 1000;
	public Timer timer = new Timer();
	public TimerTask timerTask;
	private TextView mTextViewName = null;
	private SeekBar mSeekBar = null;
	private TextView mTextViewSettings = null;
	private boolean mTextColorChangeFlag = false;
	private boolean mFocusAnimationEndFlag = false;
	private int mItemListCurPosition = -1;
	private int mPosition = 0;
	private int mDivide = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		TvInit(context);
		DataInit(context);
		VideoInit(context);

		mListView = (ListView) findViewById(R.id.mylistview);
		setAdapter();
		mListView.setOnItemSelectedListener(mOnItemSelectedListener);
		mListView.setOnKeyListener(mOnKeyListener);
		mListView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		mListView.requestFocus();

	}

	private void listTextColorSet() {
		if (mFocusAnimationEndFlag && mTextColorChangeFlag) {
			if (mTextViewName != null) {
				mTextViewName.setTextColor(this.getResources().getColor(
						R.color.white));
			}
			if (mTextViewSettings != null) {
				mTextViewSettings.setTextColor(this.getResources().getColor(
						R.color.white));
			}
			if (mSeekBar != null) {
				mSeekBar.setVisibility(View.VISIBLE);
			}

			mTextColorChangeFlag = false;
		}
	}

	private void TvInit(Context context) {
		/*
		 * changed by hyl
		 tv = Tv.open();
		 source = tv.GetCurrentSourceInputType();
		 */

	}

	@SuppressWarnings("null")
	private void DataInit(Context context) {
		focusView = (LauncherFocusView) findViewById(R.id.activity_common_focusview);
		focusView.setAnimatorEndListener(new FocusViewAnimatorEndListener() {

			@Override
			public void OnAnimateStart(View currentFocusView) {
				// TODO Auto-generated method stub
				mFocusAnimationEndFlag = false;
				Log.i("YQBBANG", "----OnAnimateStart----"
						+ "mTextColorChangeFlag==" + mTextColorChangeFlag
						+ "----mFocusAnimationEndFlag=="
						+ mFocusAnimationEndFlag);
			}

			@Override
			public void OnAnimateEnd(View currentFocusView) {
				// TODO Auto-generated method stub
				Log.i("YQBBANG", "----OnAnimateEnd----"
						+ "mTextColorChangeFlag==" + mTextColorChangeFlag
						+ "----mFocusAnimationEndFlag=="
						+ mFocusAnimationEndFlag);
				boolean a = mFocusAnimationEndFlag;
				mFocusAnimationEndFlag = true;
				listTextColorSet();
				// fixed the keyboard repeat mode
				if (!mTextColorChangeFlag) {
					if ((mItemListCurPosition == 0 || mItemListCurPosition == mListView
							.getCount() - 1)) {
						mTextColorChangeFlag = true;
						listTextColorSet();
					}
					/*
					 * if (a == false) { mFocusAnimationEndFlag = true;
					 * mTextColorChangeFlag = true; listTextColorSet(); }
					 */
				}
			}

		});
		String name[] = context.getResources().getStringArray(
				R.array.image_profile_array);
		int value[] = new int[7];
		int maxValue[] = new int[7];

		/*
		 * changed by hyl

		value[0] = tv.GetColorTemperature(source);
		value[1] = tv.GetBrightness(source);
		value[2] = tv.GetContrast(source);
		value[3] = tv.GetSaturation(source);
		value[4] = tv.GetHue(source);
		value[5] = tv.GetSharpness(source);
		value[6] = tv.GetBacklight(source);
		*/
		value[0] = 0;
		value[1] = 0;
		value[2] = 0;
		value[3] = 0;
		value[4] = 0;
		value[5] = 0;
		value[6] = 0;

		maxValue[0] = 3;
		maxValue[1] = 100;
		maxValue[2] = 100;
		maxValue[3] = 100;
		maxValue[4] = 100;
		maxValue[5] = 100;
		maxValue[6] = 100;

		for (int i = 0; i < name.length; i++) {
			ImageEntity entity = new ImageEntity();
			entity.setNameString(name[i]);
			entity.setValueString(String.valueOf(value[i]));
			entity.setMaxValue(maxValue[i]);
			list.add(entity);
		}
	}

	private void VideoInit(Context context) {
		safeback = 0;
		mVideoView = (VideoView) findViewById(R.id.myVideoView);
	/*	Uri mUri = Uri.parse("android.resource://" + getPackageName() + "/"
				+ R.raw.backmovie);
		mVideoView.setVideoURI(mUri);*/
		mVideoView.start();
		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				mVideoView.start();
				currentTime = 0;
				musicDuration = mVideoView.getDuration();
				// handler.sendEmptyMessage(1);
				timeSetter(musicDuration - reduceTime);
			}
		});
	}

	private void timeSetter(int time) {
		timerTask = new TimerTask() {

			@Override
			public void run() {
				Message m = new Message();
				m.what = 1;
				handler.sendMessage(m);

			}

		};
		// 设置定时器发送消息去激活搜索
		timer.schedule(timerTask, 0, time);
	}

	private void setAdapter() {
		if (mAdapter == null) {
			mAdapter = new ImageAdapter(list, context);
			mListView.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		mVideoView.stopPlayback();
		safeback = 1;
		timer.cancel();
		super.onDestroy();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (mVideoView.isPlaying()) {
					mVideoView.seekTo(0);
					mListView.requestFocus();
				} else {
					mVideoView.start();
				}
			}
		}
	};

	private View.OnKeyListener mOnKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
			View view = mListView.getSelectedView();
			SeekBar seek = (SeekBar) view.findViewById(R.id.seekbar);

			if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
				if (arg1 == KeyEvent.KEYCODE_DPAD_DOWN
						|| arg1 == KeyEvent.KEYCODE_DPAD_UP) {
					// mCurKeycode = arg1;
					if (arg2.getRepeatCount() == 0) {
						mTextColorChangeFlag = true;
					} else {
						mTextColorChangeFlag = false;
					}

					if (!mFocusAnimationEndFlag) {
						mTextColorChangeFlag = false;
					}
				}
				if (arg1 == KeyEvent.KEYCODE_DPAD_LEFT
						|| arg1 == KeyEvent.KEYCODE_DPAD_RIGHT) {
					mTextColorChangeFlag = true;
					seek.requestFocus();
				}
			} else if (arg2.getAction() == KeyEvent.ACTION_UP) {
				if (arg1 == KeyEvent.KEYCODE_DPAD_DOWN
						|| arg1 == KeyEvent.KEYCODE_DPAD_UP) {
					// mCurKeycode = arg1;
					if (!mTextColorChangeFlag) {
						mTextColorChangeFlag = true;
						listTextColorSet();
					}
				}
				mListView.requestFocus();
			}
			Log.i("YQBBANG", "----ONKEY----ACTION---" + arg2.getAction()
					+ "-----KECODE----" + arg1 + "mTextColorChangeFlag=="
					+ mTextColorChangeFlag + "----mFocusAnimationEndFlag=="
					+ mFocusAnimationEndFlag);
			return false;
		}
	};
	private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			mItemListCurPosition = position;
			if (bfocusViewInitStatus) {
				focusView.initFocusView(arg1, false, 0);
			}
			if (mTextViewName != null) {
				mTextViewName.setTextColor(ImageActivity.this.getResources()
						.getColor(R.color.grey6_color));
			}
			if (mTextViewSettings != null) {
				mTextViewSettings.setTextColor(ImageActivity.this
						.getResources().getColor(R.color.grey6_color));
			}
			if (mSeekBar != null) {
				Log.i("BANGBANG", "-----------------INVISIBLE-------");
				mSeekBar.setVisibility(View.INVISIBLE);
			}
			mTextViewName = (TextView) arg1.findViewById(R.id.item_name);
			mTextViewSettings = (TextView) arg1.findViewById(R.id.item_setting);
			mSeekBar = (SeekBar) arg1.findViewById(R.id.seekbar);
			if (bfocusViewInitStatus) {
				bfocusViewInitStatus = false;
				mTextColorChangeFlag = true;
				listTextColorSet();
			}

			mDivide = position - mPosition;
			if (mDivide <= -1) {
				mCurKeycode = KeyEvent.KEYCODE_DPAD_UP;
			} else if (mDivide >= 1) {
				mCurKeycode = KeyEvent.KEYCODE_DPAD_DOWN;
			}
			mPosition = position;

			Log.i("YQBBANG", "----ONITEMSELECT----" + "mTextColorChangeFlag=="
					+ mTextColorChangeFlag + "----mFocusAnimationEndFlag=="
					+ mFocusAnimationEndFlag + "-------mCurKeycode-----=="
					+ mCurKeycode);

			if (mCurKeycode == KeyEvent.KEYCODE_DPAD_DOWN) {

				if (position < 4
						|| position > mListView.getCount() - 2
						|| (mListView.getFirstVisiblePosition() == 0 && arg1
								.getTop() < (arg1.getHeight() * 3))
						|| (mListView.getFirstVisiblePosition() != 0 && arg1
								.getTop() < arg1.getHeight() * 4)) {
					focusView.moveTo(arg1);
				} else {
					listTextColorSet();
					mListView.setSelectionFromTop(position, arg1.getTop()
							- arg1.getHeight());

					/*
					 * commonSettingListView.setSelectionFromTop( position,
					 * view.getTop() - view.getHeight());
					 */
				}
			} else if (mCurKeycode == KeyEvent.KEYCODE_DPAD_UP) {
				if ((mItemListCurPosition == 0 || mListView
						.getFirstVisiblePosition() == 0
						&& arg1.getTop() > (arg1.getHeight()))
						|| (mListView.getFirstVisiblePosition() != 0 && arg1
								.getTop() > arg1.getHeight())) {
					focusView.moveTo(arg1);
				} else {
					listTextColorSet();
					mListView.setSelectionFromTop(mItemListCurPosition,
							arg1.getHeight());
				}
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};

	public class ImageAdapter extends BaseAdapter implements
			OnSeekBarChangeListener {

		private Context mAdapterContext = null;
		private LayoutInflater mInflater;

		class ViewHolder {
			TextView itemName;
			TextView itemSetting;
			SeekBar seekbar;
		}

		public ImageAdapter(List<ImageEntity> m, Context context) {
			mAdapterContext = context;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub

			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.image_item, null);
				holder.itemName = (TextView) convertView
						.findViewById(R.id.item_name);
				holder.itemSetting = (TextView) convertView
						.findViewById(R.id.item_setting);
				holder.seekbar = (SeekBar) convertView
						.findViewById(R.id.seekbar);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (list.size() > 0 && null != list.get(position).getNameString()) {
				holder.itemName.setText(list.get(position).getNameString());
			} else {
				holder.itemName.setText(null);
			}
			if (list.size() > 0 && null != list.get(position).getValueString()) {
				holder.itemSetting.setText(list.get(position).getValueString());
			} else {
				holder.itemSetting.setText(null);
			}

			if (list.size() > 0) {
				holder.seekbar.setMax(list.get(position).getMaxValue());
				holder.seekbar.setVisibility(View.INVISIBLE);
				holder.seekbar.setTag(position);
				holder.seekbar.setProgress(Integer.valueOf(list.get(position)
						.getValueString()));
				holder.seekbar.setKeyProgressIncrement(1);
			}
			holder.seekbar.setOnSeekBarChangeListener(this);
			/*
			 * holder.seekbar.setOnKeyListener(new OnKeyListener() {
			 * 
			 * @Override public boolean onKey(View paramView, int paramInt,
			 * KeyEvent paramKeyEvent) { // TODO Auto-generated method stub if
			 * (paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN) { if
			 * (paramInt == KeyEvent.KEYCODE_DPAD_DOWN || paramInt ==
			 * KeyEvent.KEYCODE_DPAD_UP) { Log.i("YQBBANG",
			 * "-----SEEKBAR----ONKEY-----"); mListView.requestFocus(); } }
			 * return false; }
			 * 
			 * });
			 */
			return convertView;
		}

		@Override
		public void onProgressChanged(SeekBar paramSeekBar, int paramInt,
				boolean paramBoolean) {
			// TODO Auto-generated method stub
			if (paramBoolean == true) {
				View view = (View) paramSeekBar.getParent();
				int p = mListView.getSelectedItemPosition();
				int pp = (Integer) paramSeekBar.getTag();
				TextView mTextView = (TextView) view
						.findViewById(R.id.item_setting);
				mTextView.setText(String.valueOf(paramInt));

				if (p == pp) {
					list.get(p).setValueString(String.valueOf(paramInt));
				}
				switch (p) {
				/*
				 * changed by hyl
				case mColorTemperature: {
					Tv.color_temperature temperature = null;
					if (paramInt == 0) {
						temperature = Tv.color_temperature.COLOR_TEMP_STANDARD;
					} else if (paramInt == 1) {
						temperature = Tv.color_temperature.COLOR_TEMP_WARM;
					} else if (paramInt == 2) {
						temperature = Tv.color_temperature.COLOR_TEMP_COLD;
					} else if (paramInt == 3) {
						temperature = Tv.color_temperature.COLOR_TEMP_MAX;
					}

					int k = tv
							.SetColorTemperature(temperature, source, is_save);
					mListView.requestFocus();
					break;
				}
				case mBrightness: {
					int k = tv.SetBrightness(paramInt, source, is_save);
					mListView.requestFocus();
					break;
				}
				case mContrast: {
					int k = tv.SetContrast(paramInt, source, is_save);
					mListView.requestFocus();
					break;
				}
				case mSaturation: {
					int k = tv.SetSaturation(paramInt, source,
							tv.GetCurrentSignalInfo().fmt, is_save);
					mListView.requestFocus();
					break;
				}
				case mHue: {
					int k = tv.SetHue(paramInt, source,
							tv.GetCurrentSignalInfo().fmt, is_save);
					mListView.requestFocus();
					break;
				}
				case mSharpness: {
					int k = tv.SetSharpness(paramInt, source, 1, 0, is_save);
					mListView.requestFocus();
					break;
				}
				case mBacklight: {
					if (SystemProperties
							.getInt("persist.tv.auto_backlight", -1) == 1) {
						tv.SaveBacklight(paramInt, source);
					} else {
						tv.SetBacklight(paramInt, source, is_save);
						SystemProperties
								.set("tv.real_backlight", "" + paramInt);
					}
					mListView.requestFocus();
					break;

				}
				*/
				}

			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar paramSeekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar paramSeekBar) {
			// TODO Auto-generated method stub
			Log.i("YQBBANG", "----requestFocus----" + mListView.requestFocus());

		}
	}
}
