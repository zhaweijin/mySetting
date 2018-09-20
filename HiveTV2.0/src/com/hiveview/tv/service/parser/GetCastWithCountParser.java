package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.SimpleCastEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

public class GetCastWithCountParser extends BaseParser {

	@Override
	public  ArrayList<SimpleCastEntity> executeToObject(InputStream in)
																	throws ServiceException {
		ArrayList<SimpleCastEntity> totalList = new ArrayList<SimpleCastEntity>();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject jsonObject = new JSONObject(json);
			errorCode = jsonObject.getString("code");
			String result = jsonObject.getString("result");
			totalList= JsonUtils.parseArray(result, SimpleCastEntity.class);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		
		return totalList;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
