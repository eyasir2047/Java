import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
       // System.out.println("Hello world!");
        Scanner sc= new Scanner(System.in);

        int num= sc.nextInt();

        int factorial=1;

        for(int i=1;i<=num;i++){
            factorial*=i;
        }
        System.out.println(factorial);

    }
}