package com.hiveview.cloudtv.settings.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class HttpUtil {

	public static int reqPost(String json, Context context) throws Exception {
		HttpClient client = new DefaultHttpClient();
		int code = 0;
		try {
			HttpPost post = new HttpPost("http://deviceapi.pthv.gitv.tv/device/uploadDeviceInfo.json");
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            //传值
		    urlParameters.add(new BasicNameValuePair("jsonStr",json));
			post.setEntity(new UrlEncodedFormEntity(urlParameters, HTTP.UTF_8));
			HttpResponse resp = client.execute(post);
			if (resp.getStatusLine().getStatusCode() == 200) {
				 String r =EntityUtils.toString(resp.getEntity());//r==true
				 //==={"code":1,"message":"ok"}
				 Log.i("=====服务器返回=====", "==="+r);
				// Toast.makeText(context, "服务器返回值===="+r, Toast.LENGTH_LONG).show();
				 JSONObject obj = new JSONObject(r);
				 String status = obj.getString("code");
				 if("1".equals(status)){//上传成功
					 code = 1;
				 }else{//上传失败
					 code = 0;
				 }
				 
			}
		} catch (ClientProtocolException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			client.getConnectionManager().shutdown();
		}
		return code;
	}
	
	


}
