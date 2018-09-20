package com.hiveview.tv.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.hiveview.tv.service.exception.ServiceException;

public class JsonUtils {

	public static <T> T parseObject(String json, Class<T> clazz) throws ServiceException {
		T t = null;
		try {
			t = clazz.newInstance();
			JSONObject result = new JSONObject(json);
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				Log.v("Field", "field"+field.getType());
				field.setAccessible(true);
				if (!result.isNull(field.getName())) {
					field.set(t, result.get(field.getName()));
				}
			}
		} catch (InstantiationException e) {
			throw new ServiceException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ServiceException(e.getMessage());
		} catch (JSONException e) {
			throw new ServiceException(e.getMessage());
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} catch (Throwable throwable) {
			throw new ServiceException(throwable.getMessage());
		}
		return t;
	}

	public static <T> ArrayList<T> parseArray(String json, Class<T> clazz) throws ServiceException {
		ArrayList<T> list = new ArrayList<T>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			Field[] fields = clazz.getDeclaredFields();
			for (int j = 0; j < jsonArray.length(); j++) {
				T t = clazz.newInstance();
				JSONObject result = jsonArray.getJSONObject(j);
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					field.setAccessible(true);
					if (!result.isNull(field.getName())) {
						if (result.has(field.getName())) {
							field.set(t, result.get(field.getName()));
						}
					}
				}
				list.add(t);
			}
		} catch (InstantiationException e) {
			throw new ServiceException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ServiceException(e.getMessage());
		} catch (JSONException e) {
			throw new ServiceException(e.getMessage());
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} catch (Throwable t) {
			throw new ServiceException(t.getMessage());
		}
		return list;
	}

}
