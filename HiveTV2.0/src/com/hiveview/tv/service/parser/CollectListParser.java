package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmListEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

public class CollectListParser extends BaseParser {

	@Override
	public ArrayList<FilmListEntity> executeToObject(InputStream in)
			throws ServiceException {
		
		ArrayList<FilmListEntity> arrayList = new ArrayList<FilmListEntity>();
		try {
			String json = StringUtils.converStreamToString(in);
			FilmListEntity entity;
			JSONObject jsonObject = new JSONObject(json);
			
			errorCode = jsonObject.getString("code");
			JSONObject obj = jsonObject.getJSONObject("result");

			entity = new FilmListEntity();
			entity.setPageCount(obj.getInt("pageCount"));
			entity.setPageNo(obj.getInt("pageNo"));
			entity.setPageSize(obj.getInt("pageSize"));
			entity.setRecCount(obj.getInt("recCount"));
			
			JSONArray array = obj.getJSONArray("pageContent");
			List<FilmEntity> filmEntities = new ArrayList<FilmEntity>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject filmObj = array.getJSONObject(i).getJSONObject("videosetVo");
				FilmEntity filmEntity = JsonUtils.parseObject(filmObj.toString(), FilmEntity.class);
				filmEntities.add(filmEntity);
			}
			
			entity.setPageContent(filmEntities);
			arrayList.add(entity);
			
		} catch (JSONException e) {
			throw new ServiceException(HiveViewErrorCode.E0000598);
		} catch (Exception e){
			throw new ServiceException(HiveViewErrorCode.E0000598);
		}
		return arrayList;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
