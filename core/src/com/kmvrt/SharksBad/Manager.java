package com.kmvrt.SharksBad;

//import java.util.LinkedList;
//import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

//import com.badlogic.gdx.math.Intersector;

public class Manager implements Disposable {

// fields -------------------------------------------------------------------
//	private static final byte FRICTION_Y = 1;
//	private static final byte FRICTION_X = 50;
	
	private byte MAP_ACCEL;
		// boost in map speed per second
	private short INIT_SPEED;
	private short MAX_SPEED;

//	public static final short CHAR_SIZE = 96;
	
	// sprite move direction
	public static final byte MOVE_UP = 1;
	public static final byte MOVE_LEFT = 2;
	public static final byte MOVE_RIGHT = 3;
	public static final byte MOVE_DOWN = 4;

	// to control the main character
	private byte moveDir;
		// main chara move direction (use the static variables)
//	private float charSpeed;
		// main chara move speed per second (in pixel)
//	private boolean stillMoving;
		// wheter the main chara is still movin or not

	private float mapSpeed;
		// map shifting speed per second (in pixel)
	
	// the map manager and the renderer
	private Navigator navigator;
	private Painter painter;
	private Advertiser advertiser;

	public TextureAtlas mainMenu;
	public TextureAtlas gameOverMenu;
	
	public TextureAtlas buttons;
	public Rectangle musicBtn;
	public Rectangle hintBtn;
	public Rectangle creditBtn;
	private boolean musicPlayed;
	private boolean hintClicked;
	private boolean creditClicked;
//	public TextureAtlas numbers;
	
	public float stateTime;

	public TextureAtlas mainCharImg;
	private Animation mainCharUp;
	private Animation mainCharDown;
	private Animation mainCharRight;
	private Animation mainCharLeft;
	private Animation mainCharDie;
	private float dieTime;
	
	private Sound scream;
	private Sound blood;
	private Sound shot;
	private Sound gunCocking;
	private Music ost;
	
	// game sprites (main characters, wall, path, etc.) 
	private Sprite mainChar;
	public Sprite feet;	// for collision detection on mainChar

	private boolean gameOver;
	private boolean paused;
	private boolean onMainMenu;
	private boolean showingOff;
//	private boolean onCredit;
	private boolean adTime;
	
	private int score;
//	private int lastShot;
		// time of the last shot (in milisec)
	private Rectangle hitspot;
	private boolean shooting;
	private boolean cocking;
	
	private int highscore;
	
// constructor --------------------------------------------------------------
	public Manager(Advertiser advertiser, int highscore) {	/////////////////////////////////////////////
	
		if(Gdx.graphics.getWidth() >= 1440) {
			MAP_ACCEL = 100;
			INIT_SPEED = 262;
			MAX_SPEED = 2500;
			
			mainMenu = new TextureAtlas("1440/Menu.pack");
			gameOverMenu = new TextureAtlas("1440/Gameover.pack");
			buttons = new TextureAtlas("1440/Button.pack");
			
		} else if(Gdx.graphics.getWidth() >= 1080) {
			MAP_ACCEL = 75;
			INIT_SPEED = 196;
			MAX_SPEED = 1875;
			
			mainMenu = new TextureAtlas("1080/Menu.pack");
			gameOverMenu = new TextureAtlas("1080/Gameover.pack");
			buttons = new TextureAtlas("1080Button.pack");
			
		} else if(Gdx.graphics.getWidth() >= 720) {
			MAP_ACCEL = 50;
			INIT_SPEED = 130;
			MAX_SPEED = 1250;
			
			mainMenu = new TextureAtlas("720/Menu.pack");
			gameOverMenu = new TextureAtlas("720/Gameover.pack");
			buttons = new TextureAtlas("720/Button.pack");
			
		} else if(Gdx.graphics.getWidth() >= 540) {
			MAP_ACCEL = 38;
			INIT_SPEED = 98;
			MAX_SPEED = 938;
			
			mainMenu = new TextureAtlas("540/Menu.pack");
			gameOverMenu = new TextureAtlas("540/Gameover.pack");
			buttons = new TextureAtlas("540/Button.pack");
			
		} else if(Gdx.graphics.getWidth() >= 480) {
			MAP_ACCEL = 33;
			INIT_SPEED = 87;
			MAX_SPEED = 833;
			
			mainMenu = new TextureAtlas("480/Menu.pack");
			gameOverMenu = new TextureAtlas("480/Gameover.pack");
			buttons = new TextureAtlas("480/Button.pack");
			
		}
		
		this.advertiser = advertiser;
		adTime = false;
		Timer.schedule(		// can only show ad every 3 mins
			new Timer.Task() {
				
				@Override
				public void run() {
					
					adTime = true;
				}
			}, 20, 20);
	
		
		this.highscore = highscore;
		
		hintClicked = false;
		creditClicked = false;
		
		initMainChar();
		initSound();
		initButtons();
		
		navigator = new Navigator(this);
		painter = new Painter(this);
		
//		onCredit = false;
		showingOff = true;
		Timer.schedule(
			new Timer.Task() {
				
				@Override
				public void run() {
					
					showingOff = false;
//					initNewGame();
				}
			}, 3);	// show the company name for 3 seconds

		initNewGame();
	} // end of Manager() constructor

