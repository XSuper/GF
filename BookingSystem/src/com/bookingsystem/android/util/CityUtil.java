package com.bookingsystem.android.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.duohuo.dhroid.db.DhDB;
import net.duohuo.dhroid.db.SqlProxy;
import android.content.Context;
import android.util.Log;

import com.ab.util.AbAppUtil;
import com.bookingsystem.android.R;
import com.bookingsystem.android.bean.City;
import com.bookingsystem.android.bean.Province;

public class CityUtil {
	private static DhDB db;
	public static List<City> citys ;
	public static List<String> AllProvince;
	
	static{
		db = new DhDB();
		db.init("city", 12);
	
	}
	public static List<String> getAllProvince(){
		if(db==null){
			db = new DhDB();
			
		}
		AllProvince = new ArrayList<String>();
		List<Province> all = db.queryAll(Province.class);
		for (Province province : all) {
			AllProvince.add(province.getName());
		}
		
		return AllProvince;
	}
	
	public static ArrayList<String> getAllCityByProvince(String province){
		ArrayList<String> mcitys = new ArrayList<String>();
		List<City> citys = db.queryList(City.class, " province = ? ", province);
		mcitys.add(province);
		for (City city : citys) {
			mcitys.add(city.getCity());
		}
		return mcitys;
	}
}
