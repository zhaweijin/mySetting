package com.hiveview.cloudtv.common;

import android.graphics.drawable.Drawable;

public class CommonItemList {
	String itemName;
	Drawable pageLeft;
	String itemSetting;
	Drawable pageRight;

	int type = 1; //0 sub
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Drawable getPageLeft() {
		return pageLeft;
	}

	public void setPageLeft(Drawable pageLeft) {
		this.pageLeft = pageLeft;
	}

	public String getItemSetting() {
		return itemSetting;
	}

	public void setItemSetting(String itemSetting) {
		this.itemSetting = itemSetting;
	}

	public Drawable getPageRight() {
		return pageRight;
	}

	public void setPageRight(Drawable pageRight) {
		this.pageRight = pageRight;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
