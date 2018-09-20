package com.hiveview.cloudtv.settings.util;

import android.content.Context;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：NetWorkDetectool
 * 类描述：大麦apk稳定性检测基类
 * 创建人：huxing
 * 创建时间：2018/4/3 18:36
 */
public class DomyAppCheck {
    private Context mContext;
    protected static int moduleId;
    protected ArrayList<String> moduleNameList = new ArrayList<String>();

    private final static int DOMY_HTTP_DNS_CHECK = 0x1000;
    private final static int DOMY_HTTP_STABILITY_CHECK = 0x2000;
    private final static int DOMY_HTTP_SPEED_CHECK = 0x3000;
    private final static int DOMY_HTTP_RESERVE1_CHECK = 0x4000;
    private final static int DOMY_HTTP_RESERVE2_CHECK = 0x5000;

    public DomyAppCheck(Context context, int id) {
        mContext = context;
        moduleId = id;
    }
    
     /**
       * 函数名称：makeDNsErrorCode
       * 函数说明：生成dns检测的错误码
       * 参数说明：错误码id
       * 返回值说明：dns检测错误码id
       * 作者：胡星
       * 日期：2018/4/10 16:23
       */
     public static int makeDNsErrorCode(int code) {
        return moduleId + DOMY_HTTP_DNS_CHECK + code;
    }
    
     /**
       * 函数名称：makeStabilityErrorCode
       * 函数说明：生成接口稳定性检测的错误码
       * 参数说明：  错误码id
       * 返回值说明：接口稳定性检测错误码
       * 作者：胡星
       * 日期：2018/4/10 16:23
       */
    public static int makeStabilityErrorCode(int code) {
        return moduleId + DOMY_HTTP_STABILITY_CHECK + code;
    }
    
     /**
       * 函数名称：makeSpeedErrorCode
       * 函数说明：生成速度检测的错误码
       * 参数说明：  错误码id
       * 返回值说明：速度检测错误码
       * 作者：胡星
       * 日期：2018/4/10 16:23
       */
     public static int makeSpeedErrorCode(int code) {
        return moduleId + DOMY_HTTP_SPEED_CHECK + code;
    }
    
     /**
       * 函数名称：makeReserve1ErrorCode
       * 函数说明：生成保留检测1的错误码
       * 参数说明： 错误码id
       * 返回值说明：最终检测错误码
       * 作者：胡星
       * 日期：2018/4/10 16:23
       */
     public static int makeReserve1ErrorCode(int code) {
        return moduleId + DOMY_HTTP_RESERVE1_CHECK + code;
    }
    
     /**
       * 函数名称：makeeReserve2ErrorCode
       * 函数说明：生成保留检测2的错误码
       * 参数说明：  错误码id
       * 返回值说明：最终检测错误码
       * 作者：胡星
       * 日期：2018/4/10 16:23
       */
     public static int makeeReserve2ErrorCode(int code) {
        return moduleId + DOMY_HTTP_RESERVE2_CHECK + code;
    }

    /**
     * 函数名称：getModuleId
     * 函数说明：获取当前模块的身份id
     * 参数说明：  无
     * 返回值说明：模块id，类型:int
     * 作者：胡星
     * 日期：2018/4/9 16:45
     */
    public int getModuleId() {
        return moduleId;
    }

    /**
     * 函数名称：setDetectorModuleName
     * 函数说明：设置模块所需检测功能的名称
     * 参数说明：  list<String>类型， size必须等于5，5个功能的名称，没有的功能默认填写“保留”
     * 返回值说明：无
     * 作者：胡星
     * 日期：2018/4/10 16:10
     */
    public void setDetectorModuleName(List<String> list) {
        if(list !=null) {
            if(list.size() != 5) {
                return;
            }

            for (int i = 0; i < list.size(); i++) {
                moduleNameList.add(list.get(i));
            }
        }
    }

     /**
       * 函数名称：domyAppDNSCheck
       * 函数说明：检测大麦apk中使用的接口域名在当地dns解析是否正常
       * 参数说明： 无
       * 返回值说明：0:表示检测正常，非0:表示检测失败
       * 作者：胡星
       * 日期：2018/4/9 16:45
       */
    public List<Integer> domyAppDNSCheck() {
        return null;
    }
    
