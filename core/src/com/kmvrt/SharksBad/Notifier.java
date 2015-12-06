package com.kmvrt.SharksBad;

import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.InputProcessor;

public class Notifier extends GestureAdapter {

	private Manager manager;

	public Notifier(Manager manager) {
		
		this.manager = manager;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		
		if(manager.isGameOver()) {
			manager.initNewGame();
		} else if(manager.isOnMainMenu() && manager.isHintClicked()) {
			// close the hint and open the credit
			manager.closeHint();
			manager.clickCredit();
		} else if(manager.isOnMainMenu() && manager.isCreditClicked()) {
			manager.closeCredit();
			
		} else if(manager.isOnMainMenu() && !manager.isHintClicked()) {
			if((x > manager.musicBtn.getX()
				&& x < manager.musicBtn.getX() + manager.musicBtn.getWidth())
				&&
				(y < Painter.CAM_HEIGHT -  manager.musicBtn.getY()
				&& y >  Painter.CAM_HEIGHT - manager.musicBtn.getY() - manager.musicBtn.getHeight())) {
				
				manager.toggleMusic();
				
			} else if((x > manager.hintBtn.getX()
					&& x < manager.hintBtn.getX() + manager.hintBtn.getWidth())
					&&
					(y < Painter.CAM_HEIGHT - manager.hintBtn.getY()
					&& y > Painter.CAM_HEIGHT - manager.hintBtn.getY() - manager.hintBtn.getHeight())) {
				
				manager.clickHint();
			}
			
		} else {
			manager.shoot();
		}
		
		
		return true;
	}
	@Override 
	public boolean fling(float velocityX, float velocityY, int button) {
		
		 if(manager.isOnMainMenu() && manager.isHintClicked()) {
			manager.closeHint();	
			manager.clickCredit();
			return true;
		} else if(manager.isOnMainMenu() && manager.isCreditClicked()) {
			manager.closeCredit();
			return true;
		}
		
//		float dip = Gdx.graphics.getDensity();
		
		if(Math.abs(velocityX) > Math.abs(velocityY)) {
			// move horizontally
			if(velocityX > 0) {
				// move right
				manager.setMovement(Manager.MOVE_RIGHT);

			} else if(velocityX < 0) {
				// move left
				manager.setMovement(Manager.MOVE_LEFT);
			}

		} else if(Math.abs(velocityY) > Math.abs(velocityX)) {
			// move vertically
			if(velocityY < 0) {
				// move up
				manager.setMovement(Manager.MOVE_UP);
				if(manager.isOnMainMenu()) {
					manager.isOnMainMenu(false);
				}
				
			} else if(velocityY > 0) {
				// move down
				manager.setMovement(Manager.MOVE_DOWN);
			}
		}
	
		return true;
	}
	
}