public class Elipse extends Shape{
	private double vrad, hrad;
	Elipse(String col, double vrad, double hrad){
		setName("Elipse");
		setColor(col);
		this.vrad = vrad;
		this.hrad = hrad;
	}
	
	public void area(){
		System.out.println("Area: "+Math.PI*vrad*hrad);
	}
	
	public void perimeter(){
		System.out.println("Perimeter: "+(Math.PI*(vrad+hrad)));
	}
	public void show(){
		super.show();
		area();
		perimeter();
	}

}
