package com.hiveview.tv.service.request;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.exception.ServiceException;

import android.util.Log;

public class GetMediaByTagRequest extends HuanWangBasePostRequest {
	/**
	 * TAG
	 */
	private static final String TAG = "GetMediaByTagRequest";
	
	private String tag;
	private int page;

	public GetMediaByTagRequest(String tag, int page) {
		super();
		this.tag = tag;
		this.page = page;
	}

	@Override
	public HttpPost executeToEntity() throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(ApiConstant.HUANWANG_DOMAIN);
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();

		JSONObject json = getCommonJson();
		try {
			json.put("action", ApiConstant.HUANWANG_GETMEDIASBYTAG);
			JSONObject jsonParam = new JSONObject();
			jsonParam.put("type", 2);
			jsonParam.put("typevalue", "");
			jsonParam.put("tag", tag);
			jsonParam.put("start_time", "");
			jsonParam.put("end_time", "");
			jsonParam.put("page", page);
			jsonParam.put("pagesize", 100);
			json.put("param", jsonParam);
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
		return ApiConstant.HUANWANG_DOMAIN+"|"+ApiConstant.HUANWANG_GETMEDIASBYTAG;
	}
}
