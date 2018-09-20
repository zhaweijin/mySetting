package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.net.HttpTaskManager;

/**
 * @author Arashmen
 *
 */
public abstract class BaseParser {
	
	protected final String ENCODE = "UTF-8";
	
	protected String errorCode;
	protected String date;
	protected HttpTaskManager manager = HttpTaskManager.getInstance();
	
	/**
	 * 解析JSON
	 * @author Arashmen
	 * @param <T>
	 * */
	public abstract  ArrayList<? extends HiveBaseEntity> executeToObject(InputStream in)throws ServiceException;
	
	/**
	 * check errorcode
	 * @author Arashmen
	 * @param <T>
	 * */
	public abstract String getErrorCode();
	
	
}
