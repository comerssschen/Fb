package cn.weipan.fb.common;

/**
 * Created by cc on 2016/8/8.
 * eventbus传递消息
 */
public class FirstEvent {
    private String mMsg;
    public FirstEvent(String msg) {
        mMsg = msg;
    }
    public String getMsg(){
        return mMsg;
    }
}

