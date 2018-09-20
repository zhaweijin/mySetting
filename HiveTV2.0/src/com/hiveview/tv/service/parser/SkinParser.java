package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.SkinEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

public class SkinParser extends BaseParser {

	@Override
	public ArrayList<SkinEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<SkinEntity> arrayList = new ArrayList<SkinEntity>();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject jsonObject = new JSONObject(json);
			errorCode = jsonObject.getString("code");

			String result = jsonObject.getString("result");
			arrayList = JsonUtils.parseArray(result.toString(), SkinEntity.class);
		} catch (JSONException e) {
			e.printStackTrace();
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
