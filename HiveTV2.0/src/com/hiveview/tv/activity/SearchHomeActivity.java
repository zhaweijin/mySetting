package com.hiveview.tv.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.hiveview.tv.R;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.TotalListSearchEntity;
import com.hiveview.tv.service.entity.WordsEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.view.InputKeyboardView;
import com.hiveview.tv.view.InputKeyboardView.IKeyboardButtonClickListener;
import com.hiveview.tv.view.InputKeyboardView.ITextChangeListener;
import com.hiveview.tv.view.ThinkWordsView;
import com.hiveview.tv.view.television.MarqueeText;

/**
 * @ClassName: SearchHomeActivity
 * @Description: 
 * @author: zhangpengzhan
 * @date 2014年6月20日 下午3:36:23
 * 
 */
public class SearchHomeActivity extends BaseActivity {

	
	//提示信息的view
	private View rlGuide;
	//联想词的view
	private ThinkWordsView thinkView;
	//消息id
	private final int REQUEST_THINK_WORDS_SUCCESS = 20;
	private final int REQUEST_THINK_WORDS_FAIL = -20;
	private final int REQUEST_FILM_FAIL = -21;
	private final int REQUEST_HUMAN_FAIL = -22;
	//输入框
	private EditText etWords = null;
	//没有数据的文字
	private TextView tvNoData = null;
	//键盘的监听
	private InputKeyboardView keyboardView;
	//返回的联想词
	private List<WordsEntity> wordList = null;
	//获取网络访问的方法
	private HiveTVService requestService;

