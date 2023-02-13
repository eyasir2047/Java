package LabExam2.PROBLEM_A.P1;

public class B extends A{
    void access_mode(){
        A ob = new A();
        //System.out.println(prv_a); NOT possible
        //System.out.println(ob.prv_a); NOT possible
        System.out.println(prt_a);
        System.out.println(ob.prt_a);
        System.out.println(d_a);
        System.out.println(ob.d_a);
        System.out.println(pbl_a);
        System.out.println(ob.pbl_a);
    }
    public static void main(String [] args){
        B b = new B();
        b.access_mode();
    }
}
