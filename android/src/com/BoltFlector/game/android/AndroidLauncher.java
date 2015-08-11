package com.BoltFlector.game.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.BoltFlector.game.ActionResolver;
import com.BoltFlector.game.BoltFlector;
import com.google.example.games.basegameutils.GameHelper;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


public class AndroidLauncher extends AndroidApplication implements GameHelperListener, ActionResolver {
	 protected AdView adView;
	  protected View gameView;
	
	private GameHelper gameHelper;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new BoltFlector(), config);
		 gameView = initializeForView(new BoltFlector());
		 if (gameHelper == null) {
		      gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		      gameHelper.enableDebugLog(true);
		    }
		    gameHelper.setup(this);
			}

			@Override
			public void onStart(){
				super.onStart();
				gameHelper.onStart(this);
			}

			@Override
			public void onStop(){
				super.onStop();
				gameHelper.onStop();
			}

			@Override
			public void onActivityResult(int request, int response, Intent data) {
				super.onActivityResult(request, response, data);
				gameHelper.onActivityResult(request, response, data);
			}
			
			@Override
			public boolean getSignedInGPGS() {
				return gameHelper.isSignedIn();
			}

			@Override
			public void loginGPGS() {
				try {
					runOnUiThread(new Runnable(){
						public void run() {
							gameHelper.beginUserInitiatedSignIn();
						}
					});
				} catch (final Exception ex) {
				}
			}

			@Override
			public void submitScoreGPGS(int score) {
				Games.Leaderboards.submitScore(gameHelper.getApiClient(), "74809814117", score);
			}
			
			@Override
			public void unlockAchievementGPGS(String achievementId) {
			  Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
			}
			
			@Override
			public void getLeaderboardGPGS() {
			  if (gameHelper.isSignedIn()) {
			    startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), "CgkI6574wJUXEAIQBw"), 100);
			  }
			  else if (!gameHelper.isConnecting()) {
			    loginGPGS();
			  }
			}

			@Override
			public void getAchievementsGPGS() {
			  if (gameHelper.isSignedIn()) {
			    startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
			  }
			  else if (!gameHelper.isConnecting()) {
			    loginGPGS();
			  }
			}
			
			@Override
			public void onSignInFailed() {
			}

			@Override
			public void onSignInSucceeded() {
			}
}
