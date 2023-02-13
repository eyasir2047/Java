public class Rectangle extends Line{
	private double width;
	public Rectangle(String c, double h, double w){
		super(c,h);
		width=w;
		setName("Rectangle");
	}
	public void area(){
		System.out.println("Area: "+getLength()*width);
	}
	public void perimeter(){
		System.out.println("Perimeter: "+(2*(getLength()+width)));	
	}


}
