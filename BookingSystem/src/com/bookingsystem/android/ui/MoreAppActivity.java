package com.bookingsystem.android.ui;

import java.io.File;
import java.util.HashMap;

import net.duohuo.dhroid.adapter.BeanAdapter.InViewClickListener;
import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.download.DownLoadManager;
import net.duohuo.dhroid.net.download.DownLoadManager.DownLoadCallBack;
import net.duohuo.dhroid.net.download.DownloadTask;

import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ab.util.AbAppUtil;
import com.ab.view.progress.AbHorizontalProgressBar;
import com.bookingsystem.android.Constant;
import com.bookingsystem.android.R;
import com.bookingsystem.android.service.DownloadAppService;
import com.bookingsystem.android.service.DownloadAppService.Mbinder;

public class MoreAppActivity extends MBaseActivity {
	
	DownloadAppService mService;
	Mbinder mb ;
	HashMap<String,DownLoadManager> downloadMap;
    boolean mBound = false;
    private ServiceConnection mConnection;
    public NotificationManager notificationManager;
	@InjectView(id=R.id.blist)
	ListView mlist;
	
	
	NetJSONAdapter netadapter;
	DhNet net;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bindService();
		//clearNotification();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setAbContentView(R.layout.activity_list);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		mAbTitleBar.setTitleText("精彩应用");
		
		netadapter = new NetJSONAdapter(Constant.BASEURL+"&a=moreapp", this, R.layout.item_moreapp);
		netadapter.addField("ico",R.id.img);;
		netadapter.addField(new FieldMap("name",R.id.name) {
			
			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				// TODO Auto-generated method stub
				if(downloadMap!=null&&downloadMap.containsKey(o.toString())){
					final AbHorizontalProgressBar progress = (AbHorizontalProgressBar)itemV.findViewById(R.id.Progress);
					final Button btn = (Button)itemV.findViewById(R.id.btn);
					DownLoadManager downloader = downloadMap.get(o.toString());
					registerDownloaderCallback(downloader, o.toString(), progress, btn);
				}
				return o;
			}
		});
		netadapter.addField(new FieldMap("url",R.id.btn) {
			
			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				// TODO Auto-generated method stub
				JSONObject joo = (JSONObject)jo;
				itemV.setTag(R.id.btn,o.toString());
				itemV.setTag(R.id.name,JSONUtil.getString(joo, "name"));
				String path = Environment.getExternalStorageDirectory()+"/gst/app/"+JSONUtil.getString(joo, "name")+".apk";
				File file = new File(path);
				if(file.exists()){
					return "安装";
				}else
				return "下载";
			}
		});
		mlist.setAdapter(netadapter);
		netadapter.refreshDialog();
		netadapter.setOnInViewClickListener(R.id.btn, new InViewClickListener() {

			@Override
			public void OnClickListener(View parentV, final View v, Integer position,
					Object values) {
				// TODO Auto-generated method stub
				final String name = parentV.getTag(R.id.name).toString();
				final String file = Environment.getExternalStorageDirectory()+"/gst/app/"+name+".apk";
				Button btn = (Button)v;
				if("安装".equals(btn.getText().toString())){
					AbAppUtil.installApk(getApplicationContext(), new File(file));
				}else{
				
				final AbHorizontalProgressBar p = (AbHorizontalProgressBar)parentV.findViewById(R.id.Progress);
				final String url = parentV.getTag(R.id.btn).toString();
				Intent service = new Intent(MoreAppActivity.this, DownloadAppService.class);
				service.putExtra("name", name);
				service.putExtra("url", url);
				service.putExtra("file", file);
				MoreAppActivity.this.startService(service);
				DownLoadManager downloader = null;
				if(downloadMap.containsKey(name)){
					downloader = downloadMap.get(name);
				}else{
					downloader = new DownLoadManager();
					downloader.download(name, url, null, file);
					downloadMap.put(name, downloader);
				}
				registerDownloaderCallback(downloader, name, p, btn);
				}
			}
		});
	}
	
	/**
	 * 绑定下载服务
	 */
	public void bindService() {
		Intent service = new Intent(MoreAppActivity.this, DownloadAppService.class);
		mConnection = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				// TODO Auto-generated method stub
				mBound = false;
			}
			
			@Override
			public void onServiceConnected(ComponentName arg0, IBinder mbinder) {
				// TODO Auto-generated method stub
				mb = (Mbinder)mbinder;
				mService = mb.getService();
				downloadMap =mb.getDownLoadMap();
				mBound = true;
			}
		};
		bindService(service,mConnection , Context.BIND_AUTO_CREATE);
		
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
	}
	
	
	/**
	 * 注册文件下载监听器
	 */
	public void registerDownloaderCallback(final DownLoadManager downloader,final String name,final AbHorizontalProgressBar progress,final Button btn){
		progress.setVisibility(View.VISIBLE);
		btn.setClickable(false);
		final String file = Environment.getExternalStorageDirectory()+"/gst/app/"+name+".apk";
		downloader.regeisterCallBack(name, new DownLoadCallBack() {
			
			@Override
			public void onStop(DownloadTask task) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPersent(DownloadTask task, float persent) {
				// TODO Auto-generated method stub
				progress.setProgress((int)(persent*100));
			}
			
			@Override
			public void onEnd(DownloadTask task) {
				// TODO Auto-generated method stub
				btn.setClickable(true);
				btn.setText("安装");
				AbAppUtil.installApk(getApplicationContext(), new File(file));
				downloader.unregeisterCallBack(name);
				downloadMap.remove(name);
			}
		});
	}

}
