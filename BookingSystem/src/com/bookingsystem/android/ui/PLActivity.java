package com.bookingsystem.android.ui;

import java.util.List;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.JSONUtil;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.bookingsystem.android.imp.MNetCallBack;
import com.bookingsystem.android.util.StringUtils;
import com.umeng.analytics.MobclickAgent;

public class PLActivity extends MBaseActivity {
	MNetAdapter mnet;// 访问网络集合对象
	boolean loadfirst = true;// 是否是第一次加载 或者是刷新

	RadioGroup typeRadio;
	int checkid = R.id.paixu_time;
	boolean behandChange = false;
	// -----------列表页属性
	AbPullListView plList;
	MBeanAdapter listAdapter;

	private List<JSONObject> jsonlist;

	public String loadCity = MApplication.sCity = AbStrUtil
			.isEmpty(MApplication.sCity) ? MApplication.city
			: MApplication.sCity;// 标示加载数据用的城市

	DhDB db;
	private int type = 0;// 类别
	private Button RightBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setLogo(R.drawable.back);
		mAbTitleBar.setBackgroundResource(R.drawable.gst_top_bg);
		mAbTitleBar.setTitleTextMargin(20, 0, 0, 0);
		mAbTitleBar.setTitleText("陪练");
		// mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.back);

		initTitleRightLayout();
		db = IocContainer.getShare().get(DhDB.class);
		init();
		type = getIntent().getIntExtra("type", 0);

		switch (type) {
		case 1:
			((RadioButton) findViewById(R.id.radio_boy)).setChecked(true);
			break;
		case 2:
			((RadioButton) findViewById(R.id.radio_girl)).setChecked(true);
			break;
		case 3:
			((RadioButton) findViewById(R.id.radio_master)).setChecked(true);
			break;
		case 4:
			((RadioButton) findViewById(R.id.radio_teacher)).setChecked(true);
			break;
		}

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
				Intent i = new Intent(PLActivity.this, ChoiceCityActivity.class);
				startActivity(i);
			}

		});
	}

	private void init() {
		setAbContentView(R.layout.activity_pl);
		plList = (AbPullListView) findViewById(R.id.pl_list);
		typeRadio = (RadioGroup) findViewById(R.id.pl_type);
		typeRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int id) {
				// TODO Auto-generated method stub
				switch (id) {
				case R.id.radio_boy:
					type = 1;
					break;
				case R.id.radio_girl:
					type = 2;
					break;
				case R.id.radio_master:
					type = 3;
					break;
				case R.id.radio_teacher:
					type = 4;
					break;

				}
				if(mnet!=null){
					mnet.addparam("type", type);
					loadfirst = true;
					mnet.refreshDialog();
				}

			}
		});

		listAdapter = new MBeanAdapter(this, R.layout.item_pl);
	}

	/**
	 * 加载数据
	 */
	public void loadData() {

		mnet = new MNetAdapter(Constant.BASEURL + "&a=sparring", this,
				new MNetCallBack() {

					@Override
					public void success(List<JSONObject> list) {
						// TODO Auto-generated method stub

						if ((list == null || list.size() == 0)) {
							showToast("您搜索的城市暂无陪练者，换个城市看看！");
						}

						listAdapter.clear();
						listAdapter.addAll(list);
						plList.stopRefresh();
						plList.stopLoadMore(mnet.hasMore());
						plList.setPullLoadEnable(mnet.hasMore());
						jsonlist = list;

					}

					@Override
					public void clear() {
						// TODO Auto-generated method stub
						listAdapter.clear();
					}
				});
		listAdapter.setJump(PLDetail.class, "uid", "uid");

		listAdapter.addField(new FieldMap("sex", R.id.sex) {

			@Override
			public Object fix(View itemV, Integer position, Object o, Object jo) {
				// TODO Auto-generated method stub

				int sexnum = StringUtils.toInt(o);
				switch (sexnum) {
				case 1:// men

					return R.drawable.man;

				case 2:// girl
					return R.drawable.girl;

				}
				return R.drawable.man;
			}
		});
		listAdapter.addField("username", R.id.name);
		listAdapter.addField("age", R.id.age, "age");
		listAdapter.addField("bpic1", R.id.image);
		listAdapter.addField("clubprice", R.id.price, "price");
		listAdapter.addField("practiceprice", R.id.practice_price, "price");
		listAdapter.addField("profession", R.id.job);
		listAdapter.addField("label", R.id.lable, "flag");
		mnet.addparam("type", type);
		if (!StringUtils.isEmpty(MApplication.sCity)) {
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
		plList.setAdapter(listAdapter);
		//
		plList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				Intent it = new Intent(getBaseContext(), listAdapter
						.getJumpClazz());
				if (position - plList.getHeaderViewsCount() + 1 > 0) {
					JSONObject jo = listAdapter.getTItem(position
							- plList.getHeaderViewsCount());
					it.putExtra("listTemp", jo.toString());
					// 将性别传过去 用于 列表性别 ta 的判断
					it.putExtra("sex", JSONUtil.getInt(jo, "sex"));
					try {
						it.putExtra(listAdapter.getJumpAs(), JSONUtil
								.getString(jo, listAdapter.getJumpKey()));

					} catch (Exception e) {
					}
					startActivity(it);

				}

			}

		});

		plList.setAbOnListViewListener(new AbOnListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mnet.showProgressOnFrist(false);
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshByCityChange();
		RightBtn.setText(StringUtils.isEmpty(MApplication.sCity) ? "位置"
				: MApplication.sCity);
		MobclickAgent.onPageStart("main-peilian"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("main-peilian");
	}

	public void refreshByCityChange() {
		if (MApplication.sCity != null && !MApplication.sCity.equals(loadCity)) {
			if (MApplication.sProvince.indexOf(StringUtils
					.placeToString(MApplication.sCity)) >= 0) {
				mnet.cleanParams();
				mnet.addparam("type", type);
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
