package com.ivangrgurevic.fling.framework.implementation;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Image;

public class AndroidGraphics implements Graphics {
	AssetManager assets;
	Bitmap frameBuffer;
	Canvas canvas;
	Paint paint;
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();

	public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
	}

	@Override
	public Image newImage(String fileName, ImageFormat format) {
		Config config = null;
		if (format == ImageFormat.RGB565)
			config = Config.RGB_565;
		else if (format == ImageFormat.ARGB4444)
			config = Config.ARGB_4444;
		else
			config = Config.ARGB_8888;

		Options options = new Options();
		options.inPreferredConfig = config;

		InputStream in = null;
		Bitmap bitmap = null;
		try {
			in = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(in, null, options);
			if (bitmap == null)
				throw new RuntimeException("Couldn't load bitmap from asset '"
						+ fileName + "'");
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load bitmap from asset '"
					+ fileName + "'");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}

		if (bitmap.getConfig() == Config.RGB_565)
			format = ImageFormat.RGB565;
		else if (bitmap.getConfig() == Config.ARGB_4444)
			format = ImageFormat.ARGB4444;
		else
			format = ImageFormat.ARGB8888;

		return new AndroidImage(bitmap, format);
	}

	@Override
	public Image newImage(Bitmap bitmap) {
		ImageFormat format;
		if (bitmap.getConfig() == Config.RGB_565)
			format = ImageFormat.RGB565;
		else if (bitmap.getConfig() == Config.ARGB_4444)
			format = ImageFormat.ARGB4444;
		else
			format = ImageFormat.ARGB8888;
		
		return new AndroidImage(bitmap, format);
	}

	
	@Override
	public void clearScreen(int color) {
		canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
				(color & 0xff));
	}

	@Override
	public void drawLine(int x, int y, int x2, int y2, int color) {
		paint.setColor(color);
		canvas.drawLine(x, y, x2, y2, paint);
	}

	@Override
	public void drawLine(int x, int y, int x2, int y2, Paint paint) {
		canvas.drawLine(x, y, x2, y2, paint);
	}

	@Override
	public void drawRect(int x, int y, int width, int height, int color) {
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
	}

	@Override
	public void drawRect(RectF rect, Paint paint) {
		canvas.drawRect(rect, paint);
	}

	@Override
	public void drawCircle(float cx, float cy, float radius, Paint paint) {
		canvas.drawCircle(cx, cy, radius, paint);
	}
	
	@Override
	public void drawPath(Path path, Paint paint) {
		canvas.drawPath(path, paint);
	}
	
	@Override
	public void drawPoints(float[] pts, Paint paint) {
		canvas.drawPoints(pts, paint);
	}
	
	@Override
	public void drawColor(int color) {
		canvas.drawColor(color);
	}
		
	@Override
	public void drawARGB(int a, int r, int g, int b) {
		paint.setStyle(Style.FILL);
		canvas.drawARGB(a, r, g, b);
	}

	@Override
	public void drawString(String text, int x, int y, Paint paint) {
		canvas.drawText(text, x, y, paint);
	}

	@Override
	public void drawImage(Image Image, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth;
		srcRect.bottom = srcY + srcHeight;

		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + srcWidth;
		dstRect.bottom = y + srcHeight;

		canvas.drawBitmap(((AndroidImage) Image).bitmap, srcRect, dstRect, null);
	}

	@Override
	public void drawImage(Image Image, int x, int y) {
		canvas.drawBitmap(((AndroidImage) Image).bitmap, x, y, null);
	}
	
	@Override
	public void drawImage(Image Image, float x, float y, Paint paint) {
		canvas.drawBitmap(((AndroidImage) Image).bitmap, x, y, paint);
	}

	@Override
	public void drawImage(Image Image, float x, float y) {
		canvas.drawBitmap(((AndroidImage) Image).bitmap, x, y, null);
	}

	@Override
	public void drawImage(Image Image, float x, float y, int alpha) {
		Paint paint = new Paint();
		paint.setAlpha(alpha);
		canvas.drawBitmap(((AndroidImage) Image).bitmap, x, y, paint);
	}

	@Override
	public void drawImage(Image Image, int x, int y, int alpha) {
		Paint paint = new Paint();
		paint.setAlpha(alpha);
		canvas.drawBitmap(((AndroidImage) Image).bitmap,x, y, paint);
	}

	@Override
	public void drawScaledImage(Image Image, int x, int y, int width,
			int height, int srcX, int srcY, int srcWidth, int srcHeight) {

		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth;
		srcRect.bottom = srcY + srcHeight;

		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + width;
		dstRect.bottom = y + height;

		canvas.drawBitmap(((AndroidImage) Image).bitmap, srcRect, dstRect, null);
	}

	@Override
	public void restore() {
		canvas.restore();
	}

	@Override
	public void rotate(float degrees, int x, int y) {
		canvas.rotate(degrees, x, y);
	}

	@Override
	public void save() {
		canvas.save();
	}
	
	@Override
	public void translate(int x, int y) {
		canvas.translate(x, y);
	}

	@Override
	public int getWidth() {
		return frameBuffer.getWidth();
	}

	@Override
	public int getHeight() {
		return frameBuffer.getHeight();
	}
	
	@Override
	public int getWidthPercentile(double percentile) {
		return (int)(frameBuffer.getWidth() * percentile);
	}
	
	@Override
	public int getHeightPercentile(double percentile) {
		return (int)(frameBuffer.getHeight() * percentile);
	}
}

