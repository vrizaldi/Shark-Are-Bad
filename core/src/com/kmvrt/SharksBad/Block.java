package com.kmvrt.SharksBad;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Block extends Sprite {

// field -----------------------------------------------------------------
	private byte typeID;


// constructor -----------------------------------------------------------
	public Block(byte typeID, TextureRegion texture, float x, float y) {
		super(texture);
		
		this.typeID = typeID;
		this.setPosition(x, y);
	}


// methods ---------------------------------------------------------------
	public byte getTypeID() {
		
		return typeID;
	} 

}
