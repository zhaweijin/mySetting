package com.hiveview.cloudtv.settings.util;

import java.util.List;

/**
 * 类名称：NetWorkDetectool
 * 类描述：
 * 创建人：huxing
 * 创建时间：2018/4/3 19:54
 */
public class DetectionResult {
    public boolean dnsResult;
    public boolean stabilityResult;
    public boolean speedResult;
    public boolean reserve1Result;
    public boolean reserve2Result;

    public  List<Integer> dnsStatus;
    public  List<Integer> stabilityStatus;
    public  List<Integer> speedStatus;
    public  List<Integer> reserve1Status;
    public  List<Integer> reserve2Status;

    public DetectionResult() {
        dnsResult = true;
        stabilityResult = true;
        speedResult = true;
        reserve1Result = true;
        reserve2Result = true;

        dnsStatus = null;
        stabilityStatus = null;
        speedStatus = null;
        reserve1Status = null;
        reserve2Status = null;
    }

    public void free() {
        dnsResult = true;
        stabilityResult = true;
        speedResult = true;
        reserve1Result = true;
        reserve2Result = true;

        dnsStatus = null;
        stabilityStatus = null;
        speedStatus = null;
        reserve1Status = null;
        reserve2Status = null;
    }
}
