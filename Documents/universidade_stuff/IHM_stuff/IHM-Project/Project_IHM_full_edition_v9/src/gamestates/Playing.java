package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BorderFactory;
import javax.swing.Timer;

import java.util.ArrayList;

import entities.CodeBlock;
import entities.EnemyManager;
import entities.Player;

import levels.LevelManager;
import main.CodePanel;
import main.Game;
import objects.ObjectManager;
import ui.GameCompletedOverlay;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import effects.DialogueEffect;
import effects.Rain;

import static utilz.Constants.Environment.*;
import static utilz.Constants.Dialogue.*;
import static utilz.Constants.BlockyGame.*;

public class Playing extends State implements Statemethods {

	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private GameCompletedOverlay gameCompletedOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private Rain rain;

	private boolean paused = false;

	private int xLvlOffset;
	private int leftBorder = (int) (0.25 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.75 * Game.GAME_WIDTH);
	private int maxLvlOffsetX;

	private BufferedImage backgroundImg, bigCloud, smallCloud, shipImgs[];
	private BufferedImage[] questionImgs, exclamationImgs;
	private ArrayList<DialogueEffect> dialogEffects = new ArrayList<>();

	private int[] smallCloudsPos;
	private Random rnd = new Random();

	private boolean gameOver;
	private boolean lvlCompleted;
	private boolean gameCompleted;
	private boolean playerDying;
	private boolean drawRain;

	// Ship will be decided to drawn here. It's just a cool addition to the game
	// for the first level. Hinting on that the player arrived with the boat.

	// If you would like to have it on more levels, add a value for objects when
	// creating the level from lvlImgs. Just like any other object.

	// Then play around with position values so it looks correct depending on where
	// you want
	// it.

	private boolean drawShip = true;
	private int shipAni, shipTick, shipDir = 1;
	private float shipHeightDelta, shipHeightChange = 0.05f * Game.SCALE;

	private int currentIndex = 0;

	private ScheduledExecutorService executorService;

	public int delay;

	private int timesLefting = -1;
	private int timesPlayerPlayed = 0;

	public String textCodeBlock;

	private boolean moveIsSelected = false;

//	private double relatedPositionX;
//	private double relatedPositionY;
//
//	private int futurePositionX;
//	private int futurePositionY;

	public Playing(Game game) {
		super(game);
		initClasses();

		backgroundImg = game.loadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		bigCloud = game.loadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
		smallCloud = game.loadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
		smallCloudsPos = new int[8];
		for (int i = 0; i < smallCloudsPos.length; i++)
			smallCloudsPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));

		shipImgs = new BufferedImage[4];
		BufferedImage temp = game.loadSave.GetSpriteAtlas(LoadSave.SHIP);
		for (int i = 0; i < shipImgs.length; i++)
			shipImgs[i] = temp.getSubimage(i * 78, 0, 78, 72);

		loadDialogue();
		calcLvlOffset();
		loadStartLevel();
//		setDrawRainBoolean();

		executorService = Executors.newSingleThreadScheduledExecutor();
	}

	public void initCodePanelElements() {

		game.getCodePanel().setBorder(BorderFactory.createLineBorder(Color.black));

		for (CodeBlock codeBlock : game.getCodePanel().getCodeBlocks()) {

			game.getCodePanel().add(codeBlock);

		}

		game.getCodePanel().revalidate();
		game.getCodePanel().repaint();

	}

	private void loadDialogue() {
		loadDialogueImgs();

		// Load dialogue array with premade objects, that gets activated when needed.
		// This is a simple
		// way of avoiding ConcurrentModificationException error. (Adding to a list that
		// is being looped through.

		for (int i = 0; i < 10; i++)
			dialogEffects.add(new DialogueEffect(0, 0, EXCLAMATION));
		for (int i = 0; i < 10; i++)
			dialogEffects.add(new DialogueEffect(0, 0, QUESTION));

//		for (DialogueEffect de : dialogEffects)
//			de.desactive();
	}

	private void loadDialogueImgs() {
		BufferedImage qtemp = game.loadSave.GetSpriteAtlas(LoadSave.QUESTION_ATLAS);
		questionImgs = new BufferedImage[5];
		for (int i = 0; i < questionImgs.length; i++)
			questionImgs[i] = qtemp.getSubimage(i * 14, 0, 14, 12);

		BufferedImage etemp = game.loadSave.GetSpriteAtlas(LoadSave.EXCLAMATION_ATLAS);
		exclamationImgs = new BufferedImage[5];
		for (int i = 0; i < exclamationImgs.length; i++)
			exclamationImgs[i] = etemp.getSubimage(i * 14, 0, 14, 12);
	}

	public void loadNextLevel() {
		levelManager.setLevelIndex(levelManager.getLevelIndex() + 1);
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		resetAll();
		drawShip = false;
	}

	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
	}

	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);

		player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

		// change the player DIRECTION

		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
		gameCompletedOverlay = new GameCompletedOverlay(this);

