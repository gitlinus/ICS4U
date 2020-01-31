/*
Linus Chen
ICS4U1
February 14, 2019
*/

import java.util.*;
public class CLMethodSet{ 

	static Scanner sc;
	
	public static double areaCircle(double radius){ //calculate area of circle given radius
		double area = Math.PI * radius * radius;
		return area;
	}
	
	public static boolean odd(int num){ //checks odd
		if(num%2==0) return false;
		return true;
	}

	public static String monthName(int month){ //checks month
		if(month==1) return "January";
		if(month==2) return "February";
		if(month==3) return "March";
		if(month==4) return "April";
		if(month==5) return "May";
		if(month==6) return "June";
		if(month==7) return "July";
		if(month==8) return "August";
		if(month==9) return "September";
		if(month==10) return "October";
		if(month==11) return "November";
		if(month==12) return "December";
		return "Invalid";
	}

	public static int daysInMonth(int month){ //checks number of days
		if(month==2) return 28;
		if(month<8&&month%2==1) return 31;
		if(month<8&&month%2==0) return 30;
		if(month>=8&&month%2==0) return 31;
		if(month>8&&month%2==1) return 30;
		return 0;
	}

	public static int random(int low, int high){ //creates random
		return (int) (Math.random() * (high-low+1)) + low; 
	}

	public static char flip(){ //creates random heads or tails
		int flip = random(1,2);
		if(flip==1) return 'H';
		return 'T';
	}

	public static char randomLetter(){ //creates random letter
		int letter = random(65,90);
		return (char) letter;
	}	

	public static void CircleArea(){ //driver method
		System.out.printf("\n\nEnter radius of circle: ");
		int radius = sc.nextInt();
		System.out.println(odd(radius) ? "The radius is odd" : "The radius is even");
		double area = areaCircle(radius);
		System.out.printf("Area = %.1f square units",area);
	}

	public static void MonthDays(){ //driver method
		System.out.printf("\n\nMonth \t\tDays\n");
		for(int i=1; i<=12; i++){
			System.out.printf("%s",monthName(i)); //formatting output
			for(int j=monthName(i).length(); j<16; j++) System.out.print(" ");
			System.out.printf("%d\n",daysInMonth(i));
		}
	}

	public static void RandomFlipsAndLetters(){ //driver method
		char cur;
		int tails = 0;
		System.out.printf("\n\n10 coin flips: ");
		for(int i=0; i<10; i++){
			cur = flip();
			System.out.print(cur);
			if(cur=='T') tails++;
		}
		double perc = (double) tails / 10 * 100;
		System.out.printf(" %.0f%s tails\n\n",perc, "%");
		System.out.printf("Five random 4-letter combos: ");
		for(int i=0; i<5; i++){
			for(int j=0; j<4; j++)
				System.out.print(randomLetter());
			System.out.print(" ");
		}
		System.out.println();
	}

	public static void main(String[] args){ 
		sc = new Scanner(System.in);
		
		char choice;
		do //menu
        {
            System.out.println ("\n\n\nMethods Menu");
            System.out.println ("--------------");
            System.out.println ("1. Circle Area");
            System.out.println ("2. Month Days");
            System.out.println ("3. Random Flips and Letters");
            System.out.println ("0. Quit");
            choice = sc.next().charAt(0);

            if (choice == '1')
                CircleArea(); // call Chapter 16 Exercise 3
            else if (choice == '2')
            	MonthDays();
            else if (choice == '3')
            	RandomFlipsAndLetters();
        }
        while (choice != '0'); // exit when 0   
        
	}
}