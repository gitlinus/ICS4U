/*
Linus Chen
ICS4U1
Box driver class
*/

import java.util.*;

public class CLBoxDriver{ //Box class driver

	static Scanner sc;
	static Box box, box2;

	public static void makeBox(boolean flag){ //make box flag checks if box is already make
		System.out.printf("Choose box to make:\n");
		System.out.printf("1. Box with different dimensions\n");
		System.out.printf("2. Box with same dimensions\n");

		int choice = sc.nextInt();
		while(choice!=1&&choice!=2){
			System.out.printf("Choose valid options\n");
			choice = sc.nextInt();
		}

		if(choice==1){
			System.out.printf("Enter width: ");
			double width = sc.nextDouble();
			System.out.printf("Enter height: ");
			double height = sc.nextDouble();
			System.out.printf("Enter length: ");
			double length = sc.nextDouble();
			if(flag) box = new Box(width, height, length); 	//no box made yet
			else box2 = new Box(width, height, length);
		}
		else{
			System.out.printf("Enter side length: ");
			double side = sc.nextDouble();
			if(flag) box = new Box(side);
			else box2 = new Box(side);
		}
	}

	public static void compare(){ //compare boxes
		System.out.printf("Make outer box:\n\n");
        makeBox(false); 
        if(box.nests(box2)) System.out.printf("The current box fits into the outer box.\n");
        else System.out.printf("The current box does not fit into the outer box.\n");
	}

	public static void displayInfo(){ //display box information
		System.out.printf("The volume of the box is %f\n",box.volume());
      	System.out.printf("The surface area of the box is %f\n",box.area());
      	System.out.printf("The width of the box is %f\n",box.width());
      	System.out.printf("The height of the box is %f\n",box.height());
      	System.out.printf("The length of the box is %f\n",box.length());
	}

	public static void makeBigger(){ //make box bigger
		box = box.biggerBox(box); 
		System.out.printf("The width, height, and length of the new box are %f, %f, and %f\n",box.width(),box.height(),box.length());
	}

	public static void makeSmaller(){ //make box smaller
		box = box.smallerBox(box);
		System.out.printf("The width, height, and length of the new box are %f, %f, and %f\n",box.width(),box.height(),box.length());
	}

	public static void main(String[] args){
		sc = new Scanner(System.in);
		makeBox(true);

		char choice;
		do //menu
        {
            System.out.println ("\n\n\nBox Operations Menu");
            System.out.println ("--------------");
            System.out.println ("1. Display box information");
            System.out.println ("2. Increase box's size by 25%");
            System.out.println ("3. Decrease box's size by 25%");
            System.out.println ("4. Check if box fits inside another box");
            System.out.println ("5. Create new box");
            System.out.println ("6. Clone current box");
            System.out.println ("0. Quit");
            choice = sc.next().charAt(0);

            if (choice == '1')
                displayInfo(); // call function
            else if (choice == '2')
            	makeBigger();
            else if (choice == '3')
            	makeSmaller();
            else if (choice == '4')
            	compare();
            else if (choice == '5')
            	makeBox(true);
            else if (choice == '6'){
            	box2 = new Box(box);
            	System.out.printf("The width, height, and length of the cloned box are %f, %f, and %f\n",box2.width(),box2.height(),box2.length());
            }
        }
        while (choice != '0'); // exit when 0   

	}
}