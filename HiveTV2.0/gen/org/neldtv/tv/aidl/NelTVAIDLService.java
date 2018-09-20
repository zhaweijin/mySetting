/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/carter/backup/HiveTV2.0/src/org/neldtv/tv/aidl/NelTVAIDLService.aidl
 */
package org.neldtv.tv.aidl;
public interface NelTVAIDLService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.neldtv.tv.aidl.NelTVAIDLService
{
private static final java.lang.String DESCRIPTOR = "org.neldtv.tv.aidl.NelTVAIDLService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.neldtv.tv.aidl.NelTVAIDLService interface,
 * generating a proxy if needed.
 */
public static org.neldtv.tv.aidl.NelTVAIDLService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.neldtv.tv.aidl.NelTVAIDLService))) {
return ((org.neldtv.tv.aidl.NelTVAIDLService)iin);
}
return new org.neldtv.tv.aidl.NelTVAIDLService.Stub.Proxy(obj);
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
case TRANSACTION_startTVSystem:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.startTVSystem();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_tuneDTVChannel:
{
data.enforceInterface(DESCRIPTOR);
this.tuneDTVChannel();
return true;
}
case TRANSACTION_openEPG:
{
data.enforceInterface(DESCRIPTOR);
this.openEPG();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.neldtv.tv.aidl.NelTVAIDLService
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
@Override public int startTVSystem() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startTVSystem, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void tuneDTVChannel() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_tuneDTVChannel, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void openEPG() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_openEPG, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_startTVSystem = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_tuneDTVChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_openEPG = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public int startTVSystem() throws android.os.RemoteException;
public void tuneDTVChannel() throws android.os.RemoteException;
public void openEPG() throws android.os.RemoteException;
}
