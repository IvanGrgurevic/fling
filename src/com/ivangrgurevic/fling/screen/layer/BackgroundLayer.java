package com.ivangrgurevic.fling.screen.layer;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;

import com.ivangrgurevic.fling.assets.GameAssets;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.util.GameTheme;

public class BackgroundLayer extends Layer {
	private Paint paintBorderYellow;

	public BackgroundLayer(Screen screen, Graphics graphics, GameAssets gameAssets) {
		super(screen, graphics);
				
		paintBorderYellow = new Paint();
		paintBorderYellow.setColor(GameTheme.YELLOW);
		paintBorderYellow.setStyle(Paint.Style.STROKE);
		paintBorderYellow.setStrokeWidth(gameAssets.BORDER_STROKE_WIDTH);
	}

	@Override
	public void draw(float deltaTime) {
		graphics.drawColor(Color.BLACK);
		
		graphics.drawLine(0, 0, 0, graphics.getHeight(), paintBorderYellow);
		graphics.drawLine(graphics.getWidth(), 0, graphics.getWidth(), graphics.getHeight(), paintBorderYellow);
}

	@Override
	public void update(List<TouchEvent> touchEvents, float deltaTime) {

	}
}
