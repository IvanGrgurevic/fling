package com.ivangrgurevic.fling.screen;

import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.widget.Toast;

import com.ivangrgurevic.fling.assets.SpriteAssets;
import com.ivangrgurevic.fling.framework.Game;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.implementation.AndroidGame;
import com.ivangrgurevic.fling.screen.layer.BackgroundLayer;
import com.ivangrgurevic.fling.screen.layer.GameOverLayer;
import com.ivangrgurevic.fling.screen.layer.GamePlayLayer;
import com.ivangrgurevic.fling.screen.layer.MenuBarLayer;
import com.ivangrgurevic.fling.screen.layer.PausedLayer;
import com.ivangrgurevic.game.R;

public class GameScreen extends Screen {
	private enum GameState {
		PLAYING, PAUSED, OVER
	}

	private GameState state = GameState.PLAYING;

	private SpriteAssets spriteAssets;
	private Graphics graphics;
	
	private BackgroundLayer backgroundLayer;
	private MenuBarLayer menuBarLayer;
	private GamePlayLayer gamePlayLayer;
	private PausedLayer pausedLayer;
	private GameOverLayer gameOverLayer;
		
	public GameScreen(Game game, SpriteAssets spriteAssets) {
		super(game);
		
        graphics = this.game.getGraphics();
		this.spriteAssets = spriteAssets;
				
		backgroundLayer = new BackgroundLayer(this, graphics);
		gamePlayLayer = new GamePlayLayer(this, graphics, spriteAssets);
		pausedLayer = new PausedLayer(this, graphics);
		menuBarLayer = new MenuBarLayer(this, graphics);
		gameOverLayer = null;
	}

	//=============================================================== UPDATE
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		if (state == GameState.PLAYING) {
			gamePlayLayer.update(touchEvents, deltaTime);
			
			if (gamePlayLayer.getLives() <= 0) {
				state = GameState.OVER;
			}
		}
		else if (state == GameState.PAUSED) {
			pausedLayer.update(touchEvents, deltaTime);
			menuBarLayer.update(touchEvents, deltaTime);
		}
		else if (state == GameState.OVER) {
			if(gameOverLayer == null) {
				gameOverLayer = new GameOverLayer(this, graphics, spriteAssets, gamePlayLayer.getPlayerSprite(), gamePlayLayer.getMinusSprites(), /*gamePlayLayer.getPlusSprites(),*/ gamePlayLayer.getDispersionEffects());
			}
			
			gameOverLayer.update(touchEvents, deltaTime);
			menuBarLayer.update(touchEvents, deltaTime);
		}
	}
	
	//=============================================================== PAINT
	@Override
	public void paint(float deltaTime) {		
		if (state == GameState.PLAYING) {
			backgroundLayer.draw(deltaTime);
			gamePlayLayer.draw(deltaTime);
		}
		else if (state == GameState.PAUSED) {
			backgroundLayer.draw(deltaTime);
			gamePlayLayer.draw(deltaTime);
			pausedLayer.draw(deltaTime);
			menuBarLayer.draw(deltaTime);
		}
		else if (state == GameState.OVER) {
			if(gameOverLayer == null) {
				gameOverLayer = new GameOverLayer(this, graphics, spriteAssets, gamePlayLayer.getPlayerSprite(), gamePlayLayer.getMinusSprites(), /*gamePlayLayer.getPlusSprites(),*/ gamePlayLayer.getDispersionEffects());
			}
			
			backgroundLayer.draw(deltaTime);
			//gamePlayLayer.draw(deltaTime);
			gameOverLayer.draw(deltaTime);
			//menuBarLayer.draw(deltaTime);
		}
	}

	//=============================================================== OTHER
	private void nullify() {
		// DO NOT NULLIFY 'spriteAssets'
		// spriteAssets = null; 

		state = null;

		graphics = null;
		
		backgroundLayer = null;
		//menuBarLayer = null;
		gamePlayLayer = null;
		pausedLayer = null;
		gameOverLayer = null;
		
		// Call garbage collector to clean up memory.
		System.gc();
	}
	
	@Override
	public void pause() {
		if (state == GameState.PLAYING) {
			state = GameState.PAUSED;
		}
	}

	@Override
	public void resume() {
		if (state == GameState.PAUSED) {
			state = GameState.PLAYING;
		}
	}

	@Override
	public void dispose() {
		nullify();		
	}

	@Override
	public void backButton() { // figure out what to do about this...
		if (state == GameState.PAUSED || state == GameState.OVER) {
			//nullify(); // causes game to crash
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		
		pause();   

        Toast.makeText((AndroidGame)game, R.string.press_back, Toast.LENGTH_SHORT).show(); // might want to remove this...
	}
	
	public void newGame() {
		game.setScreen(new GameScreen(game, spriteAssets));
	}
	
	private void browserTwitterIntent() { // should be moved to another class // maybe it should stay here
		Resources res = ((AndroidGame) game).getResources();
		String url = String.format(res.getString(R.string.twitter_intent_url), gamePlayLayer.getLevel());
		((AndroidGame) game).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));	
	}
}
