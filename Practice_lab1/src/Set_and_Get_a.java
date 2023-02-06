class MyClass{
    private int a;
     public void setA(int value){
         a= value;
    }

    public int getA(){
         return a;
    }
}

public class Set_and_Get_a {
    public static void main(String []args){
        MyClass obj1=new MyClass();
        MyClass obj2=new MyClass();
        obj1.setA(5);
        obj2.setA(10);
        System.out.println(obj1.getA());
        System.out.println(obj2.getA());

    }
}
