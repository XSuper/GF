package com.bookingsystem.android.ui;

import net.duohuo.dhroid.ioc.annotation.InjectExtra;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookingsystem.android.Constant;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;

public class PLOrderDetail extends MBaseActivity {

	@InjectView(id = R.id.img)
	ImageView img;
	@InjectView(id = R.id.ordernum)
	TextView ordernum;
	@InjectView(id = R.id.ordertime)
	TextView ordertime;
	@InjectView(id = R.id.name)
	TextView name;
	@InjectView(id = R.id.address)
	TextView address;
	@InjectView(id = R.id.date)
	TextView date;
	@InjectView(id = R.id.timestart)
	TextView timestart;
	@InjectView(id = R.id.timeend)
	TextView timeend;
	@InjectView(id = R.id.totalPrice)
	TextView totalPrice;
	@InjectView(id = R.id.payPrice)
	TextView payPrice;
	@InjectView(id = R.id.type)
	TextView type;
	@InjectView(id = R.id.tel)
	TextView tel;
	@InjectView(id = R.id.age)
	TextView age;
	@InjectView(id = R.id.submit)
	Button submit;

	@InjectExtra(name = "id")
	String id;
	@InjectExtra(name = "pay")
	boolean pay;

	// @InjectExtra(name="type")String mType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_order_pldetail);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setTitleText("订单详情");
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);

		loadData();

	}

	JSONObject jo = null;

	private void loadData() {
		// TODO Auto-generated method stub
		DhNet net = new DhNet(Constant.BASEURL + "&a=detailorder");
		net.addParam("orderid", id);
		net.addParam("uid", MApplication.user.mid);
		net.addParam("token", MApplication.user.token);
		net.doGetInDialog(new NetTask(this) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub

				try {
					jo = response.jSONArrayFromData().getJSONObject(0);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (pay) {

					ViewUtil.bindView(address,
							JSONUtil.getString(jo, "address"));
					ViewUtil.bindView(tel, JSONUtil.getString(jo, "mobile"));
					submit.setVisibility(View.GONE);
				} else {
					ViewUtil.bindView(address,
							JSONUtil.getString(jo, "address"), "addresshide");
					ViewUtil.bindView(tel, JSONUtil.getString(jo, "mobile"),
							"telpass");
					submit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub

							Intent i = new Intent(getBaseContext(),
									OrderChoiceAvtivity.class);
							i.putExtra("payprice",
									JSONUtil.getString(jo, "payprice"));
							i.putExtra("orderid", id);
							i.putExtra("body", "预约陪练");
							i.putExtra("subject",
									JSONUtil.getString(jo, "name"));
							startActivity(i);

						}
					});
				}
				ViewUtil.bindView(name, JSONUtil.getString(jo, "name"));
				ViewUtil.bindView(ordernum, JSONUtil.getString(jo, "ordernum"));
				ViewUtil.bindView(ordertime, JSONUtil.getString(jo, "dateline"));

				ViewUtil.bindView(age, JSONUtil.getString(jo, "sex"), "age");
				ViewUtil.bindView(img, JSONUtil.getString(jo, "mpic1"));
				ViewUtil.bindView(date, JSONUtil.getString(jo, "playdate"));
				ViewUtil.bindView(timestart,
						JSONUtil.getString(jo, "playstarttime"));
				ViewUtil.bindView(timeend,
						JSONUtil.getString(jo, "playendtime"));

				//修改位类别
				if ("1".equals(JSONUtil.getString(jo, "sparringtype"))) {
					ViewUtil.bindView(type, "球场");
				} else if ("2".equals(JSONUtil.getString(jo, "sparringtype"))) {
					ViewUtil.bindView(type, "练习场");
				}
				ViewUtil.bindView(totalPrice,
						JSONUtil.getString(jo, "totalprice"), "price");
				ViewUtil.bindView(payPrice, JSONUtil.getString(jo, "payprice"),
						"price");

			}
		});

	}
}
