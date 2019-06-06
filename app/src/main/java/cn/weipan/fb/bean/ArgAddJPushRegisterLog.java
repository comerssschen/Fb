package cn.weipan.fb.bean;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;

/**
 * 作者：create by comersss on 2019/4/19 09:11
 * 邮箱：904359289@qq.com
 * 添加极光注册别名结果记录请求参数
 */
public class ArgAddJPushRegisterLog {
    //收银员编号
    private String cashId;
    //别名
    private int operateType = 1;
    //状态 （1 成功 0 失败）
    private int state = 0;
    //App操作系统（1： Android， 2： IOS）
    private int appSystem = 1;
    //状态码
    private String stateCode;

    private int appNameCode;
    //APP版本号
    private String appVersion = AppUtils.getAppVersionName();
    //手机品牌
    private String mobileBrand = DeviceUtils.getManufacturer();
    //手机型号
    private String mobileType = DeviceUtils.getModel();
    //手机系统版本
    private String mobileSystemVersion = DeviceUtils.getSDKVersionName();

    public ArgAddJPushRegisterLog() {
    }

    public ArgAddJPushRegisterLog(String stateCode) {
        this.stateCode = stateCode;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public String getCashId() {
        return cashId;
    }

    public void setCashId(String cashId) {
        this.cashId = cashId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAppSystem() {
        return appSystem;
    }

    public void setAppSystem(int appSystem) {
        this.appSystem = appSystem;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getMobileBrand() {
        return mobileBrand;
    }

    public void setMobileBrand(String mobileBrand) {
        this.mobileBrand = mobileBrand;
    }

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }

    public String getMobileSystemVersion() {
        return mobileSystemVersion;
    }

    public void setMobileSystemVersion(String mobileSystemVersion) {
        this.mobileSystemVersion = mobileSystemVersion;
    }

    public int getAppNameCode() {
        return appNameCode;
    }

    public void setAppNameCode(int appNameCode) {
        this.appNameCode = appNameCode;
    }
}
