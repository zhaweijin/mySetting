package com.hiveview.tv.service.net;

import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

import com.hiveview.reporter.NetErrorReporter;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.BaseGetRequest;
import com.hiveview.tv.utils.DeviceBoxUtils;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.LogUtil;

public class HttpGetConnector extends BaseHttpGetConnector {

	private final int CONNECT_TIMEOUT = 5000;
	private final int READ_TIMEOUT = 5000;

	private String urlStr;

	@SuppressWarnings("unused")
	private HttpGetConnector() {
	}

	public HttpGetConnector(BaseGetRequest request) {
		this.urlStr = request.executeToREST();
		
		LogUtil.info("LockNet==url==========="+urlStr);

	}

	/**
	 * log 标签
	 * 
	 * @Fields TAG:TODO
	 */
	private String TAG = "HttpGetConnector";

	/**
	 * get data stream from net.
	 * 
	 * @author Arashmen
	 * @throws HiveException .
	 * */
	public InputStream getGetResponse() {
		
		InputStream in = null;
		if (HiveTVService.isAvailable(HiveviewApplication.getContext())) {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			// 创建Get方法实例
			httpclient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
			HttpGet httpgets = new HttpGet(urlStr);
			String cookiesStr = HiveviewApplication.getCookie();
			if (!cookiesStr.equals("")) {
				httpgets.setHeader("Cookie", cookiesStr);
			}
			setHeader(httpgets);
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, READ_TIMEOUT);

			HttpResponse response = null;
			try {
				response = httpclient.execute(httpgets);
			} catch (Exception e) {
				Log.d(TAG, "acturelly error is ==========="+e.toString());
				/*start by huzuwei,添加网络请求错误上报日志*/
				NetErrorReporter reporter = new NetErrorReporter(HiveviewApplication.getContext(),HiveViewErrorCode.E0000605,urlStr);
				reporter.report();
				/*end by huzuwei*/
				throw new ServiceException(HiveViewErrorCode.E0000605);				
			}

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpURLConnection.HTTP_OK) {// 抛出网络异常
				/*start by huzuwei,添加网络请求错误上报日志*/
				NetErrorReporter reporter = new NetErrorReporter(HiveviewApplication.getContext(),statusCode+"",urlStr);
				reporter.report();
				/*end by huzuwei*/
				if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {// 404错误
					throw new ServiceException(HiveViewErrorCode.E0000404);
				} else if (statusCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {// 500错误
					throw new ServiceException(HiveViewErrorCode.E0000500);
				} else if (statusCode == HttpURLConnection.HTTP_BAD_GATEWAY) {// 502错误
					throw new ServiceException(HiveViewErrorCode.E0000502);
				}
			}

			HttpEntity entity = response.getEntity();

			if (entity != null) {
				try {
					in = entity.getContent();
				} catch (Exception e) {
					throw new ServiceException(HiveViewErrorCode.E0000600);
				}

				if (null == in) {// 服务器返回数据流为空
					throw new ServiceException(HiveViewErrorCode.E0000600);
				}
			}
		} else {
			throw new ServiceException(HiveViewErrorCode.E0000599);
		}

		return in;
	}
	
	/**
	 * @Title: HttpGetConnector
	 * @author:张鹏展
	 * @Description: 设置httpget 请求的 header User-Agent 追加信息
	 * @param mHttpGet
	 */
	private void setHeader(HttpGet mHttpGet){
//		Log.d(TAG ,  "Apache-HttpClient/UNAVAILABLE (java 1.4)"+"&&MODEL="+DeviceUtils.getDeviceModel()
//				+"&&MAC="+DeviceUtils.getMac()+"&&SN="+DeviceUtils.getSn()+"&&VERSION="+DeviceUtils.getSoftVersion());
//		mHttpGet.setHeader("User-Agent", "Apache-HttpClient/UNAVAILABLE (java 1.4)"+"&&MODEL="+DeviceBoxUtils.getDeviceModel()
//				+"&&MAC="+DeviceBoxUtils.getMac()+"&&SN="+DeviceBoxUtils.getSn()+"&&VERSION="+DeviceBoxUtils.getSoftVersion());
	}
}
