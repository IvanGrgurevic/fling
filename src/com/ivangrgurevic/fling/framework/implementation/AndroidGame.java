package com.ivangrgurevic.fling.framework.implementation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.ivangrgurevic.fling.framework.Audio;
import com.ivangrgurevic.fling.framework.FileIO;
import com.ivangrgurevic.fling.framework.Game;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Input;
import com.ivangrgurevic.fling.framework.Screen;

public abstract class AndroidGame extends Activity implements Game {
	private AndroidFastRenderView renderView;
	private Graphics graphics;
	private Audio audio;
	private Input input;
	private FileIO fileIO;
	private Screen screen;
	//WakeLock wakeLock;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		int frameBufferWidth = getDisplaySize().x;
		int frameBufferHeight = getDisplaySize().y;
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);

		float scaleX = (float) frameBufferWidth / getDisplaySize().x;
		float scaleY = (float) frameBufferHeight / getDisplaySize().y;

		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		screen = getInitScreen();
		setContentView(renderView);

		/*getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		 * PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyGame");*/
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private Point getDisplaySize() {
		final Display display = getWindowManager().getDefaultDisplay();
		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		return point;
	}

	@Override
	public void onResume() {
		super.onResume();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//wakeLock.acquire();
		screen.resume();
		renderView.resume();
	}

	@Override
	public void onPause() {
		super.onPause();
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//wakeLock.release();
		renderView.pause();
		screen.pause();

		if (isFinishing())
			screen.dispose();
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	@Override
	public Graphics getGraphics() {
		return graphics;
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");

		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	@Override
	public Screen getCurrentScreen() {
		return screen;
	}
}