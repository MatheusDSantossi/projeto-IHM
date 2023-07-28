package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import gameStates.GameOptions;
import gameStates.GameState;
import gameStates.Menu;
import gameStates.MenuCodePanel;
import gameStates.Playing;
import ui.AudioOptions;
import utilz.LoadSave;

public class Game implements Runnable {

	private boolean codePanelInitialized = false; // Add a flag to track if CodePanel is initialized

	private GameWindow gameWindow;
	private GamePanel gamePanel;

	// adding to test
	private CodePanel codePanel;

	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;

	private Playing playing;
	private Menu menu;
//	private MenuCodePanel menuCodePanel;
	private GameOptions gameOptions;
	private AudioOptions audioOptions;
	private AudioPlayer audioPlayer;

	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1.3f; // 32 * 1.5 = 48??;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

	public final static int CODE_PANEL_WIDTH = (int) (300 * SCALE);
//	public final static int CODE_PANEL_HEIGHT = (int) (80 * SCALE);
	
	
	 private float interpolation;
	
	
	public Game() {

//		LoadSave.GetAllLevels();

//		initClasses();

		gamePanel = new GamePanel(this);

		// adding to test
		codePanel = new CodePanel(this);

		initClasses();

		gameWindow = new GameWindow(gamePanel, codePanel);
//		gameWindow = new GameWindow(gamePanel);

//		initClasses();

		gamePanel.setFocusable(true);

		gamePanel.requestFocus();

		startGameLoop();

	}

	private void initClasses() {

		audioOptions = new AudioOptions(this);
		audioPlayer = new AudioPlayer();
		menu = new Menu(this);

//		menuCodePanel = new MenuCodePanel(this);

		playing = new Playing(this);
		gameOptions = new GameOptions(this);

	}

	private void startGameLoop() {

		gameThread = new Thread(this);
		gameThread.start();

	}

	public void update() {

//		gamePanel.updateGame();

//		levelManager.update();
//		player.update();

		switch (GameState.state) {
		case MENU:
			menu.update();
			break;

		case PLAYING:
			playing.update();
//			if (!codePanelInitialized) {
//				playing.initializeCodePanel();
//				codePanelInitialized = true;
//				
//			}
			break;
		case OPTIONS:
			gameOptions.update();
			break;

		case QUIT:
			System.exit(0);

		default:
			break;

		}

	}

	public void render(Graphics g) {

//		levelManager.draw(g);
//		player.render(g);

		switch (GameState.state) {
		case MENU:
			menu.draw(g);
			break;

		case PLAYING:
			playing.draw(g);
			break;

		case OPTIONS:
			gameOptions.draw(g);
			break;
		case QUIT:
			System.exit(0);
			break;
		default:
			break;

		}
	}

	@Override
	public void run() {

		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;
//		long lastFrame = System.nanoTime();		
//		long now = System.nanoTime();

		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;

		while (true) {

//			now = System.nanoTime();

			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {

				update();
				updates++;
				deltaU--;

			}

			if (deltaF >= 1) {

				gamePanel.repaint();
//				lastFrame = now;
				frames++;
				deltaF--;

			}

//			if(now - lastFrame >= timePerFrame) {
//				
//				gamePanel.repaint();
//	
//				lastFrame = now;
//				frames++;
//			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {

				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + "| UPS: " + updates);
				frames = 0;
				updates = 0;
			}

		}

	}

	public void windowFocusLost() {

		if (GameState.state == GameState.PLAYING) {

			playing.getPlayer().resetDirBooleans();

		}

	}

	public Menu getMenu() {

		return menu;

	}

	public Playing getPlaying() {

		return playing;

	}

	public GameOptions getGameOptions() {

		return gameOptions;

	}

	public AudioOptions getAudioOptions() {

		return audioOptions;

	}

	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public GameWindow getGameWindow() {
		return gameWindow;
	}

	public CodePanel getCodePanel() {
		return codePanel;
	}

}
