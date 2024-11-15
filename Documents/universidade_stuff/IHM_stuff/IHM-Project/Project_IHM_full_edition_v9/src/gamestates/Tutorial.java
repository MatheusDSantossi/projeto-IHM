package gamestates;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.CodePanel;
import main.Game;
import utilz.LoadSave;

public class Tutorial extends State implements Statemethods {
	private BufferedImage backgroundImg, TutorialImg;
	private int bgX, bgY, bgW, bgH;
	private float bgYFloat;

	private ArrayList<ShowEntity> entitiesList;

	private JPanel tutorialPanel;
	private int currentStep = 0;

	private String[] tutorialSteps = { "Bem-vindo ao Tutorial!",
			"PRIMEIRO VOCE VAI APRENDER COMO{" + "FUNCIONA A JOGABILIDADE DO JOGO.",

			"PRIMEIRO TEMOS OS CODE BLOCKIES{" + "ONDE VOCE PODE ESCOLHER O PROXIMO}"
					+ " PASSO DO MR. PIGGY NESSE EXEMPLO[" + "TEMOS 2X MR.PIGGY.jump();",

			"Isso o fara pular 2 VEZES mas{" + "TENHA CUIDADO, OS MOVIMENTOS TEM UM}"
					+ "TEMPO PARA ACONTECER VOCE TERA QUE[" + "SE MOVIMENTAR NA HORA CERTA.",

			"CADA MOVIMENTO TEM um LIMITE DE{" + "VEZES QUE PODE SER EXECUTADO POR}"
					+ "tentativa, O TURN AROUND, POR EXEMPLO[" + "SO PODE SER EXECUTADO 1 VEZ A] "
					+ "CADA ENTER CLICADO.",

			"VOCE PODE ALTERAR A QUANTIDADE{" + "DE VEZES QUE O MOVIMENTO SERA}"
					+ "EXECUTADO CLINCANDO EM CIMA DO CODE[" + " BLOCKY SELECIONADO COM O BOTAO] "
					+ "ESQUERDO E DIMINUIR COM O+" + "DIREITO.",

			"CADA FASE TEM UM LIMITE DE{" + "MOVIMENTOS QUE VOCE PODE FAZER.}" + "O move.TO.CLOSEST.TREE E ESSENCIAL["
					+ "QUE VOCE APRENDA A UTILIZA-LO]" + "CORRETAMENTE.",

			"PARA AJUDAR NESSA JORNADA ESSE{" + "PEQUENO RETANGULO vai APARECER}"
					+ " NO LOCAL ONDE O MR.PIGGY VAI ANDAR[" + "TENTE USAR ELE COMO UM GUIA DO MOMENTO]"
					+ "CERTO PARA USAR O MOVE.TO.",

			"ENTRETANTO... NEM SEMPRE O{" + "MOVE.TO SERA O MOVIMENTO MAIS EFICAZ}"
					+ "PARA USAR PRESTE ATENCÃO NA POSICAO[" + "DO MR.PIGGY. MAIS UMA COISA..",

			"O BLOCK VERDE IS SELECIONADO{" + "VOCE PODE SELECIONAR UM BLOCK}"
					+ "DE CADA VEZ BOA SORTE E BOM JOGO!"};

	private BufferedImage tutorialImage1, tutorialImage2;
	private int tutorialImageX, tutorialImageY, tutorialImageWidth, tutorialImageHeight;

	private ArrayList<BufferedImage> smallText_letters = new ArrayList<>();

	private BufferedImage letterA;
	private BufferedImage letterB;
	private BufferedImage letterC;
	private BufferedImage letterD;
	private BufferedImage letterE;
	private BufferedImage letterF;

	private BufferedImage letterG;
	private BufferedImage letterH;
	private BufferedImage letterI;
	private BufferedImage letterJ;
	private BufferedImage letterK;
	private BufferedImage letterL;
	private BufferedImage letterM;
	private BufferedImage letterN;
	private BufferedImage letterO;
	private BufferedImage letterP;
	private BufferedImage letterQ;

