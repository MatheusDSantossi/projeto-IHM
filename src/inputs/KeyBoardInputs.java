package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;

import entities.CodeBlock;
import gameStates.GameState;
import gameStates.Playing;

import static utilz.Constants.Directions.*;

import main.CodePanel;
import main.GamePanel;

public class KeyBoardInputs implements KeyListener {

	private static GamePanel gamePanel;
	private static CodePanel codePanel;
	private Playing playing;

	public KeyBoardInputs(GamePanel gamePanel, CodePanel codePanel) {
//	public KeyBoardInputs(GamePanel gamePanel, CodePanel codePanel, Playing playing) {

		KeyBoardInputs.gamePanel = gamePanel;
		KeyBoardInputs.codePanel = codePanel;

	}
	
//	public KeyBoardInputs(CodePanel codePanel) {
//
//		this.codePanel = codePanel;
//		
//	}

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
			
//			if(e.getComponent().get) {
//				CodeBlock selectedBlock = (CodeBlock) e.getComponent();
			codePanel.keyPressed(e);
//			}
//			codePanel.getGame().getPlaying().keyPressed(e);
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
//			codePanel.keyReleased(e);
			break;
		default:
			break;
		
		
		
		}
		
	}

}
