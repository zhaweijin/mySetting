package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.hiveview.tv.service.entity.FirstClassListEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.StringUtils;

public class FirstClassParser extends BaseParser {

	@Override
	public ArrayList<FirstClassListEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<FirstClassListEntity> list = new ArrayList<FirstClassListEntity>();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject jsonObject = new JSONObject(json);
			JSONObject jsonObjectData=jsonObject.optJSONObject("data");
			errorCode = jsonObjectData.getString("code");
		
			JSONArray jsonArray = jsonObjectData.getJSONArray("result");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject fcObj = jsonArray.getJSONObject(i);
				FirstClassListEntity entity = new FirstClassListEntity();

				entity.setFirstclass_id(fcObj.optInt("firstclassId"));
				entity.setFirstclass_name(fcObj.optString("firstclassName"));
				entity.setPic(fcObj.optString("iconImg"));
				entity.setIcon(fcObj.optString("pic"));
				entity.setShow_type(fcObj.optInt("showType"));
				list.add(entity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		return list;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
