package com.bookingsystem.android.ui;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.adapter.INetAdapter.LoadSuccessCallBack;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import android.content.DialogInterface;
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
import com.bookingsystem.android.Constant;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;
import com.bookingsystem.android.adapter.MyOrderAdapter;
import com.bookingsystem.android.view.DelSlideListView;
import com.bookingsystem.android.view.ListViewonSingleTapUpListenner;
import com.bookingsystem.android.view.OnDeleteListioner;

public class MyOrderActivity extends BaseActivity implements OnDeleteListioner,ListViewonSingleTapUpListenner{
	@InjectView(id = R.id.ordertype)
	RadioGroup orderType;
	@InjectView(id = R.id.order_list)
	DelSlideListView orderList;
	
	
	@InjectView(id=R.id.emptyView)
	LinearLayout emptyView;
	
	@InjectView(id=R.id.back)
	TextView back;
	

	int status = 0;
	
	MyOrderAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_myorder);
		
		orderList.setDeleteListioner(this);
		orderList.setSingleTapUpListenner(this);
		
		
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setTitleText("我的订单");
		// mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);

		adapter = new MyOrderAdapter(Constant.BASEURL
				+ "&a=myorder", this, R.layout.item_order_ground);
		adapter.addparam("uid", MApplication.user.mid);
		adapter.addparam("token", MApplication.user.token);
		adapter.addparam("status", status);
		adapter.setOnDeleteListener(this);
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
						it1.putExtra("status", status);
						startActivity(it1);
						break;
					case 2:
						String id = JSONUtil.getString(jo, "id");
						Intent it = new Intent(getBaseContext(),
								LXCOrderDetail.class);
						it.putExtra("id", id);
						it.putExtra("pay", (status>=1));
						it.putExtra("status", status);
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
						it2.putExtra("status", status);
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
				adapter.showNext();
				orderList.setPullLoadEnable(adapter.hasMore());
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
				case R.id.radio_status3:
					status = 3;
					break;
				case R.id.radio_status4:
					status = 4;
					break;
				case R.id.radio_status5:
					status = 5;
					break;
				}
				adapter.addparam("status", status);
				adapter.refreshDialog();

			}
		});
	}
	
	/** ondelete */
	@Override
	public boolean isCandelete(int position) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void onDelete(final int position) {
		showDialog("提示", "确认删除该订单？", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				delete(position);
			}
		});
	}
	private void delete(final int position){
		String id ="";
		try {
			id = JSONUtil.getString(adapter.mVaules.get(position),"id");
		} catch (Exception e) {
		}
		DhNet net = new DhNet(Constant.BASEURL+"&a=delorder");
		net.addParam("orderid", id);
		net.addParam("uid", MApplication.user.mid);
		net.addParam("token", MApplication.user.token);
		net.doPostInDialog("正在删除请稍后", new NetTask(this) {
			
			@Override
			public void doInUI(Response response, Integer transfer) {
				int err = JSONUtil.getInt(response.jSON(),"err");
				if(err==0){
					showToast("删除成功");
					adapter.mVaules.remove(position);
					orderList.deleteItem();
					adapter.notifyDataSetChanged();
					orderList.deleteItem();
				}else{
					showToast(JSONUtil.getString(response.jSON(),"msg"));
				}
			}
		});
	}
	@Override
	public void onBack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSingleTapUp() {
		// TODO Auto-generated method stub
		
	}

}
