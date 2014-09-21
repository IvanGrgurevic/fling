package com.ivangrgurevic.fling.util;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ivangrgurevic.fling.framework.implementation.AndroidGame;
import com.ivangrgurevic.fling.framework.implementation.AndroidGame.TrackerName;

public class GATracker {

	public static void sendScreen(AndroidGame game, String path) {
        Tracker t = game.getTracker(TrackerName.APP_TRACKER);

        t.setScreenName(path);

        t.send(new HitBuilders.AppViewBuilder().build());
	}
}
