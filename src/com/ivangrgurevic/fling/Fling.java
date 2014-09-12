package com.ivangrgurevic.fling;

import com.ivangrgurevic.fling.assets.Assets;
import com.ivangrgurevic.fling.framework.Screen;
import com.ivangrgurevic.fling.framework.implementation.AndroidGame;
import com.ivangrgurevic.fling.screen.SplashLoadingScreen;

public class Fling extends AndroidGame {
    boolean firstTimeCreate = true;

    @Override
    public Screen getInitScreen() {
        if (firstTimeCreate) {
            Assets.load(this);
            firstTimeCreate = false;
        }

        return new SplashLoadingScreen(this);
    }

    @Override
    public void onBackPressed() {
        getCurrentScreen().backButton();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
