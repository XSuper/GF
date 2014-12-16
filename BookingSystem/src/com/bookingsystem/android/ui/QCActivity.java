package com.bookingsystem.android.ui;

import java.util.List;

import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.ioc.annotation.Inject;
import net.duohuo.dhroid.ioc.annotation.InjectExtra;
import net.duohuo.dhroid.net.JSONUtil;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class QCActivity extends MBaseActivity implements OnClickListener {
	MNetAdapter mnet;// 访问网络集合对象
	boolean loadfirst = true;// 是否是第一次加载 或者是刷新

	RadioGroup paixu;
	RadioButton p_price, p_time, p_destance;
	int paixuId = 1;// 默认按时间排序 2 为价格排序
	String desctype = "ASC";// 默认shenqu DESC 降序
	int checkid = R.id.paixu_time;
	boolean behandChange = false;
	// -----------列表页属性
	View layoutList; // 列表view
	AbPullListView groundList;
	MBeanAdapter listAdapter;

	public String loadCity = AbStrUtil.isEmpty(MApplication.sCity) ? MApplication.city
			: MApplication.sCity;// 标示加载数据用的城市;

	@Inject
	DhDB db;
	private Button RightBtn;

	// 搜索条件
	@InjectExtra(name = "name")
	String name;
	@InjectExtra(name = "date")
	String date;
	@InjectExtra(name = "time")
	String time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_qc);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(20, 0, 0, 0);
		mAbTitleBar.setTitleText("球场");
		// mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);

		initTitleRightLayout();
		init();
		// db = IocContainer.getShare().get(DhDB.class);
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
				Intent i = new Intent(QCActivity.this, ChoiceCityActivity.class);
				startActivity(i);
			}

		});
	}

	private void init() {
		layoutList = findViewById(R.id.layout_list);
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
				p_price.setButtonDrawable(R.drawable.paixu_radio);
				p_time.setButtonDrawable(R.drawable.paixu_radio);
				p_destance.setButtonDrawable(R.drawable.paixu_radio);

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

		listAdapter = new MBeanAdapter(this, R.layout.item_ground_qc);

	}

	/**
	 * 加载数据
	 */
	public void loadData() {

		mnet = new MNetAdapter(Constant.BASEURL, this, new MNetCallBack() {
			@Override
			public void success(List<JSONObject> list) {
				// TODO Auto-generated method stub
				if ((list == null || list.size() == 0)) {
					showToast("您搜索的城市暂无球场，换个城市看看！");
				}

				listAdapter.clear();
				listAdapter.addAll(list);
				groundList.stopRefresh();
				groundList.stopLoadMore(mnet.hasMore());
				groundList.setPullLoadEnable(mnet.hasMore());

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
		listAdapter.addField("logo", R.id.image);
		listAdapter.addField("type", R.id.type, "groundType");
		mnet.addparam("type", 1);
		mnet.addparam("playdate", date);
		mnet.addparam("playtime", time);
		if (!AbStrUtil.isEmpty(name)) {
			mnet.addparam("name", name);
		}
		// mnet.addparam("city", "上海");
		if (!StringUtils.isEmpty(MApplication.sCity)) {
			if (MApplication.sProvince.indexOf(StringUtils
					.placeToString(MApplication.sCity)) >= 0) {
				mnet.addparam("province", MApplication.sProvince);
			} else {
				mnet.addparam("city", MApplication.sCity);
				mnet.addparam("province", MApplication.sProvince);
			}
		}else{
			mnet.addparam("province", "全国");
		}
		mnet.addparam("ordertype", paixuId);
		mnet.addparam("desctype", desctype);
		mnet.addparam("lng", MApplication.longitude);
		mnet.addparam("lat", MApplication.latitude);
		mnet.refreshDialog();
		groundList.setAdapter(listAdapter);
		//
		groundList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				Intent it = new Intent(QCActivity.this, listAdapter
						.getJumpClazz());
				if (position - groundList.getHeaderViewsCount() + 1 > 0) {
					JSONObject jo = listAdapter.getTItem(position
							- groundList.getHeaderViewsCount());
					it.putExtra("listTemp", jo.toString());
					try {
						it.putExtra(listAdapter.getJumpAs(), JSONUtil
								.getString(jo, listAdapter.getJumpKey()));
						it.putExtra("type", 1);
						it.putExtra("city", JSONUtil.getString(jo, "city"));
					} catch (Exception e) {
					}
					startActivity(it);

					Log.v("groud", "判断是否保存");
					Ground g = db.load(Ground.class,
							JSONUtil.getString(jo, "id"));
					Log.v("groud", "判断完");
					if (g != null && !AbStrUtil.isEmpty(g.getId())) {
						Log.v("groud", "已经保存");
					} else {
						Log.v("groud", "开始保存");
						g = new Ground();
						g.setId(JSONUtil.getString(jo, "id"));
						g.setName(JSONUtil.getString(jo, "name"));
						g.setAddress(JSONUtil.getString(jo, "address"));
						g.setClubid(JSONUtil.getString(jo, "clubid"));
						g.setData(JSONUtil.getString(jo, "data"));
						g.setPic1(JSONUtil.getString(jo, "logo"));
						g.setModel(JSONUtil.getString(jo, "model"));
						g.setPrice(JSONUtil.getString(jo, "price"));
						g.setType(1);
						db.onlysave(g);
						Log.v("groud", "保存成功" + JSONUtil.getString(jo, "id")
								+ JSONUtil.getString(jo, "name"));
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
		MobclickAgent.onPageStart("main-julebu"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("main-julebu");
	}

	public void refreshByCityChange() {
		if (MApplication.sCity != null && !MApplication.sCity.equals(loadCity)) {

			if (MApplication.sProvince.indexOf(StringUtils
					.placeToString(MApplication.sCity)) >= 0) {
				mnet.cleanParams();
				mnet.addparam("type", 1);
				mnet.addparam("ordertype", paixuId);
				mnet.addparam("desctype", desctype);
				mnet.addparam("lng", MApplication.longitude);
				mnet.addparam("lat", MApplication.latitude);
				mnet.addparam("province", MApplication.sProvince);
				mnet.addparam("playdate", date);
				mnet.addparam("playtime", time);
				if (!AbStrUtil.isEmpty(name)) {
					mnet.addparam("name", name);
				}
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
