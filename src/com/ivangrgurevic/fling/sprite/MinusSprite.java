package com.ivangrgurevic.fling.sprite;

import android.graphics.Paint;

import com.ivangrgurevic.fling.assets.GameAssets;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Image;
import com.ivangrgurevic.fling.util.GameTheme;

public class MinusSprite extends Sprite {
	private Image spriteImage;
	private float spriteImageCenter;
	private int color;
	private Paint paint;

	public MinusSprite(double vx, double vy, GameAssets spriteAssets, Graphics graphics) {
		super(vx, vy, spriteAssets, graphics);
		
		spriteImage = spriteAssets.getMinusSpriteImage();
		spriteImageCenter = spriteImage.getWidth()/2;

		radius = spriteAssets.getSmallSpriteRadius();
		color = GameTheme.YELLOW;
		
		x = (float)(Math.random()*(GRAPHICS.getWidth()-radius)+radius*2);
		y = -radius;
		
		paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
	}

	@Override
	public void draw(float deltaTime) {
		GRAPHICS.drawImage(spriteImage, getCenterX(), getCenterY(), paint);
	}
	
	@Override
	public void update(float deltaTime) {
		x += vx * deltaTime;
		y += vy * deltaTime;
		
		if((x+radius) > GRAPHICS.getWidth()) {
			x = GRAPHICS.getWidth() - radius;
			vx *= -1;
		}
		else if((x-radius) < 0) {
			x = radius;
			vx *= -1;			
		}
	}
	
	@Override
	public float getRadius() {
		return radius;
	}
	
	public float getCenterX() {
		return x - spriteImageCenter;
	}
	
	public float getCenterY() {
		return y - spriteImageCenter;
	}
	
	public int getColor() {
		return color;
	}
}
