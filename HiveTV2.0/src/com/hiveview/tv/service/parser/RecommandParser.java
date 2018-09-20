package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.hiveview.tv.service.entity.RecommendEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.StringUtils;

/**
 * “推荐”，”影院“推荐位
 * 
 * @author chenlixiao
 * 
 */
public class RecommandParser extends BaseParser {

	/**
	 * 把字符串解析成推荐位的集合对象
	 */
	@Override
	public ArrayList<RecommendEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<RecommendEntity> list = new ArrayList<RecommendEntity>();
		// 解析成字符串
		String jsonStr = StringUtils.converStreamToString(in);
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			errorCode = jsonObject.getString("code");
			String array = jsonObject.getString("result");
			if (null != array && array.length() > 0 && !array.equals("null")) {// 解析json
				list = JsonUtils.parseArray(array, RecommendEntity.class);
			}
		} catch (JSONException e) {
			throw new ServiceException(HiveViewErrorCode.E0000598);
		}
		Log.d("RecommandParser", "---list:"+list.toString());
		return list;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
