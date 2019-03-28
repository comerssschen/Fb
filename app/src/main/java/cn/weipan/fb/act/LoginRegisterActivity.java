package cn.weipan.fb.act;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.constact.Constant;
import cn.weipan.fb.utils.DBCopyUtil;
import cn.weipan.fb.utils.DialogUtils;
import cn.weipan.fb.utils.HttpUtils;
import cn.weipan.fb.utils.LoadingDialog;
import cn.weipan.fb.utils.ToastUtils;

/**
 * 注册
 * Created by cc on 2016/7/22.
 * 邮箱：904359289@QQ.com.
 */
public class LoginRegisterActivity extends BaseNoLoginActivity implements OnClickListener {
    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.loginregist_edit_username)
    EditText loginregistEditUsername;
    @BindView(R.id.loginregist_edit_name)
    EditText loginregistEditName;
    @BindView(R.id.loginregist_selection)
    TextView loginregistSelection;
    @BindView(R.id.loginregist_edit_address)
    EditText loginregistEditAddress;
    @BindView(R.id.loginregist_edit_phone)
    EditText loginregistEditPhone;
    @BindView(R.id.loginreitst_commit_btn)
    RelativeLayout loginreitstCommitBtn;
    private static final int REGION_REQUEST_CODE = 001;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_regist);
        ButterKnife.bind(this);
        initView();
    }

    //初始化界面
    private void initView() {
        headViewTitle.setText("注册");
        loadingDialog = new LoadingDialog(LoginRegisterActivity.this, "提交中...");

    }


    //选择城市回调结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGION_REQUEST_CODE && resultCode == 200) {
            String province = data.getStringExtra(RegionSelectActivity.REGION_PROVINCE);
            String city = data.getStringExtra(RegionSelectActivity.REGION_CITY);
            String area = data.getStringExtra(RegionSelectActivity.REGION_AREA);
            loginregistSelection.setText(province + " " + city + " " + area);
        }
    }

    // 检验手机号码
    public boolean checkPhone(String NO) {
        boolean result = true;
        String str = NO;
        Pattern pattern = Pattern.compile("^[0-9]{11}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            result = false;
        }
        return result;
    }

    //获取随机数
    public static String getRandomString(int length) {
        String base = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 提交数据
     */
    private void uploadDate() {
        String url = Constant.URL + "/api/user/Register";
        Map<String, Object> map = new HashMap<>();
        map.put("Style", 1);
        map.put("LoginName", loginregistEditUsername.getText().toString().trim());
        map.put("CompName", loginregistEditName.getText().toString().trim());
        map.put("Phone", loginregistEditPhone.getText().toString().trim());
        map.put("AreaCode", "322");
        map.put("Address", loginregistSelection.getText().toString().trim() + loginregistEditAddress.getText().toString().trim());
        map.put("DeviceNO", getRandomString(32));

        final JSONObject obj = new JSONObject(map);

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, obj.toString());
        Request request = new Request.Builder().url(url).post(body).build();
        HttpUtils.postAsyn(LoginRegisterActivity.this, request, new HttpUtils.CallBack() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("ok", "失败");
                loadingDialog.dismiss();
                ToastUtils.showToast(LoginRegisterActivity.this, "网络连接异常，请重试");
            }

            @Override
            public void onResponse(String json) {
                loadingDialog.dismiss();
                Log.i("test", "json = zhuce" + json);
                try {
                    JSONObject object = new JSONObject(json);
                    String error = object.optString("Error");
                    String sucess = object.optString("Result");
                    if (TextUtils.equals(sucess, "0")) {

                        AndPermission.with(LoginRegisterActivity.this)
                                .permission(Manifest.permission.CALL_PHONE)
                                .send();
                        DialogUtils.customDialog(LoginRegisterActivity.this, "", "现在联系", "稍后",
                                "提交成功，请联系客服开通支付\r\n			客服电话：400-8321-606", new DialogUtils.DialogCallback() {
                                    public void PositiveButton(int i) {
                                        switch (i) {
                                            case -1:
                                                Intent in4 = new Intent(
                                                        "android.intent.action.CALL", Uri
                                                        .parse("tel:" + "400-8321-606"));
                                                startActivity(in4);
                                                break;
                                            case -2:
                                                break;

                                            default:
                                                break;
                                        }
                                    }
                                }, false, true);
                    } else {
                        ToastUtils.showToast(LoginRegisterActivity.this, error);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.ll_fanhui, R.id.loginregist_selection, R.id.loginreitst_commit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            case R.id.loginregist_selection:
                DBCopyUtil.copyDataBaseFromAssets(this, "region.db");
                startActivityForResult(new Intent(this, RegionSelectActivity.class), REGION_REQUEST_CODE);
                break;
            case R.id.loginreitst_commit_btn:
                if (loginregistEditUsername.getText().toString().trim().equals("")) {
                    ToastUtils.showToast(LoginRegisterActivity.this, "请填写用户姓名");
                } else if (loginregistEditName.getText().toString().trim().equals("")) {
                    ToastUtils.showToast(LoginRegisterActivity.this, "请填写公司名称");
                } else if (loginregistSelection.getText().toString().trim().equals("") | loginregistEditAddress.getText().toString().trim().equals("")) {
                    ToastUtils.showToast(LoginRegisterActivity.this, "请填写公司地址");
                } else if (loginregistEditPhone.getText().toString().trim().equals("")) {
                    ToastUtils.showToast(LoginRegisterActivity.this, "请填写手机号码");
                } else if (checkPhone(loginregistEditPhone.getText().toString().trim())) {
                    ToastUtils.showToast(LoginRegisterActivity.this, "手机号码格式不正确");
                } else {
                    loadingDialog.show();
                    uploadDate();
//				String sendData = "{sign|15858243335|王长朴|23|杭州西盛信息技术有限公司|益乐路|192.168.1.2|13291811115}";
                }
                break;
            default:
        }
    }
}
