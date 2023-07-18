package main;

import java.awt.Color;    
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import gameStates.Playing;
import inputs.KeyBoardInputs;
import inputs.MouseInputs;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Directions.*;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {

	public Game game;
	private Playing playing;
	
	private MouseInputs mouseInputs;
//	private float xDelta = 100, yDelta = 100;
//	private float xDir = 0.003f, yDir = 0.003f;
//	private int frames = 0;
//	private long lastCheck = 0;
//	private Color color = new Color(150, 20, 90);
//	
//	private Random random;

//	private BufferedImage img;
//	private BufferedImage[][] animations;
//	private int aniTick, aniIndex, aniSpeed = 15;
//
//	private int playerAction = IDLE;
//	private int playerDir = -1;
//	private boolean moving = false;

	// Temporary, jsut for effect

//	private ArrayList<MyRect> rects = new ArrayList<>();

	public GamePanel(Game game) {

//		random = new Random();

		mouseInputs = new MouseInputs(this);

		this.game = game;

//		importImg();
//
//		loadAnimations();

		setPanelSize();

		CodePanel codePanel = new CodePanel(game);
		
		// Add the KeyListener to the CodePanel
        codePanel.addKeyListener(codePanel.getKeyboardInputs());

        // Make the CodePanel focusable and request focus
        codePanel.setFocusable(true);
        codePanel.requestFocus();
		
		addKeyListener(new KeyBoardInputs(this, codePanel));
//		addKeyListener(new KeyBoardInputs(this, game.getCodePanel(), game.getPlaying()));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);

	}

//	private void loadAnimations() {
//
//		animations = new BufferedImage[9][6];
//
//		for (int j = 0; j < animations.length; j++) {
//
//			for (int i = 0; i < animations[j].length; i++) {
//
//				animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
//
//			}
//		}
//	}
//
//	private void importImg() {
//
//		InputStream is = getClass().getResourceAsStream("/player_sprites.png");
//
//		try {
//
//			img = ImageIO.read(is);
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//
//		} finally {
//
//			try {
//				is.close();
//			} catch (IOException e) {
//
//				e.printStackTrace();
//
//			}
//
//		}
//
//	}

	private void setPanelSize() {

		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);

//		setMinimumSize(size);
		setPreferredSize(size);
//		setMaximumSize(size);

		System.out.println("size: " + GAME_WIDTH + " | " + GAME_HEIGHT);
		
	}

//	public void setDirection(int direction) {
//
//		this.playerDir = direction;
//
//		moving = true;
//
//	}

//	public void setMoving(boolean moving) {
//
//		this.moving = moving;
//
//	}
//
////	public void spawnRect(int x, int y) {
////		
////		rects.add(new MyRect(x, y));
////		
////	}
//
//	private void updateAnimationTick() {
//
//		aniTick++;
//
//		if (aniTick >= aniSpeed) {
//
//			aniTick = 0;
//			aniIndex++;
//
//			if (aniIndex >= GetSpriteAmmount(playerAction)) {
//
//				aniIndex = 0;
//
//			}
//
//		}
//
//	}
//
//	private void setAnimation() {
//
//		if (moving) {
//
//			playerAction = RUNNING;
//
//		} else {
//
//			playerAction = IDLE;
//
//		}
//
//	}
//	
//	private void updatePos() {
//	
//		if(moving) {
//			
//			switch(playerDir) {
//			
//			case LEFT:
//				xDelta -= 5;
//				break;
//				
//			case UP:
//				yDelta -= 5;
//				break;
//				
//			case RIGHT:
//				xDelta += 5;
//				break;
//				
//			case DOWN:
//				yDelta += 5;
//				break;
//			}
//			
//		}
//		
//	}

	public void updateGame() {

//		updateAnimationTick();
//
//		setAnimation();
//		updatePos();

	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		g.setColor(Color.white);

		for (int i = 0; i < 64; i++) {

			for (int j = 0; j < 40; j++) {

				g.fillRect(i * 20, j * 20, 20, 20);

			}

		}

		game.render(g);

//		g.drawImage(animations[playerAction][aniIndex], (int) xDelta, (int) yDelta, 256, 160, null);

//		subImg = img.getSubimage(1*64, 8*40, 64, 40);

//		g.drawImage(subImg, (int)xDelta, (int)yDelta, 128, 80, null);		

		// Temp rects

//		for(MyRect rect: rects) {
//			
//			rect.updateRect();
//			rect.draw(g);
//			
//		}

//		updateRectangle();
//		
//		g.setColor(color);
//		
//		g.fillRect((int)xDelta, (int)yDelta, 200, 50);

//		g.setColor();

//		repaint();

//	private void updateRectangle() {
//		
//		xDelta += xDir;
//		
//		if(xDelta > 400 || xDelta < 0) {
//			
//			xDir *= -1;
//			
//			color = newColor();
//			
//		}
//			
//		yDelta += yDir;
//		
//		if(yDelta > 400 || yDelta < 0) {
//			
//			yDir *= -1;
//			
//			color = newColor();
//			
//		}
//		
//	}
//	
//	private Color newColor() {
//		
//		int r = random.nextInt(255);
//		int g = random.nextInt(255);
//		int b = random.nextInt(255);
//	
//		
//		return new Color(r, g, b);
//		
//	}

//	public class MyRect {
//		
//		int x, y, w, h;
//		int xDir = 1, yDir = 1;
//		Color color;
//		
//		public MyRect(int x, int y) {
//			
//			this.x = x;
//			this.y = y;
//			w = random.nextInt(50);
//			h = w;
//			color = newColor();
//		}
//	
//		public void updateRect() {
//			
//			this.x += xDir;
//			this.y += yDir;
//
//			if((x + w) > 400 || x < 0) {
//				
//				xDir *= -1;
//				color = newColor();
//				
//			}
//
//			if((y + h) > 400 || y < 0) {
//				
//				yDir *= -1;
//				color = newColor();
//				
//			}
//			
//			
//		}
//		
//		private void draw(Graphics g) {
//			
//			g.setColor(color);
//			g.fillRect(x, y, w, h);
//			
//		}
//	
//	
//		
//	}
//	
	}

	public Game getGame() {

		return game;

	}

	public void setPlaying(Playing playing) {
		this.playing = playing;
	}

}
