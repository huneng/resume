package com.huneng.paint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Arc {
	public double startAngle;
	public double sweepAngle;
	public double radius;
	public double cx, cy;
	public String text;
	public Paint paint;

	public Arc() {
		startAngle = sweepAngle = 0;
		radius = 0;
		paint = new Paint();
	}


	public PointF TextPosition() {
		PointF p = new PointF();
		double t = radius + 20;
		double angle = startAngle + sweepAngle/2;
		angle /= 360;
		angle = angle * 2 * 3.14;
		p.x = (float) (cx + t * Math.cos(angle));
		p.y = (float) (cy + t * Math.sin(angle));
		return p;
	}

	public void draw(Canvas canvas) {
		RectF rect = new RectF();
		rect.left = (float) (cx - radius);
		rect.top = (float) (cy - radius);
		rect.right = (float) (cx + radius);
		rect.bottom = (float) (cy + radius);
		canvas.drawArc(rect, (float) startAngle, (float) sweepAngle, true,
				paint);
		PointF p = TextPosition();
		Paint tPaint = new Paint();
		tPaint.setTextSize(10);
		tPaint.setColor(Color.BLACK);
		canvas.drawText(text, p.x, p.y, tPaint);
	}
}
