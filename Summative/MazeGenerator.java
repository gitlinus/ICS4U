import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import javax.imageio.*;

/**
 * Class generates dungeon that the player traverses in-game
 * @author Linus Chen
 * @author Edward Shen
 * @version June 18, 2019
 */

public class MazeGenerator {
	/**
     * A single layer of the dungeon will be contained in a 2D array
     */
	int[][] maze;
	/**
     * Binary tree for space partition
     */
	Room[] bsp; 
	/**
     * List of all hostile chracters in the layer
     */
	ArrayList<Monster> monsters;
	/**
     * List of all the rooms in the layer
     */
	ArrayList<Integer> rooms;
	/**
     * Player object that user controls
     */
	Player player;
	/**
     * Condition of whether the current layer of the maze is finished
     */
	boolean complete = false;
	/**
     * Current turn number in the layer
     */
	int turn = 0;
	/**
     * Current layer of the maze
     */
	int level;
	/**
     * Will contain the index of the starting room of the current layer
     */
	int start;
	/**
     * Condition of whether the player took damage
     */
	boolean took_damage = false;
	/**
     * Condition of whether the game is completed
     */
	boolean clear_game = false;
	/**
     * Stores the dialog string that user will see on GUI
     */
	String output = "";
	/**
     * Object to play sound effect
     */
	AudioThread sound_effect = new AudioThread();

	/**
     * Constructs MazeGenerator object
     */
	public MazeGenerator(int size, int level){
		maze = new int[size+1][size+1]; 
		bsp = new Room[(int)(Math.pow(2,((int)(Math.log(size*size)/Math.log(2))+4)))]; // tree of depth n can hold an image of 2^n * 2^n, +4 to insure no ArrayIndexOutOfBounds
		rooms = new ArrayList<Integer>();
		this.level = level;
		init(0);
		connect(0,0);
		walls();
		//start position of player
		start = (int)(Math.random()*rooms.size());
		player = new Player(bsp[rooms.get(start)].getTopLeft().getX()+1,bsp[rooms.get(start)].getTopLeft().getY()+1);
		maze[player.getX()][player.getY()] = 3;
		generate();
		display();
		getNumRooms();
	}

	/**
     * Constructs MazeGenerator object so that a player object created before can be copied
     */
	public MazeGenerator(int size, Player p, int level){
		maze = new int[size+1][size+1]; //increase size incase of ArrayIndexOutOfBounds
		bsp = new Room[(int)(Math.pow(2,((int)(Math.log(size*size)/Math.log(2))+4)))]; // tree of depth n can hold an image of 2^n * 2^n, +4 to insure no ArrayIndexOutOfBounds
		rooms = new ArrayList<Integer>();
		this.level = level;
		init(0);
		connect(0,0);
		walls();
		//start position of player
		start = (int)(Math.random()*rooms.size());
		player = new Player(bsp[rooms.get(start)].getTopLeft().getX()+1,bsp[rooms.get(start)].getTopLeft().getY()+1,p);
		maze[player.getX()][player.getY()] = 3;
		generate();
		display();
		getNumRooms();
	}

	/**
     * Constructs MazeGenerator object for the final layer
     */
	public MazeGenerator(Player p){
		level = 0;
		maze = new int[20][20];
		for(int i=0; i<20; i++)
			for(int j=0; j<20; j++){
				if(i==0||j==0||i==19||j==19) maze[i][j] = 2;
				else maze[i][j] = 1;
			}
		maze[2][2] = 3;
		player = new Player(2,2,p);
		final_generate();
	}

