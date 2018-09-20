package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.CastEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

public class CastInfoParser extends BaseParser {

	@Override
	public ArrayList<CastEntity> executeToObject(InputStream in)
																	throws ServiceException {
		ArrayList<CastEntity> arrayList = new ArrayList<CastEntity>();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject jsonObject = new JSONObject(json);
			errorCode = jsonObject.getString("code");
			System.out.println(json);
			JSONObject result = jsonObject.getJSONObject("result");
			CastEntity entity = JsonUtils.parseObject(result.toString(), CastEntity.class);
			
			arrayList.add(entity);
		} catch (JSONException e) {
			
		} catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
		
		return arrayList;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
