package com.ivangrgurevic.fling.assets;

import com.ivangrgurevic.fling.Fling;
import com.ivangrgurevic.fling.framework.Music;

public class Assets {
	public static Music theme;

	public static void load(Fling sampleGame) {
		theme = sampleGame.getAudio().createMusic("menutheme.mp3");
		theme.setLooping(true);
		theme.setVolume(0.85f);
		theme.play();
	}
}