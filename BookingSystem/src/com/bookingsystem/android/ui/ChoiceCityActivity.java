package com.bookingsystem.android.ui;

import java.util.List;

import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.util.AbStrUtil;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;
import com.bookingsystem.android.adapter.ProvinceAdapter;
import com.bookingsystem.android.util.CityUtil;

public class ChoiceCityActivity extends MBaseActivity{

	@InjectView(id=R.id.currentCity)
	TextView currentCity;
	@InjectView(id=R.id.province)
	ListView pList;
	
	BaseAdapter pAdapter ;
	DhDB db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_place);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(20, 0, 0, 0);
		mAbTitleBar.setTitleText("省市列表");
		//mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		String city = MApplication.city;
		currentCity.setText((AbStrUtil.isEmpty(city)?"定位失败":city));
		List<String> ps = CityUtil.getAllProvince();
		pAdapter = new ProvinceAdapter(this,ps);
		pList.setAdapter(pAdapter);
	}
}
