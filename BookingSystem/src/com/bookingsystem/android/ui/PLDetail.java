package com.bookingsystem.android.ui;

import java.util.ArrayList;

import net.duohuo.dhroid.activity.AbActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ab.util.AbDateUtil;
import com.ab.util.AbStrUtil;
import com.ab.view.sliding.AbSlidingPlayView;
import com.bookingsystem.android.Constant;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;

public class PLDetail extends MBaseActivity implements OnClickListener {
	@InjectView(id = R.id.pl_detail_place)
	TextView place;
	@InjectView(id = R.id.pl_detail_introduce)
	TextView introduce;
	@InjectView(id = R.id.pl_detail_tel)
	TextView tel;
	@InjectView(id = R.id.pl_detail_jiesong)
	TextView jiesong;
//	@InjectView(id = R.id.ground_detail_date)
//	TextView dateView;
//	@InjectView(id = R.id.ground_detail_date_xinqi)
//	TextView xinqi;
	// @InjectView(id = R.id.ground_detail_time)
	// TextView time;
	@InjectView(id = R.id.rootLayout)
	LinearLayout rootLayout;

	@InjectView(id = R.id.ground_detail_PlayView)
	AbSlidingPlayView playView;
	@InjectView(id = R.id.pl_detail_list)
	ListView list;

	@InjectExtra(name = "uid")
	String id;
	
	@InjectExtra(name = "pay")
	boolean pay;

	private String mobile;
	int sex;
	
	View mDateView;

	NetJSONAdapter mAdapter;

	JSONObject jo;
	String url = Constant.BASEURL + "&a=sparringdetail";
	
	
	
	int age;
	String address;
	int type =1;
	String pic1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method s tub
		super.onCreate(savedInstanceState);
		
		setAbContentView(R.layout.activity_pl_detail);
		sex = getIntent().getIntExtra("sex", 0);
		Log.v("sex", sex+"");
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		loadData();
		initView();
//		initWheelDate();
		// initWheelTime2();

		mAdapter = new NetJSONAdapter(Constant.BASEURL + "&a=sparringpub&uid="
				+ id, this, R.layout.item_pldetail);
//		mAdapter.addField(new FieldMap("username", R.id.title){
//
//			@Override
//			public Object fix(View itemV, Integer position, Object o, Object jo) {
//				// TODO Auto-generated method stub
//				final JSONObject joo = (JSONObject) jo;
//				
//				switch (sex) {
//				case 1:
//					return "预定他的陪练服务";
//
//				case 2:
//					return "预定她得陪练服务";
//				}
//				return "预定TA的陪练服务";
//			}});
		
		mAdapter.addField("playdate", R.id.date);
		mAdapter.addField("playstarttime", R.id.begintime);
		mAdapter.addField("playendtime", R.id.endtime);
		final AbActivity mc = this;
		mAdapter.addField("clubprice", R.id.price, "price");
		InViewClickListener inViewClickListener = new InViewClickListener() {

			@Override
			public void OnClickListener(View parentV, View v, Integer position,
					Object values) {
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
				if(address!= null){
					Intent i = new Intent(getApplicationContext(), PLOrder.class);
					i.putExtra("data", values.toString());
					i.putExtra("age", age+"");
					i.putExtra("mpic1", pic1+"");
					i.putExtra("address",address);
					i.putExtra("type",type+"");
					i.putExtra("mobile",mobile+"");
					
					startActivity(i);
					
				}

			}
		};
		
