package com.hiveview.tv.service.request;

import com.hiveview.tv.service.exception.ServiceException;

public abstract class SafeRunnable implements Runnable {

	public SafeRunnable() {
	}

	@Override
	public void run() {
		try {
			requestData();
		} catch (ServiceException e) {
			processServiceException(e);
		}
	}

	/**
	 * 此方法会在Runnable 的run方法异步执行，主要是完成网络请求数据的操作
	 */
	public abstract void requestData();

	/**
	 * 此方法会在Runnable 的run方法异步执行，在requestData()访问数据过程中发生异常会调用此方法
	 */
	public abstract void processServiceException(ServiceException e);

}
