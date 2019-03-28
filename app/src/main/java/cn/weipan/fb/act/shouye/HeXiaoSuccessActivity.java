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

import com.basewin.aidl.OnPrinterListener;
import com.basewin.define.FontsType;
import com.basewin.services.ServiceManager;

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
 *
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

    JSONObject printJson = new JSONObject();
    private PrinterListener printer_callback = new PrinterListener();



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

//                    Printsjjl(false);
//                    try {
//                        Thread.sleep(5000);
//                        Printsjjl(true);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    Looper.loop();
                }
            }).start();

        } else if (TextUtils.equals(activity, "MemberConsumptionActivity")) {//会员消费
            headViewTitle.setText("会员消费详情");
            tvSucess.setText("会员消费成功");
            tvTitleOne.setText("会员卡号");
            tvTitleTwo.setText("会员姓名");
            tvTitleThree.setText("应收金额");
            tvTitleFour.setText("实收金额");
            tvTitleFive.setText("交易时间");
            tvContentThree.setText("￥" + text_three);
            tvContentFour.setText("￥" + text_four);
        } else if (TextUtils.equals(activity, "JiFenActivity")) {//积分消费
            headViewTitle.setText("积分消费详情");
            tvSucess.setText("积分消费成功");
            tvTitleOne.setText("会员卡号");
            tvTitleTwo.setText("会员姓名");
            tvTitleThree.setText("消费积分");
            tvTitleFour.setText("积分余额");
            tvTitleFive.setText("交易时间");
            money.setText(text_three);
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
        } else if (TextUtils.equals(activity, "MemberIncomeSuccessActivity")) {//会员充值成功
            headViewTitle.setText("会员充值详情");
            tvSucess.setText("会员卡充值成功！");
            tvTitleOne.setText("会员卡号");
            tvTitleTwo.setText("会员姓名");
            tvTitleThree.setText("充值金额");
            tvTitleFour.setText("账户余额");
            tvTitleFive.setText("交易时间");
            tvContentThree.setText("￥" + text_three);
            tvContentFour.setText("￥" + text_four);
        } else if (TextUtils.equals(activity, "TuiKuanActivity")) {//退款成功
            headViewTitle.setText("账单详情");
            tvTitleOne.setText("付款账号");
            tvTitleTwo.setText("订单编号");
            tvTitleShouyingyuan.setText("退款时间");
        }


    }
    @OnClick({R.id.ll_fanhui, R.id.finish_rl,R.id.head_view_title})
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
        byte[] data = ESCUtil.generateMockData(appContext.getRealName(),appContext.getDeviceId(),text_three,text_four,"现金/Pos机收款",text_money,true);
        byte[] data2 = ESCUtil.generateMockData(appContext.getRealName(),appContext.getDeviceId(),text_three,text_four,"现金/Pos机收款",text_money,false);
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
            json15.put("content", "支付单号：" + text_three);
            json15.put("size", "28");
            json15.put("position", "left");
            json15.put("height", "3");

            JSONObject json16 = new JSONObject();
            json16.put("content-type", "txt");
            json16.put("content", "金    额：" + text_money);
            json16.put("size", "28");
            json16.put("position", "left");
            json16.put("height", "3");

            JSONObject json17 = new JSONObject();
            json17.put("content-type", "txt");
            json17.put("content", "交易时间：" + text_four);
            json17.put("size", "28");
            json17.put("position", "left");
            json17.put("height", "3");

            JSONObject json18 = new JSONObject();
            json18.put("content-type", "txt");
            json18.put("content", "交易详情：" + "现金/Pos机收款");
            json18.put("size", "28");
            json18.put("position", "left");
            json18.put("height", "3");

            JSONObject json19 = new JSONObject();
            json19.put("content-type", "txt");
            json19.put("content", "备注（REFERENCE）（补打小票）");
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