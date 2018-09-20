package com.hiveview.tv.view.pager3d;

import android.annotation.SuppressLint;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class MatrixRotateYAnimation extends Animation {  

    private final float mFromDegree;//旋转前的角度  
    private final float mToDegree;//旋转后的角度
    private Camera mCamera;  
    private int halfWidth;  
    private int halfHeight; 
    

    public MatrixRotateYAnimation(float fromDegree, float toDegree){  
        this.mFromDegree = fromDegree;  
        this.mToDegree = toDegree;  
        mCamera = new Camera();
    }
    

    @Override  
    public void initialize(int width, int height, int parentWidth,int parentHeight) {  
        super.initialize(width, height, parentWidth, parentHeight);  
        halfWidth = width / 2;  
        halfHeight = height / 2;  
    }

    @SuppressLint("NewApi")
	@Override  
    protected void applyTransformation(float interpolatedTime, Transformation t) {  
        Matrix matrix = t.getMatrix();    
        //当前旋转的角度�?  
        float degree = mFromDegree + ( mToDegree - mFromDegree ) * interpolatedTime;  
        
        //save()、restore()求解�? 
        mCamera.save();  
        
       {  
            //这句代码的效果是画面固定左上角缩放：halfWidth值为正，画面缩小，�?越大，画面越小；值为负，画面放大，halfWidth值越大（�?halfWidth值越小），画面越大�?  
            //这个值就如同照相机与画面之间距离的增量�?  
            mCamera.translate(0, 0,halfWidth*1.7f);  
            
            mCamera.rotateY(degree);
            
            mCamera.translate(0, 0, -halfWidth*1.7f);  
        }
       
        //注意这句代码，这句代码是以set的方式直接用mCamera里的matrix值将参数matrix覆盖掉，�?��如果除了mCamera变换外还有其它变换，请放在这句之后（比如后面的变换中心点调整）�?  
        mCamera.getMatrix(matrix);  
        mCamera.restore();    
        
        //当只有这�?��变换代码时，画面左移halfWidth,上移halfHeight，从而画面的中心与原来的左上角重合�?而左上角，是默认的变换中心点�? 
        //每一种变换，无论是缩放还是旋转，都是相对于一个中心点而言的�?图像上的每一点根据自身与中心点的距离来决定自己变换的�? 
        //这句代码可以让图像好像是以自己的中心为变换中心来变换�?��（比如不是绕左边界转而是绕自己的中轴线转），其实是让自己的中心与变换中心重合了�?已�?  
        //由于这句代码的存在，前面mCamera里的那些变换的变换中心都将不仅是原位置的左上角，也是画面本身的中心，这两个中心重合在�?��了�?  
        matrix.preTranslate(-halfWidth, -halfHeight);     
        
          
        //当只有这�?��变换代码时，画面右移halfWidth,下移halfHeight.  
        //这句话的存在是因为上�?��代码把图像中心平移到左上角去了�?要是没有这句，我们看到的画面就是立方体在原先位置的左上角转了�? 
        matrix.postTranslate(halfWidth, halfHeight);  
          
    }     
}  
  
/*注释1 
    protected void applyTransformation(float interpolatedTime, Transformation t) { 
        Matrix matrix = t.getMatrix();   
         
        float degree = mFromDegree + ( mToDegree - mFromDegree ) * interpolatedTime; 
        mCamera.save(); 
        mCamera.translate(0, 0,halfWidth); 
         
        if ( degree >= 82.0f ) 
        { 
            mCamera.rotateY(90.0f); 
        } 
        else if ( degree <= -82.0f ) 
        { 
            mCamera.rotateY(90.0f); 
        } 
        else 
        {    
            mCamera.rotateY(degree); 
            mCamera.translate(0, 0, -halfWidth ); 
        } 
         
        mCamera.getMatrix(matrix); 
        mCamera.restore(); 
        matrix.preTranslate(-halfWidth, -halfHeight);    
        matrix.postTranslate(halfWidth, halfHeight); 
    } 
} 
*/  