package cn.weipan.fb.act;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanzhenjie.permission.AndPermission;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.utils.DialogUtils;

/**
 * 设置
 * Created by cc on 2016/9/19.
 * 邮箱：904359289@QQ.com.
 */
public class SettingActivity extends BaseActivity implements OnClickListener {
    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.rl_longin_pwd)
    RelativeLayout rlLonginPwd;
    @BindView(R.id.rl_return_money_pwd)
    RelativeLayout rlReturnMoneyPwd;
    @BindView(R.id.rl_new_message)
    RelativeLayout rlNewMessage;
    @BindView(R.id.rl_feedback)
    RelativeLayout rlFeedback;
    @BindView(R.id.rl_contact_customer)
    RelativeLayout rlContactCustomer;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        ButterKnife.bind(this);
        headViewTitle.setText("设置");
    }

    @OnClick({R.id.ll_fanhui, R.id.rl_longin_pwd, R.id.rl_return_money_pwd, R.id.rl_new_message, R.id.rl_feedback, R.id.rl_contact_customer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            //登录密码
            case R.id.rl_longin_pwd:
                intent = new Intent(SettingActivity.this, LoginPwd.class);
                startActivity(intent);
                break;
            //退款密码
            case R.id.rl_return_money_pwd:
                intent = new Intent(SettingActivity.this, ReturnMoneyPwd.class);
                startActivity(intent);
                break;
            //新消息通知
            case R.id.rl_new_message:
                intent = new Intent(SettingActivity.this, NewMessage.class);
                startActivity(intent);
                break;
            //意见反馈
            case R.id.rl_feedback:
                intent = new Intent(SettingActivity.this, FeedBackActivity.class);
                startActivity(intent);
                break;
            //联系客服

            case R.id.rl_contact_customer:
                AndPermission.with(SettingActivity.this)
                        .permission(Manifest.permission.CALL_PHONE)
                        .send();
                DialogUtils.customDialog(this, "", "呼叫", "取消",
                        "客服电话：400-8321-606", new DialogUtils.DialogCallback() {
                            @Override
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
                break;

            default:
                break;
        }
    }
}
