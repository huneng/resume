package com.huneng.resume;

import java.util.LinkedList;
import java.util.List;

import com.huneng.activity.R;
import com.huneng.data.BaseData;
import com.huneng.data.MyJson;
import com.huneng.data.SkillData;
import com.huneng.data.WorkData;
import com.huneng.paint.*;
import com.huneng.resume.SimpleInputDialog.OnInputListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class AxisAnalyseResume extends View {
	private int width, height;
	private MyJson data;
	private Rect lineRect, baseRect, axisRect, rectRect;
	private int biWidth, biHeight;
	private double cx, cy, radius;
	private int angleFlag;
	private AxisDisplay ax;
	private TextDisplay remarks;
	private LineAxis lineAx;
	private HistogramAxis rectAx;
	private List<Histogram> rects;
	private List<Line> lines;
	private List<Text> texts, nodeMark;
	private List<Arc> arcs;
	private Paint mPaint, tPaint;
	private Bitmap photo;
	private List<SkillData> skills;
	int color[] = { 0xff000000, 0xff000055, 0xff0000aa, 0xff005500, 0xff005555,
			0xff0055aa, 0xff00aa00, 0xff00aa55, 0xff00aaaa, 0xff550000,
			0xff550055, 0xff5500aa, 0xff555500, 0xff555555, 0xff5555aa,
			0xff55aa00, 0xff55aa55, 0xff55aaaa, 0xffaa0000, 0xffaa0055,
			0xffaa00aa, 0xffaa5500, 0xffaa5555, 0xffaa55aa, 0xffaaaa00,
			0xffaaaa55, 0xffaaaaaa };
	int colorIndex;
	Context c;

	public AxisAnalyseResume(Context context) {
		super(context);
		c = context;
	}

	public static AxisAnalyseResume createAxisAnalyseResume(Context context,
			int width, int height) {
		return createAxisAnalyseResume(context, width, height, null);
	}

	public static AxisAnalyseResume createAxisAnalyseResume(Context context,
			int width, int height, MyJson data) {
		AxisAnalyseResume resume = new AxisAnalyseResume(context);
		resume.data = data;
		resume.width = width;
		resume.height = height;

		return resume;

	}

	public void init() {
		initPaintFeild();

		int start = data.basedata.starttime;
		int end = data.basedata.endtime;
		initAxis(end - start - 1, 10, start, end);

		lines = new LinkedList<Line>();
		nodeMark = new LinkedList<Text>();

		tPaint = new Paint();
		tPaint.setColor(Color.BLACK);
		setText(data);

		colorIndex = 0;
		mPaint = new Paint();

		skills = data.skills;
		setRect(data.works);
		setArcAngle(data.skills);
		initPicture(data.picturePath);

	}

	private void initPicture(String picturePath) {
		biWidth = biHeight = 80;
		photo = BitmapFactory.decodeFile(picturePath);
		if (photo == null)
			photo = BitmapFactory.decodeResource(getResources(),
					R.drawable.people);
		photo = Bitmap.createScaledBitmap(photo, biWidth, biHeight, true);
	}

	private void initAxis(int xCount, int yCount, int start, int end) {
		int unit_w = width / xCount;

		rectAx = new HistogramAxis(rectRect.left, rectRect.top, rectRect.right,
				rectRect.bottom, xCount, yCount, start, 0);
		ax = new AxisDisplay(axisRect, unit_w, start);
		lineAx = new LineAxis(lineRect.left, lineRect.top, lineRect.right,
				lineRect.bottom, xCount, yCount, start, 0);
	}

	private void initPaintFeild() {
		int level[] = { 200, 500, 750, 780, 1030 };
		baseRect = new Rect(0, 0, width / 2, level[0] - 5);
		remarks = new TextDisplay(width / 2, 0, width, level[0] - 5);
		cx = width / 2;
		cy = (level[0] + level[1]) / 2;
		radius = cy - level[0] - 25;
		rectRect = new Rect(0, level[1], width, level[2]);
		axisRect = new Rect(0, level[2], width, level[3]);
		lineRect = new Rect(0, level[3], width, level[4]);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		mPaint.setColor(Color.RED);
		canvas.drawRect(baseRect, mPaint);
		mPaint.setColor(Color.YELLOW);
		canvas.drawRect(remarks.left, remarks.top, remarks.right,
				remarks.bottom, mPaint);

		ax.draw(canvas);

		canvas.drawBitmap(photo, 10, 10, null);
		for (int i = 0; i < arcs.size(); i++) {
			arcs.get(i).draw(canvas);
		}
		for (int i = 0; i < rects.size(); i++) {
			rects.get(i).draw(canvas);
		}
		for (int i = 0; i < lines.size(); i++) {
			lines.get(i).draw(canvas);
		}
		for (int i = 0; i < texts.size(); i++) {
			texts.get(i).draw(canvas);
		}
		for (int i = 0; i < nodeMark.size(); i++) {
			nodeMark.get(i).draw(canvas);
		}
		remarks.draw(canvas);
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
			sweepAngle = p[i] * 360;

			Arc arc = new Arc();
			arc.startAngle = startAngle;
			arc.sweepAngle = sweepAngle;
			arc.cx = cx;
			arc.cy = cy;
			arc.radius = radius;
			colorChange();
			arc.paint = new Paint(mPaint);
			arc.text = skills.get(i).skillname;
			arcs.add(arc);
			if (startAngle + sweepAngle > 180 && startAngle < 180)
				angleFlag = i;
			startAngle += sweepAngle;
		}

	}

	private void setText(MyJson data) {
		texts = new LinkedList<Text>();
		tPaint.setTextSize(15);
		BaseData base = data.basedata;
		Text t = new Text(base.name + "    " + base.sex);
		t.setLocation(120, 30);
		t.setPaint(tPaint);
		texts.add(t);

		t = new Text(base.birth);
		t.setLocation(120, 60);
		t.setPaint(tPaint);
		texts.add(t);

		t = new Text(base.address);
		t.setLocation(15, 150);
		t.setPaint(tPaint);
		texts.add(t);

		t = new Text(base.phone);
		t.setLocation(15, 175);
		t.setPaint(tPaint);
		texts.add(t);

		remarks.add(base.salary);
		remarks.add(base.job);
		remarks.add(base.holiday);
		for (int i = 0; i < data.remarks.size(); i++)
			remarks.add(data.remarks.get(i));

	}

	private void setRect(List<WorkData> works) {
		rects = new LinkedList<Histogram>();
		tPaint.setTextSize(10);
		lines.clear();
		for (int i = 0; i < works.size(); i++) {
			WorkData child = works.get(i);
			workToRect(child);
		}
	}

	void displaySkill(int index) {
		lines.clear();
		nodeMark.clear();
		mPaint.setColor(arcs.get(index).paint.getColor());
		SkillData skill = skills.get(index);
		for (int i = 0; i < skill.length - 1; i++) {
			PointF p1 = lineAx.inverseMap(skill.starttime + i, skill.scores[i]);
			PointF p2 = lineAx.inverseMap(skill.starttime + i + 1,
					skill.scores[i + 1]);
			Line line = new Line(p1.x, p1.y, p2.x, p2.y, mPaint);
			lines.add(line);

			Text t = new Text("" + (skill.starttime + i) + ','
					+ skill.scores[i]);
			t.setLocation(p1.x, p1.y + 20);
			t.setPaint(tPaint);
			nodeMark.add(t);
		}
		PointF p = lineAx.inverseMap(skill.starttime + skill.length - 1,
				skill.scores[skill.length - 1]);
		Text t = new Text("" + (skill.starttime + skill.length - 1) + ','
				+ skill.scores[skill.length - 1]);
		t.setLocation(p.x, p.y + 20);
		t.setPaint(tPaint);
		nodeMark.add(t);
		invalidate();
	}

	int arcContains(double x, double y) {
		double r = (x - cx) * (x - cx) + (y - cy) * (y - cy);
		r = Math.sqrt(r);
		if (r >= radius)
			return -1;
		double vx = x - cx;
		double vy = cy - y;
		double v1 = Math.sqrt(vx * vx + vy * vy);
		double v2 = 1;
		double t = vx * 1 + vy * 0;
		double cos = t / (v1 * v2);
		double PI2 = 2 * 3.14;
		if (y > cy) {
			for (int i = 0; i < angleFlag; i++) {
				double cos1 = Math.cos(arcs.get(i).startAngle / 360 * PI2);
				double cos2 = Math.cos(arcs.get(i + 1).startAngle / 360 * PI2);
				if (cos <= cos1 && cos >= cos2)
					return i;
			}
			return angleFlag;
		} else {
			double cos1 = -1;
			double cos2 = Math.cos(arcs.get(angleFlag + 1).startAngle / 360
					* PI2);
			if (cos >= cos1 && cos <= cos2)
				return angleFlag;
			for (int i = angleFlag + 1; i < arcs.size() - 1; i++) {
				cos1 = Math.cos(arcs.get(i).startAngle / 360 * PI2);
				cos2 = Math.cos(arcs.get(i + 1).startAngle / 360 * PI2);
				if (cos >= cos1 && cos <= cos2)
					return i;
			}
			return arcs.size() - 1;
		}
	}

	private void colorChange() {
		mPaint.setColor(color[colorIndex++]);
		colorIndex %= color.length;
	}

	void workToRect(WorkData work) {
		int time1 = work.begintime;
		int time2 = work.endtime;
		int score = work.score;
		TimeScore timescore1 = new TimeScore(time1 / 100, time1 % 100, score);
		TimeScore timescore2 = new TimeScore(time2 / 100, time2 % 100, score);
		PointF point = rectAx.map(timescore1);
		float x1 = point.x;
		float y1 = point.y;
		point = rectAx.map(timescore2);
		float x2 = point.x;
		colorChange();
		rects.add(new Histogram(x1, y1, x2, rectAx.bottom, mPaint));
		texts.add(new Text(work.workname + work.company).setPaint(tPaint)
				.setLocation(x1 + 5, y1 - 10));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (remarks.contains(x, y)) {
				new SimpleInputDialog(c, inputlistener).show();
				return true;
			}
			int i = arcContains(x, y);
			if (i >= 0) {
				displaySkill(i);
			}
		}
		return true;
	}

	OnInputListener inputlistener = new OnInputListener() {
		@Override
		public void inputFinish(String str) {
			remarks.add(str);
			data.remarks.add(str);
			invalidate();
		}
	};
}