//		rain = new Rain();
	}

	@Override
	public void update() {

//		if (timesLefting == 0)
//			gameOver = true;

		if (paused)
			pauseOverlay.update();
		else if (lvlCompleted) {
			levelCompletedOverlay.update();
//			stopCodeExecutor();
//			resetAll();
		} else if (gameCompleted)
			gameCompletedOverlay.update();
		else if (gameOver)
			gameOverOverlay.update();
		else if (playerDying)
			player.update();
		else {
			updateDialogue();
//			if (drawRain)
//				rain.update(xLvlOffset);
			levelManager.update();
			objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData());
			checkCloseToBorder();
			if (drawShip)
				updateShipAni();
		}
	}

	private void updateShipAni() {
		shipTick++;
		if (shipTick >= 35) {
			shipTick = 0;
			shipAni++;
			if (shipAni >= 4)
				shipAni = 0;
		}

		shipHeightDelta += shipHeightChange * shipDir;
		shipHeightDelta = Math.max(Math.min(10 * Game.SCALE, shipHeightDelta), 0);

		if (shipHeightDelta == 0)
			shipDir = 1;
		else if (shipHeightDelta == 10 * Game.SCALE)
			shipDir = -1;

	}

	private void updateDialogue() {
		for (DialogueEffect de : dialogEffects)
			if (de.isActive())
				de.update();
	}

	private void drawDialogue(Graphics g, int xLvlOffset) {
		for (DialogueEffect de : dialogEffects)
			if (de.isActive()) {
				if (de.getType() == QUESTION)
					g.drawImage(questionImgs[de.getAniIndex()], de.getX() - xLvlOffset, de.getY(), DIALOGUE_WIDTH,
							DIALOGUE_HEIGHT, null);
				else
					g.drawImage(exclamationImgs[de.getAniIndex()], de.getX() - xLvlOffset, de.getY(), DIALOGUE_WIDTH,
							DIALOGUE_HEIGHT, null);
			}
	}

	public void addDialogue(int x, int y, int type) {
		// Not adding a new one, we are recycling. #ThinkGreen lol
		dialogEffects.add(new DialogueEffect(x, y - (int) (Game.SCALE * 15), type));
		for (DialogueEffect de : dialogEffects)
			if (!de.isActive())
				if (de.getType() == type) {
					de.reset(x, -(int) (Game.SCALE * 15));
					return;
				}
	}

	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;

		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < leftBorder)
			xLvlOffset += diff - leftBorder;

		xLvlOffset = Math.max(Math.min(xLvlOffset, maxLvlOffsetX), 0);
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

//		g.setFont(new Font("Arial", Font.BOLD, 30));
//		
//		g.drawString("10x", Game.GAME_WIDTH - 50, 10);

		drawClouds(g);
