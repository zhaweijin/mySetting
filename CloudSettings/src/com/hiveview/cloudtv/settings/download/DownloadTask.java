package com.hiveview.cloudtv.settings.download;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

 

import com.hiveview.cloudtv.settings.R;

import android.content.Context;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

public class DownloadTask extends Task implements Runnable {
	private static final String TAG = "DownloadTask";

	public static final int PACKAGE_SIZE = 1024 * 32;
	public static final String TAG_RANGE = "RANGE";
	public static final long FILE_LENGTH_UNKNOWN = -1000;
	private String  testURL= "http://testspeed.pthv.gitv.tv/testspeed/testspeed50mbytes";

	private static final long CONN_MGR_TIMEOUT = 10000;
	private static final int CONN_TIMEOUT = 50000;
	private static final int SO_TIMEOUT = 50000;
	
	public static final int DOWNLOAD_TIME = 40;

	private long mDownloadSize;
	private DefaultHttpClient mHttpClient;
	private TaskListener mListener;
	RandomAccessFile mRandFile;
	private int downloadCount =0;
	private Context mContext;
	
	private boolean isGroupUser = false;
	
	
	private boolean isDownloadFinished = false;

	private static final String url = "http://data.itv.iqiyi.com/itv/m3u8NetTest";
	public final static int UPDATE_THRESHOLD = 300;
	
	private final String TEST_SPEED_API = "/testspeed/testspeed50mbytes";

	//优先原则：1. 集团内网的话，使用集团内网的下载地址
	//        2. 集团外网的话，优先使用沙发服务器地址。如果地址出现状况，就使用金山服务器
	//shafa guanjia
	private String shafaService  = "http://service.shafa.com/speedtest/file";
		
	//changkuan
	private String changchengService = "http://testspeed.pthv.gitv.tv/testspeed/testspeed50mbytes";
		
	//jinshan 55个
	private String comJinshan = "http://dl.ijinshan.com/safe/speedtest/";
	private String[] jinshan = {
				"5C5AD2AEF42CBD31473897373C67FF0B.dat",
				"70E65242778C3ED3B25F0B21ACAEE366.dat",
				"AC15164876C5960C4EC0021A980AB98A.dat",
				"37C5E4347CD6EF61CAB1E557691F5A6C.dat",
				"090B8DC62E38C75BB9B416303CBA1F3D.dat",
				"D3714A32F384C192FF4277E3FAE6239B.dat",
				"AE0D28B90E0F9C67591E1FF92230BC05.dat",
				"33A9CC7E9A6D50E857F71696843E9328.dat",
				"67A48269D105F726B05D32FB1C46938D.dat",
				"3DCBE25313E6BAFF74FEDB8C66310B0E.dat",
				"9841A730FE26DE02C3DC392E836DF157.dat",
				"2CE8599B2886EDFE42C7B9E447C6F572.dat",
				"DC1B5128B49F0D9391B78C4939A9999A.dat",
				"7C493094C39FD5C933CEF90DF45EBD7D.dat",
				"FD2FDB8AC46816AEB81D8CBE4A92924A.dat",
				"2C3DBF6C89B3C5297E8DAE4568199150.dat",
				"76235F686570C28BF49B1E8A83D91541.dat",
				"7E905504CFDB5F4C60CD7408EB66FDC9.dat",
				"AD5DC14E5E9E77D01E6BE7FB699387D9.dat",
				"C7CD33056951D671439E573B63F31061.dat",
				"FDD56520651AB24F15F3860870EEE058.dat",
				"EEBEA414E9E6DA511E7BDE2B5CD2231B.dat",
				"582AF370AE3CE3F67593D63986997949.dat",
				"061684B83877FCCD0DDA2E7F438021E2.dat",
				"CFAFA75C75BA343FC0B5440A60A68F4E.dat",
				"3092CC93693CF88696DBC66D14269D41.dat",
				"11D3FF8E37508AEB1663D12F39156237.dat",
				"C9760BE09B6D011F27B2D83B17C99FB6.dat",
				"00BCDF6C42AE276A395A6CF88667BCD3.dat",
				"4B91599A9CDFCDBA1FD5742EABA85B6A.dat",
				"DB44D5377D48B734C8EC5C3EBA71D69D.dat",
				"E79BC6F01BBB17E0E88BF68C7021DD01.dat",
				"E79BC6F01BBB17E0E88BF68C7021DD01.dat",
				"B0DD006CAC8E5AB6962023F79A6DC963.dat",
				"8C53CCD787C381CD535D8DCCA12584D8.dat",
				"6A74EA902860472EB9791808CF7FFDD5.dat",
				"4A09450E6196792E63E2955DE0DA1082.dat",
				"EA492D0FB5BAB092FA64181DB65ECA29.dat",
				"AE875553D8F20C4FDCDC5224294892CE.dat",
				"99A58B236AF61F4DE60E087EEED58EC3.dat",
				"0F8DEA78730B1A05A6605766922C5C6D.dat",
				"649D449C2A5BC92BF362C323BC1D5FF6.dat",
				"946F93ED7FE769C5162ECC70135443F6.dat",
				"17F35B3D2E747ECB6B848FB5CAED0B91.dat",
				"1B8EF01BB04D5D99FE3851700B494B1F.dat",
				"419D565D30D7C7732628E70124C4DCDD.dat",
				"650E4E5A5BC95DE9F719E8EDAD10A37C.dat",
				"D267C2442C603A749C0D60F3EBDDE014.dat",
				"831FCF04B74A6626BAC17EF31C7F147D.dat",
				"246D55293B0EE35E3DFBE4387777459D.dat",
				"4150BD8E9775614BE3FF708E81B38345.dat",
				"DA080AD2A64926856B653A3714FED287.dat",
				"2D150D3EE202DE46AB1CA850D306F22D.dat",
				"0B0B59665990E30B4D43040578D0C7A4.dat",
				"0D6FB2CDCD582E01555D48C91756ED4A.dat"};
	// private static final int TOTALDOWNSIZE = 10 *1024;

