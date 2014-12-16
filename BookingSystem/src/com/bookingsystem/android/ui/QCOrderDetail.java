package com.bookingsystem.android.ui;

import org.json.JSONException;
import org.json.JSONObject;

import net.duohuo.dhroid.ioc.annotation.InjectExtra;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
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
import com.google.gson.JsonObject;

public class QCOrderDetail extends MBaseActivity {

	@InjectView(id = R.id.img)
	ImageView img;
	@InjectView(id = R.id.name)
	TextView name;
	@InjectView(id = R.id.address)
	TextView address;
	@InjectView(id = R.id.date)
	TextView date;
	@InjectView(id = R.id.time)
	TextView time;
	@InjectView(id = R.id.totalPrice)
	TextView totalPrice;
	@InjectView(id = R.id.onePrice)
	TextView onePrice;
	@InjectView(id = R.id.payPrice)
	TextView payPrice;
	@InjectView(id = R.id.cost)
	TextView cost;
	
	@InjectView(id = R.id.ordernum)
	TextView ordernum;
	@InjectView(id = R.id.ordertime)
	TextView ordertime;

	@InjectView(id = R.id.submit)
	Button submit;
	
	@InjectView(id = R.id.peopleCount)
	TextView pcount;
	@InjectView(id = R.id.position)
	TextView position;
	@InjectView(id = R.id.prompttxt)
	TextView prompt;
	

	@InjectExtra(name="id")String id;
	@InjectExtra(name="pay")
	boolean pay;
	int status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_order_qcdetail);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setTitleText("订单详情");
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		status = getIntent().getIntExtra("status",0);
		loadData();
	}
	JSONObject jo;
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

				pay = JSONUtil.getInt(jo, "status")>=1;
				if (pay) {
					submit.setVisibility(View.GONE);
				} else {
					submit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub

							Intent i = new Intent(getBaseContext(),
									OrderChoiceAvtivity.class);
							i.putExtra("payprice",
									JSONUtil.getString(jo, "payprice"));
							i.putExtra("orderid", id);
							i.putExtra("body", "预定球场");
							i.putExtra("subject",
									JSONUtil.getString(jo, "name"));
							startActivity(i);

						}
					});
				}
				ViewUtil.bindView(name, JSONUtil.getString(jo, "name"));
				ViewUtil.bindView(ordernum, JSONUtil.getString(jo, "ordernum"));
				ViewUtil.bindView(ordertime, JSONUtil.getString(jo, "dateline"));
				ViewUtil.bindView(address, JSONUtil.getString(jo, "address"));
				ViewUtil.bindView(img, JSONUtil.getString(jo, "mpic1"));
				ViewUtil.bindView(date, JSONUtil.getString(jo, "playdate"));
				ViewUtil.bindView(time, JSONUtil.getString(jo, "playtime"));
				ViewUtil.bindView(pcount, JSONUtil.getString(jo, "number"));
				ViewUtil.bindView(cost, JSONUtil.getString(jo, "cost"),"cost");

				ViewUtil.bindView(totalPrice,
						JSONUtil.getString(jo, "totalprice"), "price");
				ViewUtil.bindView(payPrice, JSONUtil.getString(jo, "payprice"),
						"price");
				ViewUtil.bindView(onePrice, JSONUtil.getString(jo, "price"),
						"price");
				switch (status) {
				case 0:
				case 1:
				case 2:
					ViewUtil.bindView(prompt, "预  付  款");
					break;
				case 3:
				case 4:
				case 5:
					ViewUtil.bindView(prompt, "退款金额");
					break;

				}

			}
		});

	}
}
