package com.ivangrgurevic.fling.sprite;

import com.ivangrgurevic.fling.assets.SpriteAssets;
import com.ivangrgurevic.fling.framework.Graphics;

public abstract class Sprite {
	protected float x;
	protected float y;
	protected double vx;
	protected double vy;
	protected float radius;
	
	protected SpriteAssets spriteAssets;

	protected final Graphics GRAPHICS;
	public final double VELOCITY_PERCENTILE = 0.18;
	public final int VELOCITY_MAX;
	public final int VELOCITY_MIN = 1;


	public Sprite(double vx, double vy, SpriteAssets spriteAssets, Graphics graphics) {
		this.vx = vx;
		this.vy = vy;
		
		GRAPHICS = graphics;
		
		this.spriteAssets = spriteAssets;
	
		VELOCITY_MAX = this.spriteAssets.getMaxVelocity();
	}

	public void draw() {};
	public void draw(float deltaTime) {};

	public void move() {};
	public void move(float deltaTime) {};

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public double getVX() {
		return vx;
	}
	
	public double getVY() {
		return vy;
	}

	public float getRadius() {
		return radius;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setVX(double vx) {
		vx = vx*VELOCITY_PERCENTILE;
		if(Math.abs(vx) > VELOCITY_MAX)
			this.vx = (vx > 0) ? VELOCITY_MAX : -VELOCITY_MAX;
		else if(Math.abs(vx) < VELOCITY_MIN)
			this.vx = (vx > 0) ? VELOCITY_MIN : -VELOCITY_MIN;
		else
			this.vx = vx;
	}

	public void setVY(double vy) {
		vy = vy*VELOCITY_PERCENTILE;
		if(Math.abs(vy) > VELOCITY_MAX)
			this.vy = (vy > 0) ? VELOCITY_MAX : -VELOCITY_MAX;
		else if(Math.abs(vy) < VELOCITY_MIN)
			this.vy = (vy > 0) ? VELOCITY_MIN : -VELOCITY_MIN;
		else
			this.vy = vy;
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}
}
