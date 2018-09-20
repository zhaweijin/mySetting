package com.hiveview.tv.service.request;

/**
 * 生成分类筛选的url
 * 
 * @ClassName: GetThirdResultRequest
 * @Description:
 * @author: zhangpengzhan
 * @date 2014年7月4日 上午10:27:25
 * 
 */
public class GetThirdResultRequest extends BaseGetRequest {

	private String videoType;

	/**
	 * 视频类型的参数
	 * 
	 * @param @param videoType
	 */
	public GetThirdResultRequest(String videoType) {
		this.videoType = videoType;
	}

	/*
	 * 得到地址 (non-Javadoc)
	 * 
	 * @see com.hiveview.tv.service.request.BaseGetRequest#executeToREST()
	 */
	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_THIRDLIST_RESULT_LIST, new Object[] { this.videoType, ApiConstant.APP_VERSION });
	}

}
