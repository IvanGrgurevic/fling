package com.ivangrgurevic.fling.screen.layer;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.Screen;

public class PausedLayer extends Layer {

	private Paint paint;
	
	public PausedLayer(Screen screen, Graphics graphics) {
		super(screen, graphics);

		paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setAlpha(100);
	}

	@Override
	public void draw(float deltaTime) {
		graphics.drawRect(new RectF(0, 0, graphics.getWidth(), graphics.getHeight()), paint);
	}

	@Override
	public void update(List<TouchEvent> touchEvents, float deltaTime) {
		updateTouchEvents(touchEvents);

	}

	private void updateTouchEvents(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		
		for (int i=0;i<len;i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				screen.resume();
			}
		}
	}
	
}
