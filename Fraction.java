/*
Linus Chen
ICS4U1
Fraction Class
*/

import java.util.*;
public class Fraction{
	
	//Declare variables
	private int numerator, denominator;

	//Constructors
	public Fraction(){
		numerator = 0;
		denominator = 1;
	}

	public Fraction(int num, int den){
		numerator = num;
		denominator = den;
	}

	public Fraction(Fraction frac){
		numerator = frac.getNumerator();
		denominator = frac.getDenominator();
	}

	//get methods
	public int getNumerator(){
		return numerator;
	}

	public int getDenominator(){
		return denominator;
	}

	//toString method
	public String toString(){
		if(denominator==0) //check if denominator is 0
			return "undefined";
		if(numerator==0) //check if numerator is 0
			return "0";
		if(denominator==1) //check if denominator is 1
			return Integer.toString(numerator/denominator);
		if(denominator==numerator) //check if denominator is 1
			return Integer.toString(1);
		return Integer.toString(numerator) + "/" + Integer.toString(denominator); 
	}

	//reduces fraction to lowest terms
	public void reduce(){
		int div = gcd(numerator,denominator);
		numerator /= div;
		denominator /= div;
	}

	//find the greatest common divisor of two integers
	public int gcd(int a, int b){
		return b == 0 ? a : gcd (b, a%b);
	}

	//find the lowest common multiply of two integers
	public int lcm(int a, int b){
		return (a*b) / gcd(a,b);
	}

	//Arithmetic Operations
	public Fraction add(Fraction frac){
		int denom = lcm(frac.getDenominator(), denominator);
		return new Fraction(denom/denominator * numerator + denom/frac.getDenominator() * frac.getNumerator(), denom);
	}

	public Fraction subtract(Fraction frac){
		int denom = lcm(frac.getDenominator(), denominator);
		return new Fraction(denom/denominator * numerator - denom/frac.getDenominator() * frac.getNumerator(), denom);	
	}

	public Fraction multiply(Fraction frac){
		return new Fraction(frac.getNumerator() * numerator, frac.getDenominator() * denominator);
	}

	public Fraction divide(Fraction frac){
		return new Fraction(numerator * frac.getDenominator(), denominator * frac.getNumerator());
	}

	//Convert Fraction to decimal
	public double toDecimal(){
		return (double) numerator / (double) denominator;
	}

	//Check if two fractions are equal
	public boolean equals(Fraction frac){
		Fraction a = new Fraction(frac.getNumerator(), frac.getDenominator());
		Fraction b = new Fraction(numerator, denominator);
		a.reduce(); //check if reduced forms of both fractions are equal
		b.reduce();
		return (a.getNumerator()==b.getNumerator())&&(a.getDenominator()==b.getDenominator());
	}

	//Compares two Fractions
	public int compareTo(Fraction frac){
		if(toDecimal() < frac.toDecimal())
			return -1;
		else if(toDecimal() == frac.toDecimal())
			return 0;
		else
			return 1;
	}

	//Reads new Fraction
	public void read(Scanner sc){
		boolean valid = false; //initial state
		String frac = "";
		int slashcnt = 0; //counts number of slashes in a string
		int spacecnt = 0; //counts number of spaces in a string
		while(!valid){
			System.out.printf("Enter fraction: ");
			frac = sc.nextLine(); //input
			slashcnt = 0;
			spacecnt = 0;
			//count spaces and slashes
			for(int i=0; i<frac.length(); i++){
				if(frac.charAt(i)=='/')
					slashcnt++;
				if(frac.charAt(i)==' ')
					spacecnt++;
			}
			//check validity of string                      
			if(frac.matches("[\\s]*-{0,1}[0-9]+[\\s]*\\/[\\s]*-{0,1}[0-9]+")||frac.matches("[\\s]*-{0,1}[0-9]+[\\s]*"))//string regex to check if given fraction matches expression
							//[\\s]* allows 0 or any number of spaces, -{0,1} allows zero or one negative sign, [0,9]+ allows 1 or more digit to be entered, \\/ checks for slashes
				valid = true;
			else {
				System.out.printf("Invalid fraction\n");
				continue;
			}
		}
		System.out.printf("Fraction accepted.\n\n"); //valid fraction was entered
		//check if there is a slash or not
		if(slashcnt==1){
			int slash = frac.indexOf("/");

			String num = frac.substring(0,slash);
			String den = frac.substring(slash+1,frac.length());
			num = num.replaceAll(" ",""); 
			den = den.replaceAll(" ","");

			numerator = Integer.parseInt(num);
			denominator = Integer.parseInt(den);
		}
		if(slashcnt==0){
			String num = frac.replaceAll(" ","");
			numerator = Integer.parseInt(num);
			denominator = 1;
		}
	}
}