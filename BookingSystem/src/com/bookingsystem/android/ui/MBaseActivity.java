package com.bookingsystem.android.ui;

import net.duohuo.dhroid.activity.BaseActivity;

import com.umeng.analytics.MobclickAgent;

public class MBaseActivity extends BaseActivity {
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
