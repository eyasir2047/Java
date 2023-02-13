public class Line extends Shape{
	private double length;
	public Line(String col,double l){
		setName("Line");
		setColor(col);
		length=l;
	}
	public void area(){
		System.out.println("A line has no area");
	} 
	
	public void perimeter(){
		System.out.println("Perimter: "+2*length);
	}
	protected double getLength(){
		return length;
	}
	public void show(){
		super.show();
		area();
		perimeter();
	}
}