//		if (drawRain)
//			rain.draw(g, xLvlOffset);

		if (drawShip)

			g.drawImage(shipImgs[shipAni], (int) (100 * Game.SCALE) - xLvlOffset,
					(int) ((288 * Game.SCALE) + shipHeightDelta), (int) (78 * Game.SCALE), (int) (72 * Game.SCALE),
					null);

		levelManager.draw(g, xLvlOffset);
		objectManager.draw(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		objectManager.drawBackgroundTrees(g, xLvlOffset);
		drawDialogue(g, xLvlOffset);

		if (levelManager.getLevelIndex() == 0 && timesLefting == -1)
			timesLefting = LVL1_MOVEMENTS;

		if (levelManager.getLevelIndex() == 1 && timesLefting == -1)
			timesLefting = LVL2_MOVEMENTS;

		if (levelManager.getLevelIndex() == 2 && timesLefting == -1)
			timesLefting = LVL3_MOVEMENTS;

		g.setFont(new Font("Arial", Font.BOLD, 25));
		g.setColor(Color.WHITE);

		if (game.getCodePanel().getSelectedBlock() != null && game.getCodePanel().getSelectedBlock().getText().contains("move")) {
		    g.setColor(Color.RED);

		    double relatedPositionY = player.getHitbox().getY();

		    double futurePositionX;
		    if (player.isGoingRight()) {
		        futurePositionX = player.getHitbox().getX() + 120;
		    } else {
		        futurePositionX = player.getHitbox().getX() - 120;
		    }

		    // Draw the small rectangle at the future position
		    g.drawRect((int) futurePositionX - xLvlOffset, (int) relatedPositionY, 20, 20);

		    System.out.println("MOVE POSITION: " + futurePositionX);
		    System.out.println("PLAYER X POSITION: " + player.getX());
		}


		String text = "Movements left " + timesLefting + "x";

		game.getTutorial().drawText(g, text, Game.GAME_WIDTH - 300, 100, 2, 5);

		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		} else if (gameOver)
			gameOverOverlay.draw(g);
		else if (lvlCompleted)
			levelCompletedOverlay.draw(g);
		else if (gameCompleted)
			gameCompletedOverlay.draw(g);

	}

	private void drawClouds(Graphics g) {
		for (int i = 0; i < 4; i++)
			g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int) (204 * Game.SCALE),
					BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);

		for (int i = 0; i < smallCloudsPos.length; i++)
			g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i],
					SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
	}

	public void setGameCompleted() {
		gameCompleted = true;
	}

	public void resetGameCompleted() {
		gameCompleted = false;
	}

	public void resetAll() {
		gameOver = false;
		paused = false;
		lvlCompleted = false;
		playerDying = false;
		drawRain = false;

//		setDrawRainBoolean();

		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
		dialogEffects.clear();

		timesLefting = -1;

		game.getCodePanel().resetCodeBlocks();
		currentIndex = 0;

		restartCodeExecutor();

		game.getCodePanel().codeFinished = false;

		if (Gamestate.state == Gamestate.MENU || Gamestate.state == Gamestate.TUTORIAL || Gamestate.state == Gamestate.CREDITS) {

			game.getCodePanel().resetDraw();

//			game.getCodePanel().setBorder(null);
//			game.getCodePanel().removeAll();
//			game.getCodePanel().revalidate();
//			game.getCodePanel().repaint();
		}

	}

//	private void setDrawRainBoolean() {
//		// This method makes it rain 20% of the time you load a level.
//		if (rnd.nextFloat() >= 0.8f)
//			drawRain = true;
//	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void checkObjectHit(Rectangle2D.Float attackBox) {
		objectManager.checkObjectHit(attackBox);
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}

	public void checkPotionTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}

	public void checkSpikesTouched(Player p) {
		objectManager.checkSpikesTouched(p);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameOver) {
			if (e.getButton() == MouseEvent.BUTTON1)
				player.setAttacking(true);
			else if (e.getButton() == MouseEvent.BUTTON3)
				player.powerAttack();

			Timer releaseTimer = new Timer(1500, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					if (timesLefting == 0 && !lvlCompleted)
						gameOver = true;

				}
			});
			// releaseTimer.setRepeats(false);
			releaseTimer.setRepeats(false);
			releaseTimer.start();

			if (timesLefting != 0 && !lvlCompleted)
				releaseTimer.stop();

//			if (timesLefting == 0 && !lvlCompleted)
//				gameOver = true;

		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!gameOver && !gameCompleted && !lvlCompleted)
 			if (game.getCodePanel().codeFinished || e.getKeyCode() == KeyEvent.VK_ESCAPE)
