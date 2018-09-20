package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.hiveview.tv.service.entity.ProgramByCodeEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.StringUtils;

public class ProgramByCodeParser extends HuanWangBaseParser {
	/**
	 * TAG
	 */
	private static final String TAG = "ProgramByCodeParser";

	@Override
	public ArrayList<ProgramByCodeEntity> executeToObject(InputStream in) throws ServiceException {

		ArrayList<ProgramByCodeEntity> list = new ArrayList<ProgramByCodeEntity>();
		// 解析成字符串
		String jsonStr = StringUtils.converStreamToString(in);
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			Log.i(TAG, jsonObject.toString());
			setErrorCode(jsonObject);
			String arrayStr = jsonObject.getString("programs");
			list = JsonUtils.parseArray(arrayStr, ProgramByCodeEntity.class);
		} catch (JSONException e) {
			throw new ServiceException(HiveViewErrorCode.E0000598);
		} catch(Exception e){
			e.printStackTrace();
		}

		return list;
	}

}
