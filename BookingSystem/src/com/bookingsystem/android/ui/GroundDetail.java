package com.bookingsystem.android.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import net.duohuo.dhroid.adapter.BeanAdapter.InViewClickListener;
import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.INetAdapter.LoadSuccessCallBack;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.ioc.annotation.InjectExtra;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.cache.CachePolicy;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.global.AbConstant;
import com.ab.util.AbDateUtil;
import com.ab.util.AbStrUtil;
import com.ab.view.sliding.AbSlidingPlayView;
import com.ab.view.wheel.AbWheelUtil;
import com.ab.view.wheel.AbWheelView;
import com.bookingsystem.android.Constant;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;
import com.bookingsystem.android.util.Util;

public class GroundDetail extends MBaseActivity implements OnClickListener {
	
	@InjectView(id = R.id.ground_detail_date)
	TextView dateView;
	@InjectView(id = R.id.ground_detail_date_xinqi)
	TextView xinqi;
	
	@InjectView(id = R.id.ground_detail_time)
	TextView time;
	@InjectView(id = R.id.detailinfo)
	ImageView detailinfo;
	

	@InjectView(id = R.id.ground_detail_PlayView)
	AbSlidingPlayView playView;
	@InjectView(id = R.id.ground_detail_list)
	ListView list;

	@InjectExtra(name = "id")
	String id;

	int type = 1;
	View mDateView, mtimeView;

	NetJSONAdapter mAdapter;

	JSONObject jo;
	String url = Constant.BASEURL + "&a=clubdetail";

	String timestr;
	String datestr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		type = getIntent().getIntExtra("type", 1);

		setAbContentView(R.layout.activity_ballpark_detail);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		// mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		loadData();
		initView();
		initWheelDate();
		initWheelTime2();
		int resid = 0;
		switch (type) {
		case 1:
			resid = R.layout.item_grounddetail_1;
			break;

		case 2:
			resid = R.layout.item_grounddetail_2;

			break;
		}
		mAdapter = new NetJSONAdapter(Constant.BASEURL + "&clubid=" + id, this,
				resid);
		if (type == 2) {
			mAdapter.addField("position", R.id.position);
			mAdapter.addField("priceunit", R.id.pricetype, "pricetype");
		}
		if (!AbStrUtil.isEmpty(MApplication.searchTime)) {
			mAdapter.addparam("playtime", MApplication.searchTime);
		}
		if (!AbStrUtil.isEmpty(MApplication.searchDate)) {
			mAdapter.addparam("playdate", MApplication.searchDate);
		}

