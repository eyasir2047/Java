package Prob3;

public class C extends A {
    public void m_C(){

        System.out.println("This is class C");
    }
    @Override
    public void m_A(){
        System.out.println("This is overloaded m_A() inside C");
    }
}