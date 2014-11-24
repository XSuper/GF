package com.bookingsystem.android.bean;




public class City {
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	private int _id;
	private String province;
	private String city;
	private String number;
	private String firstpy;
	private String allpy;
	private String allfirstpy;

	public City() {
		// TODO Auto-generated constructor stub
	}


	
	public int get_id() {
		return _id;
	}



	public void set_id(int _id) {
		this._id = _id;
	}



	public int getId() {
		return _id;
	}


	public void setId(int _id) {
		this._id = _id;
	}


	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getFirstpy() {
		return firstpy;
	}

	public void setFirstpy(String firstpy) {
		this.firstpy = firstpy;
	}

	public String getAllpy() {
		return allpy;
	}

	public void setAllpy(String allpy) {
		this.allpy = allpy;
	}

	public String getAllfirstpy() {
		return allfirstpy;
	}

	public void setAllfirstpy(String allFristpy) {
		this.allfirstpy = allFristpy;
	}

	@Override
	public String toString() {
		return "City [province=" + province + ", city=" + city + ", number="
				+ number + ", firstpy=" + firstpy + ", allpy=" + allpy
				+ ", allFristpy=" + allfirstpy + "]";
	}

}
