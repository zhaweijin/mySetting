package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.VideoNewEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

public class VideoListParser extends BaseParser {

	@Override
	public ArrayList<VideoNewEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<VideoNewEntity> list = new ArrayList<VideoNewEntity>();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject object = new JSONObject(json);
			JSONObject jsonObject = object.optJSONObject("data");
			
			errorCode = jsonObject.getString("code");

			// JSONObject jsonResult=new
			// JSONObject(jsonObject.getString("result"));
			JSONArray array = jsonObject.getJSONArray("result");// pageContent
			list = JsonUtils.parseArray(array.toString(), VideoNewEntity.class);
			return list;
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}

		
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
