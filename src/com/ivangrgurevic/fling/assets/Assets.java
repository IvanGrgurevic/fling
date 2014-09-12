package com.ivangrgurevic.fling.assets;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;

import com.ivangrgurevic.fling.Fling;
import com.ivangrgurevic.fling.framework.Graphics.ImageFormat;
import com.ivangrgurevic.fling.framework.Image;
import com.ivangrgurevic.fling.framework.Music;
import com.ivangrgurevic.fling.framework.implementation.AndroidImage;
import com.ivangrgurevic.game.R;

public class Assets {
	public static Music theme;
	public static Image playBtn;
	public static Image resetBtn;
	public static Typeface typeface;
	
	public static void load(Fling game) {
		// music
		theme = game.getAudio().createMusic("menutheme.mp3");
		theme.setLooping(true);
		theme.setVolume(0.85f);
		theme.play();
		
		// btns
		Resources res = game.getResources();
		playBtn = new AndroidImage(BitmapFactory.decodeResource(res, R.drawable.play), ImageFormat.ARGB8888);
		resetBtn = new AndroidImage(BitmapFactory.decodeResource(res, R.drawable.refresh), ImageFormat.ARGB8888);
		
		//font
		typeface = Typeface.createFromAsset(game.getAssets(), "fonts/Square.otf");
	}
}