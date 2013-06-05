package com.huneng.data;

import org.json.JSONException;
import org.json.JSONObject;

public class WorkData {
	public String workname;
	public String company;
	public int begintime;
	public int endtime;
	public int score;

	public WorkData() {
		workname = "";
		company = "";
		begintime = 0;
		endtime = 0;
		score = 0;
	}

	public WorkData(JSONObject object) throws JSONException {
		workname = object.getString("position");
		company = object.getString("company");
		begintime = object.getInt("begintime");
		endtime = object.getInt("endtime");
		score = object.getInt("score");
	}

	public JSONObject changeToJson() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("position", workname);
		o.put("company", company);
		o.put("begintime", begintime);
		o.put("endtime", endtime);
		o.put("score", score);
		return o;
	}
	public boolean check(){
		boolean bool = workname.equals("")||begintime>=endtime||score==0;
		return !bool;
		
	}
}