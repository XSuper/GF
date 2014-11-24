package com.bookingsystem.android.bean;

import net.duohuo.dhroid.db.ann.Column;

public class User {
	@Column(pk=true)
	public String mid;
	public String mobile;
	public String token;

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
