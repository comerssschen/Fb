package cn.weipan.fb.act.shouye;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import cn.weipan.fb.act.LoginNewActivity;
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
public class ShouKuanMaCopyActivity extends BaseActivity implements NetworkRequest.ReponseListener, NetworkRequest222.ReponseListener, View.OnClickListener {
    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_tishiyu)
    TextView tvTishiyu;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.iv_weixin)
    ImageView ivWeixin;
    @BindView(R.id.iv_zhifubao)
    ImageView ivZhifubao;
    @BindView(R.id.iv_baiduqianbao)
    ImageView ivBaiduqianbao;
    @BindView(R.id.iv_jingdong)
    ImageView ivJingdong;
    @BindView(R.id.iv_qq)
    ImageView ivQq;
    private String paymoney;
    private Bitmap encode;
    private String type1;
    private String type2;
    private String type3;
    private String type4;
    private String type5;
    private String danhao1;
    private String danhao2;
    private String danhao3;
    private String danhao4;
    private String danhao5;
    private Intent intent;
    private LoadingDialog loadingDialog;
    private String sendDataWei;
    private String sendDataAli;
    private String sendDataBai;
    private String sendDataQQ;
    private String sendDataJD;
    private NetworkRequest mLoginRequest;
    private boolean weixinflag = true;
    private boolean zhiflag = true;
    private boolean baiflag = true;
    private boolean jingflag = true;
    private boolean qqflag = true;
    private String qqUrl;
    private String jingUrl;
    private String baiUrl;
    private String zhiUrl;
    private String weiUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoukuanma);
        ButterKnife.bind(this);
        paymoney = getIntent().getStringExtra("paymoney");
        initView();
    }

    //初始化控件
    private void initView() {

        headViewTitle.setText("收款码收款");
        tvMoney.setText("￥" + paymoney);
        ivWeixin.setSelected(true);
        loadingDialog = new LoadingDialog(ShouKuanMaCopyActivity.this, "加载中...");
        loadingDialog.show();
        String randomStr = getRandomString(8);
        String miyao = getMiyao(randomStr);
        sendDataWei = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + "11" + "|" + paymoney + "|" + randomStr + miyao + "|tag_wx_qcode}";
        sendDataAli = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + "51" + "|" + paymoney + "|" + randomStr + miyao + "|tag_ali_qcode}";
        sendDataBai = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + "31" + "|" + paymoney + "|" + randomStr + miyao + "|tag_bfb_qcode}";
        sendDataJD = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + "72" + "|" + paymoney + "|" + randomStr + miyao + "|tag_jd_qcode}";
        sendDataQQ = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + "82" + "|" + paymoney + "|" + randomStr + miyao + "|tag_qq_qcode}";
        Log.i("test", "sendData=========" + sendDataWei);
        mLoginRequest = new NetworkRequest(sendDataWei);
        mLoginRequest.start();
        mLoginRequest.setListener(ShouKuanMaCopyActivity.this);

    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public void setsize(View view, int size) {
        int mSize = dp2px(ShouKuanMaCopyActivity.this, size);

        LinearLayout.LayoutParams para = (LinearLayout.LayoutParams) view.getLayoutParams();

        para.height = mSize;
        para.width = mSize;
//        view.setTranslationX(0);
//        view.setTranslationY(0);
        view.setLayoutParams(para);
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
                    if (TextUtils.equals(arr[5], "tag_wx_qcode")) {
                        type1 = arr[1];
                        danhao1 = arr[2];
                        weiUrl = arr[3];
                        encode = encode(weiUrl);
                        weixinflag = false;
                        handler1.postDelayed(runnable1, 1000);
                    } else if (TextUtils.equals(arr[5], "tag_ali_qcode")) {
                        type2 = arr[1];
                        danhao2 = arr[2];
                        zhiUrl = arr[3];
                        encode = encode(zhiUrl);
                        zhiflag = false;
                        handler2.postDelayed(runnable2, 1000);
                    } else if (TextUtils.equals(arr[5], "tag_bfb_qcode")) {
                        type3 = arr[1];
                        danhao3 = arr[2];
                        baiUrl = arr[3];
                        encode = encode(baiUrl);
                        baiflag = false;
                        handler3.postDelayed(runnable3, 1000);
                    } else if (TextUtils.equals(arr[5], "tag_jd_qcode")) {
                        type4 = arr[1];
                        danhao4 = arr[2];
                        jingUrl = arr[3];
                        encode = encode(jingUrl);
//                        encode = getBitmapFromBase64(jingUrl);
                        jingflag = false;
                        handler4.postDelayed(runnable4, 1000);
                    } else if (TextUtils.equals(arr[5], "tag_qq_qcode")) {
                        type5 = arr[1];
                        danhao5 = arr[2];
                        qqUrl = arr[3];
                        encode = encode(qqUrl);
                        qqflag = false;
                        handler5.postDelayed(runnable5, 1000);
                    }
                    ivImage.setImageBitmap(encode);
                }
            } else {
                ivImage.setImageDrawable(getResources().getDrawable(R.drawable.transparents));
                ToastUtils.showToast(ShouKuanMaCopyActivity.this, arr[1]);
            }

        }
    }

    //退出登录
    private void LoginOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShouKuanMaCopyActivity.this);
        builder.setTitle("登录失效");
        builder.setMessage("登录信息已失效，请重新登录！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                intent = new Intent(ShouKuanMaCopyActivity.this, LoginNewActivity.class);
                intent.setAction("finish");
                sendBroadcast(intent);
                startActivity(intent);
                finish();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.setCanceledOnTouchOutside(false);
        noticeDialog.setCancelable(false);
        noticeDialog.show();
    }

    //被扫描回调结果
    private void setStatusHuitiao(String result) {//{0||00001108000168852016120914119726|2016/12/9 14:21:13|QQ钱包收款成功|83|0.01|OVMWXLYP616AD8560CDA3A33|tag_searchorderstate_qq}
        Log.i("test", "result =setStatusHuitiao " + result);
        String[] arr = null;
        if (result != null) {
            arr = result.replace("{", "").replace("}", "").split("\\|");
            if (arr[0].equals("0")) {
                //不在发送消息轮训去请求
                handler1.removeCallbacks(runnable1);
                handler2.removeCallbacks(runnable2);
                handler3.removeCallbacks(runnable3);
                handler4.removeCallbacks(runnable4);
                handler5.removeCallbacks(runnable5);

                ToastUtils.showToast(ShouKuanMaCopyActivity.this, "收款成功");
                Intent intent = new Intent(ShouKuanMaCopyActivity.this, ReceivablesSuccessActivity.class);
                intent.putExtra("paymoney", arr[6]);
                intent.putExtra("payNumber", arr[1]);
                intent.putExtra("dingDanBianHao", arr[2]);
                intent.putExtra("Time", arr[3]);
                intent.putExtra("PayTitle", arr[4]);
                intent.putExtra("PayType", arr[5]);
                startActivity(intent);
                finish();
            }
        }
    }
    // 把Base64转换成Bitmap
    public static Bitmap getBitmapFromBase64(String iconBase64) {
        byte[] bitmapArray = Base64.decode(iconBase64, Base64.DEFAULT);
        return BitmapFactory
                .decodeByteArray(bitmapArray, 0, bitmapArray.length);
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

    //每隔一秒发送一次请求循环查询（微信）
    Handler handler1 = new Handler();
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            handler1.postDelayed(this, 1000);
            String randomStr = getRandomString(8);
            String miyao = getMiyao(randomStr);
            if (!TextUtils.isEmpty(type1)) {
//            {sign|设备ID|签到人ID|操作类型|订单号|校验码}
                String sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + type1 + "|" + danhao1 + "|" + randomStr + miyao + "|tag_searchorderstate_wx}";
                Log.i("test", "sendData=========" + sendData);

                NetworkRequest222 mLoginRequest = new NetworkRequest222(sendData);
                mLoginRequest.start();
                mLoginRequest.setListener(ShouKuanMaCopyActivity.this);
            }
        }
    };

    //每隔一秒发送一次请求循环查询（支付宝）
    Handler handler2 = new Handler();
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            handler2.postDelayed(this, 1000);
            String randomStr = getRandomString(8);
            String miyao = getMiyao(randomStr);
            if (!TextUtils.isEmpty(type2)) {
                String sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + type2 + "|" + danhao2 + "|" + randomStr + miyao + "|tag_searchorderstate_ali}";
                Log.i("test", "sendData=========" + sendData);
                NetworkRequest222 mLoginRequest = new NetworkRequest222(sendData);
                mLoginRequest.start();
                mLoginRequest.setListener(ShouKuanMaCopyActivity.this);
            }
        }
    };

    //每隔一秒发送一次请求循环查询（百度钱包）
    Handler handler3 = new Handler();
    Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            handler3.postDelayed(this, 1000);
            String randomStr = getRandomString(8);
            String miyao = getMiyao(randomStr);
            if (!TextUtils.isEmpty(type3)) {
                String sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + type3 + "|" + danhao3 + "|" + randomStr + miyao + "|tag_searchorderstate_bfb}";
                Log.i("test", "sendData=========" + sendData);
                NetworkRequest222 mLoginRequest = new NetworkRequest222(sendData);
                mLoginRequest.start();
                mLoginRequest.setListener(ShouKuanMaCopyActivity.this);
            }
        }
    };
    //每隔一秒发送一次请求循环查询（百度钱包）
    Handler handler4 = new Handler();
    Runnable runnable4 = new Runnable() {
        @Override
        public void run() {
            handler4.postDelayed(this, 1000);
            String randomStr = getRandomString(8);
            String miyao = getMiyao(randomStr);
            if (!TextUtils.isEmpty(type4)) {
                String sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + type4 + "|" + danhao4 + "|" + randomStr + miyao + "|tag_searchorderstate_jd}";
                Log.i("test", "sendData=========" + sendData);
                NetworkRequest222 mLoginRequest = new NetworkRequest222(sendData);
                mLoginRequest.start();
                mLoginRequest.setListener(ShouKuanMaCopyActivity.this);
            }
        }
    };
    //每隔一秒发送一次请求循环查询（百度钱包）
    Handler handler5 = new Handler();
    Runnable runnable5 = new Runnable() {
        @Override
        public void run() {
            handler5.postDelayed(this, 1000);
            String randomStr = getRandomString(8);
            String miyao = getMiyao(randomStr);
            if (!TextUtils.isEmpty(type5)) {
                String sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + type5 + "|" + danhao5 + "|" + randomStr + miyao + "|tag_searchorderstate_qq}";
                Log.i("test", "sendData=========" + sendData);
                NetworkRequest222 mLoginRequest = new NetworkRequest222(sendData);
                mLoginRequest.start();
                mLoginRequest.setListener(ShouKuanMaCopyActivity.this);
            }
        }
    };
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

    //页面销毁的时候不在发送请求
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler1.removeCallbacks(runnable1);
        handler2.removeCallbacks(runnable2);
        handler3.removeCallbacks(runnable3);
        handler4.removeCallbacks(runnable4);
        handler5.removeCallbacks(runnable5);
    }

    @OnClick({R.id.ll_fanhui, R.id.iv_weixin, R.id.iv_zhifubao, R.id.iv_baiduqianbao, R.id.iv_jingdong, R.id.iv_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            //生成微信二维码
            case R.id.iv_weixin:
                ivImage.setImageDrawable(getResources().getDrawable(R.drawable.transparents));
//                ivImage.setBackgroundResource(R.drawable.transparents);

                if (weixinflag) {
                    loadingDialog.show();
                    mLoginRequest = new NetworkRequest(sendDataWei);
                    mLoginRequest.start();
                    mLoginRequest.setListener(ShouKuanMaCopyActivity.this);
                } else {
                    encode = encode(weiUrl);
                    ivImage.setImageBitmap(encode);
                }
                ivWeixin.setSelected(true);
                ivZhifubao.setSelected(false);
                ivBaiduqianbao.setSelected(false);
                ivJingdong.setSelected(false);
                ivQq.setSelected(false);

                tvTishiyu.setText("请客户使用微信扫码完成收款");

                setsize(ivWeixin, 60);
                setsize(ivBaiduqianbao, 50);
                setsize(ivZhifubao, 50);
                setsize(ivJingdong, 50);
                setsize(ivQq, 50);


                break;
            //生成支付宝二维码
            case R.id.iv_zhifubao:
                ivImage.setImageDrawable(getResources().getDrawable(R.drawable.transparents));
                if (zhiflag) {
                    loadingDialog.show();
                    mLoginRequest = new NetworkRequest(sendDataAli);
                    mLoginRequest.start();
                    mLoginRequest.setListener(ShouKuanMaCopyActivity.this);
                } else {
                    encode = encode(zhiUrl);
                    ivImage.setImageBitmap(encode);
                }
                ivZhifubao.setSelected(true);
                ivWeixin.setSelected(false);
                ivBaiduqianbao.setSelected(false);
                ivJingdong.setSelected(false);
                ivQq.setSelected(false);

                setsize(ivBaiduqianbao, 50);
                setsize(ivWeixin, 50);
                setsize(ivZhifubao, 60);
                setsize(ivJingdong, 50);
                setsize(ivQq, 50);
                tvTishiyu.setText("请客户使用支付宝扫码完成收款");

                break;

            //生成百度钱包二维码
            case R.id.iv_baiduqianbao:
                ivImage.setImageDrawable(getResources().getDrawable(R.drawable.transparents));
                if (baiflag) {
                    loadingDialog.show();
                    mLoginRequest = new NetworkRequest(sendDataBai);
                    mLoginRequest.start();
                    mLoginRequest.setListener(ShouKuanMaCopyActivity.this);
                } else {
                    encode = encode(baiUrl);
                    ivImage.setImageBitmap(encode);
                }
                setsize(ivBaiduqianbao, 60);
                setsize(ivWeixin, 50);
                setsize(ivZhifubao, 50);
                setsize(ivJingdong, 50);
                setsize(ivQq, 50);

                ivBaiduqianbao.setSelected(true);
                ivWeixin.setSelected(false);
                ivZhifubao.setSelected(false);
                ivJingdong.setSelected(false);
                ivQq.setSelected(false);
                tvTishiyu.setText("请客户使用百度钱包扫码完成收款");

                break;

            //生成京东钱包二维码
            case R.id.iv_jingdong:
                ivImage.setImageDrawable(getResources().getDrawable(R.drawable.transparents));
                if (jingflag) {
                    loadingDialog.show();
                    mLoginRequest = new NetworkRequest(sendDataJD);
                    mLoginRequest.start();
                    mLoginRequest.setListener(ShouKuanMaCopyActivity.this);
                } else {
                    encode = encode(jingUrl);

//                    encode = getBitmapFromBase64(jingUrl);
                    ivImage.setImageBitmap(encode);
                }
                setsize(ivJingdong, 60);
                setsize(ivQq, 50);
                setsize(ivBaiduqianbao, 50);
                setsize(ivWeixin, 50);
                setsize(ivZhifubao, 50);

                ivBaiduqianbao.setSelected(false);
                ivWeixin.setSelected(false);
                ivZhifubao.setSelected(false);
                ivJingdong.setSelected(true);
                ivQq.setSelected(false);

                tvTishiyu.setText("请客户使用京东钱包扫码完成收款");

                break;

            //生成QQ钱包二维码
            case R.id.iv_qq:
                ivImage.setImageDrawable(getResources().getDrawable(R.drawable.transparents));
                if (qqflag) {
                    loadingDialog.show();
                    mLoginRequest = new NetworkRequest(sendDataQQ);
                    mLoginRequest.start();
                    mLoginRequest.setListener(ShouKuanMaCopyActivity.this);
                } else {
                    encode = encode(qqUrl);
                    ivImage.setImageBitmap(encode);
                }
                setsize(ivJingdong, 50);
                setsize(ivQq, 60);
                setsize(ivBaiduqianbao, 50);
                setsize(ivWeixin, 50);
                setsize(ivZhifubao, 50);

                ivBaiduqianbao.setSelected(false);
                ivWeixin.setSelected(false);
                ivZhifubao.setSelected(false);
                ivJingdong.setSelected(false);
                ivQq.setSelected(true);
                tvTishiyu.setText("请客户使用QQ钱包扫码完成收款");

                break;
        }
    }
}
