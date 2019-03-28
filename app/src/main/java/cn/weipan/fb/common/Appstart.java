package cn.weipan.fb.common;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.yanzhenjie.permission.AndPermission;

import cn.jpush.android.api.JPushInterface;
import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseNoLoginActivity;
import cn.weipan.fb.act.GestureLoginActivity;
import cn.weipan.fb.act.LoginNewActivity;
import cn.weipan.fb.act.WelcomeActivity;
import cn.weipan.fb.constact.ACache;
import cn.weipan.fb.constact.Constant;
import cn.weipan.fb.utils.SharedPre;

public class Appstart extends BaseNoLoginActivity {
    private static final String SHARE_APP_TAG = "isfirst";
    AppContext appContext;
    private View view;
    private ACache aCache;
    private String gesturePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = (AppContext) getApplication();
        // 不显示系统的标题栏，保证windowBackground和界面activity_main的大小一样，显示在屏幕不会有错位（去掉这一行试试就知道效果了）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view = View.inflate(this, R.layout.app_start, null);
        setContentView(view);
        AndPermission.with(Appstart.this)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .permission(Manifest.permission.READ_PHONE_STATE)
                .send();
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(2000);
        aCache = ACache.get(Appstart.this);
        gesturePassword = aCache.getAsString(Constant.GESTURE_PASSWORD);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        Intent intent = null;
                        SharedPreferences setting = getSharedPreferences(SHARE_APP_TAG, 0);
                        Boolean user_first = setting.getBoolean("FIRST", true);
                        if (user_first) {//第一次
                            setting.edit().putBoolean("FIRST", false).commit();
                            intent = new Intent(Appstart.this, WelcomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (gesturePassword == null || "".equals(gesturePassword)) {
                                intent = new Intent(Appstart.this, LoginNewActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                SharedPre shared = new SharedPre(Appstart.this);
                                String password = shared.getPassword();
                                if (TextUtils.isEmpty(password)) {
                                    intent = new Intent(Appstart.this, LoginNewActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    intent = new Intent(Appstart.this, GestureLoginActivity.class);
                                    intent.putExtra("Activity", "Appstart");
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                }

        );
        view.startAnimation(scaleAnimation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(appContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        JPushInterface.onPause(appContext);
    }
}
