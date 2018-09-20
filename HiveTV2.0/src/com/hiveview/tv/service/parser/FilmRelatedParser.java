package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.CastEntity;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmRelatedEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

public class FilmRelatedParser extends BaseParser {

	@Override
	public ArrayList<FilmRelatedEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<FilmRelatedEntity> entities = new ArrayList<FilmRelatedEntity>();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject jsonObject = new JSONObject(json);
			errorCode = jsonObject.getString("code");
			List<CastEntity> castList = new ArrayList<CastEntity>();
			if(jsonObject.getJSONObject("result").has("castList")){
				JSONArray castListJson = jsonObject.getJSONObject("result").getJSONArray("castList");
				castList = JsonUtils.parseArray(castListJson.toString(), CastEntity.class);
			}
			JSONArray videsetListJson = jsonObject.getJSONObject("result").getJSONArray("videsetList");
			List<FilmEntity> videsetList = JsonUtils.parseArray(videsetListJson.toString(), FilmEntity.class);
			
			FilmRelatedEntity entity = new FilmRelatedEntity();
			entity.setCastList(castList);
			entity.setVidesetList(videsetList);

			if(castList.size()!=0 || videsetList.size()!=0){
				entities.add(entity);
			}
			
		} catch (JSONException e) {
			throw new ServiceException(e.getMessage());
		} catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
		return entities;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
