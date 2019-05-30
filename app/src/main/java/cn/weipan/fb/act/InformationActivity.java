package cn.weipan.fb.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.bean.InformationBean;
import cn.weipan.fb.common.Constant;
import cn.weipan.fb.utils.CircleTransform;
import cn.weipan.fb.utils.HttpUtils;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.TakePictureManager;
import cn.weipan.fb.utils.ToastUtils;


/**
 * 个人信息
 * Created by cc on 2016/7/22.
 * 邮箱：904359289@QQ.com.
 */
public class InformationActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.iv_personal)
    ImageView ivPersonal;
    @BindView(R.id.rl_personal)
    RelativeLayout rlPersonal;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.tv_conpaney_name)
    TextView tvConpaneyName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.et_qqnumber)
    EditText etQqnumber;
    @BindView(R.id.rl_qq)
    RelativeLayout rlQq;
    @BindView(R.id.et_mail)
    EditText etMail;
    @BindView(R.id.rl_mail)
    RelativeLayout rlMail;
    @BindView(R.id.tv_agentname)
    TextView tvAgentname;
    @BindView(R.id.tv_addtime)
    TextView tvAddtime;
    @BindView(R.id.tv_endtime)
    TextView tvEndtime;
    @BindView(R.id.rl_finish)
    RelativeLayout rlFinish;
    private InputMethodManager imm;
    private LoadingDialog loadingDialog;
    private Bitmap head;
    private TakePictureManager takePictureManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infomation);
        ButterKnife.bind(this);
        initView();
        getUserMassage();
    }

    //初始化控件信息
    private void initView() {
        headViewTitle.setText("用户信息");
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        loadingDialog = new LoadingDialog(InformationActivity.this, "提交中...");
    }

    //获取用户信息
    private void getUserMassage() {
        String randomString = getRandomString(8);
        String url = Constant.URL + "/api/user/GetUserMessage?content=" + getContent(randomString) + "&key=" + getMiyaoKey(randomString);
        Request request = new Request.Builder().url(url).get().build();
        HttpUtils.getAsyn(InformationActivity.this, request, new HttpUtils.CallBack() {
            public String error;
            public InformationBean da;
            public String success;
            public String result;

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("ok", "失败=" + request);
            }

            @Override
            public void onResponse(String json) {
                Log.e("test", json);// {"Result":"0","Error":"成功","Data":{"UserID":"123456","CashType":"1","LoginName":"15824164947","Imageurl":"http://i.imgur.com/DvpvklR.png","Phone":"15824164947","QQ":"1031971893","Email":"1031971893@qq.com","AgentName":"杭州微盘代理商","AddTime":"2016-10-9","EndTime":"2016-10-9"}}
                try {
                    JSONObject object = new JSONObject(json);
                    result = object.optString("Result");
                    error = object.optString("Error");

                    if (TextUtils.equals(result, "0")) {
                        success = object.optString("Data");
                        Gson gson = new Gson();
                        da = gson.fromJson(success, InformationBean.class);
                        String imageUrl;
                        if (!TextUtils.isEmpty(da.getImageurl())) {
                            imageUrl = Constant.URL + da.getImageurl();
                            Picasso.with(InformationActivity.this).load(imageUrl).transform(new CircleTransform()).into(ivPersonal, new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError() {
                                    ivPersonal.setBackgroundResource(R.drawable.login_avatar);
                                }
                            });
                        }
                        String realName = da.getRealName();
                        String addTime = da.getAddTime();
                        String agentName = da.getAgentName();
                        String endTime = da.getEndTime();
                        String email = da.getEmail();
                        String phone = da.getPhone();
                        String qq = da.getQQ();
                        String siteName = da.getSiteName();

                        tvConpaneyName.setText(siteName);
                        username.setText(realName);
                        tvAgentname.setText(agentName);
                        tvAddtime.setText(addTime);
                        tvEndtime.setText(endTime);
                        tvPhone.setText(phone);
                        etQqnumber.setText(qq);
                        etMail.setText(email);
                    } else {
                        ToastUtils.showToast(InformationActivity.this, error);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 提交数据
     */
    private void uploadDate() {
        String randomString = getRandomString(8);
        String url = Constant.URL + "/api/user/UpdateUser";
        Map<String, Object> map = new HashMap<>();
        map.put("content", getContent(randomString));
        map.put("key", getMiyaoKey(randomString));
        map.put("QQ", etQqnumber.getText().toString().trim());
        map.put("Email", etMail.getText().toString().trim());

        if (null == head || head.getHeight() == 0 || head.getWidth() == 0) {

        } else {
            String bitmap2StrByBase64 = Bitmap2StrByBase64(head);
            map.put("Imageurl", bitmap2StrByBase64);
            map.put("ImageName", "1.png");
        }
        final JSONObject obj = new JSONObject(map);

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, obj.toString());
        Request request = new Request.Builder().url(url).post(body).build();
        HttpUtils.postAsyn(InformationActivity.this, request, new HttpUtils.CallBack() {

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("test", "失败" + request);
                loadingDialog.dismiss();
            }

            @Override
            public void onResponse(String json) {
                loadingDialog.dismiss();
                Log.e("test", json);//{"Result":"-1","Error":" 用户帐号不能为空!"}
                try {
                    JSONObject object = new JSONObject(json);
                    String secess = object.optString("Result");
                    if (TextUtils.equals(secess, "0")) {
                        finish();
                    }
                    String error = object.optString("Error");
                    ToastUtils.showToast(InformationActivity.this, error);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.ll_fanhui, R.id.rl_personal, R.id.rl_qq, R.id.rl_mail, R.id.rl_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            case R.id.rl_personal:
                takePictureManager = new TakePictureManager(InformationActivity.this, new TakePictureManager.takePictureCallBackListener() {
                    @Override
                    public void successful(Bitmap bitmap) {
                        head = bitmap;
                        ivPersonal.setImageBitmap(bitmap);//显示在iv上
                    }
                });
//                takePictureManager.setTailor(1, 1, 350, 350);
                takePictureManager.takePic();
                break;
            case R.id.rl_qq:
                etQqnumber.setEnabled(true);
                etQqnumber.setInputType(InputType.TYPE_CLASS_TEXT);
                imm.showSoftInput(etMail, 0); //第二次点击后显示软键盘
                etQqnumber.setSelection(etQqnumber.getText().length());  //光标移动最后
                break;
            case R.id.rl_mail:
                etMail.setEnabled(true);
                etMail.setInputType(InputType.TYPE_CLASS_TEXT);
                etMail.setSelection(etMail.getText().length());
                imm.showSoftInput(etMail, 0); //第二次点击后显示软键盘
                break;
            //提交
            case R.id.rl_finish:
                uploadDate();
                loadingDialog.show();
                finish();
                break;
        }
    }

    //把本地的onActivityResult()方法回调绑定到对象
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            takePictureManager.attachToActivityForResult(requestCode, resultCode, data);
        }
    }


}
