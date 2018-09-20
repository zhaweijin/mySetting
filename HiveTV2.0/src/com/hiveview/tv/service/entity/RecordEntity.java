package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class RecordEntity extends HiveBaseEntity {
	
	private static final long serialVersionUID = 8221426089328160852L;
	private int record_id; 
    private String record_img; 
    private int record_type; 
    private String record_name;
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
	@Override
	public String toString() {
		return "RecordEntity [record_id=" + record_id + ", record_img="
				+ record_img + ", record_type=" + record_type
				+ ", record_name=" + record_name + "]";
	}

    
}
