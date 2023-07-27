package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButtonMenuItem;

import utilz.LoadSave;

public class GameWindow {

	private JFrame jframe;
//	private CodePanel codePanel;
	

	private int availableWidth;

	private int totalWidth;

	// Calculate the X-coordinate to position the first panel (GamePanel)
	private int gamePanelX;

	// Calculate the X-coordinate to position the second panel (CodePanel)
	private int codePanelX;
	


	public GameWindow(GamePanel gamePanel, CodePanel codePanel) {
//	public GameWindow(GamePanel gamePanel) {

		jframe = new JFrame();

//		jframe.setSize(400, 400);

		
//		codePanel.add(label1);
//		gamePanel.add(label2);
		
		// Set the layout manager to FlowLayout
//		jframe.setLayout(new FlowLayout());
		jframe.setLayout(new BorderLayout()); // Use BorderLayout for the JFrame

        // Add the panels to the CENTER position of the BorderLayout
        jframe.add(gamePanel, BorderLayout.EAST);
        jframe.add(codePanel, BorderLayout.CENTER);
        
        availableWidth = jframe.getWidth();

		totalWidth = Game.GAME_WIDTH + Game.CODE_PANEL_WIDTH;

		// Calculate the X-coordinate to position the first panel (GamePanel)
		gamePanelX = (availableWidth - totalWidth) / 2;

		// Calculate the X-coordinate to position the second panel (CodePanel)
		codePanelX = gamePanelX + Game.GAME_WIDTH;

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//		this.codePanel = codePanel;

		// adding to test
//		jframe.add(codePanel);
//		jframe.add(gamePanel);
		
		gamePanel.getGame().getMenu().setBackgroundMiddle(gamePanelX);

		jframe.setFocusable(true);

		jframe.setResizable(false);

		jframe.setBackground(Color.red);
		
		jframe.pack();

		jframe.setLocationRelativeTo(null);

	
		jframe.setVisible(true);

		jframe.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {

				gamePanel.getGame().windowFocusLost();

			}

			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	public JFrame getJframe() {
		return jframe;
	}

	
}
