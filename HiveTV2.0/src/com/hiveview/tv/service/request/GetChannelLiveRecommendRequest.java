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

/**
 * 获取频道直播推荐
 * 
 * @ClassName: GetChannelLiveRecommendRequest
 * @Description: TODO
 * @author: guosongsheng
 * @date 2014年8月8日 下午2:19:06
 * 
 */
public class GetChannelLiveRecommendRequest extends HuanWangBasePostRequest {
	private static final String TAG = "GetChannelLiveRecommendRequest";
	/**
	 * 类型
	 */
	private String type ;
	/**
	 * 省份
	 */
	private String value ;
	/**
	 * 频道code
	 */
	private String channelCode ;

	/**
	 * 构造
	 * 
	 * @param @param names 电台名称数组
	 */
	public GetChannelLiveRecommendRequest(String type , String value , String channelCode) {
		this.type = type;
		this.value = value;
		this.channelCode = channelCode;
	}
	
	@Override
	public HttpPost executeToEntity() throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(ApiConstant.HUANWANG_DOMAIN);
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();

		JSONObject json = getCommonJson();
		try {
			json.put("action",ApiConstant.HUANWANG_GETCHANEEL_RECOMMEND);
			json.put("type", type);
			json.put("value", value);
			json.put("channelCode", channelCode);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		paramList.add(new BasicNameValuePair("jsonstr", json.toString()));
		Log.i(TAG, "......................"+json.toString());
		httpPost.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
		return httpPost;
	}
	
	@Override
	public String executeUrl() throws ServiceException {
		return ApiConstant.HUANWANG_DOMAIN+"|"+ApiConstant.HUANWANG_GETCHANEEL_RECOMMEND;
	}
}
