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
		}
		
		if(game.isSignedIn()) {
			Games.Leaderboards.submitScore(game.getApiClient(), game.getString(R.string.leaderboard_high_scores), score);
			
			int rounded = 0;
			
			if(score >= 500) {
				rounded = 500;
			}
			else if(score >= 400) {
				rounded = 400;
			}
			else if(score >= 300) {
				rounded = 300;
			}
			else if(score >= 200) {
				rounded = 200;
			}
			else if(score >= 150) {
				rounded = 150;
			}
			else if(score >= 100) {
				rounded = 100;
			}
			else if(score >= 50) {
				rounded = 50;
			}
			
			switch(rounded)
			{
			case 500:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_500_points));
			case 400:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_400_points));
			case 300:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_300_points));
			case 200:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_200_points));
			case 150:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_150_points));
			case 100:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_100_points));
			case 50:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_50_points));
			default:
				break;
			}
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
		
		
		if(game.isSignedIn()) {
			Games.Leaderboards.submitScore(game.getApiClient(), game.getString(R.string.leaderboard_games_played), gamesPlayed);

			int rounded = 0;
			
			if(gamesPlayed >= 5000) {
				rounded = 5000;
			}
			else if(gamesPlayed >= 2500) {
				rounded = 2500;
			}
			else if(gamesPlayed >= 1000) {
				rounded = 1000;
			}
			else if(gamesPlayed >= 500) {
				rounded = 500;
			}
			else if(gamesPlayed >= 200) {
				rounded = 200;
			}
			else if(gamesPlayed >= 100) {
				rounded = 100;
			}
			else if(gamesPlayed >= 50) {
				rounded = 50;
			}
			else if(gamesPlayed >= 5) {
				rounded = 5;
			}
			else if(gamesPlayed >= 1) {
				rounded = 1;
			}
			
			switch(rounded)
			{
			case 5000:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_5000_games));
			case 2500:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_2500_games));
			case 1000:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_1000_games));
			case 500:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_500_games));
			case 200:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_200_games));
			case 100:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_100_games));
			case 50:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_50_games));
			case 5:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_5_games));
			case 1:
				Games.Achievements.unlock(game.getApiClient(), game.getString(R.string.achievement_1_game));
			default:
				break;
			}
		}
	}
}
