package com.bookingsystem.android.ui;

import net.duohuo.dhroid.ioc.annotation.InjectExtra;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.util.ViewUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.util.AbStrUtil;
import com.bookingsystem.android.R;
import com.bookingsystem.android.util.StringUtils;

public class GroundInfoDetail extends MBaseActivity implements OnClickListener {
	@InjectView(id = R.id.model)
	TextView model;
	@InjectView(id = R.id.data)
	TextView data;
	@InjectView(id = R.id.createdate)
	TextView createdate;
	@InjectView(id = R.id.designer)
	TextView designer;
	@InjectView(id = R.id.area)
	TextView area;
	@InjectView(id = R.id.length)
	TextView length;
	@InjectView(id = R.id.greengrass)
	TextView greengrass;
	@InjectView(id = R.id.fairwaygrass)
	TextView fairwaygrass;
	@InjectView(id = R.id.address)
	TextView address;

	@InjectView(id = R.id.address_layout)
	LinearLayout address_layout;

	@InjectExtra(name = "model")
	String str_model;
	@InjectExtra(name = "data")
	String str_data;
	@InjectExtra(name = "createdate")
	String str_createdate;
	@InjectExtra(name = "designer")
	String str_designer;
	@InjectExtra(name = "area")
	String str_area;
	@InjectExtra(name = "length")
	String str_length;
	@InjectExtra(name = "greengrass")
	String str_greengrass;
	@InjectExtra(name = "fairwaygrass")
	String str_fairwaygrass;
	@InjectExtra(name = "address")
	String str_address;

	@InjectView(id = R.id.jianjie)
	TextView jianjie;
	@InjectExtra(name = "jianjie")
	String str_jianjie;

	@InjectView(id = R.id.peitao)
	TextView peitao;
	@InjectExtra(name = "peitao")
	String str_peitao;

	@InjectExtra(name = "name")
	String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_groundinfo);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setTitleText(name);
		// mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);

		ViewUtil.bindView(model, str_model);
		ViewUtil.bindView(data, str_data);
		ViewUtil.bindView(createdate, str_createdate);
		ViewUtil.bindView(designer, str_designer);
		ViewUtil.bindView(area, str_area);
		ViewUtil.bindView(length, str_length);
		ViewUtil.bindView(greengrass, str_greengrass);
		ViewUtil.bindView(fairwaygrass, str_fairwaygrass);

		str_peitao = StringUtils.parseMatching(str_peitao);
		ViewUtil.bindView(peitao, str_peitao);
		ViewUtil.bindView(jianjie, str_jianjie);
		ViewUtil.bindView(address, str_address);

		address_layout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.address_layout:
			Intent i = new Intent(this, MapActivity.class);
			if (!AbStrUtil.isEmpty(getIntent().getStringExtra("Lat"))) {
				i.putExtra("name", name);
				i.putExtra("Lat", getIntent().getStringExtra("Lat"));
				i.putExtra("Lon", getIntent().getStringExtra("Lon"));
				i.putExtra("place", getIntent().getStringExtra("place"));
				startActivity(i);
			} else {
				showToast("该球场坐标信息不全");
			}
			break;

		}

	}

}
