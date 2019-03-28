package cn.weipan.fb.fragments;

import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.weipan.fb.R;
import cn.weipan.fb.act.MainActivity;
import cn.weipan.fb.act.zhangdan.BaseZhangDanWebActivity;
import cn.weipan.fb.act.zhangdan.ShouKuanQuShiWebActivity;
import cn.weipan.fb.common.FirstEvent;
import cn.weipan.fb.constact.Constant;
import cn.weipan.fb.utils.HttpUtils;

/*
* 账单fragment
* Created by cc on 2016/7/27.
* */
public class StatementFragment extends BaseFragment implements View.OnClickListener {
    public ViewPager viewpager;
    int msgWhat = 0;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);// 收到消息，指向下一个页面
            handler.sendEmptyMessageDelayed(msgWhat, 5000);// 5S后在发送一条消息，由于在handleMessage()方法中，造成死循环。
        }
    };
    private LinearLayout ll_dots;
    private TextView tv_ratio;
    private TextView tv_total;
    private TextView tv_desc;
    private String[] des_arrays;
    private String[] des_arrayss;
    private String[] des_arraysss;
    private List<TextView> tv_dots_list = new ArrayList<TextView>();
    private TextView tv_dot;
    private Intent intent;
    private List<ImageView> imageList = new ArrayList<ImageView>();
    private String totalMonthCount;
    private String totalMonthMoney;
    private String lastmonthCount;
    private String lastmonthMoney;
    private String totalYestMoney;
    private String totalDayCount;
    private String totalDayMoney;
    private String totalYestCount;
    private String result;
    private RelativeLayout rlShouKuan;
    private RelativeLayout rlHeXiao;
    private RelativeLayout rlTuiKuan;
    private RelativeLayout rlShouKuanQuShi;
    private TextView tv_shoukuanzhangdan;
    private TextView tv_hexiaozhangdan;
    private TextView tv_tuikuanzhangdan;
    private TextView tv_shoukuanqushi;
    private ImageView iv_shoukuanzhangdan;
    private ImageView iv_hexiaozhangdan;
    private ImageView iv_tuikuanzhangdan;
    private ImageView iv_shoukuanqushi;
    private String url1;
    private String url2;
    private String url3;
    private String url21;
    private String title1;
    private String title2;
    private String title3;
    private String imgsrc1;
    private String imgsrc2;
    private String imgsrc3;
    private String title21;
    private String imgsrc21;
    private EventBus aDefault;
    private String urlnext11;
    private String urlnext12;
    private String urlnext13;
    private String urlnext21;
    private String type11;
    private String type12;
    private String type13;
    private String type21;
    private String urlnext22;
    private String type22;
    private String url22;
    private String title22;
    private String imgsrc22;
    private RelativeLayout rlHuiYuanZhangDan;
    private RelativeLayout rlJiFenZhangDan;
    private TextView tv_huiyuanzhangdan;
    private TextView tv_jifenzhangdan;
    private ImageView iv_huiyuanzhangdan;
    private ImageView iv_jifenzhangdan;
    private String url31;
    private String title31;
    private String imgsrc31;
    private String urlnext31;
    private String type31;
    private String style21;
    private String style22;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statement, container, false);
    }

    //初始化
    @Override
    protected void initView(View mChildContentView, Bundle savedInstanceState) {
        //注册接受消息的eventbus
        aDefault = EventBus.getDefault();
        aDefault.register(StatementFragment.this);


        viewpager = (ViewPager) mChildContentView.findViewById(R.id.viewpager);
        viewpager.setAdapter(new MyAdapter());
        viewpager.setCurrentItem(4 * 1000);// 当前页是5000页,也即是0页：因为5000%5=0.
        // 初始化指示器
        ll_dots = (LinearLayout) mChildContentView.findViewById(R.id.ll_dots);
        tv_ratio = (TextView) mChildContentView.findViewById(R.id.tv_ratio);
        tv_desc = (TextView) mChildContentView.findViewById(R.id.tv_desc);
        tv_total = (TextView) mChildContentView.findViewById(R.id.tv_total);

        initDesData();// 图片描述数据
        initIndicator();//监听pager的变化

        TextView headerTitle = (TextView) mChildContentView.findViewById(R.id.head_view_title);
        headerTitle.setText("账单统计");
        LinearLayout fanhui = (LinearLayout) mChildContentView.findViewById(R.id.ll_fanhui);
        fanhui.setOnClickListener(this);
        ImageView headerBack = (ImageView) mChildContentView.findViewById(R.id.but_header_back);
        headerBack.setImageResource(R.drawable.caidan);
        TextView tvHeaderBack = (TextView) mChildContentView.findViewById(R.id.tv_header_back);
        tvHeaderBack.setVisibility(View.INVISIBLE);
        rlShouKuan = (RelativeLayout) mChildContentView.findViewById(R.id.rl_shoukuanzhangdan);
        rlShouKuan.setOnClickListener(this);
        rlHeXiao = (RelativeLayout) mChildContentView.findViewById(R.id.rl_hexiaozhangdan);
        rlHeXiao.setOnClickListener(this);
        rlTuiKuan = (RelativeLayout) mChildContentView.findViewById(R.id.rl_tuikuanzhangdan);
        rlTuiKuan.setOnClickListener(this);
        rlShouKuanQuShi = (RelativeLayout) mChildContentView.findViewById(R.id.rl_shoukuanqushi);
        rlShouKuanQuShi.setOnClickListener(this);
        rlHuiYuanZhangDan = (RelativeLayout) mChildContentView.findViewById(R.id.rl_huiyuanzhangdan);
        rlHuiYuanZhangDan.setOnClickListener(this);

        rlJiFenZhangDan = (RelativeLayout) mChildContentView.findViewById(R.id.rl_jifenzhangdan);
        rlJiFenZhangDan.setOnClickListener(this);

        tv_shoukuanzhangdan = (TextView) mChildContentView.findViewById(R.id.tv_shoukuanzhangdan);
        tv_hexiaozhangdan = (TextView) mChildContentView.findViewById(R.id.tv_hexiaozhangdan);
        tv_tuikuanzhangdan = (TextView) mChildContentView.findViewById(R.id.tv_tuikuanzhangdan);
        tv_shoukuanqushi = (TextView) mChildContentView.findViewById(R.id.tv_shoukuanqushi);
        tv_huiyuanzhangdan = (TextView) mChildContentView.findViewById(R.id.tv_huiyuanzhangdan);
        tv_jifenzhangdan = (TextView) mChildContentView.findViewById(R.id.tv_jifenzhangdan);

        iv_shoukuanzhangdan = (ImageView) mChildContentView.findViewById(R.id.iv_shoukuanzhangdan);
        iv_hexiaozhangdan = (ImageView) mChildContentView.findViewById(R.id.iv_hexiaozhangdan);
        iv_tuikuanzhangdan = (ImageView) mChildContentView.findViewById(R.id.iv_tuikuanzhangdan);
        iv_shoukuanqushi = (ImageView) mChildContentView.findViewById(R.id.iv_shoukuanqushi);
        iv_huiyuanzhangdan = (ImageView) mChildContentView.findViewById(R.id.iv_huiyuanzhangdan);
        iv_jifenzhangdan = (ImageView) mChildContentView.findViewById(R.id.iv_jifenzhangdan);
    }

    /**
     * activity可见可交互的时候就开始发送消息，开启循环
     */
    @Override
    public void onResume() {
        super.onResume();
        getTodayCount();
        getContent();
        handler.sendEmptyMessageDelayed(msgWhat, 5000);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getTodayCount();
            Log.i("test", "切换到了stategragment");
        }
    }

    //获取统计轮播数据
    private void getTodayCount() {
        String randomString = ((MainActivity) getActivity()).getRandomString(8);
        String content = ((MainActivity) getActivity()).getContent(randomString);
        String miyaoKey = ((MainActivity) getActivity()).getMiyaoKey(randomString);
        String url = Constant.URL + "/api/FuBaAppIndex/TodayCount?content=" + content + "&key=" + miyaoKey + "&CashType=" + appContext.getCashType();
        Log.i("test", "url = " + url);
        final Request request = new Request.Builder().url(url).get().build();
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
                        totalYestCount = bannerBean.optString("TotalYestCount");
                        totalYestMoney = bannerBean.optString("TotalYestMoney");
                        totalMonthCount = bannerBean.optString("TotalMonthCount");
                        totalMonthMoney = bannerBean.optString("TotalMonthMoney");
                        lastmonthCount = bannerBean.optString("LastmonthCount");
                        lastmonthMoney = bannerBean.optString("LastmonthMoney");
                        initDesData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //接收左边系统jpush消息
    @Subscribe
    public void onMsgEvent(FirstEvent events) {
        String text = events.getMsg();
        String[] temp = null;
        temp = text.split("\\|");
        Log.i("test", "statmentferagments = text = " + text);
        Log.i("test", "statmentferagments = temp = " + temp);
        if (temp.length > 3) {
            int dayCount = Integer.parseInt(totalDayCount);
            dayCount++;
            totalDayCount = String.valueOf(dayCount);
            int monthCount = Integer.parseInt(totalMonthCount);
            monthCount++;
            totalMonthCount = String.valueOf(monthCount);

            BigDecimal b1 = new BigDecimal(totalDayMoney);
            BigDecimal b2 = new BigDecimal(temp[1]);
            BigDecimal b = b1.add(b2);
            totalDayMoney = String.valueOf(b);

            BigDecimal c1 = new BigDecimal(totalMonthMoney);
            BigDecimal c2 = new BigDecimal(temp[1]);
            BigDecimal c = c1.add(c2);
            totalMonthMoney = String.valueOf(c);

            Log.i("test", "totalMonthMoney = sta =" + totalMonthMoney);
            initDesData();
        }
    }

    private void initDesData() {
        des_arrays = new String[]{"今日收款", "昨日收款", "本月收款", "上月收款"};
        des_arrayss = new String[]{totalDayMoney, totalYestMoney, totalMonthMoney, lastmonthMoney};
        des_arraysss = new String[]{"（合计" + totalDayCount + "笔）", "（合计" + totalYestCount + "笔）", "（合计" + totalMonthCount + "笔）", "（合计" + lastmonthCount + "笔）"};
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fanhui:
                ((MainActivity) getActivity()).drawerlayout.openDrawer(((MainActivity) getActivity()).rl_drawerlayout);
                break;
            //收款账单
            case R.id.rl_shoukuanzhangdan:
                if (!TextUtils.isEmpty(url1)) {
                    intent = new Intent(mContext, BaseZhangDanWebActivity.class);
                    intent.putExtra("url", url1);
                    intent.putExtra("title", title1);
                    intent.putExtra("urlnext", urlnext11);
                    startActivity(intent);
                }
                break;
            //退款账单
            case R.id.rl_tuikuanzhangdan:
                if (!TextUtils.isEmpty(url2)) {
                    intent = new Intent(mContext, BaseZhangDanWebActivity.class);
                    intent.putExtra("url", url2);
                    intent.putExtra("title", title2);
//                    intent.putExtra("urlnext", urlnext12);
                    startActivity(intent);
                }
                break;
            //核销账单
            case R.id.rl_hexiaozhangdan:
                if (!TextUtils.isEmpty(url3)) {
                    intent = new Intent(mContext, BaseZhangDanWebActivity.class);

                    intent.putExtra("url", url3);
                    intent.putExtra("title", title3);
                    intent.putExtra("urlnext", urlnext13);
                    startActivity(intent);
                }
                break;
            //会员
            case R.id.rl_huiyuanzhangdan:

                if (!TextUtils.isEmpty(url21)) {
                    intent = new Intent(mContext, BaseZhangDanWebActivity.class);
                    intent.putExtra("url", url21);
                    intent.putExtra("title", title21);
                    intent.putExtra("urlnext", urlnext21);
                    intent.putExtra("style", style21);
                    startActivity(intent);
                }
                break;
            //积分账单
            case R.id.rl_jifenzhangdan:
                if (!TextUtils.isEmpty(url22)) {
                    intent = new Intent(mContext, BaseZhangDanWebActivity.class);

                    intent.putExtra("url", url22);
                    intent.putExtra("title", title22);
                    intent.putExtra("urlnext", urlnext22);
                    intent.putExtra("style", style22);
                    startActivity(intent);
                }
                break;
            //收款趋势
            case R.id.rl_shoukuanqushi:
                if (!TextUtils.isEmpty(url31)) {
                    intent = new Intent(mContext, ShouKuanQuShiWebActivity.class);
                    intent.putExtra("url", url31);
                    intent.putExtra("title", title31);
                    intent.putExtra("urlnext", urlnext31);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    //获取界面显示内容的接口
    private void getContent() {

        String randomString = ((MainActivity) getActivity()).getRandomString(8);
        String content = ((MainActivity) getActivity()).getContent(randomString);
        String miyaoKey = ((MainActivity) getActivity()).getMiyaoKey(randomString);
        final String url = Constant.URL + "/api/FuBaAppIndex/GetAccount?content=" + content + "&key=" + miyaoKey + "&CashType=" + appContext.getCashType();
        Log.i("test", "url = " + url);

        Request request = new Request.Builder().url(url).get().build();
        HttpUtils.getAsyn(getActivity(), request, new HttpUtils.CallBack() {

            private JSONArray jsonArray3;
            private JSONArray jsonArray2;
            private JSONArray jsonArray1;
            private String result;

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("ok", "失败=" + request);
            }

            @Override
            public void onResponse(String json) {
//{"Result":"0","Error":"成功","data":[[{"url":"CurrentMonthOrderList.aspx","title":"收款账单","imgsrc":"image/image/account_avater_receipt@2x.png","urlnext":"OrderSearch.aspx"},{"url":"CardConsumeOrderList.aspx","title":"核销账单","imgsrc":"image/image/kaquan.png","urlnext":"SearchCardTime.aspx"},{"url":"ReturnCurrentMonthOrderList.aspx","title":"退款账单","imgsrc":"image/image/account_avater_refound@2x.png","urlnext":"CurrentMonthOrderList.aspx"}],[{"url":"MMAccount.aspx","title":"会员账单","imgsrc":"image/image/hyzd.png","urlnext":"OrderSearch.aspx","Style":"1"},{"url":"MPAccount.aspx","title":"积分账单","imgsrc":"image/image/jfzd.png","urlnext":"OrderSearch.aspx","Style":"2"}][{"url":"MonthOrders.aspx","title":"收款趋势","imgsrc":"image/image/account_avater_chamberlain@2x.png","urlnext":"TypeMonthOrders.aspx"}]]}
                Log.e("test", "sta = test" + json);
                try {
                    JSONObject object = new JSONObject(json);
                    result = object.optString("Result");
                    if (TextUtils.equals(result, "0")) {
                        JSONArray data = object.optJSONArray("data");

                        if (data.length() > 0) {
                            jsonArray1 = data.optJSONArray(0);
                        }
                        if (data.length() > 1) {
                            jsonArray2 = data.optJSONArray(1);
                        }
                        if (data.length() > 2) {
                            jsonArray3 = data.optJSONArray(2);
                        }

                        if (!(jsonArray1 == null)) {
                            if (jsonArray1.length() > 0) {
                                url1 = jsonArray1.optJSONObject(0).optString("url");
                                title1 = jsonArray1.optJSONObject(0).optString("title");
                                imgsrc1 = jsonArray1.optJSONObject(0).optString("imgsrc");
                                urlnext11 = jsonArray1.optJSONObject(0).optString("urlnext");
                                type11 = jsonArray1.optJSONObject(0).optString("Type");
                                tv_shoukuanzhangdan.setText(title1);
                                Picasso.with(mContext).load(Constant.URL + "/" + imgsrc1).into(iv_shoukuanzhangdan);
                                rlShouKuan.setVisibility(View.VISIBLE);
                            }
                            if (jsonArray1.length() > 1) {
                                url2 = jsonArray1.optJSONObject(1).optString("url");
                                title2 = jsonArray1.optJSONObject(1).optString("title");
                                imgsrc2 = jsonArray1.optJSONObject(1).optString("imgsrc");
                                urlnext12 = jsonArray1.optJSONObject(1).optString("urlnext");
                                type12 = jsonArray1.optJSONObject(0).optString("Type");
                                tv_tuikuanzhangdan.setText(title2);
                                Picasso.with(mContext).load(Constant.URL + "/" + imgsrc2).into(iv_tuikuanzhangdan);
                                rlTuiKuan.setVisibility(View.VISIBLE);
                            }
                            if (jsonArray1.length() > 2) {
                                url3 = jsonArray1.optJSONObject(2).optString("url");
                                title3 = jsonArray1.optJSONObject(2).optString("title");
                                imgsrc3 = jsonArray1.optJSONObject(2).optString("imgsrc");
                                urlnext13 = jsonArray1.optJSONObject(2).optString("urlnext");
                                type13 = jsonArray1.optJSONObject(0).optString("Type");
                                tv_hexiaozhangdan.setText(title3);
                                Picasso.with(mContext).load(Constant.URL + "/" + imgsrc3).into(iv_hexiaozhangdan);
                                rlHeXiao.setVisibility(View.VISIBLE);
                            }
                        }
                        if (!(jsonArray2 == null)) {
                            if (jsonArray2.length() > 0) {
                                url21 = jsonArray2.optJSONObject(0).optString("url");
                                title21 = jsonArray2.optJSONObject(0).optString("title");
                                imgsrc21 = jsonArray2.optJSONObject(0).optString("imgsrc");
                                urlnext21 = jsonArray2.optJSONObject(0).optString("urlnext");
                                type21 = jsonArray2.optJSONObject(0).optString("Type");
                                style21 = jsonArray2.optJSONObject(0).optString("Style");
                                tv_huiyuanzhangdan.setText(title21);
                                Picasso.with(mContext).load(Constant.URL + "/" + imgsrc21).into(iv_huiyuanzhangdan);
                                rlHuiYuanZhangDan.setVisibility(View.VISIBLE);
                            }
                            if (jsonArray2.length() > 1) {
                                url22 = jsonArray2.optJSONObject(1).optString("url");
                                title22 = jsonArray2.optJSONObject(1).optString("title");
                                imgsrc22 = jsonArray2.optJSONObject(1).optString("imgsrc");
                                urlnext22 = jsonArray2.optJSONObject(1).optString("urlnext");
                                type22 = jsonArray2.optJSONObject(1).optString("Type");
                                style22 = jsonArray2.optJSONObject(1).optString("Style");
                                tv_jifenzhangdan.setText(title22);
                                Picasso.with(mContext).load(Constant.URL + "/" + imgsrc22).into(iv_jifenzhangdan);
                                rlJiFenZhangDan.setVisibility(View.VISIBLE);
                            }
                        }

                        if (!(jsonArray3 == null)) {
                            if (jsonArray3.length() > 0) {
                                url31 = jsonArray3.optJSONObject(0).optString("url");
                                title31 = jsonArray3.optJSONObject(0).optString("title");
                                imgsrc31 = jsonArray3.optJSONObject(0).optString("imgsrc");
                                urlnext31 = jsonArray3.optJSONObject(0).optString("urlnext");
                                type31 = jsonArray3.optJSONObject(0).optString("Type");
                                tv_shoukuanqushi.setText(title31);
                                Picasso.with(mContext).load(Constant.URL + "/" + imgsrc31).into(iv_shoukuanqushi);
                                rlShouKuanQuShi.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    // 初始化圆点

    private void initDots() {
        ll_dots.removeAllViews();
        tv_dots_list.clear();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
        params.setMargins(0, 0, 10, 0);
        for (int i = 0; i < 4; i++) {
            tv_dot = new TextView(mContext);
            tv_dot.setBackgroundResource(R.drawable.tv_dot_selectors);
            ll_dots.addView(tv_dot, params);
            tv_dots_list.add(tv_dot);
        }
    }

    //在viewpager的滚动监听事件中实现文字和数值的变化。
    public void initIndicator() {
        initDots();// 初始化圆点
        initViewPagerData();// 初始化viewpager中存放的数据
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tv_desc.setText(des_arrays[position % 4]);
                tv_ratio.setText(des_arrayss[position % 4]);
                tv_total.setText(des_arraysss[position % 4]);
                for (int i = 0; i < tv_dots_list.size(); i++) {
                    if (i == position % 4) {
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

    private void initViewPagerData() {
        imageList.clear();
        ImageView iva = new ImageView(appContext);
        iva.setBackgroundResource(R.drawable.zdtjbj);

        ImageView ivb = new ImageView(appContext);
        ivb.setBackgroundResource(R.drawable.zdtjbj);

        ImageView ivc = new ImageView(appContext);
        ivc.setBackgroundResource(R.drawable.zdtjbj);

        ImageView ivd = new ImageView(appContext);
        ivd.setBackgroundResource(R.drawable.zdtjbj);


        imageList.add(iva);
        imageList.add(ivb);
        imageList.add(ivc);
        imageList.add(ivd);
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
            container.addView(imageList.get(position % imageList.size()));
            return imageList.get(position % imageList.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }
}
