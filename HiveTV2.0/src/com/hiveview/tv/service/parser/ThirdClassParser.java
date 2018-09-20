package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hiveview.tv.service.entity.ThirdListResultEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.StringUtils;

/**
 * 分类搜索的json的解析
 * @ClassName: ThirdClassParser
 * @Description: 
 * @author: zhangpengzhan
 * @date 2014年7月4日 上午10:18:03
 * 
 */
public class ThirdClassParser extends BaseParser {

	@Override
	public ArrayList<ThirdListResultEntity> executeToObject(InputStream in)
																	throws ServiceException {
		//返回的数据列表
		ArrayList<ThirdListResultEntity> arrayList = new ArrayList<ThirdListResultEntity>();
		try {
			//gson解析
			Gson gson = new Gson();
			//数据流转换字符串
			String json = StringUtils.converStreamToString(in);
			//字符串转换成json
			JSONObject object = new JSONObject(json);
			JSONObject jsonObject = object.optJSONObject("data");
			//提起错误码
			errorCode = jsonObject.getString("code");
			if(json.contains("result")){
				JSONArray result = jsonObject.optJSONArray("result");
				//解析数据
				arrayList = gson.fromJson(result.toString(), new TypeToken<List<ThirdListResultEntity>>(){}.getType());
			}else{
				return arrayList;
			}
		} catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
		
		return arrayList;
	}

	//返回错误码
	public String getErrorCode() {
		return errorCode;
	}

}
