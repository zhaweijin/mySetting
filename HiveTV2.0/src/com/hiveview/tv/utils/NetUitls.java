package com.hiveview.tv.utils;

public class NetUitls {

	public static boolean ping() {
		boolean result = false;
		try {
			java.lang.Process p = Runtime.getRuntime().exec("ping -c 2 -s 100 -w 3 api.pthv.gitv.tv");
			int status = p.waitFor();
			if (status == 0) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			return result;
		}
		return result;
	}

}
