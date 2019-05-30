package cn.weipan.fb.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;


import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;
import cn.weipan.fb.act.MainActivity;
import cn.weipan.fb.bean.AllMessagBean;
import cn.weipan.fb.common.FirstEvent;
import cn.weipan.fb.common.Constant;

/**
 * @author comersss
 * @date 2018/10/23
 * 自定义JPush message 接收器,包括操作tag/alias的结果返回(仅仅包含tag/alias新接口部分)
 */

public class MyJPushMessageReceiver extends JPushMessageReceiver {

    private static final String TAG = "MyJPushMessageReceiver";

    @Override
    public Notification getNotification(Context context, NotificationMessage notificationMessage) {
        Log.i(TAG, "getNotification");
        return super.getNotification(context, notificationMessage);

    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        //打开通知
        Log.i(TAG, "onNotifyMessageOpened");
        Intent intentActivity = new Intent(context, MainActivity.class);
        intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentActivity);
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
        //接受到了通知
        Log.i(TAG, "onNotifyMessageArrived");
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageDismiss(context, notificationMessage);
        Log.i(TAG, "onNotifyMessageDismiss");
    }

    @Override
    public void onRegister(Context context, String s) {
        super.onRegister(context, s);
        Log.i(TAG, "onRegister");
    }

    @Override
    public void onConnected(Context context, boolean b) {
        super.onConnected(context, b);
        Log.i(TAG, "onConnected");
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        super.onCommandResult(context, cmdMessage);
        Log.i(TAG, "onCommandResult");
    }

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
        String message = customMessage.message;
        Log.i(TAG, message);
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
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }
}
