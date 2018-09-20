/**
 * @Title Tab.java
 * @Package com.hiveview.tv.common.statistics
 * @author haozening
 * @date 2014年6月26日 上午11:45:27
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.statistics;

/**
 * 首页Tab的位置
 * @ClassName Tab
 * @Description 
 * @author haozening
 * @date 2014年6月26日 上午11:45:27
 * 
 */
public enum Tab {
	APP("55"),
	FILM("44"),
	RECOMMEND("33"),
	BULE("77"),
	GAME("11"),
	EDUCATION("88"),
	TV("22"),
	DEFAULT("XX"),
	TAB("00"),;
	private String code;
	private Tab(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return code;
	}
}
