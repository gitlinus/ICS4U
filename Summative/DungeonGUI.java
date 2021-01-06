import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import javax.imageio.*;

/**
 * Class generates a GUI that the player can interact with the play the game
 * @author Linus Chen
 * @author Edward Shen
 * @version June 18, 2019
 */

public class DungeonGUI extends JFrame implements KeyListener{

	MazeGenerator n; //currennt dungeon level
	final int width = 30, height = 30; //dimensions of each tile 
	final int max_level = 5; //number of layers in the game
	int level = max_level-1; //records the current dungeon layer
	LoadScreen loader = new LoadScreen(600, 600); //loadscreen to be set visible/invisible as desired
	JTextArea dialog; //holds dialog of the game
	JFrame text; //another Jframe for the dialog

	/**
	* Constructor for class
	*/
	public DungeonGUI(){
		super("Linus's Specialty Tubas");
		//Show title screen
		TitleScreen t = new TitleScreen();
		t.setVisible(true);
		while(!t.hasStarted()){System.out.print("");}
		
		//Show load screen while level is generating
		loader.setVisible(true);
		n = new MazeGenerator(100, max_level-level);
		loader.setVisible(false);

		text = new JFrame("Dialog");
		dialog = new JTextArea(16, 58);
		dialog.setEditable(false);
		JScrollPane scroll = new JScrollPane(dialog);
		scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		text.add(scroll);
		text.setSize(400,500);
		text.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		text.setResizable(false);
		text.setVisible(true);
		
		// 1... Enable key listener for movement
        addKeyListener (this);
        setFocusable (true);
        setFocusTraversalKeysEnabled (false);

		JPanel content = new JPanel ();        // Create a content pane
        content.setLayout (new BorderLayout ()); // Use BorderLayout for panel
        
        DrawArea board = new DrawArea (600, 600); 
        content.add (board, "North");
        board.addKeyListener(this);

        // set JFrame attributes
        setContentPane(content);
        pack();
		setSize(600,600);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	/**
	* LoadScreen class
	*/
	class LoadScreen extends JFrame //Loadscreen to display when parts of game are generating
	{
		public LoadScreen (int width, int height) {
			super("Linus's Speciality Tubas");
			setContentPane(new Loader(width,height));
			setSize(width,height);
			setResizable(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocationRelativeTo(null);
		}
		class Loader extends JPanel{
			int w, h;
			public Loader(int width, int height){
				w = width; h = height;
				setSize(width, height);
			}
			public void paintComponent (Graphics g) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, w, h);
				g.setColor(Color.WHITE);
				g.drawString("Loading...", 300, 300);
			}
		}
	}

	private boolean keys_test[] = new boolean[0x10000]; // friendly key repeat tester

