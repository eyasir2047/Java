import java.lang.Math;
public abstract class Shape{
	private String Name, color;
	public abstract void perimeter();
	public abstract void area();
	public void name(){
		System.out.println("Name: "+Name);
	}
	public void color(){
		System.out.println("Color: "+color);
	}
	protected void setName(String name){
		this.Name = name;
	}
	protected void setColor(String color){
		this.color = color;
	}
	public void show(){
		name();
		color();
	}
}
