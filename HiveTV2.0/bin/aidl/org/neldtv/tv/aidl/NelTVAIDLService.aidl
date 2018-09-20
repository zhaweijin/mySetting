package org.neldtv.tv.aidl;
interface NelTVAIDLService {
	int startTVSystem();
	oneway void tuneDTVChannel();
	oneway void openEPG();
}