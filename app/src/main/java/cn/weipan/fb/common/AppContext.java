package cn.weipan.fb.common;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cn.jpush.android.api.JPushInterface;

/*
* Application 应用程序
* */
public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ZXingLibrary.initDisplayOpinion(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JPushInterface.init(getApplicationContext());
        //设置接收消息的最大条数
//        JPushInterface.setLatestNotificationNumber(getApplicationContext(), Integer.MAX_VALUE);

//        if (Build.MANUFACTURER.contains("basewin")) {
//            LogUtil.closeLog();
//            ServiceManager.getInstence().init(getApplicationContext());
//        }

    }

    public String getDeviceId() {
        String id = "0";
        for (int i = 0; i < 15; i++) {
            String id2 = getProperty("user.deviceid");
            if (id != null && !id.equals("")) {
                id = id2;
                break;
            }
        }
        return id;
    }

    public void setDeviceId(String Id) {
        setProperty("user.deviceid", Id);
    }

    public String getRealName() {
        return this.getProperty("user.realname");
    }

    public void setRealName(String realname) {
        setProperty("user.realname", realname);
    }

    public String getWorkKey() {
        return this.getProperty("user.key");
    }

    public void setWorkKey(String key) {
        setProperty("user.key", key);
    }

    public String getCashId() {
        return this.getProperty("user.cashid");
    }

    public void setCashId(String number) {
        setProperty("user.cashid", number);
    }

    public String getSiteId() {
        return this.getProperty("user.siteid");
    }

    public void setSiteId(String number) {
        setProperty("user.siteid", number);
    }

    public String getCashType() {
        return this.getProperty("user.cashtype");
    }

    public void setCashType(String cashtype) {
        setProperty("user.cashtype", cashtype);
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key) {
        return AppConfig.getAppConfig(this).get(key);
    }

}
