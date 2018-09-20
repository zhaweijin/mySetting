package com.hiveview.tv.service.entity;

import java.util.ArrayList;

import com.hiveview.box.framework.entity.HiveBaseEntity;

/**
 * 搜索页面的结果的 实体类
 * 
 * @author zhangpengzhan
 *
 * 2014年4月21日
 *	上午10:22:58
 */
public class SearchPagerEntity extends HiveBaseEntity {

	/**
	 * 继承的 我也想知道这是干嘛的
	 */
	private static final long serialVersionUID = -5004328965492768955L;
	
	/**
	 *
	 * 搜索窗口的实体类
	 * 
	 * 一下为在搜索界面 输入q 搜索的结果为例  搜索的字为	墙
	 */
	/**
	 * 网络返回码  状态码 以此为判断错误状态  正常的是 N000000
	 */
	private String code;
	/**
	 * 返回的时间戳 1398046322399
	 */
	private long longTime;
	/**
	 * 当前时间   2014-04-21 10:12:02.399
	 */
	private String stringTime;
	/**
	 * 信息  一般为空啊
	 */
	private String message;
	
	/**
	 * 第几页 
	 */
	private int pageNo;
	
	/**
	 * 每页的数据量
	 */
	private int pageSize;
	
	/**
	 * 多少页
	 */
	private int pageCount;
	
	/**
	 * 单条一共多少数据
	 */
	private int recCount;
	
	/**
	 * 返回结果的集合  result
	 */
	private ArrayList<SearchPagerViewItemEntity> pagerViewItemEntities;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getLongTime() {
		return longTime;
	}

	public void setLongTime(long longTime) {
		this.longTime = longTime;
	}

	public String getStringTime() {
		return stringTime;
	}

	public void setStringTime(String stringTime) {
		this.stringTime = stringTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getRecCount() {
		return recCount;
	}

	public void setRecCount(int recCount) {
		this.recCount = recCount;
	}

	public ArrayList<SearchPagerViewItemEntity> getPagerViewItemEntities() {
		return pagerViewItemEntities;
	}

	public void setPagerViewItemEntities(
			ArrayList<SearchPagerViewItemEntity> pagerViewItemEntities) {
		this.pagerViewItemEntities = pagerViewItemEntities;
	}
	
	

}
