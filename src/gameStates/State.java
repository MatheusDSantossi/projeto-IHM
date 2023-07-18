package gameStates;

import java.awt.event.MouseEvent;

import audio.AudioPlayer;
import main.Game;
import ui.MenuButton;

public class State {
	
	protected Game game;
	
	public State(Game game) {
	
		this.game = game;
		
	}
	
	/*
	 * 36:42
	 */
	
	public boolean isIn(MouseEvent e, MenuButton mb) { 
		
		return mb.getBounds().contains(e.getX(), e.getY());
		
	}
	
	public Game getGame() {
		
		return game;
		
	}
	
	public void setGameState(GameState state) {
		
		switch(state) {
		
		case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
		case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
		
		}
		
		GameState.state = state;
		
	}

}
