package com.bookingsystem.android.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bookingsystem.android.R;
import com.bookingsystem.android.view.KCalendar.OnCalendarClickListener;
import com.bookingsystem.android.view.KCalendar.OnCalendarDateChangedListener;

public class CalendarView extends LinearLayout implements OnClickListener {

	TextView popupwindow_calendar_month;
	KCalendar calendar;
	Button popupwindow_calendar_bt_enter,popupwindow_calendar_bt_cancle;
	RelativeLayout popupwindow_calendar_last_month;
	RelativeLayout popupwindow_calendar_next_month;

	public String date = null;// 设置默认选中的日期 格式为 “2014-04-05” 标准DATE格式

	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CalendarView(Context context) {
		super(context);
		init(context);
	}

	public void ok() {
	};

	private void init(Context context) {

		View view = inflate(context, R.layout.popupwindow_calendar, null);

		popupwindow_calendar_month = (TextView) view
				.findViewById(R.id.popupwindow_calendar_month);
		calendar = (KCalendar) view.findViewById(R.id.popupwindow_calendar);
		popupwindow_calendar_bt_enter = (Button) view
				.findViewById(R.id.popupwindow_calendar_bt_enter);
		popupwindow_calendar_bt_cancle = (Button) view
				.findViewById(R.id.popupwindow_calendar_bt_cancle);
		popupwindow_calendar_last_month = (RelativeLayout) view
				.findViewById(R.id.popupwindow_calendar_last_month);
		popupwindow_calendar_next_month = (RelativeLayout) view
				.findViewById(R.id.popupwindow_calendar_next_month);

		popupwindow_calendar_bt_enter.setOnClickListener(this);
		popupwindow_calendar_bt_cancle.setOnClickListener(this);
		popupwindow_calendar_last_month.setOnClickListener(this);
		popupwindow_calendar_next_month.setOnClickListener(this);

		addView(view);

		popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
				+ calendar.getCalendarMonth() + "月");

		if (null != date) {

			int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
			int month = Integer.parseInt(date.substring(date.indexOf("-") + 1,
					date.lastIndexOf("-")));
			popupwindow_calendar_month.setText(years + "年" + month + "月");

			calendar.showCalendar(years, month);
			calendar.setCalendarDayBgColor(date,
					R.drawable.calendar_date_focused);
		}
		// 监听所选中的日期
		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {

			public void onCalendarClick(int row, int col, String dateFormat) {
				int month = Integer.parseInt(dateFormat.substring(
						dateFormat.indexOf("-") + 1,
						dateFormat.lastIndexOf("-")));

				if (calendar.getCalendarMonth() - month == 1// 跨年跳转
						|| calendar.getCalendarMonth() - month == -11) {
					Date now = new Date();
					if (calendar.getCalendarYear() > (now.getYear() + 1900)
							|| (calendar.getCalendarYear() == (now.getYear() + 1900) && calendar
									.getCalendarMonth() > (now.getMonth() + 1)))
						calendar.lastMonth();

				} else if (month - calendar.getCalendarMonth() == 1 // 跨年跳转
						|| month - calendar.getCalendarMonth() == -11) {
					calendar.nextMonth();
				}
				SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd");
				Date sdate = null;
				try {
					sdate = mSimpleDateFormat.parse(dateFormat);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Date thisday = new Date();
				if (thisday.before(sdate)
						|| (thisday.getDate() == sdate.getDate()
								&& thisday.getMonth() == sdate.getMonth() && thisday
								.getYear() == sdate.getYear())) {
					calendar.removeAllBgColor();
					calendar.setCalendarDayBgColor(dateFormat,
							R.drawable.calendar_date_focused);
					date = dateFormat;// 最后返回给全局 date
				}
				else {

				}
			}
		});

		// 监听当前月份
		calendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
			public void onCalendarDateChanged(int year, int month) {
				popupwindow_calendar_month.setText(year + "年" + month + "月");
			}
		});

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.popupwindow_calendar_last_month:
			calendar.lastMonth();
			break;
		case R.id.popupwindow_calendar_next_month:
			calendar.nextMonth();
			break;
		case R.id.popupwindow_calendar_bt_enter:
			ok();
		case R.id.popupwindow_calendar_bt_cancle:
			cancel();
			break;
		}
	}

	public void cancel() {
		// TODO Auto-generated method stub
		
	}
}
