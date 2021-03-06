package handlers;

import java.util.Stack;

import states.Confirm;
import states.GameState;
import states.Initial;
import states.Menu;
import states.Play;
import states.Register;
import MMOCS.game.MMOCSClient;

import com.badlogic.gdx.Gdx;

public class GameStateManager {
	
	private MMOCSClient game;
	private Stack<GameState> gameStates;
	public static final int MENU = 3232032;
	public static final int PLAY = 1210422;
	public static final int INITIAL = 23112;
	public static final int REGISTER = 56577;
	public static final int CONFIRM = 432232;
	
	public GameStateManager(MMOCSClient game){
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(INITIAL);		
	}
	
	public MMOCSClient getGame(){ return game;}
	
	public void update(float dt){
		gameStates.peek().update(dt);
	}
	
	public void render(){
		gameStates.peek().render();
	}
	
	private GameState getState(int state){
		switch(state){
		case PLAY: return new Play(this); 
		case INITIAL: return new Initial(this);
		case MENU: return new Menu(this);
		case REGISTER: return new Register(this);
		case CONFIRM: return new Confirm(this);
		default: break;
		}
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
		if(!gameStates.empty())
			Gdx.input.setInputProcessor(gameStates.peek().getInputProcessor());
	}
	
	public GameState peek(){
		return gameStates.peek();
	}
}
