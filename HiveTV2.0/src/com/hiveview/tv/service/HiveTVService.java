package com.hiveview.tv.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.entity.AppFocusEntity;
import com.hiveview.tv.service.entity.AppMarketEntity;
import com.hiveview.tv.service.entity.CastEntity;
import com.hiveview.tv.service.entity.ChannelCategoryEntity;
import com.hiveview.tv.service.entity.ChannelEntity;
import com.hiveview.tv.service.entity.DispatchEntity;
import com.hiveview.tv.service.entity.FilmCategory;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmListEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.FilmRelatedEntity;
import com.hiveview.tv.service.entity.FirstClassListEntity;
import com.hiveview.tv.service.entity.GetDvicesCodeEntity;
import com.hiveview.tv.service.entity.LiveMediaEntity;
import com.hiveview.tv.service.entity.LiveStreamEntity;
import com.hiveview.tv.service.entity.NetLockEntity;
import com.hiveview.tv.service.entity.ProgramByCodeEntity;
import com.hiveview.tv.service.entity.ProgramEntity;
import com.hiveview.tv.service.entity.RecommandMediaEntity;
import com.hiveview.tv.service.entity.RecommendEntity;
import com.hiveview.tv.service.entity.SYSUpDataEntity;
import com.hiveview.tv.service.entity.SimpleCastEntity;
import com.hiveview.tv.service.entity.SkinEntity;
import com.hiveview.tv.service.entity.SubjectInfoList;
import com.hiveview.tv.service.entity.SubjectListEntity;
import com.hiveview.tv.service.entity.SubjectListEntitys;
import com.hiveview.tv.service.entity.TVChannelEntity;
import com.hiveview.tv.service.entity.ThirdListResultEntity;
import com.hiveview.tv.service.entity.TotalListFilmEntity;
import com.hiveview.tv.service.entity.TotalListFilmNewEntity;
import com.hiveview.tv.service.entity.TotalListSearchEntity;
import com.hiveview.tv.service.entity.VideoEntity;
import com.hiveview.tv.service.entity.VideoNewEntity;
import com.hiveview.tv.service.entity.VipLockEntity;
import com.hiveview.tv.service.entity.WordsEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.net.BaseHttpGetConnector;
import com.hiveview.tv.service.net.BaseHttpPostConnector;
import com.hiveview.tv.service.net.HttpGetConnector;
import com.hiveview.tv.service.net.HttpPostConnector;
import com.hiveview.tv.service.parser.AppFocusParser;
import com.hiveview.tv.service.parser.AppMarketListParser;
import com.hiveview.tv.service.parser.BaseParser;
import com.hiveview.tv.service.parser.CastParser;
import com.hiveview.tv.service.parser.ChannelCategoryParser;
import com.hiveview.tv.service.parser.ChannelsParser;
import com.hiveview.tv.service.parser.CollectListParser;
import com.hiveview.tv.service.parser.CommonParser;
import com.hiveview.tv.service.parser.DispatchParser;
import com.hiveview.tv.service.parser.FilmCategoryParser;
import com.hiveview.tv.service.parser.FilmDetailNewListParser;
import com.hiveview.tv.service.parser.FilmDetailParser;
import com.hiveview.tv.service.parser.FilmRelatedParser;
import com.hiveview.tv.service.parser.FirstClassParser;
import com.hiveview.tv.service.parser.GetCastWithCountParser;
import com.hiveview.tv.service.parser.GetChannelsByNamesParser;
import com.hiveview.tv.service.parser.GetDevicesCodeParser;
import com.hiveview.tv.service.parser.GetLockNetParser;
import com.hiveview.tv.service.parser.LiveCategoryTagsParser;
import com.hiveview.tv.service.parser.LiveMediaParser;
import com.hiveview.tv.service.parser.LiveStreamListParser;
import com.hiveview.tv.service.parser.PostLockVipParser;
import com.hiveview.tv.service.parser.ProgramByCodeParser;
import com.hiveview.tv.service.parser.ProgramParser;
import com.hiveview.tv.service.parser.QiyiFilmRecommandNewParser;
import com.hiveview.tv.service.parser.QiyiFilmRecommandParser;
import com.hiveview.tv.service.parser.RecommandMediaParser;
import com.hiveview.tv.service.parser.RecommandParser;
import com.hiveview.tv.service.parser.SearchListParser;
import com.hiveview.tv.service.parser.SearchParser;
import com.hiveview.tv.service.parser.SkinParser;
import com.hiveview.tv.service.parser.SubjectInfoParser;
import com.hiveview.tv.service.parser.SubjectListParser;
import com.hiveview.tv.service.parser.TVChannelByProgramParser;
import com.hiveview.tv.service.parser.ThirdClassParser;
import com.hiveview.tv.service.parser.UpDataParser;
import com.hiveview.tv.service.parser.UpgraderListParser;
import com.hiveview.tv.service.parser.VideoListParser;
import com.hiveview.tv.service.parser.WordParser;
import com.hiveview.tv.service.request.ApiConstant;
import com.hiveview.tv.service.request.BaseGetRequest;
import com.hiveview.tv.service.request.BasePostRequest;
import com.hiveview.tv.service.request.BluelightRecommandRequest;
import com.hiveview.tv.service.request.DeleteCollectRequest;
import com.hiveview.tv.service.request.DevCheckRequest;
import com.hiveview.tv.service.request.GetAppFocusListRequest;
import com.hiveview.tv.service.request.GetCastRelatedRequest;
import com.hiveview.tv.service.request.GetCastRequest;
import com.hiveview.tv.service.request.GetCastWithCountRequest;
import com.hiveview.tv.service.request.GetChannelCategoryRequest;
import com.hiveview.tv.service.request.GetChannelLiveRecommendRequest;
import com.hiveview.tv.service.request.GetChannelsByNamesRequest;
import com.hiveview.tv.service.request.GetChannelsBySPRequest;
import com.hiveview.tv.service.request.GetCollectRecordListRequest;
import com.hiveview.tv.service.request.GetDeviceCodeRequest;
import com.hiveview.tv.service.request.GetFilmCategoryRequest;
import com.hiveview.tv.service.request.GetFilmDetailRealRequest;
import com.hiveview.tv.service.request.GetFilmDetailRequest;
import com.hiveview.tv.service.request.GetFilmRelatedRequest;
import com.hiveview.tv.service.request.GetFirstClassRequest;
import com.hiveview.tv.service.request.GetLockNetRequest;
import com.hiveview.tv.service.request.GetMediaByTagRequest;
import com.hiveview.tv.service.request.GetSkinsRequest;
import com.hiveview.tv.service.request.GetSubjectInfoRequest;
import com.hiveview.tv.service.request.GetSubjectListRequest;
import com.hiveview.tv.service.request.GetThirdResultForListRequest;
import com.hiveview.tv.service.request.GetThirdResultRequest;
import com.hiveview.tv.service.request.GetLiveStreamListRequest;
import com.hiveview.tv.service.request.GetVideoListRequest;
import com.hiveview.tv.service.request.GetVideoSetListByTagRequest;
import com.hiveview.tv.service.request.GetVideoSetListRequest;
import com.hiveview.tv.service.request.GetVirtualFirstClassRequest;
import com.hiveview.tv.service.request.HomeRecommandRequest;
import com.hiveview.tv.service.request.HotWordRequest;
import com.hiveview.tv.service.request.LiveCategoryTagsRequest;
import com.hiveview.tv.service.request.MovieRecommandRequest;
import com.hiveview.tv.service.request.PostLockVipRequest;
import com.hiveview.tv.service.request.PostRecordRequest;
import com.hiveview.tv.service.request.ProgramByCodeRequest;
import com.hiveview.tv.service.request.ProgramRequest;
import com.hiveview.tv.service.request.QiyiRelativeRequest;
import com.hiveview.tv.service.request.RecommendMediasRequest;
import com.hiveview.tv.service.request.SaveCollectRecordRequest;
import com.hiveview.tv.service.request.SearchPostRequest;
import com.hiveview.tv.service.request.SearchRequest;
import com.hiveview.tv.service.request.SystemAppListRequest;
import com.hiveview.tv.service.request.TvChannelByProgramRequest;
import com.hiveview.tv.service.request.UpDataRequest;
import com.hiveview.tv.service.request.UpgraderRequest;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.DeviceBoxUtils;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.MD5Utils;
import com.hiveview.tv.utils.PayKeyUtil;

