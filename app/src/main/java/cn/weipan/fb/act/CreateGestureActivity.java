package cn.weipan.fb.act;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.star.lockpattern.util.LockPatternUtil;
import com.star.lockpattern.widget.LockPatternIndicator;
import com.star.lockpattern.widget.LockPatternView;

import java.util.ArrayList;
import java.util.List;

import cn.weipan.fb.R;
import cn.weipan.fb.constact.ACache;
import cn.weipan.fb.constact.Constant;

/**
 * 创建手势密码
 * Created by cc on 2016/7/27.
 * 邮箱：904359289@QQ.com.
 */
public class CreateGestureActivity extends BaseActivity implements View.OnClickListener {

    LockPatternIndicator lockPatternIndicator;
    LockPatternView lockPatternView;
    TextView messageTv;

    private List<LockPatternView.Cell> mChosenPattern = null;
    private ACache aCache;
    private static final long DELAYTIME = 600L;
    private String activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gesture);
        this.init();
        activity = getIntent().getStringExtra("Activity");

    }

    //初始化控件
    private void init() {
        LinearLayout fanhui = (LinearLayout) findViewById(R.id.ll_fanhui);
        fanhui.setOnClickListener(this);
        TextView header = (TextView) findViewById(R.id.head_view_title);
        header.setText("设置手势密码");

        lockPatternIndicator = (LockPatternIndicator) findViewById(R.id.lockPatterIndicator);
        lockPatternView = (LockPatternView) findViewById(R.id.lockPatternView);
//        resetBtn = (Button) findViewById(R.id.resetBtn);
        messageTv = (TextView) findViewById(R.id.messageTv);
        aCache = ACache.get(CreateGestureActivity.this);
        lockPatternView.setOnPatternListener(patternListener);
        //重新设置
//        resetLockPattern = (Button) findViewById(R.id.resetBtn);
//        resetLockPattern.setOnClickListener(this);

    }

    /**
     * 手势监听
     */
    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
            lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if (mChosenPattern == null && pattern.size() >= 4) {
                mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
                updateStatus(Status.CORRECT, pattern);
            } else if (mChosenPattern == null && pattern.size() < 4) {
                updateStatus(Status.LESSERROR, pattern);
            } else if (mChosenPattern != null) {
                if (mChosenPattern.equals(pattern)) {
                    updateStatus(Status.CONFIRMCORRECT, pattern);
                } else {
                    updateStatus(Status.CONFIRMERROR, pattern);
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * 更新状态
     *
     * @param status
     * @param pattern
     */
    private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
        messageTv.setTextColor(getResources().getColor(status.colorId));
        messageTv.setText(status.strId);
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CORRECT:
                updateLockPatternIndicator();
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case LESSERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CONFIRMERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CONFIRMCORRECT:
                saveChosenPattern(pattern);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                setLockPatternSuccess();
                break;
        }
    }

    /**
     * 更新 Indicator
     */
    private void updateLockPatternIndicator() {
        if (mChosenPattern == null)
            return;
        lockPatternIndicator.setIndicator(mChosenPattern);
    }

    /**
     * 成功设置了手势密码(跳到首页)
     */
    private void setLockPatternSuccess() {
        SharedPreferences sp = getSharedPreferences("userInfo", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isGesture", true);
        editor.commit();
//        startActivity(new Intent(CreateGestureActivity.this, ShouShiMiMaActivity.class));
        finish();
//        if ("activity".equals("MainActivity")) {
//            finish();
//        } else if ("activity".equals("GestureLoginActivity")) {
//            startActivity(new Intent(CreateGestureActivity.this, LoginNewActivity.class));
//        }
    }

    /**
     * 保存手势密码
     */
    private void saveChosenPattern(List<LockPatternView.Cell> cells) {
        byte[] bytes = LockPatternUtil.patternToHash(cells);
        aCache.put(Constant.GESTURE_PASSWORD, bytes);

//        Boolean isGesturePwd = true;
//        SharedPreferences userInfo = getSharedPreferences("userInfo", 0);
//        SharedPreferences.Editor editor = userInfo.edit();
//        editor.putBoolean("isGesturePwd",isGesturePwd);
//        editor.commit();
    }

    /**
     * 重新设置手势
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.resetBtn:
//                mChosenPattern = null;
//                lockPatternIndicator.setDefaultIndicator();
//                updateStatus(Status.DEFAULT, null);
//                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
//                break;
            case R.id.ll_fanhui:
                finish();
                break;
            default:
                break;
        }
    }

    private enum Status {
        //默认的状态，刚开始的时候（初始化状态）
        DEFAULT(R.string.create_gesture_default, R.color.grey_a5a5a5),
        //第一次记录成功
        CORRECT(R.string.create_gesture_correct, R.color.grey_a5a5a5),
        //连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
        LESSERROR(R.string.create_gesture_less_error, R.color.red_f4333c),
        //二次确认错误
        CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.red_f4333c),
        //二次确认正确
        CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.grey_a5a5a5);

        Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }

        private int strId;
        private int colorId;
    }
}
