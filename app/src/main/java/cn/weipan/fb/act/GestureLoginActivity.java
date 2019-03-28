package cn.weipan.fb.act;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.star.lockpattern.util.LockPatternUtil;
import com.star.lockpattern.widget.LockPatternView;

import java.util.List;

import cn.weipan.fb.R;
import cn.weipan.fb.common.UpdateManager;
import cn.weipan.fb.constact.ACache;
import cn.weipan.fb.constact.Constant;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.NetworkRequest;
import cn.weipan.fb.utils.SharedPre;
import cn.weipan.fb.utils.ToastUtils;

/**
 * 手势密码登录
 * Created by cc on 2016/7/27.
 * 邮箱：904359289@QQ.com.
 */
public class GestureLoginActivity extends BaseNoLoginActivity implements NetworkRequest.ReponseListener, View.OnClickListener {
    private LockPatternView lockPatternView;
    private TextView messageTv;
    private Button forgetGestureBtn;
    private ACache aCache;
    private static final long DELAYTIME = 600l;
    private byte[] gesturePassword;
    private String activity;
    private SharedPreferences.Editor editor;
    private SharedPreferences sp;
    private boolean flag = false;
    private SharedPre shared = null;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_login);
        this.init();
        Intent intent = getIntent();
        activity = intent.getStringExtra("Activity");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                UpdateManager manager = new UpdateManager(GestureLoginActivity.this);
                manager.checkUpdate();
                Looper.loop();
            }
        }).start();
    }

    //初始化界面
    private void init() {
        LinearLayout fanhui = (LinearLayout) findViewById(R.id.ll_fanhui);
        fanhui.setOnClickListener(this);
        TextView header = (TextView) findViewById(R.id.head_view_title);
        header.setText("手势密码");
        sp = getSharedPreferences("userInfo", 0);
        editor = sp.edit();
        lockPatternView = (LockPatternView) findViewById(R.id.lockPatternView);
        messageTv = (TextView) findViewById(R.id.messageTv);
        forgetGestureBtn = (Button) findViewById(R.id.forgetGestureBtn);
        aCache = ACache.get(GestureLoginActivity.this);
        //得到当前用户的手势密码
        gesturePassword = aCache.getAsBinary(Constant.GESTURE_PASSWORD);
        lockPatternView.setOnPatternListener(patternListener);
        updateStatus(Status.DEFAULT);
        //忘记密码
        forgetGestureBtn = (Button) findViewById(R.id.forgetGestureBtn);
        forgetGestureBtn.setOnClickListener(this);

        loadingDialog = new LoadingDialog(GestureLoginActivity.this, "登录中...");

    }

    //手势密码输入次数记录
    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
        }

        int i = 0;

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {

            if (pattern != null) {
                i++;
                if (LockPatternUtil.checkPattern(pattern, gesturePassword)) {
                    updateStatus(Status.CORRECT);
                } else {
                    if (i == 1) {
                        updateStatus(Status.ERROR1);
                    }
                    if (i == 2) {
                        updateStatus(Status.ERROR2);
                    }
                    if (i == 3) {
                        updateStatus(Status.ERROR3);
                    }
                    if (i == 4) {
                        updateStatus(Status.ERROR4);
                    }
                    if (i > 4) {
                        updateStatus(Status.ERROR5);
                        AlertDialog.Builder builder = new AlertDialog.Builder(GestureLoginActivity.this);
                        builder.setTitle("手势验证失败");
                        builder.setMessage("请通过登录密码验证身份");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (activity.equals("MainActivity")) {
                                    startActivity(new Intent(GestureLoginActivity.this, LoginPWDActivity.class));
                                    finish();
                                } else if (activity.equals("ShouShiMiMa")) {
                                    startActivity(new Intent(GestureLoginActivity.this, LoginPWDActivity.class));
                                    finish();
                                } else if (activity.equals("Appstart")) {
//                                    sp = getSharedPreferences("userInfo", 0);
//                                    editor = sp.edit();
//                                    editor.putBoolean("isGesture", false);
//                                    editor.commit();
//                                    aCache.put(Constant.GESTURE_PASSWORD, "");
//                                    List<LockPatternView.Cell> mChosenPattern = null;
                                    SharedPre shared = new SharedPre(GestureLoginActivity.this);
                                    shared.RememberingPassword("");
                                    startActivity(new Intent(GestureLoginActivity.this, LoginNewActivity.class));
                                    finish();
                                } else if (activity.equals("XiuGaiMiMa")) {
                                    startActivity(new Intent(GestureLoginActivity.this, LoginPWDActivity.class));
                                    finish();
                                }
                                dialog.dismiss();
                            }
                        });
                        Dialog noticeDialog = builder.create();
                        noticeDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
                        noticeDialog.show();
                    }
                }
            }
        }
    };

    /**
     * 更新状态
     *
     * @param status
     */
    private void updateStatus(Status status) {
        messageTv.setText(status.strId);
        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势验证成功（去登录界面）
     */
    private void loginGestureSuccess() {
        if (activity.equals("MainActivity")) {
            //清除手势密码，并设置下面两个不能点
            editor.putBoolean("isGesture", false);
            editor.commit();
            finish();
        } else if (activity.equals("Appstart")) {
            loadingDialog.show();
            login();

        } else if (activity.equals("ShouShiMiMa")) {
            editor.putBoolean("isGesture", false);
            editor.commit();
            //清除手势密码，并设置下面两个不能点
            aCache.put(Constant.GESTURE_PASSWORD, "");
            List<LockPatternView.Cell> mChosenPattern = null;
            finish();
        } else if (activity.equals("XiuGaiMiMa")) {
            startActivity(new Intent(GestureLoginActivity.this, CreateGestureActivity.class));
            finish();
        }
    }

    /**
     * 忘记手势密码（去手势密码设置界面）
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgetGestureBtn:
                if (activity.equals("MainActivity")) {
                    startActivity(new Intent(GestureLoginActivity.this, LoginPWDActivity.class));
                    finish();
                } else if (activity.equals("ShouShiMiMa")) {
                    startActivity(new Intent(GestureLoginActivity.this, LoginPWDActivity.class));
                    finish();
                } else if (activity.equals("Appstart")) {
//                    sp = getSharedPreferences("userInfo", 0);
//                    editor = sp.edit();
//                    editor.putBoolean("isGesture", false);
//                    editor.commit();
//                    aCache.put(Constant.GESTURE_PASSWORD, "");
//                    List<LockPatternView.Cell> mChosenPattern = null;
                    SharedPre shared = new SharedPre(this);
                    shared.RememberingPassword("");
                    startActivity(new Intent(GestureLoginActivity.this, LoginNewActivity.class));
                    finish();
                } else if (activity.equals("XiuGaiMiMa")) {
                    startActivity(new Intent(GestureLoginActivity.this, LoginPWDActivity.class));
                    finish();
                }
                break;
            case R.id.ll_fanhui:
                if (activity.equals("Appstart")) {
                    SharedPre shared = new SharedPre(this);
                    shared.RememberingPassword("");
                    startActivity(new Intent(GestureLoginActivity.this, LoginNewActivity.class));
                    finish();
                } else {
                    finish();
                }
                finish();
                break;
            default:
                break;

        }
    }

    //返回按钮
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (activity.equals("Appstart")) {
            SharedPre shared = new SharedPre(this);
            shared.RememberingPassword("");
            startActivity(new Intent(GestureLoginActivity.this, LoginNewActivity.class));
            finish();
        } else {
            finish();
        }
    }

    //设置手势轨迹样式
    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.grey_a5a5a5),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.red_f4333c),
        ERROR1(R.string.gesture_error1, R.color.red_f4333c),
        ERROR2(R.string.gesture_error2, R.color.red_f4333c),
        ERROR3(R.string.gesture_error3, R.color.red_f4333c),
        ERROR4(R.string.gesture_error4, R.color.red_f4333c),
        ERROR5(R.string.gesture_error5, R.color.red_f4333c),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.grey_a5a5a5);

        Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }

        private int strId;
        private int colorId;
    }

    //登录
    private void login() {
        if (flag) {
            return;
        }
        flag = true;
        TelephonyManager tm = (TelephonyManager) this
                .getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();

        shared = new SharedPre(this);
        String username = shared.getUsername();
        String password = shared.getPassword();
        // 参数说明： {sign|0|0|17|登陆id|密码|imei|}
        String sendData = "{app|" + "0" + "|" + "0" + "|" + "17" + "|"
                + username + "|"
                + password + "|"
                + imei + "|" + "}";
        NetworkRequest mLoginRequest = new NetworkRequest(sendData);
        new Thread(mLoginRequest).start();
        mLoginRequest.setListener(GestureLoginActivity.this);
//        flag = true;
    }

    private void setStatus(String result) {
        loadingDialog.dismiss();
        Log.i("test", "result = " + result);
        String[] arr = null;
        if (result != null) {
            arr = result.replace("{", "").replace("}", "").split("\\|");
            if (arr[0].equals("0")) {
                flag = true;
                Intent intent = new Intent(GestureLoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                appContext.setRealName(arr[2]);
                appContext.setDeviceId(arr[3]);
                appContext.setCashId(arr[4]);
                appContext.setSiteId(arr[5]);
                appContext.setWorkKey(arr[6]);
                appContext.setCashType(arr[9]);
                Constant.isTuiKuan = Boolean.valueOf(arr[10]);
            } else {
                flag = false;
                ToastUtils.showToast(GestureLoginActivity.this, arr[1]);
                startActivity(new Intent(GestureLoginActivity.this, LoginNewActivity.class));
                finish();
            }
        } else {
            ToastUtils.showToast(GestureLoginActivity.this, "网络连接异常，请重试");
        }


    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            setStatus(result);
            super.handleMessage(msg);
        }
    };

    @Override
    public void onResult(String result) {
        Message message = new Message();
        message.obj = result;
        mHandler.sendMessage(message);
    }
}
