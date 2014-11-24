package com.bookingsystem.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.duohuo.dhroid.activity.BaseActivity;
import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.ioc.IocContainer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;
import com.bookingsystem.android.bean.Place;
import com.bookingsystem.android.util.CityUtil;

public class ProvinceAdapter extends BaseAdapter implements OnClickListener {
	List<String> datas;
	BaseActivity context;
	HashSet<String> isopen;
	HashMap<String, ArrayList<String>> mcache;
	DhDB db;

	public ProvinceAdapter(BaseActivity context, List<String> datas) {
		this.context = context;
		this.datas = datas;

		db = IocContainer.getShare().get(DhDB.class);
		isopen = new HashSet<String>();
		mcache = new HashMap<String, ArrayList<String>>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Mview v = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_province, null);
			TextView mprovince = (TextView) convertView
					.findViewById(R.id.province);
			LinearLayout layout = (LinearLayout) convertView
					.findViewById(R.id.cityLayout);
			v = new Mview();
			v.layout = layout;
			v.text = mprovince;

			v.text.setTag(v.layout);
			convertView.setTag(v);
		} else {
			v = (Mview) convertView.getTag();
		}

		final String pro = (String) (datas.toArray()[position]);
		v.text.setText(pro);
		v.text.setOnClickListener(this);
		v.layout.removeAllViews();

		// Drawable r =
		// context.getResources().getDrawable(R.drawable.city_default);
		// Drawable d =
		// context.getResources().getDrawable(R.drawable.city_select);

		if (isopen.contains(pro)) {
			// v.text.setCompoundDrawables(d, null, null, null);
			ArrayList<String> citys = null;
			if (mcache.containsKey(pro)) {
				citys = mcache.get(pro);
			} else {
				citys = CityUtil.getAllCityByProvince(pro);
				mcache.put(pro, citys);
			}

			for (int i = 0; i < citys.size(); i++) {
				final TextView mt = new TextView(context);
				LayoutParams lParams = new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				mt.setLayoutParams(lParams);
				mt.setText(citys.get(i));
				mt.setPadding(40, 15, 0, 15);
				mt.setTextSize(18);

				mt.setBackgroundResource(R.drawable.view_click_bg);
				mt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						cityClick(pro, mt);
					}
				});
				v.layout.addView(mt);
			}
		}
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.toArray()[position];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	class Mview {
		TextView text;
		LinearLayout layout;
	}

	private void cityClick(final String pro, final TextView mt) {
		Log.v(pro, mt.getText().toString());
		MApplication.sProvince = pro;
		MApplication.sCity = mt.getText().toString();
		Place p = db.load(Place.class, "GST");
		if (p == null) {
			p = new Place();
			p.pid = "GST";
			p.city = MApplication.sCity;
			p.province = MApplication.sProvince;
			db.save(p);
		} else {
			p.city = MApplication.sCity;
			p.province = MApplication.sProvince;
			db.update(p);
		}
		context.finish();
	}
	
	@Override
	public void onClick(View view) {

		final String pro = ((TextView) view).getText().toString();
		LinearLayout layout = (LinearLayout) view.getTag();
		if (isopen.contains(pro)) {
			layout.removeAllViews();
			isopen.remove(pro);
		} else {
			isopen.add(pro);
			ArrayList<String> citys = null;
			if (mcache.containsKey(pro)) {
				citys = mcache.get(pro);
			} else {
				citys = CityUtil.getAllCityByProvince(pro);
				mcache.put(pro, citys);
			}

			for (int i = 0; i < citys.size(); i++) {
				final TextView mt = new TextView(context);
				LayoutParams lParams = new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				mt.setLayoutParams(lParams);
				mt.setBackgroundResource(R.drawable.view_click_bg);
				mt.setText(citys.get(i));
				mt.setPadding(40, 15, 0, 15);
				mt.setTextSize(18);
				mt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						cityClick(pro, mt);

					}

					
				});
				layout.addView(mt);
			}
		}

	}

}
