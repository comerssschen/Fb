package cn.weipan.fb.bean;

/**
 * Created by cc on 2016/8/10.
 * 消息对象
 */
public class Message {

    private String title;
    private String content;
    private Integer type;
    private Integer is_read;
    private String time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public Message() {
        super();
    }

    public Message(String title, String time) {
        this.title = title;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", is_read=" + is_read +
                ", time='" + time + '\'' +
                '}';
    }
}
