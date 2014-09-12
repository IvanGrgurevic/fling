package com.ivangrgurevic.fling.framework;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public interface Graphics {
	public static enum ImageFormat {
		ARGB8888, ARGB4444, RGB565
	}

	public Image newImage(String fileName, ImageFormat format);

	public void clearScreen(int color);

	public void drawLine(int x, int y, int x2, int y2, int color);

	public void drawRect(int x, int y, int width, int height, int color);

	public void drawImage(Image image, int x, int y, int srcX, int srcY,
			int srcWidth, int srcHeight);

	public void drawImage(Image Image, int x, int y);

	public void drawString(String text, int x, int y, Paint paint);

	public int getHeight();

	public void drawARGB(int i, int j, int k, int l);

	public int getWidthPercentile(double d);
	
	public int getHeightPercentile(double d);

	public void drawCircle(float cx, float cy, float radius, Paint paint);

	public void drawColor(int color);

	public void drawImage(Image splash, float x, float y, int alpha);

	public void drawPoints(float[] pts, Paint paint);

	public void drawLine(int x, int y, int x2, int y2, Paint paint);

	public void restore();

	public void rotate(float degrees, int px, int py);

	public void save();

	public void drawRect(RectF r, Paint paint);

	public void drawPath(Path mPath, Paint paint);

	public void translate(int px, int py);

	public Image newImage(Bitmap bm);

	public void drawImage(Image Image, float x, float y, Paint paint);

	public void drawImage(Image Image, float x, float y);

	void drawImage(Image Image, int x, int y, int alpha);

	void drawScaledImage(Image Image, int x, int y, int width, int height,
			int srcX, int srcY, int srcWidth, int srcHeight);

	public int getWidth();
}