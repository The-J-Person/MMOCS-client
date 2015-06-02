package states;

import network.Connection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import common.Request;
import common.RequestType;

import handlers.GameStateManager;

public class Register extends GameState {
	
	private Stage stage;
	private TextField userNameField;
	private TextField passField;
	private TextField emailField;
	private TextureAtlas textAtlas;
	private Connection con;
	
	public Register(GameStateManager gsm){
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
		// TODO Auto-generated method stub

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
        
        //password label and field
        table.row();
        label = new Label("Password:", labelStyle);
        table.add(label).space(10).uniform();
        passField = new TextField("", textFieldStyle);
        passField.setPasswordMode(true);
        passField.setPasswordCharacter('*');
        passField.setMaxLength(20);
        table.add(passField).height(30).width(250).space(10).align(Align.left);
        
      //code label and field
        table.row();
        label = new Label("E-mail:", labelStyle);
		table.add(label).space(10).uniform();
        emailField = new TextField("", textFieldStyle);
        emailField.setMaxLength(50);
        table.add(emailField).height(30).width(250).space(10).align(Align.left);
        
        //register button
        table.row();
        button = new TextButton("Register", textButtonStyle);
        button.addListener(new ClickListener() {
        	public void clicked(InputEvent event,float x,float y){
        		register();
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
	}
	
	private void register(){
		if(con.connect()){
			//may need to encrypt the password
			String[] args = new String[3];
			args[0] = userNameField.getText();
			args[1] = passField.getText();
			args[2] = emailField.getText();
			con.setReceiver(gsm.getGame());
			con.startReceiver();
			con.getRequestSender().sendRequest(new Request(RequestType.REGISTER, args));
			//waiting window is required 
		}
		else {
			Dialog dialog;
			Skin skin = new Skin(Gdx.files.internal("uiskin.json"));	
			dialog = new Dialog("Error", skin){
				{
				text("Failed to connect to the server");
				button("ok");
				}
				
				protected void result(Object obj){
					remove();
				}
			};;
			dialog.show(stage);
		}
		
		
	}
	private void goBack(){
		gsm.popState();
	}
	
	public InputProcessor getInputProcessor(){
		return stage;
	}

}
