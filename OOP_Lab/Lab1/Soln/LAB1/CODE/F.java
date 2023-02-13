import java.util.Scanner;
import java.io.*;


public class F 
{
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);

        String str = sc.nextLine();
        char ch;
        String ans="";
        for (int i = 0; i < str.length(); i++) 
        {
            ch = str.charAt(i);
            ans = ch + ans;
        }
        System.out.println("Reversed word: " + ans);
    }
}
