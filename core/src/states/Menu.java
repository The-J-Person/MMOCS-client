package states;

import handlers.ErrorMessage;
import handlers.GameStateManager;
import network.Connection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import common.Request;
import common.RequestType;

public class Menu extends GameState {

	private Stage stage;
	private TextField userNameField;
	private TextField passField;
	private TextureAtlas textAtlas;
	private Connection con;
	private Dialog waitingDialog;
	private Label waitLabel;
	
	public Menu(GameStateManager gsm){
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
		if(con.getRequestSender()!= null && con.getRequestSender().isProcessing()){
			if(waitLabel.getText().length <= waitLabel.getText().length - -3){
				waitLabel.setText(waitLabel.getText() + ".");
			}
			else {
				waitLabel.setText("Waiting.");
			}
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
	}
	
	private void logIn(){
		if(isWaiting())
			return;
		final Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		if(con.connect()){
			//may need to encrypt the password
			String[] args = new String[2];
			args[0] =  userNameField.getText();
			args[1] = passField.getText();
			con.setReceiver(gsm.getGame());
			con.startReceiver();
			con.getRequestSender().sendRequest(new Request(RequestType.LOG_IN, args));
			waitingDialog = new Dialog("Waiting for the server to respond", skin){
				{
				waitLabel = new Label("Waiting.",skin);
				text(waitLabel);
				}
			};;
			waitingDialog.show(stage);
			
		}
		else {
			Dialog dialog;	
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
	
	private void register(){
		if(isWaiting())
			return;
		gsm.pushState(GameStateManager.REGISTER);
	}
	private void confirm(){
		if(isWaiting())
			return;
		gsm.pushState(GameStateManager.CONFIRM);
	}
	
	private void exitGame(){
		if(con.isConnected())
			con.close();
		Gdx.app.exit();
	}

	public InputProcessor getInputProcessor(){
		return stage;
	}
	
	private boolean isWaiting(){
		if(con.getRequestSender() != null && con.getRequestSender().isProcessing())
			return true;
		return false;
	}
}
