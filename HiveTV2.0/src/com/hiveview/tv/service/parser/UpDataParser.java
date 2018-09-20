package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.AppMarketEntity;
import com.hiveview.tv.service.entity.SYSUpDataEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.StringUtils;

public class UpDataParser extends BaseParser{

	@Override
	public ArrayList<AppMarketEntity> executeToObject(InputStream in) throws ServiceException {
		return null;
	}

	public SYSUpDataEntity executeObject(InputStream in) throws ServiceException {
		String jsonStr = StringUtils.converStreamToString(in);
		SYSUpDataEntity entity = new SYSUpDataEntity();
		
		try {
			JSONObject obj = new JSONObject(jsonStr);
			
//			JSONObject dataObj = obj.getJSONObject("data");
			errorCode = obj.optString("code");
			
			JSONObject resultObj = obj.optJSONObject("result");
			if (null == resultObj) {
				return null;
			}
			
			entity.setVersion(resultObj.optString("version"));
			entity.setUpdateTime(resultObj.optInt("updateTime"));
			entity.setType(resultObj.optInt("type"));
			entity.setFeatures(resultObj.optString("features"));
			entity.setUrl(resultObj.optString("url"));
			entity.setMd5(resultObj.optString("md5"));
			entity.setSize(resultObj.optInt("size"));
				
		} catch (JSONException e) {
			throw new ServiceException();
		}
		return entity;
	}
	
	@Override
	public String getErrorCode(){
		
		return errorCode;
		
	}

}
