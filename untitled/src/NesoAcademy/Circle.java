package NesoAcademy;

import java.awt.*;

public class Circle {

    Point center;
    double radius;

    static int numberOfCircles; //static variable or method belongs to the class

    public Circle(){
        this.center=new Point(0,0);
        this.radius=5.00;
        numberOfCircles++;
    }

    public Circle(Point center,double radius){
        this.center=center;
        this.radius=radius;
        numberOfCircles++;
    }

    public double getArea(){
        return Math.PI* this.radius* this.radius;
    }
    public double getPerimeter(){
        return 2*Math.PI*this.radius;
    }

    void setCenter(Point center){
        this.center=center;

    }

    void setRadius(double radius){
        this.radius =radius;
    }

    static  int getNumberOfCircles(){
        return numberOfCircles;
    }

}
