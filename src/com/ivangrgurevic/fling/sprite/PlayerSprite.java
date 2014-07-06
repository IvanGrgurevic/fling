package com.ivangrgurevic.fling.sprite;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.ivangrgurevic.fling.assets.SpriteAssets;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Image;

public class PlayerSprite extends Sprite {
	private boolean isTouched = false;
	private float startX;
	private float startY;
	private float stationX;
	private float stationY;
	private boolean spawning = true; 
	private int spawningRadius = 0;
	private int SPAWNING_RATE = 5;
	private int bounces = 0;
	private final int BOUNCE_MAX = 1;
	private Paint paint;
	private Path arrow;
	private double deltaX;
	private double deltaY;
	private double rad;
	private double px;
	private double py;
	private float degrees;
	private int negativeArmX;
	private int positiveArmX;
	private int positiveArmY;
	private Image stationImage;
	private float arrowRadius;
	private float pullRadius;
	private float stationRaduis;
	
	public PlayerSprite(double vx, double vy, SpriteAssets spriteAssets, Graphics graphics) {
		super(vx, vy, spriteAssets, graphics);

		x = spriteAssets.getBigSpriteX();
		y = spriteAssets.getBigSpriteY();
		
		radius = this.spriteAssets.getBigSpriteRadius();
		stationRaduis = this.spriteAssets.getStationRadius();
		arrowRadius = this.spriteAssets.getStationRadius() - this.spriteAssets.getStationStrokeWidth() -1;
		
		pullRadius = arrowRadius - radius;
		
		startX = x;
		startY = y;
		
		paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);

		stationImage = this.spriteAssets.getStationImage();
		stationX = startX - stationImage.getWidth()/2;
		stationY = startY - stationImage.getHeight()/2;
	}

	@Override
	public void draw(float deltaTime) {
		// station
		GRAPHICS.drawImage(stationImage, stationX, stationY);

		// aim arrow
		if(!isTouched) {			
			deltaX = startX - x;
			deltaY = startY - y;
			rad = Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
			
			degrees = (float)(Math.atan2(deltaY, deltaX)*(180/Math.PI)+90);

			px = ((deltaX/rad)*arrowRadius) + startX;
			py = ((deltaY/rad)*arrowRadius) + startY;
			
			negativeArmX = (int) (px - spriteAssets.getArrowArmLength());
			positiveArmX = (int) (px + spriteAssets.getArrowArmLength());
			positiveArmY = (int) (py + spriteAssets.getArrowArmLength());
			
			arrow = new Path();
			arrow.setLastPoint(negativeArmX, positiveArmY); // left arm
			arrow.lineTo((int) px, (int) py); // center
			arrow.lineTo(positiveArmX, positiveArmY); // right arm
			
			paint.setStrokeWidth(spriteAssets.getBigSpriteStrokeWidth());
			paint.setColor(Color.rgb(0, 175, 255));
			
			GRAPHICS.save();
			GRAPHICS.rotate(degrees, (int)px ,(int)py);
			GRAPHICS.drawPath(arrow, paint);
			GRAPHICS.restore();
		}
		
		// player sprite
		if(spawning) {
			paint.setStrokeWidth((float)(spawningRadius*0.1));
			paint.setColor(Color.WHITE);
			GRAPHICS.drawCircle(x, y, spawningRadius, paint);
		}
		else {
			paint.setColor(Color.WHITE);
			GRAPHICS.drawCircle(x, y, radius, paint);
		}
	}
	
	@Override
	public void update(float deltaTime) {
		if(spawning) {
			spawningRadius += SPAWNING_RATE;
			
			if(spawningRadius >= radius) {
				spawningRadius = 0;
				spawning = false;
				isTouched = false;
			}
		}
		else {
			x += vx*deltaTime;
			y += vy*deltaTime;
			
			if(bounces >= BOUNCE_MAX) {
				if((x-radius) > GRAPHICS.getWidth()) {
					reset();
				}
				else if((x+radius) < 0) {
					reset();
				}
			}
			else {
				if((x+radius) > GRAPHICS.getWidth()) {
					x = GRAPHICS.getWidth() - radius;
					vx *= -1;
					if(isTouched)
						bounces++;
				}
				else if((x-radius) < 0) {
					x = radius;
					vx *= -1;			
					if(isTouched)
						bounces++;
				}
			}
			
			if((y+radius) < 0) {
				reset();
			}
			else if((y-radius) > GRAPHICS.getHeight()) {
				reset();
			}
		}
		

	}
	
	private void reset() {
		y = startY;
		x = startX;
		vy = 0;
		vx = 0;
		
		bounces = 0;
		spawning = true;
		spawningRadius = 0;
	}
	
	public void setXY(int x, int y) {
		double dX = startX - x;
		double dY = startY - y;
		double r = Math.sqrt((dX*dX)+(dY*dY));
		
		if(r > pullRadius) {
			this.x = (int) ((dX/r)*(-pullRadius) + startX);
			this.y = (int) ((dY/r)*(-pullRadius) + startY);
		}
		else {
			this.x = x;
			this.y = y;
		}
	}
	
	public float getStartX() {
		return startX;
	}

	public float getStartY() {
		return startY;
	}

	public boolean isTouched() {
		return isTouched;
	}
	
	public boolean isSpawning() {
		return spawning;
	}

	public void setIsTouched(boolean isTouched) {
		this.isTouched = isTouched;
	}
	
	public int getBounces() {
		return bounces;
	}
	
	public float getOuterRadius() {
		return stationRaduis;
	}
}
