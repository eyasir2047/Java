package LabExam2.PROBLEM_A.P2;

import LabExam2.PROBLEM_A.P1.A;

public class P2A {
    void access_mode(){
        A ob = new A();
//        System.out.println(prv_a); NOT possible
//        System.out.println(ob.prv_a); NOT possible
//        System.out.println(prt_a);NOT possible
//        System.out.println(ob.prt_a);NOT possible
//        System.out.println(d_a);NOT possible
//        System.out.println(ob.d_a);NOT possible
//        System.out.println(pbl_a);NOT possible
        System.out.println(ob.pbl_a);
    }
    public static void main(String [] args){
        P2A p2a = new P2A();
        p2a.access_mode();
    }
}
