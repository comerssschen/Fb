package cn.weipan.fb.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.weipan.fb.R;
import cn.weipan.fb.act.BoseWebActivity;
import cn.weipan.fb.act.MainActivity;
import cn.weipan.fb.common.Constant;
import cn.weipan.fb.utils.HttpUtils;
import cn.weipan.fb.utils.ToastUtils;

/**
 * 老板赚钱
 * Created by cc on 2016/7/27.
 */
public class BossFragment extends BaseFragment implements View.OnClickListener {
    private Intent intent;
    private LinearLayout shiShiFenXi;
    private LinearLayout shiJianFenXi;
    private LinearLayout ciShuFenXi;
    private LinearLayout jingeFenXi;
    private LinearLayout zhouQiFenXi;
    private LinearLayout fuGouLvFenXi;
    private RelativeLayout weiShangCheng;
    private RelativeLayout huoDongGuanLi;
    private RelativeLayout rl_faquan;
    private String url1;
    private String url2;
    private String url3;
    private String url4;
    private String url5;
    private String url6;
    private String url21;
    private String url22;
    private String url31;
    private String title1;
    private String title2;
    private String title3;
    private String title4;
    private String title5;
    private String title6;
    private String imgsrc1;
    private String imgsrc2;
    private String imgsrc3;
    private String imgsrc4;
    private String imgsrc5;
    private String imgsrc6;
    private String title21;
    private String title22;
    private String imgsrc21;
    private String imgsrc22;
    private String title31;
    private String imgsrc31;
    private TextView tv_shishifenxi;
    private TextView tv_shijianfenxi;
    private TextView tv_cishufenxi;
    private TextView tv_jinefenxi;
    private TextView tv_zhouqifenxi;
    private TextView tv_fugoulvfenxi;
    private TextView tv_weishangcheng;
    private TextView tv_huodongguanli;
    private TextView tv_faquan;
    private ImageView iv_shishifenxi;
    private ImageView iv_shijianfenxi;
    private ImageView iv_cishufenxi;
    private ImageView iv_jinefenxi;
    private ImageView iv_zhouqifenxi;
    private ImageView iv_fugoulvfenxi;
    private ImageView iv_cotentssss;
    private ImageView iv_cotentsss;
    private String cashType;


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cashType = appContext.getCashType();
        if (TextUtils.equals(cashType, "6")) {
            return inflater.inflate(R.layout.fragment_boss, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_null_boss, container, false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    //初始化
    @Override
    protected void initView(View mChildContentView, Bundle savedInstanceState) {
        TextView headerTitle = mChildContentView.findViewById(R.id.head_view_title);
        headerTitle.setText("老板赚钱");
        LinearLayout fanhui = mChildContentView.findViewById(R.id.ll_fanhui);
        fanhui.setOnClickListener(this);
        ImageView headerBack = mChildContentView.findViewById(R.id.but_header_back);
        headerBack.setImageResource(R.drawable.caidan);
        TextView tvHeaderBack = mChildContentView.findViewById(R.id.tv_header_back);
        tvHeaderBack.setVisibility(View.INVISIBLE);
        if (TextUtils.equals(cashType, "6")) {

            shiShiFenXi = mChildContentView.findViewById(R.id.ll_shishifenxi);
            shiShiFenXi.setOnClickListener(this);
            shiJianFenXi = mChildContentView.findViewById(R.id.ll_shijianfenxi);
            shiJianFenXi.setOnClickListener(this);
            ciShuFenXi = mChildContentView.findViewById(R.id.ll_cishufenxi);
            ciShuFenXi.setOnClickListener(this);
            jingeFenXi = mChildContentView.findViewById(R.id.ll_jinefenxi);
            jingeFenXi.setOnClickListener(this);
            zhouQiFenXi = mChildContentView.findViewById(R.id.ll_zhouqifenxi);
            zhouQiFenXi.setOnClickListener(this);
            fuGouLvFenXi = mChildContentView.findViewById(R.id.ll_fugoulvfenxi);
            fuGouLvFenXi.setOnClickListener(this);
            weiShangCheng = mChildContentView.findViewById(R.id.rl_weishangcheng);
            weiShangCheng.setOnClickListener(this);
            huoDongGuanLi = mChildContentView.findViewById(R.id.rl_huodongguanli);
            huoDongGuanLi.setOnClickListener(this);
            rl_faquan = mChildContentView.findViewById(R.id.rl_faquan);
            rl_faquan.setOnClickListener(this);

            tv_shishifenxi = mChildContentView.findViewById(R.id.tv_shishifenxi);
            tv_shijianfenxi = mChildContentView.findViewById(R.id.tv_shijianfenxi);
            tv_cishufenxi = mChildContentView.findViewById(R.id.tv_cishufenxi);
            tv_jinefenxi = mChildContentView.findViewById(R.id.tv_jinefenxi);
            tv_zhouqifenxi = mChildContentView.findViewById(R.id.tv_zhouqifenxi);
            tv_fugoulvfenxi = mChildContentView.findViewById(R.id.tv_fugoulvfenxi);
            tv_weishangcheng = mChildContentView.findViewById(R.id.tv_weishangcheng);
            tv_huodongguanli = mChildContentView.findViewById(R.id.tv_huodongguanli);
            tv_faquan = mChildContentView.findViewById(R.id.tv_faquan);

            iv_shishifenxi = mChildContentView.findViewById(R.id.iv_shishifenxi);
            iv_shijianfenxi = mChildContentView.findViewById(R.id.iv_shijianfenxi);
            iv_cishufenxi = mChildContentView.findViewById(R.id.iv_cishufenxi);
            iv_jinefenxi = mChildContentView.findViewById(R.id.iv_jinefenxi);
            iv_zhouqifenxi = mChildContentView.findViewById(R.id.iv_zhouqifenxi);
            iv_fugoulvfenxi = mChildContentView.findViewById(R.id.iv_fugoulvfenxi);
            iv_cotentssss = mChildContentView.findViewById(R.id.iv_cotentssss);
            iv_cotentsss = mChildContentView.findViewById(R.id.iv_cotentsss);
            getUserMassage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fanhui:
                ((MainActivity) getActivity()).drawerlayout.openDrawer(((MainActivity) getActivity()).rl_drawerlayout);
                break;
            //实时消费分析
            case R.id.ll_shishifenxi:
                if (!TextUtils.isEmpty(url1)) {
                    intent = new Intent(mContext, BoseWebActivity.class);
                    intent.putExtra("url", url1);
                    intent.putExtra("title", title1);
                    startActivity(intent);
                }
                break;
            //消费时间分析
            case R.id.ll_shijianfenxi:
                if (!TextUtils.isEmpty(url2)) {
                    intent = new Intent(mContext, BoseWebActivity.class);
                    intent.putExtra("url", url2);
                    intent.putExtra("title", title2);
                    startActivity(intent);
                }
                break;
            //消费次数分析
            case R.id.ll_cishufenxi:
                if (!TextUtils.isEmpty(url3)) {
                    intent = new Intent(mContext, BoseWebActivity.class);
                    intent.putExtra("url", url3);
                    intent.putExtra("title", title3);
                    startActivity(intent);
                }
                break;
            //消费金额分析
            case R.id.ll_jinefenxi:
                if (!TextUtils.isEmpty(url4)) {
                    intent = new Intent(mContext, BoseWebActivity.class);
                    intent.putExtra("url", url4);
                    intent.putExtra("title", title4);
                    startActivity(intent);
                }
                break;
            //消费周期分析
            case R.id.ll_zhouqifenxi:
                if (!TextUtils.isEmpty(url5)) {
                    intent = new Intent(mContext, BoseWebActivity.class);
                    intent.putExtra("url", url5);
                    intent.putExtra("title", title5);
                    startActivity(intent);
                }
                break;
            //复购率分析
            case R.id.ll_fugoulvfenxi:
                if (!TextUtils.isEmpty(url6)) {
                    intent = new Intent(mContext, BoseWebActivity.class);
                    intent.putExtra("url", url6);
                    intent.putExtra("title", title6);
                    startActivity(intent);
                }
                break;
            //微商城订单分析
            case R.id.rl_weishangcheng:
                if (!TextUtils.isEmpty(url21)) {
                    intent = new Intent(mContext, BoseWebActivity.class);
                    intent.putExtra("url", url21);
                    intent.putExtra("title", title21);
                    startActivity(intent);
                }
                break;
            //活动管理
            case R.id.rl_huodongguanli:
                if (!TextUtils.isEmpty(url22)) {
                    intent = new Intent(mContext, BoseWebActivity.class);
                    intent.putExtra("url", url22);
                    intent.putExtra("title", title22);
                    startActivity(intent);
                }
                break;
            //我要发券
            case R.id.rl_faquan:
                if (!TextUtils.isEmpty(url31)) {
                    intent = new Intent(mContext, BoseWebActivity.class);
                    intent.putExtra("url", url31);
                    intent.putExtra("title", title31);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    //获取显示界面的接口
    private void getUserMassage() {
        String randomString = ((MainActivity) getActivity()).getRandomString(8);
        String content = ((MainActivity) getActivity()).getContent(randomString);
        String miyaoKey = ((MainActivity) getActivity()).getMiyaoKey(randomString);

        final String url = Constant.URL + "/api/FuBaAppIndex/GetTool?content=" + content + "&key=" + miyaoKey;
        Log.i("test", "url = " + url);

        Request request = new Request.Builder().url(url).get().build();
        HttpUtils.getAsyn(getActivity(), request, new HttpUtils.CallBack() {

                    private String result;

                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.e("ok", "失败=" + request);
                    }

                    @Override
                    public void onResponse(String json) {
                        Log.e("test", json);

                        try {
                            JSONObject object = new JSONObject(json);
                            result = object.optString("Result");
                            if (TextUtils.equals(result, "0")) {
                                JSONArray data = object.optJSONArray("data");
                                JSONArray jsonArray1 = data.optJSONArray(0);
                                JSONArray jsonArray2 = data.optJSONArray(1);
                                JSONArray jsonArray3 = data.optJSONArray(2);
                                if (!(jsonArray1 == null)) {
                                    if (jsonArray1.length() > 0) {
                                        url1 = jsonArray1.optJSONObject(0).optString("url");
                                        title1 = jsonArray1.optJSONObject(0).optString("title");
                                        imgsrc1 = jsonArray1.optJSONObject(0).optString("imgsrc");
                                        tv_shishifenxi.setText(title1);
                                        Picasso.with(mContext).load(Constant.URL + "/" + imgsrc1).into(iv_shishifenxi);
                                    }
                                    if (jsonArray1.length() > 1) {
                                        url2 = jsonArray1.optJSONObject(1).optString("url");
                                        title2 = jsonArray1.optJSONObject(1).optString("title");
                                        imgsrc2 = jsonArray1.optJSONObject(1).optString("imgsrc");

                                        tv_shijianfenxi.setText(title2);
                                        Picasso.with(mContext).load(Constant.URL + "/" + imgsrc2).into(iv_shijianfenxi);

                                    }
                                    if (jsonArray1.length() > 2) {
                                        url3 = jsonArray1.optJSONObject(2).optString("url");
                                        title3 = jsonArray1.optJSONObject(2).optString("title");
                                        imgsrc3 = jsonArray1.optJSONObject(2).optString("imgsrc");
                                        tv_cishufenxi.setText(title3);
                                        Picasso.with(mContext).load(Constant.URL + "/" + imgsrc3).into(iv_cishufenxi);

                                    }
                                    if (jsonArray1.length() > 3) {
                                        url4 = jsonArray1.optJSONObject(3).optString("url");
                                        title4 = jsonArray1.optJSONObject(3).optString("title");
                                        imgsrc4 = jsonArray1.optJSONObject(3).optString("imgsrc");
                                        tv_jinefenxi.setText(title4);
                                        Picasso.with(mContext).load(Constant.URL + "/" + imgsrc4).into(iv_jinefenxi);
                                    }
                                    if (jsonArray1.length() > 4) {
                                        url5 = jsonArray1.optJSONObject(4).optString("url");
                                        title5 = jsonArray1.optJSONObject(4).optString("title");
                                        imgsrc5 = jsonArray1.optJSONObject(4).optString("imgsrc");
                                        tv_zhouqifenxi.setText(title5);
                                        Picasso.with(mContext).load(Constant.URL + "/" + imgsrc5).into(iv_zhouqifenxi);
                                    }
                                    if (jsonArray1.length() > 5) {
                                        url6 = jsonArray1.optJSONObject(5).optString("url");
                                        title6 = jsonArray1.optJSONObject(5).optString("title");
                                        imgsrc6 = jsonArray1.optJSONObject(5).optString("imgsrc");
                                        tv_fugoulvfenxi.setText(title6);
                                        Picasso.with(mContext).load(Constant.URL + "/" + imgsrc6).into(iv_fugoulvfenxi);
                                    }

                                }


                                if (!(jsonArray2 == null)) {
                                    if (jsonArray2.length() > 0) {
                                        url21 = jsonArray2.optJSONObject(0).optString("url");
                                        title21 = jsonArray2.optJSONObject(0).optString("title");
                                        imgsrc21 = jsonArray2.optJSONObject(0).optString("imgsrc");
                                        tv_weishangcheng.setText(title21);
                                        Picasso.with(mContext).load(Constant.URL + "/" + imgsrc21).into(iv_cotentssss);
                                    }
                                    if (jsonArray2.length() > 1) {
                                        url22 = jsonArray2.optJSONObject(1).optString("url");
                                        title22 = jsonArray2.optJSONObject(1).optString("title");
                                        imgsrc22 = jsonArray2.optJSONObject(1).optString("imgsrc");
                                        tv_huodongguanli.setText(title22);
                                        Picasso.with(mContext).load(Constant.URL + "/" + imgsrc22).into(iv_cotentsss);
                                    }
                                }
                                if (!(jsonArray3 == null)) {
                                    if (jsonArray3.length() > 0) {
                                        url31 = jsonArray3.optJSONObject(0).optString("url");
                                        title31 = jsonArray3.optJSONObject(0).optString("title");
                                        imgsrc31 = jsonArray3.optJSONObject(0).optString("imgsrc");
                                        tv_faquan.setText(title31);
                                    }
                                }

                                if (!TextUtils.isEmpty(url1)) {
                                    shiShiFenXi.setVisibility(View.VISIBLE);
                                }
                                if (!TextUtils.isEmpty(url2)) {
                                    shiJianFenXi.setVisibility(View.VISIBLE);
                                }
                                if (!TextUtils.isEmpty(url3)) {
                                    ciShuFenXi.setVisibility(View.VISIBLE);
                                }
                                if (!TextUtils.isEmpty(url4)) {
                                    jingeFenXi.setVisibility(View.VISIBLE);
                                }
                                if (!TextUtils.isEmpty(url5)) {
                                    zhouQiFenXi.setVisibility(View.VISIBLE);
                                }
                                if (!TextUtils.isEmpty(url6)) {
                                    fuGouLvFenXi.setVisibility(View.VISIBLE);
                                }
                                if (!TextUtils.isEmpty(url21)) {
                                    weiShangCheng.setVisibility(View.VISIBLE);
                                }
                                if (!TextUtils.isEmpty(url22)) {
                                    huoDongGuanLi.setVisibility(View.VISIBLE);
                                }
                                if (!TextUtils.isEmpty(url31)) {
                                    rl_faquan.setVisibility(View.VISIBLE);
                                }
                            } else {
                                ToastUtils.showToast(getActivity(), object.optString("Error"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

        );
    }
}