public class HiveTVService {
	/**
	 * 当前网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();

		if (info == null || !info.isConnected() || info.isRoaming())
			return false;

		return true;
	}

	/**
	 * 如果当前不可用抛出ServiceException异常
	 * 
	 * @param context
	 * @author chenlixiao
	 */
	public void checkNetwork(Context context) {
		if (!isAvailable(context)) {
			throw new ServiceException(HiveViewErrorCode.E0000605);
		}
	}

	/**
	 * 得到首页（推荐）的推荐位组图,按照1-6索引依次获取推荐位编号的组图
	 * 
	 * @author chenlixiao
	 * @param context
	 * @return
	 */
	public ArrayList<RecommendEntity> getHomeRecommands(Context context) {
		checkNetwork(context);
		BaseGetRequest request = new HomeRecommandRequest();
		RecommandParser parser = new RecommandParser();

		@SuppressWarnings("unchecked")
		ArrayList<RecommendEntity> recommends = (ArrayList<RecommendEntity>) getEntities(context, request, parser);

		return recommends;
	}

	/**
	 * 得到影院的推荐位组图,按照1-6索引依次获取推荐位编号的组图
	 * 
	 * @author chenlixiao
	 * @param context
	 * @return
	 */
	public ArrayList<RecommendEntity> getMovieRecommands(Context context) {
		checkNetwork(context);
		BaseGetRequest request = new MovieRecommandRequest();
		RecommandParser parser = new RecommandParser();

		ArrayList<RecommendEntity> recommends = (ArrayList<RecommendEntity>) getEntities(context, request, parser);

		return recommends;
	}

	/**
	 * 得到蓝光的推荐位组图,按照0-5索引依次获取推荐位编号的组图
	 * 
	 * @author ZhaiJianfeng
	 * @param context
	 * @return
	 */
	public ArrayList<RecommendEntity> getBluelightRecommands(Context context) {
		checkNetwork(context);
		BaseGetRequest request = new BluelightRecommandRequest();
		RecommandParser parser = new RecommandParser();

		@SuppressWarnings("unchecked")
		ArrayList<RecommendEntity> recommends = (ArrayList<RecommendEntity>) getEntities(context, request, parser);

		return recommends;
	}

