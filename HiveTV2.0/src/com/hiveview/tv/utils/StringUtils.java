package com.hiveview.tv.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hiveview.tv.digest.Charsets;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class StringUtils {
	final static int BUFFER_SIZE = 4096;
	public final static String GB2312 = "gb2312";
	public final static String UTF_8 = "UTF-8";

	/** 获取当前版本号 */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();// 得到包管理对象
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);// 获取指定包的信息
			return info.versionName;// 获取版本
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "unkonwn";
		}
	}

	/**
	 * 把InputStream对象转换成String
	 * 
	 * @param is
	 * @return
	 */
	public static String converStreamToString(InputStream is) {

		if (is == null) {
			LogUtil.info("Response inputStream is null!!!!!!");
			return null;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();

		int count = 0;
		while (count < 3) {

			try {
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				break;
			} catch (IOException e) {
				count++;
			}
		}

		LogUtil.info(buffer.toString());
		return buffer.toString();
	}

	/**
	 * 将InputStream转换成某种字符编码的String
	 * 
	 * @param in
	 * @param encoding
	 * 
	 */
	public static String converStreamToString(InputStream in, String encoding) {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		try {
			while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
				outStream.write(data, 0, count);
			}
			data = null;
			return new String(outStream.toByteArray(), encoding);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static String timeString(int time) {
		time = time / 1000;
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}

	public static String intTimeString(int time) {
		int miao = time % 60;
		int fen = time / 60;
		int hour = 0;
		if (fen >= 60) {
			hour = fen / 60;
			fen = fen % 60;
		}
		String timeString = "";
		String miaoString = "";
		String fenString = "";
		String hourString = "";
		if (miao < 10) {
			miaoString = "0" + miao;
		} else {
			miaoString = miao + "";
		}
		if (fen < 10) {
			fenString = "0" + fen;
		} else {
			fenString = fen + "";
		}
		if (hour < 10) {
			hourString = "0" + hour;
		} else {
			hourString = hour + "";
		}
		if (hour != 0) {
			timeString = hourString + ":" + fenString + ":" + miaoString;
		} else {
			timeString = fenString + ":" + miaoString;
		}
		return timeString;
	}
	
	/**
	 * 秒转换成分钟
	 * @param secondLimit second大于等于此参数才进行转化，否则只返回秒数
	 * @param second
	 * @return
	 */
	public static SecondMinuteHolder second2Minute(int secondLimit, int second) {
		int secondTemp = 0;
		int minuteTemp = 0;
		if (second >= secondLimit) {
			secondTemp = second % 60;
			int leftSecond = second - secondTemp;
			minuteTemp = leftSecond / 60;
		} else {
			secondTemp = second;
		}
		return new SecondMinuteHolder(secondTemp, minuteTemp);
	}
	
	/**
	 * 返回xx分xx秒的格式，如果没有xx分，仅返回xx秒
	 * @return
	 */
	public static String getMinuteSecondString(SecondMinuteHolder holder) {
		StringBuilder builder = new StringBuilder();
		int secondTemp = holder.second;
		int minuteTemp = holder.minute;
		if (minuteTemp > 0) {
			builder.append(minuteTemp).append("分钟");
		}
		if (secondTemp > 0) {
			builder.append(secondTemp).append("秒");
		}
		return builder.toString();
	}
	
	public static boolean isEmpty(String str) {
		if(null != str)
		{
			if(str.length() > 4 )
			{
				return false;
			}
		}
		return null == str || "".equals(str)||"NULL".equals(str.toUpperCase());
	}
	
	public static boolean isEmptyStr(String str) {
		return null == str || "".equals(str);
	}
	
	public static boolean isEmptyArray(Object[] array, int len) {
		return null == array || array.length < len;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	/**
	 * log 的标签
	 * @Fields TAG:TODO
	 */
	private static String TAG = "StringUtils";
	public static String getDateTime(long value) {
		Log.d(TAG,value+"");
		Date date = new Date(value);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Log.d(TAG,format.format(date));

		return format.format(date);
	}

	// Convert Unix timestamp to normal date style
	public static String TimeStamp2Date(String timestampString) {
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(timestamp));
		return date;
	}

	public static double getM(double b) {
		double m;
		double kb;
		kb = b / 1024.0;
		m = kb / 1024.0;
		return m;
	}

	public static String getImage260_360Url(String originalUrl) {
		int dotIndex = originalUrl.lastIndexOf(".");
		String sizeUrlExe = originalUrl.substring(dotIndex, originalUrl.length());
		String sizeUrlHead = originalUrl.substring(0, dotIndex);
		String sizeNewUrl = sizeUrlHead + "_260_360" + sizeUrlExe;
		return sizeNewUrl;
	}
	
	/**
	 * 保存分秒信息
	 * @author haozening
	 *
	 */
	public static class SecondMinuteHolder{
		public final int second;
		public final int minute;
		public SecondMinuteHolder(int second, int minute) {
			super();
			this.second = second;
			this.minute = minute;
		}
	}
	
	/**
	 * 获取当前系统时间 HH:MM:SS
	 * @Title: StringUtils
	 * @author:huzuwei
	 * @Description: TODO
	 * @return
	 */
	public static String getNowDate() {

		String s = null;
		try {

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			s = sdf.format(date);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 将时间转换成long
	 * @Title: StringUtils
	 * @author:huzuwei
	 * @Description: TODO
	 * @param str
	 * @return
	 */
	public static long getTime(String str) {

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		try {
			return sdf.parse(str).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("输入格式有误，返回当前时间");
		}
		return date.getTime();
	}
	
    public static byte[] getBytesUtf8(final String string) {
        return getBytes(string, Charsets.UTF_8);
    }
    
    /**
     * Calls {@link String#getBytes(Charset)}
     *
     * @param string
     *            The string to encode (if null, return null).
     * @param charset
     *            The {@link Charset} to encode the <code>String</code>
     * @return the encoded bytes
     */
    private static byte[] getBytes(final String string, final Charset charset) {
        if (string == null) {
            return null;
        }
        return string.getBytes(charset);
    }
}
