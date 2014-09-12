package com.ivangrgurevic.fling.screen;

import java.util.List;

import android.content.Context;
import android.os.Vibrator;
import android.widget.Toast;

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
import com.ivangrgurevic.fling.util.Score;
import com.ivangrgurevic.game.R;

public class GameScreen extends Screen {
	private enum GameState {
		PLAYING, PAUSED, OVER
	}

	private GameState state = GameState.PLAYING;

	private Vibrator vibrator;

	private GameAssets gameAssets;
	private Graphics graphics;
	
	private BackgroundLayer backgroundLayer;
	//private MenuBarLayer menuBarLayer;
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
		//menuBarLayer = new MenuBarLayer(this, graphics);
		gameOverLayer = null;
	}

	//=============================================================== UPDATE
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		if (state == GameState.PLAYING) {
			backgroundLayer.update(touchEvents, deltaTime);
			gamePlayLayer.update(touchEvents, deltaTime);
			
			if (gamePlayLayer.isGameOver()) {
				Score.set((Context)game, gamePlayLayer.getPoints());
				state = GameState.OVER;
				vibrator.vibrate(50);
			}
		}
		else if (state == GameState.PAUSED) {
			backgroundLayer.update(touchEvents, deltaTime);
			pausedLayer.update(touchEvents, deltaTime);
			//menuBarLayer.update(touchEvents, deltaTime);
		}
		else if (state == GameState.OVER) {
			if(gameOverLayer == null) {
				gameOverLayer = new GameOverLayer(this, graphics, Score.get((Context)game), gamePlayLayer.getPoints(), gameAssets, gamePlayLayer.getPlayerSprite(), gamePlayLayer.getMinusSprites(), gamePlayLayer.getDispersionEffects());
			}
			
			backgroundLayer.update(touchEvents, deltaTime);
			gameOverLayer.update(touchEvents, deltaTime);
			//menuBarLayer.update(touchEvents, deltaTime);
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
			//menuBarLayer.draw(deltaTime);
		}
		else if (state == GameState.OVER) {
			if(gameOverLayer == null) {
				gameOverLayer = new GameOverLayer(this, graphics, Score.get((Context)game), gamePlayLayer.getPoints(), gameAssets, gamePlayLayer.getPlayerSprite(), gamePlayLayer.getMinusSprites(), gamePlayLayer.getDispersionEffects());
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
		game.setScreen(new GameScreen(game, gameAssets));
	}
	
	/*private void browserTwitterIntent() { // should be moved to another class // maybe it should stay here
		Resources res = ((AndroidGame) game).getResources();
		String url = String.format(res.getString(R.string.twitter_intent_url), gamePlayLayer.getPoints());
		((AndroidGame) game).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));	
	}*/
}
