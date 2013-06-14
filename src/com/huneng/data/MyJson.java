package com.huneng.data;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MyJson {
	public List<SkillData> skills;
	public List<WorkData> works;
	public BaseData basedata;
	public List<String> remarks;
	public String picturePath;

	public MyJson() {
		basedata = new BaseData();
		skills = new LinkedList<SkillData>();
		works = new LinkedList<WorkData>();
		remarks = new LinkedList<String>();
		picturePath = "";
	}

	void setRemarks(String str) {
		String[] array = str.split(";");
		remarks.clear();
		for (int i = 0; i < array.length; i++) {
			remarks.add(array[i]);
		}
	}

	public String getRemark() {
		StringBuffer str = new StringBuffer(100);
		str.append("");
		int size = remarks.size();
		for (int i = 0; i < size; i++) {
			str.append(remarks.get(i) + ';');
		}
		return new String(str);
	}

	public MyJson(JSONObject object) throws JSONException {

		skills = new LinkedList<SkillData>();
		works = new LinkedList<WorkData>();
		remarks = new LinkedList<String>();
		basedata = new BaseData();

		JSONObject base = object.getJSONObject("fundamental");
		basedata.name = base.getString("people");
		basedata.sex = base.getString("sex");
		basedata.birth = base.getString("birth");
		basedata.phone = base.getString("phone");
		basedata.photo = base.getString("photo");

		basedata.address = base.getString("address");
		basedata.starttime = base.getInt("starttime");
		basedata.endtime = base.getInt("endtime");
		JSONObject want = object.getJSONObject("wanted");
		basedata.job = want.getString("job");
		basedata.salary = want.getString("salary");
		basedata.holiday = want.getString("holiday");

		JSONArray s = object.getJSONArray("skills");
		int size = s.length();
		for (int i = 0; i < size; i++) {
			JSONObject o = s.getJSONObject(i);
			skills.add(new SkillData(o));
		}

		JSONArray w = object.getJSONArray("works");
		size = w.length();
		for (int i = 0; i < size; i++) {
			JSONObject o = w.getJSONObject(i);
			works.add(new WorkData(o));
		}
		JSONArray remark = object.getJSONArray("remark");
		size = remark.length();
		for (int i = 0; i < size; i++) {
			String str = remark.getString(i);
			remarks.add(str);
		}

	}

	public String changToJsonData() throws JSONException {
		JSONObject resume = new JSONObject();
		JSONObject base = new JSONObject();
		base.put("people", basedata.name);
		base.put("sex", basedata.sex);
		base.put("birth", basedata.birth);
		base.put("phone", basedata.phone);
		base.put("address", basedata.address);
		base.put("photo", basedata.photo);
		JSONObject want = new JSONObject();
		want.put("job", basedata.job);
		want.put("salary", basedata.salary);
		want.put("holiday", basedata.holiday);
		resume.put("fundamental", base);
		resume.put("wanted", want);

		JSONArray s = new JSONArray();
		int size = skills.size();
		int a, b;
		a = skills.get(0).starttime;
		b = a + skills.get(0).length - 1;
		for (int i = 0; i < size; i++) {
			if (skills.get(i).check()) {
				int t1 = skills.get(i).starttime;
				int t2 = skills.get(i).length -1;
				if (a > t1) {
					a = t1;
				}
				if (b < t1 + t2) {
					b = t1 + t2;
				}
				s.put(skills.get(i).changeToJson());
			} else {
				skills.remove(i);
			}
		}
		size = works.size();
		JSONArray w = new JSONArray();
		for (int i = 0; i < size; i++) {
			int t1 = works.get(i).begintime / 100;
			int t2 = works.get(i).endtime / 100;
			if (a > t1) {
				a = t1;
			}
			if (b < t2) {
				b = t2;
			}
			if (works.get(i).check())
				w.put(works.get(i).changeToJson());
			else
				works.remove(i);
		}
		basedata.starttime = a;
		basedata.endtime = b;
		base.put("starttime", basedata.starttime);
		base.put("endtime", basedata.endtime);
		resume.put("skills", s);
		resume.put("works", w);

		setRemarks(basedata.remark);
		JSONArray r = new JSONArray();
		size = remarks.size();
		for (int i = 0; i < size; i++) {
			r.put(remarks.get(i));

		}
		resume.put("remark", r);
		return resume.toString();
	}

}
