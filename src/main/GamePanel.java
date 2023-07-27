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

		setBorder(null);
		
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

	private void setPanelSize() {

		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);

//		setMinimumSize(size);
		setPreferredSize(size);
//		setMaximumSize(size);

		System.out.println("size: " + GAME_WIDTH + " | " + GAME_HEIGHT);
		
	}

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


	}

	public Game getGame() {

		return game;

	}

	public void setPlaying(Playing playing) {
		this.playing = playing;
	}

}
