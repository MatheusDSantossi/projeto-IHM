package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Timer;

import entities.CodeBlock;

public class CodeBlockController extends MouseAdapter{
	
	private CodeBlock codeBlock;

	private int clickCount = 1;
	private Timer clickTimer;
	private boolean animatingClick = false;
	private int animationFrame = 0;
	private final int totalFrames = 10;

	private int max = 9;
	
	public CodeBlockController(CodeBlock codeBlock) {
	
		this.codeBlock = codeBlock;
		
		if(codeBlock.getText().contains("attack")) {
			
			max = 5;
			
			
		} else if(codeBlock.getText().equals("MrPigy.jump();") || codeBlock.getText().equals("MrPigy.specialAttack();")) {
			
			max = 2;
			
		} else if (codeBlock.getText().contains("Turn") || codeBlock.getText().equals("MrPigy.jumpAndAttack();")) {
			
			max = 1;
			
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	
		super.mouseClicked(e);
		
		clickTimer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Perform animation logic here
				if (animationFrame < totalFrames) {
					animatingClick = true;
					// Toggle the appearance of the CodeBlock for the animation effect
					codeBlock.setVisible(animationFrame % 2 == 0);
					animationFrame++;
				} else {
					animatingClick = false;
					codeBlock.setVisible(true); // Show the CodeBlock again after the animation is complete
					clickTimer.stop(); // Stop the timer when animation is done
				}
			}
		});
		
		if (!animatingClick && codeBlock.isSelected() && clickCount > 0) {
	        if (e.getButton() == MouseEvent.BUTTON1 && clickCount < max) {
	            clickCount++;
	        } else if (e.getButton() == MouseEvent.BUTTON3 && clickCount > 1) {
	            clickCount--;
	        } else if (e.getButton() == MouseEvent.BUTTON3 && clickCount == 1) {
	            return; // Don't do anything when click count is already 1 and right-clicked
	        }
			
			updateLabelText(codeBlock);
			
			
			
			animationFrame = 0; // Reset animationFrame before starting the timer
			clickTimer.start(); // Start the timer when the CodeBlock is clicked
			
//			updateBlockText();
		}
		
	}
	
	private void updateBlockText() {
		
		int currentIndex = codeBlock.getCodePanel().getCodeBlocks().indexOf(this);
		String newText = codeBlock.getText();
		codeBlock.getCodePanel().getBlockTexts().set(currentIndex, newText);
		
	}
	
//	private void updateLabelText(String code) {
//		// Update the JLabel's text with the new clickCount
//		codeBlock.setText(clickCount + "x " + code);
//	}

	public void updateLabelText(CodeBlock codeBlock) {
		// Update the JLabel's text with the new clickCount
		codeBlock.setText(clickCount + "x " + codeBlock.getCode());
	}
	
	public int getClickCount() {
		return clickCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public CodeBlock getCodeBlock() {
		return codeBlock;
	}

	
	
	
}
