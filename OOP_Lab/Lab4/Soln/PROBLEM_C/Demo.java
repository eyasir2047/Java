package LabExam2.PROBLEM_C;

public class Demo {
    public static void main(String args[]){
        E e = new E();
        e.m_A();
        e.m_C();
        e.m_E();

        //after overriding m_C()
        e.m_C();

        //after overriding m_A()
        e.m_A(); // The overridden method will be executed
    }
}
