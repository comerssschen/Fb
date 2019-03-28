package cn.weipan.fb.bean;

/**
 * Created by cc on 2016/10/26.
 * 激光消息保存为对象
 */
public class AllMessagBean {
    private String Title;
    private String Content;
    private String Danhao;
    private String Maoney;

    public AllMessagBean() {
        super();
    }

    public AllMessagBean(String title, String content, String danhao, String maoney) {
        Title = title;
        Content = content;
        Danhao = danhao;
        Maoney = maoney;
    }

    public String getMaoney() {
        return Maoney;
    }

    public void setMaoney(String maoney) {
        this.Maoney = maoney;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDanhao() {
        return Danhao;
    }

    public void setDanhao(String danhao) {
        Danhao = danhao;
    }

}
