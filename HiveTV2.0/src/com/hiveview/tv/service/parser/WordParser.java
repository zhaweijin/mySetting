package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.hiveview.tv.service.entity.WordsEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.StringUtils;

public class WordParser extends BaseParser {

	private String tag = "WordParser";

	@Override
	public ArrayList<WordsEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<WordsEntity> list = new ArrayList<WordsEntity>();
		// 解析成字符串
		String jsonStr = StringUtils.converStreamToString(in);
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			JSONObject jsonObjectData=jsonObject.optJSONObject("data");
			errorCode = jsonObjectData.getString("code");
			String arrayStr = jsonObjectData.optString("result");
			Log.d(tag, "2arrayStr::" + arrayStr);
			if (StringUtils.isEmpty(arrayStr)) {
				return list;
			}
			JSONArray array = new JSONArray(arrayStr);
			if (array.length() > 0) {
				for (int i = 0; i < array.length(); i++) {
					WordsEntity entity = new WordsEntity();
					entity.setHotWord(array.getString(i));
					list.add(entity);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return list;
		} catch (Exception e) {
			throw new ServiceException(HiveViewErrorCode.E0000598);
		}

		return list;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
