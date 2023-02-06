import java.awt.*;

public class Circle {

    Point center;
    double radius;



    //calling a constructor

    Circle(Point initialCenter, double initialRadius){
        center= initialCenter;
        radius =initialRadius;
    }//initialize the value

    Circle(){//constructor overloading
        center=new Point(0,0);
        radius=1;
    }

    double getPerimeter(){
        return 2*Math.PI * radius;
    }

    double getArea(){
        return Math.PI*radius*radius;
    }

    void setRadius(double newRadius){
        radius= newRadius;
    }
    void setCenter(Point newCenter){
        center= newCenter;
    }


}
