class TwoDPoint{
    public int x;
    public  int y;

    //constructor
    public TwoDPoint(int a,int b){
        x=a;
        y=b;
    }
    //method
    //method means functions
    public void add(TwoDPoint obj1, TwoDPoint obj2, TwoDPoint obj3){
        obj3.x = obj1.x + obj2.x;
        obj3.y= obj1.y + obj2.y;
        System.out.println("Resultant Coordinates : ("+obj3.x+ ","+obj3.y+")");
    }
}
public class twopoints {

    public static void main(String []args){

        TwoDPoint obj1= new TwoDPoint(1,2);
        TwoDPoint obj2= new TwoDPoint(3,4);
        TwoDPoint obj3= new TwoDPoint(0,0);

        obj3.add(obj1,obj2,obj3);

        System.out.println("Resultant Coordinates from Main: ("+obj3.x+","+obj3.y+")");
    }
    //java uses call-by-reference for objects.This means that when objects are passed as parameters,
    //the method receives a reference to the original object, not a copy of the object
    //this can be proven by the output, where the values of obj3 are modified by the add method
    //and the changes are reflected in the main method as well.


}
