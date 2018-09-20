package com.hiveview.cloudtv.settings.wifi;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;

import com.hiveview.cloudtv.settings.util.Utils;

import static android.net.wifi.WifiConfiguration.INVALID_NETWORK_ID;
import android.net.IpConfiguration;
import android.net.StaticIpConfiguration;
import android.net.IpConfiguration.IpAssignment;
import android.R.bool;
import android.R.integer;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.NetworkUtils;
import android.net.RouteInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiConfig {
	
	private String ip="";
	private String netmask="";
	private String gateway="";
	private String dns="";
	
	private String tag = "wifiutil";
	private WifiConfiguration currentConfiguration=null;
	
	private WifiManager mWifiManager;
	
	private boolean wifiActive(Context mContext) {  
	    ConnectivityManager connectivityManager = (ConnectivityManager) mContext  
	            .getSystemService(Context.CONNECTIVITY_SERVICE);  
	    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();  
	    if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {  
	        return true;  
	    }  
	    return false;  
	} 
	
	public WifiConfig(Context context){
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		Log.v(tag, "wifi ssid=="+mWifiManager.getConnectionInfo().getSSID());
		List<WifiConfiguration> configurations = mWifiManager.getConfiguredNetworks();
 
		if(configurations==null) return;
		for(int i=0;i<configurations.size();i++){
			Log.v(tag, "wifi config=="+configurations.get(i).SSID);
			if(mWifiManager.getConnectionInfo().getSSID().equals(configurations.get(i).SSID)){
				currentConfiguration = configurations.get(i);
			}
		}
		
		if(currentConfiguration==null) return;
		
		if (currentConfiguration.getIpConfiguration().ipAssignment == IpAssignment.STATIC) {
			StaticIpConfiguration sIpConfig = currentConfiguration.getIpConfiguration().getStaticIpConfiguration();

			LinkAddress linkaddress = sIpConfig.ipAddress;
			int addr = NetworkUtils.prefixLengthToNetmaskInt(linkaddress.getNetworkPrefixLength());

			ip = sIpConfig.ipAddress.getAddress().getHostAddress();
			gateway = sIpConfig.gateway.getHostAddress();
			netmask = Utils.getAddress(addr);
			dns = sIpConfig.dnsServers.get(0).getHostAddress();

		} else if (currentConfiguration.getIpConfiguration().ipAssignment == IpAssignment.DHCP) {
			DhcpInfo dhcpInfo = getDhcpInfo();
			if (null != dhcpInfo) {
				ip = Utils.getAddress(dhcpInfo.ipAddress);
				gateway = Utils.getAddress(dhcpInfo.gateway);
				netmask = Utils.getInterfaceMask("wlan0");
				dns = Utils.getAddress(dhcpInfo.dns1);
			}
		}
	}

	public String getIp() {
		return ip;
	}
	 
	public String getNetmask() {
		return netmask;
	}
 

	public String getGateway() {
		return gateway;
	}

	public String getDns() {
		return dns;
	}
	
	public DhcpInfo getDhcpInfo(){
		return mWifiManager.getDhcpInfo();
	}
 
	
	/**
	 * 判断WiFi是否动态分配
	 * @return
	 */
	public boolean isDhcp(){
		if(currentConfiguration==null) return true;
		if (currentConfiguration.getIpConfiguration().ipAssignment == IpAssignment.STATIC) {
			return false;
		}else if (currentConfiguration.getIpConfiguration().ipAssignment == IpAssignment.DHCP) {
			return true;
		}
		return true;
	}
	
 

	/**
	 * 恢复WiFi动态分配模式
	 * @param config
	 */
	public void resetDhcpWifiConfiguration(){
		Log.v(tag, "save wifi config");
		IpConfiguration mIpConfiguration = new IpConfiguration();
        mIpConfiguration.setIpAssignment(IpAssignment.DHCP);
        mIpConfiguration.setStaticIpConfiguration(null);
        
		
        currentConfiguration.setIpConfiguration(mIpConfiguration);
 
        saveWiFi(currentConfiguration,mSaveListener);
	}

	
	
	public void saveWiFi(WifiConfiguration config,WifiManager.ActionListener listener){
		if(mWifiManager == null){
			return ;
		}
		mWifiManager.save(config, listener);
	}
	
	WifiManager.ActionListener mSaveListener = new WifiManager.ActionListener() {
		public void onSuccess() {
			Log.v(tag, "wifi save success!");
		}

		public void onFailure(int reason) {
			Log.v(tag, "wifi save failed!");
		}
	};
}
