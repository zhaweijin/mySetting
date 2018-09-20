package com.hiveview.cloudtv.settings.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import com.hiveview.cloudtv.settings.SettingsApplication;
import com.hiveview.cloudtv.settings.ethernet.S905EthernetManager;
import com.hiveview.cloudtv.settings.ethernet.NetConfigureInterface;
import com.hiveview.cloudtv.settings.util.Utils;
import com.hiveview.cloudtv.settings.wifi.WifiConfig;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * @author huxing E-mail: huxing@btte.net
 * @version 创建时间：2018年1月30日 下午4:08:05 类说明
 */
public class NetWorkCheck {
	private final static String TAG = "NetWorkCheck";
	private Context mContext;
	private NetConfigureInterface netCfg;
	public int networkType;
	public boolean isEthOK;
	public int ethMode;
	public boolean dhcpMode=true;
	private boolean isEthInsert;
	private final static String DNS_CHECK_IP = "64.77.228.226";
	private final static String DNS_CHECK_URL="pic.usa.domybox.com";
	private int dnshostsflag = 0;
	
	private WifiConfig wifiConfig;
	
	public static final int NETWORKCFG_CHECK_MASK = 0x00F;
	public static final int NETWORKCFG_CHECK_ETH_DISABLE = 0x001;
	public static final int NETWORKCFG_CHECK_GETIP_FAIL = 0x002;
	public static final int NETWORKCFG_CHECK_IPCONFIG_ERROR = 0x003;
	public static final int NETWORKCFG_CHECK_PING_ERROR = 0x004;
	public static final int NETWORKCFG_CHECK_IP_INVALID = 0x005;
	
	public static final int NETWORKDNS_CHECK_MASK = 0x0F0;
	public static final int NETWORKDNS_CHECK_INVALID = 0x010;
	public static final int NETWORKDNS_CHECK_INCORRECT = 0x020;
	
	public static final int NETWORKHOSTS_CHECK_MASK = 0xF00;
	public static final int NETWORKHOSTS_CHECK_ABNORMAL = 0x100;
	public static final int NETWORKHOSTS_CHECK_IOABNORMAL = 0x200;
	
	public static final int NETWORKCNT_CHECK_MASK = 0xF000;
	public static final int NETWORKCNT_CHECK_ETHNOLINK_ERR = 0x2000;
	public static final int NETWORKCNT_CHECK_ETH_DISABLE = 0x1000;
	
	public static final int NETWORKDHCP_CHECK_MANUAL = 0x10000;
	public static final int NETWORKDHCPT_CHECK_MASK = 0xF0000;
	
	public NetWorkCheck(Context context) {
		mContext = context;
		netCfg = (NetConfigureInterface) new S905EthernetManager(mContext);
		wifiConfig = new WifiConfig(mContext);
	}
	
	public int netConnectCheck() {
		int netState = 0;
		ConnectivityManager connectivity = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = connectivity.getActiveNetworkInfo();

		if (mNetworkInfo != null) {
			netState = 0;
			Log.e(TAG, "----------type = " + mNetworkInfo.getType());
			networkType = mNetworkInfo.getType();
		} else {
			Log.e(TAG, "--netConnectStatus-------network disconnect!!");
			networkType = -1;
			isEthInsert = netCfg.isEthernetAvailable();
			Log.e(TAG, "isEthInsert-------------= " + isEthInsert);
		
			if(!isEthInsert){
				return NETWORKCNT_CHECK_ETHNOLINK_ERR;   //以太网未插入
			}else {
				
			}
		}
		return netState;
	}

