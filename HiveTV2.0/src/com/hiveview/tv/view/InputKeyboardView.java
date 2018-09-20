package com.hiveview.tv.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.hiveview.tv.R;

public class InputKeyboardView extends FrameLayout {
	private View vContainer;
	private Context mContext;
	private Button btnBackDelete;
	private Button btnByName;
	private Button btnByHuman;

	// 键盘第一行
	private Button btnQ;
	private Button btnW;
	private Button btnE;
	private Button btnR;
	private Button btnT;
	private Button btnY;
	private Button btnU;
	private Button btnI;
	private Button btnO;
	private Button btn1;
	private Button btn2;
	private Button btn3;

	// 键盘第二行
	private Button btnA;
	private Button btnS;
	private Button btnD;
	private Button btnF;
	private Button btnG;
	private Button btnH;
	private Button btnJ;
	private Button btnK;
	private Button btnL;
	private Button btn4;
	private Button btn5;
	private Button btn6;

	// 键盘第三行
	private Button btnZ;
	private Button btnX;
	private Button btnC;
	private Button btnV;
	private Button btnB;
	private Button btnN;
	private Button btnM;
	private Button btnP;
	private Button btn0;
	private Button btn7;
	private Button btn8;
	private Button btn9;

	private EditText etKeyWord;
	private ITextChangeListener textChangeListener;
	private IKeyboardButtonClickListener btnFunctionClickListener;
	private boolean isLongClick =  false;//回删长按事件删除线程的标志位
	public InputKeyboardView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public InputKeyboardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public InputKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vContainer = inflater.inflate(R.layout.input_keyboard_layout, null);
		btnBackDelete = (Button) vContainer.findViewById(R.id.btn_back_delete);
		btnByName = (Button) vContainer.findViewById(R.id.btn_search_by_name);
		btnByHuman = (Button) vContainer.findViewById(R.id.btn_search_by_human);

		btnQ = (Button) vContainer.findViewById(R.id.btn_q);
		btnW = (Button) vContainer.findViewById(R.id.btn_w);
		btnE = (Button) vContainer.findViewById(R.id.btn_e);
		btnR = (Button) vContainer.findViewById(R.id.btn_r);
		btnT = (Button) vContainer.findViewById(R.id.btn_t);
		btnY = (Button) vContainer.findViewById(R.id.btn_y);
		btnU = (Button) vContainer.findViewById(R.id.btn_u);
		btnI = (Button) vContainer.findViewById(R.id.btn_i);
		btnO = (Button) vContainer.findViewById(R.id.btn_o);
		btn1 = (Button) vContainer.findViewById(R.id.btn_1);
		btn2 = (Button) vContainer.findViewById(R.id.btn_2);
		btn3 = (Button) vContainer.findViewById(R.id.btn_3);

		btnA = (Button) vContainer.findViewById(R.id.btn_a);
		btnS = (Button) vContainer.findViewById(R.id.btn_s);
		btnD = (Button) vContainer.findViewById(R.id.btn_d);
		btnF = (Button) vContainer.findViewById(R.id.btn_f);
		btnG = (Button) vContainer.findViewById(R.id.btn_g);
		btnH = (Button) vContainer.findViewById(R.id.btn_h);
		btnJ = (Button) vContainer.findViewById(R.id.btn_j);
		btnK = (Button) vContainer.findViewById(R.id.btn_k);
		btnL = (Button) vContainer.findViewById(R.id.btn_l);
		btn4 = (Button) vContainer.findViewById(R.id.btn_4);
		btn5 = (Button) vContainer.findViewById(R.id.btn_5);
		btn6 = (Button) vContainer.findViewById(R.id.btn_6);

