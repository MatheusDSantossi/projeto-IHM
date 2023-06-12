package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;
import javax.swing.JRadioButtonMenuItem;

public class GameWindow {

	private JFrame jframe;
	
	public GameWindow(GamePanel gamePanel) {
	
		jframe = new JFrame();
		
//		jframe.setSize(400, 400);
		
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
