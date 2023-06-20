package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gameStates.GameState;

import static utilz.Constants.Directions.*;

import main.GamePanel;

public class KeyBoardInputs implements KeyListener {

	private GamePanel gamePanel;

	public KeyBoardInputs(GamePanel gamePanel) {

		this.gamePanel = gamePanel;

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		switch(GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyPressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyPressed(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().keyPressed(e);
			break;
		default:
			break;
		
		
		
		}		

//		System.out.println(e.getKeyCode());

		
//		case KeyEvent.VK_W:
//
//			gamePanel.getGame().getPlayer().setDirection(UP);
//
//			break;
//
//		case KeyEvent.VK_S:
//			gamePanel.getGame().getPlayer().setDirection(DOWN);
//			break;
//
//		case KeyEvent.VK_A:
//			gamePanel.getGame().getPlayer().setDirection(LEFT);
//			break;
//
//		case KeyEvent.VK_D:
//			gamePanel.getGame().getPlayer().setDirection(RIGHT);
//			break;
		

	}

	@Override
	public void keyReleased(KeyEvent e) {

		switch(GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyReleased(e);
			break;
		default:
			break;
		
		
		
		}
		
	}

}
