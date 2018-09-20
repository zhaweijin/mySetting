package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class SkinEntity extends HiveBaseEntity {
	private int recom_type;
	private String img_url_inside;
	private String img_url_outside;

	public int getRecom_type() {
		return recom_type;
	}

	public void setRecom_type(int recom_type) {
		this.recom_type = recom_type;
	}

	public String getImg_url_inside() {
		return img_url_inside;
	}

	public void setImg_url_inside(String img_url_inside) {
		this.img_url_inside = img_url_inside;
	}

	public String getImg_url_outside() {
		return img_url_outside;
	}

	public void setImg_url_outside(String img_url_outside) {
		this.img_url_outside = img_url_outside;
	}

}
