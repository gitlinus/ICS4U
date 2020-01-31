/*
Linus Chen
ICS4U1
Inheritance Assignment

Square class, subclass of Shape class
*/

public class Square extends Shape {
	
	private double side;
	
	//Square constructor
	public Square(double squareSide) {
	    super("Square");
	    numSides = 4;
		side = squareSide; 
	}
	
	public double perimeter() { return 4 * side; } // calculate perimeter
	
	public double area() { return side * side; } // calculate area
}