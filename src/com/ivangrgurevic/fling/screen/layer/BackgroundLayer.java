package com.ivangrgurevic.fling.screen.layer;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;

import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.Screen;

public class BackgroundLayer extends Layer {
	private final float BORDER_STROKE_WIDTH;
	private final int YELLOW = Color.rgb(255, 175, 0);

	private Paint paintBorder;

	public BackgroundLayer(Screen screen, Graphics graphics) {
		super(screen, graphics);
		
		BORDER_STROKE_WIDTH = (float) ((graphics.getWidth()*0.005 < 2) ? 2 : graphics.getWidth()*0.005); // because less than 2 pixels don't display nicely
		
		paintBorder = new Paint();
		paintBorder.setColor(YELLOW);
		paintBorder.setStyle(Paint.Style.STROKE);
		paintBorder.setStrokeWidth(BORDER_STROKE_WIDTH);
	}

	@Override
	public void draw(float deltaTime) {
		graphics.drawColor(Color.BLACK);

		graphics.drawLine(0, 0, 0, graphics.getHeight(), paintBorder);
		graphics.drawLine(graphics.getWidth(), 0, graphics.getWidth(), graphics.getHeight(), paintBorder);
	}

	@Override
	public void update(List<TouchEvent> touchEvents, float deltaTime) {
		
	}

}
