package entities;

import java.util.Hashtable;

import common.Coordinate;
import common.Request;
import common.RequestType;
import common.Resource;
import common.Tile;

import handlers.GameMap;
import network.Connection;

public class Admin extends Player {
	public Admin(Connection con, GameMap map){
		super(con,map);
	}
	
	public Admin(Player player){
		super(player);
	}
	
public int getMaxHp(){ return maxHp;}
	
	public boolean isInitialized(){
		return   currentHp != -1 && inventory != null &&  loc != null;
	}
	
	public int getCurrentHp(){ return currentHp; }
	public void setCurrentHp(int value){ currentHp = value; }
	public void setLocation(Coordinate cor){ loc = cor; }
	public Coordinate getLocation() {return loc;}
	public Hashtable<Resource,Integer> getInventory(){return inventory;}
	
	public boolean act(Tile tile){
		if(move(tile)){
			return true;
		}
		if(harvest(tile)){
			return true;
		}
		return false;
	}
	
	public boolean move(Tile tile){
		if(tile != null){
			if(map.isNearby(tile.getCoordinate())){
				Request req = new Request(RequestType.MOVE , tile.getCoordinate());
				con.sendRequest(req);
				return true;
			}
		}	
		return false;
	}
	
	public boolean harvest(Tile tile){
		if(tile != null){
				if(tile.canAttack()){
					Request req = new Request(RequestType.ATTACK , tile.getCoordinate());
					con.sendRequest(req);
					return true;
				}
				else if (tile.canHarvest()){
					Request req = new Request(RequestType.HARVEST , tile.getCoordinate());
					con.sendRequest(req);
					return true;
				}
		}	
		return false;
	}
	
	public void placeResource(Tile tile, Resource res, boolean asObject){
		Tile updatedTile = new Tile(tile);
		if(asObject){
			tile.setMapObjectType(res.place_object());
			con.sendRequest(new Request(RequestType.UPDATE_TILE, updatedTile));
		}
		else{
			tile.setFloorType(res.place_floor());
			con.sendRequest(new Request(RequestType.UPDATE_TILE, updatedTile));
		}
	}
	
	public void removeResource(Resource res){
	}
	
	public void setInventory(Hashtable<Resource,Integer> inven){ this.inventory = inven;}
	
	public void craft(Resource res){
		if(res!=null){
			con.sendRequest(new Request(RequestType.CRAFT, res));
		}
	}
	
	public void payCraft(Resource res){
	}
	
	public boolean isAdmin(){return true;}
}
