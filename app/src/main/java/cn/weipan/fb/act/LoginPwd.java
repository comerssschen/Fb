package cn.weipan.fb.act;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.common.Constant;
import cn.weipan.fb.utils.HttpUtils;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.ToastUtils;

/**
 * 修改登录密码
 * Created by cc on 2016/7/22.
 * 邮箱：904359289@QQ.com.
 */
public class LoginPwd extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.pwd_edit)
    EditText pwdEdit;
    @BindView(R.id.pwd_new)
    EditText pwdNew;
    @BindView(R.id.pwd_switch)
    ImageView pwdSwitch;
    @BindView(R.id.save_pwd)
    RelativeLayout savePwd;
    private boolean isHidden = false;
    private String loginName;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pwd);
        ButterKnife.bind(this);
        initView();
    }

    //初始化控件
    private void initView() {
        loadingDialog = new LoadingDialog(LoginPwd.this, "提交中...");

        LinearLayout fanhui = (LinearLayout) findViewById(R.id.ll_fanhui);
        fanhui.setOnClickListener(this);
        TextView header = (TextView) findViewById(R.id.head_view_title);
        header.setText("修改登录密码");
        pwdEdit.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        pwdNew.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        pwdNew.setTransformationMethod(PasswordTransformationMethod.getInstance());

        SharedPreferences sp = getSharedPreferences("userInfo", 0);
        loginName = sp.getString("LoginName", "");

    }

    /**
     * 提交数据
     */
    private void uploadDate() {
        String randomString = getRandomString(8);
        String url = Constant.URL + "/api/user/ChangePassword";
        Map<String, Object> map = new HashMap<>();
        map.put("content", getContent(randomString));
        map.put("key", getMiyaoKey(randomString));
        map.put("LoginName", loginName);
        map.put("Type", 0);
        map.put("OldPwd", pwdEdit.getText().toString());
        map.put("NewPwd", pwdNew.getText().toString());
        final JSONObject obj = new JSONObject(map);

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, obj.toString());
        Request request = new Request.Builder().url(url).post(body).build();
        HttpUtils.postAsyn(LoginPwd.this, request, new HttpUtils.CallBack() {
            public String success;
            public String error;

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("ok", "失败=" + request);
                loadingDialog.dismiss();
                ToastUtils.showToast(LoginPwd.this, "网络连接异常，请重试");
            }

            @Override
            public void onResponse(String json) {
                loadingDialog.dismiss();
                Log.i("test", "ChangePassword = " + json);
                try {
                    JSONObject object = new JSONObject(json);
                    String result = object.optString("Result");
                    if (TextUtils.equals(result, "0")) {
                        SPUtils.getInstance().put("pwd", pwdNew.getText().toString().trim());
                        finish();
                    }
                    error = object.optString("Error");
                    Toast.makeText(LoginPwd.this, error, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick({R.id.ll_fanhui, R.id.pwd_switch, R.id.save_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            case R.id.pwd_switch:
                if (isHidden) {
                    //设置EditText文本为可见的
                    pwdSwitch.setImageResource(R.drawable.eyeselecte);
                    pwdNew.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置EditText文本为隐藏的
                    pwdSwitch.setImageResource(R.drawable.eye);
                    pwdNew.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                isHidden = !isHidden;
                pwdNew.postInvalidate();
                //切换后将EditText光标置于末尾
                CharSequence charSequence = pwdEdit.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }

                break;
            case R.id.save_pwd:
                if (!TextUtils.isEmpty(pwdEdit.getText().toString()) && !TextUtils.isEmpty(pwdNew.getText().toString())) {
                    loadingDialog.show();
                    uploadDate();
                } else {
                    Toast.makeText(LoginPwd.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
    }
}
