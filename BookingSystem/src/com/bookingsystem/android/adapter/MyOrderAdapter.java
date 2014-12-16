package com.bookingsystem.android.adapter;

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
import com.bookingsystem.android.ui.LXCOrderDetail;
import com.bookingsystem.android.ui.OrderChoiceAvtivity;
import com.bookingsystem.android.ui.PLOrderDetail;
import com.bookingsystem.android.ui.QCOrderDetail;
import com.bookingsystem.android.ui.RefundActivity;
import com.bookingsystem.android.view.OnDeleteListioner;

public class MyOrderAdapter extends NetJSONAdapter implements OnClickListener {

	protected static final String Button = null;
	private LayoutInflater mInflater;
	Context context;
	OnDeleteListioner deleteListioner;

	public MyOrderAdapter(String api, Context context, int mResource) {
		super(api, context, mResource);
		mInflater = LayoutInflater.from(context.getApplicationContext());
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void setOnDeleteListener(OnDeleteListioner deleteListioner) {
		this.deleteListioner = deleteListioner;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final JSONObject jsonObject = mVaules.get(position);
		Integer type = JSONUtil.getInt(jsonObject, "type");
		switch (type) {
		case 1:// 球场
		case 2:// 练习场
			ViewHoderGround ground = null;
			if (convertView != null
					&& convertView.getTag(R.layout.item_order_ground) != null) {
				ground = (ViewHoderGround) convertView
						.getTag(R.layout.item_order_ground);
			} else {
				convertView = mInflater.inflate(R.layout.item_order_ground,
						null);
				ImageView img = (ImageView) convertView
						.findViewById(R.id.image);
				TextView name = (TextView) convertView.findViewById(R.id.name);
				TextView date = (TextView) convertView.findViewById(R.id.date);
				TextView price = (TextView) convertView
						.findViewById(R.id.price);
				TextView ordernum = (TextView) convertView
						.findViewById(R.id.ordernum);
				Button orderBtn = (Button) convertView
						.findViewById(R.id.orderbtn);
				Button del = (Button) convertView
						.findViewById(R.id.delete_action);
				ground = new ViewHoderGround(img, name, date, price, ordernum,
						orderBtn, del);
				convertView.setTag(R.layout.item_order_ground, ground);
				orderBtn.setOnClickListener(this);
				del.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						deleteListioner.onDelete(position);
					}
				});
			}
			ground.orderBtn.setTag(jsonObject);
			ViewUtil.bindView(ground.img,
					JSONUtil.getString(jsonObject, "logo"));
			ViewUtil.bindView(ground.name,
					JSONUtil.getString(jsonObject, "name"));
			ViewUtil.bindView(ground.date,
					JSONUtil.getString(jsonObject, "dateline"));
			ViewUtil.bindView(ground.price,
					JSONUtil.getString(jsonObject, "totalprice"), "price");
			ViewUtil.bindView(ground.ordernum,
					JSONUtil.getString(jsonObject, "ordernum"));

