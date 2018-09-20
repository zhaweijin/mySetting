package com.hiveview.tv.common.silenceupgrade;

import com.hiveview.tv.service.entity.AppMarketEntity;

public class FileLoader extends Loader {
	
	private AppMarketEntity entity;
	private LoaderListener loaderListener;

	private FileLoader(String urlStr, String absolutePath,LoaderListener loadListener) {
		super(urlStr, absolutePath, loadListener);
	}

	public FileLoader(AppMarketEntity entity,LoaderListener loaderListener) {
		super(entity.getVersionUrl(), FileLoadUtils.getExternalStorageAbsolutePath(entity.getVersionUrl()), loaderListener);
		this.entity = entity;
		this.loaderListener = loaderListener;
	}
	
	
	@Override
	public void run() {
		loaderListener.onStart(entity);
		super.run();
	}
	
}
