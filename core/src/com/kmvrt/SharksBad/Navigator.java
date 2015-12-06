package com.kmvrt.SharksBad;

import java.util.ArrayList;
import java.util.Iterator;
//import java.util.NoSuchElementException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;

public class Navigator implements Disposable {

// fields -----------------------------------------------------------------
	public static short BLOCK_WIDTH;
	public static short BLOCK_HEIGHT;
	static {
		if(Gdx.graphics.getWidth() >= 1440) {
			BLOCK_WIDTH = 384;
			BLOCK_HEIGHT = 384;
		} else if(Gdx.graphics.getWidth() >= 1080) {
			BLOCK_WIDTH = 288;
			BLOCK_HEIGHT = 288;
		} else if(Gdx.graphics.getWidth() >= 720) {
			BLOCK_WIDTH = 192;
			BLOCK_HEIGHT = 192;
		} else if(Gdx.graphics.getWidth() >= 540) {
			BLOCK_WIDTH = 144;
			BLOCK_HEIGHT = 144;
		} else if(Gdx.graphics.getWidth() >= 480) {
			BLOCK_WIDTH = 128;
			BLOCK_HEIGHT = 128;
		}
	}
	
	// for collision detection
	public static final byte HIT_WALL = 1;
	public static final byte HIT_OBS = 2;

	private ArrayList<Block> map;
//	private Texture cloud;
//	private ArrayList<Sprite> clouds;
	private LayoutHolder layouts;
	private Manager manager;
	
	public ArrayList<Sprite> sharks;
	private TextureAtlas sharkImg;
	private Animation sharkUp;
	private Animation sharkDown;
	private Animation sharkRight;
	private Animation sharkLeft;
	
	private Animation blockObsAnim;
	private Animation blockPath1Anim;
	private Animation blockPath2Anim;
	private Animation blockPath3Anim;

	private int toRemove;

// constructor ------------------------------------------------------------
	public Navigator(Manager manager) {
	
		this.manager = manager;
		map = new ArrayList<Block>();
		layouts = new LayoutHolder();
		initShark();
		initBlockAnim();
//		this.manager = manager;
		toRemove = -1;
	}

	private void initShark() {
		
		sharks = new ArrayList<Sprite>();
		if(Painter.CAM_WIDTH >= 1440) {
			sharkImg = new TextureAtlas("1440/Shark.pack");
		} else if(Painter.CAM_WIDTH >= 1080) {
			sharkImg = new TextureAtlas("1080/Shark.pack");
		} else if(Painter.CAM_WIDTH >= 720) {
			sharkImg = new TextureAtlas("720/Shark.pack");
		} else if(Painter.CAM_WIDTH >= 540) {
			sharkImg = new TextureAtlas("540/Shark.pack");
		} else if(Painter.CAM_WIDTH >= 480) {
			sharkImg = new TextureAtlas("480/Shark.pack");
		}
		
		sharkUp = new Animation(0.1f, sharkImg.findRegion("sharkUp1"),
			sharkImg.findRegion("sharkUp2"));
		sharkUp.setPlayMode(PlayMode.LOOP);
		
		sharkDown = new Animation(0.1f, sharkImg.findRegion("sharkDown1"),
			sharkImg.findRegion("sharkDown2"));
		sharkDown.setPlayMode(PlayMode.LOOP);
		
		sharkRight = new Animation(0.1f, sharkImg.findRegion("sharkRight1"),
			sharkImg.findRegion("sharkRight2"));
		sharkRight.setPlayMode(PlayMode.LOOP);
		
		sharkLeft = new Animation(0.1f, sharkImg.findRegion("sharkLeft1"),
			sharkImg.findRegion("sharkLeft2"));
		sharkLeft.setPlayMode(PlayMode.LOOP);
	}

	private void initBlockAnim() {
		
		blockObsAnim = new Animation(0.2f, layouts.textures.findRegion("blockObs"),
			layouts.textures.findRegion("blockObs2"));
		blockObsAnim.setPlayMode(PlayMode.LOOP);
		
		blockPath1Anim = new Animation(1, layouts.textures.findRegion("blockPath1"),
				layouts.textures.findRegion("blockPath12"));
		blockPath1Anim.setPlayMode(PlayMode.LOOP);
		
		blockPath2Anim = new Animation(1, layouts.textures.findRegion("blockPath2"),
				layouts.textures.findRegion("blockPath22"));
		blockPath2Anim.setPlayMode(PlayMode.LOOP);
		
		blockPath3Anim = new Animation(1, layouts.textures.findRegion("blockPath3"),
				layouts.textures.findRegion("blockPath32"));
		blockPath3Anim.setPlayMode(PlayMode.LOOP);
	}
	
	
// methods ----------------------------------------------------------------
	public void move(float xSpeed, float ySpeed) {
		// move the map by the number specified
	
		for(Iterator<Block> i = map.iterator(); i.hasNext();) {
			// loop through every block in the map
			// move them by xSpeed and ySpeed
			Block b = i.next();
			
			b.translate(xSpeed, ySpeed);
			if(b.getY() <= -b.getHeight())
				i.remove();
		}
		
		for(Iterator<Sprite> i = sharks.iterator(); i.hasNext();) {
			// loop through every shark in the collection
			// move them by xSpeed and ySpeed
			
			Sprite s = i.next();
			
			s.translate(xSpeed, ySpeed);
			if(s.getY() <= -s.getHeight())
				i.remove();
		}
		
/*		for(Iterator<Sprite> i = clouds.iterator(); i.hasNext();) {
			// loop through every cloud.png
			
			Sprite c = i.next();
			
			c.translate(xSpeed / 8, ySpeed / 8);
				// only move an eighth of the other stuff
			if(c.getY() <= -c.getHeight())
				i.remove();

			Sprite topCloud = getTopCloud();
			while(topCloud.getX() + topCloud.getHeight() < Painter.CAM_HEIGHT) {
				c = new Sprite(cloud);
				c.setPosition(topCloud.getX(), topCloud.getY() + topCloud.getHeight());
				clouds.add(c);
				
				topCloud = getTopCloud();
			}
		} */
	}
	
