package com.ivangrgurevic.fling.screen.layer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Vibrator;

import com.ivangrgurevic.fling.assets.SpriteAssets;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.implementation.AndroidGame;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.sprite.DispersionEffect;
import com.ivangrgurevic.fling.sprite.MinusSprite;
import com.ivangrgurevic.fling.sprite.PlayerSprite;
import com.ivangrgurevic.fling.sprite.PlusSprite;
import com.ivangrgurevic.fling.util.Range;

public class GamePlayLayer extends Layer {
	
	private SpriteAssets spriteAssets;
	
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
	private int livesLeft = 3;
	private final int MAX_LIVES = 3;
	private final float LIVES_DISTANCE;
	private final float LIVES_Y;
	private final float LIVES_RADIUS;
	private final float LIVES_HOLLOW_RADIUS;
	private final int LIVES_STROKE_WIDTH;
	
	private final int LEVEL_AND_LIVES_COLOR;
	private final double SPRITE_CREATION_RATIO = 0.8;

	private Paint paintLevel, paintLives, paintLivesHollow;

	
	public GamePlayLayer(Screen screen, Graphics graphics, SpriteAssets spriteAssets) {
		super(screen, graphics);
		
		this.spriteAssets = spriteAssets;
		
		
		Context ctx = (AndroidGame)screen.getGame();
		vibrator = (Vibrator)ctx.getSystemService(Context.VIBRATOR_SERVICE);

		playerSprite = new PlayerSprite(0, 0, spriteAssets, graphics);

		// sprite
		minusSpriteArr = new ArrayList<MinusSprite>();
		plusSpriteArr = new ArrayList<PlusSprite>();
				
		spriteNum = 0;
		spriteSpeed = this.spriteAssets.getSmallSpriteSpeed();
		NODE_SPEED_INCREMENT = spriteSpeed*0.2;

		// dispersion effect
		dispersionArr = new ArrayList<DispersionEffect>();
		
		// level
		LEVEL_X = graphics.getWidth()/2;
		LEVEL_Y = graphics.getHeight()/4;

		// constants for lives
		LIVES_DISTANCE = graphics.getWidth()/(MAX_LIVES+1);
		LIVES_Y = graphics.getHeight()/3;
		LIVES_RADIUS = (float) (graphics.getWidth()*0.025);
		LIVES_HOLLOW_RADIUS = (float) (graphics.getWidth()*0.03);
		
		// constants for paint objects
		LEVEL_AND_LIVES_COLOR = Color.rgb(60,60,60);
		LEVEL_TEXT_SIZE = graphics.getHeight()/4;
		LIVES_STROKE_WIDTH = 2;
		
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
		paintLives.setStrokeWidth(LIVES_STROKE_WIDTH);

		paintLivesHollow = new Paint();
		paintLivesHollow.setFlags(Paint.ANTI_ALIAS_FLAG);
		paintLivesHollow.setStyle(Paint.Style.STROKE);
		paintLivesHollow.setColor(LEVEL_AND_LIVES_COLOR);
		paintLivesHollow.setStrokeWidth(LIVES_STROKE_WIDTH);
		
	}

	@Override
	public void draw(float deltaTime) {		
		// level
		graphics.drawString(String.valueOf(level), LEVEL_X, LEVEL_Y, paintLevel);

		// lives
		for(int i=0;i<MAX_LIVES;i++) {
			graphics.drawCircle(LIVES_DISTANCE*(i+1), LIVES_Y, LIVES_HOLLOW_RADIUS, paintLivesHollow);
			if(i < livesLeft) {
				graphics.drawCircle(LIVES_DISTANCE*(i+1), LIVES_Y, LIVES_RADIUS, paintLives);
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
			effect.draw(deltaTime);
		}

	}

	@Override
	public void update(List<TouchEvent> touchEvents, float deltaTime) {
		updateTouchEvents(touchEvents);

		updateSprites(deltaTime);
		
		updateSpriteArrays();
				
		checkLevel();
	}

	private void updateSpriteArrays() {
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
				dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX()*-1, sprite.getVY()*-1, sprite.getRadius(), sprite.getColor(), 80, spriteAssets, graphics));				
				removeLife();
			}
			else if(playerRad < (sprite.getRadius()+playerSprite.getRadius()) && playerSprite.isTouched() && !playerSprite.isSpawning()) {
				minusSpriteArr.remove(i);
				i--;
				dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY(), sprite.getRadius(), sprite.getColor(), 80, spriteAssets, graphics));
			}
			else if((sprite.getY()-sprite.getRadius()) > graphics.getHeight()) {
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
				dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY(), sprite.getRadius(), sprite.getColor(), 80, spriteAssets, graphics));
				addLife();
			}
			else if(playerRad < (sprite.getRadius()+playerSprite.getRadius()) && playerSprite.isTouched() && !playerSprite.isSpawning()) {
				plusSpriteArr.remove(i);
				i--;
				dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY(), sprite.getRadius(), sprite.getColor(), 80, spriteAssets, graphics));
				//addLife();
			}
			else if((sprite.getY()-sprite.getRadius()) > graphics.getHeight()) {
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
	
	private void updateTouchEvents(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (!playerSprite.isTouched() && Range.inBounds(event, playerSprite.getX(), playerSprite.getY(), playerSprite.getRadius()+playerSprite.VELOCITY_MAX)) {
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
	
	private void updateSprites(float deltaTime) {
		playerSprite.update(deltaTime);
		
		for(MinusSprite sprite : minusSpriteArr)
			sprite.update(deltaTime);

		for(PlusSprite sprite : plusSpriteArr)
			sprite.update(deltaTime);

		for(DispersionEffect sprite : dispersionArr)
			sprite.update(deltaTime);
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
				minusSpriteArr.add(new MinusSprite(vx, vy, spriteAssets, graphics));
			}
			else {
				plusSpriteArr.add(new PlusSprite(vx, vy, spriteAssets, graphics));
			}
		}
		else {
			minusSpriteArr.add(new MinusSprite(vx, vy, spriteAssets, graphics));
		}
	}

	private void changeLevel() {
		level++;
				
		// player sprite
		dispersionArr.add(new DispersionEffect(playerSprite.getX(), playerSprite.getY(), 0, 0, playerSprite.getRadius(), Color.WHITE, 80, spriteAssets, graphics));				
		playerSprite = new PlayerSprite(0, 0, spriteAssets, graphics);
		
		// minus sprite
		for(MinusSprite sprite : minusSpriteArr) {
			dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY()*-1, sprite.getRadius(), sprite.getColor(), 80, spriteAssets, graphics));				
		}
		minusSpriteArr.clear();
		
		// plus sprite
		for(PlusSprite sprite : plusSpriteArr) {
			dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY()*-1, sprite.getRadius(), sprite.getColor(), 80, spriteAssets, graphics));				
		}
		plusSpriteArr.clear();
				
		dispersionArr.add(new DispersionEffect(0, 0, 0, 0, graphics.getWidth(), graphics.getHeight(), Color.rgb(153,153,153), 1000, spriteAssets, graphics));
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

	public int getLives() {
		return livesLeft;
	}
	
	public int getLevel() {
		return level;
	}
}
