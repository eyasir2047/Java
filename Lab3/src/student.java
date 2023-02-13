public class student {
    private String name;
    private int roll;
    public student(String name) {
        this.name = name;
    }
    public student(String name, int roll) {
        this.name = name;
        this.roll = roll;
    }
    public void changeData(String name, int roll) {
        this.name = name;
        this.roll = roll;
    }
    public void display() {
        System.out.println("Name: " + name + "\nRoll: " + roll);
    }
    public  static student createObj() {
        var now = new student(" Jenny", 10); //var is a data type in new java
        return now;
    }
    public static void main(String[] args) {
        var __obj1 = new student("Only name"); //demonstrate constructor overloading
        var obj1 = new student("Abrar", 12);
        obj1.display(); //print before change
        obj1.changeData("Galib", 2022);
        obj1.display(); //print after change
        // Change should be reflected as objects follow pass-by-reference scheme;
        var obj2 = createObj(); //create object inside another method
        obj2.display();
        obj2.changeData("Tamanna", 2023); //changing data of obj2 created outside main
        obj2.display(); //display obj2
        // Change should be reflected as objects follow pass-by-reference scheme;
    }
}
