package com.bookingsystem.android.adapter;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.util.ViewUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookingsystem.android.R;
import com.bookingsystem.android.bean.Ground;
import com.bookingsystem.android.bean.Weather;
import com.bookingsystem.android.util.Util;

public class WeatherAdapter extends MBaseAdapter{
	public WeatherAdapter(BaseActivity context,ArrayList<Weather> datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public View getView(int position,View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		viewHoder v = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_weather, null);
			ImageView image = (ImageView)convertView.findViewById(R.id.weather_img);
			TextView date = (TextView)convertView.findViewById(R.id.weather_date);
			TextView wendu = (TextView)convertView.findViewById(R.id.weather_wendu);
			TextView weather = (TextView)convertView.findViewById(R.id.weather_text);
			TextView wind = (TextView)convertView.findViewById(R.id.weather_wind);
			v = new viewHoder();
			v.image = image;
			v.date = date;
			v.wendu = wendu;
			v.weather = weather;
			v.wind = wind;
			convertView.setTag(v);
		}else{
			v= (viewHoder)convertView.getTag();
		}
		if(!Util.setWeatherImg(v.image, ((Weather)datas.get(position)).getWeather())){
			ViewUtil.bindView(v.image,((Weather)datas.get(position)).getDayPictureUrl());
		}
		ViewUtil.bindView(v.date,((Weather)datas.get(position)).getDate());
		ViewUtil.bindView(v.wendu,((Weather)datas.get(position)).getTemperature());
		ViewUtil.bindView(v.wind,((Weather)datas.get(position)).getWind());
		ViewUtil.bindView(v.weather,((Weather)datas.get(position)).getWeather());
		
		
		return convertView;
	}
	
	class viewHoder{
		ImageView image;
		TextView date;
		TextView wendu;
		TextView wind;
		TextView weather;
	}

}
