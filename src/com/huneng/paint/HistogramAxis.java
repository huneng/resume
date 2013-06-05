package com.huneng.paint;

import android.graphics.PointF;

public class HistogramAxis {
	public int left, right, top, bottom;
	public float unit_w, unit_h;
	public int xCount, yCount;
	public int x0, y0;
	public HistogramAxis(int l, int t, int r, int b, int ux, int uy, int x, int y){
		left = l;
		right = r;
		bottom = b;
		top = t;
		xCount = ux;
		yCount = uy;
		unit_w = ((float)r-l)/xCount;
		unit_h = ((float)b-t)/yCount;
		x0 = x;
		y0 = y;
	}

	public boolean contains(float x, float y){
		if(x>left&&x<right&&y<bottom&&y>top)
			return true;
		return false;
				
	}
	
	public PointF map(TimeScore t){
		float x, y;
		x = t.year - x0;
		x += ((float)(t.month - 1)) / 12;
		y = yCount-(t.score - y0);

		x *= unit_w;
		y *= unit_h;
		x += left;
		y += top;
		PointF point = new PointF(x, y);
		return point;
	}
}
