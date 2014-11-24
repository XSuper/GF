package com.bookingsystem.android.ui;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.adapter.INetAdapter.LoadSuccessCallBack;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullListView;
import com.bookingsystem.android.Constant;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;
import com.bookingsystem.android.adapter.MyOrderAdapter;

public class MyOrderActivity extends BaseActivity {
	@InjectView(id = R.id.ordertype)
	RadioGroup orderType;
	@InjectView(id = R.id.order_list)
	AbPullListView orderList;
	
	
	@InjectView(id=R.id.emptyView)
	LinearLayout emptyView;
	
	@InjectView(id=R.id.back)
	TextView back;
	

	int status = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_myorder);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setTitleText("我的订单");
		// mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);

		final MyOrderAdapter adapter = new MyOrderAdapter(Constant.BASEURL
				+ "&a=myorder", this, R.layout.item_order_ground);
		adapter.addparam("uid", MApplication.user.mid);
		adapter.addparam("token", MApplication.user.token);
		adapter.addparam("status", status);
		adapter.setOnLoadSuccess(new LoadSuccessCallBack() {

			@Override
			public void callBack(Response response) {
				// TODO Auto-generated method stub
				orderList.stopRefresh();
				orderList.stopLoadMore();
				
				if((adapter.mVaules==null||adapter.mVaules.size()==0)&&status==0){
					orderList.setVisibility(View.GONE);
					emptyView.setVisibility(View.VISIBLE);
					
					back.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							finish();
						}
					});
				}else{
					emptyView.setVisibility(View.GONE);
					orderList.setVisibility(View.VISIBLE);
				}
			}
		});
		orderList.setAdapter(adapter);
		adapter.refresh();

		orderList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				if (position - orderList.getHeaderViewsCount() + 1 > 0) {
					JSONObject jo = adapter.getTItem(position
							- orderList.getHeaderViewsCount());
					int type = JSONUtil.getInt(jo, "type");
					int status = JSONUtil.getInt(jo, "status");
					switch (type) {
					case 1:
						String id1 = JSONUtil.getString(jo, "id");
						Intent it1 = new Intent(getBaseContext(),
								QCOrderDetail.class);
						it1.putExtra("id", id1);
						it1.putExtra("pay", (status>=1));
						startActivity(it1);
						break;
					case 2:
						String id = JSONUtil.getString(jo, "id");
						Intent it = new Intent(getBaseContext(),
								LXCOrderDetail.class);
						it.putExtra("id", id);
						it.putExtra("pay", (status>=1));
						startActivity(it);
						break;
					case 3:
						break;
					case 4:
						String sparringid = JSONUtil.getString(jo, "id");
						Intent it2 = new Intent(getBaseContext(),
								PLOrderDetail.class);
						it2.putExtra("id", sparringid);
						it2.putExtra("sex", JSONUtil.getInt(jo,"sex"));
						// 判断是否支付
						if (status >= 1) {
							it2.putExtra("pay", true);
						} else {
							it2.putExtra("pay", false);
						}
						startActivity(it2);

						break;

					}

				}
			}
		});

		AbOnListViewListener listViewListener = new AbOnListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				adapter.refresh();
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				adapter.showNext();
			}
		};
		orderList.setAbOnListViewListener(listViewListener);
		orderType.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.radio_notpay:
					status = 0;
					break;
				case R.id.radio_pay:
					status = 1;
					break;
				}
				adapter.addparam("status", status);
				adapter.refreshDialog();

			}
		});
	}

}