	public int netWorkConfigureCheck() {
		if (networkType == ConnectivityManager.TYPE_WIFI) {
           dhcpMode = wifiConfig.isDhcp();
           return ipCheck();
		} else {
			boolean isPPPOE = Utils.getActiveNetworkType(mContext) == SettingsApplication.TYPE_PPPOE ? true: false;
			if (!isPPPOE) { // ethernet
				ethMode = netCfg.getEthernetType();
				dhcpMode = netCfg.isEthernetDhcp();
				Log.v(TAG, "ethMode="+ethMode);
				if (ethMode == -1){
					dhcpMode=false;
					return NETWORKCFG_CHECK_ETH_DISABLE;
				}
				return ipCheck();
			}
		}
		return 0;
	}
	
	
	private int ipCheck(){
		String ip ="";
		String mask = "";
		String gateway = "";
		String dns = "";
		if (networkType == ConnectivityManager.TYPE_WIFI) {
			dhcpMode = wifiConfig.isDhcp();
			ip = wifiConfig.getIp();
			mask = wifiConfig.getNetmask();
			gateway = wifiConfig.getGateway();
			dns = wifiConfig.getDns();
		} else {
			boolean isPPPOE = Utils.getActiveNetworkType(mContext) == SettingsApplication.TYPE_PPPOE ? true: false;
			if (!isPPPOE) { // ethernet
				ip = netCfg.getEthernetIpAddress();
				mask = netCfg.getEthernetNetmask();
				gateway = netCfg.getEthernetGateway();
				dns = netCfg.getEthernetDns();
			}
		}
		

		String[] ipArray = ip.split("\\.");
		String[] maskArray = mask.split("\\.");
		String[] gatewayArray = gateway.split("\\.");
		String[] dnsArray = dns.split("\\.");

		int[] ipbuf = new int[4];
		int[] maskbuf = new int[4];
		int[] gatewaybuf = new int[4];
		int[] dnsbuf = new int[4];

		int maskflag = 0;
		
		Log.i(TAG, "---ip = " +ip);
		Log.i(TAG, "---mask = " +mask);
		Log.i(TAG, "---gateway = " +gateway);
		Log.i(TAG, "---dns = " +dns);
		Log.i(TAG, "---ipArray[0] = " +ipArray[0]);
		Log.i(TAG, "---len1 = " +ipArray.length + ", len2= "+ maskArray.length+", len3= "+gatewayArray.length + ", len4="+dnsArray.length);
		if (!((ipArray != null && maskArray != null && gatewayArray != null && dnsArray != null)
				&& (ipArray.length == 4 && maskArray.length == 4 && gatewayArray.length == 4
						&& dnsArray.length == 4))) {
			return NETWORKCFG_CHECK_GETIP_FAIL;
		} else {
			for (int i = 0; i < 4; i++) {
				ipbuf[i] = Integer.parseInt(ipArray[i]);
				maskbuf[i] = Integer.parseInt(maskArray[i]);
				gatewaybuf[i] = Integer.parseInt(gatewayArray[i]);
				dnsbuf[i] = Integer.parseInt(dnsArray[i]);

				if ((ipbuf[i] & maskbuf[i]) != (gatewaybuf[i] & maskbuf[i])) {
					maskflag = 1;
				}
			}
			
			if(ipbuf[0] == gatewaybuf[0] &&
					ipbuf[1] == gatewaybuf[1] && 
					ipbuf[2] == gatewaybuf[2] &&
					ipbuf[3] == gatewaybuf[3]) {
				return NETWORKCFG_CHECK_IP_INVALID;
			}
			
			if (maskflag == 1) {
				return NETWORKCFG_CHECK_IPCONFIG_ERROR;
			}
			
			int pingstatus = isPingSuccess(5,gateway);
			Log.e(TAG, "pingstatus------------"+ pingstatus);
			if(pingstatus !=0) {
				return NETWORKCFG_CHECK_PING_ERROR;
			}
		}
		return 0;
	}
	
	
	public int netWorkDNSCheck() {
		String ip = getIP(DNS_CHECK_URL);
		Log.e(TAG,"ip = " + ip);
		if(ip == null) {
			return NETWORKDNS_CHECK_INVALID;
		}
		
		if(!ip.equals(DNS_CHECK_IP)) {
			return NETWORKDNS_CHECK_INCORRECT;
		}
		return 0;
	}
	
