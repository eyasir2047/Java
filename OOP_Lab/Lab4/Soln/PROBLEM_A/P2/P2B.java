package LabExam2.PROBLEM_A.P2;
import LabExam2.PROBLEM_A.P1.*;

public class P2B extends A{
    void access_mode(){
        A ob = new A();
        //System.out.println(prv_a); NOT possible
        //System.out.println(ob.prv_a); NOT possible
        System.out.println(prt_a);
//        System.out.println(ob.prt_a);Not Possible
//        System.out.println(d_a); Not Possible
//        System.out.println(ob.d_a); Not Possible
        System.out.println(pbl_a);
        System.out.println(ob.pbl_a);
    }
    public static void main(String [] args){
        P2B p2b = new P2B();
        p2b.access_mode();
    }
}
