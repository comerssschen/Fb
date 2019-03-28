package cn.weipan.fb.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.weipan.fb.R;
import cn.weipan.fb.act.JpushMessageActivity;
import cn.weipan.fb.act.MainActivity;
import cn.weipan.fb.act.SystemMessageActivity;
import cn.weipan.fb.bean.AllMessagBean;
import cn.weipan.fb.button.SegmentButton;
import cn.weipan.fb.common.SecondEvent;
import cn.weipan.fb.common.ThreadEvent;
import cn.weipan.fb.constact.Constant;
import cn.weipan.fb.listener.OnSegmentChangeListener;
import cn.weipan.fb.utils.HttpUtils;

/*
* 接收消息的fragment
* Created by cc on 2016/7/27.
* */
public class MessageFragment extends BaseFragment implements View.OnClickListener {
    private TextView tvLeft;
    private TextView tvRight;
    private List<AllMessagBean> allMessage;
    private List<AllMessagBean> SystemMessage = new ArrayList<>();
    private Boolean mHideorShow;
    private RecyclerView recyclerview;
    private MyAdapter myAdapter;
    private boolean isLeft = true;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EventBus aDefault;
    private int PageIndex = 1;
    private int PageSize = 20;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册接受消息的eventbus
        aDefault = EventBus.getDefault();
        aDefault.register(MessageFragment.this);
    }

    //初始化界面
    protected void initView(final View mChildContentView, Bundle savedInstanceState) {
        TextView headerTitle = (TextView) mChildContentView.findViewById(R.id.head_view_title);
        headerTitle.setText("消息");

        LinearLayout fanhui = (LinearLayout) mChildContentView.findViewById(R.id.ll_fanhui);
        fanhui.setOnClickListener(this);
        ImageView headerBack = (ImageView) mChildContentView.findViewById(R.id.but_header_back);
        headerBack.setImageResource(R.drawable.caidan);
        TextView tvHeaderBack = (TextView) mChildContentView.findViewById(R.id.tv_header_back);
        tvHeaderBack.setVisibility(View.INVISIBLE);

        SegmentButton sbLock = (SegmentButton) mChildContentView.findViewById(R.id.sb_lock);
        tvLeft = (TextView) sbLock.findViewById(R.id.tv_left);
        tvRight = (TextView) sbLock.findViewById(R.id.tv_right);
        tvLeft.setText("系统消息");
        tvRight.setText("活动消息");
        getSysMassage();

        swipeRefreshLayout = (SwipeRefreshLayout) mChildContentView.findViewById(R.id.swiprefreshlayout);
        //设置下拉圆圈：是否缩放，出现的位置，结束的位置(进度条在Y轴的展示范围)
        swipeRefreshLayout.setProgressViewOffset(true, 10, 30);
        //设置圆圈的背景色
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(android.R.color.white));
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);

        //设置下拉圆圈的颜色变化,默认白底黑圈进度条，不是setColorSchemeColors()
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerview = (RecyclerView) mChildContentView.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        //默认显示系统界面

        this.allMessage = Constant.jpushMessage;
        myAdapter = new MyAdapter();
        myAdapter.notifyDataSetChanged();
        recyclerview.setAdapter(myAdapter);
        sbLock.setOnSegmentChangeListener(new OnSegmentChangeListener() {
            @Override
            public void onChange(boolean isLeftSelected) {
                if (isLeftSelected) {
                    //点了左边
                    isLeft = true;
                    initData();
                } else {
                    //点了右边
                    isLeft = false;
                    initMessage();
                }
            }
        });
        //刷新过程2秒
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isLeft) {
                            allMessage = Constant.jpushMessage;
                            myAdapter.notifyDataSetChanged();
                        }
                        if (!isLeft) {

                            allMessage = SystemMessage;
                            myAdapter.notifyDataSetChanged();
                        }
                        recyclerview.scrollToPosition(0);//回到顶部
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "数据已刷新", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }
        });
    }


    //接收左边系统消息
    @Subscribe
    public void onMsgEvent(ThreadEvent event) {
        Log.i("test", "MessageFragment ThreadEvent收到了消息");
        if (isLeft) {
            allMessage = Constant.jpushMessage;
            myAdapter.notifyDataSetChanged();
        }
        if (!isLeft) {
            allMessage = SystemMessage;
            myAdapter.notifyDataSetChanged();
        }
    }


    //初始化系统
    protected void initData() {
        mHideorShow = false;
        aDefault.post(new SecondEvent(mHideorShow));
        allMessage = Constant.jpushMessage;
        myAdapter.notifyDataSetChanged();
    }

    //初始化消息
    private void initMessage() {
        mHideorShow = false;
        aDefault.post(new SecondEvent(mHideorShow));
        allMessage = SystemMessage;
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fanhui:
                ((MainActivity) getActivity()).drawerlayout.openDrawer(((MainActivity) getActivity()).rl_drawerlayout);
                break;
            default:
                break;
        }
    }

    //获取活动消息列表
    private void getSysMassage() {
        String randomString = ((MainActivity) getActivity()).getRandomString(8);
        String content = ((MainActivity) getActivity()).getContent(randomString);
        String miyaoKey = ((MainActivity) getActivity()).getMiyaoKey(randomString);

        String url = Constant.URL + "/api/Notices/GetNotices?content=" + content + "&key=" + miyaoKey + "&PageIndex=" + PageIndex + "&PageSize=" + PageSize;

        Log.i("test", "messgehuodong url= " + url);
        Request request = new Request.Builder().url(url).get().build();
        HttpUtils.getAsyn(getActivity(), request, new HttpUtils.CallBack() {
            private String success;

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("test", "失败" + request);
            }

            @Override
            public void onResponse(String json) {//{"Result":"0","Error":"成功","TatalPage":1,"data":[{"Url":"NoticeDetail.aspx?code=1","Title":"adfd","AddTime":"2016/11/1 18:57:07"}]}
                Log.i("test", "messgehuodong = " + json);
                try {
                    JSONObject object = new JSONObject(json);
                    success = object.optString("Result");
                    if (TextUtils.equals(success, "0")) {
                        JSONArray array = object.optJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            AllMessagBean allMessagBean = new AllMessagBean();
                            allMessagBean.setTitle(array.getJSONObject(i).optString("Title"));
                            allMessagBean.setContent(array.getJSONObject(i).optString("AddTime"));
                            allMessagBean.setDanhao(array.getJSONObject(i).optString("Url"));
                            allMessagBean.setMaoney("-1");
                            SystemMessage.add(allMessagBean);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        public MyAdapter() {
        }

        @Override
        public int getItemCount() {
            return allMessage.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(View.inflate(getActivity(), R.layout.layout_parent, null));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final AllMessagBean message = allMessage.get(position);
            holder.tvTitle.setText(message.getTitle());
            holder.tvTime.setText(message.getContent());

            if (!TextUtils.equals(message.getMaoney(), "-1")) {
                holder.tv_money.setText("+" + message.getMaoney());
            } else {
                holder.tv_money.setText("");
            }
            holder.rlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHideorShow = false;
                    aDefault.post(new SecondEvent(mHideorShow));

                    if (isLeft) {
                        Intent intent = new Intent(getActivity(), JpushMessageActivity.class);
                        intent.putExtra("danhao", message.getDanhao());
                        intent.putExtra("payType", message.getTitle());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), SystemMessageActivity.class);
                        intent.putExtra("url", message.getDanhao());
                        startActivity(intent);
                    }
                }
            });
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvTime;
        ImageView ivImage;
        RelativeLayout rlItem;
        TextView tv_money;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.parent_textview);
            tvTime = (TextView) itemView.findViewById(R.id.textview);
            tv_money = (TextView) itemView.findViewById(R.id.tv_money);
            ivImage = (ImageView) itemView.findViewById(R.id.arrow);
            rlItem = (RelativeLayout) itemView.findViewById(R.id.rl_item);
            itemView.setTag(this);
        }
    }
}
