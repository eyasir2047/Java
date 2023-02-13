package problem3;

class sceneA{
    private int n;

    public sceneA(int n) {
        this.n = n;
    }

}

class sceneB{
    private int m;

    public sceneB(int m) {
        this.m = m;
    }


}

public class Main {


    public static void main(String[] args) {

        sceneA a=new sceneA(10);
        sceneB b=new sceneB(20);
        sceneA c=new sceneA(10);


     //   System.out.println(a+" "+b+" "+c);

       a=null;
        System.gc();

        //System.out.println(a+" "+b+" "+c);



        sceneB d=new sceneB(200);
        sceneA e=new sceneA(0);

        c=e;
        b=d;

        System.gc();


        //System.out.println(a+" "+b+" "+c);









    }

}
