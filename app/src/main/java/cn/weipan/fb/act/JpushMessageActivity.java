package cn.weipan.fb.act;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.common.Constant;
import cn.weipan.fb.utils.HttpUtils;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.ToastUtils;

/**
 * Created by cc on 2016/10/26.
 * 邮箱：904359289@QQ.com.
 * 推送消息详情页面
 */
public class JpushMessageActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.tv_success)
    TextView tvSuccess;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_paytime)
    TextView tvPaytime;
    @BindView(R.id.tv_paycode)
    TextView tvPaycode;
    @BindView(R.id.tv_paydanhao)
    TextView tvPaydanhao;
    @BindView(R.id.tv_paybianhao)
    TextView tvPaybianhao;
    @BindView(R.id.tv_payname)
    TextView tvPayname;
    private String danhao;
    private String payType;
    private int IntPayType;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpushmessage);
        ButterKnife.bind(this);
        initview();
    }

    //初始化界面
    private void initview() {
        headViewTitle.setText("订单详情");
        danhao = getIntent().getStringExtra("danhao");
        payType = getIntent().getStringExtra("payType");


        if (payType.startsWith("微信")) {
            IntPayType = 1;
            tvSuccess.setText("微信收款成功");
        } else if (payType.startsWith("支付宝")) {
            IntPayType = 2;
            tvSuccess.setText("支付宝收款成功");
        } else if (payType.startsWith("百度钱包")) {
            IntPayType = 3;
            tvSuccess.setText("百度钱包收款成功");
        } else if (payType.startsWith("现金")) {
            IntPayType = 4;
            tvSuccess.setText("现金收款成功");
        } else if (payType.startsWith("pos机")) {
            IntPayType = 5;
            tvSuccess.setText("pos机收款成功");
        } else if (payType.startsWith("QQ钱包")) {
            IntPayType = 6;
            tvSuccess.setText("QQ钱包收款成功");
        } else if (payType.startsWith("京东钱包")) {
            IntPayType = 7;
            tvSuccess.setText("京东钱包收款成功");
        } else {
            IntPayType = 1;
            tvSuccess.setText("收款成功");
        }

        getUserMassage();
        loadingDialog = new LoadingDialog(JpushMessageActivity.this, "加载中...");
        loadingDialog.show();
    }


    //获取账单详情
    private void getUserMassage() {
        String randomString = getRandomString(8);
        String url = Constant.URL + "/api/Pay/GetOrderDetail?content=" + getContent(randomString) + "&key=" + getMiyaoKey(randomString) + "&transaction_id=" + danhao + "&PayType=" + IntPayType;
        Log.i("test", "JpushMessageActivity = url = " + url);
        Request request = new Request.Builder().url(url).get().build();
        HttpUtils.getAsyn(JpushMessageActivity.this, request, new HttpUtils.CallBack() {
            private String error;
            private String Result;
            private String success;

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("test", "失败" + request);
                loadingDialog.dismiss();
            }

            @Override
            public void onResponse(String json) {
                loadingDialog.dismiss();
                Log.e("test", json);
                try {
                    JSONObject object = new JSONObject(json);
                    Result = object.optString("Result");
                    error = object.optString("Error");
                    if (TextUtils.equals(Result, "0")) {
                        success = object.optString("Data");
                        JSONObject jpush = new JSONObject(success);
                        String orderMoney = jpush.optString("OrderMoney");
                        String payMoney = jpush.optString("PayMoney");
                        String payUserCode = jpush.optString("PayUserCode");
                        String payTime = jpush.optString("PayTime");
                        String orderState = jpush.optString("OrderState");
                        String orderStateName = jpush.optString("OrderStateName");
                        String orderCode = jpush.optString("OrderCode");
                        String realName = jpush.optString("RealName");

                        tvPayname.setText(realName);
                        tvMoney.setText(payMoney);
                        tvPaytime.setText(payTime);
                        tvPaydanhao.setText(danhao);
                        tvPaybianhao.setText(orderCode);
                        tvPaycode.setText(orderStateName);

                    } else {
                        ToastUtils.showToast(JpushMessageActivity.this, error);
//                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @OnClick(R.id.ll_fanhui)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;

            default:
                break;
        }
    }
}
