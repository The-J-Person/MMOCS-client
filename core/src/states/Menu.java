package states;

import handlers.GameStateManager;
import handlers.MyDialog;

import java.util.Hashtable;
import java.util.LinkedList;

import network.Connection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import common.Acknowledgement;
import common.Coordinate;
import common.Request;
import common.RequestType;
import common.Resource;
import common.Update;

import entities.Player;

public class Menu extends GameState {

	private Stage stage;
	private TextField userNameField;
	private TextField passField;
	private TextureAtlas textAtlas;
	private Connection con;
	private Window waitingDialog;
	private Label waitLabel;
	private boolean loading;
	private Player player;
	
	public Menu(GameStateManager gsm){
		super(gsm);
		player = gsm.getGame().getPlayer();
		loading = false;
		con = gsm.getGame().getCon();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		fillStage(stage);
	}
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float dt) {		
		if(con.isProcessing() == true || loading){
			stage.getActors().get(1).setVisible(true);
			stage.getActors().get(0).setTouchable(Touchable.disabled);
			stage.setKeyboardFocus(null);
		}
		else{
			stage.getActors().get(1).setVisible(false);
			stage.getActors().get(0).setTouchable(Touchable.enabled);
		}
		handleUpdates();
		if(isLoaded()){
			gsm.pushState(GameStateManager.PLAY);
		}

	}

	@Override
	public void render() {
		Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void dispose() {
		textAtlas.dispose();

	}
	
	private void fillStage(Stage stage){	
		TextButton button;
		Label label;
		Table subTable;
		Table table = new Table();
		BitmapFont font = new BitmapFont();
		Skin skin = new Skin();
		textAtlas = new TextureAtlas(Gdx.files.internal("Initial.pack"));
		skin.addRegions(textAtlas);
		//defining
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = font;
		labelStyle.fontColor = Color.WHITE;	
		
		TextFieldStyle textFieldStyle = new TextFieldStyle();
		textFieldStyle.font = font;
		textFieldStyle.fontColor = Color.BLACK;
		textFieldStyle.background = skin.getDrawable("White");
		
		TextButtonStyle textButtonStyle = new TextButtonStyle(); 
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("Button-up");
        textButtonStyle.down = skin.getDrawable("Button-down");
        
        //creating
        //username label and field
        subTable = new Table();
        label = new Label("User Name:", labelStyle);
		subTable.add(label).space(10);
        userNameField = new TextField("", textFieldStyle);
        userNameField.setMaxLength(20);
        subTable.add(userNameField).height(30).width(120).space(10);
        
        subTable.row();
        //password label and field
        label = new Label("Password:", labelStyle);
		subTable.add(label).space(10);
        passField = new TextField("", textFieldStyle);
        passField.setPasswordMode(true);
        passField.setPasswordCharacter('*');
        passField.setMaxLength(20);
        subTable.add(passField).height(30).width(120).space(10);
        table.add(subTable).space(10);
        
        //login button
        button = new TextButton("Log in", textButtonStyle);
        button.addListener(new ClickListener() {
        	public void clicked(InputEvent event,float x,float y){
        		logIn();
        	}
            
        });
        table.add(button).height(80).width(120).space(10);
        
        table.row();
        
        //register
        button = new TextButton("Register", textButtonStyle);
        button.addListener(new ClickListener() {
            
        	public void clicked(InputEvent event,float x,float y){
        		register();
        	}
            
        });
        table.add(button).height(80).space(10).colspan(2).fillX();
        
        table.row();
        
        //confirm
        button = new TextButton("Confirm account", textButtonStyle);
        button.addListener(new ClickListener() {
            
        	public void clicked(InputEvent event,float x,float y){
        		confirm();
        	}
            
        });
        table.add(button).height(80).space(10).colspan(2).fillX();
        
        table.row();
        
        //exit
        button = new TextButton("Exit", textButtonStyle);
        button.addListener(new ClickListener() {
            
        	public void clicked(InputEvent event,float x,float y){
        		exitGame();
        	}
            
        });
        table.add(button).height(80).space(10).colspan(2).fillX();;
        
        table.setFillParent(true);
        
        table.setBackground(skin.getDrawable("Background"));
        stage.addActor(table);	
        
        table = new Table();
        labelStyle = new LabelStyle();
        
        labelStyle.font = font;
		labelStyle.fontColor = Color.BLACK;
		labelStyle.background = skin.getDrawable("White");   
		waitLabel = new Label("Waiting for server...", labelStyle); 
        table.add(waitLabel);
        table.bottom().left();
        table.setVisible(false);
        stage.addActor(table);
	}
	
	private void logIn(){
		if(con.connect()){
			//may need to encrypt the password
			String[] args = new String[2];
			args[0] =  userNameField.getText();
			args[1] = passField.getText();
			con.getRequestSender().sendRequest(new Request(RequestType.LOG_IN, args));
			
		}
		else {
			MyDialog dia = new MyDialog("Error","Couldn't connect to the server.");
			dia.show(stage);		
		} 
	}
	
	private void register(){

		gsm.pushState(GameStateManager.REGISTER);
	}
	private void confirm(){

		gsm.pushState(GameStateManager.CONFIRM);
	}
	
	private void exitGame(){
		con.close();
		Gdx.app.exit();
	}

	public Stage getInputProcessor(){
		return stage;
	}
	
	private void handleUpdates(){
		Update up;
		LinkedList<Update> updates = con.getUpdates();
		synchronized(updates){
			if(!updates.isEmpty())
				up = updates.removeFirst();
			else return;
		}
		switch (up.getType()){
		case ACKNOWLEDGMENT: 
			Acknowledgement ack = (Acknowledgement)up.getData();
			con.setProcessing(false);
			if(ack.getRequestType() == RequestType.LOG_IN){
				if(ack.getAck()){
					loading = true;
					waitLabel.setText("Loading...");
				}
				else{
					MyDialog dia = new MyDialog("Failure" , "Please check the details you entered are correct");
					dia.show(stage);
				}
			}
			break;
		case HIT_POINTS:
			Integer hp = (Integer)up.getData();
			player.setCurrentHp(hp);
			break;
		case INVENTORY:
			Hashtable<Resource,Integer> inven = (Hashtable<Resource,Integer>) up.getData();
			player.setInventory(inven);
			break;
		case COORDINATE: 
			Coordinate center = (Coordinate) up.getData();
			player.setLocation(center);
			break;
		default: break;
		}
	}
	
	private boolean isLoading(){
		return loading;
	}
	
	private boolean isLoaded(){
		return player.isInitialized();
	}
	
	

	
	
}