	/**
	 * 用户单击联想词标签监听，跳转到视频（人物或者电影）列表
	 */
	OnClickListener wordClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//如果搜索文字不为空就打开搜索列表
			Intent intent = new Intent(SearchHomeActivity.this, SearchPageActivtiy.class);
			MarqueeText tv = (MarqueeText) v;
			if (null != tv.getText().toString()) {//防止空指针的判断
				intent.putExtra("keyword", tv.getText().toString());
				intent.putExtra("cast_id", 0);
				intent.putExtra("select_mode", 0);// 按片名搜索传0
				startActivity(intent);
			}
		}
	};

	/**
	 * 搜索的关键字的监听
	 */
	ITextChangeListener keyboardTextChangeListener = new ITextChangeListener() {

		@Override
		public void onTextChanged(String finalText) {
			//如果没有搜索文字就显示没有数据的提示信息
			if (finalText.length() == 0||finalText.equals("")) {// 如果关键字为空的情况下显示提示信息
				handler.sendEmptyMessage(REQUEST_HUMAN_FAIL);
				return;
			}
			//获取联想词
			requestThinkWords(finalText);
		}
	};

	/**
	 * 这个没有使用
	 * 实现键盘上回删，按片名搜索，按人物搜索按钮点击事件的回调方法
	 */
	IKeyboardButtonClickListener keyboardButtonClickListener = new IKeyboardButtonClickListener() {

		@Override
		public void onSearchByHumanName() {
			// requestByHumanName();
			alert("暂时没有开通！！！");
		}

		@Override
		public void onSearchByFilmName() {
			// 通过电影名字搜索
			requestByFilmName();
		}

		@Override
		public void onBackDeleteChar() {

		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.search_layout);
		init();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		//重用搜索页面、再次进来时清空上次的数据
		wordList=null;
		etWords.setText("");
		keyboardView.setEtKeyWord(etWords);
		thinkView.setVisibility(View.INVISIBLE);
		rlGuide.setVisibility(View.VISIBLE);
		keyboardView.requestFocus();
	}
	/**
	 * 初始化相关控件
	 * 
	 * @Title: SearchHomeActivity @
	 * @Description:
	 */
	private void init() {
		requestService = new HiveTVService();
		rlGuide = findViewById(R.id.rl_search_guide);
		thinkView = (ThinkWordsView) findViewById(R.id.tv_think_view);
		etWords = (EditText) findViewById(R.id.et_input_words);
		tvNoData = (TextView) findViewById(R.id.tv_no_data);
		keyboardView = (InputKeyboardView) findViewById(R.id.search_input_keyboard_view);
		keyboardView.setEtKeyWord(etWords);
		keyboardView.setTextChangeListener(keyboardTextChangeListener);
		keyboardView.setFunctionButtonClickListener(keyboardButtonClickListener);

	}

	@Override
	protected void processData(int msgWhat) {
		switch (msgWhat) {
		case REQUEST_THINK_WORDS_SUCCESS:// 显示搜索到的关键词
				thinkView.setDataSource(wordList, wordClickListener);
				rlGuide.setVisibility(View.INVISIBLE);
				thinkView.setVisibility(View.VISIBLE);
				tvNoData.setVisibility(View.INVISIBLE);
			break;

		case REQUEST_HUMAN_FAIL:// 按人物名称搜索，没有用户输入的关键词关联的人物结果，控制UI显示无相关结果
			tvNoData.setVisibility(View.INVISIBLE);
			thinkView.setVisibility(View.INVISIBLE);
			rlGuide.setVisibility(View.VISIBLE);
			break;
		case REQUEST_FILM_FAIL:// 按片名称搜索，没有用户输入的关键词关联的人物结果，控制UI显示无相关结果
			tvNoData.setVisibility(View.VISIBLE);
			thinkView.setVisibility(View.INVISIBLE);
			rlGuide.setVisibility(View.INVISIBLE);
			break;
		case REQUEST_THINK_WORDS_FAIL://获取数据失败的消息
			tvNoData.setVisibility(View.VISIBLE);
			thinkView.setVisibility(View.INVISIBLE);
			rlGuide.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}

	/**
	 * 根据用户输入的关键词去搜索电影
	 * 联想词
	 * @param finalText
	 */
	private void requestThinkWords(final String finalText) {
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				wordList = requestService.getHotWordEntities(SearchHomeActivity.this, finalText);
				//添加获取数据异常的判断
				if (null != wordList && wordList.size() != 0) {//如果获取的数据不为空就发送成功的消息
					handler.sendEmptyMessage(REQUEST_THINK_WORDS_SUCCESS);
				} else {//如果没有数据就发送失败的消息，显示失败的ui布局
					handler.sendEmptyMessage(REQUEST_THINK_WORDS_FAIL);
				}
			}

			@Override
			public void processServiceException(ServiceException e) {
				e.printStackTrace();
				//发送失败的消息
				handler.sendEmptyMessage(REQUEST_THINK_WORDS_FAIL);
				showErrorDialog(e.getErrorCode(), false);
			}
		});
	}

	/**
	 * 0 按片名搜索，如果有结果直接跳转到列表页
	 */
	private void requestByFilmName() {
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				ArrayList<TotalListSearchEntity> list = requestService.getSearchList(etWords.getText().toString(), 0, 0, 0, 1, 10);
				if (null == list) {
					handler.sendEmptyMessage(REQUEST_FILM_FAIL);
					return;
				}
				if (list.get(0).getFilms().size() <= 0) {// 没有返回信息，发出获取失败的消息
					handler.sendEmptyMessage(REQUEST_FILM_FAIL);
				} else {// 跳转到按片名搜索的列表页
					Intent intent = new Intent(SearchHomeActivity.this, SearchPageActivtiy.class);
					intent.putExtra("keyword", etWords.getText().toString());
					intent.putExtra("cast_id", 0);
					intent.putExtra("select_mode", 0);// 按片名搜索传0
					startActivity(intent);
				}
			}

			@Override
			public void processServiceException(ServiceException e) {
				e.printStackTrace();
				//断网出现dialog
				showErrorDialog(e.getErrorCode(), false);
				handler.sendEmptyMessage(REQUEST_THINK_WORDS_FAIL);
			}
		});
	}

}
