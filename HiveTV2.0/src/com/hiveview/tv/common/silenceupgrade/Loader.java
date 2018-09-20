package com.hiveview.tv.common.silenceupgrade;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.service.entity.AppMarketEntity;
import com.hiveview.tv.service.exception.ServiceException;

public class Loader implements Runnable{

	
	private String urlStr;
	private String absolutePath;
	private LoaderListener loadListener;

	
	/**
	 * constructor to create a object.
	 * 
	 * @param urlStr, the String of url.
	 * */
	public Loader(String urlStr,String absolutePath,LoaderListener loadListener){
		this.urlStr = urlStr;
		this.absolutePath = absolutePath;
		this.loadListener = loadListener;
	}

	public void run(){
		
		URL mURL = null;
		InputStream in = null;
		boolean flag = true;
		int  count = 0;
		long currentPosition = 0;
		long totalSize = 0;
		
		File file = new File(absolutePath);
		
		try {
			
			mURL = new URL(urlStr);
			totalSize = getContentLength(mURL);
			
			long availableSize = FileLoadUtils.getAvailableExternalStorageSize();
			if(totalSize > availableSize && availableSize > 0){
				throw new ServiceException("Lack of AvailableExternalStorageSize!");
			}
			
			file.createNewFile();
			RandomAccessFile randomFileTotal = new RandomAccessFile(file, "rwd");
			randomFileTotal.setLength(totalSize);
			randomFileTotal.close();
		} catch (ServiceException e1) {
			flag = false;
			loadListener.onError(e1.getMessage(),1);
		} catch (IOException e) {
			flag = false;
			loadListener.onError(e.getMessage(),1);
		}
		
		
		while (count < AppConstant.NO_3 && flag) {
			try {
				
				Thread.sleep(1500);
				HttpURLConnection connection = (HttpURLConnection) mURL.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setConnectTimeout(AppConstant.NO_5 * AppConstant.SECOND_1);
				connection.setReadTimeout(AppConstant.NO_5*AppConstant.NO_10 * AppConstant.SECOND_1);
				connection.setRequestProperty("Range", new StringBuffer()
															.append("bytes=")
															.append(currentPosition)
															.append("-")
															.append(totalSize)
															.toString());

				in = connection.getInputStream();
				
				RandomAccessFile randomFile = new RandomAccessFile(file, "rwd");
				randomFile.seek(currentPosition);
				
				byte[] buffer = new byte[AppConstant.BUFFER];
				int length = 0;
				
					
					while ((length = in.read(buffer)) != AppConstant.NO_END) {
						
						try{
							randomFile.write(buffer, AppConstant.NO_0, length);
							currentPosition += length;
							Log.i("msg","currentPosition = " + currentPosition);
						
							}catch(IOException e){
						
							}
						}

					connection.disconnect();
				
					if(randomFile!=null)
						randomFile.close();
					
					loadListener.onComplete(file.getName());
					break;
				
			} catch (InterruptedException e) {
				
			} catch (IOException e) {
				count ++;
				if(count == AppConstant.NO_3){
					loadListener.onFailure(e.getMessage(),1);
				}
			} catch (Throwable t) {
				loadListener.onFailure(t.getMessage(),-1);
			}finally {
				try {
					
					if (in != null)
						 in.close();

				} catch (Throwable e) {
					loadListener.onFailure(e.getMessage(),1);
				}
			}
		}
		
	}
	
	public long getContentLength(URL mURL) throws IOException{
		long totalSize = 0;
		int count = AppConstant.NO_0 ;
		while(count<AppConstant.NO_3){
			try {
				if(totalSize>AppConstant.NO_0)
					return totalSize;
				HttpURLConnection connection = (HttpURLConnection) mURL.openConnection();
				totalSize = connection.getContentLength();
			} catch (IOException e) {
				count++;
			} catch (NullPointerException e) {
				count++;
			}
		}
		
		if(totalSize < 0){
			throw new IOException();
		}
		
		return totalSize;
	}
	
	public interface LoaderListener{
		
		void onStart(AppMarketEntity entity);
		void onError(String message,int code);
		void onFailure(String message,int code);
		void onComplete(String message);
		
	}
}
