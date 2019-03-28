package cn.weipan.fb.act.shouye;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseActivity;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.ToastUtils;


/**
 * 智慧码
 * Created by cc on 2016/10/11.
 * 邮箱：904359289@QQ.com.
 */
public class ZhiHuiMaActivity extends BaseActivity implements View.OnClickListener {

    private Window dialogWindow;
    private Dialog dialog;
    private Bitmap bitmap;
    private String smallUrl;
    private String BigImageUrl;
    private LoadingDialog loadingDialog;
    private LoadingDialog ewmDialog;
    private String deviceId;
    private File fileImage;
    private String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/FB_ZHM/";
    private Bitmap bigBitmap;
    private ImageView imageViewEWM;
    private String qcodeurlshow;
    private int GETPIC_OK = 0x123;
    private Bitmap mergeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihuima);
        dialog = new Dialog(ZhiHuiMaActivity.this, R.style.dialog);
        dialog.setContentView(R.layout.dialog);

        deviceId = appContext.getDeviceId();
        initView();
        dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);

        loadingDialog = new LoadingDialog(ZhiHuiMaActivity.this, "保存中...");


        ewmDialog = new LoadingDialog(ZhiHuiMaActivity.this, "加载中...");
        ewmDialog.show();
    }

    //初始化界面
    private void initView() {
        LinearLayout fanhui = (LinearLayout) findViewById(R.id.ll_fanhui);
        fanhui.setOnClickListener(this);
        ImageView tv_flashlight = (ImageView) findViewById(R.id.tv_flashlight);
        tv_flashlight.setOnClickListener(this);
        imageViewEWM = (ImageView) findViewById(R.id.iv_image_ewm);
        smallUrl = getIntent().getStringExtra("qcodeurl");

        BigImageUrl = getIntent().getStringExtra("bgqcodeurl");
        qcodeurlshow = getIntent().getStringExtra("qcodeurlshow");

        //保存图片到本地
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                URL url;
                URL bigUrl;
                try {
                    url = new URL(qcodeurlshow);
//                    url = new URL(smallUrl);
                    InputStream is = url.openStream();
//                    bitmap = converToBitmap(is);
                    bitmap = BitmapFactory.decodeStream(is);
                    handler.sendEmptyMessage(GETPIC_OK);
                    Bitmap scaleImg = scaleImg(bitmap, 496, 496);

                    bigUrl = new URL(BigImageUrl);
                    InputStream bigIs = bigUrl.openStream();
//                    bigBitmap = converToBitmap(bigIs);
                    bigBitmap = BitmapFactory.decodeStream(bigIs);

                    mergeBitmap = mergeBitmap(bigBitmap, scaleImg);

                    onCreateSaveFile(mergeBitmap, deviceId + "_bigzhm.jpg");

                    is.close();
                    Looper.loop();

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }).start();

        RelativeLayout quxiao = (RelativeLayout) dialog.findViewById(R.id.rl_quxiao);
        quxiao.setOnClickListener(this);
        LinearLayout ll_weixin = (LinearLayout) dialog.findViewById(R.id.ll_weixin);

        ll_weixin.setOnClickListener(this);
        LinearLayout ll_pengyouquan = (LinearLayout) dialog.findViewById(R.id.ll_pengyouquan);
        ll_pengyouquan.setOnClickListener(this);
        LinearLayout ll_qq = (LinearLayout) dialog.findViewById(R.id.ll_qq);
        ll_qq.setOnClickListener(this);
        LinearLayout ll_qqkongjian = (LinearLayout) dialog.findViewById(R.id.ll_qqkongjian);
        ll_qqkongjian.setOnClickListener(this);
        LinearLayout ll_baocun = (LinearLayout) dialog.findViewById(R.id.ll_baocun);
        ll_baocun.setOnClickListener(this);

        String testImagePath = ALBUM_PATH + (deviceId + "_bigzhm.jpg");
        fileImage = new File(testImagePath);

    }

    protected Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
        // 图片源
        // Bitmap bm = BitmapFactory.decodeStream(getResources()
        // .openRawResource(id));
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 设置想要的大小
        int newWidth1 = newWidth;
        int newHeight1 = newHeight;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth1) / width;
        float scaleHeight = ((float) newHeight1) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x123) {
                imageViewEWM.setImageBitmap(bitmap);
                ewmDialog.dismiss();
            }
        }
    };

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            //右上角点击弹出分享对话框
            case R.id.tv_flashlight:
                dialog.show();
                break;
            //取消
            case R.id.rl_quxiao:
                dialog.dismiss();
                break;
            case R.id.ll_weixin:
                shareImg("com.tencent.mm",
                        "com.tencent.mm.ui.tools.ShareImgUI", fileImage);
                dialog.dismiss();
                break;
            case R.id.ll_pengyouquan:
                shareImg("com.tencent.mm",
                        "com.tencent.mm.ui.tools.ShareToTimeLineUI", fileImage);
                dialog.dismiss();
                break;
            case R.id.ll_qq:
                shareImg("com.tencent.mobileqq",
                        "com.tencent.mobileqq.activity.JumpActivity", fileImage);
                dialog.dismiss();
                break;
            case R.id.ll_qqkongjian:
                shareImg("com.qzone.activity",
                        "com.qzone.ui.operation.QZonePublishMoodActivity", fileImage);
                dialog.dismiss();
                break;
            case R.id.ll_baocun:
                dialog.dismiss();
                loadingDialog.show();
