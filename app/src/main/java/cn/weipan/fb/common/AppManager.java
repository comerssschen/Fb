package cn.weipan.fb.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * @author cc
 * @version 1.0
 * @created 2016-7-26
 */
public class AppManager {
	
	private static Stack<Activity> activityStack;
	private static AppManager instance;
	
	private AppManager(){}
	/**
	 * 单一实例
	 */
	public static AppManager getAppManager(){
		if(instance==null){
			instance=new AppManager();
		}
		return instance;
	}
	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity){
		if(activityStack==null){
			activityStack=new Stack<Activity>();
		}
		activityStack.add(activity);
	}
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
			activity.finish();
			activity=null;
		}
	}
	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity(){
		for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
            	activityStack.get(i).finish();
            }
	    }
		activityStack.clear();
	}
	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {	}
	}
}