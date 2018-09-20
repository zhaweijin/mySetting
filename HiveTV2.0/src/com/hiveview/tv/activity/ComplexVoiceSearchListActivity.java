package com.hiveview.tv.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.content.ContentInvoker;
import com.hiveview.tv.common.voice.search.impl.ComplexSearch;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.FilmRelatedNewEntity;
import com.hiveview.tv.service.entity.SearchEntity;
import com.hiveview.tv.service.entity.TotalListFilmNewEntity;
import com.hiveview.tv.service.entity.TotalListSearchEntity;
import com.hiveview.tv.utils.PlayerParamsUtils;

public class ComplexVoiceSearchListActivity extends BaseVoiceSearchNewListActivity {

	private Intent searchIntent;
	
	@Override
	protected void search(Intent intent, int pageSize, int connectNum, int filter) {
		searchIntent = intent;
		int searchMethod = intent.getIntExtra(ComplexSearch.DATA_SEARCH_METHOD, -1);
		switch (searchMethod) {
		case ComplexSearch.SEARCH_BY_TAGS:
			searchByTag(pageSize, connectNum, filter);
			break;
		case ComplexSearch.SEARCH_CATEGORY:
			searchCategory(pageSize, connectNum, filter);
			break;
		case ComplexSearch.SEARCH_MOVIE_NAME:
			searchMovieName(pageSize, connectNum, filter);
			break;
		case ComplexSearch.SEARCH_PERSON_NAME:
			searchPersonRelatedFilm(pageSize, connectNum, filter);
			break;
		case ComplexSearch.SEARCH_INDEX:
			break;
		case ComplexSearch.SEARCH_PREVIOUS:
			break;
		case ComplexSearch.SEARCH_NEXT:
			break;
		}
	}

	@Override
	protected void onItemClick(FilmNewEntity entity) {
		
		String action = ContentInvoker.getInstance().getContentAction(
				entity.getSeriesType());
		if (entity.getCid() == AppConstant.VIDEO_TYPE_BLUE) {
			Intent intent = new Intent();
			intent.setAction(action);
			intent.putExtra("videoset_id", entity.getId());
			sendBroadcast(intent);
		} else if(entity.getCid() == AppConstant.VIDEO_TYPE_CLIPS) {
			Intent intent = new Intent();
			intent.setAction(action);
			Bundle bundle = new Bundle();
			bundle.putString("play_info", createClipsEntityJson(entity));
			intent.putExtras(bundle);
			sendBroadcast(intent);
		} else if (null == action) {
			PlayerParamsUtils.getVideoPlayParams(entity.getId(), entity.getCid(),"", getApplicationContext(),null);
		} else {
			Intent intent = new Intent();
			intent.setAction(action);
			intent.putExtra("id", entity.getId());
			startActivity(intent);
		}
	}
	
