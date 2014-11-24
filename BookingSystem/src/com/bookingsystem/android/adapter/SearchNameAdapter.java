package com.bookingsystem.android.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookingsystem.android.R;
import com.bookingsystem.android.bean.SearchName;
import com.bookingsystem.android.ui.MBaseActivity;

public class SearchNameAdapter extends MBaseAdapter{
	public SearchNameAdapter(MBaseActivity activity,List<SearchName> names){
		// TODO Auto-generated constructor stub
		context = activity;
		datas = names;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView name = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_ground_name_search, null);
			name = (TextView) convertView.findViewById(android.R.id.text1);
			convertView.setTag(name);
			
		}
		name = (TextView) convertView.getTag();
		SearchName sname = (SearchName) datas.get(position);
		name.setText(sname.getName());
		return convertView;
	}

}
