package cn.weipan.fb.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/*
* 对话框工具类
* */
public class DialogUtils {
	
	public static void showDialogOnly(Activity activity, String title, String str, final DialogCallback dialogCallback) {
		AlertDialog.Builder builder = new Builder(activity);
		builder.setMessage(str);
		builder.setTitle(title);
		builder.setPositiveButton("确认", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				dialogCallback.PositiveButton(which);
			}
		});
		builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH;
			}
		});
		builder.create().show();
	}

	public static void showProcessDialog(Activity activity, String[] str, final DialogCallback dialogCallback) {
		AlertDialog.Builder builder = new Builder(activity);
		builder.setItems(str, new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				dialogCallback.PositiveButton(which);
			}
		}).setNegativeButton("取消", null);
		builder.create().show();
	}


	public static void showDialog(Activity activity, String str, final DialogCallback dialogCallback) {
		AlertDialog.Builder builder = new Builder(activity);
		builder.setMessage(str);
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				dialogCallback.PositiveButton(which);
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public static void uploadDialog(Activity activity, String str, final DialogCallback dialogCallback) {
		AlertDialog.Builder builder = new Builder(activity);
		builder.setMessage(str);
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				dialogCallback.PositiveButton(which);
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				dialogCallback.PositiveButton(which);
			}
		});
		builder.create().show();
	}

	public static void customDialog(Activity activity, String title, String PositiveTitle, String NegativeTitle, String str, final DialogCallback dialogCallback, boolean istitle, boolean isCanCanel) {
		AlertDialog.Builder builder = new Builder(activity);
		builder.setMessage(str);
		builder.setCancelable(isCanCanel);
		if(istitle)builder.setTitle(title);
		builder.setPositiveButton(PositiveTitle, new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				dialogCallback.PositiveButton(which);
			}
		});

		builder.setNegativeButton(NegativeTitle, new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				dialogCallback.PositiveButton(which);
			}
		});
		builder.create().show();
	}

	public interface DialogCallback {
        void PositiveButton(int i);
    }
	
	// 点击非输入框组件时 输入框组件失去焦点
	public static boolean checkKeyboardShowing(Context activity, View view,
											   EditText editView) {
		view.requestFocus();
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean active = imm.isActive(editView);
		imm.hideSoftInputFromWindow(editView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		editView.clearFocus();
		return active;
	}
}
