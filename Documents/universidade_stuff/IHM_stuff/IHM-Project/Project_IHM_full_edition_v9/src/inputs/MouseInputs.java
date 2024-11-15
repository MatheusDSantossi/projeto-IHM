package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.Gamestate;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener {

	private GamePanel gamePanel;

	public MouseInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void mouseDragged(MouseEvent e) {
		switch (Gamestate.state) {
		case PLAYING -> gamePanel.getGame().getPlaying().mouseDragged(e);
		case OPTIONS -> gamePanel.getGame().getGameOptions().mouseDragged(e);
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void mouseMoved(MouseEvent e) {
		switch (Gamestate.state) {
		case MENU -> gamePanel.getGame().getMenu().mouseMoved(e);
		case PLAYING -> gamePanel.getGame().getPlaying().mouseMoved(e);
		case OPTIONS -> gamePanel.getGame().getGameOptions().mouseMoved(e);
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void mouseClicked(MouseEvent e) {
		switch (Gamestate.state) {
		case PLAYING:
			if (gamePanel.getGame().getPlaying().isLvlCompleted() || gamePanel.getGame().getPlaying().isGameOver()
					|| gamePanel.getGame().getPlaying().isPaused())
				gamePanel.getGame().getPlaying().mouseClicked(e);

			break;
		case TUTORIAL:
			gamePanel.getGame().getTutorial().mouseClicked(e);
			break;
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void mousePressed(MouseEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().mousePressed(e);
			break;
		case PLAYING:
			if (gamePanel.getGame().getPlaying().isLvlCompleted() || gamePanel.getGame().getPlaying().isGameOver()
					|| gamePanel.getGame().getPlaying().isPaused()
					|| gamePanel.getGame().getPlaying().isGameCompleted())
				gamePanel.getGame().getPlaying().mousePressed(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().mousePressed(e);
			break;
		case TUTORIAL:
			gamePanel.getGame().getTutorial().mousePressed(e);
			break;
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void mouseReleased(MouseEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseReleased(e);
			break;
//		case PLAYING -> gamePanel.getGame().getPlaying().mouseReleased(e);
		case PLAYING:
			if (gamePanel.getGame().getPlaying().isLvlCompleted() || gamePanel.getGame().getPlaying().isGameOver()
					|| gamePanel.getGame().getPlaying().isPaused()
					|| gamePanel.getGame().getPlaying().isGameCompleted())
				gamePanel.getGame().getPlaying().mouseReleased(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().mouseReleased(e);
			break;
		case TUTORIAL:
			gamePanel.getGame().getTutorial().mouseReleased(e);
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Not In use
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Not In use
	}

}