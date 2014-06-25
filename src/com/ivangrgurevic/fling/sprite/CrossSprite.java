package com.ivangrgurevic.fling.sprite;

import android.graphics.Color;
import android.graphics.Paint;

import com.ivangrgurevic.fling.assets.SpriteAssets;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Image;

public class CrossSprite extends Sprite {
	private Image spriteImage;
	private float spriteImageCenter;
	private int color;
	private Paint paint;
	
	public CrossSprite(double vx, SpriteAssets spriteAssets, Graphics graphics) {
		super(vx, 0, spriteAssets, graphics);
		
		spriteImage = spriteAssets.getCrossSpriteImage();
		spriteImageCenter = spriteImage.getWidth()/2;

		radius = spriteAssets.getSmallSpriteRadius();
		color = Color.rgb(255,0,0);
		
		if(Math.random() < 0.5) {
			x = GRAPHICS.getWidth()+radius;
			this.vx = (Math.random()+1)*-1;
		}
		else {
			x = -radius;
			this.vx = Math.random()+1;
		}
		
		y = (float)(Math.random()*(GRAPHICS.getHeight()*0.7) + radius);
		
		paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
	}

	@Override
	public void draw(float deltaTime) {
		GRAPHICS.drawImage(spriteImage, getCenterX(), getCenterY(), paint);
	}
	
	@Override
	public void move(float deltaTime) {
		x += vx*deltaTime;
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
