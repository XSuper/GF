package com.ab.view;


import net.duohuo.dhroid.R;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast {
	// toast信息常量
	/*	public static final String TOAST_MSG_SUCCESS_TITLE = "成功";
		public static final String TOAST_MSG_ERROR_TITLE = "失败";
		public static final String TOAST_MSG_WARNING_TITLE = "提示";

		public static final String TOAST_MSG_LOAD_SUCCESS_CONTENT = "信息更新成功";
		public static final String TOAST_MSG_LOAD_ERROR_CONTENT = "信息更新失败";

		public static final String TOAST_MSG_LOADMORE_SUCCESS_CONTENT = "信息加载成功";
		public static final String TOAST_MSG_LOADMORE_ERROR_CONTENT = "信息加载失败";

		public static final String TOAST_MSG_REFRESH_SUCCESS_CONTENT = "刷新成功";
		public static final String TOAST_MSG_REFRESH_ERROR_CONTENT = "刷新失败";

		public static final String TOAST_MSG_NOMORE_CONTENT = "没有更多的内容！";*/
		
	static Toast toast;

	/**
	 * 居中的Toast
	 * 
	 * @param context
	 * @param content
	 * @param duration
	 */
	public static void centerToast(Context context, String content) {
//		toast = Toast.makeText(context, content, duration);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.show();
		customToast(context, Toast.LENGTH_SHORT , content);
	}

	/**
	 * 自定义样式的Toast
	 * @param context
	 * @param duration
	 * @param title
	 * @param content
	 * @param img
	 */
	public static void customToast(Context context, int duration,String content) {
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		View view = inflater.inflate(R.layout.userdefinedtoast,
				null);
		//view.setBackgroundResource(R.drawable.bg);
		TextView txtView_Content = (TextView) view
				.findViewById(R.id.txt_content);
		txtView_Content.setText(content);
		toast = new Toast(context);
		toast.setGravity(Gravity.BOTTOM, 0, 100);
		toast.setDuration(duration);
		toast.setView(view);
		toast.show();
	}
	
	/**
	 * 錯誤的提示
	 * @param context
	 * @param content
	 */
	public static void ErrorToast(Context context,String content){
		customToast(context, Toast.LENGTH_SHORT , content);
	}
	/**
	 * 成功的提�?
	 * @param context
	 * @param content
	 */
	public static void SuccessToast(Context context,String content){
		customToast(context, Toast.LENGTH_SHORT , content);
	}
	/**
	 * 警告的提�?
	 * @param context
	 * @param content
	 */
	public static void WarningToast(Context context,String content){
		customToast(context, Toast.LENGTH_SHORT , content);
	}

}
