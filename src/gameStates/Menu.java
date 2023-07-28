package gameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import entities.CodeBlock;
import main.Game;
import ui.MenuButton;
import utilz.HelpMethods;
import utilz.LoadSave;

public class Menu extends State implements StateMethods {

	private MenuButton[] buttons = new MenuButton[3];
	private BufferedImage menuBackgroundImg, backgroundImgPink;
	private int menuX, menuY, menuWidth, menuHeight;

	private int gamePanelX;
	
	private boolean onlyOne = false;

	public Menu(Game game) {
		super(game);

		loadButtons();
		loadBackground();
		
		initCodePanelElements();

//		backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
//		pinkBackground = LoadSave.gifAnimation(LoadSave.MENU_BACKGROUND_IMG);

//		if(game.getGamePanel() != null) {

//		game.getGamePanel().add(pinkBackground);
//		}

//			game.getGamePanel().removeAll();

	}

	private void initCodePanelElements() {
		
		System.out.println("TESTING");
		
	}
	

	public int setBackgroundMiddle(int gamePanelX) {

		return gamePanelX;
		
	}

	private void loadBackground() {

		backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_MENU_PART);
		

		
		menuBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		menuWidth = (int) (menuBackgroundImg.getWidth() * Game.SCALE);
		menuHeight = (int) (menuBackgroundImg.getHeight() * Game.SCALE);
		menuX = (Game.GAME_WIDTH / 3) - menuWidth / 2;
		menuY = (int) (45 * Game.SCALE);

	}

	private void loadButtons() {

		buttons[0] = new MenuButton(Game.GAME_WIDTH / 3, (int) (150 * Game.SCALE), 0, GameState.PLAYING);
		buttons[1] = new MenuButton(Game.GAME_WIDTH / 3, (int) (220 * Game.SCALE), 1, GameState.OPTIONS);
		buttons[2] = new MenuButton(Game.GAME_WIDTH / 3, (int) (290 * Game.SCALE), 2, GameState.QUIT);
	}

	@Override
	public void update() {

		for (MenuButton mb : buttons) {

			mb.update();

		}

	}

	@Override
	public void draw(Graphics g) {

//		g.setColor(Color.black);
//		g.drawString("-------MENU--------", Game.GAME_WIDTH / 2, 200);

		g.setColor(Color.pink);

		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		g.drawImage(backgroundImgPink, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
//		g.drawImage(i, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		g.drawImage(menuBackgroundImg, menuX, menuY, menuWidth, menuHeight, null);

		for (MenuButton mb : buttons) {

			mb.draw(g);

		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

		for (MenuButton mb : buttons) {

			if (isIn(e, mb)) {

				mb.setMousePressed(true);
				break;
			}

			mb.update();

		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {

		for (MenuButton mb : buttons) {

			if (isIn(e, mb)) {

				if (mb.isMousePressed())

					mb.applyGameState();

				if (mb.getState() == GameState.PLAYING) {
					game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
				
//				game.getPlaying().initializeCodePanel(); // Call the method to initialize CodePanel
					game.getPlaying().initCodePanelElements();
				}
				
				break;
			}

		}

		resetButtons();

	}

	@Override
	public void mouseMoved(MouseEvent e) {

		for (MenuButton mb : buttons) {

			mb.setMouseOver(false);

		}

		for (MenuButton mb : buttons) {

			if (isIn(e, mb)) {

				mb.setMouseOver(true);
				break;
			}

		}

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {

			game.getGamePanel().removeAll();
			GameState.state = GameState.PLAYING;
			
			
			game.getPlaying().initCodePanelElements();
			
//			if(GameState.state == GameState.PLAYING) {
//			game.getPlaying().initializeCodePanel(); // Call the method to initialize CodePanel
//			}


		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	private void resetButtons() {

		for (MenuButton mb : buttons) {

			mb.resetBools();

		}

	}


}
