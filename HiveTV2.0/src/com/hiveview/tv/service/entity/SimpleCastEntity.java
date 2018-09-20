package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class SimpleCastEntity extends HiveBaseEntity{
	private String cast_id;
	private String cast_name;
	private int record_count;

	public String getCast_id() {
		return cast_id;
	}

	public void setCast_id(String cast_id) {
		this.cast_id = cast_id;
	}

	public String getCast_name() {
		return cast_name;
	}

	public void setCast_name(String cast_name) {
		this.cast_name = cast_name;
	}

	public int getRecord_count() {
		return record_count;
	}

	public void setRecord_count(int record_count) {
		this.record_count = record_count;
	}

}
