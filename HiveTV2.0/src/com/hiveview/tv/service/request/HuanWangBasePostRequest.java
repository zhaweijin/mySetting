package com.hiveview.tv.service.request;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.exception.ServiceException;

public class HuanWangBasePostRequest extends BasePostRequest {

	@Override
	public HttpPost executeToEntity() throws UnsupportedEncodingException {
		return null;
	}

	/***
	 * 得到公用的参数(user,device,developer)的Json对象
	 * 
	 * @return
	 */
	public JSONObject getCommonJson() {
		JSONObject json = new JSONObject();
		try {
			// developer
			JSONObject keySecJosn = new JSONObject();
			
			//正式
			keySecJosn.put("apikey", "NLS2AW29");
			keySecJosn.put("secretkey", "ab72e987020d824940a1294f49a0c763");

			//测试
//			keySecJosn.put("apikey", "7V39QDZB");
//			keySecJosn.put("secretkey", "18230ad1c1d306022d46398cef477b4c");
//			
			json.put("developer", keySecJosn);

			// device
			JSONObject jsonDnum = new JSONObject();
			jsonDnum.put("dnum", ApiConstant.UUID);
			json.put("device", jsonDnum);

			// user
			JSONObject josnUserId = new JSONObject();
			josnUserId.put("userid", "123");
			json.put("user", josnUserId);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	@Override
	public String executeUrl() throws ServiceException {
		return null;
	}

}
