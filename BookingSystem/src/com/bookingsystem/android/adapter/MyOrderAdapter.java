package com.bookingsystem.android.adapter;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookingsystem.android.R;
import com.bookingsystem.android.ui.OrderChoiceAvtivity;
import com.bookingsystem.android.util.StringUtils;

public class MyOrderAdapter extends NetJSONAdapter implements OnClickListener{
	
	
	protected static final String Button = null;
	private LayoutInflater mInflater;
	Context context;


	public MyOrderAdapter(String api, Context context, int mResource) {
		super(api, context, mResource);
		mInflater = LayoutInflater.from(context.getApplicationContext());
		// TODO Auto-generated constructor stub
		this.context = context;
		
		
		
	}


	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final
		JSONObject jsonObject = mVaules.get(position);
		Integer type = JSONUtil.getInt(jsonObject, "type");
		switch (type) {
		case 1://球场
		case 2://练习场
			ViewHoderGround ground = null;
			if(convertView!=null&&convertView.getTag(R.layout.item_order_ground)!=null){
				ground = (ViewHoderGround) convertView.getTag(R.layout.item_order_ground);
			}else{
				convertView= mInflater.inflate(R.layout.item_order_ground, null);
				ImageView img  = (ImageView) convertView.findViewById(R.id.image);
				TextView name = (TextView) convertView.findViewById(R.id.name);
				TextView date = (TextView) convertView.findViewById(R.id.date);
				TextView price = (TextView) convertView.findViewById(R.id.price);
				TextView ordernum = (TextView) convertView.findViewById(R.id.ordernum);
				Button orderBtn =  (Button) convertView.findViewById(R.id.orderbtn);
				ground = new ViewHoderGround(img, name, date, price, ordernum,orderBtn);
				convertView.setTag(R.layout.item_order_ground,ground);
				orderBtn.setOnClickListener(this);
			}
			ground.orderBtn.setTag(jsonObject);
			ViewUtil.bindView(ground.img, JSONUtil.getString(jsonObject, "mpic1"));
			ViewUtil.bindView(ground.name, JSONUtil.getString(jsonObject, "name"));
			ViewUtil.bindView(ground.date, JSONUtil.getString(jsonObject, "dateline"));
			ViewUtil.bindView(ground.price, JSONUtil.getString(jsonObject, "totalprice"),"price");
			ViewUtil.bindView(ground.ordernum, JSONUtil.getString(jsonObject, "ordernum"));
			ViewUtil.bindView(ground.orderBtn, JSONUtil.getString(jsonObject, "status"),"orderstatus-btn");
			
			
			break;
		case 3://团购
			break;
		case 4://陪打
			ViewHoderPL pl = null;
			if(convertView!=null&&convertView.getTag(R.layout.item_order_pl)!=null){
				pl = (ViewHoderPL) convertView.getTag(R.layout.item_order_pl);
			}else{
				convertView= mInflater.inflate(R.layout.item_order_pl, null);
				ImageView img  = (ImageView) convertView.findViewById(R.id.image);
				ImageView sex  = (ImageView) convertView.findViewById(R.id.sex);
				TextView name = (TextView) convertView.findViewById(R.id.name);
				TextView time = (TextView) convertView.findViewById(R.id.time);
				TextView age = (TextView) convertView.findViewById(R.id.age);
				TextView price = (TextView) convertView.findViewById(R.id.price);
				TextView ordernum = (TextView) convertView.findViewById(R.id.ordernum);
				Button orderBtn =  (Button) convertView.findViewById(R.id.orderbtn);
				pl = new ViewHoderPL(img, sex, name, age, time, price, ordernum, orderBtn);
				convertView.setTag(R.layout.item_order_pl);
				orderBtn.setOnClickListener(this);
			}
			pl.orderBtn.setTag(jsonObject);
			ViewUtil.bindView(pl.img, JSONUtil.getString(jsonObject, "mpic1"));
			
			int sexnum = JSONUtil.getInt(jsonObject, "sex");
			switch (sexnum) {
			case 1:// men
				pl.sex.setImageResource(R.drawable.man);
				break;
			case 2://girl
				pl.sex.setImageResource(R.drawable.girl);
				break;
			}
			ViewUtil.bindView(pl.name, JSONUtil.getString(jsonObject, "name"));
			ViewUtil.bindView(pl.age, JSONUtil.getString(jsonObject, "age"),"age");
			ViewUtil.bindView(pl.time, JSONUtil.getString(jsonObject, "dateline"));
			ViewUtil.bindView(pl.price, JSONUtil.getString(jsonObject, "totalprice"),"price");
			ViewUtil.bindView(pl.ordernum, JSONUtil.getString(jsonObject, "ordernum"));
			ViewUtil.bindView(pl.orderBtn, JSONUtil.getString(jsonObject, "status"),"orderstatus-btn");
			
			break;
		}
		return convertView;
	}
	
	class ViewHoderGround{
		ImageView img;
		TextView name;
		TextView date;
		TextView price;
		TextView ordernum;
		Button orderBtn;
		public ViewHoderGround(ImageView img, TextView name, TextView date, TextView price, 
				TextView ordernum, Button orderBtn) {
			super();
			this.img = img;
			this.name = name;
			this.date = date;
			this.price = price;
			this.ordernum = ordernum;
			this.orderBtn = orderBtn;
		}
		
		
	}
	class ViewHoderPL{
		ImageView img;
		ImageView sex;
		TextView name;
		TextView age;
		TextView time;
		TextView price;
		TextView ordernum;
		Button orderBtn;
		public ViewHoderPL(ImageView img, ImageView sex, TextView name,
				TextView age, TextView time, TextView price, TextView ordernum,
				android.widget.Button orderBtn) {
			super();
			this.img = img;
			this.sex = sex;
			this.name = name;
			this.age = age;
			this.time = time;
			this.price = price;
			this.ordernum = ordernum;
			this.orderBtn = orderBtn;
		}
		
		
	}
	
	//submit 按钮点击事件
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		String str = ((Button)arg0).getText().toString();
		JSONObject jsonObject =  (JSONObject) arg0.getTag();
		if("立即付款".equals(str)){
			Intent i = new 
					Intent(context,OrderChoiceAvtivity.class);
			i.putExtra("payprice",JSONUtil.getString(jsonObject, "payprice"));
			i.putExtra("orderid",JSONUtil.getString(jsonObject, "id"));
			int type = JSONUtil.getInt(jsonObject, "type",-2);
			// 1:球场 2:练习场  3：团购商品 4：陪打
			switch (type) {
			case 1:
				
				i.putExtra("body", "预定球场");
				break;
			case 2:
				i.putExtra("body", "预定练习场");
				
				break;
			case 3:
				i.putExtra("body", "商品团购");
				
				break;
			case 4:
				i.putExtra("body", "预约陪练");
				break;
			default:
				i.putExtra("body", "订单支付");
				break;
			}
			i.putExtra("subject", JSONUtil.getString(jsonObject, "name"));
			context.startActivity(i);
		}else if("申请退款".equals(str)){
			((BaseActivity)context).showDialog("申请退款", "请登陆网站进行退款操作！");
		}
	}
	

}
