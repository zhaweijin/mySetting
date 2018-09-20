package com.hiveview.tv.service.request;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.exception.ServiceException;

import android.util.Log;

public class ProgramByCodeRequest extends HuanWangBasePostRequest {
	/**
	 * TAG
	 */
	private static final String TAG = "ProgramByCodeRequest";
	private String[] codes;
	private String startTime;
	private String endTime;

	public ProgramByCodeRequest(String[] codes, String start_time, String end_time) {
		this.startTime = start_time;
		this.endTime = end_time;
		this.codes = codes;

		if (start_time == null || end_time == null) {
			start_time = "";
			end_time = "";
		}
	}

	@Override
	public HttpPost executeToEntity() throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(ApiConstant.HUANWANG_DOMAIN);
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();

		JSONObject json = getCommonJson();
		try {
			json.put("action", ApiConstant.HUANWANG_GETPROGRAMBYCODES);
			JSONObject jsonParam = new JSONObject();
			String codeTmp = "[";
			if (codes != null) {
				for (int i = 0; i < codes.length; i++) {
					codeTmp += codes[i] + ",";
				}
				codeTmp += "]";
				codeTmp = codeTmp.replace(",]", "]");

			}

			JSONArray array = new JSONArray(codeTmp);
			jsonParam.put("codes", array);

			json.put("param", jsonParam);
			json.put("start_time", startTime);
			json.put("end_time", endTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i(TAG, json.toString());
		paramList.add(new BasicNameValuePair("jsonstr", json.toString()));
		httpPost.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
		return httpPost;
	}

	@Override
	public String executeUrl() throws ServiceException {
		return ApiConstant.HUANWANG_DOMAIN+"|"+ApiConstant.HUANWANG_GETPROGRAMBYCODES;
	}
}
