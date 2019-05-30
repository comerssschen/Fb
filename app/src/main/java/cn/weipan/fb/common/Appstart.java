package cn.weipan.fb.common;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.PhoneUtils;

import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseNoLoginActivity;
import cn.weipan.fb.act.GuideActivity;
import cn.weipan.fb.act.LoginNewActivity;
import cn.weipan.fb.act.MainActivity;
import cn.weipan.fb.service.TagAliasOperatorHelper;
import cn.weipan.fb.utils.NetworkRequest;
import cn.weipan.fb.utils.SharedPre;
import cn.weipan.fb.utils.ToastUtils;

import static cn.weipan.fb.service.TagAliasOperatorHelper.ACTION_SET;
import static cn.weipan.fb.service.TagAliasOperatorHelper.sequence;

public class Appstart extends BaseNoLoginActivity implements NetworkRequest.ReponseListener {
    private static final String SHARE_APP_TAG = "isfirst";
    AppContext appContext;
    private View view;
    private SharedPreferences setting;
    private Intent intent;
    private boolean user_first;
    private String imei;
    private String alias;
    private boolean loginSucess;
    private SharedPre shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = (AppContext) getApplication();
        // 不显示系统的标题栏，保证windowBackground和界面activity_main的大小一样，显示在屏幕不会有错位（去掉这一行试试就知道效果了）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view = View.inflate(this, R.layout.app_start, null);
        setContentView(view);
        shared = new SharedPre(this);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1500);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        setting = getSharedPreferences(SHARE_APP_TAG, 0);
                        user_first = setting.getBoolean("FIRST", true);
                        if (!user_first && PermissionUtils.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            login();
                        }
                        Log.i("test", System.currentTimeMillis() + "onAnimationStart");
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        PermissionUtils.permission(PermissionConstants.STORAGE, PermissionConstants.PHONE, PermissionConstants.CAMERA).callback(new PermissionUtils.SimpleCallback() {
                            @Override
                            public void onGranted() {
                                if (user_first) {//第一次
                                    setting.edit().putBoolean("FIRST", false).commit();
                                    intent = new Intent(Appstart.this, GuideActivity.class);
                                } else {
                                    if (loginSucess) {
                                        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
                                        tagAliasBean.action = ACTION_SET;
                                        sequence++;
                                        tagAliasBean.alias = alias;
                                        tagAliasBean.isAliasAction = true;
                                        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
                                        intent = new Intent(Appstart.this, MainActivity.class);
                                    } else {
                                        intent = new Intent(Appstart.this, LoginNewActivity.class);
                                    }
                                }
                                Log.i("test", System.currentTimeMillis() + "onAnimationEnd");
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onDenied() {
                                Log.i("test", "onDenied");
                                ToastUtils.showToast(Appstart.this, "请检查权限");
                                finish();
                            }
                        }).request();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                }
        );
        view.startAnimation(scaleAnimation);
    }


    //登录
    private void login() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        imei = PhoneUtils.getIMEI();
        String username = shared.getUsername();
        String password = shared.getPassword();
        if (ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(password)) {
            return;
        }
        // 参数说明： {sign|0|0|17|登陆id|密码|imei|}
        String sendData = "{app|" + "0" + "|" + "0" + "|" + "17" + "|"
                + username + "|"
                + password + "|"
                + imei + "|" + "}";
        NetworkRequest mLoginRequest = new NetworkRequest(sendData);
        new Thread(mLoginRequest).start();
        mLoginRequest.setListener(Appstart.this);
    }

    @Override
    public void onResult(String result) {
        Log.i("test", "result = " + result);
        String[] arr = null;
        if (result != null) {
            arr = result.replace("{", "").replace("}", "").split("\\|");
            if (arr[0].equals("0")) {
                loginSucess = true;
                alias = arr[3];
                Log.i("test", alias + "----------alias-------------");
                appContext.setDeviceId(arr[3]);
                appContext.setRealName(arr[2]);
                appContext.setCashId(arr[4]);
                appContext.setSiteId(arr[5]);
                appContext.setWorkKey(arr[6]);
                appContext.setCashType(arr[9]);
                Constant.isTuiKuan = Boolean.valueOf(arr[10]);
            } else {
                loginSucess = false;
            }
        } else {
            loginSucess = false;
        }
    }

}
