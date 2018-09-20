package com.hiveview.cloudtv.settings.util;

/**
 * @author huxing E-mail: huxing@btte.net
 * @version 创建时间：2018年2月5日 下午6:26:27 类说明
 */
public interface NetConfigureInterface1 {

	/**
	 * 函数名称：setEthernetSwitch 
	 * 函数说明：控制以太网的连接及断开 
	 * 参数说明： opened boolean类型, true
	 *        表示开启以太网，false表示关闭以太网 返回值说明：boolean类型,表示设置是否成功，默认值false 
	 * 作者：胡星
	 * 日期：2017/10/26 14:10
	 */
	boolean setEthernetSwitch(boolean opened);

	/**
	 * 函数名称：getEthernetStatus 
	 * 函数说明：获取以太网的状态（连接或者断开）
	 * 参数说明： 无 返回值说明：boolean类型，
	 *          true表示以太网开启状态，false表示以太网关闭状态；默认返回false；
	 * 作者：胡星 
	 * 日期：2017/10/26 14:11
	 */
	boolean getEthernetStatus();

	/**
	 * 函数名称：isEthernetAvailable 
	 * 函数说明：检测以太网是否可用，也就是硬件网口是否插上以太网线
	 * 参数说明：null
	 * 返回值说明：true:以太网插入； false:以太网拔除 作者：胡星 日期：2017/12/4 11:55
	 */
	boolean isEthernetAvailable();

	/**
	 * 函数名称：getEthernetType 
	 * 函数说明：获取当前有线网络类型：pppoe or dhcp 
	 * 参数说明：null
	 * 返回值说明：-1：表示获取失败； 0:表示pppoe拨号模式，1：表示dhcp模式 
	 * 作者：胡星 
	 * 日期：2017/12/4 14:40
	 */
	int getEthernetType();

	/**
	 * 函数名称：getEthernetIpAddress 
	 * 函数说明：获取以太网ip 
	 * 参数说明： null 
	 * 返回值说明：默认返回""，
	 *        有线网络IP："xx.xx.xx.xx" 
	 * 作者：胡星 
	 * 日期：2017/12/4 14:42
	 */
	String getEthernetIpAddress();

	/**
	 * 函数名称：getEthernetNetmask 
	 * 函数说明：获取以太网子网掩码 参数说明： null 
	 * 返回值说明：默认返回""，
	 *       有线网络子网掩码："xx.xx.xx.xx" 
	 * 作者：胡星 
	 * 日期：2017/12/4 14:45
	 */
	String getEthernetNetmask();

	/**
	 * 函数名称：getEthernetGateway 
	 * 函数说明：获取以太网网关 
	 * 参数说明： null 
	 * 返回值说明：默认返回""，
	 *         有线网络网关："xx.xx.xx.xx" 
	 * 作者：胡星 
	 * 日期：2017/12/4 14:48
	 */
	String getEthernetGateway();

	/**
	 * 函数名称：getEthernetDns 
	 * 函数说明：获取以太网dns 
	 * 参数说明： null 返回值说明：默认返回""，
	 *        有线网络dns："xx.xx.xx.xx" 
	 * 作者：胡星 
	 * 日期：2017/12/4 14:48
	 */
	String getEthernetDns();

	/**
	 * 函数名称：isEthernetDhcp 
	 * 函数说明：自动dhcp还是手动设置静态ip 参数说明：null 
	 * 返回值说明：false：手动dhcp，
	 *        true: 自动设置， 默认为false 
	 * 作者：胡星 
	 * 日期：2017/12/4 16:09
	 */
	boolean isEthernetDhcp();

	/**
	 * 函数名称：setEthernetIpConfiguration 
	 * 函数说明：设置以太网ip配置，动态ip或者静态ip
	 * 参数说明：isDhcp:true表示自动dhcp, false表示手动dhcp idAddr:手动这只ip地址 mask：子网掩码
	 *         dns：域名地址 gateway：网关地址 
	 * 返回值说明：true:成功； false：失败； 默认为false 
	 * 作者：胡星
	 * 日期：2017/12/4 16:09
	 */
	boolean setEthernetIpConfiguration(boolean isDhcp, String ipAddr, String mask, String dns, String gateway);
}
