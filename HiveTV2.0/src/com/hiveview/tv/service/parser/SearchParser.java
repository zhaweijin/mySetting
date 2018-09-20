/**
 * @Title SearchParser.java
 * @Package com.hiveview.tv.service.parser
 * @author haozening
 * @date 2014年8月7日 下午7:09:27
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.TotalListFilmNewEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.StringUtils;

/**
 * @ClassName SearchParser
 * @Description 
 * @author haozening
 * @date 2014年8月7日 下午7:09:27
 * 
 */
public class SearchParser extends BaseParser {

	@Override
	public ArrayList<TotalListFilmNewEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<TotalListFilmNewEntity> totalList = new ArrayList<TotalListFilmNewEntity>();
		String json = StringUtils.converStreamToString(in);
		try {
			JSONObject jsonObject = new JSONObject(json);
			int count = jsonObject.optInt("totalCount");
			JSONArray jsonArray = jsonObject.optJSONArray("videoList");
			if (null != jsonArray) {
				TotalListFilmNewEntity entity = new TotalListFilmNewEntity();
				ArrayList<FilmNewEntity> list = new ArrayList<FilmNewEntity>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					FilmNewEntity filmEntity = new FilmNewEntity();
					filmEntity.setId(object.optInt("videoid"));
					filmEntity.setPosterUrl(object.optString("imgurl"));
					filmEntity.setName(object.optString("name"));
					list.add(filmEntity);
				}
				entity.setRecCount(count);
				entity.setFilms(list);
				totalList.add(entity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
		return totalList;
	}

	@Override
	public String getErrorCode() {
		return ErrorCode.CODE_SUCCESS;
	}

}
