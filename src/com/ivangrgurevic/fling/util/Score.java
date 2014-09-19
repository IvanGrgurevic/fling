package com.ivangrgurevic.fling.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Score {
	private final static String PREF_KEY = "flingPrefs";
	private final static String KEY = "score";
	
	public static void set(Context context, int score) {
		SharedPreferences prefs = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
		
		int highScore = prefs.getInt(KEY, 0);
		
		if(score > highScore) {
			Editor editor = prefs.edit();
			editor.putInt(KEY, score);
			editor.commit();			
		}
	}

	public static int get(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
		return prefs.getInt(KEY, 0);
	}
}
