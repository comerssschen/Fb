package cn.weipan.fb.common;

/**
 * eventbus传递消息
 * Created by cc on 2016/8/15.
 */
public class SecondEvent {
    private Boolean mHideorShow;
    public SecondEvent(Boolean hideorShow) {
        mHideorShow = hideorShow;
    }
    public Boolean getBoolean(){
        return mHideorShow;
    }


}

