package com.ivangrgurevic.fling.screen;

import android.graphics.Color;
import android.graphics.Paint;

import com.ivangrgurevic.fling.assets.GameAssets;
import com.ivangrgurevic.fling.framework.Game;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.framework.implementation.AndroidGame;
import com.ivangrgurevic.fling.util.GameTheme;
import com.ivangrgurevic.game.R;

public class SplashLoadingScreen extends Screen {
	// splash
	private int alpha = 255;
	private int x;
	private int y;
	private Paint paintSplash;

	private boolean loaded;
	private GameAssets gameAssets;
		
	private final String AUTHOR;

	public SplashLoadingScreen(Game game) {
		super(game);
		
		AUTHOR = ((AndroidGame)game).getResources().getString(R.string.author);
		
		Graphics g = game.getGraphics();
				
		// splash
		x = g.getWidth() / 2;
		y = g.getHeight() / 2;
	
		paintSplash = new Paint();
		paintSplash.setTextSize(32);
		paintSplash.setTextAlign(Paint.Align.CENTER);
		paintSplash.setAntiAlias(true);
		paintSplash.setColor(GameTheme.YELLOW);
		
		loaded = false;
	}

	@Override
	public void update(float deltaTime) {		
		if(!loaded) {
			Graphics g = game.getGraphics();
			gameAssets = new GameAssets(g);
	
			// This is how you would load a sound if you had one.
			// Assets.click = game.getAudio().createSound("explode.ogg");
			
			loaded = true;
		}
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
				
		g.drawColor(Color.BLACK);

		alpha -= 2;
		
		if(alpha <= 0) {
			alpha = 0;
			game.setScreen(new GameScreen(game, gameAssets));			
		}
		
		paintSplash.setAlpha(alpha);
		g.drawString(AUTHOR, x, y, paintSplash);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {

	}
}
