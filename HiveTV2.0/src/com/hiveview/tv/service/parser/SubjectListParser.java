package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.hiveview.tv.service.entity.SubjectEntity;
import com.hiveview.tv.service.entity.SubjectListEntity;
import com.hiveview.tv.service.entity.SubjectListEntitys;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

public class SubjectListParser extends BaseParser {

	@Override
	public ArrayList<SubjectListEntitys> executeToObject(InputStream in)
			throws ServiceException {
		ArrayList<SubjectListEntitys> arrayList = new ArrayList<SubjectListEntitys>();
		SubjectListEntitys entity;
		
		try {
			Log.v("SubjectListParser", "SubjectListParser1");
			String json = StringUtils.converStreamToString(in);
		
			JSONObject jsonObject = new JSONObject(json);
			JSONObject jsonObjectData=jsonObject.optJSONObject("data");
			errorCode = jsonObjectData.getString("code");
			JSONObject obj = jsonObjectData.getJSONObject("result");
		
			entity = new SubjectListEntitys();
			entity.setPageCount(obj.getInt("pageCount"));
			entity.setPageNo(obj.getInt("pageNo"));
			entity.setPageSize(obj.getInt("pageSize"));
			entity.setRecCount(obj.getInt("recCount"));
			
			JSONArray array = obj.getJSONArray("pageContent");
			List<SubjectListEntity> subjectEntities = new ArrayList<SubjectListEntity>();
			for (int i = 0; i < array.length(); i++) {
//				SubjectListEntity filmEntity=new SubjectListEntity();
				JSONObject filmObj = array.getJSONObject(i);
//				filmEntity.setCreatedTime(filmObj.getString("createdTime"));
//				filmEntity.setImgSize(filmObj.getString("imgSize"));
//				filmEntity.setIsEffective(filmObj.getInt("isEffective"));
//				filmEntity.setLauncherId(filmObj.getString("launcherId"));
//				filmEntity.setLauncherId(filmObj.getString("launcherName"));
//				filmEntity.setSeq(filmObj.getInt("seq"));
//				filmEntity.setSubjectBgImg(filmObj.getString("subjectBgImg"));
//				filmEntity.setSubjectDesc(filmObj.getString("subjectDesc"));
//				filmEntity.setSubjectId(filmObj.getInt("subjectId"));
//				filmEntity.setSubjectName(filmObj.getString("subjectName"));
//				filmEntity.setSubjectPic(filmObj.getString("subjectPic"));
//				filmEntity.setUpdatedTime(filmObj.getString("updatedTime"));
//				JSONArray subjectContentVosArray=filmObj.getJSONArray("subjectContentVos");
//				
//				List<SubjectEntity> subjectContentVos=new ArrayList<SubjectEntity>();
//				for (int j = 0; j < subjectContentVosArray.length(); j++) {
//				JSONObject subjectContentVosObject = subjectContentVosArray.getJSONObject(j);
//				Log.v("SubjectListParser", "SubjectListParser2"+subjectContentVosObject.toString());
				SubjectListEntity sEntity= JsonUtils.parseObject(filmObj.toString(), SubjectListEntity.class);
				subjectEntities.add(sEntity);
				}

			
			entity.setPageContent(subjectEntities);
			arrayList.add(entity);
			
		} catch (JSONException e) {
			throw new ServiceException(HiveViewErrorCode.E0000598);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return arrayList;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
