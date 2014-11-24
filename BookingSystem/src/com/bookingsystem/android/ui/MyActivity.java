package com.bookingsystem.android.ui;

import java.util.List;

import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.ioc.annotation.Inject;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bookingsystem.android.Constant;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;
import com.bookingsystem.android.UserShared;
import com.bookingsystem.android.bean.Ground;
import com.umeng.analytics.MobclickAgent;

public class MyActivity extends MBaseActivity implements OnClickListener {

	TextView text;

	TextView oftentxt, historytxt, moreapp, myOrder;
	View often, history;

	List<Ground> hisList;

	@Inject
	DhDB db;

	int count = 0;// 经常访问球场的个数

	private Button RightBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.fragment_me);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(20, 0, 0, 0);
		mAbTitleBar.setTitleText("我的");
		// mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);

		initTitleRightLayout();
		//db = IocContainer.getShare().get(DhDB.class);

		text = (TextView) findViewById(R.id.text);

		oftentxt = (TextView) findViewById(R.id.often_text);
		historytxt = (TextView) findViewById(R.id.history_text);
		moreapp = (TextView) findViewById(R.id.moreapp);
		myOrder = (TextView) findViewById(R.id.myorder);
		often = findViewById(R.id.often);
		history = findViewById(R.id.history);

		often.setOnClickListener(this);
		history.setOnClickListener(this);
		moreapp.setOnClickListener(this);
		myOrder.setOnClickListener(this);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(RightBtn!=null){
			RightBtn.setText(MApplication.islogin ? "注销" : "登录");
		}
		checkLoginState();

		hisList = db.queryAll(Ground.class);

		if (hisList != null && hisList.size() > 0) {
			historytxt.setText(hisList.size() + "");
		}
		loadCount();
	}

	public void initTitleRightLayout() {
		mAbTitleBar.clearRightView();
		View v = mInflater.inflate(R.layout.view_top_right, null);
		RightBtn = (Button) v.findViewById(R.id.tbtn);
		mAbTitleBar.addRightView(v);
		RightBtn.setText(MApplication.islogin ? "注销" : "登录");
		RightBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MApplication.islogin) {

					showDialog("注销用户", "注销后将清理所有用户数据!",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									//db.delete(MApplication.user);
									UserShared userShared = UserShared.getInstance();
									userShared.user = null;
									userShared.clearAll();
									userShared.commit();
									MApplication.islogin = false;
									MApplication.user = null;
									RightBtn.setText("登陆");
									checkLoginState();
								}
							});

				} else {

					Intent in = new Intent(MyActivity.this, LoginActivity.class);
					startActivity(in);
				}
			}
		});
	}

	public void checkLoginState() {
		boolean islogin = MApplication.islogin;
		if (islogin) {
			text.setText(MApplication.user.getMobile() + "您好,欢迎使用高盛通订场系统");
		} else {
			text.setText("您好，目前您处于离线状态，敬请点击右上角登陆！");
			oftentxt.setText("0");

		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == often) {
			if (MApplication.islogin) {
				Intent i = new Intent(this, OftenActivity.class);
				startActivity(i);

			} else {
				showToast("您需要先登陆");
			}
		} else if (view == history) {
			Intent i = new Intent(this, BrowseActivity.class);
			startActivity(i);
		} else if (view == moreapp) {
			Intent i = new Intent(this, MoreAppActivity.class);
			startActivity(i);
		} else if (view == myOrder) {
			if (MApplication.islogin) {
				Intent i = new Intent(this, MyOrderActivity.class);
				startActivity(i);

			} else {
				showToast("您需要先登陆");
			}
		}

	}

	public void loadCount() {
		if (!MApplication.islogin) {
			oftentxt.setText("0");
			return;
		} else {
			Log.v("me loadcount", "----------");
			DhNet net = new DhNet(Constant.BASEURL + "&a=ordercount");
			net.addParam("status", 1);
			net.addParam("type", 1);
			net.addParam("uid", MApplication.user.getMid());
			net.addParam("token", MApplication.user.getToken());
			if (count++ == 0) {
				oftentxt.setText("请稍后");
			}
			net.execuse(new NetTask(this) {

				@Override
				public void doInUI(Response response, Integer transfer) {
					// TODO Auto-generated method stub
					Log.v("data", response.result);
					JSONObject jo = response.jSONFromData();
					int c = JSONUtil.getInt(jo, "clubcount", 0);
					oftentxt.setText(c + "");
				}
			});
		}
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("main-me");
	}

}
