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

import com.basewin.aidl.OnPrinterListener;
import com.basewin.define.FontsType;
import com.basewin.services.ServiceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import cn.weipan.fb.R;
import cn.weipan.fb.act.shouye.TuiKuanMiMaActivity;
import cn.weipan.fb.constact.Constant;
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

    JSONObject printJson = new JSONObject();
    private PrinterListener printer_callback = new PrinterListener();

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

//                Printsjjl(false);
//                try {
//                    Thread.sleep(5000);
//                    Printsjjl(true);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
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



    /*
        * content-type   内容类型, 可选字段有”txt”,“jpg”,”one-dimension”(一维码), “two-dimension”(二维码)(必填)
        * content   字体内容    打印的文字或一维码二维码对应的文字内容
        * size  字体大小    打印类型为txt:字体大小可选字段为”1”,”2”,”3”分别对应字号为：16,24,36，如果想要自定义字号，直接传入实际字号即可，字号必须大于5 打印类型为two-dimension:二维码大小, 可选1-8 打印类型为one-dimension:一维码宽度, 可选1-3
        * position  位置  对齐方式, 可选字段有”left”, “center”, “right”(选填)默认为left
        * bold  字体粗细    “1”表示字体加粗, “0”表示不加粗(选填)默认为0
        * italic 斜体     1 斜体     0 正常
        * height 默认-1    一维码高度, 可选1-3
        *
        * */

    private void Printsjjl(boolean isSecond) {
        try {
            // 組打印json字符串
            JSONArray printTest = new JSONArray();

            JSONObject json10 = new JSONObject();
            json10.put("content-type", "txt");
            json10.put("content", "     ");
            json10.put("size", "30");
            json10.put("position", "center");
            json10.put("bold", "1");


            JSONObject json11 = new JSONObject();
            json11.put("content-type", "txt");
            json11.put("content", "云支付收银小票");
            json11.put("size", "28");
            json11.put("position", "center");
            json11.put("bold", "1");

            JSONObject json12 = new JSONObject();
            json12.put("content-type", "txt");
            json12.put("content", "商户存根: MERCHANT COPY");
            json12.put("size", "28");
            json12.put("position", "left");
            json12.put("height", "3");

            JSONObject json13 = new JSONObject();
            json13.put("content-type", "txt");
            json13.put("content", "商户名称：" + appContext.getRealName());
            json13.put("size", "28");
            json13.put("position", "left");
            json13.put("height", "3");

            JSONObject json14 = new JSONObject();
            json14.put("content-type", "txt");
            json14.put("content", "收银员号: " + appContext.getDeviceId());
            json14.put("size", "28");
            json14.put("position", "left");
            json14.put("height", "3");

            JSONObject json15 = new JSONObject();
            json15.put("content-type", "txt");
            json15.put("content", "支付单号：" + dingDanBianHao);
            json15.put("size", "28");
            json15.put("position", "left");
            json15.put("height", "3");

            JSONObject json16 = new JSONObject();
            json16.put("content-type", "txt");
            json16.put("content", "金    额：" + paymoney);
            json16.put("size", "28");
            json16.put("position", "left");
            json16.put("height", "3");

            JSONObject json17 = new JSONObject();
            json17.put("content-type", "txt");
            json17.put("content", "交易时间：" + time);
            json17.put("size", "28");
            json17.put("position", "left");
            json17.put("height", "3");

            JSONObject json18 = new JSONObject();
            json18.put("content-type", "txt");
            json18.put("content", "交易详情：" + PayTitle);
            json18.put("size", "28");
            json18.put("position", "left");
            json18.put("height", "3");

            JSONObject json19 = new JSONObject();
            json19.put("content-type", "txt");
            json19.put("content", "备注（REFERENCE）");
            json19.put("size", "22");
            json19.put("position", "left");
            json19.put("height", "3");

            JSONObject json20 = new JSONObject();
            json20.put("content-type", "txt");
            json20.put("content", "技术支持：杭州微盘信息技术有限公司");
            json20.put("size", "22");
            json20.put("position", "left");
            json20.put("height", "3");

            JSONObject json21 = new JSONObject();
            json21.put("content-type", "txt");
            json21.put("content", "免费热线：400-8321-606");
            json21.put("size", "22");
            json21.put("position", "left");
            json21.put("height", "3");

            JSONObject json22 = new JSONObject();
            json22.put("content-type", "txt");
            json22.put("content", "持卡人签名：                                                                                                               ");
            json22.put("size", "22");
            json22.put("position", "left");
            json22.put("height", "3");

            JSONObject json23 = new JSONObject();
            json23.put("content-type", "txt");
            json23.put("content", "本人确认以上交易，对交易无任何纠纷意见");
            json23.put("size", "22");
            json23.put("position", "left");
            json23.put("height", "3");

            JSONObject json24 = new JSONObject();
            json24.put("content-type", "txt");
            json24.put("content", "I ACKNOWLEDGE SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICE");
            json24.put("size", "22");
            json24.put("position", "left");
            json24.put("height", "3");

            JSONObject json25 = new JSONObject();
            json25.put("content-type", "txt");
            json25.put("content", "----------------------------");
            json25.put("size", "22");
            json25.put("position", "center");
            json25.put("height", "3");


            ServiceManager.getInstence().getPrinter().setPrintGray(2000);
            ServiceManager.getInstence().getPrinter().setPrintFont(FontsType.simsun);


            printTest.put(json11);

            printTest.put(json10);

            printTest.put(json12);
            printTest.put(json13);
            printTest.put(json14);
            printTest.put(json15);
            printTest.put(json16);
            printTest.put(json17);
            printTest.put(json18);

            printTest.put(json10);


            printTest.put(json19);
            printTest.put(json20);
            printTest.put(json21);

            if (!isSecond) {

                printTest.put(json22);
                printTest.put(json23);
                printTest.put(json24);

                printTest.put(json10);

                printTest.put(json25);
            }


            printJson.put("spos", printTest);
            // 设置底部空3行
            // Set at the bottom of the empty 3 rows
            ServiceManager.getInstence().getPrinter().printBottomFeedLine(3);
//            Bitmap qr = BitmapFactory.decodeResource(getResources(), R.drawable.test);

//            Bitmap[] bitmaps = new Bitmap[]{qr.createScaledBitmap(qr, 240, 240, true)};
            ServiceManager.getInstence().getPrinter().print(printJson.toString(), null, printer_callback);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    class PrinterListener implements OnPrinterListener {

        @Override
        public void onStart() {
            // Print start
        }

        @Override
        public void onFinish() {
            // End of the print
        }

        @Override
        public void onError(int errorCode, String detail) {
            // print error
        }
    }


}