     /**
       * 函数名称：domyAppStabilityCheck
       * 函数说明：检测大麦apk中使用的接口在当地请求稳定性
       * 参数说明：  无
       * 返回值说明：0:表示检测正常，非0:表示检测失败
       * 作者：胡星
       * 日期：2018/4/9 16:45
       */
    public List<Integer> domyAppStabilityCheck() {
        return null;
    }
    
     /**
       * 函数名称：domyAppSpeedCheck
       * 函数说明：检测大麦播放器类型apk在用户网络环境中的流下载速度
       * 参数说明：  无
       * 返回值说明：0:表示检测正常，非0:表示检测异常
       * 作者：胡星
       * 日期：2018/4/9 16:45
       */
    public List<Integer> domyAppSpeedCheck() {
        return null;
    }
    
     /**
       * 函数名称：domyAppReserve1Func
       * 函数说明：保留检测功能1，用户自定义检测功能项
       * 参数说明：  无
       * 返回值说明：0:表示检测正常，非0:表示检测异常
       * 作者：胡星
       * 日期：2018/4/9 16:46
       */
    public List<Integer> domyAppReserve1Func() {
        return null;
    }
    
     /**
       * 函数名称：domyAppReserve2Func
       * 函数说明：保留检测功能2，用户自定义检测功能项
       * 参数说明：  无
       * 返回值说明：0:表示检测正常，非0:表示检测异常
       * 作者：胡星
       * 日期：2018/4/9 16:46
       */
    public List<Integer> domyAppReserve2Func() {
        return null;
    }
    
     /**
       * 函数名称：domyAppErrorMessage
       * 函数说明：获取当前模块的报错信息的错误报告
       * 参数说明：  错误结果集DetectionResult类
       * 返回值说明：错误报告的String字符串
       * 作者：胡星
       * 日期：2018/4/9 16:46
       */
     public String domyAppErrorMessage(DetectionResult result) {
         List<Integer> dnsStatus = result.dnsStatus;
         List<Integer> speedStatus = result.speedStatus;
         List<Integer> stabilityStatus = result.stabilityStatus;
         List<Integer> reserve1Status = result.reserve1Status;
         List<Integer> reserve2Status = result.reserve2Status;

         ArrayList<String> listMsgs = new ArrayList<String>();
         String retMsg = "";

         if(dnsStatus != null) {
             if (dnsStatus.size() != 0) {
                 StringBuffer tmpbuffer = new StringBuffer();
                 tmpbuffer.append(moduleNameList.get(0)+"失败，错误码:");
                 for (int i = 0; i < dnsStatus.size(); i++) {
                     tmpbuffer.append(dnsStatus.get(i));
                     tmpbuffer.append(" ");
                 }
                 listMsgs.add(tmpbuffer.toString());
             }
         }

         if(stabilityStatus != null) {
             if (stabilityStatus.size() != 0) {
                 StringBuffer tmpbuffer = new StringBuffer();
                 tmpbuffer.append(moduleNameList.get(1)+"失败，错误码:");
                 for (int i = 0; i < stabilityStatus.size(); i++) {
                     tmpbuffer.append(stabilityStatus.get(i));
                     tmpbuffer.append(" ");
                 }
                 listMsgs.add(tmpbuffer.toString());
             }
         }

         if(speedStatus != null) {
             if (speedStatus.size() != 0) {
                 StringBuffer tmpbuffer = new StringBuffer();
                 tmpbuffer.append(moduleNameList.get(2)+"失败，错误码:");
                 for (int i = 0; i < speedStatus.size(); i++) {
                     tmpbuffer.append(speedStatus.get(i));
                     tmpbuffer.append(" ");
                 }
                 listMsgs.add(tmpbuffer.toString());
             }
         }

         if(reserve1Status != null) {
             if (reserve1Status.size() != 0) {
                 StringBuffer tmpbuffer = new StringBuffer();
                 tmpbuffer.append(moduleNameList.get(3)+"失败，错误码:");
                 for (int i = 0; i < reserve1Status.size(); i++) {
                     tmpbuffer.append(reserve1Status.get(i));
                     tmpbuffer.append(" ");
                 }
                 listMsgs.add(tmpbuffer.toString());
             }
         }

         if(reserve2Status != null) {
             if (reserve2Status.size() != 0) {
                 StringBuffer tmpbuffer = new StringBuffer();
                 tmpbuffer.append(moduleNameList.get(4)+"失败，错误码:");
                 for (int i = 0; i < reserve2Status.size(); i++) {
                     tmpbuffer.append(reserve2Status.get(i));
                     tmpbuffer.append(" ");
                 }
                 listMsgs.add(tmpbuffer.toString());
             }
         }
         retMsg = formatMessae(listMsgs);

         return retMsg;
     }
    
