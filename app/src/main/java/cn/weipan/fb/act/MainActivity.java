package cn.weipan.fb.act;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import cn.weipan.fb.R;
import cn.weipan.fb.bean.BadgeView;
import cn.weipan.fb.bean.InformationBean;
import cn.weipan.fb.common.AppContext;
import cn.weipan.fb.common.AppManager;
import cn.weipan.fb.common.FirstEvent;
import cn.weipan.fb.common.SecondEvent;
import cn.weipan.fb.common.ThreadEvent;
import cn.weipan.fb.common.UpdateManager;
import cn.weipan.fb.constact.Constant;
import cn.weipan.fb.fragments.BossFragment;
import cn.weipan.fb.fragments.CashierPlatformFragment;
import cn.weipan.fb.fragments.MessageFragment;
import cn.weipan.fb.fragments.StatementFragment;
import cn.weipan.fb.utils.DialogUtils;
import cn.weipan.fb.utils.HttpUtils;
import cn.weipan.fb.utils.ToastUtils;

/*
* 主界面
* Created by cc on 2016/8/2.
* 邮箱：904359289@QQ.com.
*
* */
public class MainActivity extends BaseBaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    public AppContext appContext;
    RadioGroup radiogroup;
    private ArrayList<Fragment> fragments;
    private boolean mHideorShow = false;
    private BadgeView badge;
    public Intent intent;
    private CashierPlatformFragment cashierPlatformFragment;
    private StatementFragment statementFragment;
    private MessageFragment messageFragment;
    private BossFragment bossFragment;
    private int mPercentForBuffering = 0;
    private int mPercentForPlaying = 0;
    public SpeechSynthesizer mTts;
    private String voicer = "xiaoyan";
    private SharedPreferences sp;
    private String jMsg;
    private Vibrator vibrator;
    private int count;
    boolean bisConnFlag = false;
    public DrawerLayout drawerlayout;
    public RelativeLayout rl_drawerlayout;
    private LinearLayout llMenu;
    private ImageView ivPersonal;
    private TextView tv_nicheng;
    private TextView tv_username;
    private InformationBean da;
    private String loginName;
    private String imageUrl;
    private String realName;
    private String result;
    private String success;
    private String userInfo;
    private String error;
    private static final String SHARE_APP_TAG = "isfirst";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appContext = (AppContext) getApplication();
        EventBus.getDefault().register(MainActivity.this);
        //注册声音
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=57c80d8f");
//        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=593e06d2");
        mTts = SpeechSynthesizer.createSynthesizer(this, null);

        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        //添加数字
        badge = new BadgeView(this, radiogroup);
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        sp = getSharedPreferences("userInfo", 0);

        fragments = new ArrayList<>();
        cashierPlatformFragment = new CashierPlatformFragment();
        statementFragment = new StatementFragment();
        bossFragment = new BossFragment();
        messageFragment = new MessageFragment();
        fragments.add(cashierPlatformFragment);
        fragments.add(statementFragment);
        fragments.add(bossFragment);
        fragments.add(messageFragment);

        setDefaultFragment(fragments.get(0));
        radiogroup.setOnCheckedChangeListener(this);
        radiogroup.check(R.id.rb_home);

        initView();
    }

    //初始化界面
    private void initView() {
        SharedPreferences settings = getSharedPreferences(SHARE_APP_TAG, 0);
        Boolean user_first = settings.getBoolean("GESFIRST", true);
        if (user_first) {
            settings.edit().putBoolean("GESFIRST", false).commit();
            DialogUtils.customDialog(this, "", "跳过", "确定", "设置手势密码后，可以通过此手势登录付吧", new DialogUtils.DialogCallback() {

                @Override
                public void PositiveButton(int i) {
                    switch (i) {
                        case -1:
                            // 取消
                            break;
                        case -2:
                            Intent intent = new Intent(MainActivity.this, CreateGestureActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            }, false, true);
        }
        rl_drawerlayout = (RelativeLayout) findViewById(R.id.rl_drawerlayout);//抽屉
        llMenu = (LinearLayout) findViewById(R.id.ll_menu);//主页面
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);//drawerlayout
        //抽屉监听
        drawerlayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                llMenu.setTranslationX(rl_drawerlayout.getMeasuredWidth() * slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        ivPersonal = (ImageView) findViewById(R.id.iv_personal);
        tv_nicheng = (TextView) findViewById(R.id.tv_nicheng);
        tv_username = (TextView) findViewById(R.id.tv_username);
        LinearLayout persona = (LinearLayout) findViewById(R.id.ll_personal);
        persona.setOnClickListener(this);
        TextView netWorkIsOpen = (TextView) findViewById(R.id.tv_network);
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
        }
        if (bisConnFlag) {
            netWorkIsOpen.setText("已启用");
        } else {
            netWorkIsOpen.setText("已关闭");
        }
        LinearLayout gesturePwd = (LinearLayout) findViewById(R.id.ll_gesture_pwd);
        gesturePwd.setOnClickListener(this);
        LinearLayout setting = (LinearLayout) findViewById(R.id.ll_setting);
        setting.setOnClickListener(this);
        RelativeLayout rl_Updata = (RelativeLayout) findViewById(R.id.rl_updata);
        rl_Updata.setOnClickListener(this);
        TextView versionCode = (TextView) findViewById(R.id.tv_version_code);
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //设置版本号
        String version = packInfo.versionName;
        if (version != null) {
            versionCode.setText(version);
        } else {
            versionCode.setText("");
        }
        RelativeLayout loginOut = (RelativeLayout) findViewById(R.id.rl_login_out);
        loginOut.setOnClickListener(this);

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
        String randomStr = this.getRandomString(8);
        userInfo = randomStr + id + number + "0000";
    }

    //获取用户信息
    private void getUserMassage() {
        String randomString = getRandomString(8);
        String url = Constant.URL + "/api/user/GetUserMessage?content=" + getContent(randomString) + "&key=" + getMiyaoKey(randomString);
        Log.i("test", "getUserMassage  url =" + url);
        Request request = new Request.Builder().url(url).get().build();
        HttpUtils.getAsyn(MainActivity.this, request, new HttpUtils.CallBack() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("ok", "失败=" + request);

            }

            @Override
            public void onResponse(String json) {
                Log.e("test", json);// {"Result":"0","Error":"成功","Data":{"UserID":"123456","CashType":"1","LoginName":"15824164947","Imageurl":"http://i.imgur.com/DvpvklR.png","Phone":"15824164947","QQ":"1031971893","Email":"1031971893@qq.com","AgentName":"杭州微盘代理商","AddTime":"2016-10-9","EndTime":"2016-10-9"}}
                try {
                    JSONObject object = new JSONObject(json);
                    result = object.optString("Result");
                    error = object.optString("Error");

                    if (TextUtils.equals(result, "0")) {
                        success = object.optString("Data");
                        Gson gson = new Gson();
                        da = gson.fromJson(success, InformationBean.class);
                        if (!TextUtils.isEmpty(da.getImageurl())) {
                            imageUrl = Constant.URL + da.getImageurl();
                            Picasso.with(MainActivity.this).load(imageUrl).transform(new Transformation() {
                                @Override
                                public Bitmap transform(Bitmap source) {
                                    int width = source.getWidth();
                                    int height = source.getHeight();
                                    Bitmap bitmap = Bitmap.createBitmap(width, height, source.getConfig());
                                    Canvas canvas = new Canvas(bitmap);
                                    float radius = Math.min(width, height) * 0.5f;
                                    Paint paint = new Paint();
                                    paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                                    canvas.drawCircle(width * 0.5f, height * 0.5f, radius, paint);
                                    source.recycle();
                                    return bitmap;
                                }

                                @Override
                                public String key() {
                                    return "key";
                                }
                            }).into(ivPersonal, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    ivPersonal.setBackgroundResource(R.drawable.login_avatar);
                                }
                            });
                        } else {
                            imageUrl = da.getImageurl();
                        }
                        loginName = da.getLoginName();
                        realName = da.getRealName();
//                        String addTime = da.getAddTime();
//                        String agentName = da.getAgentName();
//                        cashType = da.getCashType();
//                        int deviceID = da.getDeviceID();
//                        int cashID = da.getCashID();
//                        int siteID = da.getSiteID();
//                        String endTime = da.getEndTime();
//                        String email = da.getEmail();
//                        String phone = da.getPhone();
//                        String qq = da.getQQ();
                        String siteName = da.getSiteName();
                        //保存用户信息到首选项
                        SharedPreferences sp = getSharedPreferences("userInfo", 0);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("SiteName", siteName);
                        editor.putString("LoginName", loginName);
                        editor.putString("RealName", realName);
//                        editor.putString("Phone", phone);
//                        editor.putString("QQ", qq);
//                        editor.putString("Email", email);
//                        editor.putString("AgentName", agentName);
//                        editor.putString("AddTime", addTime);
//                        editor.putString("EndTime", endTime);
                        editor.putString("Imageurl", imageUrl);
                        editor.commit();

                        tv_nicheng.setText(realName);
                        tv_username.setText(loginName);
                    } else {
                        ToastUtils.showToast(MainActivity.this, error);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //当页面重新进入主界面读取用户信息
    @Override
    protected void onResume() {
        super.onResume();
        voicer = sp.getString("voicer", "xiaoyan");
        getUserMassage();
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //个人中心
            case R.id.ll_personal:
                intent = new Intent(MainActivity.this, InformationActivity.class);
                startActivity(intent);
                break;
            //手势密码
            case R.id.ll_gesture_pwd:
                intent = new Intent(MainActivity.this, ShouShiMiMaActivity.class);
                startActivity(intent);

                break;
            //当前版本
            case R.id.rl_updata:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        UpdateManager manager = new UpdateManager(MainActivity.this);
                        manager.checkUpdate();
                        Looper.loop();

                    }
                }).start();
                break;
            //设置
            case R.id.ll_setting:
                intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            //退出登录
            case R.id.rl_login_out:
                Constant.jpushMessage.clear();
                appContext.setDeviceId("");
                AppManager.getAppManager().finishAllActivity();
                intent = new Intent(MainActivity.this, LoginNewActivity.class);
                intent.putExtra("activity", "MainActivity");
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    //接受小红点开关
    @Subscribe
    public void onEvent(SecondEvent event) {
        mHideorShow = event.getBoolean();
        Log.i("test", "onEventonEvent" + mHideorShow);
        if (mHideorShow) {
            badge.show();
        } else {
            count = 0;
            badge.hide();
        }
    }

    //接收左边系统jpush消息
    @Subscribe
    public void onMsgsEvent(FirstEvent events) {
        jMsg = events.getMsg();
        boolean isvoice = sp.getBoolean("isvoice", true);
        boolean ismove = sp.getBoolean("ismove", true);
        //是否开启震动
        if (ismove) {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
            vibrator.vibrate(pattern, -1);
        }
        count++;
        if (count > 99) {
            count = 99;
            badge.setText("99+");
        } else {
            badge.setText(String.valueOf(count));
        }
        badge.show();

        //是否开启声音
        if (isvoice) {
            String text = jMsg;
            String[] temp = null;
            temp = text.split("\\|");

            Log.i("test", "main = text = " + text);
            if (text != null) {
                setParam2();
                if (temp.length > 3) {
                    if (temp[0].equals("1")) {
                        temp[0] = "微信";
                    } else if (temp[0].equals("2")) {
                        temp[0] = "支付宝";
                    } else if (temp[0].equals("3")) {
                        temp[0] = "百度钱包";
                    } else if (temp[0].equals("4")) {
                        temp[0] = "现金";
                    } else if (temp[0].equals("5")) {
                        temp[0] = "pos机";
                    }else if (temp[0].equals("6")) {
                        temp[0] = "QQ钱包";
                    } else if (temp[0].equals("7")) {
                        temp[0] = "京东钱包";
                    }else{
                        temp[0] = "";
                    }
                    mTts.startSpeaking(temp[0] + "收款" + temp[1] + "元", mTtsListener);
//                    AllMessagBean allMessagBean = new AllMessagBean(temp[0] + "支付收款", temp[3], temp[2], temp[1]);//标题，时间，单号，类型
//                    Constant.jpushMessage.add(0, allMessagBean);
                    EventBus.getDefault().post(new ThreadEvent(jMsg));
                } else {
                    mTts.startSpeaking(temp[0], mTtsListener);
                }
            }
        }

    }

    //返回按钮监听
    @Override
    public void onBackPressed() {
        DialogUtils.customDialog(this, "", "取消", "确定", "确定要退出付吧吗？", new DialogUtils.DialogCallback() {

            @Override
            public void PositiveButton(int i) {
                switch (i) {
                    case -1:
                        // 取消
                        break;
                    case -2:
                        appContext.setDeviceId("");
                        finish();
                        AppManager.getAppManager().AppExit(getBaseContext());
                        break;

                    default:
                        break;
                }
            }
        }, false, true);

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

    //获取key
    public String getMiyaoKey(String randomStr) {
        return randomStr + getMiyao(randomStr);
    }

    //获取content
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

    //获取随机数
    public String getRandomString(int length) {
        String base = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    private Fragment preFragment;

    //底部导航栏切换
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int index = group.indexOfChild(group.findViewById(checkedId));
        Fragment fragment = fragments.get(index);

        if (preFragment != fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!fragment.isAdded()) { // 先判断是否被add过
                transaction.hide(preFragment).add(R.id.framelayout, fragment).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(preFragment).show(fragment).commit(); // 隐藏当前的fragment，显示下一个
            }

//            transaction.replace(R.id.framelayout, fragment).commit(); // 替换fragment
            preFragment = fragment;
        }
    }

    private void setDefaultFragment(Fragment fm) {
        FragmentTransaction mFragmentTrans = getSupportFragmentManager().beginTransaction();

        mFragmentTrans.add(R.id.framelayout, fm).commit();

        preFragment = fm;
    }

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    public void setParam2() {
        // 设置合成
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置发音人

        mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
        // 设置语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        // 设置音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        // 设置音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
    }

    /**
     * 合成回调监听。
     */
    public SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            mPercentForBuffering = percent;
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            mPercentForPlaying = percent;
        }

        @Override
        public void onCompleted(SpeechError error) {
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

}
