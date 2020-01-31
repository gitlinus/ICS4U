/*
* Linus Chen
* Mexico Game
* ICS4U1
*/

/*
Rules implemented:
The number of rolls the lead player takes (up to 3) determines the number of rolls each subsequent player can take
If there is a tie between two highest scoring players, the left-most player is lead player for next round
If there is a tie between two lowest scoring players, both players lose a life
If all three players get the same score, no one loses lives, the round is played again, lead player stays the same
if all players roll different scores, lowest scoring player loses a life
Last player alive is the winner of the game

In a case where lead roller rolls a Mexico (21) on the first roll, subsequent players will receive 2 rerolls each 
*/

import java.util.*;
public class CLMexico {

	public static Scanner sc;
	//each player is represented by an index: 0, 1, 2, correspond to players 1, 2, 3, respectively
	public static int[] lives = new int[3]; //keep track of lives left
	public static int[] scores = new int[3]; //keep track of scores of each player
	public static int current = 1; //current player playing
	public static int played = 0; //number of players that played in the round
	public static int rolls = 2; //number of rolls players are allowed to take
	public static int leadReRolls = 2; //numbers of rolls set by lead player
	public static int leadPlayer = 1; //keeps track of the lead player of round

	public static void Rolls(int cur){ //Roll function, rolls dice and determines score for player "cur"
		if(Alive(cur)){ //check if the player is currently alive
			int die1 = (int) (Math.random() * 6) + 1; //roll the dice
			int die2 = (int) (Math.random() * 6) + 1;
			int score = Math.max(die1,die2)*10 + Math.min(die1,die2); //calculate score

			//set the player's score
			if(score==21) score = scores[cur-1] = score + 200; //if score is 21
			else if(score%11==0) score = scores[cur-1] = score + 100; //if roll is doubles
			else scores[cur-1] = score; 

			System.out.printf("Player %d rolls %d %d. Score is: %d\n", cur, die1, die2, score); //print their roll and their score
		
			char c;
			if(score!=221){ //a player would only want to reroll if they did not already roll a Mexico (21)
				if(rolls>0){ //check if player has rerolls left
					System.out.printf("Rerolls remaining: %d\n", rolls); 
					System.out.printf("Reroll? (y/n): ");
					c = sc.next().charAt(0);
					if(c=='y'){ //player wants reroll
						rolls -= 1;
						Rolls(cur);
					}
					else if(c=='n'&&played==0){ //check if player is currently lead, if player doesn't want reroll, he sets number of rerolls for subsequent players that round
						rolls = 2 - rolls;
						leadReRolls = rolls;
						leadPlayer = current;
						played++; 
					}
					else if(c=='n'){ //player is not lead player and does not want to reroll
						rolls = leadReRolls; //reset to lead player reroll
						played++;
					}
				}
				else if(rolls==0){ //no rerolls left
					if(played==0){ //if lead player of round
						rolls = 2 - rolls;  //lead determines how many rerolls subsequent players get
						leadReRolls = rolls;
					}
					else rolls = leadReRolls; //was not lead player, reset number of rerolls for next player
					played++;
				}
			}
			else{ //player rolled a mexico
				if(played==0&&rolls==2){ //check if it was lead player's first try
					leadReRolls = 2; //subsequent players get 2 rolls each
					rolls = 2;
					leadPlayer = current;
				}
				else if(played==0){ //lead player rolled a 21 but not on first try
					rolls = 2 - rolls;
					leadReRolls = rolls; //set rolls for next players
					leadPlayer = current;
				}
				else rolls = leadReRolls; //not lead player, reset rerolls for next player
				played++;
			}
		}
	}

	public static int findMax(){ //finds the index of the maximum value of the score array (used to determine highest scoring player of round)
		int idx = 0;
		int max = -1; //lower than lowest possible score (ensures that max of array will be found)
		for(int i=0; i<3; i++)
			if(max < scores[i] && Alive(i+1)){ //check player score and if currently alive
				max = scores[i];
				idx = i;
			}
		return idx;
	}

