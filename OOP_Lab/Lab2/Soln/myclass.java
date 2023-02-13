public class myclass {
    private int a;
    public void set_a(int x){
        a=x;
    }
    public int get_a(){
        return a;
    }
    public static void main(String [] args){
        myclass X= new myclass();
        myclass Y= new myclass();
        int temp=100;
        int temp2=202;
        int temp3;
        X.set_a(temp);
        Y.set_a(temp2);
        System.out.println(X.get_a() + " " + Y.get_a());
        temp3=X.get_a();
        X.set_a(Y.get_a());
        Y.set_a(temp3);
        System.out.println(X.get_a() + " " + Y.get_a());
    }
}