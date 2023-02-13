package LabExam2.PROBLEM_A.P1;

public class D {
    void access_mode(){
        A ob = new A();
//        System.out.println(prv_a); NOT possible
//        System.out.println(ob.prv_a); NOT possible
//        System.out.println(prt_a); NOT possible
        System.out.println(ob.prt_a);
//        System.out.println(d_a); NOT possible
        System.out.println(ob.d_a);
//        System.out.println(pbl_a); NOT possible
        System.out.println(ob.pbl_a);
    }
    public static void main(String [] args){
        D d = new D();
        d.access_mode();
    }
}
