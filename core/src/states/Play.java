package states;

import handlers.GameMap;
import handlers.GameStateManager;
import handlers.MyInput;
import handlers.MyInputProcessor;

import java.util.Hashtable;
import java.util.LinkedList;

import network.Connection;
import utility.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
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
	private boolean isSelected; 
	private Resource selectedRes;
	private MyInputProcessor stage;
	private BitmapFont font;
	private boolean openedWindow;
	
	public Play(GameStateManager gsm){
		super(gsm);
		openedWindow = false;
		stage = new MyInputProcessor();
		font = new BitmapFont();
		Gdx.input.setInputProcessor(stage);
		selectedRes = null;
		isSelected = false;
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
			if(isSelected){
				if(map.mouseOnMap(MyInput.getMouseX(MyInput.LEFT_MOUSE), MyInput.getMouseY(MyInput.LEFT_MOUSE))){
					player.placeResource(map.getTile(
							map.parsePixelsX(MyInput.getMouseX(MyInput.LEFT_MOUSE)), 
							map.parsePixelsY(MyInput.getMouseY(MyInput.LEFT_MOUSE))), 
							selectedRes, true);
				}
			}
			//if mouse is on map, move in the tile pressed
			else{ 
				if(map.mouseOnMap(MyInput.getMouseX(MyInput.LEFT_MOUSE), MyInput.getMouseY(MyInput.LEFT_MOUSE))){
					player.move(map.getTile(
						map.parsePixelsX(MyInput.getMouseX(MyInput.LEFT_MOUSE)), 
						map.parsePixelsY(MyInput.getMouseY(MyInput.LEFT_MOUSE))));
				}	
			}
		}
		else if(MyInput.isMouseClicked(MyInput.RIGHT_MOUSE)){
			if(isSelected){
				if(map.mouseOnMap(MyInput.getMouseX(MyInput.LEFT_MOUSE), MyInput.getMouseY(MyInput.LEFT_MOUSE))){
					player.placeResource(map.getTile(
							map.parsePixelsX(MyInput.getMouseX(MyInput.LEFT_MOUSE)), 
							map.parsePixelsY(MyInput.getMouseY(MyInput.LEFT_MOUSE))),
							selectedRes, false);
					isSelected = false;
				}
			}
			else{
			//if mouse is on map, harvest/attack in the tile pressed
				if(map.mouseOnMap(MyInput.getMouseX(MyInput.RIGHT_MOUSE), MyInput.getMouseY(MyInput.RIGHT_MOUSE))){
					player.harvest(map.getTile(
							map.parsePixelsX(MyInput.getMouseX(MyInput.RIGHT_MOUSE)), 
							map.parsePixelsY(MyInput.getMouseY(MyInput.RIGHT_MOUSE))));
					isSelected = false;
				}
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
		else if(MyInput.isPressed(MyInput.CANCEL_KEY)){
			selectedRes = null ;
		}
		
		//must reset the mouse every time its handled
		MyInput.resetMouseXY();
		
		

	}

	@Override
	public void update(float dt) {
		handleUpdates();
		if(!openedWindow){
			stage.getActors().get(0).setTouchable(Touchable.enabled);
			handleInput();
		}
		else{
			stage.getActors().get(0).setTouchable(Touchable.disabled);
		}
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
		con.sendRequest(new Request(RequestType.LOG_OUT, null));
		player.setCurrentHp(-1); 
		player.setLocation(null);
		player.setInventory(null);
		map.resetMap();
		con.close();

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
        		if(!con.isProcessing())
        			craft();
        	}
            
        });
        table.add(button).height(80).width(120).space(10);

        //equipments inventory button
        button = new TextButton("Equipment", textButtonStyle);
        button.addListener(new ClickListener() {
            
        	public void clicked(InputEvent event,float x,float y){
        		if(!con.isProcessing())
        			openEquipments();
        	}
            
        });
        table.add(button).height(80).width(120).space(10);
        
      //inventory button
        button = new TextButton("Inventory", textButtonStyle);
        button.addListener(new ClickListener() {
            
        	public void clicked(InputEvent event,float x,float y){
        		if(!con.isProcessing())
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
		batch.draw(map.getFloorSprites().getTexture("RED"),5, 5,getPercentHpBar(150),30);
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
	private void logOut(){ 
		gsm.popState();
	}
	private void openInventory(){
		TextButton button;
		Table table;
		Label label;
		Array<Resources> arr;
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		openedWindow = true;
		
		final Window win = new Window("Inventory", skin);
		win.setKeepWithinStage(true);
		
		final List<Resources> list = new List<Resources>(skin);
		arr = new Array<Resources>();
		Hashtable<Resource,Integer> inven = player.getInventory();
		for(Resource res : inven.keySet()){
			arr.add(new Resources(res, inven.get(res) ));
		}	
		list.setItems(arr);
		win.add(list).top().left();
		
		win.row();
		
		button = new TextButton("Pick" , skin);
		button.addListener(new ClickListener() {
            
        	public void clicked(InputEvent event,float x,float y){
        		if(list.getSelected() != null)
        			selectedRes = list.getSelected().getResource();{
        			isSelected = true;
        			}
        		openedWindow = false;
        		MyInput.resetMouseXY();
        		win.remove();
        	}
            
        });
		win.add(button).bottom();
		
		button = new TextButton("Close", skin);
		button.addListener(new ClickListener() {
            
        	public void clicked(InputEvent event,float x,float y){
        		openedWindow = false;
        		MyInput.resetMouseXY();
        		win.remove();
        	}
            
        });
		win.add(button).bottom();
		win.setPosition(350, 350);
		stage.addActor(win);
	}
	
	private void craft(){ System.out.println("weeee crafting"); }
	private void openEquipments(){ System.out.println("as if i have equipment..."); }
	
	public Stage getInputProcessor(){
		return stage;
	}
	
	private void handleUpdates(){
		Update up;
		LinkedList<Update> updates = con.getUpdates();
		for(int i= 0 ; i < 6 ; i++){
			synchronized(updates){
				if(!updates.isEmpty())
					up = updates.removeFirst();
				else return;
			}
			switch (up.getType()){
			case ACKNOWLEDGMENT:
				Acknowledgement ack = (Acknowledgement)up.getData();
				con.setProcessing(false);
				switch(ack.getRequestType()){
				case MOVE:
					if(ack.getAck()){
						Coordinate newMid= (Coordinate)con.getRequestSender().requestToAck().getData();
						map.MoveCenter(map.getDirection(newMid));
						player.setLocation(map.getCenter());
						LinkedList<Coordinate> cors = map.missingTiles();
						for(Coordinate cor : cors){
							con.sendRequest(new Request(RequestType.TILE , cor));
						}
					}
					break;
				case HARVEST:
					//i would like to add animation here
					break;
				case ATTACK:
					//i would like to add animation here
					break;
				case UPDATE_TILE:
					player.removeResource(selectedRes);
					selectedRes = null;
					break;
				default: break;
				
				}
				break;
			case HIT_POINTS:
				Integer hp = (Integer)up.getData();
				player.setCurrentHp(hp);
				if(hp == 0){
					logOut();
				}
				break;
			case RESOURCES:
				Resource res = (Resource)up.getData();
				player.addResource(res);
				break;
			case TILE: 
				Tile tile = (Tile) up.getData();
				map.update(tile);
				break;
			default: break;
			}
		}
	}
}
