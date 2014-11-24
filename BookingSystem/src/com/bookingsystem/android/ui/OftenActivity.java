package com.bookingsystem.android.ui;

import net.duohuo.dhroid.adapter.INetAdapter.LoadSuccessCallBack;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullListView;
import com.bookingsystem.android.Constant;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;

public class OftenActivity extends MBaseActivity {
	
	@InjectView(id=R.id.blist)
	AbPullListView mlist;
	
	
	NetJSONAdapter netadapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_refleshlist);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		mAbTitleBar.setTitleText("经常去的球场");
		
		netadapter = new NetJSONAdapter(Constant.BASEURL+"&a=orderlist", this, R.layout.item_ground_jc);
		netadapter.addparam("status", 1);
		netadapter.addparam("uid", MApplication.user.getMid());
		netadapter.addparam("token", MApplication.user.getToken());
		netadapter.setJump(GroundDetail.class, "clubid", "id");
		netadapter.addField("name", R.id.name);
		netadapter.addField("address", R.id.address);
		netadapter.addField("pic1", R.id.image);
		netadapter.addField("type", R.id.type,"groundType");
		mlist.setAdapter(netadapter);
		mlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				NetJSONAdapter adapter = netadapter;
				Intent it = new Intent(mlist.getContext(),
						adapter.getJumpClazz());
				adapter.getJumpKey();
				if (position - mlist.getHeaderViewsCount() + 1 > 0) {
					JSONObject jo = adapter.getTItem(position
							- mlist.getHeaderViewsCount());
					it.putExtra("listTemp",jo.toString());
					try {
						it.putExtra(
								adapter.getJumpAs(),
								JSONUtil.getString(jo,
										adapter.getJumpKey()));
					} catch (Exception e) {
					}
					it.putExtra("type", JSONUtil.getInt(jo, "type"));
					startActivity(it);
				}
			}
		});
		netadapter.refreshDialog();
		mlist.setAbOnListViewListener(new AbOnListViewListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				netadapter.refresh();
				netadapter.setOnLoadSuccess(new LoadSuccessCallBack() {
					
					@Override
					public void callBack(Response response) {
						// TODO Auto-generated method stub
						mlist.stopRefresh();
					}
				});
			}
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				netadapter.showNext();
			}
		});
	}

}
