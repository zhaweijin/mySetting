package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.WordsEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.StringUtils;

public class LiveCategoryTagsParser extends HuanWangBaseParser {

	@Override
	public ArrayList<WordsEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<WordsEntity> list = new ArrayList<WordsEntity>();
		// 解析成字符串
		String jsonStr = StringUtils.converStreamToString(in);
		try {

			JSONObject jsonObject = new JSONObject(jsonStr);
			setErrorCode(jsonObject);
			String arrayStr = jsonObject.getString("class");
			JSONArray array = new JSONArray(arrayStr);
			if (array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					WordsEntity entity = new WordsEntity();
					entity.setHotWord(array.getString(i));
					list.add(entity);
				}
			}

		} catch (JSONException e) {
			throw new ServiceException(HiveViewErrorCode.E0000598);
		}

		return list;
	}

	
}
