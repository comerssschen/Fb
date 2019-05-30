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

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.weipan.fb.R;
import cn.weipan.fb.common.Constant;
import cn.weipan.fb.utils.HttpUtils;
import cn.weipan.fb.utils.LoadingDialog;

/**
 * Created by cc on 2016/7/22.
 * 邮箱：904359289@QQ.com.
 * 退款密码
 */
public class ReturnMoneyPwd extends BaseActivity implements View.OnClickListener {

    private boolean isHidden = false;
    private EditText pwdEdit;
    private ImageView pwdSwitch;
    private EditText pwdNew;
    private String loginName;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_money_pwd);
        initView();
    }

    //初始化界面
    private void initView() {
        LinearLayout fanhui = (LinearLayout) findViewById(R.id.ll_fanhui);
        fanhui.setOnClickListener(this);
        TextView header = (TextView) findViewById(R.id.head_view_title);
        header.setText("修改退款密码");

        pwdNew = (EditText) findViewById(R.id.pwd_new);
        pwdNew.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        pwdNew.setTransformationMethod(PasswordTransformationMethod.getInstance());

        pwdEdit = (EditText) findViewById(R.id.pwd_edit);
        pwdEdit.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        pwdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        pwdSwitch = (ImageView) findViewById(R.id.pwd_switch);
        pwdSwitch.setOnClickListener(this);
        //保存
        RelativeLayout savePwd = (RelativeLayout) findViewById(R.id.save_repwd);
        savePwd.setOnClickListener(this);
        SharedPreferences sp = getSharedPreferences("userInfo", 0);
        loginName = sp.getString("LoginName", "");

        loadingDialog = new LoadingDialog(ReturnMoneyPwd.this, "提交中...");

    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            //设置输入的密码是否可见
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
                CharSequence charSequence = pwdNew.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
                break;
            case R.id.save_repwd:
                if (!TextUtils.isEmpty(pwdEdit.getText().toString()) && !TextUtils.isEmpty(pwdNew.getText().toString())) {
                    loadingDialog.show();
                    uploadDate();
                } else {
                    Toast.makeText(ReturnMoneyPwd.this, "请输入有效密码！", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
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
        map.put("Type", 1);
        map.put("OldPwd", pwdEdit.getText().toString());
        map.put("NewPwd", pwdNew.getText().toString());
        final JSONObject obj = new JSONObject(map);

        Log.i("test", "obj = " + String.valueOf(obj));
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, obj.toString());
        Request request = new Request.Builder().url(url).post(body).build();
        HttpUtils.postAsyn(ReturnMoneyPwd.this, request, new HttpUtils.CallBack() {
            public String error;

            @Override
            public void onFailure(Request request, IOException e) {
                loadingDialog.dismiss();
                Toast.makeText(ReturnMoneyPwd.this, "提交失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String json) {
                loadingDialog.dismiss();
                Log.i("test", "ChangePassword = " + json);
                try {
                    JSONObject object = new JSONObject(json);
                    String result = object.optString("Result");
                    if (TextUtils.equals(result, "0")) {
                        finish();
                    }
                    error = object.optString("Error");
                    Log.i("test", "error = " + error);
                    Toast.makeText(ReturnMoneyPwd.this, error, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}