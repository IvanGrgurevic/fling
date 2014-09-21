package com.ivangrgurevic.fling.util;

import com.google.android.gms.games.Games;
import com.ivangrgurevic.fling.R;
import com.ivangrgurevic.fling.framework.implementation.AndroidGame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GameStats {
	private final static String PREF_KEY = "flingPrefs";
	private final static String KEY_SCORE = "score";
	private final static String KEY_GAMES_PLAYED = "gamesPlayed";
	
	public static void setScore(AndroidGame game, int score) {
		SharedPreferences prefs = game.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
		
		int highScore = prefs.getInt(KEY_SCORE, 0);
		
		if(score > highScore) {
			Editor editor = prefs.edit();
			editor.putInt(KEY_SCORE, score);
			editor.commit();
			
			Games.Leaderboards.submitScore(game.getApiClient(), game.getString(R.string.leaderboard_high_scores), score);
		}
	}

	public static int getScore(AndroidGame game) {
		SharedPreferences prefs = game.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
		return prefs.getInt(KEY_SCORE, 0);
	}
	
	public static void incrementGamesPlayed(AndroidGame game) {
		SharedPreferences prefs = game.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
		
		int gamesPlayed = prefs.getInt(KEY_GAMES_PLAYED, 0);
		
		gamesPlayed++;
		
		Editor editor = prefs.edit();
		editor.putInt(KEY_GAMES_PLAYED, gamesPlayed);
		editor.commit();
		
		switch(gamesPlayed)
		{
		case 1:
			Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_1_game));
			break;
		case 5:
			Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_5_games));
			break;
		case 50:
			Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_50_games));
			break;
		case 100:
			Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_100_games));
			break;
		case 200:
			Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_200_games));
			break;
		case 500:
			Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_500_games));
			break;
		case 1000:
			Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_1000_games));
			break;
		case 2500:
			Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_2500_games));
			break;
		case 5000:
			Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_5000_games));
			break;
		default:
			break;
		}
	}
}
