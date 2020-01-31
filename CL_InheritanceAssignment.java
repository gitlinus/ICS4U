/*
Linus Chen
ICS4U1
Inheritance Assignment

Driver class for Inheritance Assignment classes
*/

import java.util.*;
public class CL_InheritanceAssignment {

	static Scanner sc;
	static Shape shape; //will be instantiated later as either a Square or a Circle

	public static void makeShape(){
		System.out.printf("Which shape do you want to make: \n");
		System.out.printf("1. Circle\n2. Square\n");
		int c = sc.nextInt();
		while(c!=1&&c!=2){
			System.out.printf("Enter valid choice\n");
			c = sc.nextInt();
		}
		if(c==1){
			System.out.printf("Enter radius of circle: ");
			double rad = sc.nextDouble();
			shape = new Circle(rad); //make Circle object
		}
		else{
			System.out.printf("Enter side length of square: ");
			double side = sc.nextDouble();
			shape = new Square(side); //make Square object
		}
	}

	public static void displayInfo(){ //display the shape info
		System.out.printf("This shape is a %s\n", shape.getName());
		System.out.printf(shape.numSides()==0 ? "This shape has no sides or internal angles\n" : "This shape has %d sides and its internal angles add to %d degrees\n", shape.numSides(), shape.sumOfAngles());
	}

	public static void computeArea(){ //display the area of the shape
		System.out.printf("The area of this shape is %f\n", shape.area()); //the corresponding method will be executed based on which object was instantiated
	}

	public static void computePerimeter(){ //display the perimeter of the shape
		System.out.printf("The perimeter of this shape is %f\n", shape.perimeter()); //the corresponding method will be executed based on which object was instantiated
	}

	public static void main(String[] args){
		sc = new Scanner(System.in);

		System.out.printf("\n\nLet's make a shape.\n\n");
		makeShape();

		//menu
		char choice;
		do{
			System.out.println ("\n\n\nShape Operations");
            System.out.println ("----------------");
            System.out.println ("1. Display Information of Shape");
            System.out.println ("2. Compute Area of Shape");
            System.out.println ("3. Compute Perimeter of Shape");
			System.out.println ("4. Make new Shape");
			System.out.println ("0. Quit");

			choice = sc.next().charAt(0);

			System.out.println();

			if(choice=='1')
				displayInfo(); //call method
			else if(choice=='2')
				computeArea();
			else if(choice=='3')
				computePerimeter();
			else if(choice=='4')
				makeShape();
		}while(choice!='0'); //exit
	}
}