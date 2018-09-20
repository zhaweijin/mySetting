package com.hiveview.tv.utils;

import android.graphics.Typeface;

import com.hiveview.tv.common.HiveviewApplication;

public class TypefaceUtils {
	private static Typeface standardfFontFace;
	private static Typeface ce35FontFace; 
	private static TypefaceUtils typeFace = new TypefaceUtils();

	private TypefaceUtils() {}
	
	/**幼圆字体*/
	public static Typeface  getStandardfFontFace(){
		if(standardfFontFace==null){
			standardfFontFace = Typeface.createFromAsset(HiveviewApplication.mContext.getAssets(),"fonts/standard.TTF");
		}
		return standardfFontFace;
	}
	
	public static Typeface  getCE35FontFace(){
		if(ce35FontFace==null){
			ce35FontFace = Typeface.createFromAsset(HiveviewApplication.mContext.getAssets(),"fonts/ce_35.TTF");
		}
		return ce35FontFace;
	}
	
	public TypefaceUtils getInstance(){
		if(typeFace==null){
			typeFace=new TypefaceUtils();
		}
		return typeFace;
	}
	
}
