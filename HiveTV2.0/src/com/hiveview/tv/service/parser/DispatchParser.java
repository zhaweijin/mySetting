/**
 * @Title DispatchParser.java
 * @Package com.hiveview.cloudscreen.videolive.parser
 * @author haozening
 * @date 2015-2-5 上午10:13:44
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.service.entity.DispatchEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.StringUtils;

/**
 * @ClassName DispatchParser
 * @Description
 * @author haozening
 * @date 2015-2-5 上午10:13:44
 * 
 */
public class DispatchParser extends BaseParser {


	@Override
	public ArrayList<DispatchEntity> executeToObject(InputStream in)
			throws ServiceException {
		// TODO Auto-generated method stub
		String json = StringUtils.converStreamToString(in);
		JSONObject jsonObject;
		ArrayList<DispatchEntity> entity = new ArrayList<DispatchEntity>();
		DispatchEntity dispatchEntity = new DispatchEntity();
		try {
			jsonObject = new JSONObject(json);
			errorCode="N000000";
			dispatchEntity.setU(jsonObject.getString("u"));
			entity.add(dispatchEntity);
			return entity;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entity;
	}

	@Override
	public String getErrorCode() {
		// TODO Auto-generated method stub
		return errorCode;
	}

}
