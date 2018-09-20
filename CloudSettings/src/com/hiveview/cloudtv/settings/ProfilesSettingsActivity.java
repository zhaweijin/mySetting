package com.hiveview.cloudtv.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.hiveview.cloudtv.settings.ProfilesSettingsActivity.MyAdapter.ViewHolder;
import com.hiveview.cloudtv.settings.util.ImageSharePreference;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView;
import com.hiveview.cloudtv.settings.widget.LauncherFocusView.FocusViewAnimatorEndListener;
import com.hiveview.manager.SoundEffectManager;

public class ProfilesSettingsActivity extends Activity {
	private static final String TAG = "NetSettingsActivity";
	private Context mContext;
	private MyAdapter listItemAdapter;
	private ListView listConnectDevice;
	/* 按键音开 */
	private int AUDIO_ON = 1;
	/* 按键音关 */
	private int AUDIO_OFF = 0;
	private VideoView mVideoView;
	private LauncherFocusView focusView;
	private RelativeLayout layoutTitle;
	private boolean bfocusViewInitStatus;
	private AudioManager mAudioManager ;
	private boolean mTextColorChangeFlag = false;
	private ArrayList<HashMap<String, Object>> arrayListItem = new ArrayList<HashMap<String, Object>>();
	private SoundEffectManager manager;
	private int textStatus = PROFILES_MODE_NULL;
	private int currentMode = PROFILES_MODE_NORMAL;
	private ImageSharePreference mPreference = null;
	private static final int PROFILES_MODE_NULL = -1;
	private static final int PROFILES_MODE_NORMAL = 0;
	private static final int PROFILES_MODE_THEATER = 1;
	private static final int PROFILES_MODE_MUSIC = 2;
	private static final int PROFILES_MODE_GAME = 3;
	private static final int PROFILES_MODE_GORGEOUS = 4;
	private static final int PROFILES_MODE_SIZE = 5;
	private int currentTime;
	private int musicDuration;
	private int safeback;
	private int reduceTime = 1000;
	private int mFinalSelection = -1;
	private int mFirstSelection = -1;
	public Timer timer = new Timer();
	public TimerTask timerTask;
	private View mItemListCurView = null;
	private ViewHolder viewHolder = null;
	private int mIs_save = 1;
	
	/*
	 * changed by hyl
	 private Tv mTV = Tv.open();
	 private SourceInput_Type mSource;
	 */

