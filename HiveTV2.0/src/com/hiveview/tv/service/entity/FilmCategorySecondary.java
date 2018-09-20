package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class FilmCategorySecondary extends HiveBaseEntity {
	
	private int thirdclass_id; 
    private String thirdclass_name;
	public int getThirdclass_id() {
		return thirdclass_id;
	}
	public void setThirdclass_id(int thirdclass_id) {
		this.thirdclass_id = thirdclass_id;
	}
	public String getThirdclass_name() {
		return thirdclass_name;
	}
	public void setThirdclass_name(String thirdclass_name) {
		this.thirdclass_name = thirdclass_name;
	}
	@Override
	public String toString() {
		return "FilmCategorySecondary [thirdclass_id=" + thirdclass_id
				+ ", thirdclass_name=" + thirdclass_name + "]";
	}
	
}
