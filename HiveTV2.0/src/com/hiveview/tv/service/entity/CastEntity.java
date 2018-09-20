package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class CastEntity extends HiveBaseEntity {

	private static final long serialVersionUID = -4754335827260799100L;
	private int cast_id; 
    private String cast_name; 
    private int cast_type; 
    private String cast_desc; 
    private String cast_picture;
	public int getCast_id() {
		return cast_id;
	}
	public void setCast_id(int cast_id) {
		this.cast_id = cast_id;
	}
	public String getCast_name() {
		return cast_name;
	}
	public void setCast_name(String cast_name) {
		this.cast_name = cast_name;
	}
	public int getCast_type() {
		return cast_type;
	}
	public void setCast_type(int cast_type) {
		this.cast_type = cast_type;
	}
	public String getCast_desc() {
		return cast_desc;
	}
	public void setCast_desc(String cast_desc) {
		this.cast_desc = cast_desc;
	}
	public String getCast_picture() {
		return cast_picture;
	}
	public void setCast_picture(String cast_picture) {
		this.cast_picture = cast_picture;
	}
	@Override
	public String toString() {
		return "CastEntity [cast_id=" + cast_id + ", cast_name=" + cast_name
				+ ", cast_type=" + cast_type + ", cast_desc=" + cast_desc
				+ ", cast_picture=" + cast_picture + "]";
	}
	
    
}
