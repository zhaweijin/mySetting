package com.hiveview.cloudtv.settings.connectivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.EthernetManager;
import android.net.IpConfiguration;
import android.net.IpConfiguration.IpAssignment;
import android.net.StaticIpConfiguration;
import android.net.NetworkUtils;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


import com.droidlogic.app.SystemControlManager;

/**
 * Listens for changes to the current connectivity status.
 */
public class ConnectivityManagerData{

    private static final String TAG = "ConnectivityManagerData";
    private static final boolean DEBUG = true;

	public final static String ETH_NAME  = "eth0";
	private  final String WLAN_NAME  = "wlan0";


    private final Context mContext;
    //private final Listener mListener;
    // private final IntentFilter mFilter;
    //private final BroadcastReceiver mReceiver;
    //private boolean mStarted;

    private final ConnectivityManager mConnectivityManager;
    private final WifiManager mWifiManager;
    private final EthernetManager mEthernetManager;
	
    private final SystemControlManager mSystemControl;
    //private WifiNetworkListener mWifiListener;



    public ConnectivityManagerData(Context context) {
        mContext = context;
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mEthernetManager = (EthernetManager) mContext.getSystemService(Context.ETHERNET_SERVICE);

		mSystemControl = new SystemControlManager (mContext);;
    }

	public boolean isWifiConnect()
	{
		return mWifiManager.isWifiEnabled();
	}

    /**
     * Return whether Ethernet port is available.
     */
    public boolean isEthernetAvailable() {
    	try {
    		String link= mSystemControl.readSysFs("/sys/class/ethernet/linkspeed");
    		Log.e(TAG, "isEthernetAvailable type 1" + link);

    		if(link.contains("unlink"))
    			return false;
    		else
    			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
    }

	public boolean isEthernetConnecting(){
		
		NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
		if(networkInfo != null)
		{
			if(networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET)
				return true;
		}
		Log.e(TAG, "isEthernetConnecting false");
		return false;
	}


    public boolean isDhcpRuning(String itfname) {
		return "running".equals(SystemProperties.get("init.svc.dhcpcd_"+itfname,"stopped"));
	}
    
    public boolean isDhcpSucess() {
		return "ok".equals(SystemProperties.get("dhcp.eth0.result",""));
	}


	public void setEthernetInfo(boolean isDhcp,String ipAddr,String mask,String dns,String gateway) {
		IpConfiguration mIpcfg;
		
        Intent intent = new Intent("android.net.ethernet.ETH_DNS_CHANGED");
		mContext.sendBroadcast(intent);

		if(isEthernetAvailable())
		{
			mIpcfg = mEthernetManager.getConfiguration();
			Log.e(TAG, "ipcfg "+ mIpcfg.getIpAssignment());

			
			if(mIpcfg.getIpAssignment() == IpAssignment.STATIC)
			{
				//Log.e(TAG, "ipcfg "+ mIpcfg.getStaticIpConfiguration().toString());
				//Log.e(TAG, "ipcfg "+ mIpcfg.getStaticIpConfiguration().ipAddress.toString());
				//Log.e(TAG, "ipcfg "+ mIpcfg.getStaticIpConfiguration().gateway.toString());
				//Log.e(TAG, "ipcfg "+ mIpcfg.getStaticIpConfiguration().dnsServers.get(0).toString());
				//Log.e(TAG, "ipcfg "+ mIpcfg.getStaticIpConfiguration().dnsServers.get(1).toString());
			}


			if(isDhcp)
			{
				mIpcfg.setIpAssignment(IpAssignment.DHCP);
				mIpcfg.setStaticIpConfiguration(null);
			}
			else
			{

				mIpcfg.setIpAssignment(IpAssignment.STATIC);
            	StaticIpConfiguration staticConfig = new StaticIpConfiguration();
            	mIpcfg.setStaticIpConfiguration(staticConfig);


				Inet4Address inetAddr = null;
				try {
					inetAddr = (Inet4Address) NetworkUtils.numericToInetAddress(ipAddr);
				} catch (IllegalArgumentException|ClassCastException e) {
					Log.e(TAG, "inetAddr setting invalid ");
				}


				IPv4Util iputil = new IPv4Util();
				int NetworkPrefixLen = iputil.getNetworkPrefixLength(mask);
				Log.e(TAG, "NetworkPrefixLen= "+ NetworkPrefixLen);
				
				try {
					if(NetworkPrefixLen!=0)
					  staticConfig.ipAddress = new LinkAddress(inetAddr,NetworkPrefixLen);
				} catch (Exception e) {					
					Log.e(TAG, "ip setting invalid ");
				}


		        if (!TextUtils.isEmpty(gateway)) {
		            try {
		                staticConfig.gateway =
		                        (Inet4Address) NetworkUtils.numericToInetAddress(gateway);
		            } catch (IllegalArgumentException|ClassCastException e) {
						Log.e(TAG, "gateway setting invalid ");
		            }
		        }

		        if (!TextUtils.isEmpty(dns)) {
		            try {
		                staticConfig.dnsServers.add(
		                        (Inet4Address) NetworkUtils.numericToInetAddress(dns));
		            } catch (IllegalArgumentException|ClassCastException e) {
						Log.e(TAG, "dns setting invalid ");
		            }
		        }

		        /*if (!TextUtils.isEmpty(dns2)) {
		            try {
		                staticConfig.dnsServers.add(
		                        (Inet4Address) NetworkUtils.numericToInetAddress(dns2));
		            } catch (IllegalArgumentException|ClassCastException e) {
		                return R.string.wifi_ip_settings_invalid_dns;
		            }
		        }*/
				
			}

			mEthernetManager.setConfiguration(mIpcfg);
		}

		
	}

	
    public String getEthernetMacAddress() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (networkInfo == null ||networkInfo.getType() != ConnectivityManager.TYPE_ETHERNET) {
            return "";
        } else {
            return networkInfo.getExtraInfo();
        }
    }

