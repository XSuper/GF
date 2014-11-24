package com.bookingsystem.android.ui;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.ioc.annotation.InjectExtra;
import net.duohuo.dhroid.net.JSONUtil;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ab.util.AbStrUtil;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullListView;
import com.bookingsystem.android.Constant;
import com.bookingsystem.android.MApplication;
import com.bookingsystem.android.R;
import com.bookingsystem.android.adapter.MBeanAdapter;
import com.bookingsystem.android.adapter.MNetAdapter;
import com.bookingsystem.android.bean.Ground;
import com.bookingsystem.android.imp.MNetCallBack;
import com.bookingsystem.android.util.StringUtils;
import com.umeng.analytics.MobclickAgent;

public class LXCActivity extends MBaseActivity implements OnClickListener {
	MNetAdapter mnet;// 访问网络集合对象
	boolean loadfirst = true;// 是否是第一次加载 或者是刷新

	RadioGroup paixu;
	RadioButton p_price, p_time, p_destance;
	int paixuId = 3;// 默认按距离排序 2 为价格排序
	String desctype = "ASC";// 默认shenqu DESC 降序
	int checkid = R.id.paixu_time;
	boolean behandChange = false;

	// -----------列表页属性
	AbPullListView groundList;
	MBeanAdapter listAdapter;

	// 搜索条件
	@InjectExtra(name = "name")
	String name;
	@InjectExtra(name = "date")
	String date;
	@InjectExtra(name = "time")
	String time;

	public String loadCity = MApplication.sCity = AbStrUtil
			.isEmpty(MApplication.sCity) ? MApplication.city
			: MApplication.sCity;// 标示加载数据用的城市

