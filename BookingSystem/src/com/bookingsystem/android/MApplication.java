package com.bookingsystem.android;

import net.duohuo.dhroid.Const;
import net.duohuo.dhroid.Dhroid;
import net.duohuo.dhroid.adapter.ValueFix;
import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.dialog.DialogImpl;
import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.Instance.InstanceScope;
import net.duohuo.dhroid.ioc.IocContainer;
import android.app.Application;

import com.ab.util.AbAppUtil;
import com.ab.util.AbStrUtil;
import com.baidu.location.BDLocation;
import com.bookingsystem.android.bean.Place;
import com.bookingsystem.android.bean.User;

public class MApplication extends Application {
	
	/**
	 * 记录搜索的时间
	 * 
	 */
	
	public static String searchDate;
	public static String searchTime;
	
	/**
	 * 用户相关
	 */
	public static User user;
	public static boolean islogin;
	public static String province, city;// 定位地址或者之前选择的城市
	public static String sProvince, sCity;// 用户选择的城市，没有选择则为定位城市
	public static double latitude, longitude;
	public static BDLocation location;

	@Override
	public void onCreate() {
		super.onCreate();

		Dhroid.init(this);
		// 数据库初始化
		IocContainer.getShare().initApplication(
				this);
		DhDB db = IocContainer.getShare().get(DhDB.class);
		db.init("bookingsystem", Const.DATABASE_VERSION);
//		// 自动登录判断
//		List<User> users = db.queryAll(User.class);
//		if (users.size() >= 1) {
//			MApplication.user = users.get(0);
//			if (MApplication.user != null
//					&& !AbStrUtil.isEmpty(MApplication.user.getMid())) {
//				MApplication.islogin = true;
//
//			} else {
//				MApplication.islogin = false;
//			}
//		}

		UserShared us = UserShared.getInstance();
		us.load();
		if(us.user==null||AbStrUtil.isEmpty(us.user.token)){
			MApplication.islogin = false;
		}else{
			MApplication.islogin = true;
			MApplication.user = us.user;
		}
		// 一些常量的配置
		Const.netadapter_page_no = "page";
		Const.netadapter_step = "pagesize";
		Const.response_data = "data";
		Const.netadapter_step_default = 10;
		Const.netadapter_json_timeline = "pubdate";
		Const.DATABASE_VERSION = 50;
		Const.net_pool_size = 30;
		Const.net_error_try = true;

		// IOC的初始化
		// IocContainer.getShare().initApplication(this);
		// IOC配置下面两个是必须配置的
		// 配置对话框对象,这是接口配置写法
		// 项目中可以自己写对话框对象,然后在这进行配置,这里使用的是提供的默认配置
		IocContainer.getShare().bind(DialogImpl.class).to(IDialog.class)
		// 这是单例
				.scope(InstanceScope.SCOPE_SINGLETON);
		// 配置ValueFix对象基本每个项目都有自己的实现
		IocContainer.getShare().bind(MValueFixer.class).to(ValueFix.class)
				.scope(InstanceScope.SCOPE_SINGLETON);

		AbAppUtil.importDatabase(this, "city", R.raw.city);
		// 读取历史位置信息
		Place p = db.load(Place.class, "GST");
		if (p != null && "GST".equals(p.getPid())) {
			MApplication.sProvince = p.province;
			MApplication.sCity = p.city;
		}

	}

}
