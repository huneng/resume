package com.huneng.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SkillData {
	public String skillname;
	public int starttime;
	public int[] scores;
	public int length;

	public SkillData() {
		length = 0;
		scores = new int[20];
		skillname = "";
		starttime = 0;
	}

	public SkillData(JSONObject o) throws JSONException {
		skillname = o.getString("skillname");
		starttime = o.getInt("starttime");
		JSONArray array = o.getJSONArray("scores");
		scores = new int[20];
		int len = array.length();
		for (int i = 0; i < len; i++)
			scores[length++] = array.getInt(i);
	}

	JSONObject changeToJson() throws JSONException {
		JSONObject object = new JSONObject();
		object.put("skillname", skillname);
		object.put("starttime", starttime);

		JSONArray array = new JSONArray();
		for (int i = 0; i < length; i++)
			array.put(scores[i]);
		object.put("scores", array);
		return object;
	}
	void addScore(int s){
		scores[length++] = s;
	}

	public void setScore(String str) {
		int t = 0;
		length = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c < 58 && c > 47)
				t = t * 10 + c - 48;
			else {
				if (t != 0) {
					scores[length++] = t;
					t = 0;
				}
			}
		}
		if (t != 0) {
			scores[length++] = t;
		}
	}
	public boolean check(){
		boolean bool = skillname.equals("")||starttime==0;
		return !bool;
	}
	public float average(){
		float r = 0;
		for(int i = 0; i < length; i++){
			r+=scores[i];
		}
		r = r /length;
		return r;
	}
}
