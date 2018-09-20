package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.hiveview.tv.service.entity.ProgramEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.StringUtils;

public class ProgramParser extends HuanWangBaseParser {
	private static final String TAG="ProgramParser";
	@Override
	public ArrayList<ProgramEntity> executeToObject(InputStream in) throws ServiceException {

		ArrayList<ProgramEntity> list = new ArrayList<ProgramEntity>();
		// 解析成字符串
		String jsonStr = StringUtils.converStreamToString(in);
		
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			Log.i(TAG, jsonObject.toString());
			setErrorCode(jsonObject);
			String arrayStr = jsonObject.getString("program");
			Log.v(TAG, arrayStr);
			list = JsonUtils.parseArray(arrayStr, ProgramEntity.class);
		} catch (JSONException e) {
			LogUtil.info(e.toString());
			return list;
			//throw new ServiceException(ErrorCode.CODE_JOSN_PARSER_ERROR);
		}

		return list;
	}

}
