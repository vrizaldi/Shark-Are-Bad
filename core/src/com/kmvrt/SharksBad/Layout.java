package com.kmvrt.SharksBad;

import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;

public class Layout {

// field ------------------------------------------------------------------
//	private static final byte INITIAL_SIZE = 10;
	
	private ArrayList<byte[]> blocks;
//	private short lastIndex;
		// last index for blocks


// constructor ------------------------------------------------------------
	public Layout() {
	
		blocks = new ArrayList<byte[]>();

	}
// methods ----------------------------------------------------------------
	public byte[] getRow(short index) {
		// return the row specified
	
		return blocks.get(index); 
	}	// getRow(short)'s end

	public int getSizeRow() {
		// return the size of the layout (int rows)
		
		return blocks.size();
	}

	public void addRow(byte[] row) {
		// add the specified row to the collection
	
		if(row.length != LayoutHolder.BLOCKS_PER_ROW) {
			System.err.println("Invalid input in the method: addRow(byte[])");
			Gdx.app.exit();
		}
		
		blocks.add(row);
	
	}
}
