package cn.weipan.fb.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ViewSwitcher;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class CallClient extends WebViewClient {

	private Context mContext;
	private ViewSwitcher mViewSwitcher;

	public CallClient(Context context) {
		this.mContext = context;
	}

	public CallClient(Context context, ViewSwitcher viewSwitcher) {
		this(context);
		this.mViewSwitcher = viewSwitcher;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, final String url) {
		if (url != null && !"".equals(url)) {
			if (url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
				mContext.startActivity(intent);
			} else {
				view.loadUrl(url);
			}
		}
		return true;
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		if (mViewSwitcher != null && mViewSwitcher.getDisplayedChild() == 0) {
			mViewSwitcher.showNext();
		}
	}
}
