package org.neldtv.mstar.aidl;
oneway interface NelAIDLClient {
    void surfaceResume();
    void mEthernetChange(int res);
    void isHasInputSource(int flag);
}