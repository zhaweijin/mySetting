package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.StringUtils;

public class FilmDetailParser extends BaseParser {

	private String tag = "FilmDetailParser";

	@Override
	public ArrayList<FilmNewEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<FilmNewEntity> arrayList = new ArrayList<FilmNewEntity>();
		try {
			Gson gson = new Gson();
			String json = StringUtils.converStreamToString(in);
			JSONObject object = new JSONObject(json);
			JSONObject jsonObject = object.optJSONObject("data");
			errorCode = jsonObject.getString("code");
			// Log.d(tag, "jsonObject::"+json);
			if (json.contains("result")) {
				JSONObject result = jsonObject.optJSONObject("result");
				if (null != result && !"".equals(result) && !"null".equals(result)) {
					Log.d(tag, "jsonObject::" + result.toString());
					// 获取数据列表
//					Log.d(tag, "result.optString(actors)::" + result.optString("actors"));
//					Log.d(tag, "videoset_focus::" + result.optString("videoset_focus"));
//					Log.d(tag, "videoset_name::" + result.optString("videoset_name"));
//					Log.d(tag, "videoset_brief::" + result.optString("videoset_brief"));

					arrayList.add(gson.fromJson(result.toString(), FilmNewEntity.class));
					return arrayList;
				} else {
					throw new ServiceException(HiveViewErrorCode.E0000598);
				}
			} else {
				throw new ServiceException(HiveViewErrorCode.E0000598);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(HiveViewErrorCode.E0000598);
		}

	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
