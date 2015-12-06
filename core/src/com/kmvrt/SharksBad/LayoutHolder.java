package com.kmvrt.SharksBad; 

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

public class LayoutHolder implements Disposable {

// fields ---------------------------------------------------------------------------------------
	public static final byte BLOCKS_PER_ROW = 25;

	/* 3 types of blocks: 
	 * path -> player could walk in it
	 * wall -> player couln't get pass it
	 * obstacle -> player would die if she touch it
	 */
	public static final byte BLOCK_TYPE_PATH1 = 1;
	public static final byte BLOCK_TYPE_PATH2 = 2;
	public static final byte BLOCK_TYPE_PATH3 = 3;
	private static final byte PATH_TYPE = 3;
	public static final byte BLOCK_TYPE_OBS = 4;
	
	private ArrayList<Layout> layouts;
	private Layout firstLayout;
	// initial width and height of the blocks
//	private short blockHeight;
//	private short blockWidth;

	public TextureAtlas textures;

// constructor -----------------------------------------------------------------------------------
	public LayoutHolder() {
	
		if(Painter.CAM_WIDTH >= 1440) {
			textures = new TextureAtlas("1440/Blocks.pack");
		} else if(Painter.CAM_WIDTH >= 1080) {
			textures = new TextureAtlas("1080/Blocks.pack");
		} else if(Painter.CAM_WIDTH >= 720) {
			textures = new TextureAtlas("720/Blocks.pack");
		} else if(Painter.CAM_WIDTH >= 540) {
			textures = new TextureAtlas("540/Blocks.pack");
		} else if(Painter.CAM_WIDTH >= 480) {
			textures = new TextureAtlas("480/Blocks.pack");
		}
		
		layouts = new ArrayList<Layout>();
		try {
			initLayout();
		} catch(IOException e) {
			System.err.println("Failed to read file");
			Gdx.app.exit();
		}
		if(layouts.size() == 0) {
			System.err.println("Failed to initialise layouts");
			Gdx.app.exit();
		}
//		this.blockHeight = blockHeight;
	//	this.blockWidth = blockWidth;
	}


// methods ----------------------------------------------------------------------------------------
	// initialise the layout held **************************************************************
	private void initLayout() throws IOException { //////////////////////////////////////////////////////////
		// load the layouts from the file: layout.ldr
	
		BufferedReader res = new BufferedReader(new InputStreamReader(
			Gdx.files.internal("layout.ldr").read()));
			// get the file from the assets folder
			/* rules:
		 	 * p -> path (place where the player could move)
		 	 * w -> walls (player can't go through these)
		 	 * o -> obstacle (player die if they hit/go through it) 
		 	 * each line of the layout contains 25 blocks(default) data
		 	 * a layout opened by the word "start" and ended by "end"
		 		 but is deployed from the word "end" to the "start"
			 */
		 
		Layout nLayout = null;
		String tester = null;

		boolean gettingInput = false;
	
		while((tester = res.readLine()) != null) {
			// iterate through every line of the file

			String[] line = tester.trim().split("\t");
			byte[] row = new byte[BLOCKS_PER_ROW]; 
			byte count = 0;
			for(byte x = 0; x < row.length; x++) {
				// reset the array

				row[x] = 0;
			}
			
	/*		if((line.length != 0) 
				|| (line.length != 1)
				|| (line.length != BLOCKS_PER_ROW)) {
				/* if the lines doesn't follow either the rules:
				 * the line is empty
				 * the line only contains one word, which is either "start" or "end"
				 * the line contains the map layout
				 / 

				System.err.println(
					"Wrong input in the file: layout.ldr(case: 1)");	
				Gdx.app.exit();
			} */

			for(String s : line) {
				// iterate through every word of the line

				if(line.length == 0) {
					// if the line is empty
					continue;		// go to the next line

				} else if(gettingInput) {
					if(s.equalsIgnoreCase("p")) {
						byte pathType = (byte)((Math.random() * PATH_TYPE) + 1);
						switch(pathType) {
							case 1:
								row[count] = BLOCK_TYPE_PATH1;
								count++;	
								break;
							case 2:
								row[count] = BLOCK_TYPE_PATH2;
								count++;
								break;
							case 3:
								row[count] = BLOCK_TYPE_PATH3;
								count++;
								break;
						}
						continue;		// go to the next word

					} else if(s.equalsIgnoreCase("h")) {
						row[count] = BLOCK_TYPE_OBS;
						count++;
						continue;		

					} else if(s.equalsIgnoreCase("end") 
						&& (line.length == 1)) {
						// if the line ONLY contains the word "end"
						gettingInput = false;
							// unflag that it's getting the input
						layouts.add(nLayout);
							// save the layout to the layouts collection
						nLayout = null;
							// empty nLayout
					}

				} else if(s.equalsIgnoreCase("start") 
					&& (line.length == 1)
					&& (nLayout == null)) {
					// if the line ONLY contains the word "start" and nLayout is empty
					gettingInput = true;
						// flag that it's getting input
					nLayout = new Layout();
					break;	// go to the next line

				} else {
					System.err.println(
						"Wrong file input in the file: layout.ldr(case: 2)");
					Gdx.app.exit();
				}	// end of if-else if-else statements
			} // for loop (the row/line iteration)'s end

			if((isRowFilled(row))
				&& (nLayout != null)) {
				// if the row is filled completely
				// and nLayout is not empty

				nLayout.addRow(row);
				count = 0;	// reset the count
			}
		}	// for loop (the file iteration)'s end

		// initialise the first layout
		firstLayout = new Layout();
		
		for(byte i = 0; i < 7; i++) {
			byte[] row = new byte[BLOCKS_PER_ROW];
			for(byte n = 0; n < BLOCKS_PER_ROW; n++) {
				if(n == 0) {
					row[n] = BLOCK_TYPE_PATH3;
				} else { 
					row[n] = BLOCK_TYPE_OBS;
				}
			}
			if(isRowFilled(row))
				firstLayout.addRow(row);
		}
	}	// initLayout()'s end

