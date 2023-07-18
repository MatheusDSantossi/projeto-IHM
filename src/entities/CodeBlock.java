package entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import gameStates.Playing;
import main.CodePanel;

public class CodeBlock extends JLabel {

	private Rectangle blockHitbox;
	private int initialX;
	private int initialY;
	private Point initialPosition;
	private boolean locked = false;
	private boolean selected = false;

	private boolean focused = false;

	private Playing playing;

	private CodePanel codePanel;

	public CodeBlock(String code, boolean codeFinished, CodePanel codePanel) {
		super(code);

		this.codePanel = codePanel;

		changeBlockColor(codeFinished);

		setFocusable(true);

	}

	public void changeBlockColor(boolean codeFinished) {

		if (!selected || codeFinished) {

			paintMethod();
			return;
		}

		setOpaque(true);
		setBackground(Color.yellow);
		setFont(new Font("Arial", Font.PLAIN, 15));
		setPreferredSize(new Dimension(150, 40));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setHorizontalAlignment(SwingConstants.CENTER);
		return;

	}

	private void paintMethod() {

		setOpaque(true);
		setBackground(Color.LIGHT_GRAY);
		setPreferredSize(new Dimension(150, 40));
		setFont(new Font("Arial", Font.PLAIN, 15));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setHorizontalAlignment(SwingConstants.CENTER);

	}

	public void initHitbox(int x, int y, int width, int height) {
		blockHitbox = new Rectangle(x, y, width, height);
		initialX = x;
		initialY = y;
	}

	public boolean isAtInitialPosition() {
		return getX() == initialX && getY() == initialY;
	}

	public void setInitialPosition(int x, int y) {
		initialPosition = new Point(x, y);
		setLocation(initialPosition);
	}

	public void resetPosition() {
		setLocation(initialPosition);
		blockHitbox.setLocation(initialPosition);
	}

	public ArrayList<String> getOrderedBlockTexts() {
		ArrayList<String> orderedTexts = new ArrayList<>();
		for (CodeBlock block : codePanel.getCodeBlocks()) {
			orderedTexts.add(block.getText());
		}
		return orderedTexts;
	}

//	private void verifyCode(CodeBlock selectedBlock) {
	private void verifyCode(ArrayList<String> correctedOrder) {

//		dict.put(correctBlockIndex, selectedBlock.getText());

		for (String codeBlockOrder : correctedOrder) {
//		for(int i = 0; i < correctedOrder.size(); i++) {
			System.out.println(codeBlockOrder);
			switch (codeBlockOrder) {
			case "MrPigy.moveToClosestThree();":

				System.out.println("Moving to closest Three");
				break;
			case "MrPigy.jump();":

				System.out.println("Jumping!!");
				break;
			case "MrPigy.attack();":

				System.out.println("Attacking");
				break;
			default:
				System.out.println("another move");
			}

		}

	}

//	public void checkAllLocked() {
	public boolean checkAllLocked() {

		for (CodeBlock codeBlock : codePanel.getCodeBlocks()) {

			if (codeBlock.isLocked() && !codePanel.getCodeBlocksLocked().contains(codeBlock)) {
				codePanel.getCodeBlocksLocked().add(codeBlock);
				codePanel.indexLocked++;

			}

		}

//		if(codePanel.getCodeBlocksLocked().size() == codePanel.getCodeBlocks().size()) {
//			
//			return true;
//			
//		})

		if (codePanel.indexLocked == codePanel.getCodeBlocks().size()) {

			return true;

		}

		return false;

	}

	public void moveBlockUp() {
		int currentIndex = codePanel.getCodeBlocks().indexOf(this);
		if (currentIndex > 0) {
			CodeBlock aboveBlock = codePanel.getCodeBlocks().get(currentIndex - 1);
			codePanel.getCodeBlocks().set(currentIndex - 1, this);
			codePanel.getCodeBlocks().set(currentIndex, aboveBlock);

			codePanel.remove(this);
			codePanel.add(this, currentIndex - 1);

			codePanel.revalidate();
			codePanel.repaint();

			// Update codePanel.getBlockTexts() array
			String blockText = getText();
			codePanel.getBlockTexts().set(currentIndex - 1, blockText);
			codePanel.getBlockTexts().set(currentIndex, aboveBlock.getText());
		}
	}

	public void moveBlockDown() {
		int currentIndex = codePanel.getCodeBlocks().indexOf(this);
		if (currentIndex < codePanel.getCodeBlocks().size() - 1) {
			CodeBlock belowBlock = codePanel.getCodeBlocks().get(currentIndex + 1);
			codePanel.getCodeBlocks().set(currentIndex + 1, this);
			codePanel.getCodeBlocks().set(currentIndex, belowBlock);

			codePanel.remove(this);
			codePanel.add(this, currentIndex + 1);

			codePanel.revalidate();
			codePanel.repaint();

			// Update codePanel.getBlockTexts() array
			String blockText = getText();
			codePanel.getBlockTexts().set(currentIndex + 1, blockText);
			codePanel.getBlockTexts().set(currentIndex, belowBlock.getText());
		}
	}

	public void moveFocusToNextBlock(CodeBlock block) {
		int currentIndex = codePanel.getCodeBlocks().indexOf(block);

		// Find the next available, unlocked block
		int nextIndex = (currentIndex + 1) % codePanel.getCodeBlocks().size();
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
//	        codePanel.getCodeBlocks().get(nextIndex).isFocusable();
//	        codePanel.getCodeBlocks().get(nextIndex).hasFocus();

		} else {
			// Wrap around to the first block
			block.setSelected(false);
			block.setLocked(true);
			changeBlockColor(codePanel.codeFinished);

			codePanel.getCodeBlocks().get(0).setLocked(false);
			codePanel.getCodeBlocks().get(0).requestFocus();
		}

		if (nextIndex == currentIndex) {
			resetCodeBlock();
			codePanel.codeFinished = true;

			System.out.println(getOrderedBlockTexts());

//	        player

			verifyCode(codePanel.getBlockTexts());
		}

		codePanel.getBlockTexts().set(currentIndex, block.getText());
	}

	private void resetCodeBlock() {

		for (CodeBlock codes : codePanel.getCodeBlocks()) {

			codes.resetBlock();

		}

	}

	public boolean isCollision(CodeBlock other) {
		return blockHitbox.intersects(other.blockHitbox);
	}

	public void resetPosition(int initialX, int initialY) {
		setLocation(initialX, initialY);
		blockHitbox.setLocation(initialX, initialY);
	}

	public Rectangle getBlockHitbox() {
		return blockHitbox;
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

	public void resetBlock() {

		paintMethod();

	}

	public boolean isFocused() {
		return focused;
	}

	public void setFocused(boolean focused) {
		this.focused = focused;
	}

}
