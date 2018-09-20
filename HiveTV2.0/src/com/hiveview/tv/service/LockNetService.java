package com.hiveview.tv.service;

import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import com.hiveview.tv.activity.NetExceptionActivity;
import com.hiveview.tv.service.entity.NetLockEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.DeviceBoxUtils;
import com.hiveview.tv.view.HiveViewNetFaultDialog;
import com.hiveview.tv.view.locknet.LockNetDialogView;

/**
 * 
 * @ClassName: LockNetReceiver
 * @Description: 
 * @author: guosongsheng
 * @date 2014年9月20日 下午6:19:17
 * 
 */
public class LockNetService extends Service {
	/**
	 * 访问接口成功
	 */
	protected static final int GET_NET_LOCK_SUCCESS = 10000;
	/**
	 * 访问接口失败
	 */
	private Context mContext = null;
	/**
	 * 锁网弹出框View
	 */
	private LockNetDialogView lockNetDialogView = null;

	private WindowManager mWm = null;
	/**
	 * 提示框的布局 位置 等数据
	 */
	private WindowManager.LayoutParams mParams;
	/** 
	 * 延迟访问接口的时间
	 */
	private static long FIVE_OF_MINUNTES = 1000 * 60 * 30;
	/**
	 * 延迟访问接口的时间
	 */
	private static long ONE_OF_MINUNTES = 1000 * 60;
	/**
	 * 执行4次后停止服务
	 */
	protected static final int GET_NET_LOCK_FAIL_STOP_SELF = -30000;
	/**
	 * 访问接口失败(time out)
	 */
	protected static final int GET_NET_LOCK_FAIL_TIMEOUT = -1000;
	/**
	 * 访问接口失败,一分钟后再次访问
	 */
	protected static final int GET_NET_LOCK_FAIL_RELOAD = -3000;
	/**
	 * 访问接口失败(json解析错误)
	 */
	protected static final int GET_NET_LOCK_FAIL_JSONERROR = -2000;
	
	HiveViewNetFaultDialog deviceCheckFailedDialog = null;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("LockNet", "GET_SUO_SERVICE onCreate ....................");
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				getLockNet();	
			}
		}, ONE_OF_MINUNTES);
		
	}

	@Override
	public void onStart(Intent intent, int startId) {

	}

	private void getLockNet() {
		new HttpTaskManager().submit(new SafeRunnable() {

			@Override
			public void requestData() {
				Log.i("LockNet", "suo..................start1");
				ArrayList<NetLockEntity> appMarketEntities = new HiveTVService()
						.getLockNet(mContext, DeviceBoxUtils.getDeviceModel(),
								DeviceBoxUtils.getMac(), DeviceBoxUtils.getSn());
				Log.i("LockNet", "suo..................start2:"
						+ appMarketEntities.toString());
				if (null != appMarketEntities && appMarketEntities.size() > 0) {
					boolean isLockNet = appMarketEntities.get(0).isResult();
					Log.i("LockNet", "suo..................start3：" + isLockNet);
					if (isLockNet) {
						Log.i("LockNet", "GET_SUO..................success"
								+ isLockNet);
						handler.sendEmptyMessage(GET_NET_LOCK_SUCCESS);
					} else {
						Log.i("LockNet", "GGET_SUO_FAIL..................fail:"
								+ isLockNet);
						handler.sendEmptyMessage(GET_NET_LOCK_FAIL_STOP_SELF);
					}
				}
			}

			@Override
			public void processServiceException(ServiceException e) {
				Log.i("LockNet",
						"suo..................processServiceException:"
								+ e.getErrorCode());
/*				// 盒子网络故障，应该弹出设置网络的提示框
				if (e.getErrorCode().equals(HiveViewErrorCode.E0000605)) {
					
					handler.sendEmptyMessage(GET_NET_LOCK_FAIL_TIMEOUT);
				} else
				// 解析JSON出错
				if (e.getErrorCode().equals(HiveViewErrorCode.E0000598)) {
					handler.sendEmptyMessage(GET_NET_LOCK_FAIL_JSONERROR);
				}*/
				
				//统一设置网络提示
				handler.sendEmptyMessage(GET_NET_LOCK_FAIL_TIMEOUT);
			}
		});
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_NET_LOCK_SUCCESS:
				isShowLockNetDialog();
				break;
			case GET_NET_LOCK_FAIL_TIMEOUT:
				Log.i("LockNet",
						"GET_SUO_FAIL_TO_NEXT..................PER_FIVE_OF_MINUNTES:"
								+ ONE_OF_MINUNTES);
				/*handler.sendEmptyMessageDelayed(GET_NET_LOCK_FAIL_RELOAD, ONE_OF_MINUNTES);	
				Intent mIntent = new Intent(LockNetService.this,NetExceptionActivity.class);
				mIntent.addCategory(Intent.CATEGORY_DEFAULT);
				mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
				startActivity(mIntent);		*/
				break;
			case GET_NET_LOCK_FAIL_JSONERROR:	
				//暂时没用到。time out统一处理异常
				break;
			case GET_NET_LOCK_FAIL_STOP_SELF:
				LockNetService.this.stopSelf();
				break;
			case GET_NET_LOCK_FAIL_RELOAD:
				getLockNet();
				break;
			default:
				break;
			}
		};
	};

	protected void isShowLockNetDialog() {
		if (null == mWm) {
			mWm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
			mParams = new WindowManager.LayoutParams();
			mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
			mParams.alpha = 0.9f;
			mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
			mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
			lockNetDialogView = new LockNetDialogView(this, null);
			mWm.addView(lockNetDialogView, mParams);
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}


}
