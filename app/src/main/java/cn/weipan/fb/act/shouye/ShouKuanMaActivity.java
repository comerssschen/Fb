package cn.weipan.fb.act.shouye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseActivity;
import cn.weipan.fb.act.ReceivablesSuccessActivity;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.NetworkRequest;
import cn.weipan.fb.utils.NetworkRequest222;
import cn.weipan.fb.utils.ToastUtils;


/**
 * 生成三种收款码
 * Created by cc on 2016/10/17.563
 * 邮箱：904359289@QQ.com.
 */
public class ShouKuanMaActivity extends BaseActivity implements NetworkRequest.ReponseListener, NetworkRequest222.ReponseListener {
    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.tv_tishiyu)
    TextView tvTishiyu;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    private String paymoney;
    private Bitmap encode;
    private String type1;
    private String danhao1;
    private LoadingDialog loadingDialog;
    private String sendDataWei;
    private NetworkRequest mLoginRequest;
    private String weiUrl;
    private String type;
    private String randomStr;
    private String miyao;
    private NetworkRequest222 mLoginRequest1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoukuanma);
        ButterKnife.bind(this);

        initView();
    }

    //初始化控件
    private void initView() {
        randomStr = getRandomString(8);
        miyao = getMiyao(randomStr);
        paymoney = getIntent().getStringExtra("paymoney");
        type = getIntent().getStringExtra("type");

        headViewTitle.setText("收款码收款");
        tvMoney.setText("￥" + paymoney);

        if (TextUtils.equals(type, "11")) {
            tvTishiyu.setText("微信支付收款");
        } else if (TextUtils.equals(type, "51")) {
            tvTishiyu.setText("支付宝收款");
        } else if (TextUtils.equals(type, "31")) {
            tvTishiyu.setText("百度钱包收款");
        } else if (TextUtils.equals(type, "72")) {
            tvTishiyu.setText("京东钱包收款");
        } else if (TextUtils.equals(type, "82")) {
            tvTishiyu.setText("QQ钱包收款");
        }
        loadingDialog = new LoadingDialog(ShouKuanMaActivity.this, "加载中...");
        loadingDialog.show();
        sendDataWei = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + type + "|" + paymoney + "|" + randomStr + miyao + "|tag_wx_qcode}";
        Log.i("test", "sendDataWei=========" + sendDataWei);
        mLoginRequest = new NetworkRequest(sendDataWei);
        mLoginRequest.start();
        mLoginRequest.setListener(ShouKuanMaActivity.this);

    }

    //请求回调结果
    private void setStatus(String result) {
        loadingDialog.dismiss();
        Log.i("test", "result =ShouKuanMaActivity " + result);//{0|12|00000419000130742016112816561544|weixin://wxpay/bizpayurl?pr=Z2HTLya|UCJTVXZU6D829B9B6A3E7E44|tag_wx_qcode}
        String[] arr = null;
        if (result != null) {
            arr = result.replace("{", "").replace("}", "").split("\\|");
            if (arr[0].equals("0")) {
                if (!TextUtils.isEmpty(arr[3])) {
                    type1 = arr[1];
                    danhao1 = arr[2];
                    weiUrl = arr[3];
                    encode = encode(weiUrl);
                    ivImage.setImageBitmap(encode);
                    if (!TextUtils.isEmpty(type1)) {
//            {sign|设备ID|签到人ID|操作类型|订单号|校验码}
                        String sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + type1 + "|" + danhao1 + "|" + randomStr + miyao + "|tag_searchorderstate_wx}";
                        Log.i("test", "sendData=========" + sendData);
                        NetworkRequest222 mLoginRequest = new NetworkRequest222(sendData);
                        mLoginRequest.start();
                        mLoginRequest.setListener(ShouKuanMaActivity.this);
                    }
                }
            } else {
                ivImage.setImageDrawable(getResources().getDrawable(R.drawable.transparents));
                ToastUtils.showToast(ShouKuanMaActivity.this, arr[1]);
            }
        }else {
            ToastUtils.showToast(ShouKuanMaActivity.this,"网络连接超时，请重试！");
        }
    }


    //被扫描回调结果
    private void setStatusHuitiao(String result) {//{0||00001108000168852016120914119726|2016/12/9 14:21:13|QQ钱包收款成功|83|0.01|OVMWXLYP616AD8560CDA3A33|tag_searchorderstate_qq}
        Log.i("test", "result =setStatusHuitiao " + result);
        String[] arr = null;
        if (result != null) {
            arr = result.replace("{", "").replace("}", "").split("\\|");
            if (arr[0].equals("0")) {
                Intent intent = new Intent(ShouKuanMaActivity.this, ReceivablesSuccessActivity.class);
                intent.putExtra("paymoney", arr[6]);
                intent.putExtra("payNumber", arr[1]);
                intent.putExtra("dingDanBianHao", arr[2]);
                intent.putExtra("Time", arr[3]);
                intent.putExtra("PayTitle", arr[4]);
                intent.putExtra("PayType", arr[5]);
                startActivity(intent);
                finish();
            } else if (arr[0].equals("7")) {
                try {
                    boolean finishing = ShouKuanMaActivity.this.isFinishing();
                    if (!finishing) {
                        Thread.sleep(1000);
                        String sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + type1 + "|" + danhao1 + "|" + randomStr + miyao + "|tag_searchorderstate_wx}";
                        Log.i("test", "sendData=========" + sendData);
                        mLoginRequest1 = new NetworkRequest222(sendData);
                        mLoginRequest1.start();
                        mLoginRequest1.setListener(ShouKuanMaActivity.this);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                ToastUtils.showToast(ShouKuanMaActivity.this, arr[1]);
            }
        } else {
            ToastUtils.showToast(ShouKuanMaActivity.this, "网络连接超时，请重试！");

        }
    }

    //将服务端返回的url装换成bitmap展示到页面
    private Bitmap encode(String url) {
        try {
            url = new String(url.getBytes("GBK"), "ISO-8859-1");
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 300, 300, hints);
            int width = 300;
            int height = 300;
            int[] pixels = new int[width * height];
            for (int y = 0; y < width; ++y) {
                for (int x = 0; x < height; ++x) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000; // black pixel
                    } else {
                        pixels[y * width + x] = 0xffffffff; // white pixel
                    }
                }
            }
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bmp.setPixels(pixels, 0, width, 0, 0, width, height);
            return bmp;
        } catch (Exception ex) {
            return null;
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
    Handler mHandler2 = new Handler() {
        public void handleMessage(Message msg) {
            // 处理逻辑
            String result = (String) msg.obj;
            setStatusHuitiao(result);
            super.handleMessage(msg);
        }
    };

    @Override
    public void onResult22(String result) {
        Message message = new Message();
        message.obj = result;
        mHandler2.sendMessage(message);
    }

    @Override
    public void onResult(String result) {
        Message message = new Message();
        message.obj = result;
        mHandler.sendMessage(message);
    }


    @OnClick(R.id.ll_fanhui)
    public void onClick() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
