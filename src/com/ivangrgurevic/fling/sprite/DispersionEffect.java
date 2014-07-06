package com.ivangrgurevic.fling.sprite;

import android.graphics.Paint;

import com.ivangrgurevic.fling.assets.SpriteAssets;
import com.ivangrgurevic.fling.framework.Graphics;

public class DispersionEffect extends Sprite {
	private boolean isDone = false;
	private int numPoints;
	private float[] points;
	private float[] pointsVelocity;
	private Paint paint;
	private int alpha = 255;
	private final int ALPHA_DECREMENT = 2;
	
	public DispersionEffect(float x, float y, double vx, double vy, float radius, int color, int numPoints, SpriteAssets spriteAssets, Graphics graphics) {
		super(vx, vy, spriteAssets, graphics);
		
		this.numPoints = numPoints;
		
		points = new float[numPoints];
		pointsVelocity = new float[numPoints];
		
		double t;
		double u;
		double r;
		
		for(int i=0;i<numPoints/2;i++) {
			t = 2 * Math.PI * Math.random();
			u = Math.random() * 2;
			r = (u > 1) ? (2 - u) : u;
						
			points[2*i] = (float)(x + (r * Math.cos(t) * radius));
			points[2*i+1] = (float)(y + (r * Math.sin(t) * radius));
			
			pointsVelocity[2*i] = (float)(Math.random()*2-1);
			pointsVelocity[2*i+1] = (float)(Math.random()*2-1);
		}	
		
		paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(spriteAssets.getDispersionStrokeWidth());
		paint.setColor(color);
	}

	public DispersionEffect(float x, float y, double vx, double vy, float width, float height, int color, int numPoints, SpriteAssets spriteAssets, Graphics graphics) {
		super(vx, vy, spriteAssets, graphics);
		
		this.numPoints = numPoints;
		
		points = new float[numPoints];
		pointsVelocity = new float[numPoints];
		
		for(int i=0;i<numPoints/2;i++) {
			points[2*i] = (float)(x+(Math.random()*(width*2)-radius));
			points[2*i+1] = (float)(y+(Math.random()*(height*2)-radius));
			
			pointsVelocity[2*i] = (float)(Math.random()*2-1);
			pointsVelocity[2*i+1] = (float)(Math.random()*2-1);
		}	
		
		paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(spriteAssets.getDispersionStrokeWidth());
		paint.setColor(color);
	}

	
	@Override
	public void draw(float deltaTime) {
		GRAPHICS.drawPoints(points, paint);				
	}
	
	@Override
	public void update(float deltaTime) {
		for(int i=0;i<numPoints/2;i++) {
			points[2*i] += pointsVelocity[2*i] + vx + (float)(Math.random()*2-1);
			points[2*i+1] += pointsVelocity[2*i+1] + vy + (float)(Math.random()*2-1);
		}
		
		if(!isDone) {
			alpha -= ALPHA_DECREMENT;
			
			if(alpha < 0) {
				alpha = 0;
				isDone = true;
			}
			else {
				paint.setAlpha(alpha);
			}
		}
	}
	
	public boolean isDone() {
		return isDone;
	}
}
