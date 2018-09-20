package com.hiveview.tv.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SeekBar;

public class DateUtils {

	/**
	 * 计算两个yyyy-MM-dd HH:mm:ss 格式的时间的差值,单位为毫秒数
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	private static final String TAG = "DateUtils";
	
	/**
	 * @Fields oneHour:一个小时的时长
	 */
	public static int oneHour = 1 * 60 * 60 * 1000;
	/**
	 * @Fields tenMinute:十分钟
	 */
	public static int tenMinute = 10 * 60 * 1000;
	
	/**
	 * 计算两个时间差
	 * @Title: DateUtils
	 * @author:郭松胜
	 * @Description: TODO
	 * @param strBegin 开始时间
	 * @param strEnd  结束时间
	 * @return 如果strEnd为空返回-2001,否则正常返回
	 */
	@SuppressLint("SimpleDateFormat")
	public static long twoTimeDiffer(String strBegin, String strEnd) {
		//如果strEnd为空返回-2001
		if(TextUtils.isEmpty(strEnd)){
			return -2001 ;
		}
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long between = 0;
		try {
			Date begin = dfs.parse(strBegin);
			Date end = dfs.parse(strEnd);
			between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return between;
	}

	/**
	 * 根据节目的开始，结束时间和当前时间，确定播放进度，显示在进度条上
	 * 
	 * @param bar
	 *            显示进度的进度条，是SeekBar对象
	 * @param startTime
	 *            节目开始时间
	 * @param endTime
	 *            节目结束时间
	 */
	@SuppressWarnings("deprecation")
	public static void setProgramPlayProgress(SeekBar bar, String startTime,
			String endTime) {

		if (startTime.equals("") || null == startTime || endTime.equals("")
				|| null == endTime) {
			return;
		}

		String fullStartDateTime = "";
		String fullEndDateTime = "";

		Calendar calendar = Calendar.getInstance();
		// 时间格式是 hh:mm,格式化成yyyy-mm-dd hh:mm
		fullStartDateTime = startTime.length() <= 5 ? calendar
				.get(Calendar.YEAR)
				+ "-"
				+ (calendar.get(Calendar.MONTH) + 1)
				+ "-"
				+ calendar.get(Calendar.DAY_OF_MONTH)
				+ " "
				+ startTime
				+ ":" + calendar.get(Calendar.SECOND) : startTime;
		fullEndDateTime = endTime.length() <= 5 ? calendar.get(Calendar.YEAR)
				+ "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + " " + endTime + ":"
				+ calendar.get(Calendar.SECOND) : endTime;

		// 计算总差值
		long endLength = DateUtils.twoTimeDiffer(fullStartDateTime,
				fullEndDateTime);
		bar.setMax((int) endLength);

		Date nowDate = new Date();
		// 计算进度值
		long currentProgress = DateUtils.twoTimeDiffer(fullStartDateTime,
				DateUtils.formatTimeYyMmDdHhMmSs(nowDate));
		bar.setProgress((int) currentProgress);
	}

	/**
	 * 
	 * 将yyyy-MM-dd HH:mm:ss格式的时间格式化话hh:mm格式
	 * 
	 * @param fullDate
	 * @return
	 */
	public static String getTimeFromFullDate(String fullDate) {
		Date date = null;
		Date endDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = formatter.parse(fullDate);

			String sHour = date.getHours() >= 10 ? date.getHours() + "" : "0"
					+ date.getHours();
			String sMinute = date.getMinutes() >= 10 ? date.getMinutes() + ""
					: "0" + date.getMinutes();
			return sHour + ":" + sMinute;
		} catch (ParseException e) {

		}

		return "00:00";
	}

