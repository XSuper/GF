package com.bookingsystem.android;

import net.duohuo.dhroid.activity.ActivityTack;
import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.ioc.annotation.Inject;
import net.duohuo.dhroid.ioc.annotation.InjectView;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.ab.util.AbStrUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.bookingsystem.android.bean.Place;
import com.bookingsystem.android.ui.ADWebViewActivity;
import com.bookingsystem.android.ui.GroundSearchActivity;
import com.bookingsystem.android.ui.MBaseActivity;
import com.bookingsystem.android.ui.MyActivity;
import com.bookingsystem.android.ui.PLActivity;
import com.bookingsystem.android.view.ADView;
import com.umeng.update.UmengUpdateAgent;

public class HomeActivity extends MBaseActivity implements OnClickListener {

	@InjectView(id = R.id.clube)
	public Button clube;
	@InjectView(id = R.id.boy)
	public Button boy;
	@InjectView(id = R.id.girl)
	public Button girl;
	@InjectView(id = R.id.lxc)
	public Button lxc;
	@InjectView(id = R.id.my)
	public Button my;
	@InjectView(id = R.id.teacher)
	public Button teacher;

	@InjectView(id = R.id.adview)
	public ADView ad;
	@Inject
	DhDB db;

	DhNet net;

	// *********************************************
	// 百度地图定位
	public static LocationClient mMapClient = null;
	public MyLocationListenner myListener = null;
	public static String TAG = "BaiDuMapGPS";

	// *********************************************

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UmengUpdateAgent.update(this);
		setContentView(R.layout.activity_home);
		clube.setOnClickListener(this);
		boy.setOnClickListener(this);
		girl.setOnClickListener(this);
		lxc.setOnClickListener(this);
		my.setOnClickListener(this);
		teacher.setOnClickListener(this);

		initBaiduMap();

		loadAD();
	}

	private void loadAD() {
		String url = "http://basic.gocen.cn:8060/basic/advertisementJsonController.htm?aGroupId=1";
		// TODO Auto-generated method stub
		net = new DhNet(url);
		net.doGet(new NetTask(this) {

			@Override
			public void doInUI(Response response, Integer transfer) {
				// TODO Auto-generated method stub
				if(!response.isSuccess()){
					
					return;
				}
				ad.setVisibility(View.VISIBLE);
				JSONArray ja = response.jSONArrayFromData();
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = JSONUtil.getJSONObjectAt(ja, i);
					String url = JSONUtil.getString(jo, "linkUrl");
					String showpic = JSONUtil.getString(jo, "showPic");
					String pic = JSONUtil.getString(jo, "pic");
					final String value = AbStrUtil.isEmpty(url) ? showpic : url;
					final String name = JSONUtil.getString(jo, "name");
					if (!AbStrUtil.isEmpty(value)) {
						ImageView view = new ImageView(getBaseContext());
						view.setBackgroundResource(R.drawable.imgdefault);
						view.setLayoutParams(layoutParamsFF);
						view.setScaleType(ScaleType.CENTER_CROP);
						ViewUtil.bindView(view, pic);
						ad.addView(view);
						view.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent i = new Intent(getBaseContext(),ADWebViewActivity.class);
								i.putExtra("name",name);
								i.putExtra("url",value);
								startActivity(i);
							}
						});
					}
				}
				ad.startPlay();
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent i = null;
		switch (arg0.getId()) {
		case R.id.clube:
			i = new Intent(this, GroundSearchActivity.class);
			i.putExtra("type", 1);
			//i = new Intent(this, QCActivity.class);
			break;
		case R.id.boy:
			i = new Intent(this, PLActivity.class);
			i.putExtra("type", 1);
			break;
		case R.id.girl:
			i = new Intent(this, PLActivity.class);
			i.putExtra("type", 2);
			break;
		case R.id.lxc:
			i = new Intent(this, GroundSearchActivity.class);
			i.putExtra("type", 2);
			break;
		case R.id.my:
			i = new Intent(this, MyActivity.class);
			break;
		case R.id.teacher:
			i = new Intent(this, PLActivity.class);
			i.putExtra("type", 4);

			break;
		}
		if (i != null)
			startActivity(i);
	}

	private void initBaiduMap() {

		mMapClient = new LocationClient(getApplicationContext());
		myListener = new MyLocationListenner();
		LocationClientOption option = new LocationClientOption();

		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		// option.setScanType(5000);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mMapClient.setLocOption(option);

		mMapClient.registerLocationListener(myListener);
		mMapClient.setLocOption(option);
		mMapClient.start();
		mMapClient.requestLocation();// 请求定位
	}

	@Override
	public void onBackPressed() {
		showDialog("退出程序", "确定退出高盛通系统?", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				ActivityTack.getInstanse().exit(getBaseContext());
			}
		});
	}

	/**
	 * 
	 * 百度gps定位
	 * 
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(final BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());// 纬度
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());// 经度
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\n省：");
				sb.append(location.getProvince());
				sb.append("\n市：");
				sb.append(location.getCity());
				sb.append("\n区/县：");
				sb.append(location.getDistrict());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());

			}
			sb.append("\n11省：");
			sb.append(location.getProvince());
			sb.append("\n11市：");
			sb.append(location.getCity());
			sb.append("\n11区/县：");
			sb.append(location.getDistrict());
			sb.append("\n11addr : ");
			sb.append(location.getAddrStr());
			sb.append("\nsdk version : ");
			sb.append(mMapClient.getVersion());
			sb.append("\nisCellChangeFlag : ");
			sb.append(location.isCellChangeFlag());
			MApplication.location = location;
			MApplication.latitude = location.getLatitude();
			MApplication.longitude = location.getLongitude();
			// MApplication.city =
			// StringUtils.placeToString(location.getCity());
			MApplication.city = location.getCity();
			MApplication.province = location.getProvince();

			String city = MApplication.city;
			if (AbStrUtil.isEmpty(MApplication.sCity)) {
				Place p = new Place();
				p.pid = "GST";
				p.city = location.getCity();
				p.latitude = location.getLatitude();
				p.longitude = location.getLongitude();
				p.province = location.getProvince();
				p.district = location.getDistrict();
				db.onlysave(p);
				MApplication.sCity = p.city;
				MApplication.sProvince = p.province;
			} else if (!AbStrUtil.isEmpty(MApplication.city)
					&& !MApplication.city.equals(MApplication.sCity)) {
				showDialog("更换城市", "当前定位城市为" + city + "是否将地址更换为" + city,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								Place p = new Place();
								p.pid = "GST";
								p.city = location.getCity();
								p.latitude = location.getLatitude();
								p.longitude = location.getLongitude();
								p.province = location.getProvince();
								p.district = location.getDistrict();
								db.update(p);
								MApplication.sCity = p.city;
								MApplication.sProvince = p.province;

							}
						});
			}

			Log.v(TAG, sb.toString());
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi()) {
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			} else {
				sb.append("noPoi information");
			}
			Log.v(TAG, sb.toString());
		}
	}
	// *********百度地图相关end***********************************************************************************************

}
