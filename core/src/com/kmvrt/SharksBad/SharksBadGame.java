package com.kmvrt.SharksBad;

//import android.content.SharedPreferences;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
//import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class SharksBadGame extends ApplicationAdapter {

	// fields ----------------------------------------------------------------------
	private static final String HIGHSCORE = "highscoreOfTheGame";
	private static final String FIRST_TIME = "isStillVirgin";
	
	private Preferences pref;
	private Manager manager;
		// handle almost everything: map, rendering
		// and updating every game components
	private Notifier notifier;
	
	private Advertiser advertiser;
	
	private boolean isInitialized;

	
// constructor ------------------------------------------------------------
	public SharksBadGame(Advertiser advertiser) {
		this.advertiser = advertiser;
		isInitialized = false;
	}
	
	
// methods ---------------------------------------------------------------------
	// inherited from ApplicationAdapter **************************************
	@Override
	public void create() { //////////////////////////////////////////////////
		// when first created

		// get the last highscore
		if(!isInitialized) {
			pref = Gdx.app.getPreferences("save");
			manager = new Manager(advertiser, pref.getInteger(HIGHSCORE, 0));
			if(pref.getInteger(FIRST_TIME, 0) == 0) {
				manager.clickHint();
				pref.putInteger(FIRST_TIME, 1);
				pref.flush();
			}
	
			notifier = new Notifier(manager);
	
			Gdx.input.setInputProcessor(new GestureDetector(notifier));
			isInitialized = true;
		}
	//	Gdx.input.setInputProcessor(notifier);
	} // end of create() method

	@Override
	public void render() { /////////////////////////////////////////////////
		// when it needs to be rendered

		if(manager.getScore() > pref.getInteger(HIGHSCORE)) {
			pref.putInteger(HIGHSCORE, manager.getScore());
			pref.flush();
		} 
		
		if(!manager.isPaused()) {
			manager.update(); 	// update the sprites before rendering
			manager.render(); 	// render the sprites to the screen
		}
	}	// end of render() method

	
/*		BitmapFont font = new BitmapFont();
		font.setColor(Color.RED);
		String msg = "YOU DIED"; */

/*		font.draw(gameOverBatch, msg, 
			30,
			(Gdx.graphics.getHeight() - font.getXHeight()) / 2); */	
			
			// draw it in the middle of the screen
	
	@Override
	public void dispose() { ////////////////////////////////////////////////
		// clear the resources after being used
	
		manager.dispose();
	} // end of deploy() method

	@Override
	public void pause() {
		// called when the application is paused
	
		if(manager.getHighscore() > pref.getInteger(HIGHSCORE)) {
			pref.putInteger(HIGHSCORE, manager.getHighscore());
			pref.flush();
		} 
		manager.isPaused(true);
	}

	@Override
	public void resume() {
		// called when application is resumed after paused
	
		manager.isPaused(false);
	}
}
