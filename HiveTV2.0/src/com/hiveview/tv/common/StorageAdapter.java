package com.hiveview.tv.common;

import java.util.HashMap;

public class StorageAdapter {

	private String deviceModel;
	private HashMap<String, GetStorageListener> map = new HashMap<String, GetStorageListener>();

	public StorageAdapter(String model) {
		deviceModel = model;
		map.put("DM1001", new TWGetStorageListener());
		map.put("DM1002", new SkyworthGetStorageListener());
		map.put("DM1003", new TWGetStorageListener());
		map.put("DM1004", new TWGetStorageListener());
		map.put("DM1005", new TWGetStorageListener());
		map.put("DM2001", new TWGetStorageListener());
		map.put("DM2002", new SkyworthGetStorageListener());
		map.put("DM2003", new TWGetStorageListener());
		map.put("DM2004", new TWGetStorageListener());
		map.put("DM2005", new TWGetStorageListener());
	}

	public GetStorageListener getStorageInfo() {
		if (null == deviceModel || deviceModel.length() == 0) {
			return null;
		}
		
		GetStorageListener listener = map.get(deviceModel) != null ? map.get(deviceModel): map.get("DM1001");
		return listener;
	}

}
