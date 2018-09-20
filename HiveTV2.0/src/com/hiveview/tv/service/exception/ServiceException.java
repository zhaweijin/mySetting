package com.hiveview.tv.service.exception;

/**
 * 接口异常
 * 
 * @author HiveView  - Arashmen
 *
 */
public class ServiceException extends BaseException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ServiceException() {
		super();
	}
	
	public ServiceException(String errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}
	
	public ServiceException(String errorMessage, Throwable t) {
		super(errorMessage, t);
	}
	 
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
	
}