	public void animate(float stateTime) {
		// animate the block
		
		for(Block b : map) {
			
			switch(b.getTypeID()) {
			
				case LayoutHolder.BLOCK_TYPE_OBS:
					b.setRegion(blockObsAnim.getKeyFrame(stateTime));
					break;
				
				case LayoutHolder.BLOCK_TYPE_PATH1:
					b.setRegion(blockPath1Anim.getKeyFrame(stateTime));
					break;
					
				case LayoutHolder.BLOCK_TYPE_PATH2:
					b.setRegion(blockPath2Anim.getKeyFrame(stateTime));
					break;

				case LayoutHolder.BLOCK_TYPE_PATH3:
					b.setRegion(blockPath3Anim.getKeyFrame(stateTime));
					break;
			}
		}
	}

//	short layoutCt = 0;
	public void deploy() {
		// deploy the map to the game

//		System.out.println("Layout:" + ++layoutCt);
		Block topBlock = getTopBlock();
		if(topBlock.getTypeID() == -1) {
			// first deployment
			addBlocks(layouts.getFirst(), false);
			return;
		}

		addBlocks(layouts.getRandom(
			topBlock.getX(),
			topBlock.getY() + topBlock.getHeight()), true);
			// random layout with position after the last row
	} // deploy() method's end

//	short originXCt = 0;
	private void addBlocks(Block[] blocks, boolean addSharks) {
		// add the block to the map
	
		for(Block b : blocks) {
			// loop through every block to be added
			// and put them into the map
			
			if(b == null) {
				System.err.println("Null block() from LayoutHolder: layout");
				Gdx.app.exit();
			}

			if(addSharks && b.getTypeID() == LayoutHolder.BLOCK_TYPE_PATH1
				|| addSharks && b.getTypeID() == LayoutHolder.BLOCK_TYPE_PATH2
				|| addSharks && b.getTypeID() == LayoutHolder.BLOCK_TYPE_PATH3) {
				// randomly add shark
				byte i = (byte)(Math.random() * 5);
				if(i == 0) {
					Sprite shark = new Sprite(sharkDown.getKeyFrame(0));
					float x = b.getX() + ((b.getWidth() - shark.getWidth()) / 2);
					float y = b.getY() + (b.getHeight() / 4);
					shark.setPosition(x, y);
					sharks.add(shark);
//					System.out.println("shark added at: x = " + x + ", y = " + y);
				}
			}
			
/*			if(b.getX() == 0) 
				System.out.println("X coordinate blocks =" + ++originXCt); */
	//		b.scale(3);
			map.add(b);
		}
//		missingBlockCt = 0;
	}

	public void setShark(Sprite mainChar) {
		// set the shark move direction
		
		float x, y;
		for(Sprite s : sharks) {
			
			if(sharks.indexOf(s) == toRemove)
				return;
			
			if(s.getY() > mainChar.getY() + mainChar.getHeight()) {
				// s is above mainChar
				x = s.getX();
				y = s.getY();
				s.setRegion(sharkDown.getKeyFrame(manager.stateTime));
				s.setPosition(x, y);
				
			} else if(s.getY() + s.getHeight() < mainChar.getY()) {
				// s is below mainChar
				x = s.getX();
				y = s.getY();
				s.setRegion(sharkUp.getKeyFrame(manager.stateTime));
				s.setPosition(x, y);
				
			} else if(s.getX() > mainChar.getX() + mainChar.getWidth()) {
				// s is on the right of mainChar
				x = s.getX();
				y = s.getY();
				s.setRegion(sharkLeft.getKeyFrame(manager.stateTime));
				s.setPosition(x, y);
				
			} else if(s.getX() + s.getWidth() < mainChar.getX()) {
				// s is on the left of mainChar
				x = s.getX();
				y = s.getY();
				s.setRegion(sharkRight.getKeyFrame(manager.stateTime));
				s.setPosition(x, y);
			}
		}
	} // setShark()'s end
	