	public static int findMin(){ //finds the index of the minimum value of the score array (used to determine lowest scoring player of round)
		int idx = 0;
		int min = 300; //beyond range of possible scores (ensures min of array will be found)
		for(int i=0; i<3; i++)
			if(min > scores[i] && Alive(i+1)){ //check player score and if currently alive
				min = scores[i];
				idx = i; 
			}
		return idx; 
	}

	public static void TakeLives(int idx){ //used in an event if 2 people have the same minimum score when 3 people are playing (both lose lives)
		for(int i=0; i<3; i++)
			if(scores[i]==scores[idx]){
				lives[i]-=1;
				System.out.printf("Player %d loses 1 life, has %d remaining life/lives\n", i+1, lives[i]);
			}
	}

	public static int Count(int idx){ //returns number of indexes that have the same value as the given index
		int counter = 0;
		for(int i=0; i<3; i++)
			if(scores[i]==scores[idx]) counter++;
		return counter;
	}

	public static void Round(){
		int mindex = findMin(); //find lowest scorer
		int maxdex = findMax(); //find highest scorer
		if(numAlive()==3){ //if 3 people are currently playing
			if(Count(mindex)==1){ //1 person got lowest score
				System.out.printf("Player %d loses this round, -1 life.\n", mindex+1);
				lives[mindex] -= 1; //played loses life
				System.out.printf("Played %d has %d remaining life/lives.\n\n", mindex+1, lives[mindex]);
				current = maxdex + 1; //set the lead player for next round
			}
			if(Count(mindex)==2){ //2 people got the lowest score
				System.out.printf("Two players with lowest score. Both lose lives\n\n");
				TakeLives(mindex); //take 1 life from both players
				current = maxdex + 1; //set the lead player for next round
			}
			if(Count(mindex)==3){ //3 people got the same score
				System.out.printf("Tie. Round is replayed. No one loses lives\n\n"); //no one loses lives
				current = leadPlayer; //reset current player to lead player of round
			}
			System.out.printf("Player %d is lead player for next round.\n\n", current);
			played = 0; //reset played counter
		}
		else if(numAlive()==2){ //if only 2 players left
			if(Count(mindex)==2){
				System.out.printf("Tie. Round is replayed. No one loses lives\n\n");
				current = leadPlayer;
			}
			else{
				System.out.printf("Player %d loses this round, -1 life.\n", mindex+1);
				lives[mindex] -= 1;
				System.out.printf("Player %d has %d remaining life/lives.\n\n", mindex+1, lives[mindex]);
				current = maxdex + 1;
			}
			played = 0;
		}
		rolls = 2; //reset number of rerolls for next lead player
	}

	public static boolean Alive(int player){ //check if player is alive
		if(lives[player-1]==0) return false;
		else return true;
	}

	public static int numAlive(){ //check how many players are currently alive
		int counter = 0;
		for(int i=1; i<=3; i++)
			if(Alive(i)) counter++;
		return counter;
	}	

	public static void Run(){ //Function to run the game
		System.out.printf("Player 1 is leader for first round.\n\n");
		while(numAlive()!=1){ //check if there's more than 1 player alive
			Rolls(current); //Roll
			current++; //next player
			//System.out.printf("Played: %d\n", played);
			if(numAlive()==3&&played==3) //Round checker for 3 players
				Round();
			if(numAlive()==2&&played==2) //Round checker for 2 players
				Round();
			if(current==4)
				current = 1; //reset to left-most player
		}
		System.out.printf("Player %d wins the game!!!\n\n", findMax()+1); //player wins game
	}

	public static void main(String[] args){
		sc = new Scanner(System.in);
		System.out.printf("Mexico Dice Game\n\n");
		System.out.printf("How many lives?: "); //asks user to enter how many lives
		int n = sc.nextInt();
		if(n <= 0) System.out.println("Game closed");
		else {
			for(int i=0; i<3; i++) //sets number of lives for the game
				lives[i] = n;
			Run();
		}
	}
}