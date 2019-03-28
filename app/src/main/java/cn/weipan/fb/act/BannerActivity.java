package cn.weipan.fb.act;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.weipan.fb.R;
import cn.weipan.fb.common.CallClient;
import cn.weipan.fb.utils.LoadingDialog;

/**
 * h5基类
 * Created by cc on 2016/11/7.
 * 邮箱：904359289@QQ.com.
 */
public class BannerActivity extends BaseActivity implements View.OnClickListener {
    private WebView mWebView;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseweb);
        initView();
    }

    private void initView() {
        LinearLayout headerBack = (LinearLayout) findViewById(R.id.ll_fanhui);
        headerBack.setOnClickListener(this);
        TextView header = (TextView) findViewById(R.id.head_view_title);
        header.setText("活动详情");
        String url = getIntent().getStringExtra("url");

        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH); //加速加载
        webSettings.setGeolocationEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webSettings.setSupportZoom(true);//是否可以缩放，默认true
        webSettings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        webSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setAppCacheEnabled(true);//是否使用缓存
        webSettings.setDomStorageEnabled(true);//DOM Storage
        webSettings.setUserAgentString("User-Agent:Android");//设置用户代理，一般不用    (51广告图必须加上这一句)
        mWebView.setWebViewClient(new CallClient(this));
        loadingDialog = new LoadingDialog(BannerActivity.this, "加载中...");
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.requestFocus();
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.endsWith(".apk")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        return false;
                    }

                } else {
                    if (url.startsWith("http://") || url.startsWith("https://")) {
//                        mWebView.loadUrl(url);
                        view.loadUrl(url);
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
//                            mWebView.goBack();// 返回前一个页面
                            e.printStackTrace();
                            return false;
                        }
                    }
                }

                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受所有证书
            }

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


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;

            default:
                break;
        }
    }
}

