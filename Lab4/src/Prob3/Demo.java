package Prob3;

public class Demo {
    public static void main(String args[]){
        E e = new E();
        e.m_A();
        e.m_C();
        e.m_E();
        //m_B does not exist



        e.m_C();
        //after overriding m_C()
        e.m_C(5);

        //after overriding m_A()
        e.m_A(); // The overridden method will be executed

        //m_A() ->E extends C and there is a override of m_A()

      /* In this code, m_A() method is overridden in class C and the overridden version of m_A() is invoked inside E class,
      as we can see from the output. The m_C() method is overloaded in class E and both the overloaded methods are executed.

       */


        /*overloading-> happens in only one class, method overloading
        ** parameter list must be different
        ** inheritance is not involved

        */

        /*overriding-> at least two class -> parent and child, method overriding
        ** parameter list must be same
        ** inheritance must be included
        **return type must be same
        ** child method hide the parent's method
         */

    }
}
