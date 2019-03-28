package cn.weipan.fb.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.weipan.fb.R;
import cn.weipan.fb.common.AppContext;


/**
 * A simple {@link Fragment} subclass.
 * <p/>
 * 公共的属性： 拿上下文，  加载布局, 是否显示标题, 加载布局由父类， 提供布局的xml资源由子类
 * 不一样：制定规则
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    private View mViewRoot;
    public AppContext appContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mViewRoot == null) {

            mViewRoot = inflater.inflate(R.layout.fragment_base, container, false);
            appContext = (AppContext) getActivity().getApplication();
            FrameLayout mViewContainer = (FrameLayout) mViewRoot.findViewById(R.id.rl_container);
            //加载getContentLayoutRes()从子类获取的资源布局同时添加到mViewContainer里面
            View childView = createView(inflater, container, savedInstanceState);
            mViewContainer.addView(childView);
            //加载完成通知子类
            initView(mViewRoot, savedInstanceState);
        }

        return mViewRoot;
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void initView(View mChildContentView, Bundle savedInstanceState);

}
