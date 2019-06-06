package cn.weipan.fb.common;

import android.app.Application;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
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
        JPushInterface.setDebugMode(true);// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);// 初始化 JPush
        CrashReport.initCrashReport(getApplicationContext(), "e6bfb33b3f", false);//上线时改成false
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);

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