	private BufferedImage letterR;
	private BufferedImage letterS;
	private BufferedImage letterT;
	private BufferedImage letterU;
	private BufferedImage letterV;
	private BufferedImage letterX;
	private BufferedImage letterY;
	private BufferedImage letterW;
	private BufferedImage letterZ;

	// Numbers
	private BufferedImage number1;
	private BufferedImage number2;
	private BufferedImage number3;
	private BufferedImage number4;
	private BufferedImage number5;
	private BufferedImage number6;
	private BufferedImage number7;
	private BufferedImage number8;
	private BufferedImage number9;
	private BufferedImage number0;

	// Special characters
	private BufferedImage lessSign;
	private BufferedImage periodSimbol;
	private BufferedImage exclamationSimbol;

	private int tutorialImageNumber = 1;

	private long startTime;
	private int typingSpeed = 100; // Adjust the typing speed as needed (in milliseconds)

//	private long lastUpdate;
	private int currentTypedCharacters = 0;
	private long lastUpdate = System.currentTimeMillis();

	private boolean animationStarted = true; // Add this variable

	private boolean firstRectTutorial = false, secondRectTutorial = false, thirdRectTutorial = false;

	// Define constants for GUI layout
	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HEIGHT = 40;
	private static final int BUTTON_MARGIN = 10;
	
	private boolean musicStarted = false;


	public Tutorial(Game game) {
		super(game);
		
//		game.getAudioPlayer().playSong(4);

		initialize();

	}