	private void initMainChar() {

		if(Painter.CAM_WIDTH >= 1440) {
			mainCharImg = new TextureAtlas("1440/mainChar.pack");
		} else if(Painter.CAM_WIDTH >= 1080) {
			mainCharImg = new TextureAtlas("1080/mainChar.pack");
		} else if(Painter.CAM_WIDTH >= 720) {
			mainCharImg = new TextureAtlas("720/mainChar.pack");
		} else if(Painter.CAM_WIDTH >= 540) {
			mainCharImg = new TextureAtlas("540/mainChar.pack");
		} else if(Painter.CAM_WIDTH >= 480) {
			mainCharImg = new TextureAtlas("480/mainChar.pack");
		}
		
		mainChar = new Sprite(mainCharImg.findRegion("mainCharUp2"));
		feet = new Sprite(mainCharImg.findRegion("feet"));
		
		mainCharUp = new Animation(0.1f, mainCharImg.findRegion("mainCharUp1"),
				mainCharImg.findRegion("mainCharUp2"),
				mainCharImg.findRegion("mainCharUp3"));
		mainCharUp.setPlayMode(PlayMode.LOOP_PINGPONG);
		
		mainCharDown = new Animation(0.1f, mainCharImg.findRegion("mainCharDown1"),
				mainCharImg.findRegion("mainCharDown2"),
				mainCharImg.findRegion("mainCharDown3"));
		mainCharDown.setPlayMode(PlayMode.LOOP_PINGPONG);
		
		mainCharRight = new Animation(0.1f, mainCharImg.findRegion("mainCharRight1"),
				mainCharImg.findRegion("mainCharRight2"),
				mainCharImg.findRegion("mainCharRight3"));
		mainCharRight.setPlayMode(PlayMode.LOOP_PINGPONG);
		
		mainCharLeft = new Animation(0.1f, mainCharImg.findRegion("mainCharLeft1"),
				mainCharImg.findRegion("mainCharLeft2"),
				mainCharImg.findRegion("mainCharLeft3")); 
		mainCharLeft.setPlayMode(PlayMode.LOOP_PINGPONG);
		
		mainCharDie = new Animation(0.2f, mainCharImg.findRegion("die1"),
				mainCharImg.findRegion("die2"),
				mainCharImg.findRegion("die3"),
				mainCharImg.findRegion("die4"),
				mainCharImg.findRegion("die5"),
				mainCharImg.findRegion("die6"));
//		mainCharDie.setPlayMode(PlayMode.NORMAL);
	}
	
	private void initSound() {
		
		ost = Gdx.audio.newMusic(Gdx.files.internal("ost.wav"));
		ost.setLooping(true);
		musicPlayed = true;
		
		scream = Gdx.audio.newSound(Gdx.files.internal("Child_Scream.wav"));
		blood = Gdx.audio.newSound(Gdx.files.internal("Blood_Squirt.wav"));
		shot = Gdx.audio.newSound(Gdx.files.internal("Shotgun_Blast.wav"));
		gunCocking = Gdx.audio.newSound(Gdx.files.internal("Gun_Cocking_Fast.wav"));
	}
	
	private void initButtons() {
	
		musicBtn = new Rectangle(
				Painter.CAM_WIDTH - buttons.findRegion("musicOn").packedWidth - 10,
				(Painter.CAM_HEIGHT - mainMenu.findRegion("logo").packedHeight) -
				buttons.findRegion("musicOn").packedHeight - 30,
				buttons.findRegion("musicOn").packedWidth,
				buttons.findRegion("musicOn").packedHeight);
		
		hintBtn = new Rectangle(
				Painter.CAM_WIDTH - buttons.findRegion("hint").packedWidth - 10,
				(Painter.CAM_HEIGHT - mainMenu.findRegion("logo").packedHeight) -
				buttons.findRegion("musicOn").packedHeight - 30 - 
				buttons.findRegion("hint").packedHeight - 30,
				buttons.findRegion("hint").packedWidth,
				buttons.findRegion("hint").packedHeight);
	}
	
	
// methods ------------------------------------------------------------------
	public void update() { /////////////////////////////////////////////
		// update the sprites data 

		updateCharMap();
		navigator.animate(stateTime);
		 while(navigator.getTopBlock().getY() < Painter.CAM_HEIGHT) {
			// if it's running out of block
			navigator.deploy();
		}
		navigator.setShark(mainChar);
		checkCollision();
	} // end of update() method
	
