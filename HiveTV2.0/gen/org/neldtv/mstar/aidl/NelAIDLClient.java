/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/carter/backup/HiveTV2.0/src/org/neldtv/mstar/aidl/NelAIDLClient.aidl
 */
package org.neldtv.mstar.aidl;
public interface NelAIDLClient extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.neldtv.mstar.aidl.NelAIDLClient
{
private static final java.lang.String DESCRIPTOR = "org.neldtv.mstar.aidl.NelAIDLClient";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.neldtv.mstar.aidl.NelAIDLClient interface,
 * generating a proxy if needed.
 */
public static org.neldtv.mstar.aidl.NelAIDLClient asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.neldtv.mstar.aidl.NelAIDLClient))) {
return ((org.neldtv.mstar.aidl.NelAIDLClient)iin);
}
return new org.neldtv.mstar.aidl.NelAIDLClient.Stub.Proxy(obj);
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
case TRANSACTION_surfaceResume:
{
data.enforceInterface(DESCRIPTOR);
this.surfaceResume();
return true;
}
case TRANSACTION_mEthernetChange:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.mEthernetChange(_arg0);
return true;
}
case TRANSACTION_isHasInputSource:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.isHasInputSource(_arg0);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.neldtv.mstar.aidl.NelAIDLClient
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
@Override public void surfaceResume() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_surfaceResume, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void mEthernetChange(int res) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(res);
mRemote.transact(Stub.TRANSACTION_mEthernetChange, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
@Override public void isHasInputSource(int flag) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(flag);
mRemote.transact(Stub.TRANSACTION_isHasInputSource, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_surfaceResume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_mEthernetChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_isHasInputSource = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void surfaceResume() throws android.os.RemoteException;
public void mEthernetChange(int res) throws android.os.RemoteException;
public void isHasInputSource(int flag) throws android.os.RemoteException;
}
