package com.ivangrgurevic.fling.screen.layer;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.ivangrgurevic.fling.assets.SpriteAssets;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.sprite.DispersionEffect;
import com.ivangrgurevic.fling.sprite.MinusSprite;
import com.ivangrgurevic.fling.sprite.PlayerSprite;
import com.ivangrgurevic.fling.util.Range;

public class GamePlayLayer extends Layer {
	
	private SpriteAssets spriteAssets;
	
	private PlayerSprite playerSprite;
	private boolean playerSpriteSelected = false;
	private ArrayList<MinusSprite> minusSpriteArr;
	private ArrayList<DispersionEffect> dispersionArr;
	private double spriteSpeed;

	private boolean gameOver = false;
	
	private int points = 0;	
	private final int LEVEL_X;
	private final int LEVEL_Y;
	private final float LEVEL_TEXT_SIZE;
	
	private final int LEVEL_COLOR;
	
	private final int SPRITE_CREATION_RATE = 120*1000;
	private float gameTime = 0;
	private final double SPRITE_CREATION_START = 0.01;

	private Paint paintLevel;

	
	public GamePlayLayer(Screen screen, Graphics graphics, SpriteAssets spriteAssets) {
		super(screen, graphics);
		
		this.spriteAssets = spriteAssets;
		
		playerSprite = new PlayerSprite(0, 0, spriteAssets, graphics);

		// sprite
		minusSpriteArr = new ArrayList<MinusSprite>();
		//plusSpriteArr = new ArrayList<PlusSprite>();
				
		spriteSpeed = this.spriteAssets.getSmallSpriteSpeed();

		// dispersion effect
		dispersionArr = new ArrayList<DispersionEffect>();
		
		// level
		LEVEL_X = graphics.getWidth()/2;
		LEVEL_Y = graphics.getHeight()/4;
		
		// constants for paint objects
		LEVEL_COLOR = Color.rgb(60,60,60);
		LEVEL_TEXT_SIZE = graphics.getHeight()/4;
		
		// paint objects
		Typeface typeFace = Typeface.create("Droid Sans Mono", Typeface.NORMAL);
		paintLevel = new Paint();
		paintLevel.setTypeface(typeFace);
		paintLevel.setTextSize(LEVEL_TEXT_SIZE);
		paintLevel.setTextAlign(Paint.Align.CENTER);
		paintLevel.setAntiAlias(true);
		paintLevel.setColor(LEVEL_COLOR);
	}

	@Override
	public void draw(float deltaTime) {		
		// level
		graphics.drawString(String.valueOf(points), LEVEL_X, LEVEL_Y, paintLevel);
		
		// player
		playerSprite.draw(deltaTime);
		
		// minus sprite
		for(MinusSprite sprite : minusSpriteArr)
			sprite.draw(deltaTime);
		
		// dispersion effect
		for(DispersionEffect effect : dispersionArr) {
			effect.draw(deltaTime);
		}
	}
	
	float deltaTimee = 0;

	@Override
	public void update(List<TouchEvent> touchEvents, float deltaTime) {
		updateTouchEvents(touchEvents);
		updateSprites(deltaTime);
		updateSpriteArrays();
		
		updateGameTime(deltaTime);
		
		checkLevel();
	}

	private void updateGameTime(float deltaTime) {
		gameTime = (gameTime + deltaTime) % SPRITE_CREATION_RATE;
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
				
				gameOver = true;
			}
			else if(playerRad < (sprite.getRadius()+playerSprite.getRadius()) && playerSprite.isTouched() && !playerSprite.isSpawning()) {
				minusSpriteArr.remove(i);
				i--;
				dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY(), sprite.getRadius(), sprite.getColor(), 80, spriteAssets, graphics));
				
				addPoint();
			}
			else if((sprite.getY()-sprite.getRadius()) > graphics.getHeight()) {
				minusSpriteArr.remove(i);
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

		for(DispersionEffect sprite : dispersionArr)
			sprite.update(deltaTime);
	}

	private void checkLevel() {
		createSprites();
	}
		
	private void createSprites() {
		double vx = Math.random()*spriteSpeed - (spriteSpeed/2);
		double vy = Math.random()*spriteSpeed + spriteSpeed;
		
		double spriteCreationProbability = SPRITE_CREATION_START+(gameTime/SPRITE_CREATION_RATE);
		
		if(Math.random() < spriteCreationProbability) {
			minusSpriteArr.add(new MinusSprite(vx, vy, spriteAssets, graphics));	
		}
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	private void addPoint() {
		points++;
	}
	
	public int getPoints() {
		return points;
	}
	
	public PlayerSprite getPlayerSprite() {
		return playerSprite;
	}

	public ArrayList<MinusSprite> getMinusSprites() {
		return minusSpriteArr;
	}
		
	public ArrayList<DispersionEffect> getDispersionEffects() {
		return dispersionArr;
	}
}
