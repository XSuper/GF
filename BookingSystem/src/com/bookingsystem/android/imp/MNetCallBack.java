package com.bookingsystem.android.imp;

import java.util.List;

import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

public abstract class MNetCallBack {
	public abstract void clear();
	public abstract void success(List<JSONObject> list);
	public  void error(Response response){};
	public  void cancle(){};

}
