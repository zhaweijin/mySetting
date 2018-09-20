package com.hiveview.tv.view.television.voicecontrol;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hiveview.tv.R;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.LoadService;
import com.hiveview.tv.service.dao.AppFocusDAO;
import com.hiveview.tv.service.dao.BaseDAO;
import com.hiveview.tv.service.dao.ChannelDAO;
import com.hiveview.tv.service.dao.EducationDAO;
import com.hiveview.tv.service.dao.GameFocusDAO;
import com.hiveview.tv.service.dao.RecommendDAO;
import com.hiveview.tv.service.entity.AppFocusEntity;
import com.hiveview.tv.service.entity.FirstClassListEntity;
import com.hiveview.tv.service.entity.RecommendEntity;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.utils.DateUtils;
import com.hiveview.tv.utils.NetUitls;
import com.hiveview.tv.view.pager3d.HomeActions;

/**
 * 监听开机广播，预加载数据
 * 
 * @author chenlixiao
 * 
 */
public class PreLoadDataReceiver extends BroadcastReceiver {

	private RecommendDAO recommendDAO = null;
	private ChannelDAO channelDAO = null;
	private AppFocusDAO appFocusDao = null;
	GameFocusDAO gameFocusDao = null;
	private EducationDAO educationDAO =  null;
	private HiveTVService service = new HiveTVService();
	private Context mContext;
	
	

	@Override
	public void onReceive(Context context, Intent intent) {
		recommendDAO = new RecommendDAO(context);
		channelDAO = new ChannelDAO(context);
		appFocusDao = new AppFocusDAO(context);
		gameFocusDao = new GameFocusDAO(context);
		educationDAO = new EducationDAO(context);
		mContext = context;
		//start by zhagnpengzhan 调试log记录
		//context.getSharedPreferences("PreLoadDataReceiver", Context.MODE_PRIVATE).edit().putString(DateUtils.getAfterMinuteDate(0), " date form LoadService").commit();
		//end zhangpengzhan
/*		Intent intentService = new Intent(mContext, LoadService.class);
		intentService.putExtra("isNeedDeviceCheck", true);
		mContext.startService(intentService);*/
//		loadRecommendData();

	}


	/**
	 * 请求点播频道的频道信息，如电影，电视剧，音乐，体育,并缓存到本地
	 */
	private void loadChannels() {
		HttpTaskManager.getInstance().submit(new Runnable() {

			@Override
			public void run() {
				ArrayList<FirstClassListEntity> channelList = service.getFirstClassList(HiveviewApplication.mContext.getResources().getString(R.string.language));
				channelDAO.delete(null, null);
				channelDAO.insert(channelList);
				System.out.println("频道：" + channelList.size());
			}
		});
	}


}
