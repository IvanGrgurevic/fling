package com.ivangrgurevic.fling.assets;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;

import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Image;

public class SpriteAssets {
	private int maxVelocity;
	// big sprite
	private float bigSpriteX;
	private float bigSpriteY;
	private float bigSpriteRadius;
	private float bigSpriteStrokeWidth;
	// small sprite
	private Image minusSpriteImage;
	private Image plusSpriteImage;
	private float smallSpriteRadius;
	private float smallSpriteStrokeWidth;
	private double smallSpriteSpeed;
	private final double SPEED_PERCENTILE = 0.0008;
	// station
	private Image stationImage;
	private float stationRadius;
	private float stationStrokeWidth;
	private float arrowArmLength;
	// dispersion effect
	private int dispersionStrokeWidth;
	private final double DISPERSION_STROKE_WIDTH_PERCENTILE = 0.002;
	
	public SpriteAssets(Graphics g) {
		maxVelocity = (int) (g.getHeight() * 0.13);
		
		// dispersion effect
		dispersionStrokeWidth = (int) (g.getHeight() * DISPERSION_STROKE_WIDTH_PERCENTILE);
		
		// big sprite
		bigSpriteX = g.getWidthPercentile(0.5);
		bigSpriteY = g.getHeightPercentile(0.8);
		bigSpriteRadius = (float) ((g.getWidth() + g.getHeight()) * 0.02);
		bigSpriteStrokeWidth = (float) (bigSpriteRadius * 0.1);
		
		// station
		stationRadius = (float) (g.getHeight()*0.13 + bigSpriteRadius*1.2);
		stationStrokeWidth = bigSpriteStrokeWidth;
		
		// small sprite
		smallSpriteRadius = (float) ((g.getWidth() + g.getHeight()) * 0.01);
		smallSpriteStrokeWidth = (float) (smallSpriteRadius * 0.2);
		smallSpriteSpeed = g.getHeight() * SPEED_PERCENTILE;
		
		// create bitmaps
		Bitmap bitmap;
		Canvas canvas;
		Paint paint;
		Path path;
		
		int antiAlias = 2;
		
		int spriteBitmapSize = (int) (smallSpriteRadius*2 + smallSpriteStrokeWidth)+antiAlias*2;
		int spriteBitmapCenter = spriteBitmapSize/2;
		float armLength = (float) (smallSpriteRadius*0.5);
		float negativeArm = spriteBitmapCenter-armLength;
		float positiveArm = spriteBitmapCenter+armLength;
		float armCenter = spriteBitmapCenter;
		
		// minus Sprite
		bitmap = Bitmap.createBitmap(spriteBitmapSize, spriteBitmapSize, Config.ARGB_4444);
		canvas = new Canvas(bitmap);
		
		paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.rgb(255,175,0));
		paint.setStrokeWidth(smallSpriteStrokeWidth);
		paint.setStrokeCap(Paint.Cap.ROUND);
		
		canvas.drawCircle(spriteBitmapCenter, spriteBitmapCenter, smallSpriteRadius, paint);
		
		path = new Path();
		path.setLastPoint(negativeArm, armCenter); // left point
		path.lineTo(positiveArm, armCenter); // right point
		canvas.drawPath(path, paint);
		
		minusSpriteImage = g.newImage(bitmap); // END minus Sprite
		
		// plus Sprite
		bitmap = Bitmap.createBitmap(spriteBitmapSize, spriteBitmapSize, Config.ARGB_4444);
		canvas = new Canvas(bitmap);
		
		paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.rgb(0,175,255));
		paint.setStrokeWidth(smallSpriteStrokeWidth);
		paint.setStrokeCap(Paint.Cap.ROUND);
		
		canvas.drawCircle(spriteBitmapCenter, spriteBitmapCenter, smallSpriteRadius, paint);
		
		path = new Path();
		path.setLastPoint(negativeArm, armCenter); // left point
		path.lineTo(positiveArm, armCenter); // right point
		path.moveTo(armCenter, negativeArm); // top point
		path.lineTo(armCenter, positiveArm); // bottom point
		canvas.drawPath(path, paint);
		
		plusSpriteImage = g.newImage(bitmap); // END plus Sprite
		
		// player station
		spriteBitmapSize = (int) (stationRadius*2 + stationStrokeWidth)+antiAlias*2;
		spriteBitmapCenter = spriteBitmapSize/2;
		float stationCenterCircle = (float) (bigSpriteRadius*1.2);
		
		bitmap = Bitmap.createBitmap(spriteBitmapSize, spriteBitmapSize, Config.ARGB_4444);
		canvas = new Canvas(bitmap);
		
		paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(stationStrokeWidth);
		paint.setColor(Color.rgb(0, 175, 255));
		paint.setAlpha(50);			

		canvas.drawCircle(spriteBitmapCenter, spriteBitmapCenter, stationRadius, paint);
		paint.setAlpha(255);
		canvas.drawCircle(spriteBitmapCenter, spriteBitmapCenter, stationCenterCircle, paint);
		
		stationImage = g.newImage(bitmap);
		
		// play station arrow arm length
		arrowArmLength = (float) (bigSpriteRadius*0.3);
	}

	// big Sprite
	public float getBigSpriteRadius() {
		return bigSpriteRadius;
	}

	public float getBigSpriteStrokeWidth() {
		return bigSpriteStrokeWidth;
	}

	// small Sprite
	public float getSmallSpriteRadius() {
		return smallSpriteRadius;
	}

	public float getSmallSpriteStrokeWidth() {
		return smallSpriteStrokeWidth;
	}

	// station
	public float getStationRadius() {
		return stationRadius;
	}

	public float getStationStrokeWidth() {
		return stationStrokeWidth;
	}

	public Image getMinusSpriteImage() {
		return minusSpriteImage;
	}

	public Image getPlusSpriteImage() {
		return plusSpriteImage;
	}

	public Image getStationImage() {
		return stationImage;
	}

	public float getArrowArmLength() {
		return arrowArmLength;
	}

	public float getBigSpriteX() {
		return bigSpriteX;
	}

	public float getBigSpriteY() {
		return bigSpriteY;
	}

	public int getMaxVelocity() {
		return maxVelocity;
	}

	public double getSmallSpriteSpeed() {
		return smallSpriteSpeed;
	}

	public int getDispersionStrokeWidth() {
		return dispersionStrokeWidth;
	}
}
