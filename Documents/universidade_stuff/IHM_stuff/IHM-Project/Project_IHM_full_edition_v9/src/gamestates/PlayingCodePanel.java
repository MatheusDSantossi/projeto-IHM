package gamestates;

import static main.Game.CODE_PANEL_WIDTH;
import static main.Game.GAME_HEIGHT;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

public class PlayingCodePanel extends State implements Statemethods {
	
	private MenuButton[] buttons = new MenuButton[3];
	private BufferedImage stars_background, backgroundImgPink;
	private int menuX, menuY, menuWidth, menuHeight;
	
	private BufferedImage codeBlock_background;
	
	public PlayingCodePanel(Game game) {
		super(game);
		
//		loadButtons();
		loadBackground();
		
//		backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);		
	}

	private void loadBackground() {

//		codeBlock_background = LoadSave.GetSpriteAtlas(LoadSave.CODE_BLOCK_BACKGROUND);
		stars_background = game.loadSave.GetSpriteAtlas(LoadSave.STARS_BACKGROUND_IMG);
		
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

		g.drawImage(stars_background, 0, 0, Game.CODE_PANEL_WIDTH, Game.GAME_HEIGHT, null);
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