//                shareToWxFriend(fileImage);
                //保存图片到本地
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        URL url;
                        try {
//                            url = new URL(BigImageUrl);
//                            InputStream is = url.openStream();
//                            bitmap = BitmapFactory.decodeStream(is);
                            saveFile(mergeBitmap, deviceId + "_bigzhm.jpg");
//                            is.close();
                            Looper.loop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            default:
                break;
        }
    }

//    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/FB_ZHM/";

    // 指定保存的路径：
    public void saveFile(Bitmap bm, String fileName) {
        try {
            File dirFile = new File(ALBUM_PATH);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            File myCaptureFile = new File(ALBUM_PATH + fileName);
            BufferedOutputStream bos = null;

            bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            loadingDialog.dismiss();
            ToastUtils.showToast(ZhiHuiMaActivity.this, "保存成功！");

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(myCaptureFile);
            intent.setData(uri);
            ZhiHuiMaActivity.this.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
            loadingDialog.dismiss();
            ToastUtils.showToast(ZhiHuiMaActivity.this, "保存失败，请联系管理员");
        }
    }

    // 指定保存的路径：
    public void onCreateSaveFile(Bitmap bm, String fileName) {

        try {
            File dirFile = new File(ALBUM_PATH);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            File myCaptureFile = new File(ALBUM_PATH + fileName);
            BufferedOutputStream bos = null;

            bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(myCaptureFile);
            intent.setData(uri);
            ZhiHuiMaActivity.this.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享图片给微信好友
     *
     * @param file
     */
    private void shareToWxFriend(File file) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(intent);
    }

    //分享到第三方
    private void shareImg(String packageName, String className, File file) {
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            if (stringCheck(packageName) && stringCheck(className)) {
                intent.setComponent(new ComponentName(packageName, className));
            } else if (stringCheck(packageName)) {
                intent.setPackage(packageName);
            }
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            Intent chooserIntent = Intent.createChooser(intent, "分享到:");
            startActivity(chooserIntent);
        } else {
            Toast.makeText(ZhiHuiMaActivity.this, "文件不存在,请先保存文件", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean stringCheck(String str) {
        return null != str && !TextUtils.isEmpty(str);
    }

    //图片合成
    private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        Bitmap bitmap3 = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(), firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap3);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        canvas.drawBitmap(secondBitmap, 343, 430, null);  //120、350为bitmap2写入点的x、y坐标
        return bitmap3;
    }

    //InputStream转bitmap
    private Bitmap converToBitmap(InputStream is) {
        WindowManager wm = this.getWindowManager();
        int windowWidth = wm.getDefaultDisplay().getWidth();
        int windowHeight = wm.getDefaultDisplay().getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        int scaleWidth = options.outWidth / windowWidth;
        int scaleHeight = options.outHeight / windowHeight;
        options.inSampleSize = scaleWidth > scaleHeight ? scaleWidth : scaleHeight;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        return bitmap;
    }
}
