package states;

import handlers.GameStateManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Initial extends GameState {

	private Stage stage;
	private TextField field;
	private TextureAtlas textAtlas;
	
	public Initial(GameStateManager gsm){
		super(gsm);
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
		Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT);
		stage.draw();

	}

	@Override
	public void dispose() {
		textAtlas.dispose();
		// TODO Auto-generated method stub

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
        label = new Label("IP:", labelStyle);
		table.add(label).space(10);
        
        field = new TextField("", textFieldStyle);
		field.setMaxLength(15);
        table.add(field).height(30).width(120).space(10);
        
        table.row();
        
        //enter
        button = new TextButton("Enter", textButtonStyle);
        button.addListener(new ClickListener() {
            
        	public void clicked(InputEvent event,float x,float y){
        		enterGame();
        	}
            
        });
        table.add(button).height(80).width(120).space(10).colspan(2);
        
        table.setBackground(skin.getDrawable("Background"));
        table.setFillParent(true);
        stage.addActor(table);
	}
	
	private void enterGame(){
		gsm.getGame().getCon().setAddress(field.getText(), 15001);
		gsm.setState(GameStateManager.MENU);
	}

	public Stage getInputProcessor(){
		return stage;
	}
}


