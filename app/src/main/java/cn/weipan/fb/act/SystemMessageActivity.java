package cn.weipan.fb.act;

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
import cn.weipan.fb.common.CallClient;
import cn.weipan.fb.constact.Constant;
import cn.weipan.fb.utils.LoadingDialog;

/**
 * 消息详情界面
 * Created by cc on 2016/9/1.
 * 邮箱：904359289@QQ.com.
 */
public class SystemMessageActivity extends BaseActivity implements View.OnClickListener {

    private WebView mWebView;
    private String mUrl;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemmassage);
        initview();
    }

    //初始化界面
    private void initview() {
        loadingDialog = new LoadingDialog(SystemMessageActivity.this, "加载中...");

        String randomString = getRandomString(8);
        LinearLayout headerBack = (LinearLayout) findViewById(R.id.ll_fanhui);
        headerBack.setOnClickListener(this);
        TextView header = (TextView) findViewById(R.id.head_view_title);
        header.setText("活动消息");
        String url = getIntent().getStringExtra("url");
        Log.i("test", "url = " + url);
        mUrl = Constant.H5URL + url + "&content=" + getContent(randomString) + "&key=" + getMiyaoKey(randomString);
        Log.i("test", "mUrl = " + mUrl);
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(false);
        // // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //加速加载
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 设置支持缩放
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
        Log.i("test", "mURL" + Constant.H5URL + url);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
