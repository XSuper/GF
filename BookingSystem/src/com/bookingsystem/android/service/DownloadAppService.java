package com.bookingsystem.android.service;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.duohuo.dhroid.net.download.DownLoadManager;
import net.duohuo.dhroid.net.download.DownLoadManager.DownLoadCallBack;
import net.duohuo.dhroid.net.download.DownloadTask;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.ab.util.AbAppUtil;
import com.bookingsystem.android.R;
import com.bookingsystem.android.ui.MoreAppActivity;

public class DownloadAppService extends Service {

	public HashMap<String, DownLoadManager> downloadMap;// 保存下载器
	public HashMap<String, Integer> idmap;// 保存下载器

	public NotificationManager notificationManager;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		downloadMap = new HashMap<String, DownLoadManager>();
		notificationManager = (NotificationManager) this
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		idmap = new HashMap<String, Integer>();
		
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		// String name = intent.getStringExtra("name");

		
		return new Mbinder();
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
		clearNotification();
		Log.v("onrebind","-------" );
	}
	@Override
	public boolean onUnbind(Intent intent) {
		showNotification();
		return true;
	}


	
	private void showNotification() {
		Iterator<Map.Entry<String, DownLoadManager>> iter = downloadMap
				.entrySet().iterator();
		int id = 1;
		while (iter.hasNext()) {
			Map.Entry<String, DownLoadManager> entry = iter.next();
			final String name = entry.getKey();
			final DownLoadManager d = entry.getValue();
			final Notification notification = new Notification(
					R.drawable.desk_icon, "高盛通", System.currentTimeMillis());
			// 定义Notification的各种属性
			notification.contentView = new RemoteViews(getPackageName(),
					R.layout.notification_down);
			notification.contentView.setProgressBar(R.id.n_progress, 100, 0,
					false);
			notification.contentView.setTextViewText(R.id.n_name, name);
			//notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
			//notification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
			Intent notificationIntent = new Intent(DownloadAppService.this,
					MoreAppActivity.class); // 点击该通知后要跳转的Activity
			PendingIntent contentItent = PendingIntent.getActivity(this, 0,
					notificationIntent, 0);
			notification.contentIntent = contentItent;
			notificationManager.notify(id, notification);
			idmap.put(name, id++);
			d.regeisterCallBack(name, new DownLoadCallBack() {

				int mid = idmap.get(name);
				@Override
				public void onStop(DownloadTask task) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPersent(DownloadTask task, float persent) {
					// TODO Auto-generated method stub
					if((int)(persent * 100)%5==0){
						
						notification.contentView.setProgressBar(R.id.n_progress,
								100, (int) (persent * 100), false);
						notificationManager.notify(mid, notification);
					}
				}

				@Override
				public void onEnd(DownloadTask task) {
					// TODO Auto-generated method stub
					notificationManager.cancel(mid);
					idmap.remove(name);
					d.unregeisterCallBack(name);
					downloadMap.remove(name);
					String file = Environment.getExternalStorageDirectory()
							+ "/gst/app/" + name + ".apk";
					AbAppUtil.installApk(getApplicationContext(),
							new File(file));
				}
			});
		}

	}

	// 删除通知
	private void clearNotification() {
		try {
//			Iterator<Map.Entry<String, Integer>> iter = idmap
//					.entrySet().iterator();
//			while (iter.hasNext()) {
//				Map.Entry<String, Integer> entry = iter.next();
//				final int d = entry.getValue();
//			notificationManager.cancel(d);
//			}
			notificationManager.cancelAll();
		} catch (Exception e) {
			// 处理没有提示的时候的nullPointerException
			e.printStackTrace();
		}
	}

	public class Mbinder extends Binder {

		public DownloadAppService getService() {
			return DownloadAppService.this;
		}

		public DownLoadManager getDownLoad(String name) {
			if (downloadMap.containsKey(name)) {
				return downloadMap.get(name);
			} else {
				return null;
			}
		}

		public HashMap<String, DownLoadManager> getDownLoadMap() {
			return downloadMap;
		}

	}
}
