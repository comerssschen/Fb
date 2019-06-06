package cn.weipan.fb.bean;


import com.blankj.utilcode.util.TimeUtils;

public class ArgBase {
    private String app_uid = "WP20181112000002";
    private String method;
    private String format = "json";
    private String charset = "utf-8";
    private String sign = "123";
    private String timestamp = TimeUtils.getNowString();
    private String version = "1.0";
    private String biz_content;

    public String getApp_uid() {
        return app_uid;
    }

    public void setApp_uid(String app_uid) {
        this.app_uid = app_uid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBiz_content() {
        return biz_content;
    }

    public void setBiz_content(String biz_content) {
        this.biz_content = biz_content;
    }

    @Override
    public String toString() {
        return "ArgBase{" +
                "app_uid='" + app_uid + '\'' +
                ", method='" + method + '\'' +
                ", format='" + format + '\'' +
                ", charset='" + charset + '\'' +
                ", sign='" + sign + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", version='" + version + '\'' +
                ", biz_content='" + biz_content + '\'' +
                '}';
    }
}
