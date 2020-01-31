/*
Linus Chen
ICS4U1
Decisions Problem Set
*/


import java.util.*;

public class CLDecisions{
	static Scanner sc;

	public static void OrderChecker(){ //lets user input number of things they want to buy and checks if order is OK and calculates total cost
		final int boltPrice = 5; //set prices for each thing
		final int nutPrice = 3;
		final int washerPrice = 1;

		System.out.print("Number of bolts: "); //input
		int bolts = sc.nextInt();
		System.out.print("Number of nuts: ");
		int nuts = sc.nextInt();
		System.out.print("Number of washers: ");
		int washers = sc.nextInt();
		System.out.println();
		int cost = bolts * boltPrice + nuts*nutPrice + washers * washerPrice;
		if(nuts >= bolts && washers >= 2 * bolts) //checks if there are at least as many nuts as bolts and if there are at least twice as many washers as bolts
			System.out.println("Order is OK.");
		else{
			if(nuts < bolts) System.out.println("Check the Order: too few nuts"); //check if too few nuts
			if(washers < 2 * bolts) System.out.println("Check the Order: too few washers"); //check if too few bolts
		}
		System.out.printf("\nTotal cost: %d\n", cost); //print total cost
	}

	public static int InputChecker(int lower, int upper){ //forces user to input a value in given range
		int num = sc.nextInt();
		while(num<lower||num>upper){ //continually check if number is within limits
			System.out.println("Invalid input");
			num = sc.nextInt();
		}
		return num;
	}

	public static void FantasyGame(){ //allows user to create new character and sets values for character stats
		System.out.println("Welcome to Yertle's Quest");
		System.out.println("Enter the name of your character:");
		sc.nextLine(); //clear the input
		String name = sc.nextLine();
		System.out.println("Enter strength (1-10):"); //input
		int strength = InputChecker(1,10);
		System.out.println("Enter health (1-10):");
		int health = InputChecker(1,10);
		System.out.println("Enter luck (1-10):");
		int luck = InputChecker(1,10);
		int total = strength + health + luck; //calculate total points given
		//check if to many points assigned
		if(total > 15) { strength = health = luck = 5; System.out.printf("You have given your character too many points! Default values have been assigned: \n%s, strength: %d, health: %d, luck: %d\n", name, strength, health, luck);}
		else System.out.printf("Successfully created character: \n%s, strength: %d, health: %d, luck: %d\n", name, strength, health, luck);
	}

	public static void MoreTirePressure(){ //checks tire pressure if it's ok
		boolean goodPressure = true; //initialize to true
		System.out.println("Input right front pressure"); //input
		int rfp = sc.nextInt();
		goodPressure = goodPressure ? rfp >= 35 && rfp <= 45 : goodPressure; //ternary operators to save space
		//if goodPressure is false already, it stays false, else it checks if the pressure of tire is within range and assigns it false if not in range, and true if in range
		System.out.printf(rfp >= 35 && rfp <= 45 ? "" : "Warning: pressure is out of range\n\n"); //print warning if pressure not in range
		System.out.println("Input left front pressure");
		int lfp = sc.nextInt();
		goodPressure = goodPressure ? lfp >= 35 && lfp <= 45 : goodPressure;
		System.out.printf(lfp >= 35 && lfp <= 45 ? "" : "Warning: pressure is out of range\n\n");
		System.out.println("Input right rear pressure");
		int rrp = sc.nextInt();
		goodPressure = goodPressure ? rrp >= 35 && rrp <= 45 : goodPressure;		
		System.out.printf(rrp >= 35 && rrp <= 45 ? "" : "Warning: pressure is out of range\n\n");
		System.out.println("Input left rear pressure");
		int lrp = sc.nextInt();
		goodPressure = goodPressure ? lrp >= 35 && lrp <= 45 : goodPressure;
		System.out.printf(lrp >= 35 && lrp <= 45 ? "" : "Warning: pressure is out of range\n\n");
		if(rfp!=lfp||rrp!=lrp) goodPressure = false; //check if pressure is the same in both front tires and both back tires
		System.out.printf(goodPressure ? "\nInflation is OK" : "\nInflation is BAD"); //if goodPressure is true, prints OK. if goodPressure is false, prints BAD 	
	}

	public static void main(String[] args){
		sc = new Scanner(System.in);
		char choice;
        do //menu
        {
            System.out.println ("\n\n\nDecisions");
            System.out.println ("--------------");
            System.out.println ("1. Order Checker");
            System.out.println ("2. Fantasy Game");
            System.out.println ("3. More Tire Pressure");
            System.out.println ("0. Quit");
            choice = sc.next().charAt(0);

            if (choice == '1')
                OrderChecker(); // call OrderChecker
            else if (choice == '2')
            	FantasyGame();
            else if (choice == '3')
            	MoreTirePressure();
        }
        while (choice != '0'); // exit when 0    

	}
}