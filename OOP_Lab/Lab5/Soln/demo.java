public class demo{
	public static void main(String[] args){
		Line l= new Line("Red",10);
		Circle c=new Circle("Blue",2.5);
		Elipse e=new Elipse("Green",3,4);
		Rectangle r= new Rectangle("Yellow", 5,2);
		Square s= new Square("Orange", 7);
		Triangle t= new Triangle("Pink",3,4,5,2.2);
		
		c.show();
		System.out.println();
		
		e.show();
		System.out.println();
		
		r.show();
		System.out.println();
		
		s.show();
		System.out.println();
		
		t.show();
		System.out.println();
		
		l.show();
	}
}
