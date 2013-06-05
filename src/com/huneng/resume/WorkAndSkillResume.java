package com.huneng.resume;

import java.util.LinkedList;
import java.util.List;

import com.huneng.data.BaseData;
import com.huneng.data.MyJson;
import com.huneng.data.SkillData;
import com.huneng.data.WorkData;
import com.huneng.paint.*;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.View;

public class WorkAndSkillResume extends View {
	private int width, height;
	private MyJson data;

	public WorkAndSkillResume(Context context) {
		super(context);
	}

	public static WorkAndSkillResume createWorkAndSkillResume(Context context,
			int width, int height) {
		return createWorkAndSkillResume(context, width, height, null);
	}

	public static WorkAndSkillResume createWorkAndSkillResume(Context context,
			int width, int height, MyJson data) {
		WorkAndSkillResume resume = new WorkAndSkillResume(context);
		resume.width = width;
		resume.height = height;
		resume.data = data;
		return resume;
	}

	List<Histogram> rects;
	List<Line> lines;
	List<Text> texts;
	AxisDisplay axis;
	HistogramAxis rectAx;
	LineAxis lineAx;
	Paint mPaint, tPaint;
	Rect textsRec;
	int color[] = { 0xff000000, 0xff000055, 0xff0000aa, 0xff005500, 0xff005555,
			0xff0055aa, 0xff00aa00, 0xff00aa55, 0xff00aaaa, 0xff550000,
			0xff550055, 0xff5500aa, 0xff555500, 0xff555555, 0xff5555aa,
			0xff55aa00, 0xff55aa55, 0xff55aaaa, 0xffaa0000, 0xffaa0055,
			0xffaa00aa, 0xffaa5500, 0xffaa5555, 0xffaa55aa, 0xffaaaa00,
			0xffaaaa55, 0xffaaaaaa };
	int colorIndex;

	public void init() {
		int start = data.basedata.starttime;
		int end = data.basedata.endtime;
		int t = height / 2;
		t -= 15;
		int xCount = end - start;
			
		int level[] = { 300, 330, 530 };
		axis = new AxisDisplay(0, level[0], width, level[1], width / xCount,
				start);
		rectAx = new HistogramAxis(0, level[0] - 200, width, level[0], xCount,
				10, start, 0);
		lineAx = new LineAxis(0, level[1], width, level[2], xCount, 10, start,
				0);
		t = level[2];
		if (height - level[2] > 200)
			t = height - 200;
		textsRec = new Rect(0, t, width, height);
		colorIndex = 0;
		mPaint = new Paint();
		mPaint.setTextSize(10);
		tPaint = new Paint();
		tPaint.setTextSize(10);
		tPaint.setColor(Color.BLACK);
		colorChange();
		lines = new LinkedList<Line>();
		texts = new LinkedList<Text>();
		rects = new LinkedList<Histogram>();
		setLines(data.skills);
		setRects(data.works);
		setTexts(data.basedata, data.remarks);
	}

	private void setTexts(BaseData basedata, List<String> remarks) {
		int top = textsRec.top;

		Paint p = new Paint();
		p.setTextSize(10);
		p.setColor(Color.BLACK);
		texts.add(new Text("我需要的工作是 " + basedata.job).setLocation(20, top + 30)
				.setPaint(p));
		texts.add(new Text("我需要的薪水 " + basedata.salary + " 每月").setLocation(20,
				top + 50).setPaint(p));
		texts.add(new Text("我的假期" + basedata.holiday + "每星期").setLocation(20,
				top + 70).setPaint(p));
		for (int i = 0; i < remarks.size(); i++) {
			texts.add(new Text(remarks.get(i)).setLocation(width / 2,
					top + 20 + i * 20).setPaint(p));
		}

	}

	void colorChange() {
		mPaint.setColor(color[colorIndex]);
		colorIndex++;
		colorIndex %= 27;
	}

	private void setRects(List<WorkData> works) {

		for (int i = 0; i < works.size(); i++) {
			workToRect(works.get(i));
		}
	}

	void workToRect(WorkData work) {
		colorChange();
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
		rects.add(new Histogram(x1, y1, x2, rectAx.bottom, mPaint));
		texts.add(new Text(work.workname).setPaint(tPaint).setLocation(x1,
				y1 - 10));
		texts.add(new Text(work.company).setPaint(tPaint).setLocation(x1, y1));
	}

	private void setLines(List<SkillData> skills) {
		for (int j = 0; j < skills.size(); j++) {
			colorChange();
			int x0 = 50 + j * 25;
			int y0 = lineAx.bottom;
			SkillData skill = skills.get(j);
			for (int i = 0; i < skill.length - 1; i++) {
				PointF p1 = lineAx.inverseMap(skill.starttime + i,
						skill.scores[i]);
				PointF p2 = lineAx.inverseMap(skill.starttime + i + 1,
						skill.scores[i + 1]);
				Line line = new Line(p1.x, p1.y, p2.x, p2.y, mPaint);
				lines.add(line);

				Text t = new Text("" + skill.starttime + ',' + skill.scores[i]);
				t.setLocation(p1.x, p1.y + 20);
				t.setPaint(tPaint);
				texts.add(t);
			}
			PointF p = lineAx.inverseMap(skill.starttime + skill.length - 1,
					skill.scores[skill.length - 1]);
			Text t = new Text("" + skill.starttime + ','
					+ skill.scores[skill.length - 1]);
			t.setLocation(p.x, p.y + 10);
			t.setPaint(tPaint);
			texts.add(t);

			t = new Text(skill.skillname).setLocation(x0, y0);
			t.setPaint(mPaint);
			texts.add(t);

		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		axis.draw(canvas);
		mPaint.setARGB(190, 255, 0, 0);
		canvas.drawRect(textsRec, mPaint);
		int size = lines.size();
		for (int i = 0; i < size; i++) {
			lines.get(i).draw(canvas);
		}

		size = rects.size();
		for (int i = 0; i < size; i++) {
			rects.get(i).draw(canvas);
		}

		size = texts.size();
		for (int i = 0; i < size; i++) {
			texts.get(i).draw(canvas);
		}

		super.onDraw(canvas);
	}

}
