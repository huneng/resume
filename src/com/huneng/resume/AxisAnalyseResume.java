package com.huneng.resume;

import java.util.LinkedList;
import java.util.List;

import com.huneng.data.MyJson;
import com.huneng.data.SkillData;
import com.huneng.data.WorkData;
import com.huneng.paint.Histogram;
import com.huneng.paint.Line;
import com.huneng.paint.MyColor;
import com.huneng.paint.Text;
import com.huneng.paint.TimeAxis;
import com.huneng.paint.TimeScore;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import android.view.View;

public class AxisAnalyseResume extends View {

	int colorIndex;
	Context c;
	private MyJson data;
	private int width;
	private int height;
	private PointF axis_pos;
	private int axis_height;
	private TimeAxis axis;
	private List<List<Line>> skill_lines;
	private List<Histogram> work_rects;
	private List<Text> text1;
	private List<Text> text2;
	private List<Line> textLink;

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

	int color[];

	public void init() {
		color = MyColor.color;
		colorIndex = 0;
		axis_height = 30;
		axis = new TimeAxis(data.basedata.starttime, data.basedata.endtime,
				new Point(width, axis_height));
		axis_pos = new PointF(0, height / 2 - axis_height / 2);
		axis.setYUnit(20);
		int skill_text_y_pos = (int) (axis_pos.y - 11 * axis_height);
		skill_lines = new LinkedList<List<Line>>();
		List<SkillData> skills = data.skills;
		List<WorkData> works = data.works;
		List<PointF> points = new LinkedList<PointF>();
		text1 = new LinkedList<Text>();
		text2 = new LinkedList<Text>();
		textLink = new LinkedList<Line>();
		for (int i = 0; i < skills.size(); i++) {
			points.clear();
			SkillData skill = skills.get(i);
			for (int j = 0; j < skill.length; j++) {
				TimeScore value = new TimeScore(skill.starttime + j, 6,
						skill.scores[j]);
				points.add(axis.timeScoreMap(value));
			}
			List<Line> lines = new LinkedList<Line>();
			Paint paint = createPaint();
			paint.setStrokeWidth(2);
			for (int j = 0; j < points.size() - 1; j++) {
				lines.add(new Line(points.get(j).x + axis_pos.x, axis_pos.y
						- points.get(j).y, points.get(j + 1).x + axis_pos.x,
						axis_pos.y - points.get(j + 1).y, paint));
			}
			paint.setTextSize(20);
			Text text = new Text();
			text.x = axis_pos.x + i * 50 + 40;
			text.y = skill_text_y_pos;
			text.data = skill.skillname;
			text.paint = paint;
			text1.add(text);

			Paint p = new Paint(paint);
			p.setAlpha(200);
			p.setStrokeWidth(1);
			Line l = new Line(text.x, text.y, points.get(0).x, axis_pos.y-points.get(0).y, p);
			textLink.add(l);
			skill_lines.add(lines);
		}
		work_rects = new LinkedList<Histogram>();
		Paint tp = new Paint();
		tp.setColor(Color.BLACK);

		for (int i = 0; i < works.size(); i++) {
			WorkData work = works.get(i);
			TimeScore value1 = new TimeScore(work.begintime / 100,
					work.begintime % 100, work.score);
			TimeScore value2 = new TimeScore(work.endtime / 100,
					work.endtime % 100, work.score);
			PointF point1 = axis.timeScoreMap(value1);
			PointF point2 = axis.timeScoreMap(value2);
			Histogram his = new Histogram();
			his.x1 = point1.x + axis_pos.x;
			his.y1 = axis_pos.y + axis_height;
			his.x2 = point2.x + axis_pos.x;
			his.y2 = point2.y + axis_pos.y + axis_height;
			his.paint = createPaint();
			work_rects.add(his);
			Text text = new Text();
			text.x = his.x1;
			text.y = his.y2 + 20;
			text.data = work.workname + ',' + work.company;
			text.paint = tp;
			text2.add(text);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		axis.drawAxis(canvas, axis_pos.x, axis_pos.y);
		for (int i = 0; i < skill_lines.size(); i++) {
			for (int j = 0; j < skill_lines.get(i).size(); j++) {
				List<Line> lines = skill_lines.get(i);
				lines.get(j).draw(canvas);
			}
		}
		for (int i = 0; i < work_rects.size(); i++) {
			work_rects.get(i).draw(canvas);
		}
		for (int i = 0; i < text1.size(); i++) {
			text1.get(i).draw(canvas);
		}
		for(int i = 0; i < text2.size(); i++){
			text2.get(i).draw(canvas);
		}
		for(int i = 0; i < textLink.size(); i++){
			textLink.get(i).draw(canvas);
		}
		super.onDraw(canvas);
	}

	private Paint createPaint() {
		Paint paint = new Paint();
		paint.setColor(color[colorIndex++]);

		if (colorIndex == color.length)
			colorIndex = 0;
		return paint;
	}
}
