package cn.weipan.fb.common;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.AppUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import cn.weipan.fb.R;
import cn.weipan.fb.service.ParseXmlService;
import cn.weipan.fb.utils.DownloadUtil;


/**
 * Created by Administrator on 2016/8/2.
 */
public class UpdateManager {
    HashMap<String, String> mHashMap;
    private Context mContext;
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    //检查更新
    public void checkUpdate() {
        if (isUpdate()) {
            showNoticeDialog();
        } else {
        }
    }

    private boolean isUpdate() {
        int versionCode = AppUtils.getAppVersionCode();
        URL url = null;
        try {
            url = new URL("http://www.payweipan.com/androidfb/versioncode.xml");
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        conn.setConnectTimeout(5000);
        InputStream is = null;
        try {
            is = conn.getInputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ParseXmlService service = new ParseXmlService();
        try {
            mHashMap = service.parseXml(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != mHashMap) {
            int serviceCode = Integer.valueOf(mHashMap.get("version"));
            String text = mHashMap.get("name");
            Log.i("test", "serviceCode==================" + serviceCode);//2
            Log.i("test", "versionCode==================" + versionCode);//1
            Log.i("test", "text==================" + text);//1
            if (serviceCode > versionCode) {
                return true;
            }
        }
        return false;
    }

    private void showNoticeDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("更新提示");
        builder.setMessage("每日付有新的版本,请及时更新");
        builder.setPositiveButton("立即更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.setCanceledOnTouchOutside(false);
        noticeDialog.show();
    }

    private void showDownloadDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("更新中");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.soft_update_cancel);
        builder.setView(v);
        mDownloadDialog = builder.create();
        mDownloadDialog.setCanceledOnTouchOutside(false);
        mDownloadDialog.show();
        downloadApk();
    }

    private void downloadApk() {
        DownloadUtil.get().download(mHashMap.get("url"), "download/fb", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                mDownloadDialog.dismiss();
                AppUtils.installApp(new File(Environment.getExternalStorageDirectory() + "/download/fb/Fb.apk"));
            }

            @Override
            public void onDownloading(int progress) {

                mProgress.setProgress(progress);
            }

            @Override
            public void onDownloadFailed() {
                mDownloadDialog.dismiss();
            }
        });
    }


}
