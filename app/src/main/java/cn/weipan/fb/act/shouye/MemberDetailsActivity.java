package cn.weipan.fb.act.shouye;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseActivity;
import cn.weipan.fb.utils.LoadingDialog;


/**
 * 作者：Created by cc on 2016/11/28 13:56.
 * 邮箱：904359289@QQ.com.
 * 类 ：会员详情
 */
public class MemberDetailsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.tv_title_one)
    TextView tvTitleOne;
    @BindView(R.id.tv_content_one)
    TextView tvContentOne;
    @BindView(R.id.tv_title_two)
    TextView tvTitleTwo;
    @BindView(R.id.tv_content_two)
    TextView tvContentTwo;
    @BindView(R.id.tv_title_three)
    TextView tvTitleThree;
    @BindView(R.id.tv_content_three)
    TextView tvContentThree;
    @BindView(R.id.tv_title_four)
    TextView tvTitleFour;
    @BindView(R.id.tv_content_four)
    TextView tvContentFour;
    @BindView(R.id.commit_rl)
    RelativeLayout commitRl;
    private LoadingDialog loadingDialog;
    private String activity;
    private String memberNumber;
    private String memberName;
    private String memberType;
    private String memberMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberdetails);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        activity = getIntent().getStringExtra("Activity");
        memberNumber = getIntent().getStringExtra("MemberNumber");
        memberName = getIntent().getStringExtra("MemberName");
        memberType = getIntent().getStringExtra("MemberType");
        memberMoney = getIntent().getStringExtra("MemberMoney");

        tvContentOne.setText(memberNumber);
        tvContentTwo.setText(memberName);
        tvContentThree.setText(memberType);


        headViewTitle.setText("会员消费");
        loadingDialog = new LoadingDialog(MemberDetailsActivity.this, "提交中...");
        if (TextUtils.equals(activity, "MemberConsumptionActivity")) {//会员消费
            headViewTitle.setText("会员消费");
            tvTitleFour.setText("会员余额");
            tvContentFour.setText("RMB " + memberMoney);

        } else if (TextUtils.equals(activity, "JiFenActivity")) {//积分消费
            headViewTitle.setText("积分消费");
            tvTitleFour.setText("积分余额");
            tvContentFour.setText(memberMoney);
        } else if (TextUtils.equals(activity, "MemberIncomeActivity")) {//会员充值

            headViewTitle.setText("会员充值");
            tvTitleFour.setText("会员余额");
            tvContentFour.setText("RMB " + memberMoney);
        }

    }


    @OnClick({R.id.ll_fanhui, R.id.commit_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            case R.id.commit_rl:
                Intent intent = new Intent(MemberDetailsActivity.this, PayMoneyActivity.class);

                if (TextUtils.equals(activity, "MemberIncomeActivity")) {//会员充值
                    intent.putExtra("Activity", "MemberIncomeActivity");
                    intent.putExtra("MemberNumber", memberNumber);
                    intent.putExtra("MemberName", memberName);
                    intent.putExtra("MemberType", memberType);
                    intent.putExtra("MemberMoney", memberMoney);
                } else if (TextUtils.equals(activity, "MemberConsumptionActivity")) { //会员消费

                    intent.putExtra("Activity", "MemberConsumptionActivity");
                    intent.putExtra("MemberNumber", memberNumber);
                    intent.putExtra("MemberName", memberName);
                    intent.putExtra("MemberType", memberType);
                    intent.putExtra("MemberMoney", memberMoney);

                } else if (TextUtils.equals(activity, "JiFenActivity")) {//积分消费

                    intent.putExtra("Activity", "JiFenActivity");
                    intent.putExtra("MemberNumber", memberNumber);
                    intent.putExtra("MemberName", memberName);
                    intent.putExtra("MemberType", memberType);
                    intent.putExtra("MemberMoney", memberMoney);
                }
                startActivity(intent);
//                    loadingDialog.show();

//                    String randomStr = getRandomString(8);
//                    String miyao = getMiyao(randomStr);
////                    String sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + type + "|" + result + "|" + randomStr + miyao + "|tag_returnfee_wx}";
//                    String sendData = "{app" + "|" + appContext.getDeviceId() + "|" + appContext.getCashId() + "|" + type + "|" + danhao_edit.getText().toString().trim() + "|" + randomStr + miyao + "|tag_returnfee_wx}";
//                    Log.i("test", "sendData=========" + sendData);
//                    NetworkRequest mLoginRequest = new NetworkRequest(sendData);
//                    mLoginRequest.start();
//                    mLoginRequest.setListener(TuiKuanDanHao.this);
                break;
        }
    }
}
