package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.util.Log;

import com.hiveview.tv.service.entity.LiveStreamEntity;
import com.hiveview.tv.service.entity.SubjectEntity;
import com.hiveview.tv.service.entity.SubjectListEntity;
import com.hiveview.tv.service.entity.SubjectListEntitys;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.JsonUtils;
import com.hiveview.tv.utils.StringUtils;

public class LiveStreamListParser extends BaseParser {

	@Override
	public ArrayList<LiveStreamEntity> executeToObject(InputStream in)
			throws ServiceException {
		ArrayList<LiveStreamEntity> arrayList = new ArrayList<LiveStreamEntity>();
		LiveStreamEntity entity = null;

		try {
			String json = StringUtils.converStreamToString(in);
			Log.v("LiveStreamListParser", "json  "+json);
			JSONObject object = new JSONObject(json);
			JSONObject jsonObject = object.optJSONObject("data");
			errorCode = jsonObject.getString("code");
			JSONArray jsonArray = jsonObject.getJSONArray("result");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				entity = new LiveStreamEntity();
				entity.setTv_id(obj.optInt("tv_id"));
				entity.setTv_name(obj.getString("tv_name"));
				entity.setTv_pyname(obj.getString("tv_pyname"));
				entity.setTvlogo(obj.getString("tvlogo"));
				entity.setLiveurl(obj.getString("liveurl"));
				entity.setCp(obj.optInt("cp"));
				entity.setCpId(obj.optInt("cpId"));
				entity.setIsVip(obj.optInt("isVip"));
				entity.setIsFreeLimit(obj.optInt("isFreeLimit"));
			}
			arrayList.add(entity);

		} catch (JSONException e) {
			throw new ServiceException(HiveViewErrorCode.E0000598);
			//e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return arrayList;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

}
