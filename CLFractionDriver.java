/*
Linus Chen
ICS4U1
Fraction Driver class
*/

import java.util.*;
public class CLFractionDriver {
	
	static Scanner sc;
	static Fraction a, b;

	//Display fractions
	public static void display(){
		System.out.printf("Fraction 1: %s\n", a.toString());
		System.out.printf("Fraction 2: %s\n", b.toString());
	}

	//Reduce fractions
	public static void reduce(){
		System.out.printf("Reduced Fractions:\n");
		if(a.getDenominator()==0)
			System.out.printf("Unable to be reduced, Fraction 1 is undefined\n");
		else{
			a.reduce();
			System.out.printf("Fraction 1: %s\n", a.toString());
		}
		if(b.getDenominator()==0)
			System.out.printf("Unable to be reduced, Fraction 2 is undefined\n");
		else{
			b.reduce();
			System.out.printf("Fraction 2: %s\n", b.toString());
		}
	}

	//Add fractions
	public static void add(){
		Fraction sum;
		if(a.getDenominator()==0)
			System.out.printf("Cannot add, Fraction 1 is undefined\n");
		else if(b.getDenominator()==0)
			System.out.printf("Cannot add, Fraction 2 is undefined\n");
		else{
			sum = a.add(b);
			System.out.printf("%s + %s = %s\n",a.toString(), b.toString(), sum.toString());
		}
	}


	//Subtract fractions
	public static void subtract(){
		Fraction difference;
		if(a.getDenominator()==0)
			System.out.printf("Cannot subtract, Fraction 1 is undefined\n");
		else if(b.getDenominator()==0)
			System.out.printf("Cannot subtract, Fraction 2 is undefined\n");
		else{
			difference = a.subtract(b);
			System.out.printf("%s - %s = %s\n",a.toString(), b.toString(), difference.toString());
		}
	}

	//Multiply fractions
	public static void multiply(){
		Fraction product;
		if(a.getDenominator()==0)
			System.out.printf("Cannot multiply, Fraction 1 is undefined\n");
		else if(b.getDenominator()==0)
			System.out.printf("Cannot multiply, Fraction 2 is undefined\n");
		else{
			product = a.multiply(b);
			System.out.printf("%s * %s = %s\n",a.toString(), b.toString(), product.toString());
		}
	}

	//Divide fractions
	public static void divide(){
		Fraction quotient;
		if(a.getDenominator()==0)
			System.out.printf("Cannot divide, Fraction 1 is undefined\n");
		else if(b.getDenominator()==0)
			System.out.printf("Cannot divide, Fraction 2 is undefined\n");
		else{
			quotient = a.divide(b);
			System.out.printf("%s / %s = %s\n",a.toString(), b.toString(), quotient.toString());
		}
	}

	//Check equality of fractions
	public static void equals(){
		if(a.getDenominator()==0&&b.getDenominator()==0)
			System.out.printf("Both Fractions are undefined");
		else if(a.getDenominator()!=0&&b.getDenominator()!=0&&a.equals(b))
			System.out.printf("Fractions 1 and 2 are equal\n");
		else
			System.out.printf("Fractions 1 and 2 are not equal\n");
	}

	//compare fractions
	public static void compare(){
		if(a.getDenominator()==0||b.getDenominator()==0)
			System.out.printf("Cannot compare, at least one Fraction is undefined");
		else if(a.compareTo(b)==-1)
			System.out.printf("Fraction 1 (%s) < Fraction 2 (%s)\n",a.toString(), b.toString());
		else if(a.compareTo(b)==0)
			System.out.printf("Fraction 1 (%s) = Fraction 2 (%s)\n",a.toString(), b.toString());
		else
			System.out.printf("Fraction 1 (%s) > Fraction 2 (%s)\n",a.toString(), b.toString());
	}

	//display decimal representation
	public static void decimal(){
		if(a.getDenominator()==0)
			System.out.printf("Fraction 1: %s\n", a.toString());
		else
			System.out.printf("Fraction 1: %s = %f\n", a.toString(),a.toDecimal());
		if(b.getDenominator()==0)
			System.out.printf("Fraction 2: %s\n", b.toString());
		else
			System.out.printf("Fraction 2: %s = %f\n", b.toString(),b.toDecimal());
	}

	//new fraction
	public static void newFrac(){
		a = new Fraction();
		b = new Fraction();
		System.out.printf("Fraction 1\n");
		a.read(sc);
		System.out.printf("Fraction 2\n");
		b.read(sc);
	}

	public static void main(String[] args){
		sc = new Scanner(System.in);
		newFrac();

		int choice;
		do //menu
        {
            System.out.println ("\n\n\nFraction Operations");
            System.out.println ("-------------------");
            System.out.println ("1. Display Fractions");
            System.out.println ("2. Reduce Fractions");
            System.out.println ("3. Add Fractions");
            System.out.println ("4. Subtract Fractions");
            System.out.println ("5. Multiply Fractions");
            System.out.println ("6. Divide Fractions");
            System.out.println ("7. Check Equality of Fractions");
            System.out.println ("8. Compare Fractions");
            System.out.println ("9. Check Decimal Respresentation of Fractions");
            System.out.println ("10. Enter new Fractions");
            System.out.println ("0. Quit");
            choice = sc.nextInt();

            System.out.println();

            if (choice == 1)
                display(); // call function
            else if (choice == 2)
            	reduce();
            else if (choice == 3)
            	add();
            else if (choice == 4)
            	subtract();
            else if (choice == 5)
            	multiply();
            else if (choice == 6)
            	divide();
         	else if (choice == 7)
            	equals();
            else if (choice == 8)
            	compare();
            else if (choice == 9)
            	decimal();
            else if (choice == 10)
  				newFrac();
        } while (choice != 0); // exit when 0 
	}
}