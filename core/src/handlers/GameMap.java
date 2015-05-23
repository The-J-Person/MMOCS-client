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
		y = (int)((tile.getCoordinate().Y() - center.Y()) + ((rowsNum()-1)/2));
		map.get(y).set(x, tile);
	}
	
	public void moveCenter(short dir){
		switch(dir){
		case LEFT:
			center.setX(center.X() -1);
			for(int i = 0; i < rowsNum() ; i++){
				map.get(i).addFirst(null);
				map.removeLast();
			}
			break;
		case UP:
			center.setY(center.Y() + 1);
			map.addFirst(null);
			map.removeLast();
			break;
		case RIGHT:
			center.setX(center.X() + 1);
			for(int i = 0; i < rowsNum() ; i++){
				map.get(i).addLast(null);
				map.removeFirst();
			}
			break;
		case DOWN:
			center.setY(center.Y() - 1);
			map.addLast(null);
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
	}
	
	
	private void setSprites(){
		floors = new Content();
		objects = new Content();
		
		floors.loadTexture("red.jpg", "void");
		floors.loadTexture("red.jpg",FloorType.GRASS.name());
		floors.loadTexture("red.jpg",FloorType.DIRT.name());
		floors.loadTexture("red.jpg",FloorType.WATER.name());
		floors.loadTexture("red.jpg",FloorType.MUD.name());
		floors.loadTexture("red.jpg",FloorType.SAND.name());
		floors.loadTexture("red.jpg",FloorType.STONE.name());
		floors.loadTexture("red.jpg",FloorType.WOOD.name());
		floors.loadTexture("red.jpg",FloorType.STONE_BRICK.name());
		
		objects.loadTexture("red.jpg",MapObjectType.PLAYER.name()); 
		objects.loadTexture("red.jpg",MapObjectType.PILE.name());
		objects.loadTexture("red.jpg",MapObjectType.MONSTER.name());
		objects.loadTexture("red.jpg",MapObjectType.TREE.name());
		objects.loadTexture("red.jpg",MapObjectType.BUSH.name());
		objects.loadTexture("red.jpg",MapObjectType.ROCK.name());
		objects.loadTexture("red.jpg",MapObjectType.WALL_WOOD.name());
		objects.loadTexture("red.jpg",MapObjectType.WALL_STONE.name());
		
	}
}
