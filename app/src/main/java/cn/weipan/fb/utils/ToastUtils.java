package cn.weipan.fb.utils;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

/**
 *
 *文字简单提示工具
 */

public class ToastUtils {
    public static void showToast(Activity activity, String str) {
//		Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();

        Toast toast = Toast.makeText(activity, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
