package gameStates;

import static main.Game.GAME_HEIGHT; 
import static main.Game.GAME_WIDTH;
import static main.Game.CODE_PANEL_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

public class MenuCodePanel extends State implements StateMethods {
	
	private MenuButton[] buttons = new MenuButton[3];
	private BufferedImage backgroundMenuImg, backgroundImgPink;
	private int menuX, menuY, menuWidth, menuHeight;
	
	private BufferedImage codeBlock_background;
	
	public MenuCodePanel(Game game) {
		super(game);
		
//		loadButtons();
		loadBackground();
		
//		backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);		
	}

	private void loadBackground() {

//		codeBlock_background = LoadSave.GetSpriteAtlas(LoadSave.CODE_BLOCK_BACKGROUND);
		codeBlock_background = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_CODE_PART);
//		backgroundMenuImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		
		System.out.println("size code: " + CODE_PANEL_WIDTH + " | " + GAME_HEIGHT);
		
		
		System.out.println(codeBlock_background.getWidth());
		System.out.println(codeBlock_background.getHeight());
		
//		menuWidth = (int) (codeBlock_background.getWidth() * Game.SCALE);
//		menuHeight = (int) (codeBlock_background.getHeight() * Game.SCALE);
//		menuX = Game.CODE_PANEL_WIDTH / 2 - menuWidth / 2;
//		menuY = (int) (45 * Game.SCALE);

		
	}

	private void loadButtons() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics g) {

//		g.setColor(Color.pink);
//
//		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		g.drawImage(codeBlock_background, 0, 0, Game.CODE_PANEL_WIDTH, Game.GAME_HEIGHT, null);
//		g.drawImage(backgroundMenuImg, menuX, menuY, menuWidth, menuHeight, null);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}	

}
