package entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import controllers.CodeBlockController;
import gamestates.Playing;
import main.CodePanel;
import utilz.Constants;
import utilz.LoadSave;

public class CodeBlock extends JLabel {

	private int initialX;
	private int initialY;
	private Point initialPosition;
	private boolean locked = false;
	private boolean selected = false;

	private boolean focused = false;

	private String code;

	private CodePanel codePanel;

	private CodeBlockController codeBlockController;

	private int currentIndex;

	// Find the next available, unlocked block
	private int nextIndex;

//	private ImageIcon i = new ImageIcon(LoadSave.GetSpriteAtlas(LoadSave.CODE_BLOCK_BACKGROUND));

//	public CodeBlock(String code, boolean codeFinished, CodePanel codePanel) {
////	public CodeBlock(String code, ImageIcon i, boolean codeFinished, CodePanel codePanel) {
//		super(code);
//
//		this.code = code;
//		
//		this.initialX = getX();
//		this.initialY = getY();
//
//		setInitialPosition(initialX, initialY);
//
//		this.codePanel = codePanel;
//		
//		codeBlockController = new CodeBlockController(this);
//		
//		addMouseListener(codeBlockController);
//
//		changeBlockColor(codeFinished);
//
//		setFocusable(true);
//
//	}

	public CodeBlock(String code, boolean codeFinished, CodePanel codePanel) {
		// Initialize the action

		super(code);

		this.code = code;

		this.initialX = getX();
		this.initialY = getY();

		setInitialPosition(initialX, initialY);

		this.codePanel = codePanel;

		codeBlockController = new CodeBlockController(this);

		addMouseListener(codeBlockController);

		changeBlockColor(codeFinished);

		setFocusable(true);

//		this.action = action;

		// Other constructor code
	}

	public String getAction() {
		return code;
	}

	// Add a method to execute the action
	public void executeAction(String code) {

		codePanel.codeFinished = true;

//		String number = codePanel.getGame().getPlaying().moveMethod_substring(code, false);

//		int times = Integer.parseInt(number);

//		if(!code.equals("MrPigy.attack();") && !code.equals("MrPigy.specialAttack();"))
//		
//			for(int i = 0; i < times; i++) 

		codePanel.getGame().getPlaying().move(code);
//		else
//			codePanel.getGame().getPlaying().move(code);

		// Execute the corresponding action based on the action string
		// For example:
//		if (code.equals("MrPigy.moveToClosestThree();")) {
//			// Move the player character
//			codePanel.getGame().getPlaying().move("MrPigy.moveToClosestThree();");
//		} else if (code.equals("MrPigy.jump();")) {
//			// Make the player character jump
//			codePanel.getGame().getPlaying().move("MrPigy.jump();");
//		}
		// Add more actions as needed

	}

	public void changeBlockColor(boolean codeFinished) {

		if (!selected || codeFinished) {

			paintMethod();
			return;
		}

		setOpaque(true);
//		setBackground(Color.yellow);
		setBackground(new Color(99, 157, 109));
//		setForeground(Color.BLACK);
		setForeground(Color.WHITE);
		setFont(new Font("Arial", Font.BOLD, 15));
		setPreferredSize(new Dimension(150, 40));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setHorizontalAlignment(SwingConstants.CENTER);
		return;

	}

	private void paintMethod() {

		setOpaque(false);
		setBackground(Color.LIGHT_GRAY);
		setForeground(Color.WHITE);
		setPreferredSize(new Dimension(150, 40));
		setFont(new Font("Arial", Font.BOLD, 15));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setHorizontalAlignment(SwingConstants.CENTER);

	}

	public boolean isAtInitialPosition() {
		return getX() == initialX && getY() == initialY;
	}

	public void setInitialPosition(int x, int y) {
		initialPosition = new Point(x, y);
		setLocation(initialPosition);
	}

	public ArrayList<String> getOrderedBlockTexts() {
		ArrayList<String> orderedTexts = new ArrayList<>();
		for (CodeBlock block : codePanel.getCodeBlocks()) {
			orderedTexts.add(block.getText());
		}
		return orderedTexts;
	}

//	private void verifyCode(CodeBlock selectedBlock) {

//	public void checkAllLocked() {
	public boolean checkAllLocked() {

		for (CodeBlock codeBlock : codePanel.getCodeBlocks()) {

			if (codeBlock.isLocked() && !codePanel.getCodeBlocksLocked().contains(codeBlock)) {
				codePanel.getCodeBlocksLocked().add(codeBlock);
				codePanel.indexLocked++;

			}

		}

		if (codePanel.indexLocked == codePanel.getCodeBlocks().size()) {

			return true;

		}

		return false;

	}

	public void moveBlockUp(CodeBlock selectedBlock) {
		int currentIndex = codePanel.getCodeBlocks().indexOf(selectedBlock);
		if (currentIndex > 0) {
			selectedBlock.setSelected(false);
			selectedBlock.changeBlockColor(codePanel.codeFinished);
			selectedBlock = codePanel.getCodeBlocks().get(currentIndex - 1);
			selectedBlock.setSelected(true);
			selectedBlock.changeBlockColor(codePanel.codeFinished);
		}
	}