	private boolean isRowFilled(byte[] row) { /////////////////////////////////////////////
		// return wether the row is filled
	
		byte filled = 0;
		byte empty = 0;

		for(byte b : row) {
			
			if(b == 0) {
				empty++;
				
			} else {
				filled++;
			}
		}

		if(filled == BLOCKS_PER_ROW
			// if completely filled
			&& empty == 0) {
			return true;

		} else if(filled > 0
			&& empty > 0) {
			// if the row is only partly filled
			// it means there is a wrong input in the file
			System.err.println(
				"Wrong input in the file: layout.ldr(case: 3)");
			Gdx.app.exit();
		}
		// otherwise 
		return false;
	}	// isRowFilled(byte[])'s end

	
	// method to retrieve any layout held ********************************************************
	public Block[] getRandom(float startX, float startY) { /////////////////////////////////////////////////////
		// get blocks from random layout
	
		short rIndex = (short)((Math.random() * layouts.size()));
			// get a random index number of the layouts

		return getLayout(layouts.get(rIndex), startX, startY);
	}	// getRandom()'s end

	public Block[] getFirst() {
	
		return getLayout(firstLayout, 0, 0);
	}
	
	private Block[] getLayout(Layout layout, float startX, float startY) {

		Block[] nMap = new Block[layout.getSizeRow() * 25];
		short cBlock = 0;	// the last index for nMap
		
		short n = 0;
		for(short i = (short)(layout.getSizeRow() - 1); i >= 0; i--) {
			// loop through every row in the layout
			// start from the last one

			byte[] row = layout.getRow(i);
			float lastX = startX;
			for(byte block : row) {
				// loop through every block in the row
				
					// last block's x coordinate
				switch(block) {
					case BLOCK_TYPE_PATH1:
	//					System.out.println("path readed, x = " + lastX);
						nMap[cBlock] = new Block(BLOCK_TYPE_PATH1, 
							textures.findRegion("blockPath1"), lastX,
							startY + (Navigator.BLOCK_HEIGHT * n));
	//					nMap[cBlock].scale(3);
						break;
						

					case BLOCK_TYPE_PATH2:
	//					System.out.println("path readed, x = " + lastX);
						nMap[cBlock] = new Block(BLOCK_TYPE_PATH2, 
							textures.findRegion("blockPath2"), lastX,
							startY + (Navigator.BLOCK_HEIGHT * n));
	//					nMap[cBlock].scale(3);
						break;
						

					case BLOCK_TYPE_PATH3:
	//					System.out.println("path readed, x = " + lastX);
						nMap[cBlock] = new Block(BLOCK_TYPE_PATH3, 
							textures.findRegion("blockPath3"), lastX,
							startY + (Navigator.BLOCK_HEIGHT * n));
	//					nMap[cBlock].scale(3);
						break;
						

					case BLOCK_TYPE_OBS:
	//					System.out.println("obs readed");
						nMap[cBlock] = new Block(BLOCK_TYPE_OBS,
							textures.findRegion("blockObs"), lastX,
							startY + (Navigator.BLOCK_HEIGHT * n));
	//					nMap[cBlock].scale(3);
						break;

					default:
						System.err.println("Unidentified block type in the Layout Holder");
						Gdx.app.exit();
				}
				lastX += Navigator.BLOCK_WIDTH;
				cBlock++;
			}	// second/internal for loop's end
			n++;
		}	// for loop's end

		return nMap;
	}
	
	@Override
	public void dispose() {
		textures.dispose();
	}
}
