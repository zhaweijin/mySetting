package com.hiveview.cloudtv.settings.widget;



import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.hiveview.cloudtv.settings.R;

public class SubmitDialog extends Dialog
{
    private LayoutInflater mFactory = null;
    private View mView = null;
    private TextView mTypeView = null;
    private EditText mContentView = null;
    private Context mContext;
 //   private int mType = 0;  //1为密码
    private String mContent = "";
    private int mWidth = 1000;
    private int mHeight = 400;
    private OnDismiss mListener;
    private String num;
    private Handler mHandler;
    private onResolution resolutionListener; 
   // private boolean isSetResolution = false ;
    /*public boolean isSetResolution() {
		return isSetResolution;
	}

	public void setSetResolution(boolean isSetResolution) {
		this.isSetResolution = isSetResolution;
	}*/

	public onResolution getResolutionListener() {
		return resolutionListener;
	}

	public void setResolutionListener(onResolution resolutionListener) {
		this.resolutionListener = resolutionListener;
	}

	public interface OnDismiss {
        void setOnDismiss(String value);
   }
    public interface onResolution{
    	void setResolution();
    }
    public SubmitDialog(Context context, OnDismiss onDismiss ,String num) {
    	super(context, R.style.MyDialog);
        this.mContext = context;
        mListener = onDismiss;  
        this.num=num;
        initUI();
    }

    private void initUI()  
    {
    	mFactory = LayoutInflater.from(mContext);
    	mView = mFactory.inflate(R.layout.submitdialog, null);

    	mTypeView = (TextView)mView.findViewById(R.id.type_txtv);
    	mContentView = (EditText)mView.findViewById(R.id.value_edtv);
    	String type =mContext.getResources().getString(R.string.surenum)+num;
        mTypeView.setText(type);
     //   String text ="11";
     //   mContentView.setText("11");
        mContentView.requestFocus();
		mContentView.setSelection(0);

		
		mContentView.addTextChangedListener(mWatcher);
		
		mContentView.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE
						|| actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
//					getFragmentManager().popBackStack();
					try {
						Log.e("cfc", "getEditText()="+getEditText());
						InputMethodManager mng = (InputMethodManager) mContext
								.getSystemService(Activity.INPUT_METHOD_SERVICE);
						mng.hideSoftInputFromWindow(v.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
						// Log.e("Keybroad", "Hide key broad...");
						
						dismiss();
						mListener.setOnDismiss(getEditText());

					} catch (Exception e) {
						Log.e("Keybroad", "Hide key broad error", e);
					}
				}
				return false;
			}

		});
		mContentView.requestFocus();
    	this.setContentView(mView);
    }
    
    private TextWatcher mWatcher = new TextWatcher()
    {
		public void onTextChanged(CharSequence sequence, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		public void beforeTextChanged(CharSequence sequence, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			Log.e("cfc", "sequence="+sequence);
			Log.e("cfc", "start="+start);
			Log.e("cfc", "count="+count);
			Log.e("cfc", "after="+after);
		}
		
		public void afterTextChanged(Editable editable) {
			// TODO Auto-generated method stub
			try {
				String tempString = editable.toString();
				Log.e("cfc", "tempString="+tempString);
			
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	};
    
    public void show() {
    	LayoutParams params = this.getWindow().getAttributes();
		params.width = mWidth;
		params.height = mHeight;
		this.getWindow().setAttributes(params);
		super.show();
    }
	
	public String getEditText()
	{
		String result = null;
		if(mContentView != null)
		{
			result = mContentView.getText().toString();
		}
		return result;
	}
	
	public EditText getEditView()
	{
		return mContentView;
	}
	
	@Override
	public void dismiss()
	{
		/*if(!isSetResolution){
			Log.d("SubmitDialog","submit faild");
			resolutionListener.setResolution();
		}else{
			Log.d("SubmitDialog","submit success");
		}*/
		super.dismiss();
	}
	
	
  
   
   public void setMaxInputLength(InputFilter[] inputFilter){
	   mContentView.setFilters(inputFilter);
   }
   
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			resolutionListener.setResolution();
		}
		return super.onKeyDown(keyCode, event);
	}
}

