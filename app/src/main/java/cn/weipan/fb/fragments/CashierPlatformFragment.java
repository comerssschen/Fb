package cn.weipan.fb.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.weipan.fb.R;
import cn.weipan.fb.act.BannerActivity;
import cn.weipan.fb.act.MainActivity;
import cn.weipan.fb.act.shouye.PayMoneyActivity;
import cn.weipan.fb.act.shouye.SaoMaActivity;
import cn.weipan.fb.act.shouye.ZhiHuiMaActivity;
import cn.weipan.fb.common.FirstEvent;
import cn.weipan.fb.common.Constant;
import cn.weipan.fb.utils.HttpUtils;
import cn.weipan.fb.utils.ToastUtils;

/**
 * 收银主页
 * Created by cc on 2016/9/19.
 */
public class CashierPlatformFragment extends BaseFragment implements View.OnClickListener {
    private Intent intent;
    public ViewPager viewpager;
    int msgWhat = 0;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);// 收到消息，指向下一个页面
            handler.sendEmptyMessageDelayed(msgWhat, 3000);// 5S后在发送一条消息，由于在handleMessage()方法中，造成死循环。
        }
    };
    private LinearLayout ll_dots;
    private TextView tv_ratio;
    private TextView tv_total;
    private TextView tv_desc;
    private String[] des_arrays = new String[]{"今日收款合计(元)", "本月收款合计(元)"};
    private String[] des_arrayss = new String[]{"0.00", "0.00"};
    private String[] des_arraysss = new String[]{"（合计0笔）", "（合计0笔）"};
    private List<TextView> tv_dots_list = new ArrayList<TextView>();
    private TextView tv_dot;
    private ImageView bannerImageView;
    private String totalDayCount;
    private String totalDayMoney;
    private String result;
    private String totalMonthMoney;
    private String totalMonthCount;
    private EventBus aDefault;
    private String linkurl;
    private String qcodeurl;
    private String bgqcodeurl;
    private String qcodeurlshow;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cashier_platform, container, false);
    }

    //初始化界面
    protected void initView(View mChildContentView, Bundle savedInstanceState) {
        Log.i("test", System.currentTimeMillis() + "initView");
        //注册接受消息的eventbus
        aDefault = EventBus.getDefault();
        aDefault.register(CashierPlatformFragment.this);
        viewpager = mChildContentView.findViewById(R.id.viewpager);
        viewpager.setAdapter(new MyAdapter());
        viewpager.setCurrentItem(2 * 1000);// 当前页是5000页,也即是0页：因为5000%5=0.
        // 初始化指示器
        ll_dots = mChildContentView.findViewById(R.id.ll_dots);
        tv_ratio = mChildContentView.findViewById(R.id.tv_ratio);
        tv_desc = mChildContentView.findViewById(R.id.tv_desc);
        tv_total = mChildContentView.findViewById(R.id.tv_total);
        TextView headerTitle = mChildContentView.findViewById(R.id.head_view_title);
        headerTitle.setText("收银主页");
        LinearLayout fanhui = mChildContentView.findViewById(R.id.ll_fanhui);
        fanhui.setOnClickListener(this);
        ImageView headerBack = mChildContentView.findViewById(R.id.but_header_back);
        headerBack.setImageResource(R.drawable.caidan);
        TextView tvHeaderBack = mChildContentView.findViewById(R.id.tv_header_back);
        tvHeaderBack.setVisibility(View.INVISIBLE);
        //扫一扫
        LinearLayout main_reach_scan = mChildContentView.findViewById(R.id.main_reach_scan);
        main_reach_scan.setOnClickListener(this);
        //收款码
        LinearLayout main_payment_code = mChildContentView.findViewById(R.id.main_payment_code);
        main_payment_code.setOnClickListener(this);
        //卡券收款
        LinearLayout kaquanshoukuan = mChildContentView.findViewById(R.id.ll_kaquanshoukuan);
        kaquanshoukuan.setOnClickListener(this);
        //卡券核销
        LinearLayout kaquanhexiao = mChildContentView.findViewById(R.id.ll_kaquanhexiao);
        kaquanhexiao.setOnClickListener(this);
        //智慧码
        LinearLayout ll_zhihuima = mChildContentView.findViewById(R.id.ll_zhihuima);
        ll_zhihuima.setOnClickListener(this);
        //退款
        LinearLayout ll_tuikuan = mChildContentView.findViewById(R.id.ll_tuikuan);
        ll_tuikuan.setOnClickListener(this);
        //banner广告图
        bannerImageView = mChildContentView.findViewById(R.id.iv_image_banner);
        bannerImageView.setOnClickListener(this);
        initIndicator();//监听pager的变化
        getTodayCount();
        getBannerUrl();
        getUserMassage();
    }


    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fanhui:
                ((MainActivity) getActivity()).drawerlayout.openDrawer(((MainActivity) getActivity()).rl_drawerlayout);
                break;
            //扫一扫
            case R.id.main_reach_scan:
                intent = new Intent(appContext, PayMoneyActivity.class);
                intent.putExtra("Activity", "SaoMaActivity");
                startActivity(intent);
                break;
            //收款码
            case R.id.main_payment_code:
                intent = new Intent(appContext, PayMoneyActivity.class);
                intent.putExtra("Activity", "CollectionActivity");
                startActivity(intent);
                break;
            //卡券收款
            case R.id.ll_kaquanshoukuan:
