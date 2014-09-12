package com.ivangrgurevic.fling.screen.layer;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.ivangrgurevic.fling.assets.Assets;
import com.ivangrgurevic.fling.framework.Game;
import com.ivangrgurevic.fling.framework.Graphics;
import com.ivangrgurevic.fling.framework.Image;
import com.ivangrgurevic.fling.framework.Input.TouchEvent;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.screen.GameScreen;
import com.ivangrgurevic.fling.util.Range;

public class PausedLayer extends Layer {

	private Paint paint;
	
	private Image playBtn;
	private final int playBtnX;
	private final int playBtnY;
	private final int playBtnWidth;
	private final int playBtnHeight;
	
	private Image restartBtn;
	private final int restartBtnX;
	private final int restartBtnY;
	private final int restartBtnWidth;
	private final int restartBtnHeight;

	
	public PausedLayer(Screen screen, Graphics graphics, Game game) {
		super(screen, graphics);
		
		int btnPadding = graphics.getWidth()/5;
		int widthSize = graphics.getWidth() - btnPadding*2;
		int heightSize = graphics.getHeight()/2 - btnPadding*2;
		int btnSize = (heightSize > widthSize) ? widthSize : heightSize;
		
		playBtn = Assets.playBtn;
		playBtnX = graphics.getWidth()/2 - btnSize/2;
		playBtnY = graphics.getHeight()*3/4 - btnSize/2;
		playBtnWidth = btnSize;
		playBtnHeight = btnSize;
		
		restartBtn = Assets.resetBtn;
		restartBtnX = graphics.getWidth()/2 - btnSize/2;
		restartBtnY = graphics.getHeight()/4 - btnSize/2;
		restartBtnWidth = btnSize;
		restartBtnHeight = btnSize;

		paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setAlpha(100);
	}

	@Override
	public void draw(float deltaTime) {
		graphics.drawRect(new RectF(0, 0, graphics.getWidth(), graphics.getHeight()), paint);
		
		graphics.drawScaledImage(playBtn, playBtnX, playBtnY, playBtnWidth, playBtnHeight, 0, 0, playBtn.getWidth(), playBtn.getHeight());

		graphics.drawScaledImage(restartBtn, restartBtnX, restartBtnY, restartBtnWidth, restartBtnHeight, 0, 0, restartBtn.getWidth(), restartBtn.getHeight());
	}

	@Override
	public void update(List<TouchEvent> touchEvents, float deltaTime) {
		updateTouchEvents(touchEvents);
	}

	private void updateTouchEvents(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		
		for (int i=0;i<len;i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if(Range.inBounds(event, 0, 0, graphics.getWidth(), graphics.getHeight()/2)) {
					((GameScreen)screen).newGame();
				}
				else if(Range.inBounds(event, 0, graphics.getHeight()/2, graphics.getWidth(), graphics.getHeight())) {
					screen.resume();					
				}	
			}
		}
	}
	
}
