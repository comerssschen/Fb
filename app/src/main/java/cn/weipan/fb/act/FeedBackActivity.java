package cn.weipan.fb.act;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.weipan.fb.R;
import cn.weipan.fb.constact.Constant;
import cn.weipan.fb.utils.HttpUtils;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.ToastUtils;

/**
 * 意见反馈
 * Created by cc on 2016/7/27.
 * 邮箱：904359289@QQ.com.
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener {
    private File fileSD;
    private File file;
    private ImageView imageTrouble;
    private static String path = "/sdcard/myHead/";//sd路径
    private Bitmap head;//头像Bitmap
    private EditText etText;
    private TextView hasnum;
    int num = 300;//限制的最大字数
    private String loginName;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initView();
    }

    //初始化界面
    private void initView() {
        loadingDialog = new LoadingDialog(FeedBackActivity.this, "提交中...");

        //照相机权限检查，提醒用户
        AndPermission.with(this)
                .requestCode(101)
                .permission(Manifest.permission.CAMERA)
                .send();


        LinearLayout fanhui = (LinearLayout) findViewById(R.id.ll_fanhui);
        fanhui.setOnClickListener(this);
        TextView header = (TextView) findViewById(R.id.head_view_title);
        header.setText("意见反馈");

        hasnum = (TextView) findViewById(R.id.tv_num);//字数记录
        etText = (EditText) findViewById(R.id.et_text);//输入的文本

        etText.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            //完全自定义的一个类部类
            @Override
            public void afterTextChanged(Editable s) {
                hasnum.setText(s.length() + "/" + num);
                selectionStart = etText.getSelectionStart();
                selectionEnd = etText.getSelectionEnd();
                if (temp.length() > num) {
                    Toast.makeText(FeedBackActivity.this, "已超出字数最大限制", Toast.LENGTH_SHORT).show();
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    etText.setText(s);
                    etText.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
        imageTrouble = (ImageView) findViewById(R.id.iv_trouble);
        imageTrouble.setOnClickListener(this);

        //保存
        RelativeLayout finish = (RelativeLayout) findViewById(R.id.rl_finish);
        finish.setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("userInfo", 0);
        loginName = sp.getString("LoginName", "");

    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.ll_fanhui:
                finish();
                break;
            case R.id.iv_trouble:
                new AlertDialog.Builder(FeedBackActivity.this).setCancelable(true).setTitle("选择来源").setItems(new String[]{"拍照", "图片库"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            takePhoto();
                        } else if (which == 1) {
                            Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                            intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(intent1, 1);
                        }
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.rl_finish:
                if (!TextUtils.isEmpty(etText.getText().toString())) {
                    loadingDialog.show();
                    uploadDate();
                } else {
                    ToastUtils.showToast(FeedBackActivity.this, "请输入反馈意见");
                }
                break;

            default:

                break;
        }
    }

    //将bitmap图片装换为base64的流传给服务端
    public String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        Log.i("test", "bytes = " + bytes.toString());
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 提交数据
     */
    private void uploadDate() {
        String randomString = getRandomString(8);
        String url = Constant.URL + "/api/Notices/AddAdvise";
        Map<String, Object> map = new HashMap<>();
        map.put("content", getContent(randomString));
        map.put("key", getMiyaoKey(randomString));
        map.put("Source", 1);
        map.put("CashName", loginName);
        map.put("Contents", etText.getText().toString());
        if (null == head || head.getHeight() == 0 || head.getWidth() == 0) {

        } else {
            String bitmap2StrByBase64 = Bitmap2StrByBase64(head);
            map.put("Img1", bitmap2StrByBase64);
        }
        final JSONObject obj = new JSONObject(map);
        Log.i("test", "obj = " + String.valueOf(obj));

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, obj.toString());

        Request request = new Request.Builder().url(url).post(body).build();
        HttpUtils.postAsyn(FeedBackActivity.this, request, new HttpUtils.CallBack() {

                    public String secess;
                    private String error;

                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.e("test", "失败" + request);
                        ToastUtils.showToast(FeedBackActivity.this, "网络连接异常，请重试");
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String json) {//{"Result":0,"Error":"成功"}

                        Log.i("test", "feedback = " + json);
                        try {
                            JSONObject object = new JSONObject(json);
                            secess = object.optString("Result");
                            if (TextUtils.equals(secess, "0")) {
                                finish();
                                loadingDialog.dismiss();
                            }
                            error = object.optString("Error");
                            Toast.makeText(FeedBackActivity.this, error, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    //系统相机拍照
    private void takePhoto() {
        String filepath = null;
        boolean sdExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (!sdExist) {
            return;
        } else {
            fileSD = new File(path);
            if (fileSD.exists()) {
                filepath = path + "/" + System.currentTimeMillis() + ".jpg";
            } else {
                fileSD.mkdir();
                filepath = fileSD.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
            }
            file = new File(filepath);
            // 启动Camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, 2);
        }
    }

    //回调结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    cropPhoto(Uri.fromFile(file));//裁剪图片
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        imageTrouble.setImageBitmap(head);//用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪
     * 调用系统的裁剪
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }
}