	public Block getTopBlock() {
		// return the top block (left-most block from the last row)
		
		Block topBlock;
		try {
			topBlock = map.get(0);
		} catch(Exception e) {
			// if there is no block (first execution)
			topBlock = new Block(
				(byte)-1, layouts.textures.findRegion("blockPath3"), 0, 0);
	//		topBlock.setRegion(layouts.textures.findRegion("blockObs"));
				// new block with -1 type ID and (0, 0) coordinates
			return topBlock;
		} 

		for(Block b : map) {
			// loop through every block in the map
			
			if(b.getY() > topBlock.getY()) {
				// if b is in higher row than topBlock
				topBlock = b;

			} else if((b.getY() == topBlock.getY())
					&& (b.getX() < topBlock.getX())) {
				// if b is on the same row as topBlock
				// but is in the lefter column 
				topBlock = b;
			}
		} // for loop's end

		return topBlock;
	} // getTopBlock()'s end

	public Block getStartPos() {
		// get the starting position (left-most first row block) for the main character
		
		Block startPos = map.get(0);

		
		for(Block b : map) {
			// loop through every block in the map
			
			if(b.getY() < startPos.getY()) {
				// if b is lower than startPos
				startPos = b;

			} else if ((b.getY() == startPos.getY())
				&& (b.getX() < startPos.getY())) {
				// if b is on the same row as topBlock
				// but is in the lefter column	
				startPos = b;
			}
		}	// for loop's end
		
		return startPos;
	}

	public float getX() {
		
		Block b = getTopBlock();
		return b.getX();
	}
	
	public int getWidth() {
		
		return Navigator.BLOCK_WIDTH * LayoutHolder.BLOCKS_PER_ROW;
	}
	
	public ArrayList<Block> getMap() {
		
		return map;	
	}

	public ArrayList<Sprite> getShark() {
		
		return sharks;
	}
	
/*	public ArrayList<Sprite> getClouds() {
		
		return clouds;
	} */
	
	// initialise new map ***********************************************
	public void initNewMap() {
		// reset the map
		
		for(Iterator<Block> i = map.iterator(); i.hasNext(); ) {
			// iterate through the map

			i.next();
			i.remove();
		}
		
		for(Iterator<Sprite> i = sharks.iterator(); i.hasNext(); ) {
			// iterate through every shark
			
			i.next();
			i.remove();
		}
		toRemove = -1;
		deploy();
	} // initNewMap()'s end

	
	// checkers *********************************************************
	public Block isHitObs(Sprite sprite) {
	// check if the sprite collide with any wall 	
		
		Rectangle in = new Rectangle();
		for(Block b : map) {
			// loop through every block in the map		
			
			if(b.getTypeID() == LayoutHolder.BLOCK_TYPE_OBS) {
				if(Intersector.intersectRectangles(sprite.getBoundingRectangle(), 
					b.getBoundingRectangle(), in)) {
			//		if(in.getWidth() > 15
			//			&& in.getHeight() > 15) {
						// create a duplicate of b and return it
					Block block = new Block((byte)-1, layouts.textures.findRegion("blockObs"),
							b.getX() + in.getWidth(), b.getY() + in.getHeight());
					return block;
					}
			}
		}	// for loop's end

		// otherwise
		return null;
	} // isHitObs(sprite)'s end

/*	public boolean isHitShark(Sprite sprite) {
		
		for()
	} // isHitShark(Sprite)'s end */
	
	public boolean isHitShark(Rectangle hitspot) {
		// if the shot hit any shark
		// remove the shark/initiate dying sequence
		
		for(Iterator<Sprite> i = sharks.iterator(); i.hasNext(); ) {
			
			Sprite s = i.next();
			if(Intersector.intersectRectangles(
				hitspot, s.getBoundingRectangle(), new Rectangle())) {
				s.setRegion(sharkImg.findRegion("die"));
				toRemove = sharks.indexOf(s);
				Timer.schedule(new Timer.Task() {
					@Override
					public void run() {
						sharks.remove(toRemove);
						toRemove = -1;
					}
				}, 0.1f);
				return true;
			}	
		}
		return false;
	} // isHitShark(Rectangle)'s end
	
	public boolean isRightHidden() {
		// wether there is hidden right side column
		
		for(Block b : map) {
			// loop through every block in the map
		
			if(b.getX() + b.getWidth() > Painter.CAM_WIDTH) {
				// if it's out (in the right side) of the screen
				return true;
			}
		}

		return false;
	}	// isRightHidden()'s end

	public boolean isLeftHidden() {
		// wether there is hidden left side column
	
		for(Block b : map) {
			// loop through every block in the map
		
			if(b.getX() < 0) {
				// if it's out (in the left side) of the screen
				return true;
			}
		}

		return false;
	}
	
/*	private Sprite getTopCloud() {
		// get the top most cloud.png on screen
		
		Sprite topCloud = clouds.get(0);
		for(Sprite c : clouds) {
			
			if(topCloud.getY() > c.getY())
				topCloud = c;
		}
		return topCloud;
	} */
	
	@Override
	public void dispose() {
		sharkImg.dispose();
		layouts.dispose();
	}
}
