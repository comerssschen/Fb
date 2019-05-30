package cn.weipan.fb.act.zhangdan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseActivity;
import cn.weipan.fb.common.CallClient;
import cn.weipan.fb.common.Constant;
import cn.weipan.fb.utils.LoadingDialog;

/**
 * 账单详情h5页面
 * Created by Administrator on 2016/10/13.
 */
public class BaseZhangDanWebActivity extends BaseActivity implements View.OnClickListener {
    private WebView mWebView;
    private String mUrl;
    private String nextUrl;
    private LoadingDialog loadingDialog;
    private String style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basezhangdanweb);
        initView();
    }


    //初始化
    private void initView() {
        loadingDialog = new LoadingDialog(BaseZhangDanWebActivity.this, "加载中...");

        String randomString = getRandomString(8);
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        nextUrl = getIntent().getStringExtra("urlnext");
        style = getIntent().getStringExtra("style");

        LinearLayout headerBack = (LinearLayout) findViewById(R.id.ll_fanhui);
        headerBack.setOnClickListener(this);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
        TextView tv_flashlight = (TextView) findViewById(R.id.tv_flashlight);
        tv_flashlight.setOnClickListener(this);
        if (TextUtils.isEmpty(nextUrl)) {
            tv_flashlight.setVisibility(View.GONE);
        }
        mUrl = Constant.H5URL + url + "?content=" + getContent(randomString) + "&key=" + getMiyaoKey(randomString) + "&CashType=" + appContext.getCashType();
        Log.i("test", "mUrl = " + mUrl);
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        //        LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(false);
        // // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //加速加载
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // // 设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setJavaScriptEnabled(true);

        //对webview的返回键进行监听
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                    mWebView.goBack();// 返回前一个页面
                    return true;
                }
                return false;
            }
        });

        mWebView.setWebViewClient(new CallClient(this));
        mWebView.requestFocus();
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //开始
                super.onPageFinished(view, url);
                loadingDialog.dismiss();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //结束
                super.onPageStarted(view, url, favicon);

                loadingDialog.show();
            }
        });


    }

    //返回键
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mWebView.goBack();// 返回前一个页面
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮关闭界面
            case R.id.ll_fanhui:
                finish();
                break;
            //右上角历史账单
            case R.id.tv_flashlight:
                Intent intent = new Intent(BaseZhangDanWebActivity.this, NextWebActivity.class);
                intent.putExtra("url", nextUrl);
                intent.putExtra("style", style);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
