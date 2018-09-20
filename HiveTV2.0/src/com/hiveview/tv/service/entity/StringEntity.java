package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class StringEntity extends HiveBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5105796719383938415L;
	private String strParams;
	private int intParams;

	public StringEntity() {

	}

	public StringEntity(String params) {
		this.strParams = params;
	}

	public String getStrParams() {
		return strParams;
	}

	public void setStrParams(String strParams) {
		this.strParams = strParams;
	}

	public int getIntParams() {
		return intParams;
	}

	public void setIntParams(int intParams) {
		this.intParams = intParams;
	}

}
