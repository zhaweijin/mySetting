package com.hiveview.tv.common.voice.search.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hiveview.tv.R;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.voice.IVoiceController;
import com.hiveview.tv.common.voice.search.IVoiceSearch;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.FirstClassListEntity;
import com.hiveview.tv.service.net.HttpTaskManager;

/**
 * 类名：ComplexSearch
 * <pre>
 * name String 名称
 * person String 演员
 * category String 分类：电视剧、电影、体育、动漫、综艺...
 * area String 地区
 * modifier String 类型：动作|喜剧|爱情|青春|惊怵
 * 
 * *********相关逻辑*********
 * 1、name存在，直接搜索
 * 2、person存在，直接搜人相关的电影
 * 3、
 *   1)、modifier存在，记录tag
 *   2)、area存在，记录tag
 *   3)、如果以上两项不存在，结束
 *   如果存在，继续如下判断
 *    category存在，调用videoSetListByTag，指定videoSetType和分类名
 *    category不存在，调用videoSetListByTag，指定videoSetType为蓝光极清和分类名
 * 5、category存在  调用 videoSetList接口
 * **************************
 * </pre>
 * 用到的接口:
 * <ul>
 * 	<li>HiveTVService().getSearchRecord() // 1.0搜索接口</li>
 * 	<li>HiveTVService().getVideoSetListByTag() // VideoSetListByTag接口</li>
 * 	<li>HiveTVService().getFirstClassList() // FirstClassList接口</li>
 * </ul>
 * 
 * @author haozening
 *
 */
public class ComplexSearch implements IVoiceSearch {
	
	/**
	 * 搜索指定的方法
	 */
	public static final String DATA_SEARCH_METHOD = "search_method";
	public static final String DATA_MOVIE_NAME = "movie_name";
	public static final String DATA_PERSON_NAME = "person_name";
	public static final String DATA_TAGS = "tags";
	public static final String DATA_CATEGORY = "category";
	public static final String DATA_INDEX = "index";
	
	public static final int SEARCH_MOVIE_NAME = 1001;
	public static final int SEARCH_PERSON_NAME = 1002;
	public static final int SEARCH_BY_TAGS = 1003;
	public static final int SEARCH_CATEGORY = 1004;
	public static final int SEARCH_INDEX = 1005;
	public static final int SEARCH_PREVIOUS = 1006;
	public static final int SEARCH_NEXT = 1007;
	public Class<? extends Activity> clazz;
	private Context context;
	
	@SuppressLint("UseSparseArrays")
	private static final Map<Integer, String> CHANNELS = new HashMap<Integer, String>();
	/**
	 * 语音识别到的频道id，对应的所有需要搜索的频道列表
	 */
	private static final Map<Integer, List<Integer>> SEARCH_CHANNEL_LIST = new HashMap<Integer, List<Integer>>();
	private static boolean isGetChannels = false;

	static {
		// 获取频道列表
		new HttpTaskManager().submit(new Runnable() {

			@Override
			public void run() {
				ArrayList<FirstClassListEntity> channels = new HiveTVService().getFirstClassList(HiveviewApplication.mContext.getResources().getString(R.string.language));
				ArrayList<FirstClassListEntity> virtualChannels = new HiveTVService().getVirtualFirstClassList(HiveviewApplication.mContext.getResources().getString(R.string.language));
				for (FirstClassListEntity entity : channels) {
					CHANNELS.put(entity.getFirstclass_id(), entity.getFirstclass_name());
				}
				for (FirstClassListEntity entity : virtualChannels) {
					CHANNELS.put(entity.getFirstclass_id(), entity.getFirstclass_name());
				}
				
				// #################注册频道对应的所有频道列表##################
				SEARCH_CHANNEL_LIST.put(1, Arrays.asList(1, 16)); // 电影频道对应电影和蓝光极清
				
				isGetChannels = true;
			}
		});
	}
	
	public ComplexSearch(Context context, Class<? extends Activity> clazz) {
		this.context = context;
		this.clazz = clazz;
	}

	@Override
	public void search(Intent intent) {
		parserIntent(intent);
		// 获取预定义信息
		if (Holder.action != null && isSelectEpisodeAction()) {
			selectAction();
		} else if (Holder.name != null){ // 如果有电影名，直接搜索
			searchByMovieName();
		} else if (Holder.person != null) { // 如果有人名，直接搜索相关电影
			searchByPersonName();
		} else {
			tagSearch();
		}
	}

	/**
	 * 解析intent
	 * @param intent
	 */
	public void parserIntent(Intent intent) {
		Holder.clean();
		Holder.name = intent.getStringExtra("name");
		Holder.person = intent.getStringExtra("person");
		Holder.modifier = intent.getStringExtra("modifier");
		Holder.category = intent.getStringExtra("category");
		Holder.area = intent.getStringExtra("area");
		Holder.action = intent.getStringExtra(IVoiceController.ACTION);
		Holder.index = intent.getStringExtra("index");
	}