	/**
     * Initializes and builds the dungeon layer using randomized partitioning and procedural generation
     *
     * <p>
     * The layer generation algorithm is as follows:
     * 1. Arbitrarily split the current space horizonally or vertically 
     * 2. Recursively call 1. until minimum size of a partition is reached, storing each partition in a binary tree
     * 3. Create a randomly sized room within the partition 
     * Binary tree is used for simplicity as the children of node n can be reached at 2n+1 and 2n+2
     * This is important later as a minimum spanning tree is used to connect each room so that each children is connected and can be accessed by the player
     * </p>
     */
	public void init(int depth){ //dungeon generation algorithm
		if(depth==0) bsp[0] = new Room(new Pair(0,0), new Pair(maze.length-1,maze.length-1), false); //root of the tree contains the entire layer
		if(bsp[depth].getHeight()>25&&bsp[depth].getLength()>25){
			int dir = (int) (Math.random()*2); //picks spliting horizontally or vertically 
			if(dir==0){
				int div = (int) (Math.random()*(bsp[depth].getHeight()/2-6)) + 7; //choose dividing point (at least size 7

				//split into two new spaces
				int topx1, topy1, botx1, boty1;
				topx1 = bsp[depth].getTopLeft().getX()+1; //small adjustments are just to ensure the rooms are spaced out more
				topy1 = bsp[depth].getTopLeft().getY()+1;
				botx1 = bsp[depth].getBotRight().getX()-div-1;
				boty1 = bsp[depth].getBotRight().getY()-1;
				
				int topx2, topy2, botx2, boty2;
				topx2 = botx1+2;
				topy2 = topy1;
				botx2 = bsp[depth].getBotRight().getX();
				boty2 = boty1;
				
				bsp[depth*2+1] = new Room(new Pair(topx1, topy1), new Pair(botx1, boty1), false);
				bsp[depth*2+2] = new Room(new Pair(topx2, topy2), new Pair(botx2, boty2), false);
				init(depth*2+1); //initialize children
				init(depth*2+2);
			}
			else{
				int div = (int) (Math.random()*(bsp[depth].getLength()/2-6)) + 7;
				
				int topx1, topy1, botx1, boty1;
				topx1 = bsp[depth].getTopLeft().getX()+1;
				topy1 = bsp[depth].getTopLeft().getY()+1;
				botx1 = bsp[depth].getBotRight().getX()-1;
				boty1 = bsp[depth].getBotRight().getY()-div-1;
				
				int topx2, topy2, botx2, boty2;
				topx2 = topx1;
				topy2 = boty1+2;
				botx2 = bsp[depth].getBotRight().getX();
				boty2 = bsp[depth].getBotRight().getY();
				
				bsp[depth*2+1] = new Room(new Pair(topx1, topy1), new Pair(botx1, boty1), false);
				bsp[depth*2+2] = new Room(new Pair(topx2, topy2), new Pair(botx2, boty2), false);
				init(depth*2+1); //initialize children
				init(depth*2+2);
			}
		}
		else if(bsp[depth].getHeight()>25){ 
			int div = (int) (Math.random()*(bsp[depth].getHeight()/2-8)) + 7;
			int topx1, topy1, botx1, boty1;
			topx1 = bsp[depth].getTopLeft().getX()+1;
			topy1 = bsp[depth].getTopLeft().getY();
			botx1 = bsp[depth].getBotRight().getX()-div-1;
			boty1 = bsp[depth].getBotRight().getY();
			int topx2, topy2, botx2, boty2;
			topx2 = botx1+2;
			topy2 = topy1;
			botx2 = bsp[depth].getBotRight().getX();
			boty2 = boty1;
			bsp[depth*2+1] = new Room(new Pair(topx1, topy1), new Pair(botx1, boty1), false);
			bsp[depth*2+2] = new Room(new Pair(topx2, topy2), new Pair(botx2, boty2), false);
			init(depth*2+1);
			init(depth*2+2);
		}
		else if(bsp[depth].getLength()>25){
			int div = (int) (Math.random()*(bsp[depth].getLength()/2-8)) + 7;
			int topx1, topy1, botx1, boty1;
			topx1 = bsp[depth].getTopLeft().getX();
			topy1 = bsp[depth].getTopLeft().getY()+1;
			botx1 = bsp[depth].getBotRight().getX();
			boty1 = bsp[depth].getBotRight().getY()-div-1;
			int topx2, topy2, botx2, boty2;
			topx2 = topx1;
			topy2 = boty1+2;
			botx2 = bsp[depth].getBotRight().getX();
			boty2 = bsp[depth].getBotRight().getY();
			bsp[depth*2+1] = new Room(new Pair(topx1, topy1), new Pair(botx1, boty1), false);
			bsp[depth*2+2] = new Room(new Pair(topx2, topy2), new Pair(botx2, boty2), false);
			init(depth*2+1);
			init(depth*2+2);
		}
		else{ //minimum partition size reached
			int length = (int) (Math.random() * (bsp[depth].getLength()-5)) + 4; //construct room at least size 4*4
			int height = (int) (Math.random() * (bsp[depth].getHeight()-5)) + 4;
			int topx = (int) (Math.random() * (bsp[depth].getBotRight().getX()-1-height-bsp[depth].getTopLeft().getX())) + bsp[depth].getTopLeft().getX() + 1;
			int botx = topx + height;
			int topy = (int) (Math.random() * (bsp[depth].getBotRight().getY()-1-length-bsp[depth].getTopLeft().getY())) + bsp[depth].getTopLeft().getY() + 1;
			int boty = topy + length;
			bsp[depth] = new Room(new Pair(topx, topy), new Pair(botx, boty), true);
			rooms.add(depth); //add room to list
		}
	}

	/**
     * Connects each room created by generating an minimum spanning tree (MST) from the binary tree
     * 
     * <p>
     * Recursively call the function until the children of each parent node are connected
     * </p>
     */
	public void connect(int node1, int node2){ //connect the partitions
		if(node1==0&&node2==0) connect(1,2);
		if(!bsp[node1].isFilled()&&!bsp[node2].isFilled()){ //keep traversing tree until rooms are found
			connect(2*node1+1, 2*node1+2);
			connect(2*node2+1, 2*node2+2);
			bsp[node1].changeCond(true);
			bsp[node2].changeCond(true);
			connect(node1,node2);
		}
		else if(bsp[node1].isFilled()&&!bsp[node2].isFilled()){
			connect(2*node2+1, 2*node2+2);
			bsp[node2].changeCond(true);
			connect(node1,node2);
		}
		else if(!bsp[node1].isFilled()&&bsp[node2].isFilled()){
			connect(2*node1+1, 2*node1+2);
			bsp[node1].changeCond(true);
			connect(node1,node2);
		}
		else{ //room found, connect them in the minimal possible way
			int x1_1 = bsp[node1].getTopLeft().getX();
			int y1_1 = bsp[node1].getTopLeft().getY();
			int x1_2 = bsp[node1].getBotRight().getX();
			int y1_2 = bsp[node1].getBotRight().getY();
			int x2_1 = bsp[node2].getTopLeft().getX();
			int y2_1 = bsp[node2].getTopLeft().getY();
			int x2_2 = bsp[node2].getBotRight().getX();
			int y2_2 = bsp[node2].getBotRight().getY();

			Pair rec1 = new Pair(x1_1,y1_1), rec2 = new Pair(x2_1,y2_1), p1 = new Pair(x1_1,y1_1), p2 = new Pair(x2_1,y2_1);
			int distX=1000000, distY=1000000;
			for(int i=x1_1; i<x1_2; i++) //construct MST
				for(int j=y1_1; j<y1_2; j++){
					if(maze[i][j]==1)
						p1 = new Pair(i,j);
					for(int k=x2_1; k<x2_2; k++){
						for(int l=y2_1; l<y2_2; l++){
							if(maze[k][l]==1)
								p2 = new Pair(k,l);
							if(Math.abs(p1.distanceX(p2))+Math.abs(p1.distanceY(p2))<Math.abs(distX)+Math.abs(distY)){
								rec1 = p1;
								rec2 = p2;
								distX = p1.distanceX(p2);
								distY = p1.distanceY(p2);
							}
						}
					}
				}
			int cur = rec1.getX();
			if(distX<0) //draw path on map
				for(int i=0; i<Math.abs(distX)+1; i++){
					maze[rec1.getX()+i][rec1.getY()] = 1;
					cur = rec1.getX()+i;
				}
			else
				for(int i=0; i<Math.abs(distX); i++){
					maze[rec1.getX()-i][rec1.getY()] = 1;
					cur = rec1.getX()-i;
				}
			if(distY<0)
				for(int i=0; i<Math.abs(distY)+1; i++)
					maze[cur][rec1.getY()+i] = 1;
			else
				for(int i=0; i<Math.abs(distY); i++)
					maze[cur][rec1.getY()-i] = 1;
		}	
	}

