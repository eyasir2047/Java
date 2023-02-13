package Prob3;

public class E extends C {
    public void m_E(){

        System.out.println("This is class E");
    }
    //@Override
   /* public void m_C(){

        System.out.println("This is the overloaded m_C() method");
    }*/
    //overloading m_C inside E
    public  void m_C(int x){//parameter must be different
        System.out.println("This is the overloaded m_C() method from Class E");
    }
}
