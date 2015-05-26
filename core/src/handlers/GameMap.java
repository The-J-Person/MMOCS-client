package handlers;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import common.Coordinate;
import common.FloorType;
import common.MapObjectType;
import common.Tile;

public class GameMap {
	
	public static final short LEFT = 0;
	public static final short UP = 1;
	public static final short RIGHT = 2;
	public static final short DOWN = 3;
	
	private Coordinate center;
	private int width,height;
	private LinkedList<LinkedList<Tile>> map;
	private SpriteBatch sb;
	private Content floors;
	private Content objects;
	
	/*
	 * draws the map in the area in jumps of 40x40 
	 * */
	public GameMap(SpriteBatch sb, Coordinate center ,int width, int height){
		this.sb = sb;
		this.center = center;
		this.width = width/40;
		this.height = height/40 - 1;
		map = new LinkedList <LinkedList<Tile>>();
		for(int i = 0; i < this.height + 2 ; i++){
			LinkedList<Tile> list = new LinkedList<Tile>();
			for(int j = 0; j < this.width + 2 ; j++){
				list.add(null);
			}
			map.add(list);
		}
		setSprites();
	}
	
	public Coordinate getCenter() { return center; }
	
	public boolean inRange(Tile tile){
		int x, y;
		x = (colsNum()-1)/2;
		y = (rowsNum()-1)/2;
		if ( tile.getCoordinate().X() > center.X() + x) return false;
		if ( tile.getCoordinate().X() < center.X() - x) return false;
		if ( tile.getCoordinate().Y() > center.Y() + y) return false;
		if ( tile.getCoordinate().Y() < center.Y() - y) return false;
		return true;
	}
	
	public int rowsNum(){ return map.size(); }
	public int colsNum(){ return map.getFirst().size();}
	
	public void update(Tile tile){
		if (!inRange(tile))
			return;
		int x,y;
		x = (int)((tile.getCoordinate().X() - center.X()) + ((colsNum()-1)/2));
		y = (int)((center.Y() - tile.getCoordinate().Y()) + ((rowsNum()-1)/2));
		map.get(y).set(x, tile);
	}
	
	public Tile lookInDirection(short dir){
		int x = (colsNum()-1) / 2;
		int y = (rowsNum()-1) / 2;
		switch(dir){
		case LEFT:
			return map.get(y).get(x-1);
		case UP:
			return map.get(y-1).get(x);
		case RIGHT:
			return map.get(y).get(x+1);
		case DOWN:
			return map.get(y+1).get(x);
		default: break;
		}
		return null;
	}
	
	public void moveCenter(short dir){
		LinkedList<Tile> list;
		switch(dir){
		case LEFT:
			center.setX(center.X() -1);
			for(int i = 0; i < rowsNum() ; i++){
				map.get(i).addFirst(null);
				map.get(i).removeLast();
			}
			break;
		case UP:
			center.setY(center.Y() + 1);
			list = new LinkedList<Tile>();
			for(int i = 0; i < colsNum(); i++)
				list.add(null);
			map.addFirst(list);
			map.removeLast();
			break;
		case RIGHT:
			center.setX(center.X() + 1);
			for(int i = 0; i < rowsNum() ; i++){
				map.get(i).addLast(null);
				map.get(i).removeFirst();
			}
			break;
		case DOWN:
			center.setY(center.Y() - 1);
			list = new LinkedList<Tile>();
			for(int i = 0; i < colsNum(); i++)
				list.add(null);
			map.addLast(list);
			map.removeFirst();
			break;
		default: break;
		}
	}
	
	public LinkedList<Coordinate> missingTiles(){
		LinkedList<Coordinate> res = new LinkedList<Coordinate>();
		for(int i = 0 ; i < rowsNum() ; i++){
			for(int j = 0 ; i < colsNum() ; j++){
				if(map.get(i).get(j) == null){
					int x = j - ((colsNum()-1)/2);
					int y = i - ((rowsNum()-1)/2);
					res.add(new Coordinate(center.X() + x , center.Y() + y));
				}  
			}
		}
		return res;
	}
	
	public void drawMap(){
		Iterator <Tile> w_iter;
		Iterator<LinkedList<Tile>> h_iter = map.iterator();
		if(h_iter.hasNext())
			h_iter.next();
		for(int i = 0 ; i < height && h_iter.hasNext() ; i++){
			w_iter = h_iter.next().iterator();
			if(w_iter.hasNext())
				w_iter.next();
			for(int j = 0; j < width && w_iter.hasNext() ; j++){
				Tile tile = w_iter.next(); // 
				if(tile != null){
					sb.begin();
					sb.draw(floors.getTexture(tile.getFloorType().name()), j*40, 40*(height-i));
					if(tile.getMapObjectType() != null)
						sb.draw(objects.getTexture(tile.getMapObjectType().name()), j*40, 40*(height-i));
					sb.end();
				}
				else{
					sb.begin();
					sb.draw(floors.getTexture("void"), j*40, 40*(height-i));
					sb.end();
				}
			}
		}
		//before network is here
		sb.begin();
		sb.draw(floors.getTexture(FloorType.WOOD.name()), ((width-1)/2)*40, 40*(height-((height-1)/2)));
		sb.draw(objects.getTexture(MapObjectType.PLAYER.name()), ((width-1)/2)*40, 40*(height-((height-1)/2)));
		sb.end();
	}
	
	public void MapAction(short dir){
		Tile tile = lookInDirection(dir);
		if(tile == null)
			return;
		if(tile.canMoveOn()){
			if(tile.canPickUp()){
				//here we need to send pick up action
				System.out.println("im picking things up");
				tile.setMapObjectType(null);
			}
			moveCenter(dir);
		}
		else if (tile.canAttack()){
			//here we need to send attack command
			System.out.println("dieeeee you monster");
		}
		else if (tile.canHarvest()){
			//here we need to send harvest command
			System.out.println("im a lousy farmer");
		}
	}
	
	
	private void setSprites(){
		floors = new Content();
		objects = new Content();
		
		floors.loadTexture("void.jpg", "void");//not official
		floors.loadTexture("red.jpg",FloorType.GRASS.name());//missing	
		floors.loadTexture("red.jpg",FloorType.DIRT.name());//missing
		floors.loadTexture("blue.jpg",FloorType.WATER.name()); //not official
		floors.loadTexture("red.jpg",FloorType.MUD.name());//missing
		floors.loadTexture("red.jpg",FloorType.SAND.name());//missing
		floors.loadTexture("red.jpg",FloorType.STONE.name()); //missing
		floors.loadTexture("Wood_floor.png",FloorType.WOOD.name());
		floors.loadTexture("Stone_brick_floor.png",FloorType.STONE_BRICK.name());
		
		objects.loadTexture("player.png",MapObjectType.PLAYER.name()); 
		objects.loadTexture("pile.png",MapObjectType.PILE.name());
		objects.loadTexture("monster.png",MapObjectType.MONSTER.name());
		objects.loadTexture("tree.png",MapObjectType.TREE.name());
		objects.loadTexture("Bush.png",MapObjectType.BUSH.name());
		objects.loadTexture("Rock.png",MapObjectType.ROCK.name());
		objects.loadTexture("Wood_wall.png",MapObjectType.WALL_WOOD.name());
		objects.loadTexture("Stone_wall.png",MapObjectType.WALL_STONE.name());
		
	}
}
