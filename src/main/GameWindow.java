package main;

import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;
import javax.swing.JRadioButtonMenuItem;

public class GameWindow {

	private JFrame jframe;
	
	public GameWindow(GamePanel gamePanel, CodePanel codePanel) {
	
		jframe = new JFrame();
		
//		jframe.setSize(400, 400);
		
		 // Set the layout manager to FlowLayout
	    jframe.setLayout(new FlowLayout());
		
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		// adding to test
		jframe.add(codePanel);
		jframe.add(gamePanel);
		
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
		
	}
	
}
