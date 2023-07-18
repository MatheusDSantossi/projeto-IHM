package main;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.SwingUtilities;

import entities.CodeBlock;
import gameStates.Playing;
import inputs.KeyBoardInputs;

public class CodePanel extends JPanel {

	public Game game;

	private KeyBoardInputs keyboardInputs;
	public boolean codeFinished = false;

	public int nextIndex = 0;

	public int indexLocked = 0;

	private ArrayList<CodeBlock> codeBlocks = new ArrayList<>();
	private ArrayList<CodeBlock> codeBlocksLocked = new ArrayList<>();

	private ArrayList<CodeBlock> codeBlocksCorrected = new ArrayList<>();
	public int correctBlockIndex = 0;

	private ArrayList<String> blockTexts = new ArrayList<>();

	private CodeBlock selectedBlock;

	public static boolean playerCanMove = false;

	public CodePanel(Game game) {

		keyboardInputs = new KeyBoardInputs(game.getGamePanel(), this);

		this.game = game;

		setLayout(new GridLayout(6, 1));

		// Add code blocks to the programming area
		CodeBlock block1 = new CodeBlock("MrPigy.moveToClosestThree();", codeFinished, this);
		CodeBlock block2 = new CodeBlock("MrPigy.jump();", codeFinished, this);
		CodeBlock block3 = new CodeBlock("MrPigy.attack();", codeFinished, this);

		CodeBlock block4 = new CodeBlock("MrPigy.moveBack();", codeFinished, this);
		CodeBlock block5 = new CodeBlock("MrPigy.attack();", codeFinished, this);
		CodeBlock block6 = new CodeBlock("MrPigy.respawnInTheBeforePlace();", codeFinished, this);

//		playerMoveTest = new PlayerMoveTest(300, 150);

		codeBlocks.add(block1);
		codeBlocks.add(block2);
		codeBlocks.add(block3);
		codeBlocks.add(block4);
		codeBlocks.add(block5);
		codeBlocks.add(block6);

		blockTexts.add(block1.getText());
		blockTexts.add(block2.getText());
		blockTexts.add(block3.getText());
		blockTexts.add(block4.getText());
		blockTexts.add(block5.getText());
		blockTexts.add(block6.getText());

		block1.setSelected(false);
		block2.setSelected(false);
		block3.setSelected(false);
		block4.setSelected(false);
		block5.setSelected(false);
		block6.setSelected(false);

		add(block1);
		add(block2);
		add(block3);

		add(block4);
		add(block5);
		add(block6);

		setPanelSize();
		addKeyListener(keyboardInputs);
//		setFocusable(true);
//	    requestFocus();     

//		SwingUtilities.invokeLater(() -> block1.requestFocusInWindow());

//		setFocusable(true);

		setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());

		block1.setFocused(true);

//		block1.requestFocusInWindow();
//		block1.requestFocus();

	}

	public void keyPressed(KeyEvent e) {

		int keyCode = e.getKeyCode();

		ArrayList<CodeBlock> codeBlocksCopy = new ArrayList<>(codeBlocks); // Create a copy of codeBlocks

		int currentIndex = -1; // Initialize with an invalid value

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

		if (selectedBlock != null) {
			switch (keyCode) {
			case KeyEvent.VK_UP:
				correctBlockIndex--;
				selectedBlock.moveBlockUp();
				break;

			case KeyEvent.VK_DOWN:
				correctBlockIndex++;
				selectedBlock.moveBlockDown();
				break;

			case KeyEvent.VK_ENTER:
				selectedBlock.moveFocusToNextBlock(selectedBlock);

				if (selectedBlock.checkAllLocked()) {

					System.out.println("Player start moving!");
					playerCanMove = true;

				}

//				selectedBlock.getOrderedBlockTexts();
				break;
			}
			selectedBlock.changeBlockColor(codeFinished);
		}
	}

	private void setPanelSize() {

		Dimension size = new Dimension(GAME_WIDTH / 3, GAME_HEIGHT);

		setPreferredSize(size);

		System.out.println("size: " + GAME_WIDTH / 3 + " | " + GAME_HEIGHT);

	}

	public Game getGame() {
		return game;
	}

	public KeyBoardInputs getKeyboardInputs() {
		return keyboardInputs;
	}

	public boolean isCodeFinished() {
		return codeFinished;
	}

//	public int getNextIndex() {
//		return nextIndex;
//	}
//
//	public int getIndexLocked() {
//		return indexLocked;
//	}

	public ArrayList<CodeBlock> getCodeBlocks() {
		return codeBlocks;
	}

	public ArrayList<CodeBlock> getCodeBlocksLocked() {
		return codeBlocksLocked;
	}

	public ArrayList<CodeBlock> getCodeBlocksCorrected() {
		return codeBlocksCorrected;
	}

	public int getCorrectBlockIndex() {
		return correctBlockIndex;
	}

	public ArrayList<String> getBlockTexts() {
		return blockTexts;
	}

}
