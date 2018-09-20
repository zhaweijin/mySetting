package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.FilmCategory;
import com.hiveview.tv.service.entity.FilmCategorySecondary;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.StringUtils;
/**
 * 
 * @author haozening
 *
 */
public class FilmCategoryParser extends BaseParser {

	@Override
	public ArrayList<FilmCategory> executeToObject(InputStream in)
			throws ServiceException {
		ArrayList<FilmCategory> arrayList = new ArrayList<FilmCategory>();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject jsonObject = new JSONObject(json);
			errorCode = jsonObject.getString("code");
			
			JSONArray jsonArray = jsonObject.getJSONArray("result");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject categoryObj = jsonArray.getJSONObject(i);
				
				// 解析二级分类
				JSONArray filmSecondaryArray = categoryObj.getJSONArray("classThirdList");
				List<FilmCategorySecondary> categorySecondaries = new ArrayList<FilmCategorySecondary>();
				for (int j = 0; j < filmSecondaryArray.length(); j++) {
					JSONObject categorySecondaryObj = filmSecondaryArray.getJSONObject(j);
					FilmCategorySecondary categorySecondary = new FilmCategorySecondary();
					categorySecondary.setThirdclass_id(categorySecondaryObj.getInt("thirdclass_id"));
					categorySecondary.setThirdclass_name(categorySecondaryObj.getString("thirdclass_name"));
					
					categorySecondaries.add(categorySecondary);
				}
				
				// 解析一级分类
				FilmCategory category = new FilmCategory();
				String secondclassName = categoryObj.getString("secondclass_name");
				category.setFirstclass_id(categoryObj.getInt("firstclass_id"));
				category.setSecondclass_id(categoryObj.getInt("secondclass_id"));
				category.setSecondclass_name(secondclassName);
				category.setClassThirdList(arrayResort(secondclassName, categorySecondaries));
				
				// 保存分类信息
				arrayList.add(category);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch(Exception e){
			throw new ServiceException(e.getMessage());
		}
		return arrayList;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}
	
	/**
	 * 对二级分类重新排序，并增加 "全部XX" <br />
	 * <B><I>"全部XX" 的thirdclass_id暂定为-1</I></B>
	 * @param secondclassName
	 * @param categorySecondaries
	 * @return
	 */
	private List<FilmCategorySecondary> arrayResort(String secondclassName, List<FilmCategorySecondary> categorySecondaries){
		List<FilmCategorySecondary> list = new ArrayList<FilmCategorySecondary>();
		
		if (categorySecondaries.size() == 0) {
			FilmCategorySecondary categorySecondary = new FilmCategorySecondary();
			categorySecondary.setThirdclass_id(-1);
			categorySecondary.setThirdclass_name(secondclassName);
			list.add(categorySecondary);
		}
		
		int size = categorySecondaries.size();
		int start = size / 2 + size % 2;
		
		for (int i = start; i < size + start; i++) {
			if (i % size == 0) {
				FilmCategorySecondary categorySecondary = new FilmCategorySecondary();
				categorySecondary.setThirdclass_id(-1);
				categorySecondary.setThirdclass_name(secondclassName);
				list.add(categorySecondary);
			}
			list.add(categorySecondaries.get(i % size));
		}
		return list;
	}

}