	private String createClipsEntityJson(FilmNewEntity entity) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("videoset_id", entity.getId());
			jo.put("videoset_name", entity.getName());
			jo.put("videoset_type", entity.getCid());
			jo.put("videoset_img", entity.getPosterUrl());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo.toString();
	}
	
	/**
	 * 通过标签筛选
	 * @param pageSize
	 * @param connectNum
	 * @param filter
	 */
	private void searchByTag(int pageSize, int connectNum, int filter) {
		ArrayList<TotalListFilmNewEntity> list = combineTagSearchData(pageSize, connectNum, filter);
//		String tags = searchIntent.getStringExtra(ComplexSearch.DATA_TAGS);
//		ArrayList<TotalListFilmEntity> list = getService().getVideoSetListByTag(this, 16, tags, pageSize, connectNum);
		setList(list);
	}
	
	/**
	 * 获取对应分类列表
	 * @param pageSize
	 * @param connectNum
	 * @param filter
	 */
	private void searchCategory(int pageSize, int connectNum, int filter) {
		int category = searchIntent.getIntExtra(ComplexSearch.DATA_CATEGORY, -1);
		ArrayList<TotalListFilmNewEntity> list = getService().getVideoSetList(this, category, pageSize, connectNum);
		setList(list);
	}
	
	/**
	 * 按电影名搜索
	 * @param pageSize
	 * @param connectNum
	 * @param filter
	 */
	private void searchMovieName(int pageSize, int connectNum, int filter) {
		String name = searchIntent.getStringExtra(ComplexSearch.DATA_MOVIE_NAME);
		ArrayList<TotalListSearchEntity> list = getService().getSearchRecord("", "", name, connectNum, pageSize);
		setList(convertSearchEntityToFilmEntity(list));
	}

	/**
	 * 俺演职员搜索相关电影
	 * @param pageSize
	 * @param connectNum
	 * @param filter
	 */
	private void searchPersonRelatedFilm(int pageSize, int connectNum, int filter) {
		ArrayList<TotalListSearchEntity> list = searchPersonName(pageSize, connectNum, filter);
		ArrayList<FilmRelatedNewEntity> relatedEntities = getPersonRelatedListEntities(list);
		setList(convertRelatedEntityToFilmEntity(relatedEntities));
	}
	
	/**
	 * 将搜索的影片信息转换成列表展示用的影片信息
	 * @param list
	 * @return
	 */
	private ArrayList<TotalListFilmNewEntity> convertSearchEntityToFilmEntity(ArrayList<TotalListSearchEntity> list) {
		ArrayList<TotalListFilmNewEntity> arrayList = new ArrayList<TotalListFilmNewEntity>();
		for (TotalListSearchEntity listSearchEntity : list) {
			TotalListFilmNewEntity listFilmEntity = new TotalListFilmNewEntity();
			listFilmEntity.setRecCount(listSearchEntity.getRecCount());
			listFilmEntity.setPositionInItemView(listSearchEntity.getPositionInItemView());
			ArrayList<FilmNewEntity> filmEntities = new ArrayList<FilmNewEntity>();
			for (SearchEntity searchEntity : listSearchEntity.getFilms()) {
				FilmNewEntity entity = new FilmNewEntity();
				entity.setPositionInItemView(searchEntity.getPositionInItemView());
				entity.setId(searchEntity.getRecord_id());
				entity.setPosterUrl(searchEntity.getRecord_img());
				entity.setSeriesType(searchEntity.getRecord_type());
				entity.setName(searchEntity.getRecord_name());
				filmEntities.add(entity);
			}
			listFilmEntity.setFilms(filmEntities);
			arrayList.add(listFilmEntity);
		}
		return arrayList;
	}
	
	/**
	 * 将搜索的演职员相关电影信息转换成列表展示用的影片信息
	 * @param list
	 * @return
	 */
	private ArrayList<TotalListFilmNewEntity> convertRelatedEntityToFilmEntity(ArrayList<FilmRelatedNewEntity> relatedEntities) {
		ArrayList<TotalListFilmNewEntity> arrayList = new ArrayList<TotalListFilmNewEntity>();
		for (FilmRelatedNewEntity relatedEntity : relatedEntities) {
			TotalListFilmNewEntity listFilmEntity = new TotalListFilmNewEntity();
			listFilmEntity.setPositionInItemView(relatedEntity.getPositionInItemView());
			ArrayList<FilmNewEntity> filmEntities = new ArrayList<FilmNewEntity>();
			for (FilmNewEntity filmEntity : relatedEntity.getVidesetList()) {
				FilmNewEntity entity = filmEntity;
				filmEntities.add(entity);
			}
			listFilmEntity.setFilms(filmEntities);
			arrayList.add(listFilmEntity);
		}
		return arrayList;
	}

	/**
	 * 获取和演职员相关的所有电影信息
	 * @param list
	 * @return
	 */
	private ArrayList<FilmRelatedNewEntity> getPersonRelatedListEntities(ArrayList<TotalListSearchEntity> list) {
		ArrayList<FilmRelatedNewEntity> filmRelatedEntities = new ArrayList<FilmRelatedNewEntity>();
		for(TotalListSearchEntity listSearchEntity : list) {
			ArrayList<SearchEntity> entities = listSearchEntity.getFilms();
			for (SearchEntity entity : entities) {
//				filmRelatedEntities.addAll(getService().getCastRelated(entity.getRecord_id()));
			}
		}
		return filmRelatedEntities;
	}
	
	/**
	 * 获取相关演职员信息
	 * @param pageSize
	 * @param connectNum
	 * @param filter
	 * @return
	 */
	private ArrayList<TotalListSearchEntity> searchPersonName(int pageSize, int connectNum, int filter) {
		String name = searchIntent.getStringExtra(ComplexSearch.DATA_PERSON_NAME);
		ArrayList<TotalListSearchEntity> list = getService().getSearchRecord("0", "0", name, connectNum, pageSize);
		return list;
	}
	
	/**
	 * 组合按标签搜索结果
	 * @param pageSize
	 * @param connectNum
	 * @param filter
	 * @return
	 */
	private ArrayList<TotalListFilmNewEntity> combineTagSearchData(int pageSize, int connectNum, int filter) {
		ArrayList<TotalListFilmNewEntity> list = new ArrayList<TotalListFilmNewEntity>();
		ArrayList<Integer> categories = searchIntent.getIntegerArrayListExtra(ComplexSearch.DATA_CATEGORY);
		String tags = searchIntent.getStringExtra(ComplexSearch.DATA_TAGS);
		int pageNumBeforeNextRequest = 0;
		for (Integer i : categories) {
			if (list.isEmpty()) { // 如果筛选结果为空，就进行第一次请求数据
				ArrayList<TotalListFilmNewEntity> temp = getService().getVideoSetListByTag(this, i, tags, pageSize, connectNum);
				pageNumBeforeNextRequest = (int)Math.ceil(temp.get(0).getRecCount() / pageSize);
				list.addAll(temp);
			} else if(list.get(0).getFilms().isEmpty() || list.get(0).getFilms().size() < pageSize) { // 如果筛选结果电影为空，或者筛选结果列表总数不够当前页需要的数量，就进行其他接口的请求
				if (list.get(0).getFilms().isEmpty() || list.size() == 0) {
					ArrayList<TotalListFilmNewEntity> temp = getService().getVideoSetListByTag(this, i, tags, pageSize, connectNum - pageNumBeforeNextRequest);
					list.get(0).getFilms().addAll(temp.get(0).getFilms());
				} else {
					int tempSize = list.get(0).getFilms().size();
					ArrayList<TotalListFilmNewEntity> temp = getService().getVideoSetListByTag(this, i, tags, pageSize - tempSize, connectNum);
					// 不能使用temp.get(0).getRecCount()，不准确（如果搜索结果为0，getRecCount却为1，如：动作电影）
					list.get(0).getFilms().addAll(temp.get(0).getFilms());
				}
			}
		}
		int tempResultSize = 0;
		for (Integer i : categories) {
			ArrayList<TotalListFilmNewEntity> temp = getService().getVideoSetListByTag(this, i, tags, pageSize, connectNum);
			if (!temp.get(0).getFilms().isEmpty() && temp.get(0).getFilms().size() > 0) {
				tempResultSize += temp.get(0).getFilms().size();
			}
		}
		list.get(0).setRecCount(tempResultSize);
		return list;
	}
	
	public static class EpisodeHolder{
		public static String cpVideoSetId;
	}
}
