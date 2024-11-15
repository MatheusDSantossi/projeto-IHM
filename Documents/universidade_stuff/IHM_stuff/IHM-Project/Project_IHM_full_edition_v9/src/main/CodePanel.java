package main;

import static main.Game.GAME_HEIGHT;
import static main.Game.CODE_PANEL_WIDTH;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.LayoutFocusTraversalPolicy;

import entities.CodeBlock;
import gamestates.Gamestate;
import gamestates.MenuCodePanel;
import gamestates.PlayingCodePanel;
import gamestates.Tutorial.ShowEntity;
import gamestates.TutorialCodePanel;
import inputs.KeyboardInputs;
import utilz.LoadSave;

public class CodePanel extends JPanel {

	public Game game;

	private KeyboardInputs keyboardInputs;
	public boolean codeFinished = false;

	public int nextIndex = 0;

	public int indexLocked = 0;

	private ArrayList<CodeBlock> codeBlocks = new ArrayList<>();
	private ArrayList<CodeBlock> codeBlocksLocked = new ArrayList<>();

	private ArrayList<CodeBlock> codeBlocksFirst = new ArrayList<>();
	public int correctBlockIndex = 0;

	private ArrayList<String> blockTexts = new ArrayList<>();

	private CodeBlock selectedBlock;

	private MenuCodePanel menuCodePanel;

	private PlayingCodePanel playingCodePanel;
	
	private TutorialCodePanel tutorialCodePanel;

	public static boolean playerCanMove = false;

	private ArrayList<String> possible_ways_to_win = new ArrayList<>();
	

	public CodePanel(Game game) {
		this.game = game;
		keyboardInputs = new KeyboardInputs(game.getGamePanel(), this);

		menuCodePanel = new MenuCodePanel(game);
		playingCodePanel = new PlayingCodePanel(game);
		tutorialCodePanel = new TutorialCodePanel(game);

		setBackground(Color.red);

		setLayout(new GridLayout(6, 1));

//        setBorder(BorderFactory.createLineBorder(Color.black));

		possible_ways_to_win.add("MrPigy.moveToClosestTree();");
		possible_ways_to_win.add("MrPigy.jump();");
		possible_ways_to_win.add("MrPigy.attack();");
		possible_ways_to_win.add("MrPigy.TurnAround();");
		possible_ways_to_win.add("MrPigy.jumpAndAttack();");
		possible_ways_to_win.add("MrPigy.specialAttack();");

		Collections.shuffle(possible_ways_to_win); // Shuffle the list to get random elements

		for (int i = 0; i < 6; i++) {
			String randomElement = possible_ways_to_win.get(i);
			CodeBlock block = new CodeBlock(randomElement, codeFinished, this);
			codeBlocks.add(block);
			blockTexts.add(block.getText());

			codeBlocksFirst.add(block);

			block.setSelected(false);
//            add(block);
		}

		setPanelSize();
		addKeyListener(keyboardInputs);
		setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
		codeBlocks.get(0).setFocused(true); // Set the focus on the first block
	}

	public void resetCodeBlocks() {
		for (CodeBlock codeBlock : codeBlocks) {
			codeBlock.resetBlock();
			
		}

		codeFinished = false;
//        playerCanMove = false;
//        codeBlocksLocked.clear();
//        indexLocked = 0;
		revalidate();
		repaint();

		setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
//        getCodeBlocks().get(0).setFocused(true); // Set the focus on the first block

		setFocusable(true);
		requestFocus();
	}

