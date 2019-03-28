package cn.weipan.fb.act;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.permission.AndPermission;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.weipan.fb.R;
import cn.weipan.fb.common.AppManager;
import cn.weipan.fb.common.Appstart;
import cn.weipan.fb.common.UpdateManager;
import cn.weipan.fb.constact.ACache;
import cn.weipan.fb.constact.Constant;
import cn.weipan.fb.utils.DialogUtils;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.NetworkRequest;
import cn.weipan.fb.utils.SharedPre;
import cn.weipan.fb.utils.ToastUtils;


/**
 * 登录
 * Created by cc on 2016/7/22.
 * 邮箱：904359289@QQ.com.
 */
public class LoginNewActivity extends BaseNoLoginActivity implements NetworkRequest.ReponseListener, OnClickListener {
    @BindView(R.id.loginnew_edit_phone)
    EditText loginnewEditPhone;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.loginnew_edit_messageverify)
    EditText loginnewEditMessageverify;
    @BindView(R.id.login_nologin)
    TextView loginNologin;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.login_regist)
    TextView loginRegist;
    String imei;
    private SharedPre shared = null;
    private boolean flag;
    private boolean isLogin;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String alias;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loadingDialog = new LoadingDialog(LoginNewActivity.this, "登录中...");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            AndPermission.with(LoginNewActivity.this)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .send();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    UpdateManager manager = new UpdateManager(LoginNewActivity.this);
                    manager.checkUpdate();
                    Looper.loop();
                }
            }).start();
        }

        String activity = getIntent().getStringExtra("activity");
        if (TextUtils.equals(activity, "MainActivity")) {
            alias = "";
            jHandler.sendMessage(jHandler.obtainMessage(MSG_SET_ALIAS, alias));
        }
        initView();
    }

    //初始化界面
    private void initView() {
        sp = getSharedPreferences("userInfo", 0);
        editor = sp.edit();
        loginnewEditMessageverify.setTransformationMethod(PasswordTransformationMethod.getInstance());
        try {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AndPermission.with(LoginNewActivity.this)
                        .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .permission(Manifest.permission.READ_PHONE_STATE)
                        .send();
                return;
            }
            imei = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        shared = new SharedPre(this);
        loginnewEditPhone.setText(shared.getUsername());
        loginnewEditMessageverify.setText(shared.getPassword());

    }

    //登录
    private void login() {
        if (loginnewEditPhone.getText().toString().equals("")) {
            ToastUtils.showToast(LoginNewActivity.this, "账号不能为空");
        } else if (loginnewEditMessageverify.getText().toString()
                .equals("")) {
            ToastUtils.showToast(LoginNewActivity.this, "请输入密码");
        } else {
            loadingDialog.show();
            if (flag) {
                return;
            }

            flag = true;
            // 参数说明： {sign|0|0|17|登陆id|密码|imei|}
            String sendData = "{app|" + "0" + "|" + "0" + "|" + "17" + "|"
                    + loginnewEditPhone.getText().toString() + "|"
                    + loginnewEditMessageverify.getText().toString() + "|"
                    + imei + "|tag_login" + "}";
            NetworkRequest mLoginRequest = new NetworkRequest(sendData);
            new Thread(mLoginRequest).start();
            mLoginRequest.setListener(LoginNewActivity.this);
        }
    }

    //返回键
    @Override
    public void onBackPressed() {
        DialogUtils.customDialog(this, "", "取消", "确定", "确定要退出付吧吗？", new DialogUtils.DialogCallback() {

            @Override
            public void PositiveButton(int i) {
                switch (i) {
                    case -1:
                        // 取消
                        break;
                    case -2:
                        appContext.setDeviceId("");
                        finishs();
                        AppManager.getAppManager().AppExit(getBaseContext());
                        break;
                    default:
                        break;
                }
            }
        }, false, true);
    }

    //请求结果回调
    private void setStatus(String result) {
        loadingDialog.dismiss();
        Log.i("test", "result = " + result);
        String[] arr = null;
        if (result != null) {
            arr = result.replace("{", "").replace("}", "").split("\\|");
            if (arr[0].equals("0")) {
                flag = true;
                shared = new SharedPre(this);
                shared.getUsername();
                if (!TextUtils.equals(shared.getUsername(), loginnewEditPhone.getText().toString())) {

                    editor.putBoolean("isGesture", false);
                    editor.commit();
                    ACache aCache = ACache.get(LoginNewActivity.this);
                    aCache.put(Constant.GESTURE_PASSWORD, "");
                }

                alias = arr[3];
                Log.i("test", alias + "----------alias-------------");
                jHandler.sendMessage(jHandler.obtainMessage(MSG_SET_ALIAS, alias));
                isLogin = true;

                Intent intent = new Intent(LoginNewActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
//            { 0|CashId|RealName|DeviceID|CashID|SiteID|workKey|云支付|code|CashType|校验码|tag_login}
//            {0|27|杭州微盘信息技术有限公司|10000022|27|419|06520364|xxx|111|0|True||881E0C8E11818B17|tag_login}
                appContext.setDeviceId(arr[3]);
                appContext.setRealName(arr[2]);
                appContext.setCashId(arr[4]);
                appContext.setSiteId(arr[5]);
                appContext.setWorkKey(arr[6]);
                appContext.setCashType(arr[9]);
                Constant.isTuiKuan = Boolean.valueOf(arr[10]);

                shared.RememberingUsername(loginnewEditPhone.getText().toString());
                shared.RememberingPassword(loginnewEditMessageverify.getText().toString());
            } else {
                flag = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginNewActivity.this);
                builder.setTitle("登录失败");
                builder.setMessage(arr[1]);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog noticeDialog = builder.create();
                noticeDialog.setCanceledOnTouchOutside(false);
                noticeDialog.show();
            }
        } else {
            flag = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginNewActivity.this);
            builder.setTitle("登录失败");
            builder.setMessage("网络连接超时，请重试！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            Dialog noticeDialog = builder.create();
            noticeDialog.setCanceledOnTouchOutside(false);
            noticeDialog.show();
        }
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Constant.jpushMessage.clear();
                    Log.i("test", logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i("test", logs);
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("test", logs);
            }

        }

    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("test", logs);
                    Constant.jpushMessage.clear();
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i("test", logs);
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("test", logs);
            }
        }

    };

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;


    private final Handler jHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d("test", "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), alias, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d("test", "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;
                default:
                    Log.i("test", "Unhandled msg - " + msg.what);
            }
        }
    };

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

    @OnClick({R.id.iv_clear, R.id.login_nologin, R.id.tv_login, R.id.login_regist})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_regist:
                Intent intent = new Intent(LoginNewActivity.this, LoginRegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.iv_clear:
                loginnewEditPhone.setText("");
                break;
            case R.id.login_nologin:

                AndPermission.with(LoginNewActivity.this)
                        .permission(Manifest.permission.CALL_PHONE)
                        .send();
                DialogUtils.customDialog(LoginNewActivity.this, "", "呼叫", "取消",
                        "客服电话：400-8321-606", new DialogUtils.DialogCallback() {
                            @Override
                            public void PositiveButton(int i) {
                                switch (i) {
                                    case -1:
                                        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "400-8321-606"));
                                        startActivity(intent);
                                        break;
                                    case -2:
                                        break;

                                    default:
                                        break;
                                }
                            }
                        }, false, true);
                break;
            default:
                break;
        }
    }
}