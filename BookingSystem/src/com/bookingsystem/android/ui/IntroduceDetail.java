package com.bookingsystem.android.ui;

import net.duohuo.dhroid.ioc.annotation.InjectExtra;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.util.ViewUtil;
import android.os.Bundle;
import android.widget.TextView;

import com.bookingsystem.android.R;

public class IntroduceDetail extends MBaseActivity{

	@InjectView(id=R.id.introduce)
	TextView introduce;
	
	@InjectExtra(name="name")
	String name;
	@InjectExtra(name="introduce")
	String str_introduce;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_introduce);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setTitleText(name);
		//mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		ViewUtil.bindView(introduce, str_introduce);
		
	}
}