	/**
     * Generates locations of every non-environment block
     */
	public void generate(){

		//end position of layer (always bottom right)
		maze[bsp[rooms.get(rooms.size()-1)].getBotRight().getX()][bsp[rooms.get(rooms.size()-1)].getBotRight().getY()] = 4;

		monsters = new ArrayList<Monster>();

		//generate monster locations
		int x, y;
		Room room;
		for(int i=0; i<rooms.size(); i++){
			if(i!=start){
				room = bsp[rooms.get(i)];
				for(int j=0; j<Math.max(room.getHeight()*room.getLength()/32,1); j++){
					x = (int) (Math.random() * (room.getBotRight().getX() - room.getTopLeft().getX())) + room.getTopLeft().getX();
					y = (int) (Math.random() * (room.getBotRight().getY() - room.getTopLeft().getY())) + room.getTopLeft().getY();
					if(maze[x][y]==1){
						int rand = (int)(Math.random() * 3);
						if(rand == 1)
							monsters.add(new Bat(x, y, (6+2*(level-1)), (2+(level-1))));
						else if(rand == 2)
							monsters.add(new Goblin(x, y, 10+2*(level-1), 1+(level-1)));
						else
							monsters.add(new Slime(x, y, 20+4*(level-1), 2+2*(level-1)));
						maze[x][y] = 5;
					}
				}
			}
		}

		//generate medkits
		for(int i=0; i<rooms.size()-1; i++){
			room = bsp[rooms.get(i+1)];
			if(Math.random()<0.3){
				int rnd = (int)(Math.random()*4);
				if(rnd==0){
					x = room.getTopLeft().getX();
					y = room.getTopLeft().getY();
					if(maze[x][y]==1)maze[x][y] = 6;
				}
				else if(rnd==1){
					x = room.getBotRight().getX();
					y = room.getTopLeft().getY();
					if(maze[x][y]==1)maze[x][y] = 6;
				}
				else if(rnd==2){
					x = room.getTopLeft().getX();
					y = room.getBotRight().getY();
					if(maze[x][y]==1)maze[x][y] = 6;
				}
				else if(rnd==3){
					x = room.getBotRight().getX();
					y = room.getBotRight().getY();
					if(maze[x][y]==1)maze[x][y] = 6;
				}
			}
		}

		//generate tubas
		for(int i=0; i<2; i++){
			int gen = (int)(Math.random()*rooms.size());
			room = bsp[rooms.get(gen)];
			int rnd = (int)(Math.random()*4);
			if(rnd==0){
				x = room.getTopLeft().getX();
				y = room.getTopLeft().getY();
				if(maze[x][y]==1)maze[x][y] = 7;
			}
			else if(rnd==1){
				x = room.getBotRight().getX();
				y = room.getTopLeft().getY();
				if(maze[x][y]==1)maze[x][y] = 7;
			}
			else if(rnd==2){
				x = room.getTopLeft().getX();
				y = room.getBotRight().getY();
				if(maze[x][y]==1)maze[x][y] = 7;
			}
			else if(rnd==3){
				x = room.getBotRight().getX();
				y = room.getBotRight().getY();
				if(maze[x][y]==1)maze[x][y] = 7;
			}	
		}
	}

	/**
     * Generates location of final boss
     */
	public void final_generate(){
		int x = maze.length/2;
		int y = maze[0].length/2;
		monsters = new ArrayList<Monster>();
		for(int i=0; i<3; i++) for(int j=0; j<3; j++) maze[x+i][y+j] = 5;
		monsters.add(new Dragon(x,y,100,10));
	}

	/**
     * Generates wall around path
     */
	public void walls(){ 
		for(int i=0; i<maze.length; i++){
			for(int j=0; j<maze.length; j++){
				if(maze[i][j]==1){
					if(maze[i-1][j-1]==0) maze[i-1][j-1] = 2;
					if(maze[i][j-1]==0) maze[i][j-1] = 2;
					if(maze[i][j+1]==0) maze[i][j+1] = 2;
					if(maze[i-1][j]==0) maze[i-1][j] = 2;
					if(maze[i+1][j]==0) maze[i+1][j] = 2;
					if(maze[i-1][j+1]==0) maze[i-1][j+1] = 2;
					if(maze[i+1][j-1]==0) maze[i+1][j-1] = 2;
					if(maze[i+1][j+1]==0) maze[i+1][j+1] = 2;
				}
			}
		}
	}

