package com.hiveview.tv.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.hiveview.tv.digest.DigestUtils;


public class SignUtils {

	public final static String shardedKey="04S78gY88h79576ml10jbb29phlc34ana6p2oVVxq";
	
	public static String sign(Map<String, String> parameterMap, String shardedKey) {
		String src = buildQuery(parameterMap) + "&key=" + shardedKey;
		String sign = DigestUtils.sha256Hex(src).toUpperCase();
		return sign;
	}
	
	public static String buildQuery(Map<String, String> map) {
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		StringBuffer query = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = map.get(key);
			if (value == null || value.length() == 0) {
				continue;
			}
			query.append("&").append(key);
			query.append("=").append(value);
		}
		return query.substring(1);
	}

}
