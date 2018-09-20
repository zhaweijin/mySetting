package com.hiveview.tv.service.exception;


/**
 * 异常基类（暂时还没用上）
 * @author Arashmen
 *
 */
public class BaseException extends RuntimeException{
	
	protected String errorCode ;
	
	/**
	 * serial UID 
	 */
	private static final long serialVersionUID = 1L;


	public BaseException() {
		super();
	}
	
	public BaseException(String errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}
	
	/**
	 * Constructor
	 * @param e
	 */
	public BaseException(Throwable e){
		super(e);
	}
	
	
	protected BaseException(String errorMessage, Throwable t) {
		super(errorMessage, t);
	}
	
	
	public String getMessage(){
		return super.getMessage();
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode){
		this.errorCode = errorCode;
	}

}
