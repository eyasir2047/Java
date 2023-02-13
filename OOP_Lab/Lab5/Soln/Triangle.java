public class Triangle extends Line{
	private double a1,a2,a3;
	public Triangle(String s, double s1, double s2, double s3, double h){
		super(s, h);
		setName("Triangle");
		a1=s1;
		a2=s2;
		a3=s3;
	}
	public void area(){
		double s = (a1+a2+a3)/2;
		double a = Math.sqrt(p*(s-a1)*(s-a2)*(s-a3));
		System.out.println("Area: "+a);
	}
	public void perimeter(){
		System.out.println("Area: "+(a1+a2+a3));
	}	
}
