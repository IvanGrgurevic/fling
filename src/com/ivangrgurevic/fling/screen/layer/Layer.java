package com.ivangrgurevic.fling.screen.layer;

import java.util.List;

import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;

public abstract class Layer {
	protected Graphics graphics;
	protected Screen screen;
	
	public Layer(Screen screen, Graphics graphics) {
		this.screen = screen;
		this.graphics = graphics;
	}
	
	public abstract void draw(float deltaTime);
	public abstract void update(List<TouchEvent> touchEvents, float deltaTime);
}
