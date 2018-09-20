package com.hiveview.tv.service.request;

/**
 * 生成分类筛选数据的url
 * 
 * @ClassName: GetThirdResultRequest
 * @Description:
 * @author: zhangpengzhan
 * @date 2014年7月4日 上午10:27:25
 * 
 */
public class GetThirdResultForListRequest extends BaseGetRequest {

	/**
	 * 视频类型
	 * @Fields videoType
	 */
	private String videoType;
	/**
	 * 搜索标签
	 * @Fields tag
	 */
	private String tag;
	/**
	 * 每页数量
	 * @Fields pageSize
	 */
	private String pageSize;
	/**
	 * 页码
	 * @Fields ate
	 */
	private String pageNumber;

	/**
	 * 视频类型的参数
	 * 搜索的标签
	 * 
	 * @param @param videoType
	 */
	public GetThirdResultForListRequest(int videoType,String tag,int pageSize,int pageNumber) {
		this.videoType = String.valueOf(videoType);
		this.tag = tag;
		this.pageSize = String.valueOf(pageSize);
		this.pageNumber = String.valueOf(pageNumber);
	}

	/*
	 * 得到地址 (non-Javadoc)
	 * @see com.hiveview.tv.service.request.BaseGetRequest#executeToREST()
	 */
	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_VIDEO_SET_LIST_BY_TAG, new Object[] { videoType, tag, pageNumber,pageSize,ApiConstant.APP_VERSION });
	}

}