	private void updateCharMap() {
		// update main chara and map
		
		float delta = Gdx.graphics.getDeltaTime();
		stateTime += delta;
		
		if(!onMainMenu && !paused && ! gameOver) {
//			float x;
//			float y;
			
			switch(moveDir) {
			
				case MOVE_UP:	
					if(mainChar.getY() < (Painter.CAM_HEIGHT / 2) - mainChar.getHeight()) {
						mainChar.translate(0, mapSpeed * delta);
							// move upward
						feet.translate(0, mapSpeed * delta);
					} else {
						navigator.move(0, -(mapSpeed * delta));
						// move the map vertically
						// to create the illusion of the map moving

					}
					
					if(!shooting) {
//						x = mainChar.getX();
//						y = mainChar.getY();
						mainChar.setRegion(mainCharUp.getKeyFrame(stateTime));
//						mainChar.setPosition(x, y);
					}
					break;

				case MOVE_DOWN:
					// move downward
					mainChar.translate(0, -(mapSpeed * delta));
					feet.translate(0, -mapSpeed * delta);
					
					if(!shooting) {
//					x = mainChar.getX();
//					y = mainChar.getY();
						mainChar.setRegion(mainCharDown.getKeyFrame(stateTime));
//					mainChar.setPosition(x, y);
					}
					break;

				case MOVE_RIGHT:
					if(mainChar.getX() >= ((Gdx.graphics.getWidth() - mainChar.getWidth()) / 2)
						&& navigator.isRightHidden()) {
						// if mainChar is in the middle or a bit more to the right
						// and there is still hidden regions in the right
						// just move the map the opposite direction of the mainChar
						// to make an illusion of the character moving
						navigator.move(-mapSpeed * delta, 0);
						if(navigator.getX() + navigator.getWidth() < Painter.CAM_WIDTH) {
							// over to the right
							navigator.move((Painter.CAM_WIDTH - (navigator.getX() + navigator.getWidth())- 1), 0);
								// make x + width = screen width
						}
					} else {
						// move right
						mainChar.translate(mapSpeed * delta, 0);
						feet.translate(mapSpeed * delta, 0);
					}
	
					if(!shooting) {
//					x = mainChar.getX();
//					y = mainChar.getY();
						mainChar.setRegion(mainCharRight.getKeyFrame(stateTime));
//					mainChar.setPosition(x, y);
					}
					break;

				case MOVE_LEFT:
					if(mainChar.getX() <= ((Gdx.graphics.getWidth() - mainChar.getWidth()) / 2)
						&& navigator.isLeftHidden()) {
						navigator.move(mapSpeed * delta, 0);
						if(navigator.getX() > 0) {
							// over to the right
							navigator.move(-navigator.getX(), 0);
								// make the x into 0
						}
						
					} else {
						// move left
						mainChar.translate(-mapSpeed * delta, 0);
						feet.translate(-mapSpeed * delta, 0);
					}
					
					if(!shooting) {
//					x = mainChar.getX();
//					y = mainChar.getY();
						mainChar.setRegion(mainCharLeft.getKeyFrame(stateTime));
//					mainChar.setPosition(x, y);
					}
					break;

				default:
					System.err.println("Invalid data for variable: moveDir");
					Gdx.app.exit();
			}	// switch's end
		
			if(mapSpeed < MAX_SPEED)
				mapSpeed += (float)MAP_ACCEL * delta;
			else if(mapSpeed > MAX_SPEED)
				mapSpeed = MAX_SPEED; 
				// increase the map speed
		} else if(gameOver) {
			// keep move the camera upward
			// and initiate the death animation
			dieTime += delta;
			navigator.move(0, -(mapSpeed * delta));
			mainChar.translate(0, -(mapSpeed * delta));
			float x = mainChar.getX();
			float y = mainChar.getY();
			mainChar.setRegion(mainCharDie.getKeyFrame(dieTime));
			mainChar.setPosition(x, y);	
		}
	}