	public void moveBlockDown(CodeBlock selectedBlock) {
		int currentIndex = codePanel.getCodeBlocks().indexOf(selectedBlock);
		if (currentIndex < codePanel.getCodeBlocks().size() - 1) {
			selectedBlock.setSelected(false);
			selectedBlock.changeBlockColor(codePanel.codeFinished);
			selectedBlock = codePanel.getCodeBlocks().get(currentIndex + 1);
			selectedBlock.setSelected(true);
			selectedBlock.changeBlockColor(codePanel.codeFinished);
		}
	}

//	public void moveBlockUp() {
//		int currentIndex = codePanel.getCodeBlocks().indexOf(this);
//		if (currentIndex > 0) {
//			CodeBlock aboveBlock = codePanel.getCodeBlocks().get(currentIndex - 1);
//			codePanel.getCodeBlocks().set(currentIndex - 1, this);
//			codePanel.getCodeBlocks().set(currentIndex, aboveBlock);
//
//			codePanel.remove(this);
//			codePanel.add(this, currentIndex - 1);
//
//			codePanel.revalidate();
//			codePanel.repaint();
//
//			// Update codePanel.getBlockTexts() array
//			String blockText = getText();
//			codePanel.getBlockTexts().set(currentIndex - 1, blockText);
//			codePanel.getBlockTexts().set(currentIndex, aboveBlock.getText());
//		}
//	}
//
//	public void moveBlockDown() {
//		int currentIndex = codePanel.getCodeBlocks().indexOf(this);
//		if (currentIndex < codePanel.getCodeBlocks().size() - 1) {
//			CodeBlock belowBlock = codePanel.getCodeBlocks().get(currentIndex + 1);
//			codePanel.getCodeBlocks().set(currentIndex + 1, this);
//			codePanel.getCodeBlocks().set(currentIndex, belowBlock);
//
//			codePanel.remove(this);
//			codePanel.add(this, currentIndex + 1);
//
//			codePanel.revalidate();
//			codePanel.repaint();
//
//			// Update codePanel.getBlockTexts() array
//			String blockText = getText();
//			codePanel.getBlockTexts().set(currentIndex + 1, blockText);
//			codePanel.getBlockTexts().set(currentIndex, belowBlock.getText());
//		}
//	}

	public void moveFocusToNextBlock(CodeBlock block) {
//		int currentIndex = codePanel.getCodeBlocks().indexOf(block);
//
//		// Find the next available, unlocked block
//		int nextIndex = (currentIndex + 1) % codePanel.getCodeBlocks().size();

		currentIndex = codePanel.getCodeBlocks().indexOf(block);

		nextIndex = (currentIndex + 1) % codePanel.getCodeBlocks().size();

//		game.get
		
		while (nextIndex != currentIndex && codePanel.getCodeBlocks().get(nextIndex).isLocked()) {
			nextIndex = (nextIndex + 1) % codePanel.getCodeBlocks().size();
		}

		// If a valid next block is found, set the focus to it
		if (nextIndex != currentIndex) {
			block.setSelected(false);
			block.setLocked(true);
			changeBlockColor(codePanel.codeFinished);

			codePanel.getCodeBlocks().get(nextIndex).setLocked(false);
			codePanel.getCodeBlocks().get(nextIndex).setFocused(true);
			block.setFocused(false);

			// Request focus in order to activate the focus transfer
			codePanel.requestFocus();

			// Check if the block actually has the focus
			if (codePanel.getCodeBlocks().get(nextIndex).isFocusOwner()) {
				System.out.println("Focus transferred successfully!");
			} else {
				System.out.println("Focus transfer failed!");
			}

		} else {
			// Wrap around to the first block
			block.setSelected(false);
			block.setLocked(true);
			changeBlockColor(codePanel.codeFinished);

			codePanel.getCodeBlocks().get(0).setLocked(false);
			codePanel.getCodeBlocks().get(0).requestFocus();
		}

//		verifyOneCodeBlock(block.getText());

//		if (nextIndex == currentIndex) {
//
//			codePanel.codeFinished = true;
//
//			System.out.println(getOrderedBlockTexts());
//
//			verifyCode(codePanel.getBlockTexts());
//		}

		codePanel.getBlockTexts().set(currentIndex, block.getText());
	}

	public void verifyOneCodeBlock(String text) {

		codePanel.getGame().getPlaying().move(text);

	}

//	private void verifyCode(ArrayList<String> correctedOrder) {
//
////		codePanel.getGame().getPlaying().getPlayer().move(correctedOrder);
//		codePanel.getGame().getPlaying().move(correctedOrder);
//
//	}

	private void resetCodeBlock() {

		for (CodeBlock codes : codePanel.getCodeBlocks()) {

			codes.resetBlock();

		}

	}

	public void resetBlock() {

//		codePanel.resetAll();

		codeBlockController.setClickCount(1);
		// Reset the block position to the initial position
		// For example:
//		resetPosition();

		for (CodeBlock codeBlock : codePanel.getCodeBlocksFirst()) {
			codePanel.remove(codeBlock);

			codeBlockController.updateLabelText(codeBlock);

			codeBlock.setLocked(false);
		}

//		codePanel.revalidate();
//		codePanel.repaint();

		for (CodeBlock codeBlock : codePanel.getCodeBlocksFirst()) {
			codePanel.add(codeBlock);
		}

		codePanel.setCodeBlocks(codePanel.getCodeBlocksFirst());

//		codePanel.revalidate();
//		codePanel.repaint();
//		
//		codePanel.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
//        codePanel.getCodeBlocks().get(0).setFocused(true); // Set the focus on the first block
//		
//        codePanel.setFocusable(true);
//        codePanel.requestFocus();

		resetColorBlock();
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void resetColorBlock() {

		paintMethod();

	}

	public boolean isFocused() {
		return focused;
	}

	public void setFocused(boolean focused) {
		this.focused = focused;
	}

	public CodePanel getCodePanel() {
		return codePanel;
	}

	public String getCode() {
		return code;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public int getNextIndex() {
		return nextIndex;
	}

}
