package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hiveview.tv.service.entity.SearchEntity;
import com.hiveview.tv.service.entity.SearchPagerEntity;
import com.hiveview.tv.service.entity.SearchPagerViewItemEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

/**
 * 搜索列表页面的 解析类
 * @author zhangpengzhan
 *
 * 2014年4月21日
 *	上午10:54:33
 */
public class SearchPagerParser {
	
	/**
	 * 网络返回的状态码
	 */
	private String errorCode = "";

	public SearchPagerEntity executeToObject(InputStream in)throws ServiceException {
		ArrayList<SearchPagerViewItemEntity> totalList = new ArrayList<SearchPagerViewItemEntity>();
		SearchPagerEntity entity = new SearchPagerEntity();
	
		
		try {
			Gson gson = new Gson();
			String json = StringUtils.converStreamToString(in);
			System.out.println("===search-json=="+json);
			JSONObject jsonObject = new JSONObject(json);
			errorCode = jsonObject.getString("code");
			entity.setCode(errorCode);
			entity.setLongTime(jsonObject.getLong("longTime"));
		//	entity.setMessage(jsonObject.getString("message")+"");
			entity.setStringTime(jsonObject.getString("stringTime"));
			//返回的结果 数据 result
			JSONObject result = jsonObject.getJSONObject("result");
		
			entity.setPageNo(result.getInt("pageNo"));
			entity.setPageSize(result.getInt("pageSize"));
			entity.setPageCount(result.getInt("pageCount"));
			entity.setRecCount(result.getInt("recCount"));
			// view 数据
			JSONArray jsonArray = result.getJSONArray("pageContent");
			//获取数据列表
			Type type = new TypeToken<List<SearchPagerViewItemEntity>>(){}.getType();
			totalList = gson.fromJson(jsonArray.toString(), type);
			entity.setPagerViewItemEntities(totalList);
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return entity;
	}

	
	public String getErrorCode() {
		return errorCode;
	}

}