	public void keyTyped (KeyEvent e){}
    public void keyReleased (KeyEvent e){
    	int key = e.getKeyCode();
		if (0 <= key && key <= 0xFFFF) {
			if (!keys_test[key])
				return;
			keys_test[key] = false;
		}
    }
    public void keyPressed (KeyEvent e)  // handle cursor keys 
    {	
    	if(!n.player.isDead()){
	    	boolean valid = false;
	        int key = e.getKeyCode();
	        if (0 <= key && key <= 0xFFFF) {
				if (n.took_damage && keys_test[key]) //prevents user from holding movement key after damage has been taken
					return;
				keys_test[key] = true;
			}
			if (keys_test[key]) {
				JOptionPane information;
		        switch (key)
		        {
		            case KeyEvent.VK_UP:
		            n.player.moveU();
		            valid = true;
		            break;
		            case KeyEvent.VK_LEFT:
		            n.player.moveL();
		            valid = true;
		            break;
		            case KeyEvent.VK_DOWN:
		            n.player.moveD();
		            valid = true;
		            break;
		            case KeyEvent.VK_RIGHT:
		            n.player.moveR();
		            valid = true;
		            break;
		            case KeyEvent.VK_X:
		            n.player.attack();
		            valid = true;
		            break;
		            case KeyEvent.VK_Z:
		            n.player.useMedkit();
		            valid = true;
		            break;
		            case KeyEvent.VK_S: //show stats
		            information = new JOptionPane(n.player.toString(), JOptionPane.INFORMATION_MESSAGE);
					JDialog stats = information.createDialog("Player Statistics");
					stats.setAlwaysOnTop(true);
					stats.setVisible(true);
		            break;
		            case KeyEvent.VK_H: //help
		            information = new JOptionPane("UP ARROW - move up\nDOWN ARROW - move down\nLEFT ARROW - move left\nRIGHT ARROW - move right\nX - attack\nZ - use medkit\nS - show player statistics\nH - help", JOptionPane.INFORMATION_MESSAGE);
					JDialog help = information.createDialog("Player Controls");
					help.setAlwaysOnTop(true);
					help.setVisible(true);
		            break;
		        }
		        if(valid){
		        	dialog.setText(n.output);
			        repaint();
			        n.turn();
					dialog.setText(n.output);
					if(n.clear_game) {
						JOptionPane.showMessageDialog(null, "Congratulations! You cleared the game!", "Victory", JOptionPane.INFORMATION_MESSAGE);
						this.dispose();
						text.dispose();
						System.exit(0);
					} else if(n.player.isDead()) {
						JOptionPane.showMessageDialog(null, "You died!", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
						this.dispose();
						text.dispose();
						System.exit(0);
					}
			        if(n.complete){
			        	level--;
			        	if(level!=0){
			        		loader.setVisible(true);
			        		setVisible(false);
			        		n = new MazeGenerator(100,n.player,max_level-level);
			        		loader.setVisible(false);
			        		setVisible(true);
			        	}
			        	else
			        		n = new MazeGenerator(n.player);
			        	dialog.setText(n.output);
			        	repaint();
			        }
			    }
			}
		}
    }


    /**
	* DrawArea class
	*/
	class DrawArea extends JPanel //provides area where Graphics can draw things
    {
        public DrawArea (int width, int height)
        {
            this.setPreferredSize (new Dimension (width, height)); // size
        }

        public void paintComponent (Graphics g)
        {
        	Image wall = null;
        	try {wall = ImageIO.read(new File("wall.png"));}
	   		catch (IOException e){e.printStackTrace();}
	   		Image path = null;
	   		try {path = ImageIO.read(new File("floor.png"));}
	   		catch (IOException e){e.printStackTrace();}
	   		Image exit = null;
	   		try {exit = ImageIO.read(new File("leave.png"));}
	   		catch (IOException e){e.printStackTrace();}
	   		Image medkit = null;
			try {medkit = ImageIO.read(new File("medkit.png"));}
	   		catch (IOException e){e.printStackTrace();}
	   		Image tuba = null;
			try {tuba = ImageIO.read(new File("tuba.png"));}
	   		catch (IOException e){e.printStackTrace();}
		    Image player = n.player.getSprite();
		    
            for (int row = 0 ; row < 20; row++)// number of rows
	        {
	            for (int col = 0 ; col < 20; col++)// length of first row
	            {
	            	int x = 0, y = 0, flag = 0;
	            	if(row <= 10 && col <= 10){
	            		x = n.player.getX() - (10 - row);
	            		y = n.player.getY() - (10 - col);
	            	}
	            	else if(row <= 10 && col > 10){
	            		x = n.player.getX() - (10 - row);
	            		y = n.player.getY() + (col - 10);
	            	}
	            	else if(row > 10 && col <= 10){
	            		x = n.player.getX() + (row - 10);
	            		y = n.player.getY() - (10 - col);
	            	}
	            	else if(row > 10 && col > 10){
	            		x = n.player.getX() + (row - 10);
	            		y = n.player.getY() + (col - 10);
	            	}
	            	if(flag==0){
		            	if(x < 0 || y < 0 || y >= n.maze[0].length || x >= n.maze.length){
		        			g.setColor(Color.black);
		        			g.fillRect(col * height, row * width, width, height);
		            	}
		        		else{
		            		if (n.maze [x] [y] == 0) { // empty
			                    g.setColor(Color.black);
		        				g.fillRect(col * height, row * width, width, height);
			                }
			                else if (n.maze [x] [y] == 1) // path
			                    g.drawImage(path, col * height, row * width, this);
			                else if (n.maze [x] [y] == 2) // wall
			                    g.drawImage(wall, col * height, row * width, this);
			                else if (n.maze [x] [y] == 3) // player
			                	g.drawImage(player, col * height, row * width, this);
			                else if (n.maze [x] [y] == 4){ // next layer
			                	g.drawImage(exit, col * height, row * width, this);
			                }
			                else if (n.maze [x] [y] == 5){ // monster
		                		for(int i=0; i<n.monsters.size(); i++)
			                		if(n.monsters.get(i).getX()==x&&n.monsters.get(i).getY()==y){
			                			Image monster = n.monsters.get(i).getSprite();
			                			g.drawImage(monster, col * height, row * width, this);

			                			if(!n.monsters.get(i).toString().equals("Dragon")){
				                			int health_bar_width = width;
											int health_bar_height = 5;
											g.setColor(new Color(50, 60, 60));
											g.fillRect(col * height, row * width, health_bar_width, health_bar_height);
											g.setColor(Color.RED);
											g.fillRect(col * height, row * width, (int)(health_bar_width * (double)n.monsters.get(i).getHealth()/n.monsters.get(i).getTotalHealth()), health_bar_height);
										}
										else{
											int health_bar_width = width;
											int health_bar_height = 5;
											g.setColor(new Color(50, 60, 60));
											g.fillRect(col * height, row * width, health_bar_width * 3, health_bar_height);
											g.setColor(Color.RED);
											g.fillRect(col * height, row * width, (int)(health_bar_width * (double)n.monsters.get(i).getHealth()/n.monsters.get(i).getTotalHealth()) * 3, health_bar_height);
										}
			                		}
			                }
			                else if(n.maze [x] [y] == 6) //medkit
			                	g.drawImage(medkit, col * height, row * width, this);
			                else if(n.maze [x] [y] == 7) //tuba
			                	g.drawImage(tuba, col * height, row * width, this);
		        		}
		        	}
	            }
	        }
	        // draw health bar
			final int health_bar_width = 200;
			final int health_bar_height = 10;
			final int health_bar_border_width = 210;
			final int health_bar_border_height = 20;
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, health_bar_border_width, health_bar_border_height);
			g.setColor(new Color(50, 60, 60));
			g.fillRect(0, 5, health_bar_width, health_bar_height);
			g.setColor(Color.RED);
			g.fillRect(0, 5, (int)(health_bar_width * (double)n.player.getHealth()/n.player.getTotalHealth()), health_bar_height);

			// draw exp bar
			g.setColor(new Color(50, 60, 60));
			g.fillRect(0, health_bar_border_height, health_bar_border_width, health_bar_height);
			g.setColor(Color.YELLOW);
			g.fillRect(0, health_bar_border_height, (int)(health_bar_border_width * (double)n.player.getExp()/n.player.getLevelExp()), health_bar_height);

			// draw level and medkit count
			g.setColor(Color.WHITE);
			if(level!=0) g.drawString("Layer " + n.level, 500, 20);
			else g.drawString("Final Layer ", 500, 20);
			g.drawString("Level " + n.player.getLevel(), 5, 50);
			g.drawString("Medkits: " + n.player.getMedKits(), 5, 70);
        }//paint component
    }//End of DrawArea

