package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.StringUtils;

public class QiyiFilmRecommandParser extends BaseParser {

	@Override
	public ArrayList<FilmEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<FilmEntity> arrayList = new ArrayList<FilmEntity>();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject jsonObject = new JSONObject(json);
			String result = jsonObject.getString("data");
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				FilmEntity entity = new FilmEntity();
				entity.setVideoset_type(1);
				
				// 影片简介
				entity.setVideoset_brief(obj.getString("albumDesc"));
				// vrsAlbumId 和 vrsTvId 调用奇异播放器的时候会用到
				entity.setCp_videoset_id(obj.getString("vrsTvId") + "," + obj.getString("vrsAlbumId"));
				// 主演信息
				JSONObject actors = new JSONObject(obj.getString("albumProducer"));
				entity.setActors(actors.getString("mainActor"));
				// 导演信息
				entity.setDirector(actors.getString("director"));

				// 影片的tag
				entity.setTag(obj.getString("tag"));
				// 上映时间
				entity.setIs_suetime(obj.getString("issueTime"));
				// 影片名称
				entity.setVideoset_name(obj.getString("albumName"));
				// 影片时长
				entity.setTime_length(obj.getInt("playLength"));
				// 影片海报图
				entity.setVideoset_img(obj.getString("albumPic"));

				arrayList.add(entity);
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

		return arrayList;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
