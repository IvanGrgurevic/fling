package com.ivangrgurevic.fling.screen;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;

import com.ivangrgurevic.fling.assets.SpriteAssets;
import com.ivangrgurevic.fling.framework.Game;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.implementation.AndroidGame;
import com.ivangrgurevic.fling.sprite.DispersionEffect;
import com.ivangrgurevic.fling.sprite.MinusSprite;
import com.ivangrgurevic.fling.sprite.PlayerSprite;
import com.ivangrgurevic.fling.sprite.PlusSprite;
import com.ivangrgurevic.game.R;

public class GameScreen extends Screen {
	private enum GameState {
		PLAYING, PAUSED, OVER
	}

	private GameState state = GameState.PLAYING;
	
	private PlayerSprite playerSprite;
	private boolean playerSpriteSelected = false;
	private ArrayList<MinusSprite> minusSpriteArr;
	private ArrayList<PlusSprite> plusSpriteArr;
	private ArrayList<DispersionEffect> dispersionArr;
	private Vibrator vibrator;
	private final int MAX_NODE_NUM = 50;
	private final double NODE_SPEED_INCREMENT;
	private int spriteNum;
	private double spriteSpeed;
	private int level = 0;	
	private final int LEVEL_X;
	private final int LEVEL_Y;
	private final float LEVEL_TEXT_SIZE;
	private final float BORDER_STROKE_WIDTH;
	private Graphics g;
	private int livesLeft = 3;
	private final int MAX_LIVES = 3;
	private final float LIVES_DISTANCE;
	private final float LIVES_Y;
	private final float LIVES_RADIUS;
	private final float LIVES_HOLLOW_RADIUS;
	private final int LIVES_STROKE_WIDTH;
	
	private final float LIFE_START_RADIUS;
	private final float LIFE_STROKE_WIDTH;
	private final float LIFE_SPACE;

	private final int LEVEL_AND_LIVES_COLOR;
	private final double SPRITE_CREATION_RATIO = 0.8;

	
	private Paint paintLevel, paintBorder, paintLives, paintLivesHollow;
	private Paint paint2, paint; // find better names and should be fucking changed
	private int YELLOW = Color.rgb(255, 175, 0);
	private int BLUE = Color.rgb(0, 175, 255);
	private SpriteAssets spriteAssets;
	
	private boolean backPressedOnce; // should be moved to another class
	
	public GameScreen(Game game, SpriteAssets spriteAssets) {
		super(game);
		this.spriteAssets = spriteAssets;
		
		g = game.getGraphics();
		
		Context ctx = (AndroidGame)game;
		vibrator = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
		
		// level
		LEVEL_X = g.getWidth()/2;
		LEVEL_Y = g.getHeight()/4;
		
		// player sprite
		Graphics g = game.getGraphics();

		playerSprite = new PlayerSprite(0, 0, spriteAssets, g);

		// sprites
		minusSpriteArr = new ArrayList<MinusSprite>();
		plusSpriteArr = new ArrayList<PlusSprite>();
				
		spriteNum = 0;
		spriteSpeed = this.spriteAssets.getSmallSpriteSpeed();
		NODE_SPEED_INCREMENT = spriteSpeed*0.2;

		// dispersion effect
		dispersionArr = new ArrayList<DispersionEffect>();
		
		// constants for lives
		LIVES_DISTANCE = g.getWidth()/(MAX_LIVES+1);
		LIVES_Y = g.getHeight()/3;
		LIVES_RADIUS = (float) (g.getWidth()*0.025);
		LIVES_HOLLOW_RADIUS = (float) (g.getWidth()*0.03);
		LIFE_START_RADIUS = this.spriteAssets.getLifeStartRadius();
		LIFE_STROKE_WIDTH = this.spriteAssets.getLifeStrokeWidth();
		LIFE_SPACE = this.spriteAssets.getLifeSpace();
		
		// constants for paint objects
		LEVEL_AND_LIVES_COLOR = Color.rgb(60,60,60);
		LEVEL_TEXT_SIZE = g.getHeight()/4;
		LIVES_STROKE_WIDTH = 2;
		BORDER_STROKE_WIDTH = (float) ((g.getWidth()*0.005 < 2) ? 2 : g.getWidth()*0.005);
		
		// paint objects
		Typeface typeFace = Typeface.create("Droid Sans Mono", Typeface.NORMAL);
		paintLevel = new Paint();
		paintLevel.setTypeface(typeFace);
		paintLevel.setTextSize(LEVEL_TEXT_SIZE);
		paintLevel.setTextAlign(Paint.Align.CENTER);
		paintLevel.setAntiAlias(true);
		paintLevel.setColor(LEVEL_AND_LIVES_COLOR);

		paintLives = new Paint();
		paintLives.setFlags(Paint.ANTI_ALIAS_FLAG);
		paintLives.setStyle(Paint.Style.FILL);
		paintLives.setColor(LEVEL_AND_LIVES_COLOR);
		paintLives.setStrokeWidth(LIFE_STROKE_WIDTH);

		paintLivesHollow = new Paint();
		paintLivesHollow.setFlags(Paint.ANTI_ALIAS_FLAG);
		paintLivesHollow.setStyle(Paint.Style.STROKE);
		paintLivesHollow.setColor(LEVEL_AND_LIVES_COLOR);
		paintLivesHollow.setStrokeWidth(LIVES_STROKE_WIDTH);
		
		paintBorder = new Paint();
		paintBorder.setColor(YELLOW);
		paintBorder.setStyle(Paint.Style.STROKE);
		paintBorder.setStrokeWidth(BORDER_STROKE_WIDTH);

		// TO CHANGE
		paint = new Paint();
		paint.setTextSize(30);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		
		paint2 = new Paint();
		paint2.setTextSize(100);
		paint2.setTextAlign(Paint.Align.CENTER);
		paint2.setAntiAlias(true);
		paint2.setColor(Color.WHITE);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		if (state == GameState.PLAYING)
			updatePlaying(touchEvents, deltaTime);
		if (state == GameState.PAUSED)
			updatePaused(touchEvents);
		if (state == GameState.OVER)
			updateGameOver(touchEvents);
	}
	
