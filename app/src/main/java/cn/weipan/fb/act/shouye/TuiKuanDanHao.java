package cn.weipan.fb.act.shouye;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
 * 退款单号手动输入
 * Created by cc on 2016/10/12.
 * 邮箱：904359289@QQ.com.
 */
public class TuiKuanDanHao extends BaseActivity implements NetworkRequest.ReponseListener, View.OnClickListener {
    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.tv_reminder)
    TextView tvReminder;
    @BindView(R.id.danhao_edit)
    EditText danhaoEdit;
    @BindView(R.id.commit_rl)
    RelativeLayout commitRl;
    private String type;
    private LoadingDialog loadingDialog;
    private String activity;
    private String paymoney;
    private Intent intent;
    private String randomStr;
    private String miyao;
    private String sendData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputdanhao);
        ButterKnife.bind(this);
        initView();
    }

    //初始化界面
    private void initView() {
        danhaoEdit.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        type = getIntent().getStringExtra("type");
        activity = getIntent().getStringExtra("Activity");
        paymoney = getIntent().getStringExtra("paymoney");

        loadingDialog = new LoadingDialog(TuiKuanDanHao.this, "提交中...");

        if (TextUtils.equals(activity, "MemberConsumptionActivity")) {//会员消费
            headViewTitle.setText("会员消费");
            tvReminder.setText("会员卡号");
            danhaoEdit.setHint("请输入会员卡号");
        } else if (TextUtils.equals(activity, "TuiKuanActivity")) {//退款
            headViewTitle.setText("退款");
            tvReminder.setText("退款单号");
            danhaoEdit.setHint("请输入商户订单号");
        } else if (TextUtils.equals(activity, "KaquanshoukuanActivity")) {//卡券收款
            headViewTitle.setText("卡券收款");
            tvReminder.setText("卡券号码");
            danhaoEdit.setHint("请输入卡券号码");
        } else if (TextUtils.equals(activity, "KaquanhexiaoActivity")) {//卡券核销
            headViewTitle.setText("卡券核销");
            tvReminder.setText("卡券号码");
            danhaoEdit.setHint("请输入卡券号码");
        } else if (TextUtils.equals(activity, "MemberIncomeActivity")) {//会员充值
            headViewTitle.setText("会员充值");
            tvReminder.setText("会员卡号");
            danhaoEdit.setHint("请输入会员卡号");
        } else if (TextUtils.equals(activity, "JiFenActivity")) {//积分消费
            headViewTitle.setText("积分消费");
            tvReminder.setText("会员卡号");
            danhaoEdit.setHint("请输入会员卡号");
        }


        randomStr = getRandomString(8);
        miyao = getMiyao(randomStr);
    }


    //请求回调
    private void setStatus(String result) {//{0|0.01|0.01|2016/10/28 14:08:42|支付成功|4002632001201610287962568701|00000419000000272016102814084
        loadingDialog.dismiss();
        Log.i("test", "result = " + result);
        String[] arr = null;
        if (result != null) {
            arr = result.replace("{", "").replace("}", "").split("\\|");
            if (arr[0].equals("0")) {
                if (TextUtils.equals(activity, "MemberConsumptionActivity")) {//会员消费
                    Intent intent = new Intent(TuiKuanDanHao.this, MemberDetailsActivity.class);
                    intent.putExtra("MemberNumber", arr[1]);
                    intent.putExtra("MemberName", arr[2]);
                    intent.putExtra("MemberType", arr[3]);
                    intent.putExtra("MemberMoney", arr[4]);
                    intent.putExtra("Activity", "MemberConsumptionActivity");
                    startActivity(intent);

                } else if (TextUtils.equals(activity, "TuiKuanActivity")) {//退款
                    intent = new Intent(TuiKuanDanHao.this, TuiKuanZhangDanXiangQing.class);
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
                } else if (TextUtils.equals(activity, "MemberIncomeActivity")) {//会员充值
                    Intent intent = new Intent(TuiKuanDanHao.this, MemberDetailsActivity.class);
                    intent.putExtra("MemberNumber", arr[1]);
                    intent.putExtra("MemberName", arr[2]);
                    intent.putExtra("MemberType", arr[3]);
                    intent.putExtra("MemberMoney", arr[4]);
                    intent.putExtra("Activity", "MemberIncomeActivity");
                    startActivity(intent);


                } else if (TextUtils.equals(activity, "JiFenActivity")) {//积分消费
                    Intent intent = new Intent(TuiKuanDanHao.this, MemberDetailsActivity.class);
                    intent.putExtra("MemberNumber", arr[1]);
                    intent.putExtra("MemberName", arr[2]);
                    intent.putExtra("MemberType", arr[3]);
                    intent.putExtra("MemberMoney", arr[5]);
                    intent.putExtra("Activity", "JiFenActivity");
                    startActivity(intent);
                } else if (TextUtils.equals(activity, "KaquanhexiaoActivity")) {//卡券核销
                    intent = new Intent(appContext, PayMoneyActivity.class);
                    intent.putExtra("Activity", "KaquanhexiaoActivity");
                    intent.putExtra("MemberNumber", danhaoEdit.getText().toString().trim());
                    startActivity(intent);
                } else if (TextUtils.equals(activity, "KaquanshoukuanActivity")) {//卡券收款
                    //{0|CASH|10|1|tag_scan_wxcard}
//                    {app|设备ID|签到人ID|操作类型|卡券编号|支付操作类型|抵扣金额|实付金额|付款码|校验码|tag_wx_card}
                    if (TextUtils.equals("CASH", arr[1])) {
                        if (Double.parseDouble(paymoney) >= Double.parseDouble(arr[2])) {
                            intent = new Intent(TuiKuanDanHao.this, SaoMaActivity.class);
                            intent.putExtra("Activity", "KaquanshoukuanSuccessActivity");
                            intent.putExtra("paymoney", paymoney);
                            intent.putExtra("memberNumber", danhaoEdit.getText().toString().trim());
                            startActivity(intent);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(TuiKuanDanHao.this);
                            builder.setTitle("扫描失败");
                            builder.setMessage("未达到卡券核销条件");

                            builder.setPositiveButton("直接付款", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转到扫码消费界面
                                    intent = new Intent(TuiKuanDanHao.this, SaoMaActivity.class);
                                    intent.putExtra("Activity", "SaoMaActivity");
                                    intent.putExtra("paymoney", paymoney);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("重新核销", new DialogInterface.OnClickListener() {
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
                        intent = new Intent(TuiKuanDanHao.this, SaoMaActivity.class);
                        intent.putExtra("Activity", "KaquanshoukuanSuccessActivity");
                        intent.putExtra("paymoney", paymoney);
                        intent.putExtra("memberNumber", danhaoEdit.getText().toString().trim());
                        startActivity(intent);
                    }
                }

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(TuiKuanDanHao.this);
                builder.setTitle("查找失败");
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
            ToastUtils.showToast(TuiKuanDanHao.this,"网络连接超时，请重试！");
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

    @OnClick({R.id.ll_fanhui, R.id.commit_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            //确定
            case R.id.commit_rl:
                if (!TextUtils.isEmpty(danhaoEdit.getText().toString().trim())) {
                    loadingDialog.show();
                    if (TextUtils.equals(activity, "TuiKuanActivity")) {//退款
                        sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|21|" + danhaoEdit.getText().toString().trim() + "|" + randomStr + miyao + "|tag_refund}";
                        Log.i("test", "sendData = " + sendData);
                        NetworkRequest mLoginRequest = new NetworkRequest(sendData);
                        mLoginRequest.start();
                        mLoginRequest.setListener(TuiKuanDanHao.this);
                    } else if (TextUtils.equals(activity, "JiFenActivity")) {//积分消费
                        sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|131|" + danhaoEdit.getText().toString().trim() + "|" + randomStr + miyao + "|tag_scan_jd}";
                        Log.i("test", "sendData = " + sendData);
                        NetworkRequest mLoginRequest = new NetworkRequest(sendData);
                        mLoginRequest.start();
                        mLoginRequest.setListener(TuiKuanDanHao.this);
                    } else if (TextUtils.equals(activity, "MemberConsumptionActivity")) {//会员消费
                        sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|131|" + danhaoEdit.getText().toString().trim() + "|" + randomStr + miyao + "|tag_scan_jd}";
                        Log.i("test", "sendData = " + sendData);
                        NetworkRequest mLoginRequest = new NetworkRequest(sendData);
                        mLoginRequest.start();
                        mLoginRequest.setListener(TuiKuanDanHao.this);
                    } else if (TextUtils.equals(activity, "KaquanshoukuanActivity")) {//卡券收款

                        sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|121|" + danhaoEdit.getText().toString().trim() + "|" + randomStr + miyao + "|tag_wx_card}";
                        Log.i("test", "sendData = " + sendData);
                        NetworkRequest mLoginRequest = new NetworkRequest(sendData);
                        mLoginRequest.start();
                        mLoginRequest.setListener(TuiKuanDanHao.this);


                    } else if (TextUtils.equals(activity, "KaquanhexiaoActivity")) {//卡券核销

                        sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|121|" + danhaoEdit.getText().toString().trim() + "|" + randomStr + miyao + "|tag_wx_card}";
                        Log.i("test", "sendData = " + sendData);
                        NetworkRequest mLoginRequest = new NetworkRequest(sendData);
                        mLoginRequest.start();
                        mLoginRequest.setListener(TuiKuanDanHao.this);


                    } else if (TextUtils.equals(activity, "MemberIncomeActivity")) {//会员充值
                        sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|131|" + danhaoEdit.getText().toString().trim() + "|" + randomStr + miyao + "|tag_scan_jd}";
                        Log.i("test", "sendData = " + sendData);
                        NetworkRequest mLoginRequest = new NetworkRequest(sendData);
                        mLoginRequest.start();
                        mLoginRequest.setListener(TuiKuanDanHao.this);
                    }

                } else {
                    ToastUtils.showToast(TuiKuanDanHao.this, "您输入的内容有误，请重新输入！");
                }
                break;
            default:
                break;
        }
    }
}
