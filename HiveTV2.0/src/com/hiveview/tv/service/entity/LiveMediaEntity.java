package com.hiveview.tv.service.entity;

import java.io.Serializable;

import org.json.JSONObject;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class LiveMediaEntity extends HiveBaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String start_time;	
	private String end_time;
	private String channel_code;
	private String channel_name;
	private String channel_memo;
	private String next_name;
	private String program_id;
	private String channel_logourl;
	private JSONObject info;
	private JSONObject posters;

	public JSONObject getPosters() {
		return posters;
	}

	public void setPosters(JSONObject posters) {
		this.posters = posters;
	}

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

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getChannel_code() {
		return channel_code;
	}

	public void setChannel_code(String channel_code) {
		this.channel_code = channel_code;
	}

	public String getChannel_name() {
		return channel_name;
	}

	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}

	public String getChannel_memo() {
		return channel_memo;
	}

	public void setChannel_memo(String channel_memo) {
		this.channel_memo = channel_memo;
	}

	public String getChannel_logourl() {
		return channel_logourl;
	}

	public void setChannel_logourl(String channel_logourl) {
		this.channel_logourl = channel_logourl;
	}

	public JSONObject getInfo() {
		return info;
	}

	public void setInfo(JSONObject info) {
		this.info = info;
	}

	public String getNext_name() {
		return next_name;
	}

	public void setNext_name(String next_name) {
		this.next_name = next_name;
	}

	public String getProgram_id() {
		return program_id;
	}

	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}
	
	

	

}
