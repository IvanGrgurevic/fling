package com.ivangrgurevic.fling.util;

import com.ivangrgurevic.fling.framework.Input.TouchEvent;

public class Range {
	public static boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1)
			return true;
		else
			return false;
	}

	public static boolean inBounds(TouchEvent event, float x, float y, float radius) {
		double deltaX = event.x - x;
		double deltaY = event.y - y;
		
		double rad = Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
		
		if (rad < radius)
			return true;
		else
			return false;
	}

}
