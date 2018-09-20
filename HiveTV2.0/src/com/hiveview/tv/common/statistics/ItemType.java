/**
 * @Title ItemType.java
 * @Package com.hiveview.tv.common.statistics
 * @author haozening
 * @date 2014年6月26日 上午11:22:18
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.statistics;

/**
 * 点击的目标的类型
 * @ClassName ItemType
 * @Description 
 * @author haozening
 * @date 2014年6月26日 上午11:22:18
 * 
 */
public enum ItemType {

	/**
	 * 点击的是按钮
	 */
	BUTTON("button"),
	/**
	 * 点击的是推荐位视频
	 */
	VIDEO("video"),
	/**
	 * 点击的是推荐位游戏
	 */
	GAME("game"),
	EDUCATION("education"),
	/**
	 * 点击的是推荐位游戏
	 */
	SUBJECT("subject"),
	/**
	 * 点击的是推荐位应用
	 */
	APP("app"),
	/**
	 * 点击的是推荐位应用
	 */
	CLASS("class"),
	/**
	 * 点击影院页面专题
	 */
	VIDEOSETTYPE("videosetType");
	
	private String type;
	private ItemType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return type;
	}
}
