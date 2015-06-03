package states;

import handlers.GameMap;
import handlers.GameStateManager;
import handlers.MyDialog;
import handlers.MyInput;
import handlers.MyInputProcessor;

import java.util.Hashtable;
import java.util.LinkedList;

import network.Connection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import common.Acknowledgement;
import common.Coordinate;
import common.Request;
import common.RequestType;
import common.Resource;
import common.Tile;
import common.Update;

import entities.Player;

public class Play extends GameState {

	private GameMap map;
	private Player player;
	private Connection con;
	private Resource selectedRes; 
	private MyInputProcessor stage;
	private TextureAtlas textAtlas;
	private BitmapFont font;
	
	public Play(GameStateManager gsm){
		super(gsm);
		stage = new MyInputProcessor();
		font = new BitmapFont();
		Gdx.input.setInputProcessor(stage);
		selectedRes = null;
		map = gsm.getGame().getMap();
		con = gsm.getGame().getCon();
		player = gsm.getGame().getPlayer();
		map.setCenter(player.getLocation());
		
		LinkedList<Coordinate> cors = map.missingTiles();
		for(Coordinate cor : cors){
			con.sendRequest(new Request(RequestType.TILE , cor));
		}
		
		//testing area
//		map = new GameMap(batch, null , 440, 320);
//		map.setCenter(new Coordinate(0,0));
//		map.update(new Tile(2,0,FloorType.STONE_BRICK, MapObjectType.MONSTER));
//		map.update(new Tile(-3,-3,FloorType.STONE_BRICK, MapObjectType.MONSTER));
//		map.update(new Tile(1,0,FloorType.STONE_BRICK, null));
//		map.update(new Tile(1,1,FloorType.STONE_BRICK, null));
//		map.update(new Tile(1,2,FloorType.STONE_BRICK, null));
//		map.update(new Tile(0,2,FloorType.WATER, null));
//		player.setCurrentHp(50);
//		//end of testing area
		
		//filling my stage :)
		fillStage(stage);
		
	}
	@Override
	public void handleInput() {
		if(MyInput.isMouseClicked(MyInput.LEFT_MOUSE)){
			if(isSelected()){
				//do a different action because i have an item selected
			}
			//if mouse is on map, move in the tile pressed
			else if(map.mouseOnMap(MyInput.getMouseX(MyInput.LEFT_MOUSE), MyInput.getMouseY(MyInput.LEFT_MOUSE))){
				player.move(map.getTile(
						map.parsePixelsX(MyInput.getMouseX(MyInput.LEFT_MOUSE)), 
						map.parsePixelsY(MyInput.getMouseY(MyInput.LEFT_MOUSE))));
			}		
		}
		else if(MyInput.isMouseClicked(MyInput.RIGHT_MOUSE)){
			if(isSelected()){
				//do a different action because i have an item selected
			}
			//if mouse is on map, harvest/attack in the tile pressed
			if(map.mouseOnMap(MyInput.getMouseX(MyInput.RIGHT_MOUSE), MyInput.getMouseY(MyInput.RIGHT_MOUSE))){
				player.act(map.getTile(
						map.parsePixelsX(MyInput.getMouseX(MyInput.RIGHT_MOUSE)), 
						map.parsePixelsY(MyInput.getMouseY(MyInput.RIGHT_MOUSE))));
			}
			
		}
		if(MyInput.isPressed(MyInput.DOWN_KEY)){
			player.act(map.getTile(
					map.getMiddleX() , map.getMiddleY()+1));
		}
		else if(MyInput.isPressed(MyInput.UP_KEY)){
			player.act(map.getTile(
					map.getMiddleX() , map.getMiddleY()-1));
		}
		else if(MyInput.isPressed(MyInput.LEFT_KEY)){
			player.act(map.getTile(
					map.getMiddleX()-1 , map.getMiddleY()));
		}
		else if(MyInput.isPressed(MyInput.RIGHT_KEY)){
			player.act(map.getTile(
					map.getMiddleX()+1 , map.getMiddleY()));
		}
		
		//must reset the mouse every time its handled
		MyInput.resetMouseXY();
		
		

	}

