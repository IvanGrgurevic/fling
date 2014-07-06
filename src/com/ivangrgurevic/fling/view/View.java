package com.ivangrgurevic.fling.view;

import java.util.List;

import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;

public abstract class View {
	protected Graphics g;
	protected Screen screen;
	
	public View(Screen screen, Graphics g) {
		this.screen = screen;
		this.g = g;
	}
	
	public abstract void draw(float deltaTime);
	public abstract void update(List<TouchEvent> touchEvents, float deltaTime);
}
