package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.hiveview.tv.service.entity.ChannelEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.StringUtils;

public class ChannelsParser extends HuanWangBaseParser {
	private static final String TAG = "ChannelsParser";
	@Override
	public ArrayList<ChannelEntity> executeToObject(InputStream in) throws ServiceException {

		ArrayList<ChannelEntity> list = new ArrayList<ChannelEntity>();
		// 解析成字符串
		String jsonStr = StringUtils.converStreamToString(in);
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			Log.i(TAG,  jsonObject.toString());
			setErrorCode(jsonObject);
			String arrayStr = jsonObject.getString("channels");
			if (null != arrayStr && !arrayStr.equals("null") && !arrayStr.equals("")) {
				list = JsonUtils.parseArray(arrayStr, ChannelEntity.class);
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
