package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.ChannelCategoryEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.StringUtils;

public class ChannelCategoryParser extends HuanWangBaseParser {

	@Override
	public ArrayList<ChannelCategoryEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<ChannelCategoryEntity> list = new ArrayList<ChannelCategoryEntity>();
		// 解析成字符串
		String jsonStr = StringUtils.converStreamToString(in);
		try {

			JSONObject jsonObject = new JSONObject(jsonStr);
			setErrorCode(jsonObject);
			String arrayStr = jsonObject.getString("class");
			JSONObject jsonClass = new JSONObject(arrayStr);
			Iterator<String> iterator = jsonClass.keys();

			String key = "", value = "";
			while (iterator.hasNext()) {
				ChannelCategoryEntity entity = new ChannelCategoryEntity();
				key = iterator.next().toString();
				entity.setCategoryCode(key);
				value = jsonClass.getString(key);
				entity.setCategoryName(value);
				list.add(entity);
			}

		} catch (JSONException e) {
			throw new ServiceException(HiveViewErrorCode.E0000598);
		}

		return list;
	}
}
