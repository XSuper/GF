package com.bookingsystem.android.ui;

import java.util.List;

import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.ioc.annotation.Inject;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bookingsystem.android.R;
import com.bookingsystem.android.adapter.BrowseAdapter;
import com.bookingsystem.android.adapter.MBaseAdapter;
import com.bookingsystem.android.bean.Ground;

public class BrowseActivity extends MBaseActivity {
	
	@InjectView(id=R.id.blist)
	ListView list;
	List<Ground> datas;
	
	MBaseAdapter adapter; 
	
	@Inject
	DhDB db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_list);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		//mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);
		mAbTitleBar.setTitleText("浏览历史");
		datas =db.queryAll(Ground.class);
		adapter = new BrowseAdapter(this,datas);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i = new Intent(BrowseActivity.this, GroundDetail.class);
				i.putExtra("id", ((Ground)datas.get(arg2)).getClubid());
				i.putExtra("type", ((Ground)datas.get(arg2)).getType());
				startActivity(i);
				
			}
		});
	}

}