    /**
	* This method plays the game soundtrack, Rite of Spring by Stravinsky, on repeat
	*/
    public static void play(String path) {
		new Thread() {
			@Override
			public void run() {
				try {
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(path));
			        Clip clip = AudioSystem.getClip();
			        clip.open(inputStream);
			        clip.loop(Clip.LOOP_CONTINUOUSLY);

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}.start();
	}

	/**
	* Title Screen
	*/
	class TitleScreen extends JFrame implements ActionListener{
		JButton play_btn, instruction_btn;
		String instructions = "Instructions\n\n";
		boolean start = false;
		public TitleScreen() {
			super("Linus's Specialty Tubas");
			play_btn = new JButton("Play!");
			instruction_btn = new JButton("Instructions");
			play_btn.addActionListener(this);
			instruction_btn.addActionListener(this);
			initInstructions();
			setLayout(new GridLayout(0, 1));
			add(play_btn);
			add(instruction_btn);
			pack();
			setSize(600,600);
			setResizable(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocationRelativeTo(null);
		}
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == play_btn) {
				this.setVisible(false);
				start = true;
			}
			else if(e.getSource() == instruction_btn) {
				JOptionPane.showMessageDialog(null, instructions, "Instructions", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		public boolean hasStarted(){return start;}
		private void initInstructions() {
			instructions+="Linus has lost his tubas in a dungeon! He needs to find his tubas and defeat the dragon to get out!\n";
			instructions+="Beware! There are many monsters that would like to eat Linus! Help him defeat the monsters and go to the lowest floor in the dungeon to get out!\n";
			instructions+="Staircases are located in the bottom left of each layer. Going down one staircase will prevent Linus from heading back up!\n";
			instructions+="The red bar at the top represents Linus's health. Once it reaches zero, it's game over!\n";
			instructions+="Linus needs to gain experience, medical kits, and collect tubas in order to strengthen himself to defeat the dragon!\n";
			instructions+="Experience can be gained by defeating monsters. Filling the yellow bar at the top will grant Linus an increase in level, gaining more strength!\n";
			instructions+="Medkits can be collected and used to restore 20% of Linus's health. Use them in combat to stay alive!\n";
			instructions+="Tubas grant Linus a 25% damage bonus! They're very useful in quickly increasing strength.\n";
			instructions+="Linus will miss more often at lower health, and will deal more critical damage at higher health. Make sure to keep Linus healthy!\n";
			instructions+="Linus will slowly regenerate health when not fighting, up to 75%. Take advantage of this to not use medkits too often.\n";
			instructions+="\nGet out there and help Linus exit the dungeon!\n\n";
			instructions+="UP ARROW - move up\nDOWN ARROW - move down\nLEFT ARROW - move left\nRIGHT ARROW - move right\nX - attack\nZ - use medkit\nS - show player statistics\nH - help";
		}
	}

	/**
	* Main method
	*/
	public static void main(String[] args){
		DungeonGUI dungeon = new DungeonGUI(); //make new object
		dungeon.setVisible(true); //set it to be visible
		play("stravinsky.wav");
	}	
}