	/**
     * Displays the layer
     */
	public void display(){
		for(int i=0; i<maze.length; i++){
			for(int j=0; j<maze[0].length; j++)
				System.out.print(maze[i][j]);
			System.out.println();
		}
	}

	/**
     * Displays the number of rooms and the indexes of the binary tree containing rooms
     */
	public void getNumRooms(){
		for(int i=0; i<rooms.size(); i++)
			System.out.println(rooms.get(i));
		System.out.println("Total rooms: "+rooms.size());
		System.out.println("Number of monsters: "+monsters.size());
	}

	/**
     * Game probability mechanics
     */
	public boolean crit(){return Math.random() < ((double)player.health/player.total_health)*0.3;} //probability that player deals a critical hit
	public boolean p_miss(){return Math.random() > Math.max(((double)player.health/player.total_health),0.5);} //probability that player misses
	public boolean m_miss(){return Math.random() < ((double)player.health/player.total_health)*0.5;} //probability that monster misses
	public boolean p_heal(){return Math.random() < 1;} //probability that player will heal

	/**
     * Action function for hostile characters
     */
	public void turn(){
		took_damage = false;
		try{
			for(int i=0; i<monsters.size(); i++){
				if(monsters.get(i).name.equals("Bat")||monsters.get(i).name.equals("Dragon")){
					monsters.get(i).move(); //bats and dragon can move twice or move and attack on the same turn
						if(!monsters.get(i).move()) 
							monsters.get(i).attack(); 
				}
				else if(monsters.get(i).name.equals("Slime")) {
					if(turn % 2 == 0) //slimes can move every 2nd turn
						if(!monsters.get(i).move())
							monsters.get(i).attack();
					else monsters.get(i).attack();
				}
				else //goblins can move or attack every turn but not both
					if(!monsters.get(i).move()) 
						monsters.get(i).attack();
			}
		}catch(NullPointerException e){}
		player.heal();
		turn++;
	}

	/**
     * Find the Monster object in the list who is at (x,y) in the layer
     */
	public Monster findMonster(int x, int y){
		for(int i=0; i<monsters.size(); i++)
			if(monsters.get(i).getX()==x&&monsters.get(i).getY()==y)
				return monsters.get(i);
		return null;
	}

	/**
     * Player Class
     *
     * Sprites drawn by Janet Chen
     */
	class Player { 
		private int x, y, total_health, dmg, health; //x coordinate of player, y coordinate of the player, total health of the player, attack damage of the player, and current health of the player
		private int mod = 0; //variable used for movement
		private int medkits; //holds number of medkits the player has
		private char curdir; //current facing direction
		private Image image = null; //holds sprite of the player
		private boolean dead; //condition of whether the player is dead
		private int cnt = 0; //records number of terms left unharmed
		private int p_level, level_exp, cur_exp; //current player level, experience needed to level up, and current amount of experience
		private final int stat_scale = 2;

		public Player(int x, int y){ //default Player constructor
			this.x = x;
			this.y = y;
			System.out.println("PlayerX: " + x + " PlayerY: " + y);
			try {image = ImageIO.read(new File("playerfrontN.png"));}
	   		catch (IOException e){e.printStackTrace();}
	   		curdir = 'D';
	   		total_health = 20;
	 		health = 20;
	   		dmg = 4;
	   		medkits = 0;
			dead = false;
			p_level = 1;
			level_exp = p_level * 100;
			cur_exp = 0;
		}

		public Player(int x, int y, Player p){ //copy constructorfor Player but at a different location
			this.x = x;
			this.y = y;
			System.out.println("PlayerX: " + x + " PlayerY: " + y);
			try {image = ImageIO.read(new File("playerfrontN.png"));}
	   		catch (IOException e){e.printStackTrace();}
	   		curdir = 'D';
	   		this.total_health = p.total_health;
	 		this.health = p.health;
	   		this.dmg = p.dmg;
	   		this.medkits = p.medkits;
			dead = false;
			p_level = p.p_level;
			level_exp = p_level * 100;
			cur_exp = p.cur_exp;
		}

		public void addExp(int exp) { //adds experience to the player
			cur_exp += exp;
			while(levelUp());
		}

		public boolean levelUp() { //level up function, calculates stat changes
			if(cur_exp >= level_exp) {
				soundeffect("powerup.wav");
				p_level++;
				dmg++;
				total_health += stat_scale * p_level;
				health = total_health;
				cur_exp -= level_exp;
				level_exp = p_level * 100;
				System.out.println("GAME: Level Up! You are now level " + p_level + ".");
				output += "Level Up! You are now level " + p_level + ".\n";
				return true;
			}
			return false;
		}

		//movement functions
		public void moveR(){cnt++;check(x,y+1);if(maze[x][y+1]==1){maze[x][y]=1;y += 1;maze[x][y]=3;}mod++;mod%=2;setPlayerSprite('R');}
		public void moveL(){cnt++;check(x,y-1);if(maze[x][y-1]==1){maze[x][y]=1;y -= 1;maze[x][y]=3;}mod++;mod%=2;setPlayerSprite('L');}
		public void moveU(){cnt++;check(x-1,y);if(maze[x-1][y]==1){maze[x][y]=1;x -= 1;maze[x][y]=3;}mod++;mod%=2;setPlayerSprite('U');}
		public void moveD(){cnt++;check(x+1,y);if(maze[x+1][y]==1){maze[x][y]=1;x += 1;maze[x][y]=3;}mod++;mod%=2;setPlayerSprite('D');}
		public void check(int next_x, int next_y){
			if(maze[next_x][next_y]==7){
				soundeffect("powerup.wav");
				dmg += (int)(0.25*dmg);
				maze[next_x][next_y] = 1;
				System.out.println("GAME: Your attack is now "+dmg);
				output += "Your attack is now " + dmg + "\n";
			}
			if(maze[next_x][next_y]==6){
				medkits++;
				maze[next_x][next_y] = 1;
			}
			if(maze[next_x][next_y]==4){
				complete = true;
				maze[next_x][next_y] = 1;
			}
		}