	/**
	 * 是否是选集用的Action
	 * @return
	 */
	private boolean isSelectEpisodeAction() {
		if (Holder.action.equals(INDEX) || Holder.action.equals(PREVIOUS) || Holder.action.equals(NEXT)) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 通过预定义语义的Action选择相应操作
	 */
	private void selectAction() {
		// 判断指定页是否为空，如果不为空并且action为"index"说明是请求要看第几集
		if (Holder.index != null && Holder.action.equals(INDEX)) {
			assignedIndex();
		} else {
			if (Holder.action.equals(PREVIOUS)) { // 上一集
				previous();
			} else if (Holder.action.equals(NEXT)) { // 下一集
				next();
			}
		}
	}
	
	/**
	 * 通过标签进行搜索，使用多维度筛选
	 */
	private void tagSearch() {
		String tags = createTags();
		// 如果没有获取频道列表，结束此次操作
		if (!isGetChannels) {
			return;
		}
		if (Holder.category != null) { // 如果有分类，videoset_type为category对应的id
			// 如果没有标签，但是有分类，就直接按分类查询
			if (tags.length() <= 0){
				searchVideoSetList();
				return;
			}
			searchByTag(tags, true);
		} else { 
			// 如果没有标签，并且没有分类，就结束此次操作
			if (tags.length() <= 0){
				return;
			}
			// 如果没有，videoset_type为蓝光极清对应的id
			searchByTag(tags, false);
		}
	}
	
	/**
	 * 创建标签
	 * @return
	 */
	private String createTags() {
		StringBuilder builder = new StringBuilder();
		// 如果有类型使用VideoSetListByTag多维度筛选搜索
		if (Holder.modifier != null) {
			builder.append(Holder.modifier).append(",");
		}
		// 如果有地区使用VideoSetListByTag多维度筛选搜索
		if (Holder.area != null) {
			builder.append(Holder.area).append(",");
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}
	
	private ArrayList<Integer> getChannel() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (Map.Entry<Integer, String> entry : CHANNELS.entrySet()) {
			String category = entry.getValue();
			if (category.contains(Holder.category) || Holder.category.contains(category)) {
				if (SEARCH_CHANNEL_LIST.containsKey(entry.getKey())) {
					list.addAll(SEARCH_CHANNEL_LIST.get(entry.getKey()));
					break;
				}
				list.add(entry.getKey());
			}
		}
		return list;
	}

	/**
	 * 通过电影名搜索
	 */
	private void searchByMovieName() {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(DATA_SEARCH_METHOD, SEARCH_MOVIE_NAME);
		intent.putExtra(DATA_MOVIE_NAME, Holder.name);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 通过人名搜索
	 */
	private void searchByPersonName() {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(DATA_SEARCH_METHOD, SEARCH_PERSON_NAME);
		intent.putExtra(DATA_PERSON_NAME, Holder.person);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	/**
	 * 通过标签搜索
	 * @param tags 标签列表
	 * @param hasVideoSetType 是否有指定的频道ID，如果没有，指定为蓝光的id(16)
	 */
	private void searchByTag(String tags, boolean hasVideoSetType) {
		if (hasVideoSetType) {
			Intent intent = new Intent(context, clazz);
			intent.putExtra(DATA_SEARCH_METHOD, SEARCH_BY_TAGS);
			intent.putIntegerArrayListExtra(DATA_CATEGORY, getChannel());
			intent.putExtra(DATA_TAGS, tags);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} else {
			ArrayList<Integer> channels = new ArrayList<Integer>();
			channels.add(16);
			Intent intent = new Intent(context, clazz);
			intent.putExtra(DATA_SEARCH_METHOD, SEARCH_BY_TAGS);
			intent.putIntegerArrayListExtra(DATA_CATEGORY, channels);
			intent.putExtra(DATA_TAGS, tags);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}
	
	/**
	 * 如果只有分类(category)直接查询
	 */
	private void searchVideoSetList() {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(DATA_SEARCH_METHOD, SEARCH_CATEGORY);
		intent.putExtra(DATA_CATEGORY, getChannel());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	/**
	 * 指定第几集
	 */
	private void assignedIndex() {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(DATA_SEARCH_METHOD, SEARCH_INDEX);
		intent.putExtra(DATA_INDEX, Holder.index);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	/**
	 * 上一集
	 */
	private void previous() {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(DATA_SEARCH_METHOD, SEARCH_PREVIOUS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	/**
	 * 下一集
	 */
	private void next() {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(DATA_SEARCH_METHOD, SEARCH_NEXT);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	static class Holder {
		static String name; // 电影名
		static String person; // 人名
		static String modifier; // 类型(动作|喜剧|爱情...)
		static String category; // 分类(电视剧、电影、体育、动漫、综艺...)
		static String area; // 地区
		static String action; // 剧集(上一集、下一集、第几集)
		static String index; // 第几集

		static void clean() {
			name = null;
			person = null;
			modifier = null;
			category = null;
			area = null;
			action = null;
			index = null;
		}
	}
	
}
