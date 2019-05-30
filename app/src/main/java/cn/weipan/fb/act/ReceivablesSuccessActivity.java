package cn.weipan.fb.act;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import cn.weipan.fb.R;
import cn.weipan.fb.act.shouye.TuiKuanMiMaActivity;
import cn.weipan.fb.common.Constant;
import cn.weipan.fb.utils.BluetoothUtil;
import cn.weipan.fb.utils.ESCUtil;

/**
 * 支付成功界面
 * Created by cc on 2016/9/19.
 * 邮箱：904359289@QQ.com.
 */
public class ReceivablesSuccessActivity extends BaseActivity implements OnClickListener {

    private TextView money;
    private RelativeLayout finish_rl;
    private String paymoney;
    private String time;
    private Intent intent;
    private String payNumber;
    private String dingDanBianHao;
    private String PayTitle;
    private String payType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receivablessuccess);
        initView();
    }

    //初始化界面
    private void initView() {
        TextView headViewTitle = (TextView) findViewById(R.id.head_view_title);
        headViewTitle.setText("收款详情");
        headViewTitle.setOnClickListener(this);

        TextView tv_returnmoney = (TextView) findViewById(R.id.tv_returnmoney);
        tv_returnmoney.setOnClickListener(this);
        TextView tv_sucess = (TextView) findViewById(R.id.tv_sucess);
        LinearLayout fanhui = (LinearLayout) findViewById(R.id.ll_fanhui);
        fanhui.setOnClickListener(this);
        RelativeLayout rl_pay_number = (RelativeLayout) findViewById(R.id.rl_pay_number);
        paymoney = getIntent().getStringExtra("paymoney");
        payNumber = getIntent().getStringExtra("payNumber");
        dingDanBianHao = getIntent().getStringExtra("dingDanBianHao");
        time = getIntent().getStringExtra("Time");
        PayTitle = getIntent().getStringExtra("PayTitle");
        payType = getIntent().getStringExtra("PayType");

        money = (TextView) findViewById(R.id.money);
        money.setText("+" + paymoney);
        TextView tv_bianhao = (TextView) findViewById(R.id.tv_bianhao);

        if (!Constant.isTuiKuan) {
            tv_returnmoney.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(payType)) {
            tv_returnmoney.setVisibility(View.GONE);
        }
        TextView tv_resultstring = (TextView) findViewById(R.id.tv_resultstring);
        TextView tv_time = (TextView) findViewById(R.id.tv_time);

        if (TextUtils.isEmpty(payNumber)) {
            rl_pay_number.setVisibility(View.GONE);
        } else {
            tv_resultstring.setText(payNumber);
        }

        tv_time.setText(time);
        tv_sucess.setText(PayTitle);
        tv_bianhao.setText(dingDanBianHao);
        finish_rl = (RelativeLayout) findViewById(R.id.finish_rl);
        finish_rl.setOnClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Print();
                Looper.loop();
            }
        }).start();
    }

    //点击按钮
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_view_title:


                break;
            case R.id.ll_fanhui:
                finish();
                break;
            //点击退款
            case R.id.tv_returnmoney:
                intent = new Intent(ReceivablesSuccessActivity.this, TuiKuanMiMaActivity.class);
                intent.putExtra("ShiShouJinE", paymoney);
                intent.putExtra("DingDanHao", dingDanBianHao);
                intent.putExtra("type", payType);
                startActivity(intent);
                break;
            case R.id.tv_header_back:
                finish();
                break;
            case R.id.finish_rl:
//                intent = new Intent(ReceivablesSuccessActivity.this, MainActivity.class);
                intent = new Intent();
                intent.setAction("finish");
                sendBroadcast(intent);
//                startActivity(intent);
//                finish();
                break;
            default:
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
        byte[] data = ESCUtil.generateMockData(appContext.getRealName(), appContext.getDeviceId(), dingDanBianHao, time, PayTitle, paymoney, true);
        byte[] data2 = ESCUtil.generateMockData(appContext.getRealName(), appContext.getDeviceId(), dingDanBianHao, time, PayTitle, paymoney, false);
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
