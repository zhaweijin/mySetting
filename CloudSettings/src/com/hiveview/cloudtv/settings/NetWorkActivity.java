package com.hiveview.cloudtv.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hiveview.cloudtv.settings.network.NetWorkCheck;
import com.hiveview.cloudtv.settings.util.ResultCallback;
import com.hiveview.cloudtv.settings.widget.LauncherFocusViewH;

 

public class NetWorkActivity extends Activity {
	private final static String TAG = "NetWorkDetectool";
	private NetWorkCheck mNetWrok;
	private TextView textView_check1,textView_check2,textView_check3,textView_check4,
		textView_check5;
	private TextView state1,state2,state3,state4,state5;
	private TextView title1,title2,title3,title4,title5;
	private ProgressBar probar;
	private ProgressBar progressBar;
	private Button buttonCheckOK;
	private int errorflag ,index = 0;
	private Context mContext;
	private int errorcnt = 0;
	private LauncherFocusViewH focusView = null;
	private boolean isRunning=true;
	private String errorTipsTitle="";
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int type = msg.what;
			int status = msg.arg1;
			if (index>=6){
				index=0;
				errorTipsTitle="";
			}
			progressBar.setProgress(index);
			switch(type) {
			case 1:
				setTextViewChecking(textView_check1);
				index++;
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//1.检查网络硬件连接状态
						int nRet = mNetWrok.netConnectCheck();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						sendMessageView(2,nRet);
					}
				}).start();
				break;
			case 2:
				setTextViewChecking(textView_check2);
				if(status == 0) {
					setTextViewNormal(textView_check1,state1,title1);
				} else {
					setTextViewAbnormal(textView_check1);
					if(status == NetWorkCheck.NETWORKCNT_CHECK_ETHNOLINK_ERR) {
						errorTipsTitle=errorTipsTitle+getResources().getString(R.string.nethardware)+",";
						showDialog(mContext.getResources().getString(R.string.netexception1)+getErrorMsg(), null);
						close.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								initTextViewCheck();
								dialog.dismiss();
								progressBar.setVisibility(View.GONE);
								buttonCheckOK.setBackgroundResource(R.drawable.testnetbutton_focus);
							}
						});
						return ;
					}
					
					if(status == NetWorkCheck.NETWORKCNT_CHECK_ETH_DISABLE) {
						errorcnt++;
						errorTipsTitle=errorTipsTitle+getResources().getString(R.string.nethardware)+",";
						errorflag = errorflag|NetWorkCheck.NETWORKCNT_CHECK_ETH_DISABLE;
					}
				}
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 1.检查网络硬件连接状态

						int nRet = mNetWrok.netWorkConfigureCheck();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						sendMessageView(3, nRet);

					}
				}).start();
				break;
			case 3:
				setTextViewChecking(textView_check3);
				if(status == 0) {
					setTextViewNormal(textView_check2,state2,title2);
				} else {
					Log.e(TAG,"NetWorkDetectool----status = "+status);
					errorflag = errorflag|status;
					errorcnt++;
					errorTipsTitle=errorTipsTitle+getResources().getString(R.string.netconnect)+",";
					setTextViewAbnormal(textView_check2);
				}
				Log.v(TAG,"mNetWrok.dhcpMode==="+mNetWrok.dhcpMode);
				if(mNetWrok.dhcpMode)  
					sendMessageView(4, 0);
				else
					sendMessageView(4, 1);
				break;
			case 4:
				setTextViewChecking(textView_check4);
				if(status == 0) {
					setTextViewNormal(textView_check3,state3,title3);
				} else {
					setTextViewAbnormal(textView_check3);
					errorcnt++;
					errorTipsTitle=errorTipsTitle+getResources().getString(R.string.networktip3)+",";
					errorflag = errorflag|NetWorkCheck.NETWORKDHCP_CHECK_MANUAL;
				}
				
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 1.检查网络硬件连接状态

						int nRet = mNetWrok.netWorkDNSCheck();
						Log.v(TAG,">>>nRet=="+nRet);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						sendMessageView(5, nRet);

					}
				}).start();
				
				break;
			case 5:
				setTextViewChecking(textView_check5);
				if(status == 0) {
					setTextViewNormal(textView_check4,state4,title4);
				} else {
					errorflag = errorflag|status;
					errorcnt++;
					errorTipsTitle=errorTipsTitle+getResources().getString(R.string.dnstip)+",";
					setTextViewAbnormal(textView_check4);
				}
				
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 1.检查网络硬件连接状态

						int nRet = mNetWrok.netWorkHostsCheck();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						sendMessageView(6, nRet);

					}
				}).start();
				break;
			case 6:
				if(status == 0) {
					setTextViewNormal(textView_check5,state5,title5);
				} else {
					setTextViewAbnormal(textView_check5);
					errorflag = errorflag|status;
					errorTipsTitle=errorTipsTitle+getResources().getString(R.string.host)+",";
					errorcnt++;
				}
				Log.e(TAG,"errorflag = " + errorflag);
				if(errorflag == 0) {
                    showDialog(mContext.getResources().getString(R.string.netnormaltip),null);
					tv_msg.setText(mContext.getResources().getString(R.string.domynetwork));
					wait_msg.setVisibility(View.VISIBLE);
					close.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
						/*	focusView.setVisibility(View.VISIBLE);
							focusView.moveTo(v);*/
						}
					});
					close.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							initTextViewCheck();
							dialog.dismiss();
							progressBar.setVisibility(View.GONE);
							buttonCheckOK.setBackgroundResource(R.drawable.testnetbutton_focus);
						}
					});
				} else {
					final ResultCallback EthernetStatusCallback = new ResultCallback() {

						@Override
						public boolean callback(Context contxt, int data) {
							// TODO Auto-generated method stub
							Log.e(TAG, "----------callback");
							int nRet = mNetWrok.netWorkRepair(contxt, data);

							Log.v(TAG, "nRet.....="+nRet);
							if(nRet == mNetWrok.NETWORK_REPAIR_ETH_DISABLE ||
									nRet == mNetWrok.NETWORK_REPAIR_ETH_MANUL_DHCP) {
								probar.setVisibility(ProgressBar.GONE);
								showDialog(mContext.getResources().getString(R.string.unrepair),null);
								close.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										initTextViewCheck();
										dialog.dismiss();
										progressBar.setVisibility(View.GONE);
										buttonCheckOK.setBackgroundResource(R.drawable.testnetbutton_focus);
									}
								});
								return false;
							} else if(nRet == mNetWrok.NETWORK_REPAIR_ETH_HOSTS) {
								probar.setVisibility(ProgressBar.GONE);
								showDialog(mContext.getResources().getString(R.string.repairfailure),null);
								close.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										initTextViewCheck();
										dialog.dismiss();
										progressBar.setVisibility(View.GONE);
										buttonCheckOK.setBackgroundResource(R.drawable.testnetbutton_focus);
									}
								});
								return false;
							} else if(nRet == mNetWrok.NETWORK_REPAIR_ETH_AUTO_DHCP) {
								probar.setVisibility(ProgressBar.GONE);
								showDialog(mContext.getResources().getString(R.string.repairfailure2),null);
								close.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										initTextViewCheck();
										dialog.dismiss();
										progressBar.setVisibility(View.GONE);
										buttonCheckOK.setBackgroundResource(R.drawable.testnetbutton_focus);
									}
								});
								return false;
							} else if(nRet == mNetWrok.NETWORK_REPAIR_ETH_DNS) {
								probar.setVisibility(ProgressBar.GONE);
								showDialog(mContext.getResources().getString(R.string.repairfailure2),null);
								close.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										initTextViewCheck();
										dialog.dismiss();
										progressBar.setVisibility(View.GONE);
										buttonCheckOK.setBackgroundResource(R.drawable.testnetbutton_focus);
									}
								});
								return false;
							}

							errorflag = 0;
							errorcnt = 0;
							//1.检查网络硬件连接状态
							Message msg = Message.obtain();
							msg.what = 1;
							msg.arg1=0;
							handler.sendMessageDelayed(msg, 3000);
							return true;
						}

					};
					showDialog(mContext.getResources().getString(R.string.netexception1)+getErrorMsg()+errorcnt+ mContext.getResources().getString(R.string.netexception2),EthernetStatusCallback);
					close_btn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							initTextViewCheck();
							dialog.dismiss();
							buttonCheckOK.setBackgroundResource(R.drawable.testnetbutton_focus);
						}
					});
					adjust.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							probar.setVisibility(ProgressBar.VISIBLE);
							EthernetStatusCallback.callback(mContext, errorflag);
						}
					});
				}
				break;
			}
		};
	};
	
	
	private String getErrorMsg(){
		if(errorTipsTitle.equals("")){
			return "";
		}
		return "( " + errorTipsTitle.substring(0, errorTipsTitle.length()-1) + " )";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network);
		mContext = this;
		mNetWrok = new NetWorkCheck(this);
		

	//	focusView = (LauncherFocusViewH) findViewById(R.id.network_focus_view);
		buttonCheckOK = (Button)findViewById(R.id.buttonOK);
		progressBar = (ProgressBar)findViewById(R.id.ProgressBar01);
		progressBar.setVisibility(View.GONE);
