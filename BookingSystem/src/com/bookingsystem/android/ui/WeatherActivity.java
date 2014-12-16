package com.bookingsystem.android.ui;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.ioc.annotation.InjectExtra;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import android.os.Bundle;
import android.widget.ListView;

import com.bookingsystem.android.R;
import com.bookingsystem.android.adapter.WeatherAdapter;
import com.bookingsystem.android.bean.Weather;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WeatherActivity extends BaseActivity{
	@InjectView(id=R.id.list)
	ListView listView;
	WeatherAdapter adapter;
	@InjectExtra(name="weatherinfo")
	String weatherinfo;
	
	ArrayList<Weather> weathers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_weather);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setTitleText("天气预报");
		//mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		Gson gson = new Gson();
		weathers = gson.fromJson(weatherinfo, new TypeToken<ArrayList<Weather>>(){}.getType());
		adapter = new WeatherAdapter(this, weathers);
		listView.setAdapter(adapter);
		
	}
}