	private long calculate(final long downloadTime, final long bytesIn) {
		long bytespersecond = (bytesIn / downloadTime) * 1000;
		return Math.round(bytespersecond / 1024);
	}

	public DownloadTask(Context ctx, TaskListener listener,boolean isGroupUser) {
		super(listener);
		mListener = listener;
		mDownloadSize = 0;
		mContext = ctx;
		this.isGroupUser = isGroupUser;
		String strUrl = SystemProperties.get("ro.product.testspeedurl", "");
		Log.v(TAG, "DownloadTask()");
		if(!"".equals(strUrl) && null != strUrl){
			changchengService = strUrl+TEST_SPEED_API;
			Log.i(TAG, "---strUrl---"+strUrl+"----changchengService---"+changchengService);
		}
	}



	@Override
	public void get() throws Exception {
		String strUrl = testURL;
   
		Log.v(TAG, "isUserGroup2=="+isGroupUser);
		if(isGroupUser){
			testURL = changchengService;
		}else {
			testURL = shafaService;
		}
		testGroupOrShafaServer();
 
        
	}
	
	
	private void testGroupOrShafaServer(){
		
		try {
			if (!TextUtils.isEmpty(testURL)) {
				if (listener != null) {
					listener.taskStarted(this);
				}
				Log.v(TAG, "url=="+testURL);
				URL url = new URL(testURL);
				byte[] arrayOfByte = new byte[1024];
				HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
				httpConn.setUseCaches(false);
				httpConn.setDoInput(true);
				httpConn.setDoOutput(false);
				httpConn.setConnectTimeout(5000);
				httpConn.setReadTimeout(5000);
				Log.v(TAG, "code>>"+httpConn.getResponseCode());
				if(httpConn.getResponseCode()!=200){
					if (mListener != null) {
						mListener.taskFailed(this,mContext.getResources().getString(R.string.network_error));
					}
					return;
				}
 
 
				boolean isException = false;
				long start = System.currentTimeMillis();
				BufferedInputStream localBufferedInputStream = new BufferedInputStream(
						httpConn.getInputStream());
				try {

					if (localBufferedInputStream == null || mIsCanceled) {
						try {
							if (localBufferedInputStream != null)
								localBufferedInputStream.close();
							httpConn.disconnect();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						if (mListener != null) {
							mListener.taskFailed(this,mContext.getResources().getString(R.string.network_error));
						}
						return;
					}
					int currentByte = 0;
					long updateStart = System.currentTimeMillis();
					long updateDelta = 0;
					int bytesInThreshold = 0;
					mDownloadSize =0;
					while ((currentByte = localBufferedInputStream.read(arrayOfByte)) != -1
							&& !mIsCanceled) {
						mDownloadSize += currentByte;
						bytesInThreshold += currentByte;
						updateDelta = System.currentTimeMillis() - updateStart;
						
						if (updateDelta >= UPDATE_THRESHOLD) {
							long curspeed = calculate(updateDelta, bytesInThreshold);
							long downloadTime = (System.currentTimeMillis() - start);
							
							if (downloadTime == 0) {
								downloadTime = 1;
							}
							long averspeed = calculate(downloadTime, mDownloadSize);
							
							Log.v(TAG, "downloadCount=="+downloadCount);
							if(downloadCount>DOWNLOAD_TIME){
								mListener.taskCancelCompleted(this);
								isDownloadFinished = true;
								break;
							}
							
							if (mListener != null) {
//								Log.v(TAG, "curspeed=="+curspeed+",averspeed="+averspeed);
								mListener.taskProgress(this, curspeed, averspeed);
								downloadCount++;
							}

//							Log.v("aa", "iscancel=="+mIsCanceled+" downloadCount=="+downloadCount);
							updateStart = System.currentTimeMillis();
							bytesInThreshold = 0;
						}
						
					}
					localBufferedInputStream.close();
					
					
					if(downloadCount<DOWNLOAD_TIME && !mIsCanceled && !isException){  //针对千兆网络，下载太快了，需要重复下载
						Log.v(TAG, "again download");
						call();
					}
					
					if (mIsCanceled || downloadCount>=DOWNLOAD_TIME) {
						Log.e(TAG, "mIsCanceled =========="+mIsCanceled+",downloadCount=="+downloadCount+",downloadFinished==="+isDownloadFinished);
						if(!isDownloadFinished)
						    mListener.taskCancelCompleted(this);
					}
				} catch (Exception e) {
					e.printStackTrace();
					isException = true;
				}finally{
					Log.v(TAG, "finished");
					try {
						if (localBufferedInputStream != null)
							localBufferedInputStream.close();
						httpConn.disconnect();
						
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				

				if (isException) {
					if (mListener != null) {
						mListener.taskFailed(this,mContext.getResources().getString(R.string.network_error));
					}
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (mListener != null) {
				mListener.taskFailed(this,mContext.getResources().getString(R.string.network_error));
			}
		} 
	}
	


	private String getJinshanServer(){
		
		Random random = new Random();
		int re = random.nextInt(jinshan.length);
		String result = comJinshan + jinshan[re];
		return result;
	}
	
 
}