		btnZ = (Button) vContainer.findViewById(R.id.btn_z);
		btnX = (Button) vContainer.findViewById(R.id.btn_x);
		btnC = (Button) vContainer.findViewById(R.id.btn_c);
		btnV = (Button) vContainer.findViewById(R.id.btn_v);
		btnB = (Button) vContainer.findViewById(R.id.btn_b);
		btnN = (Button) vContainer.findViewById(R.id.btn_n);
		btnM = (Button) vContainer.findViewById(R.id.btn_m);
		btnP = (Button) vContainer.findViewById(R.id.btn_p);
		btn0 = (Button) vContainer.findViewById(R.id.btn_0);
		btn7 = (Button) vContainer.findViewById(R.id.btn_7);
		btn8 = (Button) vContainer.findViewById(R.id.btn_8);
		btn9 = (Button) vContainer.findViewById(R.id.btn_9);

		btnQ.setOnClickListener(charButtonClickListener);
		btnW.setOnClickListener(charButtonClickListener);
		btnE.setOnClickListener(charButtonClickListener);
		btnR.setOnClickListener(charButtonClickListener);
		btnT.setOnClickListener(charButtonClickListener);
		btnY.setOnClickListener(charButtonClickListener);
		btnU.setOnClickListener(charButtonClickListener);
		btnI.setOnClickListener(charButtonClickListener);
		btnO.setOnClickListener(charButtonClickListener);
		btn1.setOnClickListener(charButtonClickListener);
		btn2.setOnClickListener(charButtonClickListener);
		btn3.setOnClickListener(charButtonClickListener);

		btnA.setOnClickListener(charButtonClickListener);
		btnS.setOnClickListener(charButtonClickListener);
		btnD.setOnClickListener(charButtonClickListener);
		btnF.setOnClickListener(charButtonClickListener);
		btnG.setOnClickListener(charButtonClickListener);
		btnH.setOnClickListener(charButtonClickListener);
		btnJ.setOnClickListener(charButtonClickListener);
		btnK.setOnClickListener(charButtonClickListener);
		btnL.setOnClickListener(charButtonClickListener);
		btn4.setOnClickListener(charButtonClickListener);
		btn5.setOnClickListener(charButtonClickListener);
		btn6.setOnClickListener(charButtonClickListener);

		btnZ.setOnClickListener(charButtonClickListener);
		btnX.setOnClickListener(charButtonClickListener);
		btnC.setOnClickListener(charButtonClickListener);
		btnV.setOnClickListener(charButtonClickListener);
		btnB.setOnClickListener(charButtonClickListener);
		btnN.setOnClickListener(charButtonClickListener);
		btnM.setOnClickListener(charButtonClickListener);
		btnP.setOnClickListener(charButtonClickListener);
		btn0.setOnClickListener(charButtonClickListener);
		btn7.setOnClickListener(charButtonClickListener);
		btn8.setOnClickListener(charButtonClickListener);
		btn9.setOnClickListener(charButtonClickListener);

		btnByName.setOnClickListener(functionButtonClickListener);
		btnBackDelete.setOnClickListener(functionButtonClickListener);
		btnByHuman.setOnClickListener(functionButtonClickListener);
		
		btnByName.setOnLongClickListener(functionButtonLongClickListener);
		btnBackDelete.setOnLongClickListener(functionButtonLongClickListener);
		btnByHuman.setOnLongClickListener(functionButtonLongClickListener);

		btnQ.setNextFocusLeftId(R.id.btn_3);
		btn3.setNextFocusRightId(R.id.btn_q);
		btnA.setNextFocusLeftId(R.id.btn_6);
		btn6.setNextFocusRightId(R.id.btn_a);
		btnZ.setNextFocusLeftId(R.id.btn_9);
		btn9.setNextFocusRightId(R.id.btn_z);
		
		btnByName.setNextFocusLeftId(R.id.btn_back_delete);
		btnBackDelete.setNextFocusRightId(R.id.btn_search_by_name);

