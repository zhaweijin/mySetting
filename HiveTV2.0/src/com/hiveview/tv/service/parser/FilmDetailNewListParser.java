package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.TotalListFilmEntity;
import com.hiveview.tv.service.entity.TotalListFilmNewEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

public class FilmDetailNewListParser extends BaseParser {

	@Override
	public ArrayList<TotalListFilmNewEntity> executeToObject(InputStream in)
																	throws ServiceException {
		ArrayList<TotalListFilmNewEntity> totalList = new ArrayList<TotalListFilmNewEntity>();
		TotalListFilmNewEntity totalFilmEntity = new TotalListFilmNewEntity();
		ArrayList<FilmNewEntity> arrayList = new ArrayList<FilmNewEntity>();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject object = new JSONObject(json);
			JSONObject jsonObject = object.optJSONObject("data");
			errorCode = jsonObject.getString("code");
			JSONObject result = jsonObject.getJSONObject("result");
			JSONArray jsonArray = result.getJSONArray("pageContent");
			int recCount = result.getInt("recCount");
			arrayList= JsonUtils.parseArray(jsonArray.toString(), FilmNewEntity.class);
			totalFilmEntity.setRecCount(recCount);
			totalFilmEntity.setFilms(arrayList);
			totalList.add(totalFilmEntity);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ServiceException(HiveViewErrorCode.E0000598);
		} catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(HiveViewErrorCode.E0000598);
		}
		
		return totalList;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
