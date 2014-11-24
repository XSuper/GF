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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookingsystem.android.Constant;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;

public class LXCOrder extends MBaseActivity implements OnClickListener {

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
	@InjectView(id = R.id.tel)
	TextView tel;

	@InjectView(id = R.id.submit)
	Button submit;
	@InjectView(id = R.id.addbtn)
	Button add;
	@InjectView(id = R.id.delbtn)
	Button del;
	@InjectView(id = R.id.peopleCount)
	TextView pcount;
	@InjectView(id = R.id.danwei)
	TextView danwei;
	@InjectView(id = R.id.position)
	TextView position;

	JSONObject jo;
	int count = 1;
	double onepPrice;
	@InjectExtra(name = "data")
	String data;

	int type =2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_order_lxc);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setTitleText("提交订单");
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		try {
			jo = new JSONObject(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}

//		type = JSONUtil.getInt(jo, "type");
//		if(type==2)danwei.setText("RMB/盒");
		int pricetype = JSONUtil.getInt(jo, "priceunit");
		switch (pricetype) {
		case 1:
			danwei.setText("RMB/盒");
			break;
		case 2:
			danwei.setText("RMB/小时");
			break;

		default:
			break;
		}
		onepPrice = JSONUtil.getDouble(jo, "price");
		ViewUtil.bindView(name, JSONUtil.getString(jo, "name"));
		ViewUtil.bindView(position, JSONUtil.getString(jo, "position"));
		ViewUtil.bindView(address, JSONUtil.getString(jo, "address"), "address");
		ViewUtil.bindView(img, JSONUtil.getString(jo, "mpic1"));
		ViewUtil.bindView(onePrice, JSONUtil.getString(jo, "price"), "price");
//		ViewUtil.bindView(date, JSONUtil.getString(jo, "playdate"));
		ViewUtil.bindView(date,MApplication.searchDate);
		ViewUtil.bindView(payPrice, JSONUtil.getDouble(jo, "price")*Constant.RATE,
				"price");
		ViewUtil.bindView(cost, JSONUtil.getString(jo, "cost"), "cost");
		totalPrice.setText(" ¥" + count * onepPrice);

		add.setOnClickListener(this);
		del.setOnClickListener(this);

		ViewUtil.bindView(time,MApplication.searchTime);
		if (MApplication.islogin && MApplication.user != null
				&& MApplication.user.mobile != null) {
			ViewUtil.bindView(tel, MApplication.user.mobile);
		}

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DhNet net = new DhNet(Constant.BASEURL + "&a=addorder");
				net.addParam("id", JSONUtil.getString(jo, "id"));
				net.addParam("ordertype", 2);
				net.addParam("type", type);
				net.addParam("number", count);
				net.addParam("totalprice", count * onepPrice);
				net.addParam("payprice", count * onepPrice*Constant.RATE);
				net.addParam("uid", MApplication.user.mid);
				net.addParam("token", MApplication.user.token);
				net.doGetInDialog(new NetTask(LXCOrder.this) {
					@Override
					public void doInUI(Response response, Integer transfer) {
						if (response.success) {
							String id = JSONUtil.getString(
									response.jSONFromData(), "orderid");
							Intent i = new Intent(LXCOrder.this,
									OrderChoiceAvtivity.class);
							i.putExtra("payprice", count * onepPrice*Constant.RATE + "");
							i.putExtra("orderid", id);
							switch (type) {
							case 1:
								i.putExtra("body", "预定球场");
								break;
							case 2:
								i.putExtra("body", "预定练习场");
								break;
							}
							i.putExtra("subject", JSONUtil.getString(jo, "name"));
							startActivity(i);
						} else {
							showToast(response.msg);
						}
					}
				});
			}
		});

		Log.v("data", data);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.addbtn:
			pcount.setText(++count + "");
			totalPrice.setText(" ¥" + (double)((int)(count*onepPrice*100))/100);
			payPrice.setText(" ¥" + (double)((int)(count*onepPrice*1000*Constant.RATE))/1000);
			break;
		case R.id.delbtn:
			if (count <= 1) {
				showToast("最少一位打球者");
				return;
			}
			pcount.setText(--count + "");
			totalPrice.setText(" ¥" + (double)((int)(count*onepPrice*100))/100);
			payPrice.setText(" ¥" + (double)((int)(count*onepPrice*1000*Constant.RATE))/1000);
			break;
		}

	}
}
