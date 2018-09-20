package com.hiveview.tv.activity;

import android.os.Bundle;
import android.view.View;

import com.hiveview.tv.R;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.ApiConstant;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.DeviceBoxUtils;

public class TestDevActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.setContentView(R.layout.test_dev_layout);
	}

	private int devCount = 0;
	private String errorCode = "";

	public void goDev(View v) {

		submitRequest(new Runnable() {

			@Override
			public void run() {
				while (!errorCode.equals(ErrorCode.CODE_SUCCESS) || devCount <= 40) {
					try {
						errorCode = service.deviceCheck(ApiConstant.APP_VERSION, AppUtil.getLocaldeviceId(getApplicationContext()),DeviceBoxUtils.getMac(), ApiConstant.UUID);
					} catch (ServiceException e) {
						System.out.println("鉴权失败");
					}
					devCount++;
				}
			}
		});

	}

}
