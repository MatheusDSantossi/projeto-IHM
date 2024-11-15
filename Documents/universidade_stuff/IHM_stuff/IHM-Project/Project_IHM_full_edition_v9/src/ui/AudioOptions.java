package ui;

import static utilz.Constants.UI.PauseButtons.SOUND_SIZE;
import static utilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import gamestates.Gamestate;
import main.Game;

public class AudioOptions {

	private VolumeButton volumeButton;
	private SoundButton musicButton, sfxButton;

	private VolumeButton volumeButton_pauseOverlay;
	private SoundButton musicButton_pauseOverlay, sfxButton_pauseOverlay;
	
	private Game game;

	private int soundX;
	private int vX;
	
	private int soundX_pauseOverlay;
	private int vX_pauseOverlay;

	public AudioOptions(Game game) {
		this.game = game;
		createSoundButtons(game);
		createVolumeButton(game);
	}

	private void createVolumeButton(Game game) {
//		int vX = (int) (309 * Game.SCALE);
//		if (Gamestate.state == Gamestate.MENU)
		
		vX = (int) (((Game.GAME_WIDTH / 3) - 108) * Game.SCALE);
		
		vX_pauseOverlay = (int) (((Game.GAME_WIDTH / 2) - 108) * Game.SCALE);

		int vY = (int) (278 * Game.SCALE);
		volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT, game);
		
		volumeButton_pauseOverlay = new VolumeButton(vX_pauseOverlay, vY, SLIDER_WIDTH, VOLUME_HEIGHT, game);
		
		
	}

	private void createSoundButtons(Game game) {
//		soundX = (int) (450 * Game.SCALE);

//		if (Gamestate.state == Gamestate.MENU)
		soundX = (int) (((Game.GAME_WIDTH / 3) + 30) * Game.SCALE);
		soundX_pauseOverlay = (int) (((Game.GAME_WIDTH / 2) + 30) * Game.SCALE);

		int musicY = (int) (140 * Game.SCALE);
		int sfxY = (int) (186 * Game.SCALE);
		musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE, game);
		sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE, game);
		
		musicButton_pauseOverlay = new SoundButton(soundX_pauseOverlay, musicY, SOUND_SIZE, SOUND_SIZE, game);
		sfxButton_pauseOverlay = new SoundButton(soundX_pauseOverlay, sfxY, SOUND_SIZE, SOUND_SIZE, game);
	}

	public void update() {

//		if (Gamestate.state == Gamestate.PLAYING && game.getPlaying().isPaused()) {
//			soundX = (int) (450 * Game.SCALE);
//			vX = (int) (309 * Game.SCALE);
//			createSoundButtons();
//			createVolumeButton();
//
//		} else {
//
//			createSoundButtons();
//			createVolumeButton();
//
//		}

		// this is a trouble because my sounds not work whne i do this above
		
		musicButton.update();
		musicButton_pauseOverlay.update();
		
		sfxButton.update();
		sfxButton_pauseOverlay.update();

		volumeButton.update();
		volumeButton_pauseOverlay.update();
	}
	
	public void drawPauseOverlay(Graphics g) {
		// Sound buttons
		musicButton_pauseOverlay.draw(g);
		sfxButton_pauseOverlay.draw(g);

		// Volume Button
		volumeButton_pauseOverlay.draw(g);
	}

	public void draw(Graphics g) {
		// Sound buttons
		musicButton.draw(g);
		sfxButton.draw(g);

		// Volume Button
		volumeButton.draw(g);
	}

	public void mouseDragged(MouseEvent e) {
		if (volumeButton.isMousePressed()) {
			float valueBefore = volumeButton.getFloatValue();
			volumeButton.changeX(e.getX());
			volumeButton_pauseOverlay.changeX(e.getX());

			
			float valueAfter = volumeButton.getFloatValue();
			if (valueBefore != valueAfter)
				game.getAudioPlayer().setVolume(valueAfter);
		}
		
		if (volumeButton_pauseOverlay.isMousePressed()) {
			float valueBefore = volumeButton_pauseOverlay.getFloatValue();
			volumeButton_pauseOverlay.changeX(e.getX());
			volumeButton.changeX(e.getX());
			
			float valueAfter = volumeButton_pauseOverlay.getFloatValue();
			if (valueBefore != valueAfter)
				game.getAudioPlayer().setVolume(valueAfter);
		}
		
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(e, musicButton))
			musicButton.setMousePressed(true);
		else if (isIn(e, sfxButton))
			sfxButton.setMousePressed(true);
		else if (isIn(e, volumeButton))
			volumeButton.setMousePressed(true);
		
		
		if (isIn(e, musicButton_pauseOverlay))
			musicButton_pauseOverlay.setMousePressed(true);
		else if (isIn(e, sfxButton_pauseOverlay))
			sfxButton_pauseOverlay.setMousePressed(true);
		else if (isIn(e, volumeButton_pauseOverlay))
			volumeButton_pauseOverlay.setMousePressed(true);
		
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, musicButton)) {
			if (musicButton.isMousePressed()) {
				musicButton.setMuted(!musicButton.isMuted());
				game.getAudioPlayer().toggleSongMute();
			}

		} else if (isIn(e, sfxButton)) {
			if (sfxButton.isMousePressed()) {
				sfxButton.setMuted(!sfxButton.isMuted());
				game.getAudioPlayer().toggleEffectMute();
			}
		}

		musicButton.resetBools();
		sfxButton.resetBools();

		volumeButton.resetBools();
		
		
		if (isIn(e, musicButton_pauseOverlay)) {
			if (musicButton_pauseOverlay.isMousePressed()) {
				musicButton_pauseOverlay.setMuted(!musicButton_pauseOverlay.isMuted());
				game.getAudioPlayer().toggleSongMute();
			}

		} else if (isIn(e, sfxButton_pauseOverlay)) {
			if (sfxButton_pauseOverlay.isMousePressed()) {
				sfxButton_pauseOverlay.setMuted(!sfxButton_pauseOverlay.isMuted());
				game.getAudioPlayer().toggleEffectMute();
			}
		}

		musicButton_pauseOverlay.resetBools();
		sfxButton_pauseOverlay.resetBools();

		volumeButton_pauseOverlay.resetBools();
		
	}

	public void mouseMoved(MouseEvent e) {
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);

		volumeButton.setMouseOver(false);

		if (isIn(e, musicButton))
			musicButton.setMouseOver(true);
		else if (isIn(e, sfxButton))
			sfxButton.setMouseOver(true);
		else if (isIn(e, volumeButton))
			volumeButton.setMouseOver(true);
		
		
		musicButton_pauseOverlay.setMouseOver(false);
		sfxButton_pauseOverlay.setMouseOver(false);

		volumeButton_pauseOverlay.setMouseOver(false);

		if (isIn(e, musicButton_pauseOverlay))
			musicButton_pauseOverlay.setMouseOver(true);
		else if (isIn(e, sfxButton_pauseOverlay))
			sfxButton_pauseOverlay.setMouseOver(true);
		else if (isIn(e, volumeButton_pauseOverlay))
			volumeButton_pauseOverlay.setMouseOver(true);
		
	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

}
