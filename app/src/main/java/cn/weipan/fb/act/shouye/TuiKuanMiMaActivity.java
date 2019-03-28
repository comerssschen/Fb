package cn.weipan.fb.act.shouye;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseActivity;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.NetworkRequest;
import cn.weipan.fb.utils.ToastUtils;

/**
 * 退款密码
 * Created by cc on 2016/10/12.
 * 邮箱：904359289@QQ.com.
 */
public class TuiKuanMiMaActivity extends BaseActivity implements NetworkRequest.ReponseListener, View.OnClickListener {
    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.tv_tuikuanjine)
    TextView tvTuikuanjine;
    @BindView(R.id.et_tuikuanmima)
    EditText etTuikuanmima;
    @BindView(R.id.rl_next)
    RelativeLayout rlNext;
    private String dingDanHao;
    private String shiShouJinE;
    private String type;
    private boolean flag;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuikuanmima);
        ButterKnife.bind(this);
        initView();
    }

    //初始化
    private void initView() {
        headViewTitle.setText("验证密码");
        etTuikuanmima.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        etTuikuanmima.setTransformationMethod(PasswordTransformationMethod.getInstance());
        shiShouJinE = getIntent().getStringExtra("ShiShouJinE");
        dingDanHao = getIntent().getStringExtra("DingDanHao");
        type = getIntent().getStringExtra("type");
        tvTuikuanjine.setText("￥" + shiShouJinE);

        loadingDialog = new LoadingDialog(TuiKuanMiMaActivity.this, "提交中...");
    }


    //请求回调结果
    private void setStatus(String result) {
        //{0|微信退款成功|0.01|4002632001201610318242211068|2016-10-31 11:44:52|21|QIKGJGFN43B0221B04F05262}
//       {0|微信退款成功|0.01|4007362001201612082161111059|2016-12-08 15:01:49|21|JHBMRXHGC25A00F6A77520A2|tag_returnfee_wx}
        loadingDialog.dismiss();
        Log.i("test", "result = " + result);
        String[] arr = null;
        if (result != null) {
            arr = result.replace("{", "").replace("}", "").split("\\|");
            if (arr[0].equals("0")) {
                flag = true;
                Intent intent = new Intent(TuiKuanMiMaActivity.this, HeXiaoSuccessActivity.class);
                intent.putExtra("Activity", "TuiKuanActivity");
                intent.putExtra("Text_sucess", arr[1]);
                intent.putExtra("Text_money", "-￥" + arr[2]);
                intent.putExtra("Text_one", arr[6]);
                intent.putExtra("Text_two", arr[3]);
                intent.putExtra("Text_shouyingyuan", arr[4]);
                startActivity(intent);
            } else {
                flag = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(TuiKuanMiMaActivity.this);
                builder.setTitle("退款失败");
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
            ToastUtils.showToast(TuiKuanMiMaActivity.this, "网络连接超时，请重试！");
        }


    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // 处理逻辑
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

    @OnClick({R.id.ll_fanhui, R.id.rl_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            //退款
            case R.id.rl_next:
                if (!TextUtils.isEmpty(etTuikuanmima.getText().toString())) {
                    loadingDialog.show();
                    if (flag) {
                        return;
                    }
                    flag = true;
                    String randomStr = getRandomString(8);
                    String miyao = getMiyao(randomStr);
                    String sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + type + "|" + dingDanHao + "|" + etTuikuanmima.getText().toString().trim() + "|" + shiShouJinE + "|" + randomStr + miyao + "|tag_returnfee_wx}";
                    Log.i("test", "sendData = " + sendData);
                    NetworkRequest mLoginRequest = new NetworkRequest(sendData);
                    mLoginRequest.start();
                    mLoginRequest.setListener(TuiKuanMiMaActivity.this);

                } else {
                    ToastUtils.showToast(TuiKuanMiMaActivity.this, "请输入退款密码");
                }
                break;
            default:
                break;
        }
    }
}
