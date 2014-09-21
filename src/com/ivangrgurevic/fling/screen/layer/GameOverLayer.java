package com.ivangrgurevic.fling.screen.layer;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.android.gms.games.Games;
import com.ivangrgurevic.fling.R;
import com.ivangrgurevic.fling.assets.Assets;
import com.ivangrgurevic.fling.assets.GameAssets;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Image;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.framework.implementation.AndroidGame;
import com.ivangrgurevic.fling.screen.GameScreen;
import com.ivangrgurevic.fling.sprite.DispersionEffect;
import com.ivangrgurevic.fling.sprite.MinusSprite;
import com.ivangrgurevic.fling.sprite.PlayerSprite;
import com.ivangrgurevic.fling.util.GameTheme;
import com.ivangrgurevic.fling.util.Range;

public class GameOverLayer extends Layer {
	private ArrayList<DispersionEffect> dispersionArr;
	
	private final int btnPadding;
	private final int btnPaddingHeight;
	
	private Image playBtn;
	private final int playBtnX;
	private final int playBtnY;
	private final int playBtnWidth;
	private final int playBtnHeight;

	private Image shareBtn;
	private final int shareBtnX;
	private final int shareBtnY;
	private final int shareBtnWidth;
	private final int shareBtnHeight;
	
	private Image achievementsBtn;
	private final int achievementsBtnX;
	private final int achievementsBtnY;
	private final int achievementsBtnWidth;
	private final int achievementsBtnHeight;

	private Image leaderboardBtn;
	private final int leaderboardBtnX;
	private final int leaderboardBtnY;
	private final int leaderboardBtnWidth;
	private final int leaderboardBtnHeight;


	private final int HIGH_SCORE;
	private final int SCORE;
	
	private final int POINTS_X;
	private final int POINTS_Y;
	private final float HIGH_SCORE_TEXT_SIZE;
	private final float SCORE_TEXT_SIZE;
	
	private Paint paintHighScore;
	private Paint paintScore;
	
	private Rect scoreBounds = new Rect();
	private Rect highScoreBounds = new Rect();
	
	private AndroidGame game;

	public GameOverLayer(Screen screen, Graphics g, AndroidGame game, int highScore, int score, GameAssets gameAssets, PlayerSprite playerSprite, ArrayList<MinusSprite> minusSprites, ArrayList<DispersionEffect> dispersionEffects) {
		super(screen, g);

		this.game = game;
		
		HIGH_SCORE = highScore;
		SCORE = score;
		
		dispersionArr = new ArrayList<DispersionEffect>();
		
		// convert all sprites to dispersion effects
		for(DispersionEffect effect : dispersionEffects) {
			dispersionArr.add(effect);
		}

		//border
		dispersionArr.add(new DispersionEffect(0, 0, 0, 0, graphics.getWidth(), graphics.getHeight(), Color.WHITE, 2000, gameAssets, graphics));				
		
		// player
		dispersionArr.add(new DispersionEffect(playerSprite.getX(), playerSprite.getY(), 0, 0, playerSprite.getRadius(), Color.WHITE, 80, gameAssets, graphics));				
		dispersionArr.add(new DispersionEffect(playerSprite.getStartX(), playerSprite.getStartY(), 0, 0, playerSprite.getOuterRadius(), GameTheme.BLUE, 500, gameAssets, graphics));				
		
		// minus sprite
		for(MinusSprite sprite : minusSprites) {
			dispersionArr.add(new DispersionEffect(sprite.getX(), sprite.getY(), sprite.getVX(), sprite.getVY(), sprite.getRadius(), sprite.getColor(), 80, gameAssets, graphics));
		}
		
		// buttons
		btnPadding = graphics.getWidth()/6;
		btnPaddingHeight = graphics.getHeight()/16;
		int btnSize = graphics.getWidth()/2 - btnPadding*2;
		
		playBtn = Assets.playBtn;
		playBtnX = btnPadding;
		playBtnY = graphics.getHeight() - btnPaddingHeight - btnSize;
		playBtnWidth = btnSize;
		playBtnHeight = btnSize;
		
		shareBtn = Assets.shareBtn;
		shareBtnX = btnSize + btnPadding*3;
		shareBtnY = graphics.getHeight() - btnPaddingHeight - btnSize;
		shareBtnWidth = btnSize;
		shareBtnHeight = btnSize;

		achievementsBtn = Assets.achievementsBtn;
		achievementsBtnX = btnPadding;
		achievementsBtnY = btnPaddingHeight;
		achievementsBtnWidth = btnSize;
		achievementsBtnHeight = btnSize;

		leaderboardBtn = Assets.leaderboardBtn;
		leaderboardBtnX = btnSize + btnPadding*3;
		leaderboardBtnY = btnPaddingHeight;
		leaderboardBtnWidth = btnSize;
		leaderboardBtnHeight = btnSize;

		// points
		POINTS_X = graphics.getWidth()/2;
		POINTS_Y = graphics.getHeight()/2;
		HIGH_SCORE_TEXT_SIZE = graphics.getHeight()/8;
		SCORE_TEXT_SIZE = graphics.getHeight()/4;
		
		paintHighScore = new Paint();
		paintHighScore.setTypeface(Assets.typeface);
		paintHighScore.setTextSize(HIGH_SCORE_TEXT_SIZE);
		paintHighScore.setTextAlign(Paint.Align.CENTER);
		paintHighScore.setAntiAlias(true);
		paintHighScore.setColor(Color.rgb(60, 60, 60));

		paintScore = new Paint();
		paintScore.setTypeface(Assets.typeface);
		paintScore.setTextSize(SCORE_TEXT_SIZE);
		paintScore.setTextAlign(Paint.Align.CENTER);
		paintScore.setAntiAlias(true);
		paintScore.setColor(Color.WHITE);
}
	
