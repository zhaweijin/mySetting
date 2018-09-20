package com.hiveview.tv.common.silenceupgrade;

import java.io.File;
import java.text.DecimalFormat;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.view.Gravity;
import android.widget.Toast;

import com.hiveview.tv.common.AppConstant;

public class FileLoadUtils {

	/**
	 * check the sdcard if it is existed or available.
	 * */
	public static boolean isExternalStorageMountAvailable(){
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
			return true;
			return false;
	}
	
	/**
	 * external storage available memory size
	 * @return long , available size
	 * @author Arashmen
	 * */
	public static long getAvailableExternalStorageSize(){
		 long availableExternalMemorySize = 0;  
		 File path = Environment.getExternalStorageDirectory();   
		 StatFs stat = new StatFs(path.getPath());   
		 long blockSize = stat.getBlockSize();   
		 long availableBlocks = stat.getAvailableBlocks();   
		 availableExternalMemorySize = availableBlocks * blockSize;
		 availableExternalMemorySize = availableExternalMemorySize - AppConstant.SIZE_100M;
		 if(availableExternalMemorySize < AppConstant.NO_0)
			 availableExternalMemorySize = 0L;
		 return availableExternalMemorySize;
	}
	
	/**
	 * external storage  memory size in total
	 * return long, total size
	 * */
	public static long getTotalExternalStorageSize(){
		 long availableExternalMemorySize = 0;  
		 File path = Environment.getExternalStorageDirectory();   
		 StatFs stat = new StatFs(path.getPath());   
		 long blockSize = stat.getBlockSize();   
		 long totalBlocks = stat.getBlockCount();   
		 availableExternalMemorySize = totalBlocks * blockSize;   
		 return availableExternalMemorySize;
	}
	
	/**
	 * check the file if it exists in external storage.
	 * If it existed , it indicated that this file didn't down-load completely,
	 * it's a resuming load file.
	 * @return String , absolutely path. 
	 * */
	public static String getExternalStorageAbsolutePath(String urlStr){
		int position = urlStr.lastIndexOf(AppConstant.CHARACTER_SLASH);
		if(position>0){
			urlStr = urlStr.substring(position+1);
		}
		String dir = getLoadExternalDir();
		File file = new File(dir);
		if(!file.exists())
			file.mkdirs();
		return dir+urlStr;
	}
	
	/**
	 * clear all video file in client
	 * */
	public static void clearAll(){
		String dir = getLoadExternalDir();
		File file = new File(dir);
		deleteFile(file);
	}
	
	/**
	 * delete the children files. 
	 * */
	public static void deleteFile(File file) {
		
		if (file.exists()) {
			
			if (file.isFile()) {
				
				file.delete();
				
			} else if (file.isDirectory()) {
				
				File files[] = file.listFiles();
				
				for (int i = 0; i < files.length; i++) {
					
					deleteFile(files[i]);
				}
				
			}
			file.delete();
		}
	}
	
	
	
	/**
	 * get the directs and children files' size
	 * */
	public long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); 
			}
		}
		return dirSize;
	}
	
	/**
	 * format number into XX G   sample : 1.33 G
	 * */
	public String getNumberFormatDirSize(long dirSize) {  
		double size = AppConstant.NO_0;  
		size = (Double.valueOf(dirSize)) / (1024 * 1024 * 1024);  
		DecimalFormat df = new DecimalFormat("0.00");  
		String filesize = df.format(size);
	    return new StringBuffer().append(filesize).append("G").toString();  
	}
	
	/**
	 * get qiyi video file direct
	 * */
	public static String getLoadExternalDir(){
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		StringBuffer mBuffer = new StringBuffer();
		mBuffer.append(path);
		mBuffer.append(AppConstant.CHARACTER_SLASH);
		mBuffer.append(AppConstant.DOMYBOXDIR);
		mBuffer.append(AppConstant.CHARACTER_SLASH);
		return mBuffer.toString();
	}
	
	/**
	 * show toast
	 * */
	public static void showToast(Context context ,int res){
		Toast toast = Toast.makeText(context,res, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 3*toast.getYOffset()/2);
		toast.show();
	}

	/**
	 * show toast
	 * */
	public static void showToast(Context context ,String res){
		Toast toast = Toast.makeText(context,res, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 3*toast.getYOffset()/2);
		toast.show();
	}
	
}