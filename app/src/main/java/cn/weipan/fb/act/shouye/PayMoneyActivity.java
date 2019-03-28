package cn.weipan.fb.act.shouye;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseActivity;
import cn.weipan.fb.utils.EditTextUtils;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.NetworkRequest;
import cn.weipan.fb.utils.ToastUtils;


/**
 * 扫一扫付款输入金额界面
 * Created by cc on 2016/9/19.
 * 邮箱：904359289@QQ.com.
 */
public class PayMoneyActivity extends BaseActivity implements NetworkRequest.ReponseListener, OnClickListener {

    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.tv_marked)
    TextView tvMarked;
    @BindView(R.id.paymoney_edit)
    EditText paymoneyEdit;
    @BindView(R.id.soft_keyboard_btn_1)
    Button softKeyboardBtn1;
    @BindView(R.id.soft_keyboard_btn_4)
    Button softKeyboardBtn4;
    @BindView(R.id.soft_keyboard_btn_2)
    Button softKeyboardBtn2;
    @BindView(R.id.soft_keyboard_btn_5)
    Button softKeyboardBtn5;
    @BindView(R.id.soft_keyboard_btn_3)
    Button softKeyboardBtn3;
    @BindView(R.id.soft_keyboard_btn_6)
    Button softKeyboardBtn6;
    @BindView(R.id.bt_delete)
    Button btDelete;
    @BindView(R.id.bt_clear)
    Button btClear;
    @BindView(R.id.soft_keyboard_btn_7)
    Button softKeyboardBtn7;
    @BindView(R.id.soft_keyboard_btn_8)
    Button softKeyboardBtn8;
    @BindView(R.id.soft_keyboard_btn_9)
    Button softKeyboardBtn9;
    @BindView(R.id.soft_keyboard_btn_0)
    Button softKeyboardBtn0;
    @BindView(R.id.bt_point)
    Button btPoint;
    @BindView(R.id.bt_confirm)
    Button btConfirm;
    private String activity;
    private Intent intent;
    private String memberNumber;
    private String memberName;
    private String memberType;
    private String memberMoney;
    private String sendData;
    private String randomStr;
    private String miyao;
    private LoadingDialog loadingDialog;
    private String paymoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymoney);
        ButterKnife.bind(this);

        initView();
        hintSystemSoftKeyboard();//隐藏系统键盘
    }


    //初始化控件
    private void initView() {
        activity = getIntent().getStringExtra("Activity");
        memberNumber = getIntent().getStringExtra("MemberNumber");
        memberName = getIntent().getStringExtra("MemberName");
        memberType = getIntent().getStringExtra("MemberType");
        memberMoney = getIntent().getStringExtra("MemberMoney");

        headViewTitle.setText("收款金额");
        tvMarked.setText("请输入收款金额");


        paymoneyEdit.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        EditTextUtils.setPriceEditText(paymoneyEdit);
        paymoneyEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(paymoneyEdit.getText().toString())) {
                    btConfirm.setBackgroundResource(R.color.header);
                    btConfirm.setTextColor(Color.WHITE);
                } else {
                    btConfirm.setBackgroundResource(R.color.light_gray);
                    btConfirm.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (TextUtils.equals(activity, "JiFenActivity")) {//积分消费
            headViewTitle.setText("积分消费");
            tvMarked.setText("请输入消费积分");
            btPoint.setClickable(false);
        } else if (TextUtils.equals(activity, "MemberConsumptionActivity")) {//会员消费
            headViewTitle.setText("会员消费");
        } else if (TextUtils.equals(activity, "MemberIncomeActivity")) {//会员充值
            headViewTitle.setText("会员充值");
            tvMarked.setText("请输入充值金额");
        } else if (TextUtils.equals(activity, "KaquanshoukuanActivity")) {//卡券收款
            headViewTitle.setText("卡券收款");
            tvMarked.setText("请输入消费金额");
        } else if (TextUtils.equals(activity, "KaquanhexiaoActivity")) {//卡券核销
            headViewTitle.setText("卡券核销");
            tvMarked.setText("请输入核销金额，如不需要记账 点击确定");
            btConfirm.setBackgroundResource(R.color.header);
            btConfirm.setTextColor(Color.WHITE);
        }
        loadingDialog = new LoadingDialog(PayMoneyActivity.this, "提交中...");
        randomStr = getRandomString(8);
        miyao = getMiyao(randomStr);
    }


    /**
     * 回退
     */
    private void rollBack(Editable editable, int start) {
        if (editable != null && editable.length() > 0) {
            if (start > 0) {
                editable.delete(start - 1, start);
            }
        }
    }

    private void hintSystemSoftKeyboard() {
        if (Build.VERSION.SDK_INT <= 10) {//4.0以下
            paymoneyEdit.setInputType(InputType.TYPE_NULL);
        } else {
            PayMoneyActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                if (Build.VERSION.SDK_INT < 17) {
                    setShowSoftInputOnFocus = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                } else {
                    setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                }
                setShowSoftInputOnFocus.setAccessible(false);
                setShowSoftInputOnFocus.invoke(paymoneyEdit, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick({R.id.ll_fanhui, R.id.soft_keyboard_btn_1, R.id.soft_keyboard_btn_4, R.id.soft_keyboard_btn_2, R.id.soft_keyboard_btn_5, R.id.soft_keyboard_btn_3, R.id.soft_keyboard_btn_6, R.id.bt_delete, R.id.bt_clear, R.id.soft_keyboard_btn_7, R.id.soft_keyboard_btn_8, R.id.soft_keyboard_btn_9, R.id.soft_keyboard_btn_0, R.id.bt_point, R.id.bt_confirm})
    public void onClick(View view) {
        Editable editable = paymoneyEdit.getText();
        int start = paymoneyEdit.getSelectionStart();
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            case R.id.soft_keyboard_btn_0:
                editable.insert(start, Character.toString('0'));
                break;
            case R.id.soft_keyboard_btn_1:
                editable.insert(start, Character.toString('1'));
                break;
            case R.id.soft_keyboard_btn_2:
                editable.insert(start, Character.toString('2'));
                break;
            case R.id.soft_keyboard_btn_3:
                editable.insert(start, Character.toString('3'));
                break;
            case R.id.soft_keyboard_btn_4:
                editable.insert(start, Character.toString('4'));
                break;
            case R.id.soft_keyboard_btn_5:
                editable.insert(start, Character.toString('5'));
                break;
            case R.id.soft_keyboard_btn_6:
                editable.insert(start, Character.toString('6'));
                break;
            case R.id.soft_keyboard_btn_7:
                editable.insert(start, Character.toString('7'));
                break;
            case R.id.soft_keyboard_btn_8:
                editable.insert(start, Character.toString('8'));
                break;
            case R.id.soft_keyboard_btn_9:
                editable.insert(start, Character.toString('9'));
                break;

            case R.id.bt_delete:
                rollBack(editable, start);
                break;
            case R.id.bt_clear:
                paymoneyEdit.setText("");
                break;
            case R.id.bt_point:
                if (!paymoneyEdit.getText().toString().contains(".")) {
                    editable.insert(start, Character.toString('.'));
                }
                break;
            case R.id.bt_confirm:
                paymoney = paymoneyEdit.getText().toString().trim();

                if (paymoney.endsWith(".")) {
                    paymoney = paymoney + "00";
                }
                if (TextUtils.equals(activity, "KaquanhexiaoActivity")) {// 卡券核销
                    loadingDialog.show();
                    sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|122|" + memberNumber + "|" + paymoney + "|" + randomStr + miyao + "|tag_wx_card}";
                    Log.i("test", "sendData = " + sendData);
                    NetworkRequest mLoginRequest = new NetworkRequest(sendData);
                    mLoginRequest.start();
                    mLoginRequest.setListener(PayMoneyActivity.this);
                } else if (!TextUtils.isEmpty(paymoney)) {
                    if (TextUtils.equals(paymoney, "0")) {
                        ToastUtils.showToast(PayMoneyActivity.this, "请输入有效金额");
                    } else if (TextUtils.equals(paymoney, "0.")) {
                        ToastUtils.showToast(PayMoneyActivity.this, "请输入有效金额");
                    } else if (TextUtils.equals(paymoney, "0.0")) {
                        ToastUtils.showToast(PayMoneyActivity.this, "请输入有效金额");
                    } else if (TextUtils.equals(paymoney, "0.00")) {
                        ToastUtils.showToast(PayMoneyActivity.this, "请输入有效金额");
                    } else {
                        if (TextUtils.equals(activity, "MemberConsumptionActivity")) {  //会员消费
                            loadingDialog.show();
                            sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|132|" + memberNumber + "|" + paymoney + "|" + randomStr + miyao + "|tag_wx_card}";
                            Log.i("test", "sendData = " + sendData);
                            NetworkRequest mLoginRequest = new NetworkRequest(sendData);
                            mLoginRequest.start();
                            mLoginRequest.setListener(PayMoneyActivity.this);
//                            {app|10006740|16885|133|00000000006|55|0.01|284521431838212758|N5GU3B7C1625ADEDBCFB2C05|tag_scan_ali}
//                            {app|10006740|16885|133|00000000006|55|0.01|281952558269955860|VYMNHOCW494D86F3F1F5E4A9|tag_scan_ali}
//                            {app|10006740|16885|55|0.01|280307017614890316|9HE32TTN528B0DE1BB7DC46D|tag_scan_ali}
                        } else if (TextUtils.equals(activity, "MemberIncomeActivity")) {  //会员充值

                            intent = new Intent(PayMoneyActivity.this, SaoMaActivity.class);
                            intent.putExtra("Activity", "MemberIncomeSuccessActivity");
                            intent.putExtra("paymoney", paymoney);
                            intent.putExtra("memberNumber", memberNumber);
                            startActivity(intent);

                        } else if (TextUtils.equals(activity, "JiFenActivity")) {//积分消费
                            loadingDialog.show();
                            sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|134|" + memberNumber + "|" + paymoney + "|" + randomStr + miyao + "|tag_wx_card}";
                            Log.i("test", "sendData = " + sendData);
                            NetworkRequest mLoginRequest = new NetworkRequest(sendData);
                            mLoginRequest.start();
                            mLoginRequest.setListener(PayMoneyActivity.this);

                        } else if (TextUtils.equals(activity, "CollectionActivity")) {//收款码

//                            intent = new Intent(PayMoneyActivity.this, ShouKuanMaActivity.class);
                            intent = new Intent(PayMoneyActivity.this, ChoseShouKuanMaActivity.class);
                            intent.putExtra("paymoney", paymoney);
                            startActivity(intent);

                        } else if (TextUtils.equals(activity, "SaoMaActivity")) {//扫一扫
                            intent = new Intent(PayMoneyActivity.this, SaoMaActivity.class);
                            intent.putExtra("paymoney", paymoney);
                            intent.putExtra("Activity", "SaoMaActivity");
                            startActivity(intent);
                        } else if (TextUtils.equals(activity, "KaquanshoukuanActivity")) { // 卡券收款
                            intent = new Intent(PayMoneyActivity.this, SaoMaActivity.class);
                            intent.putExtra("paymoney", paymoney);
                            intent.putExtra("Activity", "KaquanshoukuanActivity");
                            startActivity(intent);
                        }

                    }
                } else {
                    ToastUtils.showToast(PayMoneyActivity.this, "请输入有效金额");
                }
                break;
        }
    }

    private void setStatus(String result) {//{0|会员卡号|会员名称|交易时间|支付类型|交易状态|会员卡余额|实收金额|会员等级|tag_scan_vip}
        loadingDialog.dismiss();
        Log.i("test", "paymonet = result = " + result);//{0|会员卡收款成功|2.7|00000000006|ggg|3|2016/12/6 17:29:33|VIP测试|tag}
//        {0|00000000006|ggg|2016/12/7 9:28:33|积分余额支付|交易成功|9876|2|金牌会员|tag_scan_vip}
        String[] arr = null;
        if (result != null) {
            arr = result.replace("{", "").replace("}", "").split("\\|");
            if (arr[0].equals("0")) {
                if (TextUtils.equals(activity, "MemberConsumptionActivity")) {//会员消费
                    intent = new Intent(PayMoneyActivity.this, HeXiaoSuccessActivity.class);
                    intent.putExtra("Activity", "MemberConsumptionActivity");
                    intent.putExtra("Text_sucess", arr[1]);
                    intent.putExtra("Text_money", "￥" + arr[2]);
                    intent.putExtra("Text_one", arr[3]);
                    intent.putExtra("Text_two", arr[4]);
                    intent.putExtra("Text_three", arr[5]);
                    intent.putExtra("Text_four", arr[2]);
                    intent.putExtra("Text_five", arr[6]);
                    intent.putExtra("Text_shouyingyuan", arr[7]);
                    startActivity(intent);
                } else if (TextUtils.equals(activity, "JiFenActivity")) {//积分消费
                    intent = new Intent(PayMoneyActivity.this, HeXiaoSuccessActivity.class);
                    intent.putExtra("Activity", "JiFenActivity");
                    intent.putExtra("Text_money", arr[7]);
                    intent.putExtra("Text_one", arr[1]);
                    intent.putExtra("Text_two", arr[2]);
                    intent.putExtra("Text_three", arr[7]);
                    intent.putExtra("Text_four", arr[6]);
                    intent.putExtra("Text_five", arr[3]);
                    intent.putExtra("Text_shouyingyuan", appContext.getRealName());
                    startActivity(intent);
                } else if (TextUtils.equals(activity, "KaquanhexiaoActivity")) {//卡券核销
//                    {0|核销成功|2016/12/7 15:00:04|tag_scan_wxcard}
                    intent = new Intent(PayMoneyActivity.this, HeXiaoSuccessActivity.class);
                    intent.putExtra("Activity", "KaquanhexiaoActivity");
                    intent.putExtra("Text_one", memberNumber);
                    if (!TextUtils.isEmpty(paymoney)) {
                        intent.putExtra("Text_two", "￥" + paymoney);
                        intent.putExtra("Text_money", "￥" + paymoney);
                    }
                    intent.putExtra("Text_three", arr[2]);
                    intent.putExtra("Text_shouyingyuan", appContext.getRealName());
                    startActivity(intent);
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(PayMoneyActivity.this);
                builder.setTitle("消费失败");
                builder.setMessage(arr[1]);

                if (TextUtils.equals(activity, "MemberConsumptionActivity")) {
                    builder.setPositiveButton("会员充值", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //跳转到会员充值界面
                            activity = "MemberIncomeActivity";
                            headViewTitle.setText("会员充值");
                            tvMarked.setText("请输入充值金额");
                            paymoneyEdit.setText("");
//                            intent = new Intent(PayMoneyActivity.this, PayMoneyActivity.class);
//                            intent.putExtra("Activity", "MemberIncomeActivity");
//                            intent.putExtra("MemberNumber", memberNumber);
//                            intent.putExtra("MemberName", memberName);
//                            intent.putExtra("MemberType", memberType);
//                            intent.putExtra("MemberMoney", memberMoney);
//                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("直接消费", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //跳转到扫码消费界面
                            intent = new Intent(PayMoneyActivity.this, SaoMaActivity.class);
                            intent.putExtra("Activity", "SaoMaActivity");
                            intent.putExtra("paymoney", paymoney);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });

                } else {
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }


                Dialog noticeDialog = builder.create();
                noticeDialog.setCanceledOnTouchOutside(false);
                noticeDialog.show();
            }
        }else {
            ToastUtils.showToast(PayMoneyActivity.this,"网络连接超时，请重试！");
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

}