			setOrderBtn(jsonObject, ground.orderBtn);
			break;
		case 3:// 团购
			break;
		case 4:// 陪打
			ViewHoderPL pl = null;
			if (convertView != null
					&& convertView.getTag(R.layout.item_order_pl) != null) {
				pl = (ViewHoderPL) convertView.getTag(R.layout.item_order_pl);
			} else {
				convertView = mInflater.inflate(R.layout.item_order_pl, null);
				ImageView img = (ImageView) convertView
						.findViewById(R.id.image);
				ImageView sex = (ImageView) convertView.findViewById(R.id.sex);
				TextView name = (TextView) convertView.findViewById(R.id.name);
				TextView time = (TextView) convertView.findViewById(R.id.time);
				TextView age = (TextView) convertView.findViewById(R.id.age);
				TextView price = (TextView) convertView
						.findViewById(R.id.price);
				TextView ordernum = (TextView) convertView
						.findViewById(R.id.ordernum);
				Button orderBtn = (Button) convertView
						.findViewById(R.id.orderbtn);
				Button del = (Button) convertView
						.findViewById(R.id.delete_action);
				pl = new ViewHoderPL(img, sex, name, age, time, price,
						ordernum, orderBtn, del);
				convertView.setTag(R.layout.item_order_pl);
				orderBtn.setOnClickListener(this);

				del.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						deleteListioner.onDelete(position);
					}
				});
			}
			pl.orderBtn.setTag(jsonObject);
			ViewUtil.bindView(pl.img, JSONUtil.getString(jsonObject, "mpic1"));

			int sexnum = JSONUtil.getInt(jsonObject, "sex");
			switch (sexnum) {
			case 1:// men
				pl.sex.setImageResource(R.drawable.man);
				break;
			case 2:// girl
				pl.sex.setImageResource(R.drawable.girl);
				break;
			}
			ViewUtil.bindView(pl.name, JSONUtil.getString(jsonObject, "name"));
			ViewUtil.bindView(pl.age, JSONUtil.getString(jsonObject, "age"),
					"age");
			ViewUtil.bindView(pl.time,
					JSONUtil.getString(jsonObject, "dateline"));
			ViewUtil.bindView(pl.price,
					JSONUtil.getString(jsonObject, "totalprice"), "price");
			ViewUtil.bindView(pl.ordernum,
					JSONUtil.getString(jsonObject, "ordernum"));
			setOrderBtn(jsonObject, pl.orderBtn);
		}

		return convertView;
	}

	public void setOrderBtn(JSONObject jsonObject,Button orderBtn){
		int ostate = JSONUtil.getInt(jsonObject, "status");
		switch (ostate) {
		case 0:
		case 1:
			orderBtn.setEnabled(true);
			ViewUtil.bindView(orderBtn,
					JSONUtil.getString(jsonObject, "status"),
					"orderstatus-btn");
			break;
		case 3:
		case 4:
			orderBtn.setEnabled(false);
			ViewUtil.bindView(orderBtn,
					JSONUtil.getString(jsonObject, "status"),
					"orderstatus-btn");
			break;
		case 5:
			int refundstatus = JSONUtil.getInt(jsonObject, "refundstatus");
			switch (refundstatus) {
			case 0:
				orderBtn.setEnabled(true);
				ViewUtil.bindView(orderBtn, "未通过");
				break;
			case 1:
				ViewUtil.bindView(orderBtn, "等待退款");
				orderBtn.setEnabled(false);
				break;
			}
			break;
		}
		
	}
	
	class ViewHoderGround {
		ImageView img;
		TextView name;
		TextView date;
		TextView price;
		TextView ordernum;
		Button orderBtn;
		Button del;

		public ViewHoderGround(ImageView img, TextView name, TextView date,
				TextView price, TextView ordernum, Button orderBtn, Button del) {
			super();
			this.img = img;
			this.name = name;
			this.date = date;
			this.price = price;
			this.ordernum = ordernum;
			this.orderBtn = orderBtn;
			this.del = del;
		}

	}

	class ViewHoderPL {
		ImageView img;
		ImageView sex;
		TextView name;
		TextView age;
		TextView time;
		TextView price;
		TextView ordernum;
		Button orderBtn;
		Button del;

		public ViewHoderPL(ImageView img, ImageView sex, TextView name,
				TextView age, TextView time, TextView price, TextView ordernum,
				Button orderBtn, Button del) {
			super();
			this.img = img;
			this.sex = sex;
			this.name = name;
			this.age = age;
			this.time = time;
			this.price = price;
			this.ordernum = ordernum;
			this.orderBtn = orderBtn;
			this.del = del;
		}

	}

	// public void orderDel(String id,final int positon) {
	//
	// AlertDialog.Builder builder = new AlertDialog.Builder(context);
	// builder.setTitle("删除订单");
	// builder.setMessage("确认删除该订单？");
	// builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// mVaules.remove(positon);
	// notifyDataSetInvalidated();
	// }
	// });
	// builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// }
	// });
	// builder.create().show();
	// }

	// submit 按钮点击事件
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		String str = ((Button) arg0).getText().toString();
		JSONObject jsonObject = (JSONObject) arg0.getTag();
		if ("立即付款".equals(str)) {
			Intent i = new Intent(context, OrderChoiceAvtivity.class);
			i.putExtra("payprice", JSONUtil.getString(jsonObject, "payprice"));
			i.putExtra("orderid", JSONUtil.getString(jsonObject, "id"));
			int type = JSONUtil.getInt(jsonObject, "type", -2);
			// 1:球场 2:练习场 3：团购商品 4：陪打
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
		} else if ("申请退款".equals(str) || "未通过".equals(str)) {
			// ((BaseActivity)context).showDialog("申请退款", "请登陆网站进行退款操作！");
			Intent intent = new Intent(context, RefundActivity.class);
			intent.putExtra("orderid", JSONUtil.getString(jsonObject, "id"));
			intent.putExtra("money", JSONUtil.getDouble(jsonObject, "payprice"));

			context.startActivity(intent);
		}
	}

	public void itemClick(JSONObject jo) {
		
			int type = JSONUtil.getInt(jo, "type");
			int status = JSONUtil.getInt(jo, "status");
			switch (type) {
			case 1:
				String id1 = JSONUtil.getString(jo, "id");
				Intent it1 = new Intent(context, QCOrderDetail.class);
				it1.putExtra("id", id1);
				it1.putExtra("pay", (status >= 1));
				context.startActivity(it1);
				break;
			case 2:
				String id = JSONUtil.getString(jo, "id");
				Intent it = new Intent(context, LXCOrderDetail.class);
				it.putExtra("id", id);
				it.putExtra("pay", (status >= 1));
				context.startActivity(it);
				break;
			case 3:
				break;
			case 4:
				String sparringid = JSONUtil.getString(jo, "id");
				Intent it2 = new Intent(context, PLOrderDetail.class);
				it2.putExtra("id", sparringid);
				it2.putExtra("sex", JSONUtil.getInt(jo, "sex"));
				// 判断是否支付
				if (status >= 1) {
					it2.putExtra("pay", true);
				} else {
					it2.putExtra("pay", false);
				}
				context.startActivity(it2);

				break;

			}
	}

}
