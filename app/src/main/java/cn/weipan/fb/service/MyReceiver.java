package cn.weipan.fb.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;
import cn.weipan.fb.bean.AllMessagBean;
import cn.weipan.fb.common.FirstEvent;
import cn.weipan.fb.constact.Constant;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "test";

    //这是jpush接受消息的广播
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.i(TAG, "JPush用户注册成功");
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "接受到推送下来的自定义消息");
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            Log.i(TAG, ">>>>>>>>>>>>>>>>" + message);
            if (message.split("\\|").length > 3) {
                String[] temp = null;
                temp = message.split("\\|");
                Log.i("test", "main = text = " + message);
                if (message != null) {
                    if (temp.length > 3) {
                        if (temp[0].equals("1")) {
                            temp[0] = "微信支付";
                        } else if (temp[0].equals("2")) {
                            temp[0] = "支付宝";
                        } else if (temp[0].equals("3")) {
                            temp[0] = "百度钱包";
                        } else if (temp[0].equals("4")) {
                            temp[0] = "现金";
                        } else if (temp[0].equals("5")) {
                            temp[0] = "pos机";
                        } else if (temp[0].equals("6")) {
                            temp[0] = "QQ钱包";
                        } else if (temp[0].equals("7")) {
                            temp[0] = "京东钱包";
                        } else {
                            temp[0] = "";
                        }
                        AllMessagBean allMessagBean = new AllMessagBean(temp[0] + "收款", temp[3], temp[2], temp[1]);//标题，时间，单号，类型
                        if (Constant.jpushMessage.size() > 0) {
                            String danhao = Constant.jpushMessage.get(0).getDanhao();
                            if (!TextUtils.equals(temp[2], danhao)) {
                                Constant.jpushMessage.add(0, allMessagBean);
                            }
                        } else {
                            Constant.jpushMessage.add(0, allMessagBean);
                        }
                        EventBus.getDefault().post(new FirstEvent(message));
                    }
                }
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.i(TAG, "接受到推送下来的通知");
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.i(TAG, "用户点击打开了通知");
        } else {
            Log.i(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}