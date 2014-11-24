package com.bookingsystem.android.ui;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import android.os.Bundle;
import android.webkit.WebView;

import com.bookingsystem.android.R;

public class ADWebViewActivity extends BaseActivity {

	@InjectView(id=R.id.content)
	WebView web;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_ad);
		
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		//mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		
		String name = getIntent().getStringExtra("name");
		String url = getIntent().getStringExtra("url");
		
		mAbTitleBar.setTitleText(name);
		web.loadUrl(url );
	}

}
