package cn.weipan.fb.act.shouye;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseActivity;
import cn.weipan.fb.act.ReceivablesSuccessActivity;
import cn.weipan.fb.common.AppContext;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.NetworkRequest;
import cn.weipan.fb.utils.ToastUtils;


/**
 * 扫一扫 二维码扫描界面
 * Created by cc on 2016/10/12.
 * 邮箱：904359289@QQ.com.
 */
public class SaoMaActivity extends BaseActivity implements NetworkRequest.ReponseListener, View.OnClickListener {

    public AppContext appContext;
    String imei;
    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_flashlight)
    TextView tvFlashlight;
    @BindView(R.id.tv_reminder)
    TextView tvReminder;
    @BindView(R.id.tv_dingdan)
    TextView tvDingdan;
    @BindView(R.id.ll_truemoney)
    LinearLayout llTruetvMoney;
    @BindView(R.id.ll_pos)
    LinearLayout llPos;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    private String payMoney;
    private CaptureFragment captureFragment;
    private Intent intent;
    private String randomStr;
    private String miyao;
    private LoadingDialog loadingDialog;
    private String activity;
    private String sendData;
    private String memberNumber;
    private String hexiaoNumber;
    private NetworkRequest mLoginRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saoma);
        ButterKnife.bind(this);
        payMoney = getIntent().getStringExtra("paymoney");
        activity = getIntent().getStringExtra("Activity");
        memberNumber = getIntent().getStringExtra("memberNumber");
        Log.i("test", "SaoMaActivity =memberNumber " + memberNumber);
        appContext = (AppContext) this.getApplication();
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        imei = tm.getDeviceId();

        PermissionUtils.permission(PermissionConstants.PHONE).callback(new PermissionUtils.SimpleCallback() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied() {
                ToastUtils.showToast(SaoMaActivity.this, "请检查权限");
                finish();
            }
        }).request();

        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
        initView();

    }


    //初始化控件
    private void initView() {
        if (TextUtils.equals(activity, "SaoMaActivity")) { //扫一扫
            tvDingdan.setVisibility(View.INVISIBLE);
            tvTitle.setText("扫一扫收款");
            tvReminder.setText("请将二维码/条形码放入框内，即可自动扫描");
            tvMoney.setText("收款金额￥ " + payMoney);
        } else if (TextUtils.equals(activity, "KaquanshoukuanActivity")) {//卡券收款
            tvTitle.setText("扫描卡券码");
            tvReminder.setText("请先扫描微信卡券完成核销");
            tvMoney.setText("消费金额￥ " + payMoney);
            llBottom.setVisibility(View.INVISIBLE);
        } else if (TextUtils.equals(activity, "KaquanshoukuanSuccessActivity")) {//卡券收款成功
            tvDingdan.setVisibility(View.INVISIBLE);
            tvTitle.setText("核销收款");
            tvReminder.setText("请将二维码/条码放入框内，即可自动扫描");
            tvMoney.setText("消费金额￥ " + payMoney);
        } else if (TextUtils.equals(activity, "KaquanhexiaoActivity")) { //卡券核销
            tvTitle.setText("扫描卡券码");
            tvReminder.setText("请扫描微信卡券完成核销");
            tvMoney.setVisibility(View.INVISIBLE);
            llBottom.setVisibility(View.INVISIBLE);
        } else if (TextUtils.equals(activity, "TuiKuanActivity")) { //退款
            tvTitle.setText("退款");
            tvReminder.setText("扫描订单二维码或手动输入退款订单编号");
            tvMoney.setVisibility(View.INVISIBLE);
            llBottom.setVisibility(View.INVISIBLE);
        }
        randomStr = getRandomString(8);
        miyao = getMiyao(randomStr);
        loadingDialog = new LoadingDialog(SaoMaActivity.this, "提交中...");
    }


    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {

        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            loadingDialog.show();
            if (TextUtils.equals(activity, "SaoMaActivity")) {//扫一扫
                sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|1|" + payMoney + "|" + result + "|" + randomStr + miyao + "|tag_scan_pay}";
                Log.i("test", "sendData = " + sendData);
                mLoginRequest = new NetworkRequest(sendData);
                mLoginRequest.start();
                mLoginRequest.setListener(SaoMaActivity.this);
            } else if (TextUtils.equals(activity, "KaquanhexiaoActivity")) {//卡券核销
                hexiaoNumber = result;
//                {app|设备ID|签到人ID|操作类型|卡券编号|校验码|tag_wx_card}
                sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|121|" + result + "|" + randomStr + miyao + "|tag_wx_card}";
                Log.i("test", "sendData = " + sendData);
                mLoginRequest = new NetworkRequest(sendData);
                mLoginRequest.start();
                mLoginRequest.setListener(SaoMaActivity.this);
            } else if (TextUtils.equals(activity, "KaquanshoukuanActivity")) {//卡券收款
//                activity = "KaquanshoukuanSuccessActivity";
                memberNumber = result;
                sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|121|" + result + "|" + randomStr + miyao + "|tag_wx_card}";
                Log.i("test", "sendData = " + sendData);
                mLoginRequest = new NetworkRequest(sendData);
                mLoginRequest.start();
                mLoginRequest.setListener(SaoMaActivity.this);
