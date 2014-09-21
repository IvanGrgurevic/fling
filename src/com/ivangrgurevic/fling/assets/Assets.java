package com.ivangrgurevic.fling.assets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;

import com.ivangrgurevic.fling.Fling;
import com.ivangrgurevic.fling.R;
import com.ivangrgurevic.fling.framework.Graphics.ImageFormat;
import com.ivangrgurevic.fling.framework.Image;
import com.ivangrgurevic.fling.framework.implementation.AndroidImage;

public class Assets {	
	public static Image playBtn;
	public static Image resetBtn;
	public static Image shareBtn;
	public static Image achievementsBtn;
	public static Image leaderboardBtn;

	public static Typeface typeface;
	
	public static void load(Fling game) {		
		// buttons
		Resources res = game.getResources();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		
		Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.play, options); 
		playBtn = new AndroidImage(bitmap, ImageFormat.ARGB8888);
		
		bitmap = BitmapFactory.decodeResource(res, R.drawable.refresh, options);
		resetBtn = new AndroidImage(bitmap, ImageFormat.ARGB8888);

		bitmap = BitmapFactory.decodeResource(res, R.drawable.share, options);
		shareBtn = new AndroidImage(bitmap, ImageFormat.ARGB8888);

		bitmap = BitmapFactory.decodeResource(res, R.drawable.medal, options);
		achievementsBtn = new AndroidImage(bitmap, ImageFormat.ARGB8888);

		bitmap = BitmapFactory.decodeResource(res, R.drawable.trophy, options);
		leaderboardBtn = new AndroidImage(bitmap, ImageFormat.ARGB8888);
		
		//font
		typeface = Typeface.createFromAsset(game.getAssets(), "fonts/bit.otf");
	}
}