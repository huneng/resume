package com.huneng.resume;

import java.util.LinkedList;
import java.util.List;

import com.huneng.data.MyJson;
import com.huneng.data.SkillData;
import com.huneng.paint.*;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class ArcResume extends View {
	private int width, height;
	private MyJson data;

	public ArcResume(Context context) {
		super(context);
	}

	public static ArcResume createArcResume(Context context, int width,
			int height) {

		return createArcResume(context, width, height, null);
	}

	public static ArcResume createArcResume(Context context, int width,
			int height, MyJson data) {
		ArcResume resume = new ArcResume(context);
		resume.width = width;
		resume.height = height;
		resume.data = data;
		return resume;
	}

	List<Arc> arcs;
	float cx, cy, radius;
	Paint mPaint;
	int color[];
	int colorIndex;

	public void init() {
		color = MyColor.color1;
		cx = width / 2;
		cy = height / 2 - 10;
		radius = cx - width / 6;
		colorIndex = 0;
		mPaint = new Paint();
		colorChange();
		setArcAngle(data.skills);
		color = MyColor.color1;
	}

	void colorChange() {
		mPaint.setColor(color[colorIndex++]);
		colorIndex %= color.length;
	}

	void setArcAngle(List<SkillData> skills) {
		arcs = new LinkedList<Arc>();
		int size = skills.size();
		float sum = 0;
		for (int i = 0; i < size; i++) {
			sum += skills.get(i).average();
		}
		float p[] = new float[size];
		for (int i = 0; i < size; i++) {
			p[i] = skills.get(i).average() / sum;
		}
		float startAngle = 0, sweepAngle = 0;
		for (int i = 0; i < size; i++) {
			colorChange();
			sweepAngle = p[i] * 360;

			Arc arc = new Arc();
			arc.startAngle = startAngle;
			arc.sweepAngle = sweepAngle;
			arc.cx = cx;
			arc.cy = cy;
			arc.radius = radius;
			arc.paint = new Paint(mPaint);
			arc.text = skills.get(i).skillname;
			arcs.add(arc);
			startAngle += sweepAngle;
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		for (int i = 0; i < arcs.size(); i++) {
			arcs.get(i).draw(canvas);
		}
		super.onDraw(canvas);
	}

}
