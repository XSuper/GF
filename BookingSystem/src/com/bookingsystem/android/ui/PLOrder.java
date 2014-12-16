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

public class PLOrder extends MBaseActivity{
	
	@InjectView(id=R.id.img)
	ImageView img;
	@InjectView(id=R.id.name)
	TextView name;
	@InjectView(id=R.id.address)
	TextView address;
	@InjectView(id=R.id.date)
	TextView date;
	@InjectView(id=R.id.timestart)
	TextView timestart;
	@InjectView(id=R.id.timeend)
	TextView timeend;
	@InjectView(id=R.id.totalPrice)
	TextView totalPrice;
	@InjectView(id=R.id.payPrice)
	TextView payPrice;
	@InjectView(id=R.id.type)
	TextView type;
	@InjectView(id=R.id.tel)
	TextView tel;
	@InjectView(id=R.id.age)
	TextView age;
	
	@InjectView(id=R.id.submit)
	Button submit;

	@InjectExtra(name="data")
	String data;
	@InjectExtra(name="address")
	String maddress;
	@InjectExtra(name="age")
	String mage;
	@InjectExtra(name="type")
	String mType;
	@InjectExtra(name="mpic1")
	String pic1;
	@InjectExtra(name="mobile")
	String mobile;
	
	JSONObject jo;
	
	double price;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_order_pl);
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
		
		ViewUtil.bindView(name,JSONUtil.getString(jo, "username"));
		ViewUtil.bindView(address,maddress,"addresshide");
		ViewUtil.bindView(age,mage,"age");
		ViewUtil.bindView(img,pic1);
		ViewUtil.bindView(date,JSONUtil.getString(jo, "playdate"));
		ViewUtil.bindView(timestart,JSONUtil.getString(jo, "playstarttime"));
		ViewUtil.bindView(timeend,JSONUtil.getString(jo, "playendtime"));
		
		if("1".equals(mType)){
			price = JSONUtil.getDouble(jo, "clubprice");
			ViewUtil.bindView(type,"球场");
		}else if("2".equals(mType)){
			price = JSONUtil.getDouble(jo, "practiceprice");
			ViewUtil.bindView(type,"练习场");
		}
		ViewUtil.bindView(totalPrice,price,"price");
		ViewUtil.bindView(payPrice,price*Constant.RATE,"price");
		ViewUtil.bindView(tel,mobile,"telpass");
		
		
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DhNet net = new DhNet(Constant.BASEURL + "&a=addorder");
				net.addParam("id", JSONUtil.getString(jo, "id"));
				net.addParam("ordertype", 2);
				net.addParam("type", 4);
				net.addParam("sparringtype", mType);
				net.addParam("number", 1);
				net.addParam("totalprice",price);
				net.addParam("payprice",price*Constant.RATE);
				net.addParam("uid", MApplication.user.mid);
				net.addParam("token", MApplication.user.token);
				net.doGetInDialog(new NetTask(PLOrder.this) {
					@Override
					public void doInUI(Response response, Integer transfer) {
						if (response.success) {
							String id = JSONUtil.getString(
									response.jSONFromData(), "orderid");
							Intent i = new Intent(PLOrder.this,
									OrderChoiceAvtivity.class);
							i.putExtra("payprice",price*Constant.RATE+"");
							i.putExtra("orderid", id);
							i.putExtra("body", "预定陪练者");
							i.putExtra("subject", JSONUtil.getString(jo, "username"));
							startActivity(i);
						} else {
							showToast(response.msg);
						}
					}
				});

			}
		});
		 
	}
}
