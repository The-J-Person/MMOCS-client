package handlers;

import java.util.Stack;

import states.GameState;
import states.Play;
import MMOCS.game.MMOCSClient;

public class GameStateManager {
	
	private MMOCSClient game;
	private Stack<GameState> gameStates;
	public static final int LOG = 3232032;
	public static final int PLAY = 1210422;
	
	public GameStateManager(MMOCSClient game){
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(PLAY);		
	}
	
	public MMOCSClient getGame(){ return game;}
	
	public void update(float dt){
		gameStates.peek().update(dt);
	}
	
	public void render(){
		gameStates.peek().render();
	}
	
	private GameState getState(int state){
			if(state == PLAY) return new Play(this);
			return null;
	}
	
	public void setState(int state){
		popState();
		pushState(state);	
	}
	
	public void pushState(int state){
		gameStates.push(getState(state));
	}
	public void popState(){
		GameState st = gameStates.pop();
		st.dispose();
	}
}
