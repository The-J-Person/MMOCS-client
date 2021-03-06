package handlers;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import common.Coordinate;
import common.Tile;

public class GameMap {
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
	}
	
	public void setSprites(Content floors, Content objects){
		this.floors = floors;
		this.objects = objects;
	}
	
	public void setCenter(Coordinate center){ this.center = center;}
	public Coordinate getCenter() { return center; }
	
	public boolean tileInRange(Tile tile){
		int x, y;
		x = (colsNum()-1)/2;
		y = (rowsNum()-1)/2;
		if ( tile.getCoordinate().X() > center.X() + x) return false;
		if ( tile.getCoordinate().X() < center.X() - x) return false;
		if ( tile.getCoordinate().Y() > center.Y() + y) return false;
		if ( tile.getCoordinate().Y() < center.Y() - y) return false;
		return true;
	}
	
	public int parsePixelsX(int pixelsX){
		return pixelsX/80 +1;
	}
	
	public int parsePixelsY(int pixelsY){
		return pixelsY/80 +1;
	}
	
	public boolean mouseOnMap(int pixelsX , int pixelsY){
		if (parsePixelsY(pixelsY) <= height)
			return true;
		return false;
	}
	
	public int rowsNum(){ return map.size(); }
	public int colsNum(){ return map.getFirst().size();}
	
	public void update(Tile tile){
		if (!tileInRange(tile))
			return;
		int x,y;
		x = (int)((tile.getCoordinate().X() - center.X()) + getMiddleX());
		y = (int)((center.Y() - tile.getCoordinate().Y()) + getMiddleY());
		map.get(y).set(x, tile);
	}
	
	public boolean isNearby(Coordinate cor){
		if(center.equals(cor))
			return false;
		if(Math.abs(cor.X() - center.X()) <= 1 && Math.abs(cor.Y() - center.Y()) <= 1 )
			return true;
		return false;
	}
	
	//null if its not nearby
	public Direction getDirection(Coordinate cor){
		if(isNearby(cor)){
			int x = (int)(cor.X() - center.X());
			int y = (int)(cor.Y() - center.Y());
			
			if(x == 1 && y == 1) return Direction.NORTH_EAST;
			if(x == 0 && y == 1) return Direction.NORTH;
			if(x == -1 && y == 1) return Direction.NORTH_WEST;
			if(x == 1 && y == 0) return Direction.EAST;
			if(x == -1 && y == 0) return Direction.WEST;
			if(x == 1 && y == -1) return Direction.SOUTH_EAST;
			if(x == 0 && y == -1) return Direction.SOUTH;
			if(x == -1 && y == -1) return Direction.SOUTH_WEST;
		}
		return null;
	}
	
	public void MoveCenter(Direction dir){
		switch (dir){
		case WEST:
			moveCenterStraight(Direction.WEST);
			break;
		case EAST:
			moveCenterStraight(Direction.EAST);
			break;
		case NORTH:
			moveCenterStraight(Direction.NORTH);
			break;
		case SOUTH:
			moveCenterStraight(Direction.SOUTH);
			break;
		case NORTH_WEST:
			moveCenterStraight(Direction.NORTH);
			moveCenterStraight(Direction.WEST);
			break;
		case NORTH_EAST:
			moveCenterStraight(Direction.NORTH);
			moveCenterStraight(Direction.EAST);
			break;
		case SOUTH_WEST:
			moveCenterStraight(Direction.SOUTH);
			moveCenterStraight(Direction.WEST);
			break;
		case SOUTH_EAST:
			moveCenterStraight(Direction.SOUTH);
			moveCenterStraight(Direction.EAST);
			break;
		default: break;
		}
	}
	
	private void moveCenterStraight(Direction dir){
		LinkedList<Tile> list;
		switch(dir){
		case WEST:
			center.setX(center.X() -1);
			for(int i = 0; i < rowsNum() ; i++){
				map.get(i).addFirst(null);
				map.get(i).removeLast();
			}
			break;
		case NORTH:
			center.setY(center.Y() + 1);
			list = new LinkedList<Tile>();
			for(int i = 0; i < colsNum(); i++)
				list.add(null);
			map.addFirst(list);
			map.removeLast();
			break;
		case EAST:
			center.setX(center.X() + 1);
			for(int i = 0; i < rowsNum() ; i++){
				map.get(i).addLast(null);
				map.get(i).removeFirst();
			}
			break;
		case SOUTH:
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
			for(int j = 0 ; j < colsNum() ; j++){
				if(map.get(i).get(j) == null){
					int x = j - getMiddleX();
					int y = getMiddleY() - i;
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
					if(tile.getFloorType() != null)
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
	}
	
	public Tile getTile(int x, int y){
		return map.get(y).get(x);
	}
	
	public int getMiddleX(){ return (colsNum()-1)/2; }
	public int getMiddleY(){ return (rowsNum()-1)/2; }
	
	public void resetMap(){
		for(int i = 0 ; i < rowsNum(); i++){
			for(int j = 0 ; j < colsNum(); j++){
				map.get(i).set(j, null);
			}
		}
	}
}
