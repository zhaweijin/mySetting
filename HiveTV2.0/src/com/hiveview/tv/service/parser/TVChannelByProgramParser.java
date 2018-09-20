package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.ChannelCategoryEntity;
import com.hiveview.tv.service.entity.TVChannelEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

public class TVChannelByProgramParser extends HuanWangBaseParser {

	@Override
	public ArrayList<TVChannelEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<TVChannelEntity> list = new ArrayList<TVChannelEntity>();
		// 解析成字符串
		String jsonStr = StringUtils.converStreamToString(in);
		try {

			JSONObject jsonObject = new JSONObject(jsonStr);
			setErrorCode(jsonObject);
			String arrayStr = jsonObject.getString("programs");
			list = JsonUtils.parseArray(arrayStr, TVChannelEntity.class);
		} catch (JSONException e) {
			throw new ServiceException(HiveViewErrorCode.E0000598);
		}

		return list;
	}
}
