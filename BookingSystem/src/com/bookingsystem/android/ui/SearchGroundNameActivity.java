package com.bookingsystem.android.ui;

import java.util.List;

import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.ioc.annotation.Inject;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ab.util.AbStrUtil;
import com.bookingsystem.android.Constant;
import com.bookingsystem.android.R;
import com.bookingsystem.android.adapter.MBaseAdapter;
import com.bookingsystem.android.adapter.SearchNameAdapter;
import com.bookingsystem.android.bean.SearchName;

public class SearchGroundNameActivity extends MBaseActivity implements TextWatcher{ 
	
	@InjectView(id = R.id.back)
	ImageButton back;
	@InjectView(id=R.id.search)
	EditText searchView;
	
	@InjectView(id=R.id.listView)
	ListView list;
	@Inject
	DhDB db;
	DhNet net;
	MBaseAdapter adapter ;
	List<SearchName> names_history;
	List<SearchName> names;
	
	int type ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_groundname);
		 
		type = getIntent().getIntExtra("type", 1);
		names = db.queryList(SearchName.class,"type = ?",type);
		names_history = names;
		adapter = new SearchNameAdapter(this, names);
		
		list.setAdapter(adapter);
		searchView.addTextChangedListener(this);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SearchName name = names.get(arg2);
				name.setType(type);
				boolean save = true;
				for (int i = 0; i < names_history.size(); i++) {
					if(names_history.get(i).getName().equals(name.getName())){
						save = false;
					}
				}
				if(save){
					db.save(name);
				}
				Intent i = new Intent();
				i.putExtra("name", name.getName());
				setResult(10001, i);
				finish();
				
			}
		});
	}
	
	
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		String str = s.toString();
		if(AbStrUtil.isEmpty(str)){
			names = names_history;
			adapter.refresh(names);
		}else{
			net = new DhNet(Constant.BASEURL+"&a=clublist");
			net.addParam("name", str);
			net.addParam("type", type);
			net.addParam("pagesize",20);
			net.doGet(new NetTask(this) {
				@Override
				public void doInUI(Response response, Integer transfer) {
					// TODO Auto-generated method stub
					names = response.listFromData(SearchName.class);
					adapter.refresh(names);
				}
			});
		}
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	

}
