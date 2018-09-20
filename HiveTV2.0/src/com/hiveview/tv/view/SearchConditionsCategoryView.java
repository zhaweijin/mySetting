package com.hiveview.tv.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.R;
import com.hiveview.tv.service.entity.ThirdListEntity;

/**
 * 分类筛选控件
 * @ClassName: SearchConditionsCategoryView
 * @Description: 
 * @author: zhangpengzhan
 * @date 2014年7月6日 上午11:23:52
 * 
 */
public class SearchConditionsCategoryView extends BaseView {
	
	/**
	 * 每行最小的view 的个数
	 * @Fields minViews
	 */
	private int minViews = 10;

	public SearchConditionsCategoryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SearchConditionsCategoryView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SearchConditionsCategoryView(Context context,List<ThirdListEntity> list) {
		super(context,list);
	}

	/*
	 * 初始化控件 (non-Javadoc)
	 */
	public void initView(List<ThirdListEntity> list) {
		//autoCompletion(list);
		ArrayList<ThirdListEntity> mItemEntityList = new ArrayList<ThirdListEntity>();
		mItemEntityList = autoCompletion(list);
		
		initViewContainer(R.layout.search_conditions_category_layout);
		findItemViewByList(mItemEntityList);
		
		
	}
	
	/**
	 * 补全为奇数
	 * @Title: SearchConditionsCategoryView
	 * @author:张鹏展
	 * @Description: 
	 * @param list
	 * @return
	 */
	public ArrayList<ThirdListEntity>  autoCompletion(List<ThirdListEntity> list){
		ArrayList<ThirdListEntity> entites = (ArrayList<ThirdListEntity>) list;
		ThirdListEntity entity = new ThirdListEntity();
		entity.setThirdClassId(0);
		entity.setThirdClassName("全部");
		entites.add((int)list.size()/2,entity);
		if(entites.size() % 2 != 1){
			ThirdListEntity entity1 = new ThirdListEntity();
			entity1.setThirdClassId(0);
			entity1.setThirdClassName("none");
			entites.add(0,entity1);
		}
		return entites;
	}
	

	
}
