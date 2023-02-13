import java.util.Scanner;

public class C 
{
    public static int max(int a,int b)
    {
        if(a<b)
        {
            return b;
        }
        return a;
    }
    public static int min(int a,int b)
    {
        return a+b-max(a,b);
    }
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int arr[]=new int[n+4];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        int mx=arr[0],mn=arr[0];
        for(int i=0;i<n;i++)
        {
            mx=max(mx,arr[i]);
            mn=min(mn,arr[i]);
        }
        System.out.println("Maximum of the array is : " + mx);
        System.out.println("Minimum of the array is : " + mn);
    }
}
