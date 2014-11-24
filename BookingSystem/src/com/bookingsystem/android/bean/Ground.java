package com.bookingsystem.android.bean;

import net.duohuo.dhroid.db.ann.Column;

public class Ground {
	public String name;
	public String address;
	public int type;
	public String model;
	public String data;
	public String pic1;
	@Column(pk=true)
	public String id;
	public String clubid;
	public String price;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getPic1() {
		return pic1;
	}
	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClubid() {
		return clubid;
	}
	public void setClubid(String clubid) {
		this.clubid = clubid;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	

}
