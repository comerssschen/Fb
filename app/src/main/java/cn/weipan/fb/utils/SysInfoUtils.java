package cn.weipan.fb.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class SysInfoUtils {
	
	public static String getAppVersion(Context context) {
		String versionName = "";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}
	
	/**
     * 用来判断服务是否后台运行
     * @param
     * @param
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext) {
    	String className = mContext.getPackageName();
        Boolean IsRunning = false;
        
        KeyguardManager keyguardManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
        
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> serviceList   = activityManager.getRunningAppProcesses();
       if (!(serviceList.size()>0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).processName.equals(className)) {
            	boolean isBackground = serviceList.get(i).importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND && serviceList.get(i).importance != RunningAppProcessInfo.IMPORTANCE_VISIBLE;
            	boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
				return isBackground || isLockedState;
            }
        }
        return IsRunning ;
    }
}
