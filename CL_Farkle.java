/*
Linus Chen
ICS4U1
Farkle Dice game
Supports any number of players >= 2
*/

import java.util.*;
import java.lang.*;
public class CL_Farkle {
	
	static Scanner sc;

	//declare arrays
	static int[] farkles; //keeps track of number of farkles in a row a player has
	static int[] scores; //keeps track of thes scores of each roll
	static int[] rolls = new int[6]; //keeps track of the dice rolled
	static int[] val = new int[6]; //keeps track of the number of each value of dice rolled

	//declare variables
	static int numOfPlayers; //keeps track of number of players in the game
	static int currentPlayer; //keeps track of the current player
	static boolean isFinal = false; //keeps track of current round
	static int round; //keeps track of current round
	static boolean turnEnd = false; //keeps track if turn ends
	static int diceLeft; //keeps track of dice left to reroll
	static int pointsThisTurn = 0; //keeps track of the points accumulated in a turn
	static boolean done = false; //controls the final round

	//random number between 1 and 6
	public static int dice(){ return (int) (Math.random() * 6) + 1;}

	public static boolean hasOne(int[] arr, int limit){ //check if dice rolls contain a 1
		for(int i=0; i<limit; i++) if(arr[i] == 1) return true;
		return false;
	}

	public static boolean hasFive(int[] arr, int limit){ //check if dice rolls contain a 5
		for(int i=0; i<limit; i++) if(arr[i] == 5) return true;
		return false;
	}

	public static boolean hasTriplet(int[] arr, int limit){ //check if dice rolls contain a triplet
		for(int i=0; i<limit; i++) if(arr[i] >= 3) return true;
		return false;
	}

	public static boolean threePairs(int[] arr, int limit){ //check if dice rolls have three pairs
		int cnt = 0;
		for(int i=0; i<limit; i++) if(arr[i]==2) cnt++;
		if(cnt==3) return true;
		return false;
	}

	public static boolean hasStraight(int[] arr, int limit){ //check if dice rolls contain a straight
		int cnt = 0;
		for(int i=0; i<limit; i++) if(arr[i]==1) cnt++;
		if(cnt==6) return true;
		return false;
	}

	public static boolean sixOfaKind(int[] arr, int limit){ //check if dice rolls has 6 of the same number
		for(int i=0; i<limit; i++) if(arr[i]==6) return true;
		return false;
	}

	public static void rank(){ //displays the final ranks of each player
		int currank = 1;
		int samerank = 0;
		int max = 0;
		for(int i=0; i<numOfPlayers; i++)
			if(scores[i]>max) max = scores[i]; //find the maximum score
		System.out.println("\nRanking:");
		while(currank<=numOfPlayers){ 
			for(int i=0; i<numOfPlayers; i++) //print the players who have the highest scores
				if(scores[i]==max){ System.out.printf("%d. Player %d\n",currank,i+1); samerank++;} //players have the same rank if they have the same score
			int temp = 0;
			for(int i=0; i<numOfPlayers; i++)
				if(scores[i]<max&&scores[i]>temp) temp = scores[i]; //find next largest score
			max = temp;
			currank += samerank; 
			samerank = 0;
		}
	}

	public static void check(){ //check the dice rolls and points
		if(hasStraight(val, 6)){
			System.out.printf("Straight! +1000 pts\n");
			pointsThisTurn += 1000; //add to amount of points acculumated this turn
			farkles[currentPlayer-1] = 0; //reset farkle counter
			diceLeft = 0; //all dice used
		}
		else if(threePairs(val, 6)){
			System.out.printf("Three pairs! +500 pts\n");
			pointsThisTurn += 500;
			farkles[currentPlayer-1] = 0;
			diceLeft = 0;
		}
		else if(sixOfaKind(val, 6)){
			int idx = 0;
			for(int i=0; i<6; i++) if(val[i]==6) idx=i+1;
			pointsThisTurn += idx == 1 ? 2000 : 2 * idx * 100;
			System.out.printf("Six of a kind of %d!!! +%d pts\n", idx, pointsThisTurn);
			farkles[currentPlayer-1] = 0;
			diceLeft = 0;
		}
		else if(hasTriplet(val, 6)){
			int first=0, second=0, cnt = 0; //maximum two triplets can be possible
			for(int i=0; i<6; i++) if(val[i]>=3) {if(first==0)first = i+1;else second = i+1;}
			//remove the triplet
			for(int i=0; i<6; i++){
				if(rolls[i]==first){
					rolls[i] = 0;
					cnt++;
				}
				if(cnt==3) break;
			} //removal for two triplets is not needed since all dice will be used anyways
			
			if(first==1) first *= 10; //one has special case
			if(second==1) second *= 10;

			pointsThisTurn += first * 100;
			System.out.printf("Triplet of %d! +%d pts\n", first/10 + first%10, first * 100);

			if(second!=0){ //check if there is a second triplet
				pointsThisTurn += second * 100;
				diceLeft = 0; //no more dice
				System.out.printf("Triplet of %d! +%d pts\n", second/10 + second%10, second * 100);
			}
			else{ //check for any more point scoring die and subtract used die
				if(hasFive(rolls, 6)||hasOne(rolls, 6)){
					int ones = 0;
					int fives = 0;
					for(int i=0; i<6; i++) {
						if(rolls[i]==1){ones++; rolls[i] = 0;} //set dice used to 0
						if(rolls[i]==5){fives++; rolls[i] = 0;}
					}
					if(ones>0){
						System.out.printf("1 spots! +%d pts\n", 100*ones);
						pointsThisTurn += 100 * ones;
						diceLeft -= ones; //subtract dice used
					}
					if(fives>0){
						System.out.printf("5 spots! +%d pts\n", 50*fives);
						pointsThisTurn += 50 * fives;
						diceLeft -= fives; //subtract dice used
					}
				}
				diceLeft -= 3; //subtract the three dice from the triplets
			}
			farkles[currentPlayer-1] = 0; //set number of farkles in a row to 0
		}
		else if(hasFive(rolls, 6)||hasOne(rolls, 6)){
			int ones = 0;
			int fives = 0;
			for(int i=0; i<6; i++) {
				if(rolls[i]==1){ones++; rolls[i] = 0;}
				if(rolls[i]==5){fives++; rolls[i] = 0;}
			}
			if(ones>0){
				System.out.printf("1 spots! +%d pts\n", 100*ones);
				pointsThisTurn += 100 * ones;
				diceLeft -= ones;
			}
			if(fives>0){
				System.out.printf("5 spots! +%d pts\n", 50*fives);
				pointsThisTurn += 50 * fives;
				diceLeft -= fives;
			}
			farkles[currentPlayer-1] = 0;
		}
		else{ //no point scoring die
			System.out.printf("Farkle!!!\n");
			farkles[currentPlayer-1]++;
			pointsThisTurn = 0;
			if(farkles[currentPlayer-1]==3){ //farkled three times in a row
				System.out.printf("Farkled 3 times in a row!!! Lose 1000 points!!!\n");
				pointsThisTurn = -1000;
				farkles[currentPlayer-1] = 0;
			}
			diceLeft = 0; //no dice left
		}
	}

