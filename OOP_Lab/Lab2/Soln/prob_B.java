public class prob_B {
    private String name,address;
    public prob_B(String nm, String ad){
        name=nm;
        address=ad;
    }
    public void show(){
        System.out.println("Name: "+name);
        System.out.println("Address: "+address);
    }

    public static void main(String[] args) {
        prob_B info1= new prob_B("Yeamin","Chittagong");
        prob_B info2= new prob_B("Kaiser","Dhaka");
        info1.show();
        info2.show();
    }
}
