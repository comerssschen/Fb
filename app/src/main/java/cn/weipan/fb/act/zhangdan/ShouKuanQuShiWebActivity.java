package cn.weipan.fb.act.zhangdan;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseActivity;
import cn.weipan.fb.common.CallClient;
import cn.weipan.fb.constact.Constant;
import cn.weipan.fb.utils.LoadingDialog;

/**
 * 收款趋势H5页面
 * Created by Administrator on 2016/10/13.
 */
public class ShouKuanQuShiWebActivity extends BaseActivity implements View.OnClickListener {
    private WebView mWebView;
    private String mUrl;
    private String nextUrl;
    private TextView tv_left;
    private TextView tv_right;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basewebqushi);
        initView();
    }

    private void initView() {

        loadingDialog = new LoadingDialog(ShouKuanQuShiWebActivity.this, "加载中...");


        String randomString = getRandomString(8);
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        String urlnext = getIntent().getStringExtra("urlnext");
        LinearLayout headerBack = (LinearLayout) findViewById(R.id.ll_fanhui);
        headerBack.setOnClickListener(this);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_left.setOnClickListener(this);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setOnClickListener(this);
        tv_left.setSelected(true);
        tv_right.setSelected(false);

        mUrl = Constant.H5URL + url + "?content=" + getContent(randomString) + "&key=" + getMiyaoKey(randomString) + "&CashType=" + appContext.getCashType();
        nextUrl = Constant.H5URL + urlnext + "?content=" + getContent(randomString) + "&key=" + getMiyaoKey(randomString) + "&CashType=" + appContext.getCashType();
        Log.i("test", "mUrl = " + mUrl);
        Log.i("test", "nextUrl = " + nextUrl);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            case R.id.tv_left:
                tv_left.setSelected(true);
                tv_right.setSelected(false);
                mWebView.loadUrl(mUrl);
                break;
            case R.id.tv_right:
                tv_left.setSelected(false);
                tv_right.setSelected(true);
                mWebView.loadUrl(nextUrl);
                break;
            default:
                break;
        }
    }
}
