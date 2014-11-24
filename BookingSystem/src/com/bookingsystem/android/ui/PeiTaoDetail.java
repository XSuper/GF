package com.bookingsystem.android.ui;

import net.duohuo.dhroid.ioc.annotation.InjectExtra;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.util.ViewUtil;
import android.os.Bundle;
import android.widget.TextView;

import com.bookingsystem.android.R;
import com.bookingsystem.android.util.StringUtils;

public class PeiTaoDetail extends MBaseActivity{

	@InjectView(id=R.id.peitao)
	TextView peitao;
	
	@InjectExtra(name="name")
	String name;
	@InjectExtra(name="peitao")
	String str_peitao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_peitao);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setTitleText(name);
		//mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		str_peitao = StringUtils.parseMatching(str_peitao);
		ViewUtil.bindView(peitao, str_peitao);
		
	}
}