	private void checkCollision() {
		 
		// collision detection and other cause that may cause game over
//		Block hitBlock;
		if(navigator.isHitObs(feet) != null
			&& !gameOver) {
			// hit an obstacle
			if(moveDir == MOVE_UP) {
				// above the char
//				mainChar.setRegion(mainCharImg.findRegion("dieUp"));
				mainChar.translate(0, mainChar.getHeight());
				
			} else if(moveDir == MOVE_DOWN) {
				// below the char
//				mainChar.setRegion(mainCharImg.findRegion("dieDown"));
				mainChar.translate(0, -mainChar.getHeight());
				
			} else if(moveDir == MOVE_RIGHT) {
				// next to (right side of) the char
//				mainChar.setRegion(mainCharImg.findRegion("dieRight"));
				mainChar.translate(mainChar.getWidth(), 0);
				
			} else if(moveDir == MOVE_LEFT) {
				// next to (left side of) the char
//				mainChar.setRegion(mainCharImg.findRegion("dieLeft"));
				mainChar.translate(-mainChar.getWidth(), 0);
			}  
			
			// at last
/*			x = mainChar.getX();
			y = mainChar.getY();
			mainChar.setRegion(mainCharImg.findRegion("die"));
			mainChar.setPosition(x, y); */ 
			scream.play();
			isGameOver(true);
		} else if(feet.getX() + feet.getWidth() > Painter.CAM_WIDTH
			&& !gameOver) {
			// over to the right
			mainChar.translate(mainChar.getWidth(), 0);
			scream.play();
			isGameOver(true);
			
		} else if(feet.getX() < 0 && !gameOver) {
			// over to the left
			mainChar.translate(-mainChar.getWidth(), 0);
			scream.play();
			isGameOver(true);
			
		} else if(feet.getY() < -feet.getHeight() && !gameOver) {
			// below the screen
			mainChar.translate(0, -mainChar.getHeight());
			scream.play();
			isGameOver(true);
		}
	}


	public void shoot() {
		// ask the mainChar to shoot
		
/*		int aTime = (int)(System.nanoTime() / 1000);
		int timeDiff = aTime - lastShot; */
		if(!cocking && !onMainMenu) {
			// can only shoot every 0.5 secs
//			lastShot = aTime;
			shot.play();
			cocking = true;
			Timer.schedule(
				new Timer.Task() {
					@Override
					public void run() {
						cocking = false;
					}
				}, 0.3f);
			
			
			switch(moveDir) {
				case MOVE_UP: 
					hitspot = new Rectangle(mainChar.getX(),
							feet.getY() + feet.getHeight(),
							mainChar.getWidth(), mainChar.getHeight());
					if(navigator.isHitShark(hitspot)) {
						score++;
						blood.play();
					}
					mainChar.setRegion(mainCharImg.findRegion("mainCharUpShoot"));
					break;
					
				case MOVE_DOWN:
					hitspot = new Rectangle(mainChar.getX(),
							mainChar.getY() - mainChar.getHeight(),
							mainChar.getWidth(), mainChar.getHeight());
					if(navigator.isHitShark(hitspot)) {
						score++;
						blood.play();
					}
					mainChar.setRegion(mainCharImg.findRegion("mainCharDownShoot"));
					break;
					
				case MOVE_RIGHT:
					hitspot = new Rectangle(mainChar.getX() + mainChar.getWidth(),
							mainChar.getY(),
							mainChar.getWidth(), mainChar.getHeight());
					if(navigator.isHitShark(hitspot)) {
						score++;
						blood.play();
					}
					mainChar.setRegion(mainCharImg.findRegion("mainCharRightShoot"));
					break;
				
				case MOVE_LEFT:
					hitspot = new Rectangle(mainChar.getX() - mainChar.getWidth(),
							mainChar.getY(),
							mainChar.getWidth(), mainChar.getHeight());
					if(navigator.isHitShark(hitspot)) {
						score++;
						blood.play();
					}
					mainChar.setRegion(mainCharImg.findRegion("mainCharLeftShoot"));
					break;
					
			}
			shooting = true;
			Timer.schedule(
				new Timer.Task() {
					@Override
					public void run() {
						shooting = false;
					}
				}, 0.1f);
			// turn it to false after 0.1 secs
		} else {	// is cocking
			gunCocking.play();
		}
	} // shoot()'s end
	
	public void setMovement(byte dir) { ///////////////////////
		// set how which way and far should the main character move 
		// the character can't turn 180 degrees (e.g. up to down)
		

		// set the main chara's texture
		// just return if the user is
		// trying to turn 180 degrees

		switch(dir){
			case MOVE_UP:
				if(moveDir == MOVE_DOWN)
					return;
				break;
				
			case MOVE_DOWN:
				if(moveDir == MOVE_UP)
					return;
				break;
				
			case MOVE_LEFT:
				if(moveDir == MOVE_RIGHT)
					return;
				break;
				
			case MOVE_RIGHT:
				if(moveDir == MOVE_LEFT)
					return;
				break;
		}
		
		moveDir = dir;
	}	// setMoveDistance(short)'s end

