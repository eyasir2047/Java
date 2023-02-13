public class card {
    private String title,author;
    private int number;

    public card(String t, String a, int n){
        title=t;
        author=a;
        number=n;
    }

    public void set(String t, String a, int n){
        title=t;
        author=a;
        number=n;
    }

    public void show(){
        System.out.println("Book Title: "+title);
        System.out.println("Name of Author: "+author);
        System.out.println("Number of Copies: "+number);
    }

    public static void main(String[] args) {
        card c1= new card("Calculus","XYZ",123);
        card c2= new card("Java","ABC",15);
        c1.show();
        c2.show();
        c1=c2;
	System.out.println("After assigning c2 to c1");
	c1.show();
        c2.show();
        c2.set("C++","QWERTY", 50);
        System.out.println("Updates of c2 are also visible through c1 as they refer to same object.");
        c1.show();
        c2.show();
    }


}