	private void updatePlaying(List<TouchEvent> touchEvents, float deltaTime) {
		updatePlayingTouchEvents(touchEvents);

		updatePlayingSprites(deltaTime);
		
		updatePlayingSpriteArrays();
		
		if (livesLeft <= 0)
			state = GameState.OVER;
		
		checkLevel();
	}
	
	private void updatePlayingSpriteArrays() {
		// minus
		for(int i=0;i<minusSpriteArr.size();i++) {
			MinusSprite sprite = minusSpriteArr.get(i);
			
			double playerDeltaX = sprite.getX() - playerSprite.getX();
			double playerDeltaY = sprite.getY() - playerSprite.getY();
			double stationDeltaX = sprite.getX() - playerSprite.getStartX();
			double stationDeltaY = sprite.getY() - playerSprite.getStartY();
			
			double playerRad = Math.sqrt((playerDeltaX*playerDeltaX)+(playerDeltaY*playerDeltaY));
			double stationRad = Math.sqrt((stationDeltaX*stationDeltaX)+(stationDeltaY*stationDeltaY));
			
			if(stationRad < (sprite.getRadius()+playerSprite.getOuterRadius())) {
				minusSpriteArr.remove(i);
				i--;
				dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX()*-1, sprite.getVY()*-1, sprite.getRadius(), sprite.getColor(), 80, spriteAssets, g));				
				removeLife();
			}
			else if(playerRad < (sprite.getRadius()+playerSprite.getRadius()) && playerSprite.isTouched() && !playerSprite.isSpawning()) {
				minusSpriteArr.remove(i);
				i--;
				dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY(), sprite.getRadius(), sprite.getColor(), 80, spriteAssets, g));
			}
			else if((sprite.getY()-sprite.getRadius()) > g.getHeight()) {
				minusSpriteArr.remove(i);
				i--;
				//dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY()*-1, sprite.getRadius(), sprite.getColor(), 80, spriteAssets, g));				
				//removeLife();
			}
		}
		
		// plus
		for(int i=0;i<plusSpriteArr.size();i++) {
			PlusSprite sprite = plusSpriteArr.get(i);
			
			double playerDeltaX = sprite.getX() - playerSprite.getX();
			double playerDeltaY = sprite.getY() - playerSprite.getY();
			double stationDeltaX = sprite.getX() - playerSprite.getStartX();
			double stationDeltaY = sprite.getY() - playerSprite.getStartY();
			
			double playerRad = Math.sqrt((playerDeltaX*playerDeltaX)+(playerDeltaY*playerDeltaY));
			double stationRad = Math.sqrt((stationDeltaX*stationDeltaX)+(stationDeltaY*stationDeltaY));
			
			if(stationRad < (sprite.getRadius()+playerSprite.getOuterRadius())) {
				plusSpriteArr.remove(i);
				i--;
				dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY(), sprite.getRadius(), sprite.getColor(), 80, spriteAssets, g));
				addLife();
			}
			else if(playerRad < (sprite.getRadius()+playerSprite.getRadius()) && playerSprite.isTouched() && !playerSprite.isSpawning()) {
				plusSpriteArr.remove(i);
				i--;
				dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY(), sprite.getRadius(), sprite.getColor(), 80, spriteAssets, g));
				//addLife();
			}
			else if((sprite.getY()-sprite.getRadius()) > g.getHeight()) {
				plusSpriteArr.remove(i);
				i--;
			}
		}

		// dispersion
		for(int i=0;i<dispersionArr.size();i++) {
			DispersionEffect effect = dispersionArr.get(i);
			
			if(effect.isDone()) {
				dispersionArr.remove(i);
				i--;
			}
		}
	}
	
	private void updatePlayingTouchEvents(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (!playerSprite.isTouched() && inBounds(event, playerSprite.getX(), playerSprite.getY(), playerSprite.getRadius()+playerSprite.VELOCITY_MAX)) {
					playerSprite.setXY(event.x, event.y);
					playerSpriteSelected = true;
				}
			}
			
			if (event.type == TouchEvent.TOUCH_DRAGGED) {
				if (!playerSprite.isTouched() && playerSpriteSelected) {
					playerSprite.setXY(event.x, event.y);
				}
			}
			
			if (event.type == TouchEvent.TOUCH_UP) {
				if (playerSpriteSelected) {
					playerSprite.setIsTouched(true);
					playerSpriteSelected = false;
					
					double deltaX = playerSprite.getStartX() - playerSprite.getX();
					double deltaY = playerSprite.getStartY() - playerSprite.getY();

					playerSprite.setVX(deltaX);
					playerSprite.setVY(deltaY);
				}
			}
		}
	}
	
	private void updatePlayingSprites(float deltaTime) {
		playerSprite.move(deltaTime);
		
		for(MinusSprite sprite : minusSpriteArr)
			sprite.move(deltaTime);

		for(PlusSprite sprite : plusSpriteArr)
			sprite.move(deltaTime);

		for(DispersionEffect sprite : dispersionArr)
			sprite.move();
	}

	private void updatePaused(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i=0;i<len;i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 0, 0, 800, 240)) {
					if (!inBounds(event, 0, 0, 35, 35)) {
						resume();
					}
				}
			}
		}
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i=0;i<len;i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				newGame();
			}
		}

	}

	private void checkLevel() {
		if(minusSpriteArr.size() == 0 && plusSpriteArr.size() == 0) {
			changeLevel();
			
			if(level > MAX_NODE_NUM) {
				spriteNum = MAX_NODE_NUM;
				spriteSpeed += NODE_SPEED_INCREMENT;
			}
			else {
				spriteNum = level;//(int)((level+2)/2);
			}
			
			for(int i=0;i<spriteNum;i++) {
				createSprites();
			}
		}
	}
		
	private void createSprites() {
		// speed
		double vx = Math.random()*spriteSpeed - (spriteSpeed/2);
		double vy = Math.random()*spriteSpeed + spriteSpeed;
		
		if(level > 1) {
			if(Math.random() < SPRITE_CREATION_RATIO) { 
				minusSpriteArr.add(new MinusSprite(vx, vy, spriteAssets, g));
			}
			else {
				plusSpriteArr.add(new PlusSprite(vx, vy, spriteAssets, g));
			}
		}
		else {
			minusSpriteArr.add(new MinusSprite(vx, vy, spriteAssets, g));
		}
	}

	private void changeLevel() {
		level++;
				
		// player sprite
		dispersionArr.add(new DispersionEffect(playerSprite.getX(), playerSprite.getY(), 0, 0, playerSprite.getRadius(), Color.WHITE, 80, spriteAssets, g));				
		playerSprite = new PlayerSprite(0, 0, spriteAssets, g);
		
		// minus sprite
		for(MinusSprite sprite : minusSpriteArr) {
			dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY()*-1, sprite.getRadius(), sprite.getColor(), 80, spriteAssets, g));				
		}
		minusSpriteArr.clear();
		
		// plus sprite
		for(PlusSprite sprite : plusSpriteArr) {
			dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY()*-1, sprite.getRadius(), sprite.getColor(), 80, spriteAssets, g));				
		}
		plusSpriteArr.clear();
		
		dispersionArr.add(new DispersionEffect(0, 0, 0, 0, g.getWidth(), g.getHeight(), Color.rgb(153,153,153), 1000, spriteAssets, g));
	}
	
	private void removeLife() {
		livesLeft--;
		vibrator.vibrate(50);
	}
	
	private void addLife() {
		if(livesLeft < MAX_LIVES) {
			livesLeft++;
			vibrator.vibrate(50);
			// maybe add the line below
			//dispersionArr.add(new DispersionEffect(LIVES_DISTANCE*livesLeft, LIVES_Y, 0, 0, LIVES_RADIUS, LEVEL_AND_LIVES_COLOR, 500, spriteAssets, g));
		}
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1)
			return true;
		else
			return false;
	}

	private boolean inBounds(TouchEvent event, float x, float y, float radius) {
		double deltaX = event.x - x;
		double deltaY = event.y - y;
		
		double rad = Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
		
		if (rad < radius)
			return true;
		else
			return false;
	}
	
	//=====================================================================================================================Paint
	@Override
	public void paint(float deltaTime) {
		g.drawColor(Color.BLACK);

		//border
		g.drawLine(0, 0, 0, g.getHeight(), paintBorder);
		g.drawLine(g.getWidth(), 0, g.getWidth(), g.getHeight(), paintBorder);
		
		// level
		g.drawString(String.valueOf(level), LEVEL_X, LEVEL_Y, paintLevel);
		
		
		if (state == GameState.PLAYING) {
			drawPlayingUI(deltaTime);
		}
		else if (state == GameState.PAUSED) {
			drawPlayingUI(deltaTime);
			drawPausedUI();
		}
		else if (state == GameState.OVER) {
			drawPlayingUI(deltaTime);
			drawGameOverUI();			
		}
	}

	private void drawPlayingUI(float deltaTime) {
		// lives
		for(int i=0;i<MAX_LIVES;i++) {
			g.drawCircle(LIVES_DISTANCE*(i+1), LIVES_Y, LIVES_HOLLOW_RADIUS, paintLivesHollow);
			if(i < livesLeft) {
				g.drawCircle(LIVES_DISTANCE*(i+1), LIVES_Y, LIVES_RADIUS, paintLives);
			}
		}
				
		// player
		playerSprite.draw(deltaTime);
		
		// minus sprite
		for(MinusSprite sprite : minusSpriteArr)
			sprite.draw(deltaTime);

		// plus sprite
		for(PlusSprite sprite : plusSpriteArr)
			sprite.draw(deltaTime);
		
		// dispersion effect
		for(DispersionEffect effect : dispersionArr) {
			effect.draw();
		}
	}

	// ===============================================================================================
	private void drawPausedUI() {
		// Darken the entire screen so you can display the Paused screen.
		g.drawARGB(200, 0, 0, 0);
		g.drawString("Resume", 400, 165, paint2);
		g.drawString("Menu", 400, 360, paint2);
	}

	private void drawGameOverUI() {
		g.drawARGB(200, 0, 0, 0);
		g.drawString("GAME OVER.", 400, 240, paint2);
		g.drawString("Tap to return.", 400, 290, paint);
	}
	// ===============================================================================================

	private void nullify() {
		playerSprite = null;
		playerSpriteSelected = false;
		minusSpriteArr = null;
		plusSpriteArr = null;
		dispersionArr = null;
		vibrator = null;
		spriteNum = 0;
		spriteSpeed = 0;
		g = null;
		livesLeft = 0;
		paintLevel = null;
		paintBorder = null;
		paint2 = null;
		paint = null;
		
		// DO NOT NULLIFY 'spriteAssets'
		// spriteAssets = null; 

		// Call garbage collector to clean up memory.
		System.gc();
	}
	
	@Override
	public void pause() {
		if (state == GameState.PLAYING)
			state = GameState.PAUSED;
	}

	@Override
	public void resume() {
		if (state == GameState.PAUSED)
			state = GameState.PLAYING;
	}

	@Override
	public void dispose() {
		nullify();		
	}

	@Override
	public void backButton() {
		if (state == GameState.PAUSED || state == GameState.OVER) {
			//nullify(); // causes game to crash
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		
		pause();   

        Toast.makeText((AndroidGame)game, R.string.press_back, Toast.LENGTH_SHORT).show(); // might want to remove this...
	}
	
	private void newGame() {
		game.setScreen(new GameScreen(game, spriteAssets));
	}
	
	private void browserTwitterIntent() {
		Resources res = ((AndroidGame)game).getResources();
		String url = String.format(res.getString(R.string.twitter_intent_url), level);
		((AndroidGame)game).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));	
	}
}