//				if (game.getCodePanel().getCodeBlocks().get(0).isEnterKeyPressed() || e.getKeyCode() == KeyEvent.VK_ESCAPE)
				switch (e.getKeyCode()) {
				case KeyEvent.VK_A:

					player.setLeft(true);
					break;
				case KeyEvent.VK_D:

					player.setRight(true);

					break;
				case KeyEvent.VK_SPACE:
					if (player.isGoingRight())
						player.setRight(true);
					else
						player.setLeft(true);

					player.setJump(true);
					break;
				case KeyEvent.VK_ESCAPE:
					paused = !paused;
				}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver && !gameCompleted && !lvlCompleted)
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				if (player.isGoingRight())
					player.setRight(false);
				else
					player.setLeft(false);
				player.setJump(false);
				break;
			}
	}

	public void mouseDragged(MouseEvent e) {
		if (!gameOver && !gameCompleted && !lvlCompleted)
			if (paused)
				pauseOverlay.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mousePressed(e);
		else if (paused)
			pauseOverlay.mousePressed(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mousePressed(e);
		else if (gameCompleted)
			gameCompletedOverlay.mousePressed(e);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mouseReleased(e);
		else if (paused)
			pauseOverlay.mouseReleased(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mouseReleased(e);
		else if (gameCompleted)
			gameCompletedOverlay.mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mouseMoved(e);
		else if (paused)
			pauseOverlay.mouseMoved(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mouseMoved(e);
		else if (gameCompleted)
			gameCompletedOverlay.mouseMoved(e);
	}

	public void setLevelCompleted(boolean levelCompleted) {
		game.getAudioPlayer().lvlCompleted();
		if (levelManager.getLevelIndex() + 1 >= levelManager.getAmountOfLevels()) {
			// No more levels
			gameCompleted = true;
			levelManager.setLevelIndex(0);
			levelManager.loadNextLevel();
			resetAll();
			return;
		}
		this.lvlCompleted = levelCompleted;
	}

	public String moveMethod_substring(String codeText, boolean mainMoveMethod) {

		String text = codeText;
		String number = "1"; // Default to 1 if no number is specified

		// Define the regular expression pattern to capture the number and the text part
		int xIndex = codeText.indexOf('x');

		// If 'x' is not found or the index is at the start or end, it's an invalid
		// format
		if (xIndex < 1 || xIndex >= codeText.length() - 1) {
			System.out.println("Invalid input format.");

		} else {

			// Extract the number part from the input text
			number = codeText.substring(0, xIndex);
			// Extract the text part from the input text
			text = codeText.substring(xIndex + 1).trim();
		}

		if (mainMoveMethod)
			return text;

		return number;
	}

	public void moveMethod(String codeText, String number, int keyCode) {

		int i = Integer.parseInt(number);

		int delay = 570;

		if (i > 1) {

			pressAndReleaseKey(codeText, keyCode, i);

		} else {

			if (keyCode == KeyEvent.VK_SPACE)
				delay = 100;

			if (codeText.equals("MrPigy.TurnAround();"))
				delay = 200;

			KeyEvent keyEvent = new KeyEvent(game.getGameWindow().getJframe(), KeyEvent.KEY_PRESSED,
					System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);

			keyPressed(keyEvent);

			// Schedule a timer to release the "D" key after 2 seconds
			Timer releaseTimer = new Timer(delay, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					KeyEvent keyEvent = new KeyEvent(game.getGameWindow().getJframe(), KeyEvent.KEY_RELEASED,
							System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);

					keyReleased(keyEvent);
					// currentSimulatedKey = -1; // Reset the current key
				}
			});
			// releaseTimer.setRepeats(false);
			releaseTimer.setRepeats(false);
			releaseTimer.start();

		}

	}

	private void pressAndReleaseKey(String text, int keyCode, int times) {

		delay = 600;

		System.out.println("CODE FINISHED: " + game.getCodePanel().codeFinished);

		textCodeBlock = "";

//		keyCode_turn = keyCode;

		game.getCodePanel().codeFinished = true;
		if (text.equals("MrPigy.TurnAround();")) {

			textCodeBlock = "MrPigy.TurnAround();";

//			if(player.isGoingRight())
//				player.setGoingRight(false);
//			else
//				player.setGoingRight(true);
//			keyCode = keyCode_turn;

			delay = 100;
		}

//		if (text.equals("MrPigy.jump();"))
		if (text.equals("MrPigy.jump();") || text.equals("MrPigy.jumpAndAttack();"))
			delay = 350;

//		if (text.equals("MrPigy.jumpAndAttack();"))
//			delay = 650;

		timesPlayerPlayed = times;

		timesLefting = timesLefting - timesPlayerPlayed;

		if (timesLefting < 0) {

			timesLefting = 0;

		}

		AtomicInteger remainingTimes = new AtomicInteger(times);

		Timer releaseTimer = new Timer(1500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (remainingTimes.getAndDecrement() > 0) {

					KeyEvent keyPressEvent = new KeyEvent(game.getGameWindow().getJframe(), KeyEvent.KEY_PRESSED,
							System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
					keyPressed(keyPressEvent);

					Timer keyReleaseTimer = new Timer(delay, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

							KeyEvent keyReleaseEvent = new KeyEvent(game.getGameWindow().getJframe(),
									KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, keyCode,
									KeyEvent.CHAR_UNDEFINED);
							keyReleased(keyReleaseEvent);

							if (remainingTimes.get() == 0) {
								game.getCodePanel().codeFinished = false;

							}
						}
					});
					keyReleaseTimer.setRepeats(false);
					keyReleaseTimer.start();
				} else {
					// All key presses and releases are done

					if (text.equals("MrPigy.jumpAndAttack();")) {

						MouseEvent mouseEvent_attack = new MouseEvent(game.getGameWindow().getJframe(),
								MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), MouseEvent.BUTTON3, 0, 0, 1,
								false, MouseEvent.BUTTON3);
						mouseClicked(mouseEvent_attack);

					}

					game.getCodePanel().codeFinished = false;

					((Timer) e.getSource()).stop(); // Stop the timer

					if (timesLefting == 0 && !text.equals("MrPigy.jumpAndAttack();"))
						gameOver = true;

				}
			}

		});

		releaseTimer.setRepeats(true);
		releaseTimer.start();
	}

	private void pressAndReleaseMouse(String text, int mouseCode, int times) {

		int delay = 200;

		AtomicInteger remainingTimes = new AtomicInteger(times);

		Timer releaseTimer = new Timer(600, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (remainingTimes.getAndDecrement() > 0) {

					MouseEvent mouseEvent_attack = new MouseEvent(game.getGameWindow().getJframe(),
							MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), mouseCode, 0, 0, 1, false, mouseCode);
					mouseClicked(mouseEvent_attack);

					Timer releaseMouseTimer = new Timer(delay, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

//							MouseEvent mouseEvent_attack = new MouseEvent(game.getGameWindow().getJframe(), MouseEvent.MOUSE_CLICKED,
//									System.currentTimeMillis(), mouseCode, 0, 0, 1, false, mouseCode);
//							mouseClicked(mouseEvent_attack);

//							remainingTimes.decrementAndGet();

							if (remainingTimes.get() == 0) {

								game.getCodePanel().codeFinished = false;
							}
						}
					});
					releaseMouseTimer.setRepeats(false);
					releaseMouseTimer.start();

				} else {
					// All key presses and releases are done
					game.getCodePanel().codeFinished = false;
					((Timer) e.getSource()).stop(); // Stop the timer
				}
			}

		});

		releaseTimer.setRepeats(true);
		releaseTimer.start();

