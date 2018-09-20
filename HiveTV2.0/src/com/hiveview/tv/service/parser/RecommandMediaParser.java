package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.RecommandMediaEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.StringUtils;

public class RecommandMediaParser extends HuanWangBaseParser {
	@Override
	public ArrayList<RecommandMediaEntity> executeToObject(InputStream in) throws ServiceException {

		ArrayList<RecommandMediaEntity> list = new ArrayList<RecommandMediaEntity>();
		// 解析成字符串
		String jsonStr = StringUtils.converStreamToString(in);
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			setErrorCode(jsonObject);
			String arrayStr = jsonObject.getString("media");
			if (null != arrayStr && !arrayStr.equals("null") && !arrayStr.equals("")) {
				list = JsonUtils.parseArray(arrayStr, RecommandMediaEntity.class);
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
