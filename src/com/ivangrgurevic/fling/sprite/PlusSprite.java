package com.ivangrgurevic.fling.sprite;

import android.graphics.Color;
import android.graphics.Paint;

import com.ivangrgurevic.fling.assets.SpriteAssets;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Image;

public class PlusSprite extends Sprite {
	private Image spriteImage;
	private float spriteImageCenter;
	private int color;
	private Paint paint;
	
	public PlusSprite(double vx, double vy, SpriteAssets spriteAssets, Graphics graphics) {
		super(vx, vy, spriteAssets, graphics);
		
		spriteImage = spriteAssets.getPlusSpriteImage();
		spriteImageCenter = spriteImage.getWidth()/2;

		radius = spriteAssets.getSmallSpriteRadius();
		color = Color.rgb(0,175,255);
		
		x = (float) (Math.random()*(GRAPHICS.getWidth()-radius*2)+radius);
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
		x += vx*deltaTime;
		y += vy*deltaTime;
		
		if((x+radius) > GRAPHICS.getWidth()) {
			x = GRAPHICS.getWidth() - radius;
			vx *= -1;
		}
		else if((x-radius) < 0) {
			x = radius;
			vx *= -1;			
		}
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
