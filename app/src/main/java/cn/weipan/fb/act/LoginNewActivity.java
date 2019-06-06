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
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.common.AppManager;
import cn.weipan.fb.common.UpdateManager;
import cn.weipan.fb.common.Constant;
import cn.weipan.fb.service.TagAliasOperatorHelper;
import cn.weipan.fb.utils.DialogUtils;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.NetworkRequest;
import cn.weipan.fb.utils.ToastUtils;

import static cn.weipan.fb.service.TagAliasOperatorHelper.ACTION_SET;
import static cn.weipan.fb.service.TagAliasOperatorHelper.sequence;


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
    private boolean flag;
    private String alias;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loadingDialog = new LoadingDialog(LoginNewActivity.this, "登录中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                UpdateManager manager = new UpdateManager(LoginNewActivity.this);
                manager.checkUpdate();
                Looper.loop();
            }
        }).start();
        initView();
    }

    //初始化界面
    private void initView() {
        loginnewEditMessageverify.setTransformationMethod(PasswordTransformationMethod.getInstance());
        try {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                PermissionUtils.getPermissions(PermissionConstants.PHONE);
                return;
            }
            imei = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginnewEditPhone.setText(SPUtils.getInstance().getString("loginname"));
        loginnewEditMessageverify.setText(SPUtils.getInstance().getString("pwd"));
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
                alias = arr[3];
                Log.i("test", alias + "----------alias-------------");
                TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
                tagAliasBean.action = ACTION_SET;
                sequence++;
                tagAliasBean.alias = alias;
                tagAliasBean.isAliasAction = true;
                TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);
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
                SPUtils.getInstance().put("loginname", loginnewEditPhone.getText().toString().trim());
                SPUtils.getInstance().put("pwd", loginnewEditMessageverify.getText().toString().trim());
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
                PermissionUtils.permission(PermissionConstants.PHONE).callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
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
                    }

                    @Override
                    public void onDenied() {

                    }
                }).request();
                break;
            default:
                break;
        }
    }
}