//                ToastUtils.showToast(getActivity(), "开发中，敬请期待！");
                intent = new Intent(appContext, PayMoneyActivity.class);
                intent.putExtra("Activity", "KaquanshoukuanActivity");
                startActivity(intent);
                break;
            //卡券核销
            case R.id.ll_kaquanhexiao:
                intent = new Intent(appContext, SaoMaActivity.class);
                intent.putExtra("Activity", "KaquanhexiaoActivity");
                startActivity(intent);
                break;
            //智慧码
            case R.id.ll_zhihuima:
                intent = new Intent(appContext, ZhiHuiMaActivity.class);
                intent.putExtra("qcodeurl", qcodeurl);
                intent.putExtra("bgqcodeurl", bgqcodeurl);
                intent.putExtra("qcodeurlshow", qcodeurlshow);
                startActivity(intent);
                break;
            //退款
            case R.id.ll_tuikuan:
                if (Constant.isTuiKuan) {
                    intent = new Intent(appContext, SaoMaActivity.class);
                    intent.putExtra("Activity", "TuiKuanActivity");
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(getActivity(), "您没有退款权限！");
                }
                break;
            case R.id.iv_image_banner:
                intent = new Intent(appContext, BannerActivity.class);
                intent.putExtra("url", linkurl);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    // 初始化圆点
    private void initDots() {
        ll_dots.removeAllViews();
        tv_dots_list.clear();
        // LinearLayout的宽高
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
        // layout子view的间距
        params.setMargins(0, 0, 10, 0);
        for (int i = 0; i < 2; i++) {
            tv_dot = new TextView(mContext);
            tv_dot.setBackgroundResource(R.drawable.tv_dot_selector);
            ll_dots.addView(tv_dot, params);
            tv_dots_list.add(tv_dot);
        }
    }

    //在viewpager的滚动监听事件中实现文字和数值的变化。
    public void initIndicator() {
        initDots();// 初始化圆点
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tv_desc.setText(des_arrays[position % 2]);
                tv_ratio.setText(des_arrayss[position % 2]);
                tv_total.setText(des_arraysss[position % 2]);
                for (int i = 0; i < tv_dots_list.size(); i++) {
                    if (i == position % 2) {
                        tv_dots_list.get(i).setSelected(true);
                    } else {
                        tv_dots_list.get(i).setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public class MyAdapter extends PagerAdapter {
        // 表示viewpager共存放了多少个页面
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;// 我们设置viewpager中有Integer.MAX_VALUE个页面
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("test", " cash  onResume");
        handler.sendEmptyMessageDelayed(msgWhat, 3000);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getTodayCount();
            Log.i("test", "切换到了cashfragment");
        }
    }

    /**
     * 当MainActivity不可见的时候让handler停止发送消息 防止内存泄露
     */
    @Override
    public void onStop() {
        super.onStop();
        handler.removeMessages(msgWhat);
    }

    //接收左边系统jpush消息
    @Subscribe
    public void onEvent(FirstEvent events) {
        String text = events.getMsg();
        Log.i("test", "cashfragment = text = " + text);
        getTodayCount();
    }

    //获取统计轮播数据
    private void getTodayCount() {
        String randomString = ((MainActivity) getActivity()).getRandomString(8);
        String content = ((MainActivity) getActivity()).getContent(randomString);
        String miyaoKey = ((MainActivity) getActivity()).getMiyaoKey(randomString);

        String url = Constant.URL + "/api/FuBaAppIndex/TodayCount?content=" + content + "&key=" + miyaoKey + "&CashType=" + appContext.getCashType();
        Log.i("test", "getTodayCount url = " + url);
        Request request = new Request.Builder().url(url).get().build();
        HttpUtils.getAsyn(getActivity(), request, new HttpUtils.CallBack() {
            private String success;

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("test", "请求失败");
            }

            @Override
            public void onResponse(String json) {
                Log.e("test", "getTodayCount = " + json);//{"Result":"0","Error":"成功","Data":{"UserID":"123456","CashType":"1","LoginName":"15824164947","Phone":"15824164947","QQ":"1031971893","Email":"1031971893@qq.com","AgentName":"杭州微盘代理商","AddTime":"2016-10-9","EndTime":"2016-10-9"}}
                try {
                    JSONObject object = new JSONObject(json);
                    result = object.optString("Result");
                    if (TextUtils.equals(result, "0")) {
                        success = object.optString("allTotal");
                        JSONObject bannerBean = new JSONObject(success);
                        totalDayCount = bannerBean.optString("TotalDayCount");
                        totalDayMoney = bannerBean.optString("TotalDayMoney");
                        totalMonthCount = bannerBean.optString("TotalMonthCount");
                        totalMonthMoney = bannerBean.optString("TotalMonthMoney");
                        des_arrayss = new String[]{totalDayMoney, totalMonthMoney};
                        des_arraysss = new String[]{"（合计" + totalDayCount + "笔）", "（合计" + totalMonthCount + "笔）"};
                    } else {
                        ToastUtils.showToast(getActivity(), object.optString("Error"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getBannerUrl() {
        String randomString = ((MainActivity) getActivity()).getRandomString(8);
        String content = ((MainActivity) getActivity()).getContent(randomString);
        String miyaoKey = ((MainActivity) getActivity()).getMiyaoKey(randomString);
        String url = Constant.URL + "/api/FuBaAppIndex/GetBanner?content=" + content + "&key=" + miyaoKey;
        Request request = new Request.Builder().url(url).get().build();
        HttpUtils.getAsyn(getActivity(), request, new HttpUtils.CallBack() {
            private String success;

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("test", "请求失败");
            }

            @Override
            public void onResponse(String json) {
                Log.i("test", "getBannerUrl = " + json);
                try {
                    JSONObject object = new JSONObject(json);
                    String result = object.optString("Result");
                    if (TextUtils.equals(result, "0")) {
                        bannerImageView.setVisibility(View.VISIBLE);
                        success = object.optString("data");
                        object = new JSONObject(success);
                        String Imgurl = object.optString("Imgurl");
                        linkurl = object.optString("Linkurl");
                        Picasso.with(getActivity()).load(Constant.URLZHM + Imgurl).into(bannerImageView);
                    } else {
                        ToastUtils.showToast(getActivity(), object.optString("Error"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getUserMassage() {
        String randomString = ((MainActivity) getActivity()).getRandomString(8);
        String content = ((MainActivity) getActivity()).getContent(randomString);
        String miyaoKey = ((MainActivity) getActivity()).getMiyaoKey(randomString);
        //http://testweipanapi.payweipan.com/api/FuBaAppIndex/GetQCodeUrl?content=9C460D689067368F7B495AA82AB2343552D852CCCF6E63CDA20F052256349A71&key=lRFU00Wl4603C07A4ABB929E
        String url = Constant.URL + "/api/FuBaAppIndex/GetQCodeUrl?content=" + content + "&key=" + miyaoKey;
        Log.e("test", "urlurlurlurl" + url);
        Request request = new Request.Builder().url(url).get().build();
        HttpUtils.getAsyn(getActivity(), request, new HttpUtils.CallBack() {
            public String Result;
            private String success;

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("test", "失败" + request);
            }

            @Override
            public void onResponse(String json) {
                Log.e("test", json);//{"Result": 0,"Error": "成功","Data": {"qcodeurl": "http://QCodeWXPay.payweipan.com/?siteid=1108&sId=419&DeviceId=10006740&cashId=16885","bgqcodeurl": "http://testweipanapi.payweipan.com/img/zhihuicode_bg.png"
                try {
                    JSONObject object = new JSONObject(json);
                    Result = object.optString("Result");
                    if (TextUtils.equals(Result, "0")) {
                        success = object.optString("Data");
                        JSONObject jsonObject = new JSONObject(success);
                        qcodeurl = jsonObject.optString("qcodeurl");
                        bgqcodeurl = jsonObject.optString("bgqcodeurl");
                        qcodeurlshow = jsonObject.optString("qcodeurlshow");
                    } else {
                        ToastUtils.showToast(getActivity(), object.optString("Error"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }
}



