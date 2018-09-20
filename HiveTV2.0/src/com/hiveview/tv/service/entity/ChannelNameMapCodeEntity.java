package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class ChannelNameMapCodeEntity extends HiveBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5294626755210716568L;
	
	private String channelCode;
	private String channelName;
	private String channelLogo;

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelLogo() {
		return channelLogo;
	}

	public void setChannelLogo(String channelLogo) {
		this.channelLogo = channelLogo;
	}

}
