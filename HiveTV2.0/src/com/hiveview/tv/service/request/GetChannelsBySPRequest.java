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

public class GetChannelsBySPRequest extends HuanWangBasePostRequest {
	/**
	 * TAG 
	 */
	private static final String TAG = "GetChannelsBySPRequest";
	private String tag;

	/**
	 * tag=cctv,tv,pay,hd
	 * 
	 * @param tag
	 */
	public GetChannelsBySPRequest(String tag) {
		this.tag = tag;
	}

	@Override
	public HttpPost executeToEntity() throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(ApiConstant.HUANWANG_DOMAIN);
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();

		JSONObject json = getCommonJson();
		try {
			json.put("action", ApiConstant.HUANWANG_GETCHANNELSBYSP);
			JSONObject jsonParam = new JSONObject();

			if (tag.equals("cctv") || tag.equals("tv")) {// 央视和卫视频道信息与运营商信息无关
				jsonParam.put("type", "2");
				jsonParam.put("typevalue", "");
			} else {// 本地，高清，付费频道与运营商信息有关，
					// 北京歌华（536605242dd6fce84f91eba9），东方有线（536605232dd6fce84f91ea71）
				jsonParam.put("type", "1");
				jsonParam.put("typevalue", "536605242dd6fce84f91eba9");
			}

			jsonParam.put("tag", tag);
			jsonParam.put("showlive", 1);
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
		return ApiConstant.HUANWANG_DOMAIN+"|"+ApiConstant.HUANWANG_GETCHANNELSBYSP;
	}
}