		addView(vContainer);
	}
	private Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==1){
				if (null != textChangeListener && null != etKeyWord) {
					String text = etKeyWord.getText().toString();
					if (text.length() > 0) {
						text = text.substring(0, text.length() - 1);
						etKeyWord.setText(text);
					}else{
						textChangeListener.onTextChanged("");//长按事件时当关键字长度为0时也终止线程
						isLongClick=false;
					}
				}
			}
			
			if(msg.what==-1){//长按事件结束后，获取当前的关键字并进行查询
				if (null != textChangeListener && null != etKeyWord) {
					String text = etKeyWord.getText().toString();
					textChangeListener.onTextChanged(text);
				}
			}
		}
	};
	public EditText getEtKeyWord() {
		return etKeyWord;
	}

	/**
	 * 显示用户输入的字符的文本框，这个是必须设置的
	 * 
	 * @param etKeyWord
	 */
	public void setEtKeyWord(EditText etKeyWord) {
		this.etKeyWord = etKeyWord;
		this.etKeyWord.setInputType(InputType.TYPE_NULL);
	}

	public void setTextChangeListener(ITextChangeListener changeListener) {
		this.textChangeListener = changeListener;
	}

	public void setFunctionButtonClickListener(IKeyboardButtonClickListener buttonClickListener) {
		this.btnFunctionClickListener = buttonClickListener;
	}

	/**
	 * 回删，按片名搜索，按人物搜索的按钮单击事件监听
	 */
	private String TAG = "InputKeyboradView";
	OnClickListener functionButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.d(TAG,"================"+System.currentTimeMillis());
			if (v == btnBackDelete) {
				if (null != textChangeListener && null != etKeyWord) {
					String text = etKeyWord.getText().toString();
					if (text.length() > 0) {
						text = text.substring(0, text.length() - 1);
						etKeyWord.setText(text);

						if (null != textChangeListener) {
							textChangeListener.onTextChanged(text);
						}

						if (null != btnFunctionClickListener) {
							btnFunctionClickListener.onBackDeleteChar();
						}
					}
				}
			} else if (v == btnByHuman) {
				if (null != btnFunctionClickListener) {
					btnFunctionClickListener.onSearchByHumanName();
				}
			} else if (v == btnByName) {
				if (null != btnFunctionClickListener) {
					btnFunctionClickListener.onSearchByFilmName();
				}
			}
		}
	};
	
	/**
	 * button的长摁事件监听
	 */
	OnLongClickListener functionButtonLongClickListener = new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			Log.d(TAG,"==========onLongClick======"+System.currentTimeMillis());
			if (v == btnBackDelete) {
				if(null != getEtKeyWord()){
					isLongClick = true;
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							while(isLongClick){
								handler.sendEmptyMessage(1);//开启一个线程循环，每隔0.1毫秒搜索关键字长度减1
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									Log.e("InputKeyboardView", "long click sleep thread error");
									e.printStackTrace();
								}
							}
						}
					}).start();
				}
			} else if (v == btnByHuman) {
				
			} else if (v == btnByName) {
				
			}
			return true;
		}
	};

	/**
	 * 字符按钮单击事件
	 */
	OnClickListener charButtonClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Button btnText = (Button) v;
			if (null != etKeyWord) {
				etKeyWord.setText(etKeyWord.getText() + btnText.getText().toString());
			}

			if (null != textChangeListener) {
				textChangeListener.onTextChanged(etKeyWord.getText().toString());
			}

		}

	};

	/**
	 * 当用户的输入文本发生变化时调用
	 * 
	 * @author chenlixiao
	 * 
	 */
	public interface ITextChangeListener {
		public void onTextChanged(String finalText);
	}

	/**
	 * 当用户单击回删，按片名搜索，按人物搜索时回调
	 * 
	 * @author chenlixiao
	 * 
	 */
	public interface IKeyboardButtonClickListener {
		/**
		 * 按片名搜索按钮单击发生时回调
		 */
		public void onSearchByFilmName();

		/**
		 * 按人物搜索按钮单击发生时回调
		 */
		public void onSearchByHumanName();

		/**
		 * 回删按钮单击发生时回调
		 */
		public void onBackDeleteChar();
	}

}