//                restartPreviewAfterDelay(3000);
            } else if (TextUtils.equals(activity, "KaquanshoukuanSuccessActivity")) {//卡券收款付款
                sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|123|" + memberNumber + "|1|" + payMoney + "|" + result + "|" + randomStr + miyao + "|tag_wx_card}";
                Log.i("test", "sendData = " + sendData);
                mLoginRequest = new NetworkRequest(sendData);
                mLoginRequest.start();
                mLoginRequest.setListener(SaoMaActivity.this);
            } else if (TextUtils.equals(activity, "TuiKuanActivity")) {//退款
                sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|21|" + result + "|" + randomStr + miyao + "|tag_refund}";
                Log.i("test", "sendData = " + sendData);
                mLoginRequest = new NetworkRequest(sendData);
                mLoginRequest.start();
                mLoginRequest.setListener(SaoMaActivity.this);
            }

        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            SaoMaActivity.this.setResult(RESULT_OK, resultIntent);
            SaoMaActivity.this.finish();
        }
    };

    public void restartPreviewAfterDelay(long delayMS) {
        Handler handler = captureFragment.getHandler();
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    private void setStatus(String result) {
        loadingDialog.dismiss();
        String[] arr = null;
        if (result != null) {
            arr = result.replace("{", "").replace("}", "").split("\\|");
            if (arr[0].equals("0")) {
                if (TextUtils.equals(activity, "SaoMaActivity")) {//扫一扫
                    Intent intent = new Intent(SaoMaActivity.this, ReceivablesSuccessActivity.class);
                    intent.putExtra("paymoney", arr[6]);
                    intent.putExtra("payNumber", arr[1]);
                    intent.putExtra("dingDanBianHao", arr[2]);
                    intent.putExtra("Time", arr[3]);
                    intent.putExtra("PayTitle", arr[4]);
                    intent.putExtra("PayType", arr[5]);
                    startActivity(intent);
                    finish();
                } else if (TextUtils.equals(activity, "TuiKuanActivity")) {//退款
                    intent = new Intent(SaoMaActivity.this, TuiKuanZhangDanXiangQing.class);
                    intent.putExtra("DingDanJingE", arr[7]);
                    intent.putExtra("ShiShouJinE", arr[8]);
                    intent.putExtra("JiaoYiShiJian", arr[10]);
                    intent.putExtra("JiaoYiZhuangTai", arr[6]);
                    intent.putExtra("JiaoYiDanHao", arr[1]);
                    intent.putExtra("DingDanHao", arr[2]);
                    intent.putExtra("ShouYinYuan", appContext.getRealName());
                    intent.putExtra("type", arr[3]);
                    intent.putExtra("FuKuanFangShi", arr[4]);
                    startActivity(intent);

                } else if (TextUtils.equals(activity, "KaquanshoukuanActivity")) {//卡券收款
                    if (TextUtils.equals("CASH", arr[1])) {
                        if (Double.parseDouble(payMoney) >= Double.parseDouble(arr[2])) {
                            activity = "KaquanshoukuanSuccessActivity";
                            tvDingdan.setVisibility(View.INVISIBLE);
                            llBottom.setVisibility(View.VISIBLE);
                            tvTitle.setText("核销收款");
                            tvReminder.setText("请将二维码/条码放入框内，即可自动扫描");
                            tvMoney.setText("消费金额￥ " + payMoney);
                            restartPreviewAfterDelay(3000);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SaoMaActivity.this);
                            builder.setTitle("扫描失败");
                            builder.setMessage("未达到卡券核销条件");

                            builder.setPositiveButton("直接付款", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转到扫码消费界面

                                    dialog.dismiss();
                                    activity = "SaoMaActivity";
                                    tvDingdan.setVisibility(View.INVISIBLE);
                                    llBottom.setVisibility(View.VISIBLE);
                                    tvTitle.setText("扫一扫收款");
                                    tvReminder.setText("请将二维码/条形码放入框内，即可自动扫描");
                                    tvMoney.setText("收款金额￥ " + payMoney);
                                    restartPreviewAfterDelay(3000);
                                }
                            });
                            builder.setNegativeButton("重新核销", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    restartPreviewAfterDelay(3000);
                                }
                            });
                            Dialog noticeDialog = builder.create();
                            noticeDialog.setCanceledOnTouchOutside(false);
                            noticeDialog.show();
                        }
                    } else {
                        activity = "KaquanshoukuanSuccessActivity";
                        tvDingdan.setVisibility(View.INVISIBLE);
                        llBottom.setVisibility(View.VISIBLE);
                        tvTitle.setText("核销收款");
                        tvReminder.setText("请将二维码/条码放入框内，即可自动扫描");
                        tvMoney.setText("消费金额￥ " + payMoney);
                        restartPreviewAfterDelay(3000);
                    }


                } else if (TextUtils.equals(activity, "KaquanshoukuanSuccessActivity")) {//卡券收款成功
                    intent = new Intent(SaoMaActivity.this, HeXiaoSuccessActivity.class);
                    intent.putExtra("Activity", "KaquanshoukuanSuccessActivity");
                    intent.putExtra("Text_money", "￥" + arr[9]);
                    intent.putExtra("Text_one", arr[1]);
                    intent.putExtra("Text_two", arr[3]);
                    intent.putExtra("Text_three", arr[8]);
                    intent.putExtra("Text_four", arr[9]);
                    intent.putExtra("Text_five", arr[4]);
                    intent.putExtra("Text_shouyingyuan", appContext.getRealName());
                    startActivity(intent);
                } else if (TextUtils.equals(activity, "KaquanhexiaoActivity")) {//卡券核销
                    intent = new Intent(appContext, PayMoneyActivity.class);
                    intent.putExtra("Activity", "KaquanhexiaoActivity");
                    intent.putExtra("MemberNumber", hexiaoNumber);
                    startActivity(intent);
                }

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SaoMaActivity.this);
                builder.setTitle("扫描失败");
                builder.setMessage(arr[1]);
                if (TextUtils.equals(activity, "KaquanshoukuanActivity")) {
                    builder.setPositiveButton("直接付款", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            activity = "SaoMaActivity";
                            tvDingdan.setVisibility(View.INVISIBLE);
                            tvTitle.setText("扫一扫收款");
                            tvReminder.setText("请将二维码/条形码放入框内，即可自动扫描");
                            tvMoney.setText("收款金额￥ " + payMoney);
                            llBottom.setVisibility(View.VISIBLE);
                            restartPreviewAfterDelay(3000);
                        }
                    });
                    builder.setNegativeButton("重新扫描", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            restartPreviewAfterDelay(3000);
                        }
                    });
                } else {
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            SaoMaActivity.this.finish();
                        }
                    });
                }
                Dialog noticeDialog = builder.create();
                noticeDialog.setCanceledOnTouchOutside(false);
                noticeDialog.show();
            }
        } else {
            ToastUtils.showToast(SaoMaActivity.this, "网络连接超时，请重试！");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishs();
        if (mLoginRequest != null) {
            mLoginRequest.close();
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

    public static boolean isOpen = false;

    @OnClick({R.id.ll_fanhui, R.id.tv_flashlight, R.id.tv_dingdan, R.id.ll_truemoney, R.id.ll_pos})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            case R.id.tv_flashlight:
                //开启闪光灯
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }
                break;
            case R.id.tv_dingdan:
                if (TextUtils.equals(activity, "TuiKuanActivity")) {//退款
                    intent = new Intent(SaoMaActivity.this, TuiKuanDanHao.class);
                    intent.putExtra("Activity", "TuiKuanActivity");
                    startActivity(intent);

                } else if (TextUtils.equals(activity, "KaquanshoukuanActivity")) {//卡券收款
                    intent = new Intent(SaoMaActivity.this, TuiKuanDanHao.class);
                    intent.putExtra("Activity", "KaquanshoukuanActivity");
                    intent.putExtra("paymoney", payMoney);
                    startActivity(intent);

                } else if (TextUtils.equals(activity, "KaquanhexiaoActivity")) {//卡券核销
                    intent = new Intent(SaoMaActivity.this, TuiKuanDanHao.class);
                    intent.putExtra("Activity", "KaquanhexiaoActivity");
                    intent.putExtra("paymoney", payMoney);
                    startActivity(intent);

                }
                break;
            case R.id.ll_truemoney:
                intent = new Intent(SaoMaActivity.this, TrueMoneyActivity.class);
                intent.putExtra("paymoney", payMoney);
                if (TextUtils.equals(activity, "SaoMaActivity")) {
                    intent.putExtra("Activity", "SaoMaActivity");
                } else if (TextUtils.equals(activity, "KaquanshoukuanSuccessActivity")) {//卡券收款成功
                    intent.putExtra("Activity", "KaquanshoukuanSuccessActivity");
                    intent.putExtra("memberNumber", memberNumber);
                    intent.putExtra("paymoney", payMoney);

                }

                startActivity(intent);
//                finish();
                break;
            case R.id.ll_pos:
                intent = new Intent(SaoMaActivity.this, PosActivity.class);
                intent.putExtra("paymoney", payMoney);
                if (TextUtils.equals(activity, "SaoMaActivity")) {//扫一扫
                    intent.putExtra("Activity", "SaoMaActivity");
                } else if (TextUtils.equals(activity, "KaquanshoukuanSuccessActivity")) {//卡券收款成功
                    intent.putExtra("Activity", "KaquanshoukuanSuccessActivity");
                    intent.putExtra("memberNumber", memberNumber);
                    intent.putExtra("paymoney", payMoney);
                }
                startActivity(intent);
//                finish();
                break;
        }
    }
}
