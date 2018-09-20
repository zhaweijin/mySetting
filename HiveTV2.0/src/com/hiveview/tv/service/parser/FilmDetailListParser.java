package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.TotalListFilmEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

public class FilmDetailListParser extends BaseParser {

	@Override
	public ArrayList<TotalListFilmEntity> executeToObject(InputStream in)
																	throws ServiceException {
		ArrayList<TotalListFilmEntity> totalList = new ArrayList<TotalListFilmEntity>();
		TotalListFilmEntity totalFilmEntity = new TotalListFilmEntity();
		ArrayList<FilmEntity> arrayList = new ArrayList<FilmEntity>();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject jsonObject = new JSONObject(json);
			errorCode = jsonObject.getString("code");
			JSONObject result = jsonObject.getJSONObject("result");
			JSONArray jsonArray = result.getJSONArray("pageContent");
			int recCount = result.getInt("recCount");
			arrayList= JsonUtils.parseArray(jsonArray.toString(), FilmEntity.class);
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