		//sets the current sprite of the player
		public void setPlayerSprite(char c){
			curdir = c;
			if(c=='R'){
				if(mod==1){
					try {image = ImageIO.read(new File("playerleftR.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
				else{
					try {image = ImageIO.read(new File("playerleftL.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
			}
			else if(c=='L'){
				if(mod==1){
					try {image = ImageIO.read(new File("playerrightL.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
				else{
					try {image = ImageIO.read(new File("playerrightR.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
			}
			else if(c=='U'){
				if(mod==1){
					try {image = ImageIO.read(new File("playerbackR.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
				else{
					try {image = ImageIO.read(new File("playerbackL.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
			}
			else{ //down
				if(mod==1){
					try {image = ImageIO.read(new File("playerfrontR.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
				else{
					try {image = ImageIO.read(new File("playerfrontL.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
			}
		}

		public Image getSprite(){return image;} //returns player sprite
		public int getX(){return x;} //returns x coordinate
		public int getY(){return y;} //returns y coordinate

		public void heal(){ //player will naturally regenerate health over time if unharmed up to 75% of full health
			if(cnt==10){
				cnt = 0;
				if(p_heal()&&health<(int)((double)0.75*total_health)){
					int pts = (int) (Math.random()*2) + 1;
					health += pts;
					health = Math.min(health, (int)((double)0.75*total_health));
					System.out.println("GAME: You healed " + pts + " health points!");
					System.out.println("GAME: Current health: "+health+"/"+total_health);
					output += "You healed " + pts + " health points!\nCurrent health: "+health+"/"+total_health + "\n";
				}
			}
		}

		public void useMedkit(){ //use medkit function
			if(health<total_health&&medkits>0){
				health = Math.min(health+(int)(0.20*total_health),total_health);
				medkits--;
				System.out.println("GAME: You healed " + (int)(0.20*total_health) + " health points with medkit!");
				System.out.println("GAME: You have "+medkits+" medkits left.");
				System.out.println("GAME: Current health: "+health+"/"+total_health);
				output += "You healed " + (int)(0.20*total_health) + " health points with medkit!\n" + "You have " + medkits+" medkits left.\n" + "Current health: "+health+"/"+total_health + "\n";
			}
			else{ 
				System.out.println("GAME: You don't have any medkits");
				output += "You don't have any medkits.\n";
			}
		}

		public boolean isDead(){return dead;} //returns status of player

		public void takeDmg(int val){ //take damage function
			if(!dead){
				took_damage = true;
				health -= val;
				cnt = 0;
				System.out.println("GAME: Current health: "+health+"/"+total_health);
				output += "Current health: "+health+"/"+total_health+"\n";
				if(health<=0){ 
					soundeffect("lose.wav");
					dead = true; System.out.println("GAME: You died\nGame Over");
					output += "You died\nGame Over\n";
					try {image = ImageIO.read(new File("dead.png"));}
		   			catch (IOException e){e.printStackTrace();}
		   		}
			}
		}

		public void attack(){ //attack function
			soundeffect("attack.wav");
			cnt++;
			int _x, _y;
			if(curdir=='R'){_x=x;_y=y+1;}
			else if(curdir=='L'){_x=x;_y=y-1;}
			else if(curdir=='U'){_x=x-1;_y=y;}
			else{_x=x+1;_y=y;}
			if(maze[_x][_y]==5){
				Monster m;
				if(level!=0)	m = findMonster(_x,_y);
				else m = monsters.get(0);
				if(p_miss()){
					System.out.println("GAME: You missed!");
					output += "You missed!\n";
				}
				else{
					if(crit()){
						System.out.println("GAME: Critical Hit! You dealt "+2*dmg+" damage to "+m.name+"!");
						output += "Critical Hit! You dealt "+2*dmg+" damage to "+m.name+"!\n";
						m.takeDmg(2*dmg);
					}
					else{
						System.out.println("GAME: You dealt "+dmg+" damage to "+m.name+"!");
						output += "You dealt "+dmg+" damage to "+m.name+"!\n";
						m.takeDmg(dmg);
					}
				}
				if(m.dead()){
					System.out.println("GAME: You killed "+m.name+"!");
					output += "You killed " + m.name + "!\n";
					addExp(20);
					if(m.name.equals("Dragon")){
						monsters.remove(m);
						for(int i=0; i<3; i++)for(int j=0; j<3; j++)maze[m.getX()+i][m.getY()+j]=1;
						clear_game = true;
						soundeffect("powerup.wav");

					}
					else{
						monsters.remove(m);
						maze[_x][_y] = 1;
					}
				}
			}
		}

		//Miscellaneous functions
		public int getHealth() {return health;} //return health
		public int getTotalHealth() {return total_health;} //return total health
		public int getDmg() {return dmg;} //return attack damage
		public int getLevel() {return p_level;} //return current level
		public int getExp() {return cur_exp;} //return amount of experience
		public int getLevelExp() {return level_exp;} //return experience needed to level up
		public int getMedKits() {return medkits;} //return number of medkits player has
		public String toString(){
			return "Level: "+p_level+"\nExperience: "+cur_exp+"/"+level_exp+"\nCurrent Health: "+health+"/"+total_health+"\nAttack Damage: "+dmg+"\nMedkits: "+medkits;
		}
	}

	/**
     * Room class
     */
	class Room{
		private Pair topleft, botright; //contains the top left and bottom right coordinates of the room
		private boolean fill; //condition to fill the space in the layer

		public Room(Pair x1y1, Pair x2y2, boolean fill){ //Room constructor
			topleft = x1y1;
			botright = x2y2;
			this.fill = fill;
			if(fill)
				for(int i=topleft.getX(); i<=botright.getX(); i++)
					for(int j=topleft.getY(); j<=botright.getY(); j++)
						maze[i][j] = 1;
		}

		public void changeCond(boolean change){fill = change;} //change fill condition
		public boolean isFilled(){return fill;} //returns fill condition
		public Pair getTopLeft(){return topleft;} //return the top left coordinate
		public Pair getBotRight(){return botright;} //return the bottom right coordinate
		public int getHeight(){return Math.abs(topleft.getX()-botright.getX());} //return height of the room
		public int getLength(){return Math.abs(topleft.getY()-botright.getY());} //return length of the room
		public String toString(){return "Topx: "+topleft.getX()+" Topy: "+topleft.getY()+" Botx: "+botright.getX()+" Boty: "+botright.getY();}
	}

	/**
     * Pair Class, mimics that the pair variable in C++, stores a coordinate
     */
	class Pair{
		private int x, y; //x and y of coordinate
		public Pair(int x, int y){ //pair constructor
			this.x = x;
			this.y = y;
		}
		public int getX(){return x;} //return the x coordinate
		public int getY(){return y;} //return the y coordinate
		public int distanceX(Pair p){return x - p.getX();} //return the distance between the x coordinate of this pair and the x coordinate of another pair
		public int distanceY(Pair p){return y - p.getY();} //return the distance between the y coordinate of this pair and the y coordinate of another pair
	}

	//hostile player classes below

	/**
     * Monster Class for hostile character objects
     */
	abstract class Monster{
		private int x, y, health, total_health, dmg; 
		private boolean isDead;
		private String name;
		public Monster(int x, int y, int health, int dmg, String name){
			this.x = x;
			this.y = y;
			this.health = health;
			total_health = this.health;
			this.dmg = dmg;
			this.name = name;
			isDead = false;
		}
		public int proximity(){return Math.abs(x-player.getX())+Math.abs(y-player.getY());} //calculates the absolute distance between the player and the monster
		public int getXdist(){return x-player.getX();}
		public int getYdist(){return y-player.getY();}
		public boolean dead(){return isDead;}
		public abstract void setSprite(char c);
		public abstract Image getSprite();
		public abstract boolean move();
		public void attack(){
			if(proximity()==1){
				if(m_miss()){ 
					System.out.println("GAME: You dodged an attack from "+name+"!");
					output += "You dodged an attack from "+name+"!\n";
				}
				else{
					System.out.println("GAME: You took "+dmg+" damage from "+name+"!");
					output += "You took "+dmg+" damage from "+name+"!\n";
					player.takeDmg(dmg);
				}
			}
		}
		public void takeDmg(int val){
			if(proximity()==1){
				health-=val;
				if(health<=0){soundeffect("ding.wav");isDead=true;}
			}
		}
		public int getX(){return x;}
		public int getY(){return y;}
		public void setX(int _x){x = _x;}
		public void setY(int _y){y = _y;}
		public int getHealth(){return health;}
		public int getTotalHealth(){return total_health;}
		public String toString(){return name;}
	}
	/**
     * Bat Class, a hostile character 
     */
	class Bat extends Monster{
		private Image image = null;
		private int mod = 0;
		private boolean isChasing = false;
		public Bat(int x, int y, int health, int dmg){
			super(x, y, health, dmg, "Bat");
			try {image = ImageIO.read(new File("monster2frontD.png"));}
	   		catch (IOException e){e.printStackTrace();}
	   	}
		public boolean move(){ //move function to chase player when in range
			if(proximity()<=7||isChasing){ //checks if the proximity of the player is within 7 tiles or the bat is already chasing
				if(proximity()==1)
					return false;
				mod++;mod%=2;
				isChasing = true;
				if(getX()-player.getX()>0) setSprite('U');
				else setSprite('D');

				int distX = getXdist();
				int distY = getYdist();

				int _x=getX();
				int _y=getY();
				
				//a primitive follow function was implemented instead of breadth-first search due to the worst case scenario of O(NM), where N is the number of nodes, and M is the number of monsters, which would be too slow 
				if(distX!=0&&distY!=0){
					if((int)(Math.random()*2)==0){
						if(maze[getX()-1][getY()]==1&&distX>0||maze[getX()+1][getY()]==1&&distX<0){
							_x = distX>0 ? getX()-1 : getX()+1;
							_y = getY();
						}
						else if(maze[getX()][getY()-1]==1&&distY>0||maze[getX()][getY()+1]==1&&distY<0){
							_x = getX();
							_y = distY>0 ? getY()-1 : getY()+1;
						}
					}else{
						if(maze[getX()][getY()-1]==1&&distY>0||maze[getX()][getY()+1]==1&&distY<0){
							_x = getX();
							_y = distY>0 ? getY()-1 : getY()+1;
						}
						else if(maze[getX()-1][getY()]==1&&distX>0||maze[getX()+1][getY()]==1&&distX<0){
							_x = distX>0 ? getX()-1 : getX()+1;
							_y = getY();
						}
					}
				}
				else if(distX!=0&&distY==0){
					if(distX>0) _x = maze[getX()-1][getY()]==1 ? getX()-1 : getX();
					else _x = maze[getX()+1][getY()]==1 ? getX()+1 : getX();
					_y = getY();
				}
				else if(distX==0&&distY!=0){
					if(distY>0) _y = maze[getX()][getY()-1]==1 ? getY()-1 : getY();
					else _y = maze[getX()][getY()+1]==1 ? getY()+1 : getY();
					_x = getX();
				}
				System.out.println("Bat moved from ("+getX()+","+getY()+") to ("+_x+","+_y+")");
				maze[getX()][getY()]=1;
				setX(_x);setY(_y);
				maze[getX()][getY()]=5;
				return true;
			}
			return false;
		}

		public void setSprite(char c){
			if(c=='U'){
				if(mod==1){
					try {image = ImageIO.read(new File("monster2backU.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
				else{
					try {image = ImageIO.read(new File("monster2backD.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
			}
			else{ //down
				if(mod==1){
					try {image = ImageIO.read(new File("monster2frontU.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
				else{
					try {image = ImageIO.read(new File("monster2frontD.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
			}
		}
		public Image getSprite(){return image;}
	}
	/**
     * Slime Class, a hostile character 
     */
	class Slime extends Monster{
		private Image image = null;
		private int mod = 0;
		private boolean isChasing = false;
		public Slime(int x, int y, int health, int dmg){
			super(x, y, health, dmg, "Slime");
			try {image = ImageIO.read(new File("monster3frontD.png"));} 
					catch (IOException e){e.printStackTrace();}
				}
		public boolean move(){ 
			if(proximity()<=5||isChasing){ //checks if the proximity of the player is within 5 tiles or the slime is already chasing
				if(proximity()==1)
					return false;
				mod++;mod%=2;
				isChasing = true;
				if(getX()-player.getX()>0) setSprite('U');
				else setSprite('D');

				int distX = getXdist();
				int distY = getYdist();

				int _x=getX();
				int _y=getY();

				if(distX!=0&&distY!=0){
					if((int)(Math.random()*2)==0){
						if(maze[getX()-1][getY()]==1&&distX>0||maze[getX()+1][getY()]==1&&distX<0){
							_x = distX>0 ? getX()-1 : getX()+1;
							_y = getY();
						}
						else if(maze[getX()][getY()-1]==1&&distY>0||maze[getX()][getY()+1]==1&&distY<0){
							_x = getX();
							_y = distY>0 ? getY()-1 : getY()+1;
						}
					}else{
						if(maze[getX()][getY()-1]==1&&distY>0||maze[getX()][getY()+1]==1&&distY<0){
							_x = getX();
							_y = distY>0 ? getY()-1 : getY()+1;
						}
						else if(maze[getX()-1][getY()]==1&&distX>0||maze[getX()+1][getY()]==1&&distX<0){
							_x = distX>0 ? getX()-1 : getX()+1;
							_y = getY();
						}
					}
				}
				else if(distX!=0&&distY==0){
					if(distX>0) _x = maze[getX()-1][getY()]==1 ? getX()-1 : getX();
					else _x = maze[getX()+1][getY()]==1 ? getX()+1 : getX();
					_y = getY();
				}
				else if(distX==0&&distY!=0){
					if(distY>0) _y = maze[getX()][getY()-1]==1 ? getY()-1 : getY();
					else _y = maze[getX()][getY()+1]==1 ? getY()+1 : getY();
					_x = getX();
				}
				System.out.println("Slime moved from ("+getX()+","+getY()+") to ("+_x+","+_y+")");
				maze[getX()][getY()]=1;
				setX(_x);setY(_y);
				maze[getX()][getY()]=5;
				return true;
			}
			return false;
		}

		public void setSprite(char c){
			if(c=='U'){
				if(mod==1){
					try {image = ImageIO.read(new File("monster3backU.png"));}
							catch (IOException e){e.printStackTrace();}
				}
				else{
					try {image = ImageIO.read(new File("monster3backD.png"));}
							catch (IOException e){e.printStackTrace();}
				}
			}
			else{ //down
				if(mod==1){
					try {image = ImageIO.read(new File("monster3frontU.png"));}
							catch (IOException e){e.printStackTrace();}
				}
				else{
					try {image = ImageIO.read(new File("monster3frontD.png"));}
							catch (IOException e){e.printStackTrace();}
				}
			}
		}
		public Image getSprite(){return image;}
	}
	/**
     * Goblin Class, a hostile character 
     */
	class Goblin extends Monster{
		private Image image = null;
		private int mod = 0;
		private boolean isChasing = false;
		public Goblin(int x, int y, int health, int dmg){
			super(x, y, health, dmg, "Goblin");
			try {image = ImageIO.read(new File("monster1frontN.png"));}
	   		catch (IOException e){e.printStackTrace();}
		}
		public boolean move(){ 
			if(proximity()<=6||isChasing){ //checks if the proximity of the player is within 6 tiles or the goblin is already chasing
				if(proximity()==1)
					return false;
				mod++;mod%=2;
				isChasing = true;
				if(getX()-player.getX()>0) setSprite('U');
				else setSprite('D');

				int distX = getXdist();
				int distY = getYdist();

				int _x=getX();
				int _y=getY();
				
				if(distX!=0&&distY!=0){
					if((int)(Math.random()*2)==0){
						if(maze[getX()-1][getY()]==1&&distX>0||maze[getX()+1][getY()]==1&&distX<0){
							_x = distX>0 ? getX()-1 : getX()+1;
							_y = getY();
						}
						else if(maze[getX()][getY()-1]==1&&distY>0||maze[getX()][getY()+1]==1&&distY<0){
							_x = getX();
							_y = distY>0 ? getY()-1 : getY()+1;
						}
					}else{
						if(maze[getX()][getY()-1]==1&&distY>0||maze[getX()][getY()+1]==1&&distY<0){
							_x = getX();
							_y = distY>0 ? getY()-1 : getY()+1;
						}
						else if(maze[getX()-1][getY()]==1&&distX>0||maze[getX()+1][getY()]==1&&distX<0){
							_x = distX>0 ? getX()-1 : getX()+1;
							_y = getY();
						}
					}
				}
				else if(distX!=0&&distY==0){
					if(distX>0) _x = maze[getX()-1][getY()]==1 ? getX()-1 : getX();
					else _x = maze[getX()+1][getY()]==1 ? getX()+1 : getX();
					_y = getY();
				}
				else if(distX==0&&distY!=0){
					if(distY>0) _y = maze[getX()][getY()-1]==1 ? getY()-1 : getY();
					else _y = maze[getX()][getY()+1]==1 ? getY()+1 : getY();
					_x = getX();
				}
				System.out.println("Goblin moved from ("+getX()+","+getY()+") to ("+_x+","+_y+")");
				maze[getX()][getY()]=1;
				setX(_x);setY(_y);
				maze[getX()][getY()]=5;
				return true;
			}
			return false;
		}
		public void setSprite(char c){
			if(c=='U'){
				if(mod==1){
					try {image = ImageIO.read(new File("monster1backR.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
				else{
					try {image = ImageIO.read(new File("monster1backL.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
			}
			else{ //down
				if(mod==1){
					try {image = ImageIO.read(new File("monster1frontR.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
				else{
					try {image = ImageIO.read(new File("monster1frontL.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
			}
		}
		public Image getSprite(){return image;}
	}
	/**
     * Dragon Class, a hostile character 
     */
	class Dragon extends Monster{
		private Image image = null;
		private int mod = 0;
		private boolean isChasing = false;
		public Dragon(int x, int y, int health, int dmg){
			super(x, y, health, dmg, "Dragon");
			try {image = ImageIO.read(new File("dragon.png"));}
	   		catch (IOException e){e.printStackTrace();}
		}
		@Override
		public int proximity(){ //proximity function is overwritten since the dragon takes up a 3x3 area
			int min=super.proximity();
			for(int i=0;i<3;i++)for(int j=0;j<3;j++)min=Math.min(Math.abs(getX()+i-player.getX())+Math.abs(getY()+j-player.getY()),min);
			return min;
		}
		public boolean move(){ //checks if the proximity of the player is within 10 tiles or the dragon is already chasing
			if(proximity()<=10||isChasing){ 
				if(proximity()==1)
					return false;
				mod++;mod%=2;
				isChasing = true;
				if(getX()-player.getX()>0) setSprite('U');
				else setSprite('D');

				int distX = getXdist();
				int distY = getYdist();

				int _x=getX();
				int _y=getY();

				for(int i=0; i<3; i++) for(int j=0; j<3; j++) maze[getX()+i][getY()+j] = 1;
				
				if(distX!=0&&distY!=0){
					if((int)(Math.random()*2)==0){
						if(maze[getX()-1][getY()]==1&&distX>0||maze[getX()+3][getY()]==1&&distX<0){
							_x = distX>0 ? getX()-1 : getX()+1;
							_y = getY();
						}
						else if(maze[getX()][getY()-1]==1&&distY>0||maze[getX()][getY()+3]==1&&distY<0){
							_x = getX();
							_y = distY>0 ? getY()-1 : getY()+1;
						}
					}else{
						if(maze[getX()][getY()-1]==1&&distY>0||maze[getX()][getY()+3]==1&&distY<0){
							_x = getX();
							_y = distY>0 ? getY()-1 : getY()+1;
						}
						else if(maze[getX()-1][getY()]==1&&distX>0||maze[getX()+3][getY()]==1&&distX<0){
							_x = distX>0 ? getX()-1 : getX()+1;
							_y = getY();
						}
					}
				}
				else if(distX!=0&&distY==0){
					if(distX>0) _x = maze[getX()-1][getY()]==1 ? getX()-1 : getX();
					else _x = maze[getX()+3][getY()]==1 ? getX()+1 : getX();
					_y = getY();
				}
				else if(distX==0&&distY!=0){
					if(distY>0) _y = maze[getX()][getY()-1]==1 ? getY()-1 : getY();
					else _y = maze[getX()][getY()+3]==1 ? getY()+1 : getY();
					_x = getX();
				}
				System.out.println("Dragon moved from ("+getX()+","+getY()+") to ("+_x+","+_y+")");
				setX(_x);setY(_y);
				for(int i=0; i<3; i++) for(int j=0; j<3; j++) maze[getX()+i][getY()+j] = 5;
				return true;
			}
			return false;
			
		}
		public Image getSprite(){return image;}
		public void setSprite(char c){
			if(c=='U'){
				if(mod==1){
					try {image = ImageIO.read(new File("dragon2.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
				else{
					try {image = ImageIO.read(new File("dragon.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
			}
			else{ //down
				if(mod==1){
					try {image = ImageIO.read(new File("dragon2.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
				else{
					try {image = ImageIO.read(new File("dragon.png"));}
	   				catch (IOException e){e.printStackTrace();}
				}
			}
		}
	}

	/**
     * AudioThread object for sound effects
     */
	class AudioThread extends Thread {
		public void run(String path) {
			try {
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(path));
				Clip clip = AudioSystem.getClip();
				clip.open(inputStream);
				clip.loop(0);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
     * Play sound effect method
     */
	public void soundeffect(String path) {
		sound_effect.run(path);
	}
}