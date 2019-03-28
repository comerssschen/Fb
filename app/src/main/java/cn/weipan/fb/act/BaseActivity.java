package cn.weipan.fb.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import cn.weipan.fb.common.AppManager;
import cn.weipan.fb.utils.SysInfoUtils;

/*
基内
* Created by cc on 2016/7/27.
* 邮箱：904359289@QQ.com.
* */
public class BaseActivity extends BaseBaseActivity {
    private Boolean isServiceRunning = false;
    private FinishReceiver receiver;
    private String userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("finish");
        receiver = new FinishReceiver();
        registerReceiver(receiver, intentFilter);


        String id = appContext.getDeviceId();
        if (appContext.getDeviceId().length() < 10) {
            for (int i = 0; i < 10 - appContext.getDeviceId().length(); i++) {
                id = "0" + id;
            }
        }
        String number = appContext.getCashId();
        if (appContext.getCashId().length() < 10) {
            for (int i = 0; i < 10 - appContext.getCashId().length(); i++) {
                number = "0" + number;
            }
        }
        String randomStr = getRandomString(8);
        userInfo = randomStr + id + number + "0000";

    }

    //获取秘钥
    public String getMiyaoKey(String randomStr) {
        return randomStr + getMiyao(randomStr);
    }


    protected void onStart() {
        super.onStart();
        if (isServiceRunning) {
            isServiceRunning = false;
        } else {
        }
    }

    //关闭页面的广播
    class FinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (SysInfoUtils.isServiceRunning(this)) {
            isServiceRunning = true;
        } else {
        }
    }

    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
        return hexString.toString();
    }

    //获取秘钥
    public String getMiyao(String randomStr) {
        String a = "";
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            String workKey = appContext.getWorkKey();
            DESKeySpec desKeySpec = new DESKeySpec(appContext.getWorkKey().getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] buf = cipher.doFinal(randomStr.getBytes());
            a = toHexString(buf).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;
    }

    //获取随机数
    public static String getRandomString(int length) {
        String base = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public String getContent(String randomStr) {
        String a = "";
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            DESKeySpec desKeySpec = new DESKeySpec(randomStr.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            //加密的内容
            Log.i("test", "desKeySpec=====" + appContext.getWorkKey());
            byte[] buf = cipher.doFinal(userInfo.getBytes());
            a = toHexString(buf).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        AppManager.getAppManager().finishActivity(this);
    }

}