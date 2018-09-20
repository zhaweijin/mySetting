package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

/**
 * 搜索界面返回的结果的实体类 给viewpager 显示数据的实体类
 * 
 * @author zhangpengzhan
 *
 * 2014年4月21日
 *	上午10:30:53
 */
public class SearchPagerViewItemEntity extends HiveBaseEntity {
	/**
	 * 继承的 我也想知道这是干嘛的
	 */
	private static final long serialVersionUID = -5004328965492768955L;
	
	/**
	 * 返回视频的id
	 */
	private int record_id;
	
	/**
	 * 封面的海报图
	 */
	private String record_img;
	
	/**
	 * 返回的一个类型 视频的 类型  搞笑啊 还是 时尚 啊  这类的
	 */
	private int record_type;
	
	/**
	 * 视屏的 名字
	 */
	private String record_name;
	
	/**
	 * url访问不通
	 */
	private String record_tv_img;
	
	/**
	 * 视频时长
	 */
	private int record_time_length;
	
	/**
	 * 什么东东  没有文档  自己看json字符串 不能瞎猜啊
	 */
	private int record_update;
	
	/**
	 * 这货 一般都是1 或者0 
	 * 目测
	 */
	private int record_total;

	public int getRecord_id() {
		return record_id;
	}

	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}

	public String getRecord_img() {
		return record_img;
	}

	public void setRecord_img(String record_img) {
		this.record_img = record_img;
	}

	public int getRecord_type() {
		return record_type;
	}

	public void setRecord_type(int record_type) {
		this.record_type = record_type;
	}

	public String getRecord_name() {
		return record_name;
	}

	public void setRecord_name(String record_name) {
		this.record_name = record_name;
	}

	public String getRecord_tv_img() {
		return record_tv_img;
	}

	public void setRecord_tv_img(String record_tv_img) {
		this.record_tv_img = record_tv_img;
	}

	public int getRecord_time_length() {
		return record_time_length;
	}

	public void setRecord_time_length(int record_time_length) {
		this.record_time_length = record_time_length;
	}

	public int getRecord_update() {
		return record_update;
	}

	public void setRecord_update(int record_update) {
		this.record_update = record_update;
	}

	public int getRecord_total() {
		return record_total;
	}

	public void setRecord_total(int record_total) {
		this.record_total = record_total;
	}
	
}
