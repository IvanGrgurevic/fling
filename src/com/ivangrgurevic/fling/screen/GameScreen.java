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

import com.ivangrgurevic.fling.assets.SpriteAssets;
import com.ivangrgurevic.fling.framework.Game;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.implementation.AndroidGame;
import com.ivangrgurevic.fling.sprite.CrossSprite;
import com.ivangrgurevic.fling.sprite.DispersionEffect;
import com.ivangrgurevic.fling.sprite.MinusSprite;
import com.ivangrgurevic.fling.sprite.PlayerSprite;
import com.ivangrgurevic.fling.sprite.PlusSprite;
import com.ivangrgurevic.game.R;

public class GameScreen extends Screen {
	private enum GameState {
		Running, Paused, GameOver
	}

	GameState state = GameState.Running;
	
	private PlayerSprite playerSprite;
	private boolean playerSpriteSelected = false;
	private ArrayList<MinusSprite> minusSpriteArr;
	private ArrayList<PlusSprite> plusSpriteArr;
	private ArrayList<CrossSprite> crossSpriteArr;
	private ArrayList<DispersionEffect> dispersionArr;
	private Vibrator vibrator;
	private final int MAX_NODE_NUM = 50;
	private final double NODE_SPEED_INCREMENT;
	private int spriteNum;
	private double spriteSpeed;
	private int level;	
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
	private final int LEVEL_AND_LIVES_COLOR;
	
	private Paint paintLevel, paintBorder, paintLives, paintLivesHollow;
	private Paint paint2, paint; // find better names and should be fucking changed
	private int YELLOW = Color.rgb(255, 175, 0);
	private int BLUE = Color.rgb(0, 175, 255);
	private SpriteAssets spriteAssets;
	
	public GameScreen(Game game, SpriteAssets spriteAssets) {
		super(game);
		this.spriteAssets = spriteAssets;
		
		g = game.getGraphics();
		
		Context ctx = (AndroidGame)game;
		vibrator = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);
		
		// level
		level = 0;
		LEVEL_X = g.getWidth()/2;
		LEVEL_Y = g.getHeight()/4;
		
		// player sprite
		Graphics g = game.getGraphics();

		playerSprite = new PlayerSprite(0, 0, spriteAssets, g);

		// sprites
		minusSpriteArr = new ArrayList<MinusSprite>();
		plusSpriteArr = new ArrayList<PlusSprite>();
		crossSpriteArr = new ArrayList<CrossSprite>();
				
		spriteNum = 0;
		spriteSpeed = this.spriteAssets.getSmallSpriteSpeed();
		NODE_SPEED_INCREMENT = spriteSpeed*0.2;

		// dispersion effect
		dispersionArr = new ArrayList<DispersionEffect>();
		
		// constants for lives
		LIVES_DISTANCE = g.getWidth()/(MAX_LIVES+1);
		LIVES_Y = g.getHeight()/3;
		LIVES_RADIUS = (float) (g.getWidth()*0.02);
		LIVES_HOLLOW_RADIUS = (float) (g.getWidth()*0.025);
		
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
		
		paintLivesHollow = new Paint();
		paintLivesHollow.setFlags(Paint.ANTI_ALIAS_FLAG);
		paintLivesHollow.setStyle(Paint.Style.STROKE);
		paintLivesHollow.setColor(LEVEL_AND_LIVES_COLOR);
		paintLivesHollow.setStrokeWidth(LIVES_STROKE_WIDTH);
		paintLivesHollow.setTextAlign(Paint.Align.CENTER);
		
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

