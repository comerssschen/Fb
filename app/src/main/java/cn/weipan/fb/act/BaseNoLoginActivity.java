package cn.weipan.fb.act;

import android.os.Bundle;

import cn.weipan.fb.common.AppContext;
import cn.weipan.fb.common.AppManager;

/*
 基内（不在登录状态的）
* Created by cc on 2016/7/27.
* 邮箱：904359289@QQ.com.
* */
public class BaseNoLoginActivity extends BaseBaseActivity {
    public AppContext appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        appContext = (AppContext) getApplication();
    }
}