	public void render() { /////////////////////////////////////////////
		// render the game to the screen
	
		if(paused || gameOver || onMainMenu) {
			painter.render(mainChar, navigator, true);
		} else {
			painter.render(mainChar, navigator, false);
		}
	} // end of render() method


// init new game **************************************************************************** 
	public void initNewGame() {
		// initialise a new game
		
		// reset every thing
		onMainMenu = true;
		gameOver = false;
		paused = false;

		mapSpeed = INIT_SPEED;
//		stillMoving = false;
//		charSpeed = 0;
//		setMovement(MOVE_UP);
		moveDir = MOVE_UP;
		score = 0;
		
		stateTime = 0;
		dieTime = 0;
		
//		lastShot = 0;
		shooting = false;
		cocking = false;

		navigator.initNewMap();
		Block startPos = navigator.getStartPos();
		mainChar.setRegion(mainCharImg.findRegion("mainCharUp2"));
		mainChar.setPosition((startPos.getHeight() - mainChar.getHeight()) / 2, 
				(startPos.getWidth() - mainChar.getWidth()) / 2);
				// put the mainChar in the middle of the starting block);
		
		if(Painter.CAM_WIDTH >= 1440) {
			feet.setPosition(mainChar.getX() + 52, mainChar.getY() + 20);
		} else if(Painter.CAM_WIDTH >= 1080) {
			feet.setPosition(mainChar.getX() + 39, mainChar.getY() + 15);
		} else if(Painter.CAM_WIDTH >= 720) {
			feet.setPosition(mainChar.getX() + 26, mainChar.getY() + 10);
		} else if(Painter.CAM_WIDTH >= 540) {
			feet.setPosition(mainChar.getX() + 19.5f, mainChar.getY() + 7.5f);
		} else if(Painter.CAM_WIDTH >= 480) {
			feet.setPosition(mainChar.getX() + 17.3f, mainChar.getY() + 6.75f);
		}
		
		if(isMusicPlayed())
			ost.play();
	}


// checkers and setters *********************************************************************
	// pause or resume the game //////////////////////////////////////////////////
	public boolean isPaused() {
	
		return paused;
	}

	public void isPaused(boolean setter) {
	
		paused = setter;
	}

	// return or move from main menu //////////////////////////////////////////////
	public boolean isOnMainMenu() {
	
		return onMainMenu;	
	}

	public void isOnMainMenu(boolean setter) {
	
		onMainMenu = setter;
	}

	// go to or move from game over screen ////////////////////////////////////////
	public boolean isGameOver() {
	
		return gameOver;
	}

	public void isGameOver(boolean setter) {
	
		if(setter) {
			if(score > highscore) {
				highscore = score;
			}
			
			ost.stop();
			
			if(adTime) {
				advertiser.showAd();
				adTime = false;
			}
		}
		
		gameOver = setter;
	}
	
	public int getScore() {
		
		return score;
	}
	
	public int getHighscore() {
		
		return highscore;
	}
	
	public Rectangle getHitspot() {
		
		return hitspot;
	}
	
	public boolean isShooting() {
		
		return shooting;
	}
	
	public boolean isMusicPlayed() {
		
		return musicPlayed;
	}
	
	public void toggleMusic() {
		
		if(isMusicPlayed()) {
			ost.stop();
			musicPlayed = false;
		} else {
			ost.play();
			musicPlayed = true;
		}
	}
	
	public boolean isHintClicked() {
		
		return hintClicked;
	}
	
	public void clickHint() {
		
		hintClicked = true;
	}
	
	public void closeHint() {
		
		hintClicked = false;
	}
	
	public boolean isCreditClicked() {
		
		return creditClicked;
	}
	
	public void clickCredit() {
		
		creditClicked = true;
	}
	
	public void closeCredit() {
		
		creditClicked = false;
	}
	
	public boolean isShowingOff() {
		
		return showingOff;
	}
	
//	public boolean isOnCredit() {
		
//		return onCredit;
//	}
	
	
	@Override
	public void dispose() {
		
		mainCharImg.dispose();
		navigator.dispose();
		painter.dispose();
		
		mainMenu.dispose();
		gameOverMenu.dispose();
		buttons.dispose();
		
		blood.dispose();
		shot.dispose();
		scream.dispose();
		ost.dispose();
		gunCocking.dispose();
	}
}	// class' end