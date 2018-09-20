package com.hiveview.tv.hdmiin;

/**
 * 简单统一接口的创。让子类去实现
 * @ClassName: BaseTvPlayHandlerInterface
 * @Description: TODO
 * @author: 周一川
 * @date 2014-7-10 下午5:39:06
 *
 */
public interface BaseTvPlayHandlerInterface {
	/**
	 * 初始化播放方案
	 * @Title: BaseTvPlayHandler
	 * @author:周一川 
	 * @Description: TODO
	 */
	public void tvPlaySchemeInit();
	/**
	 * 销毁播放方案
	 * @Title: BaseTvPlayHandler
	 * @author:周一川
	 * @Description: TODO
	 */
	public void tvPlaySchemeDeinit();
	
	/**
	 * @Title: BaseTvPlayHandlerInterface
	 * @author:张鹏展
	 * @Description: 解绑广播注册接收器
	 */
	public void setUnregister();
}
