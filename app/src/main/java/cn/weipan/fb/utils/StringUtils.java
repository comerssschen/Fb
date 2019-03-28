package cn.weipan.fb.utils;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;

/**
 *
 *字符串工具类
 */

public class StringUtils {
	
	/**
	 * 字符串转整数
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try{
			return Integer.parseInt(str);
		}catch(Exception e){}
		return defValue;
	}

	/**
	 * 对象转整数
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if(obj==null) return 0;
		return toInt(obj.toString(),0);
	}
	
	/**
	 * 判断给定字符串是否空白串。
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串
	 * 若输入字符串为null或空字符串，返回true
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty( String input )
	{
		if ( input == null || "".equals( input ) )
			return true;
		
		for ( int i = 0; i < input.length(); i++ ) 
		{
			char c = input.charAt( i );
			if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
			{
				return false;
			}
		}
		return true;
	}
	
	public static String clearString(String content){
		if(content.indexOf("<img")!=-1){
			 int i = content.indexOf("<img");
			 int j = content.indexOf("/>");
			 content =  content.substring(0,  i) +content.substring(j+2,  content.length());
		}
		
		if(content.indexOf("<div")!=-1){
			 int i = content.indexOf("<div");
			 int j = content.indexOf("</div>");
			 content =  content.substring(0,  i) +content.substring(j+6,  content.length());
		}
		
		return content;
	}
		
	public static Long getlongDate() {
		long str= System.currentTimeMillis()/1000;
		return str;
	}
	
	public static String getDate() {
		SimpleDateFormat sDateFormat   =   new SimpleDateFormat("hh:mm");
		String date   =   sDateFormat.format(new   java.util.Date());
		return date;
	}
	
	/**
	 * URL编码转string字符串
	 * @param
	 */
	public static String URLDecoder(String code){
		if(code == null || code.equals("")){
			return null;
		}
		String urlStr = null;
		 try {
			urlStr = URLDecoder.decode(code, "gb2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return urlStr; 
	}
	
	public static String TimeProcess(String time){
		if(time == null || time.equals("")){
			return null;
		}
		String[] co= time.split(" ");
		return co[0];
	}
	


	
	/**
	 * 时间处理
	 * @param 
	 */
	
	public static String getTime(String timeStr){
		if (StringUtils.isEmpty(timeStr)) {
			return "";
		}
		timeStr = timeStr.replace("T", " ");
		int point = timeStr.lastIndexOf(':');
		if (point != -1) {
			return timeStr.substring(0, point);
		} else {
			return timeStr;
		}
	}
	
	public static String TimeProcessTODAY(String timeStr){
		if (StringUtils.isEmpty(timeStr)) {
			return "";
		}
		return timeStr.substring(0, 10);
	}
	
	public static String TimeProcessToMouthAndDay(String timeStr){
		if (StringUtils.isEmpty(timeStr)) {
			return "";
		}
		return timeStr.substring(5, 10);
	}
	
	public static String TimeProcessToTime(String timeStr){
		if (StringUtils.isEmpty(timeStr)) {
			return "";
		}
		return timeStr.substring(10,16);
	}
	
	public static String TimeProcessToYear(String timeStr){
		if (StringUtils.isEmpty(timeStr)) {
			return "";
		}
		return timeStr.substring(0,4);
	}
	
	public static String TimeProcessToMouth(String timeStr){
		if (StringUtils.isEmpty(timeStr)) {
			return "";
		}
		return timeStr.substring(5,7);
	}
	
	public static String TimeProcessToDay(String timeStr){
		if (StringUtils.isEmpty(timeStr)) {
			return "";
		}
		return timeStr.substring(8,10);
	}
	
	
	public static String CourseTime(String timeStr1, String timeStr2){
		if (StringUtils.isEmpty(timeStr1)||StringUtils.isEmpty(timeStr2)) {
			return "";
		}
		
		String data = timeStr1.substring(5, 10).replace("-", "月")+"日";
		String time = timeStr1.substring(11, 16) + "-" + timeStr2.substring(11, 16);
		
		return data + " " + time ;
	}

	public static String CourseDetailDay(String timeStr1){
		if (StringUtils.isEmpty(timeStr1)) {
			return "";
		}
		
		String data = timeStr1.substring(5, 10).replace("-", "月")+"日";
		return data  ;
	}
	
	public static String CourseDetailTime(String timeStr1, String timeStr2){
		if (StringUtils.isEmpty(timeStr1)||StringUtils.isEmpty(timeStr2)) {
			return "";
		}
		
		String data = timeStr1.substring(0, 10);
		String time = timeStr1.substring(11, 16) + "-" + timeStr2.substring(11, 16);
		
		return data + " " + time ;
	}
	
	public static String MyIncomeDay(String timeStr1){
		if (StringUtils.isEmpty(timeStr1)) {
			return "";
		}
		
		String data = timeStr1.substring(0, 7).replace("-", "年")+"月";
		return data  ;
	}
	
	public static String TimeNoPay(String timeStr){
		if (StringUtils.isEmpty(timeStr)) {
			return "";
		}
		return timeStr.substring(0,16);
	}
	
	public static String YwuOrderTime(String timeStr1, String timeStr2){
		if (StringUtils.isEmpty(timeStr1)||StringUtils.isEmpty(timeStr2)) {
			return "";
		}
		
		String data = timeStr1.substring(0, 10);
		String time = timeStr1.substring(11, 19) ;
		
		return data + " " + time ;
	}
	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1]\\d{10}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//		String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}
	
	/**
	 * 
	 * 清楚掉网页端 代码
	 * 
	 * @param str
	 * @return
	 */
	public static Spanned replaceHTML(String str){
		return  Html.fromHtml(str);
	}
}
