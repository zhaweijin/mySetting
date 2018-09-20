package com.hiveview.tv.utils;

public class HiveViewErrorCode {
	/**
	 * 服务器数据返回成功
	 */
	public final static String N0000000 = "N0000000";
	/**
	 * 鉴权未通过
	 */
	public final static String E0000000 = "E0000000";

	/**
	 * 访问的接口不存在
	 */
	public final static String E0000404 = "E0000404";

	/**
	 * 服务器程序报错
	 */
	public final static String E0000500 = "E0000500";

	/**
	 * 服务器宕机或者运维问题
	 */
	public final static String E0000502 = "E0000502";

	/**
	 * 盒子网络故障，应该弹出设置网络的提示框
	 */
	public final static String E0000599 = "E0000599";

	/**
	 * 解析JSON出错
	 */
	public final static String E0000598 = "E0000598";

	/**
	 * 服务端返回的数据为空
	 */
	public final static String E0000600 = "E0000600";

	/**
	 * 数据字段错误（数据格式错误或内容为空)，比如说运营在cms后台，有某个字段没有填写或爱奇艺同步数据出错，可能会导致我们这边的空指针问题，
	 * 导致程序崩溃。
	 */
	public final static String E0000601 = "E0000601";

	/**
	 * 用户所观看的视频剧集已经下线
	 */
	public final static String E0000602 = "E0000602";

	/**
	 * 推荐位对应的类型出错导致客户端响应出错,比如开发中我遇到的问题是，应用，游戏的页面的推荐，应用专题的类型总是1004（应该为1006的，1004
	 * 是片花的类型）。
	 */
	public final static String E0000603 = "E0000603";

	/**
	 * 蜂巢2.0获取天气信息失败
	 */
	public final static String E0000604 = "E0000604";

	/**
	 * 网络运行时候异常，就是因为硬件、软件原因导致网络访问失败
	 */
	public final static String E0000605 = "E0000605";
	
	/**
	 * 服务器返回的数据格式问题。
	 */
	public final static String E0000607 = "E0000607";
	
	

	/**
	 * 客户端程序报错
	 */
	public final static String E0000606 = "E0000606";
	
	/**
	 *连接超时，服务器忙
	 */
	public final static String E0000608 = "E0000608";
	
	/**
	 * 未知服务器响应出错
	 */
	public final static String E0000609 = "E0000609";
}
