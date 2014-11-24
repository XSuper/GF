package com.bookingsystem.android.adapter;

import java.util.List;

import net.duohuo.dhroid.activity.BaseActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MBaseAdapter extends BaseAdapter {
	public BaseActivity context;
	public List datas;

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent) ;
	
	/**
	 * 刷新列表
	 * @param mdata
	 */
	public void refresh(List mdata) {
		this.datas = mdata;
		notifyDataSetChanged();
	}

	
	
}
