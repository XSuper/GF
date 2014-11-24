package com.bookingsystem.android.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.duohuo.dhroid.ioc.annotation.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.global.AbConstant;
import com.ab.util.AbDateUtil;
import com.ab.util.AbStrUtil;
import com.ab.view.wheel.AbWheelUtil;
import com.ab.view.wheel.AbWheelView;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;
import com.bookingsystem.android.view.CalendarView;

public class GroundSearchActivity extends MBaseActivity implements OnClickListener{
	
	@InjectView(id=R.id.view_city)
	LinearLayout viewCity;
	@InjectView(id=R.id.view_date)
	LinearLayout viewDate;
	@InjectView(id=R.id.view_time)
	LinearLayout viewTime;
	@InjectView(id=R.id.view_name)
	LinearLayout viewName;
	
	@InjectView(id=R.id.content_city)
	TextView txtCity;
	@InjectView(id=R.id.content_date)
	TextView txtDate;
	@InjectView(id=R.id.content_xingqi)
	TextView txtXingqi;
	@InjectView(id=R.id.content_time)
	TextView txtTime;
	@InjectView(id=R.id.content_name)
	TextView txtName;
	
	@InjectView(id=R.id.submit)
	Button submit;
	
	String loadCity;
	String dateStr;
	String timeStr;
	String nameStr;
	
	
	CalendarView datePopuView;
	View mtimeView;
	
	int type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_ground_search);
		type = getIntent().getIntExtra("type", 1);
		
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		// mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		mAbTitleBar.setTitleText("球场搜索");
		
		init();
		initWheelTime2();
	}
	
	private void init(){
		
		//城市
//		loadCity = MApplication.sCity = AbStrUtil
//				.isEmpty(MApplication.sCity) ? MApplication.city
//				: MApplication.sCity;
		
		//日期
		Date date = new Date();
		date.setDate(date.getDate()+1);
		dateStr = AbDateUtil.getStringByFormat(date, AbDateUtil.dateFormatYMD);
		
		dateChange();
		
		timeStr = "09:00";
		txtTime.setText(timeStr);
		
		//点击事件注册
		viewCity.setOnClickListener(this);
		viewDate.setOnClickListener(this);
		viewName.setOnClickListener(this);
		viewTime.setOnClickListener(this);
		submit.setOnClickListener(this);
		
	}

	private void dateChange() {
		Date dateByFormat = AbDateUtil.getDateByFormat(dateStr, "yyyy-MM-dd");
		Calendar c = new GregorianCalendar();
		c.setTime(dateByFormat);
		int yue = c.get(Calendar.MONTH)+1;
		int ri = c.get(Calendar.DATE);
		String txt = yue+"月"+ri+"日";
		String weekNumber = AbDateUtil.getWeekNumber(dateStr, "yyyy-MM-dd");
		txtXingqi.setText(weekNumber);
		txtDate.setText(txt);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		loadCity = MApplication.sCity = AbStrUtil
				.isEmpty(MApplication.sCity) ? MApplication.city
				: MApplication.sCity;
		if(AbStrUtil.isEmpty(loadCity)){
			txtCity.setText("全国");
		}else{
			txtCity.setText(loadCity);
		}
	}
	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.view_city:
			Intent i = new Intent();
			i.setClass(this, ChoiceCityActivity.class);
			startActivity(i);
			break;
		case R.id.view_date:
			if(datePopuView == null){
				datePopuView = new CalendarView(this){

					@Override
					public void ok() {
						// TODO Auto-generated method stub
						super.ok();
						GroundSearchActivity.this.removeDialog(AbConstant.DIALOGBOTTOM);
						if(datePopuView.date!=null){
							dateStr = datePopuView.date;
							
							dateChange();
						}
					}
					@Override
					public void cancel() {
						// TODO Auto-generated method stub
						super.cancel();
						GroundSearchActivity.this.removeDialog(AbConstant.DIALOGBOTTOM);
					}
					
				};
			}
			showDialog(AbConstant.DIALOGBOTTOM, datePopuView);
			break;
		case R.id.view_time:
			showDialog(AbConstant.DIALOGBOTTOM, mtimeView);
			break;
		case R.id.view_name:
			Intent i_name = new Intent();
			i_name.setClass(this, SearchGroundNameActivity.class);
			i_name.putExtra("type", type);
			startActivityForResult(i_name, 10001);
			break;
		case R.id.submit:
			Intent intent = new Intent();
			intent.putExtra("name", nameStr);
			intent.putExtra("date", dateStr);
			intent.putExtra("time", timeStr);
			
			//保存查询时间和日期
			MApplication.searchDate = dateStr;
			MApplication.searchTime = timeStr;
			
			switch (type) {
			case 1:
				intent.setClass(this, QCActivity.class);
				break;
			case 2:
				intent.setClass(this, LXCActivity.class);
				break;
			}
			startActivity(intent);
			break;

		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent i) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, i);
		switch (arg1) {
		case 10001:
			nameStr = i.getStringExtra("name");
			txtName.setText(nameStr);
			break;

		default:
			break;
		}
	}
	
	// 时间选择器
		public void initWheelTime2() {
			mtimeView = mInflater.inflate(R.layout.golf_wheel_choose_two, null);
			final AbWheelView mWheelViewMM = (AbWheelView) mtimeView
					.findViewById(R.id.wheelView1);
			final AbWheelView mWheelViewHH = (AbWheelView) mtimeView
					.findViewById(R.id.wheelView2);
			Button okBtn = (Button) mtimeView.findViewById(R.id.okBtn);
			Button cancelBtn = (Button) mtimeView.findViewById(R.id.cancelBtn);
			mWheelViewMM.setCenterSelectDrawable(this.getResources().getDrawable(
					R.drawable.golf_wheel_select));
			mWheelViewHH.setCenterSelectDrawable(this.getResources().getDrawable(
					R.drawable.golf_wheel_select));
			AbWheelUtil.initWheelTimePicker2(this, txtTime, mWheelViewMM,
					mWheelViewHH, okBtn, cancelBtn, 9, 0, false,
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							timeStr = txtTime.getText().toString();
						}
					});

		}

}
