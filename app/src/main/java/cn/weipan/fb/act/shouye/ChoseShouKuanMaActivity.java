package cn.weipan.fb.act.shouye;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseActivity;

/**
 * 作者：Created by cc on 2016/12/13 10:58.
 * 邮箱：904359289@QQ.com.
 * 类 ：
 */
public class ChoseShouKuanMaActivity extends BaseActivity {
    @BindView(R.id.but_header_back)
    ImageView butHeaderBack;
    @BindView(R.id.tv_header_back)
    TextView tvHeaderBack;
    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.iv_weixin)
    ImageView ivWeixin;
    @BindView(R.id.rl_weixin)
    RelativeLayout rlWeixin;
    @BindView(R.id.iv_zhifubao)
    ImageView ivZhifubao;
    @BindView(R.id.rl_zhifubao)
    RelativeLayout rlZhifubao;
    @BindView(R.id.iv_baiduqianbao)
    ImageView ivBaiduqianbao;
    @BindView(R.id.rl_baiduqianbao)
    RelativeLayout rlBaiduqianbao;
    @BindView(R.id.iv_jingdong)
    ImageView ivJingdong;
    @BindView(R.id.rl_jingdong)
    RelativeLayout rlJingdong;
    @BindView(R.id.iv_qq)
    ImageView ivQq;
    @BindView(R.id.rl_qq)
    RelativeLayout rlQq;
    private String paymoney;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choseshoukuanma);
        ButterKnife.bind(this);
        paymoney = getIntent().getStringExtra("paymoney");
    }

    @OnClick({R.id.ll_fanhui, R.id.rl_weixin, R.id.rl_zhifubao, R.id.rl_baiduqianbao, R.id.rl_jingdong, R.id.rl_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            case R.id.rl_weixin:
                startActivity("11");
                break;
            case R.id.rl_zhifubao:
                startActivity("51");
                break;
            case R.id.rl_baiduqianbao:
                startActivity("31");
                break;
            case R.id.rl_jingdong:
                startActivity("72");
                break;
            case R.id.rl_qq:
                startActivity("82");
                break;
            default:
                break;
        }
    }

    private void startActivity(String type) {
        intent = new Intent(ChoseShouKuanMaActivity.this, ShouKuanMaActivity.class);
        intent.putExtra("paymoney", paymoney);
        intent.putExtra("type", type);
        startActivity(intent);
    }


}
