package com.hiveview.tv.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;

import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.SubjectEntity;
import com.hiveview.tv.service.entity.SubjectInfoList;
import com.hiveview.tv.service.entity.SubjectListEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.LogUtil;
/**
 * 专题入口
 * @ClassName: SubjectJudgeActivity
 * @Description: TODO
 * @author: 周一川
 * 暂时没有用到
 * @date 2014-6-4 上午10:59:11
 *
 */
public class SubjectJudgeActivity extends BaseActivity {

	private ArrayList<SubjectInfoList> subjectInfoList;
	private int subjectId = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		subjectId = getIntent().getIntExtra("id", 0);
		requestSubjects();
	}

	/**
	 * 从网络上请求专题的剧集数据，比如说从专题选择“蝙蝠侠”进入的，此时在这里请求的数据是“蝙蝠侠1”，“蝙蝠侠2”等
	 */
	private void requestSubjects() {
		submitRequest(new SafeRunnable() {
			@Override
			public void requestData() {
				//subjectInfoList = new HiveTVService().getSubjectInfo(subjectId);
//				if (null != subjectInfoList && subjectInfoList.size() > 0) {
//					SubjectInfoList info = subjectInfoList.get(0);
//					SubjectListEntity entity = new SubjectListEntity();
//					entity.setImgSize(info.getImg_size());
//					entity.setSubjectBgImg(info.getSubject_bg_img());
//					entity.setSubjectDesc(info.getSubject_desc());
//					entity.setSubjectId(info.getSubject_id());
//					entity.setSubjectName(info.getSubject_name());
//					entity.setSubjectPic(info.getSubject_pic());
//					String sizeInfo = entity.getImgSize();
//					if (null != sizeInfo && sizeInfo.length() > 0 && sizeInfo.contains("x")) {
//						String[] sizeArray = sizeInfo.split("x");
//						int width = Integer.parseInt(sizeArray[0]);
//						int height = Integer.parseInt(sizeArray[1]);
//						Intent intent = new Intent();
//						intent.putExtra("entity", entity);
//						if (width > height) {
//							intent.setClass(getApplication(), SubjectDetailLandspaceActivity.class);
//						} else {
//							intent.setClass(getApplication(), SubjectDetailPortraitActivity.class);
//						}
//
//						startActivity(intent);
//						finish();
//					}
//				}
			}

			@Override
			public void processServiceException(ServiceException e) {
				e.printStackTrace();
			}
		});
	}

}