	private void initialize() {
		backgroundImg = game.loadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_MENU_PART);

		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = Game.GAME_HEIGHT;
		loadEntities();
		loadTutorial();
		loadSmallText();
		startTime = System.currentTimeMillis();
		lastUpdate = System.currentTimeMillis();

	}

	public void drawText(Graphics g, String text, int x, int y, int scaleFactor, int spaceBetween) {

		long currentTime = System.currentTimeMillis();
		if (currentTime - lastUpdate >= typingSpeed) {
			lastUpdate = currentTime;

			// Increment the number of typed characters
			currentTypedCharacters++;

			// Get the current tutorial step text
			String currentStepText = tutorialSteps[tutorialImageNumber - 1];

			// Ensure the typing animation doesn't exceed the length of the current step
			// text
			currentTypedCharacters = Math.min(currentTypedCharacters, currentStepText.length());

			// Reset currentTypedCharacters and lastUpdate when transitioning to a new step
			if (currentTypedCharacters >= currentStepText.length()) {
				currentTypedCharacters = 0;
				lastUpdate = System.currentTimeMillis();
			}
		}

		int typedCharacters = (int) ((System.currentTimeMillis() - startTime) / typingSpeed);

		typedCharacters = Math.min(typedCharacters, text.length()); // Ensure we don't exceed the text length

		String displayedText = text.substring(0, typedCharacters); // Extract the portion to be displayed

		for (int i = 0; i < displayedText.length(); i++) {
			char character = displayedText.charAt(i);
			BufferedImage letterImage = getLetterImage(character); // Replace this with your method to get the image

			if (character == ' ') {

				spaceBetween = 15;

			} else {

				spaceBetween = 15;

			}

			if (character == '{') {

//				245, 150, 2, 15 // first
//				220, 180, 2, 15 // second
// 				220, 210, 2, 15 // third
//				245, 240, 2, 15 // fourth
//				310, 270, 2, 15 // fifth

				if(tutorialImageNumber >= 8)
					x = 75;
				else
					x = 75;
				y = y + 30;

			} else if (character == '}') {
				if(tutorialImageNumber >= 8)
					x = 60;
				else
					x = 75; 
				y = y + 30;

			} else if (character == '[') {

				if(tutorialImageNumber >= 8)
					x = 75;
				else
					x = 90;
				
				y = y + 30;

			} else if (character == ']') {

				if(tutorialImageNumber >= 8)
					x = 90;
				else
				
					x = 115;
				y = y + 30;

			} else if (character == '+') {

				if(tutorialImageNumber >= 8)
					x = 150;
				else
				
					x = 180;
				y = y + 30;

			}

			if (letterImage != null) {
				int letterWidth = letterImage.getWidth() * scaleFactor;
				int letterHeight = letterImage.getHeight() * scaleFactor;
				g.drawImage(letterImage, x, y, letterWidth, letterHeight, null);
				x += spaceBetween; // Move the x-coordinate for the next letter
			} else if (character == ' ') {

				g.drawString(" ", x, y);
				x += spaceBetween; // Move the x-coordinate for the next letter

			}
		}
	}

	private BufferedImage getLetterImage(char character) {

		char upperChacacter = Character.toUpperCase(character);

		switch (upperChacacter) {
		case 'A':
			return letterA;
		case 'B':
			return letterB;
		case 'C':
			return letterC;
		case 'D':
			return letterD;
		case 'E':
			return letterE;
		case 'F':
			return letterF;
		case 'G':
			return letterG;
		case 'H':
			return letterH;
		case 'I':
			return letterI;
		case 'J':
			return letterJ;
		case 'K':
			return letterK;
		case 'L':
			return letterL;
		case 'M':
			return letterM;
		case 'N':
			return letterN;
		case 'O':
			return letterO;
		case 'P':
			return letterP;
		case 'Q':
			return letterQ;
		case 'R':
			return letterR;
		case 'S':
			return letterS;
		case 'T':
			return letterT;
		case 'U':
			return letterU;
		case 'V':
			return letterV;
		case 'W':
			return letterW;
		case 'X':
			return letterX;
		case 'Y':
			return letterY;
		case 'Z':
			return letterZ;
		case '1':
			return number1;
		case '2':
			return number2;
		case '3':
			return number3;
		case '4':
			return number4;
		case '5':
			return number5;
		case '6':
			return number6;
		case '7':
			return number7;
		case '8':
			return number8;
		case '9':
			return number9;
		case '0':
			return number0;
		case '-':
			return lessSign;
		case '.':
			return periodSimbol;
		case '!':
			return exclamationSimbol;

		default:
			return null; // Return null if the character doesn't have an image
		}
	}

	private void loadSmallText() {

		BufferedImage temp;

		for (int i = 1; i <= 52; i++) {

			temp = game.loadSave.GetSmallTexts("" + i + ".png");

			smallText_letters.add(temp);

		}

		letterA = smallText_letters.get(0);
		letterB = smallText_letters.get(1);
		letterC = smallText_letters.get(2);
		letterD = smallText_letters.get(3);
		letterE = smallText_letters.get(4);
		letterF = smallText_letters.get(5);

		letterG = smallText_letters.get(6);
		letterH = smallText_letters.get(7);
		letterI = smallText_letters.get(8);
		letterJ = smallText_letters.get(9);
		letterK = smallText_letters.get(10);
		letterL = smallText_letters.get(11);
		letterM = smallText_letters.get(12);
		letterN = smallText_letters.get(13);
		letterO = smallText_letters.get(14);
		letterP = smallText_letters.get(15);
		letterQ = smallText_letters.get(16);

		letterR = smallText_letters.get(17);
		letterS = smallText_letters.get(18);
		letterT = smallText_letters.get(19);
		letterU = smallText_letters.get(20);
		letterV = smallText_letters.get(21);
		letterW = smallText_letters.get(22);
		letterX = smallText_letters.get(23);
		letterY = smallText_letters.get(24);
		letterZ = smallText_letters.get(25);

		number1 = smallText_letters.get(26);
		number2 = smallText_letters.get(27);
		number3 = smallText_letters.get(28);
		number4 = smallText_letters.get(29);
		number5 = smallText_letters.get(30);
		number6 = smallText_letters.get(31);
		number7 = smallText_letters.get(32);
		number8 = smallText_letters.get(33);
		number9 = smallText_letters.get(34);
		number0 = smallText_letters.get(35);

		lessSign = smallText_letters.get(36);

		exclamationSimbol = smallText_letters.get(45);
		periodSimbol = smallText_letters.get(47);

		// I stopped in 48

	}

	private void loadTutorial() {

//		JLabel stepLabel = new JLabel();
//        game.getGamePanel().add(stepLabel, BorderLayout.CENTER);
//
//        JButton backButton = new JButton("Back");
//        JButton nextButton = new JButton("Next");
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(backButton);
//        buttonPanel.add(nextButton);
//        game.getGamePanel().add(buttonPanel, BorderLayout.SOUTH);

		startTime = System.currentTimeMillis(); // Initialize the startTime

		long lastCharTypedTime = System.currentTimeMillis(); // Initialize the lastCharTypedTime

		tutorialImage1 = game.loadSave.GetTutorialImages(LoadSave.TUTORIAL_IMAGE_1_GAME_PART);
		tutorialImage2 = game.loadSave.GetTutorialImages(LoadSave.TUTORIAL_IMAGE_1_GAME_PART_RECT);

//		tutorialImageX = Game.GAME_WIDTH / 6;
//		tutorialImageY = Game.GAME_HEIGHT / 6;

		tutorialImageX = -20;
		tutorialImageY = 0;

		tutorialImageWidth = tutorialImage1.getWidth();
		tutorialImageHeight = tutorialImage1.getHeight();

	}

	private void loadEntities() {
		entitiesList = new ArrayList<>();
		entitiesList.add(new ShowEntity(getIdleAni(game.loadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS), 5, 64, 40),
				(int) (Game.GAME_WIDTH / 13), (int) (Game.GAME_HEIGHT * 0.8)));

