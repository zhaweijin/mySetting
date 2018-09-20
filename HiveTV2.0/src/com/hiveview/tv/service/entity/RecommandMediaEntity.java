package com.hiveview.tv.service.entity;

import org.json.JSONObject;

import com.hiveview.box.framework.entity.HiveBaseEntity;

/**
 * 电视推荐见的媒体实体
 * 
 * @author chenlixiao
 * 
 */
public class RecommandMediaEntity extends HiveBaseEntity {
	private String id;
	private String title;
	private JSONObject info;
	private String description;
	private JSONObject posters;
	private JSONObject screens;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public JSONObject getInfo() {
		return info;
	}

	public void setInfo(JSONObject info) {
		this.info = info;
	}

	public JSONObject getPosters() {
		return posters;
	}

	public void setPosters(JSONObject posters) {
		this.posters = posters;
	}

	public JSONObject getScreens() {
		return screens;
	}

	public void setScreens(JSONObject screens) {
		this.screens = screens;
	}

}
