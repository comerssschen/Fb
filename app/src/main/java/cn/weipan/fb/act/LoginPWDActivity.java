package cn.weipan.fb.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.utils.SharedPre;

/**
 * 忘记手势密码
 * Created by cc on 2016/10/24.
 * 邮箱：904359289@QQ.com.
 */
public class LoginPWDActivity extends Activity implements View.OnClickListener {
    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.pwd_new)
    EditText pwdNew;
    @BindView(R.id.iv_qingchu)
    ImageView ivQingchu;
    @BindView(R.id.rl_finish)
    RelativeLayout rlFinish;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yanzhengpwd);
        ButterKnife.bind(this);
        intView();
    }

    //初始化界面
    private void intView() {
        headViewTitle.setText("忘记手势密码");
        SharedPre shared = new SharedPre(this);
        password = shared.getPassword();
    }


    @OnClick({R.id.ll_fanhui, R.id.iv_qingchu, R.id.rl_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            case R.id.iv_qingchu:
                pwdNew.setText("");
                break;
            case R.id.rl_finish:

                if (password.equals(pwdNew.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("验证成功");
                    builder.setMessage("请重新绘制新的手势密码！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(LoginPWDActivity.this, CreateGestureActivity.class);
                            intent.putExtra("Activity", "LoginPWDActivity");
                            startActivity(intent);
                            finish();
                        }
                    });
                    Dialog noticeDialog = builder.create();
                    noticeDialog.setCanceledOnTouchOutside(false);
                    noticeDialog.show();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("验证失败");
                    builder.setMessage("登录密码错误请重新输入！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    Dialog noticeDialog = builder.create();
                    noticeDialog.setCanceledOnTouchOutside(false);
                    noticeDialog.show();
                }
                break;
            default:
                break;
        }
    }
}