//		entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS), 5, 64, 40), (int) (Game.GAME_WIDTH * 0.05), (int) (Game.GAME_HEIGHT * 0.8)));
//		entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE), 9, 72, 32), (int) (Game.GAME_WIDTH * 0.15), (int) (Game.GAME_HEIGHT * 0.75)));
//		entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.PINKSTAR_ATLAS), 8, 34, 30), (int) (Game.GAME_WIDTH * 0.7), (int) (Game.GAME_HEIGHT * 0.75)));
//		entitiesList.add(new ShowEntity(getIdleAni(LoadSave.GetSpriteAtlas(LoadSave.SHARK_ATLAS), 8, 34, 30), (int) (Game.GAME_WIDTH * 0.8), (int) (Game.GAME_HEIGHT * 0.8)));
	}

	private BufferedImage[] getIdleAni(BufferedImage atlas, int spritesAmount, int width, int height) {
		BufferedImage[] arr = new BufferedImage[spritesAmount];
		for (int i = 0; i < spritesAmount; i++)
			arr[i] = atlas.getSubimage(width * i, 0, width, height);
		return arr;
	}


	@Override
	public void update() {
		if (!musicStarted) {
			game.getAudioPlayer().playSong(4);
			musicStarted = true; // Set the flag to true to prevent repeated music playback
		}
		
		bgYFloat -= 0.2f;

		for (ShowEntity se : entitiesList)
			se.update();

		// Only update the typing animation if the animationStarted flag is true
		if (animationStarted) {

			long currentTime = System.currentTimeMillis();
			if (currentTime - lastUpdate >= typingSpeed) {
				lastUpdate = currentTime;

				// Calculate the number of typed characters based on the elapsed time
				int elapsedCharacters = (int) ((currentTime - startTime) / typingSpeed);
				String currentStepText = tutorialSteps[tutorialImageNumber - 1];

				// Ensure the typing animation doesn't exceed the length of the current step
				// text
				currentTypedCharacters = Math.min(elapsedCharacters, currentStepText.length());

				// If all characters are typed, move to the next step
//				if (currentTypedCharacters >= currentStepText.length()) {
//					animationStarted = false;
//					currentTypedCharacters = 0;
////					tutorialImageNumber++;
//
////				if(tutorialImageNumber)
//
//					// Reset tutorialImageNumber if it goes beyond the number of steps
//					if (tutorialImageNumber > tutorialSteps.length) {
//						tutorialImageNumber = 0;
//					}
//
//					// Update the start time for the next step
//					startTime = System.currentTimeMillis();
//				}
			}
		}
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		if (tutorialImageNumber < 8) {

			g.drawImage(tutorialImage1, tutorialImageX, tutorialImageY, tutorialImageWidth, tutorialImageHeight, null);
		}

		if (tutorialImageNumber >= 8) {

			g.drawImage(tutorialImage2, tutorialImageX, tutorialImageY, tutorialImageWidth, tutorialImageHeight, null);

			String currentStepText = tutorialSteps[tutorialImageNumber - 1];

			drawText(g, currentStepText, 100, 120, 2, 15);

		}

		if (tutorialImageNumber < 8) {

			String currentStepText = tutorialSteps[tutorialImageNumber - 1];

			drawText(g, currentStepText, 110, 130, 2, 15);
		}

		if (tutorialImageNumber == 3) {

			firstRectTutorial = true;

		}

		if (tutorialImageNumber == 5) {
			firstRectTutorial = false;
			secondRectTutorial = true;

		}

		if (tutorialImageNumber == 6) {
			secondRectTutorial = false;
			thirdRectTutorial = true;

		}

		if (tutorialImageNumber == 7) {

			thirdRectTutorial = false;

//			g.setColor(Color.RED);

			TutorialCodePanel.drawHighlightedArea(g, new Point(550, 100), 280, 80);

		}

		drawButtons(g);

//		g.setFont(new Font("Arial", Font.BOLD, 30));
//		g.setColor(Color.WHITE);
//
//		if (tutorialImageNumber > 1)
//
//			g.drawString("BACK", Game.GAME_WIDTH - 800, Game.GAME_HEIGHT - 50);
//
//		if (tutorialImageNumber < tutorialSteps.length)
//			g.drawString("NEXT", Game.GAME_WIDTH - 150, Game.GAME_HEIGHT - 50);
//
//		g.drawString("MENU", Game.GAME_WIDTH - 150, 50);

		for (ShowEntity se : entitiesList)
			se.draw(g);
	}

	private void drawButtons(Graphics g) {

		g.setFont(new Font("Arial", Font.BOLD, 20));

		if (tutorialImageNumber > 1) {
			g.setColor(Color.WHITE);
			g.fillRect(Game.GAME_WIDTH - BUTTON_WIDTH - BUTTON_MARGIN, Game.GAME_HEIGHT - BUTTON_HEIGHT - BUTTON_MARGIN,
					BUTTON_WIDTH, BUTTON_HEIGHT);
			g.setColor(Color.BLACK);
			g.drawString("BACK", Game.GAME_WIDTH - BUTTON_WIDTH / 2 - 40, Game.GAME_HEIGHT - BUTTON_HEIGHT / 2);
		}

		if (tutorialImageNumber < tutorialSteps.length) {
			g.setColor(Color.WHITE);
			g.fillRect(Game.GAME_WIDTH - BUTTON_WIDTH - BUTTON_MARGIN,
					Game.GAME_HEIGHT - 2 * BUTTON_HEIGHT - 2 * BUTTON_MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT);
			g.setColor(Color.BLACK);
			g.drawString("NEXT", Game.GAME_WIDTH - BUTTON_WIDTH / 2 - 40,
					Game.GAME_HEIGHT - 3 * BUTTON_HEIGHT / 2 - BUTTON_MARGIN);
		}

		g.setColor(Color.WHITE);
		g.fillRect(Game.GAME_WIDTH - BUTTON_WIDTH - BUTTON_MARGIN, BUTTON_MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT);
		g.setColor(Color.BLACK);
		g.drawString("MENU", Game.GAME_WIDTH - BUTTON_WIDTH / 2 - 40, BUTTON_HEIGHT / 2 + 15);

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			bgYFloat = 0;
			setGamestate(Gamestate.MENU);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {

//		animationStarted = false;

	}

	@Override
	public void mouseClicked(MouseEvent e) {

		int mouseX = e.getX();
		int mouseY = e.getY();

		if (mouseInButton(mouseX, mouseY, Game.GAME_WIDTH - BUTTON_WIDTH - BUTTON_MARGIN,
				Game.GAME_HEIGHT - BUTTON_HEIGHT - BUTTON_MARGIN)) {
			if (tutorialImageNumber > 1) {
				tutorialImageNumber--;
				game.getGamePanel().repaint();
			}
		} else if (mouseInButton(mouseX, mouseY, Game.GAME_WIDTH - BUTTON_WIDTH - BUTTON_MARGIN,
				Game.GAME_HEIGHT - 2 * BUTTON_HEIGHT - 2 * BUTTON_MARGIN)) {
			if (tutorialImageNumber < tutorialSteps.length) {
				tutorialImageNumber++;
				animationStarted = true;
				currentTypedCharacters = 0;
				startTime = System.currentTimeMillis();
				game.getGamePanel().repaint();
			}
		} else if (mouseInButton(mouseX, mouseY, Game.GAME_WIDTH - BUTTON_WIDTH - BUTTON_MARGIN, BUTTON_MARGIN)) {
			if (Gamestate.state == Gamestate.TUTORIAL) {
				setGamestate(Gamestate.MENU);
			}
		}

//		int mouseX = e.getX();
//		int mouseY = e.getY();
//
//		// Check if the click was inside the "Next" area
//		if (mouseX >= Game.GAME_WIDTH - 150 && mouseX <= Game.GAME_WIDTH && mouseY >= Game.GAME_HEIGHT - 70
//				&& mouseY <= Game.GAME_HEIGHT && tutorialImageNumber < tutorialSteps.length) {
//			// Increment the tutorialImageNumber
//			tutorialImageNumber++;
//
//			// Start the animation for the next step
//			animationStarted = true;
//
//			// Reset the current typed characters and update the start time for the next
//			// step
//			currentTypedCharacters = 0;
//			startTime = System.currentTimeMillis();
//
//			// Repaint the game to show the updated content
//			game.getGamePanel().repaint();
//		}
//
//		// Check if the click was inside the "Back" area
//		if (mouseX >= Game.GAME_WIDTH - 800 && mouseX <= Game.GAME_WIDTH - 650 && mouseY >= Game.GAME_HEIGHT - 70
//				&& mouseY <= Game.GAME_HEIGHT && tutorialImageNumber >= 2) {
//
//			tutorialImageNumber--;
//
//			game.getGamePanel().repaint();
//
//		}
//
//		if (mouseX >= Game.GAME_WIDTH - 150 && mouseX <= Game.GAME_WIDTH - 50 && mouseY >= 10 && mouseY <= 70) {
//
//			if (Gamestate.state == Gamestate.TUTORIAL)
//
//				setGamestate(Gamestate.MENU);
//
//		}

	}

	  private boolean mouseInButton(int mouseX, int mouseY, int buttonX, int buttonY) {
	        return mouseX >= buttonX && mouseX <= buttonX + BUTTON_WIDTH && mouseY >= buttonY && mouseY <= buttonY + BUTTON_HEIGHT;
	    }
	
	@Override
	public void keyPressed(KeyEvent e) {
	}

	public class ShowEntity {
		private BufferedImage[] idleAnimation;
		private int x, y, aniIndex, aniTick;

		public ShowEntity(BufferedImage[] idleAnimation, int x, int y) {
			this.idleAnimation = idleAnimation;
			this.x = x;
			this.y = y;
		}

		public void draw(Graphics g) {
			g.drawImage(idleAnimation[aniIndex], x, y, (int) (idleAnimation[aniIndex].getWidth() * 4),
					(int) (idleAnimation[aniIndex].getHeight() * 4), null);
		}

		public void update() {
			aniTick++;
			if (aniTick >= 25) {
				aniTick = 0;
				aniIndex++;
				if (aniIndex >= idleAnimation.length)
					aniIndex = 0;
			}

		}
	}

	public ArrayList<ShowEntity> getEntitiesList() {
		return entitiesList;
	}

	public ArrayList<BufferedImage> getSmallText_letters() {
		return smallText_letters;
	}

	public boolean isFirstRectTutorial() {
		return firstRectTutorial;
	}

	public boolean isSecondRectTutorial() {
		return secondRectTutorial;
	}

	public boolean isThirdRectTutorial() {
		return thirdRectTutorial;
	}

	public int getTutorialImageNumber() {
		return tutorialImageNumber;
	}

}
