package com.kmvrt.SharksBad;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.utils.viewport.Viewport;
//import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.ArrayList;


public class Painter implements Disposable {

// fields -----------------------------------------------------------------
	public static short CAM_HEIGHT;
	public static short CAM_WIDTH;
	static {
		CAM_HEIGHT = (short)Gdx.graphics.getHeight();
		CAM_WIDTH = (short)Gdx.graphics.getWidth();
	}

//	private static final byte NUM_WIDTH = 96;
//	private static final short NUM_HEIGHT = 160;
	private short numWidth, numHeight;
//	private static final byte NUM_SPACE = 0;
	
	private SpriteBatch batch;
		// for rendering sprites
	
	// main menu images
//	private Texture logo;
		// game title
//	private Texture swipeToStart;

	// game over images
//	private Texture yerDead;
//	private Texture touchToContinue;
	
	private Manager manager;

	private TextureAtlas numbers;
	private Texture comp;
	private Texture shadow;
//	private Texture shadow;
//	private byte digit;
//	private OrthographicCamera cam;
//	private Viewport viewport;

// constructor ------------------------------------------------------------
	public Painter(Manager manager) {
		
		batch = new SpriteBatch();
		
		comp = null;
		if(CAM_WIDTH >= 720) {
			// if higher than 720p
			comp = new Texture("kmvrt-720.png");
			
		} else {
			// lower than 720p
			comp = new Texture("kmvrt-540.png");
		}
		
//		cam = new OrthographicCamera(CAM_WIDTH, CAM_HEIGHT);
//		cam.zoom = 3.0f;
//		cam.translate(CAM_WIDTH / 2, CAM_HEIGHT / 2, 0);
//		viewport = new FitViewport(CAM_WIDTH, CAM_HEIGHT, cam);	
//		shadow = null;
		if(CAM_WIDTH >= 1440) {
			numbers = new TextureAtlas("1440/Numbers.pack");
			shadow = new Texture("1440/shadow.png");
		} else if(CAM_WIDTH >= 1080) {
			numbers = new TextureAtlas("1080/Numbers.pack");
			shadow = new Texture("1080/shadow.png");
		} else if(CAM_WIDTH >= 720) {
			numbers = new TextureAtlas("720/Numbers.pack");
			shadow = new Texture("720/shadow.png");
		} else if(CAM_WIDTH >= 540) {
			numbers = new TextureAtlas("540/Numbers.pack");
			shadow = new Texture("540/shadow.png");
		} else if(CAM_WIDTH >= 480) {
			numbers = new TextureAtlas("480/Numbers.pack");
			shadow = new Texture("480/shadow.png");
		}
		
		numWidth = (short)numbers.findRegion("1").packedWidth;
		numHeight = (short)numbers.findRegion("1").packedHeight;
		
		this.manager = manager;
		
//		shadow = null;
		
//		initUI();
		
//		digit = 0;
	}
	
// methods ---------------------------------------------------------------
	// all methods of this object must be made static
	public void render(Sprite mainChar, Navigator navigator, boolean darken) { 
		// render the sprites to the screen
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(darken) {
//			batch.setColor(Color.NAVY);
				// darken the color 
		}
//		batch.setProjectionMatrix(cam.combined);
		batch.begin();
//		drawClouds(navigator.getClouds());
		if(manager.isShowingOff()) {

			batch.draw(comp, (CAM_WIDTH - comp.getWidth()) / 2, 
				((CAM_HEIGHT - comp.getHeight()) / 2) + 10);
			
			batch.end();
			return;
		}
		
		drawMap(navigator.getMap());
		drawSharks(navigator.getShark());
		drawMainChar(mainChar);
		
		if(!manager.isOnMainMenu() && !manager.isGameOver())
			drawScore(manager.getScore(), false); 
			
//		manager.feet.draw(batch);
//		batch.draw(mainChar, mainChar.getX(), mainChar.getY());
		batch.end();
//		batch.setColor(Color.CLEAR);
			// reset the color
//		float dpi = Gdx.graphics.getDensity() / 160;
		
		if(manager.isOnMainMenu()) {
			batch.begin();
			batch.draw(manager.mainMenu.findRegion("logo"), 
					(CAM_WIDTH - manager.mainMenu.findRegion("logo").packedWidth) / 2,
					(CAM_HEIGHT - manager.mainMenu.findRegion("logo").packedHeight));
			
			drawScore(manager.getHighscore(), true);
			batch.draw(manager.mainMenu.findRegion("swipeToStart"),
					CAM_WIDTH - manager.mainMenu.findRegion("swipeToStart").packedWidth - 30,
					30);
			
			if(manager.isMusicPlayed()) {
				batch.draw(manager.buttons.findRegion("musicOn"),
					manager.musicBtn.getX(),
					manager.musicBtn.getY());
			} else {
				batch.draw(manager.buttons.findRegion("musicOff"),
					manager.musicBtn.getX(),
					manager.musicBtn.getY());
			}
			
			if(!manager.isHintClicked()) {
				batch.draw(manager.buttons.findRegion("hint"),
						manager.hintBtn.getX(),
						manager.hintBtn.getY());
			} else {
				batch.draw(manager.buttons.findRegion("hintClicked"),
						manager.hintBtn.getX(),
						manager.hintBtn.getY());
				
				// draw the how to play screen
				
				
				batch.draw(shadow, 0, 0, CAM_WIDTH, CAM_HEIGHT);
				batch.draw(manager.mainMenu.findRegion("howToPlay"),
					(CAM_WIDTH - manager.mainMenu.findRegion("howToPlay").packedWidth) / 2,
					(CAM_HEIGHT - manager.mainMenu.findRegion("howToPlay").packedHeight) / 2);
			}
			
			if(manager.isCreditClicked()) {
				// draw the credit screen
				Texture shadow = null;
				if(CAM_WIDTH >= 1440) {
					shadow = new Texture("1440/shadow.png");
				} else if(CAM_WIDTH >= 1080) {
					shadow = new Texture("1080/shadow.png");
				} else if(CAM_WIDTH >= 720) {
					shadow = new Texture("720/shadow.png");
				} else if(CAM_WIDTH >= 540) {
					shadow = new Texture("540/shadow.png");
				} else if(CAM_WIDTH >= 480) {
					shadow = new Texture("480/shadow.png");
				}
				
				batch.draw(shadow, 0, 0, CAM_WIDTH, CAM_HEIGHT);
				batch.draw(manager.mainMenu.findRegion("credits"),
					(CAM_WIDTH - manager.mainMenu.findRegion("credits").packedWidth) / 2,
					(CAM_HEIGHT - manager.mainMenu.findRegion("credits").packedHeight) / 2);
				
				shadow.dispose();
			}

			batch.end();
			
		} else if(manager.isGameOver()) {
			batch.begin();
			
			batch.draw(manager.gameOverMenu.findRegion("yerDead"),
				(CAM_WIDTH - manager.gameOverMenu.findRegion("yerDead").packedWidth) / 2,
				(CAM_HEIGHT - manager.gameOverMenu.findRegion("yerDead").packedHeight));
			batch.draw(manager.gameOverMenu.findRegion("touchToContinue"),
				(CAM_WIDTH - manager.gameOverMenu.findRegion("touchToContinue").packedWidth) / 2,
				50);
			drawScore(manager.getScore(), manager.getHighscore());
			
			batch.end();
		} else if(manager.isPaused()) {}
	}
	
