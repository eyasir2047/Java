import java.util.Scanner;

public class Maximum_minimum {
    public static void main(String[]args){
        Scanner sc= new Scanner(System.in);
        int size=sc.nextInt();
        int []arr=new int [size];

        for(int i=0;i<size;i++){
            arr[i]=sc.nextInt();
        }

        int mini=arr[0];
        int maxi=arr[0];

        for(int i=1;i<size;i++){
            if(arr[i]<mini){
                mini=arr[i];
            }
            if(arr[i]>maxi){
                maxi=arr[i];
            }
        }

        System.out.println(mini+" "+maxi);
    }
}
