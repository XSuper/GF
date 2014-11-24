package com.bookingsystem.android.adapter;

import java.util.List;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.util.ViewUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookingsystem.android.R;
import com.bookingsystem.android.bean.Ground;

public class BrowseAdapter extends MBaseAdapter{
	public BrowseAdapter(BaseActivity context, List<Ground> datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public View getView(int position,View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		viewHoder v = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_ground_jc, null);
			ImageView image = (ImageView)convertView.findViewById(R.id.image);
			TextView name = (TextView)convertView.findViewById(R.id.name);
			TextView type = (TextView)convertView.findViewById(R.id.type);
			TextView address = (TextView)convertView.findViewById(R.id.address);
			v = new viewHoder();
			v.image = image;
			v.name = name;
			v.type = type;
			v.address = address;
			convertView.setTag(v);
		}else{
			v= (viewHoder)convertView.getTag();
		}
		ViewUtil.bindView(v.image,((Ground)datas.get(position)).pic1);
		ViewUtil.bindView(v.name,((Ground)datas.get(position)).name);
		ViewUtil.bindView(v.type,((Ground)datas.get(position)).type,"groundType");
		ViewUtil.bindView(v.address,((Ground)datas.get(position)).address);
		
		
		return convertView;
	}
	
	class viewHoder{
		ImageView image;
		TextView name;
		TextView type;
		TextView address;
	}

}
