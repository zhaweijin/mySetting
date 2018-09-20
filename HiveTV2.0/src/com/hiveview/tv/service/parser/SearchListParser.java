package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.hiveview.tv.service.entity.SearchEntity;
import com.hiveview.tv.service.entity.TotalListSearchEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

public class SearchListParser extends BaseParser {

	@Override
	public ArrayList<TotalListSearchEntity> executeToObject(InputStream in)
																	throws ServiceException {
		ArrayList<TotalListSearchEntity> totalList = new ArrayList<TotalListSearchEntity>();
		TotalListSearchEntity totalSearchEntity = new TotalListSearchEntity();
		ArrayList<SearchEntity> arrayList = new ArrayList<SearchEntity>();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject jsonObject = new JSONObject(json);
			JSONObject jsonObjectData=jsonObject.optJSONObject("data");
			errorCode = jsonObjectData.getString("code");
			JSONObject result = jsonObjectData.getJSONObject("result");
			int recount = result.getInt("recCount");
			JSONArray jsonArray = result.getJSONArray("pageContent");
			arrayList= JsonUtils.parseArray(jsonArray.toString(), SearchEntity.class);
			totalSearchEntity.setRecCount(recount);
			totalSearchEntity.setFilms(arrayList);
			totalList.add(totalSearchEntity);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
		return totalList;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
