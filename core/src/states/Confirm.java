package states;

import handlers.GameStateManager;

import java.util.LinkedList;

import utility.MyDialog;
import utility.Utility;

import network.Connection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import common.Acknowledgement;
import common.Request;
import common.RequestType;
import common.Update;
import common.UpdateType;

public class Confirm extends GameState {
	
	private Stage stage;
	private TextField userNameField;
	private TextField codeField;
	private TextureAtlas textAtlas;
	private Connection con;
	
	public Confirm(GameStateManager gsm){
		super(gsm);
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
		if(con.isProcessing() == true){
			stage.getActors().get(1).setVisible(true);
			stage.getActors().get(0).setTouchable(Touchable.disabled);
			stage.setKeyboardFocus(null);
		}
		else{
			stage.getActors().get(1).setVisible(false);
			stage.getActors().get(0).setTouchable(Touchable.enabled);
		}
		handleUpdates();

	}

	@Override
	public void render() {
		stage.draw();

	}

	@Override
	public void dispose() {
		textAtlas.dispose();

	}
	
	private void fillStage(Stage stage){
		TextButton button;
		Label label;
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
        label = new Label("User Name:", labelStyle);
		table.add(label).space(10).uniform();
        userNameField = new TextField("", textFieldStyle);
        userNameField.setMaxLength(20);
        table.add(userNameField).height(30).width(250).space(10).align(Align.left);  
        
        //code label and field
        table.row();
        label = new Label("Code:", labelStyle);
		table.add(label).space(10).uniform();
        codeField = new TextField("", textFieldStyle);
        codeField.setMaxLength(50);
        table.add(codeField).height(30).width(250).space(10).align(Align.left);
        
        //register button
        table.row();
        button = new TextButton("Confirm", textButtonStyle);
        button.addListener(new ClickListener() {
        	public void clicked(InputEvent event,float x,float y){
        		confirm();
        	}
            
        });
        table.add(button).height(80).space(10).width(200).align(Align.right);
        
        //back button 
        button = new TextButton("Back", textButtonStyle);
        button.addListener(new ClickListener() {
            
        	public void clicked(InputEvent event,float x,float y){
        		goBack();
        	}
            
        });
        table.add(button).width(120).height(80).space(10).width(200).align(Align.right);  
        
		table.setFillParent(true);
        table.setBackground(skin.getDrawable("Background"));
        stage.addActor(table);
        
        table = new Table();
        labelStyle = new LabelStyle();
        
        labelStyle.font = font;
		labelStyle.fontColor = Color.BLACK;
		labelStyle.background = skin.getDrawable("White");   
        label = new Label("Waiting for server...", labelStyle); 
        table.add(label);
        table.bottom().left();
        table.setVisible(false);
        stage.addActor(table);
	}
	
	private void confirm(){
		if(con.connect()){
			//may need to encrypt the password
			String[] args = new String[2];
			args[0] = userNameField.getText();
			args[1] = codeField.getText();
			con.sendRequest(new Request(RequestType.CONFIRM, args));
			gsm.getGame().getTimer().scheduleTask(Utility.timeOutCounter(con, stage), Connection.TIMEOUT);
		}
		else {
			MyDialog dia = new MyDialog("Error", "Couldn't connect to the server"); 
			dia.show(stage);
		}
		
		
	}
	private void goBack(){
		gsm.popState();
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
		if(up.getType() == UpdateType.ACKNOWLEDGMENT){
			con.getRequestSender().setProcessing(false);
			gsm.getGame().getTimer().clear();
			Acknowledgement ack = (Acknowledgement)up.getData();
			if(ack.getRequestType() == RequestType.CONFIRM){
				if(ack.getAck()){
					MyDialog dia = new MyDialog("Success" , "You confirmed your account, head to the game!!!");
					dia.show(stage);
				}
				else{
					MyDialog dia = new MyDialog("Failure" , "Please check the code you entered is correct");
					dia.show(stage);
				}
				con.close();
				synchronized(updates){
					updates.clear();
				}
			}
			
		}
	}

}