//		if (times > 1) {
//
//			MouseEvent mouseEvent_attack = new MouseEvent(game.getGameWindow().getJframe(), MouseEvent.MOUSE_CLICKED,
//					System.currentTimeMillis(), mouseCode, 0, 0, 1, false, mouseCode);
//			mouseClicked(mouseEvent_attack);
//
//			Timer releaseTimer = new Timer(delay, new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					MouseEvent mouseEvent_attack = new MouseEvent(game.getGameWindow().getJframe(),
//							MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), mouseCode, 0, 0, 1, false, mouseCode);
//					mouseClicked(mouseEvent_attack);
//
//					pressAndReleaseMouse(text, mouseCode, times - 1); // Recursive call with reduced times
//
//					if (times == 0)
//
//						game.getCodePanel().codeFinished = false;
//
//				}
//			});
//			releaseTimer.setRepeats(false);
//			releaseTimer.start();
//		} else {
//
//			MouseEvent mouseEvent_attack = new MouseEvent(game.getGameWindow().getJframe(), MouseEvent.MOUSE_CLICKED,
//					System.currentTimeMillis(), mouseCode, 0, 0, 1, false, mouseCode);
//			mouseClicked(mouseEvent_attack);
//
//			game.getCodePanel().codeFinished = false;
//
//		}

//		game.getCodePanel().codeFinished = false;

	}

	private void move(String text, String number, int keyCode) {
		int i = Integer.parseInt(number);

		System.out.println("text:" + text + " MOUSE EVENT: " + keyCode);
		// button 1 = 1
		// button 3 = 3

//		int delay = 600;

		if (keyCode == 1 || keyCode == 3) {

			pressAndReleaseMouse(text, keyCode, i);

		} else {
			pressAndReleaseKey(text, keyCode, i);
		}
//		if (i > 1) {
//			pressAndReleaseKey(text, keyCode, i);
//		} else {
//
//			if (text.equals("MrPigy.TurnAround();"))
//				delay = 100;
//
//			if (text.equals("MrPigy.jump();"))
//				delay = 100;
//
//			KeyEvent keyEvent = new KeyEvent(game.getGameWindow().getJframe(), KeyEvent.KEY_PRESSED,
//					System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
//			keyPressed(keyEvent);
//
//			// Schedule a timer to release the key after a delay
//			executorService.schedule(() -> {
//				KeyEvent releaseEvent = new KeyEvent(game.getGameWindow().getJframe(), KeyEvent.KEY_RELEASED,
//						System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);
//				keyReleased(releaseEvent);
//
//				game.getCodePanel().codeFinished = false;
//			}, delay, TimeUnit.MILLISECONDS); // Adjust the delay as needed
//		}
	}

	public void move(String text) {
//		executeMoveSequence(text);

		executorService.schedule(() -> {
			executeMoveSequence(text);
		}, 1000, TimeUnit.MILLISECONDS); // Increase the delay to slow down movements

	}

