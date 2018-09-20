package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.SubjectInfo;
import com.hiveview.tv.service.entity.SubjectInfoList;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.StringUtils;

public class SubjectInfoParser extends BaseParser {

	@Override
	public ArrayList<SubjectInfoList> executeToObject(InputStream in)
			throws ServiceException {
		ArrayList<SubjectInfoList> arrayList = new ArrayList<SubjectInfoList>();
		SubjectInfoList entity;
		
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject jsonObject = new JSONObject(json);
			JSONObject jsonObjectData=jsonObject.optJSONObject("data");
			errorCode = jsonObjectData.getString("code");
			JSONObject obj = jsonObjectData.getJSONObject("result");

			entity = new SubjectInfoList();
			entity.setPageCount(obj.getInt("pageCount"));
			entity.setPageNo(obj.getInt("pageNo"));
			entity.setPageSize(obj.getInt("pageSize"));
			entity.setRecCount(obj.getInt("recCount"));
			
			JSONArray array = obj.getJSONArray("pageContent");
			ArrayList<SubjectInfo> SubjectInfos = new ArrayList<SubjectInfo>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject filmObj = array.getJSONObject(i);
				SubjectInfo subjectInfo = JsonUtils.parseObject(filmObj.toString(), SubjectInfo.class);
				SubjectInfos.add(subjectInfo);
			}
			
			entity.setPageContent(SubjectInfos);
			arrayList.add(entity);
			LogUtil.info(arrayList.toString());
			
		} catch (JSONException e) {
			throw new ServiceException(HiveViewErrorCode.E0000598);
		} catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
		
		return arrayList;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
