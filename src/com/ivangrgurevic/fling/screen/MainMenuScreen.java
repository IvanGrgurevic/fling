package com.ivangrgurevic.fling.screen;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.widget.Toast;

import com.ivangrgurevic.fling.assets.SpriteAssets;
import com.ivangrgurevic.fling.framework.Game;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.implementation.AndroidGame;
import com.ivangrgurevic.game.R;

public class MainMenuScreen extends Screen {
	Paint paint = new Paint();
	private boolean backPressedOnce;
	private SpriteAssets spriteAssets;

	public MainMenuScreen(Game game, SpriteAssets spriteAssets) {
		super(game);
		this.spriteAssets = spriteAssets;
		backPressedOnce = false;
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		for (int i=0;i<touchEvents.size();i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {

				if (inBounds(event, 0, 0, g.getWidth(), g.getHeight())) {
					game.setScreen(new GameScreen(game, spriteAssets));
				}

			}
		}
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();

		// border
		g.drawColor(Color.rgb(0, 175, 255));
		g.drawRect(2, 2, g.getWidth()-4, g.getHeight()-4, Color.BLACK);

		// start text
		paint.setTextSize(100);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);

		g.drawString("Start", g.getWidth() / 2, g.getHeight() / 2, paint);
		
		// new DispersionEffect(node.getX(), node.getY(), node.getVX(), node.getVY()*-1, node.getRadius(), node.getColor(), 80, spriteAssets, g)
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
	/*
	 *  
	 */
	public void backButton() {
		if (backPressedOnce)
			android.os.Process.killProcess(android.os.Process.myPid());
		
		backPressedOnce = true;
		
        Toast.makeText((AndroidGame)game, R.string.press_back, Toast.LENGTH_SHORT).show();
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            	backPressedOnce = false;   
            }
        }, 2000);
	}
}