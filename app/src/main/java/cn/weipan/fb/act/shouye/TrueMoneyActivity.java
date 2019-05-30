package cn.weipan.fb.act.shouye;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseActivity;
import cn.weipan.fb.common.Constant;
import cn.weipan.fb.utils.EditTextUtils;
import cn.weipan.fb.utils.HttpUtils;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.NetworkRequest;
import cn.weipan.fb.utils.ToastUtils;

/**
 * 现金收款记账
 * Created by cc on 2016/10/12.
 * 邮箱：904359289@QQ.com.
 */
public class TrueMoneyActivity extends BaseActivity implements NetworkRequest.ReponseListener, View.OnClickListener {

    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.paymoneyreturn_edit)
    TextView paymoneyreturnEdit;
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
    private String paymoney;
    private LoadingDialog loadingDialog;
    private String activity;
    private Intent intent;
    private BigDecimal bdPaymoneyEdit;
    private String sendData;
    private String memberNumber;
    private String randomStr;
    private String miyao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truemoney);
        ButterKnife.bind(this);

        initView();
        hintSystemSoftKeyboard();//隐藏系统键盘
    }

    //初始化界面
    private void initView() {
        randomStr = getRandomString(8);
        miyao = getMiyao(randomStr);
        activity = getIntent().getStringExtra("Activity");
        paymoney = getIntent().getStringExtra("paymoney");
        memberNumber = getIntent().getStringExtra("memberNumber");

        loadingDialog = new LoadingDialog(TrueMoneyActivity.this, "提交中...");
        headViewTitle.setText("计算找零");
        paymoneyreturnEdit.setText("-" + paymoney);

        paymoneyEdit.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        EditTextUtils.setPriceEditText(paymoneyEdit);

        paymoneyEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mPpaymoneyEdit = paymoneyEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(mPpaymoneyEdit)) {
                    bdPaymoneyEdit = new BigDecimal(mPpaymoneyEdit);//必须用字符串
                } else {
                    //必须用字符串
                    bdPaymoneyEdit = new BigDecimal("0");
                }
                BigDecimal bdPaymoney = new BigDecimal(paymoney);
                BigDecimal b = bdPaymoneyEdit.subtract(bdPaymoney);//会输出0.1
                paymoneyreturnEdit.setText(String.valueOf(b));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

    //隐藏系统键盘
    private void hintSystemSoftKeyboard() {
        if (Build.VERSION.SDK_INT <= 10) {//4.0以下
            paymoneyEdit.setInputType(InputType.TYPE_NULL);
        } else {
            TrueMoneyActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
            case R.id.bt_confirm:
                loadingDialog.show();
                if (TextUtils.equals(activity, "SaoMaActivity")) {
                    uploadDate();
                } else if (TextUtils.equals(activity, "KaquanshoukuanSuccessActivity")) {//卡券收款成功
                    sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|123|" + memberNumber + "|24|" + paymoney + "||" + randomStr + miyao + "|tag_wx_card}";
                    Log.i("test", "sendData = " + sendData);
                    NetworkRequest mLoginRequest = new NetworkRequest(sendData);
                    mLoginRequest.start();
                    mLoginRequest.setListener(TrueMoneyActivity.this);
                }

                break;
            case R.id.bt_point:
                if (!paymoneyEdit.getText().toString().contains(".")) {
                    editable.insert(start, Character.toString('.'));
                }
                break;
            default:
                break;
        }
    }

    private void setStatus(String result) {
        loadingDialog.dismiss();
        Log.i("test", "saoma = result = " + result);
        String[] arr = null;
        if (result != null) {
            arr = result.replace("{", "").replace("}", "").split("\\|");
            if (arr[0].equals("0")) {
                if (TextUtils.equals(activity, "KaquanshoukuanSuccessActivity")) {//卡券收款成功
                    intent = new Intent(TrueMoneyActivity.this, HeXiaoSuccessActivity.class);
                    intent.putExtra("Activity", "KaquanshoukuanSuccessActivity");
                    intent.putExtra("Text_money", "￥" + arr[9]);
                    intent.putExtra("Text_one", arr[1]);
                    intent.putExtra("Text_two", arr[3]);
                    intent.putExtra("Text_three", arr[8]);
                    intent.putExtra("Text_four", arr[9]);
                    intent.putExtra("Text_five", arr[4]);
                    intent.putExtra("Text_shouyingyuan", appContext.getRealName());
                    startActivity(intent);
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(TrueMoneyActivity.this);
                builder.setTitle("充值失败");
                builder.setMessage(arr[1]);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        TrueMoneyActivity.this.finish();
                    }
                });
                Dialog noticeDialog = builder.create();
                noticeDialog.setCanceledOnTouchOutside(false);
                noticeDialog.show();
            }
        } else {
            ToastUtils.showToast(TrueMoneyActivity.this, "网络连接超时，请重试！");
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


    /**
     * 提交数据
     */
    private void uploadDate() {
        String randomString = getRandomString(8);
        String url = Constant.URL + "/api/Pay/CashPay";
        Map<String, Object> map = new HashMap<>();
        map.put("content", getContent(randomString));
        map.put("key", getMiyaoKey(randomString));
        map.put("Type", 4);
        map.put("OrderMoney", paymoney);
        map.put("PayMoney", paymoney);
        map.put("cash_fee", 0);
        final JSONObject obj = new JSONObject(map);

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, obj.toString());
        Request request = new Request.Builder().url(url).post(body).build();
        HttpUtils.postAsyn(TrueMoneyActivity.this, request, new HttpUtils.CallBack() {

            private String error;

            @Override
            public void onFailure(Request request, IOException e) {
                loadingDialog.dismiss();
                ToastUtils.showToast(TrueMoneyActivity.this, "网络连接异常，请重试");
                Log.e("test", "失败" + request);
            }

            @Override
            public void onResponse(String json) {
                loadingDialog.dismiss();
                Log.i("test", "json = " + json);
                try {
                    JSONObject object = new JSONObject(json);
                    String Result = object.optString("Result");
                    error = object.optString("Error");

                    if (TextUtils.equals(Result, "0")) {
                        String Data = object.optString("Data");
                        JSONObject sucess = new JSONObject(Data);
                        String total_fee = sucess.optString("Total_Fee");
                        String total_feeLast = sucess.optString("total_feeLast");
                        String orderID = sucess.optString("OrderID");
                        String payTime = sucess.optString("PayTime");

                        intent = new Intent(TrueMoneyActivity.this, HeXiaoSuccessActivity.class);
                        intent.putExtra("Activity", "PosActivity");
                        intent.putExtra("Text_money", "￥" + total_feeLast);
                        intent.putExtra("Text_one", total_fee);
                        intent.putExtra("Text_two", total_feeLast);
                        intent.putExtra("Text_three", orderID);
                        intent.putExtra("Text_four", payTime);
                        startActivity(intent);
                    } else {
                        Toast.makeText(TrueMoneyActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                    ToastUtils.showToast(TrueMoneyActivity.this, error);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
