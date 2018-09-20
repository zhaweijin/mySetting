package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hiveview.tv.service.entity.AppMarketEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.StringUtils;

/**
 * 静默升级类表解析器
 * 
 * @ClassName: UpgraderListParser
 * @Description: TODO
 * @author: guosongsheng
 * @date 2014年9月17日 上午10:14:56
 * 
 */
public class UpgraderListParser extends BaseParser {

	@Override
	public ArrayList<AppMarketEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<AppMarketEntity> appMarketEntities = new ArrayList<AppMarketEntity>();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject jsonObject = new JSONObject(json);
			errorCode = jsonObject.getString("code");
			if (null != jsonObject && errorCode.equals(ErrorCode.CODE_SUCCESS)) {
				//json不为空
				String result = jsonObject.getString("result");
				Gson gson = new Gson();
				Type type = new TypeToken<ArrayList<AppMarketEntity>>() {
				}.getType();
				appMarketEntities = gson.fromJson(result, type);
			} else {
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return appMarketEntities;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
