package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

public class QiyiFilmRecommandNewParser extends BaseParser {

	@Override
	public ArrayList<FilmNewEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<FilmNewEntity> arrayList = new ArrayList<FilmNewEntity>();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject object = new JSONObject(json);
			JSONObject jsonObject = object.optJSONObject("data");
			errorCode = jsonObject.getString("code");
			JSONArray array = jsonObject.optJSONArray("result");
			arrayList= JsonUtils.parseArray(array.toString(), FilmNewEntity.class);
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
