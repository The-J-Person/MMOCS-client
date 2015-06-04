package handlers;

public class MyInput {
	public static boolean[] keys;
	public static boolean[] pkeys;
	public static int[] mouseXY;
	
	public static final int NUM_KEYS = 5;
	public static final int UP_KEY = 0;
	public static final int DOWN_KEY = 1;
	public static final int RIGHT_KEY = 2;
	public static final int LEFT_KEY = 3;
	public static final int CANCEL_KEY = 3;
			
	public static final int MOUSE_KEYS = 2;
	public static final int LEFT_MOUSE = 0;
	public static final int RIGHT_MOUSE = 2;
	
	static {
		keys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
		mouseXY = new int[MOUSE_KEYS*2];
		for(int i = 0 ; i < MOUSE_KEYS*2 ; i++){
			mouseXY[i] = -1;
		}
	}
	
	public static void update(){
		for(int i =0 ; i < NUM_KEYS ; i++)
			pkeys[i] = keys[i];
	}
	
	public static void setKey(int i, boolean b) { keys[i] = b;}
	public static boolean isDown(int i){ return keys[i]; }
	public static boolean isPressed(int i){ return keys[i] && !pkeys[i]; }
	
	public static boolean isMouseClicked(int i) { return mouseXY[i] != -1;}
	public static void setMouseXY(int i,int x, int y){ mouseXY[i] = x; mouseXY[i+1] = y; }
	public static int getMouseX(int i){ return mouseXY[i] ; }
	public static int getMouseY(int i){ return mouseXY[i+1] ; }
	
	public static void resetMouseXY() {
		for(int i = 0 ; i < MOUSE_KEYS*2 ; i++)
			mouseXY[i] = -1;
	}
	
}
