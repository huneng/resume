package com.huneng.data;

public class BaseData {

	public String name;
	public String birth;
	public String phone;
	public String address;
	public String job;
	public String salary;
	public String holiday;
	public String sex;
	public String photo; 
	public int starttime, endtime;
	public String remark;

	public BaseData() {
		name=phone=address = job = salary = holiday = "";
		birth = "";
		sex = "male";
		starttime = 2010;
		endtime = 2010;
		photo = "";
		remark = "";
	}

	public boolean check() {
		boolean state;
		state = name.equals("") || job.equals("") || salary.equals("")
				|| holiday.equals("") || birth.equals("") || phone.equals("")
				|| address.equals("");
		return state;

	}
}
