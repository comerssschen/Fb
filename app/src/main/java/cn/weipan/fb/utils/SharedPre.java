package cn.weipan.fb.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/*
* 首选项保存数据
* */
public class SharedPre {

	private Context context;
	private SharedPreferences spf;

	public SharedPre(Context context) {
		this.context = context;
		spf = context.getSharedPreferences("shared", Context.MODE_PRIVATE);
	}

	/**
	 * 记住账号
	 * 
	 * @param username
	 *            要保存的用户
	 */
	public void RememberingUsername(String username) {
		Editor et = spf.edit();
		et.putString("username", username);
		et.commit();
	}

	/**
	 * 记住密码
	 * 
	 *
	 *            要保存的密码
	 */
	public void RememberingPassword(String password) {
		Editor et = spf.edit();
		et.putString("password", password);
		et.commit();
	}

	/**
	 * 得到用户?
	 * 
	 * @return
	 */
	public String getUsername() {
		return spf.getString("username", null);
	}

	/**
	 * 得到密码
	 * 
	 * @return
	 */
	public String getPassword() {
		return spf.getString("password", null);
	}

	/**
	 * 记住账户checked
	 */
	public void userNameisChecked(boolean flag) {
		Editor et = spf.edit();
		et.putBoolean("uflag", flag);
		et.commit();
	}

	/**
	 * 记住密码checked
	 */
	public void passWordisChecked(boolean flag) {
		Editor et = spf.edit();
		et.putBoolean("pflag", flag);
		et.commit();
	}

	/**
	 * 得到记住账户状?
	 * 
	 * @return true记住 false未记?
	 */
	public boolean getUisChecked() {
		return spf.getBoolean("uflag", false);
	}

	/**
	 * 得到记住密码状?
	 * 
	 * @return true记住 false未记?
	 */
	public boolean getPisChecked() {
		return spf.getBoolean("pflag", false);
	}

}
