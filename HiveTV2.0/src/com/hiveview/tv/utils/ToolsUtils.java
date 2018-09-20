package com.hiveview.tv.utils;

import android.annotation.SuppressLint;
import android.text.format.Formatter;


@SuppressLint("DefaultLocale")
public class ToolsUtils {

	public static String createImgUrl(String url,boolean isPortrait){
		if(url==null || url.equals("") || url.equals("null")){
			return "";
		}
		int position = url.lastIndexOf(".");
		if(position == -1)
			return "";
		String subString = url.substring(0,position);
		String subString1 = url.substring(position,url.length());
		StringBuilder newUrl ;
		if(isPortrait){
			newUrl = new StringBuilder(subString).append("_260_360").append(subString1);
		}else{
			newUrl = new StringBuilder(subString).append("_320_180").append(subString1);
		}
		return newUrl.toString();
	}
	/**
	 * @Title: ToolsUtils
	 * @author:张鹏展
	 * @Description: 已使用内存显示计算的曲线函数
	 * @param x
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String  getSize (double x){
		
		return String.format("%.2f", ((0.275)*x*x-0.1*x));
	}
	/**
	 * @Title: ToolsUtils
	 * @author:张鹏展
	 * @Description: 已使用内存显示计算的曲线函数
	 * @param x
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static int  getSize (long x){
		
		return  Integer.valueOf((int) ((0.275)*x*x-0.1*x));
	}
}
