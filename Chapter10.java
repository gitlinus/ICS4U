/*
 *Linus Chen
 *ICS4U1
 *Java Basics
*/

import java.util.*;

public class Chapter10{
	static Scanner in;
	public static void AreaOfCircle(){
		System.out.print("Input the radius: ");
		int radius = in.nextInt();
		System.out.printf("The radius is: %d The area is: %.1f\n", radius, Math.PI*Math.pow(radius,2));
	}
	public static void CentsToDollars(){
		System.out.printf("Input the cents: \n");
		int cents = in.nextInt();
		System.out.printf("That is %d dollars and %d cents.\n", cents/100, cents%100);
	}
	public static void CorrectChange(){
		System.out.print("Input change: ");
		int change = in.nextInt();
		System.out.printf("Your change is %d dollar(s), %d quarter(s), %d dime(s), %d nickel(s), and %d cent(s).\n", change/100, change%100/25, change%100%25/10, change%100%25%10/5, change%100%25%10%5);
	}
	public static void OhmsLaw(){
		System.out.print("Input voltage: ");
		int voltage = in.nextInt();
		System.out.print("Input resistance: ");
		int resistance = in.nextInt();
		double current = (voltage + 0.0)/resistance;
		System.out.printf("Total current is %.1f", current);
	}
	public static void main(String[] args){
		in = new Scanner(System.in);
		char choice;
        do
        {
            System.out.println ("\n\n\nProblem Set #1");
            System.out.println ("--------------");
            System.out.println ("1. Area of Circle");
            System.out.println ("2. Cents to Dollars");
            System.out.println ("3. Correct Change");
            System.out.println ("4. Ohm's Law");
            System.out.println ("0. Quit");
            choice = in.next().charAt(0);

            if (choice == '1')
                AreaOfCircle(); // call AreaOfCircle
            else if (choice == '2')
                CentsToDollars();
            else if (choice == '3')
				CorrectChange();
            else if (choice == '4')
				OhmsLaw();
        }
        while (choice != '0'); // exit when 0      
	}
}