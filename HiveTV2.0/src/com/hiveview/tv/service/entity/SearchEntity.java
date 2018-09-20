package com.hiveview.tv.service.entity;

import java.io.Serializable;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class SearchEntity extends HiveBaseEntity implements Serializable{

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
	
	
	
	
	/**
	 * 一下是点播列表里边项目点击进去的窗口的
	 * viewpager 的实体类 以时尚 生活为例子
	 */
	/**
	 * 视频的id 例如：383895
	 */
	private int videoset_id;
	/**
	 * 视频的类型   例如：17
	 */
	private int videoset_type;
	/**
	 * 视频的名字 例如：魅惑花瓣眼 Emporio Armani2012秋冬时装秀后台彩妆花絮 
	 * 需要显示的内容啊
	 */
	private String videoset_name;
	/**
	 * 视频缩略图的地址   viewpager显示的图片
	 */
	private String videoset_img;
	/**
	 * 奇异给的缩略图  例子：这个显示不错来啊
	 */
	private String videoset_tv_img;
	
	/**
	 * 自营或者是奇艺 例如：0或者1
	 */
	private int cp;
	
	public int getCp(){
		return cp;
	}
	
	public void setCP(int cp){
		this.cp = cp;
	}
	
	public int getVideoset_id() {
		return videoset_id;
	}
	public void setVideoset_id(int videoset_id) {
		this.videoset_id = videoset_id;
	}
	public int getVideoset_type() {
		return videoset_type;
	}
	public void setVideoset_type(int videoset_type) {
		this.videoset_type = videoset_type;
	}
	public String getVideoset_name() {
		return videoset_name;
	}
	public void setVideoset_name(String videoset_name) {
		this.videoset_name = videoset_name;
	}
	public String getVideoset_img() {
		return videoset_img;
	}
	public void setVideoset_img(String videoset_img) {
		this.videoset_img = videoset_img;
	}
	public String getVideoset_tv_img() {
		return videoset_tv_img;
	}
	public void setVideoset_tv_img(String videoset_tv_img) {
		this.videoset_tv_img = videoset_tv_img;
	}
	
	
	
	
	
	
	
	
	

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
