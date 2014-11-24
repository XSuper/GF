package com.bookingsystem.android.ui;

import net.duohuo.dhroid.ioc.annotation.InjectExtra;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.util.ViewUtil;
import android.os.Bundle;
import android.widget.TextView;

import com.bookingsystem.android.R;

public class GaiKuangDetail extends MBaseActivity{
	@InjectView(id=R.id.model)
	TextView  model;
	@InjectView(id=R.id.data)
	TextView  data;
	@InjectView(id=R.id.createdate)
	TextView  createdate;
	@InjectView(id=R.id.designer)
	TextView  designer;
	@InjectView(id=R.id.area)
	TextView  area;
	@InjectView(id=R.id.length)
	TextView  length;
	@InjectView(id=R.id.greengrass)
	TextView  greengrass;
	@InjectView(id=R.id.fairwaygrass)
	TextView  fairwaygrass;
	
	@InjectExtra(name="model")
	String str_model;
	@InjectExtra(name="data")
	String str_data;
	@InjectExtra(name="createdate")
	String str_createdate;
	@InjectExtra(name="designer")
	String str_designer;
	@InjectExtra(name="area")
	String str_area;
	@InjectExtra(name="length")
	String str_length;
	@InjectExtra(name="greengrass")
	String str_greengrass;
	@InjectExtra(name="fairwaygrass")
	String str_fairwaygrass;
	
	
	
	@InjectExtra(name="name")
	String name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_gaikuang);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setTitleText(name);
		//mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		
		ViewUtil.bindView(model, str_model);
		ViewUtil.bindView(data, str_data);
		ViewUtil.bindView(createdate, str_createdate);
		ViewUtil.bindView(designer, str_designer);
		ViewUtil.bindView(area, str_area);
		ViewUtil.bindView(length, str_length);
		ViewUtil.bindView(greengrass, str_greengrass);
		ViewUtil.bindView(fairwaygrass, str_fairwaygrass);
		
	}

}
