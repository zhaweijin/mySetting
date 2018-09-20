package com.hiveview.tv.common.content;

public interface ContentShowType {
	/**
	 * 单片无详情类型
	 */
	public final static int TYPE_SINGLE_VIDEO_NO_DETAIL = 0;

	/**
	 * 单片详情类型
	 */
	public final static int TYPE_SINGLE_VIDEO_DETAIL = 1;

	/**
	 * 多片详情类型
	 */
	public final static int TYPE_MULTIPLE_VIDEO_DETAIL = 2;

	/**
	 * 综艺详情类型
	 */
	public final static int TYPE_VARIETY_VIDEO_DETAIL = 3;

	/**
	 * 专题详情类型
	 */
	public final static int TYPE_SUBJECT_VIDEO_DETAIL = 4;
	
	/**
	 * 影院专题
	 */
	public final static int TYPE_MOVIE_SUBJECT = 1001;
	
	/**
	 * 影院专辑
	 */
	public final static int TYPE_MOVIE_DETIAL = 1002;
}
