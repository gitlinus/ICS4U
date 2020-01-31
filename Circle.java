/*
Linus Chen
ICS4U1
Inheritance Assignment

Circle class, subclass (child) of Shape (parent) class
*/

public class Circle extends Shape{
	
	private double radius;
	
	//Circle class constructor
	public Circle(double circleRadius){
	    super("Circle");
	    numSides = 0;
		radius = circleRadius; 
	}
	
	public double perimeter(){ return 2 * Math.PI * radius; } //calculate perimeter
	
	public double area(){ return Math.PI * radius * radius; } //calculate area

	public int sumOfAngles(){ return 0; } //overides Parent's sumOfAngles method
}