	private void drawMap(ArrayList<Block> map) { ///////////
		// render the map to the screen
		
		for(Block b : map) {
			// loop through every block in the map
		
	//		batch.draw(b, b.getX(), b.getY());
	/*		if(((b.getX() + b.getWidth() > 0 && b.getX() + b.getWidth() < Painter.CAM_WIDTH)
				|| ( b.getX() > 0 && b.getX() > Painter.CAM_WIDTH))
				&&
				((b.getY() + b.getHeight() > 0 && b.getY() + b.getHeight() < Painter.CAM_HEIGHT)
				|| (b.getY() > 0 && b.getY() < Painter.CAM_HEIGHT))) {
				// only draw stuff on screen */
				b.draw(batch);
		//	}
		}
	} // end of drawMap(LinkedList<Block>) method
	
	private void drawSharks(ArrayList<Sprite> sharks) {
		// render the sharks to the screen
		
		for(Sprite s : sharks) {
			// loop through every shark in collection
			
	/*		if(((s.getX() + s.getWidth() > 0 && s.getX() + s.getWidth() < Painter.CAM_WIDTH)
					|| ( s.getX() > 0 && s.getX() > Painter.CAM_WIDTH))
					&&
					((s.getY() + s.getHeight() > 0 && s.getY() + s.getHeight() < Painter.CAM_HEIGHT)
					|| (s.getY() > 0 && s.getY() < Painter.CAM_HEIGHT))) { */
				s.draw(batch);
		//	}
		}
	} // drawSharks(ArrayList<Sprite>)'s end

	private void drawMainChar(Sprite mainChar) {
		
		if(manager.isShooting()) {
			batch.draw(manager.mainCharImg.findRegion("blow"), 
				manager.getHitspot().getX(), manager.getHitspot().getY());
		}
		mainChar.draw(batch);
	}
	
