package cn.weipan.fb.common;

/**
 * eventbus传递消息
 * Created by cc on 2016/8/30.
 */
public class ThreadEvent {
    private String mMsg;

    public ThreadEvent(String msg) {
        mMsg = msg;
    }

    public String getMsg() {
        return mMsg;
    }
}
