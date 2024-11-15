package gamestates;

import static main.Game.CODE_PANEL_WIDTH;
import static main.Game.GAME_HEIGHT;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

public class TutorialCodePanel extends State implements Statemethods {

	private BufferedImage tutorialImage1, tutorialImage2;

	public TutorialCodePanel(Game game) {
		super(game);

		loadBackground();

	}

	private void loadBackground() {

		tutorialImage1 = game.loadSave.GetTutorialImages(LoadSave.TUTORIAL_IMAGE_1_CODE_PART);
		tutorialImage2 = game.loadSave.GetTutorialImages(LoadSave.TUTORIAL_IMAGE_1_CODE_PART_RECT);

	}

	private void loadButtons() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {

		game.getCodePanel().resetDraw();

	}

	public void drawRectTutorial(Graphics g, int x, int y) {

		g.drawRect(x, y, x, y);

	}

	@Override
	public void draw(Graphics g) {

		if (game.getTutorial().getTutorialImageNumber() < 8)
			g.drawImage(tutorialImage1, 0, 0, Game.CODE_PANEL_WIDTH, Game.GAME_HEIGHT, null);

//		if (game.getTutorial().isFirstRectTutorial()) {
//
//			g.setColor(Color.RED);
//
//			g.drawRect(0, 293, 380, 80);
//		}
//
//		if (game.getTutorial().isSecondRectTutorial()) {
//
//			g.setColor(Color.RED);
//
//			g.drawRect(0, 150, 380, 80);
//		}
//
//		if (game.getTutorial().isThirdRectTutorial()) {
//
//			g.setColor(Color.RED);
//
//			g.drawRect(0, 0, 380, 80);
//		}

		if (game.getTutorial().isFirstRectTutorial()) {
			drawHighlightedArea(g, new Point(0, 293), 380, 80);
		}

		if (game.getTutorial().isSecondRectTutorial()) {
			drawHighlightedArea(g, new Point(0, 150), 380, 80);
		}

		if (game.getTutorial().isThirdRectTutorial()) {
			drawHighlightedArea(g, new Point(0, 0), 380, 80);
		}

		if (game.getTutorial().getTutorialImageNumber() >= 8) {

			g.drawImage(tutorialImage2, 0, 0, Game.CODE_PANEL_WIDTH, Game.GAME_HEIGHT, null);

		}

	}

	// Draws a highlighted area with an arrow pointing to it
	public static void drawHighlightedArea(Graphics g, Point location, int width, int height) {
		Graphics2D g2d = (Graphics2D) g;

		// Draw the highlighted area
		g2d.setColor(new Color(255, 0, 0, 100)); // Red color with transparency
		g2d.fillRect(location.x, location.y, width, height);

		// Draw an arrow pointing to the highlighted area
		int arrowX = location.x + width / 2;
		int arrowY = location.y + height;
		int[] arrowXPoints = { arrowX, arrowX - 10, arrowX + 10 };
		int[] arrowYPoints = { arrowY, arrowY + 20, arrowY + 20 };
		Polygon arrowPolygon = new Polygon(arrowXPoints, arrowYPoints, 3);
		g2d.setColor(Color.RED);
		g2d.fill(arrowPolygon);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
