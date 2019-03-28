package cn.weipan.fb.act;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.weipan.fb.R;
import cn.weipan.fb.adapter.InOrderViewPagerAdapter;
import cn.weipan.fb.common.AppContext;
import cn.weipan.fb.utils.DialogUtils;

/**
 * Created by cc on 2016/7/26.
 * 邮箱：904359289@QQ.com.
 * 引导界面
 */
public class WelcomeActivity extends BaseNoLoginActivity implements ViewPager.OnPageChangeListener {
    private int which;
    private int[] image_id = {R.drawable.qdya, R.drawable.qdyb, R.drawable.qdyc};
    public AppContext appContext;
    private ImageView welcome_skip;
    private ImageView mPreSelectedBt;
    private LinearLayout points_view;
    private InOrderViewPagerAdapter viewPagerAdapter;
    private List<View> view_list = new ArrayList<View>();
    private ViewPager pager;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome_view);
        points_view = (LinearLayout) findViewById(R.id.points_view);
        points_view.setDividerPadding(80);
        welcome_skip = (ImageView) findViewById(R.id.welcome_skip);
        welcome_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(WelcomeActivity.this, LoginNewActivity.class);
                startActivity(intent);
                finish();
            }
        });

        for (int i = 0; i < image_id.length; i++) {
            ImageView bt = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            bt.setLayoutParams(lp);
            if (i < image_id.length - 1) {
                bt.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.zsqwxz));
                bt.setPadding(20, 20, 20, 20);
            } else {
                bt.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.zsqxz));
                bt.setPadding(20, 20, 20, 20);
            }
            points_view.addView(bt);
            mPreSelectedBt = (ImageView) points_view.getChildAt(i);
        }
        layoutViewPager();
    }

    //引导界面图片更换
    private void layoutViewPager() {
        for (int i = 0; i < image_id.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.welcome_view_image, null);
            RelativeLayout welcome_iamgeview = (RelativeLayout) view.findViewById(R.id.welcome_iamgeview);
            welcome_iamgeview.setBackgroundResource(image_id[i]);
            view_list.add(view);
//            if (i == 2) {
//                view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View arg0) {
//                        intent = new Intent(WelcomeActivity.this, LoginNewActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//            }
        }
        viewPagerAdapter = new InOrderViewPagerAdapter(view_list);
        pager = (ViewPager) findViewById(R.id.welcome_viewpager);
        pager.setOnPageChangeListener(this);
        pager.setAdapter(viewPagerAdapter);
        pager.setFocusable(true);
//        pager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_MOVE:
//                        if (pager.getCurrentItem() == image_id.length - 1 && which == image_id.length - 1) {
//                            intent = new Intent(WelcomeActivity.this, LoginNewActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                        break;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        DialogUtils.customDialog(WelcomeActivity.this, "", "取消", "确定", "确定要退出付吧吗？", new DialogUtils.DialogCallback() {

            @Override
            public void PositiveButton(int i) {

                switch (i) {
                    case -1:
                        // 取消
                        break;
                    case -2:
                        finish();
                        break;

                    default:
                        break;
                }
            }
        }, false, true);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        which = arg0;
    }

    @Override
    public void onPageSelected(int arg0) {
        if (arg0 < image_id.length - 1) {
            welcome_skip.setVisibility(View.GONE);
        } else {
            welcome_skip.setVisibility(View.VISIBLE);
        }
        if (mPreSelectedBt != null) {
            mPreSelectedBt.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.zsqwxz));
            mPreSelectedBt.setPadding(20, 20, 20, 20);
        }
        ImageView view = (ImageView) points_view.getChildAt(arg0);
        view.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.zsqxz));
        view.setPadding(20, 20, 20, 20);
        mPreSelectedBt = view;
    }
}


