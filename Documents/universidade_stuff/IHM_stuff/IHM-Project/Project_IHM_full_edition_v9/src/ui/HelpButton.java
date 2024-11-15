package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.UI.Buttons.*;

public class HelpButton {
	private int xPos, yPos, rowIndex, index;
	private int xOffsetCenter = B_WIDTH / 2;
//	private int xOffsetCenter = B_WIDTH / 3;
	private Gamestate state;
	private BufferedImage[] imgs;
	private boolean mouseOver, mousePressed;
	private Rectangle bounds;

	private int helpWidth, helpHeight;
	
	private Game game;
	
	public HelpButton(int xPos, int yPos, int rowIndex, Gamestate state, Game game) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.state = state;
		
		this.game = game;
			
				
		loadImgs();
		initBounds();
	}

	private void initBounds() {
		bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
	}

	private void loadImgs() {
		
		helpWidth = (int) (42 * Game.SCALE);
		helpHeight = (int) (42 * Game.SCALE);

		imgs = new BufferedImage[3];
		BufferedImage temp = game.loadSave.GetSpriteAtlas(LoadSave.HELP_ICON);
		for (int i = 0; i < imgs.length; i++) {
//			imgs[i] = temp.getSubimage(i * 40, rowIndex * 40, 40, 40);
			imgs[i] = temp.getSubimage(rowIndex * 42, i * 42, 42, 42);
//			imgs[i] = temp.getSubimage(rowIndex * 42, i * 42, 42, 42);
		}
	}

	public void draw(Graphics g) {
		g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, helpWidth, helpHeight, null);
	}

	public void update() {
		index = 0;
		if (mouseOver)
			index = 0;
		if (mousePressed)
			index = 0;
		
		// this is not working only the 0 is getting the image
	}
	
	public boolean isInhelpButton(MouseEvent e, HelpButton helpB) {
		return helpB.getBounds().contains(e.getX(), e.getY());
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public Rectangle getBounds() {
		return bounds;
	}
 
	public void applyGamestate() {
		Gamestate.state = state;
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}
	public Gamestate getState() {
		return state;
	}

}
