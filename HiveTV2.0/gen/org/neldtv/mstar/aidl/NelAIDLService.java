/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/carter/backup/HiveTV2.0/src/org/neldtv/mstar/aidl/NelAIDLService.aidl
 */
package org.neldtv.mstar.aidl;
public interface NelAIDLService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.neldtv.mstar.aidl.NelAIDLService
{
private static final java.lang.String DESCRIPTOR = "org.neldtv.mstar.aidl.NelAIDLService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.neldtv.mstar.aidl.NelAIDLService interface,
 * generating a proxy if needed.
 */
public static org.neldtv.mstar.aidl.NelAIDLService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.neldtv.mstar.aidl.NelAIDLService))) {
return ((org.neldtv.mstar.aidl.NelAIDLService)iin);
}
return new org.neldtv.mstar.aidl.NelAIDLService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_initCallBack:
{
data.enforceInterface(DESCRIPTOR);
org.neldtv.mstar.aidl.NelAIDLClient _arg0;
_arg0 = org.neldtv.mstar.aidl.NelAIDLClient.Stub.asInterface(data.readStrongBinder());
this.initCallBack(_arg0);
return true;
}
case TRANSACTION_initSourceVideo:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
this.initSourceVideo(_arg0, _arg1, _arg2, _arg3);
return true;
}
case TRANSACTION_reOpenSourceVideo:
{
data.enforceInterface(DESCRIPTOR);
this.reOpenSourceVideo();
return true;
}
case TRANSACTION_sourceVideoFillScreen:
{
data.enforceInterface(DESCRIPTOR);
this.sourceVideoFillScreen();
return true;
}
case TRANSACTION_sourceVideoPipscale:
{
data.enforceInterface(DESCRIPTOR);
this.sourceVideoPipscale();
return true;
}
case TRANSACTION_closeSourceVideo:
{
data.enforceInterface(DESCRIPTOR);
this.closeSourceVideo();
return true;
}
case TRANSACTION_openSourceChangeDialog:
{
data.enforceInterface(DESCRIPTOR);
this.openSourceChangeDialog();
return true;
}
case TRANSACTION_setSourceID:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setSourceID(_arg0);
return true;
}
case TRANSACTION_changeSource:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
this.changeSource(_arg0, _arg1);
return true;
}
case TRANSACTION_closeIRExe:
{
data.enforceInterface(DESCRIPTOR);
this.closeIRExe();
return true;
}
case TRANSACTION_getSourceID:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getSourceID();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getSourceName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getSourceName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getSourceSelect:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<java.lang.String> _result = this.getSourceSelect();
reply.writeNoException();
reply.writeStringList(_result);
return true;
}
case TRANSACTION_sendIR:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.sendIR(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_openIRExeDialog:
{
data.enforceInterface(DESCRIPTOR);
this.openIRExeDialog();
return true;
}
case TRANSACTION_getSupportedIRDevices:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<java.lang.String> _result = this.getSupportedIRDevices();
reply.writeNoException();
reply.writeStringList(_result);
return true;
}
case TRANSACTION_setCurrentIRDevices:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setCurrentIRDevices(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setIRCommands:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setIRCommands(_arg0);
return true;
}
case TRANSACTION_setIRType:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setIRType(_arg0);
return true;
}
case TRANSACTION_screenOrientationCheck:
{
data.enforceInterface(DESCRIPTOR);
this.screenOrientationCheck();
return true;
}
case TRANSACTION_getResolution:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getResolution();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getDisplayPosition:
{
data.enforceInterface(DESCRIPTOR);
int[] _result = this.getDisplayPosition();
reply.writeNoException();
reply.writeIntArray(_result);
return true;
}
case TRANSACTION_getSourceWHXY:
{
data.enforceInterface(DESCRIPTOR);
int[] _result = this.getSourceWHXY();
reply.writeNoException();
reply.writeIntArray(_result);
return true;
}
case TRANSACTION_getTVVolume:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getTVVolume();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setTVVolume:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
boolean _result = this.setTVVolume(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setTVMuteFlag:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
boolean _result = this.setTVMuteFlag(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.neldtv.mstar.aidl.NelAIDLService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
//与切源相关服务操作

@Override public void initCallBack(org.neldtv.mstar.aidl.NelAIDLClient callBackClient) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callBackClient!=null))?(callBackClient.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_initCallBack, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void initSourceVideo(int source_x, int source_y, int source_width, int source_height) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(source_x);
_data.writeInt(source_y);
_data.writeInt(source_width);
_data.writeInt(source_height);
mRemote.transact(Stub.TRANSACTION_initSourceVideo, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void reOpenSourceVideo() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_reOpenSourceVideo, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void sourceVideoFillScreen() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sourceVideoFillScreen, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void sourceVideoPipscale() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sourceVideoPipscale, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void closeSourceVideo() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_closeSourceVideo, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void openSourceChangeDialog() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_openSourceChangeDialog, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void setSourceID(java.lang.String sourceid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(sourceid);
mRemote.transact(Stub.TRANSACTION_setSourceID, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void changeSource(java.lang.String id, boolean needChange) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(id);
_data.writeInt(((needChange)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_changeSource, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void closeIRExe() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_closeIRExe, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public int getSourceID() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSourceID, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getSourceName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSourceName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.List<java.lang.String> getSourceSelect() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<java.lang.String> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSourceSelect, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArrayList();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//红外发射相关服务操作

@Override public void sendIR(int key) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(key);
mRemote.transact(Stub.TRANSACTION_sendIR, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void openIRExeDialog() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_openIRExeDialog, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public java.util.List<java.lang.String> getSupportedIRDevices() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<java.lang.String> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSupportedIRDevices, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArrayList();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setCurrentIRDevices(int irDevice) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(irDevice);
mRemote.transact(Stub.TRANSACTION_setCurrentIRDevices, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setIRCommands(java.lang.String commands) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(commands);
mRemote.transact(Stub.TRANSACTION_setIRCommands, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void setIRType(int type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
mRemote.transact(Stub.TRANSACTION_setIRType, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//是否切换横竖屏

@Override public void screenOrientationCheck() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_screenOrientationCheck, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
//分辨率相关

@Override public int getResolution() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getResolution, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int[] getDisplayPosition() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDisplayPosition, _data, _reply, 0);
_reply.readException();
_result = _reply.createIntArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int[] getSourceWHXY() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSourceWHXY, _data, _reply, 0);
_reply.readException();
_result = _reply.createIntArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//参数顺序:x,y,width,height 
//电视系统声音设置

@Override public int getTVVolume() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getTVVolume, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean setTVVolume(int volume) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(volume);
mRemote.transact(Stub.TRANSACTION_setTVVolume, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean setTVMuteFlag(boolean muteFlag) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((muteFlag)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setTVMuteFlag, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_initCallBack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_initSourceVideo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_reOpenSourceVideo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_sourceVideoFillScreen = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_sourceVideoPipscale = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_closeSourceVideo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_openSourceChangeDialog = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_setSourceID = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_changeSource = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_closeIRExe = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_getSourceID = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_getSourceName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_getSourceSelect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_sendIR = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_openIRExeDialog = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_getSupportedIRDevices = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_setCurrentIRDevices = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_setIRCommands = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_setIRType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_screenOrientationCheck = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_getResolution = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_getDisplayPosition = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_getSourceWHXY = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_getTVVolume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_setTVVolume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_setTVMuteFlag = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
}
//与切源相关服务操作

public void initCallBack(org.neldtv.mstar.aidl.NelAIDLClient callBackClient) throws android.os.RemoteException;
public void initSourceVideo(int source_x, int source_y, int source_width, int source_height) throws android.os.RemoteException;
public void reOpenSourceVideo() throws android.os.RemoteException;
public void sourceVideoFillScreen() throws android.os.RemoteException;
public void sourceVideoPipscale() throws android.os.RemoteException;
public void closeSourceVideo() throws android.os.RemoteException;
public void openSourceChangeDialog() throws android.os.RemoteException;
public void setSourceID(java.lang.String sourceid) throws android.os.RemoteException;
public void changeSource(java.lang.String id, boolean needChange) throws android.os.RemoteException;
public void closeIRExe() throws android.os.RemoteException;
public int getSourceID() throws android.os.RemoteException;
public java.lang.String getSourceName() throws android.os.RemoteException;
public java.util.List<java.lang.String> getSourceSelect() throws android.os.RemoteException;
//红外发射相关服务操作

public void sendIR(int key) throws android.os.RemoteException;
public void openIRExeDialog() throws android.os.RemoteException;
public java.util.List<java.lang.String> getSupportedIRDevices() throws android.os.RemoteException;
public void setCurrentIRDevices(int irDevice) throws android.os.RemoteException;
public void setIRCommands(java.lang.String commands) throws android.os.RemoteException;
public void setIRType(int type) throws android.os.RemoteException;
//是否切换横竖屏

public void screenOrientationCheck() throws android.os.RemoteException;
//分辨率相关

public int getResolution() throws android.os.RemoteException;
public int[] getDisplayPosition() throws android.os.RemoteException;
public int[] getSourceWHXY() throws android.os.RemoteException;
//参数顺序:x,y,width,height 
//电视系统声音设置

public int getTVVolume() throws android.os.RemoteException;
public boolean setTVVolume(int volume) throws android.os.RemoteException;
public boolean setTVMuteFlag(boolean muteFlag) throws android.os.RemoteException;
}
