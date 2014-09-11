package com.ivangrgurevic.fling.screen.layer;

import java.util.List;

import android.graphics.Color;

import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.Screen;

public class MenuBarLayer extends Layer {	
	private int x;
	private int y;
	private int width;
	private int height;
	
	
	public MenuBarLayer(Screen screen, Graphics graphics) {
		super(screen, graphics);
		
		x = 0;
		y = 0;
		width = graphics.getWidth();
		height = graphics.getHeightPercentile(0.1);
	}

	@Override
	public void draw(float deltaTime) {
		graphics.drawRect(x, y, width, height, Color.RED);
	}

	@Override
	public void update(List<TouchEvent> touchEvents, float deltaTime) {
		
	}
}
