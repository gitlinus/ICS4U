/*
Linus Chen
ICS4U1
Box Class Assignment
*/

public class Box{ //box class

	private double w, h, l; //variables

	public Box(double width, double height, double length){ //constructor
		w = width;
		h = height;
		l = length;
	}

	public Box(double side){ //constructor
		w = h = l = side;
	}

	public Box(Box oldBox){ //constructor
		w = oldBox.width();
		h = oldBox.height();
		l = oldBox.length();
	}

	private double faceArea(){ //face area
		return h * l;
	}

	private double topArea(){ //top area
		return w * l;
	}

	private double sideArea(){ //side area
		return h * w;
	}

	public double area(){ //surface area
		return 2 * faceArea() + 2 * topArea() + 2 * sideArea();
	}

	public double volume(){ //volume
		return w * h * l;
	}	

	public double width(){ //width
		return w;
	}

	public double height(){ //height
		return h;
	}

	public double length(){ //length
		return l;
	}

	public Box biggerBox(Box oldBox){ //make box bigger
		return new Box(1.25*oldBox.width(), 1.25*oldBox.height(), 1.25*oldBox.length());
	}

	public Box smallerBox(Box oldBox){ //make box smaller
		return new Box(0.75*oldBox.width(), 0.75*oldBox.height(), 0.75*oldBox.length());
	}

	public boolean nests(Box outsideBox){ //check if box fits in other box
		return outsideBox.width()>w&&outsideBox.height()>h&&outsideBox.length()>l;
	}
}