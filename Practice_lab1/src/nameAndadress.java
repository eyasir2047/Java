class Person{
    private String name;
    private String address;
    // constructor
    public Person(String n, String a){
        name= n;
        address =a ;
    }
    public void displayInfo(){
        System.out.print(name+" : ");
        System.out.println(address);
    }

}

public class nameAndadress {
    public static  void main(String []args){
        Person obj1= new Person("Abrar Eyasir","Bander, Narayangonj");
        Person obj2=new Person("Tom Holland","USA");

        obj1.displayInfo();
        obj2.displayInfo();
    }

}
