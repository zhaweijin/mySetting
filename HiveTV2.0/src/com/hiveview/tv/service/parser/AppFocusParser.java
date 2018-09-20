package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.AppFocusEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.StringUtils;

public class AppFocusParser extends BaseParser {
	@Override
	public ArrayList<AppFocusEntity> executeToObject(InputStream in) throws ServiceException {

		ArrayList<AppFocusEntity> list = null;
		// 解析成字符串
		String jsonStr = StringUtils.converStreamToString(in);
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			JSONObject jsonObjectData=jsonObject.optJSONObject("data");
			errorCode = jsonObjectData.getString("code");
			JSONArray result = jsonObjectData.getJSONArray("result");			
//			if (null != result && !"null".equals(result) && !"".equals(result)) { 这里会产生，两个不同对象的比较
			if(null != result && result instanceof JSONArray){
				list = JsonUtils.parseArray(result.toString(), AppFocusEntity.class);
			}
		} catch (JSONException e) {
			LogUtil.info(e.toString());
			throw new ServiceException(HiveViewErrorCode.E0000598);
		}

		return list;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}
}