	private void timeSetter(int time) {
		timerTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message m = new Message();
				m.what = 1;
				handler.sendMessage(m);
			}

		};
		// 设置定时器发送消息去激活搜索
		timer.schedule(timerTask, 0, time);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				Log.e("huxing", "huxing--------handler----1");
				if (mVideoView.isPlaying()) {
					mVideoView.seekTo(0);
				} else {
					mVideoView.start();
				}
			}

			if (msg.what == 2) {
				// AnimationSet set = new AnimationSet(false);

				Animation animation;
				animation = AnimationUtils.loadAnimation(mContext, R.anim.list_profile_anim);
				LayoutAnimationController controller = new LayoutAnimationController(animation, 1);
				animation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation paramAnimation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation paramAnimation) {
						// TODO Auto-generated method stub

						focusView.initFocusView(mItemListCurView, false, 0);

					}

					@Override
					public void onAnimationRepeat(Animation paramAnimation) {
						// TODO Auto-generated method stub

					}
				});
				listConnectDevice.setVisibility(View.VISIBLE);
				listConnectDevice.setLayoutAnimation(controller);
				listConnectDevice.requestFocus();

				Animation animation1 = AnimationUtils.loadAnimation(mContext,
						R.anim.title_profile_anim);

				LayoutAnimationController controller1 = new LayoutAnimationController(animation1, 1);

				layoutTitle.setVisibility(View.VISIBLE);
				layoutTitle.setLayoutAnimation(controller1);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profilessettings);
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		initWidget();
		initMain();
		initPqMode();
	}

	private void initPqMode() {
		/*
		 * changed by hyl
		mSource = mTV.GetCurrentSourceInputType();
		 */
		// Log.i("YQB", "---------------PQmode------------" +
		// mTV.GetPQMode(mSource));
	};

	private void initWidget() {
		mPreference = new ImageSharePreference(getApplicationContext());
		manager = SoundEffectManager.getInstance(mContext);
		focusView = (LauncherFocusView) findViewById(R.id.focusview_1);
		focusView.setAnimatorEndListener(new FocusViewAnimatorEndListener() {

			@Override
			public void OnAnimateEnd(View currentFocusView) {
				// TODO Auto-generated method stub
				focusView.initFocusView(mItemListCurView, false, 0);
				Log.i("BANGBANG", "----At the animateend---" + mTextColorChangeFlag
						+ "----------viewHolder------" + viewHolder);
				listTextColorSet();
			}

			@Override
			public void OnAnimateStart(View currentFocusView) {
				// TODO Auto-generated method stub

			}

		});
		layoutTitle = (RelativeLayout) findViewById(R.id.layout_title);

		bfocusViewInitStatus = false;
		mVideoView = (VideoView) findViewById(R.id.videoView1);
		mVideoView.requestFocus();
		mVideoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mVideoView.start();
				currentTime = 0;
				musicDuration = mVideoView.getDuration();
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				timeSetter(musicDuration - reduceTime);

			}
		});

		mVideoView.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				// playModeVideo(textStatus);
			}

		});

		listConnectDevice = (ListView) findViewById(R.id.listView_profiles_show);
		listConnectDevice.requestFocus();
		listConnectDevice.setVisibility(View.GONE);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemTitle", this.getResources().getString(R.string.listview_profiles_mode_title_1));
		map.put("ItemDescription",
				this.getResources().getString(R.string.listview_profiles_mode_discription_1));
		map.put("ItemFlag", PROFILES_MODE_NORMAL);
		arrayListItem.add(map);

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ItemTitle", this.getResources()
				.getString(R.string.listview_profiles_mode_title_2));
		map1.put("ItemDescription",
				this.getResources().getString(R.string.listview_profiles_mode_discription_2));
		map1.put("ItemFlag", PROFILES_MODE_THEATER);
		arrayListItem.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemTitle", this.getResources()
				.getString(R.string.listview_profiles_mode_title_3));
		map2.put("ItemDescription",
				this.getResources().getString(R.string.listview_profiles_mode_discription_3));
		map2.put("ItemFlag", PROFILES_MODE_MUSIC);
		arrayListItem.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("ItemTitle", this.getResources()
				.getString(R.string.listview_profiles_mode_title_4));
		map3.put("ItemDescription",
				this.getResources().getString(R.string.listview_profiles_mode_discription_4));
		map3.put("ItemFlag", PROFILES_MODE_GAME);
		arrayListItem.add(map3);

		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("ItemTitle", this.getResources()
				.getString(R.string.listview_profiles_mode_title_5));
		map4.put("ItemDescription",
				this.getResources().getString(R.string.listview_profiles_mode_discription_5));
		map4.put("ItemFlag", PROFILES_MODE_GORGEOUS);
		arrayListItem.add(map4);

		listItemAdapter = new MyAdapter(this, arrayListItem);

		listConnectDevice.setAdapter(listItemAdapter);

		listConnectDevice.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mItemListCurView = view;
				 
				focusView.moveTo(view);

				if (viewHolder != null) {
					if (viewHolder.txTitle != null) {
						viewHolder.txTitle.setTextColor(0xFF9A9A9A);
						viewHolder.image.setImageDrawable(getResources().getDrawable(
								R.drawable.settings_profiles_item_ns));
					}
				}
				viewHolder = (ViewHolder) view.getTag();
				if (!bfocusViewInitStatus) {
					bfocusViewInitStatus = true;
					mTextColorChangeFlag = true;
					listTextColorSet();
				}
				// TODO 设置情景模式
				//设置静音
			
				
				/*
				 * changed by hyl
				 * 
				 
				mTV.SetAudioMuteKeyStatus(1);
				*/
				
				//设置生效
				manager.setMaxxProfilesMode(String.valueOf(position));
				//恢复声音	
				try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/*
				 * changed by hyl
				 * 
				
				mTV.SetAudioMuteKeyStatus(0);
				 */
				
				setPqMode(position);
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				textStatus = -1;
				listItemAdapter.notifyDataSetChanged();
			}
		});

		listConnectDevice.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (event.getRepeatCount() == 0) {
						mTextColorChangeFlag = true;
						Log.i("BANGBANG", "----At the REPEATCOUNT---" + mTextColorChangeFlag);
					}
				}
				if (event.getAction() == KeyEvent.ACTION_UP) {
					if (!mTextColorChangeFlag) {
						mTextColorChangeFlag = true;
						listTextColorSet();
					}
				}
				return false;
			}
		});
		listConnectDevice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> paramAdapterView, View view, int position,
					long paramLong) {
				// TODO Auto-generated method stub
				manager.setMaxxProfilesMode(String.valueOf(position));
				setPqMode(position);
				mPreference.setModel(position);
				mFinalSelection = position;
			}

		});

	}

	private void setPqMode(int position) {
		/*
		 * changed by hyl
		 * 
		 
		if (position == 0) {
			mTV.SetPQMode(Tv.Pq_Mode.PQ_MODE_STANDARD, mSource, mIs_save);
		} else if (position == 1) {
			mTV.SetPQMode(Tv.Pq_Mode.PQ_MODE_MOVIE, mSource, mIs_save);
		} else if (position == 2) {
			mTV.SetPQMode(Tv.Pq_Mode.PQ_MODE_USER, mSource, mIs_save);
			mTV.GetContrast(mSource);
			mTV.SetContrast(mTV.GetContrast(mSource), mSource, mIs_save);
		} else if (position == 3) {
			mTV.SetPQMode(Tv.Pq_Mode.PQ_MODE_SOFTNESS, mSource, mIs_save);
		} else if (position == 4) {
			mTV.SetPQMode(Tv.Pq_Mode.PQ_MODE_COLORFUL, mSource, mIs_save);
		}
		*/
	}

	private void initMain() {
		mContext = this;
		safeback = 0;
	}

	private void listTextColorSet() {
		if (viewHolder != null && mTextColorChangeFlag) {
			mTextColorChangeFlag = false;
			viewHolder.txTitle.setTextColor(mContext.getResources().getColor(R.color.white));
			viewHolder.image.setImageDrawable(mContext.getResources().getDrawable(
					R.drawable.settings_profiles_item_s));
		}
	}

	private void playModeVideo(int mode) {

		if (mode >= PROFILES_MODE_SIZE || mode <= PROFILES_MODE_NULL) {
			return;
		}
	//	Uri mUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.backmovie);
	//	mVideoView.setVideoURI(mUri);
	}

	

	private void setAudio(int OpenOrClose) {
	//	mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.loadSoundEffects();
		
		Settings.System.putInt(getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED,
				OpenOrClose);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		setAudio(AUDIO_OFF);
		currentMode = mPreference.getModel();
		textStatus = mPreference.getModel();
		mFirstSelection = textStatus;
		mFinalSelection = textStatus;
		listConnectDevice.setSelection(textStatus);
		playModeVideo(textStatus);
		// handler.sendEmptyMessage(2);
		super.onResume();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onPause() {
		setAudio(AUDIO_ON);
		mVideoView.stopPlayback();
		// mVideoView.pause();
		Toast toast = new Toast(this);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View toastView = inflater.inflate(R.layout.toast, null);
		TextView textView = (TextView) toastView.findViewById(R.id.textview);
		if (mFinalSelection == mFirstSelection) {
			textView.setText(this.getResources().getString(R.string.modify_not));
			// TODO 设置回原来的情景模式
			manager.setMaxxProfilesMode(String.valueOf(mFirstSelection));
			setPqMode(mFirstSelection);
		} else {
			textView.setText(this.getResources().getString(R.string.modify_success));
		}
		toast.setView(toastView);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
		super.onPause();
		// mVideoView.stopPlayback();

	}

	@Override
	protected void onStop() {
		// mVideoView.stopPlayback();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// mVideoView.stopPlayback();
		safeback = 1;
		timer.cancel();
		super.onDestroy();

	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<HashMap<String, Object>> mData;

		public MyAdapter(Context context, ArrayList<HashMap<String, Object>> array) {
			mInflater = LayoutInflater.from(context);
			mData = array;
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			// convertView为null锟斤拷时锟斤拷锟绞硷拷锟絚onvertView锟斤拷
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.list_profilessettings_items, null);
				holder.txTitle = (TextView) convertView
						.findViewById(R.id.textView_profiles_items_name);
				holder.textDescription = (TextView) convertView
						.findViewById(R.id.textView_profiles_items_description);
				holder.image = (ImageView) convertView
						.findViewById(R.id.imageView_profiles_items_choice);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.txTitle.setText(mData.get(position).get("ItemTitle").toString());
			holder.textDescription.setText(mData.get(position).get("ItemDescription").toString());
			holder.flag = (Integer) mData.get(position).get("ItemFlag");
			if (currentMode == holder.flag)
				holder.image.setImageDrawable(getResources().getDrawable(
						R.drawable.settings_profiles_item_s));
			else
				holder.image.setImageDrawable(getResources().getDrawable(
						R.drawable.settings_profiles_item_ns));

			if (textStatus == position) {
				holder.txTitle.setTextColor(0xFFFFFFFF);
			} else {
				holder.txTitle.setTextColor(0xFF9A9A9A);
			}
			return convertView;
		}

		public final class ViewHolder {
			public TextView txTitle;
			public TextView textDescription;
			public int flag;
			public ImageView image;
		}
	}

}
