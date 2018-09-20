package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.CastEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

public class CastParser extends BaseParser {

	@Override
	public ArrayList<CastEntity> executeToObject(InputStream in)
			throws ServiceException {
		ArrayList<CastEntity> entities = new ArrayList<CastEntity>();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject jsonObject = new JSONObject(json);
			errorCode = jsonObject.getString("code");
			
			JSONObject result = jsonObject.getJSONObject("result");
			CastEntity castEntity = JsonUtils.parseObject(result.toString(), CastEntity.class);
			
			entities.add(castEntity);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
		return entities;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