	/**
	 * 得到热词，联想词
	 * 
	 * @param context
	 * @param hotword
	 *            文字索引
	 * @param search_type
	 *            搜索类型[0:按片名搜,1:按人名搜]
	 * @author chenlixiao
	 * @return
	 */
	public List<WordsEntity> getHotWordEntities(Context context, String hotword) {
		BaseGetRequest request = new HotWordRequest(hotword);
		WordParser parser = new WordParser();
		List<WordsEntity> hotwords = (List<WordsEntity>) getEntities(context, request, parser);
		return hotwords;
	}

	/**
	 * 分类筛选
	 * 
	 * @Title: HiveTVService
	 * @author:张鹏展
	 * @Description:
	 * @param context
	 * @param videoType
	 *            视频类型
	 * @return
	 */
	public List<ThirdListResultEntity> getThirdClassResultEntites(Context context, String videoType) {
		Log.d(TAG, "================getThirdClassResultEntites::" + videoType);
		BaseGetRequest request = new GetThirdResultRequest(videoType);
		ThirdClassParser parser = new ThirdClassParser();
		List<ThirdListResultEntity> thirdClassResult = (List<ThirdListResultEntity>) getEntities(context, request, parser);
		return thirdClassResult;
	}

	/**
	 * 鉴权设备
	 * */
	public String deviceCheck(String version, String deviceid, String mac, String uuid) throws ServiceException {
		Log.i(TAG, "deviceCheck-->POS 1");
		try {
			BasePostRequest request = new DevCheckRequest(version, deviceid, mac, uuid);
			BaseHttpPostConnector connector = new HttpPostConnector(request);
			InputStream in = connector.getPostResponse();
			Log.i(TAG, "deviceCheck-->POS 2");
			if (in == null) {
				Log.i(TAG, "deviceCheck-->POS 3");
				throw new ServiceException(HiveViewErrorCode.E0000605);
			}
			Log.i(TAG, "deviceCheck-->POS 4");
			CommonParser parser = new CommonParser();
			parser.executeToObject(in);
			String errorCode = parser.getErrorCode();
			if (errorCode.equals(ErrorCode.CODE_SUCCESS)) {
				Log.i(TAG, "deviceCheck-->POS 6");
				return ErrorCode.CODE_SUCCESS;
			} else {
				Log.i(TAG, "deviceCheck-->POS 7");
				throw new ServiceException(errorCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "deviceCheck-->POS 8");
			throw new ServiceException(HiveViewErrorCode.E0000605);
		}

	}

	/**
	 * 专题列表 test OK
	 * 
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<SubjectListEntitys> getSubjectList(String launcherId, String subjectId, String isEffective, int pageSize, int pageNumber,String language)
			throws ServiceException {
		BaseGetRequest request = new GetSubjectListRequest(launcherId, subjectId, isEffective, pageSize, pageNumber,language);
		BaseHttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		SubjectListParser parser = new SubjectListParser();
		ArrayList<SubjectListEntitys> list = parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		if (errorCode.equals(ErrorCode.CODE_SUCCESS))
			return list;
		else
			throw new ServiceException(errorCode);
	}

	/**
	 * 专题详细列表 test OK
	 * 
	 * @param pageSize
	 * @param pageNumber
	 * @return 专题列表的所有的字段
	 * @throws ServiceException
	 */
	public ArrayList<SubjectInfoList> getSubjectInfo(String id, String subjectId, String isEffective, int pageSize, int pageNumber)
			throws ServiceException {
		BaseGetRequest request = new GetSubjectInfoRequest(id, subjectId, isEffective, pageSize, pageNumber);
		BaseHttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		SubjectInfoParser parser = new SubjectInfoParser();
		ArrayList<SubjectInfoList> list = parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		if (errorCode.equals(ErrorCode.CODE_SUCCESS))
			return list;
		else
			throw new ServiceException(errorCode);
	}

	/***
	 * 得到电视剧，综艺的剧集列表
	 * 
	 * @param videoset_id
	 *            video id
	 * @param video_type
	 *            video 类型 如电影 video_type=1,综艺video_type=6
	 * @param page_size
	 *            每页请求多少数据
	 * @param page_number
	 *            请求地第几页
	 * @param year
	 *            按年份查询，主要用在综艺上，电视剧传空字符就行
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<VideoNewEntity> getVideoList(Context context, int videoset_id, int videoType, String videoid, int page_size, int page_number,
			String year) throws ServiceException {
		BaseGetRequest request = new GetVideoListRequest(videoset_id, videoType, videoid, page_size, page_number, year);
		VideoListParser parser = new VideoListParser();
		ArrayList<VideoNewEntity> list = (ArrayList<VideoNewEntity>) getEntities(context, request, parser);
		return list;
	}

	/**
	 * 电影详情 test OK
	 * 
	 * @param videosetId
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<FilmNewEntity> getFilmDetail(Context context, int videosetId) throws ServiceException {
		BaseGetRequest request = new GetFilmDetailRequest(videosetId + "");
		FilmDetailParser parser = new FilmDetailParser();
		ArrayList<FilmNewEntity> list = (ArrayList<FilmNewEntity>) getEntities(context, request, parser);
		return list;
	}

	/**
	 * 需要跳转到我们的服务器的电影的详情的数据 电影详情 test OK
	 * 
	 * @param videosetId
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<FilmEntity> getFilmDetail(Context context, String videosetId) throws ServiceException {
		BaseGetRequest request = new GetFilmDetailRealRequest(videosetId + "");
		FilmDetailParser parser = new FilmDetailParser();
		ArrayList<FilmEntity> list = (ArrayList<FilmEntity>) getEntities(context, request, parser);
		return list;
	}

	/**
	 * 获取爱奇艺相关推荐电影
	 * 
	 * @param videosetId
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<FilmNewEntity> getQiyiFilmRelativeRecommands(int chnnid, int id) throws ServiceException {
		BaseGetRequest request = new QiyiRelativeRequest(chnnid, id);
		QiyiFilmRecommandNewParser parser = new QiyiFilmRecommandNewParser();
		ArrayList<FilmNewEntity> list = (ArrayList<FilmNewEntity>) getEntities(null, request, parser);
		// String errorCode = parser.getErrorCode();
		// if (errorCode.equals(ErrorCode.CODE_SUCCESS))

		return list;
		// else
		// throw new ServiceException(errorCode);
	}

	/**
	 * 电影详情页，相关推荐 test no data
	 * 
	 * @param videoSetId
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<FilmRelatedEntity> getFilmRelated(int videoSetId) throws ServiceException {
		BaseGetRequest request = new GetFilmRelatedRequest(videoSetId);
		HttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		FilmRelatedParser parser = new FilmRelatedParser();
		ArrayList<FilmRelatedEntity> list = parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		if (errorCode.equals(ErrorCode.CODE_SUCCESS))
			return list;
		else
			throw new ServiceException(errorCode);
	}

	/**
	 * 首页应用与游戏、游戏焦点图
	 * 
	 * @param navigation_id
	 *            导航栏位置 1 游戏 5应用
	 * */
	public ArrayList<AppFocusEntity> getAppFocusList(String navigationId,String language) {
		BaseGetRequest request = new GetAppFocusListRequest(navigationId,language);
		HttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		AppFocusParser parser = new AppFocusParser();
		String errorCode = null;
		ArrayList<AppFocusEntity> list = null;
		try {
			list = parser.executeToObject(in);
			errorCode = parser.getErrorCode();
		} catch (ServiceException e) {
			/* json解析出错 */
			if (e.getErrorCode().equals(HiveViewErrorCode.E0000598)) {
				return list;
			}
		}
		if (errorCode.equals(ErrorCode.CODE_SUCCESS))
			return list;
		else
			throw new ServiceException(errorCode);

	}

	/**
	 * 演员/导演详细信息 test ok
	 * 
	 * @param castId
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<CastEntity> getCastDetail(int castId) throws ServiceException {
		BaseGetRequest request = new GetCastRequest(castId);
		HttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		CastParser parser = new CastParser();
		ArrayList<CastEntity> list = parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		if (errorCode.equals(ErrorCode.CODE_SUCCESS))
			return list;
		else
			throw new ServiceException(errorCode);
	}

	/**
	 * 演员/导演详情页，相关推荐
	 * 
	 * @param castId
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<FilmRelatedEntity> getCastRelated(int castId) throws ServiceException {
		BaseGetRequest request = new GetCastRelatedRequest(castId);
		HttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		FilmRelatedParser parser = new FilmRelatedParser();
		ArrayList<FilmRelatedEntity> list = parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		if (errorCode.equals(ErrorCode.CODE_SUCCESS))
			return list;
		else
			throw new ServiceException(errorCode);
	}

	/**
	 * 教育页面数据
	 * 
	 * @param castId
	 * @return
	 * @throws ServiceException
	 */
	// public ArrayList<EducationEntity> getEducationRelatedList(int castId)
	// throws ServiceException {
	// BaseGetRequest request = new GetCastRelatedRequest(castId);
	// HttpGetConnector connector = new HttpGetConnector(request);
	// InputStream in = connector.getGetResponse();
	//
	// EducationParser parser = new EducationParser();
	// ArrayList<EducationEntity> list = parser.executeToObject(in);
	// String errorCode = parser.getErrorCode();
	// if (errorCode.equals(ErrorCode.CODE_SUCCESS))
	// return list;
	// else
	// throw new ServiceException(errorCode);
	// }

	/**
	 * 收藏列表
	 * 
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<FilmListEntity> getCollectRecordList(int pageSize, int pageNumber) throws ServiceException {
		BaseGetRequest request = new GetCollectRecordListRequest(pageSize, pageNumber);
		BaseHttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		CollectListParser parser = new CollectListParser();
		ArrayList<FilmListEntity> list = parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		if (errorCode.equals(ErrorCode.CODE_SUCCESS))
			return list;
		else
			throw new ServiceException(errorCode);
	}

	/**
	 * 保存收藏记录
	 * 
	 * @param videoSetId
	 * @param videoType
	 * @return
	 * @throws ServiceException
	 */
	public boolean saveCollectRecord(int videoSetId, int videoType) throws ServiceException {
		BaseGetRequest request = new SaveCollectRecordRequest(videoSetId, videoType);
		BaseHttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		CommonParser parser = new CommonParser();
		parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		if (errorCode.equals(ErrorCode.CODE_SUCCESS))
			return true;
		else
			throw new ServiceException(errorCode);
	}

	/**
	 * 删除收藏记录
	 * 
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 * @throws ServiceException
	 */
	public boolean deleteCollectRecord(int videoSetId) throws ServiceException {
		BaseGetRequest request = new DeleteCollectRequest(videoSetId);
		BaseHttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		CommonParser parser = new CommonParser();
		parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		if (errorCode.equals(ErrorCode.CODE_SUCCESS))
			return true;
		else
			throw new ServiceException(errorCode);
	}

	/**
	 * 得到欢网直播的标签分类，如电影，电视剧，体育，少儿
	 * 
	 * @return
	 */
	public List<WordsEntity> getLiveCategoryTags() {
		BasePostRequest request = new LiveCategoryTagsRequest();
		LiveCategoryTagsParser parser = new LiveCategoryTagsParser();
		List<WordsEntity> list = (List<WordsEntity>) postEntities(request, parser);
		return list;
	}

	/**
	 * 得到欢网的电视台分类（央视，卫视，付费，高清等），用于快速选台
	 * 
	 * @return
	 */
	public List<ChannelCategoryEntity> getChannelCategory() {
		BasePostRequest request = new GetChannelCategoryRequest();
		ChannelCategoryParser parser = new ChannelCategoryParser();
		List<ChannelCategoryEntity> list = (List<ChannelCategoryEntity>) postEntities(request, parser);
		return list;
	}

	/**
	 * 根据标签（电视剧，电影，体育，少儿）获取直播的节目
	 * 
	 * @return
	 */
	public List<LiveMediaEntity> getMediasByTag(String tag, int page) {
		BasePostRequest request = new GetMediaByTagRequest(tag, page);
		BaseParser parser = new LiveMediaParser();
		List<LiveMediaEntity> list = (List<LiveMediaEntity>) postEntities(request, parser);
		return list;
	}

	/**
	 * 根据标签（央视cctv,卫视tv,高清hd,付费pay）快速选台
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ChannelEntity> getChannelsBySp(String tag) throws ServiceException {
		BasePostRequest request = new GetChannelsBySPRequest(tag);
		BaseParser parser = new ChannelsParser();
		List<ChannelEntity> list = (List<ChannelEntity>) postEntities(request, parser);
		return list;
	}

	/**
	 * 得到欢网推荐的资源，显示在“电视”主页的的推荐位位置
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<RecommandMediaEntity> getRecommendMedias() {
		BasePostRequest request = new RecommendMediasRequest();
		BaseParser parser = new RecommandMediaParser();
		ArrayList<RecommandMediaEntity> list = (ArrayList<RecommandMediaEntity>) postEntities(request, parser);
		return list;
	}

	/***
	 * 根据频道的得到节目单
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProgramEntity> getProgramsByChannel(String channel_code, String start_time, String end_time) {
		BasePostRequest request = new ProgramRequest(channel_code, start_time, end_time);
		BaseParser parser = new ProgramParser();
		List<ProgramEntity> list = (List<ProgramEntity>) postEntities(request, parser);
		return list;
	}

	/**
	 * 根据指定的Request和Parser网络获取BaseEntity及其子类对象集合
	 * 
	 * @author chenlixiao
	 * @param context
	 * @param request
	 *            指定的URL地址
	 * @param parser
	 *            指定的JSON解析器
	 * @return
	 */
	private List<? extends HiveBaseEntity> getEntities(Context context, BaseGetRequest request, BaseParser parser) {
		BaseHttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();
		ArrayList<? extends HiveBaseEntity> list = parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		Log.d(TAG, "0===>" + errorCode);
		if (errorCode.equals(ErrorCode.CODE_SUCCESS)) {
			Log.d(TAG, "1===>" + errorCode);
			return list;
		}
		if (!errorCode.equals(ErrorCode.CODE_SUCCESS)) {
			Log.d(TAG, "2===>" + errorCode);
			// 重新鉴权
			String dateStr = deviceCheck(ApiConstant.APP_VERSION, AppUtil.getLocaldeviceId(context), DeviceBoxUtils.getMac(), ApiConstant.UUID);
			if (dateStr != null) {
				Log.d(TAG, "3===>" + errorCode);
				BaseHttpGetConnector connector1 = new HttpGetConnector(request);
				InputStream in1 = connector1.getGetResponse();
				ArrayList<? extends HiveBaseEntity> list1 = parser.executeToObject(in1);
				Log.d(TAG, "4===>" + errorCode);
				return list1;
			}
		} else {
			Log.d(TAG, "5===>" + errorCode);
			throw new ServiceException(HiveViewErrorCode.E0000605);
		}
		return list;
	}

	/**
	 * log 标签
	 * 
	 * @Fields TAG:TODO
	 */
	private String TAG = "HiveTVService";

	/**
	 * 根据指定的Request和Parser网络获取BaseEntity及其子类对象集合
	 * 
	 * @param request
	 * @param parser
	 * @return
	 */
	private List<? extends HiveBaseEntity> postEntities(BasePostRequest request, BaseParser parser) throws ServiceException {
		BaseHttpPostConnector connector = new HttpPostConnector(request);
		InputStream in = connector.getPostResponse();
		List<? extends HiveBaseEntity> list = parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		Log.d(TAG, "postEntities0===>" + errorCode);
		if(errorCode==null){
			Log.d(TAG, "errorCode为空");
			throw new ServiceException(HiveViewErrorCode.E0000605);
		}
		if (errorCode.equals(ErrorCode.CODE_SUCCESS)) {
			Log.d(TAG, "postEntities1===>" + errorCode);
			return list;
		}
		if (!errorCode.equals(ErrorCode.CODE_SUCCESS)) {
			Log.d(TAG, "postEntities2===>" + errorCode);
			// 重新鉴权
			String dateStr = deviceCheck(ApiConstant.APP_VERSION, AppUtil.getLocaldeviceId(HiveviewApplication.mContext), DeviceBoxUtils.getMac(),
					ApiConstant.UUID);
			if (dateStr != null) {
				Log.d(TAG, "postEntities3===>" + errorCode);
				BaseHttpPostConnector connector1 = new HttpPostConnector(request);
				InputStream in1 = connector1.getPostResponse();
				List<? extends HiveBaseEntity> list1 = parser.executeToObject(in1);
				Log.d(TAG, "postEntities4===>" + errorCode);
				return list1;
			}
		} else {
			Log.d(TAG, "postEntities==+throw new ServiceException(ErrorCode.CODE_E000000);");
			throw new ServiceException(HiveViewErrorCode.E0000605);
		}
		Log.d(TAG, "postEntities5===>" + errorCode);
		return list;
	}

	/**
	 * 得到点播频道的各个子分类，如：电影，电视剧，纪录片，专题，财经等
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<FirstClassListEntity> getFirstClassList(String language) throws ServiceException {
		BaseGetRequest request = new GetFirstClassRequest(language);
		HttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		FirstClassParser parser = new FirstClassParser();
		ArrayList<FirstClassListEntity> list = parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		if (errorCode.equals(ErrorCode.CODE_SUCCESS))
			return list;
		else
			throw new ServiceException(errorCode);
	}

	/**
	 * 得到点播频道的各个子分类，如：电影，电视剧，纪录片，专题，财经等
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<FirstClassListEntity> getVirtualFirstClassList(String language) throws ServiceException {
		BaseGetRequest request = new GetVirtualFirstClassRequest(language);
		HttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		FirstClassParser parser = new FirstClassParser();
		ArrayList<FirstClassListEntity> list = parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		if (errorCode.equals(ErrorCode.CODE_SUCCESS))
			return list;
		else
			throw new ServiceException(errorCode);
	}

	/**
	 * 电影分类
	 * 
	 * @param videoSetType
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<FilmCategory> getFilmCategory(int videoSetType) throws ServiceException {
		BaseGetRequest request = new GetFilmCategoryRequest(videoSetType);
		BaseHttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		FilmCategoryParser parser = new FilmCategoryParser();
		ArrayList<FilmCategory> categories = parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		if (errorCode.equals(ErrorCode.CODE_SUCCESS))
			return categories;
		else
			throw new ServiceException(errorCode);
	}

	/**
	 * 专辑列表接口，如从点播频道（电影，电视剧，综艺等）进入，得到电影或电视剧的列表
	 * 
	 * @param videoSetType
	 *            区别专辑类型,如：videoSetType=16 标示电影
	 * @param pageSize
	 *            每页获取的条数
	 * @param pageNumber
	 *            请求的页数
	 * @return
	 * @throws ServiceException
	 * @throws NoNetWorkException
	 */
	public ArrayList<TotalListFilmNewEntity> getVideoSetList(Context context, int videoSetType, int pageSize, int pageNumber) throws ServiceException {
		BaseGetRequest request = new GetVideoSetListRequest(videoSetType, pageSize, pageNumber);
		FilmDetailNewListParser parser = new FilmDetailNewListParser();
		ArrayList<TotalListFilmNewEntity> list = (ArrayList<TotalListFilmNewEntity>) getEntities(context, request, parser);
		return list;
	}

	/**
	 * 分类筛选
	 * 
	 * @param videoSetType
	 *            区别专辑类型,如：videoSetType=16 标示电影
	 * @param pageSize
	 *            每页获取的条数
	 * @param pageNumber
	 *            请求的页数
	 * @return
	 * @throws ServiceException
	 * @throws NoNetWorkException
	 */
	public ArrayList<TotalListFilmNewEntity> getVideosSetListByTag(Context context, int videoSetType, String tag, int pageSize, int pageNumber)
			throws ServiceException {
		BaseGetRequest request = new GetThirdResultForListRequest(videoSetType, videoSetType + "," + tag, pageSize, pageNumber);
		FilmDetailNewListParser parser = new FilmDetailNewListParser();
		ArrayList<TotalListFilmNewEntity> list = (ArrayList<TotalListFilmNewEntity>) getEntities(context, request, parser);
		return list;
	}

	/**
	 * 专辑列表接口，如从点播频道（电影，电视剧，综艺等）进入，得到电影或电视剧的列表
	 * 
	 * @param videoSetType
	 *            区别专辑类型,如：videoSetType=16 标示电影
	 * @param tag
	 *            根据不同标签筛选影片 比如：华语 动作
	 * @param pageSize
	 *            每页获取的条数
	 * @param pageNumber
	 *            请求的页数
	 * @return
	 * @throws ServiceException
	 * @throws NoNetWorkException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TotalListFilmNewEntity> getVideoSetListByTag(Context context, int videoSetType, String tag, int pageSize, int pageNumber)
			throws ServiceException {
		BaseGetRequest request = new GetVideoSetListByTagRequest(videoSetType, videoSetType + "," + tag, pageSize, pageNumber);
		FilmDetailNewListParser parser = new FilmDetailNewListParser();
		ArrayList<TotalListFilmNewEntity> list = (ArrayList<TotalListFilmNewEntity>) getEntities(context, request, parser);
		return list;
	}

	/**
	 * 欢网接口，根据电视剧名称，查找正在播放该电视剧的电视台
	 * 
	 * @param programName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TVChannelEntity> getTvChannelByProgram(Context context, String programName, String end_time) throws ServiceException {
		BasePostRequest request = new TvChannelByProgramRequest(programName, end_time);
		TVChannelByProgramParser parser = new TVChannelByProgramParser();
		ArrayList<TVChannelEntity> list = (ArrayList<TVChannelEntity>) postEntities(request, parser);
		return list;
	}

	/***
	 * 欢网接口，根据频道的code批量获取直播的节目列表
	 * 
	 * @param codes
	 *            数组对象 如：{"cctv1","cctv2","cctv3"}
	 * @param startTime
	 *            开始时间，默认和没有时间要求传 -->""
	 * @param endTime
	 *            结束时间，默认和没有时间要求传-->""
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<ProgramByCodeEntity> getProgramsByCodes(String[] codes, String startTime, String endTime) throws ServiceException {
		BasePostRequest request = new ProgramByCodeRequest(codes, startTime, endTime);
		ProgramByCodeParser parser = new ProgramByCodeParser();
		@SuppressWarnings("unchecked")
		ArrayList<ProgramByCodeEntity> list = (ArrayList<ProgramByCodeEntity>) postEntities(request, parser);
		return list;
	}

	/**
	 * 根据channelNames 获取 channelCodes
	 * 
	 * @Title: HiveTVService
	 * @author:郭松胜
	 * @Description: TODO
	 * @param names
	 *            频道名称数组
	 * @return
	 */
	public ArrayList<ChannelEntity> getChannelsByNames(String[] names) {
		BasePostRequest request = new GetChannelsByNamesRequest(names);
		GetChannelsByNamesParser parser = new GetChannelsByNamesParser();
		@SuppressWarnings("unchecked")
		ArrayList<ChannelEntity> list = (ArrayList<ChannelEntity>) postEntities(request, parser);
		return list;
	}

	public ArrayList<TotalListFilmEntity> getChannelLiveRecommend(String type, String value, String channelCode) {
		value = "安徽";
		BasePostRequest request = new GetChannelLiveRecommendRequest(type, value, channelCode);
		GetChannelsByNamesParser parser = new GetChannelsByNamesParser();
		@SuppressWarnings("unchecked")
		ArrayList<TotalListFilmEntity> list = (ArrayList<TotalListFilmEntity>) postEntities(request, parser);
		return list;
	}

	/**
	 * 得到各个版块背景图
	 * 
	 * 一共返回5个元素：0->游戏，1->电视，2->推荐，3->影院，4->应用
	 * 
	 * @param programName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<SkinEntity> getPageSkins(Context context) throws ServiceException {
		BaseGetRequest request = new GetSkinsRequest();
		SkinParser parser = new SkinParser();
		ArrayList<SkinEntity> list = (ArrayList<SkinEntity>) getEntities(context, request, parser);
		return list;
	}

	/**
	 * 根据联想词和关键字搜索视频列表 SearchPostRequest
	 * */
	public ArrayList<TotalListSearchEntity> getSearchList(String keyword, int cast_id, int select_model, int record_type, int pageNo, int pageSize)
			throws ServiceException {
		BasePostRequest request = new SearchPostRequest(keyword, cast_id, select_model, record_type, pageNo, pageSize);
		SearchListParser parser = new SearchListParser();
		ArrayList<TotalListSearchEntity> list = (ArrayList<TotalListSearchEntity>) postEntities(request, parser);
		return list;
	}

	/**
	 * 根据用户输入的关键词按人物查询
	 * 
	 * @param keyword
	 *            用户输入的关键词
	 * @param videoSetType
	 *            查询类型（电影-->1，电视剧-->2，片花等,如果不限制传-1）
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<SimpleCastEntity> getCastWithCountByHumanName(String keyword, int videoSetType) throws ServiceException {
		BaseGetRequest request = new GetCastWithCountRequest(keyword, videoSetType);
		GetCastWithCountParser parser = new GetCastWithCountParser();
		ArrayList<SimpleCastEntity> list = (ArrayList<SimpleCastEntity>) getEntities(HiveviewApplication.mContext, request, parser);
		return list;
	}

	/**
	 * 搜索1.0接口
	 * 
	 * @param videoset_type
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<TotalListSearchEntity> getSearchRecord(String selectMode, String type, String keyword, int pageNo, int pageSize)
			throws ServiceException {
		BasePostRequest request = new PostRecordRequest(selectMode, type, keyword, pageNo, pageSize);
		SearchListParser parser = new SearchListParser();
		@SuppressWarnings("unchecked")
		ArrayList<TotalListSearchEntity> list = (ArrayList<TotalListSearchEntity>) postEntities(request, parser);
		return list;
	}

	/**
	 * 3、升级接口
	 * 
	 * @throws Exception
	 * @throws ServiceException
	 */
	public SYSUpDataEntity getLauncherVersionInfo(String version) throws ServiceException {
		try {
			BaseGetRequest request = new UpDataRequest(version);
			BaseHttpGetConnector connector = new HttpGetConnector(request);
			InputStream in = connector.getGetResponse();

			UpDataParser parser = new UpDataParser();
			SYSUpDataEntity entity = parser.executeObject(in);
			String errorCode = parser.getErrorCode();
			if (errorCode.equals("N000000") || entity == null) {
				return entity;
			} else {
				throw new ServiceException(errorCode);
			}
		} catch (ServiceException se) {
			throw new ServiceException(se.getErrorCode());
		} catch (Exception e) {
			throw new ServiceException();
		}
	}

	/**
	 * 系统应用列表接口 http://<Server>/api/open/app/getSystemAppList/<version>.json
	 * 
	 * @throws Exception
	 * @throws ServiceException
	 * */
	public ArrayList<AppMarketEntity> getSystemAppList(String version) throws ServiceException {
		BaseGetRequest request = new SystemAppListRequest(version);
		BaseHttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		AppMarketListParser parser = new AppMarketListParser();
		ArrayList<AppMarketEntity> list = parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		if (errorCode.equals("N000000")) {
			return list;
		} else {
			throw new ServiceException(errorCode);
		}
	}

	public ArrayList<TotalListFilmNewEntity> getSearchList(String url, String pageSize, String pageNum) throws ServiceException {
		SearchRequest request = new SearchRequest(url, pageSize, pageNum);
		SearchParser parser = new SearchParser();
		ArrayList<TotalListFilmNewEntity> list = (ArrayList<TotalListFilmNewEntity>) getEntities(HiveviewApplication.mContext, request, parser);
		return list;
	}

	/**
	 * 静默升级接口
	 * 
	 * @Title: HiveTVService
	 * @author:郭松胜
	 * @Description: TODO
	 * @param model
	 *            设备类型（DM1001）
	 * @param romVersion
	 *            rom版本（1.1）
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<AppMarketEntity> upgrader(String model, String romVersion,String mac,String sn) throws ServiceException {
		BasePostRequest request = new UpgraderRequest(model, romVersion,mac,sn);
		UpgraderListParser parser = new UpgraderListParser();
		@SuppressWarnings("unchecked")
		ArrayList<AppMarketEntity> list = (ArrayList<AppMarketEntity>) postEntities(request, parser);
		return list;
	}

	/**
	 * 获取锁网标示
	 * 
	 * @Title: HiveTVService
	 * @author:郭松胜
	 * @Description: TODO
	 * @param model
	 * @param romVersion
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<NetLockEntity> getLockNet(Context context, String model, String mac, String sn) throws ServiceException {
		PayKeyUtil payKeyUtil = new PayKeyUtil();
		ArrayList<NetLockEntity> list = null;
		// try {
		String privateKey = payKeyUtil.decodePayKey(context, AppConstant.LOCK_NET_PRIVATE_KEY, 5);
		Log.i("LockNet", "suo..............privateKey:" + privateKey);
		// Log.i("LockNet", "getLockNet..............privateKey:" + privateKey);
		String sign = MD5Utils.getMD5String(model + mac + sn + privateKey);
		// Log.i("LockNet", "getLockNet..............sign:" + sign);
		GetLockNetRequest request = new GetLockNetRequest(model, mac, sn, sign);
		GetLockNetParser parser = new GetLockNetParser();
		list = (ArrayList<NetLockEntity>) getEntities(context, request, parser);
		Log.i(TAG, "----------->" + list.size());
		return list;
		// } catch (Exception e) {
		// Log.i("LockNet",
		// "suo..............getLockNet exception."+e.toString());
		// throw new ServiceException();
		// }
	}

	/**
	 * 获取设备码
	 * 
	 * @Title: DevicesInfoService
	 * @author:郭松胜
	 * @Description: TODO
	 * @param mac
	 * @param sn
	 * @return
	 * @throws Exception
	 */
	public String getBoxCode(Context context, String mac, String sn) throws Exception {
		String devicesCode = "";
		try {
			BaseGetRequest request = new GetDeviceCodeRequest(mac, sn);
			GetDevicesCodeParser parser = new GetDevicesCodeParser();
			GetDvicesCodeEntity entity = (GetDvicesCodeEntity) getEntities(context, request, parser);
			if (null != entity) {
				devicesCode = entity.getDevicesCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return devicesCode;
		}
		return devicesCode;
	}
	
	
	
	/**
	 * 获取直播流 test OK
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<LiveStreamEntity> getTvList(String platform_id ,String tv_id , String begin_date, String end_date)
			throws ServiceException {
		BaseGetRequest request = new GetLiveStreamListRequest(platform_id, tv_id,begin_date, end_date);
		BaseHttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		LiveStreamListParser parser = new LiveStreamListParser();
		ArrayList<LiveStreamEntity> list = parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		if (errorCode.equals(ErrorCode.CODE_SUCCESS))
			return list;
		else
			throw new ServiceException(errorCode);
	}
	
	
	/**
	 * 获取直播流 test OK
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<DispatchEntity> getDownLoadURI(String url)
			throws ServiceException {
		BaseGetRequest request = new GetDownLoadURIRequest(url);
		BaseHttpGetConnector connector = new HttpGetConnector(request);
		InputStream in = connector.getGetResponse();

		DispatchParser parser = new DispatchParser();
		ArrayList<DispatchEntity> list = parser.executeToObject(in);
		String errorCode = parser.getErrorCode();
		if (errorCode.equals(ErrorCode.CODE_SUCCESS))
			return list;
		else
			throw new ServiceException(errorCode);
	}
	
	/**
	 * 海外是否冻结盒子接口 test OK
	 * 
	 * @param videosetId
	 * @return
	 * @throws ServiceException
	 */
	public ArrayList<VipLockEntity> getLockVip(Context context, String ClientID,String MacAddress,String SequenceNo,String Timestamp,String Sign) throws ServiceException {
		BasePostRequest request = new PostLockVipRequest(context,ClientID,MacAddress,SequenceNo,Timestamp,Sign);
		PostLockVipParser parser = new PostLockVipParser();
		ArrayList<VipLockEntity> list = (ArrayList<VipLockEntity>) postEntities(request, parser);
		return list;
	}
}
