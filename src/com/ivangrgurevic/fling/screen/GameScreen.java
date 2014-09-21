package com.ivangrgurevic.fling.screen;

import java.util.List;

import android.content.Context;
import android.os.Vibrator;

import com.ivangrgurevic.fling.assets.GameAssets;
import com.ivangrgurevic.fling.framework.Game;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.implementation.AndroidGame;
import com.ivangrgurevic.fling.screen.layer.BackgroundLayer;
import com.ivangrgurevic.fling.screen.layer.GameOverLayer;
import com.ivangrgurevic.fling.screen.layer.GamePlayLayer;
import com.ivangrgurevic.fling.screen.layer.PausedLayer;
import com.ivangrgurevic.fling.util.GATracker;
import com.ivangrgurevic.fling.util.GameStats;

public class GameScreen extends Screen {
	private enum GameState {
		PLAYING, PAUSED, OVER
	}

	private GameState state = GameState.PLAYING;

	private Vibrator vibrator;

	private GameAssets gameAssets;
	private Graphics graphics;
	
	private BackgroundLayer backgroundLayer;
	private GamePlayLayer gamePlayLayer;
	private PausedLayer pausedLayer;
	private GameOverLayer gameOverLayer;
		
	public GameScreen(Game game, GameAssets gameAssets) {
		super(game);

		graphics = this.game.getGraphics();
		this.gameAssets = gameAssets;
		
		vibrator = (Vibrator)((Context)game).getSystemService(Context.VIBRATOR_SERVICE);
		
		backgroundLayer = new BackgroundLayer(this, graphics, gameAssets);
		gamePlayLayer = new GamePlayLayer(this, graphics, gameAssets, game);
		pausedLayer = new PausedLayer(this, graphics, game);
		gameOverLayer = null;
		
		GATracker.sendScreen((AndroidGame)game, "com.ivangrgurevic.fling.screen.layer.GamePlayLayer");
	}
	
	//=============================================================== UPDATE
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		if (state == GameState.PLAYING) {
			backgroundLayer.update(touchEvents, deltaTime);
			gamePlayLayer.update(touchEvents, deltaTime);
			
			if (gamePlayLayer.isGameOver()) {
				GameStats.setScore((AndroidGame) game, gamePlayLayer.getPoints());
				this.gameOver();
				vibrator.vibrate(50);
			}
		}
		else if (state == GameState.PAUSED) {
			backgroundLayer.update(touchEvents, deltaTime);
			pausedLayer.update(touchEvents, deltaTime);
		}
		else if (state == GameState.OVER) {
			if(gameOverLayer == null) {
				gameOverLayer = new GameOverLayer(this, graphics, (AndroidGame) game, GameStats.getScore((AndroidGame)game), gamePlayLayer.getPoints(), gameAssets, gamePlayLayer.getPlayerSprite(), gamePlayLayer.getMinusSprites(), gamePlayLayer.getDispersionEffects());
			}
			
			backgroundLayer.update(touchEvents, deltaTime);
			gameOverLayer.update(touchEvents, deltaTime);
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
		}
		else if (state == GameState.OVER) {
			if(gameOverLayer == null) {
				gameOverLayer = new GameOverLayer(this, graphics, (AndroidGame) game, GameStats.getScore((AndroidGame)game), gamePlayLayer.getPoints(), gameAssets, gamePlayLayer.getPlayerSprite(), gamePlayLayer.getMinusSprites(), gamePlayLayer.getDispersionEffects());
			}
			
			backgroundLayer.draw(deltaTime);
			gameOverLayer.draw(deltaTime);
		}
	}

	//=============================================================== OTHER
	private void nullify() {
		state = null;
		graphics = null;
		backgroundLayer = null;
		gamePlayLayer = null;
		pausedLayer = null;
		gameOverLayer = null;
		
		System.gc();
	}
	
	@Override
	public void pause() {
		if (state == GameState.PLAYING) {
			state = GameState.PAUSED;
			GATracker.sendScreen((AndroidGame)game, "com.ivangrgurevic.fling.screen.layer.PausedLayer");
		}
	}

	@Override
	public void resume() {
		if (state == GameState.PAUSED) {
			state = GameState.PLAYING;
			GATracker.sendScreen((AndroidGame)game, "com.ivangrgurevic.fling.screen.layer.GamePlayLayer");
		}
	}

	private void gameOver() {
		state = GameState.OVER;
		GameStats.incrementGamesPlayed((AndroidGame)game);
		GATracker.sendScreen((AndroidGame)game, "com.ivangrgurevic.fling.screen.layer.GameOverLayer");
	}
	
	@Override
	public void dispose() {
		nullify();		
	}

	@Override
	public void backButton() {
		if (state == GameState.PAUSED || state == GameState.OVER) {
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		
		pause();   
	}
	
	public void newGame() {
		game.setScreen(new GameScreen(game, gameAssets));
	}
}
