/**
 * @Title BuyRecordListParser.java
 * @Package com.hiveview.user.parser
 * @author 郭松胜
 * @date 2014-6-3 下午4:57:13
 * @Description TODO
 * @version V1.0
 */
package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import android.util.Log;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.service.entity.GetDvicesCodeEntity;
import com.hiveview.tv.utils.StringUtils;
import com.hiveview.weather.service.entity.BaseEntity;

/**
 * @ClassName: BuyRecordListParser
 * @Description: TODO
 * @author: guosongsheng
 * @date 2014-6-3 下午4:57:13
 * 
 */
public class GetDevicesCodeParser extends BaseParser {

	private static final String TAG = "GetDevicesCodeParser";

	/*
	 * (non-Javadoc) 解析json数据
	 * 
	 * @see
	 * com.hiveview.user.parser.BaseParser#executeToObject(java.io.InputStream)
	 */
	@Override
	public ArrayList<? extends HiveBaseEntity> executeToObject(InputStream in) {
		ArrayList<GetDvicesCodeEntity> getDvicesCodeEntities = new ArrayList<GetDvicesCodeEntity>();
		GetDvicesCodeEntity getDvicesCodeEntity = new GetDvicesCodeEntity();
		String json = StringUtils.converStreamToString(in);
		Log.i(TAG, "GetDevicesCodeParser------>" + json);
		if (null != json && json.length() > 0) {
			// 设备码
			getDvicesCodeEntity.setDevicesCode(json);
			getDvicesCodeEntities.add(getDvicesCodeEntity);
		}
		return getDvicesCodeEntities;
	}

	@Override
	public String getErrorCode() {
		// TODO Auto-generated method stub
		return null;
	}
}
