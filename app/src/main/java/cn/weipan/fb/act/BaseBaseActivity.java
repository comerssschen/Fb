package cn.weipan.fb.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import cn.weipan.fb.R;
import cn.weipan.fb.common.AppContext;
import cn.weipan.fb.common.AppManager;
import cn.weipan.fb.utils.SysInfoUtils;

/**
 * 基类
 * Created by cc on 2016/11/9.
 * 邮箱：904359289@QQ.com.
 */
public class BaseBaseActivity extends AppCompatActivity {
    public AppContext appContext;
    private Boolean isServiceRunning = false;
    private FinishReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        appContext = (AppContext) getApplication();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("finishAll");
        receiver = new FinishReceiver();
        registerReceiver(receiver, intentFilter);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置只能竖屏
//        if (TextUtils.isEmpty(appContext.getDeviceId())) {
//            JPushInterface.stopPush(getApplicationContext());
//        }
    }

    protected void onStart() {
        super.onStart();
        if (isServiceRunning) {
            isServiceRunning = false;
        } else {
        }
    }

    //关闭页面的广播
    class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (SysInfoUtils.isServiceRunning(this)) {
            isServiceRunning = true;
        } else {
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void finishs() {
        super.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        finish();
    }

    /**
     * 点击输入框外部，隐藏键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        AppManager.getAppManager().finishActivity(this);
    }

}