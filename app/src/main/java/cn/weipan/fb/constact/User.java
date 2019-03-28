package cn.weipan.fb.constact;

/**
 * Created by cc on 2016/9/2.
 * 单例对象
 */
public class User {
    private String username;
    private static User user;
    private User(){}

    public static User getInstance(){
        if(user==null){
            user=new User();
        }
        return user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
