package com.hiveview.tv.service.entity;

import java.io.Serializable;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class GetDvicesCodeEntity extends HiveBaseEntity implements Serializable{

	/**
	 * @Fields serialVersionUID:TODO
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 设备码
	 */
	private String devicesCode;

	public String getDevicesCode() {
		return devicesCode;
	}

	public void setDevicesCode(String devicesCode) {
		this.devicesCode = devicesCode;
	}
	
	
}
