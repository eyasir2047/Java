package NesoAcademy;

import java.awt.*;
import java.rmi.ConnectIOException;

public class Main {
    public static void main(String[] args) {

        Circle c1= new Circle();
        System.out.println(c1.radius);
        System.out.println(c1.center);

        Circle c2=new Circle(new Point(1,2),10.00);
        System.out.println(c2.radius);
        System.out.println(c2.center);

        System.out.println(c2.getArea());

        c2.setCenter(new Point(1,3));
        System.out.println(c2.center);

        System.out.println(c2.getNumberOfCircles());

        //Visibility modifiers in java
        //public and private
        //default visibility
        //public ,private and protected

















    }
}
