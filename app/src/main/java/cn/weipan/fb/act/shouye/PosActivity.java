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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseActivity;
import cn.weipan.fb.constact.Constant;
import cn.weipan.fb.utils.HttpUtils;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.NetworkRequest;
import cn.weipan.fb.utils.ToastUtils;


/**
 * Pos机收款
 * Created by cc on 2016/10/12.
 * 邮箱：904359289@QQ.com.
 */
public class PosActivity extends BaseActivity implements NetworkRequest.ReponseListener, View.OnClickListener {
    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.paymoney_edit)
    TextView paymoneyEdit;
    @BindView(R.id.commit_rl)
    RelativeLayout commitRl;
    private String paymoney;
    private LoadingDialog loadingDialog;
    private String activity;
    private Intent intent;
    private String memberNumber;
    private String sendData;
    private String randomStr;
    private String miyao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);
        ButterKnife.bind(this);
        initView();
    }

    //初始化控件
    private void initView() {
        paymoney = getIntent().getStringExtra("paymoney");
        activity = getIntent().getStringExtra("Activity");
        memberNumber = getIntent().getStringExtra("memberNumber");
        headViewTitle.setText("刷卡收款");
        paymoneyEdit.setText("￥" + paymoney);
        loadingDialog = new LoadingDialog(PosActivity.this, "提交中...");
        randomStr = getRandomString(8);
        miyao = getMiyao(randomStr);
    }


    @OnClick({R.id.ll_fanhui, R.id.commit_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            case R.id.commit_rl:
                loadingDialog.show();

                if (TextUtils.equals(activity, "SaoMaActivity")) {
                    uploadDate();
                } else if (TextUtils.equals(activity, "KaquanshoukuanSuccessActivity")) {//卡券收款成功
                    sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|123|" + memberNumber + "|24|" + paymoney + "||" + randomStr + miyao + "|tag_wx_card}";
                    Log.i("test", "sendData = " + sendData);
                    NetworkRequest mLoginRequest = new NetworkRequest(sendData);
                    mLoginRequest.start();
                    mLoginRequest.setListener(PosActivity.this);
                } else if (TextUtils.equals(activity, "MemberIncomeSuccessActivity")) {//会员充值
                    sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|133|" + memberNumber + "|24|" + paymoney + "||" + randomStr + miyao + "|tag_wx_card}";
                    Log.i("test", "sendData = " + sendData);
                    NetworkRequest mLoginRequest = new NetworkRequest(sendData);
                    mLoginRequest.start();
                    mLoginRequest.setListener(PosActivity.this);
                }
                break;
        }
    }

    private void setStatus(String result) {//{0|150****9610|2016110221001004360237560288|2016-11-02 15:06:00|支付宝收款成功|63|0.01|tag_scan_ali}
//                                         //{0|oruCVt9kVBXNVDoX-6_YC-9aCGMw|4002632001201611038617028861|2016/11/3 16:00:30|微信收款成功|1402|0|tag_scan_wx}
//                                           {0|00001108000168852016120616183444|00000000006|ggg|2016/12/6 16:40:18|9978.00|6|金牌会员|tag_scan_vip}
        loadingDialog.dismiss();
        Log.i("test", "saoma = result = " + result);//{0|00000000006|ggg|普通会员|50.00|0|99|tag_scan_vip}
        String[] arr = null;
        if (result != null) {
            arr = result.replace("{", "").replace("}", "").split("\\|");
            if (arr[0].equals("0")) {
                if (TextUtils.equals(activity, "MemberIncomeSuccessActivity")) {///会员充值成功
                    intent = new Intent(PosActivity.this, HeXiaoSuccessActivity.class);
                    intent.putExtra("Activity", "MemberIncomeSuccessActivity");
                    intent.putExtra("Text_money", "￥" + arr[6]);
                    intent.putExtra("Text_one", arr[2]);
                    intent.putExtra("Text_two", arr[3]);
                    intent.putExtra("Text_three", arr[6]);
                    intent.putExtra("Text_four", arr[5]);
                    intent.putExtra("Text_five", arr[4]);
                    intent.putExtra("Text_shouyingyuan", appContext.getRealName());
                    startActivity(intent);
                } else if (TextUtils.equals(activity, "KaquanshoukuanSuccessActivity")) {//卡券收款成功
                    intent = new Intent(PosActivity.this, HeXiaoSuccessActivity.class);
                    intent.putExtra("Activity", "KaquanshoukuanSuccessActivity");
                    intent.putExtra("Text_money", "￥" + arr[9]);
                    intent.putExtra("Text_one", arr[1]);
                    intent.putExtra("Text_two", arr[3]);
                    intent.putExtra("Text_three", arr[8]);
                    intent.putExtra("Text_four", arr[9]);
                    intent.putExtra("Text_five", arr[4]);
//                    intent.putExtra("Text_money", "￥" + arr[9]);
//                    intent.putExtra("Text_one", arr[3]);
//                    intent.putExtra("Text_two", arr[9]);
//                    intent.putExtra("Text_three", arr[4]);
                    intent.putExtra("Text_shouyingyuan", appContext.getRealName());
                    startActivity(intent);
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(PosActivity.this);
                builder.setTitle("充值失败");
                builder.setMessage(arr[1]);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PosActivity.this.finish();
                    }
                });

                Dialog noticeDialog = builder.create();
                noticeDialog.setCanceledOnTouchOutside(false);
                noticeDialog.show();
            }
        }else {
            ToastUtils.showToast(PosActivity.this,"网络连接超时，请重试！");
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
        map.put("Type", 5);
        map.put("OrderMoney", paymoney);
        map.put("PayMoney", paymoney);
        map.put("cash_fee", 0);

        final JSONObject obj = new JSONObject(map);

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, obj.toString());
        Request request = new Request.Builder().url(url).post(body).build();
        HttpUtils.postAsyn(PosActivity.this, request, new HttpUtils.CallBack() {

            private String error;

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("test", "失败" + request);
                loadingDialog.dismiss();
                ToastUtils.showToast(PosActivity.this, "网络连接异常，请重试");
            }

            //请求成功返回的结果
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

                        Intent intent = new Intent(PosActivity.this, HeXiaoSuccessActivity.class);
                        intent.putExtra("Activity", "PosActivity");
                        intent.putExtra("Text_money", "￥" + total_feeLast);
                        intent.putExtra("Text_one", total_fee);
                        intent.putExtra("Text_two", total_feeLast);
                        intent.putExtra("Text_three", orderID);
                        intent.putExtra("Text_four", payTime);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PosActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