	private void drawScore(int score, boolean isHighscore) {
		
		String scoreStr = String.valueOf(score);
		byte digit = (byte)scoreStr.length();
		
		int x, y;
		if(!isHighscore) {
			x = (Painter.CAM_WIDTH - ((digit * numWidth))) / 2;
				// put it in the middle
			y = Painter.CAM_HEIGHT - numHeight - 10;
		} else {
//			float dpi = Gdx.graphics.getDensity() / 160;
			x = (Painter.CAM_WIDTH - ((digit * numWidth))) / 2;
			y = (Painter.CAM_HEIGHT - numHeight) / 2;
			
			// draw highscore label
			batch.draw(numbers.findRegion("highscore"), 
				(CAM_WIDTH - numbers.findRegion("highscore").packedWidth) / 2,
				y + numHeight);
		}
		
		for(short i = 0; i < scoreStr.length(); i++) {
			
			switch(scoreStr.charAt(i)) {
			
				case '1':
					batch.draw(numbers.findRegion("1"), x + (numWidth * i) , y);
					break;
					
				case '2':
					batch.draw(numbers.findRegion("2"), x + (numWidth * i) , y);
					break;
					
				case '3':
					batch.draw(numbers.findRegion("3"), x + (numWidth * i) , y);
					break;
					
				case '4':
					batch.draw(numbers.findRegion("4"), x + (numWidth * i) , y);
					break;
					
				case '5':
					batch.draw(numbers.findRegion("5"), x + (numWidth * i) , y);
					break;
					
				case '6':
					batch.draw(numbers.findRegion("6"), x + (numWidth * i) , y);
					break;
					
				case '7':
					batch.draw(numbers.findRegion("7"), x + (numWidth * i) , y);
					break;
					
				case '8':
					batch.draw(numbers.findRegion("8"), x + (numWidth * i) , y);
					break;
					
				case '9':
					batch.draw(numbers.findRegion("9"), x + (numWidth * i) , y);
					break;
					
				case '0':
					batch.draw(numbers.findRegion("0"), x + (numWidth * i) , y);
					break;
			}
		}
	}
	
	private void drawScore(int score, int highscore) {
		
		String scoreStr = String.valueOf(score);
		byte digit = (byte)scoreStr.length();
		
		int x, y;
		//			float dpi = Gdx.graphics.getDensity() / 160;
		x = (Painter.CAM_WIDTH - ((digit * numWidth) )) / 2;
		y = (Painter.CAM_HEIGHT - numHeight) / 2 + 10;
		
		// draw score label
		batch.draw(numbers.findRegion("score"), 
			(CAM_WIDTH - numbers.findRegion("score").packedWidth) / 2, y + numHeight);
		
		for(short i = 0; i < scoreStr.length(); i++) {
			
			switch(scoreStr.charAt(i)) {
			
				case '1':
					batch.draw(numbers.findRegion("1"), x + (numWidth * i) , y);
					break;
					
				case '2':
					batch.draw(numbers.findRegion("2"), x + (numWidth * i) , y);
					break;
					
				case '3':
					batch.draw(numbers.findRegion("3"), x + (numWidth * i) , y);
					break;
					
				case '4':
					batch.draw(numbers.findRegion("4"), x + (numWidth * i) , y);
					break;
					
				case '5':
					batch.draw(numbers.findRegion("5"), x + (numWidth * i) , y);
					break;
					
				case '6':
					batch.draw(numbers.findRegion("6"), x + (numWidth * i) , y);
					break;
					
				case '7':
					batch.draw(numbers.findRegion("7"), x + (numWidth * i) , y);
					break;
					
				case '8':
					batch.draw(numbers.findRegion("8"), x + (numWidth * i) , y);
					break;
					
				case '9':
					batch.draw(numbers.findRegion("9"), x + (numWidth * i) , y);
					break;
					
				case '0':
					batch.draw(numbers.findRegion("0"), x + (numWidth * i) , y);
					break;
			}
		}
		
		scoreStr = String.valueOf(highscore);
		digit = (byte)scoreStr.length();
		x = (Painter.CAM_WIDTH - ((digit * numWidth) )) / 2;
		y -= numHeight + 50 - 10;
		
		// draw highscore label
		batch.draw(numbers.findRegion("highscore"), 
			(CAM_WIDTH - numbers.findRegion("highscore").packedWidth) / 2, y + numHeight);
				
		for(short i = 0; i < scoreStr.length(); i++) {
			
			switch(scoreStr.charAt(i)) {
			
				case '1':
					batch.draw(numbers.findRegion("1"), x + (numWidth * i) , y);
					break;
					
				case '2':
					batch.draw(numbers.findRegion("2"), x + (numWidth * i) , y);
					break;
					
				case '3':
					batch.draw(numbers.findRegion("3"), x + (numWidth * i) , y);
					break;
					
				case '4':
					batch.draw(numbers.findRegion("4"), x + (numWidth * i) , y);
					break;
					
				case '5':
					batch.draw(numbers.findRegion("5"), x + (numWidth * i) , y);
					break;
					
				case '6':
					batch.draw(numbers.findRegion("6"), x + (numWidth * i) , y);
					break;
					
				case '7':
					batch.draw(numbers.findRegion("7"), x + (numWidth * i) , y);
					break;
					
				case '8':
					batch.draw(numbers.findRegion("8"), x + (numWidth * i) , y);
					break;
					
				case '9':
					batch.draw(numbers.findRegion("9"), x + (numWidth * i) , y);
					break;
					
				case '0':
					batch.draw(numbers.findRegion("0"), x + (numWidth * i) , y);
					break;
			}
		}
	}

	public void dispose() {
	
		comp.dispose();
		shadow.dispose();
		numbers.dispose();
		batch.dispose();
	}
}