	public static void roll(){ //roll the dice
		for(int i=0; i<6; i++) rolls[i] = 0; //reset
		for(int i=0; i<6; i++) val[i] = 0; //reset
		System.out.printf("Rolls : ");
		for(int i=0; i<diceLeft; i++) {rolls[i] = dice(); val[rolls[i]-1]++;} //assign the roll values and count number of each value
		Arrays.sort(rolls);
		for(int i=0; i<6; i++) if(rolls[i]!=0) System.out.printf("%d ", rolls[i]); //print the roll values
		System.out.println("\n");
	}

	public static void start(){
		for(int i=0; i<numOfPlayers; i++) scores[i] = 0; //initialize each score to 0
		round = 1; //assign each variable with starting values
		currentPlayer = 1;
		diceLeft = 6;
	}

	public static void turn(){ //turn function
		System.out.printf("Player %d's turn:\n\n",currentPlayer);
		while(!turnEnd) {
			roll();
			check();
			
			System.out.printf("Current pts this turn: %d\n\n", pointsThisTurn);

			if(diceLeft==0){ //check if there are remaining dice left
				scores[currentPlayer-1] += pointsThisTurn;
				turnEnd = true; //turn ends
			}
			else{ //if there are remaining die ask if user wants to reroll
				System.out.printf("Do you want to roll the remaining %d die/dice?\n1. Yes\n2. No\n",diceLeft);
				int choice = sc.nextInt();
				while(choice!=1&&choice!=2){
					System.out.printf("Please select a valid option.\n");
					choice = sc.nextInt();
				}
				if(choice==2){
					scores[currentPlayer-1] += pointsThisTurn;
					turnEnd = true; //end turn
				}
				else System.out.println();
			}
		}
	}

	public static void round(){ //round function
		while(!isFinal||!done){ //keep looping while it is not the final round
			System.out.printf("\n\n\nRound %d:\n\n", round);

			for(int i=0; i<numOfPlayers; i++){ //Each player gets a turn
				turn();
				System.out.printf("\nPlayer %d earned %d pts this turn!\nEnd of turn.\n\n\n", currentPlayer, pointsThisTurn);
				currentPlayer = currentPlayer%numOfPlayers + 1; //next player
				pointsThisTurn = 0; //reset all variables
				diceLeft = 6;
				turnEnd = false;
			}

			System.out.printf("Current scores: \n");
			for(int i=0; i<numOfPlayers; i++) System.out.printf("Player %d: %d\n", i+1, scores[i]);
			round++;

			if(isFinal) done = true; //check if it is the final round

			for(int i=0; i<numOfPlayers; i++) if(scores[i]>=10000) {isFinal = true; done = true;}
		}
	}

	public static void run(){ //run the game
		round();
		System.out.printf("\n\n\nFinal round!!!"); //if round stops it means final turn was reached
		done = false; //used to run the round function once more
		round(); //run once more
		System.out.printf("\n\n\nFinal scores: \n"); //print final scores
		for(int i=0; i<numOfPlayers; i++) System.out.printf("Player %d: %d\n", i+1, scores[i]);
		rank();
		System.out.printf("\n\nGame Over\n");
	}

	public static void main(String[] args){
		sc = new Scanner(System.in); //scanner
		System.out.println("Farkle Dice Game\n");
		System.out.printf("Enter number of players (at least 2): ");
		numOfPlayers = sc.nextInt(); //get number of players
		while(numOfPlayers<2){
			System.out.printf("Please don't do this! Pls re-enter\n");
			numOfPlayers = sc.nextInt();
		}
		farkles = new int[numOfPlayers]; //initialize the arrays
		scores = new int[numOfPlayers];
		start(); //start function to initialize everything required
		run(); //run the game
	}	
}