//	public void move(ArrayList<String> correctedOrder) {
//		currentIndex = 0;
////		executeMoveSequence(correctedOrder);
//		// Adjust the delay to control the speed of movements
//		executorService.schedule(() -> {
//			executeMoveSequence(correctedOrder);
//		}, 2000, TimeUnit.MILLISECONDS); // Increase the delay to slow down movements
//	}

	public int getKeyCodeBasedOnText(String text) {

		switch (text) {
		case "MrPigy.moveToClosestTree();":

			if (player.isGoingRight()) {
				return KeyEvent.VK_D;
			} else {
				return KeyEvent.VK_A;
			}

		case "MrPigy.jump();":
			return KeyEvent.VK_SPACE;

		case "MrPigy.jumpAndAttack();":
			return KeyEvent.VK_SPACE;

		case "MrPigy.attack();":
//			MouseEvent mouseEvent_attack1 = new MouseEvent(game.getGameWindow().getJframe(), MouseEvent.MOUSE_CLICKED,
//					System.currentTimeMillis(), MouseEvent.BUTTON1, 0, 0, 1, false, MouseEvent.BUTTON1);
//			mouseClicked(mouseEvent_attack1);
			return MouseEvent.BUTTON1;
//			break;

		case "MrPigy.TurnAround();":

			if (player.isGoingRight()) {
				player.setGoingRight(false);
				return KeyEvent.VK_A;

			} else {
				player.setGoingRight(true);
				return KeyEvent.VK_D;
			}

		case "MrPigy.specialAttack();":

			return MouseEvent.BUTTON3;

//			break;
		}
		return 0;

	}

	private void executeMoveSequence(String text) {
//		if (currentIndex < sequence.size()) {
		String codeBlockOrder = text;
		String number = moveMethod_substring(codeBlockOrder, false);
		String text_2 = moveMethod_substring(codeBlockOrder, true);
		int keyCode = getKeyCodeBasedOnText(text_2);

		move(text_2, number, keyCode);
//		currentIndex++;
		// Adjust the delay to control the speed of movements
//		executorService.schedule(() -> {
//			executeMoveSequence(text); // Recursive call for the next step
//		}, 2000, TimeUnit.MILLISECONDS); // Increase the delay to slow down movements

		if (gameOver) {

			stopCodeExecutor();
			resetAll();

		}

		if (lvlCompleted)
			stopCodeExecutor();

//		} else {
//
//			if (!gameOver || !lvlCompleted)
//				gameOver = true;
//
//		}

	}

	private void executeMoveSequence(ArrayList<String> sequence) {
		if (currentIndex < sequence.size()) {
			String codeBlockOrder = sequence.get(currentIndex);
			String number = moveMethod_substring(codeBlockOrder, false);
			String text = moveMethod_substring(codeBlockOrder, true);
			int keyCode = getKeyCodeBasedOnText(text);

			move(text, number, keyCode);
			currentIndex++;
			// Adjust the delay to control the speed of movements
			executorService.schedule(() -> {
				executeMoveSequence(sequence); // Recursive call for the next step
			}, 2000, TimeUnit.MILLISECONDS); // Increase the delay to slow down movements

			if (gameOver) {

				stopCodeExecutor();
				resetAll();

			}

			if (lvlCompleted)
				stopCodeExecutor();

		} else {

			if (!gameOver || !lvlCompleted)
				gameOver = true;

		}

	}

	public void stopCodeExecutor() {
		executorService.shutdownNow();
	}

	public void restartCodeExecutor() {
		executorService = Executors.newSingleThreadScheduledExecutor();
	}

	public boolean isPaused() {
		return paused;
	}

	public void setMaxLvlOffset(int lvlOffset) {
		this.maxLvlOffsetX = lvlOffset;
	}

	public void unpauseGame() {
		paused = false;
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public Player getPlayer() {
		return player;
	}

	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	public ObjectManager getObjectManager() {
		return objectManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isLvlCompleted() {
		return lvlCompleted;
	}

	public boolean isGameCompleted() {
		return gameCompleted;
	}
}