//		View view = LayoutInflater.from(this).inflate(R.layout.network,null);
//		focusView.initFocusView(view, false, 0);
		buttonCheckOK.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				//focusView.initFocusView(v, false, 0);
			}
		});
		buttonCheckOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buttonCheckOK.setClickable(false);
				initTextViewCheck();
				//1.检查网络硬件连接状态
				sendMessageView(1,0);
				

				buttonCheckOK.setVisibility(View.GONE);
				//focusView.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);

			}
		});

		title1 = (TextView)findViewById(R.id.textView2);
		title2 = (TextView)findViewById(R.id.textView5);
		title3 = (TextView)findViewById(R.id.textView8);
		title4 = (TextView)findViewById(R.id.textView11);
		title5 = (TextView)findViewById(R.id.textView14);

		textView_check1 = (TextView)findViewById(R.id.textView4);//网络硬件配置
		textView_check2 = (TextView)findViewById(R.id.textView7);//网络连接配置
		textView_check3 = (TextView)findViewById(R.id.textView10);//DHCP服务
		textView_check4 = (TextView)findViewById(R.id.textView13);//DNS服务
		textView_check5 = (TextView)findViewById(R.id.textView16);//HOSTS服务

		state1 = (TextView)findViewById(R.id.textView3);
		state2 = (TextView)findViewById(R.id.textView6);
		state3 = (TextView)findViewById(R.id.textView9);
		state4 = (TextView)findViewById(R.id.textView12);
		state5 = (TextView)findViewById(R.id.textView15);

		probar = (ProgressBar)findViewById(R.id.progressBarLarge);
		probar.setVisibility(ProgressBar.GONE);
	}

	private void sendMessageView(int process, int status) {
		Message msg = Message.obtain();
		msg.what = process;
		msg.arg1=status;
		handler.sendMessage(msg);
	}

	void initTextViewCheck() {
		errorflag = 0;
		errorcnt = 0;
		Log.e(TAG, "----------initTextViewCheck");
		
		errorTipsTitle="";
		index=0;
		progressBar.setProgress(0);
		
		buttonCheckOK.setClickable(true);
		buttonCheckOK.setVisibility(View.VISIBLE);
		buttonCheckOK.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
			/*	focusView.setVisibility(View.VISIBLE);
				focusView.moveTo(v);*/
			}
		});
		progressBar.setVisibility(View.GONE);
		probar.setVisibility(ProgressBar.GONE);
		textView_check1.setTextColor(Color.BLACK);
		textView_check2.setTextColor(Color.BLACK);
		textView_check3.setTextColor(Color.BLACK);
		textView_check4.setTextColor(Color.BLACK);
		textView_check5.setTextColor(Color.BLACK);

		if (buttonCheckOK.isClickable()){
			buttonCheckOK.setBackgroundResource(R.drawable.btn_select_file);
		}else {
			buttonCheckOK.setBackgroundResource(R.drawable.testnetbutton_focus);
		}
		
		textView_check1.setText(mContext.getResources().getString(R.string.untest));
		textView_check1.setTextColor(getResources().getColor(R.color.textcolor));
		setTextAlpha(textView_check1,state1,title1);
		textView_check2.setText(mContext.getResources().getString(R.string.untest));
		textView_check2.setTextColor(getResources().getColor(R.color.textcolor));
		setTextAlpha(textView_check2,state2,title2);
		textView_check3.setText(mContext.getResources().getString(R.string.untest));
		textView_check3.setTextColor(getResources().getColor(R.color.textcolor));
		setTextAlpha(textView_check3,state3,title3);
		textView_check4.setText(mContext.getResources().getString(R.string.untest));
		textView_check4.setTextColor(getResources().getColor(R.color.textcolor));
		setTextAlpha(textView_check4,state4,title4);
		textView_check5.setText(mContext.getResources().getString(R.string.untest));
		textView_check5.setTextColor(getResources().getColor(R.color.textcolor));
		setTextAlpha(textView_check5,state5,title5);
		
	}
	
	void setTextViewNormal(TextView text,TextView textView,TextView text2) {
		if(text != null) {
			index++;
			text.setTextColor(Color.GREEN);
			text.setAlpha(1);
			textView.setAlpha(1);
			text2.setAlpha(1);
			text.setText(mContext.getResources().getString(R.string.normal));

		}
	}
	
	void setTextViewChecking(TextView text) {

		if(text != null) {
			text.setTextColor(Color.GREEN);
			text.setText(mContext.getResources().getString(R.string.testing));
		}
	}
	
	void setTextViewAbnormal(TextView text) {
		if(text != null) {
			index++;
			text.setTextColor(Color.RED);
			text.setText(mContext.getResources().getString(R.string.exception));
		}
	}

	void setTextAlpha(TextView text,TextView textView,TextView text2){
		if(text != null) {
			text.setAlpha((float) 0.5);
			textView.setAlpha((float) 0.5);
			text2.setAlpha((float) 0.5);
		}
	}
	
