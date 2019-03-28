package cn.weipan.fb.bean;

/**
 * Created by cc on 2016/11/1.
 * 统计轮播对象
 */
public class BannerBean {


    /**
     * LastmonthCount : 0
     * TotalYestCount : 0
     * LastmonthMoney : 0.00
     * TotalDayMoney : 0.00
     * TotalMonthMoney : 0.00
     * TotalDayCount : 0
     * TotalMonthCount : 0
     * TotalYestMoney : 0.00
     */

    private String LastmonthCount;
    private String TotalYestCount;
    private String LastmonthMoney;
    private String TotalDayMoney;
    private String TotalMonthMoney;
    private String TotalDayCount;
    private String TotalMonthCount;
    private String TotalYestMoney;

    public BannerBean() {
        super();
    }

    public BannerBean(String lastmonthCount, String totalYestCount, String totalDayMoney, String totalMonthMoney, String totalDayCount, String totalMonthCount, String totalYestMoney, String lastmonthMoney) {
        LastmonthCount = lastmonthCount;
        TotalYestCount = totalYestCount;
        TotalDayMoney = totalDayMoney;
        TotalMonthMoney = totalMonthMoney;
        TotalDayCount = totalDayCount;
        TotalMonthCount = totalMonthCount;
        TotalYestMoney = totalYestMoney;
        LastmonthMoney = lastmonthMoney;
    }

    public String getLastmonthCount() {
        return LastmonthCount;
    }

    public void setLastmonthCount(String LastmonthCount) {
        this.LastmonthCount = LastmonthCount;
    }

    public String getTotalYestCount() {
        return TotalYestCount;
    }

    public void setTotalYestCount(String TotalYestCount) {
        this.TotalYestCount = TotalYestCount;
    }

    public String getLastmonthMoney() {
        return LastmonthMoney;
    }

    public void setLastmonthMoney(String LastmonthMoney) {
        this.LastmonthMoney = LastmonthMoney;
    }

    public String getTotalDayMoney() {
        return TotalDayMoney;
    }

    public void setTotalDayMoney(String TotalDayMoney) {
        this.TotalDayMoney = TotalDayMoney;
    }

    public String getTotalMonthMoney() {
        return TotalMonthMoney;
    }

    public void setTotalMonthMoney(String TotalMonthMoney) {
        this.TotalMonthMoney = TotalMonthMoney;
    }

    public String getTotalDayCount() {
        return TotalDayCount;
    }

    public void setTotalDayCount(String TotalDayCount) {
        this.TotalDayCount = TotalDayCount;
    }

    public String getTotalMonthCount() {
        return TotalMonthCount;
    }

    public void setTotalMonthCount(String TotalMonthCount) {
        this.TotalMonthCount = TotalMonthCount;
    }

    public String getTotalYestMoney() {
        return TotalYestMoney;
    }

    public void setTotalYestMoney(String TotalYestMoney) {
        this.TotalYestMoney = TotalYestMoney;
    }
}
