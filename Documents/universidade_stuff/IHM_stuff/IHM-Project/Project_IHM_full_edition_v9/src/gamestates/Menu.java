package gamestates;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.Game;
import main.GamePanel;
import main.GameWindow;
import ui.HelpButton;
import ui.MenuButton;
import utilz.LoadSave;

public class Menu extends State implements Statemethods {

	private MenuButton[] buttons = new MenuButton[4];
	private HelpButton helpButton;
//	private MenuButton[] buttons = new MenuButton[5];
	private BufferedImage backgroundImg, backgroundImgPink, helpIcon;
	private int menuX, menuY, menuWidth, menuHeight;
	private int helpX, helpY, helpWidth, helpHeight;

	public Menu(Game game) {
		
		super(game);
		
		
		
		loadButtons();
		loadBackground();
//		loadHelpIcon();
//		backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
		backgroundImgPink = game.loadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_MENU_PART);

	}


	private void loadBackground() {
		backgroundImg = game.loadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
		menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
//		menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
		menuX = (Game.GAME_WIDTH / 3) - menuWidth / 2;
		menuY = (int) (25 * Game.SCALE);
	}

	private void loadButtons() {
		
		helpX = Game.GAME_WIDTH - 200;
		helpY = 20;

		
//		buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (130 * Game.SCALE), 0, Gamestate.PLAYING);
//		buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (200 * Game.SCALE), 1, Gamestate.OPTIONS);
//		buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (270 * Game.SCALE), 3, Gamestate.CREDITS);
//		buttons[3] = new MenuButton(Game.GAME_WIDTH / 2, (int) (340 * Game.SCALE), 2, Gamestate.QUIT);

		buttons[0] = new MenuButton(Game.GAME_WIDTH / 3, (int) (130 * Game.SCALE), 0, Gamestate.PLAYING, game);
		buttons[1] = new MenuButton(Game.GAME_WIDTH / 3, (int) (200 * Game.SCALE), 1, Gamestate.OPTIONS, game);
		buttons[2] = new MenuButton(Game.GAME_WIDTH / 3, (int) (270 * Game.SCALE), 3, Gamestate.CREDITS, game);
		buttons[3] = new MenuButton(Game.GAME_WIDTH / 3, (int) (340 * Game.SCALE), 2, Gamestate.QUIT, game);
		
		helpButton = new HelpButton(helpX, helpY, 0, Gamestate.TUTORIAL, game);

//		buttons[4] = new MenuButton(Game.GAME_WIDTH / 3, (int) (340 * Game.SCALE), 2, Gamestate.QUIT);

	}

	@Override
	public void update() {
		for (MenuButton mb : buttons)
			mb.update();
		
		helpButton.update();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImgPink, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

//		g.drawImage(helpIcon, helpX, helpY, helpWidth, helpHeight, null);

		for (MenuButton mb : buttons)
			mb.draw(g);
		
		helpButton.draw(g);
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				mb.setMousePressed(true);
			}
		}

		if (helpButton.isInhelpButton(e, helpButton)) 
		
			helpButton.setMousePressed(true);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				if (mb.isMousePressed())
					mb.applyGamestate();
				if (mb.getState() == Gamestate.PLAYING) {
					
					game.getPlaying().resetAll();
					
					game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());

					game.getPlaying().initCodePanelElements();
				}
				break;
			}
		}
		
		if (helpButton.isInhelpButton(e, helpButton)) {
			
			if (helpButton.isMousePressed())
				helpButton.applyGamestate();
			if (helpButton.getState() == Gamestate.TUTORIAL) { // create GameState tutorial
				game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());

				game.getPlaying().initCodePanelElements();
			}
			
		}
		
		resetButtons();
	}

	private void resetButtons() {
		for (MenuButton mb : buttons)
			mb.resetBools();
		
		helpButton.resetBools();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (MenuButton mb : buttons)
			mb.setMouseOver(false);

		for (MenuButton mb : buttons)
			if (isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
		
		helpButton.setMouseOver(false);
		
		if (helpButton.isInhelpButton(e, helpButton)) 
			helpButton.setMouseOver(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}