    public String getEthernetIpAddress() {

    	try {
    		if(isDhcpRuning(ETH_NAME))
    	   	{
    			return SystemProperties.get("dhcp.eth0.ipaddress","");
    		}
    		else
    		{
    			IpConfiguration IpCfg= mEthernetManager.getConfiguration();
    			if(IpCfg.getIpAssignment() == IpAssignment.STATIC)
    			{
    				StaticIpConfiguration StaIpCfg= IpCfg.getStaticIpConfiguration();
    				if(StaIpCfg != null)
    				{
    					Log.e(TAG, "getEthernetIpAddress " +(StaIpCfg.ipAddress.getAddress()).getHostAddress());
    					return (StaIpCfg.ipAddress.getAddress()).getHostAddress();
    				}
    				
    				return "";
    			}
    		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	   	
	   
		return "";
    }


    public String getEthernetNetmask() {
    	
    	try {
    	   	if(isDhcpRuning(ETH_NAME))
    	   	{
    			return SystemProperties.get("dhcp.eth0.mask","");
    		}
    		else
    		{
    			IpConfiguration IpCfg= mEthernetManager.getConfiguration();
    			if(IpCfg.getIpAssignment() == IpAssignment.STATIC)
    			{
    				StaticIpConfiguration StaIpCfg= IpCfg.getStaticIpConfiguration();
    				if(StaIpCfg != null)
    				{
    					
    					IPv4Util iputil = new IPv4Util();

    					Log.e(TAG, "getEthernetIpAddress " +StaIpCfg.ipAddress.getNetworkPrefixLength());
    					Log.e(TAG, "mask " +iputil.prefixLen2Mask(StaIpCfg.ipAddress.getNetworkPrefixLength()));					
    					return iputil.prefixLen2Mask(StaIpCfg.ipAddress.getNetworkPrefixLength());
    				}
    				
    				return "";
    			}
    		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	   
		return "";
    }

    public String getEthernetGateway() {
    	try {
    	   	if(isDhcpRuning(ETH_NAME))
    	   	{
    			Log.e(TAG, "getEthernetGateway dhcp " + SystemProperties.get("dhcp.eth0.gateway",""));
    			return SystemProperties.get("dhcp.eth0.gateway","");
    		}
    		else
    		{
    			IpConfiguration IpCfg= mEthernetManager.getConfiguration();
    			if(IpCfg.getIpAssignment() == IpAssignment.STATIC)
    			{
    				StaticIpConfiguration StaIpCfg= IpCfg.getStaticIpConfiguration();
    				if(StaIpCfg != null)
    				{
    					Log.e(TAG, "getEthernetGateway " + StaIpCfg.gateway.getHostAddress());
    					return StaIpCfg.gateway.getHostAddress();
    				}
    				
    				return "";
    			}
    		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		   
		return "";
    }


    public String getEthernetDns(int index) {
 
        try {
        	if(isDhcpRuning(ETH_NAME))
    	   	{
    			return SystemProperties.get("dhcp.eth0.dns1","");
    		}
    		else
    		{
    			IpConfiguration IpCfg= mEthernetManager.getConfiguration();
    			if(IpCfg.getIpAssignment() == IpAssignment.STATIC)
    			{
    				StaticIpConfiguration StaIpCfg= IpCfg.getStaticIpConfiguration();
    				if(StaIpCfg != null)
    				{
    					if(StaIpCfg.dnsServers ==null || index >= StaIpCfg.dnsServers.size())
    					{
    						Log.e(TAG, "getEthernetDns null");
    						return "";
    					}
    				
    					if(StaIpCfg.dnsServers.get(index) != null)
    					{
    						Log.e(TAG, "getEthernetDns " +StaIpCfg.dnsServers.get(index).getHostAddress());
    						return StaIpCfg.dnsServers.get(index).getHostAddress();
    					}
    				}
    				
    				return "";
    			}
    		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	   	
        // IPv6 address will not be shown like WifiInfo internally does.
        return "";
    }

    public String getEthernetDomains() {
        LinkProperties linkProperties =
                mConnectivityManager.getLinkProperties(ConnectivityManager.TYPE_ETHERNET);

        if (linkProperties == null) {
            return "";
        }

        return linkProperties.getDomains();
    }

    public String getEthernetLinkProperties() {
        LinkProperties linkProperties =
                mConnectivityManager.getLinkProperties(ConnectivityManager.TYPE_ETHERNET);

        if (linkProperties == null) {
            return "";
        }

        return linkProperties.toString();
    }




    public String getWifiIpAddress() {
		if(isDhcpRuning(WLAN_NAME))
		{
			Log.e(TAG, "getWifiIpAddress dhcp " + SystemProperties.get("dhcp.eth0.gateway",""));
			return SystemProperties.get("dhcp.wlan0.ipaddress","");
		}
		else
		{
		}
		return "";

    }

    public String getWifiNetmask() {
		if(isDhcpRuning(WLAN_NAME))
	   	{
			return SystemProperties.get("dhcp.wlan0.mask","");
		}
		else
		{
		}
	   
		return "";
    }

    public String getWifiGateway() {
	   	if(isDhcpRuning(WLAN_NAME))
	   	{
			Log.e(TAG, "getWifiGateway gw " + SystemProperties.get("dhcp.wlan0.gateway",""));
			return SystemProperties.get("dhcp.wlan0.gateway","");
		}
		else
		{

		}
		   
		return "";
    }


    public String getWifiDns(int index) {
	   	if(isDhcpRuning(WLAN_NAME))
	   	{
	   		if(index == 0)
				return SystemProperties.get("dhcp.wlan0.dns1","");
			else
				return SystemProperties.get("dhcp.wlan0.dns2","");
		}
		else
		{

		}
        // IPv6 address will not be shown like WifiInfo internally does.
        return "";
    }

    /**
     * Return the MAC address of the currently connected Wifi AP.
     */
    public String getWifiMacAddress() {
        if (isWifiConnect()) {
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            return wifiInfo.getMacAddress();
        } else {
            return "";
        }
    }



    public int getWifiSignalStrength(int maxLevel) {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        return WifiManager.calculateSignalLevel(wifiInfo.getRssi(), maxLevel);
    }

    public void forgetWifiNetwork() {
        int networkId = getWifiNetworkId();
        if (networkId != -1) {
            mWifiManager.forget(networkId, null);
        }
    }

    public int getWifiNetworkId() {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            return wifiInfo.getNetworkId();
        } else {
            return -1;
        }
    }

    public WifiConfiguration getWifiConfiguration() {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            int networkId = wifiInfo.getNetworkId();
            List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
            if (configuredNetworks != null) {
                for (WifiConfiguration configuredNetwork : configuredNetworks) {
                    if (configuredNetwork.networkId == networkId) {
                        return configuredNetwork;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Return a list of wifi networks. Ensure that if a wifi network is connected that it appears
     * as the first item on the list.
     */
    public List<ScanResult> getAvailableNetworks() {
        return null;
    }

    public IpConfiguration getIpConfiguration() {
        return mEthernetManager.getConfiguration();
    }

    private boolean isSecureWifi(WifiInfo wifiInfo) {
        if (wifiInfo == null)
            return false;
        int networkId = wifiInfo.getNetworkId();
        List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration configuredNetwork : configuredNetworks) {
                if (configuredNetwork.networkId == networkId) {
                    return configuredNetwork.allowedKeyManagement.get(KeyMgmt.WPA_PSK) ||
                        configuredNetwork.allowedKeyManagement.get(KeyMgmt.WPA_EAP) ||
                        configuredNetwork.allowedKeyManagement.get(KeyMgmt.IEEE8021X);
                }
            }
        }
        return false;
    }



    private boolean updateConnectivityStatus() {
      
		return false;
	}

	public static class IPv4Util {
		private final static int INADDRSZ = 4;


			public  byte[] ipToBytesByInet(String ipAddr) {
				try {
					return InetAddress.getByName(ipAddr).getAddress();
				} catch (Exception e) {
					throw new IllegalArgumentException(ipAddr + " is invalid IP");
				}
			}

			public  int[] ipToBytesByReg(String ipAddr) {
				int[] ret = new int[4];
				try {
					String[] ipArr = ipAddr.split(".");
					
					Log.e(TAG, "ipToBytesByReg "+ ipArr[0]);
					Log.e(TAG, "ipToBytesByReg "+ ipArr[1]);
					Log.e(TAG, "ipToBytesByReg "+ ipArr[2]);
					Log.e(TAG, "ipToBytesByReg "+ ipArr[3]);
					ret[0] = (int) (Integer.parseInt(ipArr[0]) & 0xFF);
					ret[1] = (int) (Integer.parseInt(ipArr[1]) & 0xFF);
					ret[2] = (int) (Integer.parseInt(ipArr[2]) & 0xFF);
					ret[3] = (int) (Integer.parseInt(ipArr[3]) & 0xFF);
					Log.e(TAG, "ipToBytesByReg "+ ret[0]);
					Log.e(TAG, "ipToBytesByReg "+ ret[1]);
					Log.e(TAG, "ipToBytesByReg "+ ret[2]);
					Log.e(TAG, "ipToBytesByReg "+ ret[3]);
					return ret;
				} catch (Exception e) {
					throw new IllegalArgumentException(ipAddr + " is invalid IP");
				}
			}
			/**
			 * ???????IP
			 * @param bytes
			 * @return int
			 */
			public  String bytesToIp(byte[] bytes) {
				return new StringBuffer().append(bytes[0] & 0xFF).append('.').append(
						bytes[1] & 0xFF).append('.').append(bytes[2] & 0xFF)
						.append('.').append(bytes[3] & 0xFF).toString();
			}
			/**
			 * ?????? byte[] -> int
			 * @param bytes
			 * @return int
			 */
			public  int bytesToInt(byte[] bytes) {
				int addr = bytes[3] & 0xFF;
				addr |= ((bytes[2] << 8) & 0xFF00);
				addr |= ((bytes[1] << 16) & 0xFF0000);
				addr |= ((bytes[0] << 24) & 0xFF000000);
				Log.e(TAG, "bytesToInt3 "+ bytes[3]);
				Log.e(TAG, "bytesToInt2 "+ bytes[2]);
				Log.e(TAG, "bytesToInt1 "+ bytes[1]);
				Log.e(TAG, "bytesToInt0 "+ bytes[0]);
				Log.e(TAG, "bytesToInt "+ addr);
				return addr;
			}
			/**
			 * ?IP?????int
			 * @param ipAddr
			 * @return int
			 */
			public int ipToInt(String ipAddr) {
				try {
					return bytesToInt(ipToBytesByInet(ipAddr));
				} catch (Exception e) {
					throw new IllegalArgumentException(ipAddr + " is invalid IP");
				}
			}
			/**
			 * ipInt -> byte[]
			 * @param ipInt
			 * @return byte[]
			 */
			public  byte[] intToBytes(int ipInt) {
				byte[] ipAddr = new byte[INADDRSZ];
				ipAddr[0] = (byte) ((ipInt >>> 24) & 0xFF);
				ipAddr[1] = (byte) ((ipInt >>> 16) & 0xFF);
				ipAddr[2] = (byte) ((ipInt >>> 8) & 0xFF);
				ipAddr[3] = (byte) (ipInt & 0xFF);
				return ipAddr;
			}
			/**
			 * ?int->ip??
			 * @param ipInt
			 * @return String
			 */
			public  String intToIp(int ipInt) {
				return new StringBuilder().append(((ipInt >> 24) & 0xff)).append('.')
						.append((ipInt >> 16) & 0xff).append('.').append(
								(ipInt >> 8) & 0xff).append('.').append((ipInt & 0xff))
						.toString();
			}

			int Sum1ByBin(int num)
			{
				int sum = 0;
				while (num != 0)
				{
					if(num %2 ==1)
					{
						sum ++;
					}
					num/=2;
				}
				return sum;
			}



			public int getNetworkPrefixLength(String mask){
				if (null == mask || "".equals(mask)) {
					return 0;
				}

				byte[] ret = new byte[4];
				int[] retint = new int[4];

				try {
					ret = InetAddress.getByName(mask).getAddress();
				} catch (Exception e) {
					throw new IllegalArgumentException(mask + " is invalid IP");
				}

				int netMaskInt = ((ret[0] & 0xff)<<24)|((ret[1] & 0xff)<<16)|((ret[2] & 0xff)<<8)|((ret[3] & 0xff)<<24);

				retint[0] = ret[0] & 0xff;
				retint[1] = ret[1] & 0xff;
				retint[2] = ret[2] & 0xff;
				retint[3] = ret[3] & 0xff;
				return Sum1ByBin(retint[0]) + Sum1ByBin(retint[1])+Sum1ByBin(retint[2])+Sum1ByBin(retint[3]);

			}	


			String MaskStrArr[] =
			{"128.0.0.0",		"192.0.0.0",		"224.0.0.0",		"240.0.0.0",	"248.0.0.0",			"252.0.0.0",	"254.0.0.0",	"255.0.0.0",
			"255.128.0.0",		"255.192.0.0",		"255.224.0.0",		"255.240.0.0",	"255.248.0.0",			"255.252.0.0",	"255.254.0.0",	"255.255.0.0",
			"255.255.128.0",	"255.255.192.0",	"255.255.224.0",	"255.255.240.0",	"255.255.248.0",	"255.255.252.0",	"255.255.254.0",	"255.255.255.0",
			"255.255.255.128",	"255.255.255.192",	"255.255.255.224",	"255.255.255.240","255.255.255.248",	"255.255.255.252",	"255.255.255.254",	"255.255.255.255"};

			public int mask2PrefixLen (String mask){
				int i;
				for(i=0;i<MaskStrArr.length;i++)
				{
					if(mask.equals(MaskStrArr[i]))
							break;
				}

				return i+1;				
			}	
			


			public  String prefixLen2Mask (int prefixLen){
				if(prefixLen<1 || prefixLen>32)
					prefixLen = 32;
				return MaskStrArr[prefixLen-1];
			}	



			
			/**
			 * ?192.168.1.1/24 ???int????
			 * @param ipAndMask
			 * @return int[]
			 */
			public int[] getIPIntScope(String ipAndMask) {
				String[] ipArr = ipAndMask.split("/");
				if (ipArr.length != 2) {
					throw new IllegalArgumentException("invalid ipAndMask with: "
							+ ipAndMask);
				}
				int netMask = Integer.valueOf(ipArr[1].trim());
				if (netMask < 0 || netMask > 31) {
					throw new IllegalArgumentException("invalid ipAndMask with: "
							+ ipAndMask);
				}
				int ipInt = ipToInt(ipArr[0]);
				int netIP = ipInt & (0xFFFFFFFF << (32 - netMask));
				int hostScope = (0xFFFFFFFF >>> netMask);
				return new int[] { netIP, netIP + hostScope };
			}
			/**
			 * ?192.168.1.1/24 ???IP????
			 * @param ipAndMask
			 * @return String[]
			 */
			public String[] getIPAddrScope(String ipAndMask) {
				int[] ipIntArr = getIPIntScope(ipAndMask);
				return new String[] { intToIp(ipIntArr[0]),
						intToIp(ipIntArr[0]) };
			}
			/**
			 * ??IP ????(192.168.1.1 255.255.255.0)???IP?
			 * @param ipAddr ipAddr
			 * @param mask mask
			 * @return int[]
			 */
			public int[] getIPIntScope(String ipAddr, String mask) {
				int ipInt;
				int netMaskInt = 0, ipcount = 0;
				try {
					ipInt = ipToInt(ipAddr);
					if (null == mask || "".equals(mask)) {
						return new int[] { ipInt, ipInt };
					}
					netMaskInt = ipToInt(mask);
					ipcount = ipToInt("255.255.255.255") - netMaskInt;
					int netIP = ipInt & netMaskInt;
					int hostScope = netIP + ipcount;
					return new int[] { netIP, hostScope };
				} catch (Exception e) {
					throw new IllegalArgumentException("invalid ip scope express  ip:"
							+ ipAddr + "  mask:" + mask);
				}
			}
			/**
			 * ??IP ????(192.168.1.1 255.255.255.0)???IP?
			 * @param ipAddr ipAddr
			 * @param mask mask
			 * @return String[]
			 */
			public String[] getIPStrScope(String ipAddr, String mask) {
				int[] ipIntArr = getIPIntScope(ipAddr, mask);
				return new String[] { intToIp(ipIntArr[0]),
						intToIp(ipIntArr[0]) };
			}



	}


	
}
	
