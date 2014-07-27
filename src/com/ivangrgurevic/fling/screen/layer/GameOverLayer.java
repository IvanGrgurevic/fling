package com.ivangrgurevic.fling.screen.layer;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

import com.ivangrgurevic.fling.assets.GameAssets;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.screen.GameScreen;
import com.ivangrgurevic.fling.sprite.DispersionEffect;
import com.ivangrgurevic.fling.sprite.MinusSprite;
import com.ivangrgurevic.fling.sprite.PlayerSprite;
import com.ivangrgurevic.fling.util.GameTheme;

public class GameOverLayer extends Layer {
	private ArrayList<DispersionEffect> dispersionArr;
	
	public GameOverLayer(Screen screen, Graphics g, GameAssets gameAssets, PlayerSprite playerSprite, ArrayList<MinusSprite> minusSprites, ArrayList<DispersionEffect> dispersionEffects) {
		super(screen, g);

		dispersionArr = new ArrayList<DispersionEffect>();
		
		// convert all sprites to dispersion effects
		for(DispersionEffect effect : dispersionEffects) {
			dispersionArr.add(effect);
		}

		//border
		dispersionArr.add(new DispersionEffect(0, 0, 0, 0, graphics.getWidth(), graphics.getHeight(), Color.WHITE, 2000, gameAssets, graphics));				

		
		// player
		dispersionArr.add(new DispersionEffect(playerSprite.getX(), playerSprite.getY(), 0, 0, playerSprite.getRadius(), Color.WHITE, 80, gameAssets, graphics));				
		dispersionArr.add(new DispersionEffect(playerSprite.getStartX(), playerSprite.getStartY(), 0, 0, playerSprite.getOuterRadius(), GameTheme.BLUE, 500, gameAssets, graphics));				
		
		// minus sprite
		for(MinusSprite sprite : minusSprites) {
			dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY(), sprite.getRadius(), sprite.getColor(), 80, gameAssets, graphics));
		}
	}
	
	@Override
	public void draw(float deltaTime) {
		if(dispersionArr.isEmpty()) {
			graphics.drawRect(0, 0, 500, 500, Color.BLUE);
		}
		else {
			for(DispersionEffect effect : dispersionArr) {
				effect.draw(deltaTime);
			}
		}
	}

	@Override
	public void update(List<TouchEvent> touchEvents, float deltaTime) {
		if(dispersionArr.isEmpty()) {
			updateTouchEvents(touchEvents);
		}
		else {
			for(int i=0;i<dispersionArr.size();i++) {
				DispersionEffect effect = dispersionArr.get(i);
				effect.update(deltaTime);
				
				if(effect.isDone()) {
					dispersionArr.remove(i);
					i--;
				}
			}
		}
	}

	private void updateTouchEvents(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i=0;i<len;i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				((GameScreen)screen).newGame();
			}
		}

	}
}
