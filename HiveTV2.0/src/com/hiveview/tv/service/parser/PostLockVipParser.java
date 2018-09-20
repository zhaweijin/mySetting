package com.hiveview.tv.service.parser;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.entity.NetLockEntity;
import com.hiveview.tv.service.entity.VipLockEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.StringUtils;

/**
 * 静默升级类表解析器
 * 
 * @ClassName: UpgraderListParser
 * @Description: TODO
 * @author: guosongsheng
 * @date 2014年9月17日 上午10:14:56
 * 
 */
public class PostLockVipParser extends BaseParser {

	private String errorCode;

	@Override
	public ArrayList<VipLockEntity> executeToObject(InputStream in) throws ServiceException {
		ArrayList<VipLockEntity> lockEntities = new ArrayList<VipLockEntity>();
		VipLockEntity lockEntity = new VipLockEntity();
		try {
			String json = StringUtils.converStreamToString(in);
			JSONObject jsonObject = new JSONObject(json);
			if (null != jsonObject) {
				errorCode = jsonObject.getString("ErrorMessage");
				//if (errorCode.equals(ErrorCode.CODE_SUCCESS)) {
					lockEntity.setResult(jsonObject.optBoolean("Result"));
					lockEntity.setDamaiID(jsonObject.optString("DamaiID"));
					lockEntity.setErrorMessage(jsonObject.getString("ErrorMessage"));
					lockEntity.setStatus(jsonObject.optBoolean("Status"));
					lockEntities.add(lockEntity);
				//}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ServiceException(HiveViewErrorCode.E0000598);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(HiveViewErrorCode.E0000607);
		}
		return lockEntities;
	}

	@Override
	public String getErrorCode() {
		// TODO Auto-generated method stub
		return errorCode;
	}

}
