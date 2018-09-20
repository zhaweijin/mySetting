package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;

public class HuanWangBaseParser extends BaseParser {

	@Override
	public ArrayList<? extends HiveBaseEntity> executeToObject(InputStream in) throws ServiceException {
		return null;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * 设置由欢网返回的Code值并转化为当前App的code
	 * 
	 * @param jsonStr
	 */
	public void setErrorCode(JSONObject json) {
		try {
			String errorStr = json.getString("error");
			JSONObject jsonError = new JSONObject(errorStr);
			errorCode = jsonError.getInt("code") == 0 ? ErrorCode.CODE_SUCCESS : HiveViewErrorCode.E0000605;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