	public int netWorkHostsCheck() {
		int flag = 0;
		dnshostsflag = 0;
		try {
			File fbFile = new File("/system/etc/hosts");
            FileInputStream fis=new FileInputStream(fbFile);
            InputStreamReader is=new InputStreamReader(fis,"UTF-8");
            /*
            //fis.available()文件可用长度
            char input[]=new char[fis.available()];
            Log.e(TAG, "-----netWorkHostsCheck----222");
            is.read(input);
            */
            String temp=null;
            BufferedReader br = new BufferedReader(is);
            while((temp=br.readLine())!=null) {
            	Log.e(TAG, "host: " +temp);
            	if(!(temp.contains("127.0.0.1") || temp.contains("localhost"))) {
            		flag = NETWORKHOSTS_CHECK_ABNORMAL;
            		if(temp.contains(DNS_CHECK_URL))
            			dnshostsflag = 1;
            		break;
            	}
            }
            is.close();
            fis.close();
            return flag;
        }  catch(Exception e) {
        	e.printStackTrace();
            return NETWORKHOSTS_CHECK_IOABNORMAL;
        }
	}
	
	public final static int NETWORK_REPAIR_ETH_DISABLE = 0x1001;
	public final static int NETWORK_REPAIR_ETH_MANUL_DHCP = 0x1002;
	public final static int NETWORK_REPAIR_ETH_AUTO_DHCP = 0x1003;
	public final static int NETWORK_REPAIR_ETH_DNS = 0x1004;
	public final static int NETWORK_REPAIR_ETH_HOSTS= 0x1005;
	
	public int netWorkRepair(Context context, int data) {
		if (networkType == ConnectivityManager.TYPE_WIFI) {
			return wifiRepair(context, data);
		}else{
			boolean isPPPOE = Utils.getActiveNetworkType(mContext) == SettingsApplication.TYPE_PPPOE ? true: false;
			if (!isPPPOE) { // ethernet
				return ethernetRepair(context, data);
			}
		}
		return 0;
	}
	
	
	
