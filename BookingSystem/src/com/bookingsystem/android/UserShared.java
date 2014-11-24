package com.bookingsystem.android;

import com.bookingsystem.android.bean.User;

import net.duohuo.dhroid.util.Perference;

public class UserShared extends Perference{
	private static UserShared userShared;
	public User user;
	private UserShared() {
		// TODO Auto-generated constructor stub
	}

	public static UserShared getInstance(){
		if(userShared==null){
			userShared = new UserShared();
		}
		return userShared;
	}
}
