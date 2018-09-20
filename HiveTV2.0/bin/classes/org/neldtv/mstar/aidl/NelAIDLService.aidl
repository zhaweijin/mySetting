package org.neldtv.mstar.aidl;
import  org.neldtv.mstar.aidl.NelAIDLClient; 
interface NelAIDLService {
	//与切源相关服务操作
	oneway void initCallBack(NelAIDLClient callBackClient);
    oneway void initSourceVideo(in int source_x,in int source_y,in int source_width,in int source_height);
    oneway void reOpenSourceVideo();
    oneway void sourceVideoFillScreen();
    oneway void sourceVideoPipscale();
    oneway void closeSourceVideo();
    oneway void openSourceChangeDialog();
    oneway void setSourceID(String sourceid);
    oneway void changeSource(String id,boolean needChange);
    oneway void closeIRExe();
    int getSourceID();
    String getSourceName();
    List<String> getSourceSelect();
    //红外发射相关服务操作
    void sendIR(int key);
    oneway void openIRExeDialog();
    List<String> getSupportedIRDevices();
    void setCurrentIRDevices(int irDevice);
    oneway void setIRCommands(String commands);
    oneway void setIRType(int type);
    //是否切换横竖屏
    oneway void screenOrientationCheck();
    //分辨率相关
    int getResolution();
    int []getDisplayPosition();
    int []getSourceWHXY();//参数顺序:x,y,width,height 
    //电视系统声音设置
    int getTVVolume();
    boolean setTVVolume(int volume);
    boolean setTVMuteFlag(boolean muteFlag);   
}