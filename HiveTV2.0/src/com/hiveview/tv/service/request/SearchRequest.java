/**
 * @Title SearchRequest.java
 * @Package com.hiveview.tv.service.request
 * @author haozening
 * @date 2014年8月7日 下午7:10:35
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.service.request;

/**
 * @ClassName SearchRequest
 * @Description 
 * @author haozening
 * @date 2014年8月7日 下午7:10:35
 * 
 */
public class SearchRequest extends BaseGetRequest {

	private String url;
	public SearchRequest(String url, String pageSize, String pageNum) {
		this.url = url + "startindex=" + pageNum + "&count=" + pageSize;
	}
	@Override
	public String executeToREST() {
		return url;
	}

}
