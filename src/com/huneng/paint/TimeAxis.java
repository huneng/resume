package com.huneng.paint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

public class TimeAxis {
	private int starttime;
	private int endtime;
	private int width, height;
	private float yUnit;
	private float xUnit;
	public TimeAxis(int start, int end, Point size) {
		starttime = start;
		endtime = end;
		width = size.x;
		height = size.y;
		xUnit = width/(endtime-starttime+1);
	}

	public void setYUnit(float unit){
		yUnit = unit;
	}
	public void drawAxis(Canvas canvas, float x, float y) {
		int t = endtime - starttime + 1;
		
		Paint paint = new Paint();

		paint.setARGB(255, 66, 0, 66);
		
		if (t < 1) {
			canvas.drawRect(x, y, x + width, y + height, paint);
			return;
		}
		
		Paint paint2 = new Paint();
		paint2.setARGB(200, 0,0,0);
		Paint paint3 = new Paint();
		paint3.setARGB(100, 0,0,0);
		for (int i = 0; i < t; i++) {
			if (i % 2 == 0) {
				canvas.drawRect(x + i * xUnit, y, x + (i + 1) * xUnit, y + height,
						paint2);
			} else {
				canvas.drawRect(x + i * xUnit, y, x + (i + 1) * xUnit, y + height,
						paint3);
			}
			canvas.drawText(""+(starttime+i), x+i*xUnit+2, y+height/4*3, paint);
		}

	}
	
	public PointF timeScoreMap(TimeScore value){
		PointF pos = new PointF();
		float t = value.year-starttime +((float)value.month)/12;
		pos.x = xUnit*t;
		pos.y = yUnit*value.score;
		return pos;
	}
	public TimeScore posMap(PointF pos){
		float t = (pos.x/xUnit);
		int year = (int) t;
		int month = (int) ((t-year)*12);
		int score = (int) (pos.y/yUnit);
		TimeScore value = new TimeScore(year, month, score);
		return value;
	}
}
