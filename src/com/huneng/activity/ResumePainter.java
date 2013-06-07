package com.huneng.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.huneng.data.MyJson;
import com.huneng.resume.ArcResume;
import com.huneng.resume.AxisAnalyseResume;
import com.huneng.resume.BaseResume;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class ResumePainter extends Activity {
	public int resumeWidth, resumeHeight;
	private LinearLayout paintLayout;
	private RadioButton mode1, mode2, mode3;
	AxisAnalyseResume resume1;
	ArcResume resume2;
	BaseResume resume3;
	public MyJson resumeData;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main_paint);
		Intent intent = getIntent();
		String data = intent.getStringExtra("resume_data");
		if (data == null) {
			data = "";
		}
		try {
			resumeData = new MyJson(new JSONObject(data));
		} catch (JSONException e) {
			resumeData = new MyJson();
		}
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		float t = 50 * metrics.density;
		resumeWidth = metrics.widthPixels;
		resumeHeight = metrics.heightPixels - (int) t;

		initTab();
		initResumeView();

	}

	private void initResumeView() {

		paintLayout = (LinearLayout) findViewById(R.id.paint_layout);

		resume1 = AxisAnalyseResume.createAxisAnalyseResume(this, resumeWidth,
				resumeHeight, resumeData);
		resume1.init();
		resume2 = ArcResume.createArcResume(this, resumeWidth, resumeHeight,
				resumeData);
		resume2.init();
		resume3 = BaseResume.createBaseResume(this, resumeWidth, resumeHeight);
		resume3.init(resumeData.basedata, resumeData.remarks);
		paintLayout.addView(resume1);
		paintLayout.addView(resume2);
		paintLayout.addView(resume3.view);
		gotoView(mode1);

	}

	private void initTab() {
		mode1 = (RadioButton) findViewById(R.id.paint_mode1);
		mode2 = (RadioButton) findViewById(R.id.paint_mode2);
		mode3 = (RadioButton) findViewById(R.id.paint_mode3);
	}

	public void gotoView(View v) {
		int bar_normal = R.drawable.widget_bar_bg_n;
		int bar_press = R.drawable.widget_bar_bg_p;
		switch (v.getId()) {
		case R.id.paint_mode1:
			refreshView(View.VISIBLE, View.GONE, View.GONE);
			refreshTab(bar_press, bar_normal, bar_normal);
			break;
		case R.id.paint_mode2:
			refreshView(View.GONE, View.VISIBLE, View.GONE);
			refreshTab(bar_normal, bar_press, bar_normal);
			break;
		case R.id.paint_mode3:
			refreshView(View.GONE, View.GONE, View.VISIBLE);
			refreshTab(bar_normal, bar_normal, bar_press);
			break;
		}
	}

	private void refreshView(int a, int b, int c) {
		resume1.setVisibility(a);
		resume2.setVisibility(b);
		resume3.view.setVisibility(c);
	}

	private void refreshTab(int a, int b, int c) {
		mode1.setBackgroundResource(a);
		mode2.setBackgroundResource(b);
		mode3.setBackgroundResource(c);
	}

}