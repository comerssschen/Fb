package cn.weipan.fb.act.shouye;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseActivity;

/**
 * Created by cc on 2016/10/12.
 * 邮箱：904359289@QQ.com.
 * 退款账单详情
 */
public class TuiKuanZhangDanXiangQing extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.tv_dingdanjine)
    TextView tvDingdanjine;
    @BindView(R.id.tv_shishoujine)
    TextView tvShishoujine;
    @BindView(R.id.tv_fukuanfangshi)
    TextView tvFukuanfangshi;
    @BindView(R.id.tv_jiaoyishijian)
    TextView tvJiaoyishijian;
    @BindView(R.id.tv_jiaoyizhuangtai)
    TextView tvJiaoyizhuangtai;
    @BindView(R.id.tv_jiaoyidanhao)
    TextView tvJiaoyidanhao;
    @BindView(R.id.tv_dingdanhao)
    TextView tvDingdanhao;
    @BindView(R.id.tv_shouyinyuan)
    TextView tvShouyinyuan;
    @BindView(R.id.commit_rl)
    RelativeLayout commitRl;
    private String dingDanHao;
    private String type;
    private String shiShouJinE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuikuanzhangdanxiangqing);
        ButterKnife.bind(this);
        initView();

    }

    //初始化界面
    private void initView() {

        headViewTitle.setText("账单详情");
        String dingDanJingE = getIntent().getStringExtra("DingDanJingE");
        shiShouJinE = getIntent().getStringExtra("ShiShouJinE");
        String jiaoYiShiJian = getIntent().getStringExtra("JiaoYiShiJian");
        String jiaoYiZhuangTai = getIntent().getStringExtra("JiaoYiZhuangTai");
        String jiaoYiDanHao = getIntent().getStringExtra("JiaoYiDanHao");
        dingDanHao = getIntent().getStringExtra("DingDanHao");
        String shouYinYuan = getIntent().getStringExtra("ShouYinYuan");
        String fuKuanFangShi = getIntent().getStringExtra("FuKuanFangShi");
        type = getIntent().getStringExtra("type");


        tvDingdanjine.setText(dingDanJingE);
        tvShishoujine.setText(shiShouJinE);
        tvFukuanfangshi.setText(fuKuanFangShi);
        tvJiaoyishijian.setText(jiaoYiShiJian);
        tvJiaoyizhuangtai.setText(jiaoYiZhuangTai);
        tvJiaoyidanhao.setText(jiaoYiDanHao);
        tvDingdanhao.setText(dingDanHao);
        tvShouyinyuan.setText(shouYinYuan);

//        tvFukuanfangshi.setText(type);
//        if (TextUtils.equals(type, "1402")) {
//            tvFukuanfangshi.setText("微信");
//        } else if (TextUtils.equals(type, "63")) {
//            tvFukuanfangshi.setText("支付宝");
//        }

    }

    @OnClick({R.id.ll_fanhui, R.id.commit_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            //退款
            case R.id.commit_rl:
                Intent intent = new Intent(TuiKuanZhangDanXiangQing.this, TuiKuanMiMaActivity.class);
                intent.putExtra("ShiShouJinE", shiShouJinE);
                intent.putExtra("DingDanHao", dingDanHao);
                intent.putExtra("type", type);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}