		//mAdapter.setOnInViewClickListener(R.id.title, inViewClickListener);
		mAdapter.setOnInViewClickListener(R.id.submit, inViewClickListener);
		// 初始化 spinner 和设置按钮的可点击
		mAdapter.addField(new FieldMap("status", R.id.submit) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				// TODO Auto-generated method stub
				final JSONObject joo = (JSONObject) jo;
				final TextView price = (TextView) itemV
						.findViewById(R.id.price);
				// 这里可以做一些额外的工作
				final View submit = itemV.findViewById(R.id.submit);
				submit.setClickable("1".equals(o.toString()));
				Spinner spinner = (Spinner) itemV.findViewById(R.id.spinner);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(mc,
						R.layout.spinner_item, new String[] { "球场", "练习场" });
				spinner.setAdapter(adapter);
				OnItemSelectedListener listener = new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						try {
							switch (position) {
							case 0:
								ViewUtil.bindView(price,
										joo.getString("clubprice"), "price");
								submit.setTag(1);
								type = 1;
								break;
							case 1:
								type = 2;
								submit.setTag(2);
								ViewUtil.bindView(price,
										joo.getString("practiceprice"), "price");
								break;

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
					}
				};
				spinner.setOnItemSelectedListener(listener);
				return "预定";
			}
		});
		list.setAdapter(mAdapter);
		mAdapter.refresh();
		// 重绘item 的高度 解决 listview 嵌套在scorwView 的问题
		mAdapter.setOnLoadSuccess(new LoadSuccessCallBack() {
			@Override
			public void callBack(Response response) {
				// TODO Auto-generated method stub
			//	Util.setListViewHeightBasedOnChildren(list);

			}
		});
	}

	public void initView() {
		String datestr = AbDateUtil.getCurrentDate("yy-MM-dd");
		String xinqiStr = AbDateUtil.getWeekNumber(datestr, "yy-MM-dd");
//		xinqi.setText(xinqiStr);
//		dateView.setText(datestr);
//		dateView.setOnClickListener(this);
		introduce.setOnClickListener(this);
		tel.setOnClickListener(this);
		place.setOnClickListener(this);
		jiesong.setOnClickListener(this);
	}

	public void loadData() {
		DhNet mnet = new DhNet(url);
		mnet.addParam("sparringid", id);
		mnet.useCache(CachePolicy.POLICY_ON_NET_ERROR);
		mnet.doGet(new NetTask(this) {

			

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				jo = response.jSONFromData();
				pic1=JSONUtil.getString(jo, "mpic1");
				age = JSONUtil.getInt(jo, "age");
				address = JSONUtil.getString(jo, "address");
				mobile = JSONUtil.getString(jo, "mobile");
				if(pay){
					ViewUtil.bindView(place, JSONUtil.getString(jo, "address"),
							"address");
					ViewUtil.bindView(tel, mobile,
							"mobile");
				}else{
					ViewUtil.bindView(place, JSONUtil.getString(jo, "address"),
							"addresshide");
					ViewUtil.bindView(tel, mobile,
							"mobilepass");
					
				}
				
				ViewUtil.bindView(jiesong, JSONUtil.getString(jo, "isshuttle"),
						"isshuttle");
				mAbTitleBar.setTitleText(JSONUtil.getString(jo, "username"));
				ArrayList<String> srcs = new ArrayList<String>();
				srcs.add(JSONUtil.getString(jo, "mpic1"));
				srcs.add(JSONUtil.getString(jo, "mpic2"));
				srcs.add(JSONUtil.getString(jo, "mpic3"));
				srcs.add(JSONUtil.getString(jo, "mpic4"));
				srcs.add(JSONUtil.getString(jo, "mpic5"));

				initPlayView(srcs, playView);

				View view= LayoutInflater.from(getBaseContext()).inflate(R.layout.view_pl_playview,null);
				ImageView img = (ImageView) view.findViewById(R.id.sex);
				TextView name = (TextView) view.findViewById(R.id.name);
				TextView age = (TextView) view.findViewById(R.id.age);
				
				ViewUtil.bindView(name, JSONUtil.getString(jo, "username"));
				ViewUtil.bindView(age, JSONUtil.getInt(jo, "age")+"岁");
				sex = JSONUtil.getInt(jo,"sex");
				switch (sex) {
				case 1:
					img.setBackgroundResource(R.drawable.man);
					break;

				case 2:
					img.setBackgroundResource(R.drawable.girl);
					break;
				}
				
				playView.addBottomNotChangeView(view);
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


//	// 初始化时间选择器
//	public void initWheelDate() {
//		mDateView = mInflater.inflate(R.layout.golf_wheel_choose_three, null);
//		// 年月日时间选择器
//		Date date = new Date();
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH) + 1;
//		int day = calendar.get(Calendar.DATE);
//		final AbWheelView mWheelViewY = (AbWheelView) mDateView
//				.findViewById(R.id.wheelView1);
//		final AbWheelView mWheelViewM = (AbWheelView) mDateView
//				.findViewById(R.id.wheelView2);
//		final AbWheelView mWheelViewD = (AbWheelView) mDateView
//				.findViewById(R.id.wheelView3);
//		Button okBtn = (Button) mDateView.findViewById(R.id.okBtn);
//		Button cancelBtn = (Button) mDateView.findViewById(R.id.cancelBtn);
//		mWheelViewY.setCenterSelectDrawable(this.getResources().getDrawable(
//				R.drawable.golf_wheel_select));
//		mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(
//				R.drawable.golf_wheel_select));
//		mWheelViewD.setCenterSelectDrawable(this.getResources().getDrawable(
//				R.drawable.golf_wheel_select));
//		AbWheelUtil.initWheelDatePicker(this, dateView, mWheelViewY,
//				mWheelViewM, mWheelViewD, okBtn, cancelBtn, year, month, day,
//				1900, 150, false, new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						String xinqiStr = AbDateUtil.getWeekNumber(dateView
//								.getText().toString(), "yy-MM-dd");
//						xinqi.setText(xinqiStr);
//						loadlistbytime();
//					}
//
//				});
//
//	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
//		if (view == dateView) {
//			showDialog(AbConstant.DIALOGBOTTOM, mDateView);
//		} 
//	else 
		
		if (view == introduce) {
			if (jo != null) {
				Intent i = new Intent(this, IntroduceDetail.class);
				 i.putExtra("name", JSONUtil.getString(jo, "name"));
				 i.putExtra("introduce", JSONUtil.getString(jo, "intro"));
				 startActivity(i);
			}
		} else if (view == tel) {
			if (jo != null) {
			}
		} else if (view == place) {
			if (jo != null) {
			}
		}

	}

//	private void loadlistbytime() {
//		 mAdapter.addparam("playdate", dateView.getText().toString());
//		 mAdapter.refreshDialog();
//
//	}

}