	/**
	 * 
	 * 将时间格式化为yyyy-MM-dd HH:mm:ss的格式
	 * 
	 * @param fullDate
	 * @return
	 */
	public static String formatTimeYyMmDdHhMmSs(Date nowTime) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return time.format(nowTime);
	}

	/**
	 * 获取3分钟后的时间
	 * 
	 * @return 3分钟后的时间
	 */
	public static String getAfterMinuteDate(long l) {
		Date nowDate = new Date();
		Date afterDate = new Date(nowDate.getTime() + l);
		Log.d(TAG, "dateafter is :" + afterDate);
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return time.format(afterDate);

	}

	/**
	 * 根据日期获得所在周的日期
	 * 
	 * @param mdate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<String> dateToWeek() {
		Date currentDate = new Date();
		// 定义输出日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		// int b = currentDate.getDay();
		// 星期日获取的数据为0 需要转换一下
		int b = currentDate.getDay() == 0 ? 7 : currentDate.getDay();
		Date fdate;
		List<String> list = new ArrayList<String>();
		Long fTime = currentDate.getTime() - b * 24 * 3600000;
		for (int a = 1; a <= 7; a++) {
			fdate = new Date();
			fdate.setTime(fTime + (a * 24 * 3600000));
			list.add(a - 1, sdf.format(fdate));
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	public static List<String> date2Week() {
		Date currentDate = new Date();
		// 定义输出日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 星期日获取的数据为0 需要转换一下
		int b = currentDate.getDay() == 0 ? 7 : currentDate.getDay();
		Date fdate;
		List<String> list = new ArrayList<String>();
		Long fTime = currentDate.getTime() - b * 24 * 3600000;
		for (int a = 1; a <= 7; a++) {
			fdate = new Date();
			fdate.setTime(fTime + (a * 24 * 3600000));
			list.add(a - 1, sdf.format(fdate));
		}
		return list;
	}

	public static String getCurrentDate() {
		// 定义输出日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		Date currentDate = new Date();
		return sdf.format(currentDate);
	}
	
	/**
	 * @Title: DateUtils
	 * @author:张鹏展
	 * @Description: 得到当前时间的年份
	 * @return
	 */
	public static int getCurrentDateToYears(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy",Locale.CHINA);
		Date currentDate = new Date();
		Log.d(TAG, "getCurrentDateToYears"+sdf.format(currentDate));
		return Integer.valueOf(sdf.format(currentDate));
	}
	/**
	 * @Title: DateUtils
	 * @author:张鹏展
	 * @Description: 得到当前时间的月份
	 * @return
	 */
	public static int getCurrentDateToMonth(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM",Locale.CHINA);
		Date currentDate = new Date();
		Log.d(TAG, "getCurrentDateToMonth"+sdf.format(currentDate));
		return Integer.valueOf(sdf.format(currentDate));
	}
	/**
	 * @Title: DateUtils
	 * @author:张鹏展
	 * @Description: 得到当前时间日期
	 * @return
	 */
	public static int getCurrentDateToDay(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd",Locale.CHINA);
		Date currentDate = new Date();
		Log.d(TAG, "getCurrentDateToDay"+sdf.format(currentDate));
		return Integer.valueOf(sdf.format(currentDate));
	}
	/**
	 * @Title: DateUtils
	 * @author:张鹏展
	 * @Description: 得到当前时间小时
	 * @return
	 */
	public static int getCurrentDateToHour(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH",Locale.CHINA);
		Date currentDate = new Date();
		Log.d(TAG, "getCurrentDateToHour"+sdf.format(currentDate));
		return Integer.valueOf(sdf.format(currentDate));
	}
	/**
	 * @Title: DateUtils
	 * @author:张鹏展
	 * @Description: 得到当前时间分钟
	 * @return
	 */
	public static int getCurrentDateToMinute(){
		SimpleDateFormat sdf = new SimpleDateFormat("mm",Locale.CHINA);
		Date currentDate = new Date();
		Log.d(TAG, "getCurrentDateToMinute"+sdf.format(currentDate));
		return Integer.valueOf(sdf.format(currentDate));
	}
	/**
	 * 
	 * 
	 * 获取当日 前后时间
	 * 
	 * @param delta_T
	 *            时间差
	 * @return
	 */
	public static String getDateYyMmDd(int delta_T) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, delta_T);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);

		return dateString;
	}

	// string类型转换为date类型
	// strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
	// HH时mm分ss秒，
	// strTime的时间格式必须要与formatType的时间格式相同
	public static Date stringToDate(String strTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType,
				Locale.CHINA);
		Date date = null;
		date = formatter.parse(strTime);
		return date;
	}

	// string类型转换为long类型
	// strTime要转换的String类型的时间
	// formatType时间格式
	// strTime的时间格式和formatType的时间格式必须相同
	public static long stringToLong(String strTime, String formatType)
			throws ParseException {
		Date date = stringToDate(strTime, formatType); // String类型转成date类型
		if (date == null) {
			return 0;
		} else {
			long currentTime = dateToLong(date); // date类型转成long类型
			return currentTime;
		}
	}

	// date类型转换为String类型
	// dateTime要转换的String类型的时间
	// formatType时间格式
	// dateTime的时间格式和formatType的时间格式必须相同
	public static String dateToString(Date dateTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		String dateStr = formatter.format(dateTime);
		return dateStr;
	}

	// date类型转换为long类型
	// date要转换的date类型的时间
	public static long dateToLong(Date date) {
		return date.getTime();
	}

	/**
	 * 将long型时间转换成XXXX年XX月XX日的格式 暂时不用
	 * 
	 * @Title: DateUtils
	 * @author:郭松胜
	 * @Description: TODO Date型转换成String型 格式 XXXX年XX月XX日
	 */
	public static String dataToString(long date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date);
		Formatter ft = new Formatter(Locale.CHINA);
		return ft.format("%1$tY年%1$tm月%1$td日", cal).toString();
	}
	
	/**
	 * 将long型时间转换成XXXX年XX月的格式 暂时不用
	 * 
	 * @Title: DateUtils
	 * @author:郭松胜
	 * @Description: TODO Date型转换成String型 格式 XXXX年XX月XX日
	 */
	public static String dateToStringYM(long date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date);
		Formatter ft = new Formatter(Locale.CHINA);
		return ft.format("%1$tY年%1$tm月", cal).toString();
	}
	
	/**
	 * 将long型时间转换成XX日的格式 暂时不用
	 * 
	 * @Title: DateUtils
	 * @author:郭松胜
	 * @Description: TODO Date型转换成String型 格式 XXXX年XX月XX日
	 */
	public static String dateToStringD() {
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int date = c.get(Calendar.DATE); 
		return date +"";
	}

	/**
	 * @Title: DateUtils
	 * @author:郭松胜
	 * @Description: TODO 获取当前日期dt对应周几
	 * @param dt 当前日期
	 * @return
	 */
	public static String getDateWithWeek(Date dt) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	
	/**
	 * 判断两个日期是否相同
	 * @Title: DateUtils
	 * @author:郭松胜
	 * @Description:
	 * @param dateA
	 * @param dateB
	 * @return
	 */
	public static boolean isSameDay(Date dateA,Date dateB) {
	    Calendar calDateA = Calendar.getInstance();
	    calDateA.setTime(dateA);

	    Calendar calDateB = Calendar.getInstance();
	    calDateB.setTime(dateB);
	    boolean isSame =calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
	            && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
	            &&  calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
	    return isSame;
	}
	
	/**
	 * @Title: DateUtils
	 * @author:张鹏展
	 * @Description: 判断一个数字手否在一个区间内，
	 * 例如:7 在1~10之间，返回值true
	 * @param current 需要判定的数字
	 * @param min 区间最小边界值
	 * @param max 区间最大边界值
	 * @return
	 */
	public static boolean rangeInDefined(int current, int min, int max)  
    {  
        return Math.max(min, current) == Math.min(current, max);  
    }
	
	
}
