package Visibility;

public class Test {
    private  int x;

    //getters and setters to access private integer x

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    private double radius;

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = (radius>=0)? radius:0;
    }

    private static  int numberOfCircles;

    public static int getNumberOfCircles() {
        return numberOfCircles;
    }
    // we don't build any setters because I don't want to access this
    // to the user

    //constructors

    public Test(){
        numberOfCircles++;
    }



}
