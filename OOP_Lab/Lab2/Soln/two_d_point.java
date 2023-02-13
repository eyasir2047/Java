public class two_d_point {
    private int x,y;
    public two_d_point(int x1, int y){
        x=x1;
        this.y=y; //you can use this keyword to refer class attribute of the same name
    }
    public void setX(int x){
	this.x = x;
    }
    public void setY(int y){
	this.y = y;
    }
    public int getX(){
	return x;
    }
    public int getY(){
	return y;
    }
    public void add(two_d_point p, two_d_point q, two_d_point r){
	r.setX(p.getX()+q.getX());
	r.setY(p.getY()+q.getY());
        System.out.println("Inside \"add\" method was called:");
        System.out.println(r.getX()+" "+r.getY());
    }

    public static void main(String[] args) {
        two_d_point a=new two_d_point(10, -52);
        two_d_point b=new two_d_point(-15, -24);
        two_d_point c=new two_d_point(33, 9);

        System.out.println("Before \"add\" method was called:");
        System.out.println(c.getX()+" "+c.getY());
        a.add(a, b, c);
        System.out.println("After \"add\" method was called:");
        System.out.println(c.getX()+" "+c.getY());

        /*
            Call-by-refrence is being used here.
            
            This can be proved because we can see that
            the value that was assigned at first was changed 
            to the value obtained inside the "add" method.
            That changed value remained in the assigned variable 
            even after the "add" method. Thus it is clear that the
            call-by-refrence has been used here. 
        */

    }

}
