package com.hiveview.tv.service.entity;

import org.json.JSONObject;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class ChannelEntity extends HiveBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 849883707131367154L;

	private String hot;
	/**
	 * 频道logo
	 */
	private String logo;
	/**
	 * 频道code
	 */
	private String code;
	private String type;
	/**
	 * 频道别名
	 */
	private String memo;
	/**
	 * 频道名称
	 */
	private String name;
	private JSONObject program;

	public String getHot() {
		return hot;
	}

	public void setHot(String hot) {
		this.hot = hot;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JSONObject getProgram() {
		return program;
	}

	public void setProgram(JSONObject program) {
		this.program = program;
	}

}
