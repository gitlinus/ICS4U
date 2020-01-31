/*
Linus Chen
ICS4U1
Inheritance Assignment

abstract Shape class
*/

abstract class Shape implements Geometry{
	
	private String name;
	protected int numSides; 

	//Shape constructor
	public Shape(String shapeName){ name = shapeName; }

	public String getName(){ return name; }
	
	public abstract double area(); //different shapes have their own formula
	
	public abstract double perimeter(); //different shapes have their own formula

	public int numSides(){ return numSides; } 

	public int sumOfAngles(){ return (numSides - 2) * 180; }
}