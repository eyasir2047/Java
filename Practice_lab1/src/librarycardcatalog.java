class Card{

    private String title;
    private String author;
    private int copies;

    //constructor form
    public Card(String t, String a, int c){
        title=t;
        author=a;
        copies=c;
    }

    public void set(String t, String a, int c){
        title=t;
        author=a;
        copies=c;
    }
    public void show(){
        System.out.println("Title: "+ title);
        System.out.println("Author: "+author);
        System.out.println("Copies available: "+copies);
    }

}
public class librarycardcatalog {
    public static  void main(String[]args){
        Card obj1= new Card("7 habits","Grey Stey",100);
        Card obj2= obj1;
        obj1.set("7 habits","Grey Stey",5);
        System.out.println("obj1: ");
        obj1.show();
        System.out.println("obj2 :");
        obj2.show();

    }

}
