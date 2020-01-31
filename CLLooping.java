/*
*Linus Chen
*ICS4U1
*Looping Problem Set
*/

import java.util.*;
public class CLLooping{
	static Scanner sc;
	static int max = -1;
	static int seqLength = 0;

	public static void Chapter16_3(){ //Calculates standard deviation of a given list of numbers
		System.out.println("How many numbers to follow: "); //input
		int n = sc.nextInt();
		System.out.println("Gimme: ");
		double avg = 0; //declare variables
		double avgSquare = 0;
		double cur, stdDev;
		for(int i=0; i<n; i++){ //calculate average and average of squares
			cur = sc.nextDouble();
			avg += cur;
			avgSquare += Math.pow(cur, 2);
		}
		avg /= n;
		avgSquare /= n;
		stdDev = Math.sqrt(avgSquare - Math.pow(avg, 2)); //standard deviation formula
		System.out.printf("The standard deviation of the list is: %f", stdDev);
	}

	public static void AddingUpSquaresAndCubes(){ //Finds the sum of squares and cubes up to a given limit
		System.out.println("Upper Limit:"); //Get input
		int n = sc.nextInt();
		while(n<=0){ //forces user to set a positive upper limit
			System.out.println("Something positive please");
			n = sc.nextInt();
		}
		int squares = 0, cubes = 0;
		for(int i=1; i<=n; i++){
			squares += i*i; //sum up the stuff
			cubes += i*i*i;
		}
		System.out.printf("The sum of Squares is %d\n", squares); //output sums
		System.out.printf("The sum of Cubes is %d\n", cubes);
	}

	public static void MilesPerGallon(){
		System.out.println("Miles Per Gallon Program");
		
		do{
			System.out.println("Initial miles:");
			int initMiles = sc.nextInt();
			if(initMiles==-1)break; //exit loop if the user enters -1
			System.out.println("Final miles:"); //get input
			int finalMiles = sc.nextInt();
			System.out.println("Gallons");
			int gallons = sc.nextInt();
			double mpg = (double) (finalMiles - initMiles) / gallons; //calculate miles per gallon
			System.out.printf("Miles per Gallon: %.1f\n\n", mpg);
		}while(true); //infinite loop
		System.out.println("bye");
	}

	public static void AreaOfRectangles(){
		boolean valid = true;
		System.out.println("Computer Aided Design Program");
		while(valid){
			System.out.println("First corner X coordinate:"); //get input
			int firstX = sc.nextInt();
			System.out.println("First corner Y coordinate:");
			int firstY = sc.nextInt();
			System.out.println("Second corner X coordinate");
			int secondX = sc.nextInt();
			System.out.println("Second corner Y coordinate");
			int secondY = sc.nextInt();
			int width = Math.abs(secondX - firstX); //make sure width and height are positive
			int height = Math.abs(secondY - firstY);
			if(width==0||height==0)valid = !valid; //exit when either width or height are 0
			System.out.printf("Width: %d Height: %d Area: %d\n\n", width, height, width * height);
		}
		System.out.println("finished");
	}

	public static void DrugPotency(){
		int month = 0;
		double effectiveness = 100.0;
		while(!(effectiveness < 50.0)){ //loop until effectiveness is below 50%
			System.out.printf("month: %d \teffectiveness: %f\n", month, effectiveness);
			effectiveness *= 0.96; //only 96% of previous cycle effectiveness
			month++; //increment month by 1
		}
		System.out.printf("month: %d \teffectiveness: %f DISCARDED\n", month, effectiveness);
	}

	public static int HailstoneNumbers(int n){ //recursive function to calculate hailstone numbers
		seqLength++; //record sequence length
		max = Math.max(n, max); //keep track of current maximum
		if(n==1) { return 1;}
		else if(n%2==0) { System.out.printf("%d (even, next value is %d/2)\n", n, n); return HailstoneNumbers(n/2);}
		else { System.out.printf("%d (odd, next value is 3*%d+1)\n", n, n); return HailstoneNumbers(3*n+1);}
	}

	public static void HailstoneNumberRun(){ //runner program to execute hailstone numbers recursive function
		System.out.println("Input a positive integer:");
		int n = sc.nextInt();
		while(n<=0){
			System.out.println("Are you serious?");
			n = sc.nextInt();
		}
		max = -1;
		seqLength = 0;
		System.out.printf("%d (stop calculation)\n", HailstoneNumbers(n)); 
		System.out.printf("Sequence length: %d\nMaximum integer: %d\n", seqLength,max); //output sequence length and maximum integer of sequence
	}

	public static void main(String[] args){
		sc = new Scanner(System.in);
		char choice;
        do //menu
        {
            System.out.println ("\n\n\nLooping");
            System.out.println ("--------------");
            System.out.println ("1. Chapter 16 Exercise 3");
            System.out.println ("2. Adding up Squares and Cubes");
            System.out.println ("3. Miles per Gallon");
            System.out.println ("4. Area of Rectangles");
            System.out.println ("5. Drug Potency");
            System.out.println ("6. Hailstone Numbers");
            System.out.println ("0. Quit");
            choice = sc.next().charAt(0);

            if (choice == '1')
                Chapter16_3(); // call Chapter 16 Exercise 3
            else if (choice == '2')
            	AddingUpSquaresAndCubes();
            else if (choice == '3')
            	MilesPerGallon();
            else if (choice == '4')
            	AreaOfRectangles();
            else if (choice == '5')
            	DrugPotency();
            else if (choice == '6')
            	HailstoneNumberRun();
        }
        while (choice != '0'); // exit when 0   
	}
}