	@Override
	public void draw(float deltaTime) {
		if(dispersionArr.isEmpty()) {
			// points
			graphics.drawString(String.valueOf(SCORE), POINTS_X, POINTS_Y, paintScore);
			graphics.drawString(String.valueOf(HIGH_SCORE), POINTS_X, (int)(POINTS_Y+HIGH_SCORE_TEXT_SIZE), paintHighScore);

			// buttons
			graphics.drawScaledImage(playBtn, playBtnX, playBtnY, playBtnWidth, playBtnHeight, 0, 0, playBtn.getWidth(), playBtn.getHeight());
			graphics.drawScaledImage(shareBtn, shareBtnX, shareBtnY, shareBtnWidth, shareBtnHeight, 0, 0, shareBtn.getWidth(), shareBtn.getHeight());
			graphics.drawScaledImage(achievementsBtn, achievementsBtnX, achievementsBtnY, achievementsBtnWidth, achievementsBtnHeight, 0, 0, achievementsBtn.getWidth(), achievementsBtn.getHeight());
			graphics.drawScaledImage(leaderboardBtn, leaderboardBtnX, leaderboardBtnY, leaderboardBtnWidth, leaderboardBtnHeight, 0, 0, leaderboardBtn.getWidth(), leaderboardBtn.getHeight());

		}
		else {
			for(DispersionEffect effect : dispersionArr) {
				effect.draw(deltaTime);
			}
		}
	}

	@Override
	public void update(List<TouchEvent> touchEvents, float deltaTime) {
		if(dispersionArr.isEmpty()) {
			paintHighScore.getTextBounds(String.valueOf(HIGH_SCORE), 0 , String.valueOf(HIGH_SCORE).length(), highScoreBounds);
			paintScore.getTextBounds(String.valueOf(HIGH_SCORE), 0 , String.valueOf(HIGH_SCORE).length(), scoreBounds);

			updateTouchEvents(touchEvents);
		}
		else {
			for(int i=0;i<dispersionArr.size();i++) {
				DispersionEffect effect = dispersionArr.get(i);
				effect.update(deltaTime);
				
				if(effect.isDone()) {
					dispersionArr.remove(i);
					i--;
				}
			}
		}
	}

	private void updateTouchEvents(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i=0;i<len;i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if(Range.inBounds(event, playBtnX-btnPadding, playBtnY-btnPadding, playBtnWidth+btnPadding*2, playBtnHeight+btnPadding*2)) {
					((GameScreen)screen).newGame();
				}
				else if(Range.inBounds(event, shareBtnX-btnPadding, shareBtnY-btnPadding, shareBtnWidth+btnPadding*2, shareBtnHeight+btnPadding*2)) {
					this.share();
				}
				else if(Range.inBounds(event, achievementsBtnX-btnPadding, achievementsBtnY-btnPadding, achievementsBtnWidth+btnPadding*2, achievementsBtnHeight+btnPadding*2)) {
					if(isSignedIn()) {
						game.startActivityForResult(Games.Achievements.getAchievementsIntent(game.getApiClient()), 0);
					}
				}
				else if(Range.inBounds(event, leaderboardBtnX-btnPadding, leaderboardBtnY-btnPadding, leaderboardBtnWidth+btnPadding*2, leaderboardBtnHeight+btnPadding*2)) {
					if(isSignedIn()) {
						game.startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(game.getApiClient()), 0);
					}
				}
			}
		}
	}
	
	private boolean isSignedIn()
	{
		if(game.isSignedIn()) {
			return true;
		}
		else
		{
			game.beginUserInitiatedSignIn();
			
			return (game.isSignedIn()) ? true : false;
		}
	}
	
	private void share() {
		String intentText = game.getString(R.string.intent_message);
		intentText = String.format(intentText, SCORE);
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, intentText);
		game.startActivity(Intent.createChooser(intent, game.getString(R.string.intent_chooser_title)));
	}
}
