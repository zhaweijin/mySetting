package com.hiveview.tv.utils;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.hiveview.tv.activity.BaseActivity;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.VideoEntity;
import com.hiveview.tv.service.entity.VideoNewEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.service.request.SafeRunnable;

public class PlayerParamsUtils {


	protected static String TAG="PlayerParamsUtils";


	/**
	 * 通过专辑ID，得到其对应的剧集信息（多数情况下可能只有一个剧集，如电影，电视剧等）
	 * 
	 * @param videoSetId
	 *            专辑ID
	 * @param videoSetType
	 *            专辑类型，是电影，电视剧，片花，音乐
	 * @return 返回一个字符串数组，第0个元素是vrsAlbumId，第1个元素是vrsTvId
	 */
	public static  void getVideoPlayParams(final int videoSetId, final int videoSetType,final String videoId, final Context context ,final VideoPlayerListener videoPlayerListener) {
		HttpTaskManager.getInstance().submit(new SafeRunnable() {
			@Override
			public void requestData() {
				List<VideoNewEntity> newVideos = new HiveTVService().getVideoList(context,videoSetId,videoSetType,videoId, 10, 1, "");
				if (newVideos != null && newVideos.size() > 0) {
					VideoNewEntity	entity = newVideos.get(0);
//					String vrsAlbumId = entity.getCp_videoset_id().split(",")[1];
//					String vrsTvId = entity.getCp_video_id().split(",")[1];
//					String vrsAlbumId = String.valueOf(entity.getVideosetId());
//					String vrsTvId = String.valueOf(entity.getVideoId());
//					QiYiPlayerUtil.startNewQiYiPlayer(context, vrsTvId, vrsAlbumId);
					Log.v(TAG, "==getVideoPlayParams");
					QiYiPlayerUtil.startSDKPlayer(context, null, null, false, false, false,String.valueOf(videoSetId),true);	
					if(null != videoPlayerListener){
						videoPlayerListener.onStartPlayerComplete(entity);
					}
				} else {// 剧集下线
					//((BaseActivity) context).showErrorDialog(HiveViewErrorCode.E0000602, false);
					throw new ServiceException(HiveViewErrorCode.E0000602);
				}
			}

			@Override
			public void processServiceException(ServiceException e) {
				e.printStackTrace();// 剧集请求遇到网络异常
				//((BaseActivity) context).showErrorDialog(HiveViewErrorCode.E0000605, false);
				throw new ServiceException(HiveViewErrorCode.E0000605);
			}
		});

	}
	

	/**
	 * @author zhang 播放器调用完成的监听
	 *
	 */
	public interface VideoPlayerListener {
		public void onStartPlayerComplete(VideoNewEntity entity);
	}


}