	public int ethernetRepair(Context context, int data) {
		int cfgerror = data&NetWorkCheck.NETWORKCFG_CHECK_MASK;
		
		Log.e(TAG, "-----------ethMode = "+ ethMode + ", dhcpMode = " + dhcpMode);
		Log.e(TAG, "----------ethernet netWorkRepair-----1");
		cfgerror = data&NetWorkCheck.NETWORKCNT_CHECK_MASK;
		if(cfgerror == NetWorkCheck.NETWORKCNT_CHECK_ETH_DISABLE) {
			netCfg.setEthernetSwitch(true);
			isEthOK = netCfg.getEthernetStatus();
			if (!isEthOK)
				return NETWORK_REPAIR_ETH_DISABLE;
		}
		Log.e(TAG, "----------netWorkRepair-----2");
		cfgerror = data&NetWorkCheck.NETWORKCFG_CHECK_MASK;
		if(cfgerror == NetWorkCheck.NETWORKCFG_CHECK_GETIP_FAIL) {
			
			netCfg.setEthernetSwitch(true);
			isEthOK = netCfg.getEthernetStatus();
			if (!isEthOK)
				return NETWORK_REPAIR_ETH_DISABLE;
			
		} else if(cfgerror == NetWorkCheck.NETWORKCFG_CHECK_IPCONFIG_ERROR ||
				cfgerror == NetWorkCheck.NETWORKCFG_CHECK_IP_INVALID ||
				cfgerror == NetWorkCheck.NETWORKCFG_CHECK_PING_ERROR ) {
			if(ethMode == 1 && dhcpMode == false) {
				netCfg.setEthernetIpConfiguration(true, null, null, null, null);
				dhcpMode = netCfg.isEthernetDhcp();
				if (dhcpMode)
					return NETWORK_REPAIR_ETH_MANUL_DHCP;
			} else {
				return NETWORK_REPAIR_ETH_AUTO_DHCP;
			}
		}
		Log.e(TAG, "----------netWorkRepair-----3");
		Log.e(TAG, "----------DHCP-----data = " + data);
		cfgerror = data&NetWorkCheck.NETWORKDHCPT_CHECK_MASK;
		Log.e(TAG, "----------DHCP-----cfgerror = " + cfgerror);
		if(cfgerror == NetWorkCheck.NETWORKDHCP_CHECK_MANUAL) {
			Log.e(TAG, "----------DHCP-----1");
			Log.e(TAG, "----------DHCP-----ethMode = "+ ethMode + ", dhcpMode = " + dhcpMode);
			if(ethMode == 1 && dhcpMode == false) {
				netCfg.setEthernetIpConfiguration(true, null, null, null, null);
				Log.e(TAG, "----------DHCP-----2");
				dhcpMode = netCfg.isEthernetDhcp();
				Log.e(TAG, "----------DHCP-----dhcpMode = " +dhcpMode);
				if (dhcpMode)
					return NETWORK_REPAIR_ETH_MANUL_DHCP;
			}
		}
		Log.e(TAG, "----------netWorkRepair-----4");
		cfgerror = data&NetWorkCheck.NETWORKDNS_CHECK_MASK;
		if(cfgerror !=0) {
			Log.e(TAG, "----------DNS-----1");
			if(ethMode == 1 && dhcpMode == false) {
				Log.e(TAG, "----------DNS-----2");
				netCfg.setEthernetIpConfiguration(true, null, null, null, null);
				dhcpMode = netCfg.isEthernetDhcp();
				if (dhcpMode)
					return NETWORK_REPAIR_ETH_MANUL_DHCP;
			} else {
				if(dnshostsflag == 0)
					return NETWORK_REPAIR_ETH_DNS;
			}
		}
		
		cfgerror = data&NetWorkCheck.NETWORKHOSTS_CHECK_MASK;
		if(cfgerror !=0) {
				return NETWORK_REPAIR_ETH_HOSTS;
		}
		return 0;
	}
	
	
	public int wifiRepair(Context context, int data) {
		int cfgerror = data&NetWorkCheck.NETWORKCFG_CHECK_MASK;
	
		Log.e(TAG, "----------wifi netWorkRepair-----1");
		cfgerror = data&NetWorkCheck.NETWORKDHCPT_CHECK_MASK;
		Log.e(TAG, "----------DHCP-----cfgerror = " + cfgerror);
		if(cfgerror == NetWorkCheck.NETWORKDHCP_CHECK_MANUAL) {
			Log.e(TAG, "----------DHCP-----1");
			if(dhcpMode == false) {
				wifiConfig.resetDhcpWifiConfiguration();
				wifiConfig = new WifiConfig(mContext);
				dhcpMode = wifiConfig.isDhcp();
				Log.e(TAG, "----------DHCP-----dhcpMode = " +dhcpMode);
				if (!dhcpMode)
					return NETWORK_REPAIR_ETH_MANUL_DHCP;
			}
		}
		Log.e(TAG, "----------netWorkRepair-----4");
		cfgerror = data&NetWorkCheck.NETWORKDNS_CHECK_MASK;
		if(cfgerror !=0) {
			Log.e(TAG, "----------DNS-----1");
			if(dhcpMode == false) {
				Log.e(TAG, "----------DNS-----2");
				wifiConfig.resetDhcpWifiConfiguration();
				wifiConfig = new WifiConfig(mContext);
				dhcpMode = wifiConfig.isDhcp();
				if (!dhcpMode)
					return NETWORK_REPAIR_ETH_MANUL_DHCP;
			} else {
				if(dnshostsflag == 0)
					return NETWORK_REPAIR_ETH_DNS;
			}
		}
		
		cfgerror = data&NetWorkCheck.NETWORKHOSTS_CHECK_MASK;
		if(cfgerror !=0) {
				return NETWORK_REPAIR_ETH_HOSTS;
		}
		return 0;
	}
	
	private int isPingSuccess(int pingNum, String m_strForNetAddress) {
		StringBuffer tv_PingInfo = new StringBuffer();
		int result = 0;
		try {
			Log.e(TAG, "ping -c " + pingNum + " " + m_strForNetAddress);
			Process p = Runtime.getRuntime().exec("/system/bin/ping -c " + pingNum + " " + m_strForNetAddress); // 10.83.50.111
			// m_strForNetAddre
			int status = p.waitFor();
			
			if (status == 0) {
				result = 0;
			} else {
				result = 1;
				String lost = new String();
				String delay = new String();
				BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String str = new String();
				// 读出所有信息并显示
				while ((str = buf.readLine()) != null) {
					String tr = str + "\r\n";
					tv_PingInfo.append(str);
					
				}
				 Log.e(TAG, "ping error info:\r\n" + tv_PingInfo.toString());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			return -1;
		}
		
		return result;
	}
	
	private String getIP(String name) {
		InetAddress address = null;
		try {
			address = InetAddress.getByName(name);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG,"get ip error!");
			return null;
		}
		return address.getHostAddress().toString();
	}

}
