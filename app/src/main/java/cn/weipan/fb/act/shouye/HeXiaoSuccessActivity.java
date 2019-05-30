package cn.weipan.fb.act.shouye;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.weipan.fb.R;
import cn.weipan.fb.act.BaseActivity;
import cn.weipan.fb.utils.BluetoothUtil;
import cn.weipan.fb.utils.ESCUtil;


/**
 * 核销成功
 * Created by cc on 2016/10/13.
 * 邮箱：904359289@QQ.com.
 */
public class HeXiaoSuccessActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_fanhui)
    LinearLayout llFanhui;
    @BindView(R.id.head_view_title)
    TextView headViewTitle;
    @BindView(R.id.tv_sucess)
    TextView tvSucess;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.tv_title_one)
    TextView tvTitleOne;

    @BindView(R.id.tv_content_one)
    TextView tvContentOne;

    @BindView(R.id.rl_one)
    RelativeLayout rlOne;
    @BindView(R.id.tv_title_two)
    TextView tvTitleTwo;
    @BindView(R.id.tv_content_two)
    TextView tvContentTwo;
    @BindView(R.id.rl_two)
    RelativeLayout rlTwo;
    @BindView(R.id.tv_title_three)
    TextView tvTitleThree;
    @BindView(R.id.tv_content_three)
    TextView tvContentThree;
    @BindView(R.id.rl_three)
    RelativeLayout rlThree;
    @BindView(R.id.tv_title_four)
    TextView tvTitleFour;
    @BindView(R.id.tv_content_four)
    TextView tvContentFour;
    @BindView(R.id.rl_four)
    RelativeLayout rlFour;
    @BindView(R.id.tv_title_five)
    TextView tvTitleFive;
    @BindView(R.id.tv_content_five)
    TextView tvContentFive;
    @BindView(R.id.rl_five)
    RelativeLayout rlFive;
    @BindView(R.id.tv_shouyinyuan)
    TextView tvShouyinyuan;
    @BindView(R.id.rl_shouyingyuan)
    RelativeLayout rlShouyingyuan;
    @BindView(R.id.finish_rl)
    RelativeLayout finishRl;
    @BindView(R.id.tv_title_shouyingyuan)
    TextView tvTitleShouyingyuan;
    private String activity;
    private String text_three;
    private String text_four;
    private String text_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoukuanhexiaosuccess);
        ButterKnife.bind(this);
        initView();
    }

    //    intent.putExtra("ContentOne", arr[1]);
//    intent.putExtra("ContentTwo", arr[2]);
//    intent.putExtra("ContentThree", arr[7]);
//    intent.putExtra("ContentFour", arr[3]);
//    intent.putExtra("ContentFive",appContext.getRealName());
    //初始化控件
    private void initView() {
        activity = getIntent().getStringExtra("Activity");
        String Text_sucess = getIntent().getStringExtra("Text_sucess");
        text_money = getIntent().getStringExtra("Text_money");
        String Text_one = getIntent().getStringExtra("Text_one");
        String Text_two = getIntent().getStringExtra("Text_two");
        text_three = getIntent().getStringExtra("Text_three");
        text_four = getIntent().getStringExtra("Text_four");
        String Text_five = getIntent().getStringExtra("Text_five");
        String Text_shouyingyuan = getIntent().getStringExtra("Text_shouyingyuan");

        if (TextUtils.isEmpty(Text_one)) {
            rlOne.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(Text_two)) {
            rlTwo.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(text_three)) {
            rlThree.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(text_four)) {
            rlFour.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(Text_five)) {
            rlFive.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(Text_shouyingyuan)) {
            rlShouyingyuan.setVisibility(View.GONE);
        }
        tvSucess.setText(Text_sucess);
        money.setText(text_money);
        tvContentOne.setText(Text_one);
        tvContentTwo.setText(Text_two);
        tvContentThree.setText(text_three);
        tvContentFour.setText(text_four);
        tvContentFive.setText(Text_five);
        tvShouyinyuan.setText(Text_shouyingyuan);


        if (TextUtils.equals(activity, "PosActivity")) {//现金记账和Pos机记账成功
            headViewTitle.setText("交易记录");
            tvSucess.setText("收款成功");
            tvTitleOne.setText("订单金额");
            tvTitleTwo.setText("实收金额");
            tvTitleThree.setText("订单编号");
            tvTitleFour.setText("交易时间");
            tvSucess.setText("收款成功");
            tvContentOne.setText("￥" + Text_one);
            tvContentTwo.setText("￥" + Text_two);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    Print();
                    Looper.loop();
                }
            }).start();

        } else if (TextUtils.equals(activity, "KaquanhexiaoActivity")) {//卡券核销
            headViewTitle.setText("卡券核销详情");
            tvSucess.setText("卡券核销成功");
            tvTitleOne.setText("卡券编号");
            tvTitleTwo.setText("核销金额");
            tvTitleThree.setText("交易时间");
            tvContentThree.setText(text_three);
            if (TextUtils.isEmpty(Text_two)) {
                money.setVisibility(View.INVISIBLE);
            } else {
                money.setText(text_money);
                tvContentTwo.setText(Text_two);
            }
        } else if (TextUtils.equals(activity, "KaquanshoukuanSuccessActivity")) {//卡券收款
            headViewTitle.setText("核销收款详情");
            tvSucess.setText("核销收款成功");
            tvTitleOne.setText("订单编号");
            tvTitleTwo.setText("卡券编号");
            tvTitleThree.setText("核销金额");
            tvTitleFour.setText("实收金额");
            tvTitleFive.setText("交易时间");
            tvContentThree.setText("￥" + text_three);
            if (TextUtils.isEmpty(text_four)) {
                money.setVisibility(View.INVISIBLE);
            } else {
                money.setText(text_money);
                tvContentFour.setText("￥" + text_four);
            }
        } else if (TextUtils.equals(activity, "TuiKuanActivity")) {//退款成功
            headViewTitle.setText("账单详情");
            tvTitleOne.setText("付款账号");
            tvTitleTwo.setText("订单编号");
            tvTitleShouyingyuan.setText("退款时间");
        }


    }

    @OnClick({R.id.ll_fanhui, R.id.finish_rl, R.id.head_view_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_view_title:

                break;
            case R.id.ll_fanhui:
                finish();
                break;
            case R.id.finish_rl:
                Intent intent = new Intent();
                intent.setAction("finish");
                sendBroadcast(intent);
                break;
        }
    }

    private void Print() {

        // 1: Get BluetoothAdapter
        BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
        if (btAdapter == null) {
            return;
        }
        // 2: Get Sunmi's InnerPrinter BluetoothDevice
        BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
        if (device == null) {
            return;
        }
        // 3: Generate a order data
//                byte[] data = ESCUtil.generateMockData();
//                byte[] data2 = ESCUtil.generateMockData();
        byte[] data = ESCUtil.generateMockData(appContext.getRealName(), appContext.getDeviceId(), text_three, text_four, "现金/Pos机收款", text_money, true);
        byte[] data2 = ESCUtil.generateMockData(appContext.getRealName(), appContext.getDeviceId(), text_three, text_four, "现金/Pos机收款", text_money, false);
        // 4: Using InnerPrinter print data
        BluetoothSocket socket = null;
        BluetoothSocket socket2 = null;
        try {
            socket = BluetoothUtil.getSocket(device);
            BluetoothUtil.sendData(data, socket);
            try {
                Thread.sleep(5000);
                socket2 = BluetoothUtil.getSocket(device);
                BluetoothUtil.sendData(data2, socket2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}