     /**
       * 函数名称：formatMessae
       * 函数说明：格式化错误报告，方便dialog显示
       * 参数说明：  错误报告的arraylist<String>类型数组
       * 返回值说明：格式化后的错误报告String类型
       * 作者：胡星
       * 日期：2018/4/9 16:46
       */
    public String formatMessae(List<String> listMsgs) {
        if(listMsgs == null ||
                listMsgs.size() == 0) {
            return "";
        }
        int len = listMsgs.size();
        StringBuffer strbuffer = new StringBuffer();
        for(int i=0; i<len; i++) {
            strbuffer.append("  ");
            strbuffer.append(listMsgs.get(i));
            strbuffer.append("\r\n");
        }
        return strbuffer.toString();
    }
    
     /**
       * 函数名称：DnsCheckTool
       * 函数说明：用于dns服务器检测的工具类
       * 参数说明：  
       * 返回值说明：
       * 作者：胡星
       * 日期：2018/4/10 15:05
       */
    public class DnsCheckTool {
        private Context mContext;
        private final static String TAG = "DnsCheckTool";

        public DnsCheckTool(Context context) {
            mContext = context;
        }

        public static final int DNS_GET_DNS_IP_FAIL = 0x1001;     //表示域名通过当地DNS获取IP失败
        public static final int DNS_GET_IP_INCORRECT = 0x1002;    //表示ip比较的结果不对
        
          /**
            * 函数名称：dnsDetector
            * 函数说明：通过当地dns服务器获取指定域名url对应的物理ip并与ipCheck进行比较
            * 参数说明：  url：String类型，表示需要检测的域名url
            *             ipCheck: String类型，表示比较的IP
            * 返回值说明：0表示成功， 非0表示失败
            * 作者：胡星
            * 日期：2018/4/10 15:08
            */
        public int dnsDetector(String url, String ipCheck) {
            String ip = getIP(url);
            Log.e(TAG, "ip = " + ip);
            if (ip == null) {
                return DNS_GET_DNS_IP_FAIL;
            }

            if (!ip.equals(ipCheck)) {

                return DNS_GET_IP_INCORRECT;
            }
            return 0;
        }
        
         /**
           * 函数名称：getDomainNameInUrl
           * 函数说明：通过接口url获取对应的域名
           * 参数说明：  Sring类型，传入的接口url
           * 返回值说明：String类型，对应域名
           * 作者：胡星
           * 日期：2018/4/10 15:08
           */
        public String getDomainNameInUrl(String url) {
            String domainName = null;

            if (url == null)
                return null;

            String[] tmpbuff = url.split("/");

            for (int i = 0; i < tmpbuff.length; i++) {
                Log.e(TAG, "----tmpbuff[" + i + "] = " + tmpbuff[i]);
            }

            if (tmpbuff.length >= 3) {
                domainName = tmpbuff[2];
            }

            return domainName;
        }
        
         /**
           * 函数名称：getIP
           * 函数说明：通过域名获取ip
           * 参数说明： 传入的域名
           * 返回值说明：对应的ip字符传
           * 作者：胡星
           * 日期：2018/4/10 15:18
           */
        private String getIP(String name) {
            InetAddress address = null;
            try {
                address = InetAddress.getByName(name);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "get ip error!");
                return null;
            }
            return address.getHostAddress().toString();
        }
    }
}
