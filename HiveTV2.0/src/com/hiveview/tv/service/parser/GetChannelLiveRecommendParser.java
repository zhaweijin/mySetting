package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.ChannelEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.StringUtils;

public class GetChannelLiveRecommendParser extends HuanWangBaseParser {
	@Override
	public ArrayList<ChannelEntity> executeToObject(InputStream in) throws ServiceException {

		ArrayList<ChannelEntity> list = new ArrayList<ChannelEntity>();
		// 解析成字符串
		String jsonStr = StringUtils.converStreamToString(in);
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			setErrorCode(jsonObject);
			String arrayStr = jsonObject.getString("channels");
			list = JsonUtils.parseArray(arrayStr, ChannelEntity.class);
		} catch (JSONException e) {
			LogUtil.info(e.toString());
			return list;
			//throw new ServiceException(ErrorCode.CODE_JOSN_PARSER_ERROR);
		}

		return list;
	}

}
