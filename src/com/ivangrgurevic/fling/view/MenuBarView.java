package com.ivangrgurevic.fling.view;

import java.util.List;

import android.graphics.Color;

import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.util.Range;

public class MenuBarView extends View {	
	private int x;
	private int y;
	private int width;
	private int height;
	
	
	public MenuBarView(Screen screen, Graphics g) {
		super(screen, g);
		
		x = 0;
		y = 0;
		width = g.getWidth();
		height = g.getHeightPercentile(0.1);
	}

	public void draw(float deltaTime) {
		g.drawRect(x, y, width, height, Color.RED);
	}

	public void update(List<TouchEvent> touchEvents, float deltaTime) {
		
	}
}