		mAdapter.addField("name", R.id.name);
		mAdapter.addField(new FieldMap("address", R.id.description) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				// TODO Auto-generated method stub
				JSONObject joo = (JSONObject) jo;
				int mtype = JSONUtil.getInt(joo, "type");
				if (mtype == 1) {
					return "费用包含：果岭费/球童/球车/衣柜";
				} else if (mtype == 2) {

				}
				return o;
			}
		});
		mAdapter.addField("price", R.id.price, "price");
		InViewClickListener inViewClickListener = new InViewClickListener() {

			@Override
			public void OnClickListener(View parentV, View v, Integer position,
					Object values) {
				// TODO Auto-generated method stub
				if (!MApplication.islogin || MApplication.user == null) {
					android.content.DialogInterface.OnClickListener mOkOnClickListener = new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent i = new Intent(getApplication(),
									LoginActivity.class);
							startActivity(i);
						}
					};
					showDialog("请先登录", "登录后才能预定，是否立即登陆？", mOkOnClickListener);
					return;
				}
				switch (type) {
				case 0:

					break;
				case 1:
					Intent i = new Intent(getApplication(), GroundOrder.class);
					i.putExtra("data", values.toString());
					startActivity(i);
					break;
				case 2://
					Intent i2 = new Intent(getApplication(), LXCOrder.class);
					i2.putExtra("data", values.toString());
					startActivity(i2);
					break;

				}

			}
		};

		mAdapter.setOnInViewClickListener(R.id.name, inViewClickListener);
		mAdapter.setOnInViewClickListener(R.id.submit, inViewClickListener);
		list.setAdapter(mAdapter);
		mAdapter.refresh();
		mAdapter.setOnLoadSuccess(new LoadSuccessCallBack() {

			@Override
			public void callBack(Response response) {
				// TODO Auto-generated method stub
				Util.setListViewHeightBasedOnChildren(list);

			}
		});
	}

	public void initView() {
		// String timestr = AbDateUtil.getCurrentDate("HH:mm");
		// String datestr = AbDateUtil.getCurrentDate("yy-MM-dd");
		timestr = AbStrUtil.isEmpty(MApplication.searchTime) ? "09:00"
				: MApplication.searchTime;
		datestr = AbStrUtil.isEmpty(MApplication.searchDate) ? AbDateUtil
				.getCurrentDate("yy-MM-dd") : MApplication.searchDate;
		String xinqiStr = AbDateUtil.getWeekNumber(datestr, "yy-MM-dd");
		xinqi.setText(xinqiStr);
		dateView.setText(datestr);
		time.setText(timestr);

		dateView.setOnClickListener(this);
		time.setOnClickListener(this);
		
	}

	public void loadData() {
		DhNet mnet = new DhNet(url);
		mnet.addParam("clubid", id);
		mnet.useCache(CachePolicy.POLICY_ON_NET_ERROR);
		mnet.doGet(new NetTask(this) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub

				jo = response.jSONFromData();
				detailinfo.setOnClickListener(GroundDetail.this);
				type = JSONUtil.getInt(jo, "type");
				mAbTitleBar.setTitleText(JSONUtil.getString(jo, "name"));
				ArrayList<String> srcs = new ArrayList<String>();
				srcs.add(JSONUtil.getString(jo, "mpic1"));
				srcs.add(JSONUtil.getString(jo, "mpic2"));
				srcs.add(JSONUtil.getString(jo, "mpic3"));
				srcs.add(JSONUtil.getString(jo, "mpic4"));
				srcs.add(JSONUtil.getString(jo, "mpic5"));

				initPlayView(srcs, playView);
			}
		});

	}

	/**
	 * 初始化图集
	 * 
	 * @param srcs
	 * @param playView
	 */
	public void initPlayView(ArrayList<String> srcs, AbSlidingPlayView playView) {
		for (String string : srcs) {
			if (!AbStrUtil.isEmpty(string)) {
				ImageView view = new ImageView(this);
				view.setBackgroundResource(R.drawable.imgdefault);
				view.setLayoutParams(layoutParamsFF);
				view.setScaleType(ScaleType.CENTER_CROP);
				ViewUtil.bindView(view, string);
				playView.addView(view);
			}
		}
		playView.startPlay();
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
		SimpleDateFormat f = new SimpleDateFormat("HH:mm");
		Date date = null;

		try {
			date = f.parse(timestr);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			date = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		AbWheelUtil.initWheelTimePicker2(this, time, mWheelViewMM,
				mWheelViewHH, okBtn, cancelBtn, calendar.get(Calendar.HOUR),
				calendar.get(Calendar.MINUTE), false, new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						MApplication.searchTime = time.getText().toString();
						loadlistbytime();
					}
				});
		// AbWheelUtil.initWheelTimePicker2(this, mText,mWheelViewMM,
		// mWheelViewHH,okBtn,cancelBtn,16,23,false);

	}

	// 初始化时间选择器
	public void initWheelDate() {
		mDateView = mInflater.inflate(R.layout.golf_wheel_choose_three, null);
		// 年月日时间选择器
		SimpleDateFormat f = new SimpleDateFormat("yyy-MM-dd");
		Date date = null;
		try {
			date = f.parse(datestr);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			date = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		final AbWheelView mWheelViewY = (AbWheelView) mDateView
				.findViewById(R.id.wheelView1);
		final AbWheelView mWheelViewM = (AbWheelView) mDateView
				.findViewById(R.id.wheelView2);
		final AbWheelView mWheelViewD = (AbWheelView) mDateView
				.findViewById(R.id.wheelView3);
		Button okBtn = (Button) mDateView.findViewById(R.id.okBtn);
		Button cancelBtn = (Button) mDateView.findViewById(R.id.cancelBtn);
		mWheelViewY.setCenterSelectDrawable(this.getResources().getDrawable(
				R.drawable.golf_wheel_select));
		mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(
				R.drawable.golf_wheel_select));
		mWheelViewD.setCenterSelectDrawable(this.getResources().getDrawable(
				R.drawable.golf_wheel_select));
		AbWheelUtil.initWheelDatePicker(this, dateView, mWheelViewY,
				mWheelViewM, mWheelViewD, okBtn, cancelBtn, year, month, day,
				1900, 150, false, new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String xinqiStr = AbDateUtil.getWeekNumber(dateView
								.getText().toString(), "yy-MM-dd");
						xinqi.setText(xinqiStr);

						MApplication.searchDate = dateView.getText().toString();
						loadlistbytime();
					}

				});

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == dateView) {
			showDialog(AbConstant.DIALOGBOTTOM, mDateView);
		} else if (view == time) {
			showDialog(AbConstant.DIALOGBOTTOM, mtimeView);
		} else if (view == detailinfo) {
			if (jo != null) {
				Intent i = new Intent(this, GroundInfoDetail.class);
				i.putExtra("model", JSONUtil.getString(jo, "model"));
				i.putExtra("data", JSONUtil.getString(jo, "data"));
				i.putExtra("createdate", JSONUtil.getString(jo, "createdate"));
				i.putExtra("designer", JSONUtil.getString(jo, "designer"));
				i.putExtra("area", JSONUtil.getString(jo, "area"));
				i.putExtra("length", JSONUtil.getString(jo, "length"));
				i.putExtra("greengrass", JSONUtil.getString(jo, "greengrass"));
				i.putExtra("fairwaygrass",
						JSONUtil.getString(jo, "fairwaygrass"));
				i.putExtra("peitao", JSONUtil.getString(jo, "matching"));
				i.putExtra("jianjie", JSONUtil.getString(jo, "introduction"));

				i.putExtra("address", JSONUtil.getString(jo, "address"));

				i.putExtra("name", JSONUtil.getString(jo, "name"));
				i.putExtra("Lat", JSONUtil.getString(jo, "center_lat"));
				i.putExtra("Lon", JSONUtil.getString(jo, "center_lng"));
				i.putExtra("place", JSONUtil.getString(jo, "city"));

				startActivity(i);
			}
		
		}

	}

	private void loadlistbytime() {
		mAdapter.addparam("playdate", dateView.getText().toString());
		mAdapter.addparam("playtime", time.getText().toString());
		mAdapter.refreshDialog();

	}

}
