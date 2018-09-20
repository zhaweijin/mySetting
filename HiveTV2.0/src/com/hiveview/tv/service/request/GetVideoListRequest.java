package com.hiveview.tv.service.request;

import android.util.Log;

public class GetVideoListRequest extends BaseGetRequest {

	private int videoset_id;
	private int page_size;
	private int page_number;
	private String videoid;
	private String year;
	private int video_type;

	public GetVideoListRequest(int videoset_id, int video_type, String videoid, int page_size, int page_number, String year) {
		super();
		this.videoset_id = videoset_id;
		this.page_size = page_size;
		this.page_number = page_number;
		this.videoid = videoid;
		this.video_type = video_type;
		if (null != year && year.length() > 0) {
			this.year = year;
		}
	}

	@Override
	public String executeToREST() {
		String url = "";
		// if (null != year && year.length() > 0) {
		// url = String.format(ApiConstant.HIVEVIEW_API_GET_VIDEO_BY_YEAR_LIST,
		// new Object[] { this.videoset_id, this.page_number, 10,
		// ApiConstant.APP_VERSION });
		// } else {
		if (!videoid.equals("0") && videoid.length() > 0) {

			url = String.format(ApiConstant.HIVEVIEW_API_GET_VIDEO_BY_YEAR_LIST, new Object[] { this.videoset_id, this.video_type, this.videoid,
					this.page_number, this.page_size, ApiConstant.APP_VERSION });
		} else {
			url = String.format(ApiConstant.HIVEVIEW_API_GET_VIDEO_LIST, new Object[] { this.videoset_id, this.video_type, this.page_number,
					this.page_size, ApiConstant.APP_VERSION });
		}
		// }
		Log.d("GetVideoListRequest", "--url:" + url);
		return url;
	}

}
