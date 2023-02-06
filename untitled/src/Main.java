import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
       //rand that will generate random numbers
        Scanner sc=new Scanner(System.in);
        //input from the user
        int number;
        boolean ans=false;
        //rand -> 0<num<1  number*1000 ranger -> 100 to 1000 and rand only generates double type num, typecase
        // it into integer//.1 *1000= 100
        int ran= (int)(Math.random()*1000);
      // System.out.println(ran);
        do{
            System.out.println("Guess the number");
            number=sc.nextInt();//input the number from the user
            //building up our logic part
            if(number==ran){
                ans=true;
                System.out.println("Congratualions!! You win");
                break;
            }
            else if(number>ran){
                System.out.println("Number is large");
            }
            else {//number<ran
                System.out.println("Number is small");
            }
        }while(number>=0); // if the user gives a negative number, the game will end
            // user don't want to continue the game
        if(ans==false)
        System.out.println("Better luck for next time");

    }
}