//	void shwoDialog(String showtext, final ResultCallback callback) {
////		AlertDialog.Builder builder = new AlertDialog.Builder(NetWorkActivity.this);
//		AlertDialog.Builder builder = new AlertDialog.Builder(NetWorkActivity.this,R.style.selectorDialog);
//		builder.setTitle("大麦网络检测工具");
//		builder.setMessage(showtext);
//		if(callback != null) {
//			builder.setNegativeButton("修复", new DialogInterface.OnClickListener() {
//				@Override  public void onClick(DialogInterface dialog, int which) {
//					probar.setVisibility(ProgressBar.VISIBLE);
//					callback.callback(mContext, errorflag);
//				}
//			});
//		}
//		builder.setNeutralButton("关闭", new DialogInterface.OnClickListener() {
//			@Override  public void onClick(DialogInterface dialog, int which) {
//				initTextViewCheck();
//			}
//		});
//
//		AlertDialog dialog = builder.create();
//		dialog.show();
//	}

    private View sucess_view;
    private TextView wait_msg,tv_msg;
	private Button close,close_btn,adjust;
	private LinearLayout background;
	private AlertDialog dialog = null;

	void showDialog(String showtext,final ResultCallback callback){
        if (dialog != null) {
            dialog.dismiss();
        }
        
        if(probar!=null){
        	probar.setVisibility(View.GONE);
        }
        
        dialog = new AlertDialog.Builder(this, R.style.selectorDialog).create();
        sucess_view = View.inflate(this, R.layout.dialog_background, null);
		background = (LinearLayout)sucess_view.findViewById(R.id.background);
        wait_msg = (TextView) sucess_view.findViewById(R.id.wait_msg);//描述
        wait_msg.setVisibility(View.VISIBLE);
		wait_msg.setText(showtext);
        tv_msg = (TextView) sucess_view.findViewById(R.id.tv_msg);//标题
		close = (Button) sucess_view.findViewById(R.id.close);
		close_btn = (Button) sucess_view.findViewById(R.id.close_btn);
		adjust = (Button) sucess_view.findViewById(R.id.adjust);
		if(callback == null){
			background.setBackgroundResource(R.drawable.ok_dialog);
			close.setVisibility(View.VISIBLE);
			close_btn.setVisibility(View.GONE);
			adjust.setVisibility(View.GONE);
		}else {
			background.setBackgroundResource(R.drawable.error_dialog);
			close.setVisibility(View.GONE);
			close_btn.setVisibility(View.VISIBLE);
			adjust.setVisibility(View.VISIBLE);
		}
		if (isRunning){
			dialog.show();
			dialog.setCancelable(false);
			Window window = dialog.getWindow();
			window.setGravity(Gravity.CENTER_VERTICAL);
			window.setContentView(sucess_view);
			//获得window窗口的属性
			WindowManager.LayoutParams params = window.getAttributes();
			//设置窗口宽度为充满全屏
			params.width = WindowManager.LayoutParams.MATCH_PARENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
			params.height = WindowManager.LayoutParams.MATCH_PARENT;
			//设置窗口高度为包裹内容
			window.setAttributes(params);
		}
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG,"onPause");
		isRunning=false;
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG,"onStop");
		isRunning=false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isRunning=true;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		isRunning=true;
	}
}