		if (state == GameState.Running)
			updateRunning(touchEvents, deltaTime);
		if (state == GameState.Paused)
			updatePaused(touchEvents);
		if (state == GameState.GameOver)
			updateGameOver(touchEvents);
	}
	
	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		updateRunningTouchEvents(touchEvents);

		updateRunningSprites(deltaTime);
		
		updateRunningSpriteArrays();
		
		if (livesLeft <= 0)
			state = GameState.GameOver;
		
		checkLevel();
	}
	
	private void updateRunningSpriteArrays() {
		// minus
		for(int i=0;i<minusSpriteArr.size();i++) {
			MinusSprite sprite = minusSpriteArr.get(i);
			
			double deltaX = sprite.getX() - playerSprite.getX();
			double deltaY = sprite.getY() - playerSprite.getY();
			
			double rad = Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
			
			if(rad < (sprite.getRadius()+playerSprite.getRadius()) && playerSprite.isTouched() && !playerSprite.isSpawning()) {
				minusSpriteArr.remove(i);
				i--;
				dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY(), sprite.getRadius(), sprite.getColor(), 80, spriteAssets, g));
			}
			else if((sprite.getY()+sprite.getRadius()) > g.getHeight()) {
				minusSpriteArr.remove(i);
				i--;
				dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY()*-1, sprite.getRadius(), sprite.getColor(), 80, spriteAssets, g));				
				removeLife();
			}
		}
		
		// plus
		for(int i=0;i<plusSpriteArr.size();i++) {
			PlusSprite sprite = plusSpriteArr.get(i);
			
			double deltaX = sprite.getX() - playerSprite.getX();
			double deltaY = sprite.getY() - playerSprite.getY();
			
			double rad = Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
			
			if(rad < (sprite.getRadius()+playerSprite.getRadius()) && playerSprite.isTouched() && !playerSprite.isSpawning()) {
				plusSpriteArr.remove(i);
				i--;
				dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY(), sprite.getRadius(), sprite.getColor(), 80, spriteAssets, g));
				addLife();
			}
			else if((sprite.getY()-sprite.getRadius()) > g.getHeight()) {
				plusSpriteArr.remove(i);
				i--;
			}
		}

		// cross
		for(int i=0;i<crossSpriteArr.size();i++) {
			CrossSprite sprite = crossSpriteArr.get(i);
			
			double deltaX = sprite.getX() - playerSprite.getX();
			double deltaY = sprite.getY() - playerSprite.getY();
			
			double rad = Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
			
			if(rad < (sprite.getRadius()+playerSprite.getRadius()) && playerSprite.isTouched() && !playerSprite.isSpawning()) {
				crossSpriteArr.remove(i);
				i--;
				dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY(), sprite.getRadius(), sprite.getColor(), 80, spriteAssets, g));
				removeLife();
			}
			else if(((sprite.getX()-sprite.getRadius()) > g.getWidth() && sprite.getVX() > 0) || ((sprite.getX()+sprite.getRadius()) < 0 && sprite.getVX() < 0)) {
				crossSpriteArr.remove(i);
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
	
	private void updateRunningTouchEvents(List<TouchEvent> touchEvents) {
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
	
	private void updateRunningSprites(float deltaTime) {
		playerSprite.move(deltaTime);
		
		for(MinusSprite sprite : minusSpriteArr)
			sprite.move(deltaTime);

		for(PlusSprite sprite : plusSpriteArr)
			sprite.move(deltaTime);

		for(CrossSprite sprite : crossSpriteArr)
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

				if (inBounds(event, 0, 240, 800, 240)) {
					nullify();
					goToMenu();
				}
			}
		}
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i=0;i<len;i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 0, 0, 800, 480)) {
					nullify();
					goToMenu();
					return;
				}
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
			double random = Math.random();
			
			if(random > 0.05 && random < 0.9) { // minus sprite
				minusSpriteArr.add(new MinusSprite(vx, vy, spriteAssets, g));
			}
			else if(random <= 0.05 ) { // plus sprite
				plusSpriteArr.add(new PlusSprite(vx, vy, spriteAssets, g));
			}
			else { // cross sprite
				crossSpriteArr.add(new CrossSprite(vx, spriteAssets, g));
			}
		}
		else {
			minusSpriteArr.add(new MinusSprite(vx, vy, spriteAssets, g));
		}
	}

	private void changeLevel() {
		level++;
		
		// add life
		for(int i=0;i<plusSpriteArr.size();i++)
			addLife();
		
		// player sprite
		dispersionArr.add(new DispersionEffect(playerSprite.getX(), playerSprite.getY(), 0, 0, playerSprite.getRadius(), Color.WHITE, 80, spriteAssets, g));				
		playerSprite = new PlayerSprite(0, 0, spriteAssets, g);
		
		// minus sprite
		for(int i=0;i<minusSpriteArr.size();i++) {
			MinusSprite sprite = minusSpriteArr.get(i);
			
			minusSpriteArr.remove(i);
			i--;
			dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY()*-1, sprite.getRadius(), sprite.getColor(), 80, spriteAssets, g));				
		}
		
		// plus sprite
		for(int i=0;i<plusSpriteArr.size();i++) {
			PlusSprite sprite = plusSpriteArr.get(i);

			plusSpriteArr.remove(i);
			i--;
			dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), 0, 0, sprite.getRadius(), sprite.getColor(), 80, spriteAssets, g));				
		}
		
		// cross sprite
		for(int i=0;i<crossSpriteArr.size();i++) {
			CrossSprite sprite = crossSpriteArr.get(i);
			
			crossSpriteArr.remove(i);
			i--;
			dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), 0, 0, sprite.getRadius(), sprite.getColor(), 80, spriteAssets, g));
		}

		dispersionArr.add(new DispersionEffect(0, 0, 0, 0, g.getWidth(), g.getHeight(), Color.rgb(153,153,153), 1000, spriteAssets, g));
	}
	
	private void removeLife() {
		livesLeft--;
		vibrator.vibrate(50);
	}
	
	private void addLife() {
		if(livesLeft < MAX_LIVES) {
			livesLeft++;
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
		
		
		if (state == GameState.Running)
			drawRunningUI(deltaTime);
		if (state == GameState.Paused)
			drawPausedUI();
		if (state == GameState.GameOver)
			drawGameOverUI();
	}

	private void drawRunningUI(float deltaTime) {
		// lives
		for(int i=0;i<MAX_LIVES;i++) {
			g.drawCircle(LIVES_DISTANCE*(i+1), LIVES_Y, LIVES_HOLLOW_RADIUS, paintLivesHollow);
			if(i < livesLeft)
				g.drawCircle(LIVES_DISTANCE*(i+1), LIVES_Y, LIVES_RADIUS, paintLives);
		}
		
		// player
		playerSprite.draw(deltaTime);
		
		// minus sprite
		for(MinusSprite sprite : minusSpriteArr)
			sprite.draw(deltaTime);

		// plus sprite
		for(PlusSprite sprite : plusSpriteArr)
			sprite.draw(deltaTime);
		
		// cross sprite
		for(CrossSprite sprite : crossSpriteArr)
			sprite.draw(deltaTime);

		// dispersion effect
		for(DispersionEffect effect : dispersionArr) {
			effect.draw();
		}
	}

	private void drawPausedUI() {
		// Darken the entire screen so you can display the Paused screen.
		g.drawARGB(155, 0, 0, 0);
		g.drawString("Resume", 400, 165, paint2);
		g.drawString("Menu", 400, 360, paint2);
	}

	private void drawGameOverUI() {
		g.drawString("GAME OVER.", 400, 240, paint2);
		g.drawString("Tap to return.", 400, 290, paint);
	}

	private void nullify() {
		playerSprite = null;
		playerSpriteSelected = false;
		minusSpriteArr = null;
		plusSpriteArr = null;
		crossSpriteArr = null;
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
		
		//DO NOT NULLIFY 'spriteAssets'! THROWS A NULL EXCEPTION. Why? you ask. I have no damn idea.
		//spriteAssets = null; 

		// Call garbage collector to clean up memory.
		System.gc();
	}
	
	@Override
	public void pause() {
		if (state == GameState.Running)
			state = GameState.Paused;
	}

	@Override
	public void resume() {
		if (state == GameState.Paused)
			state = GameState.Running;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {
		pause();
	}

	private void goToMenu() {
		game.setScreen(new MainMenuScreen(game, spriteAssets));
	}
	
	private void browserTwitterIntent() {
		Resources res = ((AndroidGame)game).getResources();
		String url = String.format(res.getString(R.string.twitter_intent_url), level);
		((AndroidGame)game).startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));	
	}
}
