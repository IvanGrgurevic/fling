package com.ivangrgurevic.fling.screen.layer;

import java.util.List;

import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.screen.GameScreen;

public class GameOverLayer extends Layer {

	public GameOverLayer(Screen screen, Graphics g) {
		super(screen, g);

	}

	@Override
	public void draw(float deltaTime) {
		
	}

	@Override
	public void update(List<TouchEvent> touchEvents, float deltaTime) {
		int len = touchEvents.size();
		for (int i=0;i<len;i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				((GameScreen)screen).newGame();
			}
		}

	}

}
