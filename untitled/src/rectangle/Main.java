package rectangle;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Rectangle r1= new Rectangle();
        System.out.println(r1.getArea());

        //problem-> create an array of 3 rectangles read from the keyboard
        //and then print  the attributes of each rectangle

        Rectangle[] rectangles=new Rectangle[3];

        Scanner sc=new Scanner(System.in);

        for(int i=0;i<rectangles.length;i++){
            System.out.println(
                    "Do you want to enter the width and height?(Yes/No)"
            );

            char choice=sc.next().charAt(0);
           // String choice=sc.nextLine();
            if(choice=='Y'){
                rectangles[i]=new Rectangle(sc.nextDouble(),sc.nextDouble());
            }
            else if(choice=='N'){
                rectangles[i]=new Rectangle();//default value
            }
            else{
                System.out.println("Invalid");
                rectangles[i]=new Rectangle();//default value
            }
        }

        System.out.println("The rectangles are:");

        for(int i=0;i<rectangles.length;i++){
            System.out.println("Rectangles "+ (i+1)+
        "width: "+rectangles[i].getWidth()+
                    "height: "+rectangles[i].getHeight());
        }





    }
}
