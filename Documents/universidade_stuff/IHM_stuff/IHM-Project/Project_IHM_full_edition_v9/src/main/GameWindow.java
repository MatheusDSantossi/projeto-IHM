package main;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import gamestates.Gamestate;
import utilz.LoadSave;

public class GameWindow {
	private JFrame jframe;

	private BufferedImage game_logo_img;

	private int availableWidth;

	private int totalWidth;

	// Calculate the X-coordinate to position the first panel (GamePanel)
	private int gamePanelX;

//	public GameWindow(GamePanel gamePanel) {
	public GameWindow(GamePanel gamePanel, CodePanel codePanel) {

		jframe = new JFrame();

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setTitle("Crabby Conquest: Mr. Piggy's Adventure");

		game_logo_img = gamePanel.getGame().loadSave.GetSpriteAtlas(LoadSave.GAME_LOGO_IMG);

		jframe.setIconImage(game_logo_img);

//		jframe.add(gamePanel);

		jframe.setLayout(new BorderLayout()); // Use BorderLayout for the JFrame

		// Add the panels to the CENTER position of the BorderLayout
		jframe.add(gamePanel, BorderLayout.EAST);
		jframe.add(codePanel, BorderLayout.CENTER);
//        jframe.add(gamePanel, BorderLayout.EAST);

//        icon.setLocation(100, 100);

		availableWidth = jframe.getWidth();

		totalWidth = Game.GAME_WIDTH + Game.CODE_PANEL_WIDTH;

		// Calculate the X-coordinate to position the first panel (GamePanel)
		gamePanelX = (availableWidth - totalWidth) / 2;

		jframe.setFocusable(true);

		jframe.setResizable(false);
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

		jframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				confirmExit(e, jframe);
			}
		});

	}

	public void confirmExit(WindowEvent e, JFrame jfFrame) {
		int choice = JOptionPane.showConfirmDialog(jfFrame, "Are you sure you want to quit?", "Exit Confirmation",
				JOptionPane.YES_NO_OPTION);

		if (choice == JOptionPane.YES_OPTION) {
			System.exit(0); // Close the application
		} else {

			jfFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		}
	}

	public void closeWindow() {
		int choice = JOptionPane.showConfirmDialog(this.getJframe(), "Are you sure you want to quit?",
				"Exit Confirmation", JOptionPane.YES_NO_OPTION);

		if (choice == JOptionPane.YES_OPTION) {
			System.exit(0); // Close the application
		} else {
			
			Gamestate.state = Gamestate.MENU;
			
		}
	}

	public JFrame getJframe() {
		return jframe;
	}

}