	public void keyPressed(KeyEvent e) {

		if (Gamestate.state == Gamestate.PLAYING && !game.getPlaying().isGameOver() && !game.getPlaying().isLvlCompleted() && !game.getPlaying().isPaused()) {
			int keyCode = e.getKeyCode();

			ArrayList<CodeBlock> codeBlocksCopy = new ArrayList<>(codeBlocks); // Create a copy of codeBlocks

			int currentIndex = codeBlocks.indexOf(selectedBlock);

			for (int i = 0; i < codeBlocks.size(); i++) {
				if (codeBlocks.get(i).isFocused()) {
					currentIndex = i;
					selectedBlock = codeBlocks.get(i);
					break;
				}
			}

			for (CodeBlock block : codeBlocksCopy) {
				if (block.isFocused()) {
					selectedBlock = block;
					selectedBlock.setSelected(true);
					break;
				}
			}

			if (selectedBlock != null && !codeFinished) {
				switch (keyCode) {
				case KeyEvent.VK_UP:
//					selectedBlock.moveBlockUp(selectedBlock);
					if (currentIndex > 0) {
                        selectedBlock.setSelected(false);
                        selectedBlock.changeBlockColor(codeFinished);
                        selectedBlock = codeBlocks.get(currentIndex - 1);
                        selectedBlock.setSelected(true);
                        selectedBlock.changeBlockColor(codeFinished);
                    }
					break;

				case KeyEvent.VK_DOWN:
//					selectedBlock.moveBlockDown(selectedBlock);
					
					 if (currentIndex < codeBlocks.size() - 1) {
	                        selectedBlock.setSelected(false);
	                        selectedBlock.changeBlockColor(codeFinished);
	                        selectedBlock = codeBlocks.get(currentIndex + 1);
	                        selectedBlock.setSelected(true);
	                        selectedBlock.changeBlockColor(codeFinished);
	                    }
				
					break;

				case KeyEvent.VK_ENTER:

					selectedBlock.executeAction(selectedBlock.getText()); // Execute the selected action
					selectedBlock.moveFocusToNextBlock(selectedBlock); // Move focus to the next block

					break;
				}
				selectedBlock.changeBlockColor(codeFinished);
				
				 // Deselect other blocks
	            for (CodeBlock block : codeBlocks) {
	                if (block != selectedBlock) {
	                    block.setFocused(false);
	                    block.changeBlockColor(codeFinished);
	                }
	            }
			}
		}
	}

//	public void keyPressed(KeyEvent e) {
//		
////		if(Gamestate.state == Gamestate.PLAYING) {
//		int keyCode = e.getKeyCode();
//
//		ArrayList<CodeBlock> codeBlocksCopy = new ArrayList<>(codeBlocks); // Create a copy of codeBlocks
//
//		int currentIndex = -1; // Initialize with an invalid value
//
//		for (int i = 0; i < codeBlocks.size(); i++) {
//			if (codeBlocks.get(i).isFocused()) {
//				currentIndex = i;
//				selectedBlock = codeBlocks.get(i);
//				break;
//			}
//		}
//
//		for (CodeBlock block : codeBlocksCopy) {
//			if (block.isFocused()) {
//				selectedBlock = block;
//				selectedBlock.setSelected(true);
//				break;
//			}
//		}
//
//		if (selectedBlock != null && !codeFinished) {
//			switch (keyCode) {
//			case KeyEvent.VK_UP:
//
//				selectedBlock.moveBlockUp();
//				break;
//
//			case KeyEvent.VK_DOWN:
//
//				selectedBlock.moveBlockDown();
//				break;
//
//			case KeyEvent.VK_ENTER:
//
//				selectedBlock.executeAction(selectedBlock.getText()); // Execute the selected action
//                selectedBlock.moveFocusToNextBlock(selectedBlock); // Move focus to the next block
//				
//
////				selectedBlock.moveFocusToNextBlock(selectedBlock);
////
////				
////				codeFinished = true;
////
////				selectedBlock.verifyOneCodeBlock(selectedBlock.getCode());
//
////				if (selectedBlock.checkAllLocked()) {
////
////					System.out.println("Player start moving!");
//////					playerCanMove = true;
////
////				}
//				break;
//			}
//			selectedBlock.changeBlockColor(codeFinished);
//		}
//	}

//	public void keyReleased(KeyEvent e) {
//
//		int keyCode = e.getKeyCode();
//
//		switch (keyCode) {
////		case KeyEvent.VK_UP:
////
////			selectedBlock.moveBlockUp();
////			break;
////
////		case KeyEvent.VK_DOWN:
////
////			selectedBlock.moveBlockDown();
////			break;
//
//		case KeyEvent.VK_ENTER:
//
////			selectedBlock.moveFocusToNextBlock(selectedBlock);
//
//			selectedBlock.setEnterKeyPressed(false);
//
//			break;
//		}
//
//	}

	private void setPanelSize() {
		Dimension size = new Dimension(CODE_PANEL_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.white);
		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 40; j++) {
				g.fillRect(i * 20, j * 20, 20, 20);
			}
		}

		if (Gamestate.state == Gamestate.MENU || Gamestate.state == Gamestate.CREDITS)
			menuCodePanel.draw(g);
		
		if(Gamestate.state == Gamestate.TUTORIAL) {
			
//			playingCodePanel.draw(g);
			resetDraw();
			tutorialCodePanel.draw(g);
			
//			for (ShowEntity se : game.getTutorial().getEntitiesList())
//				se.draw(g);
			
			
//			g.drawImage(LoadSave.GetSpriteAtlas(LoadSave.BALLON_TALK), 100, 100, null);
			
//			int i = 0;
//			
//			
//			for(BufferedImage img: game.getTutorial().getSmallText_letters()) {
//				
//				i += 5;
//				
//				g.drawImage(img, 100 + i, 100, null);
//				
//			}
			
			
		}
		

		if (Gamestate.state == Gamestate.PLAYING)
			playingCodePanel.draw(g);
	}

	public Game getGame() {
		return game;
	}

	public KeyboardInputs getKeyboardInputs() {
		return keyboardInputs;
	}

	public boolean isCodeFinished() {
		return codeFinished;
	}

	public ArrayList<CodeBlock> getCodeBlocks() {
		return codeBlocks;
	}

	public void setCodeBlocks(ArrayList<CodeBlock> codeBlocks) {
		this.codeBlocks = codeBlocks;
	}

	public ArrayList<CodeBlock> getCodeBlocksLocked() {
		return codeBlocksLocked;
	}

	public ArrayList<CodeBlock> getCodeBlocksFirst() {
		return codeBlocksFirst;
	}

	public int getCorrectBlockIndex() {
		return correctBlockIndex;
	}

	public ArrayList<String> getBlockTexts() {
		return blockTexts;
	}

	public MenuCodePanel getMenuCodePanel() {
		return menuCodePanel;
	}

	public PlayingCodePanel getPlayingCodePanel() {
		return playingCodePanel;
	}

	public CodeBlock getSelectedBlock() {
		return selectedBlock;
	}

	public void setSelectedBlock(CodeBlock selectedBlock) {
		this.selectedBlock = selectedBlock;
	}

	public void resetDraw() {

		game.getCodePanel().setBorder(null);
		game.getCodePanel().removeAll();
		game.getCodePanel().revalidate();
		game.getCodePanel().repaint();
		
		setBorder(null);
	}

}
