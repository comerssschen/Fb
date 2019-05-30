package cn.weipan.fb.act;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.weipan.fb.R;
import cn.weipan.fb.adapter.InOrderViewPagerAdapter;

public class GuideActivity extends BaseNoLoginActivity implements ViewPager.OnPageChangeListener {

    private ViewPager vp;
    private int[] imageIdArray;//图片资源的数组
    private List<View> viewList;//图片资源的集合
    private ViewGroup vg;//放置圆点

    //实例化原点View
    private ImageView[] ivPointArray;
    private ImageView welcome_skip;
    private Intent intent;
    private TextView tv_return;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome_view);
        //加载ViewPager
        initViewPager();
        //加载底部圆点
        initPoint();
        welcome_skip = findViewById(R.id.welcome_skip);
        welcome_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(GuideActivity.this, LoginNewActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tv_return = findViewById(R.id.tv_return);
        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(GuideActivity.this, LoginNewActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * 加载底部圆点
     */
    private void initPoint() {
        //这里实例化LinearLayout
        vg = findViewById(R.id.points_view);
        //根据ViewPager的item数量实例化数组
        ivPointArray = new ImageView[viewList.size()];
        //循环新建底部圆点ImageView，将生成的ImageView保存到数组中
        int size = viewList.size();
        for (int i = 0; i < size; i++) {
            ImageView bt = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            bt.setLayoutParams(lp);
            ivPointArray[i] = bt;
            if (i == 0) {
                bt.setImageResource(R.drawable.zsqxz);
                bt.setPadding(10, 10, 10, 10);
            } else {
                bt.setImageResource(R.drawable.zsqwxz);
                bt.setPadding(10, 10, 10, 10);
            }
            vg.addView(bt);
        }


    }

    /**
     * 加载图片ViewPager
     */
    private void initViewPager() {
        vp = findViewById(R.id.welcome_viewpager);
        //实例化图片资源
        imageIdArray = new int[]{R.drawable.qdya, R.drawable.qdyb, R.drawable.qdyc};
        viewList = new ArrayList<>();
        //循环创建View并加入到集合中
        int len = imageIdArray.length;
        for (int i = 0; i < len; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.welcome_view_image, null);
            ImageView welcome_iamgeview = (ImageView) view.findViewById(R.id.imageview_guide);
            welcome_iamgeview.setBackgroundResource(imageIdArray[i]);
            viewList.add(view);
        }

        //View集合初始化好后，设置Adapter
        vp.setAdapter(new InOrderViewPagerAdapter(viewList));
        //设置滑动监听
        vp.setOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滑动后的监听
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        //循环设置当前页的标记图
        int length = imageIdArray.length;
        for (int i = 0; i < length; i++) {
            if (position != i) {
                ivPointArray[i].setImageResource(R.drawable.zsqwxz);
                ivPointArray[i].setPadding(10, 10, 10, 10);
            } else {
                ivPointArray[position].setImageResource(R.drawable.zsqxz);
                ivPointArray[position].setPadding(10, 10, 10, 10);
            }
        }
        //判断是否是最后一页，若是则显示按钮
        if (position == imageIdArray.length - 1) {
            welcome_skip.setVisibility(View.VISIBLE);
            tv_return.setVisibility(View.GONE);

        } else {
            welcome_skip.setVisibility(View.GONE);
            tv_return.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