	@Override
	public void update(float dt) {
		handleUpdates();
		handleInput();
	}

	@Override
	public void render() {
		Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(map.getFloorSprites().getTexture("STONE_BRICK"),0, 0,440,80);
		batch.end();
		map.drawMap();
		drawHealthBar();
		stage.draw();
	}

	@Override
	public void dispose() {
		textAtlas.dispose();
		// TODO Auto-generated method stub

	}
	
	private boolean isSelected(){
		return selectedRes != null;
	}
	
	private void fillStage(Stage stage){
		TextButton button;
		Table table = new Table();
		BitmapFont font = new BitmapFont();
		Skin skin = new Skin();
		TextureAtlas textAtlas = new TextureAtlas(Gdx.files.internal("Buttons.pack"));
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		
		skin.addRegions(textAtlas);
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("Button-up");
        textButtonStyle.down = skin.getDrawable("Button-down");
        
        table.setPosition(440, 0);
        table.setSize(440, 80);
        table.right().bottom();
        
        //craft button
        button = new TextButton("Craft", textButtonStyle);
        button.addListener(new ClickListener() {
            
        	public void clicked(InputEvent event,float x,float y){
        		craft();
        	}
            
        });
        table.add(button).height(80).width(120).space(10);

        //equipments inventory button
        button = new TextButton("Equipment", textButtonStyle);
        button.addListener(new ClickListener() {
            
        	public void clicked(InputEvent event,float x,float y){
        		openEquipments();
        	}
            
        });
        table.add(button).height(80).width(120).space(10);
        
      //inventory button
        button = new TextButton("Inventory", textButtonStyle);
        button.addListener(new ClickListener() {
            
        	public void clicked(InputEvent event,float x,float y){
        		openInventory();
        	}
            
        });
        table.add(button).height(80).width(120).space(10);
       
      //logout button
        button = new TextButton("Log out", textButtonStyle);
        button.addListener(new ClickListener() {
            
        	public void clicked(InputEvent event,float x,float y){
        		logOut();
        	}
            
        });
        table.add(button).height(80).width(120).space(10);
        
        stage.addActor(table);
	}
	
	private void drawHealthBar(){
		batch.begin();
		batch.draw(map.getFloorSprites().getTexture("void"),5, 5,150,30);
		batch.draw(map.getFloorSprites().getTexture("GRASS"),5, 5,getPercentHpBar(150),30);
        font.draw(batch,
        		"HP: " +player.getCurrentHp() + "/" + player.getMaxHp() , 
        		40, 20);      
		batch.end();
	}
	
	private int getPercentHpBar(int full){
		int x = 1;
		if (player.isInitialized())
			x = (int)(full * player.getCurrentHp()/((float)player.getMaxHp())); 
		return x;
	}
	private void logOut(){ System.out.println("logging out"); }
	private void openInventory(){ System.out.println("opening inventory"); }
	private void craft(){ System.out.println("weeee crafting"); }
	private void openEquipments(){ System.out.println("as if i have equipment..."); }
	
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
			if(ack.getRequestType() == RequestType.MOVE){
				if(ack.getAck()){
					System.out.println("movement was accepted");
					Coordinate newMid= (Coordinate)con.getRequestSender().requestToAck().getData();
					map.setCenter(newMid);
//					loading = true;
//					waitLabel.setText("Loading...");
				}
				else{
					System.out.println("movement was declined");
//					MyDialog dia = new MyDialog("Failure" , "Please check the details you entered are correct");
//					dia.show(stage);
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
		case TILE: 
			Tile tile = (Tile) up.getData();
			map.update(tile);
			System.out.println("updated tile" + tile.getCoordinate().X()+ "," + tile.getCoordinate().Y());
			break;
		default: break;
		}
	}
}