	DhDB db;
	private List<JSONObject> jsonlist;
	private Button RightBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_lxc);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(20, 0, 0, 0);
		mAbTitleBar.setTitleText("练习场");
		// mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);

		initTitleRightLayout();
		db = IocContainer.getShare().get(DhDB.class);
		init();
		loadData();
	}

	private void initTitleRightLayout() {
		mAbTitleBar.clearRightView();
		View v = mInflater.inflate(R.layout.view_top_right, null);
		RightBtn = (Button) v.findViewById(R.id.tbtn);
		mAbTitleBar.addRightView(v);
		RightBtn.setText(StringUtils.isEmpty(MApplication.sCity) ? "位置"
				: MApplication.sCity);
		RightBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(LXCActivity.this,
						ChoiceCityActivity.class);
				startActivity(i);
			}

		});
	}

	private void init() {
		groundList = (AbPullListView) findViewById(R.id.ground_list);
		paixu = (RadioGroup) findViewById(R.id.paixu);

		p_destance = (RadioButton) findViewById(R.id.paixu_destance);
		p_price = (RadioButton) findViewById(R.id.paixu_price);
		p_time = (RadioButton) findViewById(R.id.paixu_time);
		p_destance.setOnClickListener(this);
		p_price.setOnClickListener(this);
		p_time.setOnClickListener(this);
		paixu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int id) {
				// TODO Auto-generated method stub
				checkid = id;
				desctype = "ASC";
				behandChange = true;
				p_destance.setButtonDrawable(R.drawable.paixu_radio);
				p_price.setButtonDrawable(R.drawable.paixu_radio);
				p_time.setButtonDrawable(R.drawable.paixu_radio);
				switch (id) {
				case R.id.paixu_price:
					paixuId = 2;// 默认按时间排序 2 为价格排序
					mnet.addparam("ordertype", paixuId);
					mnet.addparam("desctype", desctype);
					loadfirst = true;
					mnet.refreshDialog();
					break;

				case R.id.paixu_time:
					paixuId = 1;
					mnet.addparam("ordertype", paixuId);
					mnet.addparam("desctype", desctype);
					loadfirst = true;
					mnet.refreshDialog();
					break;
				case R.id.paixu_destance:
					paixuId = 3;
					mnet.addparam("ordertype", paixuId);
					mnet.addparam("desctype", desctype);
					loadfirst = true;
					mnet.refreshDialog();
					break;
				}

			}
		});
		jsonlist = new ArrayList<JSONObject>();

		listAdapter = new MBeanAdapter(this, R.layout.item_ground_lxc);
	}

	/**
	 * 加载数据
	 */
	public void loadData() {

		mnet = new MNetAdapter(Constant.BASEURL, this, new MNetCallBack() {
			@Override
			public void success(List<JSONObject> list) {
				// TODO Auto-generated method stub
				if (list == null || list.size() == 0) {
					showToast("您搜索的城市暂无球场，换个城市看看！");
				} else {
					listAdapter.clear();
					listAdapter.addAll(list);
					groundList.stopRefresh();
					groundList.stopLoadMore(mnet.hasMore());
					groundList.setPullLoadEnable(mnet.hasMore());
					jsonlist = list;
				}

			}

			@Override
			public void clear() {
				// TODO Auto-generated method stub
				listAdapter.clear();
			}
		});
		listAdapter.setJump(GroundDetail.class, "clubid", "id");
		listAdapter.addField("name", R.id.name);
		listAdapter.addField("address", R.id.address);
		listAdapter.addField("price", R.id.price, "price");
		listAdapter.addField("mpic1", R.id.image);
		listAdapter.addField("type", R.id.type, "groundType");
		listAdapter.addField("priceunit", R.id.pricetype, "pricetype");
		mnet.addparam("playdate", date);
		mnet.addparam("playtime", time);
		if (!AbStrUtil.isEmpty(name)) {
			mnet.addparam("name", name);
		}
		mnet.addparam("type", 2);
		mnet.addparam("ordertype", paixuId);
		mnet.addparam("desctype", desctype);
		mnet.addparam("lng", MApplication.longitude);
		mnet.addparam("lat", MApplication.latitude);

		if (!StringUtils.isEmpty(MApplication.sProvince)) {

			if (MApplication.sProvince.indexOf(StringUtils
					.placeToString(MApplication.sCity)) >= 0) {
				mnet.addparam("province", MApplication.sProvince);
			} else {
				mnet.addparam("city", MApplication.sCity);
				mnet.addparam("province", MApplication.sProvince);
			}
		}

		loadfirst = true;
		mnet.refreshDialog();
		groundList.setAdapter(listAdapter);
		//
		groundList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				Intent it = new Intent(getBaseContext(), listAdapter
						.getJumpClazz());
				if (position - groundList.getHeaderViewsCount() + 1 > 0) {
					JSONObject jo = listAdapter.getTItem(position
							- groundList.getHeaderViewsCount());
					it.putExtra("listTemp", jo.toString());
					try {
						it.putExtra(listAdapter.getJumpAs(), JSONUtil
								.getString(jo, listAdapter.getJumpKey()));
						it.putExtra("type", 2);
					} catch (Exception e) {
					}
					startActivity(it);

					Ground g = db.load(Ground.class,
							JSONUtil.getString(jo, "id"));
					if (g != null && !AbStrUtil.isEmpty(g.getId())) {

					} else {
						g = new Ground();
						g.setId(JSONUtil.getString(jo, "id"));
						g.setName(JSONUtil.getString(jo, "name"));
						g.setAddress(JSONUtil.getString(jo, "address"));
						g.setClubid(JSONUtil.getString(jo, "clubid"));
						g.setData(JSONUtil.getString(jo, "data"));
						g.setPic1(JSONUtil.getString(jo, "logo"));
						g.setModel(JSONUtil.getString(jo, "model"));
						g.setPrice(JSONUtil.getString(jo, "price"));
						g.setType(2);
						db.onlysave(g);
					}
				}

			}

		});

		groundList.setAbOnListViewListener(new AbOnListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mnet.showProgressOnFrist(false);
				mnet.addparam("ordertype", paixuId);
				mnet.addparam("desctype", desctype);
				loadfirst = true;
				mnet.refresh();
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				loadfirst = false;
				mnet.showNext();
			}
		});

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == p_price || view == p_time || view == p_destance) {
			if (behandChange) {
				behandChange = false;
			} else {

				if (desctype.equals("DESC")) {
					desctype = "ASC";
					((RadioButton) view).setButtonDrawable(R.drawable.arrow_b);
				} else if (desctype.equals("ASC")) {
					desctype = "DESC";
					((RadioButton) view).setButtonDrawable(R.drawable.arrow_b2);
				}
				mnet.addparam("desctype", desctype);
				loadfirst = true;
				mnet.refreshDialog();
			}
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshByCityChange();
		RightBtn.setText(StringUtils.isEmpty(MApplication.sCity) ? "位置"
				: MApplication.sCity);
		MobclickAgent.onPageStart("main-lianxichang"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("main-lianxichang");
	}

	public void refreshByCityChange() {
		if (MApplication.sCity != null && !MApplication.sCity.equals(loadCity)) {
			// mnet.addparam("city", MApplication.city);
			if (MApplication.sProvince.indexOf(StringUtils
					.placeToString(MApplication.sCity)) >= 0) {
				mnet.cleanParams();
				mnet.addparam("type", 2);
				mnet.addparam("ordertype", paixuId);
				mnet.addparam("desctype", desctype);
				mnet.addparam("playdate", date);
				mnet.addparam("playtime", time);
				if (!AbStrUtil.isEmpty(name)) {
					mnet.addparam("name", name);
				}
				mnet.addparam("lng", MApplication.longitude);
				mnet.addparam("lat", MApplication.latitude);
				mnet.addparam("province", MApplication.sProvince);
			} else {
				mnet.addparam("city", MApplication.sCity);
				mnet.addparam("province", MApplication.sProvince);
			}
			loadCity = MApplication.sCity;
			loadfirst = true;
			mnet.refreshDialog();
		}
	}

}
