import java.util.Scanner;

public class FactorialFunction {
    public static  int calculateFactorial(int num){
        int fact=1;
        for(int i=1;i<=num;i++){
            fact*=i;
        }
        return  fact;
    }
    public static void main(String[] args) {

        Scanner sc=new Scanner(System.in);
        int num=sc.nextInt();
        int fact=calculateFactorial(num);
        System.out.println(fact);


    }
}