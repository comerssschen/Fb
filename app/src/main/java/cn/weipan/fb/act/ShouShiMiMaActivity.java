package cn.weipan.fb.act;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import cn.weipan.fb.R;
import cn.weipan.fb.constact.ACache;
import cn.weipan.fb.constact.Constant;

/**
 * 设置手势密码界面
 * Created by cc on 2016/10/24.
 * 邮箱：904359289@QQ.com.
 */
public class ShouShiMiMaActivity extends BaseActivity implements View.OnClickListener {
    private ACache aCache;
    private Intent intent;
    private Switch s;
    private SharedPreferences sp;
    private RelativeLayout rl_forgetgesturelogin;
    private RelativeLayout rl_creatgesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoushimima);
        initView();
    }

    //初始化界面
    private void initView() {
        LinearLayout fanhui = (LinearLayout) findViewById(R.id.ll_fanhui);
        fanhui.setOnClickListener(this);
        TextView header = (TextView) findViewById(R.id.head_view_title);
        header.setText("手势密码");
        rl_creatgesture = (RelativeLayout) findViewById(R.id.rl_creatgesture);
        rl_creatgesture.setOnClickListener(this);
        rl_forgetgesturelogin = (RelativeLayout) findViewById(R.id.rl_forgetgesturelogin);
        rl_forgetgesturelogin.setOnClickListener(this);
        sp = getSharedPreferences("userInfo", 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isGesture = sp.getBoolean("isGesture", false);
        s = (Switch) findViewById(R.id.tb_news);
        s.setOnClickListener(this);
        if (isGesture) {
            s.setChecked(true);
        } else {
            s.setChecked(false);

        }
        if (s.isChecked()) {
            rl_creatgesture.setClickable(true);
            rl_forgetgesturelogin.setClickable(true);
        } else {
            rl_creatgesture.setClickable(false);
            rl_forgetgesturelogin.setClickable(false);
        }
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fanhui:
                finish();
                break;
            //手势密码开关按钮
            case R.id.tb_news:
                if (s.isChecked()) {
                    aCache = ACache.get(ShouShiMiMaActivity.this);
                    String gesturePassword = aCache.getAsString(Constant.GESTURE_PASSWORD);
                    if (gesturePassword == null || "".equals(gesturePassword)) {
                        intent = new Intent(ShouShiMiMaActivity.this, CreateGestureActivity.class);
                        intent.putExtra("Activity", "MainActivity");
                        startActivity(intent);
                    } else {
                        intent = new Intent(ShouShiMiMaActivity.this, GestureLoginActivity.class);
                        intent.putExtra("Activity", "MainActivity");
                        startActivity(intent);
                    }
//                    finish();
                } else {
                    intent = new Intent(ShouShiMiMaActivity.this, GestureLoginActivity.class);
                    intent.putExtra("Activity", "ShouShiMiMa");
                    startActivity(intent);
                }
                break;
            //修改手势密码
            case R.id.rl_creatgesture:
                intent = new Intent(ShouShiMiMaActivity.this, GestureLoginActivity.class);
                intent.putExtra("Activity", "XiuGaiMiMa");
                startActivity(intent);
                break;
            //忘记手势密码
            case R.id.rl_forgetgesturelogin:
                startActivity(new Intent(ShouShiMiMaActivity.this, LoginPWDActivity.class));
                break;
            default:
                break;
        }
    }
}
